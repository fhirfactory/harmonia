/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.emr.scheduler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.generator.SeedRandom;
import net.fhirfactory.harmonia.paradeigma.emr.config.EmrConfig;
import net.fhirfactory.harmonia.paradeigma.emr.service.EmrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class EmrScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmrScheduler.class);

    private final EmrConfig config;
    private final EmrService emrService;
    private final SeedRandom random;

    private ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduledTask;

    public EmrScheduler(EmrConfig config, EmrService emrService) {
        this.config = config;
        this.emrService = emrService;
        this.random = new SeedRandom(config.getSeed());
    }

    @PostConstruct
    public synchronized void start() {
        if (!config.isTimerEnabled()) {
            log.info("[EMR Scheduler] Autonomous timed order generation is DISABLED");
            return;
        }

        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "emr-timer");
            t.setDaemon(true);
            return t;
        });

        scheduleNext();
        log.info("[EMR Scheduler] Autonomous order generation started in {} mode", config.getTimerMode());
    }

    private synchronized void scheduleNext() {
        if (executor == null || executor.isShutdown() || !config.isTimerEnabled()) {
            return;
        }

        long delayMs = calculateNextDelayMs();
        scheduledTask = executor.schedule(() -> {
            try {
                if (random.nextBoolean(0.6)) {
                    emrService.placeLabOrder(null);
                } else {
                    emrService.placeImagingOrder(null);
                }
            } catch (Exception e) {
                log.error("[EMR Scheduler] Error during scheduled order generation: {}", e.getMessage(), e);
            } finally {
                scheduleNext();
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    private long calculateNextDelayMs() {
        if ("RANDOM_RANGE".equalsIgnoreCase(config.getTimerMode())) {
            long min = config.getMinIntervalMs() > 0 ? config.getMinIntervalMs() : 3000L;
            long max = config.getMaxIntervalMs() > min ? config.getMaxIntervalMs() : min + 7000L;
            return random.nextLong(min, max);
        }
        return config.getFixedIntervalMs() > 0 ? config.getFixedIntervalMs() : 7000L;
    }

    public synchronized void updateConfig(boolean enabled, String mode, long fixedMs, long minMs, long maxMs) {
        config.setTimerEnabled(enabled);
        if (mode != null) config.setTimerMode(mode);
        config.setFixedIntervalMs(fixedMs);
        config.setMinIntervalMs(minMs);
        config.setMaxIntervalMs(maxMs);

        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }

        if (enabled) {
            if (executor == null || executor.isShutdown()) {
                start();
            } else {
                scheduleNext();
            }
        }
    }

    @PreDestroy
    public synchronized void stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}
