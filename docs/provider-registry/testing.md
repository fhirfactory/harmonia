# Harmonia Provider Registry — Testing Strategy & Verification

### 1. Verification Strategy

The Provider Registry test suite spans five distinct layers of validation:

1. **Unit Tests**:
   - `calliope`: Canonical model tests for `ProviderRegistryConstants` and `ProviderRegistryChangePragma`.
   - `energeia/erga`: Unit tests for each of the 7 Ergon processing activities verifying business rules, duplicate detection, and automated approval.
   - `pylai/pylai-fhir-registry`: CapabilityStatement structure verification and RBAC authorization unit tests.
2. **Provider & Persistence Tests**:
   - `hestia/mnemosyne-clinical`: Multi-parameter search queries across all 7 resources and referential validator tests (`ProviderRegistrySearchTest`, `ProviderRegistryReferenceValidatorTest`).
3. **Gateway REST Controller Tests**:
   - `pylai/pylai-fhir-registry`: MockMvc tests verifying synchronous reads, searchsets, asynchronous `POST`/`PUT` yielding `202 Accepted` with `Location: /Task/{id}`, and `GET /Task/{id}` status polling.
4. **End-to-End Integration Tests**:
   - `paradeigma/paradeigma-test`: Complete acceptance scenario creating a full interconnected provider hierarchy (`Organization` -> `Endpoint` -> `Location` -> `HealthcareService` -> `Practitioner` -> `PractitionerRole` -> `Group`), executing through Ponos, and querying via synchronous search (`ProviderRegistryEndToEndWriteTest`).
5. **Resilience & Concurrency Tests**:
   - `paradeigma/paradeigma-test`:
     - `ProviderRegistryReferentialRejectionTest`: Verifies rejection and `OperationOutcome` generation on dangling references.
     - `ProviderRegistryConcurrencyTest`: Verifies optimistic lock conflict detection on stale `If-Match` updates.
     - `ProviderRegistryRestartRecoveryTest`: Verifies resumption of in-flight change requests across engine restarts without duplicate creation.

### 2. Executing the Test Suite

```bash
# Run all Provider Registry tests across the entire Harmonia codebase
mvn clean test

# Run Paradeigma Provider Registry integration tests
mvn test -pl paradeigma/paradeigma-test -Dtest="ProviderRegistry*"
```
