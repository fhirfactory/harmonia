# Paradeigma Worked Examples & Test Fixtures `[EXAMPLE/REFERENCE]`

This document provides copy-pasteable code examples, test fixtures, and CLI workflows demonstrating how to utilize the Paradeigma simulation framework for testing, validation, and benchmarking.

---

## 1. Example 1: Deterministic Patient Generation & MLLP Dispatch `[EXAMPLE/REFERENCE]`

Demonstrates initializing a seeded generator, constructing an `ADT^A01` message, and transmitting it over standard MLLP:

```java
package net.fhirfactory.harmonia.paradeigma.example;

import net.fhirfactory.harmonia.paradeigma.common.generator.SeedRandom;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7MessageBuilders;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import org.hl7.fhir.r5.model.Patient;

public class MllpTransmissionExample {

    public static void main(String[] args) throws Exception {
        long seed = 42L;
        String harmoniaHost = "localhost";
        int mllpPort = 2575; // Pylai Inbound MLLP Port

        // Step 1: Initialize deterministic patient generator
        SyntheticPatientGenerator patientGen = new SyntheticPatientGenerator(seed);
        Patient patient = patientGen.generatePatient("sim-patient-001");
        
        String mrn = patient.getIdentifierFirstRep().getValue();
        String familyName = patient.getNameFirstRep().getFamily();
        String givenName = patient.getNameFirstRep().getGiven().get(0).getValue();

        System.out.println("Generated Synthetic Patient: " + givenName + " " + familyName + " (MRN: " + mrn + ")");

        // Step 2: Build standard HL7 v2.4 ADT^A01 frame
        String hl7Payload = Hl7MessageBuilders.buildAdtA01(
                "PAS", "HOSPITAL_A", "HARMONIA", "HIE",
                "CTRL-MSG-001", patient, "WARD-3A", "BED-01"
        );

        // Step 3: Transmit over standard MLLP TCP socket
        try (MllpClient client = new MllpClient(harmoniaHost, mllpPort)) {
            client.connect();
            System.out.println("Connected to Harmonia MLLP Ingress. Sending ADT^A01...");

            AckResult ack = client.sendMessage(hl7Payload);

            System.out.println("Received Synchronous HL7 ACK: " + ack.getAckCode());
            System.out.println("Referenced Control ID (MSA-2): " + ack.getReferencedControlId());
            
            if ("AA".equals(ack.getAckCode())) {
                System.out.println("Transmission successfully accepted downstream by Harmonia!");
            } else {
                System.err.println("Harmonia rejected message: " + ack.getTextMessage());
            }
        }
    }
}
```

---

## 2. Example 2: Governed Provider Registry Change Request with Test Probes `[EXAMPLE/REFERENCE]`

Demonstrates creating a practitioner with an Australian `HPI-I`, evaluating Themis authorization, processing through an Ergon, and asserting zero-PHI logging:

```java
package net.fhirfactory.harmonia.paradeigma.example;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.paradeigma.common.generator.PractitionerGenerator;
import net.fhirfactory.harmonia.paradeigma.common.logging.PhiLogTestProbe;
import net.fhirfactory.harmonia.paradeigma.common.logging.SecretLeakageAssertion;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ProviderRegistryWriteExampleTest {

    private final ThemisAuthorizer themisAuthorizer = ThemisAuthorizer.createDefault();
    private final FhirContext fhirContext = FhirContext.forR5();

    @Test
    void testGovernedProviderWriteWithLoggingProbe() throws Exception {
        long seed = 12345L;
        String correlationId = "corr-reg-test-01";

        // 1. Generate deterministic practitioner with Australian HPI-I
        PractitionerGenerator generator = new PractitionerGenerator(seed);
        Practitioner practitioner = generator.generateValid("pract-dr-bowman-01");
        String familyName = practitioner.getNameFirstRep().getFamily();
        String hpii = practitioner.getIdentifierFirstRep().getValue();
        String fhirJson = fhirContext.newJsonParser().encodeResourceToString(practitioner);

        // 2. Establish Provider Steward Security Context
        ThemisPrincipal steward = ThemisPrincipal.human("usr-steward-01", "steward@harmonia.local");
        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .requestingPrincipal(steward)
                .correlationId(correlationId)
                .grantedAuthorities(Set.of(ThemisAuthority.of("provider.change.submit"), ThemisAuthority.of("provider.read")))
                .build();

        ThemisResource target = ThemisResource.of("Practitioner", "pract-dr-bowman-01", "PROVIDER_REGISTRY");

        // 3. Evaluate Themis Ingress Authorization Gate
        ThemisAuthorizationRequest authReq = ThemisAuthorizationRequest.builder()
                .principal(steward)
                .authorities(context.grantedAuthorities())
                .action(ThemisAction.CREATE)
                .target(target)
                .context(context)
                .build();

        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authReq);
        assertThat(decision.isAllowed()).isTrue();

        // 4. Attach in-memory PHI logging probe to verify non-leakage
        try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {

            // Simulate ingress log entry
            org.slf4j.LoggerFactory.getLogger("net.fhirfactory.harmonia.pylai")
                    .info("Ingested Practitioner change request correlationId={}", correlationId);

            // Assert operational logs contain the correlation ID but ZERO clinical names or HPI-I
            probe.assertOperationalLogsContain(correlationId);
            probe.assertNoPhiInOperationalLogs(familyName, hpii);

            // Assert synthetic secrets (passwords, tokens) never leak
            probe.assertNoSecretsInAnyLog(SecretLeakageAssertion.getStandardSyntheticSecrets());
        }
    }
}
```

