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

package net.fhirfactory.harmonia.paradeigma.pas.scheduler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.generator.SeedRandom;
import net.fhirfactory.harmonia.paradeigma.pas.config.PasConfig;
import net.fhirfactory.harmonia.paradeigma.pas.service.PasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class PasScheduler {

    private static final Logger log = LoggerFactory.getLogger(PasScheduler.class);

    private final PasConfig config;
    private final PasService pasService;
    private final SeedRandom random;

    private ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduledTask;

    public PasScheduler(PasConfig config, PasService pasService) {
        this.config = config;
        this.pasService = pasService;
        this.random = new SeedRandom(config.getSeed());
    }

    @PostConstruct
    public synchronized void start() {
        if (!config.isTimerEnabled()) {
            log.info("[PAS Scheduler] Autonomous timed generation is DISABLED");
            return;
        }

        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "pas-timer");
            t.setDaemon(true);
            return t;
        });

        scheduleNext();
        log.info("[PAS Scheduler] Autonomous generation started in {} mode", config.getTimerMode());
    }

    private synchronized void scheduleNext() {
        if (executor == null || executor.isShutdown() || !config.isTimerEnabled()) {
            return;
        }

        long delayMs = calculateNextDelayMs();
        scheduledTask = executor.schedule(() -> {
            try {
                pasService.triggerNextLifecycleEvent();
            } catch (Exception e) {
                log.error("[PAS Scheduler] Error during scheduled event generation: {}", e.getMessage(), e);
            } finally {
                scheduleNext();
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    private long calculateNextDelayMs() {
        if ("RANDOM_RANGE".equalsIgnoreCase(config.getTimerMode())) {
            long min = config.getMinIntervalMs() > 0 ? config.getMinIntervalMs() : 2000L;
            long max = config.getMaxIntervalMs() > min ? config.getMaxIntervalMs() : min + 5000L;
            return random.nextLong(min, max);
        }
        return config.getFixedIntervalMs() > 0 ? config.getFixedIntervalMs() : 5000L;
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
