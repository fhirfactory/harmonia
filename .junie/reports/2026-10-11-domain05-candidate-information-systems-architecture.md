<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 Candidate Information Systems Architecture — Completion Report

**Date:** 2026-10-11

**Result:** initial reconstruction completed for **Step 1 — Document the Candidate
Architecture**. Domain05 and the final Solution Architecture baseline are not
declared complete. This report is an execution record; the candidate architecture
is held in RADS.

## 1. Scope and architectural standing

Established the initial **Harmonia Candidate Information Systems Architecture**
at [docs/markdown/05-information-systems-architecture](../../docs/markdown/05-information-systems-architecture/README.md).
This replaces the earlier Application Architecture framing for the current
Domain05 activity. Existing solution thinking was reconstructed from documentary
sources within established RADS constraints; another solution was not derived
from Domains01–04.

The relevant axioms are AX-01–04 for information purpose, native models and
technology independence; AX-05/11/12 for active access, durable establishment and
developer support; AX-06–09 for authority, security, evidence and transient state;
AX-10/14/15/16 for recoverability, truthful outcomes, uncertainty and progression;
AX-13 for publication; and AX-17/18 for explicit uncertainty and bounded domain
authority. The reconstruction preserves these constraints and records conflicting
historical descriptions. It introduces no new runtime boundary or product choice.

Candidate inclusion establishes documentary visibility, not approval of release
necessity. Missing upstream traceability does not invalidate a documented candidate.
Accepted ADRs retain their governance role subject to the axioms; documentary
supersession does not rescind their decisions.

**No upstream capability-to-component mapping, Business/Enterprise/Business
Enabling Capability coverage analysis, Information Architecture coverage analysis
or upstream requirements gap analysis was performed.** The 34 recorded gaps
concern the candidate documentation itself. **Excess-feature analysis, MVP
determination, final baselining and implementation planning remain future steps.**
Steps 2–5 were not commenced. No production implementation or test code changed.

## 2. RADS and navigation changes

Eight substantive Domain05 documents were created:

| File | Content |
| :--- | :--- |
| [README](../../docs/markdown/05-information-systems-architecture/README.md) | Orientation, candidate standing, five-step approach, scope and instructed semantic decisions. |
| [Candidate system map](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Responsibility-based hierarchy and separate collaboration diagram/table. |
| [Component register](../../docs/markdown/05-information-systems-architecture/component-register.md) | 22 significant components, behaviours and cross-component/support constructs. |
| [Candidate feature map](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md) | 34 source-described features, allocations, information interactions and standing. |
| [Data and persistence](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md) | 17 coupled logical/application Data Object groups; information/state/persistence responsibilities and competing structures. |
| [Internal middleware](../../docs/markdown/05-information-systems-architecture/middleware.md) | Nine internal collaboration responsibilities, documented examples and external/technology boundaries. |
| [Documentation/component-feature gaps](../../docs/markdown/05-information-systems-architecture/gaps.md) | CDG-01–34, with evidence, incomplete meaning and possible human adjudication. |
| [Sources and reconciliation](../../docs/markdown/05-information-systems-architecture/sources.md) | Authority constraints, 31 legacy source groups, source scopes, dispositions and supersession references. |

The [documentation index](../../docs/README.md) now links Domain05. The
[Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md)
records the authorised Information Systems Layer framing and Step 1 boundary.
Historical upstream completion and transition records were preserved. No files
within Domains01–04 were modified.

## 3. Files inspected and legacy sources used

The [linked source inventory](../../docs/markdown/05-information-systems-architecture/sources.md)
records individual files, inspected sections and dispositions. Its source keys
are documentary navigation identifiers, not requirement/capability mappings.

Governance and controlling RADS inspected comprised root `AGENTS.md`,
`docs/AGENTS.md`, the Architecture Completion Plan, Architectural Axioms,
`02-strategy/strategic-views/logical-component-responsibilities.md`,
`04-information-architecture/information-families/task-work.md`,
`04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md`
and `04-information-architecture/guardrails/observable-information-and-domain-meaning.md`.
These were used for established constraints, not upstream coverage evaluation.
The explicit task decisions are separately recorded as U01.

Material legacy files inspected and used are grouped below; paths are relative
to the repository root and linked in the source inventory:

| Source keys | Files / inspected documentary areas |
| :--- | :--- |
| L01–L05 | `docs/architecture/execution-model.md`; `docs/concepts/ponos.md`, `ergon.md`, `praxis.md`, `pragma.md`. |
| L06–L08 | `docs/concepts/calliope-canonical.md`, `calliope.md`, `themis.md`, `themis-security.md`; `docs/security/audit.md`. |
| L09–L10 | `docs/concepts/hestia.md`, `mneme.md`, `mnemosyne.md`, `hestia-persistence.md`. |
| L11–L14 | `docs/concepts/iris.md`, `agora.md`; `docs/architecture-decisions.md` ADR-001–020; `docs/design/governed-write-concurrency-contract.md`. |
| L15 | `docs/provider-registry/architecture.md`, `resource-model.md`, `change-processing.md`, `persistence.md`, `search.md`, `audit-provenance.md`. |
| L16–L17 | `docs/persistence-architecture.md`, `database-schema.md`, `architecture/persistence-lifecycle.md`; `docs/middleware/overview.md`, `hapi-fhir.md`, `infinispan.md`. |
| L18–L20 | `docs/latex/chapters/03-application-layer.tex`, `06-workflow-energeia.tex`, `appendix-praxis-workflow.tex`, `appendix-ergon-module.tex`, `04-data-architecture.tex`, `10-postgresql-persistence.tex`, `appendix-i-persistence-register.tex`. |
| L21 | `docs/architecture/iris-operations-console.md`, `iris-design-system.md`. |
| L22–L23 | `docs/concepts/paradeigma.md`; `docs/paradeigma/architecture.md`; `docs/modules/energeia.md`, `hestia.md`, `iris.md`; `hestia/mnemosyne-operations-cli/README.md`. |
| L24 | `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`, extracted paragraphs, especially §§5–9/13. |
| L25 | `docs/latex/diagrams/fig-app-overview-5tier.tex`, `fig-praxis-workflow-architecture.tex`, `fig-ergon-module-architecture.tex`, `fig-hestia-data-grid.tex`, `fig-iris-architecture.tex`, `fig-petasos-messaging.tex`, `fig-pragma-state-flow.tex`. |
| L26–L28 | `docs/architecture/overview.md`, `runtime-architecture.md`, `system-inventory.md`; `docs/getting-started/architecture-at-a-glance.md`; `docs/latex/chapters/appendix-backlog.tex` BL-CLIN-007/008; `docs/message-lifecycle.md`. |
| L29–L31 | `petasos/docs/architecture.md`; `docs/architecture.md`; `docs/concepts/petasos.md`, `petasos-messaging.md`, `pylai.md`, `pylai-gateways.md`; `docs/modules/dokimasia.md`. |

Broad discovery also searched documentary filenames and text for components,
relationships, state, media/binary, terminology and persistence. DOCX and
`docs/libreoffice/harmonia-architecture-specification.odt` text was extracted
read-only. The ODT was supporting publication material. `docs/latex/main.pdf`
was discovered; editable LaTeX was used rather than independently interpreting
the PDF as authority. A final targeted media/terminology search also inspected
matching passages in `docs/middleware/postgresql.md`,
`docs/architecture/convergence-report.md` and
`docs/latex/chapters/appendix-requirements.tex`; these supplied no new component
allocation or upstream requirement evaluation.

The [previous assessment](2026-10-10-domain05-application-architecture-assessment.md),
historical plans/reports, legacy index and OpenSpec inventories served as discovery
navigation only. The earlier assessment was already present as an untracked file
and was left unchanged. Its Q01–Q05 sequence and proposed next task were not adopted.
Code/module/class structure was not used to invent component hierarchy or missing
architecture; module documentation was treated as supporting documentary evidence.

## 4. Reconstructed hierarchy and major features

The [system map](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md)
separates the following documented structural groupings from collaboration:

- Calliope semantic/model/converter support; Themis policy; Kleio evidence.
- Hestia grouping: Mneme active access/coordination and Mnemosyne durable
  establishment/preservation, with clinical and operational responsibility slices.
- Energeia grouping: Ponos execution; Ergo/Ergon/Erga behaviour; Praxis definition/
  composition design area; Pragma carrier. These are not four nested execution tiers.
- Petasos opaque internal work transfer and recovery; Pylai standards-facing
  interaction/publication responsibility slices.
- Iris clinical, console and administration presentation, mediation and shared
  design support; Agora identity/context, room/membership and event/handoff slices.
- Digital Twin, Dokimasia, PragmaFactory, Provider Registry collaboration and
  support tools are represented without an invented structural parent.
  Paradeigma remains associated simulation/verification support outside production
  dependency paths.

Major documented candidate features include shared models and HL7/FHIR conversion;
governed dispatch/execution/composition; resource validation/enrichment, order/result
processing and destination fan-out; checkpoint/progression observation; active
access and conditional durable establishment; directory changes/search; evidence
and deliberate replay; standards publication; contextual presentation and
operations diagnostics; collaboration lifecycle/transaction handling; developer/
operator and synthetic verification support. Terminology services, selective
authoring, directory synchronisation/enrichment, media handling and detailed
assurance/factory realisation retain proposal or incomplete allocation standing.

The data view distinguishes logical meaning, application representation,
information management, persistence responsibility, persistence structure and
technology. It records current-row, per-version, HAPI-native, operational,
evidence, collaboration, work-journal and active working-set variants without
choosing a schema. Internal middleware is represented within Domain05; external
contracts and transport/product mechanisms remain separate.

PragmaFactory's candidate archetype embodiment is explicit. ActionableTask and
FulfillmentTask may both use Pragma with WorkOrder/ToDo/Stimulus/Effector forms
while remaining distinct. Ergon remains behaviour without an automatic Information
Resource. Praxis conflicts, ReportedTask and reserved outcome details remain open.

## 5. Candidate documentation gaps and unresolved contradictions

The [gap register](../../docs/markdown/05-information-systems-architecture/gaps.md)
contains **34 documentation/component-feature gaps**. Each identifies affected
constructs/features, type, documentary evidence, the missing or contradictory
meaning, and whether architectural adjudication appears necessary.

The major retained groups are:

- CDG-01–06: Praxis definition/composition/instance, Pragma carrier/state variants,
  PragmaFactory, Ergon behavioural contracts and definition lifecycle.
- CDG-07–09: Twin control/state and independent assurance execution/information.
- CDG-10–15: cache/write-behind authority, governed access, competing persistence/
  history structures, evidence allocation/significance and security-context content.
- CDG-16–19/34: responsibility transfer, unknown effects, replay/redelivery,
  fan-out completion, duplicate/partial transaction effects and ingress acceptance.
- CDG-20–23/29–30/32: clinical/source authority, care-team context, Agora storage/
  projections, EMPI claims, gate placement and registry external-status boundaries.
- CDG-24–28/31/33: terminology, media/binary custody, clinical versus diagnostic
  presentation, active working sets, telemetry/tools, observation/search-result
  contracts and local-directory features.

Established RADS boundaries take precedence over conflicting historical claims.
Where only detailed solution meaning is missing, the register preserves that
uncertainty rather than reopening an approved responsibility. No issue was
adjudicated merely because the previous assessment recommended it.

## 6. Legacy supersession and preservation

Added **19 section-specific wrappers across 13 legacy files**. The complete
original enclosed content is unchanged. Each wrapper identifies its Domain05
replacement; adjacent content remains outside the wrapper.

| Legacy file | Wrapped content and replacement |
| :--- | :--- |
| `docs/concepts/ponos.md` | §3 What Ponos Owns → C04, F04–06/F28. |
| `docs/concepts/ergon.md` | §3 What an Ergon Owns → C05, F07–10. |
| `docs/concepts/praxis.md` | §3 What Praxis Owns → C06, F11–12; competing overall meanings remain CDG-01. |
| `docs/concepts/pragma.md` | §3 anti-responsibilities → C07. |
| `docs/concepts/calliope-canonical.md` | §2 Core Information Models → C01, D01–02/D08. |
| `docs/concepts/themis.md` | §3 What Themis Owns → C02, D03. |
| `docs/concepts/iris.md` | §4 Provider Registry Decoupling → C16/F20. |
| `docs/concepts/agora.md` | §3 isolation constraints and §6.2 Practitioner/Group spaces → C18/F22. |
| `docs/design/governed-write-concurrency-contract.md` | §§10.2 commit/convergence distinction and 14.2 carrier/context → C11/D04/M05. |
| `docs/provider-registry/resource-model.md` | §3 first-class Endpoint/Group resources → D08/C13. |
| `docs/concepts/paradeigma.md` | §3 production isolation → C21/F29. |
| `docs/architecture-decisions.md` | ADR-013/016 evidence descriptions → C03/F15; ADR-014/015/017 handoff/replay/backlog descriptions → C10/M01–02. Accepted decision standing retained. |
| `docs/concepts/petasos.md` | §3 anti-responsibilities → C10/M01. |

Mixed/conflicting sections, LaTeX, binary publications, implementation catalogues
and merely inspected sources were not automatically superseded. Source dispositions
distinguish incorporation, partial incorporation, conflict/unresolved meaning,
supporting detail and later reconciliation.

## 7. Validation actually performed

### Architecture verification

Executed the repository-required suite with a five-minute command bound:

```bash
timeout --signal=TERM --kill-after=10s 300s mvn -o test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false -B -ntp
```

**PASS:** exit 0, `BUILD SUCCESS`, Maven duration **25.546 seconds**;
**90 tests across 11 architecture classes, zero failures/errors/skips**.
All eight named AGENTS suites executed, plus GovernedWriteComposition,
GovernedWriteContract and MnemosyneAuthoritativePersistence architecture tests.

The run emitted existing `sun.misc.Unsafe` deprecation warnings and ArchUnit
warnings while resolving runtime classes with class-file major version 69,
falling back to simple imports. This limits evidence from runtime dependency
resolution. Test success verifies executed guardrails; it neither validates the
reconstruction's semantic correctness nor approves the candidate architecture.
No timeout or stall occurred; no implementation was changed to address warnings.

### Documentation, traceability and scope verification

Bounded CommonMark/GFM parsing and rendering, relative-link/anchor checks,
table/fence/whitespace checks, identifier/source-reference checks and snapshot
preservation checks passed. The validator covered all eight new RADS documents,
this report and links added to existing documents: **242 local links, 16 tables
with 212 data rows, and six closed fences**, with no missing targets/anchors,
invalid registry/source references or preservation errors. `git diff --check`
and the cached equivalent passed.

SHA-256 checks verified the enclosed original content of all 19 sections.
Removing only the inserted wrappers reproduced the initial file hashes for all
13 legacy files. Comparison against **2,303 pre-existing files** confirmed only
the 13 wrapped files, documentation index and Completion Plan changed.
Domains01–04, implementation, test sources and original binary publications
remain unchanged; new files are confined to Domain05 and this report.

Source and register identifiers resolve consistently. The substantive review
checked candidate roles/features/data/collaborations against identified source
keys and controlling RADS boundaries, including the separately recorded U01 task
decisions. Contradictions remain explicit in the gap register. Mechanical link
or architecture-test success is not evidence of architectural completeness.

An initial preservation-checker assertion incorrectly required replacement
prefixes to be unique. Several ADR sections intentionally share replacement
references. The checker was corrected to locate each sequential wrapper and
verify its content and original-file hash; legacy content was not altered to
satisfy that assertion. The corrected validation passed.

Temporary evidence is held in `/tmp/harmonia-domain05-candidate-architecture-tests.log`,
`/tmp/harmonia-domain05-candidate-test-results.json`,
`/tmp/harmonia-domain05-candidate-validation.json`,
`/tmp/harmonia-domain05-candidate-before.json` and
`/tmp/harmonia-domain05-candidate-wrappers.json`. These are execution evidence,
not architectural authority.

### Canonical Documentation Assessment

Relevant architectural material was found extensively outside `/docs/markdown`.
The source inventory identifies its documents, scopes, candidate knowledge and
individual dispositions. Component, feature, Data Object, active/durable,
middleware and persistence-structure meaning belongs in **Domain05 Information
Systems Layer Architecture**. Product/deployment mechanisms and external contracts
remain supporting or downstream concerns; they were not migrated as logical
components or redesigned.

This authorised task incorporated a traceable initial candidate representation
and the identified clean legacy sections. Competing or incomplete designs are
summarised in RADS with explicit issues; original sources are retained. Further
bounded reconciliation is needed for the unresolved contracts and structures,
without inferring them from implementation. Upstream mapping, requirements gap
analysis, excess-feature/MVP review and implementation planning require subsequent
work. The Domain Completion Gate and final R1.x/R2.x baseline are not declared
satisfied by this reconstruction.