---

## 3. Example 3: Simulating Network Failure & Verifying Retry Behavior `[EXAMPLE/REFERENCE]`

Configuring the failure simulator to inject dropped connections and observing the client response:

```bash
# 1. Configure EMR simulator to drop 100% of TCP connections
curl -X POST http://localhost:8092/api/emr/config \
  -H "Content-Type: application/json" \
  -d '{"dropConnectionProbability": 1.0}'

# 2. Trigger an admission from PAS that attempts to fan out to EMR
curl -X POST http://localhost:8091/api/pas/patients/register \
  -H "Content-Type: application/json" \
  -d '{"patientId": "sim-fail-001"}'

# 3. Observe Artemis dead-letter queue metrics in Harmonia Iris Console
curl -s http://localhost:8090/api/operations/queues/DLQ | jq .

# 4. Reset EMR failure configuration back to healthy operation
curl -X POST http://localhost:8092/api/emr/config \
  -H "Content-Type: application/json" \
  -d '{"dropConnectionProbability": 0.0}'
```

---

## 4. Example 4: Testing Agora Collaboration & Room Linking `[EXAMPLE/REFERENCE]`

```java
package net.fhirfactory.harmonia.paradeigma.example;

import net.fhirfactory.harmonia.agora.api.model.*;
import net.fhirfactory.harmonia.agora.core.lifecycle.AgoraCollaborationLifecycleService;
import net.fhirfactory.harmonia.agora.core.reconciliation.AgoraMembershipReconciliationService;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AgoraScenarioExampleTest {

    @Test
    void testPatientSpaceProvisioningAndReconciliation(
            AgoraCollaborationLifecycleService lifecycleService,
            AgoraMembershipReconciliationService reconciliationService
    ) {
        String patientId = "pat-cardio-99";
        ThemisSecurityContext secContext = ThemisSecurityContext.systemPrincipal("service:agora");

        // 1. Request Patient Space Hierarchy
        AgoraSpaceRequest spaceRequest = new AgoraSpaceRequest(
                AgoraCollaborationLifecycleService.RESOURCE_TYPE_PATIENT,
                patientId,
                "Cardiology Inpatient Collaboration Space",
                secContext
        );

        AgoraPatientSpaceResponse spaceResponse = lifecycleService.createPatientSpace(spaceRequest);

        assertThat(spaceResponse.getSpaceId()).isNotNull();
        assertThat(spaceResponse.getChildRoomIds()).containsKeys(
                AgoraRoomType.STATISTICS,
                AgoraRoomType.TASKS,
                AgoraRoomType.DISCUSSION,
                AgoraRoomType.DIAGNOSTICS
        );

        // 2. Reconcile care team membership in the Discussion room
        String discussionRoomId = spaceResponse.getChildRoomIds().get(AgoraRoomType.DISCUSSION);
        Set<String> desiredClinicians = Set.of("@dr_smith:synapse", "@nurse_jones:synapse");

        AgoraReconciliationResult result = reconciliationService.reconcileRoomMembership(
                discussionRoomId, desiredClinicians, secContext
        );

        assertThat(result.getInvitedUserIds()).containsAll(desiredClinicians);
        assertThat(result.getKickedUserIds()).isEmpty();
    }
}
```

---

## 5. Example 5: Running ArchUnit Isolation Checks `[EXAMPLE/REFERENCE]`

Execute continuous architectural assertions directly via Maven:

```bash
# Execute only the Paradeigma isolation test suite
mvn test -pl paradeigma/paradeigma-test -am -Dtest="ParadeigmaIsolationArchitectureTest"

# Execute Agora isolation and Ponos decoupling test suite
mvn test -pl paradeigma/paradeigma-test -am -Dtest="AgoraIsolationArchitectureTest"
```
