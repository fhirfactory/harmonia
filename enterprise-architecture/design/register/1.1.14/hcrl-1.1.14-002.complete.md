<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Data Governance Services architecture / code reconciliation & unresolved architectural findings | Rejected — Review approach no longer conforms to the adopted implementation strategy. Findings remain discovery evidence; resolving them individually against existing implementation would prematurely constrain solution design and invert the architecture-to-solution relationship. | Review approach rejected; findings DI-2 through DI-9 retained as discovery evidence without mechanical forward carriage into a new HCRL. Post-result review: Accepted by Mark Hunter, Solution Architect. |
| [DR-2](#dr-2) | Preserved decisions from iteration 001 (supporting minutiae, DR-2, DR-3, DI-1) | Accepted by the Solution Architect in iteration 001 | Preserved completed history from iteration 001; DR-2 supporting minutiae, DR-3 finite-provenance governance framework repair, and DI-1 Convergence Plan status disposition remain accepted. |
| [DR-3](#dr-3) | Accepted 1.1.14 architecture release publication, model preservation verification and completed review state | Accepted; release 1.1.14 closure expressly confirmed | Mark Hunter, Solution Architect, accepts the prepared 1.1.14 resulting state in post-result review. Durable 1.1.14 ArchiMate Open Exchange XML (`Harmonia-Architecture.xml`) and derived documentation packages are confirmed. Model comparison verifies 100% preservation across elements, relationships, and Views. |

# HCRL — 1.1.13 → 1.1.14: Data Governance Services architecture development & reconciliation

**Complete and closed.** Mark Hunter, Solution Architect, reports completed post-result review of the prepared Harmonia Enterprise Architecture 1.1.14 release state and explicitly states: **“Accepted. The prepared Harmonia Enterprise Architecture 1.1.14 resulting state is accepted.”** The [closure instruction][closure-task] directs completion of HCRL 1.1.14-002 and registration of the completed review state in accordance with the governed process. This is human acceptance, distinct from earlier automated verification.

The Solution Architect's verbatim disposition on DR-1 rejects the review/resolution approach of attempting to resolve findings individually against existing implementation, while retaining findings DI-2 through DI-9 as discovery evidence. In accordance with the Solution Architect's directive, findings DI-2 through DI-9 remain retained in this review record and SHALL NOT be mechanically carried forward as unresolved architectural obligations into a new HCRL. Iteration 001 accepted decisions (DR-2 supporting minutiae, DR-3 finite-provenance governance framework repair, and DI-1 Convergence Plan status disposition) remain preserved as completed history.

The original [pending review](../../review/1.1.14/hcrl-1.1.14-002.pending.md) and all original evidence remain unchanged. Completion belongs to the same 1.1.13 → 1.1.14 transition and consumes no additional release: root `architecture-release.json` records **1.1.14**, predecessor **1.1.13**.

**Review basis.** The immediate governed predecessor release is 1.1.13, whose [completed HCRL](../../register/1.1.13/hcrl-1.1.13-002.complete.md) establishes the accepted 1.1.13 canonical architecture and durable Open Exchange projection. Entry tracked state for this review stream was commit `ff141340fdb0ab14ca46401bef5a7058138b7aad` on branch `Data-Governence-Services`, with snapshot state at commit `b73b02cf`. The common Architecture Governance, Architecture Review Praxis, Architecture Change Provenance, and Architecture Release Policy govern this transition.

The Architecture Continuum remains canonical authority. AX-04 permits machinery reuse; AX-08 supports evidence over machinery; AX-14 distinguishes canonical architecture, derived publication, discovery evidence, and human acceptance; AX-15 preserves empirical uncertainty; and AX-17 distinguishes approved architecture from unresolved matters and proposals. No canonical element Markdown, native types, standing, semantic relationships, or authored Views have been altered.

<a id="dr-1"></a>
## DR-1 — Data Governance Services architecture / code reconciliation & unresolved architectural findings

**Solution Architect Disposition — Rejected:**

> **Rejected — Review approach no longer conforms to the adopted implementation strategy. Findings remain discovery evidence; resolving them individually against existing implementation would prematurely constrain solution design and invert the architecture-to-solution relationship.**

**Governance Interpretation & Retention of Findings DI-2 through DI-9:**
- The Solution Architect's rejection applies strictly to the **review/resolution approach** (attempting to resolve findings individually against existing implementation), not to the factual existence of the discoveries recorded as DI-2 through DI-9.
- Findings **DI-2 through DI-9 remain retained as discovery evidence** within this review record. In accordance with the Solution Architect's directive, they **SHALL NOT** be mechanically carried forward as unresolved architectural obligations into a new HCRL.
- Resolving discoveries individually against current implementation would prematurely constrain solution design and invert the governed architecture-to-solution relationship (AX-08, AX-14, AX-17). Existing implementation is not, by itself, evidence of architectural intent.

**Reported Discovery Items (Retained as Discovery Evidence):**
1. *DI-2: Mnemosyne Persistence Adapter Dual-Implementation*: `@Primary AuthoritativePersistenceService` (custom Spring Data repository) versus experimental `HapiJpaAuthoritativePersistenceAdapter`, without authoritative Decision Register entries for ADR-021/022.
2. *DI-3: Absent Entity Read Result Contract (404 vs 500)*: Divergence between `AuthoritativePersistenceService.read()` returning `NotCommitted` (HTTP 500) versus expectations of `Absent` (HTTP 404).
3. *DI-4: Mneme Four-Service Surface vs C11 Point Contracts*: Atomic point operations (`GovernedReader`/`GovernedWriter`) versus Continuum candidate assertions specifying a four-service surface (Context Load/Save, Adhoc Access/Search).
4. *DI-5: Inbound Ingress Exception Handling (Observed Invariant Violation — Invariant 4 / REC-001)*: Remote cache write exception swallowed by `DefaultCommunicationService`, emitting an `AA` (Application Accept) ACK rather than an `AE` NACK.
5. *DI-6: Outbound Egress Exception Handling (Observed Invariant Violation — Invariant 5 / REC-002)*: Catch-and-log blocks in `OutboundStateLifecycleManager` swallowing exceptions during lifecycle initialization, Task/Provenance updates, and Petasos event publishing.
6. *DI-7: Legacy `FhirRestCacheStore` & Soft-Delete Cascade (Observed Invariant Tension — ADR-020 / Invariant 8)*: Synchronous write-through cache store removal invoking REST DELETE reaching `@Delete` providers and generic soft-delete.
7. *DI-8: Direct `FhirStorageService` Coupling in Mneme (Observed Invariant Violation — Invariant 8 / AX-05)*: Direct JPA `@Autowired` persistence access in `MnemeFhirServices` bypassing Themis policy enforcement and Petasos event emission.
8. *DI-9: Persistence Security Context & Default-Deny Authorization (Observed Invariant Violation — Invariant 6 / AX-07)*: `FhirStorageService` defaulting a `null` security principal to `service:mnemosyne` with self-granted permissions on internal storage paths.

**Adopted Forward Strategy: Architecture-Led Solution Development via DRPs:**
The adopted forward direction is architecture-led solution development using Design Review Proposals (DRPs). Each DRP will identify the Feature or Features it realises and will develop the required Motivation, Strategy, Business, and Application architecture before implementation and reconciliation proceed:
1. **Information Unit Framework DRP**: Addressing canonical resource representations, composite versus atomic point operations (DI-4), absent entity read contracts (DI-3), and persistence adapter alignment (DI-2).
2. **Authentication & Authorisation Framework DRP**: Addressing security principal propagation, default-deny security evaluation, and non-bypassable security context boundaries (DI-9).
3. **Audit & Traceability Framework DRP**: Addressing non-repudiable audit logging, provenance emission, and exception tracking across ingress and egress boundaries (DI-5, DI-6).
4. **Mneme-to-Mnemosyne Governed Interaction DRP**: Addressing active distributed state versus durable truth, write-through caching, soft-delete cascades (DI-7), direct persistence coupling (DI-8), and ingress/egress dual-write safety (DI-5, DI-6).

The discovery evidence represented by DI-2 through DI-9 informs future solution architecture but does not constrain it merely because it was discovered in historical or current implementation.

**Historical DI-1 Disposition (Preserved from Iteration 001):**
In iteration 001, Mark Hunter, Solution Architect, disposed DI-1 (Convergence Plan historical status):
- *Disposition*: Historical authority conflict; no current architectural conflict. The Convergence Plan and associated historical implementation/design material are discovery evidence only and do not constitute current architectural authority. Status statements, milestone completion claims, and ADR acceptance claims contained within that historical material do not establish current architectural standing. The governed Architecture Continuum and governed Decision Register remain authoritative. No Architecture Continuum change is required as a consequence of DI-1.

<a id="dr-2"></a>
## DR-2 — Preserved decisions from iteration 001 (supporting minutiae, DR-2, DR-3, DI-1)

**Solution Architect disposition: Accepted in iteration 001.** Preserved completed history from [iteration 001](../../review/1.1.14/hcrl-1.1.14-001.pending.md#dr-2).

The supporting minutiae and provenance records for the Data Governance Services reconciliation and housekeeping activity are accepted as an adequate and truthful record. This acceptance does not constitute acceptance or adjudication of any DR-1 architectural finding, disposition item, conforming path, or advisory recommendation.

The finite-provenance governing principle ("Provenance records describe the governed activity, not the mechanical production history of the provenance records themselves"), substantive activity versus provenance maintenance distinction, conditional review lifecycle, and non-recursive maintenance rules accepted in [iteration 001 DR-3](../../review/1.1.14/hcrl-1.1.14-001.pending.md#dr-3) remain authoritative.

<a id="dr-3"></a>
## DR-3 — Accepted 1.1.14 architecture release publication, model preservation verification and completed review state

**Solution Architect post-result review: Accepted.** Mark Hunter, Solution Architect, reviewed the prepared 1.1.14 resulting architecture state and issued post-result acceptance:

> **Accepted. The prepared Harmonia Enterprise Architecture 1.1.14 resulting state is accepted.**

**Actual Publication.**
The maintained publication machinery generated the durable 1.1.14 ArchiMate Open Exchange model:
`enterprise-architecture/design/archimate/published/1.1.14/Harmonia-Architecture.xml`
- SHA-256: `ee1d7ef8d5491a4a656d8c2f07c38999b6276a130064d797a08f19501aa47161`
- File size: 26,919,406 bytes (26.9 MB)

Root release authority records 1.1.14, previous 1.1.13 (`architecture-release.json`).
Derived documentation packages are generated under:
- `enterprise-architecture/documentation/continuum-manifest/published/1.1.14/`
- `enterprise-architecture/documentation/continuum-rad/published/1.1.14/`

Continuum root `MANIFEST.md` header reflects RAD 1.1.14. The active ArchiMate workbench pointer in `enterprise-architecture/design/archimate/README.md` references `published/1.1.14/Harmonia-Architecture.xml` as the navigable architecture baseline for subsequent DRP development.

**Actual Verification.**
Model comparison (`exchange_compare.py compare`) between the 1.1.13 published baseline and the 1.1.14 publication confirms 100% preservation across all architectural constructs:

| Construct / Check | Confirmed Result |
| --- | --- |
| Canonical elements | 1,602 preserved (0 missing, 0 added, 0 changed) |
| Semantic relationships | 1,651 preserved (0 missing, 0 added, 0 changed) |
| Authored Views | 1,209 preserved (0 missing, 0 added, 0 changed) |
| Visual nodes | 4,005 preserved (0 missing, 0 added) |
| Visual connections | 1,804 preserved (0 missing, 0 added) |
| Canonical contract properties | 100% preserved (canonicalId, displayName, friendlyName, canonicalName, archimateType, harmoniaType) |
| Semantic drift | Strictly confined to release metadata (`Baseline / Architecture Release`: 1.1.13 → 1.1.14) |
| Continuum validation (`architecture_continuum.py validate`) | PASS (1,602 elements, navigation PASS) |
| Tooling unit tests (`unittest discover`) | 66/66 passed |
| Release checks (`make check`) | PASS (release, manifest, XML project, documentation, links) |
| Independent reproducibility (`make verify-independent`) | PASS (independent reproduction of all 20 derived artefacts) |
| Browser smoke test (`make browser-test`) | PASS (`continuum.html` DOM-contract smoke test) |
| Conditional structural review gates (§5:225–245) | NOT TRIGGERED (zero navigation or visual structure drift) |
| Archi no-edit transport check (`verify_continuum_transport.py`) | UNAVAILABLE (Archi binary absent from PATH; uncertainty preserved under AX-15) |
| Evidence layout verification (`architecture_review_evidence.py`) | PASS (iteration 002 Task and Report layout and timestamps validated) |

**Closure and Provenance.**
Formal iteration 002 provenance is established under:
- Task: [`enterprise-architecture/design/evidence/1.1.14/002/before/ai-agent-tasks/20261025T061846Z-close-1.1.14-002-and-publish-1.1.14.md`][task]
- Report: [`enterprise-architecture/design/evidence/1.1.14/002/after/ai-agent-reports/20261025T063900Z-close-1.1.14-002-and-publish-1.1.14.md`][report]

```text
Minutiae-Accepted: Yes
Comment: Mark Hunter, Solution Architect, accepted the prepared 1.1.14 resulting architecture state. Release 1.1.14 governance cycle is closed.
```

**HCRL 1.1.14-002 is complete and closed under the Solution Architect's express instruction.**
Release 1.1.14 is completed and registered, with immediate predecessor 1.1.13. Completion consumes no additional release. The active workbench projection is `published/1.1.14/Harmonia-Architecture.xml`. Discoveries DI-2 through DI-9 remain discovery evidence to inform future architecture-led DRPs. Untracked drafts under `enterprise-architecture/design/offline-design/` remain untouched. In accordance with instructions, all changes remain in the working tree with zero Git commits, pushes, merges, rebases, or tags performed.

[task]: ../../evidence/1.1.14/002/before/ai-agent-tasks/20261025T061846Z-close-1.1.14-002-and-publish-1.1.14.md
[report]: ../../evidence/1.1.14/002/after/ai-agent-reports/20261025T063900Z-close-1.1.14-002-and-publish-1.1.14.md
[xml]: ../../archimate/published/1.1.14/Harmonia-Architecture.xml
[closure-task]: ../../evidence/1.1.14/002/before/ai-agent-tasks/20261025T061846Z-close-1.1.14-002-and-publish-1.1.14.md
