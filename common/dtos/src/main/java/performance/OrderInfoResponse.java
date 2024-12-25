package performance;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record OrderInfoResponse(
        UUID seatId,
        Integer row,
        Integer col,
        String seatGrade,
        Integer cost,
        UUID scheduleId,
        LocalDate startDate,
        UUID performanceHallId,
        String performanceHallName,
        UUID performanceId,
        String performanceName,
        Integer performanceGrade,
        UUID companyId
) { }
