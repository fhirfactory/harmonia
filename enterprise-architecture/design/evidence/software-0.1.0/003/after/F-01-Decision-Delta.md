<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 decision delta — iteration 003

**Comparison** · [Final.001.000][final] → [Draft.001.001][previous] → [Draft.001.002][draft]  
**Inputs** · [Initial adjudications][initial] and [continuation adjudications][continuation]

The continuation restores Final.001.000's missing-qualifier rule and supersedes the F-01.24 value-defaulting decision recorded in iteration 002. It resolves several open Draft.001.001 proposals. The historical documents retain their original text.

## Superseded or refined terms

| Prior term | Result in Draft.001.002 |
| --- | --- |
| F-01.24 and earlier F-01.48 wording, also reflected in the iteration-002 draft, architecture comparison, gap analysis and proposed tests | Generate `N<position>` only for a null or empty **qualifier**, at assembly and at its final zero-based position. A missing or empty **value** fails. Canonical parsing requires both fields explicitly. [F-01.55–F-01.56][continuation] |
| F-01.23 retained arbitrary nonempty versions, including whitespace | Retain `null` and `""` as Topic version absence. Present versions use printable ASCII and reject edge whitespace, including whitespace-only input. [F-01.50–F-01.54][continuation] |
| Final.001.000 rejected duplicate sequence inputs. The current Java builder and tests also reject duplicate indexed adds. | Draft indexed builder calls replace at an occupied index after validation. Required zero-based continuity at build and unambiguous completed ordering remain. [F-01.69–F-01.71][continuation] |
| Draft.001.001 proposed a reserved RegEx tuple and `@all` for universal selection. F-01.15 described ordinary ordered expressions and containment as the logical payload. | Universality is a distinct logical kind with no criteria or containment. The ordinary payload and parser keep their field roles. [F-01.45–F-01.46][continuation] |
| Draft.001.001 left escape grammar, display across depths, empty RegEx and field validation open. | Two outer/inner layers are defined, display uses the final one or two values, empty RegEx is valid, and field character and whitespace policies apply as specified. [F-01.46–F-01.62][continuation] |
| F-01.44's earlier candidate naming and iteration-002 output | F-01.74 establishes iteration 003 and Draft.001.002, evolving the accepted 001.000 design. A later accepted design would be 002.000. [F-01.74][continuation] |

## Continuation coverage

| Decision | Design incorporation | Draft location |
| --- | --- | --- |
| F-01.45 | Explicit universal kind, ordinary minimum size, equality/hash and safe logical decoding. | [§4][mask] · [§5][interop] |
| F-01.46 | Separate structural escaping for original RegEx, strict parsing and ordinary round trip. | [§5][interop] |
| F-01.47 | Raw sole value or final two values at every depth. | [§3][names] |
| F-01.48 | Reject null/empty values at construction and parsing. Explicit `N1` remains literal. | [§1][topic] · [§2][builders] · [§3][names] |
| F-01.49 | Validate supplied qualifiers as nonblank printable ASCII without edge whitespace. | [§1][topic] |
| F-01.50 | ASCII qualifiers and present versions, Unicode values, exact preservation and independent field limits. | [§1][topic] · [§6][resources] |
| F-01.51 | Reject edge whitespace on values in assembly and parsing. | [§1][topic] · [§3][names] |
| F-01.52 | Normalize absent TopicEntry version, reject whitespace-only or boundary spaces on present versions. | [§1][topic] |
| F-01.53 | Unicode allowance by category, internal U+0020 and malformed-sequence rejection. | [§1][topic] |
| F-01.54 | Code-point field limits before escaping and original expression sizing. UTF-8 limits are independent. | [§1][topic] · [§6][resources] |
| F-01.55 | Generate and validate only missing or empty qualifiers. Generated and equal explicit qualifiers behave identically. | [§1][topic] · [§2][builders] |
| F-01.56 | Strict canonical parsers require explicit valid qualifiers and values. | [§3][names] |
| F-01.57 | Empty RegEx is valid, structurally present and follows full-field semantics. | [§4][mask] · [§5][interop] |
| F-01.58 | Absent, empty and wildcard version constraints retain separate logical and matching semantics. | [§4][mask] · [§5][interop] |
| F-01.59 | Null third RegEx argument equals the two-argument form. Empty and wildcard remain distinct. | [§4][mask] |
| F-01.60 | Null qualifier/value RegEx is invalid. Designated helpers insert their explicit wildcards. | [§4][mask] |
| F-01.61 | Validate literal fields before quoting, then enforce expression resource limits. | [§4][mask] · [§6][resources] |
| F-01.62 | Literal two-argument form leaves version open. Three-argument form requires a valid version literal. | [§4][mask] |
| F-01.63 | Mask factories never generate qualifiers. Literal-value helper intentionally leaves qualifier open. | [§4][mask] |
| F-01.64 | Mask builder uses ordinary literal/value helpers and has no default-qualifier convenience. | [§4][mask] · [§8][api] |
| F-01.65 | Reuse both builders with defensive immutable snapshots and permitted sharing of immutable patterns. | [§2][builders] · [§4][mask] |
| F-01.66 | Validate supplied fields before storing, immutable RegEx entries at construction, assembled invariants at build. | [§2][builders] · [§4][mask] |
| F-01.67 | Failed add, replacement or build preserves existing builder state and previous results. | [§2][builders] · [§4][mask] |
| F-01.68 | Final position determines a generated qualifier in both modes. | [§1][topic] · [§2][builders] |
| F-01.69 | Retain indexed construction with ascending, contiguous, zero-based final positions. | [§2][builders] |
| F-01.70 | First successful addition fixes sequential or indexed mode. Equivalent ordered inputs converge. | [§2][builders] |
| F-01.71 | Validate inputs before replacing at an occupied index. | [§2][builders] |
| F-01.72 | No removal or renumbering API. Assemble a different hierarchy explicitly. | [§2][builders] · [§8][api] |
| F-01.73 | No builder-from-existing-Topic convenience. | [§2][builders] · [§8][api] |
| F-01.74 | Preserve both predecessors and retain this candidate and evidence in iteration 003 for review. | [Draft standing][draft] · [Execution report](F-01-Execution-Report.md) |

