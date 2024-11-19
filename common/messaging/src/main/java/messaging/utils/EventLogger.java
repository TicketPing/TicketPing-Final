package messaging.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Slf4j
public class EventLogger {

    public static void logSentMessage(String topic, String message) {
        log.info("Sent message to topic {}: {}", topic, message);
    }

    public static void logReceivedMessage(ConsumerRecord<String, String> record) {
        log.info("Received message from topic {}: {} (offset: {})", record.topic(), record.value(), record.offset());
    }

}