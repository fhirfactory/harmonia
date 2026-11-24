# Pass 1 — Historical Capability/Feature reconciliation

**Preparation complete; adjudication pending. Pass 1 is not complete.** This evidence supports HCRL 1.1.20/001 DR-5. Every Solution Architect decision is **PENDING**, every incorporation status is **NOT AUTHORISED**, and no Capability Model change is incorporated.

## Executive summary

The complete existing native Capability population is **842 records**. The adjudication inventory contains **436 existing identities: 388 Capabilities and 48 Features**. It distinguishes **278 explicitly historical records** (32 Pending Adjudication and 246 Superseded) from **158 existing unresolved current proposals/candidates**. The latter are included because a currentness flag does not accept a historically proposed concept; they are existing source contributions, not Pass 2 gap proposals. The remaining 406 current Canonical/accepted records supply the reference model.

Recommendations are **189 ACCEPT, 246 RETIRE and one REJECT**. ACCEPT concerns the bounded concept and remains conditional on the row’s stated definition, layering and placement judgement. RETIRE preserves existing identities and evidence; for already Superseded records it normally confirms inactive treatment without requiring a new state transition. The sole REJECT concerns incorporation of the underdefined Health Information Sharing Programme Management addition, while retaining its Candidate record and possible proposed elaboration. These are analyst recommendations, not Solution Architect dispositions.

The chief risk is collapsing different responsibilities because they share vocabulary. Internal operational work is not the external execution bridge; physical badges are not clinical credential definitions; subject assurance is not review of the assurance service; protocol mediation is not patient-facing communication; dependency tracking is not authority to determine collective readiness. Source authority, audit, provenance and assurance remain independent. Existing correspondence supplies 214 equivalence findings; none is newly manufactured.

## Complete inventory and progressive review

Start with the [complete identity index](adjudication-inventory.md). It links every identity to a full 16-column adjudication table and its detailed evidence. Review the [dependencies](dependencies.md) alongside those rows. Pages contain at most 40 rows and retain stable P1 references. Those references are scoped review locators; they allocate no architectural identity and do not replace HCRL DR-5.

| Review section | Rows |
| --- | --- |
| [01-pending-capabilities](inventory/01-pending-capabilities.md) | 11 |
| [02-pending-features](inventory/02-pending-features.md) | 21 |
| [03-superseded-unreconciled](inventory/03-superseded-unreconciled.md) | 32 |
| [04-correspondence-ct1](inventory/04-correspondence-ct1.md) | 25 |
| [04-correspondence-ct2-01](inventory/04-correspondence-ct2-01.md) | 40 |
| [04-correspondence-ct2-02](inventory/04-correspondence-ct2-02.md) | 40 |
| [04-correspondence-ct2-03](inventory/04-correspondence-ct2-03.md) | 5 |
| [04-correspondence-ct3-01](inventory/04-correspondence-ct3-01.md) | 40 |
| [04-correspondence-ct3-02](inventory/04-correspondence-ct3-02.md) | 40 |
| [04-correspondence-ct3-03](inventory/04-correspondence-ct3-03.md) | 24 |
| [05-current-unresolved-business](inventory/05-current-unresolved-business.md) | 10 |
| [05-current-unresolved-business_enabling-01](inventory/05-current-unresolved-business_enabling-01.md) | 40 |
| [05-current-unresolved-business_enabling-02](inventory/05-current-unresolved-business_enabling-02.md) | 40 |
| [05-current-unresolved-business_enabling-03](inventory/05-current-unresolved-business_enabling-03.md) | 27 |
| [05-current-unresolved-enterprise](inventory/05-current-unresolved-enterprise.md) | 14 |
| [05-current-unresolved-features](inventory/05-current-unresolved-features.md) | 27 |

Every detailed record includes the original definition or its explicit absence, preserved overview/intent/boundary, independent standing, source framing, actual incident native relationships, recorded source/revision/selector evidence, current comparison definitions and standing, recommendation, consequences and a specific SA question. Earlier retained pre-review contributions remain historical evidence rather than replacement current definitions. Comparison targets can include independently pending historical alternatives; their actual standing is shown. Reading/navigation paths and source parent/owner keys are not silently promoted to Composition or Home.

