package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.model.entity.Schedule;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ScheduleResponse (
        UUID id,
        LocalDateTime startTime
){
    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
            .id(schedule.getId())
            .startTime(schedule.getStartTime())
            .build();
    }
}


