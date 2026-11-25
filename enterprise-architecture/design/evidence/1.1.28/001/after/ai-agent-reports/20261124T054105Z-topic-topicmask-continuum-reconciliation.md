Report-Produced-At: 2026-11-24T05:41:05.573475Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

# Topic and TopicMask design-to-Continuum reconciliation

**Result** · Proposed canonical amendments for Solution Architect review.
The exact candidate is [the retained locator patch][locator] followed by
[the Naming ADD patch][candidate] against `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`.
Both patches were replayed against an independent copy of the originating
canonical paths and produced the identical candidate. The working Continuum
and `architecture-release.json` remain at accepted Architecture 1.1.26.
[Issued Task][task] · [Pending HCRL][review]

The checked-out branch is `feature/BL-0003-Calliope-Topic-and-TopicMask`, at
the same tree as foundation commit `7d5c195a`. The completed [Software HCRL 009][reset]
accepts the `0.1.0-SNAPSHOT` development foundation and supersedes the exploratory
Java candidates and their implementation compatibility obligations. [Software HCRL
004 DR-10][acceptance] qualifies the accepted [Final.001.001][design] runtime
model and API. [007][finalisation] records the subsequent design finalisation.
The two pre-existing uncommitted documentation files retain their exact bytes.

## Accepted design correspondence

| Accepted source | Proposed canonical result |
| --- | --- |
| §§1–2, Topic identity and construction | Immutable, nonempty ordered qualifier/value paths supply identity, equality and hashing, independent of entry versions. Completed entries have a qualifier, value and optional version. Indexed positions support deterministic assembly, including out-of-order inputs, validated replacement, contiguous final positions and position-based default qualifiers. First successful addition fixes mode. Failed operations preserve builder state and snapshots. Whole-entry ancestry, case-sensitive order and parent/ancestor retention of entry versions follow the design. |
| §§3, 5–6, Topic representation | Retain the qualified non-versioned and versioned canonical forms with strict escaping and parsing. Keep raw unqualified, friendly and display projections separate and preserve the relative leaf name. Ordered logical reconstruction retains optional versions and reconstructs validated immutable entries. Apply release bounds to decoded fields and the separate byte limits. XML preference under NFR-099 applies at the interchange boundary. |
| §§4–6, TopicMask | Structural equality distinguishes the ordinary and universal kinds. Ordinary masks use ordered qualifier/value and optional version expressions and the containment flag. Exact depth is default. Inclusive matching allows trailing entries. Keep full-field RE2/J, absent/present-empty/wildcard version criteria, reusable builders, explicit universality, fail-closed evaluation and safe diagnostics. Canonical ordinary expressions preserve raw expressions and round-trip with the containment flag. Logical interchange retains kind, order, containment where applicable and version presence. |
| §§1, 7, identity and reference domains | The TopicIdentityStable Feature's unqualified rename/reclassification statement conflicts with runtime path identity. Its proposed scope preserves governed identifiers for the same architectural element and recognises a distinct runtime identity when a path changes. Both IU descriptions and the IU-to-Topic association now distinguish the persistent IU UUID and its unqualified Topic designation. Fully qualified IU vocabulary and reference mappings require separate disposition. |
| §§4, 7, consumer and graph correspondence | Revise the established TopicMask-to-Topic association description. The Store adapter uses shared matching of structured Topics, keeping coverage and instance selection rules. Preserve graph topology, association identities and provenance, existing specialised Topic identities, ownership and independent authorisation. |

The exception review traced native edges referencing the common model and
active Topic identity, registry, matching and direction statements. The
Calliope-Models definitions and the neutral relationships for IU metadata,
representation and authorization, Mneme and Mnemosyne capability use, shared
package placement and consuming Features remain applicable. The other
Governed Topic Identity & Namespace Features already describe governed
Composition where defined, alias ambiguity, accepted-definition release
control and governed hierarchy matching. Their wording needs no additional
runtime policy. The four IU coordinate Topic references retain their four
referents and applicable reference contracts. The candidate touches six
elements and two existing association descriptions. [Exact scope evidence][verification]

The original active Topic and TopicMask wording superseded by this candidate,
including the old runtime UUID rules, sequence representation, selector basis
and expected unrestricted default, is recoverable at the pinned origin in the
same two DataObject files. The patch identifies its exact replacement. The
Feature, IU and Store adapter preimage remains at the same pinned source.
Already superseded text inside the canonical wrappers was preserved, with its
replacement references updated in the candidate. The older accepted design,
retained source, HCRLs and source bindings remain retrievable. This carrying of
the historical material gives it no current runtime authority.

