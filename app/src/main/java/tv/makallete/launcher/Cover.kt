package tv.makallete.launcher

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView

/**
 * A full-screen overlay that hides other apps' screens during hand-overs (the AirPlay receiver's own
 * UI before the first video frame and after mirroring stops). Main thread only.
 */
class Cover(private val context: Context) {

    private val wm = context.getSystemService(WindowManager::class.java)
    private val handler = Handler(Looper.getMainLooper())
    private val hideNow = Runnable { hide() }
    private var root: FrameLayout? = null
    private var label: TextView? = null

    val isShowing get() = root != null

    fun show(message: String? = null) {
        handler.removeCallbacks(hideNow)
        val view = root ?: build().also { wm.addView(it, params()) }
        view.animate().cancel()
        view.alpha = 1f
        label?.text = message.orEmpty()
    }

    fun hideAfter(delayMs: Long) {
        if (root == null) return
        handler.removeCallbacks(hideNow)
        handler.postDelayed(hideNow, delayMs)
    }

    fun hide() {
        handler.removeCallbacks(hideNow)
        val view = root ?: return
        view.animate().alpha(0f).setDuration(220).withEndAction {
            if (root === view) {
                wm.removeView(view)
                root = null
                label = null
            }
        }.start()
    }

    private fun build(): FrameLayout {
        val text = TextView(context).apply {
            setTextColor(Color.argb(200, 241, 243, 246))
            textSize = 22f
            letterSpacing = 0.02f
        }
        label = text
        return FrameLayout(context).apply {
            setBackgroundColor(Color.rgb(10, 13, 18))
            addView(text, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            ))
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }.also { root = it }
    }

    private fun params() = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        PixelFormat.TRANSLUCENT
    ).apply {
        windowAnimations = 0
        title = "MakalleteCover"
    }
}
