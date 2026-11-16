<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Independent engineering assessment — Calliope OO first increment

Preparation evidence for [HCRL 1.1.27/001](../../../../review/1.1.27/hcrl-1.1.27-001.pending.md)
and [BL-CALLIOPE-OO-01 revision 1](../../../../../../docs/backlog/calliope-oo-01-topic-models.md).
Proposed work only; no implementation acceptance or refactoring authority.

## Inputs and limits

Inspected commit `dc30cfbcc7ad4113b29e3710904453c023df4dc1`, branch
`Mneme-and-Mnemosyne-Build`, initially clean; accepted Architecture release 1.1.26.
Read the current root/docs agent instructions, accepted backlog template/Praxis,
Implementation Guide, common review/provenance/release contracts, relevant canonical
models/axioms and completed HCRLs 1.1.25/001 and 1.1.26/001. Accepted common model
incorporation is distinguished from qualified Domain05 source contributions.

The user's supplied governance investigation located the original Junie Phase 1
review. Read that original technical result, retained as an
[unaltered source excerpt](../before/junie-phase1-review.source.md), rather than
substitute the later investigation for its recommendations. The
[source inventory](../before/source-state.json) pins its locator/excerpt and the
current production files. This is a current capture of an investigation input,
not a retrospective Task/Report or acceptance of that earlier activity. No wholesale
historical governance audit/recovery is commissioned by this task.

## Findings and selected priority

The independent inventory finds **69** production Java files, **10** above 500 full
physical lines. Junie's result says 47 files, although its package breakdown totals
69; several quoted file sizes also differ by one line. The retained inventory uses
`bytes.splitlines()` consistently, including documentation/support declarations:

| Source under `calliope/src/main/java/net/fhirfactory/harmonia/` | Lines |
| --- | ---: |
| `model/ergon/ErgonPayload.java` | 952 |
| `model/praxis/PraxisDefinition.java` | 879 |
| `model/pragma/Pragma.java` | 874 |
| `model/core/information/InformationUnit.java` | 854 |
| `model/topic/Topic.java` | 736 |
| `model/persistence/PersistenceOperationEnvelope.java` | 701 |
| `model/topic/TopicMask.java` | 672 |
| `model/core/information/OperationalMetadata.java` | 647 |
| `model/core/information/ContainerMetadata.java` | 606 |
| `model/pragma/PragmaFhirConverter.java` | 565 |

Choose **Topic/TopicMask responsibility decomposition and construction conformance**
as the first increment. These are one coherent foundational naming/selection concern,
with accepted semantics, real consumers and existing tests. The item bounds work to
those models/support and preserves the public compatibility surface. This avoids
simultaneous churn across task models, metadata, persistence and package names.
Starting with the largest file would not establish a more coherent dependency or
an architectural priority. IU metadata/representation and compatibility facades
need a separate assessment/increment; delegation there is not inherently a defect.

Direct source review confirms:

- Topic combines immutable identity/entries, validation and derived names with
  parsing, HL7/FHIR/egress/IU factories, routing compatibility, nested Entry and
  Builder. Its builder's explicit-position overload uses `entries.put` (line 685
  in the inspected source), erasing an earlier supplied position. Both explicit
  overloads reach this path. This contradicts the accepted rejection rule.
- The [read-only duplicate-position probe](duplicate-position-probe.json) executed
  two additions at position zero and built `NewZealand` after initially supplying
  `Australia`; no rejection occurred. The source launcher warned about absent
  Jackson annotation classes in its intentionally small classpath; the probe does
  not exercise Jackson. Existing JSON unit checks ran through Maven separately.
- TopicMask combines definition validation, regex preparation/cache access,
  parsing/factories, matching and nested support. Its existing matching respects
  element boundaries and Exact/Inclusive depth. Cache fallback paths exist; no
  assertion that Java deserialisation is broken is made. Its constructor selects
  one basis and discards the unused map; new rules for conflicting constructor
  inputs/defaults are not silently included in this increment.
- Full-file size and incomplete public-operation documentation are engineering
  concerns under Guide §§4, 7. Extraction must preserve meaningful documentation
  and show responsibility improvement, not just move enough lines to pass.

The [explicit-import inventory](consumer-inventory.json) finds 59 production and
36 test source files importing Topic/TopicMask (95 total; same-package/fully qualified
references excluded). Inspecting representative users shows legacy Topic factories
and five-tuple constructors, MLLP destination selection, Praxis subscriptions and
workflow dispatch, IU/security selectors and TopicSubscription. This is enough
consumer breadth to justify stable facades and integrated testing; it is not a
complete integration coverage claim.

## Independent treatment of Junie's recommendations

