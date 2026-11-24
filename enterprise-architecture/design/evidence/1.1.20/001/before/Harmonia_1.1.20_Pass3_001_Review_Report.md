# Harmonia 1.1.20 — Pass 3 / Iteration 001: Reconciliation, Rationalisation and Traceability

**Review date:** 2026-11-05  
**Working baseline:** Candidate 017, not governed or accepted  
**Underlying pinned source:** `fhirfactory/harmonia`, `Capability-Map-Consolidation`, commit `fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374` as declared by Candidate 017  
**Nature of work:** Offline evidence-based review; no canonical identifiers assigned, no repository commits, no ArchiMate import, no acceptance declaration.

## Executive assessment

**The discovery/coverage pass is substantially ready to close as a working baseline, but the Capability Model is not ready for direct governed ingestion.** The immediate work should be *corrective rationalisation* of duplicated names, circular Feature criteria, semantic scope and unverified mappings. No open-ended new capability search is recommended.

- **Catalogue examined:** 24 CT1 (15 Business, 9 Enterprise); 145 CT2 (86 Business, 59 Enterprise); 586 CT3 (293 Business, 293 Enterprise); 2944 Features (1,474 Business and 1,470 Enterprise).
- **Mechanical expansion evidence:** 573/586 CT3s have exactly five Features (97.8%). The model's count is not evidence of distinctive or complete obligations.
- **Textual review:** 2627 Feature rows carry at least one editorial or measurement warning, **including 1,324 CamelCase-name style flags**. 1,168 rows carry one of six stronger generic/circular-measurement flags; these are *review triggers*, not automatic invalidations.
- **Potential overlap:** 18 lexical CT3 pairs and 29 lexical Feature pairs; several are intentional parallel responsibilities. **Do not auto-merge.**
- **Business↔Enterprise:** all 353 C011 links refer to valid current CT2 identifiers and cover all 86 Business CT2s, but cover only 47/59 Enterprise CT2s. Using prior C012/C014 evidence plus **clearly labelled** recent inferences yields **539 proposed unique links** and covers 59/59 Enterprise CT2s *structurally*. New links: 186, **none adjudicated**.
- **Services:** preserve 142 canonical Business Services and five canonical Application Services as source objects. The C010 service catalogue has 174 proposed granular Application Services across the original 47 Enterprise CT2s. This review proposes 60 *additional or explicitly selected* Service/operation candidates, for 234 working service candidates. **No 1:1 CT3→Service or external-exposure assertion.**
- **Enterprise Features:** all 1,470 have at least one candidate Service association at their CT2 (2,440 first-/second-ranked candidate connections), but lexical same-domain links are **not proof of satisfaction, contract adequacy or realisation**.
- **Journeys and offerings:** 179 stages across 29 Journeys have at least one proposed Business→Enterprise→Service path after semicolon-separated CT2 inputs are handled correctly. All 42 scenario referents across five validation lenses resolve structurally. **No end-to-end test, Feature satisfaction or NFR conformance was executed**.

## 1. Discovery baseline — decision reconciliation

The 26-entry [`Decision_Reconciliation_Register.csv`](Harmonia_1.1.20_Pass3_001_Decision_Reconciliation_Register.csv) reconciles the most recent discussion with Candidate 017 and existing DRPs/Design Notes. Status terms indicate **model coverage**, not authorisation, implementation or formal release commitment.

