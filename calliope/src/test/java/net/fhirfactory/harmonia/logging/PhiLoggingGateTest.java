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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PHI Security Gate & Logging Policy Matrix Tests")
class PhiLoggingGateTest {

    private Logger phiLogbackLogger;
    private Logger operationalLogbackLogger;
    private ListAppender<ILoggingEvent> phiAppender;
    private ListAppender<ILoggingEvent> opAppender;
    private PhiLogger phiLogger;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.reset();
        System.clearProperty(PhiLoggingConfig.PROPERTY_PHI_ENABLED);

        phiLogbackLogger = (Logger) LoggerFactory.getLogger(DefaultPhiLogger.PHI_LOGGER_NAME);
        operationalLogbackLogger = (Logger) LoggerFactory.getLogger("net.fhirfactory.harmonia.test");

        phiAppender = new ListAppender<>();
        phiAppender.start();
        phiLogbackLogger.addAppender(phiAppender);

        opAppender = new ListAppender<>();
        opAppender.start();
        operationalLogbackLogger.addAppender(opAppender);

        phiLogger = new DefaultPhiLogger(PhiLoggingGateTest.class.getName(), phiLogbackLogger);
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.reset();
        System.clearProperty(PhiLoggingConfig.PROPERTY_PHI_ENABLED);

