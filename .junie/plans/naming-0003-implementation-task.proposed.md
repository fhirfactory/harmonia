<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie implementation Task — Backlog Item 0003

**Standing** · Draft for Solution Architect review and issue to Junie. The
issued assignment supplies execution authority and its instruction-entry event.

**Issuing authority** · Mark Hunter, Solution Architect.

**Assignee** · Junie.

**Repository** · `fhirfactory/harmonia`.

**Branch and starting source** · `feature/BL-0003-Calliope-Topic-and-TopicMask`,
HEAD `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb` and tree
`1846199b36bbfb07c87746c1367c059698b7120d`. The tree matches the accepted
development foundation at `7d5c195ad7782ab0b39032308a2eb8f72c678da5`.
Record the actual source at issue and preserve existing work in the checkout.
[Software HCRL 009][foundation]

**Architecture and software** · Accepted Architecture `1.1.26`, software
development version `0.1.0-SNAPSHOT`, software review target `software-0.1.0`.
HCRL 009 establishes the development foundation. First software release
acceptance remains governed separately. [Software HCRL 009][foundation]

**Accepted work specification** · [Backlog Item 0003, revision 000.003][item],
SHA-256 `6c5b7bf1612105d8043e62becadb8923d66d74cbf9151130567dd030dba5947b`, including its acceptance
qualifications and AC-1–AC-13.

## Assignment

Implement the standalone Naming increment defined by the accepted backlog
and Naming DRP. Keep production Java in
`calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/`. Place
corresponding tests under the matching Calliope test package and Naming
fixtures in Calliope test resources. Implement only the accepted Naming model,
codec and locally testable diagnostics. Downstream models, functional services
and operational logging remain outside this assignment. [Accepted Naming design][design]

The only Maven edits authorised on issue are direct `com.google.re2j:re2j:1.8`
and test-scoped `org.junit.jupiter:junit-jupiter:5.10.2` in `calliope/pom.xml`.
Use the inherited Java 21 and Surefire configuration. Enforce the approved
limits using bounded input and native RE2/J compiled-program checks, with
simple, independently tested pre-compilation guards. Keep diagnostics local,
synchronous and bounded. Apply the two qualifications recorded in the backlog.
[Backlog acceptance][acceptance]

DR-6 allows this standalone implementation ahead of Architecture 1.1.27 and
1.1.28 completion and publication. Preserve the 1.1.27/002 amendment, the
1.1.28/001 reconciliation and the accepted design. Dependent acceptance follows
the recorded incorporation and verification requirements. Record the approved
interim structural and dependency verification and the absent ArchUnit suite
within the scope of this increment. [DR-6][dr6] · [ArchUnit amendment][archunit]

## Verification and handoff

Run the backlog's focused Calliope tests and full Maven reactor verification
against the exact candidate. Record the tests actually executed, applicable
solution-wide regression, integration and conformance results, and any required
missing evidence. Produce the Implementation Report with acceptance-criteria
correspondence, limit and diagnostic evidence, and the exact uncommitted
candidate source against its starting Git tree. Submit that result for
independent Codex implementation review before requesting software acceptance.
[Implementation Guide][guide] · [Software Development Praxis][software]

At issue, preserve the operative assignment and its actual issuance event under
the applicable software iteration when established. Retain the implementation
evidence and review correspondence through that iteration. The assignment
authorises local in-scope edits and tests when issued. Commit, merge, tag, push,
publication and deployment require separate authority.

[item]: ../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
[acceptance]: ../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md#solution-architect-acceptance
[guide]: ../../docs/implementation/Harmonia-Implementation-Guide-Philosophy.md
[design]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[foundation]: ../../enterprise-architecture/design/register/software-0.1.0/hcrl-software-0.1.0-009.complete.md
[dr6]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-6
[archunit]: ../../enterprise-architecture/design/review/1.1.27/hcrl-1.1.27-002.pending.md
[software]: ../../enterprise-architecture/governance/software-development-praxis.md
