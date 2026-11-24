<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->

# Domain 02 Strategy Cleanup — Step 2 Controlled Reconciliation

Date: 2026-10-09

## 1. Executive Summary

**The authorised Domain 02 Step 2 reconciliation is complete.** All 38
adjudicated findings have an explicit disposition below. Thirteen Domain 02
documents were reconciled, AX-16 was restored to the central axiom register
from its established Domain 01 definition, and this completion report was
created. No new architecture was derived to fill missing relationships.

The reconciliation applies the supplied adjudications within programme stage
1, Domain 02 Strategy Cleanup, under the
[Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md).
It does not declare Domain 02, or the overall R1.x/R2.x architecture baseline,
complete or frozen. Explicitly unresolved traceability and canonical-corpus
work remain as recorded in sections 4, 7 and 9.

The material governing axioms are AX-03 and AX-04 for native representations
and separation of semantics from machinery; AX-05 for active/durable state
separation; AX-06 and AX-07 for source authority and governed security; AX-08
and AX-14 for meaningful evidence and preserved distinctions; AX-13 for
external publication; AX-15 for uncertain outcomes; the established AX-16 for
coordinated activity/entity-state progression; and AX-17 for architectural
authority and explicit uncertainty. The approved changes preserve those
boundaries. AX-17 was not modified.

Inputs included the applicable repository instructions, current Domain 01
Motivation, all current Domain 02 files, relevant Domain 03 metamodel and
assurance boundaries, the central axiom register, the deferred register and
the [Step 1 assessment](2026-10-09-domain02-strategy-cleanup-assessment.md).
The assessment and other Junie records were used as evidence, not authority.
Deferred Register Item 04 was not used to establish current metamodel rules
or identifiers. Implementation code was not used as architectural authority.

Documentation validation passed. The normal Maven invocation failed before
architecture-test execution because the dependency cache was read-only. The
authorised offline invocation passed **90 architecture tests across 11
suites**, with zero failures, errors or skips. Environment warnings and their
verification limits are recorded separately in section 8.

## 2. Files Changed

Paths in the finding register are relative to `docs/markdown/02-strategy`
unless explicitly qualified. The links below identify each complete path.

| Changed file | Reason |
| :--- | :--- |
| [Domain 02 README](../../docs/markdown/02-strategy/README.md) | Reusable-capability terminology, present responsibility-model reference, six-components-plus-construct description and qualified Feature baseline. |
| [capabilities/business-capabilities.md](../../docs/markdown/02-strategy/capabilities/business-capabilities.md) | Corrected the generic reusable ICT label; preserved all 18 Business Capabilities. |
| [capabilities/business-enabling-capabilities.md](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) | Qualified Feature completeness, repaired a border, corrected “Instantiate”, and reconciled contract-specific technical acknowledgement semantics. |
| [capabilities/enterprise-capabilities.md](../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md) | Bounded reusable responsibility, significant provenance/evidence, technology-neutral EC-10 and references to the existing responsibility model. |
| [capabilities/ict-foundation-lenses.md](../../docs/markdown/02-strategy/capabilities/ict-foundation-lenses.md) | Generic EC-01 through EC-14 range, explicit absence of EC-14 lens allocation, canonical EC-13 name and border repair. |
| [capabilities/index.md](../../docs/markdown/02-strategy/capabilities/index.md) | Reconciled distributed participation versus semantic responsibility. |
| [capability-maps/capability-tier-model.md](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md) | Reuse/responsibility terminology, existing model reference and technical acknowledgement boundary. |
| [capability-maps/index.md](../../docs/markdown/02-strategy/capability-maps/index.md) | Distinguished six strategic logical components from the Digital Twin coordination construct. |
| [courses-of-action/index.md](../../docs/markdown/02-strategy/courses-of-action/index.md) | Current generic EC/axiom ranges, unchanged specific capability mappings and approved COA-06 rename. |
| [courses-of-action/strategic-courses-of-action.md](../../docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md) | Approved native-model, evidence, technology-neutral responsibility and COA-06 corrections; canonical EC names. |
| [resources/strategic-resources.md](../../docs/markdown/02-strategy/resources/strategic-resources.md) | Re-derived current constraint relationships and separated the Australian resource baseline from platform jurisdiction. |
| [strategic-views/logical-component-responsibilities.md](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md) | Explicit EC-14 non-allocation, bounded reuse, Ponos/EC-12 and Iris authority boundaries, canonical EC names and diagram repairs. |
| [strategic-views/strategic-value-streams.md](../../docs/markdown/02-strategy/strategic-views/strategic-value-streams.md) | Retired superseded traceability, preserved valid contributions and explicit gaps, current axiom-register summary and demand-driven Digital Twin wording. |
| [docs/architectural-axioms.md](../../docs/architectural-axioms.md) | Sole authorised authority-register exception: added the established AX-16 definition. |
| [This completion report](2026-10-09-domain02-strategy-cleanup-step2.md) | Finding coverage, derivation record, preservation checks, canonical assessment, validation and residual issues. |

