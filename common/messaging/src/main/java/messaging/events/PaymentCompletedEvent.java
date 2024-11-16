package messaging.events;

import java.util.UUID;

public record PaymentCompletedEvent(
        UUID orderId
) {
    public static PaymentCompletedEvent create(UUID orderId) {
        return new PaymentCompletedEvent(
                orderId
        );
    }
}
