package tv.makallete.launcher

import android.content.Context

/** User values live only in this device's private storage; backups are disabled. */
object LauncherPreferences {
    private fun prefs(context: Context) = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
    fun names(context: Context): String = prefs(context).getString("names", "") ?: ""
    fun city(context: Context): String = prefs(context).getString("city", "") ?: ""
    fun watcherEnabled(context: Context): Boolean = prefs(context).getBoolean("watcher", false)
    fun location(context: Context): Weather.Location? {
        val p = prefs(context)
        val lat = p.getString("latitude", null)?.toDoubleOrNull() ?: return null
        val lon = p.getString("longitude", null)?.toDoubleOrNull() ?: return null
        return Weather.Location(city(context), lat, lon)
    }
    fun save(context: Context, names: String, location: Weather.Location?, watcher: Boolean) {
        prefs(context).edit().putString("names", names).putString("city", location?.label ?: "")
            .putString("latitude", location?.latitude?.toString()).putString("longitude", location?.longitude?.toString())
            .putBoolean("watcher", watcher).apply()
    }
}
