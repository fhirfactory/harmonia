# DRP — Themis Policy Definition, Passport Management and Enforcement

**Document status:** Candidate / Draft 001 — pending Solution Architect review and architectural incorporation  
**Proposed identifier:** `DRP-Themis-Policy-Passport-Enforcement-001`  
**Programme:** Harmonia — Data Governance Services, Version 001  
**Date:** 28 October 2026  
**Architectural authority:** Solution Architect  
**Scope:** Themis policy definition, AuthorisationMap/Passport lifecycle, distribution, enforcement contracts and subsystem boundaries

## 1. Purpose

Define the candidate architecture for reusable authorisation enforcement across Harmonia, including the division of responsibility between Calliope, Themis, Mneme and protected subsystems. The proposal minimises authentication-time policy processing by preconstructing Passports when their governing authorisation inputs change.

This document records the agreed direction and identifies matters still requiring design. It does **not** assert that the proposed elements, requirements or axiom have already been incorporated into ArchiMate or the governed Continuum.

## 2. Context and design intent

Harmonia subsystems require a consistent mechanism for determining whether an authenticated Identity is authorised to perform an operation against a protected subject. Protection must not be confined to InformationUnits: the same enforcement pattern should be reusable for services, functions, tasks, workflows and other protected subjects where appropriate.

A protected subject supplies its **RequiredCriteria**. The authenticated Identity has an effective **AuthorisationMap**, also termed its **Passport**. A **Policy Enforcement Point (PEP)** evaluates the Passport against the RequiredCriteria and enforces the determination.

The authoritative Passport is built and maintained by Themis independently of ordinary authentication and access requests. Mneme holds the operational Passport used by distributed PEP clients.

## 3. Architectural principles and candidate axiom

### 3.1 Separation of model and operational authority — candidate axiom

> **Calliope SHALL define and maintain Harmonia's foundational models and contracts, while operational subsystem behaviour SHALL remain the responsibility of the architecturally assigned subsystem.**

The axiom does not prohibit model-intrinsic behaviour, such as validation and maintenance of internal consistency. It prohibits moving Themis policy enforcement, Mneme operational orchestration or Mnemosyne persistence responsibility into Calliope merely because their models or interfaces reside there.

**Status:** Candidate. Reconcile against the existing axiom catalogue before allocating an AX identifier.

### 3.2 Governing distinctions

- **RequiredCriteria** describes the authorisation conditions demanded by the protected subject.
- **AuthorisationMap / Passport** describes the effective authorisations of an authenticated Identity.
- **Policy definition** is authoritative input to Passport construction; a Passport is a derived, operationally usable authorisation representation.
- **Policy enforcement** is distinct from policy definition, Passport construction, distribution and storage.
- **Authoritative Passport state** in Themis PR is distinct from the **operational Passport manifestation** in Mneme.
- **Authentication** establishes Identity; it does not ordinarily require policy interpretation or Passport construction.
- **Change in circumstance** may require revalidation of many Passports, even if no individual Identity record changes.

## 4. Calliope model and contract structure

Proposed model package organisation:

```text
model.themis
├── policydefinition
│   ├── criteria
│   │   └── ... RequiredCriteria constituent models
│   └── passport
│       └── ... AuthorisationMap / Passport constituent models
└── policyenforcement
    └── ... PEP interfaces and related contracts
```

These packages are part of Calliope. They define types, semantics and interfaces, **not** Themis operational enforcement implementations.

A concrete `PolicyEnforcementPoint` implementation class is not required in Calliope solely to represent the service; a suitable interface/contract is sufficient. Concrete types and method signatures remain to be designed.

## 5. Proposed Themis Maven modules

Names are provisional and subject to refinement.

| Maven module | Role | Primary responsibility |
|---|---|---|
| `harmonia-themis-pep` | Distributed client library | Reusable enforcement implementation consumed by other Harmonia subsystems; evaluates a subject's RequiredCriteria against the applicable Passport. |
| `harmonia-themis-pr` | Policy Repository | Maintains authoritative policy definitions and preconstructed authoritative Passports; constructs and revalidates Passports when relevant inputs change. |
| `harmonia-themis-pd` | Policy Distribution | Listens for authentication and other relevant lifecycle events; retrieves authoritative Passports from PR; establishes, updates or withdraws operational Passports in Mneme. |
| `harmonia-themis-oam` | Operations, Administration and Management | Supports administrative interfaces, UI backend/frontend support, logging, monitoring and related operational management. |

