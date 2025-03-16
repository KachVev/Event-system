package arc.kafka.service

import arc.kafka.kafka.KafkaListener
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.bukkit.plugin.java.JavaPlugin
import java.time.Duration
import java.util.*
import kotlin.concurrent.thread

class KafkaConsumerService(
    val plugin: JavaPlugin,
    val ip: String = "localhost:9092",
    val group: String = "minecraft-server"
) {
    val consumer: KafkaConsumer<String, String>
    val listeners = mutableMapOf<String, MutableList<KafkaListener>>()
    var running = true

    init {
        val props = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ip)
            put(ConsumerConfig.GROUP_ID_CONFIG, group)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        }
        consumer = KafkaConsumer(props)
    }

    fun registerListener(listener: KafkaListener) {
        listeners.computeIfAbsent(listener.topic) { mutableListOf() }.add(listener)
        consumer.subscribe(listeners.keys)
        plugin.logger.info("Registered Kafka listener for topic: ${listener.topic}")
    }

    fun unregisterListener(listener: KafkaListener) {
        listeners[listener.topic]?.remove(listener)
        if (listeners[listener.topic]?.isEmpty() == true) {
            listeners.remove(listener.topic)
        }
        consumer.subscribe(listeners.keys)
        plugin.logger.info("Unregistered Kafka listener for topic: ${listener.topic}")
    }

    fun start() {
        thread(isDaemon = true) {
            while (running) {
                val records = consumer.poll(Duration.ofMillis(100))
                for (record in records) {
                    listeners[record.topic()]?.forEach { listener ->
                        plugin.server.scheduler.runTask(plugin, Runnable {
                            listener.onMessage(record.value())
                        })
                    }
                }
            }
        }
        plugin.logger.info("KafkaConsumerService started!")
    }

    fun stop() {
        running = false
        consumer.close()
        plugin.logger.info("KafkaConsumerService stopped!")
    }
}