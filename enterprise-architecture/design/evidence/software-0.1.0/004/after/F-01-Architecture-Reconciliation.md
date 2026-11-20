<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 architecture reconciliation — iteration 004

- **Source** · Architecture 1.1.26, `feature/BL-CALLIOPE-OO-01`, HEAD `fd52408e3d8be433bb43eedd2a448354646d7518`
- **Standing** · R1–R6 dispositioned for [Draft.001.003][draft] by the [Solution Architect][decisions]. The draft awaits review.

The [iteration 003 reconciliation][previous] records the baseline and original conflict detail. RAD 1.1.25 [DR-3][rad] and [DR-4][rad-revisions] and the canonical elements establish the accepted incorporated baseline. The accepted continuation governs this bounded draft. It does not apply those differences to the Continuum or the current software candidate.

| Finding | Decision and remaining architectural correspondence |
| --- | --- |
| **R1 · Runtime Topic identity and references** | **F-01.76 accepted.** The draft's non-versioned qualified path replaces UUID identity within the proposed model. Assembly indices preserve ordering and lack independent identity standing. The accepted [Topic element][topic] still specifies UUID, retained sequence numbers, uniqueness within the applicable registry and no Topic version. Preserve references and map registry rules, ordered inputs and collisions before dependent migration. |
| **R2 · TopicMask migration** | **F-01.77 accepted.** Structural selectors, explicit universality, conditional depth-rule equivalence and fail-closed RE2/J migration govern the draft. The current [TopicMask element][mask] still records UUID, separate name/canonical bases, historical unrestricted intent and no version axis. Verify persisted and runtime consumer scope before migration or any accepted-baseline change. |
| **R3 · IU Topic designation** | **F-01.78 accepted at the representation level.** The existing designation remains the unqualified projection. The [IU Class][iu-class] and [Final.002 identity contract][iu-final] retain independent IU identity, separate revision concepts and the current distinct Topic UUID contract. Govern exact qualifiers and approved boundary mappings separately, including legacy Topic UUID references. No qualifier sequence follows from the observed factory. |
| **R4 · Architectural identity and naming** | **F-01.79 accepted.** Preserve existing element IDs, canonical identities, naming properties, relationships and historical references. These include `9e2ffa9b4a1d4422a33c2a4bbcfc86ef` and `bb489157a6bf48be926c5a601bb0a6c3`. A runtime path change can change runtime identity while architectural rename or containment change preserves `canonicalId`. Record explicit correspondence where applicable. The earlier [Topic Identity DRP][earlier] retains candidate standing. [Naming governance][naming] |
| **R5 · Calliope dependencies** | **F-01.80 accepted.** Use a direct RE2/J dependency in the matching module and the existing logger, preserving neutral models and [Calliope-Models][calliope] responsibilities. The rule does not establish physical placement, feature compatibility or logger availability. The existing [Calliope `themis-api` declaration][pom] remains a separate module-governance concern. |
| **R6 · Backpressure and diagnostic delivery** | **F-01.81 accepted.** Synchronous Boolean fail-closed matching and routine TRACE with no remote synchronous-delivery requirement preserve the framework's buffer, acknowledgement and recovery responsibility. Bounded blocking, console independence and reliable ERROR outcomes require measured evidence. The [logging strategy][logging] establishes the server-receipt boundary; logger or sink uncertainty and competing reliability/latency requirements require explicit treatment. |

The design differences in R1–R3 require governed alignment or explicit treatment before an implementation reliant on them can be accepted. R4 preserves the accepted naming and identity contract. R5–R6 allocate responsibilities while leaving implementation conformance open. [Implementation obligations](F-01-Implementation-Gap-Analysis.md)

## Applicable Axioms

| Canonical Principles | Effect in this draft |
| --- | --- |
| [AX-04][ax04] | Harmonia defines field and selection semantics. RE2/J supplies execution, and the shared logging framework supplies delivery. |
| [AX-06][ax06] · [AX-07][ax07] · [AX-08][ax08] | Topic identity and neutral selection convey no information authority. Themis governs policy and Kleio governs authoritative audit capture; comparison traces remain operational diagnostics. |
| [AX-10][ax10] · [AX-11][ax11] | Bounded resource use and fail-closed matching preserve semantics under load and failure. Measure shared logging's impact on responsiveness, including for Mneme consumers. |
| [AX-14][ax14] · [AX-15][ax15] · [AX-17][ax17] | Keep separate identity, version, selector and outcome domains. Keep unknown delivery, migration scope, missing qualifier vocabulary and accepted-baseline discrepancies visible for governed treatment. |

## Existing evidence locator exception

The default Python initially lacked `jsonschema`. After loading the declared Python dependencies from local cached wheels, the canonical validator returned **FAIL** on a missing retained evidence path. Both common Topic/TopicMask elements' pre-existing worktree edits refer to `DRP-Topic-and-TopicMask.Final.001.000.md` under the RAD 1.1.25 accepted-input directory. The retained copy's actual basename is [DRP-Topic-and-TopicMask.Final.001.md][retained]. The validator stopped at that prerequisite. The worktree paths and historical evidence were preserved as supplied. Obtain explicit Solution Architect disposition before correcting the locator or relying on canonical validation for a dependent activity.

[previous]: ../../003/after/F-01-Architecture-Reconciliation.md
[decisions]: ../before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md
[draft]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md
[rad]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3
[rad-revisions]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[topic]: ../../../../../continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content
[mask]: ../../../../../continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md#architectural-content
[iu-class]: ../../../../../continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content
[iu-final]: ../../../../offline-design/DRP-Information-Unit-Framework.Final.002.md#2-identity-and-distinct-revisions
[naming]: ../../../../../governance/canonical-identity-and-naming.md#architectural-identity
[earlier]: ../../../../offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md
[calliope]: ../../../../../continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md#architectural-content
[pom]: ../../../../../../calliope/pom.xml
[logging]: ../../../../offline-design/DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md#22-agreed-delivery-semantics
[retained]: ../../../1.1.25/001/before/accepted-input/DRP-Topic-and-TopicMask.Final.001.md
[ax04]: ../../../../../continuum/motivation/principle/54508cda80484a6caf95b901780eaf7d.md#architectural-content
[ax06]: ../../../../../continuum/motivation/principle/952c3b6148ce4fce94b69f99a0c2fec7.md#architectural-content
[ax07]: ../../../../../continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md#architectural-content
[ax08]: ../../../../../continuum/motivation/principle/21d6864657964ba5b03e3f6b5eb29cd5.md#architectural-content
[ax10]: ../../../../../continuum/motivation/principle/0d9673833eb840eab03804aa076c1b0c.md#architectural-content
[ax11]: ../../../../../continuum/motivation/principle/8a1e995f16954484a78ae1c5c114d546.md#architectural-content
[ax14]: ../../../../../continuum/motivation/principle/d3fc6025dc7646d1b2020b51f7a44bfa.md#architectural-content
[ax15]: ../../../../../continuum/motivation/principle/ef8abbaec37e4890ad78b9f263e67906.md#architectural-content
[ax17]: ../../../../../continuum/motivation/principle/architectural-axiom/8882573df7ce43c6bc0f66da0a676139.md#architectural-content
