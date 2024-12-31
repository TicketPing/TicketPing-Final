package com.ticketPing.order.infrastructure.repository;

import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.enums.OrderStatus;
import com.ticketPing.order.domain.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderJpaRepository extends OrderRepository, JpaRepository<Order, UUID> {
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderSeat " +
            "WHERE o.userId = :userId " +
            "AND o.orderStatus != :orderStatus")
    Slice<Order> findUserOrdersExcludingStatus(UUID userId, OrderStatus orderStatus, Pageable pageable);
}
