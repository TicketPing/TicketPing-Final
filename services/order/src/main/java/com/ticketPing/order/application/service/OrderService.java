package com.ticketPing.order.application.service;

import com.ticketPing.order.application.dtos.OrderInfoForPaymentResponse;
import com.ticketPing.order.presentation.request.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderInfoResponse;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.entity.OrderSeat;
import com.ticketPing.order.domain.model.enums.OrderStatus;
import com.ticketPing.order.infrastructure.client.PerformanceClient;
import com.ticketPing.order.infrastructure.repository.OrderRepository;
import messaging.events.OrderCompletedEvent;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import caching.repository.RedisRepository;

import static com.ticketPing.order.exception.OrderExceptionCase.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final EventApplicationService eventApplicationService;
    private final RedisRepository redisRepository;
    private final PerformanceClient performanceClient;

    private final static String TTL_PREFIX = "seat_ttl:";

    @Transactional
    public OrderResponse createOrder(OrderCreateDto orderCreateRequestDto, UUID userId) {
        UUID scheduleId = orderCreateRequestDto.scheduleId();
        UUID seatId = orderCreateRequestDto.seatId();

        // 루아스크립트로
        // 좌석 선점 확인(캐시 상태 확인), 좌석 선점(캐시 변경, ttl 생성)
        // 좌석 캐시 -> "seat:" + scheduleId + ":" + seatId
        // 좌석 ttl -> TTL_PREFIX + scheduleId + ":" + seatId + ":" + orderId

        return OrderResponse.from(saveOrderWithOrderSeat(seatId, userId));
    }

    @Transactional
    public Order saveOrderWithOrderSeat(UUID seatId, UUID userId) {
        OrderInfoResponse orderData = performanceClient.getOrderInfo(seatId.toString()).getBody().getData();

        Order order = Order.create(userId, orderData.companyId(), orderData.performanceId(), orderData.performanceName(), LocalDateTime.now(), OrderStatus.PENDING, orderData.scheduleId());
        Order savedOrder = orderRepository.save(order);

        OrderSeat orderSeat = OrderSeat.create(orderData.seatId(), orderData.row(), orderData.col(), orderData.seatRate(), orderData.cost());
        savedOrder.updateOrderSeat(orderSeat);

        return order;
    }

    @Transactional(readOnly = true)
    public OrderInfoForPaymentResponse getOrderInfoForPayment(UUID orderId, UUID userId) {
        Order order = findOrderById(orderId);
        validateDuplicateOrder(order, userId);
        return OrderInfoForPaymentResponse.from(order);
    }

    public void validateDuplicateOrder(Order order, UUID userId) {
        UUID seatId = order.getOrderSeat().getSeatId();
        UUID scheduleId = order.getScheduleId();

        List<Order> duplicateOrders = orderRepository.findByScheduleIdAndOrderSeatSeatId(seatId, scheduleId)
                .stream()
                .filter(o -> !o.getUserId().equals(userId) &&
                        (o.getOrderStatus().equals(OrderStatus.PENDING) ||
                        o.getOrderStatus().equals(OrderStatus.COMPLETED)))
                .toList();

        if(!duplicateOrders.isEmpty())
            throw new ApplicationException(ORDER_ALREADY_OCCUPIED);
    }

    public List<OrderResponse> getUserOrders(UUID userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public void updateOrderStatus(UUID orderId) {
        Order order = findOrderById(orderId);
        order.complete();

        UUID performanceId = order.getPerformanceId();
        UUID scheduleId = order.getScheduleId();
        UUID seatId = order.getOrderSeat().getSeatId();

        performanceClient.updateSeatState(order.getOrderSeat().getSeatId(), true);  // 1. 좌석 db 업데이트 (kafka로 변경?)

        String ttlKey = TTL_PREFIX + scheduleId + ":" + seatId + ":" + orderId;
        redisRepository.deleteKey(ttlKey);

        String counterKey = "AvailableSeats:" + performanceId;
        redisRepository.decrement(counterKey);

        publishOrderCompletedEvent(orderId, performanceId);
    }

    @Transactional(readOnly = true)
    public Order findOrderById(UUID orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));
    }

    private void publishOrderCompletedEvent(UUID userId, UUID performanceId) {
        val event = OrderCompletedEvent.create(userId, performanceId);
        eventApplicationService.publishOrderCompletedEvent(event);
    }

}
