<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Increment 2C — Architecture Authoring and Round-Trip Praxis

**2026-10-18 — Architecture Release 1.0.11; bounded documentation increment.**

The practical [architecture authoring entry point](../../docs/archimate/README.md)
and its [normative RADS Praxis](../../docs/markdown/governance/architecture-authoring-praxis.md)
are ready for Solution Architect review. The lifecycle is serial: start from an
identified publication, author progressively, submit once and close the iteration,
adjudicate, reconcile authorised changes, validate and publish before further
governed authoring. No semantic architecture model change or blind-test processing
was performed.

## Documentation and authority

| File | Change |
| --- | --- |
| [docs/archimate/README.md](../../docs/archimate/README.md) | Existing canonical human entry point updated with a ten-step quick-start, working model, submission guidance and fictional worked example. Existing workspace navigation remains. Only the superseded architectural notes are wrapped with specific RADS replacement references; their original text is unchanged. |
| [Architecture Authoring Praxis](../../docs/markdown/governance/architecture-authoring-praxis.md) | New normative lifecycle contract in RADS, linked from the existing entry point. It is governance support, not a competing human entry point or XML-processing specification. |
| [Governance index](../../docs/markdown/governance/README.md) | Links 2C and its practical entry point alongside 2A/2B. |
| [Incoming exchange](../../docs/archimate/exchange/ingest/README.md) | Adds authoring navigation and the submission/closure reference; existing content remains unchanged. |
| [Ingestion processing](../../docs/archimate/processing/ingestion/README.md) | Links 2C closure rules and distinguishes run/evidence identity from permission for another submission; existing 2A layout/mechanics remain unchanged. |
| [Baseline correspondence](../../docs/markdown/governance/baseline/correspondence.json) | Adds only the new Praxis document's current-authority hash. All prior authority pins, semantic correspondence and recovery/history pins remain unchanged. |
| [Release authority](../../architecture-release.json) | Patch increment 1.0.10 → 1.0.11, with previous_version 1.0.10. |

Fresh repository authority was read: root/docs instructions, axioms, 2A, 2B,
identity/naming, navigation/View, mapping, baseline correspondence and release
policy. AX-04 keeps meaning with governed architecture and tools as machinery;
AX-14 preserves distinctions between lifecycle stages, semantics and presentation;
AX-15 preserves interrupted-processing uncertainty; AX-17 preserves architectural
gaps and requires adjudication. No conflict or runtime/subsystem boundary change
was identified. Existing README architecture statements were reconciled against
these sources and the explicitly authorised 2C task before the new RADS contract
was written; legacy content was not treated as independent authority.

## Praxis, terminology and responsibilities

| Acceptance coverage | Established rule |
| --- | --- |
| 1–5 | Repository is the governed system of record; Markdown RADS are the textual source, Archi the workbench, Open Exchange interchange and LaTeX a publication representation. Editing starts from one identifiable applicable publication, not an arbitrary old Archi file or a release number presumed to have a package. |
| 6–10 | Authoring Iteration is mutable working state with progressive edits and repeated saves. Architects author intent, native types/Names, permitted qualifiers, explicit relationships and useful Views. Machinery supplies authorised derived metadata, navigation and deterministic Views. Metadata inspection is allowed. Folders, geometry and visual appearances cannot manufacture semantic edges. |
| 11–16 | Submission is a deliberate handover retaining exact baseline/input/run/hash provenance. It closes the iteration and does not imply acceptance. 2A controls independent CR adjudication and executable instructions; 2B controls authorised interpretation and representation. No disposition or semantic rule was redefined. |
| 17–20 | Further governed authoring begins from the next applicable publication after actual reconciliation, validation and gates. Current lineage is serial, with no competing descendant submissions or branching/merge mechanism. Recovery retains closed-iteration state, actual effects, evidence and unresolved meaning. |
| 21–24 | Quick-start and fictional Workshop Booking lifecycle are present. Existing governance is linked; release handling and bounded validation are recorded below. No blind-test example was adopted. |

