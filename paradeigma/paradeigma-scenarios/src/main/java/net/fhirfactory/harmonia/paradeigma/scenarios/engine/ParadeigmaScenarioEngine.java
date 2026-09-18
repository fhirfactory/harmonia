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

package net.fhirfactory.harmonia.paradeigma.scenarios.engine;

import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.model.ExecutionProfile;
import net.fhirfactory.harmonia.paradeigma.scenarios.config.ScenarioEngineConfig;
import net.fhirfactory.harmonia.paradeigma.scenarios.journey.PatientJourneyScenario;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.PatientJourneyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ParadeigmaScenarioEngine implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(ParadeigmaScenarioEngine.class);

    private final ScenarioEngineConfig config;
    private final PatientJourneyScenario journeyScenario;

    private final AtomicLong totalJourneys = new AtomicLong(0);
    private final AtomicLong successfulJourneys = new AtomicLong(0);
    private final AtomicLong failedJourneys = new AtomicLong(0);
    private final AtomicBoolean running = new AtomicBoolean(false);

    private ExecutorService threadPool;

    public ParadeigmaScenarioEngine(ScenarioEngineConfig config, PatientJourneyScenario journeyScenario) {
        this.config = config;
        this.journeyScenario = journeyScenario;
        this.threadPool = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "scenario-engine-worker");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Executes a single patient journey synchronously.
     */
    public PatientJourneyResult runSingleJourney() {
        totalJourneys.incrementAndGet();
        PatientJourneyResult result = journeyScenario.executeJourney();
        if (result.isSuccess()) {
            successfulJourneys.incrementAndGet();
        } else {
            failedJourneys.incrementAndGet();
        }
        return result;
    }

    /**
     * Executes a batch of concurrent patient journeys.
     */
    public List<PatientJourneyResult> runConcurrentJourneys(int count, ExecutionProfile profile) {
        if (profile != null) {
            config.setProfile(profile);
        }
        int journeyCount = count > 0 ? count : config.getConcurrentJourneys();
        log.info("[Scenario Engine] Running {} concurrent patient journeys with profile {}", journeyCount, config.getProfile());

        List<Future<PatientJourneyResult>> futures = new ArrayList<>();
        for (int i = 0; i < journeyCount; i++) {
            futures.add(threadPool.submit(this::runSingleJourney));
        }

        List<PatientJourneyResult> results = new ArrayList<>();
        for (Future<PatientJourneyResult> f : futures) {
            try {
                results.add(f.get(60, TimeUnit.SECONDS));
            } catch (Exception e) {
                log.error("[Scenario Engine] Concurrent journey execution timed out or failed: {}", e.getMessage());
            }
        }
        return results;
    }

    /**
     * Runs continuous journeys for a specified duration in minutes.
     */
    public void runContinuousLoad(int durationMinutes, int concurrency) {
        running.set(true);
        long endTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(durationMinutes);
        log.info("[Scenario Engine] Starting continuous LOAD test for {} minutes (concurrency: {})", durationMinutes, concurrency);

        ExecutorService loadPool = Executors.newFixedThreadPool(concurrency);
        for (int c = 0; c < concurrency; c++) {
            loadPool.submit(() -> {
                while (running.get() && System.currentTimeMillis() < endTime) {
                    try {
                        runSingleJourney();
                    } catch (Exception e) {
                        log.warn("[Scenario Engine] Continuous load journey error: {}", e.getMessage());
                    }
                }
            });
        }

        loadPool.shutdown();
        try {
            loadPool.awaitTermination(durationMinutes + 1L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            running.set(false);
            log.info("[Scenario Engine] Finished continuous load run. Total: {}, Successful: {}, Failed: {}",
                    totalJourneys.get(), successfulJourneys.get(), failedJourneys.get());
        }
    }

    public long getTotalJourneys() {
        return totalJourneys.get();
    }

    public long getSuccessfulJourneys() {
        return successfulJourneys.get();
    }

    public long getFailedJourneys() {
        return failedJourneys.get();
    }

    public boolean isRunning() {
        return running.get();
    }

    public void stop() {
        running.set(false);
    }

    @Override
    @PreDestroy
    public void close() {
        stop();
        if (threadPool != null) {
            threadPool.shutdownNow();
        }
    }
}
