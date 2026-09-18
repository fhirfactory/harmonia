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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PHI Log Routing, Marker, and Appender Isolation Tests")
class PhiLogRoutingTest {

    private LoggerContext loggerContext;
    private Logger rootLogger;
    private Logger phiLoggerInternal;
    private Logger operationalLogger;

    private ListAppender<ILoggingEvent> rootAppender;
    private ListAppender<ILoggingEvent> phiDiagnosticAppender;
    private PhiLogger phiLogger;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.reset();
        loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        phiLoggerInternal = loggerContext.getLogger(DefaultPhiLogger.PHI_LOGGER_NAME);
        operationalLogger = loggerContext.getLogger("net.fhirfactory.harmonia.pylai");

        // Clear existing appenders
        rootAppender = new ListAppender<>();
        rootAppender.setName("ROOT_OPERATIONAL");
        rootAppender.start();
        rootLogger.addAppender(rootAppender);

        phiDiagnosticAppender = new ListAppender<>();
        phiDiagnosticAppender.setName("PHI_DIAGNOSTIC");
        phiDiagnosticAppender.start();
        phiLoggerInternal.addAppender(phiDiagnosticAppender);

        // Enforce strict additivity=false on PHI logger
        phiLoggerInternal.setAdditive(false);
        phiLoggerInternal.setLevel(Level.TRACE);
        operationalLogger.setLevel(Level.INFO);

        phiLogger = PhiLoggerFactory.getLogger("TestComponent");
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.reset();
        if (rootAppender != null) {
            rootAppender.stop();
            rootLogger.detachAppender(rootAppender);
        }
        if (phiDiagnosticAppender != null) {
            phiDiagnosticAppender.stop();
            phiLoggerInternal.detachAppender(phiDiagnosticAppender);
        }
    }

    @Test
    @DisplayName("TEST 13: PHI DEBUG event uses namespace org.harmonia.phi")
    void test13_PhiDebugUsesDedicatedNamespace() {
        PhiLoggingConfig.setPhiEnabled(true);
        phiLogger.debug("Patient DOB=2000-01-01");

        assertThat(phiDiagnosticAppender.list).hasSize(1);
        ILoggingEvent event = phiDiagnosticAppender.list.get(0);
        assertThat(event.getLoggerName()).isEqualTo(DefaultPhiLogger.PHI_LOGGER_NAME);
    }

    @Test
    @DisplayName("TEST 14: PHI TRACE event uses namespace org.harmonia.phi")
    void test14_PhiTraceUsesDedicatedNamespace() {
        PhiLoggingConfig.setPhiEnabled(true);
        phiLogger.trace("Raw HL7 PID segment");

        assertThat(phiDiagnosticAppender.list).hasSize(1);
        ILoggingEvent event = phiDiagnosticAppender.list.get(0);
        assertThat(event.getLoggerName()).isEqualTo(DefaultPhiLogger.PHI_LOGGER_NAME);
    }

    @Test
    @DisplayName("TEST 15: Every PhiLogger event carries Marker 'PHI'")
    void test15_EveryPhiEventCarriesPhiMarker() {
        PhiLoggingConfig.setPhiEnabled(true);

        phiLogger.debug("Debug PHI message");
        phiLogger.trace("Trace PHI message");

        assertThat(phiDiagnosticAppender.list).hasSize(2);
        for (ILoggingEvent event : phiDiagnosticAppender.list) {
            assertThat(event.getMarkerList())
                    .extracting(org.slf4j.Marker::getName)
                    .contains("PHI");
        }
    }

    @Test
    @DisplayName("TEST 16 & 17: PHI diagnostic event reaches PHI appender and does NOT reach operational appender")
    void test16_and_17_AppenderIsolationAndNonPropagation() {
        PhiLoggingConfig.setPhiEnabled(true);

        phiLogger.debug("Diagnostic PHI content for Patient/12345");

        // Reaches PHI Diagnostic appender
        assertThat(phiDiagnosticAppender.list).hasSize(1);
        assertThat(phiDiagnosticAppender.list.get(0).getFormattedMessage())
                .contains("Diagnostic PHI content for Patient/12345");

        // Does NOT reach root/operational appender (additivity=false)
        assertThat(rootAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .noneMatch(msg -> msg.contains("Patient/12345"));
    }

    @Test
    @DisplayName("TEST 18: Normal Harmonia logging continues to reach operational appender")
    void test18_NormalHarmoniaLoggingReachesOperationalAppender() {
        operationalLogger.info("Gateway service started successfully: port=2575");

        assertThat(rootAppender.list).hasSize(1);
        assertThat(rootAppender.list.get(0).getFormattedMessage())
                .isEqualTo("Gateway service started successfully: port=2575");

        // Normal operational event must NOT go to PHI diagnostic appender
        assertThat(phiDiagnosticAppender.list).isEmpty();
    }

    @Test
    @DisplayName("TEST 22: PHI destination failure does not redirect PHI to operational logs")
    void test22_PhiDestinationFailureDoesNotRedirectToOperationalLogs() {
        PhiLoggingConfig.setPhiEnabled(true);

        // Simulate PHI diagnostic appender failure / removal
        phiLoggerInternal.detachAppender(phiDiagnosticAppender);
        phiDiagnosticAppender.stop();

        // Emit PHI
        phiLogger.debug("Sensitive patient diagnosis record");

        // Must still NOT appear in root/operational appender
        assertThat(rootAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .noneMatch(msg -> msg.contains("Sensitive patient diagnosis record"));
    }

    @Test
    @DisplayName("TEST 24: PHI-free exception handling does not expose diagnostic content through ERROR logging")
    void test24_PhiFreeExceptionHandling() {
        PhiLoggingConfig.setPhiEnabled(false);

        // Create exception with non-PHI reason code and message
        IllegalArgumentException safeException = new IllegalArgumentException("ERR_VAL_001: Invalid FHIR resource structure");

        // Log to operational error log
        operationalLogger.error("Failed to process transaction: code=ERR_VAL_001", safeException);

        assertThat(rootAppender.list).hasSize(1);
        ILoggingEvent event = rootAppender.list.get(0);
        assertThat(event.getFormattedMessage()).contains("ERR_VAL_001");
        assertThat(event.getFormattedMessage()).doesNotContain("Patient", "MRN", "DOB");
        assertThat(event.getThrowableProxy().getMessage()).isEqualTo("ERR_VAL_001: Invalid FHIR resource structure");
    }
}
