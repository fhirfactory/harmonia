<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 Topic/TopicMask — Adjudication Continuation (Iteration 004)

- **Programme:** Harmonia
- **Software baseline:** `software-0.1.0`
- **Evidence iteration:** `004`
- **Working branch:** `feature/BL-CALLIOPE-OO-01`
- **Standing:** Accepted Solution Architect dispositions, F-01.75–F-01.81; pending Codex incorporation and formal design review
- **Predecessor:** Iteration 003, `DRP-Topic-and-TopicMask.draft.001.002.md`
- **Target draft:** `DRP-Topic-and-TopicMask.draft.001.003.md`
- **Intended repository path:** `enterprise-architecture/design/evidence/software-0.1.0/004/before/F-01-Topic-TopicMask-Adjudications-Continuation.md`

## Scope and authority

This record captures decisions explicitly agreed by the Solution Architect following review of Draft.001.002 and the iteration-003 reconciliation findings. These decisions supplement rather than replace F-01.1–F-01.74. They authorise design-document consolidation and architectural reconciliation **only**, not Java implementation, architecture publication, acceptance of the revised DRP, an HCRL outcome, a commit, or a push.

No prior iteration evidence or predecessor DRP may be retrospectively overwritten. Where existing architecture or deployed consumers differ from these dispositions, preserve the discrepancy and require explicit compatibility or migration evidence.

## F-01.75 — Remaining API semantics — ACCEPTED

The following four behaviours are accepted:

1. **Canonical Topic version parsing.** Strict canonical parsing rejects textual empty version segments (for example, `Role:=LAB`). An absent version is represented by omitting the colon/version segment (`Role=LAB`). At builder/API level, `null` and `""` continue to represent absent versions as previously accepted.
2. **Name projections.** Adopt Draft.001.002 conventions: `getName()` is the unqualified value path; `getFriendlyName()` is the raw final value; `getRelativeName()` is the escaped final qualified, non-versioned entry; and `getDisplayName()` is the sole value at depth one or the final two values joined with `-` at greater depth. `getCanonicalName()` and `getCanonicalNameWithVersion()` retain the previously accepted structural definitions.
3. **Navigation boundaries.** `getAncestor(depth)` accepts only `1 <= depth <= getDepth()` and rejects out-of-range arguments. `isAncestorOf(null)` returns `false`. Strict ancestry remains version-independent.
4. **Universal TopicMask API.** A universal mask has no ordinary canonical RegEx expression or containment semantics. The public API must reflect that distinction. `getCanonicalExpression()` must not manufacture a canonical expression for universality; a separate explicit universal representation is required for interchange. The exact Java API arrangement may be resolved during design incorporation without redefining these semantics.

## F-01.76 — R1: Topic identity and historical references — ACCEPTED

1. Runtime `Topic` identity is the **non-versioned qualified canonical name**.
2. Architectural identities remain separately governed; existing Architecture Continuum identifiers are preserved.
3. Information Unit identity remains its independent IU UUID.
4. Unqualified Topic names need **not** be globally unique; distinct typed paths may share the same unqualified projection.
5. Legacy runtime Topic UUIDs are **not** retained in the redesigned `Topic` class. Preserve them through explicitly justified compatibility/migration mechanisms where needed.
6. Preserve historical references or migrate them explicitly; never silently reinterpret them.
7. Entry ordering is structural. Explicit sequence indices are assembly mechanisms, not independent Topic identity components.

**Residual evidence:** Determine concrete mapping for existing identifiers, registry-scoped rules, persisted references and collisions between generated and explicit `N<position>` fields where legacy users depend on distinctions.

## F-01.77 — R2: TopicMask compatibility and migration — ACCEPTED

1. New TopicMasks have structural identity, not runtime UUID identity.
2. Legacy Exact/Inclusive may map to `includeContainedElements=false/true` only where the original criterion basis and resulting scope are equivalent.
3. Map legacy unrestricted selectors to `TopicMask.all()` **only** where unrestricted intent is established; never treat an empty/invalid ordinary mask as universal by default.
4. Explicitly reconcile name-only legacy selectors against positional qualifier/value/version criteria.
5. Validate legacy Java RegEx against RE2/J; unsupported expressions need remediation. **No Java RegEx fallback**.
6. Verify persisted masks, references and consumer behaviour to preserve intended selection scope, especially for security-sensitive consumers.
7. Invalid or ambiguous legacy selectors **fail closed**; migration must not silently broaden matching or authorisation.

**Residual evidence:** Adapter mapping, migrated persisted forms, exact-versus-descendant selection, regex-dialect validation and security regression tests remain implementation obligations.

## F-01.78 — R3: Information Unit Topic designation — ACCEPTED

1. Preserve the governed IU Topic designation `harmonia.mneme.iu.<hex-string-of-compressed-uuid>`.
2. That designation is the **unqualified** `Topic.getName()` projection of a structured runtime Topic.
3. Define its fully qualified canonical name separately using **explicitly governed qualifier vocabulary**; illustrative qualifier sequences are not an accepted schema.
4. The IU UUID remains the authoritative IU identity, not replaced by the Topic identity.
5. TopicEntry versions remain separate from IU revisions, Mneme operational versions, Content Structure Version and content-native versions.
6. Preserve or explicitly migrate the established designation across persistence, transport, messaging, matching and references.