| Phase 1 recommendation / premise | Assessment for this item |
| --- | --- |
| Decompose oversized multifunctional files. | Agree with the concern; select the cohesive Topic/Mask pair first. No credible basis is supplied for estimated final sizes or a new 350-line ceiling. All other oversized files remain disclosed work, not waived. |
| Move Topic/Mask/Subscription into `model.core.naming`, and align Continuum `friendlyName`. | Defer public migration. Guide §7.2 explicitly calls package names examples. Canonical Topic/Mask are logical DataObjects, not prescribed Java Classes; their friendlyName strings do not mandate Java packages. No Continuum edit follows merely from package preference. |
| Absence of `datatypes`, `valuesets`, `factory` is a violation. | Overstated: supporting packages SHOULD be used where applicable and must be justified. Choose cohesive support objects/packages during implementation; create no empty catalogue. |
| Extract all nested types/remove forwarding aliases. | Assess responsibilities and consumers individually. Preserve public nested type identities and valid contracts. Thin delegation can protect encapsulation; getters are not automatically a Law of Demeter violation. |
| Use deprecated subclasses as transition wrappers. | Topic/TopicMask and their nested support types are final. Such wrappers are not a mechanical compatibility solution. Retain the public facade and delegate where justified. |
| Consolidate Pragma/Ergon, rename governedwrite, reorganise workflow/persistence/security. | Separate future work. Package names alone neither establish runtime ownership nor justify a semantic merger. Check each accepted contract before a future bounded assignment. |
| A matching ArchUnit package prefix proves compatibility. | Prefix matching alone does not establish consumer, serialisation or architectural conformance. The current rule also permits Themis API while the root guardrail is broader; see the discrepancy below. |
| Module tests followed by `mvn test` establish readiness. | Actual scope/environment matters. Use real reactor paths; some named Phase 1 targets, such as `hestia/mneme-cache` and `hestia/mneme-rest`, do not exist. Apply the accepted solution-wide regression/integration requirement; Maven phase names alone are insufficient. |

Working native Content manifestations are explicitly permitted by the accepted
IU representation model. They are not automatically a portability violation to
remove during OO cleanup. PetasosParcel/Framework redesign remains deferred by
1.1.25/001 DR-6 until the operational Mneme/Mnemosyne 0.5 gate; no such work is
included. Architecture-only Java/test deferrals do not waive future software gates.

## Material discrepancies and adjudication boundaries

**Established implementation discrepancy:** Topic's duplicate-position builder
behaviour conflicts with the accepted common Topic contract and its own class
documentation. The proposed correction implements existing authority. No Topic
rename, identity redesign or new architectural element is needed for it.

**Pre-existing dependency-authority question:** the root Calliope taxonomy prohibits
higher-layer dependencies including Themis, while
[calliope/pom.xml](../../../../../../calliope/pom.xml) directly depends on
`themis-api` and Calliope security/task classes import Themis API types.
[PackageLayeringArchitectureTest](../../../../../../paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java)
prohibits `themis.core`, allowing the API dependency. No explicit exemption was
located in the bounded current-governance/accepted-register search. Passing that
test supplies no guardrail exception. Request clarification of the existing API
exception/authority, or a governed resolution; do not move contracts, alter POMs or
weaken the test under this item. This does not obstruct preparation or unaffected
naming-model work. Preserve its actual relevance when later judging solution
conformance; no automatic software-release waiver is asserted.

**Pre-existing Topic authority tension:** the accepted definition of
[TopicIdentityStable, E04.01.04.F01](../../../../../continuum/strategy/capability/feature/9210349f026b4ff1aca1092c309b4139.md)
says accepted identities survive rename/reclassification. The later accepted common
[Topic definition](../../../../../continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md)
and 1.1.25/001 DR-3 require immutable definition and a different UUID for substantive
replacement, while preserving existing specialised Topic identities. Whether the
older Feature concerns a distinct scope of rename is not established by this
inspection. Request explicit scope clarification/reconciliation before any dependent
rename or identity migration. Do not silently rewrite the Feature or weaken the
immutable contract. This item performs no rename/reclassification or identity-policy
change, so that unresolved distinction need not hold up its unaffected engineering.

No package-location adjudication, common base class, new task taxonomy, registry,
IU UUID encoding standard or runtime responsibility change is required to implement
the proposed bounded item. If such a need is discovered later, escalate before
dependent work through ADD/HCRL under AX-17.

## Verification standing

[Existing naming-model tests](topic-tests.json): **PASS**, 23 tests, no failures,
errors or skips. [Mandatory architecture suite](architecture-tests.json): **PASS**,
90 tests, no failures, errors or skips. Both used bounded offline Maven execution
on Java 21 against unchanged implementation/test sources. The duplicate probe confirms
a defect not covered by the passing existing tests.

Backlog structure/metadata, local links, review/provenance correspondence, source
preservation and whitespace checks are recorded in the accompanying validation
evidence. The new Task instruction-entry time and carrier-follow-up event time/
filename correspondence remain **UNRESOLVED**; the iteration checker must retain
that result. No solution-wide software regression/integration run, accepted
implementation, release allocation or Git transition is claimed for this authoring
task. All review dispositions remain for the Solution Architect.