This modularisation avoids requiring normal Harmonia subsystem clients to depend on PR, PD or OAM implementations. They consume the PEP library and Calliope contracts.

## 6. Component authority and dependency boundaries

### 6.1 Calliope

Defines the RequiredCriteria, Passport and policy-enforcement contracts and associated foundational models. It does not own operational policy enforcement or Passport distribution.

### 6.2 Themis PR

Owns authoritative policy definitions, their interpretation for Passport construction, and authoritative preconstructed Passport state. It is intended to be **relatively static operationally**: ordinary login events do not cause policy compilation. PR responds to changes in Identity authorisations, policy definitions or other relevant governing circumstances by constructing or revalidating affected Passports.

### 6.3 Themis PD

Owns the **event-driven operational lifecycle** of Passport distribution. On authentication, it retrieves an already constructed Passport from PR and establishes the corresponding operational Passport in Mneme. On relevant authorisation changes, it propagates replacement, invalidation or withdrawal to Mneme. PD does not independently reinterpret policy definitions to construct Passports.

### 6.4 Themis PEP

A reusable client-side enforcement library for Harmonia subsystems. It obtains the applicable operational Passport by reference through Mneme's governed interfaces, obtains the protected subject's RequiredCriteria through the subject's contract, evaluates and enforces the authorisation decision. It must not require every consumer to embed the entire Themis server-side implementation.

### 6.5 Themis OAM

Provides administration and operational support for policy and Passport management. Administrative actions affecting authorisation must be reflected through the authoritative PR lifecycle, rather than bypassing it through direct edits to operational Mneme Passport instances.

### 6.6 Mneme

Manages the operational Passport instance and makes it accessible through appropriate governed interfaces. Mneme is not the authority for policy definition or interpretation. Themis retains control of operational Passport currency, replacement and withdrawal through PD.

### 6.7 Protected Harmonia subsystems

Each protected subject owns or exposes its applicable RequiredCriteria using Calliope contracts. The subject does not construct the authenticated Identity's Passport and does not reimplement Themis policy interpretation.

## 7. Authorisation constructs

### 7.1 RequiredCriteria

`RequiredCriteria` describes what the protected subject requires. For an InformationUnit, the candidate criteria domains are:

| Domain | Illustrative criteria |
|---|---|
| `containerCriteria` | Container Topic, container status and relevant carrier context |
| `contentCriteria` | Content Topic, content status, external origin, classification and compartment tags |
| `unitCriteria` | InformationUnit status, temporal context and source within Harmonia |

These are candidate model groupings, not final Java class or attribute names. The InformationUnit's internal metadata may be used to derive its RequiredCriteria, but external callers should access them through encapsulated behaviour rather than traverse internal metadata directly.

Other protected subject types may expose different RequiredCriteria structures through compatible contracts. Do not assume InformationUnit-specific criteria are universally applicable.

### 7.2 AuthorisationMap / Passport

A Passport is a preconstructed representation of an Identity's effective authorisations under the applicable policy definitions and governing circumstances. It is authoritative in PR and distributed to Mneme for operational use.

Its precise internal composition, identity/reference, version/currency representation, validity and signature/integrity semantics are **to be designed**. Avoid presupposing a particular bit-mask implementation: **Mask** is not the preferred architectural terminology for this mechanism.

### 7.3 Policy enforcement result

The PEP evaluates RequiredCriteria against the applicable Passport and produces an authorisation determination, which it enforces. The initial conceptual result is **allow/deny**; detailed outcomes, diagnostics, audit and exceptional handling remain to be designed.

## 8. Passport lifecycle

### 8.1 Identity creation or individual authorisation update

1. An Identity is created or its relevant authorisation inputs change.
2. PR determines the affected Passport and constructs or updates it from authoritative policy definitions and applicable Identity context.
3. PR retains the authoritative Passport.
4. PD distributes the updated Passport, or its withdrawal/invalidation, to any relevant operational instance in Mneme.

An individual update ordinarily affects the relevant Identity's Passport, subject to other dependencies discovered by the policy model.

### 8.2 Authentication / login

1. Authentication establishes the Identity and emits a relevant event.
2. PD handles the event.
3. PD retrieves the **preconstructed** Passport from PR.
4. PD establishes or associates its operational manifestation in Mneme.
5. Subsequent protected operations use the PEP and the Mneme-managed Passport by reference.

