Report-Produced-At: 2026-10-25T20:41:08.833670Z

Event meaning/source: Current Codex exec tool execution: Python system UTC event captured immediately after closing the completed initial Report write. Subsequent event-field and filename synchronization is provenance maintenance under the existing Architecture Change Provenance contract; no Task event time is inferred.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Bounded ArchiMate round-trip remediation — engineering report

Producer: Codex, co-executor. Boundary: proposed RAD 1.1.15, HCRL iteration 002.
Corresponding [operative Task and governance clarification](../../before/ai-agent-tasks/unresolved-round-trip-remediation.md).
Architecture Review: REQUIRED; [pending HCRL 002](../../../../../review/1.1.15/hcrl-1.1.15-002.pending.md).

Standing: implemented and verified engineering proposal, awaiting Solution
Architect adjudication. This Report does not approve the proposed routine, apply
the 001 model changes, allocate a release or confirm HCRL completion. The
currently governed routine remains operative. The opt-in prototype is used here
only for the authorised regression benchmark.

## Outcome and recommendation

The deterministic 001 regression completed in **11.666647 seconds**, including
comparison, exact-current-Continuum projection validation, normalized delta
serialization and evidence/checkpoint preparation. The architectural reasoning
input is **68,819 bytes** of readable JSON (**40,683 bytes** in compact JSON),
compared with the original **22,012,817-byte** raw report and **55,510,287 bytes**
of input XML. The readable delta is approximately **99.69% smaller** than the raw
report. No autonomous AI review or token-cost experiment was run.

The output retains all **12 new elements, 9 new relationships, 7 new Views and
17 existing View navigation changes**, while preserving every baseline semantic
element, relationship and View identity. It classifies the 17,957 known language
changes mechanically and replaces the 2,803 positional organization findings
with a bounded View hierarchy comparison and derived-navigation treatment.
No unclassified representation anomaly or input-validation exception remains in
the benchmark delta.

**Recommendation: return to Harmonia architecture and solution development after
Solution Architect disposition of this proposal.** No further engineering work
is identified as blocking normal review preparation for this benchmark. The
remaining decisions are governance checkpoints, actual architectural
interpretation and the explicitly isolated empty-View-folder publication effect.
Do not extend this remediation into a general tooling programme.

The approximately five-minute whole review-ready target is now plausible for
routine small deltas, with mechanical processing taking about twelve seconds.
It is **not a measured guarantee**: targeted interpretation and human review
were deliberately not benchmarked through an expensive autonomous AI run. The
generated checkpoint scaffolds are explicitly incomplete until architectural
interpretation supplies independent CR decomposition and HCRL meaning.

## Authority, scope and originating state

The originating accepted release is 1.1.14, with predecessor 1.1.13. Source
history locator and protected-state verification are retained in the
[validation summary](../validation-summary.json). The actual outcome is an
uncommitted proposed tooling/documentation change with pending HCRL 002;
`architecture-release.json` remains unchanged at 1.1.14. Pending review consumes
no release under the release policy.

Loaded authority includes the repository instructions, canonical AX-04, AX-08,
AX-14, AX-15 and AX-17 Principles, common Governance, Review Praxis, Change
Provenance, release policy, Continuum Representation/Incorporation, the existing
Round-Trip Praxis, workbench procedure and Change Review contract. AX-04 supports
reuse of existing machinery; AX-08 finite meaningful provenance; AX-14 semantic,
transport, navigation and presentation distinctions; AX-15/17 explicit uncertainty
and Solution Architect authority. The implementation changes no runtime subsystem,
native metamodel, governance lifecycle, canonical allocation authority or approval
checkpoint.

The complete 001 evidence inventory was examined: operative Task, formal Report,
failed intake output, raw comparison, organization inventory, property-definition
rebinding table, Change Review and pending HCRL. Large XML/JSON material was
inspected through deterministic parsing/counting rather than supplied wholesale
to architectural reasoning. Existing 001 files were preserved unchanged. No DRP
corpus was loaded or searched; the 001 review's references to its specific DRP
were treated as review evidence, not current architectural authority. The
candidate's XML additions and existing review provide sufficient bounded input
for this engineering activity.

Baseline: 26,919,406 bytes, SHA-256
`ee1d7ef8d5491a4a656d8c2f07c38999b6276a130064d797a08f19501aa47161`.
Candidate: 28,590,881 bytes, SHA-256
`a31ed511c07e0a54c503383f6e52dfdad1e5905e642c05e7dc14fb7649af2eb9`.
Their exact locators/hashes are retained in the [normalized delta](../benchmark/architectural-delta.json).