| ID | Decision / boundary | Release horizon | Candidate 017 status |
|---|---|---|---|
| D001 | Identity.Authentication | R1.x/R2.x | PRESENT_PARTIAL |
| D002 | Enterprise profile lookup | R1.x/R2.x | PARTIAL |
| D003 | Selective enterprise API provision | R1.x/R2.x | PARTIAL |
| D004 | FHIR RESTful resource scope | R1.x/R2.x | PARTIAL |
| D005 | Purpose-specific external operations | R1.x/R2.x | PARTIAL |
| D006 | External enterprise service consumption | R1.x/R2.x | PARTIAL |
| D007 | Harmonia standalone baseline | R1.x/R2.x | PARTIAL |
| D008 | HL7 v2 contract specifics | R1.x/R2.x | PRESENT |
| D009 | FHIR protocol conformance | R1.x/R2.x | PRESENT |
| D010 | Platform integration vs management of | R1.x/R2.x | PARTIAL |
| D011 | Sylloge media management | R1.x/R2.x | PRESENT |
| D012 | Large content reference threshold | R1.x/R2.x | PARTIAL |
| D013 | General bulk-data interconnect | R1.x/R2.x scope to confirm | PRESENT |
| D014 | Bulk assurance record exports | R1.x/R2.x scope to confirm | PRESENT |
| D015 | Dereferenced evidence export | R1.x/R2.x scope to confirm | PRESENT |
| D016 | EMR boundary | ALL | PARTIAL |
| D017 | Analytics boundary | ALL | PARTIAL |
| D018 | Data Monitor | R3.x | ABSENT_AS_EXPLICIT_CT2 |
| D019 | Early-release dependency observation | R1.x/R2.x | PRESENT_PARTIAL |
| D020 | Energeia operational management | R1.x/R2.x;R3.x future | PRESENT_PARTIAL |
| D021 | Pragma specialisation names | ARCHITECTURAL_MODEL_NOT_RELEASE_SET | PARTIAL |
| D022 | No implied service deployment | ALL | PARTIAL |
| D023 | Scope & authority standing | ALL | PRESENT_AS_METADATA |
| D024 | Product perspectives | R1.x/R2.x;R3+ full EMPI | PRESENT_PARTIAL |
| D025 | Assurance and Themis division | ALL | PRESENT_PARTIAL |
| D026 | Technical enablement lens | ALL | NOT_EXPLICITLY_TRACED |

**Important close-out actions:**

1. Preserve OIDC/OAuth **Identity.Authentication as SEP**, Keycloak/PostgreSQL as replaceable dev/test/POC default, and Themis-owned authorisation. The C010 service name `Authenticate User Identity` must not be interpreted as Harmonia becoming its own credential issuer. `getProfile()` is separate enterprise profile association, not login or an authorisation grant.
2. Record the bounded externally **PROVIDED** service scope: five FHIR resource families/groups and six named purpose-specific operations. Do not infer that all Enterprise Capabilities are externally accessible, that all FHIR interactions are supported or that a “microservice” requires independent deployment.
3. Record the three **CONSUMED** service groups with four named operations: `getProfile()`, `publishDocument()`, `sendEmail()`, `sendSMS()`. The existing SSO identity federation is a separate consumed capability. Early releases stay substantially standalone.
4. Retain explicit HL7 v2 MLLP/ACK/client-tolerance standards obligations and versioned FHIR contract behaviour (C015). Treat the release-support matrix and selected references as *contract decisions*, not universal FHIR API defaults.
5. Explicitly associate E01.05 reference management with Sylloge E01.06 media management, preserving **logical IU ≠ binary payload ≠ physical custodian**. Sylloge is not Mnemosyne and large binary custody need not be local.
6. Retain reusable E03.07 bulk-data interchange distinct from Kleio/Dokimasia evidence-specific export and external analytic platforms. Preserve the non-dereferenced vs dereferenced evidence and all-or-nothing authorised release semantics.
7. Complete the **Technology Service Integration / Management Of** trace from E08 and existing ICT Foundation lenses into future service/technology interfaces. It is not a mandate for Energeia to own IT/Ops.
8. Qualify Business Capability and E09 wording so Harmonia does **not** become the EMR, clinical decision authority, data warehouse or Big Data analytics engine merely by contributing to healthcare outcomes.
9. Preserve **R3.x Data Monitor** as a deferred candidate; neither promote it into R1.x/R2.x nor fabricate source-system monitoring obligations. R3 adaptive load/resilience is a separate deferred matter.
10. Validate that `Pragma` specialisations `Entole`, `Dokime`, `Stimulus` and `Effector` are represented accurately in the later IA/Services work. Candidate 017 does not itself settle their Information Architecture identities.

**Classification:** This pass can close *catalogue discovery* as a working candidate **subject to the explicit unresolved decisions above**. It cannot close Capability Model acceptance or prove complete service realisation.

## 2. Hierarchy and language rationalisation

### 2.1 Structural integrity

- No missing CT1/CT2/CT3 parents were found in the parsed hierarchy.
- CT3 Feature-count distribution: **573 with five**, **12 with six**, **one with seven**. These counts are preserved; no forced rebalancing.
- No exact duplicated CT3 titles were detected. There *are* pairs with overlapping meaning, and duplicate Feature labels across separate subjects.
- The five-Feature rule is an authoring artefact, not a domain invariant. Future refinement must ask whether each Feature is an independent capability obligation with a testable result, not whether a CT3 has a nice symmetrical row count.

