package com.alelad.talkio.commons.kafka

import java.util.*
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serializer

fun <T, R> createIdempotentProducer(
    bootstrapServer: String,
    keySerializer: Serializer<T>,
    valueSerializer: Serializer<R>
): KafkaProducer<T, R> {
    val properties = Properties().also {
        it.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        it.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer::class.qualifiedName)
        it.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer::class.qualifiedName)

        it.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
    }

    return KafkaProducer<T, R>(properties)
}

fun <T, R> createConsumer(
    bootstrapServer: String,
    consumerGroupId: String,
    topics: List<String>,
    keySerializer: Serializer<T>,
    valueSerializer: Serializer<R>,
    enableAutoCommit: Boolean = true
): KafkaConsumer<T ,R> {
    val properties = Properties().also {
        it.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        it.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keySerializer::class.qualifiedName)
        it.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueSerializer::class.qualifiedName)
        it.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId)
        it.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit.toString())
    }

    return KafkaConsumer<T ,R>(properties).also {
        it.subscribe(topics)
    }
}