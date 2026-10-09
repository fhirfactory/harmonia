# Canonical Architectural Axioms Migration — Step 1

**Date:** 2026-10-09

**Status:** Assessment complete; migration design proposed for human adjudication.

**Authority:** This report is execution/assessment evidence. It establishes no axiom, relocation, reclassification, semantic change or approval.

## Task Goal

Assess AX-01 through AX-17 as currently established, determine a suitable single canonical location under /docs/markdown, evaluate later Strategy and Domain03 rules, and design reference reconciliation and retirement of the external authority. This step ends with this report.

No migration, axiom amendment, new axiom, renumbering, reference change, retirement, Domain04 reconciliation or implementation change was authorised or performed. Existing user changes were present at task start and are preserved. The intended outcome is a reviewable migration design; agreement is pending human decision.

## Authority and Context Loaded

Context was loaded afresh from repository files, progressively from the required inputs. Repository content, including current working-tree canonical changes, was read directly. Earlier session interpretations and report claims did not establish architecture.

| Source consulted | Role in this assessment |
| :--- | :--- |
| [Root AGENTS.md](../../AGENTS.md), [docs/AGENTS.md](../../docs/AGENTS.md) | Authority hierarchy, fresh-context/progressive loading, bounded execution and required architecture checks. There are no deeper scoped AGENTS.md files under the assessed docs or report paths. |
| [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md), §§1, 4–9 | Canonical-corpus invariant, semantic migration, explicit current external authority, task boundaries, semantic completion and retained uncertainty. Domain03 Step 3 is complete for its agreed Business scope; Domain04 remains a separate stage. This is the separately authorised axiom assessment, not that stage or the runtime-convergence programme. |
| [Current Architectural Axioms](../../docs/architectural-axioms.md), complete document | Current authoritative statements, rationale, consequences, exclusions, diagnostic observability, supporting sections and internal references. All 17 identifiers remain established, including AX-12. |
| [Motivation axioms](../../docs/markdown/01-motivation/principles/architectural-axioms.md), [reclassification record](../../docs/markdown/01-motivation/principles/reclassified-principles.md), [Motivation README](../../docs/markdown/01-motivation/README.md) | Existing competing catalogue, three-tier formatting, AX-12 reclassification claim, AX-16 reproduced wording and AX-17 external dependency. Canonical location alone does not override the expressly authoritative current register. |
| [Foundational requirements](../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md), [master navigation catalogue](../../docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md) | Existing requirement/axiom dependencies, durable acceptance, progression, subject integrity, uncertainty and independent assurance; preserve approved REQ-FND-005 and its distinct authority boundaries. |
| [Strategy logical responsibilities](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md), G1–G4, Components 1–3/7, seams and entity-specific rule | Cross-domain responsibility boundaries and the existing authoritative home of the Twin rule. No execution mechanism is derived. |
| [Strategy courses of action](../../docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md), relevant COA-01–06; [Business capabilities](../../docs/markdown/02-strategy/capabilities/business-capabilities.md), modelling/relevance rules; [Business enabling catalogue](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md), targeted SD/HSO and workflow references | Distinguish healthcare-enterprise scope, Harmonia enablement and reusable responsibility from machinery. Inspect later-rule scope and affected reference occurrences. |
| [Canonical AX-05 reconciliation](../../docs/markdown/02-strategy/reviews/ax05-state-responsibility-reconciliation.md), authority/responsibility boundaries | Established current Strategy interpretation of active management, activity execution and authoritative durable establishment. Historical quotations in the review are evidence, not reactivated contradictory definitions. |
| [Accepted ADRs](../../docs/architecture-decisions.md), targeted ADR-018/019 status and state-boundary sections | Follow the explicit AX-05 canonical dependency to corroborate current durable/active authority. These remain separately maintained accepted decisions; their migration is not authorised here. |
| [Domain03 metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md), [README](../../docs/markdown/03-business-architecture/README.md), [Service Delivery](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md), [Health Service Operations](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md), [information responsibility](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) | Direct canonical authority for semantic sufficiency, composition, layer purity, contextual capacity, clinical-work integration and non-transfer of originating authority. The user-linked HSO document was assessed directly. |
| [Domain03 Step 3 report](2026-10-09-domain03-business-architecture-completion-step3.md) | Execution/assessment evidence and navigation to the actual canonical decisions. Its conclusions and prior PASS results were not substituted for authority or this step’s validation. |
| [docs navigation](../../docs/README.md); targeted [deferred register](../../docs/deferred-document-register.md), [execution model](../../docs/architecture/execution-model.md), [Dokimasia orientation](../../docs/modules/dokimasia.md), [memory recovery](../../docs/memory-recovery.md) | Assess destination conventions and reference disposition only. Deferred/history/navigation material supplies no missing architectural decision. |
| Repository-wide reference search and selected source/test comment occurrences | Locate all axiom-document and AX-identifier references, including canonical Domain04 references. Domain04 text was not changed or reconciled, and source/tests were not architectural authority. |

The migration design materially engages AX-01/02/03/04 (purpose, standards and semantics), AX-05/06/07/08/09/10/11/12/13 (responsibility, authority, security, evidence, availability and boundaries), AX-14/15 (distinct semantics and uncertainty), and AX-16/17 (coordinated progression and truthful architectural authority). Preserving every statement and its qualification is consistent with those axioms. Generalising, deleting AX-12, inferring clinical authority or resolving a discrepancy from historical approval claims would not be a semantic migration.

The current register’s authority hierarchy and §§3–7 also contain architectural knowledge and normative obligations. The destination must preserve that material, not only the paragraphs headed “Axiom”. Its source SHA-256 at assessment is 707fb1b4cb0253a515a948685643091c65a3e72880c77cc0700503ddb36bbeea.

## Existing Axiom Inventory

The following statement blocks reproduce the current “Axiom” subsection wording without editorial replacement. They are evidence copies, not an additional maintained authority. AX-12’s malformed inline Rationale marker is identified separately; its two normative paragraphs are transcribed up to that marker. Source rationale, consequences, exclusions and supporting sections remain necessary migration inputs.

“Exact semantic migration” means retaining the current source’s meaning and normative qualifications, including consequences; it does not mean replacing it with the abridged Motivation wording. “Editorial clarification” permits only layout, grammar, terminology consistency and reference clarity without a change of duty holder, scope, modality or meaning. “Requires human review” flags reconciliation/interpretation needed before a single authoritative catalogue can be published; it does not suspend current source authority or authorise a rewrite.

### AX-01 — Harmonia Is Health-Information Centric

**Current source:** docs/architectural-axioms.md:45. **Migration classification:** Semantic migration with editorial clarification.

**Current normative statement (verbatim source excerpt):**

```text
Harmonia is a distributed health-information management and
interoperability framework.

Its primary purpose is the ingestion, validation, transformation,
governance, processing, persistence, distribution and exposure of
health-related information.

Harmonia SHALL support applicable health-information standards and
models while retaining the ability to manage information according to
Harmonia's own internal operational requirements.
```

**Semantic purpose:** Keep health-information management and interoperability as the primary purpose; healthcare semantics take precedence over generic infrastructure convenience.

**Affected domains:** Motivation and Strategy; Business, Information, Application, Integration and Technology.

**Current canonical references/dependencies:** Motivation drivers and strategic goals; Strategy COA-01 and COA-05; Domain03 care enablement and information responsibility. These are existing downstream uses, not newly established derivation edges.

**Later clarification/specialisation:** Domain03 distinguishes healthcare enterprise responsibility from Harmonia integration enablement. That specialises the purpose without making Harmonia an EMR or clinical-work manager.

**Conflict/ambiguity concerns:** No conflict in the reviewed Strategy/Domain03 statements. The Motivation duplicate adds implementation claims about classifications being attached directly to records; those are not part of the axiom.

**Implementation/obsolete terminology:** No obsolete term established. The escaped asterisk list under “This does not mean” is a formatting defect, not permission to remove its scope exclusions.

### AX-02 — Standards at the Boundary; Harmonia Within the Boundary

**Current source:** docs/architectural-axioms.md:85. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Harmonia SHALL ingest and expose health information using the standards,
information models and interoperability protocols applicable to an
external interface contract.

Within Harmonia, information MAY be augmented, represented, indexed,
cached, distributed, governed, versioned, correlated and persisted using
Harmonia-specific semantics and metadata necessary to satisfy Harmonia's
operational requirements.

Harmonia's internal operational semantics are private to Harmonia.
External systems SHALL NOT be required to understand, preserve,
reproduce or participate in them.
```

**Semantic purpose:** Make external interface contracts standards-compliant while preserving private Harmonia operational semantics internally.

**Affected domains:** Information, Application, Integration, Security and Technology; cross-domain contract governance.

**Current canonical references/dependencies:** AX-17 explicitly cites AX-02; Motivation external constraints; Strategy COA-01, Pylai responsibility and Seam 4; AX-13 and the source document’s “FHIR and Harmonia” section complement it.

**Later clarification/specialisation:** Strategy G4 separates standards interaction from transport. Domain03 distinguishes Business interaction semantics from technical vocabulary. Neither changes the external/private boundary.

**Conflict/ambiguity concerns:** No conflict in reviewed current rules. The duplicate’s concrete gateway/protocol examples must not establish new product support or require conversion away from native models.

**Implementation/obsolete terminology:** FHIR/HL7 boundary examples are applicable examples, not a universal choice of representation or transport.

### AX-03 — Native Standards Models Remain Native

**Current source:** docs/architectural-axioms.md:126. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Standards-defined information objects SHALL retain their
standards-defined representation within Harmonia wherever practicable.

Harmonia SHALL NOT introduce parallel or derivative representations
solely to accommodate internal management concerns where the standard
representation provides an appropriate extensibility mechanism.
```

**Semantic purpose:** Preserve native standards-defined representations where practicable; avoid parallel models created solely for internal management concerns.

**Affected domains:** Information, Application and Integration; Technology model use.

**Current canonical references/dependencies:** Motivation drivers and requirements navigation; Strategy COA-01; related AX-02, AX-04 and AX-12. Domain03 metamodel §§3.6 and 6 and information responsibility §1.3 preserve Business meaning before downstream representation.

**Later clarification/specialisation:** Business concepts remain independent of FHIR bindings. This does not conflict: AX-03 concerns a standards-defined information object, not a requirement that every Business concept be a FHIR resource.

**Conflict/ambiguity concerns:** The Motivation duplicate retitles it “Native Standards Representations” and says durable metadata “is stored” using native facilities. The current authority instead permits private FHIR extensions “where appropriate” and explicitly allows non-FHIR metadata. Do not migrate the duplicate’s stronger reading.

**Implementation/obsolete terminology:** HAPI FHIR, FHIR R5 and FHIR R4 AU are consequence-level choices/examples. No evidence establishes them obsolete; keep their qualifications.

### AX-04 — Harmonia Owns the Semantics; Engines Provide the Machinery

**Current source:** docs/architectural-axioms.md:165. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Harmonia owns the architectural semantics governing information
authority, security, lifecycle, concurrency, provenance, auditability,
resilience and operational integrity.

Harmonia SHALL preferentially use capabilities supplied by underlying
technology engines where those capabilities satisfy Harmonia's
architectural invariants rather than reproduce equivalent functionality.
```

**Semantic purpose:** Retain Harmonia semantic ownership while preferring suitable engine capabilities over rebuilding equivalent machinery.

**Affected domains:** All downstream design domains, especially Information, Application, Integration, Security and Technology.

**Current canonical references/dependencies:** AX-17 explicitly cites AX-04; Strategy G2/G4, COA-01 and reusable realisation; Domain03 metamodel §§3.6, 6 and 8.6; Business information responsibility §1.3.

**Later clarification/specialisation:** Business Capacity Management remains the meaningful responsibility; generic operational-state capture is lower-layer machinery. This is a layer-specific application, not a new universal axiom.

**Conflict/ambiguity concerns:** No conflict in reviewed current rules. Component reuse is conditional on architectural invariants; “preferentially” does not require reuse of an unsuitable engine.

**Implementation/obsolete terminology:** HAPI FHIR, Infinispan, Artemis and PostgreSQL appear as permissive consequence examples. Their deletion or conversion into technology-independent replacement obligations would exceed editorial migration.

### AX-05 — Active State and Authoritative Durable State Are Distinct

**Current source:** docs/architectural-axioms.md:205. **Migration classification:** Requires human review before migration.

**Current normative statement (verbatim source excerpt):**

```text
Mneme owns Harmonia's application-facing access to managed information
and the distributed active-state representation, observation and
coordination required to use that information safely.

Mnemosyne owns Harmonia's authoritative durable representation of
managed information. It atomically establishes authoritative state and
authoritative version progression and persists the durable management
metadata required to interpret that state.

Mneme manages active use; Mnemosyne establishes durable truth.
```

**Semantic purpose:** Reserve application-facing active access/coordination for Mneme and atomic authoritative durable state/version establishment for Mnemosyne; preserve distinct concurrency domains.

**Affected domains:** Information, Application, Integration, Technology, operational resilience and presentation access.

**Current canonical references/dependencies:** Strategy G3, Components 1/2/3/7 and Seams 1/2/6; canonical AX-05 reconciliation; Motivation REQ-FND-001; accepted ADR-018 and ADR-019 outside the canonical corpus remain relevant supporting authority.

**Later clarification/specialisation:** Current Strategy explicitly separates Ponos activity execution, Mneme active management and Mnemosyne durable establishment. Twin coordination consumes those responsibilities without becoming a persistence authority.

**Conflict/ambiguity concerns:** Requires review of competing text: Motivation substitutes a generic “active operational state” principle including “transient workflow coordination”, removes named responsibility and atomicity, and omits the later active-generation safeguards. Current Strategy explicitly excludes operational workflow execution from Mneme. Human review must confirm reconciliation of the duplicate against the central wording, not infer a new owner.

**Implementation/obsolete terminology:** Mneme/Mnemosyne are architectural responsibilities, not obsolete product names. Active-state generation/token and authoritative version obligations in Consequences are normative and must survive even if not in the “Axiom” subsection.

**Additional current normative safeguards in Consequences (verbatim):**

```text
Mneme active-state generation SHALL represent successfully established active state. A valid active-state token SHALL identify an observed generation of that state.

