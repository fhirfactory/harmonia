# Design Reconciliation Proposal (DRP)

## Logging, AuditEvent and Provenance — Information Governance and Export

**Status:** Candidate — Solution Architect review required  
**Architecture authority:** `enterprise-architecture/continuum/`  
**Activity:** Proposed governed reconciliation / ADD; no baseline modification authorised by this document  
**Scope boundary:** Information semantics, export responsibilities, security constraints and persistence-service requirements; not persistence implementation design.

## 1. Purpose and intended outcome

Reconcile the agreed architectural intent for Logging, AuditEvent, Provenance, Evidence and export into the Harmonia Architecture Continuum. Preserve the separation between lightweight persisted records, their referenced information, and higher-level dereferenced exports. Establish constraints for later consideration of a reusable lightweight PersistenceService without choosing its technology or deployment.

This DRP is a candidate for review, not an instruction to execute changes or to commit an architectural baseline.

## 2. Existing architectural context and constraints

- The governed Markdown architecture under `enterprise-architecture/continuum/` is authoritative; ArchiMate is a governed projection, not an independent authority.
- Mnemosyne comprises `GovernedPersistenceFramework + PersistenceService[1..X]`. A reusable PersistenceService definition or implementation does **not** imply a shared PersistenceServiceInstance.
- Mneme mediates governed access to persisted information; persistence details must not leak through its façade.
- Themis authorisation and local PEP enforcement remain applicable. Passport propagation carries security context, not credentials.
- InformationUnit identity, versioning, immutability and temporal governance must be respected where relevant.
- Logs, AuditEvents and Provenance may each contain sensitive PHI. No category is inherently exempt from governed handling.
- Sophisticated external log/forensic analytics are an SEP responsibility, not an implicit requirement for Harmonia's native persistence/search implementation.

## 3. Agreed information semantics

### 3.1 AuditEvent and Provenance

AuditEvent and Provenance are intentionally small, predominantly referential records. They may reference substantial information but do not ordinarily contain copies of that information. Limited information specific to an assertion or transformation may be embedded where needed.

Both AuditEvent and Provenance are **immutable once recorded**. Subsequent correction, qualification or extension is represented by a new record referencing prior records, not mutation of the original. Their contained Evidence constructs are likewise immutable within the recorded object.

Use of FHIR AuditEvent and Provenance representations remains separable from adoption of HAPI FHIR JPA Server as a persistence mechanism. The exact standards representation and mapping remain subject to reconciliation with the existing architecture.

### 3.2 Evidence

Evidence is a **discrete contextual reference construct**, not a duplicated payload or a new general-purpose superclass for all referenced objects.

An Evidence element contains, conceptually:

- A reference to the evidentiary object;
- The role of that object in the enclosing AuditEvent or Provenance assertion;
- Temporal-consistency qualifications needed to identify the relevant historical state;
- Other context, as subsequently defined.

Permitted referent categories identified in discussion:

1. Pragma or a specialisation of Pragma;
2. InformationUnit;
3. Another AuditEvent;
4. Another Provenance object.

`InformationThing` was informal shorthand and **must not** be introduced as an architectural element on this basis. Attribute names, types and cardinalities are not yet agreed.

### 3.3 Provenance Evidence ordering

The Evidence collection within Provenance is an **ordered list whose order represents the chronological sequence of activities**, not logical derivation order and not necessarily insertion/recording order. Ordering is semantically significant and remains immutable after recording. Concurrent or ambiguous chronology remains a design issue.

AuditEvent Evidence ordering has **not** been established.

### 3.4 Access history for audit/provenance records

Reading an AuditEvent **does not** generate another AuditEvent. Access is instead recorded in a separate lightweight **append-only access history**, with one entry per access, recording at least the accessed record's identity, access time and accessor identity (precise schema TBD). The underlying AuditEvent/Provenance record is not modified. Access tracking for Provenance follows the same discussed intent; detailed scope and policy remain to be specified.

## 4. Export capabilities and ownership

### 4.1 Non-dereferenced bulk record export

A reusable persistence capability shall support governed export of Logger, AuditEvent and Provenance records, including:

- Bulk export;
- On-demand export;
- Temporally restricted export.

These exports preserve persisted records and their references **without dereferencing** the underlying information. The purpose is to make information available for optional external analytics or data-lake ingestion. Harmonia is not required to run such external infrastructure, to continuously feed it, or to provide Elasticsearch-style analytical search or forensic correlation internally.

The applicable temporal filter basis (occurrence versus persistence time, or both) remains to be defined.

### 4.2 Dereferenced Provenance export — Dokimasia

Dokimasia shall expose an API/tool to request a **set of Provenance entries** and coordinate their dereferenced export through governed Mneme/Mnemosyne retrieval. The primary export subject remains Provenance; the output is not a separate architectural information type named 'Evidence Export'.

### 4.3 Dereferenced AuditEvent export — Kleio

Kleio shall expose an API/tool to request a **set of AuditEvent entries** and coordinate their dereferenced export through governed Mneme/Mnemosyne retrieval. The primary export subject remains AuditEvent.

### 4.4 Common characteristics of dereferenced exports

