<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Bounded round-trip machinery remediation — Task

Task-Issued-At: UNRESOLVED

Issuer: Mark Hunter, Solution Architect, through the current Codex interaction.
The interaction exposes no attributable instruction-entry timestamp. The original
time and filename prefix remain unresolved for Solution Architect treatment before
closure; a current clock reading is not substituted. This also applies to the
governance clarification below.

Boundary: proposed RAD 1.1.15, iteration 002, machinery remediation only.
Iteration 001 remains unchanged benchmark evidence, without application authority.
This pending iteration allocates no release. Architecture Review is required:
normalisation and review preparation materially affect governance-supporting
behaviour. Implementation and tests are proposed engineering results, not acceptance.

## Operative instruction (verbatim)

```text
# Goal

Remediate the existing Harmonia ArchiMate Round-Trip Engineering machinery using the empirical evidence from **RAD 1.1.15 iteration 001**.

This is a **bounded remediation activity**, not a redesign of the architecture governance process and not an opportunity to create a new framework.

The primary objective is to make routine:

**Model Updated → Solution Architect Review Ready**

predominantly deterministic, fast and inexpensive.

The 1.1.15-001 benchmark demonstrated that the underlying model comparison completes in approximately five seconds, while the overall AI-driven processing consumed hours and approximately US$50 in model usage.

That execution model is unacceptable for routine architecture development.

# Expected Outcome

After this activity, a routine ArchiMate round-trip should operate approximately as:

**Archi Export**
→ **Deterministic comparison and normalisation**
→ **Deterministic evidence/provenance preparation**
→ **Small normalised architectural delta**
→ **Targeted architectural interpretation where required**
→ **change-review.md**
→ **pending HCRL**
→ **STOP for Solution Architect**

The machinery SHALL perform mechanical work deterministically wherever practical.

AI reasoning SHALL be reserved for architectural interpretation, ambiguity and matters requiring Solution Architect adjudication.

Routine processing SHALL NOT trigger autonomous recursive investigation or repeated implementation/reviewer cycles for mechanical concerns.

Where the machinery encounters a condition it cannot deterministically resolve, it SHALL **fail closed, record the condition clearly, and present it for review** rather than commencing unbounded investigation.

The practical target for a routine model update is **Model Updated → Review Ready within approximately five minutes**, excluding explicit exceptional conditions.

# Context

RAD **1.1.15 iteration 001** is the benchmark/evidence set for this remediation.

Use the existing evidence under:

`enterprise-architecture/design/evidence/1.1.15/001/`

and the review artefacts under:

`enterprise-architecture/design/review/1.1.15/`

as the empirical basis for the work.

The benchmark established, among other things:

- baseline: 1,602 elements, 1,651 relationships, 1,209 Views;
- candidate: 12 additional elements, 9 additional relationships and 7 additional Views;
- all baseline element, relationship and View identities were retained;
- the core model comparison completed in approximately five seconds;
- `xml:lang="en"` differences generated 17,957 apparent semantic differences despite unchanged property text;
- non-View Archi organisation/navigation serialization generated substantial comparison noise;
- 17 existing Views changed navigation paths and therefore represent potentially significant architect-authored changes;
- `continuum_workbench.py review` stopped at the first newly authored element without a canonical ID rather than continuing discovery of the candidate delta;
- provenance mechanics, timestamps, evidence locations, links and validation caused repeated AI implementation/review/remediation cycles;
- corpus-wide DRP investigation contributed unnecessary reasoning and token consumption.

The final 1.1.15-001 review/HCRL state is evidence for this engineering activity. It is not authority to apply the proposed architectural changes.

# Constraints

## 1. Preserve the governance model

Do not redesign Harmonia Architecture Governance.

In particular preserve:

**Model Updated**
→ comparison/analysis
→ **change-review.md**
→ pending/update HCRL
→ **Solution Architect adjudication**
→ decision.json
→ application
→ validation/projection.

This task concerns how the machinery reaches the Solution Architect review checkpoint efficiently.

## 2. Navigation semantics

The following architectural rule is authoritative for this activity:

**ArchiMate navigation structure outside the Views hierarchy is a derived projection and SHALL be deterministically regenerated from governed Continuum containment rules. Navigation identifiers, ordering and folder serialization outside Views are not independently governed architectural state.**

The **Views hierarchy and architect-authored Views are preserved**.

Generated View behaviour may be introduced or refined in the future when a concrete architectural need arises. Do not design speculative View-generation machinery in this activity.

Therefore routine non-View navigation regeneration SHALL NOT produce architectural Change Requests merely because Archi serialized or reorganised derived navigation differently.

Changes to Views or their architect-authored navigation remain reviewable.

## 3. New architect-authored elements

An architect-created element legitimately may not yet possess a canonical Harmonia identity.

Absence of `canonicalId` on a newly discovered element SHALL NOT prevent discovery and analysis of the remainder of the submitted model.

Canonical identity allocation occurs through governed incorporation after adjudication.

## 4. Transport/serialization normalisation

Known representation-only differences should be normalised deterministically where their semantic equivalence can be established.

The 1.1.15-001 `xml:lang="en"` behaviour is the initial concrete case.

Do not generalise arbitrary differences away.

If semantic equivalence cannot be established deterministically, surface the difference for review.

## 5. DRP and Continuum retrieval

Round-trip review SHALL NOT perform corpus-wide DRP analysis.

The **Continuum is the current architectural authority**.

DRPs are provenance/design evidence and may contain proposals, superseded material or discovery evidence.

Processing should begin from the detected architectural delta and retrieve only bounded, relevant Continuum context.

Consult a DRP only when specifically relevant to interpreting a detected change.

Use the existing Continuum discovery model:

**known concept → vicinity search → bounded relevant artefacts → relationships/references → architectural interpretation**

Do not load the entire Continuum or DRP corpus.

## 6. Provenance mechanics

Mechanical provenance concerns should be implemented and validated deterministically wherever practical, including:

- input hashes and sizes;
- Task/Report evidence locations;
- timestamp/filename correspondence;
- evidence directory structure;
- relative links;
- comparison inventories;
- validation results.

Do not require an AI reasoning loop to establish these facts.

Do not create recursive provenance about the production of provenance.

## 7. AI boundary

The machinery should produce a compact, normalised representation of the actual architectural delta suitable for architectural review.

AI should not normally need to consume:

- the complete ArchiMate XML;
- the complete raw comparison JSON;
- all derived navigation differences;
- the entire DRP corpus;
- execution/session histories.

AI reasoning should principally receive:

- new/removed/semantically changed architectural elements;
- new/removed/semantically changed relationships;
- new/removed/changed Views and View navigation;
- unresolved representation anomalies;
- bounded relevant Continuum context;
- explicit exceptional conditions.

## 8. No speculative machinery

Implement only changes justified by concrete friction demonstrated by 1.1.15-001.

Do not introduce a new workflow engine, governance framework, generic AI orchestration framework, provenance framework or other speculative infrastructure.

Prefer modification and simplification of the existing tooling.

## 9. Git boundary

You may inspect Git state.

Do **not** commit, push, merge, rebase, amend, tag, stage, create branches or otherwise modify Git history or remotes.

The Solution Architect owns the Git boundary.

# Authorised Activity / Scope

First inspect the existing round-trip implementation and the complete 1.1.15-001 evidence trail.

Determine specifically which existing components should be changed to address the demonstrated problems.

Likely relevant tooling includes, but is not limited to:

- `continuum_workbench.py`
- `exchange_compare.py`
- `architecture_review_evidence.py`
- `architecture_continuum.py`
- associated tests and round-trip Praxis documentation.

Implement the **minimum coherent set of changes** required to address the benchmark findings.

At minimum investigate and, where appropriate, remediate:

1. deterministic suppression/classification of established `xml:lang` representation-only differences;
2. deterministic treatment/regeneration of non-View navigation;
3. preservation and explicit detection of View/navigation changes;
4. continued ingestion/discovery when new elements do not yet possess canonical IDs;
5. generation of a compact normalised architectural-delta artefact;
6. deterministic Task/Report/HCRL evidence mechanics where currently delegated to AI reasoning;
7. prevention of corpus-wide DRP scanning during ordinary round-trip processing;
8. fail-closed handling of exceptional/unrecognised conditions without autonomous recursive investigation;
9. elimination of unnecessary AI review/rework cycles for mechanical validation.

Do not change the architectural content represented by the 1.1.15-001 candidate as part of this task.

Do not apply its CRs to the Continuum.

Do not advance `architecture-release.json`.

## Benchmark

After implementation, use **1.1.15-001 as a regression benchmark**.

Demonstrate what the revised machinery would present to an architectural reasoning agent / Solution Architect for this iteration.

The benchmark should make clear:

- elapsed deterministic processing time;
- size and nature of the normalised delta;
- which findings were deterministically classified/removed from architectural review;
- which findings remain genuine architectural review matters;
- what information would be supplied to AI;
- what exceptional conditions, if any, remain.

Do **not** invoke an expensive autonomous AI review merely to benchmark token consumption.

The purpose is to demonstrate that the input to subsequent architectural reasoning has been materially bounded.

# Report

Produce a substantive engineering report under the repository's governed report location applicable to this activity.

The report must include:

- observed causes of the 1.1.15-001 latency/token explosion;
- changes made;
- tests added or changed;
- regression/benchmark results using 1.1.15-001;
- deterministic processing elapsed time;
- resulting normalised architectural delta;
- remaining exceptional/manual conditions;
- any governance/Praxis text that is now inconsistent with the implemented behaviour;
- explicit confirmation that no 1.1.15 architectural CR was applied;
- explicit confirmation that no Git mutation was performed.

Most importantly, conclude whether the revised machinery is suitable for returning to **Harmonia architecture and solution development**, rather than continuing round-trip tooling development.

If further tooling work is recommended, distinguish:

1. **blocking defects that prevent normal Harmonia development**, from
2. **non-blocking improvements that should be deferred until concrete future friction justifies them**.

The default expectation is that non-blocking improvements are deferred.
```

