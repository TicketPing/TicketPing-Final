package com.ticketPing.performance.domain.model.entity;

import audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_performance_halls")
public class PerformanceHall extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "performance_hall_name")
    private UUID id;
    private String name;
    private String address;
    private Integer seatNumber;
    private Integer rows;
    private Integer columns;

    public static PerformanceHall createTestData(String name, String address, Integer seatNumber,
                                                 Integer rows, Integer columns) {
        return PerformanceHall.builder()
                .name(name)
                .address(address)
                .seatNumber(seatNumber)
                .rows(rows)
                .columns(columns)
                .build();
    }
}