## Population accounting and disposition summary

| Population | Capabilities | Features | Total | ACCEPT | REJECT | RETIRE |
| --- | --- | --- | --- | --- | --- | --- |
| Pending Adjudication | 11 | 21 | 32 | 32 | 0 | 0 |
| Superseded proposals/fragments without adopted identity correspondence | 32 | 0 | 32 | 0 | 0 | 32 |
| Superseded historical identities with governed correspondence | 214 | 0 | 214 | 0 | 0 | 214 |
| Current unresolved Proposed/Candidate carry-forward | 131 | 27 | 158 | 157 | 1 | 0 |
| **Inventory** | **388** | **48** | **436** | **189** | **1** | **246** |
| Current reference model screened and excluded from adjudication population | 275 | 131 | 406 | — | — | — |
| **Complete native Capability population** | **663** | **179** | **842** | — | — | — |

The exact historical source accounting is **627 source Capability/Feature/enterprise-capability records + 214 historical references + one separately reconciled Feature = 842 existing canonical records**. This population is reconciled independently of naming and currentness in [source-population-accounting.json](source-population-accounting.json). All 842 screening decisions are in [population-screening.json](population-screening.json), and [population-summary.json](population-summary.json) provides all totals. No historical Feature is currently Superseded. There are 12 included records with Canonical semantic standing (11 pending and one Superseded); currentness and approval must remain separate.

| Element type | Semantic standing | Currentness | Recommendation | Count |
| --- | --- | --- | --- | --- |
| Capability | Candidate | current | ACCEPT | 2 |
| Capability | Candidate | current | REJECT | 1 |
| Capability | Canonical | pending_adjudication | ACCEPT | 4 |
| Capability | Canonical | superseded | RETIRE | 1 |
| Capability | Proposed | current | ACCEPT | 128 |
| Capability | Proposed | pending_adjudication | ACCEPT | 7 |
| Capability | Proposed | superseded | RETIRE | 31 |
| Capability | Superseded historical identity — direct correspondence | superseded | RETIRE | 209 |
| Capability | Superseded historical identity — evolved correspondence | superseded | RETIRE | 5 |
| Feature | Canonical | pending_adjudication | ACCEPT | 7 |
| Feature | Proposed | current | ACCEPT | 27 |
| Feature | Proposed | pending_adjudication | ACCEPT | 14 |

The previously retained **784 OpenSpec candidate/source assessments from 421 sources are not this population**. Their existing findings are contextual provenance only. No UseCase/UserStory reconciliation, retargeting or implementation-satisfaction conclusion occurs. DR-6.01–DR-6.05 retain their existing unresolved scope.

## Equivalence, overlap and unresolved semantic findings

- **214 EQUIVALENT:** 209 direct and five evolved historical correspondences are already governed under 1.1.5-002. The original missing historical definitions stay missing; existing correspondence authority supplies continuity. Successor standing is not promoted, and no new successor relationship is created.
- **213 OVERLAPPING:** target sets, broader parent contexts and EC/BEC/Feature layering may cover parts of a responsibility. Overlap is not duplication, and source reconciliation targets are not identity findings. In particular Provider Engagement’s service terms/privileges and Provider Relationship’s affiliations need individual preservation review.
- **8 DISTINCT:** material scope differences are supported by the preserved boundaries. This label concerns the comparisons shown, not an exhaustive model-wide novelty finding. It does not by itself establish placement or formal classification.
- **1 UNRESOLVED:** the sharing-programme proposal has no established original definition/parent/tier. Its later proposed elaboration is preserved but supplies insufficient evidence for the recommended addition. SA may choose another disposition after defining its mandate.

The 158 current unresolved proposals are existing bounded concepts assessed against explicit parent/owner context and native relationship evidence, with additional tailored comparisons where material boundary tension is established. Recommendations to retain them do not claim exhaustive uniqueness, establish implementation coverage, or accept every relationship in their source ancestry. Each row’s exact definition, boundary and comparison definitions make the provisional judgement reviewable.