## Subsequent governance clarification (verbatim)

```text
## 9. Normal Harmonia governance remains in force

This remediation activity SHALL itself follow the normal Harmonia architecture and development governance routines.

Nothing in this Task authorises bypassing, suspending, simplifying, or disabling the existing governance lifecycle merely because the activity is modifying governance-supporting or round-trip tooling.

Codex SHALL:

- create and maintain the normally required Task and Report provenance for its substantive activity;
- perform the normally required Architecture Review determination;
- generate or update the applicable HCRL where Architecture Review is required;
- preserve the normal Solution Architect adjudication checkpoints;
- execute the normally required validation and verification routines;
- preserve uncertainty and fail closed where Solution Architect adjudication is required;
- stop at the normal governed checkpoint rather than treating this Task as authority to approve its own architectural or governance changes.

The instruction from the Solution Architect to perform this remediation **does not implicitly authorise bypassing the governed process**.

The objective of this Task is to make the normal governance machinery deterministic, efficient and inexpensive where the work is mechanical — **not to remove the governance controls themselves**.

Where this remediation changes the implementation of an existing governance routine, Codex SHALL continue to execute the currently governed routine until the replacement behaviour has been reviewed and accepted by the Solution Architect.

A proposed tooling change SHALL NOT become authoritative merely because Codex has implemented and tested it.

## 10. Git boundary

Codex may inspect Git state.

Codex SHALL NOT commit, push, merge, rebase, amend, tag, stage, create branches or otherwise modify Git history or remotes.

The Solution Architect owns the Git boundary.
```

Both instructions link to [the unchanged 001 Change Review](../../../../../review/1.1.15/change-review.md).
The Task's requested engineering changes are authorised implementation scope;
their operational adoption, architecture acceptance and release allocation remain
reserved. No 001 CR, Continuum update or publication is authorised here.
