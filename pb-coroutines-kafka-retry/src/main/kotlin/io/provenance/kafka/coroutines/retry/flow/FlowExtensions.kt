package io.provenance.kafka.coroutines.retry.flow

import io.provenance.coroutines.retry.flow.FlowProcessor
import io.provenance.kafka.coroutines.retry.tryOnEach
import io.provenance.kafka.records.UnAckedConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecord

/**
 * Lift the [FlowProcessor] from [UnAckedConsumerRecord] to [ConsumerRecord] type.
 *
 * This is needed to match the flow type of [tryOnEach] when processing with [KafkaFlowRetry].
 */
fun <K, V> FlowProcessor<ConsumerRecord<K, V>>.lifted(): FlowProcessor<UnAckedConsumerRecord<K, V>> {
    return object : FlowProcessor<UnAckedConsumerRecord<K, V>> {
        override suspend fun send(item: UnAckedConsumerRecord<K, V>, e: Throwable) {
            this@lifted.send(item.toConsumerRecord(), e)
        }

        override suspend fun process(item: UnAckedConsumerRecord<K, V>, attempt: Int) {
            this@lifted.process(item.toConsumerRecord())
        }
    }
}

/**
 * Lift the lambda callback from [UnAckedConsumerRecord] to [ConsumerRecord] type.
 *
 * This is needed to match the flow type of [tryOnEach] when processing with [KafkaFlowRetry].
 */
fun <K, V> (suspend (ConsumerRecord<K, V>) -> Unit).lifted(): suspend (UnAckedConsumerRecord<K, V>) -> Unit =
    { this(it.toConsumerRecord()) }
