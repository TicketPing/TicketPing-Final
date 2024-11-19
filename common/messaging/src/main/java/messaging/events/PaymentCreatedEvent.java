package messaging.events;

import java.util.UUID;

public record PaymentCreatedEvent(
        UUID paymentId,
        UUID orderId
) {
    public static PaymentCreatedEvent create(UUID paymentId, UUID orderId) {
        return new PaymentCreatedEvent(
                paymentId,
                orderId
        );
    }
}
