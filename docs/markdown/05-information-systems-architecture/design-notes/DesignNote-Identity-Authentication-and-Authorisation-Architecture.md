# Design Note — Identity, Authentication and Authorisation Architecture

**Status:** Working Design Note  
**Purpose:** Capture the adjudicated Candidate Architecture for Harmonia Identity, Authentication and Authorisation prior to RADS consolidation.  
**Authority:** Working architecture record only; this Design Note does not supersede the canonical RADS.

## 1. Purpose and Scope

This Design Note records the current Harmonia Candidate Architecture for Identity.Authentication and Identity.Authorisation.

It establishes:
- the boundary between externally provided authentication and Harmonia-owned authorisation;
- the controlled authorisation nomenclature owned by Calliope;
- the Themis authorisation model;
- Criteria, Criteria containment, AuthorisationRole and assignment semantics;
- temporal validity and revocation as distinct concerns;
- precomputed Identity-specific PEP-Criteria and local PEP enforcement;
- PractitionerCapacity as the active operational capacity of a practitioner;
- the relationship between PractitionerCapacity, authorisation and operational responsibility;
- governed management and approval of authorisation changes; and
- architectural constraints for untagged information and delegation.

Detailed implementation mechanisms remain deferred where explicitly identified.

## 2. Identity Architecture

Identity comprises two distinct concerns:

### 2.1 Identity.Authentication

Identity.Authentication establishes **WHO** an Individual or System is.

Identity.Authentication is a **Separately Provided Capability (SEP)**. Harmonia's primary authentication pattern is standards-based OIDC/OAuth integration with an externally managed Identity Provider.

Harmonia does not provide or own an identity-management or credential-management framework.

For development, test and POC deployments:

> **Keycloak + PostgreSQL SHALL provide the default FOSS Identity Provider for Harmonia development, test and POC deployments. Keycloak remains an external, replaceable implementation component and SHALL NOT introduce Keycloak-specific dependencies into Harmonia's Identity.Authentication architecture.**

Keycloak is therefore the default implementation, not the architecture.

Identity.Authentication establishes a trusted authenticated identity but SHALL NOT determine Harmonia Function permissions, Function Scope or PEP-Criteria.

### 2.2 Identity.Authorisation

Identity.Authorisation establishes what an authenticated Identity may do within Harmonia.

Themis owns the Harmonia authorisation model, policy administration, policy repository semantics, policy decision/resolution and PEP enforcement framework.

Themis consumes trusted authenticated Identity. It does not authenticate that Identity.

A useful separation is:

> **Authentication establishes WHO. PractitionerCapacity establishes the capacity in which a Practitioner is acting. Themis establishes WHAT that Identity, acting in that capacity, may do.**

## 3. Controlled Authorisation Nomenclature

Calliope owns and governs the controlled nomenclature used by the Themis authorisation model.

The following release-bounded ValueSets are required:

### 3.1 Application.Function.AuthorisationPoint.Names

Architectural shorthand: **Function.Name**

Defines the bounded set of architecturally significant application authorisation points at which policy enforcement is meaningful.

The ValueSet SHALL be:
- comprehensive enough to satisfy business security requirements for the release; and
- constrained enough to avoid excessive management, development and support burden.

An AuthorisationPoint identifies a governed application function. It is not itself a permission.

### 3.2 Application.Function.AllowedAction.Names

Architectural shorthand: **Function.Scope**

Defines the bounded set of actions that may be permitted at applicable AuthorisationPoints.

Known architectural action concepts include ACCESS, CHANGE and FORWARD. The complete release-specific ValueSet remains subject to definition and governance.

### 3.3 Information.Compartments

Defines the governed information compartments used when constraining authorisation.

### 3.4 Information.Classification

Defines the governed information classifications used when constraining authorisation.

Calliope defines these vocabularies. Themis owns their policy association, resolution and enforcement.

## 4. Authorisation Criterion

A Criterion represents a governed permitted combination across the applicable authorisation dimensions.

Conceptually:

    Criterion =
        Information.Compartment
        AND Information.Classification
        AND Function.Name
        AND Function.Scope
        AND applicable Temporal constraints

The four principal authorisation dimensions describe **what** is authorised. Temporal constraints describe **when** that authorisation is applicable.

