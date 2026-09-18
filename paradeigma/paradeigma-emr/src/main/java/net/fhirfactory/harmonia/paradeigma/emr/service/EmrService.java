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

package net.fhirfactory.harmonia.paradeigma.emr.service;

import jakarta.annotation.PreDestroy;
import net.fhirfactory.harmonia.paradeigma.common.failure.FailureSimulator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7MessageBuilders;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7Parsers;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.emr.config.EmrConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmrService implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(EmrService.class);

    private final EmrConfig config;
    private final EmrPatientManager patientManager;
    private final FailureSimulator failureSimulator;

    private final AtomicLong messagesSent = new AtomicLong(0);
    private final AtomicLong messagesReceived = new AtomicLong(0);
    private final AtomicLong ackAcceptCount = new AtomicLong(0);
    private final AtomicLong ackErrorCount = new AtomicLong(0);
    private final AtomicLong ackRejectCount = new AtomicLong(0);
    private final AtomicLong failureCount = new AtomicLong(0);
    private final LocalDateTime startedAt = LocalDateTime.now();
    private volatile LocalDateTime lastActivityAt = LocalDateTime.now();

    public EmrService(EmrConfig config, EmrPatientManager patientManager) {
        this.config = config;
        this.patientManager = patientManager;
        this.failureSimulator = new FailureSimulator(config.getFaultInjection());
    }

    /**
     * Sends an HL7 ORM^O01 order to Harmonia (PD-04 :2104).
     */
    public ManualTriggerResponse sendOrder(OrderProfile order, PatientProfile patient, VisitProfile visit) {
        String ormHl7 = Hl7MessageBuilders.buildOrmO01(order, patient, visit);
        String msgControlId = Hl7Parsers.extractMessageControlId(ormHl7);
        long startTime = System.currentTimeMillis();

        String payloadToSend = ormHl7;
        if (failureSimulator.shouldCorruptPayload()) {
            payloadToSend = failureSimulator.corruptMessage(ormHl7);
        }

        try (MllpClient client = new MllpClient(config.getHarmoniaHost(), config.getHarmoniaOrmPort())) {
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

            log.info("[EMR] Placed ORM^O01 order {} ({}) | Message: {} | Harmonia ({}:{}) | ACK: {} | Duration: {} ms",
                    order.getPlacerOrderNumber(), order.getUniversalServiceId(), msgControlId,
                    config.getHarmoniaHost(), config.getHarmoniaOrmPort(), ackResult.getAckCode(), duration);

            return ManualTriggerResponse.success(msgControlId, "O01", patient.getPatientId(), ackResult.getAckCode(), ackResult.getTextMessage(), duration);

        } catch (Exception e) {
            failureCount.incrementAndGet();
            lastActivityAt = LocalDateTime.now();
            long duration = System.currentTimeMillis() - startTime;
            log.error("[EMR] Failed sending ORM^O01 order {} to Harmonia: {}", order.getPlacerOrderNumber(), e.getMessage());
            return ManualTriggerResponse.failure("O01", e.getMessage());
        }
    }

    public ManualTriggerResponse placeLabOrder(String specificTest) {
        PatientProfile patient = patientManager.getAnyPatient();
        VisitProfile visit = patientManager.getVisitForPatient(patient.getPatientId());
        OrderProfile order = patientManager.getOrderGenerator().generateLabOrder(patient, visit, specificTest);
        return sendOrder(order, patient, visit);
    }

    public ManualTriggerResponse placeImagingOrder(String specificStudy) {
        PatientProfile patient = patientManager.getAnyPatient();
        VisitProfile visit = patientManager.getVisitForPatient(patient.getPatientId());
        OrderProfile order = patientManager.getOrderGenerator().generateImagingOrder(patient, visit, specificStudy);
        return sendOrder(order, patient, visit);
    }

    public void incrementReceived() {
        messagesReceived.incrementAndGet();
        lastActivityAt = LocalDateTime.now();
    }

    public SimulatorStatusDto getStatus() {
        SimulatorStatusDto status = new SimulatorStatusDto("EMR", true, config.getInboundAdtPort());
        status.setMessagesSent(messagesSent.get());
        status.setMessagesReceived(messagesReceived.get());
        status.setAckAcceptCount(ackAcceptCount.get());
        status.setAckErrorCount(ackErrorCount.get());
        status.setAckRejectCount(ackRejectCount.get());
        status.setFailureCount(failureCount.get());
        status.setStartedAt(startedAt);
        status.setLastActivityAt(lastActivityAt);
        return status;
    }

    public EmrConfig getConfig() {
        return config;
    }

    public EmrPatientManager getPatientManager() {
        return patientManager;
    }

    public FailureSimulator getFailureSimulator() {
        return failureSimulator;
    }

    @Override
    @PreDestroy
    public void close() {
        log.info("[EMR] Shutting down EMR service.");
    }
}
