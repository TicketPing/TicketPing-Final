package messaging.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class EventLogger {

    public static void logMessageSendSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
        log.info("Successfully sent message to topic {}: {}. Offset: {}", producerRecord.topic(), producerRecord.value(), recordMetadata.offset());
    }

    public static void logMessageSendError(ProducerRecord<String, String> producerRecord, Exception exception) {
        log.error("Failed to send message to topic {}: {}. Error: {}", producerRecord.topic(), producerRecord.value(), exception.getMessage());
    }

    public static void logReceivedMessage(ConsumerRecord<String, String> record) {
        log.info("Received message from topic {}: {}. Offset: {}", record.topic(), record.value(), record.offset());
    }

    public static void logMessageConsumeError(ConsumerRecord<String, String> record, Exception exception) {
        log.error("Failed to consume message from topic {}: {}. Offset: {} Error: {}", record.topic(), record.value(), record.offset(), exception.getMessage());
    }

}