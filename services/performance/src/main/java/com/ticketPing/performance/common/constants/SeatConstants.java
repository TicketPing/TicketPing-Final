package com.ticketPing.performance.common.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SeatConstants {

    public static int PRE_RESERVE_TTL;

    private SeatConstants(@Value("${seat.pre-reserve-ttl}") int preReserveTtl) {
        PRE_RESERVE_TTL = preReserveTtl;
    }
}
