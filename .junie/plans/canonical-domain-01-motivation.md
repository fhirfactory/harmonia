---
sessionId: session-261004-131010-16sb
---

# Requirements

### Overview & Goals

Domain 01 — Motivation has been reviewed against the frozen Motivation model. The documentation structure, 13-file decomposition, Motivation model, orientation-view semantics, axiom structure, and foundational requirements are approved. This task is a **tightly scoped wording and classification cleanup only**.

The objective is to refine the canonical documentation under `docs/markdown/01-motivation/` to:
1. Preserve the frozen Motivation model and approved architectural relationships without redesign or expansion.
2. Remove implementation and protocol prescriptions from Motivation assessments (specifically False Acceptance and Concurrency Contention).
3. Keep the security assessment (Implicit Perimeter Trust) strictly focused on problem diagnosis rather than solution definitions (zero-trust, default-deny).
4. Excise unnecessarily absolute or dramatic rhetorical claims (e.g. "catastrophic", "lethal", "zero-tolerance", "absolute certainty", "skyrocketing", "exponential contention") in favor of precise architectural language while preserving essential clinical context.
5. Tighten external-authority statements so they represent sources of legal, regulatory, and standards obligations without asserting detailed technical mechanism mandates.
6. Correct downstream requirement status semantics in `master-requirements-catalogue.md`, retaining `Accepted` status for Domain 01 foundational requirements while classifying un-reconciled downstream requirements as `Candidate`.
7. Preserve the distinction between Security Architecture requirements (Domain 08) and formal architectural guardrails (Domain 13).
8. Perform rigorous validation and generate a formal completion report under `.junie/reports/YYYY-MM-DD-domain-01-final-cleanup.md`.

### Scope

#### In Scope
- Precision edits to canonical files under `docs/markdown/01-motivation/`:
  - `drivers-assessments/assessments.md`: Purge protocol prescriptions (HL7 codes, MLLP, volatile queues) from normative assessment; apply agreed technology-neutral concurrency definition; remove zero-trust/default-deny solution leakage from implicit perimeter trust.
  - `stakeholders/enterprise-stakeholders.md`: Replace dramatic/absolute claims ("catastrophic systemic failures", "absolute certainty") with precise architectural terms.
  - `stakeholders/external-authorities.md`: Characterize external bodies strictly as sources of constraint; remove assertions that authorities mandate specific cryptographic, persistence, logging, or access-gating mechanisms; replace "zero-tolerance" phrasing.
  - `drivers-assessments/drivers.md`: Tone down dramatic rhetoric ("catastrophic clinical harm", "lethal medication doses") while retaining clear clinical gravity.
  - `goals-outcomes/strategic-goals.md`: Replace dramatic phrasing ("catastrophic vulnerability") with rigorous architectural language.
  - `orientation-view.md`: Refine textual narrative in Thread 3 to remove dramatic phrasing; leave the approved frozen Mermaid diagram untouched.
  - `requirements-constraints/foundational-requirements.md`: Refine explanatory text ("fatal vulnerability", "catastrophic window") while strictly preserving the normative wording of REQ-FND-001 through REQ-FND-004.
  - `requirements-constraints/external-constraints.md`: Ensure external constraints establish existence and architectural significance without over-specifying compliance mechanisms.
  - `requirements-constraints/master-requirements-catalogue.md`: Reclassify un-reconciled downstream requirements (CORE-001, CORE-003, SEC-001, SEC-002, SEC-010, PROV-001, INT-TRANS-001, APP-SEP-001, DIR-001..006) from `Accepted` to `Candidate`, while keeping Domain 01 foundational items `Accepted`.
- Authoring the final execution report: `.junie/reports/YYYY-MM-DD-domain-01-final-cleanup.md`.

#### Out of Scope
- Redesigning, expanding, reinterpreting, or reorganizing the Domain 01 architecture.
- Modifying the frozen Motivation model (6 enterprise stakeholders, external authorities, 7 drivers, 5 assessments, 6 goals, 3 outcomes, AX-01..11, AX-13..16, AX-12 reclassification, 4 foundational requirements, 3 external constraint categories).
- Altering the approved 7-thread Motivation Orientation View Mermaid diagram.
- Modifying downstream domains (02 through 13), LaTeX, ODT, PDF, Pandoc tooling, publication pipelines, or repository source code.
- Prematurely establishing formal Domain 13 governance guardrails.

### User Stories

