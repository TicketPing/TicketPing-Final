package com.ticketPing.payment.application.client;

import java.util.UUID;
import order.OrderInfoForPaymentResponse;

public interface OrderClient {
    OrderInfoForPaymentResponse getOrderInfo(UUID orderId, UUID userId);
}
