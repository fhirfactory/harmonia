<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 architecture reconciliation

- **Review scope** · Topic and TopicMask design consolidation, software-0.1.0 / 002
- **Architecture source state** · Release 1.1.26, inspected at `fd52408e3d8be433bb43eedd2a448354646d7518`
- **Standing** · Findings and requests for Solution Architect disposition

The [F-01 discussion adjudications][f01] control the content of [Draft.002][draft]. The current Continuum contracts retain their architectural authority. In particular, the historical Final.001 cover describes its standing at the time of authoring, and the completed RAD 1.1.25 DR-3 subsequently confirms its bounded incorporation and preserves existing Topic elements and relationships. The common model element headers retain historical qualifications. Read those headers with the exact completed disposition. [Accepted DR-3][dr3]

## Conflicts requiring disposition before dependent incorporation

| Finding | Current authority and Draft.002 difference | Disposition requested |
| --- | --- | --- |
| **R1. Topic instance identity and canonical names** | The accepted model uses a full Topic UUID distinct from names, explicit persisted sequence numbers, a generated missing qualifier, applicable-registry uniqueness of the unqualified `Topic.name`, and no Topic version. F-01.1–F-01.4, F-01.22–F-01.26 and F-01.29 use the typed non-versioned path for identity, preserve order as data without requiring sequence keys, generate the missing value, and allow optional per-entry versions. Distinct qualified paths can have the same unqualified projection. Existing UUID references, registry collisions and name aliases can therefore require different treatment. [Topic][topic] · [Accepted DR-3][dr3] · [TopicIdentityStable][stable] · [AliasAmbiguityExposed][aliases] | Confirm the disposition of existing runtime IDs and historical references, the relationship between the registry's uniqueness rule and typed identity, and the treatment of old names and aliases. Establish separately which rules apply to existing governed Topic definitions. |
| **R2. TopicMask identity and selection** | The accepted model specifies an immutable selector UUID, a single name-based or canonical-name basis, an Exact/Inclusive choice, default unrestricted intent and no version axis. F-01.7–F-01.19 and F-01.36 specify structural equality, one field-expression model, optional version criteria, exact depth by default, explicit universality and RE2/J full-field matching. Exact/Inclusive map semantically to containment false/true for ordinary masks of the same depth. That mapping alone cannot preserve old name-only masks, unrestricted defaults, UUID references or ad hoc regex behaviour. [TopicMask][mask] · [Accepted DR-3][dr3] | Decide how authored and stored selectors and references are to be reviewed. Confirm which legacy inputs count as deliberate universal selection, and establish the rejection or migration rules for ambiguous and legacy-dialect entries. Approve the encoding of explicit universality separately. |
| **R3. IU Topic designation** | The IU contract has its own governed UUID and a Topic named `harmonia.mneme.iu.<hex-string-of-compressed-uuid>`. The IU framework also gives that Topic a separate UUID. The unqualified dot path cannot be consumed as Draft.002's strict qualified `qualifier=value` identity text without an explicit mapping. The reviewed sources do not establish that mapping or exact compression encoding. TopicEntry version has its own meaning alongside the IU revision, Mneme operational revision/active generation, Content Structure Version and optional native Content version. [Topic][topic] · [IU Class][iu-class] · [IU Final.002 §2][iu-final] · [Accepted DR-4][dr4] | Decide the qualified representation, treatment of existing IU Topic UUIDs and references, and the standing of the existing unqualified designation at each boundary. Preserve the IU UUID and revision contracts in that disposition. |
| **R4. Architecture identities and previous vocabulary** | Common logical DataObject identities are `9e2ffa9b4a1d4422a33c2a4bbcfc86ef` (Topic) and `bb489157a6bf48be926c5a601bb0a6c3` (TopicMask). The Continuum assigns `canonicalId` and governed naming properties to architecture resources, including existing specialised Topic elements. The earlier candidate Topic Identity DRP uses `canonicalId` for an identity stable across rename or containment changes and assigns different roles to canonical/friendly/display names. F-01 names and model equality concern the shared runtime values. [Canonical Identity and Governed Naming][naming] · [Topic][topic] · [TopicMask][mask] · [Earlier candidate][candidate] · [Accepted DR-3][dr3] | Preserve the existing architectural element identities, names and relationships. Determine the runtime correspondence and record the standing of the candidate vocabulary during separately authorised reconciliation. |

## Consumer dependencies

| Boundary | Retained rule and evidence required |
| --- | --- |
| **Themis and IU security** | IU SecurityContext supplies subject-side required Topics and TopicMasks. The authenticated identity's effective Passport/AuthorisationMap, applicable action, compartment and other criteria, and Themis enforcement determine access. Positive or universal match is only a selector result. For any later adaptation, verify that invalid or missing criteria and legacy pattern conversion cannot widen access or bypass policy. Preserve Kleio capture authority and keep routine matching diagnostics in the operational logging boundary. [IU metadata][iu-meta] · [Themis][themis] · [IU Controller][iu-controller] · [AX-07][ax07] |
| **Subscriptions, publication and routing** | The shared discriminator is reused for Topic-valued properties in `InformationUnitMask`, `OperationalMetadataMask` and `PetasosParcelMask`. Publication, subscription and filter statements compose criteria under their own feature semantics. Routing remains a consumer consequence. The Topic model grants no dispatch or broker-destination semantics. Review any held criteria, especially one-level versus descendant scope and existing default/wildcard intent. The IU framework defers detailed PetasosParcel model and publication/subscription mechanics. [TopicMask][mask] · [Accepted DR-3][dr3] · [IU Final.002][iu-final] |
| **Mneme and Mnemosyne capability discovery** | The Mnemosyne-side Mneme Adapter / Façade declares supported IU types. The Mneme-side Mnemosyne Adapter (Store) uses the shared mask and selects eligible available instances. Required types must be covered by the union of supported criteria across eligible available instances. One positive match does not establish coverage, availability, IU ownership or disclosure authority. Confirm correspondence for registered criteria and the qualified-name/version contract before applying F-01 to discovery. [Mneme Adapter / Façade][facade] · [Mnemosyne Adapter (Store)][store] |

