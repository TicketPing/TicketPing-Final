package com.ticketPing.order.application.service;

import com.ticketPing.order.application.client.PerformanceClient;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.entity.OrderSeat;
import com.ticketPing.order.domain.model.enums.OrderStatus;
import com.ticketPing.order.infrastructure.repository.OrderRepository;
import messaging.events.OrderCompletedEvent;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import caching.repository.RedisRepository;
import performance.OrderSeatResponse;

import static caching.enums.RedisKeyPrefix.AVAILABLE_SEATS;
import static com.ticketPing.order.common.exception.OrderExceptionCase.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final EventApplicationService eventApplicationService;
    private final RedisRepository redisRepository;
    private final PerformanceClient performanceClient;

    private final static String TTL_PREFIX = "{Seat}:seat_ttl:";

    @Transactional
    public OrderResponse createOrder(UUID scheduleId, UUID seatId, UUID userId) {
        OrderSeatResponse orderData = performanceClient.getOrderInfo(userId, scheduleId, seatId).getBody().getData();
        Order order = saveOrderWithOrderSeat(userId, orderData);
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse validateOrder(UUID orderId, UUID userId) {
        Order order = findOrderById(orderId);
        validateDuplicateOrder(order, userId);

        // TODO 좌석 선점 TTL 갱신

        return OrderResponse.from(order);
    }

    public List<OrderResponse> getUserOrders(UUID userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, UUID paymentId) {
        Order order = findOrderById(orderId);
        order.complete();

        UUID performanceId = order.getPerformanceId();
        UUID scheduleId = order.getScheduleId();
        UUID seatId = order.getOrderSeat().getSeatId();

        performanceClient.updateSeatState(order.getOrderSeat().getSeatId(), true);  // 1. 좌석 db 업데이트 (kafka로 변경?)

        String ttlKey = TTL_PREFIX + scheduleId + ":" + seatId + ":" + orderId;
        redisRepository.deleteKey(ttlKey);

        String counterKey = AVAILABLE_SEATS.getValue() + performanceId;
        redisRepository.decrement(counterKey);

        publishOrderCompletedEvent(order.getUserId(), performanceId);
    }

    private void publishOrderCompletedEvent(UUID userId, UUID performanceId) {
        val event = OrderCompletedEvent.create(userId, performanceId);
        eventApplicationService.publishOrderCompletedEvent(event);
    }

    @Transactional
    private Order saveOrderWithOrderSeat(UUID userId, OrderSeatResponse orderData) {
        Order order = Order.from(userId, orderData);
        Order savedOrder = orderRepository.save(order);

        OrderSeat orderSeat = OrderSeat.from(orderData, savedOrder);
        savedOrder.updateOrderSeat(orderSeat);

        return savedOrder;
    }

    private Order findOrderById(UUID orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));
    }

    private void validateDuplicateOrder(Order order, UUID userId) {
        UUID seatId = order.getOrderSeat().getSeatId();
        UUID scheduleId = order.getScheduleId();

        List<Order> duplicateOrders = orderRepository.findByScheduleIdAndOrderSeatSeatId(seatId, scheduleId)
                .stream()
                .filter(o -> !o.getUserId().equals(userId) &&
                        (o.getOrderStatus().equals(OrderStatus.PENDING) ||
                                o.getOrderStatus().equals(OrderStatus.COMPLETED)))
                .toList();

        if(!duplicateOrders.isEmpty())
            throw new ApplicationException(SEAT_ALREADY_TAKEN);
    }

}
