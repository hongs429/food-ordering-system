package com.food.ordering.system.service.messaging.publisher.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;


public interface KafkaCallbackInterface {

    default <K, V> ListenableFutureCallback<SendResult<K, V>> getKafkaCallback(
            String topicName,
            K key,
            V message,
            Logger log
    ) {
        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending message {} to topic {}",
                        message.toString(), topicName, ex);
            }

            @Override
            public void onSuccess(SendResult<K, V> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info(
                        "Received successful response from Kafka for order id: {} Topic: {}, Partition: {}, Offset: {}, Timestamp: {}",
                        key.toString(),
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp()
                );
            }
        };
    }
}
