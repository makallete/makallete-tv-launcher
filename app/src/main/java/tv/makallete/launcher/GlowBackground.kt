package tv.makallete.launcher

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.view.View

/**
 * Dark screen with a soft light in the top-right corner in the focused app's color,
 * like Ambilight spilling onto a wall. Two gradients keep the text areas dark.
 */
class GlowBackground(context: Context) : View(context) {

    private val basePaint = Paint().apply { color = Ui.SCREEN }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bottomShade = Paint()
    private val leftShade = Paint()
    private var glow = Ui.NEUTRAL_GLOW
    private var animator: ValueAnimator? = null

    fun setGlow(color: Int) {
        if (color == glow && animator == null) return
        animator?.cancel()
        animator = ValueAnimator.ofObject(ArgbEvaluator(), glow, color).apply {
            duration = 600
            addUpdateListener {
                glow = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val dark = Ui.SCREEN
        bottomShade.shader = LinearGradient(
            0f, 0f, 0f, h.toFloat(),
            intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Ui.withAlpha(dark, 0.85f), Ui.withAlpha(dark, 0.85f)),
            floatArrayOf(0f, 0.40f, 0.75f, 1f),
            Shader.TileMode.CLAMP
        )
        leftShade.shader = LinearGradient(
            0f, 0f, w.toFloat(), 0f,
            intArrayOf(Ui.withAlpha(dark, 0.9f), Color.TRANSPARENT, Color.TRANSPARENT),
            floatArrayOf(0f, 0.60f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        canvas.drawRect(0f, 0f, w, h, basePaint)
        val cx = Ui.pxf(1550f)
        val cy = Ui.pxf(130f)
        val r = Ui.pxf(650f)
        glowPaint.shader = RadialGradient(
            cx, cy, r,
            intArrayOf(Ui.withAlpha(glow, 0.42f), Color.TRANSPARENT),
            null, Shader.TileMode.CLAMP
        )
        canvas.drawCircle(cx, cy, r, glowPaint)
        canvas.drawRect(0f, 0f, w, h, bottomShade)
        canvas.drawRect(0f, 0f, w, h, leftShade)
    }
}
