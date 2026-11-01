package tv.makallete.launcher

import android.content.Context
import android.graphics.Rect
import android.widget.HorizontalScrollView

/**
 * Horizontal app row that keeps the focused card at least [edge] px away from both screen edges
 * while cards beyond it simply run off the screen.
 */
class AppsScroller(context: Context, private val edge: Int) : HorizontalScrollView(context) {

    override fun computeScrollDeltaToGetChildRectOnScreen(rect: Rect): Int {
        if (childCount == 0) return 0
        val lo = scrollX + edge
        val hi = scrollX + width - edge
        var delta = 0
        if (rect.right > hi) delta = rect.right - hi
        if (rect.left - delta < lo) delta = rect.left - lo
        val maxScroll = (getChildAt(0).width + paddingLeft + paddingRight - width).coerceAtLeast(0)
        return delta.coerceIn(-scrollX, maxScroll - scrollX)
    }
}
