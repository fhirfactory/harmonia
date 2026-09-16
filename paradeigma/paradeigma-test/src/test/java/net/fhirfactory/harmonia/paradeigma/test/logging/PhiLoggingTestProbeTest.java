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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.test.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import net.fhirfactory.harmonia.logging.PhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggerFactory;
import net.fhirfactory.harmonia.logging.PhiLoggingConfig;
import net.fhirfactory.harmonia.paradeigma.common.logging.PhiLogTestProbe;
import net.fhirfactory.harmonia.paradeigma.common.logging.SecretLeakageAssertion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validates PhiLogTestProbe and SecretLeakageAssertion across all 5 Harmonia PHI logging policy matrix scenarios.
 */
public class PhiLoggingTestProbeTest {

    private org.slf4j.Logger operationalLogger;
    private PhiLogger phiLogger;
    private Logger rootLogger;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.reset();
        PhiLoggerFactory.clearCacheForTesting();
        operationalLogger = LoggerFactory.getLogger("net.fhirfactory.harmonia.pylai.registry");
        phiLogger = PhiLoggerFactory.getLogger("ProviderRegistryGateway");
        rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        ((Logger) LoggerFactory.getLogger("ca.uhn.fhir")).setLevel(Level.WARN);
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.reset();
        PhiLoggerFactory.clearCacheForTesting();
    }

    @Test
    @DisplayName("Scenario A: INFO level + phi-enabled=false -> Operational logs present, PHI absent")
    void scenarioA_InfoLevelPhiDisabled() {
        PhiLoggingConfig.setPhiEnabled(false);
        rootLogger.setLevel(Level.INFO);

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.info("Received change request for Practitioner/pract-101: correlationId=corr-001");
            phiLogger.debug("Synthetic Practitioner PHI: Dr. Frank Bowman, HPI-I 8003610000000001");

            probe.assertNoPhiInOperationalLogs("Frank Bowman", "8003610000000001");
            assertThat(probe.getOperationalEvents()).hasSize(1);
            assertThat(probe.getPhiDiagnosticEvents()).isEmpty();
        }
    }

    @Test
    @DisplayName("Scenario B: DEBUG level + phi-enabled=false -> Operational DEBUG present, PHI absent")
    void scenarioB_DebugLevelPhiDisabled() {
        PhiLoggingConfig.setPhiEnabled(false);
        rootLogger.setLevel(Level.DEBUG);

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.debug("Ingress token parsed successfully: sub=user-steward-01, tenant=tenant-stvincents");
            phiLogger.debug("Synthetic Practitioner PHI: Dr. Frank Bowman, HPI-I 8003610000000001");

            probe.assertNoPhiInOperationalLogs("Frank Bowman", "8003610000000001");
            assertThat(probe.getOperationalEvents()).hasSize(1);
            assertThat(probe.getPhiDiagnosticEvents()).isEmpty();
        }
    }

    @Test
    @DisplayName("Scenario C: TRACE level + phi-enabled=false -> Operational TRACE present, PHI absent")
    void scenarioC_TraceLevelPhiDisabled() {
        PhiLoggingConfig.setPhiEnabled(false);
        rootLogger.setLevel(Level.TRACE);

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.trace("Evaluating RBAC decision: action=CREATE, domain=PROVIDER_REGISTRY");
            phiLogger.trace("Full Synthetic Resource Dump: Practitioner/pract-101 Dr. Frank Bowman");

            probe.assertNoPhiInOperationalLogs("Frank Bowman");
            assertThat(probe.getOperationalEvents()).hasSize(1);
            assertThat(probe.getPhiDiagnosticEvents()).isEmpty();
        }
    }

    @Test
    @DisplayName("Scenario D: DEBUG level + phi-enabled=true -> PHI logged to org.harmonia.phi with PHI Marker")
    void scenarioD_DebugLevelPhiEnabled() {
        PhiLoggingConfig.setPhiEnabled(true);
        rootLogger.setLevel(Level.DEBUG);

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.debug("Processing Practitioner change task pragma-100");
            phiLogger.debug("Diagnostic Practitioner detail: Dr. Frank Bowman, DOB=1978-05-12");

            probe.assertNoPhiInOperationalLogs("Frank Bowman", "1978-05-12");
            probe.assertPhiPresentInDiagnosticLogs("Dr. Frank Bowman", "1978-05-12");
            probe.assertPhiMarkerAttachedToDiagnosticLogs();
        }
    }

    @Test
    @DisplayName("Scenario E: TRACE level + phi-enabled=true -> Detailed PHI emitted with Marker")
    void scenarioE_TraceLevelPhiEnabled() {
        PhiLoggingConfig.setPhiEnabled(true);
        rootLogger.setLevel(Level.TRACE);

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.trace("Dispatching workflow seq-provider-registry-change-pipeline");
            phiLogger.trace("Payload trace: PractitionerRole id=role-101, Practitioner=pract-101, Org=org-101");

            probe.assertNoPhiInOperationalLogs("role-101");
            probe.assertPhiPresentInDiagnosticLogs("role-101", "pract-101");
            probe.assertPhiMarkerAttachedToDiagnosticLogs();
        }
    }

    @Test
    @DisplayName("Secret Leakage Prevention: Secrets never appear in operational or PHI logs")
    void secretLeakagePrevention() {
        PhiLoggingConfig.setPhiEnabled(true);
        rootLogger.setLevel(Level.TRACE);

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            // Emitting normal operational message with token identifier sanitized
            operationalLogger.info("Authenticated client using token digest: SHA256-abc");
            phiLogger.debug("Diagnostic provider info: Dr. Sarah Chen");

            probe.assertNoSecretsInAnyLog(SecretLeakageAssertion.getStandardSyntheticSecrets());
        }
    }

    @Test
    @DisplayName("Validation failure scenario: OperationOutcome / error contains no PHI")
    void validationFailureNoPhiLeakage() {
        PhiLoggingConfig.setPhiEnabled(false);
        rootLogger.setLevel(Level.WARN);

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.warn("Validation failed for change request: code=INVALID_IDENTIFIER, detail=Check digit failure on HPI-I");
            phiLogger.debug("Detailed invalid resource: Practitioner Dr. John Doe with bad HPI-I 8003619999999999");

            probe.assertNoPhiInOperationalLogs("John Doe", "8003619999999999");
            assertThat(probe.getOperationalEvents()).hasSize(1);
            assertThat(probe.getOperationalEvents().get(0).getFormattedMessage()).contains("INVALID_IDENTIFIER");
            assertThat(probe.getPhiDiagnosticEvents()).isEmpty();
        }
    }
}
