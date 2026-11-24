Task-Issued-At: UNRESOLVED

Event source: The Codex interaction exposes the operative instruction and its attribution without a system-recorded instruction-entry timestamp. Solution Architect treatment is required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Pragma Publication and Subscription Architecture

The operative Codex user instruction attributes the PS-01–PS-16 design decisions to the Solution Architect's review. It authorises a Draft, its entry in the existing pending HCRL.004, and reconciliation evidence originating in Iteration 005. The starting state was `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`) with pre-existing worktree changes. The software candidate and backlog references remain the ones identified in HCRL.004. [Cumulative HCRL][hcrl] · [Architecture release][release]

## Operative instruction

The Markdown below preserves the issued wording inside its enclosing message fence.

~~~
# Harmonia — Iteration 005
## Pragma Publication and Subscription Architecture

### Objective

Create:

`DRP-Pragma-Publication-and-Subscription.draft.000.001.md`

The purpose is to formalise the architectural decisions arising from the Solution Architect's review of Pragma publishing, subscription, routing, instantiation and execution.

This is an architecture-documentation and reconciliation activity only.

### 1. Review existing architecture

Review the relevant, bounded source material, including:

- `DRP-Mneme-Pragma-Model`
- `DRP-Mneme-Pragmaton-Model`
- `DRP-Topic-and-TopicMask.draft.001.003.md`
- Relevant Mneme/Petasos interaction DRPs
- Existing publication/subscription architecture
- Relevant Pylai, Digital Twin and Dokimasia architectural definitions

Prefer targeted sections and bounded excerpts. Do not load large RAD documents unnecessarily.

Identify conflicting or superseded terminology and record reconciliation obligations.

### 2. Publication and subscription model

Document the following architectural decisions.

**PS-01 — Common framework**

Pragma and its subclasses constitute Harmonia's exclusive publication/subscription work framework.

**PS-02 — Output information**

An Ergon processes a realised Pragma and generates zero or more output InformationUnits.

**PS-03 — PublishingMask**

Generated InformationUnits are evaluated against the applicable PublishingMask before downstream Pragmas are created.

**PS-04 — Filtered information**

InformationUnits that do not satisfy the PublishingMask remain governed information.

They do not generate downstream Pragmas and are unavailable to ordinary subscription matching.

Their publication disposition may be represented by a value such as `FILTERED_OUT`; the precise state representation remains subject to reconciliation.

**PS-05 — Normal disposition**

Filtering is not inherently a processing failure.

**PS-06 — Evidence**

Publication decisions are captured appropriately through OA&M statistics, Kleio audit, provenance and Dokimasia Assurance.

**PS-07 — PragmaFactory**

For eligible output InformationUnits, PragmaFactory creates new archetypal Pragmas.

Each new Pragma has exactly one input InformationUnit.

**PS-08 — Subscription matching**

Subscription selection operates on:

- Pragma attributes.
- Attributes of the InformationUnit represented by `Pragma.input()`.

Matching must not require InformationUnit content.

**PS-09 — Subscriber-specific realisation**

For each matching subscription, Mneme's Router/DecisionEngine invokes the appropriate factory mechanism to create a distinct subscriber-specific realisation.

**PS-10 — Terminology**

The currently agreed terminology is:

- `Pragma` → `Pragmatosis`
- `Stimulus` → `Noesis`

The corresponding Dokimasia-specific Pragma and realisation names remain unresolved.

Reconcile historical `ActionableTask` and `FulfillmentTask` terminology without silently retaining them as additional parallel frameworks.

**PS-11 — Independent identity**

Each realised undertaking has independent identity, lifecycle, outcome, subscriber association and processing context.

It retains its relationship to the originating archetypal Pragma.

**PS-12 — Petasos handoff**

A PetasosParcel carries the reference and required handoff metadata for the subscriber-specific realisation.

Petasos does not transport the full InformationUnit content.

**PS-13 — Information management**

Mneme owns Pragma management.

Mneme/Mnemosyne provide governed persistence, with Mneme's SummaryCache supporting efficient access to Pragma and InformationUnit summary information.

**PS-14 — InformationUnit snapshots**

InformationUnit summaries contained within Pragmas and their realisations are non-authoritative snapshots.

They must never become independent authoritative versions of the InformationUnit.

**PS-15 — Asymmetric governance**

Publishing and Subscription remain independently governed mechanisms.

Publishing controls producer-side eligibility.

Subscription controls consumer-side selection.

**PS-16 — Assurance**

Defined Praxis has a corresponding generated or derived `Dokimasia.Praxis` instance.

Assurance evaluates expected and actual publication, filtering, subscription matching, handoff and processing progression.

### 3. Ergon processing relationship

Document the relationship between:

- Pragma and its realisation.
- Ergon.ContextLoader.
- Ergon.ProcessingEngine.
- Ergon.WorkSpace.
- Mneme Workflow Cache.
- Ergon-InformationAdapter.
- Themis authorisation.

The WorkSpace is the managed processing information environment.

It is not synonymous with the Pragma, an InformationUnit or authoritative persistence.

Specialised Ergons may establish their processing context differently, including DigitalTwin-Behaviour and Pylai ingress/egress.

### 4. Reliability and consistency

Identify architectural requirements for:

- Reliable creation of subscriber-specific realisations.
- Prevention of duplicate realisations during retries.
- Authoritative persistence before acknowledged handoff.
- Independent processing and retry outcomes.
- Correlation between archetypal Pragmas, realised undertakings and originating InformationUnits.
- Appropriate Assurance evidence.

Do not invent implementation algorithms, persistence schemas or transaction mechanisms.

### 5. Architectural reconciliation

Clearly distinguish:

1. Existing accepted architectural authority.
2. Decisions established by this review.
3. Historical terminology or behaviour requiring reconciliation.
4. Matters remaining unresolved.

Pay particular attention to the distinction between `Pragmaton` and the newly agreed `Pragmatosis`.

Do not silently treat them as synonyms.

### 6. Deliverables

Create:

`DRP-Pragma-Publication-and-Subscription.draft.000.001.md`

Update:

`hcrl-software-0.1.0-004.pending.md`

Add the new DRP as a review activity within the existing cumulative HCRL, preserving every previously recorded issue and carry-forward obligation.

Place the execution/reconciliation evidence in:

`enterprise-architecture/design/evidence/software-0.1.0/005/after/`

### 7. Constraints

- Do not modify application code.
- Do not modify accepted architecture.
- Do not amend existing Final DRPs.
- Do not close the HCRL.
- Do not commit, tag or push.
- Do not introduce new architectural abstractions without identifying them for Solution Architect review.

Report the generated documents, identified discrepancies and unresolved matters.

The resulting DRP remains **Draft**, pending Solution Architect adjudication.
~~~

[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[release]: ../../../../../../../architecture-release.json
