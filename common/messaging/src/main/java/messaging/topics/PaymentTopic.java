package messaging.topics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentTopic {

    CREATED("payment-created"),
    COMPLETED("payment-completed");

    private final String topic;

}