## Observed causes of latency and token growth

The 001 comparator took approximately 5.0–5.5 seconds. Its retained raw JSON
contains 21,087 findings: 17,957 semantic classifications, 2,294 unexplained
differences, 540 information-loss findings and 296 equivalent normalizations.
The core comparison was not the primary bottleneck.

The missing-to-English language attributes were emitted as semantic differences
despite unchanged generated scalar text. They made existing records appear
changed and expanded diagnostic material. Positional traversal of anonymous
organization items produced 2,803 organization findings, conflating derived
navigation serialization with protected architect-authored View navigation.
Separately, the candidate lost 2,894 organization identifiers and placed all
1,660 relationships under Relations; those counts did not themselves establish
architectural change to a semantic identity.

Canonical intake required canonicalId on every element and aborted at
InformationUnit, before reaching the remaining eleven new elements and the
relationship/View comparison. Agents therefore reconstructed inventories through
additional inspections. Existing raw and canonical views of the model lacked a
single compact normalized boundary suitable for targeted architectural reasoning.

The formal 001 records explicitly describe corrections to guessed timestamps,
filename prefixes, relative links, elapsed-time claims and post-emission wording.
The Change Review still contains prospective statements that its HCRL does not
yet exist, while the retained HCRL does exist. The Report and Change Review also
give differing counts of DRP Topics. These are evidence of duplicated manual
facts and proposal investigation; this remediation does not resolve or rewrite
those historical assertions. They are not required to discover the actual XML
delta. Corpus-wide DRP investigation and repeated AI implementation/reviewer
cycles described by the Task amplified reasoning work without accelerating the
approximately five-second mechanical comparison.

The supplied hours/~US$50 benchmark is retained as the Solution Architect's
empirical observation. Session histories and billing records were not reloaded
to invent a more precise causal or dollar allocation.

## Changes made

| Existing component | Bounded engineering change |
| --- | --- |
| `exchange_compare.py` | Retains default raw fidelity comparison; adds proposed normalized comparison/delta output, exact generated-property language classification, identity-correlator reuse, all-record inventories, View-tree comparison, anomaly reporting and bounded context seeds. |
| `continuum_workbench.py` | Adds opt-in `review --normalised`; discovers the complete candidate before presenting exceptions, validates the exact current projection, supplies only relevant canonical source locators, summarizes mechanical validation inventories and records elapsed time. Emits failure conditions for invalid input. Existing default review is unchanged. |
| `architecture_review_evidence.py` | Adds explicit Task/Report emission, supplied-event/UTC-prefix synchronization, completed-Report-write event capture, fresh checkpoint scaffolding, hash/size/layout/link checks and refusal to overwrite human reviews. Existing read-only checks remain available. |
| `continuum_navigation.py` | Adds optional full preservation of the named Views hierarchy, including empty folders, while reusing deterministic non-View regeneration. Existing publication default remains unchanged pending DR-3 adjudication. |
| Round-Trip Praxis and tooling README | Add clearly marked proposed instructions and adoption boundary, bounded Continuum retrieval/DRP policy, explicit interpretation/SA checkpoints and exceptional-condition behavior. Existing operative stages remain in force. |

`architecture_continuum.py` required no change. Its existing validated projection,
navigation/containment machinery and canonical source loader remain sufficient.
The bounded reasoning boundary is implemented in the workbench rather than
changing canonical validation or creating another architecture/provenance registry.
No new dependency, workflow engine, AI framework, View generator or generic
provenance framework was introduced.

The language equivalence rule covers only an absent → en attribute on a single
unchanged scalar for the exact projector-generated element Property keys. It
checks the owner, definition-name resolution, value multiplicity, text, other
attributes and inherited language. It does not erase language from authored
properties, model metadata, names, Documentation or Views; different language,
text, cardinality or unrecognized representation remains visible.

The View comparison retains identities, metadata, content changes, folder
addition/removal, empty folders, placement and common-sibling order. Unrecognized
or ambiguous folder/reference conditions are explicit exceptions. Semantic
collections are compared by their existing identifiers; insertion into such a
collection does not produce a second architecture question merely because its
collection order differs. Internal View stacking/routing/layout remains exact.

