<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | RE2/J dialect and bounded construction | ACCEPTED | The Solution Architect accepts the exact current Naming candidate; preserve the predecessor finding as historical evidence. |
| [DR-2](#dr-2) | Strict interchange and safe validation context | ACCEPTED | Accept the current candidate and its reported verification; the original Codex findings remain unchanged. |
| [DR-3](#dr-3) | Supported Naming API and package containment | ACCEPTED | Accept the implementation reconciliation addendum; public visibility does not expand the supported API. |
| [DR-4](#dr-4) | Integrated verification and implementation acceptance prerequisites | ACCEPTED WITH QUALIFICATION | Standalone Naming is accepted; architecture incorporation, ArchUnit and integration coverage remain in their governed scopes. |
| [DR-5](#dr-5) | Bounded Minutiae | ACCEPTED WITH QUALIFICATION | Accept the documented provenance limitations; preserve unresolved original Report event and filename prefix. |
| [DR-6](#dr-6) | Design and Backlog reconciliation | ACCEPTED | Accept Final.003 implementation reconciliation and Backlog 0003 revision 000.005; no further Codex review is required. |

# HCRL software-0.1.0 / 010 — Naming implementation acceptance and closure

**Standing** · COMPLETE — the Solution Architect accepts the standalone Naming
development candidate subject to the DR-4 qualifications below. HCRL 010 is
registered complete without allocating a software release or tag. Maven identity
remains `0.1.0-SNAPSHOT`; this is not an accepted software release or terminal
tagged baseline. The Git transition remains reserved to the Solution Architect.

**Backlog** · The originating implementation Task referenced accepted revision
000.004; [revision 000.005](../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.005.md)
is now accepted and supersedes it for the reconciled scope and subsequent
assignments. Preserve the earlier [independent Codex review](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md)
as historical review evidence. The [completion Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261125T094849Z-naming-0003-completion-report.md)
and [closure Task](../../evidence/software-0.1.0/010/before/ai-agent-tasks/20261125T101213Z-naming-0003-closure-task.md)
record the current candidate and subsequent reconciliation. The [prior closure
Implementation Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/UNRESOLVED-naming-0003-closure-report.md)
preserves its original unresolved status; the [final closure Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261125T105242Z-naming-0003-final-closure-report.md)
records the completed disposition and final checks.

**Candidate source** · Branch `feature/BL-0003-Calliope-Topic-and-TopicMask`,
base commit `d79b62add0fb3f85cccafc29972330dd033b7145`, tree
`8b8dd180720ea36d36248d16bda7be2a7c73b4e7`. The six-file [Calliope
completion patch](../../evidence/software-0.1.0/010/after/naming-0003-completion-candidate.patch)
has SHA-256
`c9efe68fe6d5a9ed1c8e6c9dccc1411570a550de6c583b85461029875caa0df8` and
reproduced byte-for-byte against this base. The current candidate also includes
the accepted design and Backlog changes and closure evidence, all still
uncommitted; their exact paths and hashes are in the [closure proposal](../../evidence/software-0.1.0/010/after/naming-0003-release-closure-proposal.md).
[HCRL 009](../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md)
established the development foundation without an accepted software release
predecessor. The older `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb` / tree
`1846199b36bbfb07c87746c1367c059698b7120d` remains the exact source reviewed by
the original Codex report, not this accepted successor.

**Architecture authority and qualification** · Accepted Architecture `1.1.26`
remains the incorporated baseline. [Final.003](../../offline-design/DRP-Topic-and-TopicMask.Final.003.md)
and its accepted implementation reconciliation govern this standalone
acceptance. [Architecture 1.1.28/001 DR-6](../1.1.28/hcrl-1.1.28-001.pending.md#dr-6)
and DR-10 continue to govern Continuum incorporation and dependent work; that
incorporation is deferred and is not a prerequisite for this standalone Naming
acceptance. The [ArchUnit amendment](../1.1.27/hcrl-1.1.27-002.pending.md)
remains in its separate governed scope. The [Software Development Praxis](../../../governance/software-development-praxis.md)
governs release closure.

<a id="dr-1"></a>
## DR-1 — RE2/J dialect and bounded construction

**Review conclusion** · AC-4, AC-8 and AC-9 are affected. The authorized direct RE2/J 1.8 dependency and native 1,024-instruction cap work, including boundary testing against 1,025 instructions. Lexical class and quoting discrepancies let active repetitions or groups evade their approved checks. The lexical scanner rejects valid `\x{2603}`. The two canonical parsers also allocate every segment before checking maximum depth. The byte ceiling limits the total input while the 16-entry ceiling remains late. [Independent review, Findings 1 and 4](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#findings-and-required-treatment)

**Proposed disposition** · Request corrections preserving the pinned engine, accepted dialect, numeric thresholds and simple lexical safeguard qualification. Require bounded early depth enforcement and independent positive and negative regressions. Bring any proposed change to safeguards or the accepted dialect back for explicit decision under AX-04, AX-10 and AX-17.

**Solution Architect disposition · ACCEPTED.** Accept the exact successor
candidate identified above, subject to the reported verification and DR-4
qualifications. The original review finding remains historical evidence about
its reviewed source and is not rewritten or treated as a finding against the
accepted successor.

<a id="dr-2"></a>
## DR-2 — Strict interchange and safe validation context

**Review conclusion** · AC-6 through AC-8 and AC-12 are affected. XML reconstruction turns an empty completed qualifier into `N0`, creating a valid identity from an invalid record. The decoder also accepts contradictory encoding declarations and non-XML whitespace content inside a universal record. Positive byte determinism, Base64 round trips, reconstruction and DTD refusal were demonstrated. Invalid XML field or expression diagnostics can report a builder operation and lose the known position. [Independent review, Findings 2, 3 and 5](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#findings-and-required-treatment)

**Proposed disposition** · Request strict validated reconstruction of completed data and accurate safe failure context. Keep the UTF-8/Base64 representation, distinction between universal and ordinary, version presence and secure parser conditions. Require independent expected fixtures and local failure checks. The accepted operational logging deferral remains applicable. AX-14 governs the distinctions observed here.

**Solution Architect disposition · ACCEPTED.** Accept the current Naming
candidate and reported verification subject to DR-4. The original findings,
including Findings 2 and 3, remain unchanged in the predecessor's independent
review; the accepted successor conclusions are recorded separately here.

<a id="dr-3"></a>
## DR-3 — Supported Naming API and package containment

**Review conclusion** · AC-11 and AC-13 are affected. The six principal abstractions are cohesive and discoverable. Public match diagnostics and match-only enums follow from a package split despite package-local use. The public exception factory and several public resource constants also follow from cross-package access. The public zero-argument builder constructors duplicate authorized factories. The exception metadata and the four XML operations have supported consumer purposes. [Public API inventory and package assessment](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#package-containment-and-api-review)

**Proposed disposition** · Retain the six DRP model contracts and necessary XML and validation support. Request package-local evaluation diagnostics, internal limits and construction helpers, and package-private builder construction. Permit the implementation correction to preserve `interchange` only with an explicitly justified, small shared contract. Co-location with strict construction is a simpler candidate. Maintain the core/codec responsibility divide and avoid added access machinery. Require the corrected API inventory and educational documentation before acceptance under AX-12 and the Implementation Guide.

**Solution Architect disposition · ACCEPTED.** Accept the supported API and
package reconciliation in the Final.003 addendum: root model/builder types and
the supported codec facade remain the consumer surface; Java `public` visibility
for cross-package implementation helpers does not make them supported API, and
no JPMS export contract is asserted. The exact candidate is accepted subject to
the reported verification and DR-4 qualifications.

<a id="dr-4"></a>
## DR-4 — Integrated verification and implementation acceptance prerequisites

**Review conclusion** · AC-1–AC-13 are assessed in the [independent Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#ac-1ac-13-conformity). Exact replay, scoped POM and JDK package dependencies, all 23 source line counts and qualified interim structural checks were verified. OpenJDK 21.0.13.1 and Maven 3.9.12 returned PASS for Calliope `test` and the 50-module `verify` and `clean verify` runs. Each run executed 36 distinct tests in Calliope, seven classes with zero failures, errors or skips. The 72-test count has no corresponding execution. The retired ArchUnit suite was unexecuted under the scoped qualification. Cross-module application regression, runtime integration and deployment have no executed coverage on this reset candidate. Shared operational logging remains explicitly deferred. [Verification scope and commands](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#verification-and-test-quality)

**Dependent architecture** · Canonical validation against the exact underlying tree returned FAIL on the retained input locator already recorded by Software HCRL 009 DR-2 and Architecture HCRL 1.1.28/001 DR-5. The Naming patch has no canonical content. DR-6 retains the prerequisite for incorporation and verification before dependent acceptance.

**Proposed disposition** · Request changes to the exact candidate before first release acceptance. Require corrected test accounting, targeted tests and accurate documentation, an exact resubmission and repeated applicable component, conformance and solution-wide checks. Obtain explicit treatment of required missing integration and regression coverage and verify DR-6 prerequisites for the eventual acceptance. Establish the first software release scope and exact accepted source through the Solution Architect's confirmation. Branch, HEAD, index and the reviewed candidate were preserved during this review.

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.** The exact
standalone Naming implementation is accepted subject to the verification
reported in the completion Report. The Codex review is complete and no further
independent review is required. The following limits are explicit and do not
block this standalone acceptance:

- Architecture Continuum incorporation and dependent verification remain in
  Architecture 1.1.28/001 DR-6/DR-10; the missing retained-input locator remains
  an architecture-governed matter, not resolved by this software acceptance.
- The named ArchUnit suite remains unexecuted and separately governed by
  Architecture 1.1.27/002. The selector result is **UNEXECUTED**, not PASS.
- Solution-wide integration/runtime coverage was not executed. The full Maven
  `verify` passed, but the other modules reported no tests to run; this
  qualification is accepted for the standalone Naming scope and remains due in
  its existing integration/convergence scopes.
- Shared operational logging delivery remains deferred under the accepted
  standalone diagnostics qualification.
- No software release identity, tag or Git transition is authorized by this
  disposition. Maven `0.1.0-SNAPSHOT` remains a development build identity.

<a id="dr-5"></a>
## DR-5 — Bounded Minutiae

**Original review conclusion and request (preserved; superseded by the disposition below).**

The [operative Codex Task](../../evidence/software-0.1.0/010/before/ai-agent-tasks/20261124T090642Z-naming-0003-independent-review.md) and [Codex Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md) retain the attributable task and report events. The [Junie Task](../../evidence/software-0.1.0/010/before/ai-agent-tasks/20261124T082329Z-naming-0003-implementation-task.md), implementation Report and patch remain at their originating locations. Junie's original Report production event and corresponding filename prefix remain **UNRESOLVED** for Solution Architect treatment before closure. The iteration evidence checker reports **FAIL** because both prior Junie records use decorated event fields outside its conventional syntax. It recognizes both newly emitted review records. Preserve the original records and resolve the format and unavailable production event before closure.

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.**

```text
Minutiae-Accepted: Yes
Comment: The Solution Architect accepts the documented provenance limitations. Preserve original evidence and correct formatting only where independently verifiable. Do not invent missing timestamps or production events.
```

This disposition accepts the recorded provenance limitations. The eleven
historical Task/Report metadata fields identified as malformed by the initial
iteration check received formatting-only corrections to conventional
`Label: value` syntax; timestamps, `UNRESOLVED` values, filenames and substantive
record content were preserved. The closure Task's
`2026-11-25T10:12:13.284Z` value is independently corroborated. The post-
correction evidence check is **UNRESOLVED**, with zero format errors and six
records whose original timestamp and filename-prefix events remain unresolved.
Those accepted limitations are retained; no event was inferred or created.
HCRL 010 is registered complete with this qualification.

<a id="dr-6"></a>
## DR-6 — Design and Backlog reconciliation

**Solution Architect disposition · ACCEPTED.** Accept the implementation
reconciliation addendum to [Final.003](../../offline-design/DRP-Topic-and-TopicMask.Final.003.md#implementation-reconciliation-addendum)
and incorporate it into the accepted design without changing §§1–10. Accept
[Backlog Item 0003 revision 000.005](../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.005.md)
as superseding 000.004 for the reconciled scope and subsequent assignments; keep
000.004 and its original assignment history unchanged. Codex review is complete
and no further independent review is required for this candidate. The
Architecture incorporation, ArchUnit and integration qualifications are those
recorded in DR-4 and remain in their existing governed scopes.

## Registration

This completed review is registered at
[HCRL software-0.1.0 / 010](../../register/software-0.1.0/hcrl-software-0.1.0-010.complete.md).
The final activity outcome is in the [closure Report](../../evidence/software-0.1.0/010/after/ai-agent-reports/20261125T105242Z-naming-0003-final-closure-report.md).
