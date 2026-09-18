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

package net.fhirfactory.harmonia.paradeigma.lms.service;

import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7Parsers;
import net.fhirfactory.harmonia.paradeigma.lms.config.LmsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class LmsResultWorker {

    private static final Logger log = LoggerFactory.getLogger(LmsResultWorker.class);

    private final LmsConfig config;
    private final LmsService lmsService;
    private final ScheduledExecutorService executor;

    public LmsResultWorker(LmsConfig config, LmsService lmsService) {
        this.config = config;
        this.lmsService = lmsService;
        this.executor = Executors.newScheduledThreadPool(4, r -> {
            Thread t = new Thread(r, "lms-result-worker");
            t.setDaemon(true);
            return t;
        });
    }

    public void scheduleResultForRawOrm(String rawOrmHl7) {
        String placerId = extractField(rawOrmHl7, "ORC", 2);
        if (placerId == null) {
            placerId = extractField(rawOrmHl7, "OBR", 2);
        }
        String obr4 = extractField(rawOrmHl7, "OBR", 4);
        String testCode = (obr4 != null && !obr4.isBlank()) ? obr4.split("\\^")[0] : "CBC";

        scheduleResult(placerId, testCode);
    }

    public void scheduleResult(String placerOrderNumber, String testCode) {
        long delay = config.getResultDelayMs() > 0 ? config.getResultDelayMs() : 100L;
        log.info("[LMS] Scheduled lab result generation for order {} ({}) in {} ms", placerOrderNumber, testCode, delay);

        executor.schedule(() -> {
            try {
                lmsService.produceLabResult(placerOrderNumber, testCode);
            } catch (Exception e) {
                log.error("[LMS] Failed to generate and send scheduled lab result: {}", e.getMessage(), e);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    private String extractField(String rawHl7, String segmentName, int fieldIndex) {
        if (rawHl7 == null) return null;
        String[] lines = rawHl7.split("\r\n|\r|\n");
        for (String line : lines) {
            if (line.startsWith(segmentName + "|")) {
                String[] fields = line.split("\\|", -1);
                int idx = "MSH".equals(segmentName) ? fieldIndex - 1 : fieldIndex;
                if (idx < fields.length) {
                    return fields[idx].trim();
                }
            }
        }
        return null;
    }

    @PreDestroy
    public void stop() {
        executor.shutdownNow();
    }
}