**Design intent:** No routine Passport construction or full policy interpretation on the authentication path. Retrieval, validity checks and operational association may still be necessary.

### 8.3 Protected operation

1. The protected subject exposes its RequiredCriteria.
2. The PEP obtains the applicable operational Passport through Mneme.
3. The PEP evaluates the Passport against RequiredCriteria.
4. The PEP enforces the determination.

The interface should be common across Harmonia, without imposing InformationUnit-specific internals on other protected subjects.

### 8.4 Change in circumstance

A policy or governing circumstance may change without any individual Identity being edited—for example, a role's authorisations, organisational policy, classification rules or another policy input.

1. The change is established in the authoritative policy domain.
2. PR identifies Passports potentially affected by the changed circumstance.
3. PR revalidates affected Passports; this may require substantial processing and may affect many or all Passports.
4. Revalidation may leave a Passport unchanged, replace it, or render it invalid/withdrawn.
5. PD ensures affected operational Passport manifestations in Mneme are updated or withdrawn.

This is expected to be less frequent than login and access operations. The design should optimise the common path without compromising authorisation correctness.

### 8.5 Logout, expiry and revocation

The exact semantics of logout, multiple sessions, Passport reuse, expiry and explicit revocation are unresolved. The architecture must permit Themis to invalidate or withdraw operational authorisations when required; it must not assume a Passport is permanently valid once distributed.

## 9. Currency, revalidation and enforcement safety

**Candidate functional requirement:**

> **Harmonia SHALL prevent a Passport whose governing authorisation circumstances are no longer valid from being used to grant access.**

A change in circumstance may trigger expensive, non-instantaneous revalidation. The architecture must distinguish an authoritative change from the later completion of Passport regeneration and distribution.

No particular concurrency mechanism is prescribed here. Candidate approaches to investigate include policy epochs/versions, invalidation markers, atomic replacement, scoped suspension, and other means of ensuring stale Passports cannot continue to authorise access. Their suitability, cost and fail-safe semantics require a separate design decision.

The intended invariant is **authorisation correctness during transition**, not that every Passport must be indiscriminately rebuilt or withdrawn on every change.

## 10. Candidate functional requirements

These are candidate requirements for subsequent allocation to ArchiMate elements and the governed Continuum. Identifiers are local to this DRP and are **not** canonical requirement IDs.

| ID | Candidate requirement |
|---|---|
| TH-01 | Harmonia SHALL provide a common policy-enforcement contract usable by protected subsystems. |
| TH-02 | Harmonia SHALL allow a protected subject to expose its RequiredCriteria without exposing its internal representation. |
| TH-03 | Harmonia SHALL represent effective Identity authorisations through an AuthorisationMap (Passport) defined by Calliope. |
| TH-04 | Harmonia SHALL maintain authoritative policy definitions and authoritative preconstructed Passports through Themis PR. |
| TH-05 | Harmonia SHALL construct or update an affected Passport when its relevant Identity authorisation inputs change. |
| TH-06 | Harmonia SHALL revalidate potentially affected Passports when their governing authorisation circumstances change. |
| TH-07 | Harmonia SHALL ordinarily retrieve an existing Passport during authentication rather than reconstruct it. |
| TH-08 | Harmonia SHALL distribute and manage operational Passport currency through Themis PD and Mneme. |
| TH-09 | Harmonia SHALL support replacement, invalidation and withdrawal of operational Passports following authoritative changes. |
| TH-10 | Harmonia SHALL enforce authorisation determinations by evaluating RequiredCriteria against the applicable Passport through a reusable PEP. |
| TH-11 | Harmonia SHALL prevent authorisation based on a Passport invalidated by changed governing circumstances. |
| TH-12 | Harmonia SHALL preserve the distinction between Calliope's foundational models/contracts and Themis's operational enforcement behaviour. |
| TH-13 | Harmonia SHALL provide administrative and operational management of policy and Passport lifecycles without bypassing authoritative controls. |

## 11. Candidate interaction inventory

| Interaction | Initiator | Provider / target | Intent |
|---|---|---|---|
| Policy administration | OAM / authorised administrator | PR | Create or change authoritative policy definitions and related authorisation inputs. |
| Passport construction/update | Authoritative Identity or policy change | PR | Construct/revalidate affected authoritative Passport(s). |
| Login-triggered distribution | Authentication event | PD → PR → Mneme | Retrieve preconstructed Passport and establish operational manifestation. |
| Authorisation-change propagation | PR / relevant authoritative change | PD → Mneme | Replace, invalidate or withdraw affected operational Passports. |
| Protected-operation enforcement | Harmonia subsystem | PEP → Mneme | Evaluate RequiredCriteria against operational Passport and enforce determination. |