Architecture Release, Publication, Published Baseline, Authoring Iteration,
Submission and Change Review have separate definitions. The practical distinctions
are explicit: save ≠ submission; submission ≠ acceptance; acceptance ≠ reconciliation;
reconciliation ≠ publication; authoring iteration ≠ architecture release.

Architects/contributors retain architectural intent, relationships, uncertainty,
provenance and deliberate submission responsibilities; only the Solution Architect
adjudicates CR dispositions and architectural questions. Authorised processing
and agents handle evidence and deterministic machinery within accepted scope.
Agents do not supply missing architecture. During the governance interval,
review, evidence inspection and prospective discussion are allowed; further
governed submissions wait for the next publication.

The 2A run convention's predecessor-linked handling of distinct incoming evidence
does not grant a second submission from a closed iteration. Same-input review or
instruction revisions and governed processing recovery remain distinguishable
from a new authoring submission. No new recovery system was designed.

## Release and validation

The root authority at task start was **1.0.10 / previous 1.0.9**.
`python3 -B scripts/architecture_release.py bump --expect 1.0.10` applied the
policy-defined patch increment to **1.0.11 / previous 1.0.10**. This is one
reviewable change set; no commit or human acceptance is claimed. Release rules
and Maven software versions were not changed.

The [evidence bundle](2026-10-18-archimate-increment-2c/) retains the initial
2,948-file SHA-256 snapshot (including the pre-existing untracked incoming backup),
original README, exact bounded commands/results, logs, pin change and read-only
documentation/preservation audit. The audit script writes evidence only.

| Verification | Result |
| --- | --- |
| Required Maven architecture suite, offline with JDK 21, 180s bound | **PASS**, 90 tests, zero failures/errors/skips; Maven 29.384s. |
| Existing release/identity unit tests, 60s bound | **PASS**, 25 tests in 29.980s. |
| Release check, 30s bound | **PASS**, 1.0.11 / previous 1.0.10. |
| RADS source/reference validation, 60s bound | **PASS**, zero invalid; 2,467 existing unresolved findings retained. |
| Canonical identity/naming metadata validation, 60s bound | **PASS**, 1,299 valid Canonical IDs; none missing, malformed, duplicate or multiply claimed. |
| Independent existing-publisher regeneration under /tmp, 90s bound | **PASS**, two deterministic generations with existing source/identity/reference/endpoint and offline bundled XSD gates. |
| Documentation/reference, preservation and generated-output audit, 30s bound | **PASS**, 89 task-document local links/anchors and whitespace checked; original wrapped text preserved; 2,942 existing files unchanged and only the six allowed existing files changed. No protected source, publication, incoming XML or prior evidence changed. Generated XML differs from 1.0.8 only in one model-level release value; all ten generated RADS files are byte-identical. |

All commands used `timeout --signal=TERM --kill-after=5s` with the bounds above;
Exact invocations and exit codes are in `command-results.json`; outputs are in
the corresponding logs.
No timeout or stall occurred. The temporary reproduction is not a repository
exchange package or new Published Baseline; no ingestion run or CR was created.
Documentation caused no semantic, presentation or generated RADS drift.

The existing publisher's 2B Name/displayName conformance findings and unimplemented
Containment View archetype remain limitations. Successful preservation-only
reproduction does not assert conformance to those new rules or authorise a
migration. No GUI import/rendering or human Archi acceptance is claimed.

## Deferred matters and stop

Ingestion/detection/decomposition automation, executable-instruction generation,
reconciliation machinery, presentation replay and Containment View layout remain
future implementation work. Architectural branches, merge/conflict machinery and
parallel reconciliation streams require future explicit governance. Existing
Name/displayName migration and unresolved CT3/Feature or Realization/context
decisions remain deferred under 2B.

Blind-test incoming XML, its pre-existing backup and all blind-analysis evidence
retain their original bytes. No blind-test proposal was parsed for detection,
adjudicated, reconciled or allocated a Canonical ID by this task. The fictional
example confers no architectural acceptance.

**STOP:** Increment 2C documentation, patch handling and bounded validation are
complete. Return to the Solution Architect for review; do not begin an engine,
blind-test processing or another architecture increment.
