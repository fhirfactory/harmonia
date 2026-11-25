<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Topic and TopicMask DRP filename reconciliation inventory

**Standing** · Review evidence and tested proposal. The reconciliation patch
awaits Solution Architect disposition and an applicable Architecture transition.

## Entry state and identity mapping

Entry branch `feature/BL-0003-Calliope-Topic-and-TopicMask`, HEAD
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`. The index was clean and the
local upstream divergence was 0/0. Root release authority was 1.1.26,
predecessor 1.1.25. Pre-existing working changes comprised Calliope POM and
sources, the convergence plan, the two Junie handoff/plan files, Backlog Item
0003 revision 000.003 and its retained review input, and the Architecture
1.1.28/001 and Software 0.1.0/010 pending reviews and evidence.

| Original offline-design path | Proposed primary path in offline-design | Entry Git blob | Entry SHA-256 |
| --- | --- | --- | --- |
| [DRP-Topic-and-TopicMask.Final.001.000.md][old-first] | `DRP-Topic-and-TopicMask.Final.001.md` | `7ef5bc87e052cd81d0d0226bf561156a22632885` | `1690ca87200db65d2254acfb5af68b7b960fb044934682b6ed89eb77ce9c5e40` |
| [DRP-Topic-and-TopicMask.Final.001.001.md][old-second] | `DRP-Topic-and-TopicMask.Final.002.md` | `5b55f6b8998cdfa4df7806c1b5fd63d94326b7ab` | `e6136e3fb25c1a120b36d79f6f49ffa50b3b1c388f59f0408fa9afcf4667ed73` |

**Final.001** · Foundational conceptual approval and bounded incorporation
are recorded in [completed Architecture 1.1.25/001 DR-1 and DR-3][rad].
The cover already says “Final 001” and §6 retains its original pending
complete-approval wording. Preserve both with the recorded approval scope.
The [retained 1.1.25 input][retained] has the same SHA-256 as the original.
The proposed primary copy is byte-identical too.

**Final.002** · Mark Hunter accepted the exact historical Draft.001.003
with qualifications in [Software HCRL 004 DR-10][acceptance], with
[007 finalisation evidence][finalisation]. The exact acceptance time remains
unavailable. The cover's Standing label and first Final lineage link/label
are the only changes in the proposed primary copy. Other lineage labels,
including the reviewed source draft, remain historical. §§1–10, the Java
surface, acceptance tests and qualifications are byte-identical. Proposed
Final.002 SHA-256 is
`0b828ecb0e4c578a2d2cb1eff06797d4cc54e44e1dc37230295aaf8110c41bca`.

The [reconciliation patch][rename] uses copies to preserve both original
paths as byte-exact compatibility locators. This protects Git and Markdown
readers, anchored links, exact source bindings and historical evidence.
Each pair corresponds to one DRP release. After authorised application,
new primary citations use the corrected paths. The current draft uses the
available exact historical file pending adoption of the mapping. Request
explicit disposition before applying the patch or retiring either historical
locator. A destructive move, stub or symlink could affect original bytes,
section resolution, or portable Git rendering.

The Continuum validity issue at
`design/evidence/1.1.25/001/before/accepted-input/DRP-Topic-and-TopicMask.Final.001.000.md`
is a separate pre-existing retained-input locator, with two canonical
`retainedAt` references. The offline-design mapping cannot correct it. The
existing exact [DR-5 locator disposition][locator] governs its incorporation.
Preserve the old diagnostic references and reviewed patches.

## Incoming filename references at entry

The case-insensitive basename search covered the 12,906 tracked and
nonignored untracked paths in the candidate worktree. It searched readable
raw bytes and 1,391 gzip or ZIP-format containers, including the compressed
accepted Backlog Item review input. It found 93 literal occurrences on
90 lines in 49 carriers. The inventory covers exact filename and filename-stem
spellings, including paths in code, metadata and historical patches. It
does not claim coverage of refs outside this checkout, ignored work files,
other revisions or text encoded in opaque binary formats. Line numbers for
the compressed backlog artefact refer to its decompressed bytes.

Leave all listed references and historical labels intact. References in
retained HCRL snapshots and old logs retain their originating context.
Existing prose describing the then-intended file names is historical evidence.

Of 70 rendered local links to the original offline-design basenames in the
entry Markdown carriers, 64 resolve with their fragments. The six exceptions
are three rendered uses each in the two snapshots under
`1.1.28/001/before/review-disposition/`. Those verbatim copies use relative
paths from the originating [live HCRL][live-review]. The same references resolve
from that HCRL. Preserve the snapshots with their original links.

| Carrier relative to repository root | Entry line(s) |
| --- | --- |
| [.junie/plans/naming-0003-implementation-task.proposed.md](../../../../../../.junie/plans/naming-0003-implementation-task.proposed.md) | 75 |
| [docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md](../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md) | 152, 153, 154, 155, 156 |
| [docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md](../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md) | 272, 274, 275 |
| [docs/backlog/evidence/0003/000.003-reviewed.md.gz [decompressed]](../../../../../../docs/backlog/evidence/0003/000.003-reviewed.md.gz) | 243, 245, 246 |
| [enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md](../../../../../continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md) | 37, 40, 51 |
| [enterprise-architecture/continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md](../../../../../continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md) | 37, 40, 51 |
| [enterprise-architecture/design/evidence/1.1.28/001/after/01-retained-topic-input-locator.patch](01-retained-topic-input-locator.patch) | 5, 8, 17, 20 |
| [enterprise-architecture/design/evidence/1.1.28/001/after/02-topic-topicmask-continuum.patch](02-topic-topicmask-continuum.patch) | 20, 35, 71, 82, 86, 274, 285, 289, 517, 532, 611, 626, 675, 689 |
| [enterprise-architecture/design/evidence/1.1.28/001/after/ai-agent-reports/20261124T054105Z-topic-topicmask-continuum-reconciliation.md](ai-agent-reports/20261124T054105Z-topic-topicmask-continuum-reconciliation.md) | 121 |
| [enterprise-architecture/design/evidence/1.1.28/001/after/verification.json](verification.json) | 13, 166 |
| [enterprise-architecture/design/evidence/1.1.28/001/before/ai-agent-tasks/unresolved-topic-topicmask-continuum-reconciliation.md](../before/ai-agent-tasks/unresolved-topic-topicmask-continuum-reconciliation.md) | 33 |
| [enterprise-architecture/design/evidence/1.1.28/001/before/review-disposition/hcrl.before-disposition-completion.md](../before/review-disposition/hcrl.before-disposition-completion.md) | 125, 202 |
| [enterprise-architecture/design/evidence/1.1.28/001/before/review-disposition/hcrl.before-dr-6-revision.md](../before/review-disposition/hcrl.before-dr-6-revision.md) | 118, 173 |
| [enterprise-architecture/design/evidence/software-0.1.0/002/after/F-01-Decision-Delta.md](../../../software-0.1.0/002/after/F-01-Decision-Delta.md) | 35 |
| [enterprise-architecture/design/evidence/software-0.1.0/002/after/ai-agent-reports/20261120T004322Z-f-01-execution-report.md](../../../software-0.1.0/002/after/ai-agent-reports/20261120T004322Z-f-01-execution-report.md) | 51 |
| [enterprise-architecture/design/evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md](../../../software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md) | 7 |
| [enterprise-architecture/design/evidence/software-0.1.0/003/after/F-01-Decision-Delta.md](../../../software-0.1.0/003/after/F-01-Decision-Delta.md) | 72 |
| [enterprise-architecture/design/evidence/software-0.1.0/003/after/F-01-Execution-Report.md](../../../software-0.1.0/003/after/F-01-Execution-Report.md) | 47 |
| [enterprise-architecture/design/evidence/software-0.1.0/003/before/ai-agent-tasks/unresolved-f-01-design-consolidation-task.md](../../../software-0.1.0/003/before/ai-agent-tasks/unresolved-f-01-design-consolidation-task.md) | 71 |
| [enterprise-architecture/design/evidence/software-0.1.0/004/after/F-01-Architecture-Reconciliation.md](../../../software-0.1.0/004/after/F-01-Architecture-Reconciliation.md) | 32 |
| [enterprise-architecture/design/evidence/software-0.1.0/004/after/F-01-Decision-Delta.md](../../../software-0.1.0/004/after/F-01-Decision-Delta.md) | 25 |
| [enterprise-architecture/design/evidence/software-0.1.0/005/after/Calliope-G02-Architectural-Reconciliation-005.md](../../../software-0.1.0/005/after/Calliope-G02-Architectural-Reconciliation-005.md) | 179 |
| [enterprise-architecture/design/evidence/software-0.1.0/007/after/ai-agent-reports/20261123T204533Z-topic-topicmask-finalisation-report.md](../../../software-0.1.0/007/after/ai-agent-reports/20261123T204533Z-topic-topicmask-finalisation-report.md) | 29, 37 |
| [enterprise-architecture/design/evidence/software-0.1.0/007/before/ai-agent-tasks/unresolved-topic-topicmask-finalisation-task.md](../../../software-0.1.0/007/before/ai-agent-tasks/unresolved-topic-topicmask-finalisation-task.md) | 33, 94 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md](../../../software-0.1.0/008/after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md) | 133 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T010622Z-calliope-backlog-revision-2-preparation.md](../../../software-0.1.0/008/after/ai-agent-reports/20261124T010622Z-calliope-backlog-revision-2-preparation.md) | 102 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T031123Z-calliope-topic-topicmask-corrective-implementation.md](../../../software-0.1.0/008/after/ai-agent-reports/20261124T031123Z-calliope-topic-topicmask-corrective-implementation.md) | 137 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T032411Z-calliope-topic-topicmask-corrective-verification.md](../../../software-0.1.0/008/after/ai-agent-reports/20261124T032411Z-calliope-topic-topicmask-corrective-verification.md) | 102 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/UNRESOLVED-calliope-topic-topicmask-implementation-report.md](../../../software-0.1.0/008/after/ai-agent-reports/UNRESOLVED-calliope-topic-topicmask-implementation-report.md) | 12 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/before/ai-agent-tasks/UNRESOLVED-topic-topicmask-implementation-task.md](../../../software-0.1.0/008/before/ai-agent-tasks/UNRESOLVED-topic-topicmask-implementation-task.md) | 26 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-backlog-revision-2-preparation.md](../../../software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-backlog-revision-2-preparation.md) | 31, 183 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-topic-topicmask-corrective-task.md](../../../software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-topic-topicmask-corrective-task.md) | 23 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-topic-topicmask-corrective-verification.md](../../../software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-topic-topicmask-corrective-verification.md) | 29 |
| [enterprise-architecture/design/evidence/software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-topic-topicmask-independent-review.md](../../../software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-topic-topicmask-independent-review.md) | 29, 222 |
| [enterprise-architecture/design/evidence/software-0.1.0/009/after/ai-agent-reports/20261124T045808Z-software-foundation-reset.md](../../../software-0.1.0/009/after/ai-agent-reports/20261124T045808Z-software-foundation-reset.md) | 71 |
| [enterprise-architecture/design/evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md](../../../software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md) | 145, 184 |
| [enterprise-architecture/design/evidence/software-0.1.0/010/after/ai-agent-reports/unresolved-naming-0003-implementation-report.md](../../../software-0.1.0/010/after/ai-agent-reports/unresolved-naming-0003-implementation-report.md) | 24 |
| [enterprise-architecture/design/evidence/software-0.1.0/010/before/ai-agent-tasks/20261124T082329Z-naming-0003-implementation-task.md](../../../software-0.1.0/010/before/ai-agent-tasks/20261124T082329Z-naming-0003-implementation-task.md) | 75 |
| [enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.002.md](../../../../offline-design/DRP-Information-Unit-Framework.Final.002.md) | 15 |
| [enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.revised.draft.002.003.md](../../../../offline-design/DRP-Information-Unit-Framework.revised.draft.002.003.md) | 15 |
| [enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md](../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md) | 10 |
| [enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.001.md](../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md) | 10 |
| [enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.002.md](../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md) | 8 |
| [enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.003.md](../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md) | 8 |
| [enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md](../../../../review/1.1.28/hcrl-1.1.28-001.pending.md) | 138, 217 |
| [enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md](../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md) | 370, 374, 375, 376, 377 |
| [enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-007.pending.md](../../../../review/software-0.1.0/hcrl-software-0.1.0-007.pending.md) | 25 |
| [enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-008.pending.md](../../../../review/software-0.1.0/hcrl-software-0.1.0-008.pending.md) | 156 |
| [enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-010.pending.md](../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md) | 19 |

## Other filename exceptions for later disposition

Scope is direct DRP-prefixed files under offline-design at entry. The
programme index and template are excluded, as are the two mapped Finals.
The 81 syntax exceptions need individual standing and lineage checks.
Do not infer accepted standing from a name or repair an ambiguous stem,
baseline or draft number mechanically. Even a conforming filename can
carry an unaccepted design, as recorded for PetasosParcel in 1.1.25 DR-1.

- Unversioned or unrecognised suffix · 4
- Two Final components · 2
- Lowercase Draft · 46
- Lowercase and one draft component · 27
- Lowercase and nonconforming extension or extra dot · 2

| Filename | Observed exception |
| --- | --- |
| `DRP-Agora-Omilia-DigitalTwin-Agentic-Interaction.md` | Unversioned or unrecognised suffix |
| `DRP-Calliope-Identity-and-Classification-Framework.Final.001.000.md` | Two Final components |
| `DRP-Calliope-Identity-and-Classification-Framework.draft.000.001.md` | Lowercase Draft |
| `DRP-Calliope-Identity-and-Classification-Framework.draft.000.002.md` | Lowercase Draft |
| `DRP-Calliope-Lifecycle-Revision-and-Operational-State.Final.001.000.md` | Two Final components |
| `DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md` | Lowercase Draft |
| `DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.002.md` | Lowercase Draft |
| `DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md` | Lowercase Draft |
| `DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.002.md` | Lowercase Draft |
| `DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md` | Unversioned or unrecognised suffix |
| `DRP-Harmonia-Managed-Persistence-Service.draft.001.md` | Lowercase and one draft component |
| `DRP-Harmonia-OAM-UI.draft.003.md` | Lowercase and one draft component |
| `DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.001.md` | Lowercase Draft |
| `DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.002.md` | Lowercase Draft |
| `DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.003.md` | Lowercase Draft |
| `DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.004.md` | Lowercase Draft |
| `DRP-Information-Unit-Framework.draft.001.001.md` | Lowercase Draft |
| `DRP-Information-Unit-Framework.draft.001.002.md` | Lowercase Draft |
| `DRP-Information-Unit-Framework.draft.001.003.md` | Lowercase Draft |
| `DRP-Information-Unit-Framework.draft.002.001.md` | Lowercase Draft |
| `DRP-Information-Unit-Framework.draft.003.odt` | Lowercase and nonconforming extension or extra dot |
| `DRP-Information-Unit-Framework.revised.draft.002.002.md` | Lowercase Draft |
| `DRP-Information-Unit-Framework.revised.draft.002.003.md` | Lowercase Draft |
| `DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.001.001.md` | Lowercase Draft |
| `DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.001.002.md` | Lowercase Draft |
| `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md` | Lowercase Draft |
| `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md` | Lowercase Draft |
| `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.001.md` | Lowercase and one draft component |
| `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md` | Lowercase and one draft component |
| `DRP-Interaction-Mneme-to-Mnemosyne.draft.001.001.md` | Lowercase Draft |
| `DRP-Interaction-Mneme-to-Mnemosyne.draft.001.002.md` | Lowercase Draft |
| `DRP-Interaction-Mneme-to-Mnemosyne.draft.001.003.md` | Lowercase Draft |
| `DRP-Interaction-Mneme-to-Mnemosyne.draft.001.004.md` | Lowercase Draft |
| `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.001.md` | Lowercase and one draft component |
| `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md` | Lowercase and one draft component |
| `DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md` | Lowercase Draft |
| `DRP-Interaction-Petasos-to-Mneme.draft.000.003.md` | Lowercase Draft |
| `DRP-Interaction-Petasos-to-Mneme.draft.000.004.md` | Lowercase Draft |
| `DRP-Interaction-Petasos-to-Mneme.draft.001.md` | Lowercase and one draft component |
| `DRP-Interaction-Petasos-to-Mneme.draft.002.md` | Lowercase and one draft component |
| `DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md` | Unversioned or unrecognised suffix |
| `DRP-Mneme-Audit.draft.001.md` | Lowercase and one draft component |
| `DRP-Mneme-Audit.draft.002.md` | Lowercase and one draft component |
| `DRP-Mneme-Cache-Architecture.draft.001.001.md` | Lowercase Draft |
| `DRP-Mneme-Cache-Architecture.draft.001.003..md` | Lowercase and nonconforming extension or extra dot |
| `DRP-Mneme-Cache-Architecture.draft.001.004.md` | Lowercase Draft |
| `DRP-Mneme-Cache-Architecture.draft.002.md` | Lowercase and one draft component |
| `DRP-Mneme-Pragma-Model.draft.000.003.md` | Lowercase Draft |
| `DRP-Mneme-Pragma-Model.draft.000.004.md` | Lowercase Draft |
| `DRP-Mneme-Pragma-Model.draft.001.md` | Lowercase and one draft component |
| `DRP-Mneme-Pragma-Model.draft.002.md` | Lowercase and one draft component |
| `DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.001.md` | Lowercase and one draft component |
| `DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md` | Lowercase and one draft component |
| `DRP-Mneme-Pragmaton-Model.draft.000.003.md` | Lowercase Draft |
| `DRP-Mneme-Pragmaton-Model.draft.000.004.md` | Lowercase Draft |
| `DRP-Mneme-Pragmaton-Model.draft.001.md` | Lowercase and one draft component |
| `DRP-Mneme-Pragmaton-Model.draft.002.md` | Lowercase and one draft component |
| `DRP-Mneme-Security.draft.001.md` | Lowercase and one draft component |
| `DRP-Mneme-Security.draft.002.md` | Lowercase and one draft component |
| `DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md` | Lowercase and one draft component |
| `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.001.md` | Lowercase and one draft component |
| `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md` | Lowercase and one draft component |
| `DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.001.md` | Lowercase and one draft component |
| `DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md` | Lowercase and one draft component |
| `DRP-Mnemosyne-Data-Persistence-Sharding.draft.001.md` | Lowercase and one draft component |
| `DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md` | Lowercase and one draft component |
| `DRP-Mnemosyne-DurableInformation-and-DurableData.draft.000.001.md` | Lowercase Draft |
| `DRP-OperationalMetadata-and-OperationalMetadataMask.draft.001.001.md` | Lowercase Draft |
| `DRP-OperationalMetadata-and-OperationalMetadataMask.draft.001.002.md` | Lowercase Draft |
| `DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md` | Lowercase Draft |
| `DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md` | Lowercase Draft |
| `DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md` | Lowercase Draft |
| `DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md` | Lowercase Draft |
| `DRP-Pragma-Publication-and-Subscription.draft.000.001.md` | Lowercase Draft |
| `DRP-Pragma-Publication-and-Subscription.draft.000.002.md` | Lowercase Draft |
| `DRP-Pragma-Publication-and-Subscription.draft.000.003.md` | Lowercase Draft |
| `DRP-Themis-Policy-Passport-Enforcement.draft.001.md` | Lowercase and one draft component |
| `DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md` | Unversioned or unrecognised suffix |
| `DRP-Topic-and-TopicMask.draft.001.001.md` | Lowercase Draft |
| `DRP-Topic-and-TopicMask.draft.001.002.md` | Lowercase Draft |
| `DRP-Topic-and-TopicMask.draft.001.003.md` | Lowercase Draft |

[old-first]: ../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.000.md
[old-second]: ../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[retained]: ../../../1.1.25/001/before/accepted-input/DRP-Topic-and-TopicMask.Final.001.md
[rad]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-1
[acceptance]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[finalisation]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-007.pending.md#dr-1
[locator]: ../../../../review/1.1.28/hcrl-1.1.28-001.pending.md#dr-5
[rename]: topic-drp-final-filename-reconciliation.patch
[live-review]: ../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
