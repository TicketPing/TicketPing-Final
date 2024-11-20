package order;

import java.util.UUID;

public record OrderInfoForPaymentResponse(
        UUID id,
        Long amount
) {
}
