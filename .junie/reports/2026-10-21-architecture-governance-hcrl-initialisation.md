<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Governance — Initial Pending HCRL

**Date:** 2026-10-21, Australia/Sydney

**Prepared by:** Codex, submitter; no architectural acceptance authority

**Standing:** Working task report; not an architecture authority or a final
semantic acceptance record.

## Created artefacts and release proposal

Created the initial [pending HCRL][hcrl] at:

```text
enterprise-architecture/design/review/1.1.2/hcrl-1.1.2-001.pending.md
```

This report is the second and only other intended addition:

```text
.junie/reports/2026-10-21-architecture-governance-hcrl-initialisation.md
```

The proposed transition is **RAD-1.1.1 → RAD-1.1.2**. Root
[architecture-release.json][release-json] records `version=1.1.1`,
`previous_version=1.1.0`; the [release policy][release] supplies the next patch.
No explicit major/minor decision was supplied. Preparation creates neither an
accepted governance change nor a newly allocated release; root metadata remains
unchanged. Any eventual acceptance must recheck the immediate predecessor and
apply the appropriate increment in the same accepted change set.

The inspected commit was `2cbd9ded6be9f38cb4f18fe2ec083a7375750945`, branch
`ArchitecturalGovernance`. Tracked content was clean at entry; the existing
untracked [discovery report][discovery] was the only pre-existing workspace
addition. The HCRL records its exact SHA-256 separately from the commit baseline.
That report is preserved unchanged.

Only the ancestors required for the pending HCRL location have been established
under the previously absent `enterprise-architecture/`. No wider target subtree
has been created.

## Authority and HCRL content

The user's supplied A01–A12 adjudications are the authoritative input for this
task and supersede conflicting candidate recommendations in the discovery report.
The HCRL embeds them in Appendix A so subsequent reviewers can read the actual
decisions without session context. The transcription preserves decision content
and labels, with Markdown layout adapted. No separate ARB meeting date, signature
or ChatGPT concurrence is invented; the document distinguishes task/transcription
date from unavailable meeting provenance.

Repository-held authority loaded for the proposal includes root instructions,
the materially relevant [Architectural Axioms][axioms], [Architecture Change
Provenance][provenance], release policy, modelling strategy, specialised Change
Review/Authoring/Round-Trip contracts and identity rules. The pending HCRL explains
consistency with AX-04, AX-08, AX-09, AX-14, AX-15 and AX-17. Existing review and
provenance capabilities supply a foundation; no new runtime responsibility,
service or HCRL engine is inferred.

The HCRL distinguishes supplied decisions, Codex's proposed translation, proposed
application actions and actual preparation. Mark Hunter, Solution Architect,
retains final architectural decision, acceptance and closure authority. No
implementation permission or acceptance of this translation is claimed.

## Material matters and durable knowledge

| HCRL matter | Supplied adjudication translated |
| --- | --- |
| M01 | A01: collaborative ARB review; final Solution Architect authority; no agent approval, voting/quorum or inferred delegation |
| M02 | A02: one authoritative semantic HCRL per immediate governed release transition; specialised reviews feed it |
| M03 | A03: adjudicated repository/review/register/evidence structure, naming, partial-approval exclusion and supersession of the mandatory three-file package |
| M04 | A04: explicit Yes/No acceptance of a bounded Minutiae group; no per-item `ACCEPTED_WITHOUT_COMMENT` |
| M05 | A05: Architecture Review for every Harmonia change; judgement and adjudication of materiality; Git source history; bounded architectural exemplars only |
| M06 | A06: ArchiMate/native-concept and governed-specialisation taxonomy; corresponding continuum/model navigation; no invented types |
| M07 | A07: publish useful candidates using judgement; disclose uncertainty and improve through review without acquiring authority |
| M08 | A08: superseded by A03/A06; no separate knowledge master index or Domain13 catalogue |
| M09 | A09: preserve canonical identity where established; scoped references and explicit correspondence elsewhere |
| M10 | A10: Solution Architect confirms authorised and actually incorporated result; immutable completion, attributable non-semantic errata and later material transitions |
| M11 | A11: prospective adoption; no blanket historical reconstruction |
| M12 | A12: implementation discovers material architectural issues and surfaces them for adjudication; it does not authorise architecture |

Section 4 submits eleven scoped durable-knowledge statements, K01–K11, using
proposed native `<Constraint>` and `<Principle>` tags. These are reviewable
classifications, not canonical IDs, newly accepted model elements or newly
invented Harmonia specialisations. Governance sources and their corresponding
metamodel-aligned continuum placement require the specific mapping noted below.

P01–P06 propose review, explicit migration mapping, common-governance
reconciliation, bounded structural application, consumer adaptation/validation
and human-confirmed closure. Dependencies and expected evidence are recorded;
none of these application actions has been performed or turned into an
executable directive by this task.

## Minutiae

The bounded N01–N04 group contains the task details, discovery-report provenance,
this submission's formatting/scoped references/minimal scaffolding and this task
report. The material decisions and governance meanings are separately represented
in M01–M12 and are not demoted to minutiae.

The single `Minutiae-Accepted: Yes | No` control is left explicitly unanswered
with a comment placeholder. Codex has not selected Yes or No. Silence does not
accept the group, and later iterations must incorporate human correction,
escalation or reclassification.

## Unresolved application details

These questions refine application and do not reopen settled A01–A12:

