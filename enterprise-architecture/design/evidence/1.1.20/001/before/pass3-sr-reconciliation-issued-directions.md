# Harmonia 1.1.20 — Pass 3 SR Reconciliation

## Goal

Incorporate the Solution Architect's confirmed SR-001, SR-004, SR-015, SR-016 and SR-017 decisions into the current Pass3/012 offline capability catalogue.

This is an **accepted-decision incorporation activity**, not a new architecture-discovery or adjudication exercise.

## Expected Outcome

Produce a revised offline capability catalogue and adjudication workbook that:

1. Faithfully incorporate all five confirmed SR decisions.
2. Preserve all previously incorporated XD-001–XD-028 adjudications.
3. Preserve the catalogue-wide AR-001 and AR-002 normalisation.
4. Maintain existing accepted identities, relationships and responsibilities except where an SR decision explicitly authorises modification.
5. Retain historical traceability for retired or transferred elements.
6. Provide reproducible validation and reconciliation evidence.

The resulting candidate must be suitable for Solution Architect review before governed Continuum incorporation.

## Context

The incorporation baseline is the completed Pass3/012 package:

- `Harmonia_1.1.20_Candidate_017_Pass3_012_XD_Consolidated_Catalogue.md`
- `Harmonia_1.1.20_Pass3_012_XD_Adjudication_Workbook.xlsx`
- Associated reconciliation report and source-result manifest.

Locate these using the repository's actual evidence paths and verify their provenance.

The reported Pass3/012 population is:

| Level | Population |
|---|---:|
| CT1 | 24 |
| CT2 | 145 |
| CT3 | 586 |
| Features | 2,941 |

These are baseline reference counts, not mandatory target counts following authorised consolidation.

The five SR discrepancies were identified during Pass3/012 reconciliation and subsequently reviewed individually with the Solution Architect.

Their dispositions are accepted.

The existing HCRL is `1.1.20/001`, DR-5.

## Constraints

### Architectural authority

The governed Markdown Architecture Continuum under `enterprise-architecture/continuum/` remains authoritative.

The Pass3/012 package is an offline working candidate, not a published architectural baseline.

Follow the repository's current architectural governance and evidence requirements.

Do not load the full ArchiMate XML model into AI context.

### Preserve accepted decisions

Do not reopen accepted SR or XD adjudications.

Preserve the architectural distinctions established in XD-001–XD-028.

Preserve AR-001: canonical Features describe capabilities, responsibilities and observable outcomes, not KPI calculations.

Preserve AR-002: canonical descriptions do not contain discovery or verification status annotations.

Maintain measurement criteria, provenance and assurance standing separately.

### Structural discipline

Do not renumber surviving identifiers.

Do not introduce replacement Features merely to maintain Feature counts.

Do not add new CT3 capabilities unless expressly authorised.

Do not silently discard distinct obligations during consolidation.

Retired identifiers must remain traceable in historical evidence but must not persist as active capabilities.

### Scope and release control

This activity is restricted to the five accepted SR reconciliations and their directly necessary consequences.

Do not initiate FR adjudications, further capability discovery or unrelated catalogue modifications.

Do not change the governed Continuum, publish a release, or commit/push.

Report genuine conflicts rather than inventing architectural decisions.

## Authorised Activity / Scope

### SR-001 — Clinical Handover Acceptance

**Disposition: IMPLEMENT ACCEPTED CONSOLIDATION.**

Retain:

`B05.03.01 — Clinical Handover Acceptance`

Consolidate into it the responsibilities of:

`B05.03.04 — Handover Acceptance & Evidence`

Preserve the distinct obligations concerning:

- Accountable receiving party and appropriate identity assurance.
- Explicit acceptance of clinical responsibility, distinguished from receipt.
- Transfer context and outstanding care obligations.
- Acceptance timing, rejection and escalation.
- Handover evidence and traceability.

Retire `B05.03.04` from the active catalogue.

Preserve its original identifier and source Feature criteria in the historical reconciliation evidence.

Maintain independent capabilities:

- `E06.08.05` — governed operational responsibility transfer.
- `B07.04.05` — human operational handover.

Do not conflate clinical accountability with operational work allocation.

### SR-004 — Parcel Handoff and Routing

**Disposition: KEEP DISTINCT — REFINE WORDING.**

Retain:

`E06.03.01 — Governed Parcel Handoff & Accountability`

This capability governs the transfer of PetasosParcel responsibility between processing participants, including handoff state, accountability, outcome correlation and applicable audit evidence.

Retain:

`E06.03.03 — Routing Decision Implementation`

This capability applies governed routing decisions, including destination selection and preservation of routing decision context.

Distinguish:

1. Destination selection.
2. Delivery attempt.
3. Accepted handoff.

