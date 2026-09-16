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
import net.fhirfactory.harmonia.paradeigma.common.failure.FailureSimulator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticResultGenerator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7MessageBuilders;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7Parsers;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderType;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.ResultProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.lms.config.LmsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LmsService implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(LmsService.class);

    private final LmsConfig config;
    private final SyntheticResultGenerator resultGenerator;
    private final FailureSimulator failureSimulator;

    private final Map<String, PatientProfile> patientRegistry = new ConcurrentHashMap<>();
    private final Map<String, OrderProfile> receivedOrders = new ConcurrentHashMap<>();

    private final AtomicLong messagesSent = new AtomicLong(0);
    private final AtomicLong messagesReceived = new AtomicLong(0);
    private final AtomicLong ackAcceptCount = new AtomicLong(0);
    private final AtomicLong ackErrorCount = new AtomicLong(0);
    private final AtomicLong ackRejectCount = new AtomicLong(0);
    private final AtomicLong failureCount = new AtomicLong(0);
    private final LocalDateTime startedAt = LocalDateTime.now();
    private volatile LocalDateTime lastActivityAt = LocalDateTime.now();

    public LmsService(LmsConfig config) {
        this.config = config;
        this.resultGenerator = new SyntheticResultGenerator(config.getSeed());
        this.failureSimulator = new FailureSimulator(config.getFaultInjection());
    }

    /**
     * Sends an ORU^R01 lab result message to Harmonia (PD-02 :2102).
     */
    public ManualTriggerResponse sendLabResult(ResultProfile result, PatientProfile patient, VisitProfile visit) {
        String oruHl7 = Hl7MessageBuilders.buildOruR01(result, patient, visit);
        String msgControlId = Hl7Parsers.extractMessageControlId(oruHl7);
        long startTime = System.currentTimeMillis();

        String payloadToSend = oruHl7;
        if (failureSimulator.shouldCorruptPayload()) {
            payloadToSend = failureSimulator.corruptMessage(oruHl7);
        }

        try (MllpClient client = new MllpClient(config.getHarmoniaHost(), config.getHarmoniaOruPort())) {
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

            log.info("[LMS] Sent ORU^R01 lab result {} ({}) | Message: {} | Harmonia ({}:{}) | ACK: {} | Duration: {} ms",
                    result.getFillerOrderNumber(), result.getUniversalServiceId(), msgControlId,
                    config.getHarmoniaHost(), config.getHarmoniaOruPort(), ackResult.getAckCode(), duration);

            return ManualTriggerResponse.success(msgControlId, "R01", patient != null ? patient.getPatientId() : null,
                    ackResult.getAckCode(), ackResult.getTextMessage(), duration);

        } catch (Exception e) {
            failureCount.incrementAndGet();
            lastActivityAt = LocalDateTime.now();
            long duration = System.currentTimeMillis() - startTime;
            log.error("[LMS] Failed sending ORU^R01 result {} to Harmonia: {}", result.getFillerOrderNumber(), e.getMessage());
            return ManualTriggerResponse.failure("R01", e.getMessage());
        }
    }

    public ManualTriggerResponse produceLabResult(String placerOrderNumber, String testCode) {
        OrderProfile order = receivedOrders.get(placerOrderNumber);
        if (order == null) {
            String ordNum = placerOrderNumber != null ? placerOrderNumber : "ORD-" + System.currentTimeMillis() % 100000;
            order = new OrderProfile(ordNum, "PAT-1001", "VIS-2001", OrderType.LABORATORY, testCode != null ? testCode : "CBC", "Complete Blood Count");
        }
        PatientProfile patient = patientRegistry.getOrDefault(order.getPatientId(), new PatientProfile(order.getPatientId(), "Smith", "John", null, null));
        ResultProfile result = resultGenerator.generateResult(order);
        return sendLabResult(result, patient, null);
    }

    public void recordAdt(String rawHl7) {
        messagesReceived.incrementAndGet();
        lastActivityAt = LocalDateTime.now();
        String rawPatId = extractField(rawHl7, "PID", 3);
        String patId = (rawPatId != null && !rawPatId.isBlank()) ? rawPatId.split("\\^")[0].trim() : null;
        if (patId != null) {
            PatientProfile p = new PatientProfile();
            p.setPatientId(patId);
            patientRegistry.put(patId, p);
        }
    }

    public void recordOrm(String rawHl7) {
        messagesReceived.incrementAndGet();
        lastActivityAt = LocalDateTime.now();
        String placerId = extractField(rawHl7, "ORC", 2);
        String obr4 = extractField(rawHl7, "OBR", 4);
        String patId = extractField(rawHl7, "PID", 3);

        String code = "CBC";
        String text = "Complete Blood Count";
        if (obr4 != null) {
            String[] parts = obr4.split("\\^");
            if (parts.length > 0) code = parts[0];
            if (parts.length > 1) text = parts[1];
        }

        OrderProfile order = new OrderProfile(
                placerId != null ? placerId : "ORD-" + System.currentTimeMillis() % 100000,
                patId != null ? patId : "PAT-1001",
                "VIS-2001",
                OrderType.LABORATORY,
                code,
                text
        );
        receivedOrders.put(order.getPlacerOrderNumber(), order);
        log.info("[LMS] Ingested and stored Lab Order {} ({})", order.getPlacerOrderNumber(), code);
    }

    public SimulatorStatusDto getStatus() {
        SimulatorStatusDto status = new SimulatorStatusDto("LMS", true, config.getInboundOrmPort());
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

    public LmsConfig getConfig() {
        return config;
    }

    public FailureSimulator getFailureSimulator() {
        return failureSimulator;
    }

    public List<OrderProfile> getReceivedOrders() {
        return new ArrayList<>(receivedOrders.values());
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

    @Override
    @PreDestroy
    public void close() {
        log.info("[LMS] Shutting down LMS service.");
    }
}
