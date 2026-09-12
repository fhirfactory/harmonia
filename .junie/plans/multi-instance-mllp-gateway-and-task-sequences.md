---
sessionId: session-260907-134950-15ho
---

# Requirements

### Overview & Goals
The HIE platform requires automated patient identity extraction, normalization, and updating during workflow execution, as well as comprehensive patient demographics extraction from ADT event messages to enrich and maintain accurate Patient resources.

The goal is to:
1. Implement a new activity class `PatientDemographicsUpdate` in package `net.fhirfactory.hie.taskprocessors.patient.demographics` extending `TaskProcessingActivity`, capable of extracting demographic information (names, DOB, gender, marital status, language, race, ethnicity, contacts/next-of-kin, addresses, telecoms, general practitioners, managing organizations) from any incoming ADT event trigger based message and updating the corresponding Patient resource within the HIE.
2. Add `PatientDemographicsUpdate` to the same `TaskSequence` as `PatientIdentityUpdate` (`seq-patient-identity-pipeline`), ensuring that `PatientDemographicsUpdate` executes AFTER `PatientIdentityUpdate`.

### Scope
- **In Scope**:
  - Implementation of `PatientDemographicsUpdate` in `workflow-services/task-processors` under `net.fhirfactory.hie.taskprocessors.patient.demographics`.
  - Comprehensive ADT demographic extraction (PID segment fields including race, ethnicity, language, marital status, religion, deceased indicators; NK1 next-of-kin contacts; PD1 primary care providers and facilities).
  - Merging and updating existing FHIR `Patient` resources produced by upstream activities (like `PatientIdentityUpdate`) without losing identity or demographic data.
  - Integration with `TaskSequenceLoader` to include `PatientDemographicsUpdate` in `seq-patient-identity-pipeline` sequentially following `PatientIdentityUpdate`.
  - Comprehensive unit and integration test coverage.
- **Out of Scope**:
  - UI frontend modifications for manual patient record editing.

# Execution Steps

### ✓ Step 1: Implement PatientDemographicsUpdate activity in task-processors
- Create `PatientDemographicsUpdate.java` in `net.fhirfactory.hie.taskprocessors.patient.demographics` extending `TaskProcessingActivity`.
- Implement ADT demographic parsing (PID race, language, marital status, deceased date/time, religion, ethnicity, addresses, phones/telecoms; NK1 contacts; PD1 providers).
- Implement intelligent merging with existing FHIR `Patient` resource payload or constructing a new `Patient` resource.
- Set exchange headers and updated `Patient` JSON body.

### ✓ Step 2: Update TaskSequenceLoader to include PatientDemographicsUpdate after PatientIdentityUpdate
- Update `TaskSequenceLoader.seedDefaultSequences(...)` so `seq-patient-identity-pipeline` includes both `patient-identity-update` and `patient-demographics-update` in sequential order.

### ✓ Step 3: Implement unit and integration tests
- Create `PatientDemographicsUpdateTest` in `task-processors`.
- Update `TaskSequenceLoaderTest` and `TaskSequenceTest` to verify sequence chaining (`PatientIdentityUpdate` -> `PatientDemographicsUpdate`).

### ✓ Step 4: Execute full test verification across all project modules
- Run `mvn clean test` across all 14 project modules.