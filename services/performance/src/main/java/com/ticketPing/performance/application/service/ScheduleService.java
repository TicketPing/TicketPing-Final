package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.ScheduleResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.common.exception.ScheduleExceptionCase;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.repository.ScheduleRepository;
import com.ticketPing.performance.infrastructure.service.CacheService;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CacheService cacheService;

    public ScheduleResponse getSchedule(UUID id) {
        Schedule schedule = findScheduleById(id);
        return ScheduleResponse.of(schedule);
    }

    public List<SeatResponse> getAllScheduleSeats(UUID scheduleId) {
        return cacheService.getSeatsFromCache(scheduleId);
    }

    private Schedule findScheduleById(UUID id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ScheduleExceptionCase.SCHEDULE_NOT_FOUND));
    }
}