The current external Workflow & Activity Coordination CT2 also retains a source-qualified contribution about internal activity registry/timers. Its current definition is explicitly external. Both assertions are exposed, not reconciled by widening the definition or silently discarding the contribution. The independent physical-credential definition/relevance question and the device seven-child/eighth-child membership conflict likewise remain visible for SA judgement.

## Architectural consequences if recommendations are accepted

No consequence is applied by this preparation. Acceptance of a concept may lead the SA to retain its existing representation, reconcile it with a current element, or confirm inactive historical treatment. It never automatically merges canonical identities. A proposed target list supplies neither successor identity nor reference-retargeting authority.

For retained pending concepts, SA decisions may be needed on missing definitions, CT1/CT2/CT3 classification, BEC versus EC abstraction and explicit CT3-to-Feature framing. Several original Feature owner keys point to CT2 Health Information Control; source ownership cannot supply the profile’s required CT3 Composition. Changes to Composition, Aggregation, Realization, Home and responsibility are independently meaningful and must be reviewed as such. No formal name, ancestry, relationship or code is invented to remove a qualification.

BC concepts describe the wider health-service architecture; their acceptance does not assign physical clinical work or externally governed domain decisions to Harmonia. BEC integration and coordination preserve source authority under AX-18. Multiple Features/Capabilities may legitimately contribute partial coverage; none of these recommendations demonstrates implementation satisfaction.

## Adjudication dependencies and outstanding decisions

[dependencies.md](dependencies.md) provides coordinated review sets and an independently traceable per-row dependency table. Resolve responsibility boundaries and residual scope before any dependent placement, Feature containment or reference migration. Parent acceptance does not accept its children. The 214 existing correspondences are historical accounting, not reopened identity decisions.

All 436 proposed treatments require SA review to the extent they propose any new disposition; the existing correspondence/standing facts remain unchanged. The principal coordinated questions concern internal/external work, subject/service assurance, credential meanings, general security Feature framing, audit/provenance, source authority, governance publication, service readiness and device membership. Programme-management definition/mandate remains specifically unresolved. The 32 already Superseded proposals/fragments require confirmation that their target sets preserve the whole original scope; no new successor is inferred.

The next activity is **Solution Architect and ChatGPT adjudication** using the inventory. Record attributable decisions under DR-5 and retain individual row correspondence. This task neither advances the HCRL iteration nor marks the programme/Pass 1 complete. No further model changes, Pass 2 expansion or Pass 3 refinement are authorised here.

## Architectural authority and axiom consistency

The canonical Markdown Continuum owns definitions, identities, names and independent standing. The active development identity is 1.1.20; the immutable accepted predecessor is 1.1.19. The complete 1,996-element / 2,246-relationship candidate is preserved, alongside the 1,994-element / 2,235-relationship accepted predecessor and all existing publication/evidence bytes. XML is an interchange projection, not an independent authority.