### 2.2 Repeated or unhelpful language (full population examined)

| Pattern in Feature test criterion | Occurrences | Example | Concern |
|---|---:|---|---|
| “specified … quality condition” | 250 | `B01.01.01.F04` | Circular; does not state the actual condition |
| “against an explicitly defined assessment rubric” | 240 | `B01.01.01.F02` | Rubric is not defined; measuring its existence is not behaviour |
| “accessible, accurate evidence of” *repeating the Feature name* | 358 | `B01.02.01.F04` | Often circular evidence rather than a substantive test |
| “demonstrably available and usable” | 89 | `B01.01.02.F03`-family | May be necessary, but does not specify circumstances or required outcome |
| Generic “positive and negative … test cases” | 143 | multiple | Needs explicit positive, adverse and authority boundaries |
| Generic “verified [same name]” wording | 88 | multiple | Verification measure unspecified |
| “elapsed time from applicable trigger” without an interval | 37 | multiple | Metric cannot pass/fail until threshold is defined |
| CamelCase/PascalCase embedded in Feature display names | 1,324 | `UnderstoodNextStep` | Naming inconsistency; stylistic, generally **not** semantic defect |

**Treatment:** Preserve identifiers and intent. For each material/empty criterion, separate: (a) concrete Feature obligation, (b) applicable input/event/subject, (c) expected observable output or constraint, (d) objective verification. A measurable formula must not be used as a substitute for defining the required behaviour. Do **not** invent universal thresholds, test rubrics or clinical authority.

### 2.3 Highest-priority semantic/overlap review

| Possible overlap | Priority | Why review |
|---|---|---|
| E07.05.03 ↔ E07.05.06 | P1 | Probable duplication: incomplete information disclosure vs incomplete AND qualified information disclosure; inspect existing features and merge ONLY if same obligation. |
| E02.05.02 ↔ E02.05.03 | P1 | Probable duplication: access restoration vs degraded access and restoration; distinguish recovery event from degraded-mode ongoing service. |
| E02.03.01 ↔ E02.03.04 | P2 | Overlapping distributed access and consistency; separate read availability from consistency constraints. |
| E06.03.01 ↔ E06.03.03 | P2 | General parcel routing vs exclusive Artemis routing decision; retain only a demonstrable semantic distinction. |
| E01.01.02 ↔ E02.01.02 | P1 | Controlled information change vs governed information change in Access CT2; inspect whether write authority was placed under information access. |
| E02.04.04 ↔ E06.08.03 | P2 | Pub/Sub information matching vs task-subscription eligibility; potentially valid specialisation: preserve topic-specific semantics. |
| E06.08.07 ↔ E06.09.07 | P2 | Published work recovery vs Twin deferred work recovery; potentially valid boundary if different recovery obligations. |
| E06.02.04 ↔ E06.02.05 | P2 | Non-Twin vs Twin progression purposefully separate; strengthen Entole/Behaviour semantics rather than merge. |
| E03.06.04 ↔ E03.06.05 | P3 | FHIR server and client roles intentionally distinct despite similar wording. |
| E03.03.02 ↔ E03.03.05 | P2 | Boundary adaptation vs isolation possible semantic overlap; specify contract outcomes. |
| E05.05.01 ↔ E05.05.02 | P3 | Protection in exchange vs custody generally different; check precise scope. |
| B05.03.01 ↔ B05.03.04 | P2 | Clinical handover acceptance repeated with evidence variant; potential duplicate. |
| B13.04.02 ↔ B14.06.01 | P3 | Participant safeguarding in research vs community support; homonym across different subjects, likely legitimate. |
| E08.03 ↔ E08.07 | P1 | Audit/provenance reporting vs authoritative recording: verify separation of evidence capture, read/view and ownership. |
| E08.03 ↔ E08.08 | P1 | Audit/provenance reporting vs controlled export: potentially duplicate service labels and different release guarantees. |
| E01.05 ↔ E01.06 | P2 | Reference resolution vs Sylloge binary custody/assembly: maintain explicit separation, remove duplicate access APIs if present. |

Suggested dispositions are provided separately in [`Semantic_Disposition_Proposals.csv`](Harmonia_1.1.20_Pass3_001_Semantic_Disposition_Proposals.csv). Some issues are intentionally distinct, while others warrant consolidation, rehoming or clarification; the table never changes model identities.

