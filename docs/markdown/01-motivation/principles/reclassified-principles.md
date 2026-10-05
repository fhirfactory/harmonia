# Historical Principle Reclassifications

## Overview

As Harmonia's enterprise architecture matured from initial drafting to a rigorous TOGAF/ArchiMate-aligned taxonomy, foundational principles were scrutinized to ensure strict separation between enterprise motivation (why the platform exists and what foundational invariants govern it) and component design patterns (how application software and developer interfaces are structured).

During this canonical consolidation, one historical principle—**AX-12**—was identified as a component design principle rather than an enterprise motivation axiom. This document formally records the historical record, rationale, and target architectural allocation for AX-12.

---

## Historical Record: AX-12

### Historical Identifier & Title
- **Identifier**: `AX-12`
- **Original Working Titles**: *"Hide Plumbing, Not Information"* / *"Domain Model Primacy over Plumbing"*

### Historical Principle Statement
> *Developers implementing Harmonia work units shall have direct and fluent access to applicable standards-defined health-information models and Harmonia's managed-information capabilities. Framework plumbing shall provide security, governance, authority, persistence, concurrency, provenance, messaging, and operational services without unnecessarily obscuring or replacing the underlying information models.*

### Historical Rationale & Context
In early versions of the architecture (documented in `docs/architectural-axioms.md`), AX-12 was formulated to protect developers writing Ergon activities from being forced to navigate deep infrastructure plumbing (e.g., managing raw JMS sessions, Hibernate entity managers, or low-level Infinispan cache calls). It mandated that developers should interact directly with fluent standards models (e.g., FHIR `Practitioner`, `Observation`) while framework plumbing invisibly provided security, transaction coordination, and audit logging.

---

## Architectural Rationale for Reclassification

While the sentiment of AX-12 remains valid engineering practice, its classification under Domain 01 Motivation was conceptually inconsistent:

1. **Category Distinction**: Domain 01 Motivation defines the fundamental enterprise drivers, risks, target outcomes, and platform-wide invariants that govern the HIE. It is concerned with clinical safety, durable persistence, information authority, and regulatory compliance.
2. **Component Design Scope**: AX-12 is an internal application design guideline addressing developer ergonomics, SDK design, and class library abstractions. It governs how application-tier components (`energeia`, `calliope`, `iris`) wrap infrastructure plumbing for internal developers.
3. **Target Domain**: Principles governing internal component structure, developer abstractions, and software library packaging belong squarely in **Domain 05 Application Architecture**.

### Reclassification Decision
- **Status in Domain 01**: Formally **Reclassified and Transferred**.
- **Target Allocation**: **Domain 05 (Application Architecture)**.
- **Current Role**: AX-12 is **NOT** a current Domain 01 foundational axiom.

---

## Retention of Historical Identifier (AX-12)

A common pitfall in documentation refactoring is renumbering existing items to eliminate numerical gaps (e.g., renumbering AX-13 as AX-12). In Harmonia, this practice is strictly prohibited:

- **Historical Traceability**: Numerous source code comments, ArchUnit test annotations, design contracts, and Architecture Decision Records (ADRs) cite `AX-13`, `AX-14`, `AX-15`, and `AX-16`.
- **Avoiding Destabilization**: Renumbering the subsequent axioms would invalidate existing cross-references, break Git commit archaeology, and create confusion across engineering teams.
- **Intentional Sequence Gap**: The sequence `AX-01 .. AX-11`, followed by `AX-13 .. AX-16`, contains an intentional gap. The identifier `AX-12` is permanently retained in this reclassification registry to explain the gap and provide unambiguous historical continuity.
