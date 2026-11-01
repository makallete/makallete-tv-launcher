package tv.makallete.launcher

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.util.Log

/**
 * Smooths the hand-overs of the AirPlay receiver (io.github.jqssun.airplay), which we cannot modify.
 * Its own "launch_on_connect" is turned off (over adb), because Apple devices on the network open
 * and close probe connections every so often; the receiver would pop its screen up for each one.
 * Instead we bring its screen up only when mirroring really starts. We follow its log lines:
 *   "mirror running: 1"         mirroring started   -> cover, bring its screen up, lift the cover
 *   "Video size: ..."           first frames coming -> lift the cover shortly after
 *   "mirror running: 0"         mirroring stopped   -> cover, go home, lift the cover
 *   "Sender disconnected (0)"   last sender left    -> go home if its screen is still up
 */
class AirPlayWatcher : Service() {

    private val main = Handler(Looper.getMainLooper())
    private lateinit var cover: Cover
    @Volatile private var running = false
    @Volatile private var proc: Process? = null
    private var lastHomeAt = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, buildNotification())
        cover = Cover(this)
        running = true
        Thread(::watchLoop, "airplay-watcher").apply { isDaemon = true }.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

    override fun onDestroy() {
        running = false
        proc?.destroy()
        cover.hide()
        super.onDestroy()
    }

    private fun watchLoop() {
        while (running) {
            // logcat replays its buffer first; anything older than this moment is history, not an event.
            val since = System.currentTimeMillis() / 1000.0
            try {
                val p = ProcessBuilder(
                    "logcat", "-v", "epoch", "-s", "$TAG_SERVICE:I", "$TAG_NATIVE:I"
                ).redirectErrorStream(true).start()
                proc = p
                p.inputStream.bufferedReader().useLines { lines ->
                    for (line in lines) {
                        if (!running) break
                        handleLine(line, since)
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "logcat stopped: ${e.message}")
            }
            if (running) Thread.sleep(3_000)
        }
    }

    private fun handleLine(line: String, since: Double) {
        val time = TIMESTAMP.find(line)?.groupValues?.get(1)?.toDoubleOrNull() ?: return
        if (time < since) return
        when {
            "Video size:" in line -> main.post { cover.hideAfter(VIDEO_SETTLE_MS) }
            "mirror running: 1" in line -> main.post(::onMirrorStarted)
            "mirror running: 0" in line -> main.post(::onMirrorStopped)
            else -> {
                val remaining = DISCONNECTED.find(line)?.groupValues?.get(1)?.toIntOrNull() ?: return
                if (remaining == 0) main.post(::onAllDisconnected)
            }
        }
    }

    private fun onMirrorStarted() {
        if (foregroundPackage() == RECEIVER_PACKAGE) return
        cover.show(getString(R.string.cover_starting))
        val launch = packageManager.getLeanbackLaunchIntentForPackage(RECEIVER_PACKAGE)
            ?: packageManager.getLaunchIntentForPackage(RECEIVER_PACKAGE)
        launch?.let { startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
        cover.hideAfter(COVER_FALLBACK_MS)
    }

    private fun onMirrorStopped() {
        val top = foregroundPackage()
        if (top != null && top != RECEIVER_PACKAGE) return
        cover.show()
        main.postDelayed(::goHome, 60)
        cover.hideAfter(HOME_SETTLE_MS)
    }

    private fun onAllDisconnected() {
        if (SystemClock.elapsedRealtime() - lastHomeAt < 3_000) return
        val top = foregroundPackage()
        if (top != null && top != RECEIVER_PACKAGE) return
        cover.show()
        main.postDelayed(::goHome, 60)
        cover.hideAfter(HOME_SETTLE_MS)
    }

    private fun goHome() {
        lastHomeAt = SystemClock.elapsedRealtime()
        Log.i(TAG, "Mirroring ended, returning to home")
        startActivity(
            Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    /** Last resumed app according to usage stats, or null if that access was not granted. */
    private fun foregroundPackage(): String? {
        val usm = getSystemService(UsageStatsManager::class.java) ?: return null
        val now = System.currentTimeMillis()
        val events = usm.queryEvents(now - 6 * 60 * 60 * 1000L, now) ?: return null
        val event = UsageEvents.Event()
        var top: String? = null
        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) top = event.packageName
        }
        return top
    }

    private fun buildNotification(): Notification {
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, getString(R.string.watcher_channel), NotificationManager.IMPORTANCE_MIN)
        )
        return Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .setContentTitle(getString(R.string.watcher_title))
            .setContentText(getString(R.string.watcher_text))
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val TAG = "MakalleteAirPlayWatcher"
        const val RECEIVER_PACKAGE = "io.github.jqssun.airplay"
        private const val TAG_SERVICE = "AirPlayService"
        private const val TAG_NATIVE = "AirPlayNative"
        private const val CHANNEL_ID = "makallete_background"
        private const val NOTIFICATION_ID = 7
        /** Time from "Video size" until the first frame is on screen (decoder retries once on this TV). */
        private const val VIDEO_SETTLE_MS = 700L
        /** Lift the cover even if no video shows up, e.g. an audio-only AirPlay session. */
        private const val COVER_FALLBACK_MS = 5_000L
        /** Time for the home screen to draw behind the cover. */
        private const val HOME_SETTLE_MS = 650L
        private val TIMESTAMP = Regex("""^\s*(\d+\.\d+)""")
        private val DISCONNECTED = Regex("""Sender disconnected \((\d+)\)""")

        fun start(context: Context) {
            if (!LauncherPreferences.watcherEnabled(context)) return
            if (context.checkSelfPermission(android.Manifest.permission.READ_LOGS) != android.content.pm.PackageManager.PERMISSION_GRANTED) return
            if (!android.provider.Settings.canDrawOverlays(context)) return
            val ops = context.getSystemService(android.app.AppOpsManager::class.java)
            if (ops.unsafeCheckOpNoThrow(android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.packageName) != android.app.AppOpsManager.MODE_ALLOWED) return
            try { context.packageManager.getPackageInfo(RECEIVER_PACKAGE, 0) } catch (_: Exception) { return }
            try { context.startForegroundService(Intent(context, AirPlayWatcher::class.java)) }
            catch (e: RuntimeException) { Log.w(TAG, "Watcher could not start", e) }
        }
    }
}
