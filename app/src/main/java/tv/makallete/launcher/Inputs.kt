package tv.makallete.launcher

import android.content.Context
import android.content.Intent
import android.media.tv.TvContract
import android.media.tv.TvInputInfo
import android.media.tv.TvInputManager

data class InputEntry(
    val inputId: String,
    val title: String,
    /** Name reported over HDMI-CEC, e.g. "PlayStation 5". */
    val device: String?,
    /** A CEC device is connected and awake on this port. */
    val live: Boolean,
    /** Nothing is plugged in; shown dimmed like the TV's own input list. */
    val idle: Boolean,
    val icon: IconKind,
)

object Inputs {

    /** On this Philips TV the first tuner (HW0) is DTV; HW1 is ATV. */
    private const val DTV_INPUT = "com.mediatek.tvinput/.tuner.TunerInputService/HW0"

    fun load(context: Context): List<InputEntry> {
        val tim = context.getSystemService(TvInputManager::class.java) ?: return emptyList()
        val all = try { tim.tvInputList } catch (e: Exception) { emptyList<TvInputInfo>() }

        val ports = all.filter { it.type == TvInputInfo.TYPE_HDMI && it.parentId == null }
            .sortedWith(compareBy({ it.id.length }, { it.id }))
        val cecDevices = all.filter { it.parentId != null }

        val result = ArrayList<InputEntry>()
        ports.forEachIndexed { i, port ->
            val cec = cecDevices.firstOrNull { it.parentId == port.id }
            val portState = state(tim, port.id)
            val cecState = cec?.let { state(tim, it.id) }
            result += InputEntry(
                inputId = port.id,
                title = "HDMI ${i + 1}",
                device = cec?.loadLabel(context)?.toString()?.takeIf { it.isNotBlank() },
                live = cecState == TvInputManager.INPUT_STATE_CONNECTED,
                idle = cec == null && portState != TvInputManager.INPUT_STATE_CONNECTED,
                icon = IconKind.HDMI,
            )
        }

        val tuner = all.firstOrNull { it.id == DTV_INPUT } ?: all.firstOrNull { it.type == TvInputInfo.TYPE_TUNER }
        if (tuner != null) {
            result += InputEntry(tuner.id, "Anten", null, live = false, idle = false, icon = IconKind.DISH)
        }
        return result
    }

    private fun state(tim: TvInputManager, id: String): Int =
        try { tim.getInputState(id) } catch (e: Exception) { TvInputManager.INPUT_STATE_DISCONNECTED }

    /** The TV app (com.mediatek.wwtv.tvcenter) switches source when it gets this. Tested on this TV. */
    fun intentFor(inputId: String): Intent =
        Intent(Intent.ACTION_VIEW, TvContract.buildChannelUriForPassthroughInput(inputId))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}
