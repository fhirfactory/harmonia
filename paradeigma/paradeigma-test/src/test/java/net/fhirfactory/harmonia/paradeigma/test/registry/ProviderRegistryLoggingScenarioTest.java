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

package net.fhirfactory.harmonia.paradeigma.test.registry;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import net.fhirfactory.harmonia.logging.PhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggerFactory;
import net.fhirfactory.harmonia.logging.PhiLoggingConfig;
import net.fhirfactory.harmonia.paradeigma.common.generator.PractitionerGenerator;
import net.fhirfactory.harmonia.paradeigma.common.logging.PhiLogTestProbe;
import net.fhirfactory.harmonia.paradeigma.common.logging.SecretLeakageAssertion;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validates the Harmonia PHI dual-gate logging policy and secret non-leakage during Provider Registry scenario execution.
 */
public class ProviderRegistryLoggingScenarioTest {

    private org.slf4j.Logger operationalLogger;
    private PhiLogger phiLogger;
    private Logger rootLogger;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.reset();
        PhiLoggerFactory.clearCacheForTesting();
        operationalLogger = LoggerFactory.getLogger("net.fhirfactory.harmonia.pylai.registry");
        phiLogger = PhiLoggerFactory.getLogger("ProviderRegistryWorkflow");
        rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        ((Logger) LoggerFactory.getLogger("ca.uhn.fhir")).setLevel(Level.WARN);
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.reset();
        PhiLoggerFactory.clearCacheForTesting();
    }

    @Test
    @DisplayName("Logging Matrix 1: INFO level + phi-enabled=false -> Operational logs emitted, PHI suppressed")
    void testLoggingMatrix1_InfoPhiDisabled() {
        PhiLoggingConfig.setPhiEnabled(false);
        rootLogger.setLevel(Level.INFO);

        Practitioner practitioner = new PractitionerGenerator(42L).generateValid("pract-log-1");
        String name = practitioner.getNameFirstRep().getFamily();
        String hpii = practitioner.getIdentifierFirstRep().getValue();

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.info("Processing change request for resource: Practitioner/{}", practitioner.getIdPart());
            phiLogger.debug("Diagnostic provider details: name={}, hpii={}", name, hpii);

            probe.assertNoPhiInOperationalLogs(name, hpii);
            assertThat(probe.getOperationalEvents()).hasSize(1);
            assertThat(probe.getPhiDiagnosticEvents()).isEmpty();
        }
    }

    @Test
    @DisplayName("Logging Matrix 2: DEBUG level + phi-enabled=false -> Operational DEBUG emitted, PHI suppressed")
    void testLoggingMatrix2_DebugPhiDisabled() {
        PhiLoggingConfig.setPhiEnabled(false);
        rootLogger.setLevel(Level.DEBUG);

        Practitioner practitioner = new PractitionerGenerator(42L).generateValid("pract-log-2");
        String name = practitioner.getNameFirstRep().getFamily();

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.debug("Pragma checkpoint reached: VALIDATING, id=pragma-log-2");
            phiLogger.debug("Diagnostic provider detail: name={}", name);

            probe.assertNoPhiInOperationalLogs(name);
            assertThat(probe.getOperationalEvents()).hasSize(1);
            assertThat(probe.getPhiDiagnosticEvents()).isEmpty();
        }
    }

    @Test
    @DisplayName("Logging Matrix 3: TRACE level + phi-enabled=false -> Operational TRACE emitted, PHI suppressed")
    void testLoggingMatrix3_TracePhiDisabled() {
        PhiLoggingConfig.setPhiEnabled(false);
        rootLogger.setLevel(Level.TRACE);

        Practitioner practitioner = new PractitionerGenerator(42L).generateValid("pract-log-3");
        String name = practitioner.getNameFirstRep().getFamily();

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.trace("Evaluating ergon activity seq-provider-registry-change-pipeline");
            phiLogger.trace("Diagnostic trace: practitioner={}", name);

            probe.assertNoPhiInOperationalLogs(name);
            assertThat(probe.getOperationalEvents()).hasSize(1);
            assertThat(probe.getPhiDiagnosticEvents()).isEmpty();
        }
    }

    @Test
    @DisplayName("Logging Matrix 4: DEBUG level + phi-enabled=true -> PHI emitted to org.harmonia.phi with Marker PHI")
    void testLoggingMatrix4_DebugPhiEnabled() {
        PhiLoggingConfig.setPhiEnabled(true);
        rootLogger.setLevel(Level.DEBUG);

        Practitioner practitioner = new PractitionerGenerator(42L).generateValid("pract-log-4");
        String family = practitioner.getNameFirstRep().getFamily();
        String given = practitioner.getNameFirstRep().getGivenAsSingleString();

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.debug("Starting Practitioner validation: id=pract-log-4");
            phiLogger.debug("Diagnostic Practitioner profile: family={}, given={}", family, given);

            probe.assertNoPhiInOperationalLogs(family, given);
            probe.assertPhiPresentInDiagnosticLogs(family, given);
            probe.assertPhiMarkerAttachedToDiagnosticLogs();
        }
    }

    @Test
    @DisplayName("Logging Matrix 5: TRACE level + phi-enabled=true -> Full PHI diagnostics emitted with Marker PHI")
    void testLoggingMatrix5_TracePhiEnabled() {
        PhiLoggingConfig.setPhiEnabled(true);
        rootLogger.setLevel(Level.TRACE);

        Practitioner practitioner = new PractitionerGenerator(42L).generateValid("pract-log-5");
        String hpii = practitioner.getIdentifierFirstRep().getValue();

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.trace("Commit transaction start");
            phiLogger.trace("Full diagnostic payload: HPI-I={}", hpii);

            probe.assertNoPhiInOperationalLogs(hpii);
            probe.assertPhiPresentInDiagnosticLogs(hpii);
            probe.assertPhiMarkerAttachedToDiagnosticLogs();
        }
    }

    @Test
    @DisplayName("Secret Leakage Protection: Authentication tokens and passwords never appear in any log")
    void testSecretLeakageProtection() {
        PhiLoggingConfig.setPhiEnabled(true);
        rootLogger.setLevel(Level.TRACE);

        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
            operationalLogger.info("Bearer token authenticated for principal steward-01: bearer=***SANITIZED***");
            phiLogger.debug("Diagnostic log event for Practitioner Dr. Frank Bowman");

            probe.assertNoSecretsInAnyLog(SecretLeakageAssertion.getStandardSyntheticSecrets());
        }
    }
}
