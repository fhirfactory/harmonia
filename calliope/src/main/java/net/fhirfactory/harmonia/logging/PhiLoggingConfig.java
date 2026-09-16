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

package net.fhirfactory.harmonia.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Configuration and security gating manager for Harmonia PHI-aware logging.
 * <p>
 * Controls whether Patient Health Information (PHI) is permitted in diagnostic
 * logs (DEBUG and TRACE levels). Defaults strictly to {@code false}.
 * </p>
 * <p>
 * Evaluates the Java system property {@code harmonia.logging.phi-enabled} and the
 * environment variable {@code HARMONIA_LOGGING_PHI_ENABLED}.
 * </p>
 */
public final class PhiLoggingConfig {

    public static final String PROPERTY_PHI_ENABLED = "harmonia.logging.phi-enabled";
    public static final String ENV_PHI_ENABLED = "HARMONIA_LOGGING_PHI_ENABLED";

    private static final Logger STARTUP_LOGGER = LoggerFactory.getLogger(PhiLoggingConfig.class);
    private static final AtomicBoolean WARNING_EMITTED = new AtomicBoolean(false);
    private static volatile Boolean overridePhiEnabled = null;

    private PhiLoggingConfig() {
        // utility class
    }

    /**
     * Checks whether PHI diagnostic mode is globally enabled.
     * Default is {@code false}.
     *
     * @return true if PHI diagnostic logging is explicitly enabled, false otherwise
     */
    public static boolean isPhiEnabled() {
        if (overridePhiEnabled != null) {
            return overridePhiEnabled;
        }

        String sysProp = System.getProperty(PROPERTY_PHI_ENABLED);
        if (sysProp != null && !sysProp.isBlank()) {
            boolean enabled = Boolean.parseBoolean(sysProp.trim());
            if (enabled) {
                checkAndEmitStartupWarning();
            }
            return enabled;
        }

        String envVal = System.getenv(ENV_PHI_ENABLED);
        if (envVal != null && !envVal.isBlank()) {
            boolean enabled = Boolean.parseBoolean(envVal.trim());
            if (enabled) {
                checkAndEmitStartupWarning();
            }
            return enabled;
        }

        return false;
    }

    /**
     * Programmatically override the PHI logging configuration (useful for tests and runtime configuration).
     *
     * @param enabled boolean value or null to clear override
     */
    public static void setPhiEnabled(Boolean enabled) {
        overridePhiEnabled = enabled;
        if (Boolean.TRUE.equals(enabled)) {
            checkAndEmitStartupWarning();
        }
    }

    /**
     * Resets any programmatically set override and the startup warning indicator.
     */
    public static void reset() {
        overridePhiEnabled = null;
        WARNING_EMITTED.set(false);
    }

    /**
     * Emits a one-time prominent non-PHI warning banner if PHI diagnostic logging is enabled.
     */
    public static void checkAndEmitStartupWarning() {
        if (WARNING_EMITTED.compareAndSet(false, true)) {
            emitStartupWarning();
        }
    }

    /**
     * Emits the startup warning banner to the operational log at WARN level.
     */
    public static void emitStartupWarning() {
        STARTUP_LOGGER.warn("================================================================================");
        STARTUP_LOGGER.warn("PHI diagnostic logging is ENABLED.");
        STARTUP_LOGGER.warn("DEBUG/TRACE diagnostic logs may contain protected health information.");
        STARTUP_LOGGER.warn("Ensure this setting is NOT active in untrusted or production environments without");
        STARTUP_LOGGER.warn("appropriate PHI security controls and restricted destination appenders.");
        STARTUP_LOGGER.warn("================================================================================");
    }

    /**
     * Resets the startup warning emitted state for testing purposes.
     */
    public static void resetWarningEmittedForTesting() {
        WARNING_EMITTED.set(false);
    }
}
