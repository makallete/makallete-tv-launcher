package tv.makallete.launcher

import android.app.AlertDialog
import android.widget.EditText
import android.widget.CheckBox
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.tv.TvInputManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Calendar
import kotlin.concurrent.thread

class MainActivity : Activity() {

    private val main = Handler(Looper.getMainLooper())
    private lateinit var root: FrameLayout
    private lateinit var glow: GlowBackground
    private lateinit var clock: TextView
    private lateinit var date: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherTemp: TextView
    private lateinit var greeting: TextView
    private lateinit var heroTitle: TextView
    private lateinit var appCount: TextView
    private lateinit var appsScroller: AppsScroller
    private lateinit var appsRow: LinearLayout
    private lateinit var inputsRow: LinearLayout
    private lateinit var airplayChip: Chip
    private lateinit var speedText: TextView
    private lateinit var ramText: TextView
    private lateinit var ramBar: MiniBar
    private lateinit var toast: TextView

    private var apps: List<AppEntry> = emptyList()
    private var firstAppsLoad = true
    private var weatherFetchedAt = 0L
    private var speedRunning = false
    private var freeing = false

    // ---- lifecycle ---------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Ui.init(this)
        AirPlayWatcher.start(this)
        buildLayout()
        setContentView(root)
        updateClock()
        loadApps()
        loadInputs()
        registerReceiver(timeReceiver, IntentFilter(Intent.ACTION_TIME_TICK).apply {
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        })
        registerReceiver(packageReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        })
        getSystemService(TvInputManager::class.java)?.registerCallback(inputCallback, main)
    }

    override fun onResume() {
        super.onResume()
        updateClock()
        main.post(ticker)
        if (SystemClock.elapsedRealtime() - weatherFetchedAt > 30 * 60 * 1000L || weatherFetchedAt == 0L) loadWeather()
        loadInputs()
    }

    override fun onPause() {
        main.removeCallbacks(ticker)
        super.onPause()
    }

    override fun onDestroy() {
        unregisterReceiver(timeReceiver)
        unregisterReceiver(packageReceiver)
        getSystemService(TvInputManager::class.java)?.unregisterCallback(inputCallback)
        super.onDestroy()
    }

    /** Home pressed while already home: jump back to the first app. */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (hasWindowFocus()) {
            appsScroller.smoothScrollTo(0, 0)
            (appsRow.getChildAt(0))?.requestFocus()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Home screen: Back has nowhere to go.
    }

    private val ticker = object : Runnable {
        override fun run() {
            refreshMemory()
            refreshAirPlay()
            main.postDelayed(this, 5_000)
        }
    }

    private val timeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = updateClock()
    }

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            main.removeCallbacks(reloadApps)
            main.postDelayed(reloadApps, 600)
        }
    }
    private val reloadApps = Runnable { loadApps() }

    private val inputCallback = object : TvInputManager.TvInputCallback() {
        override fun onInputStateChanged(inputId: String, state: Int) = loadInputs()
        override fun onInputAdded(inputId: String) = loadInputs()
        override fun onInputRemoved(inputId: String) = loadInputs()
    }

    // ---- layout -------------------------------------------------------------------------------

    private fun lp(w: Int, h: Int, left: Float = 0f, top: Float = 0f, gravity: Int = Gravity.TOP or Gravity.START) =
        FrameLayout.LayoutParams(w, h, gravity).apply {
            leftMargin = Ui.px(left)
            topMargin = Ui.px(top)
        }

    private fun buildLayout() {
        val wrap = FrameLayout.LayoutParams.WRAP_CONTENT
        val match = FrameLayout.LayoutParams.MATCH_PARENT
        root = FrameLayout(this).apply {
            clipChildren = false
            setBackgroundColor(Ui.SCREEN)
        }
        glow = GlowBackground(this)
        root.addView(glow, FrameLayout.LayoutParams(match, match))

        // Top bar: clock, date, weather on the left; chips on the right.
        val top = FrameLayout(this).apply { clipChildren = false }
        root.addView(top, FrameLayout.LayoutParams(match, Ui.px(80)).apply {
            leftMargin = Ui.px(96); rightMargin = Ui.px(96); topMargin = Ui.px(44)
        })

        val left = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            isBaselineAligned = true
        }
        clock = TextView(this).styled(Ui.display(this, 700), 64f, Ui.FG, -0.02f)
        clock.fontFeatureSettings = "tnum"
        left.addView(clock)
        date = TextView(this).styled(Ui.body(this, 500), 16.8f, Ui.MUTED)
        left.addView(date, LinearLayout.LayoutParams(-2, -2).apply { marginStart = Ui.px(16) })

        val weather = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            isBaselineAligned = true
            background = LeftLine()
            setPadding(Ui.px(16), 0, 0, 0)
        }
        weatherIcon = ImageView(this).apply {
            baselineAlignBottom = true
            translationY = Ui.pxf(3f)
            setImageDrawable(LineIcon(IconKind.CLOUD, Ui.MUTED))
        }
        weather.addView(weatherIcon, LinearLayout.LayoutParams(Ui.px(17), Ui.px(17)))
        weatherTemp = TextView(this).styled(Ui.body(this, 500), 16.8f, Ui.FG)
        weatherTemp.text = "—°"
        weather.addView(weatherTemp, LinearLayout.LayoutParams(-2, -2).apply { marginStart = Ui.px(6) })
        val city = TextView(this).styled(Ui.body(this, 500), 16.8f, Ui.MUTED)
        city.text = " ${LauncherPreferences.city(this).substringBefore(",")}"
        weather.addView(city)
        weather.baselineAlignedChildIndex = 1
        left.addView(weather, LinearLayout.LayoutParams(-2, -2).apply { marginStart = Ui.px(16) })
        top.addView(left, FrameLayout.LayoutParams(wrap, wrap, Gravity.START or Gravity.CENTER_VERTICAL))

        val chips = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            clipChildren = false
        }
        fun addChip(chip: Chip) {
            val p = LinearLayout.LayoutParams(-2, Ui.px(ChipStyle.TOP.height))
            if (chips.childCount > 0) p.marginStart = Ui.px(10)
            chips.addView(chip, p)
        }
        val ambilight = Chip(this, ChipStyle.TOP).addIcon(IconKind.AMBILIGHT)
        ambilight.addText("Ambilight")
        ambilight.setOnClickListener { openAmbilight() }
        if (listOf("org.droidtv.ambilight.AMBILIGHTOSD", "org.droidtv.ambilight.AMBILIGHTSTYLE").any {
            Intent(it).resolveActivity(packageManager) != null
        }) addChip(ambilight)

        airplayChip = Chip(this, ChipStyle.TOP).addIcon(IconKind.AIRPLAY)
        airplayChip.addText("AirPlay")
        airplayChip.addDot(false)
        airplayChip.setOnClickListener { openAirPlay() }
        addChip(airplayChip)

        val speed = Chip(this, ChipStyle.TOP).addIcon(IconKind.SPEED)
        speedText = speed.addText("Hız testi")
        speed.setOnClickListener { runSpeedTest() }
        addChip(speed)

        val ram = Chip(this, ChipStyle.TOP)
        ramText = ram.addText("%—")
        ramText.fontFeatureSettings = "tnum"
        ramBar = ram.addBar()
        ram.addIcon(IconKind.BOLT)
        ram.addText("Temizle")
        ram.setOnClickListener { freeMemory() }
        addChip(ram)
        val preferences = Chip(this, ChipStyle.TOP)
        preferences.addText("Düzenle")
        preferences.setOnClickListener { openPreferences() }
        addChip(preferences)
        top.addView(chips, FrameLayout.LayoutParams(wrap, wrap, Gravity.END or Gravity.CENTER_VERTICAL))

        // Hero: greeting and the focused app's name.
        val hero = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        greeting = TextView(this).styled(Ui.body(this, 300), 19.5f, Ui.SOFT, 0.03f)
        hero.addView(greeting)
        heroTitle = TextView(this).styled(Ui.display(this, 800), 132f, Ui.FG, -0.035f)
        heroTitle.maxLines = 1
        heroTitle.ellipsize = TextUtils.TruncateAt.END
        hero.addView(heroTitle, LinearLayout.LayoutParams(Ui.px(1150), -2).apply { topMargin = Ui.px(14) })
        root.addView(hero, lp(wrap, wrap, 96f, 268f))

        // Apps.
        root.addView(sectionLabel("Uygulamalar", withCount = true), lp(wrap, wrap, 96f, 586f))
        appsRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            clipChildren = false
            clipToPadding = false
        }
        appsScroller = AppsScroller(this, Ui.px(96)).apply {
            isHorizontalScrollBarEnabled = false
            isHorizontalFadingEdgeEnabled = false
            overScrollMode = View.OVER_SCROLL_NEVER
            clipChildren = false
            clipToPadding = false
            isFocusable = false
            setPadding(Ui.px(96), Ui.px(30), Ui.px(96), Ui.px(40))
            addView(appsRow)
        }
        root.addView(appsScroller, lp(match, Ui.px(30 + 164 + 14 + 26 + 40), 0f, 612f))

        // Inputs.
        root.addView(sectionLabel("Girişler", withCount = false), lp(wrap, wrap, 96f, 902f))
        inputsRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            clipChildren = false
        }
        root.addView(inputsRow, lp(wrap, Ui.px(ChipStyle.INPUT.height + 12), 96f, 938f))

        // Toast.
        toast = TextView(this).styled(Ui.body(this, 600), 16.8f, Ui.DARK_TEXT).apply {
            background = GradientDrawable().apply {
                cornerRadius = Ui.pxf(11f)
                setColor(Ui.withAlpha(Ui.FG, 0.96f))
            }
            setPadding(Ui.px(20), Ui.px(11), Ui.px(20), Ui.px(11))
            alpha = 0f
            elevation = Ui.pxf(30f)
        }
        root.addView(toast, lp(wrap, wrap, 0f, 140f, Gravity.TOP or Gravity.CENTER_HORIZONTAL))
    }

    private fun sectionLabel(text: String, withCount: Boolean): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            isBaselineAligned = true
        }
        row.addView(TextView(this).styled(Ui.body(this, 700), 26f, Ui.SOFT).apply { this.text = text })
        if (withCount) {
            appCount = TextView(this).styled(Ui.body(this, 500), 22f, Ui.MUTED)
            row.addView(appCount, LinearLayout.LayoutParams(-2, -2).apply { marginStart = Ui.px(12) })
        }
        return row
    }

    // ---- clock & greeting ---------------------------------------------------------------------

    private val days = arrayOf("Paz", "Pzt", "Sal", "Çar", "Per", "Cum", "Cmt")
    private val months = arrayOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara")

    private fun updateClock() {
        val c = Calendar.getInstance()
        val h = c.get(Calendar.HOUR_OF_DAY)
        clock.text = String.format(java.util.Locale.ROOT, "%02d:%02d", h, c.get(Calendar.MINUTE))
        date.text = "${days[c.get(Calendar.DAY_OF_WEEK) - 1]} ${c.get(Calendar.DAY_OF_MONTH)} ${months[c.get(Calendar.MONTH)]}"
        val hello = when (h) {
            in 5..11 -> "Günaydın"
            in 12..17 -> "İyi günler"
            in 18..22 -> "İyi akşamlar"
            else -> "İyi geceler"
        }
        greeting.text = listOf(hello, LauncherPreferences.names(this)).filter { it.isNotBlank() }.joinToString(" ")
    }

    // ---- apps ---------------------------------------------------------------------------------

    private fun loadApps() {
        thread(name = "load-apps") {
            val loaded = try { Apps.load(this) } catch (e: Exception) { emptyList() }
            main.post { showApps(loaded) }
        }
    }

    private fun showApps(list: List<AppEntry>) {
        val focusedPkg = (currentFocus as? AppCard)?.app?.pkg
        apps = list
        appsRow.removeAllViews()
        appCount.text = list.size.toString()
        list.forEachIndexed { i, app ->
            val card = AppCard(this, app)
            card.onFocused = { showHero(app) }
            card.setOnClickListener { launch(app) }
            card.setOnLongClickListener { openAppInfo(app.pkg); true }
            // At either end of the row there is nowhere to go; without this the row scrolls into its padding.
            val first = i == 0
            val last = i == list.lastIndex
            card.setOnKeyListener { _, keyCode, _ ->
                (last && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) || (first && keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
            }
            val p = LinearLayout.LayoutParams(-2, -2)
            if (i > 0) p.marginStart = Ui.px(26)
            appsRow.addView(card, p)
        }
        val target = list.indexOfFirst { it.pkg == focusedPkg }.takeIf { it >= 0 }
        when {
            target != null -> appsRow.getChildAt(target)?.requestFocus()
            firstAppsLoad || currentFocus == null -> appsRow.getChildAt(0)?.requestFocus()
        }
        if (firstAppsLoad && list.isNotEmpty()) showHero(list[0])
        firstAppsLoad = false
    }

    private fun showHero(app: AppEntry) {
        heroTitle.text = app.label
        glow.setGlow(app.glow)
    }

    private fun launch(app: AppEntry) {
        try {
            startActivity(app.intent)
        } catch (e: Exception) {
            showToast("${app.label} açılamadı")
        }
    }

    private fun openAppInfo(pkg: String) {
        try {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$pkg"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (_: Exception) {}
    }

    // ---- inputs -------------------------------------------------------------------------------

    private fun loadInputs() {
        thread(name = "load-inputs") {
            val list = try { Inputs.load(this) } catch (e: Exception) { emptyList() }
            main.post { showInputs(list) }
        }
    }

    private fun showInputs(list: List<InputEntry>) {
        val focusedId = (currentFocus?.tag as? String)
        inputsRow.removeAllViews()
        list.forEachIndexed { i, input ->
            val chip = Chip(this, ChipStyle.INPUT).addIcon(input.icon)
            chip.tag = input.inputId
            chip.addText(input.title)
            input.device?.let { chip.addText("· $it", muted = true, weight = 500) }
            if (input.live) chip.addDot(true)
            chip.idle = input.idle
            chip.setOnClickListener { switchTo(input) }
            val p = LinearLayout.LayoutParams(-2, Ui.px(ChipStyle.INPUT.height))
            if (i > 0) p.marginStart = Ui.px(12)
            inputsRow.addView(chip, p)
            if (input.inputId == focusedId) chip.requestFocus()
        }
    }

    private fun switchTo(input: InputEntry) {
        try {
            startActivity(Inputs.intentFor(input.inputId))
        } catch (e: Exception) {
            showToast("${input.title} açılamadı")
        }
    }

    // ---- top-bar actions ----------------------------------------------------------------------

    private fun openAmbilight() {
        for (action in listOf("org.droidtv.ambilight.AMBILIGHTOSD", "org.droidtv.ambilight.AMBILIGHTSTYLE")) {
            try {
                startActivity(Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return
            } catch (_: Exception) {}
        }
        showToast("Ambilight ayarları açılamadı")
    }

    private fun openAirPlay() {
        val pm = packageManager
        val intent = pm.getLeanbackLaunchIntentForPackage(AirPlayWatcher.RECEIVER_PACKAGE)
            ?: pm.getLaunchIntentForPackage(AirPlayWatcher.RECEIVER_PACKAGE)
        if (intent == null) {
            showToast("AirPlay alıcısı yüklü değil")
            return
        }
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    private fun refreshAirPlay() {
        thread(name = "airplay-status") {
            val on = AirPlayStatus.isListening()
            main.post { airplayChip.setDot(on) }
        }
    }

    private fun runSpeedTest() {
        if (speedRunning) return
        speedRunning = true
        speedText.text = "Ölçülüyor…"
        thread(name = "speed-test") {
            val r = SpeedTest.run()
            main.post {
                speedRunning = false
                if (r == null) {
                    speedText.text = "Hız testi"
                    showToast("Hız testi yapılamadı, internet bağlantısını kontrol et")
                } else {
                    speedText.text = "↓ ${r.mbps} Mbps"
                    showToast("İndirme ${r.mbps} Mbps · gecikme ${r.pingMs} ms")
                    main.postDelayed({ if (!speedRunning) speedText.text = "Hız testi" }, 2 * 60 * 1000L)
                }
            }
        }
    }

    private fun refreshMemory() {
        val pct = Memory.usedPercent(this)
        ramText.text = "%$pct"
        ramBar.setFraction(pct / 100f)
    }

    private fun freeMemory() {
        if (freeing) return
        freeing = true
        val packages = apps.map { it.pkg }
        thread(name = "free-memory") {
            val before = Memory.availableBytes(this)
            val pctBefore = Memory.usedPercent(this)
            Memory.free(this, packages)
            Thread.sleep(1_500)
            val freedMb = (Memory.availableBytes(this) - before) / (1024 * 1024)
            val pctAfter = Memory.usedPercent(this)
            main.post {
                freeing = false
                refreshMemory()
                showToast(
                    if (freedMb >= 10) "Bellek temizlendi · %$pctBefore → %$pctAfter · $freedMb MB açıldı"
                    else "Arka planda kapatılacak uygulama yok · bellek %$pctAfter dolu"
                )
            }
        }
    }

    // ---- weather ------------------------------------------------------------------------------

    private fun loadWeather() {
        weatherFetchedAt = SystemClock.elapsedRealtime()
        thread(name = "weather") {
            val location = LauncherPreferences.location(this)
            val now = location?.let { Weather.fetch(it.latitude, it.longitude) }
            main.post {
                if (now == null) {
                    weatherFetchedAt = 0L
                    return@post
                }
                weatherTemp.text = "${now.temperature}°"
                weatherIcon.setImageDrawable(LineIcon(LineIcon.forWeatherCode(now.code), Ui.MUTED))
            }
        }
    }

    // ---- toast --------------------------------------------------------------------------------

    private val hideToast = Runnable {
        toast.animate().alpha(0f).translationY(-Ui.pxf(10f)).setDuration(250).start()
    }

    private fun showToast(text: String) {
        toast.text = text
        toast.translationY = -Ui.pxf(10f)
        toast.animate().alpha(1f).translationY(0f).setDuration(250).start()
        main.removeCallbacks(hideToast)
        main.postDelayed(hideToast, 2_600)
    }

    private fun openPreferences() {
        val fields = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 16, 32, 16)
        }
        fun field(label: String, value: String): EditText {
            fields.addView(TextView(this).apply { text = label })
            return EditText(this).apply { setSingleLine(); setText(value); fields.addView(this) }
        }
        val names = field("Selamlamada görünecek ad (isteğe bağlı)", LauncherPreferences.names(this))
        val city = field("Hava durumu şehri (boş bırakırsan kapalı)", LauncherPreferences.city(this))
        fields.addView(TextView(this).apply {
            text = "Şehir araması ve hava durumu Open-Meteo'ya gönderilir. Konum izni kullanılmaz. Hava verisi: Open-Meteo (CC BY 4.0)."
        })
        val watcher = CheckBox(this).apply {
            text = "AirPlay otomatik geçişi (ayrı alıcı + ADB izinleri gerekir)"
            isChecked = LauncherPreferences.watcherEnabled(this@MainActivity)
            fields.addView(this)
        }
        val dialog = AlertDialog.Builder(this).setTitle("Ana ekranı düzenle")
            .setView(fields).setNegativeButton("Vazgeç", null).setPositiveButton("Kaydet", null).create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val query = city.text.toString().trim().take(120)
                val displayName = names.text.toString().trim().take(80)
                fun save(location: Weather.Location?) {
                    LauncherPreferences.save(this, displayName, location, watcher.isChecked)
                    stopService(Intent(this, AirPlayWatcher::class.java))
                    AirPlayWatcher.start(this)
                    dialog.dismiss()
                    recreate()
                }
                if (query.isBlank()) save(null)
                else if (query == LauncherPreferences.city(this)) save(LauncherPreferences.location(this))
                else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    thread(name = "city-search") {
                        val results = Weather.search(query)
                        main.post {
                            if (isFinishing || isDestroyed || !dialog.isShowing) return@post
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                            if (results.isNullOrEmpty()) {
                                showToast("Şehir bulunamadı; bağlantını ve şehir adını kontrol et")
                            } else {
                                AlertDialog.Builder(this).setTitle("Şehrini seç")
                                    .setItems(results.map { it.label }.toTypedArray()) { _, index -> save(results[index]) }
                                    .setNegativeButton("Vazgeç", null).show()
                            }
                        }
                    }
                }
            }
        }
        dialog.show()
    }
}

/** A thin vertical rule on the left edge, separating the weather from the date. */
private class LeftLine : Drawable() {
    private val paint = Paint().apply { color = Ui.LINE }
    override fun draw(canvas: Canvas) {
        val b = bounds
        canvas.drawRect(b.left.toFloat(), b.top.toFloat(), b.left + Ui.pxf(2f), b.bottom.toFloat(), paint)
    }
    override fun setAlpha(alpha: Int) { paint.alpha = alpha }
    override fun setColorFilter(colorFilter: ColorFilter?) { paint.colorFilter = colorFilter }
    @Deprecated("Deprecated in Java")
    override fun getOpacity() = PixelFormat.TRANSLUCENT
}
