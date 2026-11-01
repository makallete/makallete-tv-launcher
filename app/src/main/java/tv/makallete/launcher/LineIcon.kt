package tv.makallete.launcher

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable

enum class IconKind { AMBILIGHT, AIRPLAY, SPEED, BOLT, HDMI, DISH, GLOBE, SUN, CLOUD, RAIN, SNOW, STORM }

/** Line icons drawn on a 32x32 grid, matching the mockup's stroke style. */
class LineIcon(kind: IconKind, color: Int, private val strokeUnits: Float = 2.6f) : Drawable() {

    private val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        this.color = color
    }
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        this.color = color
    }
    private val path = Path()
    private val dots = ArrayList<FloatArray>()

    var color: Int
        get() = stroke.color
        set(value) {
            stroke.color = value
            fill.color = value
            invalidateSelf()
        }

    init {
        build(kind)
    }

    private fun line(x1: Float, y1: Float, x2: Float, y2: Float) {
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
    }

    private fun cloud(dy: Float) {
        val c = Path().apply {
            addCircle(23f, 18f + dy, 6f, Path.Direction.CW)
            op(Path().apply { addCircle(15.5f, 16f + dy, 7.5f, Path.Direction.CW) }, Path.Op.UNION)
            op(Path().apply { addCircle(9f, 19f + dy, 5f, Path.Direction.CW) }, Path.Op.UNION)
            op(Path().apply { addRect(9f, 17f + dy, 23f, 24f + dy, Path.Direction.CW) }, Path.Op.UNION)
        }
        path.addPath(c)
    }

    private fun build(kind: IconKind) {
        when (kind) {
            IconKind.AMBILIGHT -> {
                path.addRoundRect(RectF(9f, 11f, 23f, 21f), 2f, 2f, Path.Direction.CW)
                line(4f, 9f, 4f, 23f); line(28f, 9f, 28f, 23f)
                line(9f, 5f, 23f, 5f); line(9f, 27f, 23f, 27f)
            }
            IconKind.AIRPLAY -> {
                path.moveTo(8f, 22f); path.lineTo(6f, 22f); path.quadTo(4f, 22f, 4f, 20f)
                path.lineTo(4f, 7f); path.quadTo(4f, 5f, 6f, 5f); path.lineTo(26f, 5f)
                path.quadTo(28f, 5f, 28f, 7f); path.lineTo(28f, 20f); path.quadTo(28f, 22f, 26f, 22f)
                path.lineTo(24f, 22f)
                path.moveTo(16f, 19f); path.lineTo(23f, 27f); path.lineTo(9f, 27f); path.close()
            }
            IconKind.SPEED -> {
                path.arcTo(RectF(5f, 11f, 27f, 33f), 180f, 180f, true)
                line(16f, 22f, 22f, 15f)
            }
            IconKind.BOLT -> {
                path.moveTo(18f, 3f); path.lineTo(7f, 18f); path.lineTo(15f, 18f)
                path.lineTo(13f, 29f); path.lineTo(25f, 13f); path.lineTo(17f, 13f); path.close()
            }
            IconKind.HDMI -> {
                path.moveTo(5f, 11f); path.lineTo(27f, 11f); path.lineTo(27f, 17f)
                path.lineTo(23f, 21f); path.lineTo(9f, 21f); path.lineTo(5f, 17f); path.close()
            }
            IconKind.DISH -> {
                path.moveTo(5f, 10f); path.cubicTo(5f, 20f, 12f, 27f, 22f, 27f); path.close()
                line(13.5f, 18.5f, 20.5f, 11.5f)
                path.addCircle(22f, 10f, 1.8f, Path.Direction.CW)
                path.moveTo(24.5f, 4.5f); path.cubicTo(26f, 5f, 27f, 6f, 27.5f, 7.5f)
                line(10f, 23.5f, 8f, 29f); line(4.5f, 29f, 12.5f, 29f)
            }
            IconKind.GLOBE -> {
                path.addCircle(16f, 16f, 12f, Path.Direction.CW)
                path.addOval(RectF(10.5f, 4f, 21.5f, 28f), Path.Direction.CW)
                line(16f, 4f, 16f, 28f)
                line(4f, 16f, 28f, 16f)
                line(5.6f, 10f, 26.4f, 10f)
                line(5.6f, 22f, 26.4f, 22f)
            }
            IconKind.SUN -> {
                path.addCircle(16f, 16f, 5f, Path.Direction.CW)
                for (i in 0 until 8) {
                    val a = Math.toRadians(i * 45.0)
                    line(
                        16f + 8.5f * Math.cos(a).toFloat(), 16f + 8.5f * Math.sin(a).toFloat(),
                        16f + 11.5f * Math.cos(a).toFloat(), 16f + 11.5f * Math.sin(a).toFloat()
                    )
                }
            }
            IconKind.CLOUD -> cloud(0f)
            IconKind.RAIN -> {
                cloud(-4f)
                line(11f, 24f, 9.5f, 28f); line(16f, 24f, 14.5f, 28f); line(21f, 24f, 19.5f, 28f)
            }
            IconKind.SNOW -> {
                cloud(-4f)
                dots += floatArrayOf(10f, 26f, 1.4f)
                dots += floatArrayOf(16f, 28f, 1.4f)
                dots += floatArrayOf(22f, 26f, 1.4f)
            }
            IconKind.STORM -> {
                cloud(-4f)
                path.moveTo(17f, 21f); path.lineTo(14f, 26f); path.lineTo(18f, 26f); path.lineTo(15f, 30f)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        val b = bounds
        if (b.isEmpty) return
        val s = b.width() / 32f
        canvas.save()
        canvas.translate(b.left.toFloat(), b.top.toFloat())
        canvas.scale(s, s)
        stroke.strokeWidth = strokeUnits
        canvas.drawPath(path, stroke)
        for (d in dots) canvas.drawCircle(d[0], d[1], d[2], fill)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        stroke.alpha = alpha
        fill.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        stroke.colorFilter = colorFilter
        fill.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity() = PixelFormat.TRANSLUCENT

    companion object {
        /** Open-Meteo WMO weather codes to an icon. */
        fun forWeatherCode(code: Int): IconKind = when (code) {
            0, 1 -> IconKind.SUN
            in 51..67, in 80..82 -> IconKind.RAIN
            in 71..77, 85, 86 -> IconKind.SNOW
            in 95..99 -> IconKind.STORM
            else -> IconKind.CLOUD
        }
    }
}
