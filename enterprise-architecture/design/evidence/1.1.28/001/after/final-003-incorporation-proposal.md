<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Final.003 Continuum incorporation proposal

**Standing** · ADD candidate for Architecture Review. [Final.003][final] is
the accepted Topic and TopicMask design under the [operative disposition][task].
Its proposed Continuum correspondence and software realisation require their own
reviews. The current incorporated Architecture baseline is 1.1.26. The
[1.1.28/001 HCRL][architecture] and its exact previously reviewed candidate
remain the original reconciliation and provenance boundary for this proposal.

## Exact ADD input and correspondence

Use HEAD `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`, for the proposal's source
comparison. In an isolated exact copy, apply the [DR-5 locator patch][locator]
and then the [Final.003 ADD candidate][candidate]. The latter is a replacement
candidate for the [previously reviewed ADD][previous]. Its preimage is the
locator-corrected 1.1.26 source. Preserve the earlier ADD and dispositions as
review evidence. This sequence establishes a reproducible review candidate.
Use the actual accepted immediate predecessor for any later application.

The patch records each affected element's distinct Final.003 section scope
and pins the accepted source at SHA-256
`3902908dbffec9b35c15f3d31bf05b9b3a3e885dabecc71780c51fe4d9886043`.
The accepted offline DRP is the retained input, with the Task as its
attributable authority. Earlier source bindings, historical evidence and
wrapper contents retain their provenance.

| Existing canonical element | Final.003 correspondence |
| --- | --- |
| [Topic][topic] | Replace runtime UUID and compulsory sequence representation with immutable, ordered qualified identity, version-independent equality, hashing and ordering, opaque versions, strict Topic construction and reconstruction, and the accepted Naming surface. Preserve architectural identity and applicable registry authority. §§1–3, 5, 7–8. |
| [TopicMask][mask] | Record immutable ordinary/universal selection, raw structural identity, positional wildcard and version constraints, endpoint-only ranges, canonical/XML rules, bounds, fail-closed outcomes and neutral authority. Replace the old regex selector description on the existing directed association `id-1adf053e54b74ba8a15db8eec3760f06`. §§4–8. |
| [Calliope-Models][models] | Refine the existing library boundary for the six Naming types, factory-led builders, XML codec, typed validation and internal optional diagnostics. Preserve policy, audit, persistence and operational ownership and exclude a regex engine or logging infrastructure dependency. §§6–8. |
| [Logical InformationUnit][iu] and [conceptual Information Unit][iu-concept] | Distinguish the persistent IU UUID, the established unqualified IU designation and the Topic's qualified runtime identity. Reconcile the wording of existing association `id-14ff6e983e1e45ac81f7f181b5339c8c`. Keep qualified vocabulary, mappings and independent revision domains under their established decisions. §§1, 7. |
| [TopicIdentityStable][feature] | Preserve identifiers of the same governed architectural element. A changed qualified runtime path establishes different runtime identity. Keep current-authority reference rules and require affected consumers to use the current contract. §§1, 7. |
| [Mnemosyne Adapter (Store)][store] | Use the shared TopicMask against structured Topics and verify affected capability discovery consumers. Keep the existing five interfaces, complete-coverage criterion, eligible-instance selection, availability and Themis authority. §§4, 7. |

The proposal changes seven definitions and two association descriptions. It
preserves native types, canonical and Exchange identifiers, relationship
directions and endpoints, specialised Topic elements, navigation and Views.
The Java and XML interface details refine established library contracts in the
existing elements. The established application interfaces, dependency edges
and authority boundaries express the applicable topology. Naming introduces
no new deployment, transport, operational logging or public diagnostic
interface. [Incorporation Praxis][incorporation-praxis] · [Continuum representation][representation]

Read the candidate against [AX-04, AX-06–AX-12, AX-14–AX-15 and AX-17][axioms].
Harmonia owns Naming semantics, with bounded platform string operations,
strict XML and fail-closed outcomes. Selection conveys no authority.
Optional transient diagnostics are independent of typed validation and
mandatory audit, which follows the applicable Themis and Kleio responsibilities.
The proposal preserves each identity domain, provenance and the standing of
unresolved release and implementation work.

## Disposition required for incorporation

- **R1/R2 and dependent definitions.** The current 1.1.25 common-model
  statements and prior 1.1.28 DR-1–DR-4 review include runtime UUID,
  positional RE2/J or older governed-reference qualifications in their
  respective scopes. Review the replacement exact candidate before
  application. Carry compatible findings and ownership forward, resolve
  superseded engine and selector semantics explicitly, and assess any
  independently governed live registry, persistence or interface contract
  at its owning boundary. Coherent pre-1.0.0 refactoring provides no legacy
  runtime service and does not waive active data obligations.
