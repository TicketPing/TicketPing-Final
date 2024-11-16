package events;

import java.util.UUID;

public record OrderCompletedEvent(
        String userId,
        String performanceId
) {
    public static OrderCompletedEvent create(UUID userId, UUID performanceId) {
        return new OrderCompletedEvent(
                userId.toString(),
                performanceId.toString()
        );
    }
}
