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

package net.fhirfactory.harmonia.paradeigma.emr.mllp;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.emr.config.EmrConfig;
import net.fhirfactory.harmonia.paradeigma.emr.service.EmrPatientManager;
import net.fhirfactory.harmonia.paradeigma.emr.service.EmrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Inbound MLLP TCP Server listening on port 2201 (PD-05 EMR-ADT-OUT) to receive
 * ADT messages fanned out from Harmonia.
 */
@Component
public class EmrMllpListener {

    private static final Logger log = LoggerFactory.getLogger(EmrMllpListener.class);

    private final EmrConfig config;
    private final EmrPatientManager patientManager;
    private final EmrService emrService;
    private MllpServer server;

    public EmrMllpListener(EmrConfig config, EmrPatientManager patientManager, EmrService emrService) {
        this.config = config;
        this.patientManager = patientManager;
        this.emrService = emrService;
    }

    @PostConstruct
    public synchronized void start() {
        try {
            server = new MllpServer(config.getInboundAdtPort(), this::handleInboundAdt);
            server.start();
            log.info("[EMR] Inbound MLLP Listener started on port {}", server.getPort());
        } catch (IOException e) {
            log.error("[EMR] Failed to start MLLP listener on port {}: {}", config.getInboundAdtPort(), e.getMessage(), e);
        }
    }

    private String handleInboundAdt(String rawHl7) {
        log.info("[EMR] Received fan-out ADT message ({} chars)", rawHl7 != null ? rawHl7.length() : 0);
        emrService.incrementReceived();
        patientManager.recordAdtMessage(rawHl7);

        // Evaluate ACK using configured failure simulator
        return emrService.getFailureSimulator().evaluateAck(rawHl7);
    }

    public MllpServer getServer() {
        return server;
    }

    @PreDestroy
    public synchronized void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
