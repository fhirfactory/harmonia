<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — Mneme post-creation availability candidate amendment

Task-Issued-At: UNRESOLVED

Event source: the operative user instruction entered this Codex interaction without
an exposed attributable system instruction-entry timestamp. Original time and
filename prefix remain unresolved for Solution Architect treatment before closure.
Current/capture/Report time and filesystem metadata are not substituted.

This is a substantive follow-up within HCRL 1.1.19/001 DR-18/DR-19 to the
[original ADD Task](unresolved-dokimasia-provenance-add.md) and its
[Report](../../after/ai-agent-reports/20261103T000246Z-dokimasia-provenance-add.md).
Prior Report production time and original evidence are preserved. The user-issued
instruction records Solution Architect review recommendations and the bounded
amendment authority; it explicitly supplies no candidate acceptance or closure.

## Operative issued instruction (verbatim, outer code fence omitted)

# Harmonia — HCRL 1.1.19-001
## DR-18 Provenance Management / DR-19 Provenance and Assurance
### Architectural Review Findings and Bounded Candidate Amendment

### 1. Goal

Incorporate the Solution Architect's architectural review findings into the existing DR-18/DR-19 ADD candidate, without changing the accepted architectural baseline or expanding the scope of the ADD operation.

The component-level review has concluded.

The candidate is architecturally acceptable subject to one required amendment concerning Mneme's post-creation cache-availability behaviour.

**This instruction authorises candidate refinement, not candidate acceptance or HCRL closure.**

### 2. Expected Outcome

The existing ADD candidate is updated to:

1. Preserve the reviewed Provenance Management and Assurance architecture.
2. Introduce the agreed `Cache{InformationUnit}.postCreateAvailability` property and its three enumeration values.
3. Allocate responsibility for interpreting and applying that policy to Mneme.
4. Preserve Mnemosyne's authoritative persistence responsibilities unchanged.
5. Retain all previously identified unresolved design questions.
6. Produce updated reconciliation and verification evidence for Solution Architect review.

No new architectural decisions beyond those explicitly recorded below are authorised.

### 3. Context

The Solution Architect and ChatGPT have reviewed the following six definitions:

| Definition | Review disposition |
|---|---|
| Provenance — DataObject | Recommended acceptance unchanged |
| Provenance Client Library — ApplicationComponent | Recommended acceptance unchanged |
| Dokimasia — ApplicationComponent | Recommended acceptance unchanged |
| Kleio — ApplicationComponent | Recommended acceptance unchanged |
| Mneme — ApplicationComponent | Amendment required |
| Mnemosyne — ApplicationComponent | Recommended acceptance unchanged |

These are architectural review recommendations, **not formal Solution Architect acceptance**.

The established responsibilities remain:

- **Dokimasia:** Provenance Management framework and lifecycle; Assurance.
- **Kleio:** Audit Information Management.
- **Mneme:** Governed information management, including cache availability and managed information access.
- **Mnemosyne:** Authoritative persistence, versioning and persistence outcomes.

Provenance remains governed information managed through Mneme and durably persisted through Mnemosyne.

No independent Provenance persistence subsystem is authorised.

### 4. Agreed Architectural Amendment

#### 4.1 Property

Introduce the following architectural property:

`Cache{InformationUnit}.postCreateAvailability`

The property governs the required post-creation availability behaviour of an InformationUnit within Mneme's managed cache environment.

#### 4.2 Enumeration

The agreed values and semantics are:

| Value | Semantics |
|---|---|
| `Immediate-And-Distributed` | Following creation, the InformationUnit is immediately materialised in the originating Mneme cache and proactively distributed across the applicable cache environment. |
| `Immediate` | Following creation, the InformationUnit is immediately materialised in the originating Mneme cache without requiring proactive distribution. |
| `OnRequest` | The InformationUnit is durably persisted without requiring proactive cache materialisation or distribution. Materialisation occurs on demand through governed retrieval. |

#### 4.3 Architectural invariants

The following apply to all three values:

1. **Authoritative persistence:** Mnemosyne establishes and reports durable persistence.
2. **Governed accessibility:** Information remains accessible through existing governed retrieval and search mechanisms.
3. **Separation of concerns:** Cache materialisation and distribution do not establish durable persistence authority.
4. **Information semantics:** The policy does not redefine InformationUnit identity, versioning, lifecycle or governance.
5. **Persistence obligations:** The policy does not weaken existing durability, reliability, recovery or security requirements.