Within a Criterion, applicable dimensions are conjunctive.

Only explicitly established governed combinations have authorisation meaning. The existence of values in each ValueSet does not imply that every Cartesian combination is meaningful or permitted.

Absence of a matching effective Criterion results in **DENY**.

The Candidate Architecture does not introduce explicit negative/DENY Criteria. Such a mechanism SHALL NOT be introduced unless a demonstrated business requirement justifies the additional policy semantics.

## 5. Criteria Containment and Collections

Criteria may be collected and nested for management and reuse.

Conceptually:

    CriteriaCollection
      ├── Criterion
      ├── Criterion
      └── CriteriaCollection
           ├── Criterion
           └── Criterion

Containment is primarily a management and reuse construct. It does not create arbitrary Boolean policy expressions.

Flattening nested collections produces the same additive set of effective Criteria.

The final implementation name and representation of the collection construct remain open; the architectural requirement is containment/nesting, not a specific class hierarchy.

## 6. AuthorisationRole

An **AuthorisationRole** is a named, governed, assignable collection of authorisation Criteria and/or Criteria collections.

Criteria contained by an AuthorisationRole are **additive**.

Each Criterion independently contributes its permitted capability to the effective Criteria derived from the AuthorisationRole.

Therefore:

    EffectiveCriteria(AuthorisationRole)
        = UNION(all contained Criteria)

AuthorisationRole containment SHALL NOT be interpreted as requiring all contained Criteria to be simultaneously satisfied.

Role hierarchy is not required merely to provide reuse or nesting; Criteria containment provides that capability.

## 7. Identity Assignment and Effective Criteria

An Identity may have:
- directly assigned Criteria;
- one or more assigned AuthorisationRoles; or
- both.

The resulting authorisation grants are additive:

    IdentityEffectiveCriteria =
        DirectCriteria
        UNION AuthorisationRole1.Criteria
        UNION AuthorisationRole2.Criteria
        UNION ...
        UNION AuthorisationRoleN.Criteria

The union semantics apply to grants. Individual Criteria retain conjunctive semantics across their applicable dimensions.

The model applies to both human and system Identities. Different allowable assignments may exist, but a separate authorisation architecture is not required for system identities.

## 8. Temporal Validity and Revocation

Temporal validity and revocation are distinct authorisation concerns.

### 8.1 Temporal Validity

Temporal constraints define predetermined periods or conditions during which a Criterion or assignment is valid.

Temporal constraints may apply to:
- a Criterion;
- a direct Criterion assignment;
- an AuthorisationRole assignment; and
- operational context such as PractitionerCapacity where applicable.

Detailed temporal expression syntax remains deferred.

### 8.2 Revocation

Revocation withdraws an otherwise valid authorisation.

Revocation SHALL NOT be treated merely as temporal expiry.

The detailed propagation and invalidation mechanism for already-issued PEP-Criteria / PEP-Context remains a design concern. The Candidate Architecture requires support for both predetermined temporal expiry and explicit revocation.

## 9. PEP-Criteria

Themis resolves its authorisation model into a **precomputed, Identity-specific set of permitted combinations**: PEP-Criteria.

PEP-Criteria is not a general-purpose policy program to be interpreted by every PEP.

The PDP resolves:
- direct Criteria;
- AuthorisationRole assignments;
- nested Criteria collections;
- applicable temporal constraints;
- PractitionerCapacity context where applicable; and
- authoritative policy state.

The resulting PEP-Criteria is distributed through the applicable PEP-Context and can be enforced locally.

This supports the established availability rule that an existing valid PEP-Context may continue to support in-flight/local enforcement during PDP unavailability. If a new, renewed or replacement authorisation context is required and the PDP is unavailable, activity waits; PDP unavailability is not itself a DENY decision.

### 9.1 PEP Enforcement

A local PEP evaluates the requested operation against:
- authenticated Identity context;
- effective Information.Compartment;
- effective Information.Classification;
- Function.Name;
- Function.Scope;
- applicable temporal state; and
- the Identity's valid PEP-Criteria.

The PEP is intentionally constrained and deterministic. It SHALL NOT become a general-purpose policy-language interpreter.

