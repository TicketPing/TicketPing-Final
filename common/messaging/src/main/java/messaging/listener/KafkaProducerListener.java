package messaging.listener;

import messaging.utils.EventLogger;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerListener implements ProducerListener<String, String>  {

    @Override
    public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
        ProducerListener.super.onSuccess(producerRecord, recordMetadata);
        EventLogger.logMessageSendSuccess(producerRecord, recordMetadata);
    }

    @Override
    public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        ProducerListener.super.onError(producerRecord, recordMetadata, exception);
        EventLogger.logMessageSendError(producerRecord, exception);
    }

}