Failed or degraded convergence following authoritative state progression SHALL NOT establish or advance a valid active-state generation. Where the active representation cannot be successfully converged, its coordination state SHALL be treated as untrusted until reconciled with authoritative state.

Mneme active-state generation and Mnemosyne authoritative version SHALL remain distinct concurrency domains. Neither SHALL be inferred from, substituted for, or treated as an alias of the other.
```

### AX-06 — Information Authority Is Explicit

**Current source:** docs/architectural-axioms.md:257. **Migration classification:** Semantic migration with editorial clarification.

**Current normative statement (verbatim source excerpt):**

```text
Harmonia SHALL represent the authority and credibility of managed
information independently of its technical transport, cache state,
persistence state or concurrency state.

Harmonia SHALL support governed information-authority concepts
including: \* Authoritative, \* Informational, and \* Anecdotal.
```

**Semantic purpose:** Keep credibility/information authority distinct from transport reliability, persistence, cache and concurrency state.

**Affected domains:** Business, Information, Application, Integration, Security and assurance/evidence governance.

**Current canonical references/dependencies:** AX-17 explicitly distinguishes information authority from architectural authority; Motivation REQ-FND-003/005; Strategy semantic governance; Domain03 metamodel §3.7 and information responsibility §§1.2/1.5.

**Later clarification/specialisation:** Capacity, patient-flow, discharge and Twin consumers retain originating clinical-fact authority. Evidence credibility does not establish independent assurance or clinical decision authority.

**Conflict/ambiguity concerns:** No conflict in reviewed rules. The duplicate’s “authority tag” realisation must not require caller-supplied tags to confer security authority or a single concrete representation.

**Implementation/obsolete terminology:** Authoritative, Informational and Anecdotal are explicitly supported governed concepts; preserve all three. Repairing the escaped list is editorial only.

### AX-07 — Security Is Intrinsic to Managed Operations

**Current source:** docs/architectural-axioms.md:295. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Every governed operation SHALL execute within an established security
context and SHALL be subject to Harmonia security policy.

Security SHALL be enforced by framework and platform boundaries where
practicable and SHALL NOT depend solely upon developer knowledge, coding
convention or voluntary caller behaviour.
```

**Semantic purpose:** Require established security context and security policy for governed operations; enforce through boundaries where practicable.

**Affected domains:** All governed operations across Business, Information, Application, Integration, Security and Technology.

**Current canonical references/dependencies:** Motivation security/privacy drivers and REQ-FND-005; Strategy policy/evidence courses of action; Domain03 Health Information Control and assurance. Root Invariant 6 supplies default-deny/context enforcement, subordinate to the axiom.

**Later clarification/specialisation:** Clinical/operational privilege distinctions and independent assurance operate under policy; they do not invent another security mechanism or clinical authority.

**Conflict/ambiguity concerns:** Motivation’s rewritten principle drops the “where practicable” qualification and its implications say “All operations”. Preserve the current scope “Every governed operation” and the framework/platform qualification; do not broaden obligations during migration.

**Implementation/obsolete terminology:** Themis is the established policy responsibility. Security context remains operational context and is not automatically persisted information content.

### AX-08 — Evidence Records Meaning, Not Machinery

**Current source:** docs/architectural-axioms.md:329. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Provenance and audit evidence SHALL describe information-significant,
security-significant and business-significant events, assertions and
decisions.

Internal implementation mechanics SHALL NOT ordinarily become provenance
or audit evidence merely because they occurred.
```

**Semantic purpose:** Make durable provenance/audit describe meaningful information, security and Business events rather than routine implementation mechanics.

**Affected domains:** Business, Information, Application, Integration, Security, assurance and operational observability.

**Current canonical references/dependencies:** Motivation privacy/accountability and REQ-FND-005; Strategy evidence and provenance realisation; Domain03 assurance and information responsibility; Kleio is the named durable-evidence responsibility.

**Later clarification/specialisation:** Independent assurance consumes evidence but is not evidence custody; an evidence record is not itself an assurance conclusion. REQ-FND-005 adds an independent obligation rather than redefining AX-08.

**Conflict/ambiguity concerns:** No conflict in current rules. The duplicate’s clinical/legal examples do not exhaust the axiom’s Business and information-significance scope.

**Implementation/obsolete terminology:** Cache/CAS/retry examples are intentionally machinery examples. Preserve “ordinarily”, “normally” and “MAY require” rather than absolute exclusion or universal audit.

### AX-09 — Transient Operational State Is Ephemeral by Default

**Current source:** docs/architectural-axioms.md:362. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Harmonia MAY maintain transient operational state associated with
managed information where required for caching, persistence,
concurrency, security, routing, resilience, diagnostics and processing
integrity.

Such state SHALL be retained only for as long as required by its
operational purpose and SHALL normally disappear when that purpose has
been satisfied.

Transient operational state SHALL NOT automatically become provenance or
audit evidence.
```

**Semantic purpose:** Retain transient operational state only for its purpose; allow controlled diagnostic observation and promote meaningful evidentiary information where required.

**Affected domains:** Information, Application, Security, Technology, processing integrity and observability.

**Current canonical references/dependencies:** Motivation operational assessments and REQ-FND-005; Strategy G1/evidence realisation; Domain03 assurance evidence/progression boundaries; related AX-08 and AX-15.

**Later clarification/specialisation:** Business capacity/progression concepts can be enduring even when active representations are transient. Business meaning is not made ephemeral merely by use of Mneme or a cache.

**Conflict/ambiguity concerns:** The Motivation duplicate omits the explicit normative meaningful-information promotion clause and oversimplifies purge timing. Preserve the entire “Diagnostic observability” subsection, not just the main Axiom paragraphs.

**Implementation/obsolete terminology:** “Normally” and purpose-limited retention are deliberate. Cache/routing/security state are examples; no obsolete mechanism established.

**Additional current normative Diagnostic observability subsection (verbatim):**

```text
Harmonia SHALL provide controlled mechanisms by which designated
transient operational state MAY be observed or reported when diagnostics
are explicitly enabled at framework, subsystem or component scope.

Where transient state directly contributes to an information-significant
decision, assertion or evidentiary fact, the meaningful information
SHALL be promoted into the appropriate durable provenance or audit
representation.
```

### AX-10 — Distribution, Load and Failure Are Normal Operating Conditions

**Current source:** docs/architectural-axioms.md:402. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Harmonia SHALL be designed on the assumption of distributed deployment,
sustained processing load and failure or degradation of individual
runtime components.

Loss or degradation of implementation machinery SHALL NOT silently alter
the authoritative, security, governance or information-authority
semantics of managed information.
```

**Semantic purpose:** Treat distribution, sustained load and machinery failure as normal; preserve semantics and recover work according to explicit durable boundary contracts.

**Affected domains:** Application, Integration, Technology, Information, Security and operational resilience.

**Current canonical references/dependencies:** Motivation availability/false-acceptance assessments, REQ-FND-001; Strategy COA-02/04; root ingress safety and fan-out guardrails; related AX-05, AX-11 and AX-15.

**Later clarification/specialisation:** Observable capacity/activity progression and outcome uncertainty preserve failure meaning. G1/G2 and Twin demand-driven coordination distinguish responsibility from process/network topology.

**Conflict/ambiguity concerns:** Motivation changes “durable processing boundary” to “authoritative durable boundary” and strengthens bounded backlog preference into “must”. Durable transfer of processing responsibility is distinct from committed information state; preserve the original qualified contract.

**Implementation/obsolete terminology:** JVM heap, threads, database connections and Java boundary examples constrain resource growth/topology interpretation without requiring a runtime service for every responsibility.

### AX-11 — Managed Information Access Is Highly Available and Responsive

**Current source:** docs/architectural-axioms.md:435. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Mneme SHALL provide highly available, responsive and load-tolerant
access to Harmonia-managed active information.

Its architecture SHALL favour bounded resource consumption, distributed
workload, reconstructable active state and graceful expression of
processing pressure.

Availability SHALL NOT be achieved by weakening authoritative-state,
concurrency, security, governance or information-authority guarantees.
```

**Semantic purpose:** Require highly available, responsive, load-tolerant access through Mneme without weakening authority, concurrency, security or governance guarantees.

**Affected domains:** Information, Application, presentation, Technology and operational availability.

**Current canonical references/dependencies:** Motivation availability drivers/goals; Strategy COA-02/05 and Mneme access responsibility; accepted ADR-019 supports reconstructable active access; AX-05 and AX-10 complement it.

**Later clarification/specialisation:** Twin demand-driven lifecycle and Strategy responsibility/topology distinctions specialise scalability without prescribing permanent threads or one universal cache topology.

**Conflict/ambiguity concerns:** The Motivation duplicate changes the duty holder from Mneme to Harmonia and extends the object to active coordination state; that is not a verbatim substitute. Do not acquire general application-access or clinical capacity guarantees from this wording.

**Implementation/obsolete terminology:** REPL_SYNC is a negative example, not an endorsed universal cache setting. Mneme remains an architectural responsibility, not merely an implementation brand.

### AX-12 — Hide Plumbing, Not Information

**Current source:** docs/architectural-axioms.md:471. **Migration classification:** Requires human review before migration.

**Current normative statement (verbatim source excerpt):**

```text
Developers implementing Harmonia work units SHALL have direct and fluent
access to applicable standards-defined health-information models and
Harmonia's managed-information capabilities.

Framework plumbing SHALL provide security, governance, authority,
persistence, concurrency, provenance, messaging and operational services
without unnecessarily obscuring or replacing the underlying information
models.
```

**Semantic purpose:** Give work-unit developers fluent standards models and governed information capabilities while making the safe path convenient and hiding unnecessary infrastructure plumbing.

**Affected domains:** Application and developer contracts, with Information, Integration, Security and Technology consequences.

**Current canonical references/dependencies:** Current central register still establishes AX-12. Motivation reclassified-principles and README claim transfer to Domain05. The master requirements catalogue still cites AX-12. Related AX-03/04/07 govern model access, machinery and safe boundaries.

**Later clarification/specialisation:** Domain03 Business-layer purity is compatible but does not authorise removal of an axiom. Current canonical AX-05 reconciliation explicitly leaves AX-12 classification a separate matter.

**Conflict/ambiguity concerns:** Direct authority conflict: the central register contains a current AX-12, whereas canonical Motivation calls it formally reclassified/not a current foundational axiom and omits it from its catalogue. Preserve AX-12 as currently established. Human adjudication must determine how to correct/demote the competing reclassification claim; no Domain05 transfer is performed or assumed.

**Implementation/obsolete terminology:** The source fuses the end of the Axiom subsection with an escaped “#### Rationale”. Ergon, RemoteCache, EntityManager and JMS are illustrative developer/machinery terminology; do not silently replace architectural terms or delete examples.

### AX-13 — Harmonia Management Has an Explicit Boundary

**Current source:** docs/architectural-axioms.md:511. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Ingress establishes Harmonia management. Internal processing preserves
Harmonia management. Egress terminates Harmonia management of the
emitted representation.

Harmonia MAY retain evidence describing the information from which an
external representation was derived, the transformation applied, the
transmission performed and the externally observable outcome.

Harmonia SHALL NOT attribute its internal authority, governance,
concurrency, security, availability or operational guarantees to an
emitted representation after that representation crosses an external
egress boundary.
```

**Semantic purpose:** Define ingress/internal management/egress scope; emitted representations lose Harmonia operational control while retained internal information and evidence remain managed.

**Affected domains:** Information, Application, Integration, Security, provenance and governance.

**Current canonical references/dependencies:** Strategy Pylai and Seam 4; root external publication Invariant 9; Motivation boundary orientation; PylaiPublicationBoundaryArchitectureTest and FHIR gateway comments/tests cite AX-05/13.

**Later clarification/specialisation:** Clinical-work integration and Business ownership constraints prevent authority expansion but are distinct from external-copy management. Clinical decision authority is not granted simply because a representation is inside Harmonia.

**Conflict/ambiguity concerns:** Motivation’s realisation says outbound dispatch terminates the operational lifecycle “for that transaction”; that could incorrectly close a parent activity awaiting response. Preserve emitted-representation scope, not a blanket termination of internal workflow or retained evidence.

**Implementation/obsolete terminology:** FHIR ETag/meta.versionId and HL7 acknowledgements remain legitimate external transaction/version semantics. Their existence does not export internal concurrency control.

### AX-14 — Semantic Distinctions Are Preserved

**Current source:** docs/architectural-axioms.md:563. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Harmonia SHALL preserve meaningful distinctions between states, outcomes and concepts throughout its internal processing and across subsystem boundaries.

A distinction that is significant to information meaning, authority, security, concurrency, persistence, processing outcome or operational correctness SHALL NOT be collapsed merely because an underlying technology, transport, API or implementation abstraction does not represent that distinction directly.

Where an external interface contract intentionally presents a simpler or different semantic model, Harmonia MAY project its internal semantics into that contract explicitly at the applicable boundary.
```

