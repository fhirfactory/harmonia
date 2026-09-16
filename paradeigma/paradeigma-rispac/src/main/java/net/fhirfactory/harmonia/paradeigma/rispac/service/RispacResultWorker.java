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

package net.fhirfactory.harmonia.paradeigma.rispac.service;

import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.rispac.config.RispacConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RispacResultWorker {

    private static final Logger log = LoggerFactory.getLogger(RispacResultWorker.class);

    private final RispacConfig config;
    private final RispacService rispacService;
    private final ScheduledExecutorService executor;

    public RispacResultWorker(RispacConfig config, RispacService rispacService) {
        this.config = config;
        this.rispacService = rispacService;
        this.executor = Executors.newScheduledThreadPool(4, r -> {
            Thread t = new Thread(r, "rispac-report-worker");
            t.setDaemon(true);
            return t;
        });
    }

    public void scheduleReportForRawOrm(String rawOrmHl7) {
        String placerId = extractField(rawOrmHl7, "ORC", 2);
        if (placerId == null) {
            placerId = extractField(rawOrmHl7, "OBR", 2);
        }
        String obr4 = extractField(rawOrmHl7, "OBR", 4);
        String studyCode = (obr4 != null && !obr4.isBlank()) ? obr4.split("\\^")[0] : "XR_CHEST";

        scheduleReport(placerId, studyCode);
    }

    public void scheduleReport(String placerOrderNumber, String studyCode) {
        long delay = config.getReportingDelayMs() > 0 ? config.getReportingDelayMs() : 100L;
        log.info("[RIS-PAC] Scheduled imaging report for order {} ({}) in {} ms", placerOrderNumber, studyCode, delay);

        executor.schedule(() -> {
            try {
                rispacService.produceImagingReport(placerOrderNumber, studyCode);
            } catch (Exception e) {
                log.error("[RIS-PAC] Failed to generate and send scheduled imaging report: {}", e.getMessage(), e);
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
