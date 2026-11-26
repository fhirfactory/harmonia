<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# BL-0005 Calliope InformationUnit Implementation Report

Report-Produced-At: UNRESOLVED — the attributable system event time for completed Report emission was unavailable at sufficient precision; the task-context clock supplies only local minute precision. No UTC event or filename prefix is inferred.
Task-Issued-At: UNRESOLVED — see the originating Task record; no event time is inferred.

**Agent / role** · Junie · bounded software implementation investigation and reporting.  
**Software target** · `software-0.1.0` candidate · iteration `011`.  
**Branch** · `feature/BL-0005-Calliope-InformationUnit`.  
**Source at task entry** · commit `a2e68b9f871102c435dca4cc2583ea69d881dfe1`; the working tree already had a modified Information Unit Draft and an untracked BL-0005 Backlog Item.  
**Architecture baseline** · accepted Architecture `1.1.26`.

**Disposition** · **STOPPED — NO IMPLEMENTATION CANDIDATE.** A material conflict with accepted architecture and unresolved model authority were found before candidate verification. The source sketch created during initial implementation was removed; it was neither compiled nor tested and is not submitted as evidence. This Report requests Solution Architect disposition and does not claim implementation completion, architectural acceptance, software release acceptance or authority to continue past the identified boundaries.

## 1. Authority, scope and outcome

The operative assignment references [Backlog Item 0005 revision 000.001][backlog] and [Information Unit Framework Draft 002.004][draft]. The Backlog Item identifies itself as a **proposed work specification**; Draft 002.004 identifies itself as a **draft for Solution Architect review**. The [Software Development Praxis][software-praxis] states that a Backlog Item can propose work or architecture but does not itself authorise or enact architecture. The implementation assignment authorises engineering only within accepted architectural authority; it cannot disposition the conflicts below.

The exact implemented scope is **none**. No production source, tests, Calliope README section, or candidate patch remains from this activity. Existing Calliope Naming files were not edited. The pre-existing Draft 002.004 working-tree edit and untracked Backlog Item were preserved unchanged; this Report does not silently adopt or reconcile either document.

The Task record is retained at its originating location. The pending [software HCRL 011][hcrl] records independently reviewable blockers and awaits attributable Solution Architect decisions. It is not an implementation-acceptance submission because no code candidate exists.

## 2. Architecture review and required dispositions

| Matter | Repository evidence and conflict | Required Solution Architect treatment |
| --- | --- | --- |
| IU identity and classification | Accepted G02 [Final.001.000 §2][g02] defines immutable `informationProfile : ConceptFDN` with fixed value `solution=harmonia.domain=internal.concept=InformationUnit`; a Topic is a derived projection where required. Accepted [HCRL 1.1.28/001 DR-1/DR-4][hcrl-1.1.28] preserves independent IU identity and defers IU qualifier vocabulary and specialised mappings. Draft 002.004 §1.1 and Backlog 0005 instead replace the profile with `informationConcept : Topic`. [HCRL software 010][hcrl-010] accepted the standalone Naming implementation, not this replacement. | Decide whether to retain the accepted profile/derived-Topic contract and revise the software scope, or initiate the separately governed Architecture HCRL/ADD needed to amend the accepted IU Class and resolve reference/boundary correspondence. No dependent code is authorised until the applicable disposition is recorded. |
| Cache/operational metadata allocation | Accepted [HCRL 1.1.25 DR-2][hcrl-1.1.25] places Mneme operational revision, Active Generation, cache/working facts and operation/handshake facts in **associated peer `OperationalMetadata`**, explicitly not automatic IU constituency. Draft 002.004 §§1–2 and the Backlog model `CacheLifecycleMetadata` as a direct `InformationMetadata` constituent. | Confirm the accepted peer allocation and define any permitted Calliope-facing projection, or provide an explicit Architecture disposition for a changed IU composition and its semantic boundary. |
| Explicit information authority | Draft 002.004 §1.5 says the IU retains an explicit reference to applicable governed information authority under [AX-06][axioms], but §1's model tree and Backlog 0005 define no field or association for it. `Canonical` quality is expressly independent of authoritative standing and cannot supply that reference. | Specify the authority reference's governed type, ownership and relationship to the IU model, or explicitly direct a separately scoped architecture resolution. Do not infer a field from implementation convenience. |
| `RAW` and release vocabulary | Backlog 0005 calls for the approved qualified `RAW` Topic and release-defined builders, but the cited draft supplies no exact qualified path or complete in-scope value list. The candidate path in the implementation plan was not found as an accepted definition and has no independent authority. | Identify the exact accepted `RAW` Topic path and the applicable release-defined vocabulary sources/owners before implementing builders that encode them. |
| Candidate verification and ArchUnit | No implementation or tests were produced; no Maven command, Naming regression, Javadoc, `jdeps`, or architecture selector was run. Backlog 0005 states that the named ArchUnit suite is absent and its retirement is pending; [HCRL 1.1.27/002][hcrl-1.1.27] remains pending and [HCRL software 010 DR-4][hcrl-010] treats the selector as unexecuted, not passed. | After architecture blockers are resolved and a candidate exists, run the assignment's Calliope test command and applicable solution-wide regression, integration and conformance checks. Obtain explicit treatment for any required check that remains unavailable; do not infer a waiver from the pending retirement proposal. |
| Software baseline | [HCRL software 009][hcrl-009] establishes the development foundation without an accepted software predecessor; HCRL 010 accepts standalone Naming but no Software Release 0.1.0 or tag. | Before eventual release acceptance, explicitly establish whether `0.1.0` is the first accepted software baseline and confirm the exact accepted source and predecessor treatment. No release is allocated here. |

