package tv.makallete.launcher

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/** Sizes for the two pill families: the small top-bar chips and the input chips. */
data class ChipStyle(
    val height: Float,
    val radius: Float,
    val padH: Float,
    val gap: Float,
    val text: Float,
    val icon: Float,
    val dot: Float,
    val border: Float,
    val focusScale: Float,
) {
    companion object {
        val TOP = ChipStyle(32f, 16f, 13f, 6f, 13f, 14f, 6f, 1f, 1.08f)
        val INPUT = ChipStyle(44f, 12f, 18f, 9f, 18f, 22f, 8f, 1f, 1.06f)
    }
}

/**
 * A focusable pill made of parts (icons, texts, a status dot, a small bar). On focus it turns
 * white with dark content and grows a little, like the mockup.
 */
class Chip(context: Context, private val style: ChipStyle) : LinearLayout(context) {

    private val icons = ArrayList<LineIcon>()
    private val texts = ArrayList<Pair<TextView, Boolean>>() // view, muted
    private val bars = ArrayList<MiniBar>()
    private var dot: View? = null
    private var dotOn = false
    var idle = false
        set(value) {
            field = value
            alpha = if (value && !isFocused) 0.45f else 1f
        }

    private val bg = GradientDrawable().apply {
        cornerRadius = Ui.pxf(style.radius)
        setColor(Ui.GLASS)
        setStroke(Ui.px(style.border).coerceAtLeast(1), Ui.LINE)
    }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        isFocusable = true
        isClickable = true
        defaultFocusHighlightEnabled = false
        background = bg
        val pad = Ui.px(style.padH)
        setPadding(pad, 0, pad, 0)
        setOnFocusChangeListener { _, has -> applyFocus(has) }
    }

    private fun spaced(view: View, w: Int, h: Int) {
        val lp = LayoutParams(w, h)
        if (childCount > 0) lp.marginStart = Ui.px(style.gap)
        addView(view, lp)
    }

    fun addIcon(kind: IconKind): Chip {
        val icon = LineIcon(kind, Ui.FG)
        icons += icon
        val iv = ImageView(context).apply { setImageDrawable(icon) }
        spaced(iv, Ui.px(style.icon), Ui.px(style.icon))
        return this
    }

    fun addText(text: String, muted: Boolean = false, weight: Int = 600): TextView {
        val tv = TextView(context).styled(Ui.body(context, weight), style.text, if (muted) Ui.MUTED else Ui.FG)
        tv.text = text
        tv.maxLines = 1
        texts += tv to muted
        spaced(tv, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        return tv
    }

    fun addDot(on: Boolean): Chip {
        val v = View(context)
        dot = v
        spaced(v, Ui.px(style.dot), Ui.px(style.dot))
        setDot(on)
        return this
    }

    fun setDot(on: Boolean) {
        dotOn = on
        dot?.background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(if (on) Ui.ON else Ui.OFF)
        }
    }

    fun addBar(): MiniBar {
        val bar = MiniBar(context)
        bars += bar
        spaced(bar, Ui.px(style.height), Ui.px(4))
        return bar
    }

    private fun applyFocus(has: Boolean) {
        val content = if (has) Ui.DARK_TEXT else Ui.FG
        bg.setColor(if (has) Ui.FG else Ui.GLASS)
        bg.setStroke(Ui.px(style.border).coerceAtLeast(1), if (has) Ui.FG else Ui.LINE)
        icons.forEach { it.color = content }
        texts.forEach { (tv, muted) ->
            tv.setTextColor(
                when {
                    has && muted -> Ui.DARK_MUTED
                    has -> Ui.DARK_TEXT
                    muted -> Ui.MUTED
                    else -> Ui.FG
                }
            )
        }
        bars.forEach { it.setFocused(has) }
        alpha = if (idle && !has) 0.45f else 1f
        val s = if (has) style.focusScale else 1f
        animate().scaleX(s).scaleY(s).setDuration(180).start()
    }
}

/** The tiny memory bar inside the RAM chip. */
class MiniBar(context: Context) : View(context) {
    private val track = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply { color = Ui.LINE }
    private val fillPaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply { color = Ui.FG }
    private var fraction = 0f

    fun setFraction(f: Float) {
        fraction = f.coerceIn(0f, 1f)
        invalidate()
    }

    fun setFocused(has: Boolean) {
        fillPaint.color = if (has) Ui.DARK_TEXT else Ui.FG
        track.color = if (has) Ui.withAlpha(Ui.DARK_TEXT, 0.18f) else Ui.LINE
        invalidate()
    }

    override fun onDraw(canvas: android.graphics.Canvas) {
        val r = height / 2f
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), r, r, track)
        canvas.drawRoundRect(0f, 0f, width * fraction, height.toFloat(), r, r, fillPaint)
    }
}