New elements without canonical IDs are discovered without allocation. Duplicate,
invalid, conflicting or lost canonical claims still fail closed after discovery.
Canonical continuity/rebindings are reported using the existing correlator; the
prototype does not silently adjudicate a rebinding. Context locators cover changed
or removed canonical subjects and existing relationship endpoints in the delta.
For wholly new concepts, the element names/types and explicit relationships seed
a bounded vicinity search. No automatic corpus retrieval is added.

The generated Change Review and HCRL are draft scaffolds, not mechanically
invented architectural decisions. They allocate no CR IDs. The processing agent
still performs targeted independent CR decomposition under the existing contract,
records the architectural conclusions in HCRL, and stops for the Solution
Architect. A new-schema delta is explicitly rejected by the existing element
amendment apply adapter. Application continues through separately adjudicated,
applicable existing incorporation/amendment operations; this review-preparation
task supplies no new automatic application authority.

## Regression results and input to architectural reasoning

Reproduction: run the retained [benchmark driver](../benchmark/benchmark.py) with
the declared tooling Python environment and a fresh explicit output destination
if repeating after the retained scaffold already exists. The driver refuses
silent overwrite. It runs no AI and mutates no canonical source or Git state.
The [benchmark summary](../benchmark/benchmark-summary.json) records the measured
results and exact evidence sizes.

| Population | Baseline retained | Added | Removed | Existing content changed |
| --- | ---: | ---: | ---: | ---: |
| Semantic elements | 1,602 | 12 | 0 | 0 |
| Relationships | 1,651 | 9 | 0 | 0 |
| Views | 1,209 | 7 | 0 | 0 |

Existing View navigation changes: **17**. View-folder paths: **23 added, 12
removed**, reflecting new and relocated authored hierarchy. No common-sibling
reordering was detected. The 17 moves consist of five REQ-FND co-governance Views
under Requirements, eleven Topic Views under Topics, and the information-map
boundary View under Topics. Their exact before/submitted paths are in the delta.

Deterministically classified material comprises **17,957** generated-value
language changes, **251** keyed property/definition ordering normalizations and
**45** property-definition identifier rebindings. The exact old/new definition
bindings remain reported, satisfying the existing evidence obligation. The
**2,803** positional organization findings are replaced by direct inventory and
the explicit View comparison. The three non-organization collection-order
observations associated with the added populations are accounted for through
the element/relationship/View inventories. Raw transport evidence remains intact.

The reasoning agent receives the compact normalized JSON and, when necessary,
bounded relevant canonical source content reached through its locators/vicinity
search. It does not need the complete XML, 22 MB raw JSON, corpus-wide DRPs,
session histories or full baseline validation inventories. Mechanical validation
retains counts/gates, a result digest and the existing baseline's unresolved
property count; it does not expose every baseline uncertainty as a new review.

The genuine interpretation/adjudication matters remain the logical packages and
InformationUnit class, functional/non-functional requirement groups and the
durability requirement; their explicit specializations, containment and naming
questions; seven authored overview Views, including the empty diagram and lack
of visual connections; and the seventeen View moves. All twelve new elements
await canonical identity allocation after adjudication. The earlier CR-001–007
provide historical correspondence, not automatic current decisions. Mechanical
non-View navigation and the proven language/ordering/rebinding cases do not
create new architectural CRs in the proposed routine. The actual 001 CR-008/009
records were not modified or adjudicated by this activity.

Timing: comparison plus canonical validation **11.498381 s**; evidence preparation
**0.040084 s**; complete measured deterministic regression **11.666647 s**.
An initial prototype exposed a large full-validation inventory in the delta;
this was replaced with the compact validation summary. An experimental full
empty-folder preservation projection also correctly failed the exact-baseline
gate; the unaccepted publication change was isolated behind an option. These
were bounded engineering tests, not routine autonomous investigation/reviewer
cycles. Final replay establishes the same retained delta apart from measured
elapsed time, as recorded in the validation summary.

## Verification

The full current EA suite passed **79 tests in 288.835 s**. After the last focused
changes, the normalized-review suite passed **10 tests**, and the evidence suite
passed **16 tests**. The additional two normalized-review cases supplement the
earlier full run; this is not a claim that all 81 distinct tests ran in one full
invocation. Added/extended tests cover narrow English normalization versus text,
language, inherited-language and multiplicity drift; complete new-element
discovery; invalid/lost identity and references; authored View geometry, empty
folders, moves, order and duplicate-path exceptions; malformed-input condition
emission; changed-relationship context seeds; event/prefix emission; missing
times; input tampering; evidence boundaries; links; explicit exceptions; and
preservation of existing human review files. Existing creation, amendment,
containment, publication, release and lineage tests remain in force.

