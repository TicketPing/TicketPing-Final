package messaging.config;

import java.util.HashMap;
import java.util.Map;
import messaging.listener.KafkaProducerListener;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 동시 요청 수 설정
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 3);

        // 멱등성 설정
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        // Ack 설정
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // 재시도 설정
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(KafkaProducerListener kafkaProducerListener) {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        return kafkaTemplate;
    }

}