Pre-existing changes to `docs/README.md`, the Step 1 assessment, the
Architecture Completion Plan and its separate Junie report were left intact
and are not changes made by this task. No source, POM, test or implementation
configuration file was modified.

## 3. Finding Disposition Register

Each finding occurs exactly once in this register. “Preserved” identifies
the material checked even where no change was necessary. Reconciliation of
an obsolete assertion is complete when the assertion is retired and the
unestablished relationship remains explicit; it does not imply the gap has
been architecturally filled.

| Finding | Disposition | File(s) Changed | Validation / Notes |
| :--- | :--- | :--- | :--- |
| A001 | Implemented — generic EC range | `capabilities/ict-foundation-lenses.md` | Generic references now EC-01 through EC-14. Explicitly establishes no EC-14 ICT-lens relationship; existing examples preserved. |
| A002 | Implemented — generic COA eligibility | `courses-of-action/index.md`; `courses-of-action/strategic-courses-of-action.md` | Generic catalogue range now EC-01 through EC-14. Existing six COA mappings remain bounded to established EC-01 through EC-13 contributions; EC-14 relationships unresolved. |
| A003 | Implemented — responsibility-model standing | `strategic-views/logical-component-responsibilities.md` | Acknowledges EC-14 Service Guardian and unresolved component allocation. Existing compositions, six components and seams preserved. |
| A004 | Implemented — generic axiom summaries | `courses-of-action/index.md`; `strategic-views/strategic-value-streams.md` | Generic references use the current register including AX-17; no axiom semantics changed or new stakeholder motivation assigned. |
| A005 | Implemented — canonical EC names | `strategic-views/logical-component-responsibilities.md`; `courses-of-action/strategic-courses-of-action.md`; `capabilities/ict-foundation-lenses.md` | EC-03 Managed State & Lifecycle; EC-04 Information Management; EC-09 Event & Subscription; EC-10 Activity & Execution; EC-13 Semantic Governance & Conformance. Identifiers and composition memberships preserved. Superseded stream meanings handled through re-derivation rather than name substitution. |
| A006 | Implemented — present model references | `README.md`; `capabilities/enterprise-capabilities.md`; `capability-maps/capability-tier-model.md` | Active “Pass B” future references replaced with the existing responsibility model. Historical quotations preserved; no closure/freeze claim added. |
| A007 | Implemented — component count distinction | `README.md`; `capability-maps/index.md`; `strategic-views/logical-component-responsibilities.md` | Six strategic logical components and the Digital Twin coordination construct; all seven responsibility profiles preserved. |
| A008 | Implemented — presentation repair | `capabilities/business-enabling-capabilities.md`; `capabilities/ict-foundation-lenses.md`; `strategic-views/logical-component-responsibilities.md` | Three damaged horizontal borders and identified missing box edges repaired. Unicode/column checks pass; labels, relationships and connectors preserved, including the unchanged Mermaid responsibility view. |
| A009 | Implemented — spelling | `capabilities/business-enabling-capabilities.md` | FEAT-HSO-19 description now says “Instantiate”; identifier, name and responsibility unchanged. |
| A010 | Implemented — contextual reusable labels | `README.md`; `capabilities/business-capabilities.md`; `capabilities/business-enabling-capabilities.md`; `capability-maps/capability-tier-model.md` | Generic reuse labels corrected in context. ICT engineering-lens terminology, concern/responsibility distinction and historical quotations retained. |
| B011 | Implemented — bounded reusable responsibility | `capabilities/enterprise-capabilities.md`; `capabilities/index.md`; `capability-maps/capability-tier-model.md`; `strategic-views/logical-component-responsibilities.md`; `courses-of-action/strategic-courses-of-action.md` | Distributed participation does not imply distributed responsibility; cross-cutting concern does not imply cross-cutting responsibility. No capability allocated to fill a diagram. |
| B012 | Implemented — approved COA-06 rename | `courses-of-action/index.md`; `courses-of-action/strategic-courses-of-action.md`; `strategic-views/strategic-value-streams.md` | “Collaborative Reusable Capability Realisation” propagated to active references. COA-06 retains EC-12 Operational Assurance; no independent Governed Assurance/EC-14 relationship inferred. |
| B013 | Implemented — technology-neutral Strategy | `capabilities/enterprise-capabilities.md`; `courses-of-action/strategic-courses-of-action.md` | EC-10 assigns/dispatches work to eligible performers without normative daemons/queues. Pragma, Petasos, themis-api and petasos-api remain explicitly illustrative downstream traceability; no API, package, framework or deployment choice mandated. |
| B014 | Implemented — native standards representations | `courses-of-action/strategic-courses-of-action.md` | Native external/standards representations permitted; private management, execution, governance and coordination semantics remain Harmonia-owned. No parallel model or Calliope/Mneme redesign. |
| B015 | Implemented — significant provenance/evidence | `capabilities/enterprise-capabilities.md`; `courses-of-action/strategic-courses-of-action.md` | Required provenance, audit and evidence preserved according to obligations/significance. Routine technical activity does not automatically require durable evidence. Telemetry, operational facts, provenance, audit and assurance evidence remain distinct; explicit security requirements retained. |
| B016 | Implemented — Iris/user-input authority | `strategic-views/logical-component-responsibilities.md` | Iris captures authoritative human assertions through governed interfaces; authority derives from actor, process, source or applicable responsibility, not Iris. Presentation/database and activity-execution boundaries retained. |
| B017 | Implemented — bounded Feature baseline | `capabilities/business-enabling-capabilities.md`; `README.md` | Existing 137-Feature R1.x/R2.x baseline qualified to presently decomposed capabilities/views. No BC-18/assurance Feature decomposition; all Feature identifiers/names/order preserved. |
| B018 | Implemented — truthful stream traceability | `strategic-views/strategic-value-streams.md` | Contributions should be traceable where established. Absence does not invalidate authoritative capabilities; no BC-18, EC-14 or other membership inferred. |
| B019 | Implemented — Ponos assurance boundary | `strategic-views/logical-component-responsibilities.md` | Ponos consumes EC-12 for dependable activity progression/monitoring/recovery. No independent Governed Assurance, adjudication, Findings/Conclusions or EC-14 authority. Management Monitoring ownership remains a Business Architecture determination. |
| B020 | Implemented — AX-16 central-register restoration | `docs/architectural-axioms.md` | One substantive Domain 01 definition found; Principle, Implications and Realisation copied verbatim under the approved title. Source untouched; AX-17 and subsequent central-register text unchanged. See section 5. |
| B021 | Implemented — contract-specific technical ACK | `capabilities/business-enabling-capabilities.md`; `capability-maps/capability-tier-model.md` | HTTP 200/202, MLLP and other acknowledgements establish only contract-defined acceptance. Stronger semantics require an explicit contract; eventual outcome not assumed. REQ-FND-001 durable ingress obligation preserved. |
| C022 | Reconciled — VS-01 superseded BC block retired | `strategic-views/strategic-value-streams.md` | Current BC/BEC/view relationships explicitly unresolved. Purpose, stages and outcomes preserved; no old-to-current crosswalk. Partial valid EC contributions retained as described in section 4. |
| C023 | Reconciled — VS-02 superseded BC block retired | `strategic-views/strategic-value-streams.md` | Current BC/BEC/view relationships explicitly unresolved. Purpose, stages and outcomes preserved; valid existing AX-05/AX-07 and partial EC contributions retained. |
| C024 | Reconciled — VS-03 superseded BC block retired | `strategic-views/strategic-value-streams.md` | Current BC/BEC/view relationships explicitly unresolved. Purpose, stages and outcomes preserved; existing AX-15/AX-16 and EC-02/EC-12 retained without allocating EC-14. |
| C025 | Reconciled — VS-04 superseded BC block retired | `strategic-views/strategic-value-streams.md` | Current BC/BEC/view relationships explicitly unresolved. Purpose, stages and outcomes preserved; existing AX-07 and EC-02/EC-06 retained. |
| C026 | Reconciled — obsolete EC contribution meanings retired | `strategic-views/strategic-value-streams.md` | Only already-established, correctly named EC contributions survive comparison with current definitions. No replacement membership inferred from compatible fragments, narrower labels or identifier resemblance. |
| C027 | Reconciled — obsolete contextual views retired | `strategic-views/strategic-value-streams.md` | Historical view ordinals/meanings removed as current authority; current BEC/view/Feature contributions unresolved in all four streams. |
| C028 | Reconciled — superseded motivation identifiers retired | `strategic-views/strategic-value-streams.md` | Historical DRV-/GOAL- relationships removed; current named Driver/Goal relationships unresolved. Current catalogues do not establish aliases or stream membership. |
| C029 | Reconciled — incompatible axiom meanings retired | `strategic-views/strategic-value-streams.md` | Retained only unaffected, valid existing axiom relationships; replacement grounding unresolved. AX-17 governs derivation without becoming a newly assigned stakeholder motivation. |
| C030 | Reconciled — constraints re-derived | `resources/strategic-resources.md` | SR-01 boundary → CST-EXT-003; SR-02 HI facet → CST-EXT-002; privacy/regulatory candidate → CST-EXT-001 classification. SR-03 and broader directory/locator relationships unresolved. No CON-to-CST crosswalk. |
| C031 | Implemented — deployment jurisdiction boundary | `resources/strategic-resources.md` | Australian R1.x/R2.x resource instances preserved. Australia is not an intrinsic platform boundary; other obligations apply through governing context. No HIPAA/GDPR resource or obligation added to this baseline. |
| C032 | Implemented — demand-driven Twin activity | `strategic-views/strategic-value-streams.md` | “Active threads” shorthand removed from current text. Twin identity implies no dedicated/permanent thread, process or runtime engine; established execution responsibility preserved. Historical quoted wording remains historical. |
| D033 | Checked and preserved — EC-12/Ponos | No additional change; checked `capabilities/enterprise-capabilities.md` and `strategic-views/logical-component-responsibilities.md` | EC-12 definition and existing Ponos dependency/composition preserved. Boundary clarification implemented under B019; no EC-14 substitution. |
| D034 | Checked and preserved — approved assurance semantics | No additional change; checked `capabilities/business-capabilities.md`, `capabilities/business-enabling-capabilities.md`, `capabilities/enterprise-capabilities.md`, `capability-maps/health-service-assurance-derivation.md` | BC-18 and three assurance capabilities, EC-12/EC-14 distinction and approved contribution matrix preserved. Operational assurance, independent Governed Assurance, evidence, assessment, adjudication, Findings and Conclusions not collapsed. |
| D035 | Checked and preserved — state separation and seams | No additional change; checked `strategic-views/logical-component-responsibilities.md` and `courses-of-action/strategic-courses-of-action.md` | Mneme active management, Mnemosyne durable authority and Ponos activity progression preserved; six components/seams and COA-02 boundaries unchanged in substance. |
| D036 | Checked and preserved — entity-centred Twin | No additional change; checked `strategic-views/logical-component-responsibilities.md` and COA-05 in `courses-of-action/strategic-courses-of-action.md` | Composite context, candidate-Twin test and construct/component distinction preserved. No permanent execution construct or independent component introduced. |
| D037 | Checked and preserved — dated records | None; preserved `reviews/ax05-state-responsibility-reconciliation.md` and `reviews/business-capability-five-region-reconciliation.md` | Both files byte-identical to task-entry copies. Historical quotations, counts, approval standing and prior deferred consequences not rewritten as current architecture. |
| D038 | Checked and preserved — representative reuse/counts | No additional change; checked `capabilities/enterprise-capabilities.md`, `capability-maps/capability-tier-model.md`, `capabilities/business-enabling-capabilities.md` | Representative composition examples and 16 Service Delivery contexts retained; 137 Feature identifiers/names/order unchanged. No artificial decomposition or altered counts. |

