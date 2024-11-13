package events;

import java.util.UUID;

public record PaymentCompletedEvent(
        UUID paymentId
) {
    public static PaymentCompletedEvent create(UUID paymentId) {
        return new PaymentCompletedEvent(
                paymentId
        );
    }
}
