<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item: 00002 — Mneme–Mnemosyne Governed Persistence V001

**Backlog Item ID:** 00002

**Backlog Item Revision:** 1

**Target software release:** 0.1.0

**Accepted Architecture baseline:** 1.1.26

The Solution Architect's explicit disposition in the V001 implementation assignment authorizes this bounded item and its implementation. The issued assignment in this interaction is its scope source; this item does not establish software-release acceptance.

## Overview

### High-Level Synopsis of Effort

Deliver a first tested vertical slice for governed InformationUnit and operational Status persistence through Mneme and Mnemosyne to PostgreSQL.

### High-Level Synopsis of Activities

Implement the Mneme governed read/write path, Mnemosyne governance and persistence coordination, and the Non-FHIR PersistenceService; exercise successful and adverse outcomes with PostgreSQL-backed tests.

### High-Level Synopsis of Outcome

The assigned operations establish and retrieve authoritative local database state without representing cache acceptance as durable success, bypassing Themis, or converting owner-defined Status into an InformationUnit.

### High-Level Synopsis of Acceptance Criteria

Demonstrate the governed IU round trip and Status round trip, integrity/version preservation, default-deny behaviour, and accurate success/failure/indeterminate outcomes with automated tests and qualified durability claims.

## Context

This item covers the V001 assignment issued for software candidate `0.1.0` against Architecture baseline `1.1.26`. The entry source was branch `feature/BL-CALLIOPE-OO-01` at commit `8af315134a1f2e03e75512f66513ef2f7eeb80b8`, with a clean working tree.

The assignment is bounded to the existing `hestia/mneme-cluster`, `hestia/mneme-persistence`, `hestia/mnemosyne-api`, `hestia/mnemosyne-clinical`, and `hestia/mnemosyne-operations` modules and their necessary model, Themis, database, and test dependencies. It does not complete the distributed cache architecture or implement PostgreSQL production HA/DR. A successful local transaction may be reported as local transactional persistence only; production cross-site durability remains unproven.

The FHIR PersistenceService is outside the primary V001 implementation scope. Its existing implementation may be inspected to preserve the integration boundary, but this item does not authorize a wholesale rewrite or independent FHIR feature expansion. The Solution Architect's subsequent task-specific disposition establishes `software-0.1.0/006` as the implementation evidence boundary and authorizes `hcrl-software-0.1.0-006.pending.md`; the missing iteration-005 HCRL/layout-check matter remains unresolved and is not closed by that authorization.

The explicit Solution Architect disposition supplied with the assignment directs creation of this revision-1 item, authorizes implementation under it, and states that the assignment's scope remains unchanged. Its event timestamp was not supplied in that disposition and is not inferred here.

## Associated Requirements

