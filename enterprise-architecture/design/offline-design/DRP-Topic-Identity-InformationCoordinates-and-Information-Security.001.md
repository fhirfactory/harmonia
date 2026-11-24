# Harmonia — Design Review Package
## Topic Identity, InformationCoordinates and Information Security

**Status:** Candidate — awaiting Solution Architect review  
**Architectural activity:** Information Architecture development  
**Proposed operation:** Governed ADD/UPDATE, subject to reconciliation  
**Authority:** Solution Architect  
**Target:** Harmonia Architecture Continuum

---

## 1. Purpose

This Design Review Package captures the agreed architectural concepts concerning:

- Topic identity and naming.
- Distinguished-name conventions.
- Topic immutability and lifecycle.
- InformationUnit identity and associated Topic references.
- InformationCoordinates.
- Hierarchical matching for subscriptions, routing and security filtering.
- Integration with Themis security classifications, compartments and authorisation.

The purpose is to preserve architectural intent for subsequent review and incorporation into the Architecture Continuum.

This DRP does not authorise implementation, introduce new security policy or establish additional architectural requirements.

## 2. Architectural rationale

Harmonia requires a consistent mechanism for identifying information and describing its ownership, location and architectural classification.

The mechanism must support information processing across component boundaries without requiring every component to independently reconstruct the meaning of an information identifier.

The architecture uses **Topic** as a common identity and naming construct.

Topics provide persistent identity and structured names capable of representing different containment hierarchies.

The same naming machinery can support hierarchical matching for information subscriptions, routing and security filtering.

InformationCoordinates brings together named Topic references describing distinct dimensions of an InformationUnit.

Themis provides the complementary security attributes and authorisation mechanisms.

## 3. Topic

### 3.1 Definition

**Topic** is an identifiable architectural and information-management construct that provides persistent identity and structured naming representations.

Its foundational properties are:

| Property | Description |
|---|---|
| `canonicalId` | Persistent identity of the Topic |
| `canonicalName` | Architecturally qualified distinguished name |
| `friendlyName` | Unqualified, containment-oriented name |
| `displayName` | Locally meaningful presentation name |

### 3.2 Identity

`canonicalId` is immutable throughout the Topic's lifecycle.

Renaming, reclassification and changes in containment do not change the canonicalId.

The canonicalId is therefore independent of the Topic's current naming representation.

### 3.3 Development-time mutability

During architecture and development activities, the following may change:

- `canonicalName`
- `friendlyName`
- `displayName`
- Architectural classification
- Containment relationships

The canonicalId remains unchanged.

### 3.4 Runtime immutability

At runtime, Topic representations are immutable.

This permits Topic values to be distributed and used by multiple components without mutation during processing.

Topic identity is independent of the particular runtime object instance carrying its representation.

Runtime immutability does not prohibit governed changes to Topic definitions during development or between releases.

The mechanism for propagating updated Topic definitions to runtime environments remains outside this DRP.

## 4. Distinguished-name conventions

Harmonia's naming approach draws conceptually upon the distinguished-name conventions associated with CMIP/CMISE.

It adopts selected concepts rather than requiring complete CMIP/CMISE naming semantics.

### 4.1 canonicalName

`canonicalName` expresses the architectural classification of an element and its qualified containment hierarchy.

The general structure is:

`ObjectClassification::QualifiedContainmentPath`

Each path segment identifies a classification and a local name.

Example:

`Constraint::SolutionFacet=ArchitecturalGovernance.SolutionFacet=ArchitectureConformance.MotivationFacet=Constraints`

The prefix before `::` identifies the classification of the named object.

The subsequent segments identify the qualified containment path.

The qualifiers reflect the architectural classification of the corresponding elements.

### 4.2 Capability and Feature naming

The established capability naming convention historically combined classification dimensions.

Example:

`CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-CrossAuthorityIdentifierCorrelation`

The Solution Architect has identified the following preferred representation:

`BEC::CT1=EntityInformationEnablement.CT2=ClientAdministration.CT3=PersonIdentityAdministration.FEAT=CrossAuthorityIdentifierCorrelation`

This separates the overall classification from the classifications of individual containment segments.

This is a candidate correction to the existing naming convention, not an instruction to migrate all capability names immediately.

### 4.3 friendlyName

`friendlyName` expresses the containment hierarchy as a dot-separated name without architectural type qualifiers.

Example:

`ArchitecturalGovernance.ArchitectureConformance.Constraints`

The name is intended to be useful in Java and other programming contexts, including filtering, parsing and contextual discrimination.

### 4.4 displayName

`displayName` is a locally meaningful, human-readable presentation name.

For ordinary elements, the local name is generally sufficient.

For repeatedly used generic leaf names, the immediate parent name may be incorporated to provide useful context.

Example:

`ArchitectureConformance - Constraints`