## Logging and dependency qualifications

- **R5. Application dependency boundary.** F-01 selects RE2/J and Harmonia application logging. The Calliope-Models dependency row lists JDK and HAPI FHIR structures and excludes higher layers. The task decision specifies engine and logging behaviour, with allocation unresolved. Confirm the permitted RE2/J and application-logging dependency placement. The current POM's SLF4J dependency supplies no architecture precedent. Its `themis-api` dependency is an existing discrepancy with the stated layer rule that also requires disposition if relied on. The logging DRP leaves the application backend, transport and persistence technologies open. [Calliope-Models][calliope] · [Repository rules §2][guardrails] · [Calliope POM][pom] · [F-01.18, F-01.39][f01] · [Logging strategy][logs]
- **R6. Logging under backpressure.** The strategy uses parallel console and in-JVM satellite paths. It calls for a bounded memory buffer, producer backpressure at exhaustion, no intentional event shedding and forwarding responsibility through Log-Server receipt acknowledgement. F-01 requires a Boolean false and ERROR diagnostic for a runtime evaluation failure. A blocked shared logging path may delay completion. An unavailable sink may leave delivery uncertain. Verify saturation, failure, recovery, console independence and the outcome/delivery contract using the sanctioned integration. No queue, drop policy, timeout, durability guarantee or alternative audit channel is inferred here. rsyslog is the strategic Log-Server choice. [Logging strategy §2][logs] · [Satellite capability][satellite] · [F-01.37–F-01.40][f01]

## Applicable Axioms

| Axiom | Consequence for the review candidate |
| --- | --- |
| [AX-04][ax04] | Harmonia defines positional matching and validation; RE2/J supplies regex execution. Establish feasible compile and evaluation controls from the pinned library and measurements. |
| [AX-06][ax06], [AX-07][ax07] | Classification and matching leave information authority and Themis policy decisions at their established boundaries. |
| [AX-08][ax08] | A comparison exit is ordinarily diagnostic. Security-significant capture follows policy and Kleio authority. |
| [AX-10][ax10], [AX-11][ax11] | Bound mask depth and field work, and demonstrate how the logging failure/backpressure path preserves outcomes under load before relying on it for Mneme access. |
| [AX-14][ax14] | Preserve Topics, versioned representations, IU revisions, and diagnostic versus audit meaning. Retain the distinction between an ordinary mismatch and an evaluation failure in the diagnostic contract even when both return false. |
| [AX-15][ax15], [AX-17][ax17] | Preserve unknown log-delivery outcomes, unverified limits and the unresolved reconciliation findings. The accepted current architecture continues to govern dependent activity pending disposition. |

[f01]: ../before/F-01-Topic-TopicMask-Adjudications.md
[draft]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md
[dr3]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3
[dr4]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[topic]: ../../../../../continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content
[mask]: ../../../../../continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md#architectural-content
[stable]: ../../../../../continuum/strategy/capability/feature/9210349f026b4ff1aca1092c309b4139.md#architectural-content
[aliases]: ../../../../../continuum/strategy/capability/feature/dd48fda835c54996b4d932c8fe14b641.md#architectural-content
[iu-class]: ../../../../../continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content
[iu-final]: ../../../../offline-design/DRP-Information-Unit-Framework.Final.002.md#2-identity-and-distinct-revisions
[iu-meta]: ../../../../../continuum/application/data-object/c05b7e37278141488ecc90b9a9ef96eb.md#architectural-content
[naming]: ../../../../../governance/canonical-identity-and-naming.md#architectural-identity
[candidate]: ../../../../offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md
[themis]: ../../../../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#architectural-content
[iu-controller]: ../../../../../continuum/application/application-function/ef864d509efa44a7a4160db2ca821163.md#architectural-content
[facade]: ../../../../../continuum/application/application-function/22bdac97fe0d48d1844f1e982d03d387.md#architectural-content
[store]: ../../../../../continuum/application/application-function/9c680d56a6b943da8ff22f3264e19bc7.md#architectural-content
[calliope]: ../../../../../continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md#architectural-content
[guardrails]: ../../../../../../AGENTS.md#2-system-taxonomy--module-hierarchy
[pom]: ../../../../../../calliope/pom.xml
[logs]: ../../../../offline-design/DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md#2-architectural-decisions-and-constraints
[satellite]: ../../../../../continuum/strategy/capability/659d34c0e1194cdeaf5c555b58f48100.md#architectural-content
[ax04]: ../../../../../governance/architectural-axioms.md#ax-04
[ax06]: ../../../../../governance/architectural-axioms.md#ax-06
[ax07]: ../../../../../governance/architectural-axioms.md#ax-07
[ax08]: ../../../../../governance/architectural-axioms.md#ax-08
[ax10]: ../../../../../governance/architectural-axioms.md#ax-10
[ax11]: ../../../../../governance/architectural-axioms.md#ax-11
[ax14]: ../../../../../governance/architectural-axioms.md#ax-14
[ax15]: ../../../../../governance/architectural-axioms.md#ax-15
[ax17]: ../../../../../governance/architectural-axioms.md#ax-17
