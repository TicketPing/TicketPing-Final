package com.ticketPing.performance.infrastructure.service;

import com.ticketPing.performance.common.exception.SeatExceptionCase;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LuaScriptService {
    private final RedissonClient redissonClient;
    private final String preReserveScript;

    @Value("${seat.pre-reserve-ttl}")
    private int PRE_RESERVE_TTL;

    public void preReserveSeat(UUID scheduleId, UUID seatId) {
        String hashKey = "seat:{" + scheduleId + "}";
        String ttlKey = "ttl:{" + scheduleId + "}:" + seatId;

        String response = redissonClient.getScript(StringCodec.INSTANCE)
                .evalSha(
                        RScript.Mode.READ_WRITE,
                        preReserveScript,
                        RScript.ReturnType.VALUE,
                        Arrays.asList(hashKey, ttlKey),
                        "\"" + seatId.toString() + "\"", PRE_RESERVE_TTL
                );

        if (!response.equals("SUCCESS")) {
            throw new ApplicationException(SeatExceptionCase.getByValue(response));
        }
    }
}