## 4. Re-Derived Traceability

### Method and authoritative basis

The four existing stream blocks were reconsidered as one problem. Each
relationship was checked against current Motivation, the current 18-entry
Business Capability catalogue, the Business Enabling catalogue and current
Enterprise Capability definitions. Current
[Drivers](../../docs/markdown/01-motivation/drivers-assessments/drivers.md),
[Goals](../../docs/markdown/01-motivation/goals-outcomes/strategic-goals.md)
and the axiom register were consulted without translating historical
identifiers into their current ordinal positions.

The current catalogues establish their own elements and responsibilities.
They do not establish the missing stream-specific Business Capability or
Business Enabling contribution sets. No complete Motivation → Stream → BC →
BEC → EC chain was manufactured. The revised diagram shows the intended
derivation direction with dashed edges and explicitly disclaims completed
element-level traceability. No candidate relationship was promoted to
authority.

### Value Stream relationships retained after current-authority checks

These are valid pre-existing relationships, not replacements inferred for
retired historical assertions. Each retained EC identifier/name pair was
already present in its stream's original block and retains its current
catalogue meaning. Current definitions validate that meaning; the original
unaffected relationship supplies the stream membership. A lower-level
contribution does not fill missing BC/BEC ancestry.

| Stream | Established current relationships retained | Authoritative basis and limit |
| :--- | :--- | :--- |
| VS-01 | EC-02 Context Management; EC-07 Provenance & Traceability. Existing COA-01/02/03 relationships retained. | Existing stream purpose/stages and unaffected contribution assertions, checked against current EC definitions. Replacement stream-specific axiom set and named Driver/Goal, BC and BEC relationships remain unresolved. |
| VS-02 | AX-05 and AX-07; EC-02 Context Management, EC-06 Policy & Control, EC-07 Provenance & Traceability. Existing COA-01/02/03 relationships retained. | Existing durable-information/security grounding and unaffected EC assertions checked against current register/catalogue responsibilities. No inference of replacements for other obsolete axiom/EC meanings or intermediate levels. |
| VS-03 | AX-15 and AX-16; EC-02 Context Management and EC-12 Operational Assurance. Existing COA-04/05/06 relationships retained. | Existing uncertainty/activity-state grounding; AX-16 is also explicit in the purpose and entity-state progression stage. Current authoritative AX-16 definition and EC-12 operational boundary confirm these existing meanings. No independent assurance or EC-14 stream relationship. |
| VS-04 | AX-07; EC-02 Context Management and EC-06 Policy & Control. Existing COA-01/03/06 relationships retained. | Existing access-governance grounding and unaffected contribution assertions checked against current meanings. Other motivation and capability relationships remain unresolved. |

