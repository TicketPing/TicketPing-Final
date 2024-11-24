package messaging.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class EventLogger {

    public static void logSentMessageOnSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
        log.info("Successfully sent message to topic {}: {}. Offset: {}", producerRecord.topic(), producerRecord.value(), recordMetadata.offset());
    }

    public static void logSentMessageOnError(ProducerRecord<String, String> producerRecord, Exception exception) {
        log.error("Failed to send message to topic {}: {}. Error: {}", producerRecord.topic(), producerRecord.value(), exception);
    }

    public static void logReceivedMessage(ConsumerRecord<String, String> record) {
        log.info("Received message from topic {}: {}. Offset: {}", record.topic(), record.value(), record.offset());
    }

}