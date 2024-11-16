package order;

import java.util.UUID;

public record OrderInfoForPaymentResponse(
        UUID id,
        UUID performanceId,
        UUID performanceScheduleId,
        UUID seatId,
        Long amount
) {
}
