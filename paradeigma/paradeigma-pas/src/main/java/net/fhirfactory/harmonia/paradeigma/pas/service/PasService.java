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

package net.fhirfactory.harmonia.paradeigma.pas.service;

import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.failure.FailureSimulator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7Parsers;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.pas.config.PasConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PasService implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(PasService.class);

    private final PasConfig config;
    private final PasPatientLifecycleManager lifecycleManager;
    private final FailureSimulator failureSimulator;

    private final AtomicLong messagesSent = new AtomicLong(0);
    private final AtomicLong ackAcceptCount = new AtomicLong(0);
    private final AtomicLong ackErrorCount = new AtomicLong(0);
    private final AtomicLong ackRejectCount = new AtomicLong(0);
    private final AtomicLong failureCount = new AtomicLong(0);
    private final LocalDateTime startedAt = LocalDateTime.now();
    private volatile LocalDateTime lastActivityAt = LocalDateTime.now();

    public PasService(PasConfig config, PasPatientLifecycleManager lifecycleManager) {
        this.config = config;
        this.lifecycleManager = lifecycleManager;
        this.failureSimulator = new FailureSimulator(config.getFaultInjection());
    }

    /**
     * Sends an HL7 ADT message to Harmonia via MLLP and handles the synchronous ACK.
     */
    public ManualTriggerResponse sendAdtMessage(String rawHl7) {
        String msgControlId = Hl7Parsers.extractMessageControlId(rawHl7);
        String triggerEvent = Hl7Parsers.extractTriggerEvent(rawHl7);
        long startTime = System.currentTimeMillis();

        String payloadToSend = rawHl7;
        if (failureSimulator.shouldCorruptPayload()) {
            payloadToSend = failureSimulator.corruptMessage(rawHl7);
        }

        try (MllpClient client = new MllpClient(config.getHarmoniaHost(), config.getHarmoniaPort())) {
            messagesSent.incrementAndGet();
            lastActivityAt = LocalDateTime.now();

            String ack = client.sendAndReceive(payloadToSend);
            long duration = System.currentTimeMillis() - startTime;
            AckResult ackResult = Hl7AckHandler.parseAck(ack);

            if (ackResult.isAccept()) {
                ackAcceptCount.incrementAndGet();
            } else if (ackResult.isError()) {
                ackErrorCount.incrementAndGet();
            } else if (ackResult.isReject()) {
                ackRejectCount.incrementAndGet();
            }

            log.info("[PAS] Sending ADT^{} | Message: {} | Destination: Harmonia ({}:{}) | ACK: {} | Duration: {} ms",
                    triggerEvent, msgControlId, config.getHarmoniaHost(), config.getHarmoniaPort(),
                    ackResult.getAckCode(), duration);

            return ManualTriggerResponse.success(msgControlId, triggerEvent, null, ackResult.getAckCode(), ackResult.getTextMessage(), duration);

        } catch (Exception e) {
            failureCount.incrementAndGet();
            lastActivityAt = LocalDateTime.now();
            long duration = System.currentTimeMillis() - startTime;
            log.error("[PAS] Failed sending ADT^{} [Message: {}] to Harmonia: {}", triggerEvent, msgControlId, e.getMessage());
            return ManualTriggerResponse.failure(triggerEvent, e.getMessage());
        }
    }

    public ManualTriggerResponse triggerNextLifecycleEvent() {
        String adtHl7 = lifecycleManager.nextLifecycleEvent();
        return sendAdtMessage(adtHl7);
    }

    public SimulatorStatusDto getStatus() {
        SimulatorStatusDto status = new SimulatorStatusDto("PAS", true, config.getHarmoniaPort());
        status.setMessagesSent(messagesSent.get());
        status.setAckAcceptCount(ackAcceptCount.get());
        status.setAckErrorCount(ackErrorCount.get());
        status.setAckRejectCount(ackRejectCount.get());
        status.setFailureCount(failureCount.get());
        status.setStartedAt(startedAt);
        status.setLastActivityAt(lastActivityAt);
        return status;
    }

    public PasConfig getConfig() {
        return config;
    }

    public PasPatientLifecycleManager getLifecycleManager() {
        return lifecycleManager;
    }

    @Override
    @PreDestroy
    public void close() {
        log.info("[PAS] Shutting down PAS service.");
    }
}