### 9.2 Provenance

Themis SHALL retain sufficient provenance to explain the derivation of effective PEP-Criteria, including the authoritative Criteria, Criteria collections, AuthorisationRole assignments, direct assignments and applicable policy state from which authorisation was derived.

Runtime PEP-Criteria may use stable references rather than carrying the complete derivation history.

## 10. Untagged Information and Effective Security Attributes

Absence of an explicit information security tag SHALL NOT imply absence of security control.

Where required Information.Compartment and/or Information.Classification attributes are absent, the applicable enforcement mechanism SHALL establish effective values through governed, deterministic rules.

If required effective security attributes cannot be established, enforcement SHALL fail closed.

Deriving effective security attributes for an authorisation decision is distinct from persistently assigning or changing security metadata on an Information Unit.

The precise ownership and precedence rules for deriving effective Compartment/Classification from information context, containment or other authoritative relationships remain to be adjudicated. Themis enforces the resulting attributes; it is not yet assumed to own all semantic derivation rules.

## 11. PractitionerCapacity

The term **PractitionerCapacity** distinguishes Harmonia's active operational context from both the FHIR PractitionerRole resource and Themis AuthorisationRole.

Definitions:

- **Practitioner** — the individual practitioner.
- **FHIR PractitionerRole** — the formal practitioner role/service relationship represented through FHIR.
- **PractitionerCapacity** — the selected operational capacity in which the Practitioner is currently acting within Harmonia.
- **AuthorisationRole** — a governed collection of additive authorisation Criteria used by Themis.

For a Practitioner, the available PractitionerCapacities are constrained by the practitioner's governed profile and applicable operational context.

A Practitioner selects **one active PractitionerCapacity** for the applicable session or operational/roster context.

PractitionerCapacity does not itself confer authority.

> **The selected PractitionerCapacity provides operational context used by Themis when deriving effective authorisation. Authority remains derived from governed Criteria applicable to the authenticated Identity within that Capacity.**

Conceptually:

    Authenticated Practitioner
            +
    Practitioner Profile
            +
    applicable operational/roster context
            ↓
    eligible PractitionerCapacity set
            ↓
    SELECT ONE
            ↓
    Active PractitionerCapacity
            ├── Themis authorisation context
            │      ↓
            │   PEP-Criteria
            │
            └── operational responsibility context
                   ↓
                Agora / Synapse membership
                   ↓
             escalation / approval notifications

The detailed mapping between PractitionerCapacity and FHIR PractitionerRole remains a later design concern.

## 12. Operational Responsibility versus Authorisation

PractitionerCapacity may establish both:
1. the operating context used by Themis when deriving effective PEP-Criteria; and
2. operational responsibility/membership context consumed by other Harmonia capabilities, including future Agora/Synapse mechanisms.

These concerns remain distinct.

Themis answers:

> **May this Identity perform this action on this information?**

Agora/Synapse will answer questions concerning current operational responsibility, notification, escalation and approval routing.

Receipt of an escalation or approval notification does not itself grant information access. When the Practitioner follows such an activity into Iris or another application surface, Themis PEP enforcement applies using the current valid PEP-Criteria.

Detailed Agora/Synapse architecture is explicitly outside the scope of this Design Note.

## 13. Delegation / Forwarding

Harmonia does not require a separate general-purpose delegation architecture at this stage.

Delegation is represented as **Identity / PEP-Criteria pass-off (forwarding)**.

Forwarding SHALL itself be subject to applicable FORWARD authorisation.

Provenance SHALL preserve the distinction between:
- the Identity from which authority originated; and
- the Identity operating using forwarded authority.

Forwarding does not cause the receiving Identity to become the originating Identity.

Detailed forwarding representation and lifecycle remain deferred.

## 14. Authorisation Administration and Approval

Authorisation administration is governed Harmonia work.

Changes such as:
- creation/change of Criteria;
- Criteria collection management;
- AuthorisationRole management;
- direct Identity Criteria assignment;
- AuthorisationRole assignment;
- temporal changes; and
- revocation

SHALL be subject to appropriate governed management and approval processes.

