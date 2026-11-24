# Harmonia 1.1.20 — Capability Model Consolidation
## Pass 3 — XD Adjudication Incorporation and Catalogue Semantic Normalisation

### Goal

Incorporate the Solution Architect's accepted XD-001–XD-028 adjudications and catalogue-wide Feature authoring rules into the current Harmonia 1.1.20 offline Capability Model working candidate.

Produce a consolidated, semantically coherent capability catalogue suitable for subsequent governed Architecture Continuum incorporation.

This is an **authorised incorporation and consolidation activity**, not a new discovery, architecture redesign or requirements-generation exercise.

### Expected Outcome

A complete revised 1.1.20 working capability catalogue that:

1. Incorporates all 28 accepted XD adjudications.
2. Preserves previously accepted SR and other applicable architectural decisions.
3. Correctly implements accepted Feature consolidations, retentions, renames and CT3 refinements.
4. Expresses canonical Features as meaningful architectural capabilities, responsibilities and observable outcomes.
5. Separates Feature definitions from their associated KPI and measurement criteria.
6. Excludes discovery and verification status annotations from canonical element names and descriptions.
7. Preserves the original measurement evidence, historical decisions and architectural traceability.
8. Maintains valid capability identifiers, containment and hierarchy relationships.
9. Provides an updated adjudication workbook and reconciliation evidence.
10. Is internally consistent and ready for Solution Architect review.

No governed Continuum publication is authorised by this task.

### Context

Harmonia 1.1.20 is consolidating the Business, Business Enabling and Enterprise Capability hierarchies.

The established hierarchy remains:

**CT1 → CT2 → CT3 → Feature**

The combined Business/Business Enabling hierarchy remains distinct from the Enterprise Capability hierarchy.

The most recent known offline working candidate is:

`Harmonia_1.1.20_Candidate_017_Pass3_011_XD009_Working_Overlay.md`

Its recorded population is:

- 586 CT3 capabilities.
- 2,941 Features.

The corresponding adjudication workbook is:

`Harmonia_1.1.20_Pass3_011_NR001_Adjudication_Workbook.xlsx`

Verify these against the available working artefacts before proceeding. Do not assume remembered repository paths or release state are authoritative.

The primary accepted architectural input for this activity is:

**Harmonia 1.1.20 — XD-001–XD-028 Consolidated Adjudications, Revision 2**

`Harmonia_1.1.20_XD001-XD028_Consolidated_Adjudications_Rev2.md`

Use the complete supplied Revision 2 document, including its accepted authoring rules, architectural distinctions, detailed adjudications, qualifications and incorporation criteria.

The Solution Architect has reviewed and accepted the consolidated decisions.

The accepted Revision 2 specification supersedes earlier, conflicting XD screening proposals and preliminary interpretations. Existing accepted SR decisions remain applicable unless explicitly superseded.

### Constraints

#### 1. Architectural authority

The governed Harmonia architecture resides under:

`enterprise-architecture/continuum/`

The governed Markdown RAD is authoritative.

ArchiMate is the governed architectural projection and workbench. Open Exchange XML is interchange, not an independent source of authority.

Do not load the large ArchiMate XML model into an AI context unnecessarily.

Follow the repository's current architecture and change governance.

#### 2. Preserve accepted architectural decisions

Do not reopen accepted XD or SR adjudications.

Do not substitute earlier screening recommendations for later accepted decisions.

Preserve meaningful distinctions between capabilities even where names, implementation mechanisms or underlying information overlap.

The governing NR-001 principle remains applicable: different containment does not automatically establish different responsibilities, and similar terminology does not automatically establish duplication.

#### 3. Feature semantic authoring — AR-001

Canonical Features must describe architectural responsibilities, capabilities or observable outcomes.

They must not be written primarily as KPI calculations merely to make them measurable.

For example:

**Measurement-oriented wording:**

> Proportion of critical findings directed to an appropriately accountable recipient.

**Canonical Feature wording:**

