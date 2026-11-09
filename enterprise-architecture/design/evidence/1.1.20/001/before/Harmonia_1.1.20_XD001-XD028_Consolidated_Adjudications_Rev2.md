# Harmonia 1.1.20 — Consolidated Cross-Path Feature Adjudications (XD-001–XD-028)

**Date:** 9 November 2026  
**Status:** Consolidation of accepted Solution Architect adjudications and preserved qualifications; **not yet applied as a whole to the working catalogue or governed Continuum**.  
**Scope:** The 28 NR-001 cross-path (`XD`) comparisons in Capability Model Consolidation, Pass 3, **and two accepted catalogue-wide authoring rules** applying to every CT3/Feature in the 1.1.20 candidate.  
**Working catalogue baseline:** `Harmonia_1.1.20_Candidate_017_Pass3_011_XD009_Working_Overlay.md` — **586 CT3s; 2,941 Features**.  
**Decision register baseline:** `Harmonia_1.1.20_Pass3_011_NR001_Adjudication_Workbook.xlsx`.  
**Other evidence:** Pass3/005 NR-001 screening report, dedicated XD-001/002/005/007/008/009 decisions, and the subsequent Solution Architect adjudications in the 1.1.20 Capability Model Consolidation discussions.

## 1. Purpose and authority

This is the **complete current adjudicated XD decision set**, not a new duplication scan, design exploration, or instruction to publish. The later explicit adjudications in dialogue supersede earlier *screening proposals* and later accepted clarifications supersede contradictory earlier *proposed wording*. Existing applied offline changes remain authoritative for the working candidate unless explicitly amended here. A changed semantic description is not permission to overwrite unrelated obligations in a parent CT3.

The governed Markdown Architecture Continuum in `enterprise-architecture/continuum/` remains project architectural authority. This document is an accepted-intent consolidation intended to support subsequent controlled reconciliation of the offline Candidate 017. The current repository state, release controls and actual candidate bytes must be checked at execution; no claim of repository amendment, publication, commit, push or validation of an updated candidate is made here.

**NR-001 interpretation:** Context, qualified path and architectural responsibility establish meaning. Similar wording across CT3s is not necessarily duplication; equally, different paths do not legitimise identical obligations. Do not renumber surviving Features, invent replacement Features to reach an arbitrary count of five, or reopen accepted SR-001–SR-019 decisions merely because an XD item overlaps their subject.

## 2. Accepted catalogue-wide canonical authoring rules (AR-001–AR-002)

The Solution Architect subsequently accepted these **two rules for the entire 1.1.20 Capability Catalogue**, not only the XD-001–XD-028 elements. They govern how the existing candidate is expressed and prepared for eventual Architecture Continuum incorporation. Their adoption **does not reopen** accepted architectural outcomes, remove evidence, establish implementation conformance or authorise publication.

### AR-001 — Separate Feature semantics from measurement

**Accepted rule:** Each canonical Feature name and description states an architectural capability, responsibility or observable outcome in intelligible domain language. The fact that a Feature must be measurable **does not** require defining the Feature as a KPI calculation.

- A Feature answers **what capability exists, what responsibility applies or what outcome must be supported**. Descriptions should be positive, meaningful, subject-specific and assessable, rather than formulaic assessment scaffolding.
- An associated KPI or acceptance/assurance criterion answers **how effectiveness or conformance is assessed**. Expressions such as *“proportion of ...”*, *“percentage of ...”*, elapsed-time measures, sampled-case rates and test counts belong here **when they genuinely measure the Feature**. Their numerators, denominators, applicability rules and assessment conditions may be preserved or refined as measurement metadata, but are not to be silently discarded.
- A Feature may have **multiple** suitable measures. Do not manufacture numerical thresholds, targets, sampling programmes or mandatory dashboard mechanisms merely to satisfy measurability.
- Measurement is **not prohibited language** where the capability itself is monitoring, measurement, statistical assessment or reporting. Apply semantic judgement rather than blind string replacement.
- An original Feature acceptance criterion that already happens to use measurement language must first be interpreted as evidence of its intended capability. Rewrite the **canonical description** to express that meaning; retain the accepted evaluative information as **separate measurement or assurance criteria**, rather than altering the obligation.
- Apply these conventions to all Business, Business Enabling and Enterprise Features, and to CT3 descriptions where measurement boilerplate has been embedded. **Do not replace the historical input artefacts or audit trail.**

**Illustrative canonical separation (XD-018-related; illustrates wording, not an extra adjudication):**

| Concern | Representation |
|---|---|
| Feature | `B04.03.04.F01` — accountable critical-result recipient (retain identifier; apply accepted XD-018 identity/accountability wording) |
| Canonical definition | Critical diagnostic findings are directed to an appropriately accountable recipient under the applicable responsibility arrangements, with sufficient recipient identity assurance. |
| Possible associated measure | Proportion of applicable critical findings directed to the appropriately accountable, sufficiently identity-assured recipient. |
| Evidence | The accepted XD-002/XD-018 decisions, source criteria and subsequent implementation/assurance evidence, with their actual provenance and status. |

**Additional examples:** *“Service capacity is assessed against demand and shortfalls are identifiable”* is a Feature outcome; *“percentage of demand accommodated”* is one possible measure (XD-010). *“Accepted work is recoverable after supported operational failure”* is a Feature obligation; *“percentage of sampled recovery exercises satisfying the recovery condition”* is one possible test/measure (XD-013). These examples do **not** set new thresholds or prescribe tests.

### AR-002 — Separate canonical descriptions from provenance and verification status