- **Source and release path.** Record which transition carries the precise
  DR-5 repair and the new ADD. Preserve [1.1.27/002][archunit] as the separate
  review identified in DR-6, and establish the actual accepted predecessor,
  release sequence and exact gate treatment. The review must confirm the
  incorporated change and its validation before completion under the
  [Review Praxis][review-praxis] and [release policy][release-policy].
- **Software Development Praxis.** Retain the exact [pre-1.0.0 amendment][praxis-proposal]
  for a reviewed governance incorporation. Decide whether it shares the
  authorised Architecture transition. The accepted design already requires
  scoped consumer refactoring. The common Praxis amendment has its own
  governance approval and release correspondence.
- **Verification.** Recheck canonical source against the applicable
  predecessor. Exercise canonical validation, projection identity and
  reference checks and any applicable structural review and conformance
  gates. Confirm architecture test treatment, dependent verification and
  whole-result standing explicitly. Candidate validation and projection
  support review. Authorised application and acceptance remain separate.

## Follow-on software disposition

Propose the next accepted revision of [Backlog Item 0003][backlog] and a
separate authorised assignment against Final.003. Keep the current revision
and the [Software 010][software] implementation and review evidence bound
to their exact RE2/J candidate.

| Area | Recommended follow-on |
| --- | --- |
| Work scope and acceptance criteria | Identify actual direct and indirect users, codec and fixtures, persistence/interchange and build effects. Refactor all affected authorised consumers. Preserve Topic and builder invariants under AC-1, AC-2 and AC-5. Rebase AC-3–AC-4 on raw constraint identity, exact/wildcard/numeric behaviour and endpoint-only ranges. Update AC-6–AC-9 for strict canonical/XML fixtures, lexical and resource bounds, absence/present-empty distinction, early depth limits and safe failures. |
| API, diagnostics and verification | For AC-10–AC-13, verify immutable concurrent reuse, accepted packaging and public surface, disabled and throwing diagnostic paths, dependency removal and accurate implementation documentation. Require the exact build candidate, component and applicable integration and regression results, and independent conformance review. Keep logging delivery obligations in their governed scope. |
| Software 010 DR-1–DR-3 | Seek explicit disposition of the existing candidate and its engine-specific findings. Preserve those findings in the original review. A successor follows Final.003's wildcard rules and version constraints. Carry strict XML reconstruction, accurate boundary metadata, early depth validation and API/diagnostic containment into that successor. The design decision establishes the required public surface; the exact code requires verification. |
| Software 010 DR-4–DR-5 | Preserve the existing candidate test accounting and evidence qualifications. Reconcile actual test counts, repeat applicable checks on the exact successor, and obtain explicit treatment of unexecuted required checks and DR-6 dependencies. Preserve the original Junie evidence and route its event-format and unavailable timestamp treatment through the owning review. Establish first software acceptance through its own confirmed result and release scope. |

[task]: ../before/ai-agent-tasks/20261124T222614Z-topic-drp-final-003-acceptance.md
[final]: ../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[architecture]: ../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[locator]: 01-retained-topic-input-locator.patch
[previous]: 02-topic-topicmask-continuum.patch
[candidate]: final-003-continuum-add.proposed.patch
[praxis-proposal]: software-development-praxis-pre-1-0-0.proposed.patch
[archunit]: ../../../../review/1.1.27/hcrl-1.1.27-002.pending.md
[incorporation-praxis]: ../../../../../governance/continuum-incorporation-praxis.md
[representation]: ../../../../../governance/continuum-representation.md
[review-praxis]: ../../../../../governance/architecture-review-praxis.md
[release-policy]: ../../../../../governance/architecture-release-policy.md
[axioms]: ../../../../../governance/architectural-axioms.md
[topic]: ../../../../../continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md
[mask]: ../../../../../continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md
[models]: ../../../../../continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md
[iu]: ../../../../../continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md
[iu-concept]: ../../../../../continuum/business/business-object/10284034c8534b639284386b064f2c54.md
[feature]: ../../../../../continuum/strategy/capability/feature/9210349f026b4ff1aca1092c309b4139.md
[store]: ../../../../../continuum/application/application-function/9c680d56a6b943da8ff22f3264e19bc7.md
[backlog]: ../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
[software]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