> Critical findings are directed to an appropriately accountable recipient under the applicable clinical responsibility arrangements.

**Associated measurement:**

> Proportion of applicable critical findings delivered to the correctly accountable recipient.

Apply this principle throughout the entire capability catalogue, not merely the XD-affected Features.

Preserve meaningful existing KPI definitions and assessment criteria separately from canonical descriptions.

Do not discard measurable obligations, thresholds, timing conditions or other assurance-relevant details.

Where measurement or reporting is itself the architectural responsibility, measurement terminology remains appropriate.

Do not invent new performance thresholds, measurement obligations or KPIs.

#### 4. Canonical definition versus governance status — AR-002

Canonical capability and Feature names and descriptions must not contain discovery or verification-status annotations such as:

- `PROPOSED`
- `unverified`
- `PROPOSED — unverified`
- `INFERRED_FROM_EVIDENCE`
- `[NEW C014]` or equivalent discovery labels.

Preserve applicable provenance and governance status in the appropriate supporting evidence.

Removing these labels does not constitute implementation verification, solution acceptance or release approval.

Do not silently promote an unverified implementation claim into an assured implementation claim.

#### 5. Structural and identity preservation

Preserve existing accepted identities and hierarchy relationships.

Apply only authorised consolidation, retirement, relocation or renaming decisions.

Do not renumber surviving Features to close numbering gaps.

Do not manufacture replacement Features merely to satisfy a numerical target.

Feature counts may legitimately vary between CT3s.

Preserve traceability for retired and transferred identifiers.

#### 6. Architectural and implementation separation

Capability definitions must be understandable without requiring knowledge of Harmonia's internal SolutionFacet names.

Internal component responsibilities may be used to establish the relevant architectural boundary, but should not unnecessarily dominate business-facing capability definitions.

Do not introduce implementation technologies, class structures, deployment arrangements or framework-specific mechanisms into capability definitions unless explicitly governed by accepted architecture.

#### 7. Scope control

Do not initiate a separate discovery exercise, wholesale architecture redesign or new release programme.

Do not extend this activity into the outstanding FR-001–FR-009 adjudications or other unresolved architectural workstreams.

Do not automatically publish, commit or push.

If a genuine conflict prevents faithful incorporation, preserve the established state and report the precise issue, affected elements and available alternatives.

Ordinary reconciliation, semantic normalisation and validation are part of this authorised activity and do not require a separate discovery task.

### Authorised Activity / Scope

#### A. Establish the incorporation baseline

Inspect the available Pass3/011 candidate, adjudication workbook and accepted Revision 2 specification.

Confirm the current candidate population, previously incorporated XD decisions, retired identities and outstanding changes.

Do not repeat work already correctly incorporated.

#### B. Incorporate XD-001–XD-028

Apply the complete accepted adjudication set.

In particular, preserve the significant architectural outcomes:

- **XD-001:** Single durable-persistence-before-acknowledgement obligation.
- **XD-002:** Accepted critical finding communication CT3 consolidation.
- **XD-005:** Consolidated exchange conformance test repeatability.
- **XD-007:** Historical version accessibility consolidation and cross-domain longitudinal presentation refinements.
- **XD-008–XD-009:** Distinct Twin isolation, allocation, execution recovery and operational-context recoverability responsibilities.
- **XD-014:** Policy-governed selective or all-or-nothing evidence disclosure.
- **XD-018:** Retained critical-result recipient accountability, identity assurance, notification, acknowledgement and follow-up coverage following XD-002.
- **XD-020:** Governed change preconditions across Mneme/Mnemosyne versus persistence-boundary enforcement.
- **XD-021:** Comprehensive model verification, including structure, terminology, reference data and dependencies, distinct from information terminology conformance.
- **XD-022:** Enterprise continuity obligation preserved separately from operational work acceptance, queueing and pre-processing assurance.
- **XD-023:** Versioned value-set access distinct from published terminology stability.
- **XD-024:** Governed Information Lifecycle Actions, distinct from information retention and retirement-specific processing.
- **XD-025:** Temporal LHR presentation distinct from source-established encounter/episode relationships.
- **XD-027:** Selective Digital Twin Stimulus information contextualisation, distinct from Twin execution contextualisation and operational-context recovery.
- **XD-028:** Distributed information availability distinct from distributed reference coherence and access permissions.

