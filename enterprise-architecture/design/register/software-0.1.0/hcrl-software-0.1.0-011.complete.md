<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#design-decisions) | IU identity/classification and accepted G02 correspondence | ACCEPTED | `informationConcept : Topic` supersedes the G02 ConceptFDN profile for the Calliope IU model. |
| [DR-2](#design-decisions) | Cache lifecycle and peer OperationalMetadata allocation | ACCEPTED | `CacheLifecycleMetadata` is a direct `InformationMetadata` constituent. |
| [DR-3](#design-decisions) | Explicit information-authority reference under AX-06 | ACCEPTED | Required `InformationMetadata.informationAuthority : Topic`. |
| [DR-4](#design-decisions) | Qualified RAW and release vocabulary authority | ACCEPTED | Applied as corrected by DR-9 and DR-13. |
| [DR-5](#dr-5) | Verification and candidate readiness | ACCEPTED WITH QUALIFICATION | ArchUnit/Paradeigma retained, not retired; **NOT EXECUTED — DEFERRED**, not a pass. |
| [DR-6](#dr-6) | Software 0.1.0 first-baseline and predecessor | ACCEPTED | Continue under `0.1.0-SNAPSHOT`; no formal release or tag. |
| [DR-7](#design-decisions) | Engineering decisions with semantic effect | DISPOSITIONED | Through DR-9 and DR-13; the original candidate choices remain as history. |
| [DR-8](#dr-8) | Architecture-target incorporation of the accepted design | CONTINUING OBLIGATION | Separate Architecture-target HCRL; not performed here. |
| [DR-9](#design-decisions) – [DR-13](#design-decisions) | Review corrections, profile schemas, helper clarification, DR-7 reconciliation | ACCEPTED / APPLIED | Incorporated in the accepted candidate-005. |
| [DR-14](#dr-14) | Candidate-005 implementation acceptance | ACCEPTED | Accepted as the BL-0005 Calliope InformationUnit development increment. |

# HCRL software-0.1.0 / 011 — BL-0005 Calliope InformationUnit acceptance

**Status** · COMPLETE — ACCEPTED BL-0005 CALLIOPE INFORMATIONUNIT DEVELOPMENT INCREMENT.
This registration records Solution Architect acceptance of the exact candidate-005
and its continuing qualifications. It does not allocate or establish an accepted
software release or a terminal tagged baseline.

**Software identity** · Maven `0.1.0-SNAPSHOT` remains the development identity.
No Software Release 0.1.0 version or `software/v0.1.0` tag is allocated or authorised.
The accepted source is located by the implementation commit and tree below, not by
the mutable `SNAPSHOT` label.

**Predecessor** · [HCRL 010][hcrl-010]'s accepted standalone Naming implementation
is the preceding accepted development increment (DR-6). [HCRL 009][hcrl-009]
established the `0.1.0-SNAPSHOT` development foundation without an accepted software
predecessor.

**Architecture baseline** · Accepted Architecture `1.1.26` remains the incorporated
baseline. `architecture-release.json` is unchanged; no Architecture increment or
Continuum publication was made. [HCRL 1.1.27/002][hcrl-1.1.27] and
[HCRL 1.1.28/001][hcrl-1.1.28] remain pending and unchanged.

**Backlog and design lineage** · [Backlog Item 0005 revision 000.004][backlog-004]
and [Draft 002.007 §1][draft-007] describe the accepted scope. Revisions 000.002 and
000.003, and Drafts 002.005 and 002.006, remain preserved predecessors. The originating
revision 000.001 and the earlier edit to Draft 002.004 were present in the working tree
before this iteration. Neither is part of the accepted candidate, and both remain
uncommitted, outside this registration (see [Qualifications](#qualifications)).

**Accepted candidate and source** · Branch `feature/BL-0005-Calliope-InformationUnit`.

| Item | Value |
| --- | --- |
| Base commit / tree | `a2e68b9f871102c435dca4cc2583ea69d881dfe1` / `d1af1bb1069345c973a54e77729a616aed5f45cb` |
| Accepted candidate | [candidate-005 patch][patch-005], SHA-256 `4d056a5711986ac0d5d37dd71ec7cc228d9f6c651a222add7ef09573bac460b9` ([sidecar][patch-005-sha]); 48 files |
| Accepted implementation commit | `3f71dc3d6e5e2955c09a112ea94b858a0181f83f`, parent `a2e68b9f…` |
| Accepted implementation tree | `4308e1a24ae3e3afadd5cd9d4d27de72869b0abb`, identical to candidate-005 applied to the base tree |

These closure records are committed separately, in a successor of the implementation
commit. Under Software Development Praxis §8, this record does not contain its own
commit hash; the [Closure Report][report-closure] is in that same commit, and the
resulting closure commit is reported outside the record.

**Evidence** · The operative [implementation Task][task], its successor Tasks and
Reports, and candidates 001–005 remain at their originating locations, as indexed in
the [review record][review]. The [Closure Task][task-closure] preserves this
instruction verbatim, and the [Closure Report][report-closure] records the
closure checks and the Git state.

<a id="design-decisions"></a>
## DR-1 to DR-4, DR-7 and DR-9 to DR-13 — accepted design decisions

The dispositions, superseded differences and preserved obligations are recorded in
the [review record][review] and are not restated here. They supersede, for the
Calliope IU model only, the identified parts of G02 Final.001.000 §2 and HCRL 1.1.25
DR-2. Unrelated obligations of those contracts remain in force. Accepting these
design decisions is distinct from the DR-14 implementation acceptance, and neither
amounts to Architecture Continuum incorporation.

<a id="dr-5"></a>
## DR-5 — Verification and candidate readiness

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.** ArchUnit/Paradeigma
is retained, not retired. Its execution is deferred until Harmonia begins implementing
complete subsystems, and the outcome is recorded as **NOT EXECUTED — DEFERRED**, not
PASS. For BL-0005, the JUnit, Maven verification and `jdeps` evidence is sufficient.
`AGENTS.md` is unchanged, and future complete-subsystem work must apply the retained
requirement. [Review DR-5][review-dr5].

Closure verification against the accepted tree (details in the [Closure Report][report-closure]):

| Check | Result |
| --- | --- |
| Working-tree candidate files compared with candidate-005 applied to the base | **PASS** · identical tree `4308e1a2…` |
| Candidate-005 SHA-256 sidecar | **PASS** |
| `timeout 300s mvn -B -ntp verify` | **PASS** · finished `2026-11-26T23:17:45Z`; all 50 reactor modules succeeded; 318 Calliope tests (91 Naming, 227 InformationUnit), 0 failures, errors or skips |
| `mvn -B -ntp -q -pl calliope -DskipTests javadoc:javadoc` | **PASS** · no warnings |
| `jdeps --list-deps calliope/target/classes` | **PASS** · `java.base`, `java.xml` |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** · not a pass |

Only Calliope contains tests. The reactor `verify` does not establish solution-wide
integration or runtime coverage, which remains due in its governed scopes.

<a id="dr-6"></a>
## DR-6 — Software 0.1.0 first-baseline and predecessor

**Solution Architect disposition · ACCEPTED.**

- Continue under `0.1.0-SNAPSHOT`.
- Do not allocate a formal Software Release 0.1.0 or create a release tag.
- Treat HCRL 010's accepted Naming implementation as the preceding accepted development increment.
- Establish the first formal software baseline when a meaningful integrated implementation is available.

No release identity, tag or release-version edit was made.

<a id="dr-8"></a>
## DR-8 — Architecture-target incorporation (continuing obligation)

Incorporating the accepted BL-0005 design into the Architecture Continuum is a separate
Architecture-target transition with its own HCRL and any applicable Architecture patch
allocation. That includes the IU Class and IU Metadata allocations, generic profile
schemas, the Themis-Krites boundary, and the successor and RAW ContentSchema decisions.
This software registration neither performs nor discharges it. [Review DR-8][review-dr8].

<a id="dr-14"></a>
## DR-14 — Candidate-005 implementation acceptance

**Solution Architect disposition · ACCEPTED.** "The Solution Architect accepts
candidate-005 as the BL-0005 Calliope InformationUnit development increment." This
binds the exact candidate and source identified above, the conformance evidence in the
[DR-7 Final-Reconciliation Report][report-dr13], and the closure verification in DR-5.
The qualifications below apply.

<a id="qualifications"></a>
## Qualifications and continuing obligations

- ArchUnit/Paradeigma: **NOT EXECUTED — DEFERRED** (DR-5). Solution-wide
  integration and runtime coverage was not executed.
- Architecture Continuum incorporation remains outstanding (DR-8). This includes the
  Themis-Krites boundary, which has no Continuum element and no implemented service.
- No Krites service, Mneme/Mnemosyne persistence integration, codec or serialisation
  is implemented. Media-type subtype registry membership is not verified; only
  RFC 6838 syntax and a recognised top-level type are checked.
- Task-Issued-At and Report-Produced-At remain `UNRESOLVED` for every 011 Task and
  Report. No timestamp is inferred.
- Backlog Item 0005 revision 000.001 remains untracked and uncommitted, as it was at
  task entry. Links to it from the committed 011 evidence therefore do not resolve in
  Git until the Solution Architect commits or otherwise disposes of it. The earlier
  local edit to Draft 002.004 also remains uncommitted; committed links to Draft
  002.004 resolve to the version committed at `a2e68b9f`.
- No push, merge, tag, software release or Architecture release was performed.

## Final governance closure

HCRL 011 is complete for the accepted BL-0005 Calliope InformationUnit development
increment. Architecture remains `1.1.26`. Software remains `0.1.0-SNAPSHOT`, and no
release or tag is allocated. The next BL assignment starts from the accepted
implementation commit above and carries the qualifications and obligations listed here.

[hcrl-009]: hcrl-software-0.1.0-009.complete.md
[hcrl-010]: hcrl-software-0.1.0-010.complete.md
[hcrl-1.1.27]: ../../review/1.1.27/hcrl-1.1.27-002.pending.md
[hcrl-1.1.28]: ../../review/1.1.28/hcrl-1.1.28-001.pending.md
[review]: ../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md
[review-dr5]: ../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md#dr-5
[review-dr8]: ../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md#dr-8
[backlog-004]: ../../../../docs/backlog/BacklogItem-0005-Calliope-InformationUnit.v000.004.md
[draft-007]: ../../offline-design/DRP-Information-Unit-Framework.Draft.002.007.md
[patch-005]: ../../evidence/software-0.1.0/011/after/bl-0005-calliope-informationunit-candidate-005.patch
[patch-005-sha]: ../../evidence/software-0.1.0/011/after/bl-0005-calliope-informationunit-candidate-005.patch.sha256
[task]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-calliope-informationunit-implementation-task.md
[task-closure]: ../../evidence/software-0.1.0/011/before/ai-agent-tasks/UNRESOLVED-bl-0005-hcrl-011-acceptance-closure-task.md
[report-closure]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-hcrl-011-acceptance-closure-report.md
[report-dr13]: ../../evidence/software-0.1.0/011/after/ai-agent-reports/UNRESOLVED-bl-0005-dr-7-final-reconciliation-report.md
