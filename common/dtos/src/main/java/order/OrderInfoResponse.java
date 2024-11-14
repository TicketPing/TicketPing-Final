package order;

import java.util.UUID;

public record OrderInfoResponse(
        UUID orderId,
        String performanceName,
        UUID performanceScheduleId,
        Long amount,
        UUID seatId
) {
}
