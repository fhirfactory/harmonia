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

package net.fhirfactory.harmonia.paradeigma.lms.mllp;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.lms.config.LmsConfig;
import net.fhirfactory.harmonia.paradeigma.lms.service.LmsResultWorker;
import net.fhirfactory.harmonia.paradeigma.lms.service.LmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Inbound MLLP Listeners for LMS:
 * - Port 2202: PD-06 LMS-ADT-OUT (Receives fan-out ADT messages)
 * - Port 2204: PD-08 LMS-ORM-OUT (Receives routed Lab ORM orders)
 */
@Component
public class LmsMllpListener {

    private static final Logger log = LoggerFactory.getLogger(LmsMllpListener.class);

    private final LmsConfig config;
    private final LmsService lmsService;
    private final LmsResultWorker resultWorker;

    private MllpServer adtServer;
    private MllpServer ormServer;

    public LmsMllpListener(LmsConfig config, LmsService lmsService, @Lazy LmsResultWorker resultWorker) {
        this.config = config;
        this.lmsService = lmsService;
        this.resultWorker = resultWorker;
    }

    @PostConstruct
    public synchronized void start() {
        try {
            adtServer = new MllpServer(config.getInboundAdtPort(), this::handleInboundAdt);
            adtServer.start();
            log.info("[LMS] Inbound ADT Listener started on port {}", adtServer.getPort());

            ormServer = new MllpServer(config.getInboundOrmPort(), this::handleInboundOrm);
            ormServer.start();
            log.info("[LMS] Inbound Lab ORM Listener started on port {}", ormServer.getPort());

        } catch (IOException e) {
            log.error("[LMS] Failed to start MLLP listeners: {}", e.getMessage(), e);
        }
    }

    private String handleInboundAdt(String rawHl7) {
        log.info("[LMS] Received fan-out ADT message ({} chars)", rawHl7 != null ? rawHl7.length() : 0);
        lmsService.recordAdt(rawHl7);
        return lmsService.getFailureSimulator().evaluateAck(rawHl7);
    }

    private String handleInboundOrm(String rawHl7) {
        log.info("[LMS] Received routed Lab ORM order ({} chars)", rawHl7 != null ? rawHl7.length() : 0);
        lmsService.recordOrm(rawHl7);

        if (config.isAutoProduceResults() && resultWorker != null) {
            resultWorker.scheduleResultForRawOrm(rawHl7);
        }

        return lmsService.getFailureSimulator().evaluateAck(rawHl7);
    }

    public MllpServer getAdtServer() {
        return adtServer;
    }

    public MllpServer getOrmServer() {
        return ormServer;
    }

    @PreDestroy
    public synchronized void stop() {
        if (adtServer != null) adtServer.stop();
        if (ormServer != null) ormServer.stop();
    }
}
