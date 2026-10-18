<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# ArchiMate workspace restructure — preflight stop

Date: 2026-10-18

Status: **STOPPED — release-governance adjudication required before migration**

Starting checkpoint: `107cf351f0a784d8d43a9942e0443390ea98e74e`

Architecture Release: **1.0.7**, previous release **1.0.6**

This is an unaccepted working preflight report, not a completed migration report
or an architectural decision. No workspace files have been moved or edited.
The task's explicit stop condition was reached during authority inspection.

## Governance conflict and decision required

[Architecture release policy, Authority and scope](../../docs/markdown/governance/architecture-release-policy.md#authority-and-scope)
states:

> Every accepted repository change SHALL increment the RAD / Architecture patch
> release, regardless of author or tool. One accepted change set receives one
> increment, including its governance documents, tests and generated artefacts.

[AGENTS.md section 1.3](../../AGENTS.md#13-rad--architecture-release-governance)
also requires every accepted repository change to apply that patch increment.
Neither inspected provision supplies a repository-location-only exception.

The task requires retaining Release 1.0.7 and explicitly says:

> If an existing release-policy rule appears to require otherwise, stop and report
> the conflict for Solution Architect adjudication rather than inventing a release
> transition.

Those requirements cannot both be satisfied for acceptance of the migration under
the currently documented policy. The existing request is not treated as an
implicit exception because it expressly requires adjudication at this boundary.
No release transition or policy amendment has been inferred.

**Required Solution Architect decision:** whether to authorise and record a
task-specific exception to the patch-increment rule for this structure-only
migration, including its tooling, documentation, tests and report, so the
accepted architecture release can remain 1.0.7. This report proposes that
decision for review; it does not grant it. No permission to increment the
architecture release is requested or inferred.

AX-17 requires explicit architectural authority and preservation of unresolved
decisions. AX-14 requires preservation of semantic distinctions. They support
raising this conflict and keeping architectural acceptance, workspace relocation
and unperformed validation distinct.

## Starting inventory

The initial Git worktree was clean. The checkpoint contains 183
tracked files under `docs/archimate/`, all within
`docs/archimate/capability-architecture/`.

| Existing location | Tracked files |
| --- | ---: |
| `(workspace-root files)` | 14 |
| `baseline/` | 3 |
| `recovery-v0.2/` | 26 |
| `schemas/` | 7 |
| `synchronised-baseline/` | 109 |
| `tests/` | 10 |
| `tools/` | 14 |

The complete tracked-file inventory captured at preflight is in Appendix A.
Ignored/untracked filesystem content has not been comprehensively audited.
The tracked inventory is not represented as completion of the task's full
filesystem inventory or path-assumption audit.

## Source → destination mapping and classification

No source → destination mapping has been approved or executed. Responsibility
classification remains pending. The required categories remain FRAMEWORK,
PUBLISHED EXCHANGE ARTEFACT, INGESTED EXCHANGE ARTEFACT, HISTORICAL / FROZEN
EVIDENCE, DOCUMENTATION and UNCERTAIN.

The requested destination responsibilities are recorded here only as task scope:

| Requested boundary | Intended responsibility |
| --- | --- |
| `docs/archimate/framework/` | Existing publication, validation, comparison and recovery machinery and support material |
| `docs/archimate/exchange/publish/` | Governed generated publications and inspectable release history |
| `docs/archimate/exchange/ingest/` | Incoming Archi exports requiring comparison/reconciliation before acceptance |

A file-by-file responsibility review must precede movement after adjudication.
No ambiguous file destination has been resolved through inference.

## References and preservation

A preliminary repository search used
`rg --hidden --glob '!.git/**' --count-matches 'docs/archimate/capability-architecture'`.
It found 73 files with this literal path. Appendix B preserves
that result, excluding this newly written report. This is a literal-path search,
not a completed audit of indirect, relative or constructed path assumptions.

No operational references, manifests, schemas, fixtures, release metadata,
generated reports or historical references have been changed. Operational versus
frozen classification is pending; no historical narrative has been rewritten.
Release 1.0.6 and Increment 1 evidence, and the Release 1.0.7 identity-establishment
and empirical Archi acceptance evidence, remain at their checked-in locations.

## Implementation and final tree

No framework/publish/ingest directories have been created. The final tracked
`docs/archimate/` tree is identical to the starting tree in Appendix A.
No compatibility mechanism has been added. Tooling, command paths, build
configuration, tests, fixtures and operational documentation remain unchanged.

The only work product from this preflight is this new, unaccepted report.
No commit has been created.

## Release 1.0.7 preservation and validation status

All previously tracked repository files remain unchanged from the checkpoint.
That preserves the existing files without asserting independent regeneration
or re-verification of their architectural meaning.

| Required result | Preflight status |
| --- | --- |
| 1,299 semantic elements, Canonical IDs, governed names and types | Existing publication/source files untouched; no post-migration comparison performed |
| Documentation and semantic Properties | Existing files untouched |
| 852 relationships and their endpoints | Existing files untouched; no post-migration comparison performed |
| 14 EC bindings | Existing files untouched; no post-migration comparison performed |
| Views, visual objects/connections, geometry, bendpoints and navigation | Existing files untouched; no post-migration comparison performed |
| Canonical ID allocation/modification | None performed |
| Accepted Release 1.0.7 publication | Unchanged at its existing location |
| Independent regeneration and byte reproduction | NOT RUN — migration stopped before implementation |
| Generated-output byte differences | None introduced; regeneration comparison not performed |
| Release 1.0.6 historical evidence | Files untouched; recovery not rerun |
| Release 1.0.7 real-Archi acceptance evidence | Files untouched; empirical conclusions not altered or independently rerun |
| Architecture release authority | Unchanged: version 1.0.7, previous_version 1.0.6 |

No expected hashes, fidelity classifications or comparison normalisation have
been changed.

## Complete execution results

| Activity | Result |
| --- | --- |
| Initial `git status --short` | PASS — clean before this report |
| Final `git diff --exit-code HEAD` and `git status --short` | PASS — tracked files unchanged; only this report is untracked |
| Checked-in release authority inspection | PASS — 1.0.7 / previous 1.0.6 |
| Repository/docs instructions and release-policy inspection | Conflict established; explicit task stop condition applies |
| Tracked workspace inventory | Captured in Appendix A |
| Literal old-path reference search | Captured in Appendix B; broader assumption/classification audit pending |
| Publication/recovery tests | NOT RUN — stopped before implementation |
| Round-trip tests | NOT RUN — stopped before implementation |
| Identity/naming tests | NOT RUN — stopped before implementation |
| Identity/release tests | NOT RUN — stopped before implementation |
| Repository architecture suite | NOT RUN — stopped before implementation |
| New-location commands and publish/ingest boundary validation | NOT RUN — boundaries not implemented |
| Independent regeneration / publication comparison | NOT RUN — stopped before implementation |
| Historical recovery/evidence verification | NOT RUN — checked-in evidence preserved unchanged |

No test failure, timeout or stall has been observed. Unexecuted checks are not
reported as passes.

## Outstanding work and recommendation

The release-policy conflict is unresolved. File responsibility classification,
the movement mapping, indirect path/dependency analysis, migration, operational
documentation, focused path tests, independent generation and every required
post-migration verification remain outstanding.

**Architecture Release remains 1.0.7. Increment 2 has not begun.**
Increment 2 readiness is not established by this preflight. Obtain the explicit
Solution Architect release-governance adjudication, then resume this bounded
workspace migration and its required validations before considering Increment 2.

## Appendix A — complete starting tracked-file inventory

Paths are repository-relative and record the before-state.

```text
docs/archimate/capability-architecture/.gitignore
docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml
docs/archimate/capability-architecture/Makefile
docs/archimate/capability-architecture/README.md
docs/archimate/capability-architecture/baseline/Harmonia-Capability-Architecture-v2.1a.xml
docs/archimate/capability-architecture/baseline/navigation-baseline-1.0.3.json
docs/archimate/capability-architecture/baseline/semantic-lock.json
docs/archimate/capability-architecture/documentation-map.json
docs/archimate/capability-architecture/documentation-publication-report.md
docs/archimate/capability-architecture/navigation-release-report.md
docs/archimate/capability-architecture/navigation-release-verification.json
docs/archimate/capability-architecture/recovery-v0.2/1.0.5A-recovery-report.md
docs/archimate/capability-architecture/recovery-v0.2/Harmonia-Architecture-1.0.5A.xml
docs/archimate/capability-architecture/recovery-v0.2/archi-inspection-guide.md
docs/archimate/capability-architecture/recovery-v0.2/architecture-release.json
docs/archimate/capability-architecture/recovery-v0.2/corpus-drift.json
docs/archimate/capability-architecture/recovery-v0.2/recovery-findings.json
docs/archimate/capability-architecture/recovery-v0.2/representation-crosswalk.json
docs/archimate/capability-architecture/recovery-v0.2/source-manifest.json
docs/archimate/capability-architecture/recovery-v0.2/validation.json
docs/archimate/capability-architecture/recovery-v0.2/verification.json
docs/archimate/capability-architecture/recovery-v0.2/verification/.gitignore
docs/archimate/capability-architecture/recovery-v0.2/verification/architecture-tests.log
docs/archimate/capability-architecture/recovery-v0.2/verification/definition-concordance.log
docs/archimate/capability-architecture/recovery-v0.2/verification/diff.log
docs/archimate/capability-architecture/recovery-v0.2/verification/identity-validation.log
docs/archimate/capability-architecture/recovery-v0.2/verification/initial-maven.log
docs/archimate/capability-architecture/recovery-v0.2/verification/jdk25-diagnostic.txt
docs/archimate/capability-architecture/recovery-v0.2/verification/legacy-v02-check.log
docs/archimate/capability-architecture/recovery-v0.2/verification/make-all.log
docs/archimate/capability-architecture/recovery-v0.2/verification/publication-tests.log
docs/archimate/capability-architecture/recovery-v0.2/verification/rad-validation.log
docs/archimate/capability-architecture/recovery-v0.2/verification/regeneration.log
docs/archimate/capability-architecture/recovery-v0.2/verification/release.log
docs/archimate/capability-architecture/recovery-v0.2/verification/script-tests.log
docs/archimate/capability-architecture/recovery-v0.2/verification/snapshot-tests.log
docs/archimate/capability-architecture/recovery-v0.2/verification/snapshot-validation.log
docs/archimate/capability-architecture/reference-id-reconciliation-report.md
docs/archimate/capability-architecture/release-verification.json
docs/archimate/capability-architecture/requirements.txt
docs/archimate/capability-architecture/roundtrip-increment-1.md
docs/archimate/capability-architecture/schemas/LICENSE.txt
docs/archimate/capability-architecture/schemas/archimate3_Diagram.xsd
docs/archimate/capability-architecture/schemas/archimate3_Model.xsd
docs/archimate/capability-architecture/schemas/archimate3_View.xsd
docs/archimate/capability-architecture/schemas/dc.xsd
docs/archimate/capability-architecture/schemas/provenance.json
docs/archimate/capability-architecture/schemas/xml.xsd
docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md
docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-1.0.7.xml
docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml
docs/archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md
docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-archi-guide.md
docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-establishment.md
docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-validation.json
docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-verification.json
docs/archimate/capability-architecture/synchronised-baseline/corrections-preservation.json
docs/archimate/capability-architecture/synchronised-baseline/documentation-correction-verification.json
docs/archimate/capability-architecture/synchronised-baseline/human-inspection-start-preservation.json
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/architecture.json
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/manifest.json
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/motivation.json
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/architecture-release.json
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/architecture.json
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/business-capabilities.md
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/business-enabling-capabilities.md
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/discrepancies.json
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/enterprise-capabilities.md
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/features.md
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/functions.md
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/motivation.json
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/motivation.md
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/processes.md
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/relationships.md
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/prior-release/services.md
docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-report.md
docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-verification.json
docs/archimate/capability-architecture/synchronised-baseline/pre-human-inspection-synchronisation-report.md
docs/archimate/capability-architecture/synchronised-baseline/pre-human-inspection-verification.json
docs/archimate/capability-architecture/synchronised-baseline/projection-start-preservation.json
docs/archimate/capability-architecture/synchronised-baseline/validation.json
docs/archimate/capability-architecture/synchronised-baseline/verification.json
docs/archimate/capability-architecture/synchronised-baseline/verification/.gitignore
docs/archimate/capability-architecture/synchronised-baseline/verification/architecture.log
docs/archimate/capability-architecture/synchronised-baseline/verification/baseline_contract.log
docs/archimate/capability-architecture/synchronised-baseline/verification/bindings.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/architecture.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/baseline-tests.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/bindings.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/diff.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/documentation-samples.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/generation.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/generation_check.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/identity.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/independent_regeneration.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/preservation.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/publication.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/rad.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/readme-lifecycle.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/recovery_replay.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/regeneration_comparison.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/release.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/report.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/scripts.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/snapshot.log
docs/archimate/capability-architecture/synchronised-baseline/verification/corrections/snapshot_tests.log
docs/archimate/capability-architecture/synchronised-baseline/verification/diff.log
docs/archimate/capability-architecture/synchronised-baseline/verification/generation_xsd.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/archi_sort.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/architecture.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/audit.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/bindings.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/check.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/final-workflow.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/generation.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/identity.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/publication.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/rad.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/recovery.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/release.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/scripts.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/snapshot-check.py
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/snapshot.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/snapshot_tests.log
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/starting-candidate-audit.py
docs/archimate/capability-architecture/synchronised-baseline/verification/identity.log
docs/archimate/capability-architecture/synchronised-baseline/verification/independent_regeneration.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/all-output-comparison.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/archi-cli-display.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/archi-cli-no-display.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/architecture.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/bindings.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/diff.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/generation_check.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/generation_xsd.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/identity.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/independent_regeneration.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/preservation.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/publication.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/rad.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/recovery_replay.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/regeneration_comparison.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/release.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/report-check.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/report-independent.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/scripts.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/snapshot.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/snapshot_tests.log
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/structural_audit.log
docs/archimate/capability-architecture/synchronised-baseline/verification/publication.log
docs/archimate/capability-architecture/synchronised-baseline/verification/rad.log
docs/archimate/capability-architecture/synchronised-baseline/verification/recovery_replay.log
docs/archimate/capability-architecture/synchronised-baseline/verification/regeneration_comparison.log
docs/archimate/capability-architecture/synchronised-baseline/verification/release.log
docs/archimate/capability-architecture/synchronised-baseline/verification/scripts.log
docs/archimate/capability-architecture/synchronised-baseline/verification/snapshot.log
docs/archimate/capability-architecture/synchronised-baseline/verification/snapshot_tests.log
docs/archimate/capability-architecture/tests/fixtures/roundtrip/future-contract.xml
docs/archimate/capability-architecture/tests/fixtures/roundtrip/manifest.json
docs/archimate/capability-architecture/tests/test_baseline.py
docs/archimate/capability-architecture/tests/test_documentation.py
docs/archimate/capability-architecture/tests/test_governed_metadata.py
docs/archimate/capability-architecture/tests/test_navigation.py
docs/archimate/capability-architecture/tests/test_publication_metadata.py
docs/archimate/capability-architecture/tests/test_recovery.py
docs/archimate/capability-architecture/tests/test_roundtrip.py
docs/archimate/capability-architecture/tests/test_views.py
docs/archimate/capability-architecture/tools/assess_rad.py
docs/archimate/capability-architecture/tools/diagnose_roundtrip.py
docs/archimate/capability-architecture/tools/feature_projection.py
docs/archimate/capability-architecture/tools/generate_views.py
docs/archimate/capability-architecture/tools/governed_metadata.py
docs/archimate/capability-architecture/tools/publish_documentation.py
docs/archimate/capability-architecture/tools/publish_navigation.py
docs/archimate/capability-architecture/tools/recover_v02.py
docs/archimate/capability-architecture/tools/report_baseline.py
docs/archimate/capability-architecture/tools/report_navigation.py
docs/archimate/capability-architecture/tools/report_publication.py
docs/archimate/capability-architecture/tools/report_recovery.py
docs/archimate/capability-architecture/tools/synchronise_baseline.py
docs/archimate/capability-architecture/tools/verify_baseline_outputs.py
docs/archimate/capability-architecture/validation.json
docs/archimate/capability-architecture/view-generation-report.md
```

## Appendix B — preliminary literal-path reference inventory

Each line records `repository-relative file:match count`. Historical reports,
generated evidence and current tooling occur together in these search results;
their individual roles have not yet been adjudicated.

```text
scripts/architecture_identity.py:7
scripts/tests/test_architecture_identity.py:3
.junie/reports/2026-10-18-archimate-roundtrip-increment-1.md:8
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/input-preservation.json:160
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/established-diagnostic.json:1
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/resumed-preservation-result.json:2
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/core-regeneration.json:2
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/analyse-archi-export.py:1
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/initial-preflight-report.md:5
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/established-diagnostic-summary.json:1
.junie/reports/2026-10-18-archimate-roundtrip-least-step-plan.md:7
.junie/reports/2026-10-18-archimate-roundtrip-increment-1/active-diagnostic-summary.json:1
.junie/reports/2026-10-18-archimate-canonical-identity-establishment.md:6
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/establishment-files.json:28
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/active-diagnostic-summary.json:1
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/empirical-command-validation.json:3
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/final-preservation.json:5
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-proof-manifest.json:1
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/empirical-test-validation.json:2
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/empirical-validation.json:190
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/roundtrip-diagnostic.json:1
.junie/reports/2026-10-18-archimate-canonical-identity-establishment/active-diagnostic.json:1
docs/archimate/capability-architecture/roundtrip-increment-1.md:5
docs/archimate/capability-architecture/documentation-publication-report.md:13
docs/archimate/capability-architecture/navigation-release-report.md:2
docs/archimate/capability-architecture/release-verification.json:15
docs/archimate/capability-architecture/recovery-v0.2/verification.json:83
docs/archimate/capability-architecture/baseline/navigation-baseline-1.0.3.json:6
docs/archimate/capability-architecture/navigation-release-verification.json:27
docs/archimate/capability-architecture/recovery-v0.2/source-manifest.json:6
docs/archimate/capability-architecture/view-generation-report.md:2
docs/archimate/capability-architecture/recovery-v0.2/1.0.5A-recovery-report.md:32
docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-establishment.md:4
docs/archimate/capability-architecture/tests/test_documentation.py:1
docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-archi-guide.md:3
docs/archimate/capability-architecture/tests/test_navigation.py:1
docs/archimate/capability-architecture/synchronised-baseline/documentation-correction-verification.json:197
docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md:27
docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-report.md:71
docs/archimate/capability-architecture/tests/fixtures/roundtrip/manifest.json:2
docs/archimate/capability-architecture/synchronised-baseline/pre-human-inspection-synchronisation-report.md:190
docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-verification.json:188
docs/archimate/capability-architecture/synchronised-baseline/pre-human-inspection-verification.json:260
docs/archimate/capability-architecture/synchronised-baseline/corrections-preservation.json:26
docs/archimate/capability-architecture/synchronised-baseline/projection-start-preservation.json:33
docs/archimate/capability-architecture/synchronised-baseline/human-inspection-start-preservation.json:36
docs/archimate/capability-architecture/tools/report_baseline.py:3
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/starting-candidate-audit.py:1
docs/archimate/capability-architecture/tools/report_navigation.py:1
docs/archimate/capability-architecture/tools/report_publication.py:1
docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/final-workflow.log:2
docs/archimate/capability-architecture/synchronised-baseline/verification.json:181
docs/archimate/capability-architecture/tools/publish_navigation.py:1
docs/archimate/capability-architecture/tools/report_recovery.py:1
docs/markdown/governance/baseline/README.md:3
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/generation_check.log:1
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/publication.log:1
docs/markdown/governance/baseline/correspondence.json:8
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/regeneration_comparison.log:3
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/all-output-comparison.log:4
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/independent_regeneration.log:1
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/structural_audit.log:1
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/archi-cli-no-display.log:2
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/generation_xsd.log:1
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/recovery_replay.log:1
docs/archimate/capability-architecture/synchronised-baseline/verification/projection/archi-cli-display.log:2
docs/archimate/capability-architecture/synchronised-baseline/initial-synchronisation-verification.json:94
docs/archimate/capability-architecture/synchronised-baseline/identity-establishment-1.0.7/architecture.json:1
docs/markdown/governance/architecture-identity/release-report.md:4
docs/markdown/governance/architecture-identity/verification.json:22
docs/markdown/governance/architecture-identity/adoption-baseline-1.0.2.json:4
docs/markdown/governance/architecture-identity/inventory.json:417
docs/markdown/governance/baseline/architecture.json:1
```

## Solution Architect adjudication and resumed movement plan

The Solution Architect rejected an exception and authorised the normal 1.0.7 → 1.0.8
patch transition for this workspace/tooling release. Release 1.0.7 remains the
immutable empirical identity/naming baseline. The earlier preflight stop is resolved
by this later adjudication; its account above remains as execution history.
No architectural semantic/presentation change is authorised.

Before movement, [movement-plan.json](2026-10-18-archimate-workspace-restructure/movement-plan.json)
records every filesystem source, destination, classification, rationale and SHA-256,
including tracked status and incidental ignored caches.
[before-evidence.json](2026-10-18-archimate-workspace-restructure/before-evidence.json)
pins wider source/empirical evidence. Full path-reference discovery is in
[reference-inventory.txt](2026-10-18-archimate-workspace-restructure/reference-inventory.txt).
Framework inputs, locks, schemas, tools and tests move to framework/. Publications
and release/recovery evidence move to exchange/publish/. Existing .junie empirical
exports stay unchanged. Ingest receives boundary documentation only. The mixed old
README is preserved as framework/historical-workspace-readme.md; new operational
documentation will replace its current entry-point role.

Publication 1.0.8 must independently reproduce its complete outputs byte-for-byte,
with exact semantic/presentation comparison to preserved 1.0.7 and a full account
of release/location metadata differences. No new Archi run or Increment 2 work
is required or authorised.
