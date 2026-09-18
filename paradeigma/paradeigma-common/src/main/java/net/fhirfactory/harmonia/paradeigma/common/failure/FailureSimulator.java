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

package net.fhirfactory.harmonia.paradeigma.common.failure;

import net.fhirfactory.harmonia.paradeigma.common.generator.SeedRandom;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor for simulating failures (dropped sockets, delayed ACKs, AE/AR NACKs, corrupted segments)
 * based on {@link FaultInjectionConfig}.
 */
public class FailureSimulator {

    private static final Logger LOG = LoggerFactory.getLogger(FailureSimulator.class);

    private final FaultInjectionConfig config;
    private final SeedRandom random;

    public FailureSimulator() {
        this(new FaultInjectionConfig());
    }

    public FailureSimulator(FaultInjectionConfig config) {
        this.config = (config != null) ? config : new FaultInjectionConfig();
        long seed = (this.config.getRandomSeed() != null) ? this.config.getRandomSeed() : 12345L;
        this.random = new SeedRandom(seed);
    }

    public boolean shouldDropConnection() {
        if (!config.isEnabled()) return false;
        boolean drop = random.nextBoolean(config.getDropConnectionProbability());
        if (drop) {
            LOG.warn("[Failure Simulator] Injecting FAULT: TCP connection drop");
        }
        return drop;
    }

    public boolean shouldDropAck() {
        if (!config.isEnabled()) return false;
        boolean drop = random.nextBoolean(config.getNoAckProbability());
        if (drop) {
            LOG.warn("[Failure Simulator] Injecting FAULT: No ACK response (timeout)");
        }
        return drop;
    }

    public boolean shouldInjectErrorAck() {
        if (!config.isEnabled()) return false;
        return random.nextBoolean(config.getApplicationErrorProbability());
    }

    public boolean shouldInjectRejectAck() {
        if (!config.isEnabled()) return false;
        return random.nextBoolean(config.getApplicationRejectProbability());
    }

    public boolean shouldCorruptPayload() {
        if (!config.isEnabled()) return false;
        return random.nextBoolean(config.getMalformedSegmentProbability());
    }

    public boolean shouldDuplicateMsh10() {
        if (!config.isEnabled()) return false;
        return random.nextBoolean(config.getDuplicateMsh10Probability());
    }

    public void applyAckDelay() {
        if (!config.isEnabled() || config.getAckDelayMs() <= 0) {
            return;
        }
        try {
            LOG.debug("[Failure Simulator] Delaying ACK by {} ms", config.getAckDelayMs());
            Thread.sleep(config.getAckDelayMs());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Inspects an incoming message and returns the appropriate ACK according to active failure injection rules.
     *
     * @param incomingMessage the received HL7 message
     * @return ACK string or null if ACK is dropped
     */
    public String evaluateAck(String incomingMessage) {
        applyAckDelay();

        if (shouldDropAck()) {
            return null; // Don't return any ACK
        }

        if (shouldInjectRejectAck()) {
            LOG.warn("[Failure Simulator] Injecting FAULT: Returning AR (Application Reject) ACK");
            return Hl7AckHandler.generateRejectAck(incomingMessage, "Simulated Application Reject");
        }

        if (shouldInjectErrorAck()) {
            LOG.warn("[Failure Simulator] Injecting FAULT: Returning AE (Application Error) ACK");
            return Hl7AckHandler.generateErrorAck(incomingMessage, "Simulated Application Error");
        }

        return Hl7AckHandler.generateAcceptAck(incomingMessage);
    }

    /**
     * Corrupts an HL7 message payload by mangling delimiters or segment headers.
     */
    public String corruptMessage(String rawHl7) {
        if (rawHl7 == null || !config.isEnabled()) {
            return rawHl7;
        }
        LOG.warn("[Failure Simulator] Injecting FAULT: Corrupting HL7 payload segments");
        // Corrupt PID segment by breaking the header
        return rawHl7.replace("PID|1|", "PID_CORRUPTED|INVALID|");
    }

    public FaultInjectionConfig getConfig() {
        return config;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