The architectural responsibility split is:

    Calliope
       └── controlled authorisation nomenclature

    Themis
       ├── PAP
       ├── PRP
       ├── PDP
       ├── authorisation model
       ├── Criteria / collections / AuthorisationRoles
       ├── Identity assignments
       ├── Criteria compilation
       └── PEP framework

    Ergo / Praxis
       └── governed authorisation-management and approval activity

    Iris
       └── human interaction for management, review and approval

Iris does not own the authorisation model merely because it supplies its human-facing management interface.

## 15. Target Implementation Position

The Candidate Architecture does not require a third-party general-purpose authorisation engine.

Current target responsibility allocation is:

| Responsibility | Target |
|---|---|
| Identity authentication | Keycloak / external OIDC IdP |
| Authorisation nomenclature | Calliope |
| Authorisation model | Themis-owned |
| Role/group resolution | Themis-owned |
| Criteria compilation | Themis-owned |
| Policy repository | Themis PRP, persisted through Harmonia |
| Policy administration | Themis PAP |
| Approval workflows | Ergo/Praxis |
| Administration UI | Iris |
| Runtime enforcement | Themis PEP library |
| Untagged information handling | Governed effective-attribute resolution |

Existing FOSS policy frameworks such as jCasbin, OPA, Cedar and related implementations may be used as design references and, where licensing permits, sources of reusable implementation patterns. Their conceptual models SHALL NOT redefine Harmonia's authorisation semantics.

## 16. Architectural Invariants

The following are Candidate Architecture invariants:

1. Authentication establishes Identity; it does not establish Harmonia authority.
2. Identity.Authentication is SEP.
3. Calliope owns controlled authorisation nomenclature; Themis owns policy semantics and enforcement.
4. Within a Criterion, applicable dimensions are conjunctive.
5. Between Criteria, grants are additive (UNION).
6. Direct Criteria and AuthorisationRole-derived Criteria are additive.
7. Absence of a matching effective Criterion is DENY.
8. Explicit negative/DENY Criteria are not part of the Candidate Architecture.
9. Temporal validity and revocation are distinct.
10. PEP-Criteria is precomputed and Identity-specific.
11. The local PEP performs constrained deterministic enforcement rather than general policy interpretation.
12. Untagged information is not unrestricted information.
13. PractitionerCapacity establishes active operating context but does not itself grant authority.
14. Only one PractitionerCapacity is active for the applicable practitioner session/operational context.
15. Operational responsibility and authorisation are distinct even when derived from the same PractitionerCapacity.
16. Delegated/forwarded authority preserves provenance and remains subject to FORWARD authorisation.
17. Authorisation-model changes are governed work and may require review/approval through Ergo/Praxis.

## 17. Deferred Design / Discovery

The following remain intentionally unresolved for later design:

- complete release-specific contents of each authorisation ValueSet;
- exact temporal expression model;
- revocation propagation and PEP-Context invalidation mechanism;
- exact Criteria collection implementation representation;
- exact PEP-Criteria data/wire representation;
- effective Compartment/Classification derivation ownership and precedence;
- detailed PractitionerCapacity selection UI;
- exact mapping between PractitionerCapacity and FHIR PractitionerRole;
- detailed Agora/Synapse membership, escalation and approval routing;
- detailed PAP administration Praxes/Erga;
- detailed forwarding/delegation lifecycle;
- PEP-Criteria refresh, caching and distribution mechanics beyond established architectural constraints.

These are not considered gaps preventing acceptance of the Identity Candidate Architecture.

## 18. Candidate Architecture Position

For Domain05 Candidate Architecture purposes, Identity.Authentication and Identity.Authorisation are considered sufficiently adjudicated.

The architecture deliberately avoids building a general-purpose IAM or policy-programming framework. It instead establishes a constrained Harmonia authorisation model whose effective grants can be resolved centrally by Themis and enforced deterministically at distributed PEPs.

The central relationship is:

    WHO?
      Identity.Authentication
              +
    IN WHAT CAPACITY?
      PractitionerCapacity (where applicable)
              +
    WHAT GOVERNED AUTHORITY?
      Themis Criteria / AuthorisationRoles / Policy
              ↓
         PEP-Criteria
              ↓
    WHAT MAY THIS IDENTITY DO HERE?
      Local Themis PEP
