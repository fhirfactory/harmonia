# Design Note — ConceptFDN and InstanceFDN

**Status:** Draft — Architectural Review  
**Scope:** Harmonia / Calliope — Common Identification Framework  
**Historical references:** Pegacorn and DRICATS identification frameworks

## 1. Purpose

Establish a common identification framework for Harmonia that supports:

- Concept definition and classification.
- Instance identification.
- Hierarchical containment.
- Component identification and functional decomposition.
- Information and message classification.
- Subscription and selection mechanisms.
- Reusable, release-controlled terminology definitions.

The framework shall preserve flexibility without requiring separate identification mechanisms for each Harmonia subsystem. It recognises both hierarchical instance identities and UUID-based identities, selected according to the semantic requirements of the object.

## 2. Architectural Foundation

Harmonia uses `Topic` as its general-purpose hierarchical naming construct.

```java
class Topic {
    Map<Integer, TopicEntry> entryList;
}

class TopicEntry {
    String qualifier;
    String version;
    String value;
}
```

Each Topic comprises an ordered hierarchy of qualified values.

The qualifier identifies the meaning of an entry, the value supplies its content, and the version permits individual hierarchical elements to be version-qualified.

The underlying Topic structure is general-purpose.

Two semantic specialisations are established:

- `ConceptFDN` — Fully Distinguished Name of a concept.
- `InstanceFDN` — Fully Distinguished Name of an instance.

Both retain the hierarchical capabilities of Topic.

## 3. ConceptFDN

`ConceptFDN` represents a defined concept, classification or functional hierarchy.

```java
class ConceptFDN extends Topic {
}
```

ConceptFDNs may represent:

- Software component types.
- Software component roles.
- Functional decomposition.
- Information classifications.
- Content and container formats.
- Message and parcel classifications.
- Processing and task types.
- Externally defined terminology.

### 3.1 Example — Software Component

```text
harmonia.mneme.summarycache.agent.agentlistener
```

This describes the component within Harmonia's functional decomposition, independently of its deployed instances.

### 3.2 Example — IANA Media Type

A specific MIME type may be represented as:

| Entry | Qualifier | Value |
|---|---|---|
| 0 | IANA Media Types | application |
| 1 | subtypes | fhir+xml |

This represents `application/fhir+xml`.

A ConceptFDN containing only the first entry represents the broader `application` category, not the specific media type.

The hierarchy supports classification-based matching without introducing arbitrary containment relationships beyond those established by the defining terminology.

The original external terminology representation must remain recoverable.

### 3.3 Qualifiers

`ConceptFDN` permits arbitrary qualifier strings.

It does **not** require every qualifier or value to be registered in a terminology repository.

However, Harmonia shall provide reusable value-sets for commonly used and architecturally defined concepts.

This provides standardisation without unnecessarily constraining extensibility.

## 4. InstanceFDN

`InstanceFDN` identifies a particular instance within a hierarchical containment structure.

```java
class InstanceFDN extends Topic {

    public String getId() {
        return getLeastSignificantEntry().getValue();
    }
}
```

The terminal TopicEntry identifies the terminal instance.

The complete hierarchy establishes its fully distinguished identity.

Consequently, identical terminal identifiers may occur in different containment hierarchies without representing the same instance.

### 4.1 Example — Software Component

```text
solution-0
    .site-a
    .platform-azure
    .container-z
    .component-thing
    .sub-component
```

The complete hierarchy identifies the deployed component instance.

Each level represents a qualified containment element.

### 4.2 Instance Lifecycle

The containment hierarchy participates in instance identity.

A component instantiated at a different deployment location is a different component instance, rather than an existing component whose location has changed.

The precise lifecycle rules for restart, replacement and reincarnation remain subject to further design.

### 4.3 Identity Semantics

`getId()` provides the terminal instance identifier.

It does not replace the complete InstanceFDN as the authoritative fully distinguished identity.

An InstanceFDN must contain a valid terminal instance entry.

## 5. Relationship Between ConceptFDN and InstanceFDN

ConceptFDN and InstanceFDN express different dimensions.

**ConceptFDN:** What something is.

**InstanceFDN:** Which particular thing it is, within its containment hierarchy.

For software components, conceptual decomposition and deployment containment need not follow the same hierarchy.

A component may therefore have both identifiers.

Neither identifier should automatically be derived from the other.

### 5.1 Cross-Conversion

