<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1-whole-of-calliope-scope-and-candidate) | Whole-of-Calliope scope and candidate | PENDING REVIEW | Candidate commit `2fc15febe2a91c811fd48e83f3e7544dc37f8124`; task-specific scope disposition is retained without revising accepted `BL-CALLIOPE-OO-01` revision 1. |
| [DR-2](#dr-2-design-note-hierarchy-and-cohesive-package-organization) | Design Note hierarchy and cohesive package organization | PENDING REVIEW | Candidate follows the concept-oriented Design Note hierarchy with justified `datatypes`, `valuesets`, and `factory` subpackages; the previous “13-package” shorthand is not treated as architectural authority. |
| [DR-3](#dr-3-information-model-value-sets-and-construction) | Information model, value sets, and construction contracts | PENDING REVIEW | Single IU metadata/representation model; typed Content modes, security values, operations, workflow values, and external builders; legacy relationship string remains accepted on input. |
| [DR-4](#dr-4-consumer-migration-and-architectural-conformance) | Consumer migration and architectural conformance | PENDING REVIEW | Affected consumers and tests use the refactored APIs; Topic JSON and Pragma/Ergon FHIR payload fields remain unchanged; 90 ArchUnit tests pass. |
| [DR-5](#dr-5-500-line-source-file-limit-and-documentation) | Source-file size and Implementation Guide conformance | PENDING REVIEW | 104 Calliope production Java files; maximum 473 physical lines; all measured files are below the 500-line limit. |
| [DR-6](#dr-6-software-010-candidate-verification-and-review-request) | Software 0.1.0 candidate verification and review request | PENDING REVIEW | Calliope 251 tests, architecture 90 tests, and the full 50-module reactor pass; no release, tag, or acceptance is claimed. |
| [DR-7](#dr-7-pragma-fhir-security-context-authority) | Pragma FHIR security-context authority | PENDING REVIEW | `PragmaFhirSecurityConverter` remains unchanged; determine whether Task extensions are an authorized transport representation or caller-controlled authority under Invariant 6.2. |
| [DR-8](#dr-8-persistence-delete-operation-semantics) | Persistence DELETE operation semantics | PENDING REVIEW | Existing `PersistenceOperationType.DELETE` remains unchanged; confirm its meaning against the no-physical-DELETE lifecycle rule. |
| [DR-9](#dr-9-legacy-information-unit-json-representation) | Legacy Information Unit JSON representation | PENDING REVIEW | No external consumer or accepted historical snapshot contract was found; confirm whether old `InformationUnitMetadata` JSON needs migration beyond the candidate's tested round-trip contract. |

# HCRL software-0.1.0 / 001 — Calliope Model Restructure Candidate Review

**Current implementation disposition · Superseded by the clean-slate reset.**
Under the Solution Architect's [reset assignment][reset-assignment], the Calliope
implementation candidate recorded here is closed without implementation acceptance.
The original DRs and evidence below preserve the candidate's review history.
The [reset HCRL][foundation-reset] records the exact checkpoint and supersession.

The original review record follows.

**Iteration status: ACTIVE — PENDING REVIEW.**

The original submission by Implementation Agent **Junie** on `2026-11-16T08:30:00Z` and amendment at `2026-11-17T07:45:00Z` are retained as history. This pending candidate review is supported by the current [Implementation Report](../../evidence/software-0.1.0/001/after/ai-agent-reports/UNRESOLVED-calliope-model-engineering-report.md) under the [Software Development Praxis](../../../governance/software-development-praxis.md); it requests Solution Architect review and does not claim acceptance.

- **Target Software Release:** `0.1.0`
- **Accepted Architecture Baseline:** `1.1.26` (Architecture Continuum remains unchanged)
- **Candidate Commit:** `2fc15febe2a91c811fd48e83f3e7544dc37f8124` on `feature/BL-CALLIOPE-OO-01`
- **Previous Candidate Commit:** `da16614dd832c70257405b0bf312f7fba542f1a3` (not accepted)
- **Accepted Backlog Item (historical scope unchanged):** [`BL-CALLIOPE-OO-01`, revision 1](../../../../docs/backlog/calliope-oo-01-topic-models.md)
- **Assignment Scope Source:** [`BacklogItem-0001-Calliope-Model-Restructure.001.md`](../../../../docs/backlog/BacklogItem-0001-Calliope-Model-Restructure.001.md), header ID `00001`, revision `001`; accepted for this assignment only by the Solution Architect disposition in the current Task.
- **Authoritative Design Note:** [DesignNote-Calliope-Initial-Model-Architecture.md](../../../../docs/design/DesignNote-Calliope-Initial-Model-Architecture.md)
- **Current Task Details:** [20261117T092256Z-calliope-model-engineering-task.md](../../evidence/software-0.1.0/001/before/ai-agent-tasks/20261117T092256Z-calliope-model-engineering-task.md)
- **Prior Task Details:** [20261116T055100Z-calliope-model-restructure-task.md](../../evidence/software-0.1.0/001/before/ai-agent-tasks/20261116T055100Z-calliope-model-restructure-task.md)
- **Current Implementation Report:** [UNRESOLVED-calliope-model-engineering-report.md](../../evidence/software-0.1.0/001/after/ai-agent-reports/UNRESOLVED-calliope-model-engineering-report.md)
- **Prior Implementation Report:** [20261116T083000Z-calliope-model-restructure-report.md](../../evidence/software-0.1.0/001/after/ai-agent-reports/20261116T083000Z-calliope-model-restructure-report.md)

---

<a id="dr-1-whole-of-calliope-scope-and-candidate"></a>
## DR-1 — Whole-of-Calliope Scope and Candidate

- **Context:** Accepted `BL-CALLIOPE-OO-01` revision 1 is limited to Topic/TopicMask and remains historically unchanged. The separate Backlog Item ID `00001`, revision `001`, is marked proposed; the Solution Architect's explicit follow-up preserved in the current Task authorizes its whole-Calliope scope for this assignment only. The disposition's event timestamp was not exposed and remains **UNRESOLVED**.
- **Implemented Scope:** Candidate `2fc15febe2a91c811fd48e83f3e7544dc37f8124` is submitted against that assignment-specific scope, the Design Note, the Implementation Guide, and applicable Continuum authority. The parent candidate was `da16614dd832c70257405b0bf312f7fba542f1a3`; neither commit constitutes Solution Architect acceptance.
- **Disposition Requested:** Review the exact integrated candidate against the task-specific scope authorization and decide acceptance, rejection, qualification, or requested changes. No backlog revision standing is changed by this HCRL.

<a id="dr-2-design-note-hierarchy-and-cohesive-package-organization"></a>
## DR-2 — Design Note Hierarchy and Cohesive Package Organization

- **Context:** The Design Note establishes a function- and domain-oriented hierarchy and expressly allows justified `datatypes`, `valuesets`, `factory` and other support packages. It does not define a normative count of 13 packages. The prior HCRL's “13-package” wording is corrected here and is not treated as architectural authority.
- **Implemented Scope:** The candidate realizes the Design Note's functional areas and adds cohesive support packages under naming, information entity, operations, workflow tasks/orchestration, security and FHIR domain responsibilities. Naming and operational construction are separated into factories/builders; Topic and TopicMask retain their serialized shape and matching semantics.
- **Disposition Requested:** Review the resulting package/responsibility mapping against the Design Note and applicable Continuum elements; confirm any remaining placement discrepancy.

<a id="dr-3-information-model-value-sets-and-construction"></a>
## DR-3 — Information Model, Value Sets, and Construction

- **Context:** InformationMetadata is the conceptual peer of OperationalMetadata. The Continuum permits embedded, referenced or hybrid views and preserves the IU identity/authoritative revision, Mneme working/version facts, Content Structure Version, and native Content version as distinct concerns.
- **Implemented Scope:** One InformationMetadata and one InformationRepresentation are held by InformationUnit; ManagementContext remains an optional embedded peer view. Content uses typed portable modes, with working-only state rejected by JSON/Java serialization. Topic classifications are typed, duplicate type aliases are removed from ContainerMetadata/ContentMetadata, constructors are typed, and builders/value sets are separately organized. Relationship output emits the Topic-valued `type`; the legacy `relationshipType` string is accepted on input.
- **Disposition Requested:** Review the model and serialization choices against the accepted Information Unit and InformationRepresentation contracts, including the output removal of duplicate aliases and the absence of a legacy InformationUnitMetadata snapshot replay.

<a id="dr-4-consumer-migration-and-architectural-conformance"></a>
## DR-4 — Consumer Migration and Architectural Conformance

- **Context:** The model restructure changes Java package/API locations and requires the integrated consumers and architecture tests to compile against the same candidate.
- **Implemented Scope:** Direct consumers and tests in Calliope, Agora, Pylai, Energeia, Hestia, Iris and Paradeigma were migrated as applicable. Security integration tests now establish trusted servlet principal/roles in the test container; production authorization logic was not weakened or replaced. The Pragma/Ergon FHIR payload property names and aliases remain unchanged, and the Topic JSON shape remains unchanged.
- **Disposition Requested:** Accept or qualify the consumer migration and the architecture/conformance results recorded in the Implementation Report.

<a id="dr-5-500-line-source-file-limit-and-documentation"></a>
## DR-5 — 500-Line Source-File Limit and Documentation

- **Context:** The Implementation Guide requires cohesive object responsibilities, newcomer-oriented class/method documentation and the strict 500-physical-line production source ceiling; numerical compliance alone is insufficient.
- **Implemented Scope:** The candidate has 104 Calliope production Java files; all are below 500 lines. The maximum measured file is 473 lines (`PraxisDefinition.java`); `Pragma.java` is 469 lines. Documentation and responsibility extraction are reported in the Implementation Guide Conformance section.
- **Disposition Requested:** Confirm the source inventory, responsibility decomposition and documentation quality; no size exception is requested.

<a id="dr-6-software-010-candidate-verification-and-review-request"></a>
## DR-6 — Software 0.1.0 Candidate Verification and Review Request

- **Context:** Software release identity remains independent of Architecture release allocation. A candidate submission is not an accepted software baseline.
- **Implemented Scope:** The Implementation Report records the exact candidate commit, environment, commands, results and limitations. Calliope's 251 tests, 90 architecture tests and full 50-module Maven reactor pass. Architecture release remains `1.1.26`; no software tag or release declaration was made.
- **Disposition Requested:** Review the exact candidate and determine whether it is accepted for software release `0.1.0`, qualified, or requires changes. Acceptance remains pending.

<a id="dr-7-pragma-fhir-security-context-authority"></a>
## DR-7 — Pragma FHIR Security-Context Authority

- **Context:** `PragmaFhirSecurityConverter` reconstructs Themis security context from FHIR Task extensions. The task plan explicitly excluded redesign and requested escalation under AX-17; Invariant 6.2 prohibits establishing authority from caller-controlled resource attributes/tags.
- **Implemented Scope:** The converter was not changed. Its source/transport trust boundary has not been independently established by this candidate and no new authority semantics are inferred.
- **Disposition Requested:** Determine whether the existing Task-extension representation is an authorized transport representation of canonical trusted security context and whether any corrective work is required before accepting this candidate.

<a id="dr-8-persistence-delete-operation-semantics"></a>
## DR-8 — Persistence DELETE Operation Semantics

- **Context:** The repository contains `PersistenceOperationType.DELETE`; the architectural rule prohibits physical DELETE as managed-information lifecycle semantics.
- **Implemented Scope:** The operation type and persistence implementation were not changed by this Calliope model candidate. This HCRL does not treat the enum value as authority for physical deletion.
- **Disposition Requested:** Confirm the existing DELETE operation's intended meaning or request a bounded correction before acceptance; no lifecycle or persistence semantics are inferred here.

<a id="dr-9-legacy-information-unit-json-representation"></a>
## DR-9 — Legacy Information Unit JSON Representation

- **Context:** The corrected IU model emits one InformationMetadata and an optional root ManagementContext instead of the previous InformationUnitMetadata wrapper. Repository search found no production-module consumer or retained accepted snapshot contract for the old wrapper; current candidate JSON round-trips are tested.
- **Implemented Scope:** No legacy JSON migration adapter or old serialized snapshot replay was added. The candidate preserves the Continuum's information/operational distinction and exposes the new shape; this limitation is stated in the Implementation Report.
- **Disposition Requested:** Confirm that no legacy snapshot migration evidence/adapter is required for acceptance, or specify the authoritative compatibility contract and required remediation.

[foundation-reset]: hcrl-software-0.1.0-009.pending.md
[reset-assignment]: ../../evidence/software-0.1.0/009/before/ai-agent-tasks/unresolved-software-foundation-reset.md#issued-instruction
