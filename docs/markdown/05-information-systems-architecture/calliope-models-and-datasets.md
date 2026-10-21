<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Historical source and navigation; all architectural sections incorporated. See the [canonical continuum](../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md) and [section correspondence](../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Calliope Models, DataSets and Nomenclature Architecture

**Standing: Domain05 Step A adjudicated Candidate Architecture, 2026-10-12.**
[U13](sources.md#u13) records the supplied Calliope DesignNote and explicit task
decisions. [Domain05 scope](README.md) applies: this is consolidation for review,
not whole-candidate acceptance, upstream coverage analysis or implementation design.
AX-04/05/06/07/08/09/12/14/17 constrain definition, authority, persistence,
governance, developer support and retained uncertainty.

<a id="calliope-responsibility"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope](../../../enterprise-architecture/continuum/application/application-component/466ad8f75a954c0581b5bba090443849.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## Responsibility and decomposition

Calliope manages and governs Harmonia's foundational Models, Metadata Types,
Criteria Definitions, Topics, Filter Types, Filter Elements, ValueSets, CodeSets,
Structured Metadata, core Harmonia element libraries, and Security and Assurance
Nomenclature. It also owns authoritative review/governance of ontology sets for
Harmonia use. That review does not make Harmonia the originating authority for
externally governed ontologies.

The B1 conceptual
[`Metabolē.Context.ActivityContext` vocabulary](../04-information-architecture/patterns/metabole.md#3-context-and-activitycontext)
is governed consistently with this shared definition/model/nomenclature
responsibility. It classifies the broad activity, separately from a request action
or authorisation permission. The initial examples are not an exhaustive enum;
this establishes no physical Models/DataSets allocation. Runtime policy
resolution, authorisation decisions and enforcement remain Themis responsibilities.

```text
Calliope
  +-- Calliope-Models     design/build-time governed knowledge
  `-- Calliope-DataSets   runtime management of mutable governed content
```

These are distinct logical submodules with different lifecycles and dependency
characteristics. They SHALL NOT be collapsed into one undifferentiated metadata
repository. Their names establish no Maven artefacts, processes, databases or
deployment boundaries. The [realisation boundary](#calliope-realisation) separates
Models' library dependencies from DataSets' runtime collaboration.

<a id="calliope-realisation"></a>

### Library dependency and runtime management

> **Calliope-Models provides development/build-time library dependencies.
> Calliope-DataSets provides runtime management and authoritative access to
> mutable governed datasets.**

The existence of DataSets does not alter Models' library/dependency semantics
and does not make Calliope a persistence manager. Repository pure-library and
zero-higher-layer-dependency constraints apply to Models; they do not restrict
all logical Calliope responsibilities to libraries. DataSets collaborates through
established Mneme/Mnemosyne access and persistence. No new static dependency or
physical allocation is inferred from that runtime collaboration.

```text
Calliope
  +-- Calliope-Models
  |     design / code / build-time model and library dependency
  `-- Calliope-DataSets
        runtime governed dataset management / authoritative content access
          ↓
        Mneme / Mnemosyne access and persistence

Concept / structural contract       → Calliope-Models
Current governed membership/content → Calliope-DataSets
```

> **Build-time representation and runtime authority are complementary, not
> competing, responsibilities.**

Strategy's library/local-evaluation statements concern Models. Calliope is not
a mandatory mediator of every runtime transaction; current governed content may
require DataSets runtime resolution. Packaging, APIs and other mechanisms remain
[deferred](gaps.md#calliope-contracts).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="calliope-models"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope-Models](../../../enterprise-architecture/continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## Calliope-Models

Calliope-Models is the foundational design/build-time library of:

- Models and Metadata Types;
- Criteria Definitions and Topic definitions;
- Filter Types and Filter Elements;
- Core Harmonia Element Libraries;
- Security and Assurance nomenclature definitions.

Its governed lifecycle is:

```text
Design → Code → Build → Test → Release → Deploy
```

Criteria Definitions, Topics, Filter Types and Filter Elements are
**development/build-time definitions**. Runtime uses of these definitions do not
make the definitions runtime-mutable DataSets. Models supplies structural contracts
consumed by Themis, Mneme, Mnemosyne, Ponos, Petasos, Kleio and other components;
physical packaging and distribution remain implementation concerns.

> **Definition ownership does not imply runtime operational ownership.**

| Calliope-Models definition | Runtime use and responsibility |
| :--- | :--- |
| Criteria Definition | Themis runtime Criteria / PEP-Context; policy association, resolution, compilation and enforcement remain Themis-owned. |
| Topic Definition | Metadata on Pragma / Content; Topic is classification, not a queue or destination. |
| Filter Type / Filter Element | Runtime Pattern evaluated/matched through Mneme's Publishing/Subscription semantics. |

Calliope SHALL NOT become the runtime executor of the constructs it defines.
Mneme retains runtime Pattern management/matching and Themis retains the
[Identity/Authorisation Model](component-register.md#c02). Reusable converter
support does not move interface-specific semantic processing out of
[developer-defined Ergo.BusinessLogic](pylai-external-interoperability.md#pylai-pathway-ingress).
The remaining general converter/activity allocation questions stay in CDG-05.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="calliope-datasets"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope-DataSets](../../../enterprise-architecture/continuum/application/application-component/53bf6815c7a54ed0abde9b1aad311843.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## Calliope-DataSets

Calliope-DataSets is the runtime repository/management responsibility for mutable
governed ValueSets, CodeSets, Structured Metadata and applicable governed
ontology content. Its authoritative content may change while Harmonia operates.
Governed activities may include update request, review, approval, authoritative
update, tagging/classification and making approved content available. This is
not an established state machine or a detailed DataSet workflow.

Calliope-DataSets owns the application responsibility for its datasets. It is
**not another Persistence Manager**. Governed access uses established Mneme
Context/Pragma responsibilities and authoritative persistence remains with
Mnemosyne under AX-05:

```text
Calliope-DataSets: governed content management
        ↓
Mneme: Context / Pragma and applicable governed access
        ↓
Mnemosyne: authoritative durable establishment and reporting
```

Where dataset governance requires workflow, existing Ponos / Ergo / Ergon /
Praxis capabilities may be used. No Calliope workflow engine or separate
human-interaction framework is introduced. Detailed management UI, Iris/forms
design and workflow are deferred.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="build-time-and-runtime"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope](../../../enterprise-architecture/continuum/application/application-component/466ad8f75a954c0581b5bba090443849.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## Build-time knowledge and runtime authority

> **Build-time Calliope knowledge assists software development; runtime Calliope
> authority determines validity and current governed membership/content.**

Calliope SHALL provide development-time representations of known runtime-managed
DataSets, CodeSets and ValueSets sufficient for efficient human, IDE-assisted and
AI-assisted software development. Such representations can support type safety,
code completion, documentation, static analysis and testing, with stable identity,
meaning, constraints, known build-time values and runtime-resolution contracts.

> **Development-time knowledge SHALL NOT be treated as authoritative evidence of
> current runtime membership or content.**

> **Development-time representation SHALL NOT require developers to sacrifice
> normal type-safe/code-assisted programming merely because a dataset is runtime
> mutable.**

A runtime value is not invalid merely because an earlier build did not know it.
Conversely, earlier software need not contain value-specific business behaviour
for later additions. Validation/membership uses current governed runtime authority;
the generated-code, IDE, AI-tooling and runtime access mechanisms remain deferred.

Structural contracts for accessing/managing DataSets SHALL derive from
Calliope-Models and the software build/deployment lifecycle. Runtime DataSets
provide mutable content under those contracts; they SHALL NOT bootstrap the
structural contracts required for their own access. This separates foundational
software dependency from runtime collaboration with Mneme, Mnemosyne and Ponos.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="security-and-assurance-nomenclature"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope](../../../enterprise-architecture/continuum/application/application-component/466ad8f75a954c0581b5bba090443849.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## Security and Assurance Nomenclature

Calliope has first-class responsibility for the definition, governance and
authoritative representation of shared security, privacy, policy, audit and
assurance nomenclature. This is broader than incidental storage of security
ValueSets.

```text
Calliope    defines / governs nomenclature
Themis      decides / enforces
Dokimasia   assures
Kleio       audits
```

| Calliope-Models illustrative definitions | Calliope-DataSets illustrative governed content |
| :--- | :--- |
| SecurityClassificationType | SecurityClassification ValueSet |
| CompartmentMetadataType | Compartment CodeSet / ValueSet |
| AssuranceFindingType | AssuranceFindingCategory ValueSet |
| CriteriaDefinition / DecisionReasonType | DisclosurePurpose CodeSet / AssuranceSeverity ValueSet |

Examples do not establish a final catalogue, schema or release inventory.
The four [U12 release-bounded authorisation ValueSets](component-register.md#authorisation-nomenclature)
are Calliope-controlled vocabularies:

- `Application.Function.AuthorisationPoint.Names`;
- `Application.Function.AllowedAction.Names`;
- `Information.Compartments`;
- `Information.Classification`.

Their controlled content belongs to the DataSets responsibility, while their
structural definitions/contracts belong to Models. Release-bounded scope does
not establish unrestricted runtime mutation of these particular ValueSets;
release-specific contents and detailed change/version rules remain deferred.
Themis owns their authorisation-policy association, role/Criteria resolution and
enforcement. Membership alone grants no authority. U12's Criterion conjunction,
additive UNION grants, temporal/revocation and local PEP semantics remain intact.
Calliope does not acquire policy decisions, assurance assessment or Audit Capture.
Kleio's [mandatory capture authority](component-register.md#kleio-capture-authority)
is governed intrinsic platform authority, separate from the originating
Identity's authorisation and from nomenclature ownership.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="calliope-deferred"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Calliope](../../../enterprise-architecture/continuum/application/application-component/466ad8f75a954c0581b5bba090443849.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## Deferred design and review boundary

The exact runtime ontology-management boundary, originating terminology-authority
relationships, ontology-review/update/synchronisation lifecycle, DataSet lifecycle,
version/effective-date and historical retention rules remain unresolved. So do
the relationship of historical Context to later DataSet versions, detailed
DataSet workflow, human-management UI, exact development representations/tooling,
runtime APIs, physical packaging, caching, persistence structures and final
security/assurance catalogue. Complete release-specific ValueSet contents remain
deferred under U12.

[CDG-24/49](gaps.md#calliope-contracts) distinguishes the now-established runtime
management responsibility from these open details and from proposed clinical
terminology/authoring services. Domain03 clinical-knowledge behaviour is not
automatically allocated wholesale to Calliope. No Domain04 Information Family or
capability coverage is inferred by this consolidation.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->
