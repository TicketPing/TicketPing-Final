package com.ticketPing.order.infrastructure.listener;

import caching.repository.RedisRepository;
import com.ticketPing.order.application.dtos.temp.SeatResponse;
import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.enums.OrderStatus;
import com.ticketPing.order.infrastructure.repository.OrderRepository;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.ticketPing.order.exception.OrderExceptionCase.INVALID_TTL_NAME;
import static com.ticketPing.order.exception.OrderExceptionCase.NOT_FOUND_ORDER_ID_IN_TTL;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisKeyExpiredListener implements MessageListener {
    private final RedisRepository redisRepository;
    private final OrderRepository orderRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString(); // 만료된 키
        log.info("Expired key: {}", expiredKey);

        String[] parts = expiredKey.split(":");
        if (parts.length != 4)
            throw new ApplicationException(INVALID_TTL_NAME);

        String scheduleId = parts[1];
        String seatId = parts[2];
        String orderId = parts[3];

        updateRedisSeatState(scheduleId, seatId);
        updateOrderStatus(orderId);
    }

    private void updateRedisSeatState(String scheduleId, String seatId) {
        String key = "seat:" + scheduleId + ":" + seatId;
        SeatResponse seatResponse = redisRepository.getValueAsClass(key, SeatResponse.class);
        seatResponse.updateSeatState(false);
        redisRepository.setValue(key, seatResponse);
    }

    private void updateOrderStatus(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_ORDER_ID_IN_TTL));

        order.updateOrderStatus(OrderStatus.FAIL);
        orderRepository.save(order);
    }
}
