# Domain04 — Information Unit Documentation and Integration — Step 3

**Date:** 2026-10-09. **Status:** COMPLETE within the authorised documentation/integration boundary. **Domain04 semantic sufficiency: NO.**

## Task Goal

Integrate the human-approved Harmonia-managed information and Information Unit architecture into canonical Domain04: its semantic boundary, Identity / Metadata / Content / Context partition, release-bound structures, explicit architectural evolution and platform/framework obligations. Preserve existing concepts and deliberately unresolved architecture without deriving attributes, schemas, Data Objects, components or implementation.

This is Architecture Completion Programme stage 3, Domain04 reconciliation Step 3. It is not convergence/runtime implementation. The supplied task decisions authorise this specific package; the Step 2 report's recommendation for a different next package is not an instruction or architectural authority.

## Authority and Context Loaded

The task began from fresh repository-held context. Loaded:

- [Root AGENTS.md](../../AGENTS.md) and [docs/AGENTS.md](../../docs/AGENTS.md); no narrower scoped instruction file was found for these paths.
- [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md), including programme position, canonical-corpus rules, progressive authority loading and completion gates.
- [Canonical Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), including AX-18 and supporting standards/enforcement material.
- [Step 1 assessment](2026-10-09-domain04-information-architecture-reconciliation-step1.md) and [Step 2 corrections report](2026-10-09-domain04-information-architecture-reconciliation-step2.md), as evidence/navigation only.
- The current 25-document [Domain04 corpus](../../docs/markdown/04-information-architecture/README.md): metamodel, governance, patterns, assemblies/views, guardrails, family definitions/standing and traceability; review dispositions and relevant approved boundaries were distinguished from historical quotations and candidate propositions.
- [Strategy Digital Twin definition and coordination boundary](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md#component-7-digital-twin-entity-centred-operational-coordination-construct), [Business information responsibility/non-transfer](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md#12-non-transfer-of-ownership-guardrail) and the user-linked [Business Roles](../../docs/markdown/03-business-architecture/actors-roles/roles.md), including Information Supplier/Custodian/Client and governance/assurance authority distinctions. These sources were read without changing upstream semantics.
- The explicit human-approved architectural decisions in the Step 3 task, as authority for the new definitions and obligations. No implementation structure, prior AI interpretation or report recommendation established them.

Materially relevant axioms and consistency:

| Authority | How the documented approach preserves it |
| :--- | :--- |
| **AX-01 / AX-04** | Information semantics and governed boundaries precede infrastructure convenience; engines do not invent Unit semantics. No new machinery or component is introduced. |
| **AX-02 / AX-03 / AX-12 / AX-13** | Native standards models remain available; no FHIR equivalence or replacement model is prescribed. Private management semantics do not become an external contract, and external egress terminates management of the emitted representation. |
| **AX-05** | Authoritative durable Version and valid active managed-state generation remain distinct, including the failed/degraded-convergence boundary. The complete managed-state model is deferred. |
| **AX-06 / AX-18** | Management acceptance does not originate information authority or confer domain authority. Custody, transport, presentation and consumption retain their separate meanings. |
| **AX-07 / AX-08 / AX-09** | Conceptual Management Context does not automatically become persistent resource content or universal durable evidence; operational security and transient-state boundaries remain applicable. |
| **AX-14 / AX-17** | Unit identity, represented-information identity and represented-entity identity are not conflated; Unit Structure, Content Structure and Format remain distinct. Unestablished structures, mappings and questions remain explicit. |

No material contradiction between these approved decisions and the canonical Domain04 boundaries was identified. Existing unresolved questions were retained rather than answered.

### Canonical Documentation Assessment

The Step 1/2 navigation identifies external dependencies in [Architecture Decisions](../../docs/architecture-decisions.md), [Deferred Document Register Item 04](../../docs/deferred-document-register.md), [Pragma](../../docs/concepts/pragma.md) and [Praxis](../../docs/concepts/praxis.md), as well as historical LaTeX information/data models. Their reported knowledge concerns accepted state/history obligations, Information Object/Data Object terminology and execution-envelope/workflow semantics. Their detailed canonical disposition remains a separate Domain04/downstream reconciliation question.

This task does not migrate that material, establish Information Concept / Information Object / Data Object equivalence, or map Information Unit to Pragma, Praxis or PetasosParcel. The external documents were not reloaded as authority for this new model: the human-approved task and retrieved canonical boundaries suffice for this bounded integration. Existing accepted obligations are not rejected by their external location. No required Unit definition depends solely on a report or non-canonical document; overall corpus consolidation and Domain04 completion are not declared.

## Canonical Documentation Location

The architecture is integrated in the existing [Information Architecture Metamodel & Core Concept Model](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md), **§§5–11**. That document already owns conceptual definitions, semantic characteristics and the conceptual/implementation boundary. It is therefore the coherent canonical location for management acceptance, the Unit boundary and their governing rules. **No new canonical document is created.** Existing §§1–4 remain byte-for-byte preserved.

Concise navigation/integration is added to the Domain04 index, authority/custody/provenance, lifecycle, assemblies/views, modelling guardrails and Information Families index. These links place the new model alongside existing meanings without duplicating its full specification or allocating family-specific Unit types. The Completion Plan receives only its current Domain04 status-paragraph update.

## Architectural Decisions Documented

| Approved decision | Canonical location and resulting meaning |
| :--- | :--- |
| **Harmonia-Managed Information** | [§5](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#5-harmonia-managed-information): information for which Harmonia has accepted explicit responsibility to maintain a governed representation and its management context over time. Management acceptance remains distinct from origination, domain authority, custody, persistence, transport, presentation and consumption. |
| **Information Unit** | [§6](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#6-information-unit): the governed boundary associating a Governed Representation with its Management Context. Bounded/governed, not necessarily atomic; structured, composite or related information is permitted according to its defined structure. |
| **Identity / Metadata / Content / Context** | [§§6.1–6.5](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#61-conceptual-partition): conceptual partition and simple tree. No four-object/class/table/serialised-element requirement. Identity identifies the Unit itself; Metadata supports Unit recognition/management; Content carries represented information and interpretation characteristics; Context carries management meaning over time. |
| **Identity distinctions** | Unit identity, represented-information identity and real-world entity identity remain potentially distinct. InstanceId notation defines no final attribute, datatype or identifier scheme. |
| **Unit Structure versus Content Structure** | [§§6.3–6.4](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#63-metadata): Metadata.StructureId concerns the Unit structure/type; Content.StructureId concerns represented content's semantic structure/model. The illustrative shared suffix does not establish equivalence. |
| **Content Structure versus Format** | [§6.4](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#64-content): semantic/structural definition differs from concrete encoding/representation. JSON/XML/binary/media-type examples are non-normative. Data/StructureId/Format notation supplies no final schema or supported-format catalogue. |
| **Context concerns** | [§6.5](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#65-context): provenance, authority, version, lifecycle, temporal context, relationships and active generation are conceptual concerns, not fields. Provenance differs from Authority; supplying source need not originate every assertion/relationship. Version differs from Active Generation, preserving AX-05 without completing managed-state reconciliation. |
| **Release-bound model** | [§7](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#7-release-bound-information-unit-model): each architectural release SHALL explicitly define and govern a bound set of structures and semantics. Bound does not imply identical populated attributes, mandatory conceptual elements, an immutable model or a permanently closed set. Optionality/cardinality/formats/extensions remain downstream details unless already established. |
| **Evolution rule** | [§7.1](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#71-explicit-evolution-across-releases): evolution between release-bound models is explicit architectural evolution, not uncontrolled schema drift. R1.x/R2.x/R3.x tree is illustrative; no actual release attribute sets are defined. |
| **Platform/framework obligations** | [§9](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#9-platform--framework-obligations): persistence, security, provenance and transport SHALL recognise/support both the Unit and represented information, respecting the boundary and applicable management semantics. No encryption, schema, ACL, protocol/header or cache-layout mechanism follows. |
| **Guardrails and downstream boundary** | [§10 G1–G8](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#10-information-unit-guardrails) explicitly preserves all eight approved guardrails; [§11](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#11-downstream-derivation-and-retained-questions) identifies deferred derivation and the unresolved search-management question. |

All supplied dotted notation remains illustrative. No complete Identity, Metadata, Content or Context structure is enumerated.

## Existing Domain04 Concepts Reconciled

| Existing concept/concern | Preserved relationship |
| :--- | :--- |
| **Information Concept** | Meaning remains distinct from the governed management boundary. Its definition, categories and semantic dimensions are unchanged; Unit is not a synonym or a new category in that classification. |
| **Information Relationship** | Source/target/type, roles, qualification, temporal and authority/provenance distinctions remain. Unit does not replace the relationship pattern or introduce a relationship taxonomy. |
| **Information Assembly** | Governed semantic composition retains its existing meaning and non-transfer rules. Composite Unit Content is permitted without Assembly equivalence, new cardinality or revised assembly semantics. |
| **Information View** | Purpose/consumer-oriented projection remains distinct. Presentation/contextual view alone does not establish a Unit or management acceptance. No result/member management decision follows. |
| **Managed state / lifecycle / temporal context** | These are conceptual Context concerns, not a completed state machine/history model. Authoritative durable Version is distinct from valid active Generation; existing concept-specific lifecycle meaning is preserved. |
| **Authority / provenance / custody** | Management acceptance is distinct from origination and domain authority. Source, relay and asserting authority are not collapsed. Provenance does not substitute for Authority; custody does not alone establish explicit management acceptance. |
| **Digital Twin** | The Strategy definition remains an active management construct associated with a real-world entity, coordinating information and operational activity. The Twin SHALL NOT be modelled as an Information Unit. No Twin information structure or Unit mapping is designed. |
| **Standards / FHIR** | FHIR may eventually represent Content or information within Content according to downstream architecture; no Unit-to-Resource equivalence or one-to-one mapping is established. Native standards use remains permitted without a prescribed implementation wrapper. |
| **Information Family responsibility** | The existing owned/managed Concept classification is clarified as Concept responsibility/source authority, not an exclusive taxonomy of managed Units. Externally originated representations may be managed without authority transfer. Family definitions and approval standing are unchanged. |

## Deliberately Deferred Detail

- Complete Information Unit types/structures and Identity, Metadata, Content and Context attributes; final naming, datatypes, identifiers and cardinalities.
- Actual release-specific structure sets, supported formats, optionality and extension constraints.
- Data Objects, schemas, Java classes, persistence envelopes, cache structures, transport messages, APIs, FHIR mappings and Application Component allocations.
- Full managed-state/history/snapshot reconciliation, lifecycle states, temporal models and relationship taxonomies.
- Search-result management/atomicity, Episode responsibility, generic work/result meaning and Observation/Finding derivation.
- Assurance/evidence, capacity and Digital Twin information structures; residual candidate-family approvals and external terminology/mapping reconciliation.

Detailed Unit structures are expected to become clearer through Data Object dissemination, Application Architecture decomposition, Integration Architecture, Technology Architecture and implementation design. Those derivations remain constrained by the approved semantic boundary and release governance. Implementation convenience does not establish Unit semantics.

## Conflicts or Ambiguities Found

**No new blocking contradiction was found between the approved Unit architecture and canonical Domain04.** The model adds a management boundary alongside meaning/composition/projection without silently replacing any of them. Existing authority/category/stage, generic work/result, history/evidence, retrieval and candidate-family ambiguities remain unresolved.

The Information Families index's owned/managed Concept wording could otherwise be mistaken for the new Unit management definition. A bounded explanatory paragraph preserves that table's existing responsibility/source-authority meaning while making clear that external origination does not exclude management acceptance. No ownership reallocation or family-to-Unit mapping is made.

The incoming Domain04 corpus and Step 1/2 reports contained no use of Information Container as this proposed concept requiring replacement. The new canonical terminology note explicitly retires that proposed name. Legitimate containment/container wording is preserved. PetasosParcel is mentioned only to state that no architectural mapping is established; it is not a canonical Domain04 term.

Release-model details and search-management choices remain absent decisions, not contradictions resolved through inference. No new architectural axiom is introduced or modified.

## Validation

### Domain04 local links and whitespace

`python3 /tmp/domain04-step3-links.py`: **PASS, exit 0** — all **25 Domain04 Markdown files**, **582 local link occurrences**, **88 closed code fences**, zero missing-file/anchor errors and zero structural errors. Targets outside Domain04 are checked as well. The validator uses markdown-it-py CommonMark plus tables, inline/image/reference links, HTML links/explicit anchors and GFM-style heading identifiers. This is local syntax/target verification, not a full renderer build or proof of semantic conformance.

`git diff --check`: **PASS, exit 0**, no whitespace diagnostics. The new untracked report's direct whitespace/final-newline check also passes. `python3 /tmp/domain04-step3-report-plan-links.py`: **PASS, exit 0** — report and Completion Plan, **54 local links**, **five closed fences**, zero link/structural errors. All eleven required report sections are present. Final documentation, whitespace and scope checks are refreshed after report completion; the passing architecture suite is not rerun without cause.

### Bounded architecture suite

Executed once with native `exec_command`, 10-second launch yield, a 600-second command timeout and 15-second termination grace. A two-minute no-progress investigation threshold applied. Completion waiting used `write_stdin` with a 30-second yield and returned when the process finished; no stall, timeout, termination or unchanged rerun occurred.

```bash
timeout --signal=TERM --kill-after=15s 600s mvn test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS / BUILD SUCCESS, exit 0:** **90 tests across 11 architecture suites; 0 failures, 0 errors, 0 skipped.** This matches the expected baseline. Maven duration: **16.939 seconds**; completion: `2026-10-09T21:19:10+11:00`. Surefire XML totals were checked against the current Maven log and each matching suite's presence in that log. All eight repository-named required suites ran, plus GovernedWriteComposition, GovernedWriteContract and MnemosyneAuthoritativePersistence architecture suites.

Evidence: `/tmp/domain04-step3-architecture-tests.log`, `/tmp/domain04-step3-test-summary.json` and `paradeigma/paradeigma-test/target/surefire-reports/TEST-*ArchitectureTest.xml`. This is the selected architecture reactor, not full-repository tests, runtime/deployment verification or documentary semantic proof.

**Warnings, separately from failures:** four Maven warnings concerned unwritable `com/sun/mail/jakarta.mail/resolver-status.properties` and Central/OSS snapshot metadata `.part.lock` files under the read-only `.m2/repository`. Cached dependencies sufficed. ArchUnit detected **Java 21.0.12.1**; no Java 25 / ArchUnit compatibility warning was emitted in this run. No environment, dependency, production or test repair was performed.

### Changed-file inventory

This task changes **nine files**, measured against the incoming worktree rather than HEAD:

| Task file | Change |
| :--- | :--- |
| [Information Architecture Metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) | Canonical §§5–11 integration; existing §§1–4 preserved. |
| [Domain04 index](../../docs/markdown/04-information-architecture/README.md) | Conceptual orientation and direct model/obligation/deferred-detail navigation. |
| [Authority / Custody / Provenance](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md) | Unit management-boundary integration and retained governance distinctions. |
| [Information Lifecycle](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md) | Conceptual Context/state-boundary navigation; no lifecycle/history completion. |
| [Assemblies and Views](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md) | Unit distinction and unresolved search-management navigation. |
| [Modelling Guardrails](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md) | Link to additional Unit G1–G8; sixteen core guardrail bodies/identifiers preserved. |
| [Information Families index](../../docs/markdown/04-information-architecture/information-families/README.md) | Distinguish Concept classification from management acceptance of represented information. |
| [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md) | Minimum current-position paragraph: Step 3 complete, sufficiency NO, further adjudication separately bounded. |
| [This Step 3 report](2026-10-09-domain04-information-unit-step3.md) | New execution/completion evidence. |

### Scope / immutability confirmation

The incoming worktree already contained fourteen Domain04 modifications, the Completion Plan update and the untracked Step 1/2 reports. `/tmp/domain04-step3-baseline.json` captured **2,291 tracked/input files** before this task's edits; copies of the incoming Domain04/plan documents are under `/tmp/domain04-step3-before`. **PASS — SHA-256 preservation comparison:** eight existing files changed within the authorised scope; **2,283 other baseline files remain byte-identical**, with none missing and no unexpected additions/changes. The sole new task file is this report. Historical review records, detailed family definitions, the incoming Step 1/2 reports and unrelated earlier changes are preserved. Evidence: `/tmp/domain04-step3-preservation.json` and the task-only patch `/tmp/domain04-step3-task.diff`. Metamodel §§1–4 and the Plan outside its status paragraph are independently confirmed identical to the incoming versions. Two incoming whitespace-only lines in the Families index remain unchanged; no introduced whitespace error exists.

**Implementation is unchanged; tests are unchanged; Domain01–03 semantics are unchanged; canonical axioms are unchanged; Domain05+ is unchanged.** Maven writes generated output only. No candidate clinical family is approved, upstream architecture changed, new component derived or later unresolved question commenced. The Plan's sequence/gates and Domain03 completion boundary are preserved. There is no deployment prerequisite for this documentation patch; remaining semantic decisions still constrain Domain04/Domain05 completion.

## Remaining Domain04 Questions

The Step 1/2 unresolved architecture is carried forward with this new semantic boundary; the report does not close whole findings merely because one distinction is now explicit.

| Remaining concern | Retained question / boundary |
| :--- | :--- |
| **Generic work/result meaning — IA13** | What do ActionableTask, FulfillmentTask, TaskOutcome and ReportedTask denote as information, and how do they relate to Work Order / To Do / synthetic Task without assuming authority over externally governed work? |
| **Managed state, progression and history — IA14/15/20/21/27** | What sufficient information semantics distinguish lifecycle, active/durable state, convergence/trust, uncertain effect, activity/Praxis progression, historical truth, snapshots and designated durable evidence? Version versus Active Generation is settled here; the full model is not. |
| **Search/retrieval — IA25/26** | For information returned through search, under what circumstances does Harmonia accept management responsibility and establish that information within a supported Information Unit? Each resource, a result set, neither and both remain undecided. Minimum retrieval/result qualification, completeness and management semantics also remain. |
| **Authority and vocabulary — IA07/10–12/15** | What evidence/meaning qualifies source-established correlation/correction; how do attribution, credibility, qualification, category/stage and event/lifecycle terms relate? Existing identity-origination exclusions are settled. |
| **Episode and G2 residuals — IA31** | Episode responsibility/authority and association rules; residual Appointment/Encounter, qualified Referral associations, composition and Order-change effect questions. G2-D01–D04 stay approved; G2 remains open. |
| **Assurance/evidence — IA22/23** | Minimum contextual evidence/assessment/adjudication/conclusion meaning, relation to AssuredHealthcareService and retained approval/lifecycle/retention questions. No universal Evidence entity or assurance-to-Unit structure follows. |
| **Contextual capacity and material information — IA28–30/33** | Minimum context-specific capacity and operational/clinical information semantics, applicability, source/finality/acceptance and attestation boundaries; Observation/Finding remains un-derived. |
| **Digital Twin — IA16** | Minimum relationships between entity information and active coordination, without designing Twin information structures or equating Twin and Unit. |
| **External terminology/dependencies — IA36** | Canonical disposition of Information Object/Data Object and Pragma/Praxis semantics. No PetasosParcel mapping is established. |
| **Current standing/completion — IA35/37** | Truthful coverage and eventual sufficiency review after material reconciliation; no exhaustive per-Feature Concept mandate or historical reinterpretation. |

## Semantic Sufficiency

> Is Domain04 now semantically sufficient to support Domain05 completion without Domain05 inventing missing Information Architecture?

**NO.** Management acceptance, the governed Unit boundary, conceptual partition and release/framework obligations now have canonical definitions. That is a material improvement, but generic work/result meaning, adequate managed-state/history, search-result management, contextual assurance/capacity and residual G2 meaning remain material unadjudicated architecture. Completing Domain05 would still require inventing some of those meanings.

Intentionally deferred attribute/schema/class details are not themselves a reason for NO: those belong to downstream derivation. The remaining semantic questions are the reason. This bounded integration does not justify changing the assessment to YES WITH BOUNDED RECONCILIATION or declaring Domain04 complete.

## Recommended Next Human Adjudication

**Separately adjudicate the smallest generic work/result information question (IA13): what the four existing Task/work/result Concepts denote, and their relation to the established Work Order / To Do / synthetic Task distinction.** Use only the authority/stage vocabulary necessary to answer that question, preserving externally governed clinical-work authority and explicitly assigned Harmonia activity execution. Do not combine it with the complete managed-state model, a new taxonomy, search management, assurance or implementation mappings.

The earlier recommendation remains a recommendation, not an authorised continuation. The more precise search-management question is recorded for its own future bounded adjudication. **Step 3 stops after canonical integration, validation, this report and the minimum Completion Plan update. No next question has begun.**
