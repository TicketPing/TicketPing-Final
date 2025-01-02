package com.ticketPing.performance.application.scheduler;

import com.ticketPing.performance.application.service.PerformanceService;
import com.ticketPing.performance.domain.model.entity.Performance;
import com.ticketPing.performance.infrastructure.service.DiscordNotificationService;
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
    private final DiscordNotificationService discordNotificationService;

    private static final String LOCK_KEY = "SchedulerLock";
    private static final int LOCK_TIMEOUT = 300;

    @Scheduled(cron = "0 0/10 * * * *")
    public void runScheduler() {
        try {
            log.info("Scheduler started");
            RLock lock = redissonClient.getLock(LOCK_KEY);
            boolean acquired = lock.tryLock(0, LOCK_TIMEOUT, TimeUnit.SECONDS);

            if (acquired) {
                Performance performance = performanceService.getUpcomingPerformance();
                if (performance != null) {
                    performanceService.cacheAllSeatsForPerformance(performance.getId());
                    log.info("Caching completed");
                } else {
                    log.info("No upcoming performance");
                }
            } else {
                log.warn("Another server is running the scheduler");
            }
        } catch (Exception e) {
            log.error("Error occurred during execution: {}", e.getMessage(), e);
            discordNotificationService.sendErrorNotification(e.getMessage());
        }
    }
}