- Operations may be deferred or long-running and complete 'in their own good time'.
- Request interfaces may be deliberately strict about scope and timing, and processing may be scheduled or gapped to protect primary workloads. Exact controls are TBD.
- These are **secondary** capabilities. Do not impose sophisticated bulk-processing, distributed search or performance optimisation as a baseline obligation.
- Each export represents a **specified historical point in time**, regardless of initiation or completion time; using initiation time is merely one possible selection.
- The assembled information must be temporally consistent with that selected historical state, and each Evidence reference must resolve according to its identity, version and temporal qualifications.
- Provenance Evidence chronology must be preserved.
- The depth and rules for dereferencing are **not yet designed**; they are conditional on the resources and InformationUnits being collated. Do not presume either mandatory recursion or one-level-only resolution.
- The resulting export must remain independently interpretable even if original references cannot subsequently be resolved. The precise packaging and minimum self-description remain TBD.

## 5. Security and release integrity

### 5.1 Existing Themis model applies

Export is a consumer of the existing Themis security architecture, not a new authority or privileged bypass. The request is authorised; individual dereferenced retrievals use Mneme's governed interfaces and applicable local PEP enforcement. Passport propagation and existing security constraints remain operative.

All three record domains may contain PHI; bulk and dereferenced exports must be governed accordingly.

### 5.2 Privileged caller and failure behaviour

Dereferenced export is intended for a principal already possessing sufficiently broad authority to access **every** required item, potentially across security compartments. 'God-Level' is an informal expectation of broad pre-existing authority, **not** a proposed new role or automatic elevation.

If any required information fails authorisation, the collation fails gracefully, without disclosing unauthorised information. The service does not silently omit unauthorised information to construct a reduced export.

### 5.3 All-or-nothing release integrity — explicitly agreed

**A dereferenced AuditEvent or Provenance export SHALL NOT be released unless the complete requested information set has been assembled and all applicable authorisation requirements have been satisfied.**

Intermediate or partially assembled information SHALL remain governed and SHALL NOT be released as a partial export after an authorisation failure. Detailed staging, protection, cleanup, revocation and release mechanisms remain implementation-design matters.

This requirement concerns **release integrity** and does not, by itself, mandate a cross-instance distributed database transaction.

## 6. Candidate persistence architecture — constraints only

The three record domains appear suitable for evaluation against a **common reusable lightweight PersistenceService implementation**, potentially with distinct independently configured PersistenceServiceInstances. Sharing an implementation must not be conflated with sharing an instance, storage location, retention policy or security configuration.

The agreed export requirements establish a bounded persistence capability; they do **not** establish that all three information types have identical write, indexing, lifecycle or durability policies. AuditEvent and Provenance immutability is agreed; Logger immutability and detailed Logger information semantics remain open.

Evidence dereferencing and domain-specific assembly belong to Kleio and Dokimasia, not to the generic PersistenceService. Mneme/Mnemosyne provide governed access and persistence functionality, without embedding domain-specific forensic interpretation.

## 7. Candidate architectural relationships for reconciliation

These are conceptual responsibilities; ArchiMate element types, relationship types, identities and containment must be checked against the existing continuum before any ADD:

- AuditEvent **contains contextual Evidence**; its exact multiplicity/ordering TBD.
- Provenance **contains chronologically ordered Evidence**.
- Evidence **references** one of the four permitted referent categories, qualified by role and temporal consistency.
- Kleio **coordinates dereferenced AuditEvent export**.
- Dokimasia **coordinates dereferenced Provenance export**.
- Mneme **provides governed retrieval** for both coordination paths.
- Mnemosyne / PersistenceServiceInstance **persists and retrieves** its managed records and supports non-dereferenced record export.
- Separate access-history entries **reference** accessed records without mutating them.

Do not invent new component identities or assume that the descriptive labels in this DRP are accepted canonical Topic/class names.

## 8. Explicitly unresolved matters

1. Logger record information model, immutability, write guarantees, retention and access tracking.
2. Exact Evidence attributes, referential model, cardinality, and AuditEvent Evidence ordering.
3. Temporal semantics for retrospective information, concurrent activities and selection of records into a historical snapshot.
4. Dereferencing rules, traversal limits, conditional expansion and unresolved-reference handling beyond agreed authorisation failure semantics.
5. Export selection API, scheduling/gapped processing, progress/status, completion notification and operational limits.
6. Independent interpretation format, reference manifest, PHI classification/security wrapper, and transport/protection of released exports.
7. Temporal basis for raw bulk record export filters.
8. Access-history schema, retention, security and exact applicability to each record domain.
9. FHIR AuditEvent/Provenance representation and mapping, without assuming HAPI JPA persistence.
10. PersistenceService implementation, instance boundaries, database/storage technology, indexing, durability and performance.
11. How a consistent historical export is technically realised across independently persisted information without imposing unagreed distributed transaction requirements.

## 9. Reconciliation and acceptance boundaries

Before a governed ADD, compare these candidates with existing Audit, Provenance, Logging, Evidence, Themis, Mneme, Mnemosyne, Kleio and Dokimasia definitions. Reuse established identities and semantics; flag any conflicts for Solution Architect adjudication. Preserve established architecture rather than silently replacing it.

**Acceptance sought:** agreement that Sections 3–5 correctly capture the discussion and that Sections 6–8 appropriately constrain, rather than pre-empt, the subsequent PersistenceService design.

**Not authorised by this DRP:** repository modification, implementation, technology selection, HCRL closure, or a Codex/Junie execution task.
