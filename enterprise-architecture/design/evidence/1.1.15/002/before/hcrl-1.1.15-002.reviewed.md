<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Bounded deterministic round-trip discovery and normalization | Accept | Proposed opt-in implementation; 001 architectural CRs excluded |
| [DR-2](#dr-2) | Deterministic evidence preparation and proposed Praxis text | Accept | Existing governance remains operative pending acceptance |
| [DR-3](#dr-3) | Preserve empty authored View folders during regeneration | Accept | Optional proposed projector behavior; publication effect requires adjudication |
| [DR-4](#dr-4) | Bounded supporting Minutiae and validation limitations | Accept | Task time unresolved; validation results and unchanged-state proof retained |

# HCRL — proposed 1.1.15 machinery remediation, iteration 002

**Standing: pending Solution Architect adjudication; no architectural closure or
release allocation.** The immediate governed release remains **1.1.14**. The
[operative Task](../../evidence/1.1.15/002/before/ai-agent-tasks/unresolved-round-trip-remediation.md)
authorises bounded implementation, tests and a regression benchmark of the
existing round-trip machinery. Its subsequent governance clarification requires
the existing routine to remain in force until replacement behavior is accepted.
This iteration records that distinct engineering activity within the prospective
1.1.15 review boundary; it does not supersede or accept any 001 architectural CR.
The [001 review](change-review.md) and [001 HCRL](hcrl-1.1.15-001.pending.md)
remain unchanged empirical inputs. The root release file is unchanged.

Architecture Review is required because language normalization, navigation
classification, evidence preparation and the reasoning boundary affect
governance-supporting behavior. This is not an exemption for a tooling patch.
The common [Review Praxis](../../../governance/architecture-review-praxis.md),
[provenance contract](../../../governance/architecture-change-provenance.md) and
[Round-Trip Praxis](../../../governance/archimate-round-trip-engineering-praxis.md)
remain controlling. AX-04 is met by reusing the comparator, correlation and
projector; AX-08 by finite evidence maintenance; AX-14 by separating semantic,
transport, derived navigation and authored presentation changes; AX-15/17 by
preserving exceptions and reserving adjudication. No runtime architecture,
governance lifecycle, canonical identity rule or native metamodel is changed.

<a id="dr-1"></a>
## DR-1 — Bounded deterministic discovery and normalization

**Observed problem.** The unchanged 001 evidence contains 21,087 raw findings,
including 17,957 unchanged generated-property values acquiring `xml:lang="en"`,
2,803 positional organization findings, 296 known normalization findings and
the actual additions/View changes. Canonical intake stopped on the first new
element lacking canonicalId, before completing discovery. The expensive part
was agent investigation/review/provenance repetition, not the approximately
five-second comparison. The user's hours/~US$50 observation is a supplied
benchmark, not an independently reconstructed billing measurement.

**Proposed result.** `continuum_workbench.py review --normalised` emits a compact
architectural delta after the existing exact-current-projection validation.
The default review remains unchanged. The new path discovers all records using
the existing identity correlator and never allocates identities. Missing IDs on
new elements are expected incorporation work; invalid/lost/duplicate claims and
representation rebinding become explicit conditions. It distinguishes all new,
removed and changed semantic records, authored Views and View navigation.

The language rule is intentionally limited to the demonstrated absent → en
change on a single unchanged generated scalar, with exact key/owner, unchanged
other attributes and no conflicting inherited language. Other differences remain
reviewable. The raw comparator remains unchanged by default. The Task's explicit
navigation rule treats submitted non-View folders/identifiers/order as derived
serialization, regenerated through existing canonical rules after incorporation;
these alone create no architectural CR. View folders, empty folders, metadata,
placement, order and View content remain reviewable. Unknown conditions are
recorded and returned with no autonomous investigation. A 128,000-byte delta
limit explicitly requests bounded scope rather than silently discarding content.

The [regression delta](../../evidence/1.1.15/002/after/benchmark/architectural-delta.json)
and [benchmark result](../../evidence/1.1.15/002/after/benchmark/benchmark-summary.json)
demonstrate retained 12 added elements, 9 relationships, 7 Views and 17 existing
View navigation changes. No existing semantic element/relationship/View content
changed or disappeared. The normalized output, its limited context locators and
specific exceptions are the intended architectural reasoning input. The pipeline
performs no DRP corpus scan, autonomous AI call or semantic approval. Deterministic
processing is measured separately from the remaining targeted interpretation.
The measured deterministic pipeline took **11.666647 s**, including **0.040084 s**
of evidence preparation, producing **68,819 bytes** of readable delta from the
original 22,012,817-byte raw comparison. No whole-AI latency/token measurement
or guarantee of five-minute architectural interpretation is asserted.

**Disposition requested:** review the bounded rules and implementation for
adoption in ordinary round-trip preparation. No application directive is assumed.
No 001 CR has been applied, reclassified in its historical review, or accepted.

<a id="dr-2"></a>
## DR-2 — Deterministic evidence preparation and proposed Praxis text

`architecture_review_evidence.py` adds explicit Task/Report emission and
checkpoint scaffolding using the existing evidence structure, event fields and
HCRL/CR contracts. Task timestamps come from attributable instruction-entry
events or remain UNRESOLVED. Automated Report emission records the system UTC
event immediately after the completed initial write; filename/field alignment
is finite provenance maintenance. Explicit event sources remain necessary.

Preparation checks input hashes/sizes, iteration paths, event/prefix
correspondence and local Markdown file links. It refuses to overwrite existing
reviews/HCRLs. Its generated documents are explicitly incomplete scaffolds:
targeted architectural interpretation must still establish independent CRs and
HCRL meaning before the Solution Architect checkpoint. No CR identity or human
disposition is mechanically invented. The existing application adapter rejects
the proposed review schema rather than silently accepting unsupported changes.

The [proposed Praxis section](../../../governance/archimate-round-trip-engineering-praxis.md#proposed-bounded-preparation--1115-iteration-002-not-yet-operative)
and [tooling instructions](../../../tooling/README.md#proposed-bounded-review-preparation-pending-1115-002)
are marked unaccepted. They retain comparison → Change Review → pending HCRL →
Solution Architect → decision/application → validation/projection. Existing
defaults, including publication, remain operative. A specifically relevant DRP
may be read after bounded Continuum vicinity/context retrieval; no corpus-wide
DRP analysis is proposed. No new workflow/provenance/orchestration framework is
introduced. Existing review updates with human content remain explicit bounded
work rather than an automatic merge feature.

**Disposition requested:** adjudicate adoption of the proposed preparation
behavior and accompanying operational text. Successful tests are not acceptance.

<a id="dr-3"></a>
## DR-3 — Empty authored View folders and publication preservation

While testing preservation, the existing `continuum_navigation.project` pruning
was found to remove empty folders even within the named Views hierarchy. Enabling
full hierarchy preservation on the current template changes the projected bytes,
which correctly caused the exact-baseline gate to reject that experimental
projection. That publication change was not applied. The existing default and
accepted 1.1.14 projection remain unchanged.

The optional proposed `preserve_view_hierarchy=True` behavior preserves the full
authored Views branch while regenerating only non-View navigation. Regression
tests establish deterministic regeneration from absent or misleading non-View
folders, unchanged semantic/View signatures and preserved empty folder metadata.
No View-generation machinery is added. Activation for canonical publication is
reserved for explicit Solution Architect treatment of its publication effect.

**Disposition requested:** determine the intended treatment of existing empty
template folders and adoption of the preservation option. This does not block
reviewing the 001 delta or normal architecture authoring; application involving
affected empty View folders remains dependent on this disposition. Do not
silently regenerate the accepted publication to make the baseline gate pass.

<a id="dr-4"></a>
## DR-4 — Bounded supporting Minutiae, verification and limits

The formal Task, engineering Report and selected regression evidence belong to
002. Original 001 evidence and human review remain with 001. The Task's original
instruction-entry time and the governance follow-up's time are unavailable in
the exposed interaction, so their event timestamps/filename prefix remain
UNRESOLVED for Solution Architect treatment before closure. No current-clock,
filesystem-time or historical-session guess substitutes for them.

The engineering Report records actual test counts, deterministic elapsed time,
scope, source/result correspondence, read-only publication checks and any
required verification limitation. Automated PASS establishes only its stated
checks. Actual Archi transport/visual inspection and subsequent architectural
interpretation are not performed by the benchmark. No token/cost measurement is
fabricated. No source history, index, branch, tag or remote mutation is authorised.

**Actual verification:** the full EA run passed 79 tests (288.835 s); final
focused suites passed 10 normalized-review and 16 evidence tests, supplementing
that earlier full run. Offline Maven passed 90 Java architecture tests with
zero failures/errors/skips (27.251 s) after the online resolver encountered a
read-only cache. Read-only publication/link checks and `git diff --check` passed.
The [validation summary](../../evidence/1.1.15/002/after/validation-summary.json)
records unchanged hashes for 1,619 protected files and final replay equality
apart from measured elapsed time. No 001 CR was applied, no canonical source,
candidate, original review, template or accepted publication changed, no release
increment occurred, and no Git mutation was performed. No blocking engineering
defect was identified in the demonstrated preparation path. Recommend returning
to architecture/solution development after adjudication, deferring non-blocking
tooling extensions until concrete friction warrants them. Actual Archi proof
and the unresolved Task time retain their stated limits.

Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect]

**Checkpoint:** STOP for Solution Architect disposition. No review outcome,
acceptance, application, release increment, Git mutation or HCRL completion is
inferred from this pending document.

[Engineering Report](../../evidence/1.1.15/002/after/ai-agent-reports/20261025T204108Z-round-trip-remediation.md): actual implementation, empirical findings, validation, bounded exceptions and recommendation. Production event: 2026-10-25T20:41:08.833670Z. This evidence reference supplies no Solution Architect acceptance.
