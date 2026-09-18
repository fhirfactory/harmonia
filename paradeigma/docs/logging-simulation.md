# Harmonia Paradeigma — PHI-Aware Logging Simulation Guide

### 1. The Harmonia Dual-Gate PHI Logging Policy

To prevent accidental exposure of Patient Health Information (PHI) in operational log sinks, Harmonia enforces a strict **dual-gate policy**:

| Log Level | `harmonia.logging.phi-enabled=false` (Default) | `harmonia.logging.phi-enabled=true` (Diagnostic) |
|---|---|---|
| **INFO / WARN / ERROR** | Operational logs only (PHI strictly forbidden) | Operational logs only (PHI strictly forbidden) |
| **DEBUG** | Operational DEBUG only (PHI strictly forbidden) | PHI permitted to `org.harmonia.phi` with Marker `PHI` |
| **TRACE** | Operational TRACE only (PHI strictly forbidden) | Full PHI diagnostics to `org.harmonia.phi` with Marker `PHI` |

**Dual-Gate Rule**: PHI diagnostic logging requires **BOTH**:
1. `harmonia.logging.phi-enabled=true` (or system property `HARMONIA_LOGGING_PHI_ENABLED=true`)
2. DEBUG or TRACE log level enabled on the logger.

DEBUG or TRACE alone **MUST NEVER** expose PHI.

---

### 2. Using `PhiLogTestProbe` in Tests

`PhiLogTestProbe` is an in-memory, auto-closeable Logback test probe in `net.fhirfactory.harmonia.paradeigma.common.logging`:

```java
try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
    // 1. Execute system action or scenario
    gatewayController.createResource("Practitioner", fhirJson, correlationId, "pas", "steward", null);

    // 2. Assert no PHI appears in operational log appenders
    probe.assertNoPhiInOperationalLogs("Dr. Frank Bowman", "8003610000000001");

    // 3. Assert PHI appears in diagnostic destination when dual-gate is active
    probe.assertPhiPresentInDiagnosticLogs("Dr. Frank Bowman");
    probe.assertPhiMarkerAttachedToDiagnosticLogs();

    // 4. Assert authentication secrets and passwords are never logged
    probe.assertNoSecretsInAnyLog(SecretLeakageAssertion.getStandardSyntheticSecrets());
}
```

---

### 3. Preventing Secret Leakage

Authentication credentials must **NEVER** appear in any logging destination, regardless of PHI diagnostic mode. `SecretLeakageAssertion` provides synthetic sentinels for automated validation:

- Synthetic OAuth access & refresh tokens (`eyJhbGci...`)
- Bearer tokens
- API keys (`harmonia_sec_live_...`)
- Database passwords (`pg_super_secret...`)
- User credentials & passwords
- Artemis message broker credentials
- Private keys (`-----BEGIN PRIVATE KEY-----`)

---

### 4. Complete Worked Example: Registry + Security + Logging

The following worked example demonstrates initializing a seeded generator, setting up a security context, executing a change request, and verifying multi-channel logging:

```java
@Test
void completeWorkedExample() throws Exception {
    long seed = 42L;
    String correlationId = "corr-exemplar-001";

    // Step 1: Initialize deterministic synthetic generator
    PractitionerGenerator practGen = new PractitionerGenerator(seed);
    Practitioner practitioner = practGen.generateValid("pract-dr-bowman-42");
    String familyName = practitioner.getNameFirstRep().getFamily();
    String hpii = practitioner.getIdentifierFirstRep().getValue();
    String fhirJson = FhirContext.forR5().newJsonParser().encodeResourceToString(practitioner);

    // Step 2: Establish Provider Steward Security Context
    SecurityScenarioContext steward = SecurityScenarioContext.providerSteward();
    ThemisResource resource = ThemisResource.of(
        "Practitioner",
        "pract-dr-bowman-42",
        "PROVIDER_REGISTRY",
        Set.of(ThemisSecurityLabel.of("PROVIDER_REGISTRY"))
    );

    // Step 3: Verify Themis Ingress Authorization
    ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
        .principal(steward.principal())
        .authorities(steward.grantedAuthorities())
        .action(ThemisAction.CREATE)
        .target(resource)
        .context(steward.securityContext())
        .build();
    ThemisAuthorizationDecision decision = evaluator.authorize(authRequest);
    assertThat(decision.isAllowed()).isTrue();

    // Step 4: Enable Dual-Gate PHI Logging & Attach Test Probe
    PhiLoggingConfig.setPhiEnabled(true);
    try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
        
        // Step 5: Submit Change Request via Pylai REST Gateway
        ResponseEntity<String> postResp = gatewayController.createResource(
            "Practitioner",
            fhirJson,
            correlationId,
            "pas",
            steward.principal().principalId(),
            null
        );
        assertThat(postResp.getStatusCode().value()).isEqualTo(202);

        // Step 6: Process via Ponos / Ergon & Commit to Mnemosyne
        String location = postResp.getHeaders().getLocation().toString();
        String pragmaId = location.substring(location.lastIndexOf('/') + 1);
        Pragma pragma = pragmaCache.get(pragmaId);
        practitionerErgon.processErgon(pragma, camelExchange);
        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Step 7: Validate Synchronous Read & Search
        ResponseEntity<String> getResp = gatewayController.getResource("Practitioner", "pract-dr-bowman-42", null);
        assertThat(getResp.getStatusCode().value()).isEqualTo(200);

        // Step 8: Assert PHI Logging Policy & Isolation
        probe.assertNoPhiInOperationalLogs(familyName, hpii);
        probe.assertPhiPresentInDiagnosticLogs(familyName, hpii);
        probe.assertPhiMarkerAttachedToDiagnosticLogs();

        // Step 9: Assert Secret Non-Leakage
        probe.assertNoSecretsInAnyLog(SecretLeakageAssertion.getStandardSyntheticSecrets());
    }
}
```