Construction of a ConceptFDN from an InstanceFDN, or vice versa, is deferred.

Although these conversions may be useful, they can require additional information that is not present in the source hierarchy.

Cross-conversion constructors shall not be introduced until their semantics have been established.

### 5.2 Hierarchical and UUID-Based Identity

`InstanceFDN` is appropriate when an instance is identified by its qualified containment hierarchy, such as a deployed software component.

Some system-wide objects instead require an identity that is independent of their classification, containment, current location and processing state. Such objects may use a `ConceptFDN` for their classification and an independently allocated UUID for instance identity.

These are two legitimate identification strategies, not a general rule with exceptions. A `ConceptFDN` is not part of the UUID's uniqueness guarantee, and changing a classification shall not silently change the object's identity.

A UUID is an opaque identity value. Representing it as a Java `String` does not imply that arbitrary strings are valid UUIDs; construction and validation rules shall preserve the required UUID semantics.

## 6. Value-Set Framework

Harmonia will provide predefined concept definitions through value-set enums.

Each applicable enum shall provide:

```java
ConceptFDN toConceptFDN();
```

For example:

```java
public enum MediaTypeConcept {
    APPLICATION_FHIR_XML,
    APPLICATION_JSON,
    APPLICATION_XML;

    public ConceptFDN toConceptFDN() {
        // Construct the corresponding ConceptFDN.
    }
}
```

Value-set definitions are controlled as part of the applicable software release.

Value-set conversion provides correctly structured qualifiers, values and applicable versions.

The returned ConceptFDN shall be an independent object unless the underlying Topic implementation provides suitable immutability guarantees.

Value-sets do not prohibit custom ConceptFDNs.

A universal interface for all value-set enums is not required at this stage.

## 7. Application Across Harmonia

The following are proposed applications of the common framework.

### 7.1 SoftwareComponent

```java
ConceptFDN componentType;
ConceptFDN componentRole;
InstanceFDN componentInstance;
```

The component type, component role and component instance represent distinct concerns.

### 7.2 InformationUnit

An InformationUnit has an independently assigned, immutable UUID
(`InformationUnit.id`) as its authoritative and durable instance identity. The UUID
is independent of classification, information representation, ownership, location,
lifecycle and version. An InformationUnit neither has nor requires an `InstanceFDN`.

`InformationUnit.informationProfile` is the `ConceptFDN` that identifies the
IU's conceptual classification. Its value,
`solution=harmonia.domain=internal.concept=InformationUnit`, is intrinsic to the
InformationUnit concept and immutable for the lifetime of the IU. The model retains
this fixed value to preserve explicit conceptual identity and provenance
independently of Java class names, package hierarchies, Maven modules,
implementation language and software refactoring or restructuring. A constructor
default could supply the fixed value. The initialisation mechanism remains a
detailed design choice.

