<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 architecture reconciliation — iteration 003

**Source state** · Architecture 1.1.26, branch `feature/BL-CALLIOPE-OO-01`, HEAD `fd52408e3d8be433bb43eedd2a448354646d7518`  
**Standing** · Open findings for Solution Architect disposition

The [iteration-002 reconciliation][previous] supplies R1–R6. The [continuation][continuation] governs [Draft.001.002][draft] for review. The completed [RAD 1.1.25 DR-3][dr3] confirms incorporation of the earlier Topic and TopicMask model. Its exact disposition establishes standing alongside the canonical elements and supersedes the older review qualification on the historical input. The review used bounded excerpts of those elements, the IU designation and the naming contract. At entry, the worktree included source-locator edits in the two common Topic elements and the IU Final DRP. Comparison with HEAD confirmed that those edits changed locators rather than the architectural clauses used below.

## Findings materially affected by the continuation

| Finding | Current contract and iteration-003 effect | Solution Architect disposition required |
| --- | --- | --- |
| **R1 · Runtime Topic identity and existing references** | The accepted Topic has a full UUID, persisted sequence numbers, missing-qualifier generation, registry-scoped unqualified name uniqueness and no Topic version. F-01.55 restores qualifier generation to the draft. The draft defines null/empty input and final positions precisely, validates generated qualifiers and requires explicit completed fields on parse. F-01.69–F-01.71 permit replacement during assembly while preserving completed ordering. These align in part with historical defaulting but leave UUID removal, sequence persistence, applicable-registry uniqueness, duplicate-input handling and per-entry version differences open. Equal unqualified projections can still denote distinct draft Topics. [Topic][topic] · [DR-3][dr3] | Determine correspondence between runtime typed-path identity and existing governed Topic definitions, UUIDs, aliases and references. Decide the registry uniqueness rule and which legacy ordered-input and qualification distinctions must survive, including collisions between generated and explicit `N<i>`. |
| **R2 · TopicMask compatibility and migration** | The accepted mask has a separate UUID, one name/canonical-name basis, Exact/Inclusive depth and default unrestricted intent, with no version axis. F-01.45 settles explicit universality in the draft, and F-01.46 specifies ordinary text escaping. F-01.57–F-01.63 preserve null, empty and explicit wildcard semantics. Exact/Inclusive correspond to ordinary containment false/true for a mask of the same depth. That partial correspondence leaves old name-only selectors, implicit unrestricted defaults, Java RegEx, reference identity and persisted forms open. [TopicMask][mask] · [DR-3][dr3] | Identify deliberate universal intent and approve any adapter representation and migration. Resolve old IDs, name-only masks, dialect differences and ambiguous/invalid inputs. Prove converted filters keep their approved scope. |
| **R3 · IU Topic designation** | IU authority uses its own UUID. The accepted IU designation is `harmonia.mneme.iu.<hex-string-of-compressed-uuid>` and the IU contract also has a distinct Topic UUID. The draft's strict parser cannot consume that unqualified path directly. Assembly defaults alone supply neither the appropriate typed qualifiers nor a mapping for the compression/format rule. The draft version field keeps its own meaning alongside the IU revision, Mneme operational revision/active generation, Content Structure Version and any native Content version. [IU class][iu-class] · [IU Final.002 §2][iu-final] · [Accepted DR-4][dr4] | Decide the qualified representation, status of the unqualified designation at each boundary and treatment of the legacy Topic UUID and references. Preserve the IU UUID and revision contracts. |
| **R4 · Governed naming and architecture identities** | Common DataObject identities are `9e2ffa9b4a1d4422a33c2a4bbcfc86ef` (Topic) and `bb489157a6bf48be926c5a601bb0a6c3` (TopicMask). The governing naming contract preserves an architectural `canonicalId` through rename and containment changes. The F-01 typed path can change runtime identity, and F-01.47 defines runtime display text from leaf values. The earlier Topic Identity DRP uses its own vocabulary and retains candidate standing. Identical accessor names convey no established mapping. [Naming governance][naming] · [Earlier candidate][candidate] · [DR-3][dr3] | Preserve the existing architectural IDs, names and relationships. Decide the runtime correspondence to governed properties and aliases and the standing of the earlier vocabulary. |

## Carried qualifications and consumer boundaries

