package arc.kafka.kafka


interface KafkaListener {
    val topic: String

    fun onMessage(message: String)
}