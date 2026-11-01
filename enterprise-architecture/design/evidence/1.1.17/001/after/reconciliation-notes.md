<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Iteration 001 reconciliation basis

This is supporting evidence for the authorised ADD, using the accepted specification
and the pinned RAD-1.1.16 baseline in [the input record](../before/baseline.json).
The [complete correspondence](reconciliation.md) accounts for every active statement
and subject group. The specification supplies classifications and meaning; reconciliation
compares existing instances and preserves distinctions. It supplies no new requirements.

Four native Requirement identities are reused and extended with the accepted
AssuranceRequirement specialisation:

| Source | Existing identity | Correspondence |
| --- | --- | --- |
| NFR-019 Information Provenance | `3e45e60affb44bf384d54de26d3add6a` — PROV-001 | The information provenance preservation obligation; accepted applicability remains “where required”. |
| NFR-057 Operational Failure Diagnostics | `b5d3447a05b249a8af154136fc36c4d9` — OPS-004 | Operational diagnosis without inspecting implementation internals. |
| NFR-058 Cross-Service Activity Correlation | `f2e504e7d6374e41a81303265adda6de` — CORE-004 | Correlation across the downstream services participating in originating processing activity. |
| NFR-059 Component and Dependency Health Distinction | `4c02fec3af264b878ee3e0cfaecbf55f` — OPS-003 | Dependency-aware health/readiness reporting, extended with the explicit component/dependency distinction. |

Each reuse preserves canonicalId, Exchange identifier, native Requirement type,
formal/friendly names, existing outgoing relationships and original source bindings.
The display adds the governed specialisation prefix; the accepted source name is
retained explicitly as an alias in the body and register-entry metadata. The accepted
contribution becomes current. Original historical wording and its historical standing
remain explicit; the new contribution does not independently accept its additional
historical details. Relocation follows the specialisation's native storage profile.

The remaining statements establish independently bounded obligations or directions
at their specified normative level. Material distinctions considered include:

- The existing AX-01–AX-18 definitions retain highest authority and unchanged bytes.
  Related new ArchitecturalAxiom directions do not replace those broader definitions
  or acquire a new authority rank merely through their specialisation.
- NFR-067 governs accepted processing activities and their retention until obligations
  are discharged. REQ-FND-001 governs the external ingress durable acceptance boundary.
  Their related concerns do not establish the same scoped instance.
- NFR-023 governs information designated immutable generally. NFR-045 protects audit
  records against unauthorised modification. The existing immutable-authoritative-
  AuditEvent constraint has a narrower representation and an unconditional prohibition
  on correction/overwrite; those meanings are not collapsed.
- NFR-047 requires execution traceability across participating components. NFR-058
  requires processing-activity correlation across services. Traceability and correlation
  remain distinct accepted statements; no additional dependency between them is inferred.
- NFR-055 is a binding observability Mandate. NFR-057 is the diagnostic assurance
  obligation represented by the reused OPS-004 identity. Their distinct normative
  classifications are preserved.
- NFR-151 requires sufficient diagnostic/execution evidence to verify processing
  outcomes. REQ-FND-005 additionally establishes independent assurance responsibility
  and authority; evidence provision does not replace that independence contract.
- NFR-031 is a preferred repeated-execution safety strategy; NFR-136 requires policy-
  governed handling of duplicate incoming messages. The historical INT-002 durable
  at-least-once transport and duplicate-safe-consumer obligation retains its scope.
- The accepted placeholder “Information Durability During Platform Failure”
  (`6a2a3de03beb47b884293797b92a91eb`) retains its intentional incompleteness and
  existing containment. It is not repurposed as NFR-094, whose assurance outcome
  concerns recovery from any single machine failure, accepted work and manual intervention.

Subject identities are reconciled independently of lexical matches. G02 Information
Management spans the register's information concerns and statement classifications.
The existing `[RequirementGroup] Information Management`
(`c34c366824ff452eb575345c16db720b`) is scoped beneath Functional Requirements.
It and the other accepted RequirementGroup hierarchies remain unchanged. G02 is a
native Grouping with its own accepted cross-subject scope. None of the 82 subject
groups/subgroups becomes a BusinessFunction, Capability or invented specialisation.

The 219 Compositions express the source's subject containment: 69 group-to-subgroup
and 150 primary allocations. Three Associations preserve NFR-063's explicit joint
workload correspondence with NFR-066, NFR-068 and NFR-069. No relationship is inferred
from proximity, similar names, a source identifier prefix, an implementation owner,
or a desire to complete a graph. Proper names retained in descriptions do not
automatically establish Realization, dependency or Home relationships.

The source's open thresholds and applicability qualifiers remain visible. No
availability target, response-time threshold, storage-size threshold, search metric,
internal API traversal decision or deployment-specific resilience period is supplied
by this ADD. The six reserved source identifiers remain excluded from active bindings.
This increment does not claim to complete Harmonia's requirements architecture.
