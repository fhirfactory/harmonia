<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# DR-18/DR-19 — Mneme post-creation availability reconciliation

**Standing:** candidate amendment, not acceptance. Component-level review concluded
with the six recommendations below. Consolidated Solution Architect review remains
pending; no DR/HCRL closure, version advance or accepted publication is authorised.

## Authority and originating state

The [operative follow-up Task](../../before/ai-agent-tasks/unresolved-mneme-post-create-availability-amendment.md)
is the available attributable record of the review findings and exact agreed
amendment. Its original instruction-entry event/prefix is UNRESOLVED; no current,
capture, file or Report time is substituted. Common Governance, Review Praxis,
Change Provenance, Continuum Representation/Incorporation and the existing projection/
Round-Trip contracts apply without a new process or iteration.

Current canonical Markdown is the baseline. Its originating uncommitted candidate
has 1,996 elements / 2,246 relationships / 1,288 Views and exact XML SHA-256
`13b0c5cf52d092e70a9f57e6d95a8ebc1d3fd712dfb7c60dfb92b6c38d7ecebf`.
[Exact retained preimages and state](../../before/mneme-post-create-availability/source-state.json)
identify the unique uncommitted Mneme/HCRL input and previous candidate evidence.
The preceding Git HEAD alone does not contain this candidate. Earlier Task/Report
production events, recommendations, dispositions, checks and failures are preserved.

## Six definition review dispositions

These are the Task's architectural review recommendations, not formal Solution
Architect candidate acceptance:

| Definition | CanonicalId | Review disposition | Amendment treatment |
| --- | --- | --- | --- |
| Provenance — DataObject | e282811b51274649816566aac7edeb8a | Recommended acceptance unchanged | Exact source bytes and all reference/Assurance/persistence qualifications preserved. |
| Provenance Client Library — ApplicationComponent | 171f05d188384cedb7d5c493eeaee519 | Recommended acceptance unchanged | Exact source bytes; no contract or runtime choice added. |
| Dokimasia — ApplicationComponent | d739c75dffe2453081735710b285f03b | Recommended acceptance unchanged | Exact source bytes; framework/lifecycle/independent Assurance role intact. |
| Kleio — ApplicationComponent | fefda110e2274884ad58906733606efd | Recommended acceptance unchanged | Exact source bytes; Audit management/historical evidence intact. |
| Mneme — ApplicationComponent | 1af8a56358f7429fabfb57be66542a99 | Amendment required | All prior content preserved with one inserted candidate policy subsection and one provenance contribution. |
| Mnemosyne — ApplicationComponent | 7985e647bf9e4ca3ba70faf9e3e4ae02 | Recommended acceptance unchanged | Exact source bytes; authoritative persistence/version/outcomes/Search Management intact. |

## Reuse and representation decision

Exact property/value searches across current canonical sources/governance found no
existing `postCreateAvailability`, `Immediate-And-Distributed` or `Cache{InformationUnit}`
property definition. Canonical Mneme already owns active managed-information access;
Mnemosyne already owns authoritative persistence/version/outcomes and authoritative
Search Management. Existing InformationUnit identifies the governed information
boundary. These are reused without a new cache, InformationUnit or policy entity.

The Continuum Representation contract says architectural explanation belongs in the
Markdown body and a component definition can describe its responsibilities and
boundaries. The incorporation Praxis permits existing canonical assertions to
represent meaning without allocating duplicates. Accordingly the required policy,
three permitted values, invariants and explicit unknowns are normative architectural
content in the existing Mneme component. There is no need for a new Requirement,
Class, enum element, specialisation, relationship or static model Property value.
`Cache{InformationUnit}` is the Task's policy notation, not an allocated new Class.
No runtime instance value is selected. This respects the Task's §6.2 conditional
escalation rule: a new construct is unnecessary, so no additional allocation or
representation adjudication is required by this bounded representation.

## Exact policy semantics

`Cache{InformationUnit}.postCreateAvailability` governs required post-creation
InformationUnit availability within Mneme's managed cache environment. Mneme SHALL
interpret/apply it, preserving the existing ownership/access boundary.

