<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Note — Object Immutability and Modification Guidelines

**Status:** Draft — Architectural Review  
**Scope:** Harmonia / Calliope — Object model and lifecycle design  
**Related concerns:** InformationUnit governance; Themis authorisation; ConceptFDN and InstanceFDN

## 1. Purpose

Establish pragmatic guidelines for object identity, state and permissible modification within Calliope and Harmonia. The intent is **not** to make all Java objects immutable, or to introduce a universal security framework into Calliope. It is to give implementers a consistent way to reason about object changes, particularly for the few core objects whose state requires explicit governance.

Harmonia includes integration, workflow, Digital Twin and collaboration behaviours. These require stateful objects and evolving information. Immutability is therefore applied **case by case**, according to identity, lifecycle, governance and the consequences of modification.

## 2. Foundational distinctions

### 2.1 Identity

**An established object identity is immutable.** Changing identity means creating a new object with a new identity; it is not an in-place modification of the existing object's identity.

Identity may be represented differently according to the object:

- A containment-based component instance may use an `InstanceFDN` whose complete hierarchy constitutes its identity.
- An `InformationUnit` or `PetasosParcel` may use a stable UUID independent of its location, classification or representation.

The terminal `InstanceFDN.getId()` is a convenience accessor, **not** a substitute for the complete hierarchical identity.

### 2.2 Coordinates and location

Coordinates describe location or containment, and may be identity-bearing or non-identity-bearing depending on the object. **Do not assume all coordinates are immutable or that all changes of location imply new identity.**

For a deployed software-component instance whose `InstanceFDN` includes its deployment containment hierarchy, spinning up a component in a different hierarchy creates a **new instance**. For a durable InformationUnit, changing its logical location or owner must not silently change its UUID identity. Its coordinate-change rules remain subject to the applicable governance and lifecycle contracts.

### 2.3 Object state and payload

An object's state is determined, in part, by its data. Modifying or replacing an attribute may therefore change the state or meaning of the enclosing object. The effect is not necessarily limited to the field being changed.

Committed or authoritative information may require preservation of prior versions, whereas working data and operational state may legitimately evolve. Java implementation immutability (`final` fields/classes, defensive copies, immutable collections) is a **technique**, not the definition of domain immutability.

## 3. Common CBX vocabulary

The following terms describe kinds of operations to consider when defining an object or attribute's modification contract:

| Code | Term | Intended operation |
|---|---|---|
| **CBV** | Can Be Viewed | Read or inspect a value or representation. |
| **CBA** | Can Be Added | Add an element, association or previously absent value. |
| **CBM** | Can Be Modified | Change an existing value or state in place. |
| **CBR** | Can Be Replaced | Replace an existing value or contained object as a whole, while respecting the owning object's identity contract. |
| **CBD** | Can Be Deleted | Remove an element, association or value, where permitted by lifecycle and preservation rules. |

These are **design and policy vocabulary**, not unconditional permissions, Java setter requirements, or five mandatory Boolean fields on every class.

The meanings of *modify* and *replace* must be interpreted in relation to the object boundary. Replacing a contained context is not necessarily equivalent to modifying one of its fields; both may have consequences for the state of the containing object.

## 4. How to apply CBX

For an object or attribute that warrants explicit treatment, consider:

1. **Identity boundary:** What establishes the object's identity, and when is that identity fixed?
2. **Lifecycle:** During which phases may data be added, modified, replaced, viewed or deleted?
3. **State invariants:** What must remain true before and after an operation?
4. **Governed preservation:** Must the prior state remain available through a version, historical record or new InformationUnit?
5. **Authority boundary:** Which component manages the lifecycle and enforces the operation contract?

A CBX designation may vary by lifecycle state. Construction-time population is not automatically equivalent to post-commit modification.

**Prefer ordinary object contracts over elaborate metadata.** Most Calliope classes should not require a formal CBX facet. Fine-grained, attribute-level CBX description should be exceptional and reserved for a small set of foundational governed objects where its value justifies the complexity.

## 5. Illustrative application — InformationUnit

The examples below are **questions to resolve during model redesign**, not settled permissions for every field.

