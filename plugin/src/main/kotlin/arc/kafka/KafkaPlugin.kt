package arc.kafka

import arc.kafka.kafka.KafkaLoader
import arc.kafka.kafka.base.BanKafkaListener
import arc.kafka.service.KafkaConsumerService
import org.bukkit.plugin.java.JavaPlugin

class KafkaPlugin : JavaPlugin() {
    lateinit var kafkaConsumerService: KafkaConsumerService
    lateinit var kafkaLoader: KafkaLoader

    override fun onEnable() {
        saveDefaultConfig()
        kafkaConsumerService = KafkaConsumerService(
            this,
            config.getString("kafka.ip") ?: "localhost:9092",
            config.getString("kafka.group") ?: "minecraft-server"
        )

        kafkaLoader = KafkaLoader(kafkaConsumerService)

        kafkaConsumerService.start()
        kafkaLoader.addListener(BanKafkaListener(this))

        kafkaLoader.registerAllListeners()
        logger.info("Kafka plugin enabled!")
    }

    override fun onDisable() {
        kafkaLoader.unregisterAllListeners()
        kafkaConsumerService.stop()
        logger.info("Kafka plugin disabled!")
    }
}