        if (phiAppender != null) {
            phiAppender.stop();
            phiLogbackLogger.detachAppender(phiAppender);
        }
        if (opAppender != null) {
            opAppender.stop();
            operationalLogbackLogger.detachAppender(opAppender);
        }
    }

    @Test
    @DisplayName("Default configuration is strictly disabled")
    void testDefaultIsDisabled() {
        assertThat(PhiLoggingConfig.isPhiEnabled())
                .as("harmonia.logging.phi-enabled must default to false")
                .isFalse();
    }

    @Test
    @DisplayName("TEST 1: INFO enabled, PHI disabled -> no PHI output")
    void test1_InfoEnabled_PhiDisabled_NoPhiOutput() {
        PhiLoggingConfig.setPhiEnabled(false);
        phiLogbackLogger.setLevel(Level.INFO);

        phiLogger.debug("Patient DOB=1980-01-01, Name=John Doe");
        phiLogger.trace("Patient MRN=MRN-12345");

        assertThat(phiAppender.list).isEmpty();
        assertThat(phiLogger.isDebugEnabled()).isFalse();
        assertThat(phiLogger.isTraceEnabled()).isFalse();
    }

    @Test
    @DisplayName("TEST 2: DEBUG enabled, PHI disabled -> no PHI output")
    void test2_DebugEnabled_PhiDisabled_NoPhiOutput() {
        PhiLoggingConfig.setPhiEnabled(false);
        phiLogbackLogger.setLevel(Level.DEBUG);

        phiLogger.debug("Patient DOB=1980-01-01, Name=John Doe");
        phiLogger.trace("Patient MRN=MRN-12345");

        assertThat(phiAppender.list).isEmpty();
        assertThat(phiLogger.isDebugEnabled()).isFalse();
        assertThat(phiLogger.isTraceEnabled()).isFalse();
    }

    @Test
    @DisplayName("TEST 3: TRACE enabled, PHI disabled -> no PHI output")
    void test3_TraceEnabled_PhiDisabled_NoPhiOutput() {
        PhiLoggingConfig.setPhiEnabled(false);
        phiLogbackLogger.setLevel(Level.TRACE);

        phiLogger.debug("Patient DOB=1980-01-01, Name=John Doe");
        phiLogger.trace("Patient MRN=MRN-12345");

        assertThat(phiAppender.list).isEmpty();
        assertThat(phiLogger.isDebugEnabled()).isFalse();
        assertThat(phiLogger.isTraceEnabled()).isFalse();
    }

    @Test
    @DisplayName("TEST 4: DEBUG enabled, PHI enabled -> PHI DEBUG output emitted")
    void test4_DebugEnabled_PhiEnabled_PhiDebugOutputEmitted() {
        PhiLoggingConfig.setPhiEnabled(true);
        phiLogbackLogger.setLevel(Level.DEBUG);

        assertThat(phiLogger.isDebugEnabled()).isTrue();
        assertThat(phiLogger.isTraceEnabled()).isFalse();

        phiLogger.debug("Patient DOB=1980-01-01, Name=John Doe");
        phiLogger.trace("Patient MRN=MRN-12345"); // TRACE should not be emitted at DEBUG level

        assertThat(phiAppender.list).hasSize(1);
        ILoggingEvent event = phiAppender.list.get(0);
        assertThat(event.getLevel()).isEqualTo(Level.DEBUG);
        assertThat(event.getFormattedMessage()).contains("Patient DOB=1980-01-01, Name=John Doe");
        assertThat(event.getMarkerList()).extracting(org.slf4j.Marker::getName).contains("PHI");
    }

    @Test
    @DisplayName("TEST 5: TRACE enabled, PHI enabled -> PHI TRACE output emitted")
    void test5_TraceEnabled_PhiEnabled_PhiTraceOutputEmitted() {
        PhiLoggingConfig.setPhiEnabled(true);
        phiLogbackLogger.setLevel(Level.TRACE);

        assertThat(phiLogger.isDebugEnabled()).isTrue();
        assertThat(phiLogger.isTraceEnabled()).isTrue();

        phiLogger.debug("Patient DOB=1980-01-01, Name=John Doe");
        phiLogger.trace("Patient MRN=MRN-12345");

        assertThat(phiAppender.list).hasSize(2);
        assertThat(phiAppender.list.get(0).getLevel()).isEqualTo(Level.DEBUG);
        assertThat(phiAppender.list.get(1).getLevel()).isEqualTo(Level.TRACE);
    }

    @Test
    @DisplayName("TEST 19: org.harmonia.phi=TRACE with phi-enabled=false -> no PHI output")
    void test19_LoggerTrace_PhiDisabled_NoOutput() {
        System.setProperty(PhiLoggingConfig.PROPERTY_PHI_ENABLED, "false");
        phiLogbackLogger.setLevel(Level.TRACE);

        assertThat(PhiLoggingConfig.isPhiEnabled()).isFalse();
        phiLogger.trace("Patient details: Alice Smith");

        assertThat(phiAppender.list).isEmpty();
    }

    @Test
    @DisplayName("TEST 20: phi-enabled=true with org.harmonia.phi=INFO -> no PHI output")
    void test20_PhiEnabled_LoggerInfo_NoOutput() {
        System.setProperty(PhiLoggingConfig.PROPERTY_PHI_ENABLED, "true");
        phiLogbackLogger.setLevel(Level.INFO);

        assertThat(PhiLoggingConfig.isPhiEnabled()).isTrue();
        assertThat(phiLogger.isDebugEnabled()).isFalse();
        assertThat(phiLogger.isTraceEnabled()).isFalse();

        phiLogger.debug("Sensitive patient observation");
        phiLogger.trace("Sensitive patient diagnostic");

        assertThat(phiAppender.list).isEmpty();
    }

    @Test
    @DisplayName("TEST 21: PHI emitted only when BOTH phi-enabled=true AND DEBUG/TRACE is enabled")
    void test21_DualGateConjunctionRequirement() {
        // Condition A: false, false -> NO
        PhiLoggingConfig.setPhiEnabled(false);
        phiLogbackLogger.setLevel(Level.WARN);
        phiLogger.debug("Test PHI");
        assertThat(phiAppender.list).isEmpty();

        // Condition B: false, true -> NO
        PhiLoggingConfig.setPhiEnabled(false);
        phiLogbackLogger.setLevel(Level.DEBUG);
        phiLogger.debug("Test PHI");
        assertThat(phiAppender.list).isEmpty();

        // Condition C: true, false -> NO
        PhiLoggingConfig.setPhiEnabled(true);
        phiLogbackLogger.setLevel(Level.WARN);
        phiLogger.debug("Test PHI");
        assertThat(phiAppender.list).isEmpty();

        // Condition D: true, true -> YES
        PhiLoggingConfig.setPhiEnabled(true);
        phiLogbackLogger.setLevel(Level.DEBUG);
        phiLogger.debug("Test PHI");
        assertThat(phiAppender.list).hasSize(1);
    }

    @Test
    @DisplayName("TEST 25: Normal DEBUG/TRACE operational logging remains possible without phi-enabled")
    void test25_OperationalDebugTraceRemainsActive() {
        PhiLoggingConfig.setPhiEnabled(false);
        operationalLogbackLogger.setLevel(Level.DEBUG);

        operationalLogbackLogger.debug("Operational component initialized: component=Pylai");

        assertThat(opAppender.list).hasSize(1);
        assertThat(opAppender.list.get(0).getFormattedMessage())
                .isEqualTo("Operational component initialized: component=Pylai");
    }
}