### 4.5 Relationship to containment

Composition establishes authoritative architectural containment.

The canonicalName and friendlyName express containment but do not independently establish it.

A naming discrepancy must not automatically cause a change to Composition relationships.

The canonicalId remains stable when the element's containment or naming changes.

## 5. InformationUnit

### 5.1 Conceptual structure

An InformationUnit may carry Topic references identifying the information itself and its associated information-management dimensions.

The initial conceptual model discussed was:

```java
class InformationUnit {
    TopicID id;
    TopicID informationOwner;
    TopicID informationLocation;
    // Other governed information properties
}
```

The subsequent introduction of InformationCoordinates provides an encapsulation for the named contextual references.

The precise final InformationUnit class structure remains subject to architectural reconciliation.

### 5.2 Topic references

A TopicID identifies a Topic rather than embedding a distinguished-name string directly in the InformationUnit.

The associated Topic provides its identity and naming representations.

A runtime component may resolve the reference or receive the relevant Topic representation by value.

The precise representation and resolution contract remain to be established.

## 6. InformationCoordinates

### 6.1 Definition

**InformationCoordinates** is a structured collection of named Topic references identifying an InformationUnit's position across distinct information-management dimensions.

The initially agreed dimensions are:

- Information ownership.
- Information location.

### 6.2 Conceptual structure

```java
class InformationCoordinates {
    TopicID informationOwner;
    TopicID informationLocation;
}
```

This is a conceptual class definition, not an implementation contract.

### 6.3 Named reference semantics

`informationOwner` and `informationLocation` are explicitly named properties with distinct meanings.

They are not interchangeable entries in an arbitrary Topic collection.

**informationOwner** identifies the Topic representing the owner of the information.

**informationLocation** identifies the Topic representing the location of the information.

These dimensions may use different classification and containment hierarchies.

### 6.4 Examples

An ownership Topic might have:

`Patient::EMPI=...`

or:

`Patient::Id=...`

An information-location Topic might have:

`Observation::Enterprise=TasHealth`

Other examples include:

`Thing::Enterprise=TasHealth.Department=...`

and:

`Thing::Enterprise=TasHealth.System=Harmonia.Subsystem=...`

These examples demonstrate that ownership and location are not restricted to a single common hierarchy.

They do not establish mandatory Topic classifications or naming segments.

### 6.5 Relationship to InformationUnit

The proposed conceptual relationship is:

```java
class InformationUnit {
    TopicID id;
    InformationCoordinates informationCoordinates;
    // Other governed information properties
}
```

This represents the intended encapsulation of named Topic references.

Whether InformationCoordinates is embedded, referenced or otherwise represented within the InformationUnit remains an implementation and contract-design matter.

## 7. Topic distribution and runtime processing

Topics are immutable at runtime.

They may therefore be passed by value between components.

This permits processing logic to inspect the relevant Topic identity and naming properties without necessarily performing a repository lookup for each evaluation.

The same Topic representations may be used by multiple architectural mechanisms.

Passing a Topic by value does not change its canonicalId.

The architecture must distinguish a Topic's stable identity from the runtime instance containing its values.

No specific caching, synchronisation, transport or release-propagation mechanism is established by this DRP.

## 8. Hierarchical matching and rule discrimination

### 8.1 Classification discrimination

The classification prefix of a canonicalName may be used to select applicable rules.

Examples:

`Patient::`

`Observation::`

`Thing::`

`Constraint::`

The classification identifies the kind of architectural or information-management object to which a rule may apply.

### 8.2 Containment matching

The qualified containment path provides additional discrimination within the selected classification.

Rules may evaluate particular path segments or hierarchical scopes.

This permits matching against broader or narrower information-management structures.

### 8.3 Shared matching mechanism

The Topic naming framework may support:

- Subscriptions.
- Routing.
- Security filtering.
- Information ownership discrimination.
- Information-location discrimination.

These mechanisms may share structural matching logic without sharing policy semantics.

A successful structural match does not itself confer authorisation.

The exact rule syntax, matching operators and precedence rules remain unresolved.

## 9. InformationCoordinates and Themis

### 9.1 Complementary responsibilities

InformationCoordinates identifies information-management dimensions through named Topic references.

Themis governs security attributes, policy evaluation and authorisation.

The mechanisms are complementary.

| Construct | Responsibility |
|---|---|
| Topic | Identity and hierarchical naming |
| InformationCoordinates | Named ownership and location references |
| Security classification | Classification of protected information |
| Security compartments | Compartment-based access constraints |
| Themis | Authorisation requirements and determinations |

### 9.2 Security evaluation

An authorisation decision may consider:

- The requested action.
- The requesting principal and applicable authority.
- Information ownership.
- Information location.
- Information classification.
- Information compartments.
- Other applicable security constraints.

