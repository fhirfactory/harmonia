<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item: 0003 — Cohesive Topic and TopicMask implementation

**Backlog Item ID:** 0003

**Backlog Item Revision:** 000.002

**Standing:** Accepted with qualifications by the Solution Architect under
[HCRL.008 / DR-3][backlog-acceptance].

## Overview

### High-Level Synopsis of Effort

Bring the existing Calliope Topic and TopicMask candidate and its directly
affected consumers into conformance with the accepted runtime design.

### High-Level Synopsis of Activities

Correct the reviewed boundary behaviour, demonstrate applicable historical
compatibility or obtain an explicit disposition, and verify the exact integrated
candidate.

### High-Level Synopsis of Outcome

Cohesive, documented Java objects enforce construction, identity and matching
rules. Supported consumers retain their agreed semantics across model and
interchange boundaries.

### High-Level Synopsis of Acceptance Criteria

Demonstrate Topic/TopicMask component conformance and report the integrated
Harmonia verification separately. Submit outstanding platform integration,
logging and reliability evidence for Solution Architect disposition before
dependent software-release acceptance.

## Context

Architecture **1.1.26** is the baseline for software candidate **0.1.0**,
Iteration **008**. Final.001.001 was accepted with qualifications in
HCRL.004 DR-10. The historical predecessor, [BL-CALLIOPE-OO-01, revision 1][historical-predecessor],
records the original accepted work specification used by the Iteration 008
implementation assignment. Backlog Item `0003`, revision `000.002`, is the
accepted work specification for the subsequent bounded corrective implementation
assignment. The assignment supplies execution authority. Acceptance of the
Iteration 008 implementation candidate and integrated software release remains
pending. [Design acceptance][design-acceptance] ·
[Pending HCRL.008][hcrl] · [Implementation Task][implementation-task] · [Implementation Report][implementation-report] ·
[Independent review and candidate locator][review] · [Preparation Task][preparation-task]

Scope covers Calliope Topic, TopicMask, their entries, builders, factories and
necessary collaborators, focused tests and documentation, and directly affected
model, selector and representation boundaries. Use TopicSubscription, IU and
operational/security metadata, Ergon Task Topic interchange, and relevant
MLLP/Petasos/Praxis consumers to establish compatibility. Limit any consumer
correction to the established Topic or TopicMask defect and its authorised
boundary. Whole-Calliope restructuring, unrelated oversized files, Mneme and
Mnemosyne V001 work, collective G03 work, registry or UUID allocation redesign,
qualified IU vocabulary design, new wire protocols, and changes to routing or
security policy orchestration or Themis allocation are outside scope. The first
accepted software baseline remains a separate decision. [Historical predecessor][historical-predecessor] ·
[HCRL.004][original-review] · [Architecture 1.1.26 baseline disposition][baseline]

## Associated Requirements

| Authority | Applicable obligation and standing |
| --- | --- |
| [Final.001.001 §§1–10][design] and [design acceptance][design-acceptance] | Implement the accepted runtime model, §8 Java API and §10 acceptance behaviours. Preserve all §9 qualifications. |
| [Calliope-Models][calliope], [Topic][ea-topic], [TopicMask][ea-mask] and [IU identity contract][iu-contract] | Keep the shared model and neutral matching within Calliope. The existing Continuum and incorporated IU contracts retain their standing. Conflicts in identity, sequencing, registry rules and selector semantics remain governed dependencies under R1–R4 and finding B-02. Preserve existing architectural identities and obtain any required separate alignment or explicit treatment before dependent acceptance. |
| [AX-04][ax04], [AX-07][ax07], [AX-14][ax14] and [AX-17][ax17] | Preserve Harmonia semantics and distinct model/interchange states. Consumer operations retain independent Themis authorisation. Record uncertain authority or migration scope for adjudication. |
| [NFR-099][nfr] and [Final.001.001 §5][interchange] | Prefer XML where practical for an applicable boundary and retain JSON or other formats required by its contract. Establish the supported encoding and verify it at that boundary. |
| [Implementation Guide][guide] and [Software Development Praxis][software] | Retain the cohesive OO design, meaningful unhappy paths, educational documentation, full source-file line counts and solution-wide candidate evidence. |

## Expected / Associated Activity

1. Establish the exact source for the authorised correction and the predecessor
   sources for each supported boundary. Inventory actual readers, writers and
   consumers. Trace UUID-backed references, registry rules and aliases, generated
   versus explicit qualifiers, public APIs, and historical selector and
   serialization formats. Use representative historical fixtures to demonstrate
   preservation. Where a mapping, boundary applicability, migration or
   qualification is unresolved, obtain an explicit disposition for the affected
   scope before relying on it. Preserve the unqualified IU designation, independent
   IU UUID and distinct revision meanings. Use only separately approved qualified
   IU mappings. [F-01 reconciliation][architecture-reconciliation] · [Review findings][findings]
