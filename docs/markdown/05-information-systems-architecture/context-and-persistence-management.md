<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Mneme Context and Pragma Management / Mnemosyne Persistence Management

**Standing: Domain05 Step A — bounded A2/A3 adjudicated candidate correction,
2026-10-11.** [U10/U11](sources.md#u10) record the two supplied DesignNotes and
explicit task decisions. This view is self-contained architectural meaning;
external notes are provenance. [Scope and completion status](README.md) apply:
Step A remains OPEN, A2/A3 IN PROGRESS, A4 PENDING and Step B NOT STARTED.

AX-05 preserves active use versus authoritative durable state; AX-11/12 govern
efficient information access and insulation of business logic from plumbing.
AX-07 governs operations, AX-08/09 distinguish evidence from operational logging,
AX-14/15 preserve semantic distinctions and uncertain outcomes, and AX-04/17/18
prevent technology assumptions, invented detail and inferred domain authority.
The [axiom register](../governance/architectural-axioms.md) remains controlling.

<a id="mneme-responsibility"></a>

## Mneme — Context and Pragma Manager

> **Mneme manages the information context of Harmonia activity — principally
> through Pragma and Context — providing controlled, concurrent and seamless
> access to existing resource values and management of the new Content produced
> by that activity.**

Mneme is Harmonia's **Context and Pragma Manager**. Distributed active
representation, observation, coordination, reconstruction and convergence
support this responsibility under AX-05/11; they do not make Mneme a generic
repository or persistence authority. Pragma Management retains its adjudicated
PragmaFactory and Publishing/Subscription/Pattern responsibilities. Semantic
selection remains distinct from Petasos handoff and Ponos execution.

Mneme exposes **four principal services**:

| Exposed service | Architectural meaning |
| :--- | :--- |
| **Context Load** | Materialise a managed Context from authoritative resource values for anticipated activity requirements. |
| **Context Save** | Return resulting Context state to Mneme management for appropriate subsequent information handling, alongside distinguishable Pragma Outputs. |
| **Adhoc Resource Access** | Governed runtime acquisition of resource information outside the anticipated loaded Context where the bounded activity requires it. |
| **Adhoc Search** | Activity/context-facing governed search, using Mnemosyne's underlying authoritative persistence Search Management. |

Resource Harmonisation Manager is an **internal responsibility**, not a fifth
exposed service. Historical read/write facades and supporting contracts are not
additional principal services; their detailed relationship to this surface remains
unresolved. No additional exposed Mneme services are established here.

<a id="context-values"></a>

## Context — versioned temporal values

> **Context is a versioned, temporal load-snapshot composed from authoritative
> resource values materialised for a Harmonia activity's anticipated information
> requirements.**

Context contains **values, not merely references**. Each value represents the
defined authoritative resource state/version actually materialised when it is
incorporated. Loaded values are composed into the Context bucket:

```text
Context — identity / Context version / temporal load basis
  └── Resource Bucket                         [Composition]
        ├── Resource Value A @ authoritative state/version
        ├── Resource Value B @ authoritative state/version
        └── Resource Value C @ authoritative state/version
```

**Composition** concerns loaded values belonging to the snapshot. The Context
does not existentially own the underlying authoritative resources. This is
distinct from the separately established Aggregation relationships for Praxis,
Journey and PragmaFactory.

Context version and constituent resource authoritative state/version SHALL
remain distinct. Neither is an alias for a valid active-state generation or
security PEP-Context. Materialisation does **not** imply a globally
transactionally consistent distributed snapshot or a universal frozen instant.
Context is the Ergon's managed working information environment, not a closed
universe of all information it may access or produce.

Authoritative constituent resource state may progress while an Ergon operates.
Mneme needs sufficient state/version information to recognise delivery that cannot
safely proceed normally; generic conflict handling remains outside the originating
Ergon. No concurrency or conflict-detection algorithm is established here.

This activity Context does not redefine Domain04's Information Unit
[Management Context](../04-information-architecture/metamodel/information-architecture-metamodel.md#65-context).
Their detailed mapping, physical bucket representation, Context identity/version
mechanisms and concurrency algorithms remain unresolved under AX-17.

<a id="ergon-information-access"></a>

## Expected resource set and the Ergon information boundary

During development/definition, an **Ergo identifies the resource set it is most
likely to access**. The set is deliberately **not exhaustive**. The Ergo
framework works with Mneme to efficiently load anticipated values before Ergon
Business Logic executes; preloading every conceivably accessible resource is
not required.

```text
Ergo Definition
    ↓
Expected / Likely Resource Set
    ↓
Mneme Context Load
    ↓
Context Loader
    ↓
Ergon Business Logic
    ↓
Context Unloader
    ↓
Mneme Context Save
```

| Boundary | Responsibility |
| :--- | :--- |
| **Mneme Context Load** | Locate/acquire, map, establish authoritative state/version basis, apply controls and compose values to materialise the managed Context. |
| **Ergo Context Loader** | Ordinarily exposes the **already-loaded Context** to Ergon Business Logic, without taking over Mneme resource acquisition. U14's [special Pylai ingress](pylai-external-interoperability.md#pylai-pathway-ingress) instead establishes initial Context/Task.input from Communication Information through established Mneme/persistence responsibilities; it performs no semantic mapping. |
| **Ergon Business Logic** | Perform bounded activity, primarily reading and acting upon values within its Context, with governed Adhoc acquisition where required. |
| **Context Unloader** | Bring resulting bounded processing back across the business-logic boundary, preserving resulting Context state and Pragma Outputs. It is **not a persistence mechanism**. |
| **Mneme Context Save** | Resume Mneme management of resulting Context state and the subsequent information consequences of Pragma Outputs; determine appropriate normal or exceptional handling. |

Context Load is the **preferred, performance-optimised common path**. Ergon
Business Logic SHOULD primarily use loaded values. It MAY use **Adhoc Resource
Access** and **Adhoc Search** where bounded activity requires runtime acquisition,
particularly for interaction with external systems, deep interrogation outside
reasonably known boundaries, or dynamically emerging information requirements.
Adhoc access may be materially more expensive than Context Load; it remains
legitimate. External interactions retain Pylai's established boundary.

The originating Ergon SHALL NOT be responsible for persistence mechanics,
database/shard selection, transactions, persistence tagging/indexing or generic
conflict resolution. No exact loader/unloader API, acquisition/batching strategy,
query language, locking/MVCC or Context concurrency algorithm is selected.

<a id="pragma-output"></a>

## Pragma and new Content

**Mneme manages Pragma.** Context is the value-based working environment;
Pragma is the task/activity information envelope through which inputs and outputs
are expressed. They are related and distinct.

An Ergon may read existing Context values, act upon/change working Context
values, and generate **new Content**. New Content is expressed as **Pragma
Output**, rather than merely an incidental Context mutation:

```text
Existing Context values → Ergon → changed working Context values
Context + bounded business logic → Ergon → Pragma Output → new Content
                  Context Unloader returns both, distinctly
                                    ↓
                 Mneme manages subsequent information consequences
```

Changed Context values and newly generated Pragma Outputs SHALL remain
semantically distinguishable. A working change or produced output does not
itself establish authoritative durable state. Task/undertaking/outcome ownership
and output rules remain unchanged. Exact Output representation, downstream
routing and detailed Pragma/Information Unit mappings remain unresolved.

<a id="resource-harmonisation"></a>

## Resource Harmonisation Manager — internal exceptional path

Mneme's **Resource Harmonisation Manager C40** identifies/manages resource-delivery
conditions that cannot proceed normally and causes appropriate bounded
harmonisation work to occur. It **does not itself perform domain harmonisation**.
Conflicting, discordant, malformed or otherwise unsuitable resulting resource
state may require specialised work; no exhaustive categories are established.

```text
Mneme resulting resource delivery
  ├── Normal handling → Mnemosyne persistence as required
  └── Exceptional condition → Resource Harmonisation Manager
        → established Harmonia work/execution contract
        → Ponos execution → appropriate Harmonisation Ergon
        → resulting information returns to governed management
```

This is expected to represent **approximately 5% or less at worst of
resource-delivery activity**, a design expectation **not a hard SLA**. The normal
path SHALL remain efficient and SHALL NOT be unnecessarily burdened by expensive
exceptional harmonisation processing.

Actual harmonisation SHALL occur through an appropriate **Harmonisation Ergon**
using the common Harmonia execution architecture. It is **new governed work**;
execution SHALL NOT resume inside the originating Ergon. The originating bounded
activity is not made responsible for resolving generic resource conflicts.

Resource Harmonisation Manager SHALL reuse **Ponos execution capability through
the established Harmonia work/execution contract**. It SHALL NOT duplicate, embed
or assume Ponos execution-framework responsibilities or couple to its implementation
internals. A reusable **Ergo Library** is retained only as a candidate reuse
mechanism. No Iris, reusable forms or detailed UI architecture is established.
Condition detection, harmonisation algorithms/catalogue, work-contract detail and
terminal/escalation handling for repeated harmonisation remain unresolved.
This path does not override Kleio's separately excluded R1.x/R2.x cross-target
audit harmonisation.

<a id="mnemosyne-responsibility"></a>

## Mnemosyne — Persistence Manager

> **Mneme manages the information context of Harmonia activity; Mnemosyne manages
> the authoritative persistence of the information/metadata required to sustain
> that Context and reports authoritative persistence outcomes.**

Mnemosyne is Harmonia's **Persistence Manager**, with first-class responsibilities:

| Responsibility | Meaning and boundary |
| :--- | :--- |
| **Transaction oversight** | Harmonia-level oversight of persistence activity and outcome; heterogeneous services may retain their own transaction mechanics. No single physical or distributed transaction mechanism is prescribed. |
| **Persistent-service oversight and reporting** | Determine whether persistent services can fulfil Harmonia persistence responsibilities and report outcomes; this is distinct from IT/Ops infrastructure management. |
| **Shard management** | Manage shards within established semantic constraints; Mnemosyne does not independently decide a domain is semantically safe to shard. |
| **Authoritative persistence reporting** | Establish and report Harmonia's authoritative persistence outcome, preserving confirmed, degraded and indeterminate distinctions. |
| **Persistence logging** | Provide persistence-management operational logging; this does not replace Kleio Audit Capture or automatically become AuditEvent. Zero-PHI diagnostic controls apply. |
| **Compartment validation/reapplication** | Validate and reapply applicable compartments at the persistence boundary; persistence SHALL NOT strip, weaken or lose classification. Mnemosyne does not independently determine semantic applicability. |
| **Themis-owned PEP hosting** | Prevent governed persistence operations through Themis-owned enforcement and applicable PEP-Criteria through PEP-Context; Themis owns the model, reasoning, Criteria and enforcement semantics. |
| **Search Management** | Perform underlying authoritative managed-persistence search and manage supporting Harmonia-level persistence search metadata. |
| **Research Metadata Management** | Manage capture, persistence, management and search utilisation of distinct AI-derived Research Metadata; derivation executes outside Mnemosyne. |

Mnemosyne atomically establishes authoritative durable state/version progression
and required durable management metadata under AX-05. **Persistence API/engine
success is not automatically an authoritative Harmonia persistence outcome.**
Service responses require the governing persistence contract and outcome oversight;
uncertainty SHALL remain explicit under AX-15. Cache state or successful Ergon
execution cannot substitute for authoritative persistence.

Persistence responsibility confers no originating clinical, information or
application ownership. The established [Clinical/C&G/O&A semantic domains](data-and-persistence.md#persistence-responsibility-domains)
remain: coherent C&G governance authority, O&A partitionability preserving meaning,
and no prescribed database/service topology. Energeia may observe/report Mnemosyne
operational state under **R1.x/R2.x Observe → Report**, without acquiring persistence
management or infrastructure authority.

<a id="search-management"></a>

## Mnemosyne Search Management

**Search Management is a first-class Mnemosyne responsibility.** Mneme exposes
**Adhoc Search** at the activity/context-facing layer; Mnemosyne performs the
underlying authoritative persistence search. This does not expose its storage
implementation as an application-access path.

Search SHALL operate against **authoritative managed persistence**, without
assuming a transient cache is the complete searchable information space.
Mnemosyne MAY manage Harmonia-level search metadata for persistence-domain,
shard, persistent-service and search/index capabilities. Research Metadata is
distinct from, but usable by, Search Management. Query/result contracts,
completeness/consistency detail and physical search/index technology remain
unresolved; no distributed search algorithm is prescribed.

<a id="research-metadata"></a>

## Mnemosyne Research Metadata Management

**Research Metadata Management is a first-class Mnemosyne responsibility,
distinct from Search Management.** Research Metadata supports AI-derived capture
of **"meaning" or relationships across content sets based on proximity**.

Authoritative source Content SHALL remain unchanged. AI-derived Research Metadata
SHALL remain distinguishable from authoritative source Content and metadata;
durable capture of a derived assertion does not confer source-content authority
or turn inference into observed fact.

```text
Authoritative source Content
  → appropriate governed Harmonia behavioural/execution construct
      → AI derivation outside Mnemosyne
          → distinct Research Metadata
              → Mnemosyne capture / persistence / management
                  → utilisation by Search Management
```

Mnemosyne manages the metadata and its search utilisation; the deriving AI
processing SHALL NOT execute within Mnemosyne. No specific Ergon class,
Dokimasia allocation or new execution framework is inferred. The appropriate
Harmonia construct retains governed execution/security and originating authority
boundaries. No AI framework, model/provider, vector technology, embedding
mechanism, proximity algorithm or physical index is selected. Detailed
**provenance, confidence and Research Metadata schema remain unresolved**.

<a id="unresolved-management-detail"></a>

## Retained uncertainty and existing adjudications

[CDG-45–48](gaps.md#context-and-pragma-contracts) retain the new detailed gaps:
Context and access representation/concurrency, exceptional harmonisation,
persistence oversight/compartments/outcome contracts, and Search/Research Metadata.
Transaction boundaries, rollback/retry, shard routing/topology/replication,
service-health metrics, semantic compartment-applicability source, compartment
representation/validation, PEP interaction and logging schemas remain unresolved.

Kleio essential capture/immutable history and cross-target harmonisation SEP,
Themis distributed enforcement/default-deny, Petasos queues/Parcel/handoff,
Energeia Praxis/Journey ownership, DigitalTwin/Dokimasia specialisations and
terminal rules, and Pathway/control/observed-evidence distinctions are preserved.
The existing [Themis audit-authorisation proposal versus AX-07 conflict](gaps.md#themis-detailed-contracts)
remains unresolved. No audit-authorisation exception is introduced or AX-07
weakened. A2/A3 correction accepts neither the whole candidate nor A4 or Step B.


## Calliope and Pylai use of established responsibilities

[Calliope-DataSets](calliope-models-and-datasets.md#calliope-datasets) owns governed
mutable content and uses established Mneme/Mnemosyne access/persistence; this
adds no persistence manager or Mneme service. Models/build definitions supply
structural contracts independently of runtime DataSet availability.

[PylaiServer READ/SEARCH](pylai-external-interoperability.md#pylai-server-controller)
uses Diadromí-Controller → Mneme Adhoc Resource Access / Adhoc Search → Mnemosyne
as needed. Mneme remains the Context and Pragma Manager with four exposed services,
not a generic Information Manager or external interoperability service. No
ActionableTask/Praxis/Ergon is fabricated for this read-oriented pattern.

[PylaiPathway ingress](pylai-external-interoperability.md#pylai-pathway-ingress)
establishes initial Task.input/Context through a specialised Ergo ContextLoader;
semantic mapping remains in BusinessLogic. EgressHoros returns communication
state/outcome before ContextUnloader completes. Neither Loader/Unloader nor Pylai
establishes durable truth. Protocol success, engine success and authoritative
Harmonia persistence outcome remain distinct, including uncertainty.