Topic classification and containment provide mechanisms for identifying the scope of applicable rules.

Themis remains responsible for determining whether the requested operation is authorised.

### 9.3 Architectural boundary

InformationCoordinates does not itself grant access.

Topic matching does not itself grant access.

The existence of a subscription does not imply authorisation to receive information.

Routing eligibility does not imply authorisation to disclose information.

These distinctions must remain explicit when the shared matching framework is incorporated into security processing.

### 9.4 Existing security architecture

This proposal is intended to complement Harmonia's established Themis architecture, including local Policy Enforcement Points and governed authorisation requirements.

It does not replace the established authorisation model or redefine the meaning of existing security masks.

The detailed relationship between InformationCoordinates, classification, compartments and Themis authorisation requirements requires subsequent architectural mapping.

## 10. Architectural relationships

The following are candidate relationships for reconciliation against the existing Architecture Continuum.

| Source | Relationship | Target | Interpretation |
|---|---|---|---|
| InformationUnit | Association / structural property | InformationCoordinates | InformationUnit has associated information coordinates |
| InformationCoordinates | Named reference | Topic | `informationOwner` |
| InformationCoordinates | Named reference | Topic | `informationLocation` |
| Topic | Identity attribute | canonicalId | Persistent identity |
| Topic | Naming attribute | canonicalName | Qualified distinguished name |
| Topic | Naming attribute | friendlyName | Unqualified containment name |
| Topic | Naming attribute | displayName | Presentation label |
| Themis | Uses governed information attributes | InformationUnit / associated security information | Authorisation evaluation |

These are conceptual relationships, not instructions to create specific ArchiMate relationship types.

Existing elements, Topics, InformationUnits, information-security constructs and relationships must be reconciled before ADD operations are authorised.

## 11. Established architectural intent

The following decisions have been established through Solution Architect discussion:

1. Topic provides persistent identity and structured naming.
2. canonicalId remains immutable throughout development and runtime.
3. Topic naming and containment may change during development.
4. Runtime Topic representations are immutable.
5. Topics may be passed by value for runtime processing.
6. canonicalName uses architecturally qualified naming.
7. friendlyName provides an unqualified containment-oriented name.
8. displayName provides a locally meaningful presentation label.
9. Composition establishes authoritative containment.
10. InformationCoordinates is the agreed name, replacing the provisional InformationContext.
11. InformationCoordinates contains explicitly named Topic references.
12. informationOwner and informationLocation are distinct named dimensions.
13. Topic classification and containment may support subscriptions, routing and security filtering.
14. Themis security classifications, compartments and authorisation remain distinct from Topic matching.

## 12. Outstanding architectural questions

The following matters have not yet been resolved:

**OQ-01 — InformationUnit identity**

Confirm the precise meaning and type of the conceptual `TopicID id` property, including its relationship to the InformationUnit's own identity.

**OQ-02 — InformationCoordinates structural contract**

Confirm whether InformationCoordinates is embedded within InformationUnit, independently referenced or supported through multiple representations.

**OQ-03 — Topic naming grammar**

Formalise the qualified naming grammar, including qualifier selection, escaping, validation and handling of ambiguous containment.

**OQ-04 — Capability naming migration**

Determine the governed migration approach for existing capability and feature canonicalNames.

**OQ-05 — Runtime Topic definition lifecycle**

Establish how updated Topic definitions become available across releases and running components.

**OQ-06 — Matching semantics**

Define hierarchical matching operators, precedence and evaluation boundaries for subscriptions, routing and security filtering.

**OQ-07 — Themis integration**

Reconcile InformationCoordinates with existing Themis information classifications, compartments, authorisation requirements and Policy Enforcement Points.

**OQ-08 — Existing architecture reconciliation**

Identify existing Topic, InformationUnit, identity and security elements that already express these concepts, avoiding duplicate architectural definitions.

## 13. Proposed architectural capture

Following Solution Architect review, the Architecture Continuum should be examined for existing definitions of:

- Topic and TopicID.
- InformationUnit.
- Information ownership.
- Information location.
- Distinguished-name properties.
- Themis security classification and compartments.
- Subscription, routing and filtering mechanisms.

InformationCoordinates is a candidate architectural construct to be reconciled against those existing definitions.

No new canonical identity should be allocated where an existing element already represents the agreed concept.

Any resulting ArchiMate ADD or UPDATE activity must follow the governed architecture process.

## 14. Review and acceptance

**DRP standing:** Candidate.

The architectural concepts in Section 11 reflect the discussion and agreements reached with the Solution Architect.

The proposed class structures and relationships are conceptual representations requiring reconciliation with the existing Architecture Continuum.

The outstanding questions in Section 12 remain open.

This DRP does not authorise Codex or Junie execution, architectural publication, HCRL closure or Git operations.

**Next activity:** Return to the current ArchiMate capture and review process.