- **As a Systems Architect**, I want Motivation assessments to remain protocol- and technology-neutral so that the platform's motivational foundation diagnoses systemic hazards without predetermining downstream implementation choices.
- **As a Security & Governance Architect**, I want security assessments separated from security solutions and downstream guardrails cleanly demarcated so that policy boundaries and technical requirements remain distinct.
- **As a Downstream Domain Owner**, I want downstream requirements in the master catalogue marked as `Candidate` until formally reconciled within their owning domains, avoiding premature status freezing.
- **As a Technical Reader or Agent**, I want sober, precise architectural terminology throughout Domain 01 so that clinical risk is communicated with professional gravity rather than hyperbolic rhetoric.

### Functional Requirements

1. **Technology-Neutral False Acceptance**:
   - In `drivers-assessments/assessments.md`, express the assessment purely as the problem of positively accepting an event before crossing the required durable acceptance boundary.
   - Remove HL7 acknowledgement codes, MLLP/TCP behaviour, and volatile process queues from the normative assessment text.
   - Retain HL7 `AA` / `AE` behavior strictly as a clearly labeled downstream Integration Architecture example.
   - Preserve normative requirement REQ-FND-001 verbatim.
2. **Technology-Neutral Concurrency Processing**:
   - In `drivers-assessments/assessments.md`, replace implementation-leaning text with the agreed technology-neutral assessment:
     > "Requiring central synchronous persistence interaction for every unit of active processing can introduce contention, latency, unnecessary I/O and coordination bottlenecks that limit concurrent throughput and scalability."
   - Eliminate claims that lock contention escalates "exponentially" or that I/O wait times "skyrocket".
3. **Separation of Security Assessment from Security Solution**:
   - In `drivers-assessments/assessments.md` (Assessment 4: Implicit Perimeter Trust), diagnose only the hazard of assuming internal segments or callers are trusted by default.
   - Remove assertions that the assessment itself adheres to or requires a "zero-trust, default-deny model".
   - Reference AX-07 as the governing principle from which downstream requirements (e.g. `SEC-001`, `SEC-002`) derive default-deny enforcement.
4. **Excise Rhetorical and Hyperbolic Language**:
   - Review and replace overstated dramatic terms across all 13 canonical documents with precise architectural equivalents:
     - "catastrophic risk of unrecoverable data loss" -> "severe risk of unrecoverable data loss"
     - "catastrophic clinical harm" -> "severe clinical harm"
     - "lethal medication doses" -> "contraindicated or fatal medication doses"
     - "catastrophic vulnerability" / "fatal vulnerability" -> "critical vulnerability"
     - "catastrophic window" -> "vulnerable operational window"
     - "catastrophic systemic failures" -> "major systemic failures"
     - "Absolute certainty" -> "Deterministic certainty" or "High-assurance tracking"
     - "zero-tolerance standards" -> "strict regulatory standards"
     - "escalates exponentially" -> "increases substantially under load"
     - "skyrocket" -> "grow significantly"
5. **Tighten External-Authority Characterisation**:
   - In `stakeholders/external-authorities.md` and `requirements-constraints/external-constraints.md`, describe external authorities strictly as sources of applicable statutory, identifier, privacy, or interoperability obligations.
   - Eliminate claims that authorities mandate specific cryptographic algorithms, identifier-resolution mechanisms, logging implementations, persistence mechanisms, or access-gating implementations.
6. **Correct Downstream Requirement Status Semantics**:
   - In `requirements-constraints/master-requirements-catalogue.md`, update downstream requirements (`CORE-001`, `CORE-003`, `SEC-001`, `SEC-002`, `SEC-010`, `PROV-001`, `INT-TRANS-001`, `APP-SEP-001`, `DIR-001..006`) to `Candidate`.
   - Maintain `Accepted` status for the four Domain 01 foundational requirements (`REQ-FND-001..004`) and three external constraint categories (`CST-EXT-001..003`).
7. **Security Architecture vs Governance Guardrail Demarcation**:
   - Maintain Zero-PHI logging as a downstream Security Architecture requirement/constraint in Domain 08.
   - Confirm that formal architectural guardrail governance is deferred to Domain 13.
8. **Completion Reporting**:
   - Produce a structured completion report at `.junie/reports/YYYY-MM-DD-domain-01-final-cleanup.md` containing all required sections.

### Non-Functional Requirements