**Additional exact/near Feature name matches for review:** `E01.03.04.F05` vs `E04.03.04.F04` (`NativeRepresentationTraceable`); `E03.02.04.F03` vs `E03.06.01.F05` (`ContractChangeTraceable`); `E07.04.01.F01` vs `.F02` (restoration of user work context); `E03.03.03.F01` vs `.F02` (standards-native fidelity). The Business/Enterprise same-name pair `B12.04.04.F01` vs `E08.06.04.F03` may be *appropriate traceability*, **not** duplication.

**Architecture-level review risks:**

- `E08.02.04` Log-Satellite and `E08.02.05` Log-Server name concrete application/middleware designs in a capability layer. The requirement is reliable capture, transfer, receipt, durable log management and separation of concerns; the specific satellite/server structure belongs to Information Systems/technology realisation. **Review placement; do not erase the logging requirements.**
- `E06.03.03` exclusive parcel-routing choice could be an application architecture invariant rather than a peer end-user capability. Preserve Artemis routing authority as an invariant even if its CT3 placement changes.
- Distinguish `E05.01` federated authentication *consumption* from Themis `E05.02`/`E05.06` authority computation and `E05.03` enforcement. Capability labels should not accidentally imply Harmonia is the IdP.
- E08.03 vs E08.07 vs E08.08 need an explicit **reporting / authoritative recording / export-collation** separation; do not merge based on topic words alone.
- `B02` through `B06` describe healthcare enterprise clinical capabilities, most of which Harmonia **supports but does not itself perform**. Their continued inclusion is legitimate in the Business Capability map so long as every proposed Harmonia support link distinguishes *enablement* from *clinical responsibility*.

**Files:** full 2,944-row [`Full_Feature_Language_Audit.csv`](Harmonia_1.1.20_Pass3_001_Full_Feature_Language_Audit.csv); 586-row [`CT3_Structural_Audit.csv`](Harmonia_1.1.20_Pass3_001_CT3_Structural_Audit.csv); ranked [`Semantic_Duplication_Candidates.csv`](Harmonia_1.1.20_Pass3_001_Semantic_Duplication_Candidates.csv); human adjudication shortlist [`Priority_Semantic_Review.csv`](Harmonia_1.1.20_Pass3_001_Priority_Semantic_Review.csv).

## 3. Business → Enterprise → Services traceability

### 3.1 Business↔Enterprise CT2 links

The C011 table contains **353 distinct, valid CT2 pairs**, all labelled `PROPOSED_LINK_NOT_APPROVED_NOT_IMPLEMENTED`. Its scope classifications matter: 215 `BUSINESS_CAPABILITY_SUPPORT_ONLY`, 104 `SOLUTION_SCOPE_UNDETERMINED`, and 34 `POTENTIALLY_OUTSIDE_HARMONIA_SOLUTION`. None should silently become committed Harmonia solution obligations.

To reconcile later changes without discarding C011:

- `C012_NEW_BUSINESS`: direct Business CT3 → Enterprise CT2 references, collapsed to candidate parent CT2 pairs.
- `C012_NEW_ENTERPRISE`: Business CT2 → Enterprise CT3 references, collapsed to candidate parent CT2 pairs.
- `C014_CT3`: Business CT3 → Enterprise CT3 stress-test relationships, collapsed to candidate CT2 pairs.
- `C015_C017_INFERENCE`: explicitly marked review hypotheses for newly introduced standards, Sylloge media, bulk exchange and late directory/work components that still needed a Business contribution bridge.

**Result:** 539 unique proposed pairs; +186 beyond C011. All 59 Enterprise CT2s have at least one structurally plausible Business CT2 contribution after reconciliation, but **the 186 supplemental links are not approved and are not satisfaction proofs**.

### 3.2 Previously unlinked Enterprise CT2s

| Enterprise CT2 | Capability | Proposed Business links | Candidate Services |
|---|---|---:|---:|
| E01.06 | Governed Binary & Large-Media Content Management | 6 | 3 |
| E02.06 | Entity & Service Directory Discovery | 6 | 5 |
| E03.06 | Standards-Native Healthcare Interaction Behaviour | 6 | 9 |
| E03.07 | Governed Bulk Data Interconnect | 7 | 4 |
| E05.06 | Authorisation Policy & Effective Authority Management | 8 | 3 |
| E05.07 | Health Entity Identity & Association | 5 | 4 |
| E06.07 | Work Acceptance & Outcome Reconciliation | 6 | 3 |
| E06.08 | Task Publication, Subscription & Assignment | 5 | 4 |
| E06.09 | Asynchronous Digital Twin–Entity Work Coordination | 8 | 3 |
| E07.06 | Human–Digital Twin Work Interaction | 5 | 3 |
| E08.07 | Audit, Provenance & Assurance Evidence Recording | 9 | 3 |
| E08.08 | Evidence Collation, Export & Controlled Release | 8 | 3 |

