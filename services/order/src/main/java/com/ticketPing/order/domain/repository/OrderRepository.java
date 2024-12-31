package com.ticketPing.order.domain.repository;

import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.enums.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID orderId);

    Optional<Order> findByIdAndOrderStatus(UUID orderId, OrderStatus orderStatus);

    List<Order> findByUserId(UUID userId);

    boolean existsByOrderSeatSeatIdAndOrderStatusIn(UUID seatId, List<OrderStatus> pending);
}