- **R5 · Calliope dependency placement.** F-01 still selects RE2/J and Harmonia application logging. Compilation at entry construction and reconstruction reinforces the need to allocate those dependencies, while preserving Calliope-Models' restricted library boundary and consuming components' runtime responsibilities. The current POM's SLF4J and `themis-api` entries do not resolve that allocation. The latter remains a discrepancy with the stated module guardrail if relied on. [Calliope-Models][calliope] · [Repository guardrails][guardrails] · [Calliope POM][pom]
- **R6 · Logging under backpressure.** Prior requirements still apply. A shared logging path may delay a fail-closed Boolean result when the satellite buffer is saturated. Preserve the parallel console path, bounded buffer, producer backpressure, no intentional shedding and Log-Server receipt acknowledgement. Verify return behaviour, ERROR delivery, console independence, failure and recovery. An uncertain delivery stays uncertain. [Logging strategy][logging] · [Iteration-002 finding][previous]
- **Themis and IU security.** IU-side subject Topic requirements and TopicMasks participate in applicable policy. Themis determines access with the effective Passport/AuthorisationMap, action, compartment and other criteria. Kleio retains authoritative capture. Test invalid, absent and universal selection for scope preservation during any later compatibility work. [IU metadata][iu-meta] · [IU Controller][iu-controller] · [AX-07][ax07]
- **Subscription, publication and routing.** InformationUnitMask, OperationalMetadataMask and PetasosParcelMask compose neutral selectors. Feature-specific statements and routing criteria retain their own semantics. Confirm existing exact, descendant and unrestricted intent before adapting any authored selector. [DR-3][dr3] · [Implementation gaps](F-01-Implementation-Gap-Analysis.md)
- **Mneme and Mnemosyne discovery.** Existing adapter eligibility, declared capabilities, availability and union coverage across eligible instances remain separate from individual positive selector results. The qualified-name and mask migration dispositions also apply to registered criteria. [Mneme Adapter / Façade][facade] · [Mnemosyne Adapter (Store)][store]

## Axiom assessment

| Applicable axiom | Effect on the review candidate |
| --- | --- |
| [AX-04][ax04] | Harmonia defines positional and presence semantics. RE2/J provides expression execution. Its actual capabilities and permitted allocation require evidence. |
| [AX-06][ax06], [AX-07][ax07] | Keep classification and mask results separate from information authority and governed security decisions. |
| [AX-08][ax08] | Comparison exit points are operational diagnostics. Security-significant events follow Themis policy and Kleio capture authority. |
| [AX-10][ax10], [AX-11][ax11] | Bounded field and match work support load tolerance. Logging failure and backpressure still require evidence before dependent Mneme use. |
| [AX-14][ax14] | Preserve runtime identity versus architecture identity, Topic version versus IU revisions, explicit universal intent versus broad ordinary criteria, and absent versus empty expressions. Diagnostic categories preserve the difference between an expected mismatch and evaluation failure. |
| [AX-15][ax15], [AX-17][ax17] | Keep migration, dependency and delivery uncertainty visible. The candidate's internal consistency alone supplies no governing disposition. |

[previous]: ../../002/after/F-01-Architecture-Reconciliation.md
[continuation]: ../before/F-01-Topic-TopicMask-Adjudications-Continuation.md
[draft]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md
[dr3]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3
[dr4]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[topic]: ../../../../../continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content
[mask]: ../../../../../continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md#architectural-content
[iu-class]: ../../../../../continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content
[iu-final]: ../../../../offline-design/DRP-Information-Unit-Framework.Final.002.md#2-identity-and-distinct-revisions
[naming]: ../../../../../governance/canonical-identity-and-naming.md#architectural-identity
[candidate]: ../../../../offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md
[calliope]: ../../../../../continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md#architectural-content
[guardrails]: ../../../../../../AGENTS.md#2-system-taxonomy--module-hierarchy
[pom]: ../../../../../../calliope/pom.xml
[logging]: ../../../../offline-design/DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md#2-architectural-decisions-and-constraints
[iu-meta]: ../../../../../continuum/application/data-object/c05b7e37278141488ecc90b9a9ef96eb.md#architectural-content
[iu-controller]: ../../../../../continuum/application/application-function/ef864d509efa44a7a4160db2ca821163.md#architectural-content
[facade]: ../../../../../continuum/application/application-function/22bdac97fe0d48d1844f1e982d03d387.md#architectural-content
[store]: ../../../../../continuum/application/application-function/9c680d56a6b943da8ff22f3264e19bc7.md#architectural-content
[ax04]: ../../../../../governance/architectural-axioms.md#ax-04
[ax06]: ../../../../../governance/architectural-axioms.md#ax-06
[ax07]: ../../../../../governance/architectural-axioms.md#ax-07
[ax08]: ../../../../../governance/architectural-axioms.md#ax-08
[ax10]: ../../../../../governance/architectural-axioms.md#ax-10
[ax11]: ../../../../../governance/architectural-axioms.md#ax-11
[ax14]: ../../../../../governance/architectural-axioms.md#ax-14
[ax15]: ../../../../../governance/architectural-axioms.md#ax-15
[ax17]: ../../../../../governance/architectural-axioms.md#ax-17
