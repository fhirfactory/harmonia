<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Increment 2 — executable ArchiMate Ingestion Change Review

**2026-10-18 — bounded documentation increment; Architecture Release 1.0.9.**

The governed Change Review contract and reusable template are ready for Solution
Architect review. This change establishes the detection-to-adjudication-to-execution
contract, not the reconciliation implementation. No current blind-test proposal
has been processed or adjudicated.

## Documents created and changed

| Document | Result |
| --- | --- |
| [Change Review contract](../../docs/markdown/governance/archimate-ingestion-change-review.md) | New authoritative RADS process contract, lifecycle, CR granularity, decision/instruction boundaries, dependencies and audit requirements. |
| [Governed template](../../docs/markdown/governance/templates/archimate-ingestion-change-review-template.md) | New reusable worked form with a compact linked summary, stable CR IDs, all four illustrative dispositions and detailed records. No invented architectural instances. |
| [Governance index](../../docs/markdown/governance/README.md) | Links the contract and template into existing governance. |
| [Ingestion processing](../../docs/archimate/processing/ingestion/README.md#run-layout) | Adds deterministic baseline/run naming and evidence placement; preserves existing historical guidance and observation bundles. |
| [Workspace guide](../../docs/archimate/README.md) and [framework guide](../../docs/archimate/framework/README.md) | Link the governed mechanism while retaining the unimplemented reconciliation-engine boundary and Increment 1 diagnostic restrictions. |
| [Release authority](../../architecture-release.json) | Patch transition 1.0.8 → 1.0.9, with previous_version 1.0.8. |

This report and its [evidence bundle](2026-10-18-archimate-ingestion-change-review/)
record validation. No implementation source, model, incoming export or historical
blind-analysis report is part of this task's changes.

## Decisions formalised

The task explicitly authorises this process contract. Repository authority was
loaded afresh: root/docs instructions, axioms, release policy, identity/naming,
navigation/View convention and mapping profile. AX-04 preserves meaning independently
of machinery; AX-14 preserves evidence/interpretation/decision/instruction and
semantic/navigation/presentation distinctions; AX-15 preserves uncertain execution
effects; AX-17 reserves missing architecture for explicit adjudication. No conflict
with these axioms was identified.

- A CR is independently adjudicable architecture, not an XML/GUI/file delta.
  One activity can yield several CRs; several observations can support one CR.
- Review-local CR-nnn IDs remain stable and never become Canonical IDs. The first
  visible content is a linked `ID | Brief Description | Status` dashboard, with an
  80-character description limit and unrestricted detail below.
- The only dispositions are Accept, Reject, On-Hold and Manual Intervention.
  Newly generated CRs default to On-Hold. Architectural disposition is distinct
  from subsequent execution/validation state.
- The Solution Architect decides architectural meaning. Accepted CRs require
  precise decision-linked instructions and source/target evidence; execution
  agents cannot broaden, reinterpret or improve the decision. Ambiguity returns
  to adjudication or deliberate manual resolution, with partial effects retained.
- Manual Intervention prohibits automatic execution and records human action and
  completion evidence. Prerequisites must produce the required evidenced governed
  state before dependent execution. Instance acceptance creates no universal rule.
- Observed evidence remains unchanged after adjudication. Semantic relationship
  creation/modification/removal and another View appearance remain distinct.
  Existing identity, naming, navigation, publication and release authority is cited.

The run convention is
`docs/archimate/processing/ingestion/<baseline-release>/run-<nnn>/`, containing
`change-review.md`, `detection/`, `reconciliation/` and `validation/` as evidence
becomes available. Allocate the next unused serial run under the exact comparison
baseline; preserve immutable inputs/revisions and never reuse the run identity.
No run or live blind-test CR was created during this documentation task.

All three blind analyses were read as empirical evidence, not architectural
authority. Their semantic/presentation distinctions, missing metadata and unresolved
interpretations inform the contract. The third report's unavailable exact second
XML remains an evidence limitation; no historical input was reconstructed.

## Release and validation

The starting root authority was 1.0.8 / previous 1.0.7. The existing helper applied
`bump --expect 1.0.8`, producing 1.0.9 / previous 1.0.8. No minor/major decision,
Maven version change or semantic architecture change was introduced.

| Verification | Result and evidence |
| --- | --- |
| Required architecture suite, offline Maven / JDK 21, 180-second bound | **PASS**, 90 tests, zero failures/errors/skips; Maven 30.798 seconds. [Log](2026-10-18-archimate-ingestion-change-review/architecture-tests.log.gz). |
| Release validator, 30-second bound | **PASS**, 1.0.9 / previous 1.0.8. [Log](2026-10-18-archimate-ingestion-change-review/release-check.log.gz). |
| Existing release-helper unit tests, 60-second bound | **PASS**, 3 tests. [Log](2026-10-18-archimate-ingestion-change-review/release-tests.log.gz). |
| RADS source/reference validation, 60-second bound | **PASS**, zero invalid; 2,467 existing unresolved architectural findings retained. [Log](2026-10-18-archimate-ingestion-change-review/rads-validation.log.gz). |
| Canonical metadata validation, 60-second bound | **PASS**, 1,299 existing valid Canonical IDs, no missing/malformed/duplicate/multiple claims. [Log](2026-10-18-archimate-ingestion-change-review/metadata-validation.log.gz). |
| Independent deterministic generation, 90-second bound | **PASS**, generated twice by existing tooling under `/tmp/harmonia-change-review-1.0.9/`, with source, identity, reference, endpoint and bundled offline XSD gates. [Log](2026-10-18-archimate-ingestion-change-review/independent-generation.log.gz), [generated validation](2026-10-18-archimate-ingestion-change-review/independent-generation-validation.json). |
| Independent output comparison | **PASS**, XML differs from governed 1.0.8 only in one model release value; all 10 generated RADS outputs byte-identical. [Markdown comparison](2026-10-18-archimate-ingestion-change-review/independent-markdown-comparison.json). No exchange package was written into the repository. |
| Documentation and preservation | **PASS**, local references/anchors in task documents, template summary/detail dispositions and 80-character limit, focused whitespace and protected-file hashes. [Validation](2026-10-18-archimate-ingestion-change-review/documentation-preservation-validation.json), [starting hashes](2026-10-18-archimate-ingestion-change-review/start-preservation.json), [check script](2026-10-18-archimate-ingestion-change-review/validate-documents.py). |
| Whole-worktree whitespace | Exit 2, 306 pre-existing supplied incoming XML CRLF/trailing-whitespace findings; that XML remains hash-identical. Task changes pass the focused check. [Results](2026-10-18-archimate-ingestion-change-review/whitespace-checks.json). |

Key commands, run from the repository root with `timeout --signal=TERM
--kill-after=5s` and the bounds above:

```bash
env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
python3 -B scripts/architecture_release.py check
python3 -B -m unittest discover -s scripts/tests -p 'test_architecture_release.py' -v
python3 -B scripts/rads_business_architecture.py validate
python3 -B docs/archimate/framework/tools/governed_metadata.py validate
python3 -B docs/archimate/framework/tools/synchronise_baseline.py --output-directory /tmp/harmonia-change-review-1.0.9
python3 -B .junie/reports/2026-10-18-archimate-ingestion-change-review/validate-documents.py
```

The initial regeneration attempt detected authority-source drift from an added
cross-reference in the hash-pinned mapping profile. That append was removed,
restoring the exact original mapping bytes; governance/ingestion guides provide
the integration links. The corrected run passed without changing any source pin
or validation rule. The [initial diagnostic](2026-10-18-archimate-ingestion-change-review/independent-generation-initial.log.gz)
is retained. No operation timed out or stalled.

The first documentation check reported its own not-yet-written validation output
as a missing linked file. Once that evidence existed, the check passed; its
initial result is retained in the evidence bundle. Final preservation establishes
452 of 457 pre-existing files unchanged, with only the five intended documentation/
release files changed. All incoming XML/native/backup files, semantic models,
blind-analysis reports and their existing evidence remain byte-identical.

Limitations: link/anchor checking covers task documents and their local targets,
using the existing RADS anchor helper; it is not a whole-repository link repair.
Pre-existing incoming XML/native workbench changes and staged/untracked blind
reports remain user work, preserved by start/end hashes. No GUI rendering or human
Archi inspection is claimed; no new repository publication was generated for
inspection. Existing architecture findings and historical evidence gaps remain open.

## Deliberate deferrals and stop point

Detection/decomposition automation, instruction-generation machinery,
reconciliation execution, retry/processing-state design and presentation replay,
View generation/layout remain unimplemented. Current blind-test identity,
definitions, governed names, classification/standing, relationship acceptance,
Realization/context interpretation, CT3/Feature Composition profile and View
acceptance remain for later Solution Architect adjudication.

**STOP:** the mechanism, template, patch release and bounded validation are
complete. No reconciliation, blind-test CR execution, identity allocation,
governed-name derivation or blind-test acceptance follows. Return this bounded
change set to the Solution Architect for review.
