# DesignNote — Mnemosyne Persistence Management and Authoritative Persistence Oversight

**Status:** Architectural Design Note  
**Scope:** Harmonia Information Systems Layer Architecture / Domain05 Candidate Architecture Step A

## 1. Purpose

This DesignNote records the current architectural position for **Mnemosyne**, Harmonia's **Persistence Manager**.

Mnemosyne provides Harmonia-level oversight and management of authoritative persistence activity and persistent services. It is not merely a database adapter beneath Mneme and does not acquire semantic ownership of information merely because it persists that information.

## 2. Fundamental Responsibility

> **Mnemosyne is Harmonia's Persistence Manager.**

Its principal responsibilities include:

- transaction **oversight**;
- persistent-service **oversight** and reporting;
- shard management;
- authoritative persistence reporting;
- persistence logging;
- validation and reapplication of applicable information **compartments** at the persistence boundary; and
- local policy enforcement through a Themis-owned PEP.

```text
Mneme
  Governed Information Management
             |
             v
Mnemosyne
  Persistence Manager
  - Transaction Oversight
  - Persistent-Service Oversight
  - Shard Management
  - Compartment Validation/Reapplication
  - Themis PEP
  - Persistence Logging
  - Authoritative Persist Reporting
             |
             v
Persistent Services / Engines
```

## 3. Mneme / Mnemosyne Boundary

> **Mneme manages governed information; Mnemosyne manages and authoritatively reports its persistence.**

Mneme owns information-management semantics and determines the governed information operation required.

Mnemosyne owns persistence-management responsibility and establishes the authoritative persistence outcome.

Persistence responsibility does not confer semantic ownership of persisted information.

## 4. Transaction Oversight

The word **oversight** is deliberate.

Mnemosyne does not require heterogeneous persistent services to expose a single common physical transaction mechanism. Underlying services may retain their own transaction semantics.

Mnemosyne provides Harmonia-level oversight of the persistence transaction/activity and its outcome.

```text
                 Mnemosyne
          Transaction Oversight
                  |
          +-------+-------+
          v               v
 Persistent Service A   Persistent Service B
 own transaction        own transaction
 mechanics              mechanics
```

Detailed transaction boundaries, rollback semantics and distributed-transaction mechanisms remain unresolved.

## 5. Persistent-Service Oversight

Mnemosyne oversees persistent services sufficiently to determine whether they can fulfil Harmonia persistence responsibilities and to report persistence outcomes.

This is distinct from infrastructure management:

```text
IT / Operations
    Is the database/storage infrastructure healthy?

Mnemosyne
    Can the persistent service fulfil Harmonia's
    required persistence responsibility, and what
    was the persistence outcome?

Energeia
    Is Harmonia operating correctly?
```

Mnemosyne does not become the infrastructure-management component for PostgreSQL, HAPI FHIR, object storage, Kubernetes or storage infrastructure.

## 6. Authoritative Persistence Reporting

An underlying persistence-engine response is not automatically Harmonia's authoritative statement that persistence has occurred.

```text
Mneme
   | "Persist X"
   v
Mnemosyne
   +-- transaction oversight
   +-- service oversight
   +-- shard management
   +-- compartment validation/reapplication
   +-- Themis PEP enforcement
   +-- persistence invocation
   +-- outcome observation
   +-- persistence logging
   `-- authoritative persistence reporting
              |
              v
       Authoritative Outcome
```

Mnemosyne establishes and reports the authoritative Harmonia persistence outcome. The detailed outcome/status model remains unresolved.

## 7. Shard Management

Mnemosyne owns **shard management** within architectural constraints.

It does not independently decide that an information domain is semantically safe to shard.

Current semantic persistence responsibility domains remain:

```text
Mnemosyne
|
+-- Clinical Information
+-- Harmonia Configuration & Governance
`-- Harmonia Operations & Assurance
```

**Harmonia Configuration & Governance (C&G)** SHALL remain a single coherent governed information space and SHALL NOT be partitioned in a manner that creates independent or divergent governance authority.

**Harmonia Operations & Assurance (O&A)** SHALL permit horizontal partitioning without changing semantic identity, provenance, relationships or governed meaning.

These constraints do not select shard keys, topology or technology.

## 8. Compartment Validation and Reapplication ("Tagging")

For Mnemosyne, **tagging** means validation and reapplication of applicable information **compartments** at the persistence boundary.

Persistence SHALL NOT accidentally strip, weaken or lose the applicable compartment classification.

```text
Managed Information
      |
      | applicable compartments
      v
Mnemosyne
      +-- validate compartments
      +-- apply persistence representation
      `-- reapply/tag compartments
              |
              v