`make ... check` passed release/Manifest checks, exact published Exchange
comparison, browser and narrative-publication staleness checks, and canonical
link validation. The accepted Exchange hash remains
`ee1d7ef8d5491a4a656d8c2f07c38999b6276a130064d797a08f19501aa47161`.
The optional navigation regression also proves regeneration from absent and
misleading non-View navigation with unchanged semantic/View signatures and full
empty authored View-folder preservation. `git diff --check` passed.

The repository-required Java architecture command was attempted under a bounded
110-second timeout. The online invocation failed after 1.975 seconds because its
resolver could not write the read-only Maven cache. This was a harness/cache
condition, not a product-test failure. An evidenced offline retry using cached
dependencies completed successfully: **90 architecture tests, zero failures,
errors or skips; Maven total 27.251 s**. No production Java change was needed.
No command timed out or stalled.

Protected-state verification covers **1,619 files**, including the canonical
Continuum, accepted projection, presentation template, release file, all original
001 evidence/review files and available Git HEAD/index/packed-refs bytes. Their
before/after aggregate hashes agree. Initial unrelated untracked working files
were left in place. No frozen publication was regenerated or overwritten.

## Remaining conditions and consistency with governed text

The benchmark has **no unclassified representation or structural-input exception**.
This does not establish export authenticity or architectural approval. Original
001 workbench provenance/no-edit-control limitations and planning-versus-execution
timestamp attribution remain in 001. Actual Archi import/export and visual
inspection were not performed. Existing specializations/containment/naming
questions belong to the architecture review, not to an automatic tooling repair.

The 002 Task instruction-entry timestamps are not exposed by the current
interaction. The original event times and corresponding filename prefix remain
UNRESOLVED for Solution Architect treatment before closure. This Report's actual
completed emission time is captured directly at its completed tool-write event
under the existing Provenance contract; the prototype is not treated as adopted.
No guessed Task time, filesystem metadata or unrelated historical timestamp is used.

The default workbench documentation still describes valid-canonical-ID-only
review; that remains accurate for the existing default and differs deliberately
from the unaccepted opt-in proposal. The existing Praxis already separates
derived navigation from authored Views, but its source-context relationship-folder
description differs from the later accepted workbench policy retaining native
Relations organization. This review path does not invent a new relationship
folder policy or change the accepted projector. Its bounded responsibility is
to regenerate non-View representation through the current governed projector
after incorporation and present any material rule conflict to the Solution
Architect. The proposed Praxis section documents the new normalization and
reasoning boundaries without replacing the operative routine before acceptance.

The existing projector prunes empty folders under Views. Preserving the whole
current template's Views hierarchy changes baseline projection bytes. The
proposed preservation option is tested but **not activated for publication**;
HCRL DR-3 explicitly requests treatment of those folders. No speculative View
generation or accepted-publication repair was undertaken. The normal safeguards
for actual Archi structural inspection still apply to any later material
publication change.

Existing-review merging, unsupported application adapters and deltas beyond the
128,000-byte reasoning limit return for explicit bounded treatment. Invalid XML,
unknown representation, identity ambiguity, stale baselines and invalid evidence
facts produce clear failure/unresolved conditions. The machinery performs no
autonomous follow-on investigation, recursive provenance or repeated AI review.

## Closure boundary and further work

**Blocking engineering defects preventing ordinary Harmonia development:** none
identified for the demonstrated review-preparation path. Adoption still requires
Solution Architect adjudication; the existing default remains operative until
then. The five-minute target remains subject to actual targeted interpretation,
and exceptional architectural scope is surfaced rather than hidden.

**Non-blocking work to defer:** broader transport-equivalence rules, automated
merging of human-edited review documents, a new-schema automatic application
adapter, generated Views, corpus indexing, AI orchestration/cost instrumentation,
and general performance frameworks. Implement none without concrete future
friction. Empty-folder publication treatment is a current bounded adjudication
question, not a reason to continue expanding round-trip machinery.

**No 1.1.15 architectural CR was applied.** No canonical element/relationship,
submitted candidate, original 001 review/HCRL, accepted projection, release number
or template was changed. **No Git mutation was performed:** no staging, commit,
push, merge, rebase, amend, tag, branch creation or remote/history mutation.
The Solution Architect owns that boundary. Stop here at the pending HCRL review
checkpoint; implementation/test success supplies no acceptance or completion.
