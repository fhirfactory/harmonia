Report-Produced-At: 2026-11-20T04:09:24.015541Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — A04 InformationProfile semantics and IU identity

The [operative follow-up][task] applied to the working-tree draft after the preceding A04 update. The repository was on `feature/BL-CALLIOPE-OO-01` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d` (`Software-Version-0.1.0-Snapshot-004`), with Architecture 1.1.26. [Preceding A04 report][prior] · [HCRL.004 DR-1][hcrl]

## Applied draft correction

- §7.2 records the independently assigned, immutable UUID as the IU's authoritative instance identity and the absence of an InstanceFDN. It records `InformationUnit.informationProfile` as the immutable ConceptFDN `solution=harmonia.domain=internal.concept=InformationUnit`. Its fixed value preserves explicit conceptual provenance regardless of implementation and refactoring. The constructor default remains a design observation.
- The single governed representation and IU lifecycle retain their established roles. `InformationRepresentation`, `ContainerMetadata`, `ContentMetadata` and `Content` carry representation characteristics. §11 reflects the profile's separate role.
- Where a Topic representation is required, §7.2 records `InformationUnit.informationProfile + InformationUnit.id` as the inputs to a stable deterministic projection. The IU UUID remains authoritative. G02 records the outstanding correspondence. [Design Note][note] · [G02 follow-up][g02]

## Architectural correspondence

The profile and stable projection can coexist with the accepted IU UUID. Adopting a projection for an existing Topic contract requires demonstrating correspondence to the accepted per-IU designation and distinct Topic UUID. F-01.78 preserves the unqualified designation and leaves its exact qualified path and boundary mappings open. F-01.76 provides for retention or explicit migration of required legacy UUID references outside the redesigned runtime Topic. Substituting the shared concept values for the accepted `mneme.iu` designation segments would change the contract. The scope of a normative `informationProfile` allocation, exact binding representations, and legacy UUID, alias and reference treatment remain for Solution Architect review. [Topic][topic] · [IU Class][iu] · [F-01.76/.78][f004] · [G02 follow-up][g02]

## Verification

- **PASS** · Compared against the preceding uncommitted Design Note. Its historical Topic paragraph and authority links are byte-for-byte preserved. The edits are limited to §7.2 and the corresponding §11 principle. The Task records the preceding section and principle. G02 retains the prior text and adds a follow-up section, traceability row and Task link. Checked 88 source paths and fragments, the unchanged index and HEAD, and preservation of the other 16 pre-existing untracked files. Repository changes are limited to the draft and Iteration 005 evidence.
- **PASS** · ArchUnit reported 90 tests with zero failures, errors or skips on OpenJDK 21.0.13.1 and Maven 3.9.12 across the 36-module dependency reactor in 30.078 seconds.
- **UNRESOLVED** · The original instruction-entry timestamp was unavailable. The Task retains the instruction and unresolved prefix for Solution Architect treatment before closure. HCRL.005 remains reserved, so the whole-iteration checker remains for that stage.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-calliope-a04-information-profile-semantics-task.md
[prior]: 20261120T040336Z-calliope-a04-informationunit-conceptfdn-clarification-report.md
[note]: ../../../../../../../docs/design/DesignNote-ConceptFDN-and-InstanceFDN.md#72-informationunit
[g02]: ../Calliope-G02-Architectural-Reconciliation-005.md#solution-architect-follow-up--informationprofile-and-iu-topic-projection
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate
[topic]: ../../../../../../continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content
[iu]: ../../../../../../continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content
[f004]: ../../../004/before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md#f-0176--r1-topic-identity-and-historical-references--accepted