Relevant authority is [the current HCRL DR-4–DR-6/DR-14](../../../../../review/1.1.20/hcrl-1.1.20-001.pending.md#dr-5), [Continuum Representation](../../../../../../governance/continuum-representation.md), [Capability/Feature Profile](../../../../../../governance/capability-feature-profile.md), [model mapping](../../../../../../governance/archimate-model-mapping.md), [modelling strategy](../../../../../../governance/architecture-modelling-strategy.md), [Review Praxis](../../../../../../governance/architecture-review-praxis.md) and [Change Provenance](../../../../../../governance/architecture-change-provenance.md). The report records preparation facts under the existing DR-5 without replacing any accepted decision.

The relevant [canonical axioms](../../../../../../governance/architectural-axioms.md) are applied as follows:

| Authority | Application in this reconciliation |
| --- | --- |
| AX-01/02/04/12/13 | Preserve health-information purpose, standards boundary, semantic ownership and explicit management boundary; infer no engine or component allocation. |
| AX-05/06/07/11 | Keep active/durable state, information credibility, security policy and availability distinct; neither technical success nor general control establishes information authority. |
| AX-08/09 | Retain meaningful source contributions, decisions and review evidence; do not capture routine processing history as architecture. |
| AX-10/15/16 | Preserve failure, indeterminate effect, authoritative observation and entity/activity progression as distinct meanings in recovery/work recommendations. |
| AX-14/17/18 | Preserve identities, source qualifications, different responsibilities and domain authority; leave unresolved meaning explicit and recommendations unaccepted. |

The approach proposes reviewable semantic treatments without applying them, so uncertainty or overlapping scope does not become an architectural assertion. No implementation is used as architectural authority, and no claim is made to inspect software satisfaction of these capabilities. Existing architecture/model guards can validate any subsequently authorised incorporation, but no acceptance is inferred from this task’s automated checks.

## Evidence and provenance

- [Formal completed Report](../ai-agent-reports/20261103T071256Z-pass1-historical-capability-feature-reconciliation.md): factual outcomes, original task correspondence and production event.
- [Formal operative Task](../../before/ai-agent-tasks/20261103T065659Z-pass1-historical-capability-feature-reconciliation.md): issued instruction preserved verbatim; Task-Issued-At **2026-11-03T06:56:59.964Z**, recovered from the attributable system interaction event.
- [Originating source/protection state](../../before/pass1-source-state.json): entry Git revision and exact canonical, publication and initial-candidate file pins. Source control retains originals without duplicating whole source packages.
- [1.1.20 opening baseline](../../before/initial-baseline.json) and [initialisation evidence](../initialisation.json): exact accepted predecessor, carried candidate and originating decision/evidence correspondence.
- [Source population accounting](source-population-accounting.json), [complete screening](population-screening.json), [reconciliation evidence](reconciliation.json) and [summary](population-summary.json): exact source/identity/row/standing correspondence, historical contributions and actual graph context. Each row links recorded historical sources and their revisions/selectors/excerpts.
- [Historical ARB reconciliation plan](../../../../1.1.5/002/before/arb-reconciliation-plan.json): pre-existing correspondence authority, not a new acceptance record.
- [Original mapping targets](../../../../1.1.18/001/after/reconciliation/mapping-targets.json): contextual preservation of the prior target identities and qualifications; source assessments remain at their originating iteration.
- [Canonical validation](canonical-validation.json), [inventory verification](verification.json) and [evidence layout check](evidence-layout.json): bounded automated results and explicit limits. The verifier is evidence-local and reads current canonical resources; it does not author them.

Canonical explanation wording is preserved. Relative Markdown links in presented excerpts are rebased for navigation; an embedded locator that cannot be resolved in its original context is displayed as a literal source locator. Exact source text, editions and qualifiers remain in the JSON/source records. This does not repair or reinterpret historical architecture.

## Completeness verification and limits

The verification checks exact source identity-set correspondence, all pending/superseded records, the additional current Proposed/Candidate population, no duplicates or omissions, full table columns, recommendation presence, evidence/definition/standing equality, native relationship context, existing correspondence and inverse links, usable review links, and exact preservation of protected input files. All 32 pending and 246 Superseded records appear exactly once; all 158 existing unresolved current proposals/candidates appear exactly once.

The maintained canonical validator reports **PASS: 1,996 elements, 2,246 relationships, 462 qualified predicates, 3,852 source bindings and complete identity/provenance coverage**. The default system Python lacked `jsonschema`; the same check succeeded in the existing `/tmp/harmonia-tooling-venv` with the declared tooling dependencies. That initial invocation was an environment failure, not a model failure. No dependency or production-code change was made.

Automated PASS verifies the stated accounting and preservation controls; it cannot establish semantic truth, source-authority authenticity, architectural equivalence beyond existing dispositions, or Solution Architect acceptance. No new Archi import/export, visual acceptance, publication regeneration, release allocation, staging, commit or push is performed. Inherited transport and naming qualifications remain unchanged. Preparation is complete only to this evidence-producing boundary; **Pass 1 remains pending adjudication**.