**Accepted rule:** Canonical CT/Feature names and descriptions must **not** carry discovery, authoring or verification-status labels as if they were architectural semantics.

- Strip such editorial/status tokens from the **canonical projection** of names/descriptions, including `PROPOSED`, `PROPOSED — unverified`, `unverified`, `[NEW C012]`, `[NEW C013]`, `[NEW C014]`, and comparable generated candidate markers, when they are acting as stage/status metadata.
- Retain provenance, AI/discovery origin, evidence keys, original wording, review standing, adjudication decisions and verification status in **separate governed evidence and appropriate metadata**, including their distinctions and traceability. Do not silently erase uncertainty from the evidence record.
- **Architectural acceptance** means an architectural definition or decision was accepted for the relevant increment; it does **not** mean the capability is implemented, tested, operational or verified. **Implementation assurance** must be reported separately using actual supporting evidence.
- Do not indiscriminately delete substantive words such as “proposed” where they are genuinely part of domain subject matter (rather than a candidate-status annotation). The stripping operation is semantic and scoped to presentation of canonical definitions.
- Newness markers may remain in review history and source evidence where needed for traceability; they must not be visible as part of the published canonical name.

**Canonical artefact separation:**

| Layer | Includes | Excludes |
|---|---|---|
| Architecture Continuum — canonical CT3/Feature | Stable identifier, friendly name, qualified context and architectural definition | KPI formula as a surrogate for the definition; discovery-stage labels; unsubstantiated verification claim |
| Associated KPI/assurance definition | Relevant measurable indicator, numerator/denominator or other measure, applicability and evidence expectations where actually established | Unauthorised numerical target or assertion of successful test execution |
| Governance/traceability record | Original wording, source, discovery/proposal history, XD/SR/FR decisions, identity transfers/retirements, acceptance and verification evidence with their actual status | Silent conversion of a proposal into an implementation claim |

### Application boundaries

These are **catalogue-wide authoring/representation rules**, **not** additional XD dispositions and not an instruction to automatically rewrite 2,941 Features without semantic review. Apply them during the already identified Feature wording-quality work and whenever revising the XD-affected Features. Preserve the original evidentiary record, stable identities, accepted responsibilities, valid exceptions and separately reviewable criteria. Record ambiguous transformations rather than inventing intended behaviour. The earlier **1,322 wording flags are a screening set, not 1,322 proven defects**; neither they nor the original `PROPOSED` labels are to be blindly translated into claims of architectural completeness.

## 3. Complete decision matrix

| XD | Compared elements | Accepted disposition | Incorporation condition |
|---|---|---|---|
| 001 | `E01.01.01.F02` / `E01.01.05.F02` | **REMOVE DUPLICATE** | Already applied offline |
| 002 | `B04.03.03` / `B04.03.04` | **MERGE CT3** | Already applied offline; incorporate XD-018 coverage refinements |
| 003 | `E05.01.02.F04` / `E05.01.04.F04` | **KEEP BOTH — CLARIFY** | Internal vs external trust wording pending |
| 004 | `E02.03.01.F07` / `E02.03.04.F02` | **KEEP BOTH — CLARIFY** | Complete node loss vs intermittent connectivity wording pending |
| 005 | `E03.05.03.F02` / `E03.05.04.F02` | **REMOVE DUPLICATE** | Already applied offline |
| 006 | `E01.05.02.F02` / `E01.05.04.F04` | **KEEP BOTH — CLARIFY** | Internal vs external media custody wording pending |
| 007 | `E07.03.01.F04` / `E07.03.04.F03` | **REMOVE DUPLICATE; REFINE CT3** | Already applied offline; separate `E07.03.05` review remains outside XD-007 |
| 008 | `E06.02.03.F04` / `E06.02.05.F01` | **KEEP BOTH — REFINE** | Already applied offline |
| 009 | `E06.02.03.F05` / `E06.02.05.F04` | **KEEP BOTH — REFINE** | Already applied offline |
| 010 | `B07.03.01.F04` / `B07.03.02.F02` | **KEEP BOTH — REFINE** | Service offering vs capacity; align parent contexts |
| 011 | `E04.01.02.F05` / `E04.01.03.F02` | **KEEP BOTH — REFINE** | General versioned reference data vs ontology specialisation |
| 012 | `E06.03.02.F04` / `E06.03.03.F02` | **KEEP BOTH — REFINE** | Actual Parcel routing assurance vs routing decision evidence |
| 013 | `E08.06.02.F01` / `E08.06.04.F03` | **KEEP BOTH — REFINE** | Normal operational vs catastrophic recovery; respect XD-022 |
| 014 | `E08.08.03.F05` / `E08.08.04.F01` | **KEEP BOTH — CHANGE DISCLOSURE SEMANTICS** | Policy may require partial or all-or-nothing release |
| 015 | `E01.02.01.F03` / `E01.02.03.F03` | **KEEP BOTH — REFINE** | Version content retrieval vs specification/identity integrity |
| 016 | `E03.04.01.F04` / `E03.04.05.F04` | **KEEP BOTH — REFINE** | Protocol-level ACK vs multilevel ACK correlation |
| 017 | `E06.08.01.F03` / `E06.08.05.F03` | **KEEP BOTH — REFINE** | Processing acceptance vs `Ergon.start()` commencement |
| 018 | Retired `B04.03.03.F02` / `B04.03.04.F01` | **SUPERSEDED BY XD-002; REFINE RETAINED COVERAGE** | Do not restore retired CT3; revise F01/F04/F05 and timing requirements |
| 019 | `E05.05.01.F01` / `E05.05.02.F01` | **KEEP BOTH — UNCHANGED** | Protected exchange vs protected custody |
| 020 | `E01.01.02.F03` / `E02.01.02.F03` | **KEEP BOTH — REFINE** | Overall Mneme–Mnemosyne preconditions vs Mnemosyne boundary enforcement |
| 021 | `E04.04.01.F01` / `E04.04.02.F01` | **KEEP BOTH — REFINE** | Comprehensive model verification vs terminology conformance |
| 022 | `B12.04.04.F01` / `E08.06.02.F01` | **KEEP BOTH — PRESERVE ENTERPRISE CONTINUITY** | Do not redefine B12 Feature as queue mechanism |
| 023 | `E04.02.02.F02` / `E04.02.04.F01` | **KEEP BOTH — REFINE** | Correct published value-set version vs stable version meaning |
| 024 | `E01.04.01.F03` / `E01.04.04.F02` | **KEEP BOTH — RENAME E01.04.04** | Governed Information Lifecycle Actions; preserve retire-specific CT3 |
| 025 | `B05.04.01.F01` / `B05.04.04.F01` | **KEEP BOTH — REFINE** | Longitudinal care intent/context; no inferred LHR encounter/episode links |
| 026 | `E05.05.01.F02` / `E05.05.02.F02` | **KEEP BOTH — UNCHANGED** | Encryption for information exchange vs custody |
| 027 | `E06.02.04.F02` / `E06.02.05.F04` | **KEEP DISTINCT; RECONCILE SELECTIVE STIMULUS CONTEXTUALISATION** | No overwrite of Twin recovery or security Feature |
| 028 | `E02.03.01.F04` / `E02.03.02.F05` | **KEEP BOTH — REFINE E02.03.02** | Distributed information availability vs reference coherence; keep F05 permission |