## Initial decision coverage carried forward

| Initial adjudications | Retained contracts and explicit corrections |
| --- | --- |
| F-01.1–F-01.6 | Canonical identity, immutability, ordered typed paths, repeated qualifiers, implicit hierarchy and whole-entry containment. [§1][topic] · [§2][builders] |
| F-01.7–F-01.14 | Positional field criteria, helpers, version presence, structural mask identity, `TopicMask.matches(Topic)`, full-field matching and compiled patterns. [§4][mask] |
| F-01.15–F-01.21 | Logical data and format independence, explicit universal matching, default exact depth, RE2/J, literal helpers, null candidate handling and case sensitivity. [§4][mask] · [§5][interop] |
| F-01.22–F-01.28 | Optional versions and normalization, corrected qualifier generation and field rules, strict Topic and mask grammars and display/text APIs. [§1][topic] · [§3][names] · [§5][interop] |
| F-01.29–F-01.36 | Version-independent order, strict navigation, depths, release limits, evidence of feasible RegEx resource controls and early exit. [§1][topic] · [§2][builders] · [§4][mask] · [§6][resources] |
| F-01.37–F-01.42 | Boolean fail-closed diagnostics, application logging and audit boundary, levels, concurrent mask reuse and no global result cache. [§6][resources] |
| F-01.43–F-01.44 | Future JUnit evidence and preserved design history. The continuation supplies the next draft name and review boundary. [§10][acceptance] · [Execution report](F-01-Execution-Report.md) |

Draft-level proposals still needing review include strict empty-version text rejection, human-facing name projections, navigation argument handling and ordinary-only mask accessor behaviour. Release limits, permitted dependencies, production logging behaviour and the compatibility decisions are open. [Draft §9][open] · [Architecture reconciliation](F-01-Architecture-Reconciliation.md)

[initial]: ../../002/before/F-01-Topic-TopicMask-Adjudications.md
[continuation]: ../before/F-01-Topic-TopicMask-Adjudications-Continuation.md
[final]: ../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.000.md
[previous]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md
[draft]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md
[topic]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#1-topic-identity-and-structure
[builders]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#2-topic-construction-and-navigation
[names]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#3-topic-names-and-strict-canonical-parsing
[mask]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#4-topicmask-kinds-and-matching
[interop]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#5-canonical-mask-expressions-and-logical-interchange
[resources]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#6-resource-limits-concurrency-and-diagnostics
[api]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#8-proposed-java-surface
[open]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#9-open-decisions-and-evidence
[acceptance]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#10-mandatory-behavioural-acceptance