**Full links:** [`Business_Enterprise_CT2_Reconciliation.csv`](Harmonia_1.1.20_Pass3_001_Business_Enterprise_CT2_Reconciliation.csv). The `EvidenceSources` column lets reviewers separate inherited evidence from this review's inferences. **No arbitrary 1:1 relationship is required.**

### 3.3 Service bridge and responsibilities

- **142 existing canonical Business Services + five existing canonical Application Services:** preserve without renaming or pretending C010 has proven their counterparts. See [`Existing_Canonical_Services_Preserve.csv`](Harmonia_1.1.20_Pass3_001_Existing_Canonical_Services_Preserve.csv).
- **174 C010 proposed Application Services:** preserve their existing candidate identity (`ES-*`), without inferring direction from the service name.
- **60 explicit new/proposed Service or operation candidates:** 39 aimed at CT2s missing from C010; 16 explicitly defined external-facing/consumed operations (including OIDC federation); five technical interface candidates. This is a *service review inventory*, **not** 60 approved new implementation services. They must be reconciled against C010 and canonical services before ADD.
- Working classification across the 234 candidate services: **174 existing C010 services still `UNCLASSIFIED_C010`**, 32 new `INTERNAL`, 17 new `PROVIDED`, six new `CONSUMED`, five `TECHNICAL_INTERFACE`. We did not guess external exposure for the original 174.

**Provided:** the nominated FHIR resource groups and six purpose-specific operations; controlled disclosure; only supported interactions, profiles and referenced elements.

**Consumed:** OIDC/SSO SEP, `getProfile()`, `publishDocument()`, `sendEmail()`, `sendSMS()`. These are optional dependencies/operational contracts, not reason to weaken the standalone baseline. The email/SMS operations are separate despite sharing a communication service family.

**Internal:** governed persistence/reference, task, policy, evidence, Pub/Sub, Twin progression and supporting coordination contracts, typically aligned to existing Harmonia component responsibilities but not equated to deployment units.

**Technical interfaces:** health/readiness, dependency signals, technical telemetry, faults and controlled recovery/deployment participation; hosting platforms and IT/Ops retain management authority. Keep the 18 ICT Foundation lenses as assessment lenses, not new Enterprise CT1s.

Full inventories: [`Complete_Candidate_Service_Bridge.csv`](Harmonia_1.1.20_Pass3_001_Complete_Candidate_Service_Bridge.csv), [`Proposed_Service_Bridge_Additions.csv`](Harmonia_1.1.20_Pass3_001_Proposed_Service_Bridge_Additions.csv), [`Enterprise_Feature_to_Service_Candidates.csv`](Harmonia_1.1.20_Pass3_001_Enterprise_Feature_to_Service_Candidates.csv), [`Business_to_Enterprise_to_Service_Paths.csv`](Harmonia_1.1.20_Pass3_001_Business_to_Enterprise_to_Service_Paths.csv).

### 3.4 Exact limit of current feature-to-service alignment

All 1,470 Enterprise Features have a **CT2-scoped possible service route**, not a proven service realisation. The 2,440 candidate Feature→Service rows use same-CT2 restriction and lexical similarity ranking, with an explicit `CANDIDATE_CONNECTION_NOT_FEATURE_SATISFACTION` status. **This is not a strong semantic match:** the best-ranked connection scores below 0.10 for **782/1,470** Enterprise Features (135 score precisely zero); only **79/1,470** have a lexical score at or above 0.30. These are vocabulary-match diagnostics, not confidence probabilities. A reader must not infer that all Features have a valid fulfilling Service merely because every CT2 has a candidate Service. Neither service I/O, security, versioned contract, assigned Function, NFR conformance nor performer allocation has been verified.

The workbook and CSV make the *bridge* inspectable. **The next governed semantic review must adjudicate Feature→Service promises, not mechanically accept the lexical rankings.** Some C010 services may be redundant with the 60 explicit candidates and need consolidation, not growth.

