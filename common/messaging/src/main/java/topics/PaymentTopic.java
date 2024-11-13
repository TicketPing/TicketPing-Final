package topics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentTopic {

    COMPLETED("order-completed");

    private final String topic;

}
