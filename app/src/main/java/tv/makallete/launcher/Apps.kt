package tv.makallete.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import kotlin.math.max

data class AppEntry(
    val pkg: String,
    val label: String,
    val intent: Intent,
    val banner: Bitmap,
    /** Card color behind the banner, visible only where the banner is transparent or letterboxed. */
    val background: Int,
    /** The app's dominant color, used for the screen light and the focus glow. */
    val glow: Int,
)

/**
 * Reads every TV app straight from the system, so anything installed later (e.g. from Play Store)
 * shows up with its own official TV banner. Apps without a banner get a card built from their icon.
 */
object Apps {

    private val HIDDEN = setOf(
        BuildConfigPkg.SELF,
        AirPlayWatcher.RECEIVER_PACKAGE,        // has its own button in the top bar
        "com.mediatek.wwtv.tvcenter",            // live TV, reached through the Anten button
        "me.efesser.flauncher",
        "com.google.android.tvlauncher",
        "org.droidtv.eum",                       // Philips help / e-manual
    )

    /** Preferred order; anything else goes after these, alphabetically, before the tail. */
    private val HEAD = listOf(
        "com.netflix.ninja",
        "com.google.android.youtube.tv",
        "com.beonetv.tod",
        "com.dsmart.blu.android",
        "com.exxen.android",
        "tv.twitch.android.app",
        "org.videolan.vlc",
        "com.phlox.tvwebbrowser",
        "com.mediatek.wwtv.mediaplayer",
    )
    private val TAIL = listOf("com.android.vending", "com.android.tv.settings")

    private val LABELS = mapOf(
        "com.mediatek.wwtv.mediaplayer" to "Medya Oynatıcı",
        "com.android.tv.settings" to "Ayarlar",
        "com.phlox.tvwebbrowser" to "Tarayıcı",
        "com.android.vending" to "Play Store",
    )