## 4. Adjudicated decisions and required architectural meaning

### XD-001 — Persistence before acknowledgement

**REMOVE DUPLICATE — already incorporated in offline candidate.** Retain `E01.01.01.F02 — PersistenceBeforeAcknowledgement` as the single owned obligation that durable persistence is evidenced before durable-acceptance acknowledgement. Retire `E01.01.05.F02 — DurableBeforeAcknowledgement`, retaining `E01.01.05.F01`, `.F03`, `.F04`, `.F05` unchanged. Preserve both CT3s; no new Feature, replacement or renumbering. No need to reapply the removal to Pass3/011.

### XD-002 — Critical findings: CT3 merger

**MERGE CT3 — already incorporated offline.** Retire `B04.03.03 — Critical Finding Communication` into surviving `B04.03.04 — Critical Result Receipt & Follow-Up Confirmation`, broadening the latter to classification, rapid notification, receipt, escalation, follow-up and accountability. Original `B04.03.04.F01`–`.F05` remain; source `.F03 — Critical Finding Classification` became `B04.03.04.F06`; source `.F04 — Rapid Appropriate Notification` became `.F07`, retaining their respective criteria. Source `.F01`, `.F02`, `.F05`, `.F06` were retired as separate identifiers; source `.F06` escalation was represented by retained `B04.03.04.F04`. Do **not** restore source CT3 or source identifiers. The original residual coverage qualifications are resolved architecturally through **XD-018** but require incorporation and verification of refined survivor criteria.

### XD-003 — Internal vs external system trust

**KEEP BOTH — CLARIFY.** `E05.01.02.F04` concerns expired/revoked machine or component trust within Harmonia's internal subsystem boundary. `E05.01.04.F04` concerns expired/revoked trust at the Harmonia-to-external-system machine-to-machine boundary. Both reject invalid trust, but at independent boundaries. Retain both identifiers and CT3s; clarify the respective parent/Feature wording without imposing a new trust implementation. Accepted earlier, not incorporated into Pass3/011 wording.

### XD-004 — Node loss vs intermittent connectivity

**KEEP BOTH — CLARIFY.** `E02.03.01.F07` concerns restoration of distributed access following complete node loss or replacement, including resynchronisation and truthful availability/degradation. `E02.03.04.F02` concerns maintaining coherence and consistency while active nodes suffer intermittent or partitioned connectivity. These are distinct failure models. Preserve both; refine criteria without requiring a specific replication mechanism. Accepted earlier, wording outstanding in Pass3/011.

### XD-005 — Repeatable interface conformance tests

**REMOVE DUPLICATE — already incorporated offline.** Keep `E03.05.03.F02 — Repeatable Conformance Test`; retire `E03.05.04.F02 — ConformanceSuiteRepeatable`. Accepted retained criterion: for each applicable Harmonia release and supported external exchange contract/version, the conformance test suite can be rerun with reproducible and reviewable positive/negative results. `E03.05.04` survives with `.F01`, `.F03`, `.F04`, `.F05`; no replacement/renumbering. No need to reapply to Pass3/011.

### XD-006 — Internal and external media integrity

**KEEP BOTH — CLARIFY.** `E01.05.02.F02` concerns integrity and governed referencing of media managed within Harmonia's custodial boundary; `E01.05.04.F04` concerns integrity of externally managed media objects that Harmonia references without assuming custody of their original objects. Ownership, control and evidence obligations differ. Retain both and align descriptive wording. This does not imply that large binary content is to be persisted in PostgreSQL.

### XD-007 — Historical version access and longitudinal presentation

