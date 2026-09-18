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

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Configuration options for simulating transport and application-level failures in Paradeigma.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaultInjectionConfig {

    private boolean enabled = false;
    private Long randomSeed = 12345L;

    /** Probability (0.0 - 1.0) of not returning an ACK (simulates network/service timeout). */
    private double noAckProbability = 0.0;

    /** Fixed delay in milliseconds before returning an ACK. */
    private long ackDelayMs = 0L;

    /** Probability (0.0 - 1.0) of returning an Application Error (AE) ACK. */
    private double applicationErrorProbability = 0.0;

    /** Probability (0.0 - 1.0) of returning an Application Reject (AR) ACK. */
    private double applicationRejectProbability = 0.0;

    /** Probability (0.0 - 1.0) of abruptly closing the TCP socket connection. */
    private double dropConnectionProbability = 0.0;

    /** Probability (0.0 - 1.0) of introducing malformed segments / syntax errors. */
    private double malformedSegmentProbability = 0.0;

    /** Probability (0.0 - 1.0) of sending duplicate MSH-10 control IDs. */
    private double duplicateMsh10Probability = 0.0;

    public FaultInjectionConfig() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(Long randomSeed) {
        this.randomSeed = randomSeed;
    }

    public double getNoAckProbability() {
        return noAckProbability;
    }

    public void setNoAckProbability(double noAckProbability) {
        this.noAckProbability = noAckProbability;
    }

    public long getAckDelayMs() {
        return ackDelayMs;
    }

    public void setAckDelayMs(long ackDelayMs) {
        this.ackDelayMs = ackDelayMs;
    }

    public double getApplicationErrorProbability() {
        return applicationErrorProbability;
    }

    public void setApplicationErrorProbability(double applicationErrorProbability) {
        this.applicationErrorProbability = applicationErrorProbability;
    }

    public double getApplicationRejectProbability() {
        return applicationRejectProbability;
    }

    public void setApplicationRejectProbability(double applicationRejectProbability) {
        this.applicationRejectProbability = applicationRejectProbability;
    }

    public double getDropConnectionProbability() {
        return dropConnectionProbability;
    }

    public void setDropConnectionProbability(double dropConnectionProbability) {
        this.dropConnectionProbability = dropConnectionProbability;
    }

    public double getMalformedSegmentProbability() {
        return malformedSegmentProbability;
    }

    public void setMalformedSegmentProbability(double malformedSegmentProbability) {
        this.malformedSegmentProbability = malformedSegmentProbability;
    }

    public double getDuplicateMsh10Probability() {
        return duplicateMsh10Probability;
    }

    public void setDuplicateMsh10Probability(double duplicateMsh10Probability) {
        this.duplicateMsh10Probability = duplicateMsh10Probability;
    }
}