A successful routing decision does not establish that responsibility was accepted by the destination.

Preserve the existing Features and identifiers.

No structural change or Feature migration is authorised.

### SR-015 — Ergon Outcomes and Asynchronous Activities

**Disposition: KEEP DISTINCT — REFINE WORDING.**

Retain and refine:

`E06.08.07 — Ergon Output Processing & Continuation`

Responsible for processing Ergon execution outcomes and determining their consequences for the enclosing Task, including continuation, subsequent work, failure handling and outstanding activity correlation.

Retain and refine:

`E06.09.07 — Long-Duration Asynchronous Activity Management`

Responsible for maintaining continuing activities within their applicable Task context, including activity state, correlation, progress and eventual outcomes.

Preserve the distinctions:

- Ergon completion is not necessarily activity completion.
- Activity completion is not necessarily Task completion.
- Task completion is not necessarily authoritative information commitment.
- Long-duration activities do not automatically require additional Tasks.
- Activity semantics are independent of the particular Stimulus, Effector or implementation mechanism.

Preserve existing Features and identifiers.

No additional capabilities are authorised.

### SR-016 — Research Information Protection

**Disposition: KEEP DISTINCT — REFINE WORDING.**

Retain and refine:

`B13.04.02 — Research Information De-identification & Minimisation`

Research-purpose information preparation, de-identification, pseudonymisation, minimisation, preservation of research utility and management of re-identification risks.

Retain and refine:

`B14.06.01 — Participant-Contextual Information Protection`

Context-sensitive protection of participant information during authorised access, presentation and disclosure.

Preserve the distinctions:

- Research de-identification does not automatically authorise unrestricted disclosure.
- Participant-contextual protection does not necessarily require de-identification.
- Research utility and re-identification risks remain independently relevant.
- Consent, authorisation and disclosure conditions remain governed.
- Business Capability definitions do not imply that Harmonia performs every underlying transformation or makes every disclosure decision.

Preserve existing Features and identifiers.

No structural changes or additional Features are authorised.

### SR-017 — FHIR RESTful Client and Server Responsibilities

**Disposition: KEEP DISTINCT — CORRECT NARRATIVE CORRESPONDENCE.**

Retain:

`E03.06.04 — FHIR RESTful Server Interaction Support`

Harmonia subsystems act as FHIR RESTful servers, exposing supported interactions to external FHIR clients.

Retain:

`E03.06.05 — FHIR RESTful Client Interaction Support`

Harmonia subsystems act as FHIR RESTful clients, consuming services exposed by external FHIR servers.

Correct the reversed client/server correspondence in the adjudication narrative.

Verify that existing Feature definitions align with their proper client or server responsibilities.

Do not swap identifiers, migrate Features or change containment relationships.

## Reconciliation and Validation

Produce the next sequential offline working candidate without modifying the Pass3/012 inputs.

Validate:

1. All five SR decisions are faithfully incorporated.
2. `B05.03.04` has been retired from the active hierarchy.
3. All unique obligations from its original Features remain represented or explicitly qualified.
4. Existing operational handover responsibilities remain distinct.
5. All previously accepted XD dispositions remain intact.
6. AR-001 and AR-002 remain satisfied.
7. No unintended Feature or CT3 identifiers have changed.
8. Measurement criteria and historical evidence remain preserved.
9. No unrelated catalogue changes have occurred.
10. Before/after population counts reconcile with the authorised structural modifications.

Do not assume a numerical Feature delta for SR-001 before examining the actual source and target Features.

Report whether the existing workbook can be loaded and validated using available OOXML tooling. Retain any outstanding external-office-tool qualification unless genuinely resolved.

## Report

Use the existing governed evidence convention under:

`enterprise-architecture/design/evidence/1.1.20/<iteration>/`

Retain instructions and original inputs under `before/` and execution results under `after/`, following repository governance.

The execution report must contain:

- Exact Pass3/012 source artefacts and hashes.
- Per-SR incorporation results.
- SR-001 source-to-target Feature reconciliation.
- Retired-identifier traceability.
- Before/after capability and Feature counts.
- Description and measurement-criteria preservation evidence.
- XD and AR regression results.
- Automated validation results and limitations.
- Outstanding qualifications and unresolved conflicts.
- Final candidate and workbook locations.

Record factual provenance timestamps only where available. Do not fabricate missing metadata.

Update HCRL `1.1.20/001`, DR-5 with the factual execution outcome and references to the resulting evidence, subject to existing governance.

## Completion Boundary

Stop after producing the revised offline candidate, updated workbook and reconciliation evidence.

Do not publish to the governed Architecture Continuum.

Do not close HCRL `1.1.20/001`.

Do not commit or push.

Present the completed results and remaining qualifications for Solution Architect review.
