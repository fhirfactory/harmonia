Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

# Junie Assignment — Calliope Topic/TopicMask Corrective Implementation

## 1. Authority

**Assignment:** Correct the existing Calliope Topic/TopicMask implementation candidate.

**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-CALLIOPE-OO-01`  
**Architecture baseline:** `1.1.26`  
**Software candidate:** `0.1.0`  
**Implementation iteration:** `008`

**Accepted Backlog Item:**

`docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md`

**Accepted design:**

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md`

**Review authority:**

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-008.pending.md`

**Independent review:**

`enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md`

The accepted Backlog Item and design govern this assignment.

The independent review provides the corrective findings and supporting evidence.

## 2. Objective

Correct the existing implementation to satisfy the accepted Topic/TopicMask contracts and resolve the identified implementation defects.

Preserve the conforming implementation already delivered.

Do not redesign Topic, TopicMask, their identity model, builders, canonical expressions or matching semantics.

**This is corrective implementation work, not a new development baseline or architecture exercise.**

## 3. Mandatory corrections

### A-01 — Strict logical deserialization

Correct the deserialization of TopicMask and its logical representation.

Reject malformed or ambiguous inputs, including:

- Universal masks containing ordinary-mask criteria.
- Missing, null or incorrectly typed discriminators.
- Duplicate or conflicting JSON properties.
- Coerced numeric or string discriminator values.
- Invalid or incorrectly typed RegEx expressions.

Universality must be explicit.

A malformed ordinary mask must never become universal through deserialization.

Apply equivalent validation to nested consumer representations.

Preserve the accepted distinctions between absent, empty and wildcard version expressions.

Introduce a representation adapter or dedicated decoder if necessary.

Do not introduce an unrelated serialization framework.

### A-02 — Security action criteria

Correct `InformationSecurityContext` so typed TopicMask action criteria retain their semantics.

Ensure:

- Typed action criteria survive applicable serialization/deserialization.
- Exact versus inclusive selection is preserved.
- Version constraints are preserved.
- Presence and emptiness checks account for typed criteria.
- Equality and hashing reflect the relevant semantic state.
- Ambiguous legacy action strings cannot silently change selector scope.

Add negative tests proving that affected selector changes cannot widen permitted selection.

Remember that TopicMask matching remains neutral selection. Themis retains authorisation authority.

### A-03 — Operational subject conversion

Correct `OperationalMetadataMask` legacy subject handling.

Preserve the verified historical qualifier semantics.

Do not reinterpret qualified historical selectors through generated `N<position>` qualifiers.

Where equivalence cannot be established, reject the affected selector and document the unresolved mapping.

Add before/after regression cases covering Exact and Inclusive behaviour, including the independently demonstrated `Domain=Alpha` and `N0=Alpha` distinction.

### A-04 — Historical Ergon Task Topic interchange

Correct the existing `ErgonPayloadFhirConverter` Topic reader/writer boundary.

Investigate the historical Topic representations actually supported by that boundary.

Preserve supported representations or implement explicit format dispatch and validated migration where authorised.

Do not use a permissive fallback that silently changes Topic identity or qualifier meaning.

Add regression fixtures for:

- Historical Topic JSON.
- Current Topic JSON.
- Qualified canonical names.
- Versioned Topic information where applicable.
- Invalid and ambiguous representations.

Identify any historical UUID or native Java serialization compatibility that cannot be preserved without an architectural or migration decision.

Do not invent a universal migration mechanism.

### A-05 — Fail-closed evaluation surface

Correct the public `TopicMaskEvaluator` behaviour that allows empty ordinary criteria to match universally.

Ensure universal matching is possible only through the explicit accepted universal-mask contract.

Restrict or redesign the affected evaluator entry point within the accepted implementation boundary.

Preserve valid existing mask evaluation behaviour.

Add regression tests for the invalid empty-criteria case.

### A-06 — Educational documentation

Complete the missing documentation identified by Codex.

Document the important contracts, including:

- Topic equality and version-independent identity.
- TopicMask equality, hashing and representation.
- Builder state and replacement semantics.
- Non-obvious validation and conversion rules.
- Failure handling and fail-closed behaviour.

Retain the established 500-physical-line limit per production Java class unless a documented exception receives Solution Architect approval.

## 4. Focused test completion

Address the missing mandatory acceptance vectors identified in C-05.

Include:

- Hash-based collection behaviour.
- Composed/decomposed Unicode preservation.
- Generated versus explicit qualifiers.
- Topic version retention through serialization.
- Unsupported RE2/J constructs.
- Broad ordinary masks remaining ordinary.
- Invalid logical representations.
- Security and operational selector regressions.
- Handling-boundary and diagnostic failure behaviour.

Use the actual configured serializers and consumers where relevant.

Do not treat equality that ignores versions as proof that version information survived serialization.

## 5. Compatibility and migration

Follow Backlog Item 0003 / AC-2.

For each affected historical boundary:

1. Identify the supported predecessor representation or contract.
2. Establish the actual compatibility requirement.
3. Compare historical and corrected behaviour.
4. Preserve the contract where practical and authorised.
5. Record any unsupported or unresolved migration requirement for Solution Architect adjudication.

Do not silently widen security-sensitive selection or workflow routing.

Do not independently establish a new IU qualifier vocabulary, UUID allocation policy, registry model or historical migration policy.

## 6. Serialization and XML

Preserve the accepted format-independent logical model.

Use Jackson where appropriate, but enforce strict logical decoding before ambiguous information can become a valid domain object.

Immutable representation records or external adapters are permitted where they provide a concrete benefit.

They are **not mandatory**.

For XML:

- Verify XML round trips where an existing governed boundary requires XML.
- Preserve the accepted expression and version-presence semantics.
- Report any encoding restrictions that prevent lossless interchange.

Do not introduce a new XML protocol or restructure the entire core model merely to demonstrate XML support.

## 7. Resource limits and logging

Verify the existing Topic/TopicMask resource safeguards.

Retain the direct RE2/J dependency and bounded expression handling.

Add bounded performance measurements for representative compilation and matching workloads where practical.

Ensure diagnostics do not expose PHI, RegEx payloads or unsafe exception contents.

Integrate with the applicable existing logging contract.

**Do not implement Log-Satellite infrastructure as part of this assignment.**

Distinguish component logging conformance from outstanding platform-level logging and reliability obligations.

## 8. Verification

Execute the applicable Maven and architecture tests against the corrected candidate.

Include:

- Focused Calliope tests.
- Directly affected consumer tests.
- Architecture/ArchUnit tests.
- Full Maven reactor where feasible.
- Applicable integration tests.

Report all results accurately.

Skipped, failed, unavailable or unexecuted tests must remain explicit.

Record sufficient source-state evidence to associate test results with the exact corrected candidate.

Do not claim that unavailable Docker, broker or infrastructure-dependent tests passed.

## 9. Evidence and governance

Retain the existing Iteration 008 implementation and independent review reports unchanged.

Create new corrective Task/Report evidence within the existing Iteration 008 evidence structure, using repository conventions and actual available provenance.

The corrective report must include:

- Exact source changes.
- A-01–A-06 finding dispositions.
- C-05 test coverage.
- AC-1–AC-7 evidence mapping.
- Historical compatibility findings.
- Verification commands and outcomes.
- Outstanding qualifications.
- Exact candidate identification.

Submit the corrected candidate for renewed independent review through HCRL.008.

Do not mark the implementation accepted.

## 10. Repository constraints

Before editing, inspect and record the local repository state, including modified and untracked files.

Preserve existing unrelated work.

In particular:

- Do not modify Mneme/Mnemosyne V001.
- Do not modify HCRL.006.
- Do not reopen G03.
- Do not modify the accepted Architecture Continuum.
- Do not alter accepted Backlog Item 0003.
- Do not overwrite prior evidence.
- Do not commit, merge, tag or push.
- Do not establish a software release or baseline.

Keep source modifications confined to Calliope and the minimum necessary directly affected consumers.

If a necessary correction requires a change outside the authorised boundary, report it rather than expanding scope independently.

## 11. Completion

Return:

1. A concise implementation summary.
2. A-01–A-06 correction outcomes.
3. Test and regression results.
4. AC-1–AC-7 correspondence.
5. Outstanding compatibility and integration qualifications.
6. Exact modified-source inventory.
7. The new corrective Implementation Report location.

**Stop after implementation, verification and submission for independent review.**

No acceptance or release authority is implied.