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

package net.fhirfactory.harmonia.paradeigma.rispac.mllp;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.rispac.config.RispacConfig;
import net.fhirfactory.harmonia.paradeigma.rispac.service.RispacResultWorker;
import net.fhirfactory.harmonia.paradeigma.rispac.service.RispacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Inbound MLLP Listeners for RIS-PAC:
 * - Port 2203: PD-07 RISPAC-ADT-OUT (Receives fan-out ADT messages)
 * - Port 2205: PD-09 RISPAC-ORM-OUT (Receives routed Imaging ORM orders)
 */
@Component
public class RispacMllpListener {

    private static final Logger log = LoggerFactory.getLogger(RispacMllpListener.class);

    private final RispacConfig config;
    private final RispacService rispacService;
    private final RispacResultWorker resultWorker;

    private MllpServer adtServer;
    private MllpServer ormServer;

    public RispacMllpListener(RispacConfig config, RispacService rispacService, @Lazy RispacResultWorker resultWorker) {
        this.config = config;
        this.rispacService = rispacService;
        this.resultWorker = resultWorker;
    }

    @PostConstruct
    public synchronized void start() {
        try {
            adtServer = new MllpServer(config.getInboundAdtPort(), this::handleInboundAdt);
            adtServer.start();
            log.info("[RIS-PAC] Inbound ADT Listener started on port {}", adtServer.getPort());

            ormServer = new MllpServer(config.getInboundOrmPort(), this::handleInboundOrm);
            ormServer.start();
            log.info("[RIS-PAC] Inbound Imaging ORM Listener started on port {}", ormServer.getPort());

        } catch (IOException e) {
            log.error("[RIS-PAC] Failed to start MLLP listeners: {}", e.getMessage(), e);
        }
    }

    private String handleInboundAdt(String rawHl7) {
        log.info("[RIS-PAC] Received fan-out ADT message ({} chars)", rawHl7 != null ? rawHl7.length() : 0);
        rispacService.recordAdt(rawHl7);
        return rispacService.getFailureSimulator().evaluateAck(rawHl7);
    }

    private String handleInboundOrm(String rawHl7) {
        log.info("[RIS-PAC] Received routed Imaging ORM order ({} chars)", rawHl7 != null ? rawHl7.length() : 0);
        rispacService.recordOrm(rawHl7);

        if (config.isAutoProduceReports() && resultWorker != null) {
            resultWorker.scheduleReportForRawOrm(rawHl7);
        }

        return rispacService.getFailureSimulator().evaluateAck(rawHl7);
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