The COA-06 name in retained relationships reflects the explicitly authorised
rename. Other COA relationships were not newly derived by this exercise.

Historical BC contribution sets were retired in all four streams. Historical
contextual views called Ingress & Processing, Information Store & Query,
Security & Audit, Cross-Enterprise Interoperability and Operational Activity &
Workflow were retired without transferring their ordinals into the current
views. Historical DRV-/GOAL- labels and incompatible axiom meanings were
retired. Incompatible EC contribution labels, including semantic
normalisation, entity-state coordination, activity lifecycle and task-envelope
meanings at superseded identifiers, were removed rather than renamed into new
contribution claims.

For every stream, current BC and BEC/contextual-view/Feature relationships
remain explicitly unresolved. Current named Driver/Goal relationships and
replacement grounding for retired axiom meanings remain unresolved. The
retained EC subsets are partial; removed contributions have no inferred
replacement. BC-17, BC-18 and EC-14 stream relationships remain unresolved.

The approved Health Service Assurance derivation still establishes
REQ-FND-005 → BC-18 → Assurance Design / Assurance Criteria Management /
Governed Assurance → the approved collaborative EC contributions, including
EC-14. That separate established chain supplies no Value Stream, COA,
resource or component relationship.

### Strategic Resource relationships successfully re-derived