### 3.5 Journey checks — 29 Journeys / 179 stages

- All 179 source stage rows were parsed, including 13 stages that specify **multiple Business CT2s separated by semicolons**; these are not invalid identifiers.
- All 179 now have at least one *proposed* Business CT2 → Enterprise CT2 → Service chain. The chain reuses the earlier 011 evidence and new candidate links and respects the stage's original Enterprise CT2 hypothesis where possible.
- Stage coverage has **not been executed or verified**. No narrative leap from 'linked' to 'working' is warranted; negative probes are retained per stage.

Full [`Journey_Stage_Service_Trace.csv`](Harmonia_1.1.20_Pass3_001_Journey_Stage_Service_Trace.csv) preserves stakeholder, stage, business path, original Enterprise hypothesis, service candidate, adverse probe and non-verification.

### 3.6 Offering stress-test checks — 42 scenarios

| Validation lens | Scenario count | Scope note |
|---|---:|---|
| Provider Directory | 8 | Source-qualified practitioner, organisation, location, service and affiliations |
| EMPI / identity correlation | 8 | Full EMPI is R3+; early-release scoped correlation is not equivalent |
| Longitudinal Health Record | 8 | Longitudinal access/presentation does not make Harmonia an EMR |
| Health Information Exchange | 8 | Standards-native receipt, persistence, retry, profile tolerance and partner conformance |
| Digital Twin / physical work | 10 | Async human fulfilment, task Pub/Sub, entity-relative actor authority and recovery |

All 42 rows resolve to valid CT1, CT2 or CT3 referents in Candidate 017. A few intentionally reference CT2 rather than CT3; this is not an error. All scenarios still carry `PROPOSED_TEST_NOT_EXECUTED` standing, including adverse cases. See [`Offering_Scenario_Service_Review.csv`](Harmonia_1.1.20_Pass3_001_Offering_Scenario_Service_Review.csv).

## 4. Suggested adjudication order

1. **P1: Fix high-consequence semantic ambiguity** — external OIDC vs Themis, externally provided APIs, clinical authority/EMR boundary, audit/provenance/export ownership, task vs information Pub/Sub, and misleading operational-management ownership.
2. **P1: Refine the most circular Feature criteria** — begin with the 250 tautological quality-condition instances, 240 generic rubric instances and 358 circular evidence instances. Preserve content/IDs; replace invented quality-measure placeholders only where the original requirement is recoverable.
3. **P2: Review 19 priority semantic pairing/boundary rows** and assign KEEP_DISTINCT / MERGE_CANDIDATE / REHOME_CANDIDATE with rationale and governing IDs. Do not automatically deduplicate similar labels.
4. **P2: Adjudicate 186 supplemental CT2 contribution links** (especially the 30 latest candidates inferred solely from this pass) before asserting business demand and before service-level realisation claims.
5. **P2: Reconcile 60 proposed Service additions against the existing 174 C010 candidates and the canonical 147 Services**. Resolve direction, access contracts, scope, I/O and component realisation deliberately.
6. **P2: Select a bounded set of Journey and offering negative cases** for human semantic walkthrough first (HL7 ACK vs persistence; LHR wrong-person source; provider affiliation withdrawal; physical work pending; partial audit export authorisation), then extend tests to all 42/179 hypotheses.
7. **Deferred:** R3.x Data Monitor (external-source availability → information impact) and R3 adaptive load/resilience; retain explicit review triggers, no silent R1.x/R2.x obligations.

## 5. Completion statement and remaining qualifications

**Completed in this offline review:** 26-decision working register; full 2,944-Feature textual scan; full 586-CT3 structural audit; duplicate/overlap review shortlist; reconstructed 539-row CT2 crosswalk; 234-row working Service bridge; 1,470-Feature service adjacency (2,440 candidate connections); 179-stage Journey candidate paths; 42 offering stress-case referent checks.

**Not completed / cannot honestly claim:** full semantic adjudication of 2,944 Features, validation of service fulfilment, production security/API contract selection, test execution, accepted R1.x/R2.x scoping of every Feature, canonical ID assignment or 1.1.20 governed ADD. The existing architectural authority and prior decisions remain intact.

**Recommendation:** review the priority issues and contract boundaries; approve a bounded *rationalisation change set* before permitting automated ingestion. This review is the evidence pack for that adjudication, not a request for Codex execution.
