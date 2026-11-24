<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Note — Object and Class Granularity

**Status:** Draft — Architectural Review  
**Scope:** Harmonia / Calliope — object definitions, DataTypes and structural relationships  
**Purpose:** Guidance for a holistic Calliope object-model redesign; not an implementation authorisation.

## 1. Purpose and boundary

This note guides decisions about **what to model as an independently meaningful object, what to model as a dependent object, and what to model as an attribute or DataType**. Its primary applications are object definitions, the structure of associated DataTypes, and the choice and semantics of association, aggregation and composition.

It does **not** establish separate runtime-management frameworks for three categories, prescribe Java inheritance patterns, or require every modelled object to be independently persisted. Java classes and architectural object classifications are different concerns.

## 2. The three-tick assessment

Assess the conceptual object against three tests:

| Test | Question | Interpretation |
|---|---|---|
| **Identifiability** | Can an identifier be assigned so that a caller using it unambiguously locates and retrieves that *same logical object*? | The identity is managed within a defined scope; it need not be derivable from the object's descriptive attributes. |
| **Independent meaning** | Does the object have meaning without reference to another object? | It represents a meaningful thing in its own right, not merely a field whose meaning comes entirely from its parent. |
| **Independent lifecycle** | Can the object's existence and lifecycle be managed independently of another object's lifecycle? | Its creation, changes and retirement are not inherently dictated by a containing object's lifecycle. |

**Classification heuristic:**

- **Three ticks → First-class object.**
- **Two ticks → Second-class object.**
- **Zero or one tick → Attribute / DataType.**

The assessment must be applied to the **intended domain semantics**, not to accidental properties of a Java implementation. Record difficult cases and the rationale for the classification. These names express modelling independence, **not value, importance, or sophistication**.

## 3. First-class objects

A first-class object has unambiguous identity, independent meaning and an independent lifecycle. A caller resolving its identifier through the responsible management boundary obtains the same **logical object**, even if access is mediated by a cache, service or persistence mechanism.

Where the object is **Harmonia-managed**, Mneme is the governed information-access boundary. Where it is **SEP-managed**, the responsible external service/system establishes how its object is identified and retrieved. First-class status does not imply that every object belongs in Mneme or that all instances must be stored in one database.

First-class objects will often be modelled with references or associations rather than being exclusively lifecycle-owned by a parent. This is a modelling consequence to consider, **not a rule that automatically determines relationship type**.

## 4. Second-class objects

A second-class object meets two tests and is independently meaningful to some degree, but depends on another object for at least one essential aspect of identity, meaning or lifecycle.

Its independent representation must deliver **tangible benefit**, such as coherent validation and behaviour, reuse across models, an explicit semantic boundary, or a meaningful association. A second-class object **may be a fully fledged Java class**; it does not automatically require its own registry, lookup API, cache or persistence lifecycle.

**Example — `HumanName`:** Its fields and validation may warrant a reusable OO class. A name can be meaningful as a value, but a person's name occurrence normally derives its relevance and lifecycle from the Person/Patient association. A system-wide catalogue of name occurrences would not be justified merely because the data structure is reusable. Its precise tick classification depends on the intended domain, not on assigning a synthetic UUID.

A second-class object is normally reached in the context of its containing or associated object unless independent access has a clear use case. Sharing may justify association or aggregation; exclusive lifecycle ownership may justify composition.

## 5. Attributes and DataTypes

An attribute or DataType is a constituent of a larger object rather than an independently managed domain entity. **Complexity is not a promotion criterion.**

A DataType such as `InformationLocation` may have nested fields, validation rules, behaviour, constructors, useful methods and its **own OO Java class**. None of that inherently justifies an independent identity, management service or persistence strategy.

Group related data into a cohesive DataType where it improves meaning, consistency and implementation quality. Conversely, do not create a class solely because several primitive fields happen to appear together; seek a meaningful boundary and tangible benefit.

## 6. Relationships: association, aggregation and composition

Use classification as input to relationship modelling, then determine relationships from actual semantics:

- **Association:** one object refers to another without asserting exclusive ownership of its lifecycle.
- **Aggregation:** an object participates in a whole/part relationship without necessarily being exclusively lifecycle-owned by that whole.
- **Composition:** the part is lifecycle-owned by the whole, to the extent required by the chosen modelling semantics.

**Do not map classification mechanically to relationship type.** A second-class object can be shared. A complex DataType can be represented by a composed class. A first-class object can participate in a containment relationship without surrendering its identity or lifecycle autonomy.

Prefer explicit reference semantics when independently managed objects are involved. Prefer contained values where a DataType's meaning and lifecycle are owned by its parent.

## 7. Identity and management are separate questions

An identifier denotes one logical object, not merely a reproducible set of attribute values. Two callers resolving the same valid identity within its defined scope must not silently receive two different logical entities.

Whether lookup is achieved through Mneme, a SEP, a cache, dependency injection, a registry or another established mechanism is a separate architectural decision. Do not introduce management infrastructure simply to justify giving a DataType an identifier.

The classification tests guide **object-model granularity**. They do not independently decide security policy, mutability, serialisation, deployment or persistence technology.

## 8. Guidance for Calliope redesign

When reviewing a candidate class or group of fields:

1. State the concept and its meaning in the domain.
2. Apply the three tests and record the resulting classification when non-obvious.
3. Identify whether a separate Java class provides tangible modelling or engineering benefit.
4. Specify the owning or referencing relationship and relevant lifecycle semantics.
5. Introduce independent identity and lookup only where justified by the conceptual model.
6. Reconcile with the Architecture Continuum and the agreed identification and immutability Design Notes.

The existing Calliope code is **a redesign candidate, not an accepted structural baseline**. This note is guidance for Junie's holistic redesign and subsequent independent Codex review, not a requirement to preserve existing class boundaries.

## 9. Design cautions and open review points

- The three-tick scheme is a **decision aid**, not a mechanical substitute for architectural judgement. Borderline cases require a rationale.
- “Uniquely identifiable” must not be conflated with “has a UUID field” or “has a unique combination of descriptive attributes”.
- A shared, reusable value is not automatically a first-class managed entity.
- The ability to instantiate a Java class independently does not establish an independent **domain lifecycle**.
- The relationship between identity scope and discoverability should be tested against actual Harmonia-managed and SEP-managed examples during review.

**Review status:** Draft. No Calliope implementation changes or architectural acceptance are authorised by this note alone.
