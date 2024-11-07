package com.ticketPing.queue_manage.infrastructure.listener;

import static com.ticketPing.queue_manage.domain.model.enums.WorkingQueueTokenDeleteCase.ORDER_COMPLETED;
import static com.ticketPing.queue_manage.domain.utils.TokenValueGenerator.generateTokenValue;

import com.ticketPing.queue_manage.application.service.WorkingQueueService;
import common.utils.EventSerializer;
import events.OrderCompletedEvent;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.retry.Retry;
import topics.OrderTopic;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
    private final WorkingQueueService workingQueueService;

    @EventListener(ApplicationReadyEvent.class)
    public void consumeMessage() {
        reactiveKafkaConsumerTemplate
                .receive()
                .flatMap(this::handleMessage)
                .doOnError(throwable -> log.error("Error occurred while consuming message:", throwable))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))) // 최대 3회 재시도
                .subscribe();
    }

    private Mono<Void> handleMessage(ReceiverRecord<String, String> record) {
        if (record.topic().equals(OrderTopic.COMPLETED.getTopic())) {
            return handleOrderCompletedEvent(record);
        }
        return Mono.empty();
    }

    private Mono<Void> handleOrderCompletedEvent(ReceiverRecord<String, String> record) {
        log.info("Received message: {}, Offset: {}", record.value(), record.offset());
        OrderCompletedEvent event = EventSerializer.deserialize(record.value(), OrderCompletedEvent.class);
        String tokenValue = generateTokenValue(event.userId(), event.performanceId());

        return Mono.fromRunnable(() -> workingQueueService.transferToken(ORDER_COMPLETED, tokenValue))
                .doOnTerminate(() -> record.receiverOffset().acknowledge())
                .then();
    }

}