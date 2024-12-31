package com.ticketPing.order.infrastructure.repository;

import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends OrderRepository, JpaRepository<Order, UUID> {

}