- **Traceability & Integrity**: Preserve 100% resolution of all 248 internal relative Markdown hyperlinks.
- **Model Invariance**: Zero modifications to the approved seven-thread orientation view diagram or conceptual element inventory.
- **Tone & Rigor**: Ensure authoritative, precise architectural phrasing that accurately conveys clinical stakes without hyperbole.

# Technical Design

### Current Implementation & Baseline

The canonical Domain 01 documentation is authored across 13 Markdown files under `docs/markdown/01-motivation/`:
```text
docs/markdown/01-motivation/
├── README.md
├── orientation-view.md
├── stakeholders/
│   ├── enterprise-stakeholders.md
│   └── external-authorities.md
├── drivers-assessments/
│   ├── drivers.md
│   └── assessments.md
├── goals-outcomes/
│   ├── strategic-goals.md
│   └── business-outcomes.md
├── principles/
│   ├── architectural-axioms.md
│   └── reclassified-principles.md
└── requirements-constraints/
    ├── foundational-requirements.md
    ├── external-constraints.md
    └── master-requirements-catalogue.md
```

While the structural decomposition and core model are approved, a review against the frozen Motivation model identified specific areas where text requires cleanup:
1. In `drivers-assessments/assessments.md`:
   - False Acceptance (Assessment 1): References to specific protocol codes (`MSA-1 = AA`, `AE`), transport mechanisms (`TCP/MLLP`), and volatile process queues exist within the normative hazard description rather than being isolated to an illustrative example.
   - Centralised Concurrency (Assessment 5): Wording uses hyperbolic claims ("lock contention escalates exponentially", "I/O wait times skyrocket") and leans toward prescriptive persistence patterns.
   - Implicit Perimeter Trust (Assessment 4): The architectural response asserts adherence to a "zero-trust, default-deny model" directly within the assessment, rather than diagnosing the risk and deferring default-deny enforcement to downstream requirements derived from AX-07.
2. Across multiple files (`assessments.md`, `drivers.md`, `strategic-goals.md`, `enterprise-stakeholders.md`, `external-authorities.md`, `foundational-requirements.md`, `orientation-view.md`):
   - Overstated dramatic language ("catastrophic", "lethal", "zero-tolerance", "absolute certainty", "skyrocketing", "exponential") appears in explanatory text.
3. In `stakeholders/external-authorities.md` and `requirements-constraints/external-constraints.md`:
   - External authorities and constraints assert detailed mandates (e.g. specific cryptographic mechanisms, logging implementations, persistence rules, access-gating) rather than establishing the existence and architectural significance of external obligations.
4. In `requirements-constraints/master-requirements-catalogue.md`:
   - Downstream requirements owned by Domains 04, 05, 06, 08, and 11 are marked as `Accepted`. Because those domains have not yet undergone canonical reconciliation, they must be marked `Candidate`.

### Key Technical Decisions & Changes

1. **Protocol-Neutral False Acceptance Assessment**:
   - Refactor Assessment 1 in `drivers-assessments/assessments.md` to focus strictly on the core architectural invariant: positively acknowledging acceptance of an incoming event before crossing the required durable acceptance boundary creates severe risk of unrecoverable data loss.
   - Demarcate HL7 v2 `AA`/`AE` behavior under an explicit subsection heading: `#### Downstream Integration Architecture Example (HL7 v2 / MLLP)`.
   - Preserve REQ-FND-001 verbatim.

2. **Technology-Neutral Concurrency Assessment**:
   - Refactor Assessment 5 in `drivers-assessments/assessments.md` to use the approved normative statement:
     > "Requiring central synchronous persistence interaction for every unit of active processing can introduce contention, latency, unnecessary I/O and coordination bottlenecks that limit concurrent throughput and scalability."
   - Replace hyperbolic terms with measured statements: "contention increases under high concurrent load" and "I/O wait times increase, causing gateway buffers to fill".

3. **Separation of Problem Diagnosis from Security Solution**:
   - In Assessment 4 (`Implicit Perimeter Trust`), diagnose the operational hazard of perimeter-only trust without pre-empting the solution.
   - Replace "adhering to a zero-trust, default-deny model" with "requiring platform-enforced security context across all managed operations".
   - State clearly that default-deny authorization (`SEC-002`) is a downstream Security Architecture requirement derived from AX-07.