**REMOVE DUPLICATE; REFINE CT3 — already incorporated offline.** Keep general `E07.03.01.F04 — Historical Version Accessibility`. Retire `E07.03.04.F03 — HistoricalVersionAccessible`. Retain/rename `E07.03.04 — Cross-Domain Longitudinal Presentation` (formerly Cross-Domain Longitudinal Correlation View); `E07.03.04.F04 — QualifiedAssociationPresentation` presents *source-established*, governed associations and recorded uncertainty without inventing identity matches or clinical inferences. Keep `.F01`, `.F02`, `.F05` and `E07.03.01.F04` unchanged. **Separate review:** `E07.03.05 — Cross-Source Clinical Timeline Synthesis` is not decided by XD-007; do not silently alter it.

### XD-008 — Twin execution isolation and input allocation

**KEEP BOTH — REFINE; already incorporated offline.** `E06.02.03.F04 — Twin Execution Context Isolation` means each Twin instance maintains logically isolated input, output, work state and progression, including asynchronous processing. `E06.02.05.F01 — Twin Input Allocation` means the controlling choreography/orchestration framework directs inputs/work to the correct instance without cross-instance contamination. Within-instance context isolation differs from controller-to-instance allocation. No prescribed thread, process, queue or physical isolation model.

### XD-009 — Twin execution recovery and context survival

**KEEP BOTH — REFINE; already incorporated offline.** `E06.02.03.F05 — Twin Execution Recovery`: execution framework recovers and continues governed work after interruption without unintended work loss/duplication. `E06.02.05.F04 — Twin Operational Context Recoverability`: Twin work state and associated information remain recoverable independently of the failed execution engine. Recoverable context is not proof that execution has resumed. No mandated storage/checkpoint design.

### XD-010 — Service offering vs service capacity

**KEEP BOTH — REFINE WORDING.** `B07.03.01.F04 — Service Offering Availability`: determine where and when a particular service is intended to be offered. **Candidate wording:** “The availability of a service offering is established for its applicable locations and time periods, identifying where and when the service is intended to be provided.” `B07.03.02.F02 — Service Capacity Adequacy`: assess actual/anticipated capacity against demand, exposing shortfalls. **Candidate wording:** “The capacity available to deliver a service is assessed against its actual or anticipated demand, identifying whether sufficient capacity exists and exposing any shortfall.” Service offering does not guarantee capacity, and spare capacity does not establish that a service is offered. Keep IDs/CT3s; reconcile parent descriptions with this distinct subject because Pass3/011 names both in terms of unmet demand. No new Features.

### XD-011 — General reference data and specialised ontology management

**KEEP BOTH — REFINE WORDING.** `E04.01.02.F05 — Versioned Reference Data Management` is the broad capability: manage lifecycle, approved publication, retrieval and historical references for versioned value sets, enumerations and other application/reference datasets. `E04.01.03.F02 — Ontology Management` is the explicit specialisation: concepts, relationships, semantics, version history and interpretation in their applicable context. The broader capability includes governed application enumerations, but does not require every consumer to fetch enumerations dynamically at runtime. Retain both IDs/CT3s. Align current historical-model criteria and parents without deleting historical interpretability. No additional Features.

**Candidate criteria:**

- F05: “Govern the lifecycle, publication, retrieval and historical referencing of versioned reference datasets used throughout Harmonia, including terminology value sets, application reference data and enumerated values, ensuring that consumers can identify and access the applicable dataset version.”
- F02: “Govern the management, versioning and availability of ontologies and their defined concepts, relationships and semantics, including historical versions required to interpret information within its applicable semantic context.”

### XD-012 — Observed Parcel movement vs routing-decision rationale

**KEEP BOTH — REFINE WORDING.** `E06.03.02.F04 — Parcel Routing Assurance` observes, tracks and reports **actual** Parcel movement: sources, destinations, transitions and results, using governed routing evidence. `E06.03.03.F02 — Routing Decision Evidence` records the **selected route**, criteria/rules and rationale. Decision and outcome must be correlatable but are not identical; a Parcel can fail to reach its intended destination. Assurance observes/reports and must not invent decision rationale. Preserve IDs/CT3s; align consumer/assurance versus routing ownership; consistent with SR-004.

**Candidate criteria:**

- F04: “The assurance framework supports observation, tracking and reporting of Parcel movements through Harmonia, including the actual source, destination, routing transitions and associated outcomes, using governed routing evidence.”
- F02: “The routing mechanism records sufficient information about each governed routing decision to establish the selected destination, applicable routing rules or criteria, and the rationale for that selection, including relevant decision context and qualifications.”

### XD-013 — Operational durability vs catastrophic recoverability

**KEEP BOTH — REFINE WORDING.** `E08.06.02.F01` concerns durability of accepted work and required processing state through supported ordinary component/node failures, including pre-processing assurance as refined in XD-022. `E08.06.04.F03 — Catastrophic Work Recoverability` concerns restoring accepted work and necessary state after catastrophic infrastructure loss so governed processing can resume, with no accepted-work loss. A high-availability node-failure test does **not** prove disaster recovery. Maintain distinct, independently testable failure assumptions and avoid mandating a particular backup/replication technology.

**XD-013 original candidate wording** for F01 (“All accepted governed work is durably persisted…”) is **qualified by the later XD-022** distinction: F01 at the processing boundary assures existing preservation and recoverability; it does not automatically mandate a second persistence of the same work. F03 candidate wording: “Following catastrophic infrastructure failure, previously accepted governed work and its required processing state can be recovered from surviving or restored recovery mechanisms, made accessible to Harmonia, and returned to governed processing without loss of accepted work.”

