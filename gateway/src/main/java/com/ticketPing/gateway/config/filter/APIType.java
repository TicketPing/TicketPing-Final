package com.ticketPing.gateway.config.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@RequiredArgsConstructor
public enum APIType {

    ENTER_WAITING_QUEUE("/api/v1/waiting-queue", HttpMethod.POST),
    GET_QUEUE_INFO("/api/v1/waiting-queue", HttpMethod.GET),
    CREATE_ORDER("/api/v1/orders", HttpMethod.POST),
    REQUEST_PAYMENT("/api/v1/payments", HttpMethod.POST);

    private final String path;
    private final HttpMethod method;

    public static APIType findByRequest(String requestPath, String httpMethod) {
        for (APIType api : APIType.values()) {
            if (api.matches(requestPath, httpMethod)) {
                return api;
            }
        }
        return null;
    }

    private boolean matches(String requestPath, String httpMethod) {
        return requestPath.startsWith(this.path) && this.method.name().equals(httpMethod);
    }

}