4. **Sober, Clinically Grounded Language**:
   - Replace dramatic rhetoric across the suite:
     - `drivers-assessments/assessments.md`: "catastrophic risk" -> "severe risk"; "escalates exponentially" -> "increases substantially"; "skyrocket" -> "grow significantly".
     - `drivers-assessments/drivers.md`: "catastrophic clinical harm" -> "severe clinical harm"; "lethal medication doses" -> "contraindicated or fatal medication doses".
     - `goals-outcomes/strategic-goals.md`: "catastrophic vulnerability" -> "critical vulnerability".
     - `stakeholders/enterprise-stakeholders.md`: "catastrophic systemic failures" -> "major systemic failures"; "Absolute certainty" -> "Deterministic certainty".
     - `stakeholders/external-authorities.md`: "zero-tolerance standards" -> "strict regulatory standards".
     - `requirements-constraints/foundational-requirements.md`: "fatal vulnerability" -> "critical vulnerability"; "catastrophic window" -> "vulnerable operational window".
     - `orientation-view.md`: "risks catastrophic, unrecoverable data loss" -> "risks severe, unrecoverable data loss" (in narrative text only; Mermaid graph remains untouched).

5. **Tightening External Authority and Constraint Descriptions**:
   - In `stakeholders/external-authorities.md`:
     - Under HI Service, replace claims that it "Mandates strict operational and cryptographic rules for identifier resolution, validation, encryption, access gating, and persistence" with: "Establishes authoritative national identifiers and governing rules for identifier validation, use, and disclosure."
     - Under OAIC, replace "zero-tolerance standards" and detailed logging prescriptions with: "Enforces statutory obligations governing the collection, handling, disclosure, and protection of health information."
   - In `requirements-constraints/external-constraints.md`:
     - CST-EXT-001 & CST-EXT-002: Frame architectural impacts as platform obligations to satisfy applicable jurisdictional privacy and identifier laws without dictating specific cryptographic algorithms or persistence engines.

6. **Downstream Requirement Lifecycle Semantics**:
   - Update `requirements-constraints/master-requirements-catalogue.md` status table:
     - Retain `Accepted` for: `REQ-FND-001`, `REQ-FND-002`, `REQ-FND-003`, `REQ-FND-004`, `CST-EXT-001`, `CST-EXT-002`, `CST-EXT-003`.
     - Update to `Candidate`: `CORE-001`, `CORE-003`, `SEC-001`, `SEC-002`, `SEC-010`, `PROV-001`, `INT-TRANS-001`, `APP-SEP-001`, `DIR-001..006`.
   - Update the explanatory narrative to emphasize that Domain 01 tracks candidate downstream requirements for traceability, while authoritative acceptance is conferred when each downstream domain completes canonical reconciliation.

7. **Preservation of Security Architecture vs Governance Distinction**:
   - In `master-requirements-catalogue.md` and `assessments.md`, ensure `SEC-010` (PHI-Safe Operational Logging) is catalogued under Domain 08 (Security Architecture), with formal architectural guardrails deferred to Domain 13 (Governance & Decisions).

# Testing

### Validation Approach

Validation will confirm that all cleanup actions have been applied precisely, that the frozen Motivation model and orientation view remain intact, and that no unintended modifications were introduced.

### Conformance Verification Criteria

1. **File Inventory & Integrity**:
   - Exactly the 13 canonical Domain 01 files exist under `docs/markdown/01-motivation/`.
   - No files added, deleted, or renamed.
2. **Relative Hyperlink Resolution**:
   - An automated link verification script verifies that all 248 internal relative Markdown hyperlinks resolve cleanly with zero broken references.
3. **Frozen Model & Diagram Invariance**:
   - The 7-thread Mermaid diagram in `orientation-view.md` is bit-for-bit identical to the approved frozen model.
   - All 6 enterprise stakeholders, external authorities, 7 drivers, 5 assessments, 6 goals, 3 outcomes, AX-01..11, AX-13..16, AX-12 reclassification, 4 foundational requirements, and 3 constraint categories remain accounted for.
4. **Assessment Neutrality & Security Separation**:
   - `drivers-assessments/assessments.md` Assessment 1 contains no protocol prescriptions in its normative body; HL7 AA/AE is isolated to a downstream example.
   - Assessment 5 uses the exact agreed technology-neutral definition; "exponential" and "skyrocket" are eliminated.
   - Assessment 4 diagnoses implicit perimeter trust without prescribing "zero-trust" or "default-deny" as part of the assessment itself.