| InformationUnit concern | Guideline |
|---|---|
| UUID | Identity immutable once established. Changing it creates a new InformationUnit. |
| `informationProfile` (`ConceptFDN`) | Distinguish classification changes from identity changes; specify classification lifecycle rather than assuming an unrestricted setter. |
| `InformationCoordinates` | Changes must respect logical ownership/location semantics and may affect governance; UUID identity remains stable. |
| `InformationRepresentation` | Distinguish assembly of working information from modification of governed/committed representation. Preserve prior authoritative information as required by its versioning rules. |
| `SecurityContext` | A change affects the enclosing object's governance state, not merely a replaceable field. |
| `LifecycleContext` | Changes are lifecycle transitions governed by defined state invariants. |
| `ManagementContext` | Operational characteristics may evolve; distinguish transient management state from durable governed information. |

An InformationUnit has its own UUID-based identity. Its canonical Topic name remains deterministically related to that UUID; content or payload versioning does not redefine the identity. The payload's characteristics are described through `InformationRepresentation`, including `ContainerMetadata` and `ContentMetadata`, rather than being conflated with `informationProfile`.

## 6. Relationship to Themis

The CBX vocabulary provides a **shared foundation** for two distinct activities:

- **Calliope / object design:** What changes are structurally meaningful and lifecycle-valid for this object or attribute?
- **Themis / authorisation:** Who or what may perform a supported operation, in what context and under which security constraints?

**Security restrictions take precedence over an otherwise supported mutable operation.** Conversely, authorisation cannot make a structurally or lifecycle-prohibited operation valid—for example, changing an established identity in place.

The effective operation must satisfy **both** the object's invariants/lifecycle contract **and** applicable authorisation. This Design Note does not design Themis's decision engine, masks, policies or enforcement architecture.

### 6.1 Possible CBX facets

For the limited number of core objects needing fine-grained control, object/class facets may eventually describe CBX characteristics per attribute. The placement and form of such facets—within Calliope's model, Themis, or their shared contract—remain to be decided.

Do **not** introduce a mandatory facet, interface, annotation or security-policy dependency on every Calliope object.

## 7. Guidance for Java implementation

- Choose mutable, immutable or staged-construction forms according to the object's actual lifecycle; do not default to making every class `final`.
- Protect identity-bearing fields and invariant-bearing structures against unintended mutation after identity establishment.
- Avoid exposing mutable internals where doing so bypasses the object's lifecycle contract.
- Separate identity changes (new object) from legitimate state transitions (same identity).
- Do not disguise destructive replacement of committed information as an ordinary object update.
- Treat version creation, provenance and retention as governed information-management concerns rather than accidental consequences of Java cloning.
- Use CBX terminology in design and review where it clarifies permitted operations; do not generate boilerplate merely to represent it.

## 8. Decisions and boundaries

**Established design direction**

- Object identity is immutable once established.
- Containment-based identity and UUID-based identity are both legitimate, depending on the object.
- There is **no blanket Calliope immutability mandate**.
- CBV, CBA, CBM, CBR and CBD are the common descriptive vocabulary for object operations.
- Structural/lifecycle validity and security authorisation are distinct requirements; both must be satisfied.
- Fine-grained CBX facets, if introduced, apply to **very few** core objects, not all classes.

**Open for later design**

- Exact CBX rules for individual InformationUnit attributes and other candidate foundational objects.
- Whether any CBX facets belong to Calliope, Themis, or a shared contract.
- Lifecycle-specific variation and validation of CBX characteristics.
- Detailed definitions of replacement/deletion for particular object boundaries.
- Concrete enforcement patterns for mutable runtime state versus preserved authoritative versions.

## 9. Intended use in Calliope redesign

Junie should use this Design Note alongside the Architecture Continuum, the ConceptFDN/InstanceFDN Design Note, and the independent Codex reviews when reconsidering the Calliope model **holistically**. Existing implementation choices are not presumed accepted. The resulting candidate model should demonstrate sensible object granularity, lifecycle ownership, preservation of identity, and proportionate constraints, with remaining ambiguities surfaced for architectural adjudication.

**This is a draft design guideline, not authorisation to implement a universal CBX framework or an amendment to Themis's accepted security architecture.**
