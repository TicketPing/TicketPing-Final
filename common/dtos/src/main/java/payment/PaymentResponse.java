package payment;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID Id,
        UUID userId,
        String status,
        UUID orderId,
        Long amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}