### XD-014 — Provenance assembly vs policy-governed evidence disclosure

**KEEP BOTH — REVISE DISCLOSURE SEMANTICS (explicitly accepted).** **All-or-nothing is a policy-governed disclosure constraint, not universal Harmonia behaviour.** An internally complete evidence set may be only partially disclosable to a consumer. Policy can allow selective release or require completeness and hence all-or-nothing treatment for a given request. The presence of withheld content, including qualification or error metadata, must not itself leak protected information.

- `E08.08.03 — Dereferenced Provenance Evidence Assembly`: assembles/dereferences evidence, preserving attribution, relationships, completeness, chronology and qualifications. Its `E08.08.03.F05` must govern **assembly completeness/qualification**, not demand universal whole-set release authorisation.
- Rename/clarify `E08.08.04 — Governed Evidence Disclosure` (from All-or-Nothing Governed Release): policy-based constituent authorisation and protected release. `E08.08.04.F01` governs **Selective Evidence Disclosure** (the permitted portion, with all-or-nothing where applicable); `.F02` governs **Disclosure Completeness & Qualification** (do not disclose prohibited omission details); `.F03` retains staged-payload protection; `.F04` retains release-time authorisation; `.F05` retains protected failure reporting.
- Retain **all CT3 and Feature identifiers**. Do not conflate assembled completeness with completeness of the disclosable subset. No prescribed eager, incremental or post-assembly authorisation algorithm. SR-006/SR-007 evidence responsibilities remain distinct.

This supersedes the original Pass3/011 `CompleteReleaseGate`, `All-or-Nothing Governed Release` and `NoPartialReleaseAfterDenial` wording wherever they imposed unconditional all-or-nothing policy.

### XD-015 — Versioned retrieval vs integrity of version specification

**KEEP BOTH — REFINE WORDING.** `E01.02.01.F03 — Version-Specific Information Retrieval` retrieves the precise specified version of information. `E01.02.03.F03 — Version Specification Integrity` preserves and verifies the version's identity, metadata, specification and relationships. Correct content retrieval does not prove specification integrity; a valid version specification does not prove content availability. Preserve both IDs/CT3s; keep the historical retrieval and immutable custody boundaries distinct. No additional Features.

### XD-016 — Protocol vs end-to-end acknowledgement correlation

**KEEP BOTH — REFINE WORDING.** `E03.04.01.F04 — Protocol Acknowledgement Correlation`: associate an individual protocol ACK with its transaction/interaction and context. `E03.04.05.F04 — End-to-End Acknowledgement Correlation`: identify *all* associated acknowledgements across applicable protocol, application and logical processing/commitment levels, potentially many ACKs to one governed transaction. A successful protocol ACK does not prove application or logical processing success; preserve distinct ACK semantics and correlate across levels. Retain identifiers/CT3s.

**Candidate criteria:**

- F04 (E03.04.01): “Each protocol-level acknowledgement can be associated with its corresponding transaction or interaction, preserving the identity and context required to establish that relationship.”
- F04 (E03.04.05): “All acknowledgements associated with a governed transaction can be identified and correlated across applicable protocol, application and logical processing layers, including multiple acknowledgements relating to the same transaction.”

### XD-017 — Processing acceptance vs assured execution commencement

**KEEP BOTH — REFINE WORDING.** A published `Pragma` makes work available; publication is **not** processing acceptance, dispatch receipt, execution or commitment. `E06.08.01.F03 — Task Processing Acceptance`: processing engine accepts the undertaking and establishes the associated `FulfillmentTask`. `E06.08.05.F03 — Assured Task Execution Commencement`: accepted FulfillmentTask is contextualised, loaded into its `Ergon` and advances beyond governed `Ergon.start()` checkpoint. Assured commencement is **not** assured completion. Retain both IDs/CT3s; distinguish these three lifecycle stages from XD-022 pre-processing preservation.

**Candidate criteria:**

- E06.08.01.F03: “A published Pragma becomes eligible for processing, but does not enter active fulfilment until a processing engine accepts the work and establishes the corresponding FulfillmentTask undertaking.”
- E06.08.05.F03: “Execution commencement is assured only after the accepted FulfillmentTask has been fully contextualised, loaded into the assigned Ergon and successfully progressed beyond the governed `Ergon.start()` checkpoint, with the required execution state established.”

### XD-018 — Critical-result recipient accountability after XD-002 merger

**CLOSE AS STRUCTURALLY SUPERSEDED BY XD-002 — REFINE RETAINED COVERAGE (explicitly accepted).** Retired `B04.03.03.F02 — CorrectCriticalResultRecipient` must **not** be reinstated; neither should `B04.03.03` be recreated. The distinction between *knowing an intended recipient*, *correct accountable recipient*, *assured identity*, *delivery*, *acknowledgement within the required interval* and *clinical follow-up* must survive in the **seven retained `B04.03.04` Features**.

- Refine `B04.03.04.F01` to establish an **appropriately accountable intended recipient** with sufficient recipient identity assurance, using authoritative clinical responsibility arrangements as appropriate (Harmonia need not itself decide clinical responsibility).
- Refine `B04.03.04.F04` to preserve required time-bounded acknowledgement and escalation where acknowledgement is missing or late; notification does not equal clinical response completion.
- Refine `B04.03.04.F05` to preserve attributable evidence of intended and actual recipient, delivery, acknowledgements and applicable follow-up.
- Preserve appropriate notification timing in existing Features, including `.F07`, and ensure evidence can distinguish delivery, acknowledgement and clinical action.