The [current Domain 01 external constraint definitions](../../docs/markdown/01-motivation/requirements-constraints/external-constraints.md)
provide explicit categorical obligations and concrete instances. These
definitions support the following limited current relationships:

| Resource or candidate | Current relationship | Authoritative basis | Unresolved limit |
| :--- | :--- | :--- | :--- |
| SR-01 — Healthcare Interoperability Standards and Specifications | CST-EXT-003 applies at the applicable external interaction boundary. | Definition explicitly covers mandated protocols, information standards and serialisation; FHIR REST/regional profiles are explicit instances. | No universal internal-model prescription, or relationship to every constraint category. |
| SR-02 — Australian National Healthcare Directory & Identifier Specifications | CST-EXT-002 applies to its HI-ecosystem facet where a deployment participates. | Explicit Australian HI Act/operating rules and IHI/HPI-I/HPI-O instances; directory/identifier interaction rules are an explicit impact. | Broader directory/locator-specific constraint relationships not established. |
| Candidate 1 — Healthcare Regulatory & Privacy Compliance Frameworks | Classified under CST-EXT-001, not admitted as a Strategic Resource. | Explicit Australian privacy, My Health Records and state health-records instances in the current category. | No new jurisdictional resource or obligation admitted. |
| SR-03 — National Clinical Terminology Assets | Resource-specific constraint relationship unresolved. | The current model does not explicitly establish a terminology-resource relationship. | No category assigned through lexical resemblance or the asset's status as a specification. |

The historical CON-01 through CON-11 assertions were retired as current
authority. References to that range now explain retirement only. No
eleven-to-three identifier correspondence was created; individual historical
relationships and unsupported current categories remain unresolved.

## 5. AX-16 Reconciliation