**Residual design decision:** Select the actual qualifier sequence and document the unqualified/qualified boundary mappings. Do not invent or silently promote illustrative qualifiers.

## F-01.79 — R4: Architectural identity and naming — ACCEPTED

1. Preserve existing architectural element IDs and governed `canonicalId` through architectural rename or containment change as prescribed by architecture governance.
2. Runtime Topic identity is structural: a change to its non-versioned canonical path changes runtime Topic identity.
3. Architectural `canonicalId` and runtime Topic canonical name are **different identity domains**; matching text does not establish equivalence.
4. Runtime `getCanonicalName()`, `getName()`, `getFriendlyName()` and `getDisplayName()` retain F-01 semantics. Architectural naming/display properties retain their separately governed meanings.
5. Where an architectural element describes or governs a runtime Topic, record correspondence explicitly rather than infer it from labels.
6. Maintain historical architectural references; any architecture update requires normal ADD/HCRL governance.
7. Do **not** add an architecture UUID or `canonicalId` field to runtime `Topic`.

## F-01.80 — R5: Calliope dependency placement — ACCEPTED

1. RE2/J is a direct Maven dependency of the module that implements TopicMask matching.
2. Topic and TopicMask remain neutral naming/matching models, independent of Themis and higher-level governance services.
3. Diagnostics use the existing Harmonia application-logging abstraction; the core model has no direct dependency on rsyslog, Log-Server or Log-Persistence.
4. Inspect any pre-existing `themis-api` dependency as a **separate** module-governance concern. Do not remove or restructure it incidentally as part of F-01.
5. Do not add an unnecessary RE2/J wrapper/abstraction without a demonstrated requirement.
6. Confirm module placement and dependency compatibility with governed Calliope structure when implementation is authorised.

**Residual verification:** Actual RE2/J dependency version and features, module boundary checks and logging abstraction availability.

## F-01.81 — R6: Logging under backpressure — ACCEPTED

1. `TopicMask.matches(Topic)` remains a synchronous Boolean operation.
2. A log failure must never turn a mismatch or evaluation error into a successful match; preserve fail-closed matching.
3. Ordinary TRACE-level matching diagnostics do not require synchronous **remote** delivery.
4. Use the existing Harmonia application logger and Log-Satellite architecture.
5. Buffering, backpressure, delivery acknowledgement and recovery belong to the logging framework, not TopicMask.
6. Do not add TopicMask-specific queues, worker threads or delivery subsystems.
7. Any blocking due to backpressure must be bounded, measured and checked against applicable performance requirements.
8. ERROR diagnostics remain subject to governed reliability expectations; no silent intentional discard is authorised.
9. Verify saturation, sink outage, recovery, console independence, latency and ERROR delivery during implementation.

**Important:** This disposition does not resolve a potential reliability/latency trade-off by silently shedding events. Any unsatisfied or conflicting requirement remains visible for explicit disposition.

## Architectural reconciliation summary

| Finding | Decision | Architectural disposition | Remaining obligation |
| --- | --- | --- | --- |
| R1 — Topic identity/references | F-01.76 | Accepted | Compatibility, registry and reference evidence |
| R2 — TopicMask migration | F-01.77 | Accepted | Equivalent selection and regex migration proof |
| R3 — IU Topic designation | F-01.78 | Accepted at representation level | Govern exact qualifiers; verify boundary mapping |
| R4 — Architectural naming/identity | F-01.79 | Accepted | Record explicit correspondence as applicable |
| R5 — Calliope dependencies | F-01.80 | Accepted | Module/dependency verification |
| R6 — Logging backpressure | F-01.81 | Accepted | Bounded-latency and reliable-delivery verification |

Architectural **disposition** is not implementation **verification**. R1–R6 may be marked architecturally dispositioned, but their listed compatibility, migration, concrete vocabulary and runtime evidence remain open until demonstrated or separately governed.

## Instructions for iteration-004 consolidation

- Preserve `software-0.1.0/003` and prior iterations unchanged.
- Incorporate F-01.75–F-01.81 into `DRP-Topic-and-TopicMask.draft.001.003.md`, updating substantive sections rather than merely appending decisions.
- Update decision delta, architecture reconciliation and implementation-gap evidence under `software-0.1.0/004/after/`; distinguish architectural dispositions from pending proofs.
- Prepare the appropriate HCRL for Solution Architect review **if separately authorised in the Codex task**; do not infer approval from this adjudication record.
- Do not implement code, modify the accepted Architecture Continuum, finalise the draft, accept an HCRL, publish, commit or push without explicit authorisation.

## Review checkpoint

The next checkpoint is Solution Architect review of the revised DRP, supporting evidence and any governance exceptions. This adjudication record records **accepted decisions**, not acceptance of the resulting Draft.001.003 or release.
