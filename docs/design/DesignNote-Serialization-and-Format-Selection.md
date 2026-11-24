<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Note — Serialization and Format Selection

**Status:** Draft — Architectural Review  
**Scope:** Harmonia / Calliope — Object-model serialization and interchange  
**Related requirement:** NFR-099 — Prefer XML for structured information encapsulation where practical; allow JSON for UI interactions and other formats required by interoperability/contracts.

## 1. Purpose

Establish a consistent approach to selecting serialization formats without allowing those formats to dictate the structure or responsibilities of Calliope's object model.

Serialization is necessary for information interchange, persistence, integration, configuration and publication. **The choice of serialization format is a separate design decision from the definition of the information being serialized.**

This note expresses a *preference*, not a universal prohibition or an obligation to serialize every object.

## 2. Format-selection preference

For Harmonia-controlled structured information, the default preference is:

**XML → JSON → YAML**

This is an order of preference **when multiple formats are practical and otherwise suitable**, not a requirement to convert externally mandated formats.

- **XML:** Preferred for structured information, especially large, complex, multi-page documents and governed artefacts. Explicit opening and closing elements assist human inspection, navigation and comprehension. This is a deliberate engineering preference, including a subjective readability judgement.
- **JSON:** Appropriate for APIs, user interfaces, lightweight interchange and integration contracts where JSON is more natural, efficient or required. Its use is fully supported, not regarded as a deficiency.
- **YAML:** Permitted where existing tooling, platforms, ecosystems or contracts use it. Do not introduce YAML as a new authoritative Harmonia-owned format merely out of habit; do not build conversion machinery solely to avoid consuming or producing required YAML.

Other formats are also permitted where required by an external standard, protocol, content type or implementation need. There is no requirement that every data exchange must be XML, JSON or YAML.

## 3. Rationale and human readability

Large, multi-page representations must remain understandable to engineers and reviewers. XML's explicit structure, including matching start and end tags, can make deeply nested content easier to inspect and reason about than indentation-dependent alternatives.

This preference is **not** a universal claim that XML is always more readable, smaller or faster. It does not override interoperability, performance or operational requirements.

> We spent twenty years escaping Makefiles and their tab-induced hell, only to invent an entirely new framework called YAML, where whitespace is once again part of the programming model. When will the pain end?

The observation captures a historical maintainability concern; it is **not** a technical prohibition against YAML.

## 4. Object-model independence

**Calliope shall define its domain objects in terms of domain semantics, relationships, constraints and lifecycle—not the syntax of a particular serialization format.**

Where an object must be serialized, its definition should permit appropriate XML and JSON representations and, when needed, YAML or other contract-specific representations, without creating alternative domain classes solely for those formats.

Consequences:

1. Domain attributes and relationships are not to be invented or distorted merely to satisfy a particular mapper's conventions.
2. The same logical object may have distinct wire representations, so long as all *required* semantics are preserved.
3. Format-specific concerns—element/attribute layout, JSON property names, namespaces, escaping, schema binding and encoding—belong principally at serialization boundaries.
4. The presence of nested or complex OO DataTypes is not itself a reason to choose one format over another.
5. A serializer's ability to round-trip an object does not by itself establish that the object has a sound domain model.

Format independence does **not** imply that all representations are byte-equivalent, or that every format can express every feature in exactly the same way.

## 5. Implementation guidance

Prefer explicit serialization adapters, codecs or mapping layers at component and interchange boundaries. Framework annotations may be used pragmatically when they do not compromise the domain model, couple core semantics to a transport technology, or make unrelated serialization concerns inseparable.

For each material interchange contract, identify as applicable:

- The intended audience or consumer, controlling specification and chosen format.
- The canonical source of meaning for names, identifiers, versioning and relationships.
- Required representation fidelity, including absent versus null values, ordering where meaningful, number/date precision, content encoding, namespaces and type discrimination.
- Validation, error handling and handling of unknown or extension fields.
- Whether the serialized form is a transient transport envelope, an authoritative persisted representation, or a generated publication artefact.

Use a format-neutral domain object where appropriate. Do not introduce a general-purpose serialization framework simply to avoid using an existing, suitable library or integration-specific adapter.

## 6. Format choice in specific contexts

| Context | Guidance |
|---|---|
| Governed, human-reviewed structured artefacts | Prefer XML when practical and appropriately supported. |
| Large, deeply nested information representations | Prefer XML where readability and explicit structural boundaries provide value. |
| Browser-facing and SPA interactions | JSON is appropriate, and may be the natural or contractual format. |
| External interoperability APIs and standards | Honour the mandated or negotiated representation; do not force XML. |
| Platform deployment/configuration (e.g. YAML-native tools) | Use the platform's supported representation; YAML is acceptable. |
| Existing configuration or integration artefacts | Retain the established format unless there is an evidenced reason to change it. |

This table expresses selection guidance, not blanket architectural mandates.

## 7. Relationship to information governance

A serialization choice must not silently change the identity, meaning, security classification, provenance or lifecycle of the represented information.

In particular, the InformationUnit identity and governed information semantics remain distinct from the serialization of its `InformationRepresentation`, including `ContainerMetadata` and `ContentMetadata`.

Transformations that create a new governed information representation must follow the applicable InformationUnit and provenance contracts. Merely encoding or decoding the *same* logical object at an interchange boundary is not automatically a new InformationUnit; the governing lifecycle and transformation semantics determine that distinction.

Security and authorization requirements apply independently of the chosen serialization format.

## 8. Implementation and review expectations for Calliope

Junie should assess existing Calliope classes for incidental JSON/Jackson coupling, stringification fallbacks, ungoverned object conversion and accidental loss of semantics. Findings should be resolved where material to the domain design or declared interchange contracts.

The redesign should demonstrate representative serialization behaviour with focused tests where needed—especially round-trip fidelity, validation failures, complex nested values and identity preservation. **This note does not require implementing XML and JSON codecs for every class regardless of use.**

The governing Architecture Continuum and accepted contracts remain authoritative; this Design Note provides implementation-oriented guidance pending architectural review and acceptance.

## 9. Decisions and qualifications

**Agreed design direction**

- Serialization is necessary; format selection is contextual.
- Prefer XML over JSON over YAML **where there is genuine freedom of choice**.
- XML readability for large structured artefacts is an explicit project preference.
- YAML is permitted when called for by existing tools or contracts.
- Calliope objects must not be structurally defined by a specific serialization framework.
- Reuse suitable libraries and boundary adapters rather than building unnecessary infrastructure.

**Further specification when implementing particular contracts**

- Exact XML/JSON/YAML schemas and field mappings.
- Which objects require serialization and which are strictly internal.
- Compatibility, evolution and required semantic round-trip properties per interface.

**Status:** Draft — subject to architectural review; not independent authorization for software changes.