2. Correct A-01 and A-05 by enforcing explicit universal intent at logical
   decoding and throughout the evaluation surface. Address A-02, A-03 and A-04 in
   the existing security-action, operational subject and Ergon Topic boundaries.
   Compare old and corrected selection across Exact/Inclusive, case, version
   presence, name-only and qualified criteria where applicable. Reject invalid
   or ambiguous input. Demonstrate that no affected selector widens access or
   routing without explicit authority. Preserve the existing implementation's
   conforming builder, naming and matching behaviour. [Review findings][findings]
3. Keep the division of responsibilities clear and complete the A-06 documentation.
   Add focused tests for the reviewed defects and the outstanding §10 cases in
   C-05. Use the actual configured readers and writers. An XML test is required
   where the boundary uses or is governed to use XML. The choice described in
   D-01 remains optional. [Review findings][findings] · [Mandatory behaviours][design-tests]
4. Verify Topic/TopicMask component conformance against the exact corrected source,
   including resource limits, the direct pinned RE2/J dependency, bounded
   compilation and evaluation costs, and safe diagnostics. For integrated
   Harmonia software-release review, perform the applicable solution-wide
   regression, integration, ArchUnit and implementation-conformance checks
   against the exact integrated candidate. Verify the shared logging, delivery
   and latency obligations in §6 and report covered, skipped and unavailable
   scenarios. Carry outstanding platform integration, logging and reliability
   evidence forward for separate Solution Architect disposition.
   [Design §6][resources] · [Software Development Praxis][software]
5. Submit contract, implementation and evidence correspondence by criterion in an
   Implementation Report, with the exact candidate and related Task evidence.
   Route acceptance and remaining qualifications through the software HCRL.
   Preserve the historical revision references and the standing of the earlier
   Implementation Task, Report and independent review. [Pending HCRL.008][hcrl]

## Expected Outcomes

- Cohesive, documented and immutable Topic and TopicMask models, with verified
  indexed replacement, positional matching and logical interchange.
- Bounded corrections with tested consumer scope, an explicit account of
  supported historical boundaries, and individually traceable migration or
  qualification decisions where required.
- Attributable component conformance and a responsibility and full-file line-count
  inventory. A separate integrated verification record identifies the exact
  candidate and outstanding software-release prerequisites.

## Acceptance Criteria

| Criterion | Observable result | Verification / expected evidence |
| --- | --- | --- |
| AC-1 | Each changed or new production object has a cohesive responsibility and justified collaborators. Keep construction, validation, compatibility and pattern evaluation in appropriate objects. Every affected production Java file is at most 500 physical lines including documentation and declarations, unless the Solution Architect accepts a documented per-file exception. | Review the bounded diff, responsibility-to-class mapping and full-file counts. Verify that decomposition preserves useful documentation and avoids artificial fragmentation and unsupported abstractions. |
| AC-2 | The Java API and logical information model conform to Final.001.001, including the non-versioned qualified path as Topic identity, the canonical string API, and structural masks. Preserve applicable historical Topic and TopicMask identifiers and references, registry uniqueness and aliases, public APIs and consumer contracts, persisted selectors, JSON/XML/Java serialization boundaries, and security-sensitive selection and routing, or record an explicitly authorised migration or qualification for each affected obligation. | Compare the relevant predecessor APIs, selectors and fixtures with the corrected candidate. Cover TopicSubscription and the A-02–A-04 boundaries. Verify typed action criteria retain their matching, presence and relevant equality semantics through interchange and Themis remains independent. Compare ordered values and version presence across the formats in scope. Replay historical Ergon payloads and relevant reference/registry cases, prove equivalent selector scope, and verify the governed IU designation and any approved mappings. Record boundary applicability and any evidence limits (C-01/C-07). |
| AC-3 | The first successful add fixes sequential or indexed mode. Indexed additions require nonnegative indices and a contiguous zero-based path at build. A valid indexed add to an occupied position replaces the entry without increasing depth. Validate before mutation. Failed adds, replacements and builds preserve builder state and published snapshots. Builders remain reusable and completed Topics immutable. | Exercise both indexed overloads, including identical repeats, mixed-mode attempts, negative indices, out-of-order continuity and gap recovery. Check failed first adds and invalid replacements, final-position qualifier defaults, canonical naming and versioned projections, boundary input validation, defensive copying and immutable read access. Compare generated and explicit qualifiers in identity, matching and interchange. |
| AC-4 | Ordinary and universal masks retain distinct kinds. `TopicMask.all()` supplies explicit universality. Ordinary masks apply ordered qualifier/value/optional version criteria using full-field RE2/J, exact depth by default and optional trailing containment. Absent, present-empty and wildcard version constraints have distinct semantics. Canonical expression and logical reconstruction preserve those distinctions. Invalid or ambiguous selectors and null candidates fail closed. | Exercise positive, negative, null, depth, positional, case/flag, literal/escaped field, version-presence, concurrency and consumer vectors under §10. Verify positive and negative interchange with the actual binders, including ambiguous discriminator/member inputs (A-01), a broad mask that remains ordinary, and empty ordinary criteria across the evaluation surface (A-05). Confirm no Java RegEx fallback and verify independent Themis evaluation where applicable. |
| AC-5 | Educational documentation covers affected classes and interfaces, public and protected operations and non-obvious private logic, including failures, immutability, compatibility and neutral matching. | Review the documentation and concise conformance inventory, including A-06. Compilation or a coverage percentage alone is insufficient. |
| AC-6 | **Topic/TopicMask component conformance.** Demonstrate the applicable Final.001.001 §10 behaviours with component/unit and directly affected consumer checks, including release-specific bounds, resource behaviour, concurrency and safe diagnostics. **Integrated Harmonia software-release acceptance.** Verify the exact integrated candidate under the Software Development Praxis with applicable component/unit, solution-wide regression and integration, and architectural/implementation-conformance results. Outstanding platform integration, shared logging and reliability evidence requires separate Solution Architect disposition. | Report component and integrated verification outcomes separately. Record bounded commands, exact source, modules/scenarios, environments/dependencies, outcomes and failures. Cover C-02/C-03 with workload measurements, safe handling-boundary diagnostics and the governed logging path, including console independence, receipt acknowledgement, outage, saturation, recovery and return latency. Record uncertain delivery and verify routine TRACE without synchronous remote delivery. Address C-04's skipped live integration cases on the corrected candidate and C-05's focused gaps. Required unavailable, failed, skipped, timed-out, stalled or unexecuted checks require explicit Solution Architect disposition before being treated as satisfied. |
| AC-7 | The Implementation Report and pending software HCRL identify the applicable item revision and exact candidate, trace AC-1–AC-6 to evidence, and preserve scope and historical provenance. Acceptance is attributable to the Solution Architect. | Retain the Task/Report links, exact source and bounded diff, and actual outcomes against the applicable revision. Trace the recorded B-01 disposition and retain B-02 and C-06 for applicable adjudication. First baseline and release closure require their own authority. |