Persistent Representation
      +-- applicable compartment A
      `-- applicable compartment B
```

Mnemosyne does **not** independently determine which compartments semantically ought to apply.

```text
"What compartments SHOULD apply?"
        != Mnemosyne responsibility

"Are the applicable compartments valid and
 correctly represented/reapplied on persistence?"
        = Mnemosyne responsibility
```

The source of semantic compartment applicability and detailed compartment representation/validation mechanisms remain unresolved.

## 9. Themis PEP

Mnemosyne performs governed persistence operations and therefore hosts a **Themis-owned Policy Enforcement Point**.

```text
Proposed Persistence Operation
            |
            v
       Mnemosyne
            |
            v
     Themis-owned PEP
            |
            | PEP-Context Criteria
            v
      Local Decision
            |
            v
     Persistence Operation
```

Themis owns the Authorisation Model, PDP reasoning, PEP-Context Criteria and PEP enforcement semantics.

Mnemosyne owns the persistence operation and is the location capable of preventing it.

Mnemosyne SHALL NOT independently reinterpret the Authorisation Model in place of the Themis PEP.

## 10. Persistence Logging and Kleio

Mnemosyne is responsible for persistence-management logging necessary for its operational and authoritative persistence responsibilities.

This is distinct from Kleio's authoritative Audit Information Management.

Where a persistence occurrence is auditable, it may also be reported through Kleio Audit Capture.

Persistence logging does not replace AuditEvent creation, and Kleio audit responsibility does not replace Mnemosyne persistence logging.

## 11. Relationship to Energeia

Mnemosyne owns persistence operational state and authoritative persistence outcomes.

Energeia may observe/report that state as part of Harmonia-level operational observation:

```text
Mnemosyne
   persistence operational state
              |
              v
Energeia
   Observe -> Report
```

This does not transfer persistence-management responsibility to Energeia.

## 12. Persistence Responsibility Domains

Clinical Information, Harmonia Configuration & Governance, and Harmonia Operations & Assurance are **semantic persistence responsibility domains**.

They SHALL NOT automatically be interpreted as three databases, schemas, services, workloads or physical deployments.

Physical topology remains downstream design.

## 13. Architectural Guardrails

1. **Mnemosyne is Harmonia's Persistence Manager.**
2. Mnemosyne is not merely a database adapter or thin persistence API.
3. Mneme owns governed information-management responsibility; Mnemosyne owns persistence-management responsibility.
4. Persistence responsibility does not confer semantic ownership of persisted information.
5. Mnemosyne provides transaction oversight without requiring one physical transaction mechanism.
6. Mnemosyne provides persistent-service oversight without becoming infrastructure management.
7. Mnemosyne owns shard management within architectural semantic constraints.
8. Mnemosyne does not independently decide that an information domain is semantically safe to shard.
9. Mnemosyne validates and reapplies applicable information compartments at the persistence boundary.
10. Persistence SHALL NOT strip or weaken applicable compartment classification.
11. Mnemosyne does not independently determine which compartments semantically ought to apply.
12. Mnemosyne hosts Themis-owned PEP capability for governed persistence operations.
13. Themis owns enforcement semantics; Mnemosyne owns the persistence operation.
14. Mnemosyne provides authoritative persistence reporting.
15. A persistence-engine response is not automatically Harmonia's authoritative persistence outcome.
16. Mnemosyne provides persistence-management logging; Kleio owns authoritative Audit Information Management.
17. Energeia may observe/report Mnemosyne state without assuming persistence-management responsibility.
18. C&G remains a coherent governance information space without divergent semantic sharding.
19. O&A permits horizontal partitioning without changing governed meaning.
20. Persistence responsibility domains do not prescribe physical topology.

## 14. Deliberately Unresolved

This DesignNote does not determine persistence products, database topology, schemas, transaction implementation, distributed transaction mechanisms, rollback/retry algorithms, persistence outcome schema, shard keys/topology/routing, replication, service-health metrics, compartment representation or validation algorithms, source of semantic compartment applicability, detailed PEP interaction, logging schema, indexing/search implementation, caching or deployment topology.

## 15. Summary

Mnemosyne is Harmonia's **Persistence Manager**.

It provides Harmonia-level transaction and persistent-service oversight, shard management, compartment validation and reapplication, local Themis policy enforcement, persistence logging and authoritative reporting of persistence outcomes.

Mneme determines the governed information-management operation. Mnemosyne manages the authoritative persistence of that information. Underlying persistent services provide technology-specific storage mechanics.

**Mneme manages governed information; Mnemosyne manages and authoritatively reports its persistence.**
