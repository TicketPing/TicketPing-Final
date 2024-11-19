package com.ticketPing.payment.domain.model.entity;

import audit.BaseEntity;
import com.ticketPing.payment.domain.model.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
import order.OrderInfoForPaymentResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    private PaymentStatus status;

    @NotNull
    private UUID orderId;

    @NotNull
    private Long amount;

    public static Payment create(UUID userId, OrderInfoForPaymentResponse orderInfo) {
        return Payment.builder()
                .userId(userId)
                .status(PaymentStatus.PENDING)
                .orderId(orderInfo.id())
                .amount(orderInfo.amount())
                .build();
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
    }

}