These summaries are navigational only. The complete Revision 2 specification governs the exact dispositions.

Do not create additional Features merely because an adjudication identifies an architectural responsibility. Reconcile against the existing capability population first and follow the accepted disposition.

#### C. Apply catalogue-wide authoring normalisation

Review all CT3 and Feature definitions in the working candidate.

Apply AR-001 and AR-002 systematically.

Distinguish:

1. Canonical capability/Feature semantics.
2. Associated KPI and measurement criteria.
3. Architectural provenance and adjudication evidence.
4. Implementation and verification evidence, where available.

Maintain traceability between a Feature and its associated measurement criteria.

Use the repository's established representation where suitable. Do not invent a new mandatory architectural metamodel or governance process solely for this activity.

Where the existing documentation representation cannot express the required distinction, identify and implement the smallest appropriate extension consistent with current repository governance.

Ensure canonical descriptions remain understandable to business, clinical, operational and engineering stakeholders.

#### D. Reconcile the catalogue

Check that the revised catalogue:

- Preserves the intended CT1/CT2/CT3/Feature hierarchy.
- Contains no unintended duplicate identifiers.
- Contains no broken containment or references caused by accepted consolidations.
- Does not retain references to retired elements as though they were active.
- Preserves deliberate generalisation/specialisation and contextual distinctions.
- Retains the accepted relationships between Business and Enterprise responsibilities.
- Does not lose architectural obligations through editorial normalisation.

Record any substantive discrepancy rather than silently inventing an architectural decision.

#### E. Update adjudication evidence

Update the appropriate working adjudication workbook to represent all accepted XD decisions and their actual incorporation state.

Distinguish:

- Accepted and incorporated.
- Accepted but qualified.
- Reconciliation exception, where genuinely applicable.

Retain relevant historical decisions and retired-identifier traceability.

Remove obsolete pending labels where the corresponding adjudication has been accepted and incorporated.

Do not represent a screening proposal as an active unresolved decision after its accepted disposition has superseded it.

#### F. Validate the resulting candidate

Perform available structural, semantic and consistency checks.

Reconcile the resulting CT3 and Feature counts against the baseline and the authorised changes.

Verify that:

- AR-001 has been applied across the catalogue.
- AR-002 annotations do not appear in canonical names or descriptions.
- Measurement criteria have not been silently discarded.
- Accepted XD decisions are represented.
- Retired identifiers are absent from the active hierarchy but preserved in historical evidence.
- No unrelated capability changes have been introduced.

Preserve qualifications for genuine exceptions.

### Report

Use the repository's current governed evidence convention:

`enterprise-architecture/design/evidence/1.1.20/<iteration>/`

Place task inputs/directions under `before/` and execution reports/results under `after/`, subject to current repository governance.

Do not use `.junie/` as the governed evidence destination.

Produce a report containing:

1. Baseline and source artefacts actually used.
2. XD-001–XD-028 incorporation matrix.
3. Structural changes, including retired and retained identifiers.
4. CT3 and Feature wording changes.
5. AR-001 normalisation results, including treatment of KPI criteria.
6. AR-002 cleanup results, including preserved provenance.
7. Before/after catalogue population and reconciliation.
8. Validation results and identified exceptions.
9. Unresolved architectural conflicts, if any.
10. Final working candidate and workbook locations.

Clearly distinguish completed changes from qualifications or deferred work.

### Completion Boundary

The authorised outcome is a **fully reconciled offline 1.1.20 working candidate and associated adjudication evidence**.

Do not publish the candidate into the governed Architecture Continuum, modify the accepted release baseline, or commit/push without separate authority.

Stop at the completed working candidate and report the actual results for Solution Architect review.
