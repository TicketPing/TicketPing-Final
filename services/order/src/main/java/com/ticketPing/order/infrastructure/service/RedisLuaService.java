package com.ticketPing.order.infrastructure.service;

import com.ticketPing.order.exception.OrderExceptionCase;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisLuaService {

    private final RedisTemplate<String, String> redisTemplate;
    private final DefaultRedisScript<String> preReserveScript;

    public void updateSeatStatus(String seatKey, String ttlKey, String ttl) {
        String result = Optional.ofNullable(
                redisTemplate.execute(preReserveScript, List.of(seatKey, ttlKey), ttl)
            ).orElseThrow(() -> new ApplicationException(OrderExceptionCase.PRE_RESERVE_ERROR));

        if(!result.equals("SUCCESS")) {
            throw new ApplicationException(OrderExceptionCase.getByValue(result));
        }
    }
}