| HCRL reference | Detail preserved for adjudication |
| --- | --- |
| U01 | Required before/after evidence selection and representation after superseding the old mandatory archive package; no automatic evidence waiver or replacement snapshot mandate |
| U02 | Exact native-concept/specialisation mapping and continuum organisation, including correspondence between architecture-governance sources and their continuum/model navigation |
| U03 | Concrete source-to-destination/reference/consumer inventory and migration sequence, including publication material; no inferred migration of all `docs/` or legacy LaTeX |
| U04 | Pending-iteration retention/allocation and the attributable erratum carrier/location, preserving completed-record immutability |
| U05 | Actual adoption/acceptance boundary and explicit reconciliation of the older separately authorised historical migration/pilot reference |

The HCRL records dependencies and the option for explicit deferral/exclusion
under A10. Codex has made no architectural completion or non-blocking-deferral
decision.

## Conflicts with existing governance

The existing provenance contract requires exactly `before.tar.gz`, `after.tar.gz`
and `change-review.md`, with exact changed-file archive rules. A02/A03 replace
that final record/package design. The HCRL reports the conflict rather than
quietly retaining compulsory archives or assuming transition evidence is no
longer needed.

Root instructions and the current governance index still define RADS as
`docs/markdown`; references, publication paths and specialised review/Praxis
contracts use the existing layout and `change-review.md` checkpoints. A03 and A06
require reviewed structural and consumer reconciliation. The existing ArchiMate
strategy remains binding; the task does not redesign or run it.

The discovery report's independent taxonomy, knowledge-guide/master-index and
per-item no-comment disposition recommendations are displaced by A03/A04/A06/A08.
Prospective Domain13 statements need reconciliation under those decisions. The
older provenance adoption's intended 1.0.10 → 1.1.0 migration/pilot is not this
first prospective HCRL and receives no execution authority here.

The every-accepted-change patch rule, specialised native/identity semantics,
legacy original-content preservation and unique-content/recovery safeguards
remain applicable. No conflict with an Architectural Axiom was established by
this bounded review; outstanding authority/application details remain explicit.

## Verification

| Check | Result and practical limit |
| --- | --- |
| Required architecture suite | **PASS:** 90 tests across 11 architecture-test classes; zero failures, errors or skips. Maven BUILD SUCCESS, 19.250 seconds. This establishes existing implementation conformance, not acceptance of the governance proposal. |
| Release authority | **PASS:** `python3 -B scripts/architecture_release.py check`; current 1.1.1, previous 1.1.0. No bump invoked. |
| Markdown and references | **PASS:** CommonMark/table parsing and temporary HTML rendering of both additions; local file links, explicit anchors, reference definitions, fenced blocks, inline type-tag visibility and whitespace checked. |
| Preservation and scope | **PASS:** hashes of all 13 recorded input files, including root instructions, release metadata and discovery report, match entry. Tracked and staged diffs are empty. Untracked paths are the pre-existing discovery report and the two intended additions. The new architecture tree contains only the pending HCRL and its required ancestors. |
| Submission structure | **PASS:** A01–A12 and M01–M12 anchors, eleven proposed durable statements, four bounded minutiae, five open application details and the unanswered Minutiae control verified. This structural check does not substitute for review of their architectural interpretation. |
| Model/publication/migration execution | Not performed: no ArchiMate intake, WIP-ingest inspection, model reconciliation, publication generation, evidence migration, historical replay or disposal. |

The architecture command was bounded to 240 seconds with a five-second
termination grace; it completed normally:

```bash
timeout --signal=TERM --kill-after=5s 240s mvn test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

The temporary engineering log is
`/tmp/harmonia-hcrl-initialisation-architecture-tests.log`. A temporary
documentation/scope checker and HTML renderings remain under `/tmp`; none is
installed machinery, architectural authority or an additional transition-evidence
component. No new repository tests were introduced. Successful checks do not
accept, implement or complete the proposed governance transition.

## Canonical Documentation Assessment

The existing `.junie` discovery report remains a working submission. Its findings
were checked against maintained governance sources where relevant, and its
recommendations were not copied into current architecture as authority. The
explicit adjudications came from the user's task and are preserved in the HCRL,
not reverse-derived from that report or implementation.

This report remains execution provenance. Proposed durable governance meaning
is submitted in the HCRL for review and later authorised incorporation into
appropriate governed sources. No legacy architectural content was migrated,
rewritten or marked superseded in this task; no supersession wrapper was needed
for unchanged content. No source-code history or processing workspace was copied
as architectural evidence.

## Change boundary

The intended result is two new Markdown files and the minimum review-directory
ancestors. No existing file, release metadata, governed architecture/governance,
agent instruction, implementation, model, publication, machinery, frozen record
or historical evidence is changed. No register/evidence package, completed HCRL,
historical reconstruction or processing disposal is performed. The candidate is
ready for review; adoption, application and closure remain unperformed.

[hcrl]: ../../enterprise-architecture/design/review/1.1.2/hcrl-1.1.2-001.pending.md
[discovery]: 2026-10-21-architecture-governance-hcrl-discovery-and-design.md
[release-json]: ../../architecture-release.json
[release]: ../../docs/markdown/governance/architecture-release-policy.md
[provenance]: ../../docs/markdown/governance/architecture-change-provenance.md
[axioms]: ../../docs/markdown/governance/architectural-axioms.md