    fun load(context: Context): List<AppEntry> {
        val pm = context.packageManager
        val found = pm.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER), 0
        )
        val byPkg = LinkedHashMap<String, ResolveInfo>()
        for (ri in found) {
            val pkg = ri.activityInfo.packageName
            if (pkg in HIDDEN || byPkg.containsKey(pkg)) continue
            byPkg[pkg] = ri
        }
        val w = Ui.px(AppCard.CARD_W)
        val h = Ui.px(AppCard.CARD_H)
        val entries = byPkg.values.mapNotNull { ri ->
            try {
                entryFor(context, ri, w, h)
            } catch (e: Exception) {
                null
            }
        }
        return entries.sortedWith(compareBy<AppEntry>({ rank(it.pkg) }, { it.label.lowercase() }))
    }

    private fun rank(pkg: String): Int {
        val head = HEAD.indexOf(pkg)
        if (head >= 0) return head
        val tail = TAIL.indexOf(pkg)
        if (tail >= 0) return 10_000 + tail
        return 1_000
    }

    private fun entryFor(context: Context, ri: ResolveInfo, w: Int, h: Int): AppEntry {
        val pm = context.packageManager
        val ai = ri.activityInfo
        val pkg = ai.packageName
        val label = LABELS[pkg] ?: ri.loadLabel(pm).toString()
        val intent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
            .setComponent(ComponentName(pkg, ai.name))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)

        if (pkg == BROWSER_PACKAGE) {
            // TV Bro's own banner doesn't fit the home screen; it gets a plain browser card instead.
            return AppEntry(pkg, label, intent, browserCard(w, h), BROWSER_DARK, BROWSER_LIGHT)
        }

        val bannerDrawable = ai.loadBanner(pm)
        return if (bannerDrawable != null) {
            val raw = render(bannerDrawable, w, h)
            val transparent = hasTransparency(raw)
            val glow = Palette.dominant(raw) ?: Ui.NEUTRAL_GLOW
            val bg = if (transparent) TRANSPARENT_BANNER_BG else Palette.corner(raw)
            AppEntry(pkg, label, intent, if (transparent) onColor(raw, bg) else raw, bg, glow)
        } else {
            val icon = ai.loadIcon(pm)
            val iconBmp = render(icon, Ui.px(96), Ui.px(96))
            val glow = Palette.dominant(iconBmp) ?: Ui.NEUTRAL_GLOW
            val bg = Palette.darken(glow, 0.32f)
            AppEntry(pkg, label, intent, iconCard(iconBmp, w, h, bg), bg, glow)
        }
    }

    private val TRANSPARENT_BANNER_BG = Color.rgb(20, 26, 36)

    private const val BROWSER_PACKAGE = "com.phlox.tvwebbrowser"
    private val BROWSER_DARK = Color.rgb(11, 47, 112)
    private val BROWSER_LIGHT = Color.rgb(46, 140, 230)

    /** Blue card with a white globe, drawn in the same line style as the launcher's icons. */
    private fun browserCard(w: Int, h: Int): Bitmap {
        val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(out)
        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
        paint.shader = android.graphics.LinearGradient(
            0f, h.toFloat(), w.toFloat(), 0f, BROWSER_DARK, BROWSER_LIGHT, android.graphics.Shader.TileMode.CLAMP
        )
        c.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)
        paint.shader = android.graphics.RadialGradient(
            w * 0.3f, h * 0.15f, w * 0.7f,
            Color.argb(70, 255, 255, 255), Color.TRANSPARENT, android.graphics.Shader.TileMode.CLAMP
        )
        c.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)
        val size = (h * 0.58f).toInt()
        val globe = LineIcon(IconKind.GLOBE, Color.WHITE, strokeUnits = 1.7f)
        globe.setBounds((w - size) / 2, (h - size) / 2, (w + size) / 2, (h + size) / 2)
        globe.draw(c)
        return out
    }

    /** Draws a drawable filling w x h like CSS object-fit: cover. */
    private fun render(d: Drawable, w: Int, h: Int): Bitmap {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        val iw = d.intrinsicWidth
        val ih = d.intrinsicHeight
        if (iw <= 0 || ih <= 0) {
            d.setBounds(0, 0, w, h)
        } else {
            val s = max(w.toFloat() / iw, h.toFloat() / ih)
            val dw = (iw * s).toInt()
            val dh = (ih * s).toInt()
            val l = (w - dw) / 2
            val t = (h - dh) / 2
            d.setBounds(l, t, l + dw, t + dh)
        }
        d.draw(c)
        return bmp
    }

    private fun hasTransparency(b: Bitmap): Boolean {
        val stepX = max(1, b.width / 24)
        val stepY = max(1, b.height / 14)
        var y = 0
        while (y < b.height) {
            var x = 0
            while (x < b.width) {
                if (Color.alpha(b.getPixel(x, y)) < 250) return true
                x += stepX
            }
            y += stepY
        }
        return false
    }

    private fun onColor(src: Bitmap, bg: Int): Bitmap {
        val out = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(out)
        c.drawColor(bg)
        c.drawBitmap(src, 0f, 0f, null)
        src.recycle()
        return out
    }

    private fun iconCard(icon: Bitmap, w: Int, h: Int, bg: Int): Bitmap {
        val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(out)
        c.drawColor(bg)
        c.drawBitmap(icon, (w - icon.width) / 2f, (h - icon.height) / 2f, null)
        icon.recycle()
        return out
    }
}

/** Small color helpers for banners. */
object Palette {

    /** The most common vivid hue in the image, averaged; null for grey-only images. */
    fun dominant(b: Bitmap): Int? {
        val buckets = 18
        val weight = FloatArray(buckets)
        val r = FloatArray(buckets)
        val g = FloatArray(buckets)
        val bl = FloatArray(buckets)
        val hsv = FloatArray(3)
        val step = max(1, minOf(b.width, b.height) / 40)
        var y = 0
        while (y < b.height) {
            var x = 0
            while (x < b.width) {
                val p = b.getPixel(x, y)
                if (Color.alpha(p) > 200) {
                    Color.colorToHSV(p, hsv)
                    if (hsv[1] > 0.35f && hsv[2] > 0.35f) {
                        val i = ((hsv[0] / 360f) * buckets).toInt().coerceIn(0, buckets - 1)
                        val wgt = hsv[1] * hsv[2]
                        weight[i] += wgt
                        r[i] += Color.red(p) * wgt
                        g[i] += Color.green(p) * wgt
                        bl[i] += Color.blue(p) * wgt
                    }
                }
                x += step
            }
            y += step
        }
        var best = -1
        for (i in 0 until buckets) if (weight[i] > 0 && (best < 0 || weight[i] > weight[best])) best = i
        if (best < 0) return null
        val wgt = weight[best]
        return Color.rgb((r[best] / wgt).toInt(), (g[best] / wgt).toInt(), (bl[best] / wgt).toInt())
    }

    fun corner(b: Bitmap): Int = b.getPixel(1, 1) or 0xFF000000.toInt()

    fun darken(color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = hsv[2] * factor
        return Color.HSVToColor(hsv)
    }
}

object BuildConfigPkg {
    const val SELF = "tv.makallete.launcher"
}
