package com.ticketPing.payment.infrastructure.repository;

import com.ticketPing.payment.domain.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findById(UUID paymentId);
}
