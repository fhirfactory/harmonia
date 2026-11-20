Task-Issued-At: UNRESOLVED

Event source: The Codex interaction did not expose the attributable instruction-entry event timestamp. Solution Architect treatment is required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 Topic/TopicMask design consolidation — Task Detail

- **Attribution** · User-issued instruction in the Harmonia Codex interaction. The instruction identifies the Solution Architect as authority. The supplied continuation is the decision input.
- **Evidence boundary** · `software-0.1.0/003`, bounded documentation preparation and analysis.
- **Related backlog source** · [BL-CALLIOPE-OO-01 revision 1](../../../../../../../docs/backlog/calliope-oo-01-topic-models.md), Git blob `3c93558550da6a140a8668e120638efb8e9a368d`.
- **Inputs** · [Continuation adjudications](../F-01-Topic-TopicMask-Adjudications-Continuation.md) · [Initial adjudications](../../../002/before/F-01-Topic-TopicMask-Adjudications.md).

## Operative instruction as issued

~~~
# Harmonia — F-01 Topic/TopicMask Design Consolidation

**Repository:** `fhirfactory/harmonia`  
**Working branch:** `feature/BL-CALLIOPE-OO-01`  
**Software release:** `software-0.1.0`  
**Iteration:** `003`  
**Activity:** Design adjudication incorporation and architectural reconciliation  
**Authority:** Solution Architect  
**Status:** Authorised for documentation preparation and analysis only.

## 1. Objective

Incorporate the Solution Architect's accepted F-01.45–F-01.73 adjudications into the Topic/TopicMask design.

The work continues iteration 002 and must preserve its historical evidence.

Produce:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.002.md`

This is the second draft evolving the accepted `Final.001.000` baseline.

The new draft must be complete and self-contained, incorporating both the original and continuation adjudications.

## 2. Authoritative Inputs

### 2.1 Iteration 002 — Initial adjudications

Read:

`enterprise-architecture/design/evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md`

This contains the original F-01.1–F-01.44 decisions.

### 2.2 Iteration 003 — Continuation adjudications

Read:

`enterprise-architecture/design/evidence/software-0.1.0/003/before/F-01-Topic-TopicMask-Adjudications-Continuation.md`

This contains F-01.45–F-01.73 and the subsequent consolidation disposition.

**The continuation adjudications supersede earlier decisions wherever explicitly stated.**

In particular:

**Missing or empty qualifiers generate `N<position>`. Values are mandatory, non-empty and never generated.**

Do not perpetuate the incorrect qualifier/value interpretation introduced during iteration 002.

### 2.3 Existing DRPs

Read:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.000.md`

and:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.001.md`

Final.001.000 is the historical accepted predecessor.

Draft.001.001 is the design produced during iteration 002.

Preserve both unchanged.

## 3. Required Design Consolidation

Create:

`DRP-Topic-and-TopicMask.draft.001.002.md`

in the existing `enterprise-architecture/design/offline-design/` directory.

Integrate all accepted decisions into the relevant design sections, rather than appending a list of amendments.

The revised document must address:

### 3.1 Topic identity and structure

- Canonical-name identity without runtime UUID.
- Version-independent equality and hashing.
- Deterministic natural ordering.
- Immutable Topic and TopicEntry objects.
- Ordered hierarchical entries.
- Repeated qualifiers.
- Optional per-entry versions.
- Strict canonical-name construction and parsing.

### 3.2 TopicEntry validation

Incorporate the corrected rules:

- Missing or empty qualifiers generate `N<position>` during Topic assembly.
- Generated qualifiers use final zero-based hierarchy positions.
- Values are mandatory and never generated.
- Qualifiers use printable ASCII.
- Values use printable Unicode, subject to accepted category restrictions.
- Versions use printable ASCII.
- Leading and trailing whitespace is prohibited.
- No silent trimming or Unicode normalisation.
- Length limits are measured in Unicode code points.
- Generated qualifiers undergo the same validation as literal qualifiers.
- Canonical parsing requires explicit, valid qualifiers and values.

### 3.3 Topic construction

- Sequential and explicitly indexed construction.
- Indexed positions are zero-based and contiguous at `build()`.
- Builder instances cannot mix indexed and sequential construction.
- Duplicate indexed additions replace the existing entry after validation.
- Builder operations preserve state on failure.
- Builders support reuse and immutable snapshots.
- No `removeEntry()` requirement.
- No `Topic.builder(existingTopic)` convenience method.

### 3.4 TopicMask

- Immutable structural matching specification.
- Independent qualifier, value and optional version RegEx expressions.
- Positional matching without skipped hierarchy levels.
- Explicit `includeContainedElements`.
- RE2/J full-field matching.
- Literal and RegEx construction helpers.
- Strict null and empty-expression distinctions.
- Version-constraint presence semantics.
- Explicit universal matching through `TopicMask.all()`.

**Universal masks must have an explicit logical representation.**

Do not implement universality using a reserved RegEx tuple or special wildcard expression.

### 3.5 Canonical-expression parsing

Incorporate the accepted two-layer escaping mechanism.

Preserve the original RegEx expressions through construction, canonical representation, parsing and reconstruction.

Include mandatory round-trip requirements.

### 3.6 Logging and performance

Preserve the previously accepted requirements concerning:

- Fail-closed matching.
- Diagnostic exit-point logging.
- Harmonia application logging integration.
- Themis audit responsibility.
- Thread-safe immutable masks.
- Short-circuit evaluation.
- Release-defined resource limits.
- No global matching-result cache.

Do not introduce new logging or caching infrastructure.

## 4. Evidence Structure

Use:

`enterprise-architecture/design/evidence/software-0.1.0/003/`

### `before/`

Preserve the supplied continuation adjudication record unchanged.

### `after/`

Create new iteration-specific execution evidence.

At minimum, provide:

1. `F-01-Decision-Delta.md`
2. `F-01-Architecture-Reconciliation.md`
3. `F-01-Implementation-Gap-Analysis.md`
4. `F-01-Execution-Report.md`

These should describe the changes and remaining exceptions for iteration 003.

Do not overwrite iteration 002 evidence.

## 5. Architectural Reconciliation

Use the existing iteration 002 reconciliation as the starting point.

Reassess only findings materially affected by the continuation adjudications.

Retain unresolved issues concerning:

- Runtime Topic identity versus governed architectural identity.
- Legacy Topic UUIDs and references.
- TopicMask compatibility and migration.
- Information Unit Topic designation.
- Architectural naming vocabulary.
- Calliope dependency boundaries.
- Logging under backpressure.

Do not infer that a conflict has been resolved merely because the new design is internally consistent.

Use targeted searches and bounded excerpts. Avoid loading large RAD documents.

## 6. Implementation Gap Analysis

Update the exception-based implementation gap analysis to reflect the new design.

Particularly examine the consequences of:

- Corrected qualifier generation.
- Explicit universal-mask representation.
- Revised field validation.
- Builder construction and replacement semantics.
- Literal versus RegEx validation.
- Version-constraint distinctions.
- Canonical parsing and round-trip requirements.

Do not modify Java source code, tests or Maven configuration.

## 7. Mandatory Acceptance Evidence

Update the proposed JUnit acceptance criteria to include:

- Generated `N<position>` qualifiers.
- Rejection of missing and empty values.
- Printable ASCII qualifier/version validation.
- Printable Unicode value validation.
- Unicode code-point length measurement.
- Indexed and sequential builder behaviour.
- Duplicate indexed entry replacement.
- Builder reuse and failed-operation state preservation.
- Explicit universal-mask representation.
- Null, empty and wildcard RegEx distinctions.
- Literal helper validation.
- Canonical-expression round trips.

These remain **future implementation acceptance requirements**. Do not execute implementation work during this task.

## 8. Governance Constraints

This is a documentation and analysis activity only.

Do not:

- Modify iteration 002 evidence.
- Modify historical Final DRPs.
- Modify Draft.001.001.
- Change the accepted Architecture Continuum.
- Initiate ADD or HCRL.
- Modify Java source code or tests.
- Modify Maven dependencies.
- Publish a release.
- Commit or push without explicit authorisation.

The resulting Draft.001.002 remains pending Solution Architect review.

## 9. Completion Criteria

Return an exception-based execution report confirming:

1. All continuation adjudications have been accounted for.
2. Earlier decisions explicitly superseded have been identified.
3. Draft.001.002 is internally consistent and self-contained.
4. Iteration 002 evidence remains unchanged.
5. Iteration 003 evidence is complete.
6. Outstanding architectural conflicts are identified.
7. Implementation gaps and acceptance requirements are updated.
8. No unauthorised implementation or architectural publication occurred.

Stop at the Solution Architect review boundary.
~~~