AX-04 keeps Naming semantics in Harmonia and uses RE2/J and encodings as
machinery. AX-07 preserves authorisation independently of selection. AX-10
supports bounded evaluation and defined failure behaviour. AX-14 keeps
architectural identifiers, runtime names, IU identity, revision domains and
mask representation distinct. AX-17 keeps the Feature correction, unresolved
mappings and release decisions subject to adjudication. [Architectural Axioms][axioms]

## Retained input and remaining decisions

The missing historical basename was reproduced by the live validator. The
retained `DRP-Topic-and-TopicMask.Final.001.md` matches the SHA-256 in both
canonical entries and the 1.1.25 input inventory. The retained file and the
separately referenced offline `Final.001.000` file are byte-identical. Point
only the two `retainedAt` values at the actual retained basename. Leave both
source paths, original authority, target bytes and historical records intact.
[Retained input][retained] · [Historical inventory][historical-inventory] · [Locator patch][locator]

- The candidate resolves R1/R2's common-model contradiction and represents
  R4's identity-domain distinction when incorporated. The Feature amendment
  needs explicit scope adjudication. Retain applicable governed registry/alias
  rules, persisted identifiers and selectors, migration equivalence and exact
  specialised-reference correspondence where a downstream activity needs them.
- R3 preserves the IU UUID and established unqualified name. Its qualified
  vocabulary, UUID encoding details and exact mappings remain open for the IU
  work. Pragma/subscription, Parcel, security and runtime service work retain
  their existing responsibilities and require their own acceptance evidence.
- R5/R6 still require release limits, pinned RE2/J conformance, module and
  dependency treatment, the separate Themis API matter where applicable,
  governed logger availability, safe failure behaviour and measured delivery
  and latency evidence. Naming implementation and behavioural acceptance
  remain future software work.
- Confirm the release sequence. The separate 1.1.28/001 proposal assumes the
  separate 1.1.27/002 amendment goes first. If the Solution Architect prefers
  one change set, explicitly govern the combined review. DR-5 can be
  dispositioned independently for dependent canonical verification in 1.1.27.
  Establish the exact accepted predecessor before any Naming application.
  [Pending ArchUnit amendment][archunit] · [Release coordination][review]

## Verification

| Check | Result |
| --- | --- |
| Live `architecture_continuum.py validate` | **FAIL**, pre-existing missing retained-input path. |
| `architecture_continuum.py --root <scratch-root> validate`, first with only the locator patch and then with both patches | **PASS** in both cases. Full candidate has 6,563 elements and 9,427 relationships, with 462 qualified predicates preserved. |
| `architecture_continuum.py --root <scratch-root> project --output <scratch-xml>` and repeat with `--check` | **PASS**, including offline XSD, reference, containment and naming checks and byte-identical regeneration. Existing graph identities, navigation and 1,304 authored Views preserved. |
| Bounded mutations in scratch canonical Markdown | **PASS**, the validator rejected a missing Association endpoint and a duplicate source binding using the expected diagnostics. |
| `mvn -B -ntp -o test -pl paradeigma/paradeigma-test -am -Dtest=*ArchitectureTest -Dsurefire.failIfNoSpecifiedTests=false`, JDK 21 | Maven exited zero with **zero tests executed**. Required ArchUnit coverage remains **UNRESOLVED** on the reset foundation. |
| Scope and existing work | **PASS**, only these review/evidence files were added to the working tree. The roadmap and backlog hashes match entry. The accepted source and existing 1.1.25 Exchange, reader and narrative publications are unchanged. The 1.1.26 governance-only transition retained those publications. |

Candidate checks used bounded commands and Python 3.14 with the declared EA
packages available locally. The canonical patch and verification record
support independently regenerating the temporary XML. The supplied evidence
does not include a native Archi round trip or human acceptance. After an
authorised incorporation, regenerate the Continuum Manifest and approved
version's Open Exchange, HTML/Markdown reader, catalogue and narrative LaTeX
through the maintained tooling. Publish derived PDFs only in scope of that
later transition. [Verification results][verification] · [1.1.26 closure][predecessor]

[task]: ../../before/ai-agent-tasks/unresolved-topic-topicmask-continuum-reconciliation.md
[review]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[locator]: ../01-retained-topic-input-locator.patch
[candidate]: ../02-topic-topicmask-continuum.patch
[verification]: ../verification.json
[reset]: ../../../../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md
[acceptance]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[finalisation]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-007.pending.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[axioms]: ../../../../../../governance/architectural-axioms.md
[retained]: ../../../../1.1.25/001/before/accepted-input/DRP-Topic-and-TopicMask.Final.001.md
[historical-inventory]: ../../../../1.1.25/001/before/baseline.json
[archunit]: ../../../../../review/1.1.27/hcrl-1.1.27-002.pending.md
[predecessor]: ../../../../../register/1.1.26/hcrl-1.1.26-001.complete.md
