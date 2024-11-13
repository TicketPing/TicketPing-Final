package payment;

import java.util.UUID;

public record PaymentResponse(
        UUID Id,
        UUID userId,
        String status,
        UUID orderId,
        String performanceName,
        UUID performanceScheduleId,
        Long amount,
        UUID seatId
) {
}