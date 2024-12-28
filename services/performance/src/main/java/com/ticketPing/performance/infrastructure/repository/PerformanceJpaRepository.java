package com.ticketPing.performance.infrastructure.repository;

import com.ticketPing.performance.domain.model.entity.Performance;

import java.util.Optional;
import java.util.UUID;

import com.ticketPing.performance.domain.repository.PerformanceRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceJpaRepository extends PerformanceRepository, JpaRepository<Performance, UUID> {
    @Query("SELECT p FROM Performance p " +
            "LEFT JOIN FETCH p.schedules s " +
            "WHERE p.id = :id ")
    Optional<Performance> findByIdWithSchedules(UUID id);

    @Query("SELECT p FROM Performance p " +
            "LEFT JOIN FETCH p.performanceHall ph " +
            "LEFT JOIN FETCH p.seatCosts sc " +
            "WHERE p.id = :id ")
    Optional<Performance> findByIdWithDetails(UUID id);
}