| Value | Architectural semantics |
| --- | --- |
| `Immediate-And-Distributed` | Following creation, the InformationUnit is immediately materialised in the originating Mneme cache and proactively distributed across the applicable cache environment. |
| `Immediate` | Following creation, the InformationUnit is immediately materialised in the originating Mneme cache without requiring proactive distribution. |
| `OnRequest` | The InformationUnit is durably persisted without requiring proactive cache materialisation or distribution. Materialisation occurs on demand through governed retrieval. |

Every value preserves authoritative persistence established/reported by Mnemosyne,
existing governed retrieval/search accessibility, separation of cache and durable
state, InformationUnit identity/version/lifecycle/governance and existing durability,
reliability, recovery, security and authorisation obligations. `OnRequest` defers
cache materialisation, not persistence or accessibility. Mnemosyne Search Management
already searches authoritative managed persistence independently of cache completeness.
No new principal Mneme service, persistence mechanism or independent store is added.

## Axioms, conflicts and preserved uncertainty

AX-05 separates active representation/generation from authoritative durable state/
version; the policy operates entirely within Mneme's active availability role and
never gives cache/distribution durable authority. AX-11 expressly permits different
cache strategies without universal memory residence and forbids weakened guarantees;
on-demand materialisation preserves governed access. AX-10/15 retain failure,
recovery and uncertain outcomes without manufacturing persistence/publication success.
AX-14 preserves availability, materialisation, persistence, discoverability, versions
and authority as distinct. AX-17 preserves unresolved mechanisms, classifications,
source standing and the review/acceptance distinction. No conflict with these
canonical axioms or the bounded current component definitions was identified.
The historical DATA-003 Cache Transparency and Sub-ms Cache Access resources remain
historical; they supply no new acceptance, timing threshold or policy requirement.

The mechanism determining the value remains unresolved: per-creation supplied value
and governing information metadata are possibilities, with neither selected and no
precedence rule. No default, additional value, threshold, topology, cache technology,
API or deployment arrangement is inferred. Provenance is a natural `OnRequest`
candidate, not a universal binding. The policy does not settle creation/publication
ordering, durable acknowledgement, retry or recovery contracts.

The six original DR-18/19 questions remain unresolved and represented: retry original
versus linked new Pragma; detailed execution attempts; synchronous/asynchronous
Provenance publication; acknowledgement/recovery; schemas/cardinalities/lifecycle
states; client-library contracts/deployment. Existing independent Assurance,
Praxis/Journey, finite Dokimasia and no separate own-AuditEvent qualifications remain
unchanged. No additional unestablished architectural representation decision is
needed; unavailable Task events/prefixes remain a separate governance qualification.

## Projection placement correction

Verification found that a separate top-level amendment section at the end of the
component did not appear in the narrative renderer, which selects the main
Architectural content section. Only the new policy block was moved into that
section as a candidate subsection. All prior source text is recovered exactly by
removing the inserted block; no renderer, classification, old definition or
architectural meaning changed. The final XML/reader/narrative are checked for the
property and three values. This resolves the discovered projection mismatch within
the authorised representation scope, without a new architectural decision.

## Affected representations and verification boundary

Only [Mneme](../../../../../../continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md)'s canonical component body/provenance is amended. All identities, names,
types, specialisations, standing, bindings, old provenance, source-qualified content,
relationships, navigation, Manifest and maintained Views remain intact. The other
five reviewed definitions, accepted release publications and earlier candidate/evidence
are preserved byte for byte. New XML, reader and narrative source evidence reproduce
the updated Mneme meaning; no historical candidate is overwritten or published as
accepted. No runtime policy Property value is inserted into the model.

Actual verification results, exact hashes, preservation proof and real Archi
import/export and strict/raw qualification are in the adjacent validation/preservation,
checks and transport evidence and new governed Report. The inherited 575 relationship
`xml:lang` drift remains a strict FAIL, not a new regression or accepted loss. Existing
guards remain unchanged. Candidate recommendation readiness is distinct from human
acceptance: after verification the amended candidate is submitted for consolidated
Solution Architect review. DR-1, DR-18, DR-19 and HCRL remain open; no release transition,
accepted publication, commit or push.