These are not engineering choices that can be resolved by this implementation assignment. Architecture changes require their own Architecture-target HCRL and authority; the software HCRL does not duplicate or accept such a transition.

## 3. Implementation Guide conformance and limitations

- **Functional correctness:** no InformationUnit classes or public contracts were retained; therefore no functional behaviour is claimed.
- **Unhappy paths and defensive behaviour:** no implementation or negative tests were retained. No partial-failure, recovery, concurrency or provider-boundary behaviour was executed.
- **Educational documentation:** no class, method or README documentation was added. The mandatory [Implementation Guide][implementation-guide] was read after an initial source sketch had already been created; that sequencing deviation is disclosed. The sketch was removed before compilation or verification and is not represented as compliant implementation.
- **Architecture and security boundaries:** the removed sketch is not evidence of conformance. No code change remains that can be assessed for Themis, Mneme/Mnemosyne, Pylai, persistence, logging or module-isolation conformance. No security enforcement or authority model is claimed.
- **Known limitations:** the requested InformationUnit model, tests, documentation and Naming regression verification are all incomplete pending the dispositions above. The only verified repository change from this activity is the formal Task/Report/HCRL evidence; pre-existing user edits remain untouched.

## 4. Verification record

| Command/check | Result | Scope and interpretation |
| --- | --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **NOT RUN** | No code candidate exists and the affected model contract is blocked. Naming regression is therefore also unverified in this activity. |
| Solution-wide regression/integration and architectural conformance | **NOT RUN** | No integrated candidate was submitted; no waiver is implied. |
| Architecture selector | **NOT RUN** | No selector was invoked; the Backlog and pending HCRL identify its separate unresolved status. |
| Git source state | **PASS — inspection only** | `HEAD` remained `a2e68b9f871102c435dca4cc2583ea69d881dfe1` (tree `d1af1bb1069345c973a54e77729a616aed5f45cb`) on `feature/BL-0005-Calliope-InformationUnit`. After removing the source sketch, the observed pre-report worktree contained only the pre-existing Draft modification, untracked Backlog Item and this iteration's Task evidence. |

No build, test, application, publication or Git transition was attempted. PASS above describes only the read-only source-state inspection; it is not a software or architecture result.

## 5. Requested Solution Architect disposition

1. Decide the identity/classification conflict in DR-1 and establish whether an Architecture-target HCRL/ADD is required before this software task resumes.
2. Decide the `CacheLifecycleMetadata` / peer `OperationalMetadata` allocation in DR-2.
3. Specify how the explicit AX-06 authority reference required by Draft §1.5 is represented and governed, or direct that question to an appropriately bounded architecture assignment.
4. Identify the accepted qualified `RAW` Topic and release-defined vocabulary source for DR-4.
5. After the architecture matters are resolved, confirm the applicable verification treatment and the first Software 0.1.0 baseline/predecessor before candidate acceptance.

The assigned implementation remains **stopped**. This Report supplies no further implementation authority and no Architecture or software acceptance, release allocation, commit, push, merge, tag, publication or deployment authority.

[task]: ../../before/ai-agent-tasks/UNRESOLVED-bl-0005-calliope-informationunit-implementation-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md
[backlog]: ../../../../../../../docs/backlog/BacklogItem-0005-Calliope-InformationUnit.v000.001.md
[draft]: ../../../../../offline-design/DRP-Information-Unit-Framework.Draft.002.004.md
[g02]: ../../../../../offline-design/DRP-Calliope-Identity-and-Classification-Framework.Final.001.000.md
[hcrl-1.1.25]: ../../../../../register/1.1.25/hcrl-1.1.25-001.complete.md
[hcrl-1.1.28]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[hcrl-1.1.27]: ../../../../../review/1.1.27/hcrl-1.1.27-002.pending.md
[hcrl-010]: ../../../../../register/software-0.1.0/hcrl-software-0.1.0-010.complete.md
[hcrl-009]: ../../../../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md
[software-praxis]: ../../../../../../governance/software-development-praxis.md
[review-praxis]: ../../../../../../governance/architecture-review-praxis.md
[axioms]: ../../../../../../governance/architectural-axioms.md
[implementation-guide]: ../../../../../../../docs/implementation/Harmonia-Implementation-Guide-Philosophy.md