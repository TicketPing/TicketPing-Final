package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.model.entity.Schedule;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {
    Optional<Schedule> findById(UUID id);
}