The *architectural coverage decision* is closed; the **mechanical evidence qualification** closes only after the retained Feature criteria have actually been updated and verified in the working catalogue. No additional Features/CT3s or renumbering.

### XD-019 — Protected exchange vs protected custody

**KEEP BOTH — UNCHANGED.** `E05.05.01.F01 — ProtectedExchangeConformance` applies information-protection conformance to information in transit during exchange. `E05.05.02.F01 — ProtectedCustodyConformance` applies conformance to information held in custody, including persistence/retrieval. Shared classifications/policies do not eliminate distinct trust/security boundaries. Retain existing identifiers and wording; SR-018/NR-001 support the separation.

### XD-020 — Cross-subsystem change preconditions vs Mnemosyne enforcement

**KEEP BOTH — REFINE WORDING (accepted).** `E01.01.02.F03 — Governed Change Preconditions`: the complete change operation coordinated across Mneme and Mnemosyne respects applicable preconditions, including governed rejection/reporting. `E02.01.02.F03 — Persistence Change Preconditions`: Mnemosyne independently verifies and enforces applicable preconditions before committing change, rejecting failures without corrupting governed state. Mneme-side assessment does not substitute for Mnemosyne boundary enforcement.

**Important containment decision:** Retain `E02.01.02` as a **broader governed-information-access CT3** rather than reclassifying the whole CT3 as Mnemosyne-specific. Clarify **F03's** independent persistence enforcement; do not silently migrate or rename the parent CT3. Retain IDs and all other Features; preserve its consumer-facing responsibilities.

**Candidate criteria:**

- E01.01.02.F03: “Information changes coordinated through Mneme and Mnemosyne satisfy the applicable governing preconditions across their interaction, including appropriate handling and reporting of rejected changes.”
- E02.01.02.F03: “Mnemosyne verifies and enforces applicable information-change preconditions before committing a change, rejecting operations that fail those conditions without compromising the governed information state.”

### XD-021 — Comprehensive model verification vs terminology conformance

**KEEP BOTH — REFINE WORDING AND MODEL VERIFICATION SCOPE (accepted).** `E04.04.01` is a **model verification framework**, **not merely structural validation**: it verifies a model's structural definitions, relationships, constraints/invariants, reference data, value-set/terminology dependencies, coherence, completeness and conformity with governing specifications. `E04.04.01.F01 — Model Verification Conformance` expresses this verification outcome. `E04.04.02 — Terminology Conformance Checking` and its `E04.04.02.F01 — Terminology Conformance` remain explicit for terminology-dependent instance/code/value validation. Model verification can assess the integrity of terminology bindings *as model dependencies* without displacing terminology-conformance checking for information. Model validation does not imply that all instances will pass validation. Verification can inform model publication eligibility without owning publication itself. Retain both CT3s/Feature IDs; align E04.04.01's CT3 definition with comprehensive scope. Do not erase instance-level information-model conformance elsewhere; reconcile other Features of that CT3 before narrowing any acceptance tests.

### XD-022 — Enterprise continuity, work capture and processing assurance

**KEEP BOTH — PRESERVE ENTERPRISE CONTINUITY (later explicit decision governs).** `B12.04.04` retains its **enterprise continuity / accepted service obligation** purpose. `B12.04.04.F01` remains the enterprise outcome that accepted work must not be lost and must remain recoverable; **do not rewrite it as operational queue insertion**. Initial acceptance/capture and placing content/context into a governed work queue remain an **operational work-intake responsibility** to be mapped to an existing appropriate capability; they are not imposed on `B12.04.04.F01`.

`E08.06.02.F01 — Pre-Processing Work Preservation Assurance` concerns checking/establishing that already accepted work and required processing state remain durably preserved/recoverable **as work is prepared for processing**, without necessarily persisting it again. This complements, not substitutes for, the normal operational resilience obligation of XD-013 and catastrophic recoverability in `E08.06.04.F03`. Enterprise outcome, initial work-intake mechanism and processing-boundary assurance are distinct. Retain B12/E08 CT3s and IDs; no automatic additional Feature. The earlier proposed renaming of `B12.04.04.F01` to “Accepted Work Capture & Queuing” is **superseded** by this later accepted decision.

### XD-023 — Access to published value set vs publication stability

**KEEP BOTH — REFINE WORDING (explicitly accepted).** `E04.02.02.F02 — Published Value Set Version` ensures consumers retrieve the approved membership and definition for the *specified* version with sufficient version identity. `E04.02.04.F01 — Published Value Set Stability` ensures a published version remains *semantically stable* throughout its governed release: repeated retrieval of that version cannot silently change membership/meaning through unreviewed terminology updates. New versions may be published under release/version governance; stability is not byte-identical transport serialisation. Retain both IDs/CT3s; no additional Features.

**Candidate criteria:**

- E04.02.02.F02: “Consumers can retrieve the approved membership and definition of a specified published value-set version, with sufficient version identification to distinguish it from other available versions.”
- E04.02.04.F01: “The definition and membership of a published value-set version remain stable throughout the applicable governed release, such that repeated retrieval of that version produces semantically consistent results without unauthorised modification.”

### XD-024 — Retention holds vs permission to undertake lifecycle actions