Each IU has exactly one governed `InformationRepresentation`. Its
`ContainerMetadata` describes the envelope, its `ContentMetadata` describes the
content structure and technical characteristics, and its `Content` carries the
represented information. Changes to that information leave `informationProfile`
unchanged and follow the accepted IU revision and lifecycle rules. A transformation
that produces a differently structured or formatted governed representation
creates a new IU with provenance.
[IU lifecycle](../../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.002.md#43-information-lifecycle-is-not-an-operational-handshake) ·
[IU representation](../../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.002.md#5-informationrepresentation-containermetadata-contentmetadata-and-content)

Where an existing contract requires an IU Topic representation, a deterministic
projection may be derived from `InformationUnit.informationProfile + InformationUnit.id`.
The immutable inputs keep the projection stable. The ConceptFDN supplies the
conceptual classification, and the UUID remains authoritative for the instance.
The projection introduces no additional IU identity or `InstanceFDN`.

The historical `harmonia.mneme.iu.<hex-uuid>` convention concerns the IU Topic
designation. The accepted Architecture Continuum 1.1.26 specifies that
designation as `harmonia.mneme.iu.<hex-string-of-compressed-uuid>` and gives the
IU Topic a distinct Topic UUID. F-01.78 preserves the designation as the
unqualified projection in the Topic redesign. The IU Topic contract and its
boundary mappings remain subject to the outstanding G02 reconciliation.
[Architecture Topic](../../enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content) ·
[InformationUnit Class](../../enterprise-architecture/continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content) ·
[F-01.78](../../enterprise-architecture/design/evidence/software-0.1.0/004/before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md#f-0178--r3-information-unit-topic-designation--accepted)

### 7.3 PetasosParcel

```java
ConceptFDN parcelType;
String uuid;
```

`parcelType` classifies the parcel; the UUID identifies that particular parcel independently of its classification, routing, containment or processing state. The field name `parcelType` is illustrative and may be refined when reconciled with the existing parcel model.

A parcel's UUID is distinct from the identity of the InformationUnit it carries or references, and from the identity of any associated Pragma. Retry and replacement identity semantics require explicit lifecycle design; they shall not be inferred from the common naming framework.

### 7.4 Pragma

```java
ConceptFDN pragmaType;
InstanceFDN pragmaInstance;
```

The Pragma type describes the classification of work; the instance identity distinguishes an individual Pragma.

### 7.5 InformationRepresentation and InformationCoordinates

Existing information models shall be reviewed to establish where ConceptFDN and InstanceFDN provide appropriate semantics.

This includes:

- ContainerMetadata.
- ContentMetadata.
- Logical information location.
- Component and endpoint identification.
- Other currently String- or Topic-based classifications.

These examples are proposed mappings, not authorisation to replace existing attributes without architectural review.

## 8. Subscription and Selection

Topic-based matching remains fundamental to the identification framework.

Concept classifications can be selected using `TopicMask`.

Instance selection may require matching individual instance identifiers or hierarchical containment paths. UUID-identified objects can be selected by their UUID, while hierarchical instance identities may require `InstanceFDN`-aware matching. These selection mechanisms must not be conflated.

A conceptual subscription pattern is:

```java
class PetasosParcelSubscriptionMask {
    TopicMask topicMask;
    InstanceMask instanceMask;
}
```

This is an illustrative subscription pattern, not an approved universal contract. The exact relationship between `TopicMask`, `InstanceMask`, `ConceptFDN`, `InstanceFDN` and UUID-based instance selection remains unresolved.

A domain-specific subscription may also include additional criteria concerning content, container, security, lifecycle or operational status.

The common identification framework shall not prescribe all such domain-specific matching semantics.

## 9. Historical Architectural Context

Pegacorn and DRICATS implemented related mechanisms through:

- RDN and FDN naming structures.
- Functional and topology FDNs.
- Type and instance identifiers.
- DataParcelTypeDescriptor.
- Work-item subscription and manifest structures.
- Hierarchical and multidimensional subscription matching.

These implementations demonstrate the value of separating conceptual classification, instance identity and containment.

They also illustrate the complexity that can arise when information distribution, processing coordination, availability, replication and reconciliation become tightly coupled.

Harmonia retains the useful identification concepts without adopting the historical distribution architecture.

Mneme provides the common governed information-access framework, while processing and work coordination retain their distinct architectural responsibilities.

The historical Java implementations are reference material, not implementation specifications.

## 10. Deferred Decisions

The following require further examination:

1. Exact construction and validation contracts for ConceptFDN and InstanceFDN.
2. Cross-conversion between conceptual and instance hierarchies.
3. Instance hierarchy matching and InstanceMask semantics.
4. Identifier equality, per-entry version participation and canonical serialisation, including UUID format and validation.
5. Instance lifecycle semantics, particularly component restart/replacement and parcel retry.
6. Alignment with existing TopicCoordinates and InformationCoordinates.
7. Migration of existing Calliope attributes and terminology definitions.

## 11. Design Principles

The common identification framework shall:

- Reuse Topic as its underlying hierarchical mechanism.
- Distinguish conceptual classification from instance identity.
- Use hierarchical `InstanceFDN` identity where containment is intrinsic to identity; use UUID identity where the object must remain independent of containment and location.
- Preserve the InformationUnit's immutable `informationProfile` as explicit
  conceptual classification and provenance. Keep representation characteristics
  with `InformationRepresentation`.
- Keep UUIDs and their deterministic naming projections from becoming competing identities.
- Preserve qualified containment information.
- Permit both controlled value-sets and custom terminology.
- Avoid duplicating identity information unnecessarily.
- Separate identity semantics from serialisation.
- Support reusable matching mechanisms without imposing one subscription model on every domain.
- Avoid recreating the distribution complexity of Pegacorn and DRICATS.
- Prefer established architectural semantics over implementation convenience.

**Implementation remains subject to architectural review and acceptance.**