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

package net.fhirfactory.harmonia.paradeigma.common.model;

/**
 * Execution profiles for pacing simulated clinical scenarios.
 */
public enum ExecutionProfile {
    /** Paced for human observation and demonstration (e.g. 5-30s delays). */
    DEMO(5000L, 15000L, 10000L, 20000L),

    /** Accelerated deterministic pacing for automated integration and unit testing (e.g. 10-100ms delays). */
    TEST(10L, 50L, 20L, 100L),

    /** High message rate for concurrent patient journeys and stress testing. */
    LOAD(500L, 2000L, 1000L, 3000L);

    private final long minSendIntervalMs;
    private final long maxSendIntervalMs;
    private final long minResultDelayMs;
    private final long maxResultDelayMs;

    ExecutionProfile(long minSendIntervalMs, long maxSendIntervalMs, long minResultDelayMs, long maxResultDelayMs) {
        this.minSendIntervalMs = minSendIntervalMs;
        this.maxSendIntervalMs = maxSendIntervalMs;
        this.minResultDelayMs = minResultDelayMs;
        this.maxResultDelayMs = maxResultDelayMs;
    }

    public long getMinSendIntervalMs() {
        return minSendIntervalMs;
    }

    public long getMaxSendIntervalMs() {
        return maxSendIntervalMs;
    }

    public long getMinResultDelayMs() {
        return minResultDelayMs;
    }

    public long getMaxResultDelayMs() {
        return maxResultDelayMs;
    }

    public static ExecutionProfile fromString(String name) {
        if (name == null) {
            return TEST;
        }
        try {
            return valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return TEST;
        }
    }
}