**KEEP BOTH — REFINE WORDING; RENAME CT3 (explicitly accepted).** Preserve `E01.04.01 — Governed Information Retention`: the preservation obligation and hold conditions. Its `E01.04.01.F03 — Retention Hold Protection` maintains preservation/protection for information subject to a hold. Rename **`E01.04.04 — Governed Information Lifecycle Actions`** (formerly Governed Information Disposition) to govern whether requested lifecycle operations are permissible under policy and holds. Its `E01.04.04.F02 — Retention-Hold Respect` prevents prohibited actions with governed outcomes. Preserve distinct **`E01.04.05 — Policy-Governed Retire & Retrieval`** for RETIRE-specific eligibility/state and post-retirement retrieval. RETIRE is not synonymous with physical destruction; holds do not categorically prohibit every change. Retain CT3/Feature identities and other Features; adjust affected surrounding terminology consistently. No added CT3/Features.

**Candidate criteria:**

- E01.04.01.F03: “Information subject to an applicable retention hold remains preserved and protected for the duration and under the conditions of that hold, preventing unauthorised loss, alteration or retirement that would violate the governing obligation.”
- E01.04.04.F02: “Requested governed information lifecycle actions are evaluated against applicable retention holds, preventing actions that would violate those holds and providing an appropriately governed outcome for rejected requests.”

### XD-025 — Longitudinal care context, encounter/episode links and LHR display

**KEEP BOTH — REFINE WORDING (explicitly accepted).** The final decision is **not** to overwrite the existing Business Feature with a generic encounter-detail repository. `B05.04.01.F01 — CoherentLongitudinalCare` and its CT3 remain about care-intent continuity and coherence over time/episodes. `B05.04.04.F01 — LongitudinalEncounterContinuity` and its CT3 preserve/expose meaningful **source-established episode/encounter associations** as part of longitudinal care context. An encounter/episode can contextualise referrals, investigations, results, follow-ups and subsequent work without claiming ownership of the related InformationUnits.

**Crucial LHR boundary:** The longitudinal health record is a **temporal presentation of available historic information**, whose entries need not be associated with any given encounter/episode. Chronological adjacency must not be used to **invent** an episode/encounter or clinical linkage. The LHR may offer an encounter-oriented presentation **only when pre-existing governed associations support it**. LHR display belongs to the separate presentation capabilities (consistent with XD-007); do not transfer relationship inference to the LHR. Retain both Business CT3s and Feature IDs; refine criteria to express their own intended duties rather than repurpose them as encounter management. No additional Features.

### XD-026 — Exchange encryption vs custody encryption

**KEEP BOTH — UNCHANGED.** `E05.05.01.F02 — Encrypted Exchange Channel` protects information during transport/exchange; `E05.05.02.F02 — Encrypted Custody Confirmation` protects information at rest/in custody. The same information may have both obligations, but exchange does not necessarily transfer custody, and custody is not ownership. Keep identifiers, parent CT3s and original separate obligations. This is a transport-versus-custody distinction consistent with XD-019/SR-018. No consolidation.

### XD-027 — Non-Twin work and selective Stimulus contextualisation

**KEEP ORIGINAL PAIR DISTINCT; RECONCILE A SEPARATE BEHAVIOUR-FRAMEWORK OBLIGATION (explicitly accepted).** Original screened pair: `E06.02.04.F02 — NonTwinWorkRecognised` permits non-Twin workflow without a fabricated Twin identity; `E06.02.05.F04 — Twin Operational Context Recoverability` preserves Twin state after engine loss (XD-009). They are independent. The earlier discussion's `E05.05.02.F02` identifier was **incorrect**: it is encrypted information custody (XD-026), and **must not** be repurposed.

**Additional accepted intent — Selective Stimulus Contextualisation:** A Digital Twin can legitimately execute Behaviour on *general* inputs (e.g., name/value pairs and booleans). The framework always establishes the required **execution context** for the relevant Twin/Behaviour, but contextualises **input information** into Twin-centric form **only where actually required**. Inputs already fit for processing pass through without artificial conversion or destructive rewriting. The Digital Twin Behaviour framework determines whether contextualisation is necessary, already present, or unnecessary. Non-Twin-specific *input* does **not** necessarily mean non-Twin *work*. Preserve the original information and governed execution semantics. Reconcile this requirement against existing Stimulus/Behaviour Features before proposing any new Feature; do not overwrite E06.02.05.F04, E05.05.02.F02 or E06.02.05.F01.

### XD-028 — Distributed data availability vs coherent distributed references

**KEEP BOTH — REFINE CT3/REFERENCE CRITERIA (explicitly accepted).** `E02.03.01.F04 — Distributed Read Availability` concerns governed information accessibility across distributed locations. `E02.03.02 — Distributed Reference Availability` governs coherent, identifiable, meaningful, faithful and resolvable **references** to that information across eligible distributed contexts. A reference can be **embedded** in another structure or an **independently managed element**; neither storage representation is mandated. A coherent reference does not prove current payload availability, and distributed read availability does not itself ensure coherent references. Refine the `E02.03.02` CT3 description and its reference coherence/resolution criteria (`F01`–`F04`, as applicable); **retain** `E02.03.02.F05 — Distributed Access Permission` as its separate authorisation obligation. All identifiers/CT3s retained; no added Features.

## 5. Cross-decision consistency and incorporation guidance

The following reconciliations are **part of the accepted meaning**, not new invitations to rediscover architecture:

1. **XD-013, XD-017, XD-022:** Acceptance and durable queueing, assurance of preservation before processing, execution undertaking, `Ergon.start()` commencement, ordinary failure recovery and catastrophic recovery are different boundaries. Avoid claiming completion from commencement, or disaster recovery from a successful high-availability test.
2. **XD-014:** Policy-selectable all-or-nothing/partial evidence disclosure supersedes unconditional all-or-nothing wording in the old candidate, but staging and failure protection remain obligatory. Preserve the difference between complete internal evidence and a legally/contractually disclosable subset.
3. **XD-018/XD-002:** The decision to retire the source CT3 is final for this increment. Refine survivor coverage and validate the former missing obligations; **do not** reintroduce discarded source identities.
4. **XD-020:** An F03 enforcement responsibility in Mnemosyne does **not** turn the entire `E02.01.02` governed access CT3 into a persistence-only CT3.
5. **XD-021:** Model verification includes structural *and* reference/terminology dependencies. Preserve separate terminology conformance and any existing information-instance validation; do not reduce comprehensive verification to structural linting.
6. **XD-022:** Preserve `B12.04.04` enterprise continuity. Do not implement the superseded proposal to rename B12's F01 as an operational work queue.
7. **XD-025/XD-007:** Longitudinal temporal presentation is not authority to infer source relationships; known episode/encounter associations may be displayed with attribution and appropriate qualifications.
8. **XD-027/XD-009/XD-026:** Preserve Twin engine/context recoverability, information security custody and selective input contextualisation as separate matters. In particular, do not use the mistyped E05 identifier for Twin work.
9. **XD-028:** Distributed reference coherence and distributed access permission both survive; an improvement to CT3 description must not remove F05 authorisation.
10. **XD-023/XD-011:** Versioned reference data, specialised ontology governance, published version access and publication stability are separate but compatible obligations.

## 6. State, identity and evidence requirements for the next controlled incorporation

- **Starting offline catalogue:** Pass3/011, **586 CT3s and 2,941 Features**. Already incorporated: XD-001/002/005/007 structural updates, XD-008/009 wording. Do not replay them as new deletions or merge operations.
- **Recorded but unincorporated wording:** XD-003/004/006 and, based on the latest available Pass3/011 copy, subsequent accepted XD-010–XD-028 refinements.
- **No further XD architectural adjudication is pending in this dialogue.** XD-018's **accepted semantic disposition** is closed, but verifying survivor coverage in the updated working candidate remains a concrete incorporation check, not a reason to reopen XD-002.
- **Preserve historical identity trace:** Source and target identifiers of removed/relocated Features, explicit retirement and transfers, renames without identity changes, and residual obligations from XD-002.
- **Apply AR-001 catalogue-wide:** Express canonical Feature definitions as capabilities/responsibilities/outcomes, not as KPI formulae (including generic “proportion of ...” wording). Preserve meaningful existing measures separately and allow legitimate measurement/monitoring Features to use measurement terminology. Do not invent measurement regimes, numerical targets or release mandates.
- **Apply AR-002 catalogue-wide:** Omit `PROPOSED`, `unverified`, `[NEW C###]` and similar editorial status markers from canonical descriptions and names, preserving historical standing and verification status in separate evidence. This removal must not be represented as test or implementation completion.
- **Reconcile parent CT3s and neighbouring Features** where new semantics alter titles or acceptance tests; do not silently displace old independently meaningful obligations. If an accepted requirement lacks an unambiguous existing home, report the precise collision or proposed placement rather than forcing an incorrect identifier or creating something automatically.
- **Keep separate rationalisation work:** FR-001–FR-009 same-CT3 review, the 1,322 broad wording-quality flags, unrelated SR follow-up, `E07.03.05` separate presentation semantic review, FC-042 (deferred), FC-059 (open), FC-101 (parked) and other unscreened comparison pairs have **not** been disposed of merely by finishing XD-001–XD-028.
- **Governance:** This specification does not itself authorise Continuum publication, commit/push, or any particular agent operation. Subsequent work must follow current repository governance and authorised scope. Governed evidence location, if required by current repository practice, is `enterprise-architecture/design/evidence/<version>/<iteration>/before/` and `/after/` rather than transient `.junie/`.

## 7. Incorporation acceptance checks

The XD consolidation is correctly incorporated only when:

1. The current offline candidate retains every applied accepted retirement, transfer and structural change from XD-001–XD-009 with unchanged surviving identifiers.
2. Each accepted *KEEP* distinction has semantically correct canonical capability/outcome definitions in its CT3 context, with applicable measurements/assurance criteria separately maintained; no neighbouring Feature is silently erased or repurposed.
3. XD-014 permits appropriately governed partial release *and* policy-required all-or-nothing release, with no protected-data leakage through omission/qualification/failure.
4. XD-018's accountable recipient, assured identity, notification/acknowledgement timing, evidence and follow-up obligations are demonstrably represented within the retained `B04.03.04` Feature set.
5. XD-020, XD-021, XD-022, XD-025, XD-027 and XD-028 reflect the *later accepted clarifications*, not superseded proposed renames, mistaken identifiers or the Pass3/005 preliminary duplication inference.
6. The updated decision register differentiates accepted architecture from *actually applied* candidate modifications, and reports any exact unresolved collision without assuming successful resolution.
7. **AR-001:** A catalogue-wide quality check confirms canonical Feature descriptions are intelligible architectural responsibilities/outcomes, not mechanically rendered KPI formulas, while measurement/acceptance evidence is preserved separately; legitimate measurement Features are not damaged by blanket substitutions.
8. **AR-002:** A catalogue-wide quality check confirms canonical CT3/Feature names and descriptions contain no editorial proposal/unverified/newness badges; retained evidence and metadata still state source, acceptance and actual verification status correctly.
9. No claim is made that the governed Continuum has changed merely because an offline candidate and workbook are updated.

---

**End of XD-001–XD-028 consolidated adjudications and accepted catalogue-wide authoring rules.**
