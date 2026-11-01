package tv.makallete.launcher

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import kotlin.math.roundToInt

/** Design tokens. Every size is in "design px" on a 1920x1080 canvas, scaled to the real screen. */
object Ui {
    private var scale = 1f

    fun init(context: Context) {
        val dm = context.resources.displayMetrics
        scale = maxOf(dm.widthPixels, dm.heightPixels) / 1920f
    }

    fun px(v: Float): Int = (v * scale).roundToInt()
    fun px(v: Int): Int = px(v.toFloat())
    fun pxf(v: Float): Float = v * scale

    val SCREEN = Color.rgb(10, 13, 18)
    val FG = Color.rgb(241, 243, 246)
    val SOFT = Color.rgb(199, 206, 216)
    val MUTED = Color.rgb(143, 153, 168)
    val LINE = Color.argb(33, 241, 243, 246)
    val GLASS = Color.argb(15, 241, 243, 246)
    val ON = Color.rgb(95, 227, 177)
    val OFF = Color.argb(71, 241, 243, 246)
    val DARK_TEXT = Color.rgb(10, 13, 18)
    val DARK_MUTED = Color.rgb(59, 67, 80)
    val NEUTRAL_GLOW = Color.rgb(138, 148, 166)

    private val fonts = HashMap<String, Typeface>()

    /** Bricolage Grotesque, used for the clock and the big app name. */
    fun display(context: Context, weight: Int): Typeface =
        font(context, "fonts/BricolageGrotesque.ttf", "'wght' $weight, 'opsz' 96")

    /** Onest, used for everything else. */
    fun body(context: Context, weight: Int): Typeface =
        font(context, "fonts/Onest.ttf", "'wght' $weight")

    private fun font(context: Context, path: String, axes: String): Typeface =
        fonts.getOrPut("$path|$axes") {
            try {
                Typeface.Builder(context.assets, path).setFontVariationSettings(axes).build()
                    ?: Typeface.DEFAULT
            } catch (e: Exception) {
                Typeface.DEFAULT
            }
        }

    fun withAlpha(color: Int, alpha: Float): Int =
        Color.argb((alpha * 255).roundToInt().coerceIn(0, 255), Color.red(color), Color.green(color), Color.blue(color))
}

fun TextView.styled(typeface: Typeface, sizePx: Float, color: Int, spacing: Float = 0f): TextView {
    this.typeface = typeface
    setTextSize(TypedValue.COMPLEX_UNIT_PX, Ui.pxf(sizePx))
    setTextColor(color)
    letterSpacing = spacing
    includeFontPadding = false
    return this
}
