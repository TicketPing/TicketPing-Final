package com.ticketPing.payment.domain.model.entity;

import audit.BaseEntity;
import com.ticketPing.payment.domain.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
import order.OrderInfoResponse;

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
    private UUID userId;
    private PaymentStatus status;

    private UUID orderId;
    private String performanceName;
    private UUID performanceScheduleId;
    private Long amount;
    private UUID seatId;

    public static Payment create(UUID userId, OrderInfoResponse orderInfo) {
        return Payment.builder()
                .userId(userId)
                .status(PaymentStatus.PENDING)
                .orderId(orderInfo.orderId())
                .performanceName(orderInfo.performanceName())
                .performanceScheduleId(orderInfo.performanceScheduleId())
                .amount(orderInfo.amount())
                .seatId(orderInfo.seatId())
                .build();
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
    }

}