[Accepted Java API][design-api] · [Mandatory behavioural acceptance][design-tests] · [Iteration 008 review exception register][findings]

[historical-predecessor]: calliope-oo-01-topic-models.md
[preparation-task]: ../../enterprise-architecture/design/evidence/software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-backlog-revision-2-preparation.md
[implementation-task]: ../../enterprise-architecture/design/evidence/software-0.1.0/008/before/ai-agent-tasks/UNRESOLVED-topic-topicmask-implementation-task.md
[implementation-report]: ../../enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/UNRESOLVED-calliope-topic-topicmask-implementation-report.md
[review]: ../../enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md
[findings]: ../../enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md#prioritised-exception-register
[hcrl]: ../../enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-008.pending.md
[backlog-acceptance]: ../../enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-008.pending.md#dr-3-backlog-compatibility-and-historical-migration
[original-review]: ../../enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[design-acceptance]: ../../enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[baseline]: ../../enterprise-architecture/design/register/1.1.26/hcrl-1.1.26-001.complete.md#dr-5
[design]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[interchange]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#5-canonical-mask-expressions-and-logical-interchange
[resources]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#6-resource-limits-concurrency-and-diagnostics
[design-api]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#8-accepted-java-surface
[design-tests]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#10-mandatory-behavioural-acceptance
[architecture-reconciliation]: ../../enterprise-architecture/design/evidence/software-0.1.0/004/after/F-01-Architecture-Reconciliation.md
[iu-contract]: ../../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.002.md#2-identity-and-distinct-revisions
[calliope]: ../../enterprise-architecture/continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md
[ea-topic]: ../../enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md
[ea-mask]: ../../enterprise-architecture/continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md
[ax04]: ../../enterprise-architecture/continuum/motivation/principle/54508cda80484a6caf95b901780eaf7d.md
[ax07]: ../../enterprise-architecture/continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md
[ax14]: ../../enterprise-architecture/continuum/motivation/principle/d3fc6025dc7646d1b2020b51f7a44bfa.md
[ax17]: ../../enterprise-architecture/continuum/motivation/principle/architectural-axiom/8882573df7ce43c6bc0f66da0a676139.md
[nfr]: ../../enterprise-architecture/continuum/motivation/principle/6973441f2d62448188af9bfce5ad9d9f.md
[guide]: ../implementation/Harmonia-Implementation-Guide-Philosophy.md
[software]: ../../enterprise-architecture/governance/software-development-praxis.md
