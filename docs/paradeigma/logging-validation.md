# Paradeigma PHI Logging Validation & Test Probes `[IMPLEMENTED]`

This document describes the validation of Harmonia's **Dual-Gate PHI Logging Policy** and the usage of `PhiLogTestProbe` and `SecretLeakageAssertion` within Paradeigma test suites.

---

## 1. Harmonia Dual-Gate PHI Logging Policy `[IMPLEMENTED]`

To guarantee that Protected Health Information (PHI) is never leaked into non-clinical log sinks (such as Elasticsearch, Datadog, CloudWatch, or centralized syslog), Harmonia enforces an architectural **dual-gate logging model**:

| Log Level | `harmonia.logging.phi-enabled=false` (Production Default) | `harmonia.logging.phi-enabled=true` (Diagnostic Investigation) |
| :--- | :--- | :--- |
| **`INFO` / `WARN` / `ERROR`** | Operational logs only (**PHI strictly forbidden**). Identifiers (MRN, UUIDs) allowed; patient names, DOB, addresses, and clinical notes omitted. | Operational logs only (**PHI strictly forbidden**). |
| **`DEBUG`** | Operational DEBUG only (**PHI strictly forbidden**). | PHI permitted exclusively to logger `org.harmonia.phi` with SLF4J Marker `PHI`. |
| **`TRACE`** | Operational TRACE only (**PHI strictly forbidden**). | Detailed diagnostic trace with full clinical payloads routed to isolated PHI log appender. |

### The Dual-Gate Invariant
Diagnostic logging of clinical context requires **BOTH** gates to be simultaneously open:
1. System property or environment variable `harmonia.logging.phi-enabled=true` (or `HARMONIA_LOGGING_PHI_ENABLED=true`).
2. Logging level set to `DEBUG` or `TRACE` on the target logger.

> **CRITICAL RULE**: Enabling `DEBUG` or `TRACE` alone without `phi-enabled=true` **MUST NEVER** emit PHI.

---

## 2. In-Memory Verification with `PhiLogTestProbe` `[IMPLEMENTED]`

`PhiLogTestProbe` (`net.fhirfactory.harmonia.paradeigma.common.logging.PhiLogTestProbe`) is an auto-closeable, in-memory Logback appender that attaches to the logging root during test execution:

```java
@Test
void assertZeroPhiLeakageUnderDefaultLogging() throws Exception {
    long seed = 42L;
    SyntheticPatientGenerator gen = new SyntheticPatientGenerator(seed);
    Patient patient = gen.generatePatient("pat-001");
    String familyName = patient.getNameFirstRep().getFamily();
    String mrn = patient.getIdentifierFirstRep().getValue();

    // Attach in-memory test probe
    try (PhiLogTestProbe probe = PhiLogTestProbe.startCapture()) {
        
        // Execute clinical message processing pipeline
        mllpGateway.processIncomingMessage(sampleHl7Message);

        // 1. Assert operational logs contain zero patient names or clinical values
        probe.assertNoPhiInOperationalLogs(familyName);

        // 2. Assert operational logs DO contain correlation IDs and opaque UUIDs
        probe.assertOperationalLogsContain("pat-001");

        // 3. Assert credentials, tokens, and private keys never leak
        probe.assertNoSecretsInAnyLog(SecretLeakageAssertion.getStandardSyntheticSecrets());
    }
}
```

### Core Assertion Methods
- **`assertNoPhiInOperationalLogs(String... sensitiveTerms)`**: Asserts that none of the provided search strings appear in the standard operational log appender.
- **`assertPhiPresentInDiagnosticLogs(String... sensitiveTerms)`**: When dual-gate is enabled, asserts that the clinical payload was routed to the isolated `org.harmonia.phi` destination.
- **`assertPhiMarkerAttachedToDiagnosticLogs()`**: Verifies that all diagnostic log entries carry the SLF4J Marker `PHI` for downstream log router segregation.
- **`assertNoSecretsInAnyLog(Collection<String> secretSentinels)`**: Scans all captured logs across all destinations to confirm complete absence of sensitive authentication material.

---

## 3. Secret Leakage Non-Negotiable Invariant `[IMPLEMENTED]`

Unlike PHI (which may be emitted to isolated clinical sinks for diagnostic troubleshooting), **credentials and secrets must NEVER be logged under any circumstance**, even when dual-gate is active.

`SecretLeakageAssertion` provides synthetic sentinels for automated test verification:

| Secret Category | Synthetic Sentinel Pattern | Target Systems |
| :--- | :--- | :--- |
| **Bearer Tokens** | `eyJhbGciOi...` (synthetic JWTs) | Pylai REST, Themis, Matrix Synapse |
| **API Keys** | `harmonia_sec_live_...` | External system integrations |
| **Database Passwords** | `pg_super_secret_...` | PostgreSQL, HAPI FHIR JPA |
| **Message Broker Credentials**| `artemis_secret_pass_...` | ActiveMQ Artemis queues |
| **Private Keys** | `-----BEGIN PRIVATE KEY-----` | TLS termination and mutual auth |

---

## 4. Automated Logging Scenarios (`paradeigma-test`) `[IMPLEMENTED]`

Logging policies are continuously asserted in CI via:
- **`PhiLoggingTestProbeTest`**: Verifies probe mechanics, marker filtering, and secret leakage detection.
- **`ProviderRegistryLoggingScenarioTest`**: Validates that Practitioner and PractitionerRole writes emit diagnostic traces with Marker `PHI` only when dual-gate is active, and operational entries omit names and AHPRA numbers.
