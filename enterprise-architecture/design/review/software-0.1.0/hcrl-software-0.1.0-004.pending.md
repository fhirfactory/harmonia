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
| [DR-10](#dr-10) | Draft.001.003 Topic/TopicMask design review | ACCEPTED WITH QUALIFICATIONS | Mark Hunter accepted the design recorded in [Final.001.001][topic-final-001-001], including the §8 Java API. F-01.1–F-01.81 and R1–R6 retain their decision lineage. Acceptance provenance is in [Iteration 007][topic-acceptance-boundary]. Compatibility, conformance and reconciliation obligations remain open. |
| [DR-11](#dr-11) | Bounded Minutiae | PENDING REVIEW | Supporting Task/Report provenance and source-locator clarification, with unresolved event time disclosed. |
| [DR-12](#dr-12) | Pragma publication and subscription design review | PENDING SOLUTION ARCHITECT REVIEW | Iteration 005 documents PS-01–PS-16 in Draft.000.001. Draft.000.002 reconciles execution-information semantics with Pragmaton Draft.000.003. G03 adds Mneme Pragma Model Draft.000.003 for work identity, IU cardinality and scheduling review, Ergon–Mneme Draft.000.003 for governed WorkSpace access, save and recovery, and Petasos–Mneme Draft.000.003 for reference handoff, acknowledgement and retry. Handoff order, terminology, selector scope and remaining contract decisions require adjudication. The cross-document review finds a coherent core and identifies exceptions for collective adjudication. G03 adjudications are reflected in five new pending drafts. The audit staging proposal and remaining PF decisions await review. The routing candidate captures durable decision recovery for both Petasos routers. Canonical correspondence and PF-07/PF-08 contracts remain pending. Routing Draft.000.002 records Ergon Egress work creation, direct Praxis routing of existing work and authoritative-provenance handover recovery. Routing Draft.000.003 records Mneme-managed ephemeral routing-set coordination and the qualified PF-08 strategy disposition. The processing-framework candidate adds the three-tier model and synchronous named Checkpoints for bounded intra-module review. [Final.001 records qualified acceptance of the Processing Framework and Checkpoints DRP](#g03-processing-framework-final-001). The collective G03 review remains pending. [PF-07 resolves H10/H11 responsibilities and normal-path behaviour](#g03-pf-07-ergon-egress-petasos-handover). Detailed contracts and conformance evidence remain outstanding. [Routing Final.001 records qualified acceptance of the PF-07 handover and PF-08 strategy](#g03-petasos-routing-final-001). The detailed obligations carry forward and the collective G03 review remains pending. |

# HCRL software-0.1.0 / 004 — Calliope Candidate Carry-Forward and Topic/TopicMask Design Review

**Iteration status · PENDING SOLUTION ARCHITECT REVIEW.**

[HCRL software-0.1.0/001][predecessor] is the sole preceding software HCRL found in the review/register tree and local Git history. Its record remains pending as issued, including the original Junie submission on `2026-11-16T08:30:00Z` and amendment at `2026-11-17T07:45:00Z`. Iterations 002 and 003 supplied F-01 design evidence with no corresponding software HCRLs. Iteration 004 is the expressly assigned review-preparation target. The preserved review state and intervening qualifications are in the [carry-forward reconciliation][carry].

This review record has two bases. DR-1–DR-9 retain the exact implementation candidate and its pending review requests from HCRL 001. DR-10 presents a documentation candidate for review. The source tree for preparing 004 was `fd52408e3d8be433bb43eedd2a448354646d7518` plus pre-existing local worktree edits. The [Software Development Praxis][software] and [Architecture Review Praxis][praxis] govern the review. No accepted software predecessor was established in the local register. The first-baseline/release decision remains open under DR-6.

- **Target Software Release:** `0.1.0`
- **Accepted Architecture Baseline:** `1.1.26` (Architecture Continuum remains unchanged)
- **Candidate Commit:** `2fc15febe2a91c811fd48e83f3e7544dc37f8124` on `feature/BL-CALLIOPE-OO-01`
- **Previous Candidate Commit:** `da16614dd832c70257405b0bf312f7fba542f1a3` (not accepted)
- **Accepted Backlog Item (historical scope unchanged):** [`BL-CALLIOPE-OO-01`, revision 1](../../../../docs/backlog/calliope-oo-01-topic-models.md)
- **001 Assignment Scope Source** · [`BacklogItem-0001-Calliope-Model-Restructure.001.md`](../../../../docs/backlog/BacklogItem-0001-Calliope-Model-Restructure.001.md), header ID `00001`, revision `001`. The disposition in the 001 corrective Task accepts this scope for that assignment only.
- **Authoritative Design Note:** [DesignNote-Calliope-Initial-Model-Architecture.md](../../../../docs/design/DesignNote-Calliope-Initial-Model-Architecture.md)
- **001 corrective Task Details:** [20261117T092256Z-calliope-model-engineering-task.md](../../evidence/software-0.1.0/001/before/ai-agent-tasks/20261117T092256Z-calliope-model-engineering-task.md)
- **001 initial Task Details:** [20261116T055100Z-calliope-model-restructure-task.md](../../evidence/software-0.1.0/001/before/ai-agent-tasks/20261116T055100Z-calliope-model-restructure-task.md)
- **001 corrective Implementation Report:** [UNRESOLVED-calliope-model-engineering-report.md](../../evidence/software-0.1.0/001/after/ai-agent-reports/UNRESOLVED-calliope-model-engineering-report.md)
- **001 initial Implementation Report:** [20261116T083000Z-calliope-model-restructure-report.md](../../evidence/software-0.1.0/001/after/ai-agent-reports/20261116T083000Z-calliope-model-restructure-report.md)

**Cumulative review addition** · DR-12 records the Pragma publication and subscription design review performed in Iteration 005. Its Task, reconciliation and Report retain their originating 005 evidence location. The 004 software candidate and existing dispositions and carry-forward obligations continue to govern in their recorded scope.

---

<a id="dr-1-whole-of-calliope-scope-and-candidate"></a>
## DR-1 — Whole-of-Calliope Scope and Candidate

- **Context:** Accepted `BL-CALLIOPE-OO-01` revision 1 is limited to Topic/TopicMask and remains historically unchanged. The separate Backlog Item ID `00001`, revision `001`, is marked proposed; the Solution Architect's explicit follow-up preserved in the current Task authorizes its whole-Calliope scope for this assignment only. The disposition's event timestamp was not exposed and remains **UNRESOLVED**.
- **Implemented Scope:** Candidate `2fc15febe2a91c811fd48e83f3e7544dc37f8124` is submitted against that assignment-specific scope, the Design Note, the Implementation Guide, and applicable Continuum authority. The parent candidate was `da16614dd832c70257405b0bf312f7fba542f1a3`; neither commit constitutes Solution Architect acceptance.
- **Disposition Requested:** Review the exact integrated candidate against the task-specific scope authorization and decide acceptance, rejection, qualification, or requested changes. No backlog revision standing is changed by this HCRL.
- **Iteration 004 carry-forward** · [001 / DR-1](hcrl-software-0.1.0-001.pending.md#dr-1-whole-of-calliope-scope-and-candidate) remains `PENDING REVIEW`. Open for acceptance of the exact existing candidate. The assignment-specific scope authorization stands. Iterations 002–004 concern the Topic/TopicMask design and supply no revised assignment or scope disposition for this candidate.

<a id="dr-2-design-note-hierarchy-and-cohesive-package-organization"></a>
## DR-2 — Design Note Hierarchy and Cohesive Package Organization

- **Context:** The Design Note establishes a function- and domain-oriented hierarchy and expressly allows justified `datatypes`, `valuesets`, `factory` and other support packages. It does not define a normative count of 13 packages. The prior HCRL's “13-package” wording is corrected here and is not treated as architectural authority.
- **Implemented Scope:** The candidate realizes the Design Note's functional areas and adds cohesive support packages under naming, information entity, operations, workflow tasks/orchestration, security and FHIR domain responsibilities. Naming and operational construction are separated into factories/builders; Topic and TopicMask retain their serialized shape and matching semantics.
- **Disposition Requested:** Review the resulting package/responsibility mapping against the Design Note and applicable Continuum elements; confirm any remaining placement discrepancy.
- **Iteration 004 carry-forward** · [001 / DR-2](hcrl-software-0.1.0-001.pending.md#dr-2-design-note-hierarchy-and-cohesive-package-organization) remains `PENDING REVIEW`. Open. The later [Calliope review evidence][carry] raises the placement of service contracts and the cohesion of `information.access` for the existing candidate. F-01.80 records a separate dependency question and supplies no conformance disposition for this package hierarchy.

<a id="dr-3-information-model-value-sets-and-construction"></a>
## DR-3 — Information Model, Value Sets, and Construction

- **Context:** InformationMetadata is the conceptual peer of OperationalMetadata. The Continuum permits embedded, referenced or hybrid views and preserves the IU identity/authoritative revision, Mneme working/version facts, Content Structure Version, and native Content version as distinct concerns.
- **Implemented Scope:** One InformationMetadata and one InformationRepresentation are held by InformationUnit; ManagementContext remains an optional embedded peer view. Content uses typed portable modes, with working-only state rejected by JSON/Java serialization. Topic classifications are typed, duplicate type aliases are removed from ContainerMetadata/ContentMetadata, constructors are typed, and builders/value sets are separately organized. Relationship output emits the Topic-valued `type`; the legacy `relationshipType` string is accepted on input.
- **Disposition Requested:** Review the model and serialization choices against the accepted Information Unit and InformationRepresentation contracts, including the output removal of duplicate aliases and the absence of a legacy InformationUnitMetadata snapshot replay.
- **Iteration 004 carry-forward** · [001 / DR-3](hcrl-software-0.1.0-001.pending.md#dr-3-information-model-value-sets-and-construction) remains `PENDING REVIEW`. Open. The later [Calliope review evidence][carry] raises IU and operational representation, security classification, lifecycle, and version-preservation questions. F-01.78 clarifies the draft Topic projection and independent IU UUID. It leaves the candidate’s IU model and legacy JSON questions to their original reviews.

<a id="dr-4-consumer-migration-and-architectural-conformance"></a>
## DR-4 — Consumer Migration and Architectural Conformance

- **Context:** The model restructure changes Java package/API locations and requires the integrated consumers and architecture tests to compile against the same candidate.
- **Implemented Scope:** Direct consumers and tests in Calliope, Agora, Pylai, Energeia, Hestia, Iris and Paradeigma were migrated as applicable. Security integration tests now establish trusted servlet principal/roles in the test container; production authorization logic was not weakened or replaced. The Pragma/Ergon FHIR payload property names and aliases remain unchanged, and the Topic JSON shape remains unchanged.
- **Disposition Requested:** Accept or qualify the consumer migration and the architecture/conformance results recorded in the Implementation Report.
- **Iteration 004 carry-forward** · [001 / DR-4](hcrl-software-0.1.0-001.pending.md#dr-4-consumer-migration-and-architectural-conformance) remains `PENDING REVIEW`. Open. Iterations 002–004 identify Topic/TopicMask compatibility and selection-scope obligations. The later [Calliope review evidence][carry] also raises selector, security, and routing conversions in the existing candidate. The original migrated-consumer results apply to the existing candidate; the new design’s compatibility evidence belongs to DR-10.

<a id="dr-5-500-line-source-file-limit-and-documentation"></a>
## DR-5 — 500-Line Source-File Limit and Documentation

- **Context:** The Implementation Guide requires cohesive object responsibilities, newcomer-oriented class/method documentation and the strict 500-physical-line production source ceiling; numerical compliance alone is insufficient.
- **Implemented Scope:** The candidate has 104 Calliope production Java files; all are below 500 lines. The maximum measured file is 473 lines (`PraxisDefinition.java`); `Pragma.java` is 469 lines. Documentation and responsibility extraction are reported in the Implementation Guide Conformance section.
- **Disposition Requested:** Confirm the source inventory, responsibility decomposition and documentation quality; no size exception is requested.
- **Iteration 004 carry-forward** · [001 / DR-5](hcrl-software-0.1.0-001.pending.md#dr-5-500-line-source-file-limit-and-documentation) remains `PENDING REVIEW`. Open. The later [Calliope review evidence][carry] confirms the reported production file count and size limit and adds concerns about responsibility separation and state ownership. Educational documentation and responsibility conformance remain pending.

<a id="dr-6-software-010-candidate-verification-and-review-request"></a>
## DR-6 — Software 0.1.0 Candidate Verification and Review Request

- **Context:** Software release identity remains independent of Architecture release allocation. A candidate submission is not an accepted software baseline.
- **Implemented Scope:** The Implementation Report records the exact candidate commit, environment, commands, results and limitations. Calliope's 251 tests, 90 architecture tests and full 50-module Maven reactor pass. Architecture release remains `1.1.26`; no software tag or release declaration was made.
- **Disposition Requested:** Review the exact candidate and determine whether it is accepted for software release `0.1.0`, qualified, or requires changes. Acceptance remains pending.
- **Iteration 004 carry-forward** · [001 / DR-6](hcrl-software-0.1.0-001.pending.md#dr-6-software-010-candidate-verification-and-review-request) remains `PENDING REVIEW`. Open. The iteration 004 existing ArchUnit run passed all 90 tests at branch HEAD. The original Calliope and full-reactor results retain their exact-candidate scope, including the absence of live database, broker, deployment and external-interoperability exercise. No new integrated implementation candidate is submitted. The accepted software predecessor or explicit first-release treatment remains to be established by the Solution Architect.

<a id="dr-7-pragma-fhir-security-context-authority"></a>
## DR-7 — Pragma FHIR Security-Context Authority

- **Context:** `PragmaFhirSecurityConverter` reconstructs Themis security context from FHIR Task extensions. The task plan explicitly excluded redesign and requested escalation under AX-17; Invariant 6.2 prohibits establishing authority from caller-controlled resource attributes/tags.
- **Implemented Scope:** The converter was not changed. Its source/transport trust boundary has not been independently established by this candidate and no new authority semantics are inferred.
- **Disposition Requested:** Determine whether the existing Task-extension representation is an authorized transport representation of canonical trusted security context and whether any corrective work is required before accepting this candidate.
- **Iteration 004 carry-forward** · [001 / DR-7](hcrl-software-0.1.0-001.pending.md#dr-7-pragma-fhir-security-context-authority) remains `PENDING REVIEW`. Open. The later [Calliope review evidence][carry] reiterates the question of trust for reconstructed FHIR Task context. The F-01 neutral-selector boundary supplies no transport authority disposition.

<a id="dr-8-persistence-delete-operation-semantics"></a>
## DR-8 — Persistence DELETE Operation Semantics

- **Context:** The repository contains `PersistenceOperationType.DELETE`; the architectural rule prohibits physical DELETE as managed-information lifecycle semantics.
- **Implemented Scope:** The operation type and persistence implementation were not changed by this Calliope model candidate. This HCRL does not treat the enum value as authority for physical deletion.
- **Disposition Requested:** Confirm the existing DELETE operation's intended meaning or request a bounded correction before acceptance; no lifecycle or persistence semantics are inferred here.
- **Iteration 004 carry-forward** · [001 / DR-8](hcrl-software-0.1.0-001.pending.md#dr-8-persistence-delete-operation-semantics) remains `PENDING REVIEW`. Open. The later [Calliope review evidence][carry] reiterates the lifecycle question. No attributable disposition establishes the meaning of this operation.

<a id="dr-9-legacy-information-unit-json-representation"></a>
## DR-9 — Legacy Information Unit JSON Representation

- **Context:** The corrected IU model emits one InformationMetadata and an optional root ManagementContext instead of the previous InformationUnitMetadata wrapper. Repository search found no production-module consumer or retained accepted snapshot contract for the old wrapper; current candidate JSON round-trips are tested.
- **Implemented Scope:** No legacy JSON migration adapter or old serialized snapshot replay was added. The candidate preserves the Continuum's information/operational distinction and exposes the new shape; this limitation is stated in the Implementation Report.
- **Disposition Requested:** Confirm that no legacy snapshot migration evidence/adapter is required for acceptance, or specify the authoritative compatibility contract and required remediation.
- **Iteration 004 carry-forward** · [001 / DR-9](hcrl-software-0.1.0-001.pending.md#dr-9-legacy-information-unit-json-representation) remains `PENDING REVIEW`. Open. The later [Calliope review evidence][carry] raises representation and selector evolution concerns. F-01.78 preserves the IU Topic designation and independent IU identity. No historical InformationUnitMetadata corpus, wrapper migration result or waiver was established in iterations 002–004.

<a id="dr-10"></a>
## DR-10 — Draft.001.003 Topic/TopicMask Design Review

**Solution Architect disposition · Accepted with qualifications.** Mark Hunter accepted exact [Draft.001.003][draft] for the runtime Topic/TopicMask model, matching semantics, construction, canonical representations and Java API. [Final.001.001][topic-final-001-001] is the accepted offline design for subsequent Calliope work. [Operative acceptance and amendment][topic-acceptance-task]

**Original review objective** · Determine whether [Draft.001.003][draft] correctly incorporates all accepted F-01 adjudications and is suitable for Solution Architect acceptance as the revised Topic/TopicMask design.

**Review basis**

- [Accepted Final.001.000][final], its incorporation into the Continuum under [RAD 1.1.25 DR-3][rad], and the lineage through [Draft.001.001][draft001] and immediate predecessor [Draft.001.002][draft002].
- F-01.1–F-01.81 and the originating evidence for [002][decisions002], [003][decisions003] and [004][decisions004]. See the [decision delta][delta] for coverage and supersession.
- The applicable accepted Topic, TopicMask, IU, architectural naming and Calliope-Models contracts and canonical Principles identified in the [architectural reconciliation][reconciliation], with the [implementation gap and acceptance obligations][obligations]. These records trace to the originating 002 and 003 analysis.

**Incorporated and qualified result** · The draft settles the accepted API semantics, structural runtime identities, legacy selector and reference treatment, IU designation, identity boundaries, dependency direction and logging responsibilities. The accepted Continuum still contains the previous UUID, registry, sequence and selector contracts. R1–R6 are dispositioned for the draft at the decision level. The remaining proof concerns legacy migration and selection scope, exact IU qualifiers and boundary mappings, architectural correspondence, actual module and RE2/J allocation, shared logging behaviour, and applicable performance and reliability evidence.

**Verification** · Document coverage, internal consistency, all nine carry-forward matters, Markdown links and historical preservation were checked. The existing ArchUnit suite passed 90 tests. The Continuum validator failed on a pre-existing missing retained-input locator in the common Topic/TopicMask models. Resolve that prerequisite by explicit Solution Architect disposition before dependent Continuum work. The check establishes no F-01 implementation evidence. See the [execution report][report].

**Acceptance considerations**

1. Completeness and internal consistency of the revised design.
2. Correct incorporation of F-01.75–F-01.81, including consistency of the proposed Java surface.
3. Preservation of the historical decisions and explicit supersessions.
4. Reconciliation of R1–R6 with the accepted Continuum, with its differences still visible.
5. Explicit compatibility and migration obligations, including selector scope and the unresolved IU qualifier vocabulary.
6. Complete [mandatory criteria][acceptance] for subsequent implementation acceptance.
7. Scope evidence that the activity created documentation and review material only.

**Original disposition request (Iteration 004)** · Review the exact draft and evidence, including the proposed Java exception types, and record acceptance, qualification or requested changes for the design. Preserve the stated obligations and qualifications. The scope of this review is the design document.

**Accepted Java API** · The §8 signatures, `Optional<String>` and locale-independent `String.compareTo` field ordering are accepted. `Topic.getAncestor(int)` uses `IllegalArgumentException` for out-of-range depths. Ordinary-only `TopicMask` accessors use `IllegalStateException` on universal masks. The signature block and all settled F-01 decisions carry forward. [Accepted Java surface][topic-final-java]

**Outstanding qualifications**

1. Preserve or explicitly migrate historical Topic UUID references, registry uniqueness and aliases, and persisted selectors and their references. Establish approved compatibility evidence.
2. Prove legacy selector equivalence, including security-sensitive matching and the absence of widened selection on invalid or ambiguous input.
3. Establish the fully qualified IU Topic representation, canonical qualifier vocabulary and boundary mappings. Preserve the established unqualified IU designation and independent IU UUID.
4. Establish release-specific limits, pinned RE2/J compatibility and resource safeguards. Confirm module placement and retain the separate `themis-api` governance concern.
5. Verify logging integration, bounded latency and reliability under normal operation and failure, including ERROR delivery and outstanding delivery uncertainty.
6. Demonstrate the actual Java implementation and consumer conformance against the retained mandatory behavioural criteria and applicable solution-wide evidence requirements.
7. Reconcile the design with applicable accepted Continuum contracts where required. Any incorporation requires separate authority through ADD/HCRL.

**Application and provenance** · The original review and F-01 decision history remain here. The acceptance occurred after the establishment of Iteration 006. [Iteration 007][topic-acceptance-boundary] holds the Task and finalisation evidence. Comparison with Draft.001.003 confirmed that the runtime contracts, Java signatures and behavioural acceptance criteria were preserved. The changes record standing, provenance and the explicit Java API disposition. The V001 implementation, HCRL.004 and the collective G03 review retain their pending standing. [Open evidence][topic-final-obligations] · [Behavioural criteria][topic-final-criteria] · [Finalisation Report][topic-acceptance-report]

<a id="dr-11"></a>
## DR-11 — Bounded Minutiae

Supporting preparation evidence comprises the [004 Task][task] and [execution report][report]. The supplied continuation filename includes `-004`; its actual path is cited and its source bytes are preserved. The Task instruction-entry event time and filename prefix are **UNRESOLVED** and require Solution Architect treatment before closure. The [carry-forward reconciliation][carry] records the predecessor and evidence-check limits.

The [Iteration 005 Pragma Task][ps-task] preserves the operative PS-01–PS-16 wording. Its instruction-entry event time and filename prefix are **UNRESOLVED** for Solution Architect treatment before closure. The [Iteration 005 Report][ps-report] records its own emission event. The Iteration 005 layout check requires the corresponding HCRL.005, which is still reserved.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

<a id="dr-12"></a>
## DR-12 — Pragma Publication and Subscription Design Review

**Disposition · Pending Solution Architect review.**

### Original review basis · Draft.000.001

**Candidate** · [Pragma Publication and Subscription Draft.000.001][ps-draft] records the Solution Architect's Iteration 005 design decisions PS-01–PS-16. The proposal applies the exclusive Pragma-based publication/subscription framework, producer-side InformationUnit eligibility before downstream Pragma creation, metadata-only subscription selection, and distinct subscriber-specific realisations with independent processing and retry outcomes. Normal filtering leaves the InformationUnit governed. Mneme owns work and selection, Mnemosyne establishes authoritative state, Petasos hands off a reference to the durably established undertaking, and authorised Ergon execution uses governed working information. Dokimasia compares expected and observed progression.

**Architectural reconciliation** · The existing qualified handoff contract points the parcel at the work instance and places undertaking creation at the receiver. PS-09/PS-12 place it with Mneme before acknowledged handoff. The historical publication design filters an egress Pragma, while PS-03 applies the PublishingMask to the output InformationUnit first. The proposal keeps the realised work identities `Pragmatosis` and `Noesis` distinct from working information, including the draft `Pragmaton` envelope. [Source correspondence and conformance cases][ps-reconciliation-001]

**Disposition requested** · Review the exact draft. Adjudicate the ActionableTask/FulfillmentTask role and outcome correspondence, handoff ordering and reference semantics, mask and filtered-state representation, Topic/parcel selection boundary, and mappings among WorkSpace, Pragmaton, SummaryCache and existing cache/access responsibilities. Confirm the Twin and Pylai boundary mappings, Dokimasia type names and Praxis derivation, and the required durability, replay and Assurance evidence before dependent architecture application or implementation.

**Verification** · The bounded ArchUnit reactor passed 90 tests against the entry software state. The [Iteration 005 Report][ps-report] records document coverage, source preservation and provenance-check limits. Implementation conformance for this proposed design remains future evidence.

### Follow-up review · Publication/subscription Draft.000.002 and Pragmaton Draft.000.003

**Candidate correspondence** · [Pragmaton Draft.000.003][ps-pragmaton] documents the Mneme-managed associative execution-information environment for a particular realised undertaking. `Ergon.WorkSpace` is the Ergon-facing access abstraction for that environment. [Publication/subscription Draft.000.002][ps-draft-002] preserves PS-01–PS-16 and carries that design agreement into ContextLoader and ProcessingEngine responsibilities, governed lazy acquisition through Mneme under Themis, immutable non-authoritative IU snapshots and the existing two-cache separation. The agreement is recorded for review. Architectural incorporation remains pending.

**Proposed interaction clarification** · The [reconciliation evidence][ps-reconciliation] proposes amendments to Ergon–Mneme Draft.002 for collaborative initial contextualisation, WorkSpace access and additional acquisition, Workflow Cache support, Themis enforcement and return of working changes through governed save. New Pragma Output remains subject to the PublishingMask path. The interaction draft retains its recorded standing and wording.

**Disposition requested** · Review both exact candidates and the proposed interaction amendments. The original DR-12 obligations carry forward. For the settled WorkSpace relationship, the remaining work concerns canonical correspondence and incorporation, formal Pragmaton association and lifecycle, acquisition and authority-change outcomes, and governed save/unload. Preserve the accepted two-cache allocation and its qualifications. Keep detailed API, type and lookup implementation choices in solution design. [Reconciliation evidence][ps-reconciliation] · [Original reconciliation evidence][ps-reconciliation-001]

**Verification** · The follow-up ArchUnit reactor passed 90 tests against the existing software. The [follow-up Report][ps-follow-up-report] records source preservation and link and provenance checks. Task instruction-entry time remains unresolved. The whole-iteration evidence check still requires the reserved HCRL.005. [Follow-up Task][ps-follow-up-task]

### G03 review · Mneme Pragma Model Draft.000.003

**Candidate correspondence** · The [new Pragma Model draft][g03-pragma-draft] preserves the Draft.002 responsibility boundaries, governed progression, and output/working-value distinction. It incorporates the originating Pragma and subscriber-specific Pragmatosis roles, independent outcome and lineage, a single input IU, zero or more generated outputs, immutable non-authoritative IU summaries, and Pragmaton/WorkSpace correspondence. Publication, realisation, handoff, execution, completion and retry remain distinguishable. The G03 instruction supplies `DoNotBefore`; the draft records its earliest-permitted execution meaning under the existing coherent scheduler responsibility. The companion drafts and earlier PS-01–PS-16 review remain the source for their respective detailed contracts.

**Disposition requested** · Review the exact draft and its [reconciliation evidence][g03-pragma-evidence]. Confirm canonical work and outcome correspondence, minimum lifecycle and lineage semantics, the scheduling contract beyond earliest start, and attribution of outputs to undertakings and any subsequent work resolution. The reconciliation identifies review-only follow-on corrections to the Pragma-family drafts. Prior DR-12 obligations, including the handoff, selector and specialisation reviews and Pragmaton incorporation, carry forward. G03 A06/A07 remain open.

**Verification** · The bounded ArchUnit reactor passed 90 tests on the existing software. Source preservation, review checks and the reserved HCRL.005 evidence-layout limit are recorded in the [G03 Report][g03-pragma-report]. The [G03 Task][g03-pragma-task] retains the original instruction and unresolved instruction-entry time.

### G03 review · Ergon–Mneme Pragmaton Interaction Draft.000.003

**Candidate correspondence** · The [interaction draft][g03-ergon-draft] incorporates prior WorkSpace and collaborative contextualisation proposals. It distinguishes access to existing working values, lazy reference resolution and additional governed acquisition under Themis. It preserves the accepted Workflow Cache locality, version and isolation constraints, the full-read materialisation exception, governed Mneme save and independent work, access, save, persistence and transport outcomes. Recovery uses the durable realisation and governed information at the established execution boundary.

**Disposition requested** · Review the exact candidate and its [reconciliation evidence][g03-ergon-evidence]. Confirm canonical Load/Save and Ergo/Ergon correspondence, the logical adapter's interface correspondence, establishment and access outcomes, source-version and recovery evidence, and the information and outcome contract for unload/save. Carry the Petasos–Mneme reference and reconstruction correction forward for review. Delivery resolves an already-established realisation, and execution rebuilds working context as required. Prior DR-12 obligations remain in scope.

**Verification** · The bounded ArchUnit reactor passed 90 tests on the existing software. The [G03 interaction Report][g03-ergon-report] records candidate correspondence, source preservation and the reserved HCRL.005 layout prerequisite. The [G03 interaction Task][g03-ergon-task] retains the original instruction and unresolved instruction-entry time.

### G03 review · Petasos–Mneme Interaction Draft.000.003

**Candidate correspondence** · The [interaction draft][g03-petasos-draft] describes Mneme establishing one authoritative undertaking per matching subscription before Petasos hands off its reference in a by-value parcel. The consumer resolves the existing realisation through Mneme. ContextLoader and Mneme establish or reconstruct transient working information when execution requires it. Undertaking persistence, durable acceptance, delivery/acknowledgement, execution, save and IU commitment retain independent evidence. Partial fan-out, indeterminate outcomes, duplicate delivery and permitted retry preserve the same obligation's identity and any applicable `DoNotBefore` constraint.

**Disposition requested** · Review the exact candidate and [reconciliation evidence][g03-petasos-evidence]. The qualified PetasosParcel account refers to originating work and places undertaking establishment at the consumer. The candidate changes the reference target and establishment responsibility. Confirm canonical correspondence, minimum recoverable state, acknowledgement ownership and evidence, retry equivalence and trusted metadata. Retain the earlier DR-12 handoff, selector, scheduling, trust, context and specialisation obligations. Continuum incorporation remains pending.

**Verification** · The bounded ArchUnit reactor passed 90 tests on the existing software. The [G03 Petasos–Mneme Report][g03-petasos-report] records candidate checks, source preservation and the reserved HCRL.005 layout prerequisite. The [G03 Task][g03-petasos-task] retains the original instruction and unresolved instruction-entry time.

### G03 review · Collective Pragma-family consistency and completeness

**Review result** · The [cross-document reconciliation][g03-family-evidence] pins and compares the five exact candidate revisions. It finds a coherent core for work identity, metadata-only selection, reference handoff of an established realisation, WorkSpace access and independently evidenced delivery, execution, save and persistence. It identifies inconsistent Twin execution wording and publication-gate scope, distinguishes the qualified handoff and work-model incorporation differences, and retains the unresolved cache, selector, acknowledgement, recovery, security and Assurance contracts. Prior issues and dispositions carry forward.

**Disposition requested** · Adjudicate the five candidates collectively with the exception register. Authorise the bounded wording and reference corrections or explicitly disposition them. Record the scope and standing of affected canonical changes, required contracts and any deferrals before dependent incorporation or implementation. The design-level review supports focused contract and backlog preparation and candidate verification cases. The draft set remains pending for collective acceptance.

**Verification** · All five candidate bytes and earlier evidence were preserved. The bounded ArchUnit reactor passed 90 tests on the existing software. The [collective review Report][g03-family-report] records the checks and the reserved HCRL.005 layout prerequisite. The [review Task][g03-family-task] retains the original instruction and unresolved instruction-entry time.

### G03 reconciliation · Issued adjudications, revised candidates and audit investigation

**Candidate reconciliation** · The [issued G03 adjudications][g03-adjudication-task] assign publication eligibility to Ponos with evaluation by Mneme for every newly created IU under the producing Ergon's PublishingMask (default `ALLOW_ALL`). Non-matches retain IU governance. Petasos owns and performs matching, routing and queue management and owns Pragmatosis generation. Mneme supplies governed information and constructs the requested objects in its PragmaFactory. R1/R2 selection uses summary/metadata. R3+ may also evaluate authorised IU payload. The subscription identifies the intended Ergon queue. Its parcel carries the established undertaking's identity, which the receiving Ergon resolves through Mneme. Themis enforcement remains independent of the functional allocations.

**Execution and information** · The five [revised candidates][g03-adjudications-candidates] align WorkSpace with the operational existence pocket of the executing Ergon and preserve the accepted two-cache and authoritative persistence boundaries. TaskOutcome concerns actual Ergon-instance processing under Praxis-defined rules. Undertaking disposition, output mediation, Assurance and originating Pragma disposition retain distinct meanings. Concurrent equivalent processing and recovery require execution attribution. Zero subscribers yield no Ergon TaskOutcome.

**Prior authority and residual decisions** · The [adjudication register][g03-adjudications] records PF-01–PF-07 and PF-09 as partially resolved, PF-08, PF-10 and PF-11 as unresolved, and PF-12 as editorial only. Canonical Twin, work/outcome, publication and handoff correspondence remains pending alongside the summary allocation, selector, acknowledgement, retry, access/save, trust and Assurance contracts. In particular, the qualified prior allocation of semantic selection to Mneme, egress-Pragma filtering, originating-work parcel reference/receiver-side creation and the existing TaskOutcome Topic require explicit correspondence treatment. Existing DR-7, DR-10 and G03 A06/A07 obligations carry forward.

**Petasos–Kleio candidate** · Petasos contributes its own auditable occurrences. Mneme contributes separate evidence for its information operations. The local integration corresponds to the established Kleio Audit Capture client contract. Its exact per-engine Audit.Agent/Audit.Satellite identity remains a modelling gap. Artemis 2.33.0 supports a local transaction for two properly routed persistent sends in one transactional session on one broker resource. That commit can cover the parcel and audit occurrence with the applicable durability settings. Mneme/Mnemosyne state and the later AuditEvent write retain their own transactions. The [audit candidate][g03-audit-investigation] records the durability-parity constraint, failover and ambiguity handling, staged-evidence custody through downstream durability and acknowledgement, and the required Solution Architect decisions. Treating broker staging as authoritative capture is pending explicit review. The current Kleio ownership, capture and fail-closed rules govern.

**Verification and standing** · The offline Java 21 ArchUnit reactor passed 90 tests against the existing software. The review checked candidate/source correspondence and the local references. The 005 evidence layout checker still requires a matching HCRL.005, while this instruction explicitly targets cumulative HCRL.004. The instruction-entry timestamp remains unresolved. [G03 reconciliation Report][g03-adjudication-report] records verification and limits. DR-12 and HCRL.004 remain pending Solution Architect review.

### G03 reconciliation · Petasos routing decisions and recovery

**Issued routing design** · The [routing candidate][g03-routing-draft] records the Solution Architect's direction for both routers. Subscription-Router owns matching. Praxis-Router invokes the Ponos-owned Praxis evaluation service and fulfils its successor obligations. Both establish the decision and full obligation set through Mneme/Mnemosyne before individual construction and queue injection. Recovery uses the recorded results, preserves successful siblings and stable work identities, and keeps uncertain effects explicit. Petasos owns routing orchestration and reconciliation. Mneme's PragmaFactory constructs the requested work and Mnemosyne establishes authoritative persistence. Themis and Kleio retain their existing boundaries. [Operative Task][g03-routing-task]

**Continuum correspondence** · The qualified baseline assigns subscription semantic selection to Mneme and describes an originating-work parcel reference with consumer-side undertaking creation. G03 uses Petasos matching and a parcel referencing the already-established Pragmatosis. The Ponos, Praxis and Pragma accounts assign output/continuation routing to Ponos/Ergo. The candidate makes the Ponos evaluation service and Petasos fulfilment split explicit. Canonical mapping of the continuation interaction, successor work and distinct TaskOutcome meanings remains pending. Energeia retains Praxis management. The candidate uses the existing by-value and Artemis routing mandates and the existing governed persistence contracts. The pending Mnemosyne decomposition and Petasos–Kleio audit design retain their separate standing.

**PF-07 and PF-08** · The issued strategy settles the candidate ordering, durable recovery basis and preservation of same-obligation identity after capture. A missing routing-record update or a NULL does not establish failed injection. Preserve separate authoritative work, durable queue acceptance, delivery, execution, save and durable information outcomes. Determine pre-capture trigger/acknowledgement ownership, authoritative proof of indeterminate effects and safe re-injection, and the exact decision/obligation/work identities, concurrency, retry/replay and execution-eligibility contracts. The Partially resolved PF-07 and Unresolved PF-08 register entries retain their standing for the full contracts. [Prior PF register][g03-adjudications] · [Reconciliation Report][g03-routing-report]

**Verification and standing** · The offline Java 21 ArchUnit reactor passed 90 tests against existing software. The [reconciliation Report][g03-routing-report] holds exact candidate correspondence, checks and open contract decisions. The Iteration 005 layout check requires a matching HCRL.005, while this task addresses cumulative HCRL.004. The instruction-entry time remains unresolved. HCRL.004 and DR-12 remain pending Solution Architect review.

### G03 revision · Petasos routing Draft.000.002

**Ergon Egress and Praxis routing** · The [revised candidate][g03-routing-draft-002] applies the [issued Revision 002 decisions][g03-routing-task-002]. Ergon Egress blocks dead tasks, applies the outbound Themis PEP, requests construction of the originating Pragma list through Mneme's PragmaFactory, establishes governed persistence through Mneme/Mnemosyne, and hands the existing list to Petasos. Subscription-Router retains the matching and subscriber-specific Pragmatosis design. Praxis-Router invokes the Ponos-owned evaluation service on the existing list and fulfils obligations for Ponos-selected originating Pragma identities and destination Ergons. It resolves the existing work and preserves each identity even if several rules select it. This corrects Draft.000.001's illustrative Praxis-side construction. Both routers retain decision-before-fulfilment and recovery against recorded obligations.

**Authoritative-provenance recovery** · Governed Provenance can reconstruct the original list by execution and egress occurrence when it establishes exact, complete output membership and applicable durable state. This design requires no additional durable list record solely for the handover. A partial set, indeterminate construction or missing acknowledgement supplies no proof of completion or non-occurrence. Recovery preserves original identities and lineage, checks each applicable router's existing decision before any new obligations, and preserves established effects. Construction, handover, routing decision, queue acceptance, delivery, execution, save and authoritative mutation retain separate evidence. Broker-local transactions supply no atomicity across Mneme and Artemis.

**Authority and residual decisions** · The [revision reconciliation Report][g03-routing-report-002] records the qualified baseline differences for Mneme semantic selection, older receiver-side undertaking creation, U18 Ponos/Ergo routing and continuation representation, and TaskOutcome. Resolve direct Praxis PetasosParcel/work-realisation correspondence and the exact Ponos filter and trigger/result model. Existing Provenance authority supports references and governed access. Egress occurrence identity, complete-set proof, durable evidence and governed lookup remain contract work. PF-07 remains partially resolved, including open acknowledgement and safe recovery proof. PF-08 remains unresolved at contract level, including equivalence across execution, retry/replay and concurrent recovery, permitted re-injection and execution eligibility. All previous dispositions and evidence retain their standing.

**Verification and standing** · All 90 offline Java 21 ArchUnit tests passed against existing software. Draft.000.001 and previous evidence were preserved. The [revision Report][g03-routing-report-002] records the candidate checks, unresolved instruction-entry timestamp and Iteration 005 layout-check limitation. HCRL.004 and DR-12 remain pending Solution Architect review.

### G03 revision · Petasos routing Draft.000.003

**Pre-routing reconciliation** · The [revised candidate][g03-routing-draft-003] records the [issued Revision 003 decisions][g03-routing-task-003]. Both routers recover the existing durable decision or establish a new decision and complete obligation set under the owning contract. Required reconciliation excludes confirmed completions from the working list and preserves their authoritative history. Indeterminate injection requires authoritative reconciliation or a hold. It does not automatically permit reinjection. The Subscription-Router, Praxis-Router, Ergon Egress and authoritative-provenance recovery designs retain their established responsibilities.

**Conditional recovery and shared status** · `moduleStartupInstant()` and `lastSuccessfulRoutingActivity()` are operational `Instant` properties. An earlier last success can trigger reconciliation. Absence has no synthetic value, and success in one set cannot suppress recovery in another. Loss of ownership or continuity, conflict and indeterminate results also trigger reconciliation. Petasos owns the meaning and use of ephemeral `PetasosRoutingSharedStatus`. Mneme manages cross-instance ownership at RoutingSetList scope. Durable decisions, work and Provenance retain the authoritative recovery facts. Conflicting same-set activity is prevented while independent sets may proceed. A stale or failed owner must be excluded from further effects after takeover. Refresh or validate the authoritative decision and obligation view under valid ownership before using it. The shared status and timestamps supply no broker acceptance or durable fencing proof.

**PF-08 — Architectural strategy resolved; detailed transport, concurrency and recovery contracts outstanding.** The earlier review entries preserve their historical standing. PF-07 remains partially resolved. Exact decision/set/obligation identity, evidence of indeterminate Artemis acceptance and safe reinjection, ownership/fencing and recovery, timestamp binding and initialisation, handoff ACK/retry equivalence and execution eligibility still need contracts and verification. Mneme's existing active-state coordination supports the direction. Its accepted individual-IU concurrency and qualified cache architecture establish no RoutingSetList contract, placement or cross-instance fencing guarantee. MC-01–MC-07, direct Praxis parcel/work-realisation and other residual G03 obligations remain in scope where applicable. [Reconciliation Report][g03-routing-report-003]

**Verification and standing** · All 90 offline Java 21 ArchUnit tests passed against existing software. Earlier drafts and evidence were preserved. The [revision Report][g03-routing-report-003] records candidate checks, unresolved instruction-entry time and the Iteration 005 layout-check limitation. HCRL.004 and DR-12 remain pending Solution Architect review.

### G03 candidate · Harmonia processing framework and Checkpoints

**Candidate scope** · The [processing framework Draft.000.001][g03-processing-draft] applies the [issued direction][g03-processing-task] using the [Ergon route and Checkpoint terminology reconciliation][g03-processing-terminology] as its starting point. It distinguishes Intra-Module Orchestration, Inter-Module Orchestration and the less mature OA&M Oversight subject. Ponos/common Ergon is the Tier 1 reference, with DigitalTwin-Behaviour reuse and role-specific Pylai adaptations. The candidate uses Camel processing stages and lightweight synchronous Checkpoints. CDI `@Dependent` is a lifecycle option under the execution-isolation requirement. Existing logical and deployment boundaries retain their standing.

**Route and Checkpoints** · The single-row Ponos candidate retains the issued sequence with provisional H01–H08 handovers. Context load/unload and governed save, PublishingMask evaluation and dead-task blocking, Themis release and pre-handover originating Pragma creation/persistence retain separate meanings. Adjudicate the filter slot and H08 Petasos fact. A Checkpoint validates applicable incoming state, forwards synchronously and verifies the owning outcome. Pass/Fail expresses the conceptual proceed/withhold decision and preserves specific outcomes, including Indeterminate. Per-checkpoint logging, audit and persistence granularity must preserve mandatory obligations. Mneme housekeeping and authoritative outcomes remain distinct. Dokimasia retains independent Assurance, and Kleio retains authoritative capture and fail-closed behaviour.

**G03 dependencies** · PF-07 remains partially resolved. Checkpoint identity, recovery boundary selection, minimum evidence and acknowledgement ownership remain open, including authoritative work establishment and separate routing and queue evidence. PF-08 remains architectural strategy resolved with detailed transport, concurrency and recovery contracts outstanding. The candidate supplies no retry, reinjection, fencing or execution-admission proof. Preserve the prior publication, parcel/work-realisation, TaskOutcome, delegation, context and related G03 qualifications. Pylai adaptations and OA&M scope still require adjudication. [Reconciliation Report][g03-processing-report]

**Verification and standing** · The bounded offline Java 21 ArchUnit reactor passed 90 tests on existing software. The [reconciliation Report][g03-processing-report] records document checks, the unresolved instruction-entry time and the existing Iteration 005 layout-check failure. The processing framework candidate, HCRL.004 and DR-12 remain pending Solution Architect review.

<a id="g03-processing-framework-draft-000-002"></a>

### G03 revision · Harmonia processing framework and Checkpoints Draft.000.002

**Directed candidate sequence** · The [issued Draft.000.002 Task][g03-processing-task-002] specifies the common Ergon processing sequence recorded in [Draft.000.002 §4][g03-processing-route-002]. After ingress enforcement, the Context Loader establishes the WorkSpace and Ergon business logic uses it. Context Unloader then closes off the WorkSpace and returns changed working information for governed save. The separate Output Unload/Validate function establishes discrete output IUs through governed validation, creation and persistence. Outcome Decision determines the attributable Ergon-instance execution outcome under Praxis, with separate undertaking and parent-work dispositions. Correspondence to the existing ActionableTask-resolution TaskOutcome Topic remains open.

**Release and Petasos handover** · The filter slot evaluates the producing Ergon's PublishingMask after Outcome Decision. Ponos owns eligibility and Mneme evaluates the mask. Outbound Themis enforcement independently governs release. The existing dead-task block remains required before the outbound PEP, with exact placement open. After outbound enforcement, Pragma Create invokes Mneme's PragmaFactory inside the function using eligible and authorised output IU identities. Only authoritatively established originating Pragma identities may be supplied to Petasos Route-In under the applicable handover contract. Ponos retains separate Praxis evaluation invoked by Petasos.Praxis-Router. Output IU persistence, eligibility, release, originating Pragma establishment and Petasos acceptance retain their own outcomes and evidence.

**Checkpoints and open contracts** · The route shows 11 provisional H-points, including initial H01 and terminal H11. Each defined handover requires synchronous Checkpoint execution. Checkpoint-state persistence follows its policy and mandatory recovery requirements. Independent logging, audit and persistence settings preserve mandatory safeguards and Dokimasia's independent Assurance. PF-07, the qualified PF-08 strategy disposition and their outstanding contract work retain their standing. Resolve H11 acceptance, acknowledgement and evidence, direct Praxis work-realisation correspondence, checkpoint identity and lifecycle, durable recovery boundaries, minimum evidence, granularity semantics, trusted-context propagation, CDI lifecycle/performance and independent Assurance integration. The three-tier model and scope of reuse carry forward from Draft.000.001.

**Verification and standing** · The bounded offline Java 21 ArchUnit reactor passed all 90 existing-software architecture tests. The [execution Report][g03-processing-report-002] records the document checks and Iteration 005 layout-check limitation. Draft.000.002, DR-12 and HCRL.004 remain pending Solution Architect review.

<a id="g03-processing-framework-draft-000-003"></a>

### G03 revision · Harmonia processing framework and Checkpoints Draft.000.003

**WorkSpace and unloading** · [Draft.000.003][g03-processing-draft-003] incorporates the [issued adjudications][g03-processing-task-003] as candidate direction. The WorkSpace holds immutable context-loaded and explicitly retrieved IUs, and processor-generated information. The latter comprises output IUs in `.output()` and distinct Ergon-owned `Workspace.Processor.Status`. Ergon business logic defines the release-branch rules. Context Unloader applies them using the status, prepares generated content with appropriate References and closes off the WorkSpace. Output Unload/Validate separately establishes the discrete IUs under governed Mneme contracts. The earlier Ergon–Mneme changed-working-value return requires explicit correspondence before any companion revision or canonical incorporation.

**Status domains and Praxis coordination** · Mneme manages persistence and availability of all three status domains. Ergon owns `Workspace.Processor.Status`. Its operational lifecycle completes following closure of the processing instance for the applicable Pragma, and the relevant status information ultimately resides in the authoritative AuditEvent that Kleio constructs and preserves. The representation, capture interaction and timing remain open under mandatory audit requirements. Ponos owns `Pathway.Processing.Status` and functional Praxis progression and concurrency. The Ponos-owned Praxis.Server (Manager) and local Praxis.Agent embedded in each Ergon-Execution-Module express that processing responsibility. Energeia retains Praxis definition management. The Server/Agent interface and detailed responsibility split remain open, as does the Ergon/Ponos allocation of individual `Pragma.Metadata.Status` transitions. The three domains remain distinct from the Ergon-instance execution outcome.

**Filter and continuity** · The adjudication resolves dead-task placement inside Egress Filter. Block under the applicable task state first, then evaluate PublishingMask on remaining output IUs. Preserve distinct reasons and the governing contracts for status consequences. The outbound Themis PEP retains release authorisation. The ten-function sequence, H01–H11, synchronous Checkpoints, configurable persistence subject to mandatory requirements, internal PragmaFactory invocation, successful Pragma establishment before handover and independent Dokimasia Assurance carry forward. PF-07 and the qualified PF-08 strategy disposition remain in force, including outstanding interaction, evidence, acknowledgement and reconciliation obligations. The three-tier architecture and scope of application carry forward.

**Verification and standing** · The bounded offline Java 21 ArchUnit reactor passed all 90 existing-software architecture tests. The [execution Report][g03-processing-report-003] records the document checks and Iteration 005 layout-check limitation. Draft.000.003, DR-12 and HCRL.004 remain pending Solution Architect review.

<a id="g03-processing-framework-draft-000-004"></a>

### G03 revision · Harmonia processing framework and Checkpoints Draft.000.004

**WorkSpace-to-Egress handover** · [Draft.000.004][g03-processing-draft-004] records the [issued adjudications][g03-processing-task-004] for final review. Context Unloader uses Ergon-owned `Workspace.Processor.Status` and Ergon-defined branch rules to prepare generated content and appropriate IU References. It releases the entire Pragma by value into the existing Camel Exchange and then closes the WorkSpace. H05 receives the Egress Pragma synchronously. That Pragma supplies the context for Egress processing independently of the closed WorkSpace. Output Unload/Validate extracts and validates generated output information from it and establishes the discrete IUs under the existing Mneme contracts. Exchange transfer and authoritative persistence are distinct.

**Work identity and onward routing** · The Egress Pragma retains its work identity across the by-value handover. Pragma Create uses eligible and authorised output IU identities to invoke Mneme's PragmaFactory internally and requests creation and authoritative persistence of distinct originating Pragmas. Only successfully established originating Pragma identities are supplied to Petasos Route-In. The existing Pragma model, route sequence, H01–H11, Egress Filter, outbound Themis PEP and Checkpoint governance carry forward.

**Processing-status disposition and open matters** · The complete `Workspace.Processor.Status` ends its lifecycle in the authoritative AuditEvent following closure of the processing instance for the applicable Pragma. Kleio constructs and preserves that AuditEvent. Governed references may represent substantial underlying content under the existing longitudinal evidence rules. Detailed representation, capture interaction and timing remain open within the mandatory audit requirements. Ergon and Ponos retain their semantic responsibilities, Mneme manages status persistence and availability, and Dokimasia independently assures. Preserve the three status domains, existing G03 qualifications, TaskOutcome correspondence, `Pragma.Metadata.Status` transition allocation, Praxis.Server / Praxis.Agent interfaces, detailed Checkpoint contracts and Petasos acknowledgement and routing responsibility, including PF-07 and the remaining detailed PF-08 obligations. The earlier changed-working-value return remains a correspondence matter for the owning contracts.

**Verification and standing** · The bounded offline Java 21 ArchUnit reactor passed all 90 existing-software architecture tests. Draft.000.004 is prepared for final architectural review. The [execution Report][g03-processing-report-004] records the document checks and Iteration 005 layout-check limitation. Draft.000.004, DR-12 and HCRL.004 remain pending Solution Architect review.

<a id="g03-processing-framework-final-001"></a>

### G03 disposition · Harmonia Processing Framework and Checkpoints Final.001

**Solution Architect disposition** · **Accepted with qualifications.** Mark Hunter, Solution Architect, accepted the exact [Draft.000.004][g03-processing-draft-004] in the [operative acceptance Task][g03-processing-final-task]. The accepted draft has SHA-256 `61b80fc885cc37ec783d878282ab20b6bc1059fb35da1d95a1463586d0289ef0`, matching its [review evidence][g03-processing-report-004]. [Final.001][g03-processing-final] records the same architectural content. The authority covers this offline DRP and its bounded finalisation. Implementation and Architecture Continuum incorporation require separate authority. HCRL.004, DR-12 and the collective G03 review remain pending. Other G03 DRPs retain their recorded standing.

**Qualifications and obligations carried forward**

- **PF-07** remains partially resolved. Petasos acknowledgement and routing responsibility, Egress completeness evidence and recovery, and the owning acceptance and handover contracts remain open.
- **PF-08** retains its qualified architectural-strategy disposition. Detailed routing, transport, concurrency and recovery contracts remain outstanding, including the recorded evidence, retry and reconciliation obligations.
- Preserve the owning reviews for `Pragma.Metadata.Status` transition ownership, TaskOutcome correspondence, Ergon–Mneme changed-working-value reconciliation, processing-status AuditEvent representation and capture integration, Praxis.Server / Praxis.Agent detailed interactions, and Checkpoint configuration, evidence and implementation contracts. Carry forward every other qualification in [Final.001 §6][g03-processing-final-qualifications] and [§7][g03-processing-final-register].

**Verification** · The bounded offline Java 21 ArchUnit reactor passed all 90 architecture tests on the existing software. The [finalisation Report][g03-processing-final-report] records the preservation checks and the existing Iteration 005 layout-check limitation.

<a id="g03-pf-07-ergon-egress-petasos-handover"></a>

### G03 disposition · PF-07 Ergon Egress to Petasos handover

**PF-07 — Architectural responsibilities and normal-path behaviour resolved. Detailed contracts and conformance evidence remain outstanding.** The [issued Solution Architect adjudications][g03-pf07-task] are recorded in [Petasos Routing Draft.000.004][g03-pf07-routing-draft]. They apply the H10 and H11 boundaries in [accepted Processing Framework Final.001][g03-processing-final] with its sequence, Checkpoints and standing preserved.

**H10 creation and completeness** · Ergon Egress owns the expected originating Pragma set and its completeness. The conceptual `ErgonCreatedPragmaList` has `expectedListSize` and `createdList` of `ErgonCreatedPragmaToken` entries. Each token records the originating `pragmaId` and creation/persistence `status` attributable to the producing execution and Egress occurrence. Completion requires both cardinality correspondence and confirmed authoritative establishment of each required member. Mneme owns governed construction through PragmaFactory and coordinates persistence. Mnemosyne establishes authoritative durability. The Egress Pragma retains its distinct identity. [H10 information contract][g03-pf07-h10]

**H11 complete-list acceptance** · Petasos accepts routing responsibility for the entire complete list on an all-or-nothing basis and durably establishes that responsibility before H11 claims success. The conceptual `PragmaListHandoffStatus` from `Petasos.RoutingEngine.Route(createdList)` distinguishes receipt (`received = GOOD`) from durable commitment (`commited = TRUE`). Concrete interface and acknowledgement representation remain open. A valid zero-subscriber result can follow acceptance and leaves it in force. Subscriber matching, router decisions, queue acceptance, delivery and execution retain their own outcomes. Preserve confirmed work and effects through failure, incomplete creation and uncertain acknowledgement. A permitted repeat retains the same originating work identities. [H11 complete-list acceptance][g03-pf07-h11]

**Outstanding contracts and standing** · Retain exact membership, completeness and acceptance proof, acknowledgement representation and custody, evidence, retry/idempotence and recovery in the owning contracts. H11 accepts the complete list at Petasos. Different progress across applicable routers is downstream reconciliation under PF-08. All PF-08 dispositions and qualifications retain their standing. The Pragma Model, Petasos–Mneme interaction and publication/subscription correspondence are bounded companion follow-ups. Prior dispositions and other G03 obligations carry forward. Draft.000.004, DR-12, HCRL.004 and the collective G03 review remain pending. [PF-07 disposition and follow-up][g03-pf07-companions]

**Verification** · The bounded offline Java 21 ArchUnit reactor passed all 90 architecture tests on the existing software. The [reconciliation Report][g03-pf07-report] records the checks and the existing Iteration 005 layout-check limitation.

<a id="g03-petasos-routing-final-001"></a>

### G03 disposition · Petasos Routing and Reconciliation Final.001

**Solution Architect disposition** · **Accepted with qualifications.** Mark Hunter, Solution Architect, accepted exact [Routing Draft.000.004][g03-pf07-routing-draft] in the [operative acceptance Task][g03-routing-final-task]. The draft has SHA-256 `7e71bf7d2fa84cd2d3beae723d0412804416ecab0ebfcf733e0b9b39aa7ece91`, matching the [PF-07 review evidence][g03-pf07-report]. [Routing Final.001][g03-routing-final] records the same architecture as the accepted offline design. Implementation and Architecture Continuum incorporation require separate authority. HCRL.004, DR-12 and the collective G03 review remain pending. Other DRPs retain their recorded standing.

**PF-07 and PF-08** · The acceptance covers the PF-07 H10/H11 contract. At H10, Ergon Egress owns expected-set completeness. The `ErgonCreatedPragmaList` contract retains `expectedListSize` and per-Pragma creation and persistence status. Completion needs both count correspondence and confirmed authoritative establishment of every required member through the existing Mneme/Mnemosyne contracts. At H11, Petasos durably accepts routing responsibility for the complete list on an all-or-nothing basis. Receipt, acceptance, subscription and Praxis decisions, queue acceptance, delivery and execution retain separate outcomes. A valid zero-subscriber disposition leaves H11 acceptance in force. The accepted Processing Framework's boundaries and sequence carry forward. PF-08 retains its existing architectural-strategy disposition and qualifications. [Accepted Processing Framework][g03-processing-final] · [PF-07 disposition][g03-routing-final-pf07] · [PF-08 qualified disposition][g03-routing-final-pf08]

**Qualifications and conformance carried forward** · Retain exact membership and completeness proof, durable acknowledgement and correlation, failure and indeterminate-outcome evidence, recovery, replay and idempotence, RoutingSetList ownership and concurrency controls, and authoritative Provenance reconstruction. Preserve the Pragma Model, publication/subscription and Petasos–Mneme companion correspondence, trusted security context and mandatory audit evidence, and direct Praxis routing and work-realisation correspondence. Detailed contracts and conformance evidence remain outstanding under the existing reviews. [Architecture correspondence][g03-routing-final-qualifications] · [Conformance expectations][g03-routing-final-conformance]

**Verification** · The bounded offline Java 21 ArchUnit reactor passed all 90 architecture tests on the existing software. The [finalisation Report][g03-routing-final-report] records the preservation checks and the existing Iteration 005 layout-check limitation.

[predecessor]: hcrl-software-0.1.0-001.pending.md
[software]: ../../../governance/software-development-praxis.md
[praxis]: ../../../governance/architecture-review-praxis.md
[carry]: ../../evidence/software-0.1.0/004/after/HCRL-Carry-Forward-Reconciliation.md
[task]: ../../evidence/software-0.1.0/004/before/ai-agent-tasks/unresolved-f-01-design-consolidation-and-hcrl-preparation.md
[report]: ../../evidence/software-0.1.0/004/after/ai-agent-reports/20261120T022608Z-f-01-design-consolidation-and-hcrl-preparation.md
[final]: ../../offline-design/DRP-Topic-and-TopicMask.Final.001.000.md
[draft001]: ../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md
[draft002]: ../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md
[draft]: ../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md
[topic-final-001-001]: ../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[topic-final-java]: ../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#8-accepted-java-surface
[topic-final-obligations]: ../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#9-open-decisions-and-evidence
[topic-final-criteria]: ../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#10-mandatory-behavioural-acceptance
[topic-acceptance-boundary]: hcrl-software-0.1.0-007.pending.md
[topic-acceptance-task]: ../../evidence/software-0.1.0/007/before/ai-agent-tasks/unresolved-topic-topicmask-finalisation-task.md
[topic-acceptance-report]: ../../evidence/software-0.1.0/007/after/ai-agent-reports/20261123T204533Z-topic-topicmask-finalisation-report.md
[acceptance]: ../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#10-mandatory-behavioural-acceptance
[rad]: ../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3
[decisions002]: ../../evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md
[decisions003]: ../../evidence/software-0.1.0/003/before/F-01-Topic-TopicMask-Adjudications-Continuation.md
[decisions004]: ../../evidence/software-0.1.0/004/before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md
[delta]: ../../evidence/software-0.1.0/004/after/F-01-Decision-Delta.md
[reconciliation]: ../../evidence/software-0.1.0/004/after/F-01-Architecture-Reconciliation.md
[obligations]: ../../evidence/software-0.1.0/004/after/F-01-Implementation-Gap-Analysis.md
[ps-draft]: ../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.001.md
[ps-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-pragma-publication-and-subscription-task.md
[ps-reconciliation]: ../../evidence/software-0.1.0/005/after/Pragma-Publication-and-Subscription-Reconciliation-005.md
[ps-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261120T224751Z-pragma-publication-and-subscription-report.md
[ps-pragmaton]: ../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.003.md
[ps-draft-002]: ../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[ps-reconciliation-001]: ../../evidence/software-0.1.0/005/after/Pragma-Publication-and-Subscription-Reconciliation-005.draft-000.001.md
[ps-follow-up-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-pragmaton-publication-subscription-reconciliation-task.md
[ps-follow-up-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261120T225548Z-pragmaton-publication-subscription-reconciliation-report.md
[g03-pragma-draft]: ../../offline-design/DRP-Mneme-Pragma-Model.draft.000.003.md
[g03-pragma-evidence]: ../../evidence/software-0.1.0/005/after/G03-Mneme-Pragma-Model-Reconciliation-005.md
[g03-pragma-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-mneme-pragma-model-reconciliation-task.md
[g03-pragma-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261120T230912Z-g03-mneme-pragma-model-reconciliation-report.md
[g03-ergon-draft]: ../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md
[g03-ergon-evidence]: ../../evidence/software-0.1.0/005/after/G03-Ergon-to-Mneme-Pragmaton-Reconciliation-005.md
[g03-ergon-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-ergon-to-mneme-pragmaton-reconciliation-task.md
[g03-ergon-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261120T231812Z-g03-ergon-to-mneme-pragmaton-reconciliation-report.md
[g03-petasos-draft]: ../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.000.003.md
[g03-petasos-evidence]: ../../evidence/software-0.1.0/005/after/G03-Petasos-to-Mneme-Reconciliation-005.md
[g03-petasos-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-to-mneme-reconciliation-task.md
[g03-petasos-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261120T232458Z-g03-petasos-to-mneme-reconciliation-report.md
[g03-family-evidence]: ../../evidence/software-0.1.0/005/after/G03-Pragma-Family-Cross-Document-Reconciliation-005.md
[g03-family-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-pragma-family-cross-document-reconciliation-task.md
[g03-family-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261121T061125Z-g03-pragma-family-cross-document-reconciliation-report.md
[g03-adjudications]: ../../evidence/software-0.1.0/005/after/G03-Pragma-Family-Adjudications-005.md
[g03-adjudications-candidates]: ../../evidence/software-0.1.0/005/after/G03-Pragma-Family-Adjudications-005.md#exact-candidate-revisions
[g03-adjudication-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-pragma-family-adjudication-reconciliation.md
[g03-audit-investigation]: ../../offline-design/DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md
[g03-adjudication-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261121T101132Z-g03-pragma-family-adjudication-reconciliation.md
[g03-routing-draft]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md
[g03-routing-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-and-reconciliation-task.md
[g03-routing-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261121T213340Z-g03-petasos-routing-and-reconciliation-report.md
[g03-routing-draft-002]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md
[g03-routing-task-002]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-draft-000-002-task.md
[g03-routing-report-002]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261121T214710Z-g03-petasos-routing-draft-000-002-report.md
[g03-routing-draft-003]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md
[g03-routing-task-003]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-draft-000-003-task.md
[g03-routing-report-003]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261121T223655Z-g03-petasos-routing-draft-000-003-report.md
[g03-processing-draft]: ../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.001.md
[g03-processing-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-processing-framework-and-checkpoints-task.md
[g03-processing-terminology]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261122T111009Z-g03-ergon-route-checkpoint-reconciliation-report.md
[g03-processing-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261122T211332Z-g03-processing-framework-and-checkpoints-report.md
[g03-processing-route-002]: ../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.002.md#4-reference-ponos--ergon-processing-route
[g03-processing-task-002]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T020707Z-g03-processing-framework-and-checkpoints-draft-000-002-task.md
[g03-processing-report-002]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261123T021004Z-g03-processing-framework-and-checkpoints-draft-000-002-report.md
[g03-processing-draft-003]: ../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.003.md
[g03-processing-task-003]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T024204Z-g03-processing-framework-and-checkpoints-draft-000-003-task.md
[g03-processing-report-003]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261123T024438Z-g03-processing-framework-and-checkpoints-draft-000-003-report.md
[g03-processing-draft-004]: ../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.004.md
[g03-processing-task-004]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T025640Z-g03-processing-framework-and-checkpoints-draft-000-004-task.md
[g03-processing-report-004]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261123T025859Z-g03-processing-framework-and-checkpoints-draft-000-004-report.md
[g03-processing-final]: ../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md
[g03-processing-final-qualifications]: ../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#6-reconciliation-and-g03-dependencies
[g03-processing-final-register]: ../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#7-decision-and-verification-register
[g03-processing-final-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T030653Z-g03-processing-framework-and-checkpoints-final-001-task.md
[g03-processing-final-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261123T030855Z-g03-processing-framework-and-checkpoints-final-001-report.md
[g03-pf07-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T033042Z-g03-pf-07-ergon-egress-petasos-handover-task.md
[g03-pf07-routing-draft]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md
[g03-pf07-h10]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md#21-h10--originating-pragma-creation
[g03-pf07-h11]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md#22-h11--petasos-route-in-and-complete-list-acceptance
[g03-pf07-companions]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md#72-pf-07-disposition-and-companion-follow-up
[g03-pf07-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261123T033325Z-g03-pf-07-ergon-egress-petasos-handover-report.md
[g03-routing-final]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.Final.001.md
[g03-routing-final-task]: ../../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T040005Z-g03-petasos-routing-and-reconciliation-final-001-task.md
[g03-routing-final-pf07]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.Final.001.md#72-pf-07-disposition-and-companion-follow-up
[g03-routing-final-pf08]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.Final.001.md#71-pf-08-qualified-disposition
[g03-routing-final-qualifications]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.Final.001.md#7-architecture-correspondence-and-open-decisions
[g03-routing-final-conformance]: ../../offline-design/DRP-Petasos-Routing-and-Reconciliation.Final.001.md#8-conformance-expectations
[g03-routing-final-report]: ../../evidence/software-0.1.0/005/after/ai-agent-reports/20261123T040158Z-g03-petasos-routing-and-reconciliation-final-001-report.md