5. **Rhetorical Language Elimination**:
   - Automated grep confirms zero instances of "catastrophic", "lethal", "zero-tolerance", "absolute certainty", "skyrocket", or "exponential contention" across all 13 files.
6. **External Authority Demarcation**:
   - `external-authorities.md` and `external-constraints.md` contain no claims that external bodies mandate specific cryptographic, persistence, logging, or access-gating mechanisms.
7. **Requirement Status Semantics**:
   - In `master-requirements-catalogue.md`, only REQ-FND-001..004 and CST-EXT-001..003 are marked `Accepted`.
   - CORE-001, CORE-003, SEC-001, SEC-002, SEC-010, PROV-001, INT-TRANS-001, APP-SEP-001, DIR-001..006 are marked `Candidate`.
8. **Completion Report**:
   - Report generated at `.junie/reports/YYYY-MM-DD-domain-01-final-cleanup.md` conforming to the specified format with all sections completed.

# Delivery Steps

### Step 1: Neutralize Motivation Assessments and Separate Security Problems from Solutions
The five platform assessments in `drivers-assessments/assessments.md` are purged of protocol prescriptions, hyperbolic phrases, and solution leakage.

- Refactor Assessment 1 (Silent Data Loss via False Acceptance): formulate the normative hazard around durable acceptance boundary crossing; remove HL7 codes, MLLP, and volatile queues from the normative text; isolate HL7 `AA`/`AE` behavior under a clearly labeled downstream Integration Architecture example section.
- Refactor Assessment 5 (Centralised Persistence): adopt the agreed technology-neutral wording regarding contention, latency, I/O, and coordination bottlenecks; eliminate claims of exponential lock contention and skyrocketing I/O wait times.
- Refactor Assessment 4 (Implicit Perimeter Trust): isolate the problem diagnosis of perimeter-only trust; remove assertions that the assessment itself adheres to a zero-trust/default-deny model; frame AX-07 as the governing principle and default-deny as a downstream requirement.

### Step 2: Excise Overstated Rhetorical Claims and Tighten External Authority Statements
Overstated rhetorical claims are removed across Domain 01 files and external authorities are strictly characterized as constraint sources.

- Replace hyperbolic language ("catastrophic", "lethal", "zero-tolerance", "absolute certainty", "fatal") in `stakeholders/enterprise-stakeholders.md`, `drivers-assessments/drivers.md`, `goals-outcomes/strategic-goals.md`, `requirements-constraints/foundational-requirements.md`, and narrative sections of `orientation-view.md`.
- Ensure the Mermaid diagram in `orientation-view.md` remains strictly unchanged.
- Refactor `stakeholders/external-authorities.md` and `requirements-constraints/external-constraints.md` to remove claims that authorities mandate specific cryptographic algorithms, identifier-resolution mechanisms, logging implementations, persistence mechanisms, or access-gating implementations.

### Step 3: Correct Downstream Requirement Statuses and Verify Security/Governance Boundaries
The master requirements catalogue reflects accurate lifecycle semantics for downstream domains and preserves governance boundaries.

- Update `requirements-constraints/master-requirements-catalogue.md` to mark all un-reconciled downstream requirements (`CORE-001`, `CORE-003`, `SEC-001`, `SEC-002`, `SEC-010`, `PROV-001`, `INT-TRANS-001`, `APP-SEP-001`, `DIR-001..006`) as `Candidate`.
- Retain `Accepted` status for the four Domain 01 foundational requirements (`REQ-FND-001..004`) and three external constraint categories (`CST-EXT-001..003`).
- Re-verify that Zero-PHI operational logging (`SEC-010`) is classified under Domain 08 Security Architecture, with formal governance guardrails cleanly deferred to Domain 13.

### Step 4: Execute Validation Suite and Author Completion Report
All validation criteria are satisfied and the execution record is compiled.

- Run link-checking test to confirm 100% resolution of all 248 internal relative Markdown hyperlinks across the 13 canonical documents.
- Run textual search to verify zero occurrences of prohibited rhetorical terms ("catastrophic", "lethal", "zero-tolerance", "absolute certainty", "skyrocket", "exponential contention").
- Verify that normative axioms AX-01..AX-11 and AX-13..AX-16 remain intact and AX-12 remains reclassified to Domain 05.
- Author `.junie/reports/YYYY-MM-DD-domain-01-final-cleanup.md` containing task summary, modified files, key actions, requirement status change table, validation results, deviations (`None`), and unresolved issues (`None`).