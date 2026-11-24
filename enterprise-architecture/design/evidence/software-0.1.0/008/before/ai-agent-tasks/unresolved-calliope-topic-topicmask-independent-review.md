Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Independent implementation review task — Calliope Topic and TopicMask

- **Review assignment** · Solution Architect, in the operative user instruction below.
- **Boundary** · [Software 0.1.0 / Iteration 008][hcrl], Architecture 1.1.26.
- **Inputs** · [Accepted design Final.001.001][design], [BL-CALLIOPE-OO-01 revision 1][backlog], [Implementation Task][implementation-task], [Implementation Report][implementation-report].
- **Entry locator** · branch `feature/BL-CALLIOPE-OO-01`, HEAD `8af315134a1f2e03e75512f66513ef2f7eeb80b8`, uncommitted candidate. The reviewed Calliope delta and limits of the build attribution are recorded in the corresponding report.

## Operative instruction

~~~text
# Codex Assignment — Calliope Topic/TopicMask Implementation Review

## 1. Authority and scope

**Assignment:** Independent implementation review  
**Software candidate:** 0.1.0  
**Review iteration:** 008  
**Architecture baseline:** 1.1.26  
**Review authority:** Solution Architect

**Accepted design:**

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md`

**Backlog Item:**

`docs/backlog/calliope-oo-01-topic-models.md`

ID: `BL-CALLIOPE-OO-01`, revision 1.

**Implementation evidence:**

- `hcrl-software-0.1.0-008.pending.md`
- Iteration 008 Implementation Task.
- Iteration 008 Implementation Report.

The purpose is to independently review Junie's actual implementation against the accepted design, applicable architectural authority and backlog requirements.

**This is a code review, not a new design exercise.**

## 2. Implementation conformance

Inspect the actual Java implementation, relevant consumers and tests.

Verify conformance with Final.001.001, particularly:

- Topic identity, equality, hashing and ordering.
- TopicEntry validation.
- Sequential and indexed builders.
- Canonical naming and versioning.
- TopicMask ordinary/universal semantics.
- RE2/J matching and expression handling.
- Immutability and concurrency.
- Error handling and diagnostics.
- Accepted §8 Java API.
- Mandatory §10 behavioural acceptance criteria.

Identify deviations, missing tests, incorrect implementations and unjustified implementation choices.

Do not reopen settled architectural decisions.

## 3. Backlog reconciliation

Investigate the conflicts between the existing Backlog Item and Final.001.001.

Specifically:

**AC-2:** Historical API, UUID and serialization compatibility.

**AC-3:** Duplicate indexed Topic entries.

Determine:

1. The exact conflicting requirements.
2. Whether Final.001.001 explicitly supersedes them.
3. Which historical compatibility obligations remain applicable.
4. Whether backlog revision 2 is required.

Recommend precise amendments if necessary.

**Do not modify or accept the Backlog Item.**

The Solution Architect will determine the appropriate disposition.

## 4. Serialization and representation

Investigate JSON, XML and Java serialization separately.

Determine:

1. Which Calliope core classes depend directly on Jackson annotations.
2. Whether JSON serialization and deserialization preserve the accepted logical information model.
3. Whether the same logical representations work with Jackson XML.
4. Whether XML round-trip tests exist.
5. Whether `Optional` values, absent version expressions, empty expressions and explicit universal masks survive round trips.
6. Whether deserialization consistently enforces domain validation.
7. Whether serialization adapters can be separated from core domain classes without altering accepted behaviour.

Evaluate the suitability of immutable Java records as **serialization representation objects**, distinct from rich domain models.

Consider NFR-099's XML preference.

Do not assume that Jackson annotations are automatically defective or that records are mandatory.

Provide a recommendation based on actual implementation evidence and architectural responsibilities.

## 5. Compatibility and security

Review:

- Historical Topic UUID and canonical-name changes.
- Legacy TopicMask selector conversion.
- Exact versus inclusive selection.
- Case-sensitive and case-insensitive matching.
- `TopicSubscription` compatibility.
- `InformationSecurityContext`.
- `OperationalMetadataMask`.
- InformationUnit Topic designation.
- Potential widening of security-sensitive selection.

Confirm that invalid or ambiguous selectors fail closed.

Identify any remaining compatibility requirements that cannot be demonstrated from existing repository evidence.

Do not invent historical migration requirements unsupported by the repository or accepted architecture.

## 6. Resource limits and logging

Review:

- Candidate numerical limits.
- RE2/J dependency and version.
- Expression validation and resource safeguards.
- Concurrency behaviour.
- Logging implementation and diagnostic safety.
- Existing logging infrastructure integration.
- Unverified performance and reliability requirements.

Distinguish actual defects from unexecuted acceptance criteria.

## 7. Verification evidence

Reconcile the Implementation Report with the actual test sources and available results.

The report claims:

- 50 successful Maven reactor modules.
- 1,765 tests.
- Zero failures and errors.
- Ten skipped tests.

Determine which acceptance criteria are adequately supported and which remain unverified.

Do not classify skipped or unexecuted tests as successful.

Execute additional bounded tests only where necessary to establish a specific finding.

## 8. Review reporting

Produce an exception-based review report.

For each substantive finding, provide:

- Finding ID.
- Severity.
- Applicable requirement or authority.
- Exact source location.
- Observed implementation.
- Nature of the discrepancy.
- Recommended correction or Solution Architect disposition.
- Whether the finding blocks acceptance.

Distinguish:

**A — Implementation defect:** Code contradicts accepted requirements.

**B — Governance conflict:** Existing authorities or acceptance criteria disagree.

**C — Evidence gap:** Required behaviour has not been demonstrated.

**D — Improvement opportunity:** Potentially beneficial change not required by accepted authority.

Do not convert category D recommendations into mandatory implementation requirements without Solution Architect approval.

## 9. Constraints

- Do not modify production code or tests.
- Do not modify accepted architectural documents.
- Do not revise the Backlog Item.
- Do not close or accept HCRL.008.
- Do not modify Mneme/Mnemosyne V001 or HCRL.006.
- Do not commit, tag, push or release.
- Preserve existing repository state and evidence.

Place the review report in the appropriate Iteration 008 evidence location, following repository conventions.

## 10. Completion

Return:

1. An executive assessment of Iteration 008.
2. A prioritised exception register.
3. A recommended disposition for Backlog AC-2/AC-3.
4. A JSON/XML serialization assessment.
5. An explicit list of acceptance blockers.
6. Recommended bounded corrections for Junie, if required.

**Perform the review against the actual implementation, not merely Junie's Implementation Report.**

Avoid speculative redesign, unnecessary documentation and repeated examination of settled decisions.

The objective is to identify the minimum necessary work to make Calliope Topic/TopicMask acceptable.
~~~

[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-008.pending.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[implementation-task]: UNRESOLVED-topic-topicmask-implementation-task.md
[implementation-report]: ../../after/ai-agent-reports/UNRESOLVED-calliope-topic-topicmask-implementation-report.md
