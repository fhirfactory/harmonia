<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 boundary reconciliation and A2/A3 closure assessment

**Date:** 2026-10-12. **Scope:** Domain05 Step A, three expressly adjudicated
boundary corrections followed by read-only Candidate Architecture assessment.
This report is execution/assessment evidence, not architectural authority.

The three authorised conflicts are reconciled. The assessment identifies four
remaining Candidate Architecture responsibility questions: Agora roster authority,
Agora managed-state custody, collaboration-to-formal-record handoff, and binary/media
custody. **A2 is INCOMPLETE; recommendation: NOT READY FOR A4 ADJUDICATION.**
These questions were not resolved or implemented. A4 remains Mark + ChatGPT's
decision; Step B has not started.

## Section A — Adjudication Reconciliation

### Authority, reviewed files and scope

Applied repository [AGENTS.md](../../AGENTS.md),
[documentation instructions](../../docs/AGENTS.md), canonical
[Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md)
and the [Step-A completion method](../../docs/markdown/architecture-completion-plan.md#33-domain05-five-stage-completion-method).
The explicit task supplies the three new decisions, recorded as
[U15](../../docs/markdown/05-information-systems-architecture/sources.md#u15).
Earlier reports and temporary validation scripts supplied navigation/methods,
not architecture authority. No convergence/runtime implementation step was undertaken.

Reviewed the complete twelve-file Domain05 corpus:

- [README.md](../../docs/markdown/05-information-systems-architecture/README.md),
  [candidate-system-map.md](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md),
  [component-register.md](../../docs/markdown/05-information-systems-architecture/component-register.md),
  [candidate-feature-map.md](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md).
- [calliope-models-and-datasets.md](../../docs/markdown/05-information-systems-architecture/calliope-models-and-datasets.md),
  [pylai-external-interoperability.md](../../docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md),
  [pathways-journeys-and-handoff.md](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md).
- [context-and-persistence-management.md](../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md),
  [data-and-persistence.md](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md),
  [middleware.md](../../docs/markdown/05-information-systems-architecture/middleware.md),
  [gaps.md](../../docs/markdown/05-information-systems-architecture/gaps.md),
  [sources.md](../../docs/markdown/05-information-systems-architecture/sources.md).

Also reviewed Strategy's [logical responsibility profiles, guardrails, seams and maps](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md).
Followed relevant legacy references to inspect existing section wrappers and
historical evidence: Calliope [concept](../../docs/concepts/calliope.md),
[canonical account](../../docs/concepts/calliope-canonical.md) and
[module reference](../../docs/modules/calliope.md); Pylai
[concept](../../docs/concepts/pylai.md) and wrapper references in its gateway,
module, integration and security guides; Themis/audit wrapper references, including
[decision auditing](../../docs/security/audit.md); legacy
[terminology](../../docs/getting-started/terminology.md) and LaTeX glossary/publication
replacement links. Targeted [Agora source passages](../../docs/concepts/agora.md)
corroborate the already-recorded CDG-21–23 questions; they were not adopted as authority.

No fresh capability, Information Concept or requirements coverage assessment was
performed. Domain04 relationships cited by the candidate were used as constraints,
not as a Step-B mapping exercise. External DesignNotes were not reread or altered;
their incorporated decisions are available in RADS.

### Files changed

All twelve Domain05 files listed above changed. Other changes are confined to:

| File | Change |
| :--- | :--- |
| [AGENTS.md §2](../../AGENTS.md#2-system-taxonomy--module-hierarchy) | Qualifies the Calliope table's pure-library constraints as Models constraints; records complementary runtime DataSets responsibility and existing persistence boundary. |
| [Strategy logical responsibilities](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md) | G4, Pylai profile/summary, Calliope profile/summary/Seam 5, Calliope Mermaid label and explanatory text reconciled. |
| [Canonical axioms, AX-07](../../docs/markdown/governance/architectural-axioms.md#kleio-mandatory-capture-authority) | Adds the specifically adjudicated Kleio mandatory-capture authority clarification. No new axiom or generic component-authority mechanism. |
| This report | Reconciliation evidence, remaining issue classifications and assessment conclusions. |

No source code, POM, test, deployment configuration, original legacy body or
existing legacy wrapper changed. The architecture-completion plan's programme
statuses were not advanced by this assessment.

### Strategy / Terma reconciliation

[Terma](../../docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md#terma-and-diadromi)
owns the mechanics needed to establish, conduct and complete an external
interaction: connections/sessions, listening/polling, transport/protocol interaction,
framing, segmentation/reassembly, protocol acknowledgements, API/RMI/file interaction,
communication-local retry and external identity carriage.

Application meaning remains with the applicable Harmonia execution framework,
principally Praxis/Ergo/Ergon, except where an explicitly defined interaction
pattern provides otherwise. The already-established Server READ/SEARCH pattern
remains; it grants Terma no business interpretation responsibility. Strategy's
blanket exclusions of connection/framing/retry mechanics were replaced with
the adjudicated communication/application distinction. Underlying technology
supplies machinery; generic infrastructure/network management remains separate.

The [retry/loss view](../../docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md#diadromi-recoverability)
now explicitly shows active interaction → active Diadromí → communication retry
within that interaction. Termination/loss means **NO RESUMPTION**; a later client
retry creates a new interaction and Diadromí. Terma, Diadromí and Controller supply
no durable application recovery. Petasos and established execution retain durable
messaging/recovery; unknown earlier effects remain unknown under AX-15.

### Calliope realisation reconciliation

The [realisation boundary](../../docs/markdown/05-information-systems-architecture/calliope-models-and-datasets.md#calliope-realisation)
now explicitly distinguishes Models' development/build-time model/library
dependencies from DataSets' runtime governed content management and authoritative
access. Concept/structural contract belongs to Models; current governed
membership/content belongs to DataSets where runtime resolution is required.
Type-safe development and normal IDE/AI assistance remain supported.

Models' library/dependency semantics remain. Repository §2 no longer constrains
all logical Calliope responsibility to a pure library. Strategy's local evaluation
and non-mediation statements now preserve both Models' library semantics and
DataSets' current-content authority. Build-time representation and runtime authority
are complementary. DataSets uses Mneme/Mnemosyne; Calliope acquires no persistence
manager, workflow engine, new physical allocation or static dependency permission.

### Kleio / Themis reconciliation

[Kleio mandatory capture](../../docs/markdown/05-information-systems-architecture/component-register.md#kleio-capture-authority)
uses intrinsic platform authority associated specifically with its established
authoritative audit responsibility. Capture executes within established security
context and remains subject to Harmonia security policy; Themis retains policy
evaluation/enforcement semantics. It is not a security bypass or policy exception.

The audited Identity remains evidence and is not the authority persisting the
mandatory AuditEvent. Kleio does not borrow that Identity's PEP-Criteria merely
to record its activity, including denied activity. Required capture fails the
governed operation closed if Kleio cannot sufficiently exercise its capture
authority. Durable target availability and existing essential-capture rules remain.

AX-07, component descriptions, collaboration arrows, middleware, data and lifecycle
views now express that distinction. Generic persistence-enforcement wording was
qualified so it does not reintroduce originating-Identity authorisation for
mandatory capture. Authority representation and mechanisms remain deferred.
No other component, or other Kleio activity, receives equivalent authority.
No concrete additional intrinsic-authority requirement was established in this review;
any such proposal needs separate adjudication. Audit, assurance, governance and
authorisation remain distinct; Dokimasia terminal constraints are preserved.

### Axioms, registers, maps and terminology

| Authority / record | Result |
| :--- | :--- |
| AX-04 | Machinery does not determine semantic ownership; no new technology/dependency allocation. |
| AX-05 | DataSets and Kleio retain application responsibilities; Mneme active access and Mnemosyne durable establishment remain distinct. |
| AX-06 | Runtime content authority and audit subject/authority remain explicit; no inferred source/clinical authority. |
| AX-07 | **Text changed:** Kleio-specific mandatory capture authority remains governed by security policy and fails closed when insufficient. |
| AX-08/09/10 | Meaningful audit remains distinct from diagnostics; Diadromí remains transient; durable messaging/execution recovery remains with established architecture. |
| AX-12/13 | Models supports development; external publication remains non-destructive and fail-closed, with management ending at egress. |
| AX-14/15/17/18 | Distinctions, uncertainty and deferred decisions preserved; no new authority inferred from participation. |
| CDG-51 | **CLOSED:** Strategy/Terma communication-mechanics conflict. |
| CDG-49 | **Realisation/mediation conflict CLOSED:** ontology, dataset lifecycle/version/history, APIs, workflow/UI, packaging and other detail retained. |
| CDG-42 | **Audit-authorisation conflict CLOSED:** all twelve Identity deferred-design items and other Themis mechanisms retained. |
| CDG-43/47/50 | Capture, persistence and external-interaction mechanisms remain deferred; references to the three former conflicts updated. |

The system hierarchy now names Models library dependencies, Terma's stack/retry
boundary and Kleio capture authority. Its collaboration Mermaid labels distinguish
Kleio mandatory capture authority from the Identity recorded as evidence and show
security policy governing capture. Strategy's Calliope Mermaid label reflects
Models and DataSets. Existing specialisation and Journey diagrams retain their
meaning. Text diagrams cover Calliope realisation, application interpretation and
active retry versus interaction loss. Existing ingress/egress ordering is unchanged.

The README includes a short boundary terminology table. Registers C01/C02/C03,
C12/C14/C41–44 and affected feature/data/middleware entries agree with the detailed
views. U15 records explicit task provenance. The old audit-conflict heading's
fragment was preserved as an alias to its resolved entry so earlier reports keep
working. CDG identifiers were retained; no broader gap was deleted.

### Legacy governance

Existing Calliope, Pylai and Themis/audit section wrappers already point to the
canonical component, detailed view and gap anchors now updated by U15. Their
replacement references therefore continue to encapsulate the reconciled meaning.
No newly imported legacy section requires another wrapper: the new authority is
the explicit user adjudication, not incorporation of additional legacy design.

All **43 tracked legacy Markdown/LaTeX files containing supersession markers**,
including **128 wrappers**, were compared byte-for-byte/SHA-256 against HEAD and
are unchanged. Wrapper boundaries balance; **36 LaTeX RADS replacement links**
resolve. Original contradictory historical content, existing wrappers and diagram
bodies are preserved. No whole legacy document was marked superseded.

### Validation actually executed

| Check | Result and limits |
| :--- | :--- |
| Required architecture suite | **PASS:** 90 tests, 0 failures, 0 errors, 0 skipped; BUILD SUCCESS, Maven elapsed **18.009 s**, exit 0. Eleven architecture test classes ran, including the eight named in AGENTS.md. |
| Local links | **PASS:** changed Markdown plus the full Domain05 corpus, including this report; zero missing local files/fragments. Final counts are recorded below. External network links were not fetched. |
| Markdown structure | **PASS:** CommonMark/table parsing, closed fenced blocks, unique explicit anchors and consistent source table widths. Final counts below. |
| Diagram/reference validation | **PASS for references and manual source review:** six Mermaid blocks checked; the two modified Mermaid views and modified text diagrams agree with the adjudications. Mermaid CLI/module unavailable locally; no rendered Mermaid validation is claimed. |
| Legacy preservation | **PASS:** 43 files byte-identical to HEAD; 128 balanced wrappers; 36 LaTeX replacement references resolve. No publication rendering was necessary for unchanged legacy content. |
| Deferred items / scope | **PASS:** all 47 gap rows outside the four directly affected rows (42/47/49/51) are unchanged; the twelve Identity deferrals and AI-discovery list are unchanged. Production tests were not edited. |
| Diff hygiene | **PASS:** `git diff --check`. |

Architecture command:

```bash
timeout --signal=TERM --kill-after=10s 180s mvn -B -ntp test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

No timeout or stall occurred. Architecture tests verify existing implementation
boundaries; they do not prove implementation of these Candidate decisions.
The established documentation-validation approach was reused with installed
`markdown-it-py`/BeautifulSoup, local target/fragment resolution, source structure
checks and exact preservation comparisons. No committed documentation-validation
runner was found; temporary scripts were kept outside the repository.
Raw evidence: `/tmp/harmonia-domain05-boundary-architecture-tests.log`,
`/tmp/harmonia-domain05-boundary-validation.json` and
`/tmp/harmonia-domain05-boundary-validate.py`.

Final documentation counts: **16 Markdown files, 546 local link occurrences,
48 tables, 56 fenced blocks and six Mermaid blocks**; zero broken local targets
or fragments, malformed tables, duplicate explicit anchors or unclosed fences.

## Section B — A2/A3 Closure Assessment

### Assessment method and interpretation

Read-only review compared current responsibility, component, feature, data,
middleware, lifecycle, terminology, source-disposition and gap views. Every
CDG-01–51 entry was considered. The table groups closely related detailed items;
each CDG is accounted for. Closed CDG-51 and the closed portions of CDG-42/49
are reported in Section A, not retained as remaining issues.

A retained historical contradiction is not automatically a current RADS
contradiction. Current axioms/definitions often explicitly reject it. Likewise,
an unresolved schema, algorithm, API, topology, release inventory or deliberately
proposed feature is not automatically a Candidate Architecture blocker.
The completion plan permits explicit unresolved candidate details where they do
not prevent meaningful later analysis.

The four blockers below concern missing responsibility boundaries in features
currently represented in the candidate, not completeness of implementation or
upstream coverage. They may be resolved by clarifying the intended candidate
boundary or explicitly establishing that the feature is a bounded deferred
proposal; this report makes neither decision. Missing technical mechanisms alone
would not make them blockers. No newly discovered substantive issue was repaired.

### Remaining material issues

| Issue | Classification | Affected Architecture | Why It Matters | Requires Adjudication? |
| :--- | :--- | :--- | :--- | :--- |
| **CA-01 — Authoritative roster/source for current Agora membership reconciliation (CDG-21)** | **Candidate Architecture Blocker** | [C18](../../docs/markdown/05-information-systems-architecture/component-register.md#c18), [C13](../../docs/markdown/05-information-systems-architecture/component-register.md#c13), F22/D14/M08; [gap register](../../docs/markdown/05-information-systems-architecture/gaps.md) | C18/F22 retain roster-driven membership reconciliation; CDG-21 says the Provider Registry directory/Group model does not establish roster ownership or the governing membership source. This leaves the authority supplying a defining collaboration operation unestablished. Future PractitionerCapacity notification mechanics are a separate explicit deferral. | **Yes — Mark + ChatGPT:** establish the bounded source/responsibility or explicitly defer that candidate behaviour. No roster owner inferred. |
| **CA-02 — Agora managed mappings/transactions versus homeserver discourse custody (CDG-22)** | **Candidate Architecture Blocker** | C18, [D14 and historical storage variants](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md#historical-candidate-persistence-structures), F23–24/M08 | C18 says persistence ownership and Mnemosyne collaboration remain unclear; D14 says authority/storage allocation is unresolved. Mnemosyne durability, Agora Core JPA and homeserver ownership survive as distinct descriptions without a consistent managed-information/external-store boundary. The question is what each component manages, not which database/schema is used. | **Yes — Mark + ChatGPT.** AX-05 remains controlling; no direct-JPA architecture or new store owner is selected. |
| **CA-03 — Collaboration output to formal managed record before discourse disposal (CDG-23)** | **Candidate Architecture Blocker** | C18/F24, D14 and [CDG-23](../../docs/markdown/05-information-systems-architecture/gaps.md) | F24 retains candidate clinical submission/formal-record retention, while CDG-23 says the providing/submission contract and authority/retention responsibility are insufficiently defined. A discourse surface and a durable clinical record are distinct; no component responsibility is established for that transition. Detailed optional room projections are classified separately below. | **Yes — Mark + ChatGPT:** clarify the current candidate transition/ownership or expressly defer the feature. No clinical-work authority or submission workflow invented. |
| **CA-04 — Binary/media content custodian (CDG-25)** | **Candidate Architecture Blocker** | F31, [D02/D15](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md#coupled-logicalapplication-data-objects), system-map unresolved area | The candidate carries clinical binary content and DocumentReference metadata, but explicitly has no established payload/media custodian. Metadata persistence does not identify who manages content, retrieval/link integrity and lifecycle. This is a missing responsibility, distinct from a deferred object-store product or binary schema. | **Yes — Mark + ChatGPT**, potentially preceded by documentary recovery. No Media Server or blob architecture created. |
| Praxis lifecycle, version binding, in-flight changes, activity relationships and finer Energeia decomposition (CDG-01/06/39) | Deferred Design / Discovery | C06/C24/C39, F11–12, D06/20, Praxis/Journey view | Higher-order envelope, Energeia ownership, Pathway Aggregation and assurance roles are established. Remaining lifecycle/cardinality/service-detail questions do not erase those responsibilities. | Later bounded design/adjudication where necessary; **not an A4 prerequisite solely for missing detail**. |
| Pragma/payload/carrier/factory and fan-out representation (CDG-02/03/04/37) | Deferred Design / Discovery | C07/C08/C28, D01–02/05/11/25, fan-out view | Work, distinct consumer undertakings, Mneme management and by-value Parcel/reference handoff are established. Schema, copy/version binding, identity/morph mechanics and concurrency remain deliberately open. | Later bounded contracts; no universal state machine or copy model inferred. |
| General converter support, bounded Ergon behavioural/composition contracts (CDG-05) | Deferred Design / Discovery | C01/C05/C23/C24, F02/07–10 | Current support versus executed BusinessLogic roles are stated; historical pure/atomic/idempotent/branch claims are not adopted guarantees. Atomicity, side effects and detailed composition need later definition. | Later adjudication for detailed guarantees; no current competing execution owner established. |
| DigitalTwin scheduling, activation and specialised information/state detail (CDG-07) | Deferred Design / Discovery | C19/C25, Stimulus/Effector, M11 | Ponos/Ergon reuse, entity boundary and bounded pending-work resources are established. No permanent actor/thread, schema or scheduling algorithm is required for faithful capture. | Later design; settled boundary retained. |
| Independent assurance, specialised Pragma and explicit AI activity detail (CDG-08/09/35) | Deferred Design / Discovery | C20/C26/C27, D17/19, M12 | Independence, base-contract reuse, Praxis/Journey roles and terminal rules are established. AI discovery and evidence/control details remain explicitly required before implementation modelling. | Later discovery/adjudication; component existence is not reopened. |
| Handoff continuation, acknowledgement, execution retry, replay, duplicate/partial effects and destination observations (CDG-16/17/18/19/34/44) | Deferred Design / Discovery | Petasos/Ponos/Mneme/Pylai, F10/13–14, D10–11 | Responsibility and state distinctions, durable acceptance and AX-15 uncertainty are explicit. Missing exact delivery/transaction/recovery mechanisms do not make Terma or cache state authoritative. | Later bounded contracts; no exactly-once or safe-retry algorithm inferred. |
| Calliope ontology scope, dataset lifecycle/history and proposed terminology/authoring services (CDG-24/49) | Deferred Design / Discovery | C01/C41/C42, F25/45–46, D16 | Models/DataSets and persistence boundaries are settled. Exact ontology scope, inventories, workflow/UI and detailed proposed services are expressly deferred; no wholesale clinical-service allocation is asserted. | Later discovery/adjudication; the closed realisation conflict is not reopened. |
| Journey construction/evidence/reference and operational reporting/projection contracts (CDG-28/38) | Deferred Design / Discovery | C35/C39, F27/38–39, D21, M09/15 | Owner, observed-versus-inferred distinction and Observe → Report scope are consistent. Detailed reconstruction algorithms, retention, freshness and consumption remain open. | Later bounded design; missing observation is not proof of non-occurrence. |
| Identity/Themis detailed contracts (CDG-30/42) | Deferred Design / Discovery | C02, PAP/PRP/PDP/PEP, all twelve Identity deferrals | Fixed historical gate counts do not override distributed action-hosted enforcement. Effective-attribute derivation, revocation/invalidation, representations, temporal rules, Capacity mapping/UI and forwarding mechanisms remain explicit deferrals. | Later design/discovery, including semantic derivation ownership; no generic platform authority inferred. |
| Context/access/search-result representation and concurrency detail (CDG-31/45) | Deferred Design / Discovery | C09/C11, D04/26–28, four Mneme services | Four-service surface, composed temporal values, distinct Context/resource versions and bounded Adhoc use are established. Detailed Information Unit mapping, tokens, schemas and coordination remain open. | Later design/adjudication; no extra service or raw-cache path inferred. |
| External API/status, protocol ACK, token, Horos and handover mechanics (CDG-32/50) | Deferred Design / Discovery | PylaiPathway/Broker/Server, C43–46 | Current patterns, acceptance condition, non-recoverability, meaning/mechanics split and egress return-before-unloading are explicit. Protocol-specific/API mechanics remain downstream. | Later bounded design; no fabricated READ/SEARCH work or durable interaction recovery. |
| Pattern language, selection-definition lifecycle and evaluation/error/disclosure detail (CDG-36) | Deferred Design / Discovery | Mneme Publishing/Subscription/Pattern, M13 | Provider eligibility and consumer selection are distinct and owned by Mneme; Petasos destination mechanics remain separate. Operators, precedence and implementation need not be selected now. | Later contracts; no semantic owner missing. |
| Adaptive Load and Resilience R3 direction (CDG-41) | Deferred Design / Discovery | Energeia F40 | Deliberately outside R1.x/R2.x Observe → Report. Absence of control loops or adaptation policy is intentional. | Future R3 adjudication only. |
| Audit policy/capture/history, representation, targets and coordination mechanisms (CDG-14/43) | Deferred Design / Discovery | Kleio C03, D12, M07/16 | Meaningful capture, immutable AuditEvent, source-history rules, essential durability and U15 authority are settled. Detailed significance policies, APIs, preservation, retention, target selection and authority/enforcement mechanics remain open. Cross-target harmonisation is SEP. | Later bounded design; audit cannot become best-effort. |
| Exceptional resource harmonisation contracts (CDG-46) | Deferred Design / Discovery | Internal Mneme C40 and new Harmonisation Ergon work | Manager versus domain behaviour and common execution reuse are settled. Categories, algorithms, catalogue and pathological repetition/escalation remain deferred. | Later design; no resumed originating Ergon or duplicate framework. |
| Persistence oversight, effective compartments and authoritative outcomes (CDG-47) | Deferred Design / Discovery | Mnemosyne C12, M18 | Transaction/service oversight and hosted Themis enforcement are explicit. Detailed semantic applicability source, validation and outcome/transaction mechanisms remain open. Kleio authority is not extended to persistence components. | Later bounded design/adjudication where needed. |
| Search/Research Metadata detailed contracts (CDG-48) | Deferred Design / Discovery | Mnemosyne Search/Research Metadata, D29–30 | Search space/owner and external derivation boundary are settled. Queries, consistency, metadata schemas, provenance/confidence and exact deriving activity remain deliberately unselected. | Later design; no mandatory AI technology or Dokimasia allocation inferred. |
| Local Directory sync/enrichment/EndpointCapability proposal (CDG-33) | Deferred Design / Discovery | F30, L24 disposition | Explicitly retained unallocated proposal whose reconciliation/allocation was deferred in its source and RADS. It is not represented as an established required responsibility. | Later candidate-scope decision; no current upstream gap asserted. |
| Optional Statistics/Tasks/Diagnostics projection providers (CDG-23 detail) | Deferred Design / Discovery | Agora F22 and proposed room projections | Exact room layout/providers are candidate proposals, not required independent services. Their detailed production/disclosure design is distinct from CA-03's unestablished formal-record transition. | Later bounded scope/design; no new producer inferred. |
| Legacy cache/write-behind acceptance and direct cache/database access paths (CDG-10/11) | Implementation Matter | Mneme/Mnemosyne, Iris/CLI and older gateway paths | Current AX-05 and governed-access responsibilities explicitly reject cache durability or application storage bypass. Legacy implementation convergence cannot establish competing architectural authority. | No re-adjudication of those settled boundaries; later implementation checks/contracts. |
| Historical persistence/history/index/cache structures, evidence layouts and physical allocation (CDG-12/13/27/40) | Implementation Matter | Clinical/C&G/O&A, HAPI/custom rows, working sets | Current semantic domains, durable-state and audit owners are explicit. Physical key/schema/topology variants remain historical/downstream; none is selected as current architecture. | Later design may need decisions; physical incompleteness alone does not block A4. |
| Historical automatic security-context persistence (CDG-15) | Documentation Quality Matter | L08/L18 dispositions and current C02/D03/AX-07/13 | Current RADS rejects automatic context persistence/export; superseded text remains historical. Detailed transport/retention is separately retained under CDG-42. | No current boundary adjudication; preserve original text and scoped wrappers. |
| Historical universal clinical authority, diagnostic/display conflation and EMPI claims (CDG-20/26/29) | Documentation Quality Matter | Current C13/C15–18/F08 and legacy dispositions | Current source authority, authorised presentation and bounded identifier-processing meanings expressly reject broader historical readings. Those readings do not establish an accepted master-patient engine or clinical-work authority. | No reopening of settled guardrails. Any proposed new master-identity responsibility needs separate adjudication. |
| Navigation and dated disposition wording | Documentation Quality Matter | `publication-and-selection` fragment currently precedes the Horos section; gap-disposition text still names U02–U12 | These affect navigation/status clarity, not ownership. The retained audit-conflict alias was corrected during authorised reconciliation; unrelated navigation remained unchanged. | No substantive adjudication. |
| DigitalTwin construct/component shorthand | Documentation Quality Matter | Strategy Component 7 / summary versus Domain05 C19 and specialisation map | Strategy's detailed exclusion is of an **independent platform component**; Domain05 describes a Ponos-specialised component reusing established execution and information management without independent deployment. The summary's shorter “FAIL as Component” can confuse these levels, but the explicit boundaries do not establish competing architectures. | No substantive adjudication identified; terminology can be clarified separately. |
| Business-security realisation (purpose, relationship, consent, break-glass) | Step B Traceability Matter | [Existing cross-layer boundary note](../../docs/markdown/05-information-systems-architecture/gaps.md#upstream-business-security-realisation-boundary) | The note explicitly says these Domain03-to-Candidate relationships have not been established. That is not proof of a contradictory Candidate model or permission to add policy dimensions now. | Only if later mapping establishes a substantive conflict; no Step-B assessment performed. |
| Possible upstream operational-management demand omission | Step B Traceability Matter | CDG-39 and current Energeia responsibility | Upstream coverage has intentionally not been assessed. Existing Candidate operational ownership is clear. | Later traceability/review; no missing Business Capability inferred. |

### Blocker evidence and limits

**CA-01:** C18 retains room membership reconciliation; F22 allocates collaboration
spaces/membership to Agora and governed context sources. CDG-21 states that
"Directory representation and collaborative membership do not establish the
authoritative roster/source service". C13 explicitly excludes automatic ownership
of all care-team facts. The missing conceptual source/authority is thus visible
within RADS itself; it is not discovered by searching for an upstream Capability.

**CA-02:** C18 says mapping/transaction tables and homeserver storage are documented
but "ownership, Mnemosyne collaboration and clinical submission path remain unclear".
D14 likewise retains unresolved authority/storage allocation; the historical
persistence table records Mnemosyne versus Agora Core JPA/Synapse ownership.
AX-05 already governs authoritative managed information. What remains unknown is
the collaboration-specific custody boundary and which information belongs on each
side, not whether JPA or a chosen database is permissible.

**CA-03:** F24 combines "formal record retention boundary" with "candidate clinical
submission". CDG-23 explicitly records missing providing/submission and
authority/retention responsibilities. The legacy source's commit-before-discourse-purge
claim is retained provenance, not an adopted algorithm. No current component
contract in RADS completes that conceptual handoff. An explicit deferred-feature
disposition could change this assessment; the report does not infer it from
unspecified release selection.

**CA-04:** F31 names media/binary handling and D15 names DocumentReference and binary
payload, while both identify the custodian as unknown. The system map's unresolved
area explicitly retains the media custodian question. Generic Mnemosyne durability
does not by itself allocate application responsibility for binary content.
Selecting physical storage, a schema or a new service is not necessary to state
that responsibility; none is selected here.

These are four **pre-existing, still-unadjudicated questions exposed by the closure
review**, not newly invented requirements. The table's category-1 assessment is a
recommendation for Mark + ChatGPT. It does not change their CDG rows, reject their
features, add a component or decide release necessity. All four remain unchanged
in canonical architecture pending adjudication.

### Internal reconciliation and reconstruction findings

The three U15 matters now have consistent component, map, register, lifecycle,
axiom and terminology treatment. No additional pair of established current RADS
definitions was found assigning those responsibilities to competing owners.
Earlier source-register descriptions are dated incorporation history; their
current U15 disposition is explicit. Historical legacy contradictions are labelled
as such and do not override current RADS.

Current views also consistently preserve Energeia Praxis/Journey ownership,
Mneme Context/Pragma and semantic selection, Mnemosyne durable-state/search
management, Petasos queue/handoff ownership, common execution specialisations,
and Dokimasia's independence/terminal rules. The retained missing responsibilities
above appear consistently as unresolved; they were not silently completed.

No additional known conceptual construct was found whose already-adjudicated
meaning remained solely in an external note or legacy document. Required
incorporated decisions are self-contained in RADS. This is a bounded review
finding, not a claim that all future discovery or whole-Domain05 completion is done.

### Explicit conclusions

| Activity | Assessment conclusion | Evidence / boundary |
| :--- | :--- | :--- |
| **A1 — Candidate reconstruction/documentation** | **COMPLETE** | Existing candidate responsibilities, proposals and unresolved areas are documented in RADS. This does not establish that every responsibility has been adjudicated or prove upstream coverage. |
| **A2 — Architectural review/adjudication** | **INCOMPLETE** | CA-01–04 retain substantive source/custody/handoff responsibilities requiring Mark + ChatGPT clarification or explicit candidate-scope/deferred disposition. Detailed items in categories 2–5 are not promoted to blockers. |
| **A3 — Candidate correction/reconciliation** | **COMPLETE WITH NON-BLOCKING DEFERRED ITEMS** | All currently supplied adjudications, including U15, are consistently represented. No additional correction of established meaning was identified; remaining mechanisms/navigation are explicit. A further bounded A3 pass will be needed after outstanding A2 decisions. This is an assessment of the present adjudicated snapshot, not programme closure. |
| **A4 — Candidate accepted as sufficiently faithful** | **NOT READY FOR A4 ADJUDICATION — recommendation only** | Resolve or explicitly disposition CA-01–04 before recommending acceptance. A4 is not marked complete or decided by Codex. |

No capability, information, requirement or implementation traceability completeness
test contributes to these conclusions. Whole-Domain05 acceptance, MVP selection,
Step B and implementation planning remain unperformed.