**Semantic purpose:** Preserve significant semantic distinctions internally and project deliberately where an external contract requires a different model.

**Affected domains:** All domains, especially Business, Information, Application, Integration and Security.

**Current canonical references/dependencies:** AX-17 explicitly cites AX-14; Motivation REQ-FND-003/005; Strategy G2/G3/G4 and seams; Domain03 privilege, capacity, clinical/operational progression, ownership and metamodel distinctions.

**Later clarification/specialisation:** Resource state versus capacity, fact versus consequence, clinical versus operational progression and information authority versus assurance conclusion are concrete specialisations, not new generic axioms.

**Conflict/ambiguity concerns:** No conflict in reviewed current rules. The Motivation abridgement does not retain every normative consequence and must not replace the source.

**Implementation/obsolete terminology:** ActiveStateToken/AuthoritativeVersion and security denial/resource absence examples embody distinct semantics. Preserve them without requiring a Java type for every distinction.

### AX-15 — Uncertainty Is Preserved Until Resolved

**Current source:** docs/architectural-axioms.md:600. **Migration classification:** Exact semantic migration.

**Current normative statement (verbatim source excerpt):**

```text
Where Harmonia cannot establish the state, outcome or effect of an operation with the certainty required by its governing contract, that uncertainty SHALL be represented explicitly.

Harmonia SHALL NOT infer, manufacture or assume a more certain outcome merely to simplify processing, recovery or application behaviour.

Uncertainty MAY be resolved through authoritative observation, reconciliation or other governed evidence capable of establishing the required state.
```

**Semantic purpose:** Represent uncertain state/effect/outcome explicitly until governed evidence resolves it; retry state changes only when demonstrably safe.

**Affected domains:** Business progression, Information, Application, Integration, Technology, recovery and assurance activity execution.

**Current canonical references/dependencies:** AX-17 explicitly separates operational from architectural uncertainty; Motivation REQ-FND-004/005; Strategy COA-04; Domain03 metamodel §4.4; coordination source/test comments cite AX-14/15.

**Later clarification/specialisation:** Domain03 preserves materially significant outcome uncertainty without a universal Process state. REQ-FND-005 independently governs insufficient assurance evidence, which is not necessarily uncertain execution.

**Conflict/ambiguity concerns:** Motivation’s implications require explicit INDETERMINATE representations and reconciliation-first wording more broadly. Preserve the source’s contract-sensitive scope, governed alternatives and preferential recovery qualification rather than requiring an enum or state everywhere.

**Implementation/obsolete terminology:** No obsolete terminology established. “Certainty required by its governing contract” and known non-occurrence exclusions are essential qualifications.

### AX-16 — Operational Activity & Entity State Progress Together

**Current source:** docs/architectural-axioms.md:633. **Migration classification:** Requires human review before migration.

**Current normative statement (verbatim source excerpt):**

```text
> **Operational activity associated with a real-world entity progresses through explicit and observable state transitions coordinated with the governed state of that entity. Operational activity is not treated merely as a sequence of disconnected message transfers.**
```

**Semantic purpose:** Coordinate explicit, observable operational activity transitions with governed state of the associated real-world entity; avoid reducing progression to disconnected message transfers.

**Affected domains:** Strategy, Business, Information, Application, Integration and operational activity coordination.

**Current canonical references/dependencies:** The central register expressly reproduces the established Motivation definition, with a reverse link. Strategy COA-04/05 and Digital Twin rule; Motivation REQ-FND-002; Domain03 progression and contextual coordination.

**Later clarification/specialisation:** Strategy’s entity-specific Twin SHOULD rule selects a coordination construct with reasoned exceptions. It adds a governed specialisation; AX-16 itself neither mandates every query/population activity through a Twin nor allocates clinical-work authority.

**Conflict/ambiguity concerns:** The exact normative principle agrees between both catalogues, but the external register calls the Domain01 definition “established” while the root gives the external register design authority. Human review should confirm consolidation into one owner and preserve the distinction between principle, implications and non-normative realisation. “Lockstep” implications need an explicit interpretation decision if challenged; no synchronous/atomic workflow invariant is inferred.

**Implementation/obsolete terminology:** External title uses “&”; canonical title uses “and”. Preserve current identifier/title text or approve a title-only editorial change. Fan-out checkpoints, Pragma, FHIR Task.output and AdtDistributionErgon are expressly non-normative realisation here.

### AX-17 — Architectural Authority and Explicit Uncertainty

**Current source:** docs/architectural-axioms.md:649. **Migration classification:** Semantic migration with editorial clarification.

**Current normative statement (verbatim source excerpt):**

```text
Documented architectural decisions, definitions, relationships, boundaries
and constraints are authoritative and SHALL NOT be silently reinterpreted,
replaced, bypassed or contradicted by downstream architecture or
implementation.

Where the architecture does not establish a fact, relationship,
responsibility or decision, that absence SHALL be preserved explicitly
rather than completed through inference, convention, structural
convenience, lexical similarity or anticipated implementation.

Unresolved architecture MAY be accompanied by clearly identified
candidates, suggestions or recommendations, but these SHALL remain
explicitly non-authoritative until explicitly accepted into the
architecture.

Where evidence indicates that authoritative architecture may be incorrect,
incomplete or internally inconsistent, the conflict SHALL be raised
explicitly for architectural review. The existing architecture remains
authoritative until an approved architectural change is made. That
approved change becomes the new authoritative architecture.
```

**Semantic purpose:** Preserve documented authority and distinguish established, unresolved and proposed architecture; explicitly review conflicts instead of manufacturing completeness.

**Affected domains:** Every architecture domain; repository governance, human/AI derivation, documentation and implementation.

**Current canonical references/dependencies:** Explicit source links to Purpose, ADR relationship/review sections and AX-02/04/06/14/15. Canonical Completion Plan, Strategy capability-tier/assurance rules and Domain03 metamodel/semantic sufficiency rely on it. Motivation points to the external register rather than reproducing its normative text.

**Later clarification/specialisation:** Semantic sufficiency, significance-driven edges, explicit unestablished ancestry/Feature relationships and fresh-context authority loading implement AX-17. They do not make reports, prior sessions or graphs sources of authority.

**Conflict/ambiguity concerns:** No conflict in current reviewed rules. The incomplete Motivation catalogue demonstrates a required knowledge dependency outside the canonical corpus. Historical K9–K13 examples do not confine AX-17 to Domain04.

**Implementation/obsolete terminology:** Domain04 Package2 G1/K9–K13 are historical rationale examples. Preserve their role or move only the examples to explicitly non-normative context with approval; do not rewrite the technology-independent Axiom statement.

### Inventory Classification Summary

| Classification | Axioms |
| :--- | :--- |
| Exact semantic migration | AX-02, AX-03, AX-04, AX-07, AX-08, AX-09, AX-10, AX-11, AX-13, AX-14, AX-15 (11). |
| Semantic migration with editorial clarification | AX-01, AX-06, AX-17 (3). |
| Requires human review before migration | AX-05, AX-12, AX-16 (3). |

All seventeen can be preserved semantically from the current source. The three review classifications identify single-authority reconciliation/interpretation decisions, not a finding that their present statements must be rewritten. Editorial treatment is optional and must be demonstrated harmless.

## Proposed Canonical Destination

**Recommended proposed path: docs/markdown/governance/architectural-axioms.md.** It does not exist yet. A small docs/markdown/governance/README.md may supply navigation and the location’s purpose; it must not duplicate the axioms or introduce a new authority hierarchy. “governance” is a cross-domain corpus location, not a newly numbered architectural domain or a claim that the current Domain13 architecture has been completed.

The axioms serve three connected roles: foundational principles, constraints on every downstream domain, and whole-of-architecture authority/review governance. They include specific architectural responsibilities, developer-access obligations and architecture-knowledge uncertainty, not only Motivation-domain concepts. A single cross-domain register makes their governing scope explicit while allowing Motivation to explain their relationship to drivers, goals and requirements.

The existing canonical corpus has a cross-domain governance plan at its root but no existing cross-domain axiom/governance directory. Its Motivation principles directory is an existing foundational-principles location and is a credible alternative: a principle’s file location need not narrow its scope. It is therefore **suitable only if explicitly designated the single whole-of-architecture register**, restored to all 17 current axioms and reconciled with the AX-12 claim and duplicate formulations. It is not rejected merely because it is Motivation. The recommended governance location avoids implying that placement/classification in a Motivation metamodel determines the continued standing of AX-12 or technology-qualified consequences.

| Location considered | Assessment |
| :--- | :--- |
| Existing Motivation principles/architectural-axioms.md | Minimal path churn and established Motivation navigation; requires explicit whole-architecture authority and reconciliation of its incomplete/changed catalogue. Valid alternative requiring a human location decision. |
| New governance/architectural-axioms.md | Preferred: one cross-domain authority, preserves all identifiers independently of domain classification and groups authority/review material with principles. Bounded new directory with minimal navigation. |
| Corpus-root architectural-axioms.md | Also coherent for cross-domain scope and even smaller structurally; less explicit organisation alongside future governance material. No architectural need to create a large directory hierarchy. |
| Strategy or a domain-specific Business/Information governance directory | Unsuitable as the owner of all axioms: would place upstream whole-architecture authority inside a specialising domain. |
| Speculatively numbered Domain13 or an old proposed “02-principles-governance” hierarchy | Not established by the current corpus. A historical plan cannot authorise renumbering or a new domain structure. |

Motivation should retain a **navigation/orientation page linking to the sole register**, with no maintained normative restatement. Existing Motivation mappings and foundational requirements remain where they belong. Location does not itself change semantic authority, approved domain status or the source hierarchy. Root AGENTS.md would designate the new source when the approved migration is completed.

## Later Architectural Rules Assessment

These are assessments of already canonical rules, not grants of new architectural authority.

### A. Semantic Sufficiency Versus Structural Completeness

**Classification: existing axiom consequence and cross-domain governance guardrail, with Domain03 metamodel specialisation.**

AX-17 prohibits invented hierarchy, equivalence and traceability for structural convenience. That does not by itself establish that the agreed requirements have been sufficiently expressed: positive sufficiency still needs evidence against the agreed scope and completion gates. The Completion Plan §§2/9 and Domain03 metamodel §§3.3/3.4/9 establish those governance and Business modelling tests. They permit meaningful absence and reject graph density, symmetry and one-to-one completion while retaining unresolved matters.

The rule applies across documentation domains and constrains derivation, but its anti-fabrication content overlaps AX-17 and its completion criteria belong in governance/metamodel contracts. **No new axiom recommended.** Preserve its existing homes; an explanatory reference from AX-17 could be proposed without redefining AX-17 or adding normative duplication.

### B. Reusable Composition

**Classification: domain-specific Business Architecture metamodel rule, supported by existing axioms and Strategy reuse guardrails.**

Domain03 metamodel §3.6 and Service Delivery’s residual sufficiency decisions establish that an SD-03, SD-08 or SD-14 purpose may be realised through reusable information capabilities and configured workflow/Praxis without a specialised Business Function. The actual required responsibility must be sufficiently realised; composition does not manufacture an individual Feature-to-Function edge.

AX-01/04 favour health-information meaning and appropriate reuse; AX-17 prevents invented decomposition. None alone mandates this precise Business Feature/Function rule. Its metamodel-specific types and composition-level adjudications make it unsuitable as another whole-architecture axiom. **Retain the canonical Business rule and case evidence.**

### C. Business Semantics Versus Lower-Layer Mechanics

**Classification: existing AX-04/AX-14 consequence, expressed as a Business modelling rule and cross-domain layer guardrail.**

Domain03 metamodel §§3.6/6, Business information responsibility §1.3 and Strategy G2 establish responsibility before machinery and preserve layer meaning. HSO Capacity Management is meaningful Business responsibility; operational-state capture may realise it downstream without becoming a generalised replacement Business Function. “Reuse machinery” must not be read as “replace Business responsibilities with generic infrastructure”.

AX-04 retains Harmonia semantics; AX-14 preserves distinctions; AX-17 forbids inferred relationships. Their combined guardrails adequately govern this class of error. **No new axiom recommended.** The report does not establish a common capture mechanism or Information/Application model.

### D. Context-Specific Capacity

**Classification: domain-specific Business rule, with consequences of AX-06/AX-14 and an existing ownership guardrail.**

HSO’s Capacity Management Responsibility Boundary and Business information responsibility §1.5 establish available, committed and utilised capacity individually for clinic/practice, ward, theatre, ED, outpatient and mobile contexts. A bed’s resource state is an input, not a capacity conclusion. Staffing, demand, acuity, commitments and operational constraints may be material; no universal formula, shared schema or identical lifecycle is established.

Higher-order Service Capacity Management consumes/coordinates the locally established capacities. Consumption does not move their originating responsibility. The broader lesson—different semantic concepts and their authority do not become equivalent through shared data or consumption—is already governed by AX-06/14 and Domain03’s ownership rules. **Keep capacity semantics in Business Architecture; no new axiom recommended.**

### E. Clinical-Work Integration Boundary

**Classification: established Harmonia scope/boundary principle and cross-domain guardrail, with a domain-specific Business rule; candidate new axiom status requires human adjudication.**

Domain03 metamodel §3.7 expressly establishes the limit. Service Delivery’s care-enablement principle, HSO’s boundary, Business information responsibility and Strategy’s Twin qualification apply it. Harmonia may exchange work information, route/observe operational activity and coordinate governed consequences without thereby determining clinical work, managing clinical handover, acquiring clinical allocation/decision authority, owning clinical worklists or assuming EMR workflows. Work Order, To Do and synthetic Task remain distinct.

