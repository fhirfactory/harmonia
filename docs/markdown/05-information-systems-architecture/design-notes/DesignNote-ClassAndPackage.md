# Design Note --- Class and Package

## Status

**Candidate design note --- agreed architectural direction for later
incorporation into the governed Harmonia metamodel.**

## Purpose

This note records the emerging distinction between a logical Harmonia
information construct and its concrete software-model realisation,
together with the modular organisation of those realisations.

It is intentionally small and does not attempt to resolve the existing
legacy DataObjects currently under architectural review.

## Class

Harmonia will use **`[Class]`** in the UML sense to represent the
concrete, software-usable realisation of an architectural information
concept.

A Class is not synonymous with a Java class or any other
language-specific implementation mechanism. It represents the concrete
structural construct that may subsequently be implemented using a Java
class, record, generated type, or another appropriate software
mechanism.

For example:

    [Topic] Patient
        ↓ Realization
    [Class] Patient

In this model:

-   **`[Topic] Patient`** is the logical information construct.
-   **`[Class] Patient`** is the concrete software-model realisation of
    that Topic.
-   A source-code class or equivalent implementation construct is an
    implementation of the architectural Class, not the architectural
    Class itself.

The existence of a Class requires an architectural antecedent. A Class
must not be introduced merely because an implementation requires a
convenient software structure. Where an existing implementation-oriented
construct cannot be traced to an architectural concept, that absence is
an architectural question to be resolved rather than inferred away.

The precise ArchiMate native element and Harmonia specialisation mapping
for `[Class]` remains to be established as part of the applicable
metamodel work.

## Package

Harmonia will use **`[Package]`** to represent a modular
software/library concept.

`[Package]` is a Harmonia specialisation of the ArchiMate **Grouping**
element. It provides a standards-aligned mechanism for organising
Classes and, where appropriate, subordinate Packages into coherent
modular units.

For example:

    [Package] Clinical
        ├── [Class] Patient
        ├── [Class] Encounter
        └── [Package] Identity

Package membership is organisational. It does not establish information
semantics, semantic ancestry, or justify the existence of a Class. The
architectural traceability of a Class to the concept it realises remains
independent of its Package membership.

## Resulting Distinction

The emerging model therefore separates three concerns:

    [Topic]        logical information construct
        ↓ Realization
    [Class]        concrete software-model realisation

    [Package]      modular organisation of Classes and Packages

This distinction allows Harmonia to remain technology-neutral while
using established modelling terminology rather than introducing
implementation-specific or unnecessary Harmonia-specific concepts.

## Deferred Classification

The existing elements currently named:

-   `Context Topic`
-   `Pragma Topic`
-   `PetasosParcel Topic`
-   `Pylai.Diadromí Topic`

remain unresolved.

They appear to sit closer to Class/implementation-level concerns than to
logical Information Topics, but they are **not** to be reclassified
merely on that observation. Their architectural antecedents and
appropriate classification must be established before they are changed.