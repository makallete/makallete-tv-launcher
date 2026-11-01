package tv.makallete.launcher

import android.content.Context
import android.graphics.Outline
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/** One app: its banner in a rounded card, with the name underneath, outside the card. */
class AppCard(context: Context, val app: AppEntry) : LinearLayout(context) {

    private val card = FrameLayout(context)
    private val label: TextView
    private val ring = GradientDrawable().apply {
        cornerRadius = Ui.pxf(CARD_RADIUS)
        setStroke(Ui.px(4), Ui.FG)
    }

    init {
        orientation = VERTICAL
        isFocusable = true
        isClickable = true
        isLongClickable = true
        defaultFocusHighlightEnabled = false
        clipChildren = false
        clipToPadding = false

        card.setBackgroundColor(app.background)
        card.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, Ui.pxf(CARD_RADIUS))
            }
        }
        card.clipToOutline = true
        card.outlineSpotShadowColor = app.glow
        card.outlineAmbientShadowColor = app.glow
        card.addView(ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageBitmap(app.banner)
        }, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        addView(card, LayoutParams(Ui.px(CARD_W), Ui.px(CARD_H)))

        label = TextView(context).styled(Ui.body(context, 500), 20f, Ui.MUTED)
        label.text = app.label
        label.maxLines = 1
        label.ellipsize = android.text.TextUtils.TruncateAt.END
        label.setPadding(Ui.px(4), 0, 0, 0)
        addView(label, LayoutParams(Ui.px(CARD_W), LayoutParams.WRAP_CONTENT).apply { topMargin = Ui.px(14) })

        setOnFocusChangeListener { _, has -> applyFocus(has) }
    }

    var onFocused: (() -> Unit)? = null

    private fun applyFocus(has: Boolean) {
        if (has) onFocused?.invoke()
        card.foreground = if (has) ring else null
        card.elevation = if (has) Ui.pxf(26f) else 0f
        label.setTextColor(if (has) Ui.FG else Ui.MUTED)
        val s = if (has) 1.08f else 1f
        card.animate().scaleX(s).scaleY(s).setDuration(200).start()
    }

    companion object {
        const val CARD_W = 290f
        const val CARD_H = 164f
        const val CARD_RADIUS = 20f
    }
}