| Authority / architectural reference | Applicable obligation and standing |
| --- | --- |
| [AX-04 — Harmonia Owns the Semantics; Engines Provide the Machinery](../../enterprise-architecture/continuum/motivation/principle/54508cda80484a6caf95b901780eaf7d.md) | Keep Harmonia-owned persistence and outcome meaning distinct from PostgreSQL/JDBC mechanics. |
| [AX-05 — Active State and Authoritative Durable State Are Distinct](../../enterprise-architecture/continuum/motivation/principle/0412a2084f0145babc4f022c71fea65f.md) | Mneme provides governed application access; only Mnemosyne establishes authoritative durable state. Cache acceptance is not authoritative persistence. |
| [AX-07 — Security Is Intrinsic to Managed Operations](../../enterprise-architecture/continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md) and [Invariant 6](../../AGENTS.md#invariant-6-default-deny-security-governance-themis) | Apply existing Themis authorisation and fail closed; do not add an independent security framework or let caller-controlled resource data establish authority. |
| [AX-14 — Semantic Distinctions Are Preserved](../../enterprise-architecture/continuum/motivation/principle/d3fc6025dc7646d1b2020b51f7a44bfa.md) | Preserve the distinction between InformationUnit and owner-defined operational Status, and between persistence outcome stages. |
| [AX-15 — Uncertainty Is Preserved Until Resolved](../../enterprise-architecture/continuum/motivation/principle/ef8abbaec37e4890ad78b9f263e67906.md) | An interrupted or indeterminate commit is not reported as success, failure, or absence without evidence. |
| [Accepted Mnemosyne persistence model, HCRL 1.1.23/001 DR-1](../../enterprise-architecture/design/register/1.1.23/hcrl-1.1.23-001.complete.md#dr-1) | Preserve the five logical responsibility tiers; the GovernedPersistenceFramework coordinates persistence workflow and the Persistence Adapter performs engine-specific execution. PersistenceService is jointly realised, not an extra tier. |
| [GovernedPersistenceFramework](../../enterprise-architecture/continuum/application/application-function/812fa47cef1442aeb34ec34f263616be.md) and [PersistenceService](../../enterprise-architecture/continuum/application/application-service/c050edfefe8b4d2b8892bc94b2d27a5c.md) | Reuse the canonical logical responsibilities and generic service boundary; neither element prescribes a JDBC API or database schema. |
| [Harmonia Implementation Guide](../implementation/Harmonia-Implementation-Guide-Philosophy.md) and [Software Development Praxis](../../enterprise-architecture/governance/software-development-praxis.md) | Apply cohesive object responsibilities, defensive failure handling, newcomer-oriented documentation, source-size limits, formal evidence, and solution-wide candidate verification. |

This is bounded software realisation of existing responsibilities plus the assignment's explicit Status-persistence requirement. It creates no new architectural element, persistence protocol, deployment boundary, or production durability claim. The Mneme-to-Mnemosyne interaction DRP remains outside the authority asserted by this item unless separately accepted and incorporated.

## Expected / Associated Activity

1. Reuse existing Calliope IU, Themis, persistence-result, and Mneme/Mnemosyne code where it satisfies the accepted boundary; make only the minimum cohesive changes needed for V001.
2. Provide Themis-governed Mneme read/write operations and Mnemosyne controllers that delegate to the GovernedPersistenceFramework and Non-FHIR PersistenceService.
3. Persist IU identity, representation, metadata, version and references in PostgreSQL with explicit transaction and duplicate/update semantics; preserve historical versions and do not implement physical deletion as ordinary lifecycle behaviour.
4. Persist and retrieve owner-defined operational Status without converting it into an InformationUnit or assuming semantic ownership.
5. Distinguish cache acceptance, local transaction commit, authoritative confirmation, cross-site durability, failed writes and indeterminate outcomes. Document the development-only limits of the local PostgreSQL configuration.
6. Add automated unit and PostgreSQL integration tests for successful round trips, integrity, authorization rejection, duplicate/repeated requests, conflicts and exercisable failure or indeterminate outcomes. Exercise restart/reconnection where practical.
7. Inspect the existing FHIR persistence implementation and preserve its service boundary; leave FHIR implementation changes outside V001's primary scope.
8. Produce the implementation report and exact-candidate verification required by the Software Development Praxis, including unavailable or incomplete checks and known limits.

## Expected Outcomes

- Mneme callers can write and read an InformationUnit through Mnemosyne and PostgreSQL without direct access to persistence internals.
- Mnemosyne's GPF coordinates persistence operations through a Non-FHIR PersistenceService that preserves IU identity, version, metadata, representation and references.
- Operational Status can be recorded and read under its semantic owner's identity without being represented as an IU.
- Security and persistence outcomes are explicit, fail-closed and testable; successful local database work is not presented as production cross-site durability.
- FHIR persistence remains intact and outside the primary V001 scope.
- The exact integrated candidate has attributable test and conformance evidence and is submitted for Solution Architect review without claiming acceptance.

## Acceptance Criteria

| Criterion | Observable result | Verification / expected evidence |
| --- | --- | --- |
| AC-1 — Governed IU round trip | A valid IU is written through Mneme and Mnemosyne, committed by the Non-FHIR PersistenceService to PostgreSQL, then retrieved through the governed Mneme read path. | Automated PostgreSQL integration test verifies IU identity, metadata, representation, version, references and the persistence result. |
| AC-2 — Versioning and repeat requests | Supported create/update requests preserve historical IU versions, reject stale expected versions, and handle duplicate or repeated requests consistently with the implemented contract. | Tests cover identical and conflicting duplicates, a valid version progression, stale-version conflict and repeat invocation. |
| AC-3 — Operational Status | Owner-defined Status is persisted and retrieved without automatic conversion into an InformationUnit or transfer of semantic ownership. | PostgreSQL integration round trip verifies Status identity/owner/payload and that IU persistence was not invoked for it. |
| AC-4 — Themis governance | Supported operations require an established Themis security context and authorisation decision; denial, missing decision, and evaluator failure do not proceed to persistence. | Unit/integration tests demonstrate authorised operations and default-deny rejection at both Mneme and Mnemosyne boundaries. |
| AC-5 — Outcome and durability semantics | The implementation distinguishes successful local transaction, not-committed failure, and indeterminate outcome where the failure can be exercised; it does not claim production cross-site durability. | Tests and report identify the exact outcome mappings and PostgreSQL development configuration. Any unexercisable case is explicitly reported. |
| AC-6 — Reconnection and recovery | Where practical, a fresh service instance can retrieve committed IU and Status state; database loss or reconnect failure cannot yield false successful persistence. | Restart/reconnection and bounded database-failure tests, with actual environment and limitations recorded. |
| AC-7 — Integrated candidate and conformance | The exact integrated Harmonia candidate is reviewed for implementation quality, architecture boundaries and applicable regression/integration scope. FHIR is not wholesale rewritten or expanded. | Implementation Report maps these criteria to source, commands, results, solution-wide checks, source-file/documentation conformance, failures and limitations; pending software HCRL requests Solution Architect acceptance. |

The backlog item authorizes no commit, merge, tag, push, deployment, architecture release allocation, Continuum publication, or Solution Architect acceptance beyond the explicit V001 implementation disposition recorded above.