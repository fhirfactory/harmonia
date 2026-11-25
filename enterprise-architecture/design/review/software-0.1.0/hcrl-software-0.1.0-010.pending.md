<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | RE2/J dialect and bounded construction | Not recorded | Correct and verify the approved safeguards. |
| [DR-2](#dr-2) | Strict interchange and safe validation context | Not recorded | Correct completed-record reconstruction and failure metadata. |
| [DR-3](#dr-3) | Supported Naming API and package containment | Not recorded | Decide the supported surface before implementation acceptance. |
| [DR-4](#dr-4) | Integrated verification and implementation acceptance prerequisites | Not recorded | Independent recommendation is changes requested. |
| [DR-5](#dr-5) | Bounded Minutiae | Not recorded | Retained activity provenance and unresolved original production event. |

# HCRL software-0.1.0 / 010 — Independent Naming implementation review

**Standing** · Pending Solution Architect disposition. Codex recommends changes requested for the exact Naming candidate. The candidate remains at Maven `0.1.0-SNAPSHOT`.

**Backlog** · [0003 revision 000.003](../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md). [Reviewed implementation Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/unresolved-naming-0003-implementation-report.md) · [Independent Codex review Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md).

**Source** · Branch `feature/BL-0003-Calliope-Topic-and-TopicMask`, commit `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree `1846199b36bbfb07c87746c1367c059698b7120d`. [HCRL 009](../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md) established the development foundation with no accepted software release predecessor. The uncommitted [Calliope candidate patch](../../evidence/software-0.1.0/010/after/naming-0003-candidate.patch) has SHA-256 `70ad92b2503e89b15ff150e89562723b23ca80c1f7cc94648863e7f5a553f97a`. Its 33 paths reproduced byte-for-byte against the working candidate and the index was clean.

**Architecture authority** · Accepted Architecture `1.1.26`, [qualified Naming design](../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md) and [Software HCRL 004 DR-10](hcrl-software-0.1.0-004.pending.md#dr-10). [Architecture 1.1.28/001 DR-6](../1.1.28/hcrl-1.1.28-001.pending.md#dr-6) bounds standalone implementation and governs dependent acceptance. The [ArchUnit amendment](../1.1.27/hcrl-1.1.27-002.pending.md) remains separate. [Software Development Praxis](../../../governance/software-development-praxis.md) governs exact candidate review and closure.

<a id="dr-1"></a>
## DR-1 — RE2/J dialect and bounded construction

**Review conclusion** · AC-4, AC-8 and AC-9 are affected. The authorized direct RE2/J 1.8 dependency and native 1,024-instruction cap work, including boundary testing against 1,025 instructions. Lexical class and quoting discrepancies let active repetitions or groups evade their approved checks. The lexical scanner rejects valid `\x{2603}`. The two canonical parsers also allocate every segment before checking maximum depth. The byte ceiling limits the total input while the 16-entry ceiling remains late. [Independent review, Findings 1 and 4](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#findings-and-required-treatment)

**Proposed disposition** · Request corrections preserving the pinned engine, accepted dialect, numeric thresholds and simple lexical safeguard qualification. Require bounded early depth enforcement and independent positive and negative regressions. Bring any proposed change to safeguards or the accepted dialect back for explicit decision under AX-04, AX-10 and AX-17.

<a id="dr-2"></a>
## DR-2 — Strict interchange and safe validation context

**Review conclusion** · AC-6 through AC-8 and AC-12 are affected. XML reconstruction turns an empty completed qualifier into `N0`, creating a valid identity from an invalid record. The decoder also accepts contradictory encoding declarations and non-XML whitespace content inside a universal record. Positive byte determinism, Base64 round trips, reconstruction and DTD refusal were demonstrated. Invalid XML field or expression diagnostics can report a builder operation and lose the known position. [Independent review, Findings 2, 3 and 5](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#findings-and-required-treatment)

**Proposed disposition** · Request strict validated reconstruction of completed data and accurate safe failure context. Keep the UTF-8/Base64 representation, distinction between universal and ordinary, version presence and secure parser conditions. Require independent expected fixtures and local failure checks. The accepted operational logging deferral remains applicable. AX-14 governs the distinctions observed here.

<a id="dr-3"></a>
## DR-3 — Supported Naming API and package containment

**Review conclusion** · AC-11 and AC-13 are affected. The six principal abstractions are cohesive and discoverable. Public match diagnostics and match-only enums follow from a package split despite package-local use. The public exception factory and several public resource constants also follow from cross-package access. The public zero-argument builder constructors duplicate authorized factories. The exception metadata and the four XML operations have supported consumer purposes. [Public API inventory and package assessment](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#package-containment-and-api-review)

**Proposed disposition** · Retain the six DRP model contracts and necessary XML and validation support. Request package-local evaluation diagnostics, internal limits and construction helpers, and package-private builder construction. Permit the implementation correction to preserve `interchange` only with an explicitly justified, small shared contract. Co-location with strict construction is a simpler candidate. Maintain the core/codec responsibility divide and avoid added access machinery. Require the corrected API inventory and educational documentation before acceptance under AX-12 and the Implementation Guide.

<a id="dr-4"></a>
## DR-4 — Integrated verification and implementation acceptance prerequisites

**Review conclusion** · AC-1–AC-13 are assessed in the [independent Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#ac-1ac-13-conformity). Exact replay, scoped POM and JDK package dependencies, all 23 source line counts and qualified interim structural checks were verified. OpenJDK 21.0.13.1 and Maven 3.9.12 returned PASS for Calliope `test` and the 50-module `verify` and `clean verify` runs. Each run executed 36 distinct tests in Calliope, seven classes with zero failures, errors or skips. The 72-test count has no corresponding execution. The retired ArchUnit suite was unexecuted under the scoped qualification. Cross-module application regression, runtime integration and deployment have no executed coverage on this reset candidate. Shared operational logging remains explicitly deferred. [Verification scope and commands](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#verification-and-test-quality)

**Dependent architecture** · Canonical validation against the exact underlying tree returned FAIL on the retained input locator already recorded by Software HCRL 009 DR-2 and Architecture HCRL 1.1.28/001 DR-5. The Naming patch has no canonical content. DR-6 retains the prerequisite for incorporation and verification before dependent acceptance.

**Proposed disposition** · Request changes to the exact candidate before first release acceptance. Require corrected test accounting, targeted tests and accurate documentation, an exact resubmission and repeated applicable component, conformance and solution-wide checks. Obtain explicit treatment of required missing integration and regression coverage and verify DR-6 prerequisites for the eventual acceptance. Establish the first software release scope and exact accepted source through the Solution Architect's confirmation. Branch, HEAD, index and the reviewed candidate were preserved during this review.

<a id="dr-5"></a>
## DR-5 — Bounded Minutiae

The [operative Codex Task](../../evidence/software-0.1.0/010/before/ai-agent-tasks/20261124T090642Z-naming-0003-independent-review.md) and [Codex Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md) retain the attributable task and report events. The [Junie Task](../../evidence/software-0.1.0/010/before/ai-agent-tasks/20261124T082329Z-naming-0003-implementation-task.md), implementation Report and patch remain at their originating locations. Junie's original Report production event and corresponding filename prefix remain **UNRESOLVED** for Solution Architect treatment before closure. The iteration evidence checker reports **FAIL** because both prior Junie records use decorated event fields outside its conventional syntax. It recognizes both newly emitted review records. Preserve the original records and resolve the format and unavailable production event before closure.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```