These are logical interactions, not final synchronous/asynchronous protocols, API endpoints or event contracts.

## 12. Security, audit and operational considerations

- Themis remains authoritative for policy interpretation and authorisation state; Mneme is an operational custodian, not a policy authority.
- Administrative modifications must be authorised and auditable.
- Passport construction, revalidation, distribution, invalidation and enforcement determinations should have appropriate audit evidence; precise event schemas and logging boundaries are pending.
- Passport transport and storage require suitable confidentiality and integrity protections; mechanisms are pending.
- PEP failure, missing Passport, stale Passport, PD outage and partial distribution must not silently result in authorisation being granted. Exact failure semantics require design.
- Authentication, policy changes and Passport distribution are distinct events with distinct responsibilities.
- Operational observability should expose revalidation and distribution status without unnecessarily exposing sensitive authorisation contents.

## 13. Decisions recorded and unresolved questions

### 13.1 Design direction captured

1. Calliope contains `model.themis.policydefinition.criteria`, `model.themis.policydefinition.passport` and `model.themis.policyenforcement` models/contracts.
2. Themis PEP is a reusable client Maven module for other Harmonia subsystems.
3. Themis PR holds policy definitions and authoritative preconstructed Passports.
4. Themis PD listens for login and other relevant events and distributes Passports into Mneme.
5. Themis OAM supports operational administration, management and logging/UI functions.
6. Passport construction normally occurs when authorisation inputs change, **not** at login.
7. Individual changes update affected Passports; changes in circumstance cause potentially broad revalidation.
8. Themis actively maintains operational authorisation currency after authentication.
9. Module names and architectural elements remain provisional pending Solution Architect review and governance incorporation.

### 13.2 Open design questions

- Exact meaning and identity of Passport versus AuthorisationMap; whether they are synonyms or separate conceptual layers.
- How PR determines the set of Passports affected by a policy/circumstance change.
- How PR receives authoritative Identity changes and policy change notifications.
- Whether Passport construction is synchronous with the originating change or an asynchronous governed activity.
- Passport validity, versioning, integrity and correlation to governing policy state.
- Safe transition behaviour during bulk revalidation and partial distribution.
- Operational association of Passport to Identity, session, principal or execution context; multiple concurrent sessions.
- PEP interface signatures, authorisation actions, decision representation and audit obligations.
- Mneme interfaces for storing, retrieving, replacing and invalidating operational Passports.
- Logout, revocation, expiry, cache policy, availability and failure semantics.
- Precise OAM boundaries, including whether UI and logging are separate modules.
- Final Maven module names and their packaging/deployment topology.

## 14. Relationship to InformationUnit design

This DRP defines the shared Themis authorisation model and enforcement pattern. The InformationUnit DRP should be updated subsequently to:

1. Reference Calliope's `RequiredCriteria` model and PEP contract.
2. Explain how InformationUnit-owned metadata contributes to `containerCriteria`, `contentCriteria` and `unitCriteria` without leaking encapsulated internal structures.
3. Describe the InformationUnit as a **protected subject**, not as the owner of Themis policy interpretation or Passport lifecycle.
4. Retain the separate InformationUnit structure: `Identity`, `Metadata`, and exactly one `InformationRepresentation` containing `ContainerMetadata`, `ContentMetadata` and `Content`.
5. Preserve the distinction between InformationUnit governance, Mneme operational context, Mnemosyne persistence and Themis authorisation enforcement.

No InformationUnit DRP changes are made by this document.

## 15. Governance and next steps

1. Solution Architect reviews this candidate DRP and resolves any conceptual corrections.
2. Update the InformationUnit DRP to reference the accepted Themis model and encapsulated RequiredCriteria contract.
3. Populate the relevant capabilities, features, requirements, topics, packages, interfaces and relationships in ArchiMate.
4. Round-trip the accepted architecture into the governed Markdown Continuum according to Harmonia governance.
5. Reconcile the candidate axiom and requirement identities against the canonical catalogue.
6. Refine technical contracts and lifecycle mechanisms in subsequent design work; do not treat this candidate DRP as implementation authorisation.

---

**End of candidate DRP — Draft 001.**