AX-01 explains Harmonia’s information-centric purpose but does not expressly forbid every form of clinical-work management. AX-06 separates originating information authority from technical state, AX-13 terminates management of emitted representations, and AX-14 preserves distinctions. **Those axioms do not, on their own, establish this exact clinical-work scope exclusion.** It would be inaccurate to claim that AX-13’s egress rule or AX-06’s fact credibility already says all of §3.7.

This is the strongest A–F candidate for an explicit scope axiom: it spans Business, Information, Application, Integration, presentation and Twin coordination, remains technology-independent and prevents plausible EMR/worklist authority expansion. However, the existing accepted scope rule and specialising architecture already govern it. **Recommend retaining that structure during migration; ask the human architect whether central axiomatic status would add a needed governing boundary.** If so, commission a separate semantic-change task with approved wording and assessed consequences. Do not append an axiom or broaden AX-01/06/13 during Step 2.

### F. Entity-Specific Digital Twin Coordination

**Classification: Strategy-level rule and execution-model direction; specialisation of AX-16, supported by AX-05/06/14/17.**

The controlling rule is already beside Strategy’s [Digital Twin definition](../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md#entity-specific-operational-coordination). It uses **SHOULD**, with an explicit architectural reason for an exception. AX-16 requires coordinated, observable progression; it does not name Digital Twins in its normative principle. The later rule chooses an existing coordination construct and must not be retrospectively presented as text AX-16 always contained.

Entity coordination neither makes every population/search activity Twin work nor transfers information authority or clinical-work ownership to the Twin. The candidate-Twin test, representation-not-Twin distinction and Ponos/Mneme/Mnemosyne seams remain controlling. **No duplicate/new axiom recommended.** Preserve the rule in Strategy; a navigation cross-reference from AX-16 may be proposed. Runtime queues, thread pools, archetype allocation and execution implementation remain downstream.

### Other Significant Strategy/Business Rules

| Existing rule | Assessment and disposition |
| :--- | :--- |
| Strategy G1: reusable capability does not imply centralised service; distributed participation does not imply distributed semantic responsibility | Cross-domain guardrail refining AX-04/10 and bounded responsibilities. Retain Strategy authority; no new service/topology or axiom. |
| Strategy G2: boundaries follow architectural responsibility, not representation, product or packaging | Cross-domain guardrail applying AX-04/14. Durably useful but adequately governed by existing axioms plus this rule. |
| Strategy G3: information/active state, activity execution and durable establishment are distinct | Specialisation of AX-05/14/16. The current three-responsibility boundary is clearer than Motivation’s generic AX-05 wording. Preserve it without rewriting AX-05. |
| Strategy G4: execution, standards interaction and transport are distinct | Cross-domain boundary guardrail applying AX-02/04/14. Does not introduce another deployment service or axiom. |
| Business function/service ownership, consumption non-transfer and cross-cutting responsibility | Domain03 metamodel/information-responsibility rules, supported by AX-06/14/17. Do not elevate the Business metamodel into an axiom catalogue. |
| Clinical Privilege versus Operational Privilege; source qualification facts versus decisions | Domain-specific responsibility/authority distinctions under AX-06/07/14; no new general axiom. |
| Independent assurance, governance/modelling/Guardianship, evidence sufficiency and separation from operational management | Existing approved REQ-FND-005 and its bounded Strategy/Business derivation add genuine intent not implied by audit/evidence alone. Preserve their accepted foundational requirement and responsibility rules; axiomatic promotion is unnecessary for this migration. No EC-14/Dokimasia allocation is inferred. |

### Axiom Quality Test

| Candidate/generalisation | Multi-domain and downstream constraint | Independent of technology and durable | Prevents plausible incorrect implementations | Existing axiom/domain-rule sufficiency | Recommendation |
| :--- | :--- | :--- | :--- | :--- | :--- |
| Semantic sufficiency over graph density (A) | Yes: derivation and completion across domains. | Yes. | Invented edges/decomposition and false completeness. | AX-17 plus explicit completion/metamodel gates adequately governs it. | Keep governance rule; no additional axiom. |
| Reusable Feature composition (B) | Precise rule is Business metamodel-specific. | Technology-independent, though Praxis is downstream terminology. | Spurious specialised Function proliferation. | Domain03 §3.6 plus AX-04/17 and Strategy reuse. | Keep domain rule. |
| Meaningful responsibility before lower-layer mechanics (C) | Yes. | Yes. | Generic machinery erasing Business meaning. | Already covered by AX-04/14 plus layer/metamodel rules. | Keep guardrail; avoid restatement. |
| Capacity is not resource state (D) | Spans downstream use, but normative concept is service-operations-specific. | Yes as Business meaning. | Equating spare beds/resources with service capacity. | AX-06/14 plus context-specific capacity/ownership rules. | Keep Business rule. |
| Integrating clinical work does not confer clinical-work authority (E) | Yes: several domains and coordination models. | Yes: durable Harmonia scope decision. | EMR authority, clinical worklist/allocation/handover management by inference. | Existing scope rule is adequate; exact prohibition is not fully stated in existing axioms. | Only credible A–F axiom candidate; human decision on need, separate semantic-change task if accepted. |
| Entity-specific Twin coordination (F) | Cross-domain effects, but selects an existing Strategy construct. | No product dependence; specificity is to the adopted coordination model. | Disconnected entity execution, ownership leakage and universal Twin routing. | AX-16 plus the already canonical SHOULD/exception rule suffices. | Keep specialisation; do not duplicate it. |
| Independent assurance | Yes and materially constraining. | Yes. | Self-attestation and confusion of workflow completion/evidence with assurance. | Already approved as REQ-FND-005, with explicit downstream boundaries. | Preserve existing foundational requirement; no automatic axiom promotion. |

No proposed new identifier or new normative axiom statement is supplied. A high quality score is not a reason to duplicate an already effective rule or reclassify a metamodel/requirement.

## Potential Axiom Conflicts or Ambiguities

| Finding | Evidence and architectural significance | Step 2 treatment proposed |
| :--- | :--- | :--- |
| C01 — AX-12 current standing conflicts with canonical reclassification | Central register establishes AX-12; Motivation catalogue/README/reclassified-principles state transferred/non-current and preserve a deliberate catalogue gap. Reports repeat that history but cannot resolve the authority conflict. | Human confirms retention of current AX-12 and removal/demotion of competing current reclassification claims, or separately commissions an axiom amendment. Do not remove AX-12 in a migration. |
| C02 — AX-05 competing normative formulations | Central text names Mneme/Mnemosyne, atomic durable establishment and distinct concurrency domains. Motivation generic text includes transient workflow coordination and omits safeguards; current Strategy separates Ponos execution. | Confirm canonical register uses complete central wording. Demote the duplicate, retaining compatible Motivation orientation without a replacement principle. Do not infer an active/workflow owner. |
| C03 — AX-16 authority reciprocity and “lockstep” implication | External text reproduces an “established Domain01 definition”; both statements agree, but authority points both ways. Implication wording could be overread as synchronous/atomic linkage; later Twin rule includes SHOULD/exceptions and explicit clinical-work exclusion. | One owner after migration; preserve exact current wording and explicitly non-normative realisation. Human review any interpretation/editorial wording change rather than infer a new synchronisation guarantee or universal Twin duty. |
| C04 — Modality, scope and exceptions drift in duplicates | AX-03 native metadata qualification; AX-07 governed/where-practicable scope; AX-10 processing boundary/backlog preference; AX-11 Mneme duty holder; AX-13 emitted representation; AX-15 contract-sensitive uncertainty. | Preserve source qualifications. Do not merge the stronger/different Motivation implications into axioms during migration. |
| C05 — Normative material outside “Axiom” headings | AX-05 active-generation/convergence safeguards; AX-09 Diagnostic observability and evidence promotion; AX-15 safe-retry consequences; §§3–7 subsystem/deployment, FHIR publication, enforcement and explicit review obligations. | Include complete source material or demonstrate lossless editorial reorganisation. A headings-only extract would lose architectural meaning. |
| C06 — Apparent standards/Business model mismatch | AX-03 native standards objects versus Domain03 prohibition on premature FHIR binding. | No contradiction when scopes are preserved: Business conceptual meaning and a standards-defined object representation are different derivation concerns. No redesign needed. |
| C07 — Clinical-work/Twin wording could expand authority | Historical archetype/clinical-workflow descriptions in Strategy precede the current explicit §3.7-linked qualification. | Read current qualification as controlling. Do not reinterpret AX-16 or source fact management as clinical-work authority. Further wording cleanup is separate from source migration. |
| C08 — Source formatting/anchors are brittle | Escaped lists, malformed AX-12 heading, one-line authority diagram and mixed triple-hyphen/em-dash headings. AX-17 contains manually written internal fragments. | Permit mechanical formatting with exact wording, and validate anchors explicitly. Stable AX identifiers are not sufficient for stable Markdown fragments. |

No existing pair of central axioms was established to be mutually contradictory by this bounded review. AX-05 distinguishes durable state authority, AX-06 fact authority, AX-07 permission and AX-17 architecture authority; these should not be collapsed into one use of “authority”. AX-08/09 distinguish durable evidence and transient mechanics; AX-15 and AX-17 distinguish operational uncertainty and architecture uncertainty. AX-02, AX-03 and AX-13 complement internal representation and external management boundaries rather than duplicate identical duties.

The primary demonstrated conflict is between source authority and a competing catalogue/reclassification. The report does not declare historical human approvals invalid; their status versus the expressly current register requires adjudication. Potential interpretation issues are reported as such, not confirmed axiom defects.

## Cross-Reference Inventory

Search covered all 2,285 tracked and non-ignored untracked repository files present at the snapshot, including hidden .junie content and source/tests. A broad rg scan including ignored text, excluding VCS internals and generated target/node_modules/dist/build directories, found **no additional referencing paths**. Binary files were not text-scanned. All counts below precede this new report; the report’s own assessment links are additive evidence, not part of the migration baseline.

Two searches were distinguished: the axiom-document basename, including the duplicate catalogue, and individual AX-01..AX-17 tokens. There are **126 referencing files**: **84 files / 310 matching lines** for the document basename and **119 files / 1,992 matching lines** for identifiers. Counts are matching lines, not unique link/token occurrences. Appendix A lists every file, all basename-reference line numbers, and the identifiers and their matching-line locations.

Path resolution plus exact current-path text identifies **68 files / 219 lines** pointing to docs/architectural-axioms.md and **29 files / 78 lines** pointing to the Motivation catalogue; a line may contain both. Bare filenames, tree examples and patch-relative historical links require contextual interpretation and are marked separately. An explicit old-path citation inside a history/report remains history; counting it does not turn it into authority.

| Class | Reference disposition on eventual migration |
| :--- | :--- |
| Canonical architecture | Retarget live axiom links and authority statements to the single approved register. Keep AX identifiers unchanged. Retarget links to the existing Motivation duplicate where they purport to identify authority, or retain only a clear navigation facade. Correct catalogue/navigation statements affected by confirmed AX-12/AX-16 disposition. Existing reviewed/historical quotations remain identified as evidence. |
| Repository governance/instructions | Root AGENTS.md’s current path and authority hierarchy pointer must change in the migration. docs/AGENTS.md inherits the root and has no direct axiom-path reference; its content requires no mechanical change. The deferred register’s live governing link should change without reconciling its deferred decisions. |
| Execution/report/history | Completed .junie reports, historical plans and recorded diffs should ordinarily retain the paths and wording that were used at the time. Old-path navigation can resolve through the non-authoritative stub. A currently executed plan’s live context pointer must be retargeted when that plan is next commissioned; do not bulk-rewrite historical claims of authority or completion. |
| Implementation/test | Eight source/test files contain AX identifiers, but no old document-path references were found there. Keep identifiers, comments and implementation unchanged. Required tests validate existing guardrails; passing them does not validate documentation semantics. |
| Historical/deferred material | AGENTS-old.md is a retired instruction copy; docs/memory-recovery.md is navigation/history; execution-model material is outside the corpus and subject to deferred reconciliation. Dokimasia orientation points to canonical authority and is supporting orientation. Retarget live support links where appropriate, retain historical references, and mark any apparent current authority claim as non-authoritative rather than migrate unrelated architecture. |

### Required Reference Scope and the Domain04 Boundary

Step 2 must cover the root instruction pointer; Completion Plan live axiom links; Motivation navigation, requirements and duplicate/reclassification pages; Strategy live dependencies; Domain03 metamodel/assurance references; and the live external supporting/governance links identified in Appendix A. Individual AX identifiers and valid historical quotes do not require edits.

Canonical Domain04 has direct references in appointment-scheduling, episode-encounter, order and referral families, assurance investigations, G1/G2 review records and Domain03 traceability. **Mechanical scope boundary:** link-only retargeting can satisfy “canonical references updated” without altering Domain04 architecture. The Step 2 commission should explicitly include this narrow mechanical scope, with text/decision semantics unchanged. If that future commission instead prohibits all Domain04 file edits, retain those links through compatible stub anchors and record direct-reference cleanup as deferred; do not claim all live references were retargeted. This step performs neither form of edit and does not begin Domain04 architecture work.

### Reproduction and Evidence

The baseline was obtained using rg discovery and a Git-file-based line inventory. A reproducible repository search is:

```bash
rg --hidden --no-ignore -n -g '!.git/**' -g '!**/target/**' \
  -g '!**/node_modules/**' -g '!**/dist/**' -g '!**/build/**' \
  'architectural-axioms\.md|AX-(0[1-9]|1[0-7])\b' .
```

The complete pre-task file hashes, raw reference lines and preliminary old/catalogue classification were captured under /tmp/harmonia-axioms-step1-start.json, /tmp/harmonia-axioms-step1-references.json and /tmp/harmonia-axioms-step1-classified-paths.json. These are ephemeral audit aids. Appendix A is the durable in-report inventory; neither raw dumps nor a prior plan provides authority.

## Competing/Duplicated Authority

1. **Canonical Motivation axioms:** fifteen main entries are present; twelve reproduce the central principle wording with formatting changes, while AX-05/07/11 are rewritten. AX-12 is omitted, AX-17 is external-only, and implications contain additional omissions/stronger readings described above. The page calls itself highest-level design authority and prescribes a three-tier presentation as “platform governance”. No requirement to impose that format on the complete central register has been established by this assessment.
2. **Canonical reclassified-principles.md:** reproduces AX-12’s principle and asserts a current transferred/non-foundational status. Its current-authority claim conflicts with this task’s explicitly authoritative register. It must not remain an apparently active alternative classification after migration.
3. **Motivation README/navigation:** endorses both the competing three-tier catalogue and AX-12 reclassification, while linking AX-17 to external authority. Correcting only the central path would leave the conflict discoverable and apparently current.
4. **Central AX-16:** reproduces Motivation AX-16 and calls the Domain01 definition established. Consolidation must remove reciprocal normative ownership, even though the principle wording agrees.
5. **Canonical review records:** AX-05 and Domain04 assurance/G1/G2 records contain quoted prior/current formulations. They are assessment/decision evidence in their stated scope, not replacement axiom registers. Keep quotations clearly attributed; replace only live governing/navigation links where authorised.
6. **Execution/history:** .junie plans, completed reports, diffs and AGENTS-old.md contain copied axioms, competing historical reclassification claims or authority pointers. Root AGENTS.md explicitly denies them architectural authority. Preserve history and ensure retirement/navigation cannot imply otherwise.
7. **Root architectural guardrails:** contain derived invariant text and the source pointer. These are mandated subordinate safeguards, not a replacement register. Update the designation, not the invariant wording. Domain03 README’s phrase “Core Architectural Axioms Governed in Domain 03” labels local principles without AX identifiers; clarify its relationship only in a separately authorised editorial cleanup if needed, rather than treating these as newly numbered axioms.

The eventual register should distinguish normative principle/consequences from rationale/examples and non-normative realisation without deleting an existing SHALL/SHOULD/MAY obligation. Compatible domain specialisations remain canonical in their own locations. A pointer-only Motivation facade is preferable to another maintained catalogue, even if its copied text initially matches.

## Retirement Strategy

**Recommend replacing docs/architectural-axioms.md with a short, explicitly non-authoritative redirect/pointer after successful canonical migration and validation.**

The stub should say that the axioms’ authoritative maintained text has moved to the single canonical register, provide that link, and contain no axiom definitions, current normativity claim or abbreviated rules. This follows the Completion Plan distinction between canonical knowledge and supporting navigation. It preserves many historical links while ensuring no required architectural knowledge remains solely there.

For fragment stability, the same short stub may include empty legacy anchor markers mapped to register-entry links, using the **actual fragments inventoried and renderer-validated**, without copying normative text. That is navigational compatibility, not a second catalogue. The human retirement decision should approve this small allowance; otherwise old fragment links may land only at a file-level pointer and validation must report that limitation.

Retire the existing Motivation normative duplicate at the same boundary by converting it to orientation/navigation, retaining anchor-level entry links where useful. Demote or replace its AX-12 reclassification claim according to the explicit decision. Historical text may remain in version control; creating another historical axiom file is unnecessary.

Removal would give a clean file inventory but needlessly break historical links. Moving the old text into a non-authoritative history directory would preserve a static copy but add a discoverable third catalogue; Git history and attributed existing evidence already provide history. Neither is preferred here. Do not use a symlink or an automatically mirrored normative copy as “retirement”: it conceals authority/location and leaves duplicate maintenance paths.

Retirement is performed only after the destination and current-authority pointers are ready in the same reviewed change and semantic/link validation succeeds. A short-lived migration worktree may hold both files while work is prepared, but the delivered repository state must have one authority.

## Proposed Migration Procedure

1. **Human adjudication and bounded commission.** Decide the destination; approve full preservation of all 17 current statements and normative supporting material; resolve AX-12 competing classification, AX-05 duplicate treatment and AX-16 single-owner/implication treatment. Approve the old-file/Motivation navigation stubs and explicitly define Domain04 link-only scope. New axioms and semantic revisions remain excluded.
2. **Capture the actual Step 2 baseline.** Re-read root/scoped instructions and current authority; hash all affected files and enumerate the 17 entries, every normative clause/modality and supporting section. Refresh Appendix A searches against then-current working-tree content; do not assume this assessment’s line numbers survive intervening user edits.
3. **Prepare the single canonical register.** Incorporate the complete source, including Purpose/authority hierarchy, every axiom’s rationale/consequences/exclusions, AX-05 generation safeguards, AX-09 diagnostics, AX-16 non-normative realisation, and §§3–7 responsibilities, FHIR boundary, enforcement and review/ADR obligations. Keep identifiers, substantive titles and semantics. Any reorganisation needs a clause-to-destination mapping rather than omission. Limit editorials to the approved list.
4. **Make authority unambiguous.** Designate the destination from root AGENTS.md and canonical governance/navigation. Convert Motivation’s register into a non-authoritative orientation page; reconcile its reclassification page/README only as approved. Retain requirement/Strategy/Business meanings and status.
5. **Retarget live dependencies.** Update old-register and duplicate-register links according to Appendix A and refreshed search. Preserve identifier references, recorded quotations and history. Apply only expressly authorised Domain04 link changes. Supporting guides may point into the canonical corpus without importing unrelated implementation decisions.
6. **Retire the external register.** Replace with the agreed pointer and validated legacy fragment navigation; no normative axiom body remains at the external location. Preserve the single-authority state across both path redirects.
7. **Validate semantics, authority, links and conformance.** Run the plan below with bounded waits; inspect every unresolved check. AX-17 requires stopping for a material discrepancy instead of resolving it through familiar architecture or historical report claims.
8. **Report and stop.** Record exact files changed, every approved editorial, statement/clause comparison, link outcomes, tests and uncertainty. Update only authorised migration/governance status notes; do not declare final programme corpus closure while other required non-canonical dependencies remain. Do not begin Domain04 semantic reconciliation, Domain05, runtime work or a candidate-axiom task.

## Validation Plan

### Eventual Step 2 Acceptance Checks

| Check | Required evidence |
| :--- | :--- |
| AX-01..AX-17 complete and identifiers stable | Ordered catalogue and identifier-set equality: exactly 17 current entries, including AX-12, with no additions/removals/renumbering. Preserve established titles or show separately approved title-only editorial differences. |
| Meaning and normative statements preserved | Clause-by-clause comparison of source and destination; exact baseline text by default. For each approved editorial, demonstrate unchanged duty holder, subject, scope, modality, exceptions and authority. Human semantic review is required; a text hash or test result alone is insufficient. |
| Supporting normative content preserved | Coverage map for all rationale/consequences/exclusions and §§1, 3–7; explicitly verify AX-05 generation safeguards, AX-09 observability/promotion, AX-15 safe retry and AX-16 non-normative realisation labels. Search SHALL/SHALL NOT/SHOULD/SHOULD NOT/MAY, but do not assume keyword counts alone establish semantics. |
| Canonical references updated | Refresh file/path/fragment inventory; all live authority references point to the approved register. Remaining old paths are classified history or deliberate compatibility navigation. No identifier-only source/test edits are needed. |
| One maintained authority | External old file and Motivation duplicate contain pointers/orientation only; current AX-12 reclassification conflict resolved by the recorded decision. Root instructions, navigation and canonical review links agree. No generated/symlink/copy becomes another authority. |
| Markdown references/anchors resolve | Validate changed documents and all incoming local axiom links, including explicit legacy/current anchors and relative paths. Check fences and actual renderer heading IDs; preserve or deliberately bridge renamed fragments. Capture unrelated pre-existing defects rather than silently repair them. |
| Architecture suite passes | Run the required architecture test command with a bounded timeout, capture test totals/exit/result and warnings. If environment stalls, diagnose and report TIMEOUT/STALLED/UNRESOLVED rather than change production code or repeatedly retry unchanged. |
| Canonical-corpus invariant for migrated material | All knowledge currently owned by the axiom register is maintained under /docs/markdown; source/readme stubs and reports outside it supply no sole required axiom content. Remaining ADR/deferred material prevents a claim that this task alone completes the entire corpus programme. |
| Domain04 unchanged architecturally | Byte equality if no file edits are commissioned; otherwise diff proves only approved path/fragment changes, with all wording, decisions, statuses and models unchanged. No Domain04 reconciliation starts. |
| Implementation and tests unchanged | Task-baseline comparison shows no Java, POM, runtime/configuration, implementation or test-source mutation. Required tests execute existing code. |
| Domain status and later steps preserved | No Domain01 freeze/refreeze change, Business responsibility/Feature renaming, EC-14 allocation, Domain05 derivation or runtime work. Report completion at the migration boundary only. |

Required architecture command (offline mode is an environmental option, not a replacement of test selection):

```bash
timeout --signal=TERM --kill-after=10s 300s mvn test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

The proposed bound is five minutes with forced termination after a ten-second grace; investigate unit/architecture test inactivity at approximately two minutes. Any different expected wait requires observed evidence and a reported reason.

### Step 1 Verification Actually Executed

**PASS — required architecture suite.** Executed the required reactor/test selection offline with a five-minute command bound:

```bash
timeout --signal=TERM --kill-after=10s 300s mvn -o test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Maven completed with **BUILD SUCCESS**, **90 tests**, **0 failures**, **0 errors**, **0 skipped**, total time **26.614 seconds** and command exit **0**. No timeout or stall occurred. Log: /tmp/harmonia-axioms-step1-architecture-tests.log, ephemeral execution evidence. ArchUnit 1.3.0 reported unsupported Java 25 class-file-major-69 imports and fell back to simple import for affected JDK classes; deprecated Unsafe warnings also occurred. The suite passed its assertions, with that JDK import-inspection limitation. It establishes existing code guardrail results, not semantic correctness of this report or eventual migration.

**PASS — bounded report/content/reference validation.** A 30-second-bound Python check completed with exit 0: all **13 required report sections**, all **17 verbatim current Axiom subsection excerpts**, all **126 reference-inventory files**, **153 local report links** including the referenced Twin anchor, and balanced Markdown fences were positively checked. It also verified that all **2,285 task-baseline repository files** retain their SHA-256 and that the only added repository file is this report. Thus existing user changes, axioms, references, all Domain04 files and implementation/test source remain untouched. Checker and result: /tmp/harmonia-axioms-step1-check.py and /tmp/harmonia-axioms-step1-check-result.json, ephemeral evidence.

**PASS — whitespace validation.** git diff --check and the report-specific whitespace check completed successfully. No migration acceptance result is claimed: destination creation, competing-authority retirement and incoming-link retargeting belong to the separately authorised Step 2.

## Human Decisions Required

| Decision | Recommendation and exact boundary |
| :--- | :--- |
| H01 — Canonical destination | Approve docs/markdown/governance/architectural-axioms.md as the single cross-domain register, or explicitly choose the existing Motivation path with whole-architecture designation and full reconciliation. No directory is created in Step 1. |
| H02 — Full semantic preservation and editorials | Preserve AX-01..17 and all normative supporting clauses. Permit mechanical list/heading/diagram/reference formatting only with reviewed semantic equality. Do not impose a three-tier rewrite or generalise named responsibilities as a migration convenience. |
| H03 — AX-12 competing status | Confirm current AX-12 remains an axiom and authorise the minimal correction/demotion of Motivation’s transferred/non-current claim. If actual removal/reclassification is wanted, decide it in a separate material axiom-change task before migration scope is revised. |
| H04 — AX-05 and AX-16 consolidation | Confirm central AX-05 wording/safeguards control the canonical register; approve demotion of its generic duplicate. Confirm AX-16 has one normative owner, keep existing implication meaning and non-normative realisation, and separately adjudicate any proposed “lockstep” clarification rather than infer it. |
| H05 — A–F/new axiom disposition | Retain A–D/F at their existing canonical levels; retain E’s established clinical-work boundary. Decide whether E needs future axiomatic status. Recommend no new axiom in migration Step 2. |
| H06 — Retirement/link compatibility | Approve the short non-authoritative external pointer and Motivation orientation facade, with validated legacy anchor navigation and no copied normative body; preserve completed history. |
| H07 — Mechanical reference scope | Explicitly permit live path/fragment retargeting across canonical documents, including Domain04, while prohibiting any Domain04 architectural change. Otherwise record deferred direct-link cleanup instead of claiming full retargeting. |
| H08 — Bounded Step 2 authority | Authorise the exact migration/reference/retirement scope after the above decisions; no HSO-16 name correction, broader Strategy cleanup, new axiom, implementation or downstream architecture work. |

The decisions above are unresolved proposals, not a request to interrupt this assessment or an implied approval. Existing axioms remain authoritative until the separately approved migration/change is delivered.

## Recommended Step 2

Commission **Canonical Architectural Axioms Migration — Step 2: Semantic-Preserving Canonicalisation and Authority Reconciliation**, after H01–H08 are decided.

Its deliverable should be one authoritative register containing every current axiom and normative supporting clause; reconciled navigation/authority references; a non-authoritative old-path pointer; demoted Motivation duplication; and a bounded completion report with positive semantic, link and conformance evidence. Unapproved semantic refinements and candidate axioms remain separate future work.

The assessment report is complete. The design awaits human adjudication. No migration or later architectural step has commenced.

## Appendix A — Complete Pre-Migration Reference Inventory

Paths below are repository-relative and linked from this report. **C** = canonical architecture (including canonical review evidence); **G** = repository governance/instructions, including the currently governing external axiom register; **E** = execution/report/history; **I** = implementation/test; **H** = historical/deferred/supporting material. Canonical review/history quotations remain evidence, even where the containing file is canonical.

“Doc lines” gives all lines containing the axiom-document basename, covering both the external register and Motivation duplicate. “AX lines” gives all identifier-match lines, with consecutive lines compressed as ranges. The “AX identifiers” column compresses only contiguous sets and records precisely which identifiers occur; it does not authorise renumbering. Every bare/context-sensitive path occurrence is included. Use the disposition rules above rather than a global basename replacement.

| Class | Referencing files |
| :--- | ---: |
| C | 38 |
| G | 3 |
| E | 73 |
| I | 8 |
| H | 4 |

| File | Class | Doc lines | AX identifiers | AX lines |
| :--- | :---: | :--- | :--- | :--- |
| [.junie/plans/canonical-domain-01-motivation.md](<../../.junie/plans/canonical-domain-01-motivation.md>) | E | 114 | AX-01, AX-07, AX-11..AX-13, AX-16 | 38, 64, 126, 149, 192, 214, 235 |
| [.junie/plans/create-deferred-document-register.md](<../../.junie/plans/create-deferred-document-register.md>) | E | — | AX-04, AX-07 | 43–44 |
| [.junie/plans/docker-compose-build-step2.md](<../../.junie/plans/docker-compose-build-step2.md>) | E | 151 | — | — |
| [.junie/plans/domain-01-final-cleanup.md](<../../.junie/plans/domain-01-final-cleanup.md>) | E | 79, 172, 251 | AX-01, AX-05, AX-07, AX-11..AX-13, AX-16 | 46, 59, 100, 128, 136, 172, 194, 234, 251, 259 |
| [.junie/plans/domain-02-strategy-analysis.md](<../../.junie/plans/domain-02-strategy-analysis.md>) | E | — | AX-01..AX-16 | 178–179, 233, 267–277, 285, 291, 294, 297, 352, 444 |
| [.junie/plans/domain04-information-architecture-foundation.md](<../../.junie/plans/domain04-information-architecture-foundation.md>) | E | 184 | AX-01 | 184 |
| [.junie/plans/enforce-pylai-external-fhir-publication-boundary.md](<../../.junie/plans/enforce-pylai-external-fhir-publication-boundary.md>) | E | — | AX-04..AX-05, AX-13 | 8, 45, 62, 177, 208 |
| [.junie/plans/governed-provider-registry-writes.md](<../../.junie/plans/governed-provider-registry-writes.md>) | E | — | AX-05 | 10 |
| [.junie/plans/harmonia-architectural-axiom-conformance-assessment.md](<../../.junie/plans/harmonia-architectural-axiom-conformance-assessment.md>) | E | 11–12, 20 | AX-01..AX-13 | 18, 24–36, 65, 67, 71, 73, 77, 79, 83, 85, 89, 91, 95, 101, 103, 107, 109, 113, 115, 119, 131–150, 197–199, 204–207, 212–213, 219–220 |
| [.junie/plans/harmonia-docs-restructure-plan.md](<../../.junie/plans/harmonia-docs-restructure-plan.md>) | E | 71, 182, 227–228, 258, 422, 625, 659, 897, 913, 927, 929 | AX-01, AX-05, AX-15 | 71, 258, 625, 635 |
| [.junie/plans/harmonia-documentation-restructuring-architecture.md](<../../.junie/plans/harmonia-documentation-restructuring-architecture.md>) | E | 28, 250 | AX-01, AX-05, AX-07, AX-13, AX-15 | 49, 67, 75, 249, 251–252 |
| [.junie/plans/m1-stable-docker-baseline.md](<../../.junie/plans/m1-stable-docker-baseline.md>) | E | — | AX-05 | 28, 58, 146 |
| [.junie/plans/m2-1-mneme-authoritative-http-client.md](<../../.junie/plans/m2-1-mneme-authoritative-http-client.md>) | E | — | AX-01, AX-05, AX-07, AX-13 | 39–41, 80, 225, 294–297 |
| [.junie/plans/m2-2-distributed-authoritative-docker-path.md](<../../.junie/plans/m2-2-distributed-authoritative-docker-path.md>) | E | 8 | — | — |
| [.junie/plans/m2-3-service-identity-evidence-base.md](<../../.junie/plans/m2-3-service-identity-evidence-base.md>) | E | — | AX-01, AX-05 | 73, 173 |
| [.junie/plans/m2-4-distributed-authoritative-path-verification.md](<../../.junie/plans/m2-4-distributed-authoritative-path-verification.md>) | E | — | AX-01, AX-05, AX-13 | 46, 48, 51 |
| [.junie/plans/m3-2-active-hegemon-alignment.md](<../../.junie/plans/m3-2-active-hegemon-alignment.md>) | E | — | AX-05, AX-14..AX-15 | 27, 43–45, 70–72, 100 |
| [.junie/plans/managed-information-runtime-ownership-assessment.md](<../../.junie/plans/managed-information-runtime-ownership-assessment.md>) | E | — | AX-01, AX-05..AX-06, AX-13 | 231, 256–259, 273, 408, 425, 1968 |
| [.junie/plans/mneme-managed-information-boundary.md](<../../.junie/plans/mneme-managed-information-boundary.md>) | E | — | AX-05 | 8 |
| [.junie/plans/mneme-mnemosyne-boundary-assessment.md](<../../.junie/plans/mneme-mnemosyne-boundary-assessment.md>) | E | 42 | AX-01..AX-02, AX-04..AX-07, AX-10..AX-13 | 11, 43–52, 223, 228, 347, 413, 425, 437, 450, 461 |
| [.junie/plans/pylai-trusted-ingress-security.md](<../../.junie/plans/pylai-trusted-ingress-security.md>) | E | — | AX-07, AX-13 | 10, 56, 72, 79 |
| [.junie/plans/recover-m3-1-governed-read.md](<../../.junie/plans/recover-m3-1-governed-read.md>) | E | 21 | AX-05, AX-14..AX-15 | 23, 29, 36, 107, 114 |
| [.junie/plans/revise-domain04-package2-plan.md](<../../.junie/plans/revise-domain04-package2-plan.md>) | E | 67 | AX-01..AX-08, AX-13..AX-15 | 94–102, 444 |
| [.junie/plans/strategy-authoring-pass-b.md](<../../.junie/plans/strategy-authoring-pass-b.md>) | E | — | AX-01..AX-11, AX-13..AX-16 | 42, 120, 122, 124, 126, 128, 130, 295, 354 |
| [.junie/plans/strategy-pass-c-value-streams.md](<../../.junie/plans/strategy-pass-c-value-streams.md>) | E | — | AX-10, AX-15..AX-16 | 13, 24, 39, 53–54, 58–59, 65, 77, 86, 108–109, 134 |
| [.junie/plans/validate-mneme-tier.md](<../../.junie/plans/validate-mneme-tier.md>) | E | 26 | AX-05 | 31, 55, 78 |
| [.junie/plans/validate-mnemosyne-persistence-tier.md](<../../.junie/plans/validate-mnemosyne-persistence-tier.md>) | E | 34 | AX-05 | 54 |
| [.junie/reports/2026-10-04-domain-01-final-cleanup.md](<../../.junie/reports/2026-10-04-domain-01-final-cleanup.md>) | E | 11, 40, 64 | AX-01, AX-07, AX-11..AX-13, AX-16 | 25, 64 |
| [.junie/reports/2026-10-04-domain-02-strategy-analysis.md](<../../.junie/reports/2026-10-04-domain-02-strategy-analysis.md>) | E | 305, 323 | AX-01..AX-11, AX-13..AX-16 | 46, 54, 56, 124, 126, 172, 190, 209, 217, 229, 247, 255, 265, 275, 283, 299, 305, 317, 323, 325, 334, 351, 382, 384, 413, 415, 425, 427–428, 437, 439–440, 449, 451, 461, 463, 538, 541, 544, 547, 550, 553, 622, 643 |
| [.junie/reports/2026-10-05-strategy-authoring-pass-a1-reconciliation.md](<../../.junie/reports/2026-10-05-strategy-authoring-pass-a1-reconciliation.md>) | E | 53 | — | — |
| [.junie/reports/2026-10-05-strategy-reconciliation.md](<../../.junie/reports/2026-10-05-strategy-reconciliation.md>) | E | — | AX-01..AX-03, AX-05..AX-06, AX-10, AX-13, AX-16 | 48, 184, 226–228, 235, 243, 266–268, 272, 329–330, 332 |
| [.junie/reports/2026-10-06-domain04-entity-identity-service-information-families.md](<../../.junie/reports/2026-10-06-domain04-entity-identity-service-information-families.md>) | E | 29 | — | — |
| [.junie/reports/2026-10-06-domain04-information-architecture-foundation.md](<../../.junie/reports/2026-10-06-domain04-information-architecture-foundation.md>) | E | 24 | — | — |
| [.junie/reports/2026-10-06-strategy-authoring-pass-b.md](<../../.junie/reports/2026-10-06-strategy-authoring-pass-b.md>) | E | — | AX-01..AX-11, AX-13..AX-16 | 7, 56, 60, 63, 83, 85, 87, 89, 91, 93, 160 |
| [.junie/reports/2026-10-06-strategy-authoring-pass-c.md](<../../.junie/reports/2026-10-06-strategy-authoring-pass-c.md>) | E | — | AX-01..AX-11, AX-13..AX-16 | 7, 35, 71, 90–93, 206 |
| [.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md](<../../.junie/reports/2026-10-07-domain04-package2-evidence-baseline.md>) | E | 42, 2299, 2313 | AX-01..AX-02, AX-05, AX-07, AX-13..AX-14 | 105–109, 1388, 2169 |
| [.junie/reports/2026-10-07-domain04-package2-g1-reconciliation-closure.md](<../../.junie/reports/2026-10-07-domain04-package2-g1-reconciliation-closure.md>) | E | 59–60, 110, 118 | AX-01..AX-04, AX-06, AX-12, AX-14..AX-17 | 11, 15, 19, 31, 49, 51, 53, 55, 59–60, 62, 68, 85, 91, 98, 103, 208, 212 |
| [.junie/reports/2026-10-08-ax05-domain02-strategy-approval-closure.md](<../../.junie/reports/2026-10-08-ax05-domain02-strategy-approval-closure.md>) | E | — | AX-05, AX-12, AX-16..AX-17 | 1, 5, 24, 28, 38 |
| [.junie/reports/2026-10-08-ax05-domain02-strategy-reconciliation.md](<../../.junie/reports/2026-10-08-ax05-domain02-strategy-reconciliation.md>) | E | 50–51 | AX-05, AX-12, AX-14..AX-17 | 1, 3, 9, 13, 15, 50–51, 78, 127, 129 |
| [.junie/reports/2026-10-08-capability-metamodel-deferred-register-update.md](<../../.junie/reports/2026-10-08-capability-metamodel-deferred-register-update.md>) | E | 31 | AX-17 | 31 |
| [.junie/reports/2026-10-08-dokimasia-assurance-architectural-capture.md](<../../.junie/reports/2026-10-08-dokimasia-assurance-architectural-capture.md>) | E | 38 | AX-04..AX-06, AX-12..AX-13, AX-16 | 38, 45 |
| [.junie/reports/2026-10-08-dokimasia-r1-motivation-traceability-ax05.md](<../../.junie/reports/2026-10-08-dokimasia-r1-motivation-traceability-ax05.md>) | E | 52 | AX-05, AX-07..AX-08, AX-12, AX-15..AX-16 | 1, 26, 32, 37, 43–45, 50, 52, 62, 66, 68, 70, 84, 90 |
| [.junie/reports/2026-10-08-domain01-independent-assurance-approval-refreeze.md](<../../.junie/reports/2026-10-08-domain01-independent-assurance-approval-refreeze.md>) | E | — | AX-05, AX-12, AX-16 | 30 |
| [.junie/reports/2026-10-08-domain01-independent-assurance-reconciliation.md](<../../.junie/reports/2026-10-08-domain01-independent-assurance-reconciliation.md>) | E | — | AX-05..AX-09, AX-12, AX-14..AX-17 | 53, 55–56, 58, 68–71, 84, 91–92, 115 |
| [.junie/reports/2026-10-08-domain02-business-capability-reconciliation.md](<../../.junie/reports/2026-10-08-domain02-business-capability-reconciliation.md>) | E | 42 | AX-05, AX-08, AX-14..AX-15, AX-17 | 42, 69, 80, 82, 118 |
| [.junie/reports/2026-10-08-domain02-health-service-assurance-strategy-reconciliation.md](<../../.junie/reports/2026-10-08-domain02-health-service-assurance-strategy-reconciliation.md>) | E | 11, 19–20 | AX-05..AX-10, AX-14..AX-15, AX-17 | 11, 23, 85, 105 |
| [.junie/reports/2026-10-08-domain03-assurance-business-architecture.md](<../../.junie/reports/2026-10-08-domain03-assurance-business-architecture.md>) | E | 13 | AX-05..AX-09, AX-14..AX-15, AX-17 | 26–27, 29–32, 60 |
| [.junie/reports/2026-10-08-domain03-assurance-processes-services-interactions.md](<../../.junie/reports/2026-10-08-domain03-assurance-processes-services-interactions.md>) | E | 13 | AX-06, AX-14..AX-15, AX-17 | 13, 31 |
| [.junie/reports/2026-10-08-domain03-assurance-roles-functions.md](<../../.junie/reports/2026-10-08-domain03-assurance-roles-functions.md>) | E | 13 | AX-06, AX-08, AX-14..AX-15, AX-17 | 13, 30 |
| [.junie/reports/2026-10-08-domain04-package2-g2-semantic-derivation-block1.diff](<../../.junie/reports/2026-10-08-domain04-package2-g2-semantic-derivation-block1.diff>) | E | 134, 247, 363, 496, 511, 697 | AX-01..AX-04, AX-06, AX-08, AX-12..AX-17 | 134, 247, 363, 469, 496, 511, 638, 656, 658, 687, 697, 767 |
| [.junie/reports/2026-10-08-domain04-package2-g2-semantic-derivation-block1.md](<../../.junie/reports/2026-10-08-domain04-package2-g2-semantic-derivation-block1.md>) | E | 25 | AX-01..AX-04, AX-06, AX-08, AX-12..AX-17 | 15, 25, 95 |
| [.junie/reports/2026-10-09-ai-context-independence-guardrail.md](<../../.junie/reports/2026-10-09-ai-context-independence-guardrail.md>) | E | 72 | AX-17 | 30, 33, 72, 88, 112, 151, 154, 190 |
| [.junie/reports/2026-10-09-architecture-completion-plan.md](<../../.junie/reports/2026-10-09-architecture-completion-plan.md>) | E | 103 | AX-17 | 56, 66, 68, 103 |
| [.junie/reports/2026-10-09-dokimasia-runtime-ai-orientation.md](<../../.junie/reports/2026-10-09-dokimasia-runtime-ai-orientation.md>) | E | — | AX-04..AX-06, AX-14, AX-17 | 13, 23 |
| [.junie/reports/2026-10-09-domain02-strategy-cleanup-assessment.md](<../../.junie/reports/2026-10-09-domain02-strategy-cleanup-assessment.md>) | E | 27, 95, 193, 221, 235, 249, 263, 291, 319, 333, 347, 361, 375, 389, 403, 417, 431, 445, 459, 473, 515, 529, 543, 557 | AX-01..AX-11, AX-13..AX-17 | 21, 27–28, 33, 37, 85, 93–96, 193, 221, 234–236, 249, 263, 291, 309, 317–320, 333, 347, 361, 375, 389, 403, 417, 431, 443, 445–446, 459, 473, 515, 528–529, 543, 554, 557–558, 681–682, 721, 736, 743, 756, 815, 821 |
| [.junie/reports/2026-10-09-domain02-strategy-cleanup-step2.md](<../../.junie/reports/2026-10-09-domain02-strategy-cleanup-step2.md>) | E | 80, 117, 219, 239, 273 | AX-03..AX-08, AX-13..AX-17 | 26, 37–42, 44, 80, 101, 117, 120–122, 126, 170–172, 217, 219, 233, 238, 243, 245, 263, 273, 277, 312, 431, 445 |
| [.junie/reports/2026-10-09-domain03-business-architecture-completion-step2.md](<../../.junie/reports/2026-10-09-domain03-business-architecture-completion-step2.md>) | E | 21, 202 | AX-05, AX-14..AX-17 | 21, 109 |
| [.junie/reports/2026-10-09-domain03-business-architecture-completion-step3.md](<../../.junie/reports/2026-10-09-domain03-business-architecture-completion-step3.md>) | E | 21, 225 | AX-01, AX-04..AX-06, AX-14..AX-17 | 21, 33, 206 |
| [.junie/reports/2026-10-09-domain03-business-architecture-step1-assessment.md](<../../.junie/reports/2026-10-09-domain03-business-architecture-step1-assessment.md>) | E | 23, 58, 61, 305 | AX-01..AX-02, AX-04, AX-08..AX-09, AX-13, AX-15..AX-17 | 27, 289, 305, 318, 335, 449, 469, 478, 519, 539, 589, 699, 717, 736 |
| [.junie/reports/Harmonia - Architectural Axiom Conformance Assessment.md](<../../.junie/reports/Harmonia%20-%20Architectural%20Axiom%20Conformance%20Assessment.md>) | E | 11–12, 20, 231, 246, 272, 365, 387–388, 396, 607–608, 616, 827, 842, 997, 1017, 1027–1028, 1036, 1247–1248, 1256, 1465, 1488, 1680–1681, 1689, 1900–1901, 1909, 2123, 2148, 2170, 2355, 2497, 2504, 2511, 2530, 2547, 2581, 2603, 2605, 2788, 2942, 3076–3077, 3085 | AX-01..AX-13 | 18, 24–36, 65, 67, 71, 73, 77, 79, 83, 85, 89, 91, 95, 101, 103, 107, 109, 113, 115, 119, 131–150, 197–199, 204–207, 212–213, 219–220, 231, 256–260, 272, 286, 295, 306, 315, 325, 333–335, 352, 361–363, 365, 394, 400–412, 441, 443, 447, 449, 453, 455, 459, 461, 465, 467, 471, 477, 479, 483, 485, 489, 491, 495, 507–526, 573–575, 580–583, 588–589, 595–596, 614, 620–632, 661, 663, 667, 669, 673, 675, 679, 681, 685, 687, 691, 697, 699, 703, 705, 709, 711, 715, 727–746, 793–795, 800–803, 808–809, 815–816, 827, 852–857, 869, 885, 893, 904, 906, 916, 934, 936, 944, 955–956, 958, 966–969, 986, 994, 996–997, 1004–1006, 1008, 1012, 1034, 1040–1052, 1081, 1083, 1087, 1089, 1093, 1095, 1099, 1101, 1105, 1107, 1111, 1117, 1119, 1123, 1125, 1129, 1131, 1135, 1147–1166, 1213–1215, 1220–1223, 1228–1229, 1235–1236, 1254, 1260–1272, 1301, 1303, 1307, 1309, 1313, 1315, 1319, 1321, 1325, 1327, 1331, 1337, 1339, 1343, 1345, 1349, 1351, 1355, 1367–1386, 1433–1435, 1440–1443, 1448–1449, 1455–1456, 1469, 1498–1502, 1516, 1528–1529, 1541, 1563, 1565, 1567, 1574, 1598, 1601–1602, 1604, 1606, 1617, 1619, 1621, 1629–1631, 1655, 1657, 1665, 1668, 1687, 1693–1705, 1734, 1736, 1740, 1742, 1746, 1748, 1752, 1754, 1758, 1760, 1764, 1770, 1772, 1776, 1778, 1782, 1784, 1788, 1800–1819, 1866–1868, 1873–1876, 1881–1882, 1888–1889, 1907, 1913–1925, 1954, 1956, 1960, 1962, 1966, 1968, 1972, 1974, 1978, 1980, 1984, 1990, 1992, 1996, 1998, 2002, 2004, 2008, 2020–2039, 2086–2088, 2093–2096, 2101–2102, 2108–2109, 2134, 2156, 2159–2164, 2168, 2174–2186, 2214, 2221, 2228, 2234, 2242, 2251, 2259, 2265, 2273, 2279, 2288, 2294, 2305, 2310–2311, 2319, 2324, 2331, 2336, 2343, 2359–2378, 2384, 2457, 2459–2462, 2504, 2511–2518, 2530, 2568, 2589, 2592–2597, 2601, 2607–2619, 2647, 2655, 2662, 2668, 2676, 2685, 2693, 2699, 2707, 2713, 2722, 2728, 2737, 2742–2743, 2751, 2757, 2764, 2769, 2776, 2792–2811, 2817, 2890, 2892–2895, 2900, 2902, 2904, 2906, 2908, 2910, 2943, 2962, 3007, 3018, 3083, 3089–3101, 3130, 3132, 3136, 3138, 3142, 3144, 3148, 3150, 3154, 3156, 3160, 3166, 3168, 3172, 3174, 3178, 3180, 3184, 3196–3215, 3262–3264, 3269–3272, 3277–3278, 3284–3285 |
| [.junie/reports/Harmonia - Docker Container - Mneme Managed-Information Tier.md](<../../.junie/reports/Harmonia%20-%20Docker%20Container%20-%20Mneme%20Managed-Information%20Tier.md>) | E | 235, 439, 752, 956, 1041, 1378, 1819, 2218 | AX-05 | 14, 133, 240, 264, 287, 444, 468, 491, 757, 781, 804, 961, 985, 1008, 1046, 1070, 1093, 1383, 1407, 1430, 1824, 1848, 1871, 2070, 2102, 2153, 2223, 2247, 2270 |
| [.junie/reports/Harmonia - Docker Continer - Mnemosyne Persistence tier.md](<../../.junie/reports/Harmonia%20-%20Docker%20Continer%20-%20Mnemosyne%20Persistence%20tier.md>) | E | 34, 207, 482, 655, 975, 1148, 1499 | AX-05 | 54, 227, 502, 675, 995, 1168, 1519 |
| [.junie/reports/Harmonia - Goal 2 - Step 3.3 - Mnemosyne Authoritative HTTP Server Adapter.md](<../../.junie/reports/Harmonia%20-%20Goal%202%20-%20Step%203.3%20-%20Mnemosyne%20Authoritative%20HTTP%20Server%20Adapter.md>) | E | 7, 293, 579, 865, 1014, 1300, 1586 | — | — |
| [.junie/reports/Harmonia - M1 - Stable Docker Runtime Baseline.md ](<../../.junie/reports/Harmonia%20-%20M1%20-%20Stable%20Docker%20Runtime%20Baseline.md%20>) | E | — | AX-05 | 32, 62, 150, 210, 240, 328, 386, 416, 504, 564, 594, 682, 742, 772, 860, 920, 950, 1038, 1077 |
| [.junie/reports/Harmonia - M2 - Step 2 - Distributed Authoritative Docker Support.md](<../../.junie/reports/Harmonia%20-%20M2%20-%20Step%202%20-%20Distributed%20Authoritative%20Docker%20Support.md>) | E | 5, 267, 542, 817, 1090, 1374, 1656, 1724, 2006, 2074, 2356, 2638 | — | — |
| [.junie/reports/Harmonia - M2 - Step 3 - Deployment Level Authentication of Mneme to Mnemosyne Authoritative HTTP Connection.md](<../../.junie/reports/Harmonia%20-%20M2%20-%20Step%203%20-%20Deployment%20Level%20Authentication%20of%20Mneme%20to%20Mnemosyne%20Authoritative%20HTTP%20Connection.md>) | E | — | AX-01, AX-05 | 69, 171, 381, 483, 817, 919 |
| [.junie/reports/Harmonia - M2 - Step 4 - Distributed Authoritative-Path Verification.md](<../../.junie/reports/Harmonia%20-%20M2%20-%20Step%204%20-%20Distributed%20Authoritative-Path%20Verification.md>) | E | — | AX-01, AX-05, AX-13 | 42, 44, 47, 321, 323, 326, 602, 604, 607, 883, 885, 888, 1164, 1166, 1169, 1445, 1447, 1450, 1726, 1728, 1731 |
| [.junie/reports/Harmonia - M3 - Step 1 - Governed Read Integration and Active-State Generation.md](<../../.junie/reports/Harmonia%20-%20M3%20-%20Step%201%20-%20Governed%20Read%20Integration%20and%20Active-State%20Generation.md>) | E | 17, 378, 731, 1082 | AX-05, AX-14..AX-15 | 19, 25, 32, 106, 113, 380, 386, 393, 467, 474, 733, 739, 746, 820, 827, 1084, 1090, 1097, 1171, 1178, 1414 |
| [.junie/reports/Harmonia - M3 - Step 2 - Mneme Active-Hegemon and Calliope Alignment.md](<../../.junie/reports/Harmonia%20-%20M3%20-%20Step%202%20-%20Mneme%20Active-Hegemon%20and%20Calliope%20Alignment.md>) | E | — | AX-05, AX-14..AX-15 | 27, 44–46, 73–75, 105, 307, 324–326, 353–355, 385, 587, 604–606, 633–635, 665, 867, 884–886, 913–915, 945, 1147, 1164–1166, 1193–1195, 1225 |
| [.junie/reports/Harmonia Convergence - Enforce the Pylai External FHIR Publication Boundary.md](<../../.junie/reports/Harmonia%20Convergence%20-%20Enforce%20the%20Pylai%20External%20FHIR%20Publication%20Boundary.md>) | E | — | AX-04..AX-05, AX-13 | 8, 45, 62, 177, 208, 239, 326, 363, 380, 495, 526, 536, 573, 590, 705, 736, 772, 852, 889, 906, 1021, 1052, 1062, 1099, 1116, 1231, 1262, 1302, 1364, 1386, 1423, 1440, 1555, 1586 |
| [.junie/reports/Harmonia Convergence - Goal 1 - Establish Mneme Application-Facing Managed-Information Boundary.md](<../../.junie/reports/Harmonia%20Convergence%20-%20Goal%201%20-%20Establish%20Mneme%20Application-Facing%20Managed-Information%20Boundary.md>) | E | — | AX-05 | 8, 228, 520 |
| [.junie/reports/Harmonia Convergence - Mneme and Mnemosyne Managed Information Boundary - Plan.md](<../../.junie/reports/Harmonia%20Convergence%20-%20Mneme%20and%20Mnemosyne%20Managed%20Information%20Boundary%20-%20Plan.md>) | E | 42, 390, 964 | AX-01..AX-02, AX-04..AX-07, AX-10..AX-13 | 11, 43–52, 223, 228, 347, 359, 391–400, 571, 576, 695, 761, 773, 785, 798, 809, 933, 965–974, 1145, 1150, 1269, 1335, 1347, 1359, 1372, 1383, 1624, 1636, 1648, 1661, 1672, 1833, 1845, 1857, 1870, 1881 |
| [.junie/reports/Harmonia Convergence - Pylai Trusted Ingress Security.md](<../../.junie/reports/Harmonia%20Convergence%20-%20Pylai%20Trusted%20Ingress%20Security.md>) | E | — | AX-07, AX-13 | 10, 56, 72, 79, 331, 377, 393, 400, 555, 601, 617, 624, 873, 919, 935, 942, 1097, 1143, 1159, 1166, 1437, 1483, 1499, 1506 |
| [AGENTS-old.md](<../../AGENTS-old.md>) | H | 34, 385 | AX-05, AX-13 | 334, 338 |
| [AGENTS.md](<../../AGENTS.md>) | G | 34, 445 | AX-05, AX-13, AX-15, AX-17 | 77, 361, 365, 415 |
| [docs/architectural-axioms.md](<../../docs/architectural-axioms.md>) | G | 635 | AX-01..AX-17 | 45, 85, 126, 165, 205, 257, 295, 329, 362, 402, 435, 471, 511, 563, 600, 633, 649, 731, 734, 739–740, 744 |
| [docs/architecture/execution-model.md](<../../docs/architecture/execution-model.md>) | H | 148 | AX-04, AX-06..AX-08, AX-14, AX-17 | 148 |
| [docs/deferred-document-register.md](<../../docs/deferred-document-register.md>) | G | 21 | AX-17 | 21, 127, 143, 149 |
| [docs/markdown/01-motivation/README.md](<../../docs/markdown/01-motivation/README.md>) | C | 34, 142–143 | AX-01, AX-05, AX-07, AX-10..AX-13, AX-15, AX-17 | 63, 66, 107–108, 142–144 |
| [docs/markdown/01-motivation/drivers-assessments/assessments.md](<../../docs/markdown/01-motivation/drivers-assessments/assessments.md>) | C | 34, 63, 85, 108, 132 | AX-05, AX-07, AX-09..AX-11, AX-15..AX-16 | 34, 63, 85, 106, 108, 132 |
| [docs/markdown/01-motivation/drivers-assessments/drivers.md](<../../docs/markdown/01-motivation/drivers-assessments/drivers.md>) | C | 26, 42, 59, 78, 95, 112, 129 | AX-01..AX-03, AX-05..AX-08, AX-10..AX-11, AX-14, AX-16 | 26, 42, 59, 78, 95, 112, 129 |
| [docs/markdown/01-motivation/goals-outcomes/business-outcomes.md](<../../docs/markdown/01-motivation/goals-outcomes/business-outcomes.md>) | C | 58 | AX-07..AX-08 | 58, 60 |
| [docs/markdown/01-motivation/goals-outcomes/strategic-goals.md](<../../docs/markdown/01-motivation/goals-outcomes/strategic-goals.md>) | C | 52, 82, 98 | AX-01..AX-02, AX-05..AX-06, AX-11, AX-16 | 52, 82, 98 |
| [docs/markdown/01-motivation/orientation-view.md](<../../docs/markdown/01-motivation/orientation-view.md>) | C | 258–259, 268–269, 279–281, 293–295, 304–305, 318, 321, 330 | AX-01..AX-03, AX-05..AX-11, AX-14..AX-16 | 54–55, 77–78, 99–101, 126–128, 149–150, 173–174, 202, 258–259, 268–269, 279–281, 293–295, 304–305, 318, 321, 323, 330, 338, 340, 348, 351–352 |
| [docs/markdown/01-motivation/principles/architectural-axioms.md](<../../docs/markdown/01-motivation/principles/architectural-axioms.md>) | C | 14 | AX-01..AX-17 | 1, 12, 14, 18, 35, 51, 67, 86, 106, 124, 142, 158, 174, 192, 207, 223, 241, 257 |
| [docs/markdown/01-motivation/principles/reclassified-principles.md](<../../docs/markdown/01-motivation/principles/reclassified-principles.md>) | C | 21 | AX-01, AX-11..AX-16 | 7, 11, 14, 21, 27, 30, 36, 40, 42, 44, 46 |
| [docs/markdown/01-motivation/requirements-constraints/external-constraints.md](<../../docs/markdown/01-motivation/requirements-constraints/external-constraints.md>) | C | 65 | AX-02 | 65 |
| [docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md](<../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md>) | C | 29, 49, 71, 91, 141, 154–157, 159 | AX-05..AX-10, AX-14..AX-17 | 29, 49, 71, 91, 141, 154–157, 159 |
| [docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md](<../../docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md>) | C | 21–37 | AX-02..AX-08, AX-10, AX-12..AX-16 | 21–37, 47, 49, 53, 66, 68 |
| [docs/markdown/01-motivation/reviews/independent-assurance-reconciliation.md](<../../docs/markdown/01-motivation/reviews/independent-assurance-reconciliation.md>) | C | 19 | AX-05..AX-09, AX-12, AX-14..AX-17 | 13, 19, 50, 52–53, 55, 62, 65–68, 79–84, 86, 92, 115, 117 |
| [docs/markdown/02-strategy/README.md](<../../docs/markdown/02-strategy/README.md>) | C | — | AX-17 | 148 |
| [docs/markdown/02-strategy/capabilities/business-capabilities.md](<../../docs/markdown/02-strategy/capabilities/business-capabilities.md>) | C | 326 | AX-17 | 326 |
| [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](<../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md>) | C | 744 | AX-15 | 744 |
| [docs/markdown/02-strategy/capabilities/enterprise-capabilities.md](<../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md>) | C | — | AX-05, AX-13 | 137, 173 |
| [docs/markdown/02-strategy/capability-maps/capability-tier-model.md](<../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md>) | C | 19, 249 | AX-17 | 19, 249 |
| [docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md](<../../docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md>) | C | 7, 11–15 | AX-05..AX-10, AX-14..AX-15, AX-17 | 7, 11–15, 81 |
| [docs/markdown/02-strategy/courses-of-action/index.md](<../../docs/markdown/02-strategy/courses-of-action/index.md>) | C | 51 | AX-01..AX-11, AX-13..AX-17 | 35, 51, 73–78 |
| [docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md](<../../docs/markdown/02-strategy/courses-of-action/strategic-courses-of-action.md>) | C | — | AX-01..AX-11, AX-13..AX-16 | 27–30, 39, 55–58, 63, 84–87, 106, 111–113, 138–140, 164–166, 184–189 |
| [docs/markdown/02-strategy/resources/strategic-resources.md](<../../docs/markdown/02-strategy/resources/strategic-resources.md>) | C | — | AX-01..AX-04, AX-06, AX-08, AX-13..AX-14 | 56, 80, 101 |
| [docs/markdown/02-strategy/reviews/ax05-state-responsibility-reconciliation.md](<../../docs/markdown/02-strategy/reviews/ax05-state-responsibility-reconciliation.md>) | C | 19, 25, 42, 52, 478 | AX-01..AX-12, AX-14..AX-17 | 1, 3, 7, 17, 21, 23, 25, 42, 52, 62, 64, 73, 78, 83, 92, 97, 107, 112, 118, 120, 125, 129, 131, 154, 266, 385, 391–394, 401–402, 406, 408–410, 420, 423–425, 445, 449, 464, 478, 513–514, 660, 674, 694, 709 |
| [docs/markdown/02-strategy/reviews/business-capability-five-region-reconciliation.md](<../../docs/markdown/02-strategy/reviews/business-capability-five-region-reconciliation.md>) | C | 21 | AX-05, AX-08, AX-14, AX-17 | 21, 23, 68, 80, 86 |
| [docs/markdown/02-strategy/strategic-views/index.md](<../../docs/markdown/02-strategy/strategic-views/index.md>) | C | — | AX-05 | 27 |
| [docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md](<../../docs/markdown/02-strategy/strategic-views/logical-component-responsibilities.md>) | C | 45 | AX-02, AX-04..AX-05, AX-08, AX-13, AX-15..AX-16 | 45, 112, 140–141, 168, 197, 289, 379, 393 |
| [docs/markdown/02-strategy/strategic-views/strategic-value-streams.md](<../../docs/markdown/02-strategy/strategic-views/strategic-value-streams.md>) | C | 41, 155, 212, 271 | AX-05, AX-07, AX-15..AX-17 | 41, 155, 170, 199, 212, 271, 308 |
| [docs/markdown/03-business-architecture/behaviours/health-service-assurance.md](<../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md>) | C | 7 | AX-06..AX-09, AX-14..AX-15, AX-17 | 7 |
| [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](<../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md>) | C | 369 | AX-01, AX-04, AX-14..AX-15, AX-17 | 166, 369 |
| [docs/markdown/04-information-architecture/information-families/appointment-scheduling.md](<../../docs/markdown/04-information-architecture/information-families/appointment-scheduling.md>) | C | 111 | AX-17 | 111 |
| [docs/markdown/04-information-architecture/information-families/episode-encounter.md](<../../docs/markdown/04-information-architecture/information-families/episode-encounter.md>) | C | 126 | AX-17 | 126 |
| [docs/markdown/04-information-architecture/information-families/order.md](<../../docs/markdown/04-information-architecture/information-families/order.md>) | C | 127 | AX-15, AX-17 | 100, 127 |
| [docs/markdown/04-information-architecture/information-families/referral.md](<../../docs/markdown/04-information-architecture/information-families/referral.md>) | C | 122 | AX-17 | 122 |
| [docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md](<../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md>) | C | 11, 163 | AX-01..AX-17 | 11, 18, 66, 167–174, 232–233, 239, 241, 274, 285 |
| [docs/markdown/04-information-architecture/reviews/dokimasia-r1-motivation-traceability-ax05-investigation.md](<../../docs/markdown/04-information-architecture/reviews/dokimasia-r1-motivation-traceability-ax05-investigation.md>) | C | 11, 51, 53, 184, 204 | AX-01..AX-02, AX-05..AX-10, AX-12..AX-17 | 1, 7, 11, 51, 54, 57–59, 73, 90–91, 101–109, 135–147, 149, 163, 165–168, 170, 172, 174, 176, 178, 182, 184, 204, 208, 210, 214, 216, 234, 236, 242, 248, 255–256, 258, 260, 262–265, 267–268, 271, 282, 287, 291, 302–303 |
| [docs/markdown/04-information-architecture/reviews/package2-g1-review.md](<../../docs/markdown/04-information-architecture/reviews/package2-g1-review.md>) | C | 29, 1096, 1270, 1825, 2061, 2091 | AX-01..AX-08, AX-12..AX-17 | 25, 27, 29, 82, 193, 322, 488, 651, 897, 1061, 1096, 1137, 1315, 1375, 1471, 1605, 1712, 1821, 1823, 1825, 1827, 1850, 1856, 1858, 1866, 2019, 2021, 2027, 2040, 2048, 2061, 2084–2085, 2087, 2091, 2093, 2095, 2097, 2117, 2125, 2136 |
| [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md](<../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md>) | C | 9 | AX-01..AX-04, AX-06, AX-08, AX-12..AX-17 | 9, 169, 171, 183, 264 |
| [docs/markdown/04-information-architecture/traceability/domain03-traceability.md](<../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md>) | C | 37 | AX-17 | 37 |
| [docs/markdown/architecture-completion-plan.md](<../../docs/markdown/architecture-completion-plan.md>) | C | 38, 41, 227 | AX-16..AX-17 | 41, 101, 168, 197, 203, 227, 294 |
| [docs/memory-recovery.md](<../../docs/memory-recovery.md>) | H | — | AX-05, AX-17 | 11, 59 |
| [docs/modules/dokimasia.md](<../../docs/modules/dokimasia.md>) | H | 5 | AX-17 | 5 |
| [hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/coordination/ActiveCoordinationCorruptException.java](<../../hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/coordination/ActiveCoordinationCorruptException.java>) | I | — | AX-14..AX-15 | 22 |
| [hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/coordination/ActiveCoordinationRecord.java](<../../hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/coordination/ActiveCoordinationRecord.java>) | I | — | AX-14..AX-15 | 27 |
| [hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/coordination/ActiveCoordinationRecordTest.java](<../../hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/coordination/ActiveCoordinationRecordTest.java>) | I | — | AX-14..AX-15 | 81, 97, 113 |
| [hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/coordination/HotRodActiveStateCoordinatorTest.java](<../../hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/coordination/HotRodActiveStateCoordinatorTest.java>) | I | — | AX-14..AX-15 | 248 |
| [hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/HttpTransportFailureClassifier.java](<../../hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/HttpTransportFailureClassifier.java>) | I | — | AX-01, AX-05 | 34 |
| [paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PylaiPublicationBoundaryArchitectureTest.java](<../../paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PylaiPublicationBoundaryArchitectureTest.java>) | I | — | AX-05, AX-13 | 41 |
| [pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayController.java](<../../pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayController.java>) | I | — | AX-05, AX-13 | 65–66 |
| [pylai/pylai-fhir-registry/src/test/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayControllerTest.java](<../../pylai/pylai-fhir-registry/src/test/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayControllerTest.java>) | I | — | AX-05, AX-13 | 304 |

The current source row is the migration baseline itself, including its AX-16 reverse link and AX-17 internal dependencies. Its appearance here does not imply that it was already canonical. Old document basename matches in tree examples and historical diff hunks are intentionally retained in the inventory rather than treated as live resolvable Markdown links.
