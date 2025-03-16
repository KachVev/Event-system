package arc.kafka.kafka.base

import arc.kafka.bukkit.event.PlayerBanEvent
import arc.kafka.kafka.KafkaListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class BanKafkaListener(val plugin: JavaPlugin) : KafkaListener {
    override val topic: String = "ban-events"

    override fun onMessage(message: String) {
        val data = message.split(",")
        val playerName = data[0]
        val reason = data[1]

        val event = PlayerBanEvent(playerName, reason)
        Bukkit.getScheduler().runTask(plugin, Runnable {
            Bukkit.getPluginManager().callEvent(event)
        })
    }
}