Source: [Domain 01 Architectural Axioms, AX-16](../../docs/markdown/01-motivation/principles/architectural-axioms.md#ax-16-operational-activity-and-entity-state-progress-together).
Only one materially substantive Domain 01 definition was found. Existing
references elsewhere were not alternative definitions.

The established Principle was used verbatim:

> **Operational activity associated with a real-world entity progresses
> through explicit and observable state transitions coordinated with the
> governed state of that entity. Operational activity is not treated merely
> as a sequence of disconnected message transfers.**

The source's Architectural Implications and Current Harmonia Realisation
bullets were also reproduced verbatim. Headings were adapted to the central
register's presentation, with the downstream realisation explicitly marked
non-normative. The approved central title is **AX-16 — Operational Activity &
Entity State Progress Together**; Domain 01 uses “and” in its source heading.
This conjunction/presentation difference is not a materially different
definition.

The entry was inserted between AX-15 and AX-17 in
`docs/architectural-axioms.md`, with a link to the Domain 01 source. It was not
reconstructed from its title, requirements, implementation behaviour or
neighbouring axioms. The source document and existing valid references were
preserved. The original absence was a register defect. No conflicting
substantive AX-16 definitions were encountered.

The central register's AX-17 section and all subsequent text are byte-identical
to the task-entry copy. No underlying axiom architecture was reopened.

## 6. Architecture Preservation Checks

| Required boundary | Result and evidence |
| :--- | :--- |
| EC-12/Ponos | Preserved in the Ponos dependency/profile and EC composition. EC-12 definition and its EC-14 distinction are unchanged. Added clarification excludes independent Governed Assurance, adjudication and Findings/Conclusions authority. |
| EC-14 non-allocation | EC-14 definition unchanged. No strategic logical/application component, Ponos, Digital Twin, COA, stream or ICT-lens allocation introduced. Existing approved assurance contributions remain intact. |
| Six components + Digital Twin construct | Six components, six seams and seven responsibility profiles retained. Twin remains entity-centred coordination across Mneme/Ponos, with no independent component, permanent thread/process or runtime engine. |
| Active/durable state separation | Mneme active management, Mnemosyne authoritative durable state/version establishment and Ponos activity progression remain distinct; no database or raw-cache application path introduced. |
| Current Feature baseline | All 137 identifiers, names and order preserved: Entity Management 31; Service Administration 29; Service Delivery 20; Health Service Operations 29; Intrinsic / Shared Enablement 28. No new Feature, BEC or BC; 18 BCs and 16 Service Delivery contexts retained. |
| Assurance semantics | BC-18, three approved enabling responsibilities, contribution matrix, EC-12 operational assurance and EC-14 assurance-specific semantics preserved. Governance, Management Monitoring and Governed Assurance remain distinct. No recursive assurance, Risk Management capability or Dokimasia allocation derived. |
| Iris authority boundary | Authoritative assertions may enter via Iris under authorised actor/process/source authority. Input capture does not confer authority on Iris; governed access/execution boundaries remain. |
| Standards/native-model boundary | Native standards representations permitted while external contracts govern external interactions and cannot define private Harmonia semantics. Non-destructive fail-closed publication and egress boundary preserved. |
| Evidence/provenance boundary | Required significant provenance, audit and evidence retained. Routine telemetry is not automatically durable assurance evidence; operational facts, attribution, audit, evidence and assurance conclusions remain distinct. |
| Traceability/uncertainty | Obsolete mappings retired; supported contributions preserved; missing intermediate/motivation/constraint relationships explicit. No identifier crosswalk, implementation-derived authority or completeness inference introduced. |
| Technical acknowledgement | Contract-defined acceptance only; no universal HTTP 200 business success or HTTP 202 durable-queue claim. REQ-FND-001 and explicit downstream acceptance obligations retained. |
| AX-16 / AX-17 | Established AX-16 source reproduced; AX-17 unchanged. No axiom semantics amended. |

## 7. Canonical Documentation Assessment

**Relevant material was found outside `/docs/markdown`.** The following
assessment records its knowledge, architectural standing, canonical owner,
incorporation outcome and future work. Discovery did not authorise migration.

| External source | Knowledge and assessment against current architecture | Canonical domain / governance owner | Incorporated by this task? | Required future treatment |
| :--- | :--- | :--- | :--- | :--- |
| [docs/architectural-axioms.md](../../docs/architectural-axioms.md) | Current authority register, including revised state/semantic/authority boundaries and AX-17. It remains governing authority under repository instructions and the Completion Plan; canonical-corpus goals do not displace it. Domain 01 is the established AX-16 source. | Domain 01 Motivation / architecture governance. | Only the expressly authorised AX-16 central-register restoration, from canonical Domain 01 outward. No broader migration or change to AX-17. | Separately reconcile the canonical Domain 01 axiom corpus with the current authority register, including AX-17 and other material definition differences. Preserve authority and explicit uncertainty during that work. |
| [docs/architecture-decisions.md](../../docs/architecture-decisions.md) | Component responsibility decisions; Kleio evidence ownership (ADR-013); durable processing/replay boundaries (ADR-014/015); significant-transition audit (ADR-016); Mnemosyne/Mneme state authority (ADR-018/019). These corroborate current strategic boundaries but do not establish new stream relationships, EC-14 allocations or Strategy implementation prescriptions. | Architecture decision governance / Domain 01; state and evidence concepts in Domain 04; application responsibilities in Domain 05; interaction/transfer boundaries in Domain 06. | No migration. Existing Strategy distinctions were reconciled through the user adjudications and current canonical responsibilities. | Semantically reconcile accepted decision knowledge into its canonical domains under a separate task. Retain historical decisions as supporting records where appropriate; assess obsolete wording against current axioms rather than importing it mechanically. |
| [docs/architecture/execution-model.md, section 6](../../docs/architecture/execution-model.md#6-runtime-ai-as-adjunct-ergo-execution-capability) | Runtime AI as adjunct execution capability; authority remains with the governing Harmonia construct; AI does not establish Twin identity, Service Guardian authority, independent workflow responsibility or recursive assurance. The section expressly does not establish implementation/topology or Dokimasia allocation. These positions are consistent with preserved responsibility and assurance boundaries. | Domain 05 Application Architecture, with applicable governance/security boundaries in Domain 08 and existing upstream assurance semantics. | No migration or allocation; checked for boundary consistency only. | Separately incorporate the valid architectural position into canonical downstream architecture. Keep illustrative mechanisms and unapproved allocation questions explicit. |
| [docs/deferred-document-register.md](../../docs/deferred-document-register.md) | Documentation-control backlog; Item 04 records deferred metamodel/identifier reconciliation. It is not the authority for current Business Architecture metamodel or identifiers in this task. | Architecture governance; eventual authorised Strategy/Business metamodel documentation where applicable. | No change or migration. | Execute its separately authorised metamodel task, resolving stated gaps against current authoritative architecture. Preserve the register as a control record where appropriate. |
| [docs/backlog/Harmonia - Task 9 - Backlog.md](<../../docs/backlog/Harmonia - Task 9 - Backlog.md>) | Implementation debt concerning Mneme authoritative-version metadata containment. Its boundary concern is already represented in AX-05/AX-13 and current state/publication responsibilities; it does not authorise additional Strategy architecture. | Implementation/developer support; Domain 04/06 only if future assessment identifies missing conceptual/publication knowledge. | No change, migration or implementation work. | Retain as implementation backlog; address under a separate implementation task. No canonical migration is required merely because this supporting backlog exists. |

Junie plans/reports, including the Step 1 assessment and dated reconciliation
records, remain execution/history evidence. They were not promoted to
canonical architectural authority. Relevant Domain 03 material was read to
validate terminology and boundaries, not changed or completed.

This assessment is scoped to material relevant to the authorised cleanup,
not a repository-wide external-document audit. Required canonical authority
consolidation remains explicit; this report does not assert that the
Completion Plan's Domain Completion Gate or final baseline gate is satisfied.

## 8. Validation

### Documentation and architecture-preservation validation

Command: `python3 /tmp/validate-domain02-step2.py` — **PASS** on the final
report-inclusive run. This task-local validator checks all current Domain 02
Markdown documents, the central register and this report against task-entry
copies and the source definitions. It is a temporary verification aid, not
new architectural authority or a repository test change.

Checks include:

- 291 active local Markdown links/anchors across 20 documents;
  no missing targets or anchors. Historical literal quotations/code examples
  are excluded from active-link interpretation. Literal repository document
  references are also checked for existence.
- Exactly 38 finding-register rows, each required finding once.
- All 137 Feature identifiers/names/order, all 18 BCs apart from the approved
  generic reuse wording, EC-12/EC-14 definitions, approved assurance matrix and
  dated review records preserved.
- Original stream stages, existing EC contribution pairs and component
  compositions checked against task-entry copies; no new stream EC
  contribution inferred.
- AX-16 source wording verified and AX-17/subsequent central text unchanged.
- Repaired Unicode/ASCII borders checked for alignment and damage; diagram
  labels/relationships retained. Mermaid changes manually checked for the
  intended dashed derivation direction and responsibility meaning. No rendered
  Mermaid validation is claimed; rendering tools are unavailable.
- Markdown fenced blocks balanced across all 20 checked documents.
- Scope audit against hashes of 2,278 tracked task-entry files: 14 changed
  tracked files, all in Domain 02 or the authorised central register. The
  separately requested report is the sole new file from this task.
- `git diff --check` — **PASS**, no whitespace errors.

Contextual occurrence review was performed with:

```bash
rg -n -i 'EC-01.*EC-13|seven components|7.component|7 logical|cross.cutting|CON-[0-9]|active threads|HTTP 200|HTTP 202|200/202|Pass B|Instatiate' docs/markdown/02-strategy --glob '*.md'
```

Remaining EC-01 through EC-13 ranges describe preserved component derivations,
specific COA mappings, existing capability contributions or dated records.
Generic catalogue/eligibility references include EC-14. Remaining
“cross-cutting” uses describe ICT engineering lenses, the explicit
concern/responsibility distinction or historical quotations; they do not make
Enterprise Capability semantic ownership inherently shared. CON identifiers
remain only in a retirement explanation. “Active threads”, old COA-06 names,
“Pass B” and obsolete acknowledgement claims remain only as clearly historical
quotations in untouched review records. Current HTTP 200/202 semantics are
contract-specific. Historical BC/view/motivation assertions no longer serve
as current stream traceability.

No applicable repository-wide Markdown/link validation command was found;
existing document-generation checks target other artefacts. The focused
static checks and contextual review above provide the documentation evidence
for this task.

### Repository architecture tests

Normal invocation:

```bash
timeout --signal=TERM --kill-after=10s 300s mvn test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

Result: **FAIL — dependency-cache/environment failure before architecture
tests**, not an executed architecture assertion failure. Maven reported
`java.nio.file.FileSystemException` while trying to write
`/home/mhunter/.m2/repository/com/sun/mail/jakarta.mail/resolver-status.properties`
on a read-only filesystem. Maven elapsed time: 1.709 seconds. The process
returned normally with failure; no timeout/stall termination was required.
Captured log: `/tmp/domain02-step2-architecture-online.log`.

Authorised retry, using cached dependencies without metadata writes:

```bash
timeout --signal=TERM --kill-after=10s 300s mvn -o test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

Result: **PASS — BUILD SUCCESS**, 90 tests in 11 suites, zero failures, errors
or skips. Maven elapsed time: 25.565 seconds. No timeout or stall occurred.
Captured log: `/tmp/domain02-step2-architecture-offline.log`; suite results
were cross-checked with Surefire XML reports.

| Architecture suite | Tests | Result |
| :--- | ---: | :--- |
| PylaiPublicationBoundaryArchitectureTest | 8 | PASS |
| ProviderRegistryArchitectureTest | 2 | PASS |
| SecurityEnforcementArchitectureTest | 32 | PASS |
| PetasosApiIsolationArchitectureTest | 3 | PASS |
| GovernedWriteCompositionArchitectureTest | 7 | PASS |
| GovernedWriteContractArchitectureTest | 12 | PASS |
| AgoraIsolationArchitectureTest | 7 | PASS |
| ParadeigmaIsolationArchitectureTest | 4 | PASS |
| PackageLayeringArchitectureTest | 4 | PASS |
| IrisDecouplingArchitectureTest | 5 | PASS |
| MnemosyneAuthoritativePersistenceArchitectureTest | 6 | PASS |

### Warnings, separate from failures

The normal run warned that it could not write the dependency tracking file,
then failed as described above. Both runs emitted JDK deprecation warnings
for `sun.misc.Unsafe::objectFieldOffset` in Maven/Guava; the test runtime also
reported the ArchUnit-shaded Guava use.

The passing offline run emitted 3,077 ArchUnit WARN entries while importing
JDK classes, each falling back to simple import. Diagnostics identify
`IllegalArgumentException: Unsupported class file major version 69`
(JDK 25 bytecode) in ArchUnit 1.3.0's shaded ASM importer. All reported tests
completed successfully, but full JDK-class import was unavailable; the pass
does not prove analysis that requires those unimported JDK internals. A
separate harness-maintenance task may use a compatible JDK or update the
importer and rerun. Neither implementation nor test configuration was changed
to suppress these warnings. The architecture suite checks code invariants;
the documentation-specific preservation evidence is supplied by the checks
and review above.

## 9. Residual Issues

1. Stream-specific current Business Capability, Business Enabling Capability,
   contextual-view and Feature contribution relationships remain
   unestablished. Current named Driver/Goal relationships and replacement
   grounding for retired axiom meanings also remain unresolved; retained EC
   subsets do not fill these gaps. BC-17, BC-18 and EC-14 stream relationships
   remain unresolved. These require explicit future architectural decisions
   or further authoritative evidence, not identifier translation.
2. SR-03's resource-specific constraint relationship and SR-02's broader
   directory/locator-specific relationships remain unestablished. Historical
   CON relationships do not supply missing current categories.
3. EC-14 strategic logical/application component, ICT-lens and specific COA
   relationships remain unresolved. No allocation follows from its approved
   assurance contribution model or existing EC-12 references.
4. Existing capability-tier/ancestry and BC-17 enablement documentation gaps,
   plus deferred Item 04 metamodel/identifier work, remain outside this cleanup.
   BC-18's established assurance enabling derivation does not establish
   unapproved Feature decomposition.
5. Canonical axiom/ADR and downstream execution-position consolidation remains
   future authorised documentation work, as detailed in section 7. The
   central AX-16 defect is resolved; broader canonical consolidation was not
   undertaken.

These are genuine remaining architectural/documentation gaps. No new
unadjudicated conflict requiring suspension of an approved correction was
encountered. Environment and verification warnings are recorded in section 8
and do not justify architectural changes.

## 10. Scope Confirmation

- Domain 03 completion work was not begun; its material was consulted only to
  validate current terminology and boundaries.
- Domain 04 was not reconciled; Domain 05 was not created or completed.
- Unrelated `/docs` content was not migrated. B020 was the sole central-register
  exception and used the established canonical Domain 01 AX-16 source.
- Implementation code, build definitions and tests were not modified.
- No EC-14 component or other unestablished allocation was invented; EC-12 was
  not substituted with EC-14.
- No historical identifier crosswalk, BC/BEC/Feature creation, artificial
  decomposition, new component/seam, Risk Management capability or Dokimasia
  allocation was introduced.
- No recursion of assurance, redesign of Ponos/Mneme/Mnemosyne or change to
  Service Guardian's Enterprise Capability definition was made.
- Domain 02 and the overall R1.x/R2.x architecture were not declared complete
  or frozen. Work stops at this Step 2 report.
