package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.model.entity.Seat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository {
    Seat save(Seat seat);

    Optional<Seat> findById(UUID uuid);

    Optional<Seat> findByIdWithAll(UUID seatId);

    Optional<Seat> findByIdWithSeatCost(UUID id);

    List<Seat> findByScheduleWithSeatCost(Schedule schedule);
}
