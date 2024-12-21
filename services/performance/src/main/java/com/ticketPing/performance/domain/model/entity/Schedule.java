package com.ticketPing.performance.domain.model.entity;

import audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_schedules")
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "schedule_id")
    private UUID id;
    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<Seat> seats;

    public static Schedule createTestData(LocalDateTime startTime, Performance performance) {
        return Schedule.builder()
                .startTime(startTime)
                .performance(performance)
                .seats(new ArrayList<>())
                .build();
    }
}