In particular, `OnRequest` means **on-demand materialisation**, not delayed persistence or delayed accessibility.

#### 4.4 Architectural responsibility

Mneme is responsible for applying the post-creation availability policy within its managed cache environment.

Mnemosyne remains responsible for authoritative persistence and persistence outcomes.

Mnemosyne's existing Search Management responsibility already supports searching authoritative managed persistence without assuming that transient caches contain the complete searchable information space.

No new principal Mneme service, Mnemosyne persistence mechanism or independent storage subsystem is authorised.

#### 4.5 Unresolved design detail

The mechanism by which the policy is determined remains unresolved.

Potential approaches include:

- A value supplied with an individual creation operation.
- A value derived from governing information metadata.

Do not select either approach or introduce an implicit precedence rule.

Similarly, do not invent defaults, timing thresholds, replication topology, cache implementation, new APIs or additional enumeration values.

Provenance is a natural candidate for `OnRequest`, but **this instruction does not mandate `OnRequest` for every Provenance instance**.

### 5. Constraints

Preserve the existing architectural governance and authority model.

In particular:

- Canonical Markdown under `enterprise-architecture/continuum/` remains authoritative.
- ArchiMate remains a governed architectural projection.
- Open Exchange XML remains an interchange representation.
- Existing canonical identities and relationships must be preserved.
- Existing baseline content must not be silently replaced or removed.
- Existing DR-18/DR-19 unresolved questions remain unresolved unless explicitly addressed by the agreed amendment.
- Existing security, authorisation, audit, information-management and persistence boundaries remain intact.
- No new implementation technology or runtime deployment arrangement is authorised.

The agreed policy is an architectural requirement. Its implementation design remains downstream work.

### 6. Authorised Activity / Scope

#### 6.1 Reconcile the existing candidate

Review the current HCRL 1.1.19-001 candidate and its DR-18/DR-19 evidence.

Preserve the existing candidate contributions concerning:

- Provenance.
- Provenance Client Library.
- Dokimasia.
- Kleio.
- Mneme.
- Mnemosyne.
- Existing candidate relationships.

Do not recreate existing elements or allocate replacement canonical identities.

#### 6.2 Amend Mneme

Incorporate the agreed `postCreateAvailability` property into Mneme's canonical candidate definition.

Represent the architectural semantics clearly without prescribing an implementation mechanism.

Where existing governed architectural constructs provide an appropriate representation for the property, use those constructs consistently.

If representing the property requires a new architectural element, specialisation or relationship not already authorised, report the proposed change for Solution Architect adjudication rather than silently introducing it.

#### 6.3 Preserve Mnemosyne

Do not amend Mnemosyne merely to duplicate Mneme's cache-availability policy.

Mnemosyne's existing candidate clarification remains appropriate.

#### 6.4 Reconcile architectural representations

Update the applicable candidate architectural representations and associated evidence consistently with the authorised amendment.

Preserve established model identities, relationships, view structures and governance qualifications.

Do not introduce unrelated architecture changes.

#### 6.5 Verify

Execute the applicable architecture verification and round-trip checks.

Report results and any qualifications.

The previously identified inherited strict transport-verification qualification concerning relationship `xml:lang` drift must remain visible. Do not weaken verification guards or treat inherited failures as newly introduced by this amendment.

### 7. Report

Produce an updated governed candidate report and supporting evidence using the established HCRL 1.1.19-001 evidence conventions.

The report must identify:

1. The six reviewed definitions and their review dispositions.
2. The exact Mneme amendment.
3. Any affected architectural representations.
4. Elements and relationships added, modified or preserved.
5. Confirmation that Mnemosyne's authoritative persistence boundary remains unchanged.
6. Confirmation that existing DR-18/DR-19 unresolved questions remain recorded.
7. Any additional unresolved representation or governance questions.
8. Verification results, failures and inherited qualifications.
9. The candidate's readiness for consolidated Solution Architect review.

Use the established `.junie/reports` reporting convention and governed evidence locations.

### 8. Acceptance Boundary

**Do not interpret this instruction as acceptance of DR-18, DR-19 or HCRL 1.1.19-001.**

Do not close the HCRL, publish an accepted baseline, advance the architecture version, or commit/push the candidate as accepted.

Present the amended candidate and its evidence for consolidated Solution Architect review.

Stop at that review boundary.
