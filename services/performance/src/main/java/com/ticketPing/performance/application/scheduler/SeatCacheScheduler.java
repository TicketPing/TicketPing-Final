package com.ticketPing.performance.application.scheduler;

import com.ticketPing.performance.application.service.PerformanceService;
import com.ticketPing.performance.domain.model.entity.Performance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatCacheScheduler {
    private final PerformanceService performanceService;
    private final RedissonClient redissonClient;

    private static final String LOCK_KEY = "SchedulerLock";
    private static final int LOCK_TIMEOUT = 300;

    @Scheduled(cron = "0 0/10 * * * *")
    public void runScheduler() {
        try {
            log.info("[SeatCacheScheduler] 스케줄러 실행 시작");
            RLock lock = redissonClient.getLock(LOCK_KEY);
            boolean acquired = lock.tryLock(10, LOCK_TIMEOUT, TimeUnit.SECONDS);

            if (acquired) {
                Performance performance = performanceService.getUpcomingPerformance();
                if (performance != null) {
                    performanceService.cacheAllSeatsForPerformance(performance.getId());
                    log.info("[SeatCacheScheduler] 캐싱 완료");
                } else {
                    log.info("[SeatCacheScheduler] 예정된 공연이 없음");
                }
            } else {
                log.warn("[SeatCacheScheduler] 다른 서버에서 스케줄러 실행 중");
            }
        } catch (Exception e) {
            log.error("[SeatCacheScheduler] 실행 중 오류 발생: {}", e.getMessage(), e);
            // TODO: discord 알림?
        }
    }
}
