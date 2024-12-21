package com.ticketPing.performance.domain;

import com.ticketPing.performance.application.service.SeatService;
import com.ticketPing.performance.domain.model.entity.*;
import com.ticketPing.performance.domain.repository.PerformanceHallRepository;
import com.ticketPing.performance.domain.repository.PerformanceRepository;
import com.ticketPing.performance.domain.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PerformanceRepository performanceRepository;
    private final PerformanceHallRepository performanceHallRepository;
    private final SeatRepository seatRepository;
    private final SeatService seatService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (performanceHallRepository.count() > 0) {
            return;
        }

        // 공연장 더미 데이터 생성
        PerformanceHall hall1 = PerformanceHall.createTestData("국립극장", "서울특별시 남산동 1-1", 50, 10, 5);
        performanceHallRepository.save(hall1);

        PerformanceHall hall2 = PerformanceHall.createTestData("세종문화회관", "서울특별시 세종로 81", 1200, 40, 30);
        performanceHallRepository.save(hall2);

        // 공연 더미 데이터 생성
        Performance performance1 = Performance.createTestData(
                "햄릿",
                "https://fastly.picsum.photos/id/53/250/400.jpg?hmac=XvJLere1krZjwF7Vy_J4hZpWHL9R-Ic6Hup4lQb62Yw",
                120,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10),
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(10),
                19,
                UUID.randomUUID(),
                hall1);
        performance1 = performanceRepository.save(performance1);

        Performance performance2 = Performance.createTestData(
                "라이온 킹",
                "https://fastly.picsum.photos/id/110/250/400.jpg?hmac=zs2-YrgHbuu-kSuG4dw1bynTUoD6VpOEJguE3j9NOtQ",
                120,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(15),
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(15),
                12,
                UUID.randomUUID(),
                hall2);
        performance2 = performanceRepository.save(performance2);

        // 공연 일정 더미 데이터 생성
        Schedule schedule1 = Schedule.createTestData(LocalDateTime.now().plusDays(5), performance1);
        performance1.addSchedule(schedule1);

        Schedule schedule2 = Schedule.createTestData(LocalDateTime.now().plusDays(10), performance2);
        performance2.addSchedule(schedule2);

        // 좌석 가격 더미 데이터 생성
        SeatCost seatCost1 = SeatCost.createTestData("S", 120000, performance1);
        performance1.addSeatCost(seatCost1);

        SeatCost seatCost2 = SeatCost.createTestData("A", 90000, performance1);
        performance1.addSeatCost(seatCost2);

        SeatCost seatCost3 = SeatCost.createTestData("B", 60000, performance1);
        performance1.addSeatCost(seatCost3);

        SeatCost seatCost4 = SeatCost.createTestData("S", 150000, performance2);
        performance2.addSeatCost(seatCost4);

        SeatCost seatCost5 = SeatCost.createTestData("A", 110000, performance2);
        performance2.addSeatCost(seatCost5);

        SeatCost seatCost6 = SeatCost.createTestData("B", 80000, performance2);
        performance2.addSeatCost(seatCost6);

        // 좌석 더미 데이터 생성
        createSeats(schedule1);
        createSeats(schedule2);

        // 스케줄1 좌석 캐싱
        seatService.createSeatsCache(List.of(schedule1), performance1.getId());
    }

    private void createSeats(Schedule schedule) {
        Performance performance = schedule.getPerformance();
        PerformanceHall performanceHall = performance.getPerformanceHall();

        for (int row = 1; row <= performanceHall.getRows(); row++) {
            for (int column = 1; column <= performanceHall.getColumns(); column++) {
                SeatCost seatCost = determineSeatCost(performance, row, performanceHall.getRows());
                Seat seat = Seat.createTestData(row, column, false, seatCost, schedule);
                seatRepository.save(seat);
            }
        }
    }

    // 좌석 등급을 결정하는 로직
    private SeatCost determineSeatCost(Performance performance, int row, int max) {
        if (row <= ((max*2)/10)) {
            return performance.getSeatCosts().get(0);
        } else if (row <= ((max*4)/10)) {
            return performance.getSeatCosts().get(1);
        } else {
            return performance.getSeatCosts().get(2);
        }
    }
}
