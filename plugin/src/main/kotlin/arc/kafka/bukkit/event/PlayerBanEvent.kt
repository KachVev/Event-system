package arc.kafka.bukkit.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerBanEvent(val playerName: String, val reason: String) : Event() {
    companion object {
        private val handlers = HandlerList()
        @JvmStatic fun getHandlerList(): HandlerList = handlers
    }

    override fun getHandlers(): HandlerList = handlers
}