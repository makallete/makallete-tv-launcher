package tv.makallete.launcher

import android.app.ActivityManager
import android.content.Context
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL
import kotlin.math.roundToInt

object Weather {
    data class Location(val label: String, val latitude: Double, val longitude: Double)

    fun search(query: String): List<Location>? = try {
        val encoded = java.net.URLEncoder.encode(query, "UTF-8")
        val json = JSONObject(get(URL("https://geocoding-api.open-meteo.com/v1/search?name=$encoded&count=5&language=tr&format=json")))
        val results = json.optJSONArray("results")
        if (results == null) emptyList() else (0 until results.length()).map { index ->
            val item = results.getJSONObject(index)
            Location(listOf(item.getString("name"), item.optString("admin1"), item.optString("country"))
                .filter { it.isNotBlank() }.distinct().joinToString(", "), item.getDouble("latitude"), item.getDouble("longitude"))
        }
    } catch (_: Exception) { null }

    data class Now(val temperature: Int, val code: Int)

    fun fetch(latitude: Double, longitude: Double): Now? = try {
        val url = URL(
            "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude" +
                "&current=temperature_2m,weather_code&timezone=auto"
        )
        val json = JSONObject(get(url))
        val cur = json.getJSONObject("current")
        Now(cur.getDouble("temperature_2m").roundToInt(), cur.getInt("weather_code"))
    } catch (e: Exception) {
        null
    }
}

object SpeedTest {
    data class Result(val mbps: Int, val pingMs: Long)

    private const val BASE = "https://speed.cloudflare.com/__down?bytes="

    /** Latency from the best of three empty requests, then a ~25 MB download capped at 8 seconds. */
    fun run(): Result? = try {
        var ping = Long.MAX_VALUE
        repeat(3) {
            val t0 = System.nanoTime()
            get(URL(BASE + "0"))
            ping = minOf(ping, (System.nanoTime() - t0) / 1_000_000)
        }
        val conn = URL(BASE + "25000000").openConnection() as HttpURLConnection
        conn.connectTimeout = 6_000
        conn.readTimeout = 6_000
        val buf = ByteArray(64 * 1024)
        var total = 0L
        val t0 = System.nanoTime()
        conn.inputStream.use { input ->
            while (true) {
                val n = input.read(buf)
                if (n < 0) break
                total += n
                if (System.nanoTime() - t0 > 8_000_000_000L) break
            }
        }
        conn.disconnect()
        val seconds = (System.nanoTime() - t0) / 1e9
        Result((total * 8 / seconds / 1e6).roundToInt(), ping)
    } catch (e: Exception) {
        null
    }
}

object Memory {
    /** Share of RAM in use, 0..100. */
    fun usedPercent(context: Context): Int {
        val mi = info(context)
        return (((mi.totalMem - mi.availMem) * 100) / mi.totalMem).toInt()
    }

    fun availableBytes(context: Context): Long = info(context).availMem

    /** Closes cached background processes of these apps; foreground and playing apps are left alone. */
    fun free(context: Context, packages: Collection<String>) {
        val am = context.getSystemService(ActivityManager::class.java)
        for (pkg in packages) {
            if (pkg == context.packageName || pkg == AirPlayWatcher.RECEIVER_PACKAGE) continue
            try { am.killBackgroundProcesses(pkg) } catch (_: Exception) {}
        }
    }

    private fun info(context: Context): ActivityManager.MemoryInfo {
        val am = context.getSystemService(ActivityManager::class.java)
        return ActivityManager.MemoryInfo().also { am.getMemoryInfo(it) }
    }
}

object AirPlayStatus {
    /** The receiver listens on port 7000 while it accepts AirPlay connections. */
    fun isListening(): Boolean = try {
        Socket().use { it.connect(InetSocketAddress("127.0.0.1", 7000), 300); true }
    } catch (e: Exception) {
        false
    }
}

private fun get(url: URL): String {
    val conn = url.openConnection() as HttpURLConnection
    conn.connectTimeout = 6_000
    conn.readTimeout = 8_000
    return try {
        conn.inputStream.bufferedReader().use { it.readText() }
    } finally {
        conn.disconnect()
    }
}
