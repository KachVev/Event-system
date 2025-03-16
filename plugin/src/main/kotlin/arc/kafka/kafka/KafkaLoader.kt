package arc.kafka.kafka

import arc.kafka.service.KafkaConsumerService

class KafkaLoader(val kafkaConsumerService: KafkaConsumerService) {
    private val listeners = mutableListOf<KafkaListener>()

    fun addListener(listener: KafkaListener) {
        listeners.add(listener)
        kafkaConsumerService.registerListener(listener)
    }

    fun removeListener(listener: KafkaListener) {
        listeners.remove(listener)
        kafkaConsumerService.unregisterListener(listener)
    }

    fun registerAllListeners() {
        listeners.forEach { kafkaConsumerService.registerListener(it) }
    }

    fun unregisterAllListeners() {
        listeners.forEach { kafkaConsumerService.unregisterListener(it) }
        listeners.clear()
    }
}
