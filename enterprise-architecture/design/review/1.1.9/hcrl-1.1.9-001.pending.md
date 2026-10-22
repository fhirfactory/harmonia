<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Sole normative Continuum Principle definitions | Accept | The consolidation of architectural Principle authority is accepted as implemented. The canonical Architecture Continuum Principle elements are the sole normative definitions of the Harmonia architectural axioms. Current governance and other maintained material may reference and provide contextual guidance for those Principles but does not constitute an independent normative definition. Historical material retains its historical standing.|
| [DR-2](#dr-2) | Bounded supporting Minutiae and provenance | Accept | The bounded supporting Minutiae and formal provenance are accepted. The accidental-copy guard and navigation checks are accepted as bounded safeguards and do not constitute a second source of architectural authority or a semantic classifier of normative content. The Task instruction-entry timestamp was unavailable to the executing agent and was correctly retained as unresolved rather than inferred or substituted. The available governed provenance and implementation/verification evidence are sufficient for this transition. No further remediation is required.|

# HCRL — Sole normative Continuum Principle authority

**Review basis:** **1.1.8 → 1.1.9**, iteration **001**. Originating HEAD
`73e5c4f529743ca66d5e3044ebe7a5f743701659`, clean working tree; root release
`1.1.8`, previous `1.1.7`. The supplied [reviewed 1.1.8 HCRL](../1.1.8/hcrl-1.1.8-001.pending.md)
and its [completed register](../../register/1.1.8/hcrl-1.1.8-001.complete.md)
establish the accepted immediate predecessor. They remain unchanged.

Mark Hunter, Solution Architect, directly authorised this bounded implementation
in the [operative Task](../../evidence/1.1.9/001/before/ai-agent-tasks/unresolved-principle-authority-consolidation.md)
on 2026-10-22 (interaction date). The opening table attributes that issued
instruction; it supplies no invented final disposition or result confirmation.
The common Review Praxis, Provenance contract and release policy apply.

<a id="dr-1"></a>
## DR-1 — Sole normative Continuum Principle definitions

The authorised rule is **One definition; many references**. The 18 existing
canonical Continuum Principle elements solely define the current Harmonia
architectural axioms. Governance and other current material may reference them
and discuss their application without maintaining independent normative copies.
No Principle wording, meaning, identity, standing or relationship changes.

The current governance axiom page already linked all 18 individually identified
Principles: no second full current AX-01–AX-18 definition set was found outside
the Continuum. Its retained Purpose nevertheless asserted “This document defines
the Architectural Axioms”. That competing definition-source claim is replaced
with the linked Continuum Principles. The introduction now expressly establishes
the sole normative boundary; all existing canonical links, identifiers, names
and anchors remain. The repository AGENTS authority entry and hierarchy now
identify the Continuum Principles as definitions and the governance page as
navigation/context.

Purpose, authority hierarchy, ADR relationship and architectural-review questions
remain as governance/application context. The maintained Praxis consistency
tables and scope-specific axiom discussions remain: they describe application,
not independent axiom definitions. Derived publication renderings remain reading
projections without independent normative authority. The change neither deletes
legitimate application rules nor reclassifies architecture.

Legacy-RAD, the original wrapped axiom register, old compatibility/orientation
pages, completed HCRLs, earlier pending reviews and historical evidence/publications
are unchanged. Their historical claims do not become current authority through
navigation. Canonical source provenance stays unchanged, including its original
source wording/locators. There is no new registry, synchronisation mechanism,
replacement Principle or governance redesign.

Relevant axioms: AX-04 uses existing repository/machinery mechanisms; AX-14 keeps
canonical definitions, navigation, contextual application, publication and
acceptance distinct; AX-17 preserves established meaning and raises missing
authority rather than inventing it. No runtime or module boundary is affected.
No architectural interpretation ambiguity requiring new adjudication was found;
existing canonical residuals are outside scope and retain their standing.

The [formal Report](../../evidence/1.1.9/001/after/ai-agent-reports/20261022T103426Z-principle-authority-consolidation.md)
records actual findings and scope. The
[preservation check](../../evidence/1.1.9/001/after/preservation-check.json)
establishes byte identity for all **1,602 canonical element files** and the
existing root navigation manifest against originating HEAD. All tracked
Legacy-RAD, historical publications and earlier review/register/evidence remain
unchanged. Thus canonical Principle meaning, wording, identity, standing and
relationships are unchanged; no canonical identity was allocated.

All **50 current EA tests** and the required offline JDK 21 Maven architecture
suite (**90 tests in 11 classes**, no failures/errors/skips) passed.
Temporary 1.1.9 generation and staleness/link/release checks, identity/reference,
naming, containment, offline XSD and browser DOM-contract gates passed.
The [isolated reproducibility check](../../evidence/1.1.9/001/after/reproducibility-check.json)
passed for **14 byte-identical derived artifacts**, with the full **50-test**
suite, unchanged canonical sources and no Legacy-RAD/scripts/Git/historical
configuration. No PDF compilation, visual inspection, actual Archi transport or
architectural acceptance is asserted. No retained publication was regenerated.

<a id="dr-2"></a>
## DR-2 — Bounded supporting Minutiae and provenance

This group comprises two tests in the existing current-EA suite, release metadata,
current release/publication navigation and formal Task/Report/check evidence.
The navigation test checks AX-01–AX-18 against canonical Principle names and
paths. The copy guard reads existing Axiom-section paragraphs and rejects exact
whitespace-normalised copies of ten or more words in maintained EA text. It
excludes canonical content, published projections, schemas and historical
review/register/evidence. It is a bounded accidental-copy check, not a classifier
of paraphrases, normative meaning or legitimate contextual guidance. No second
axiom content source is stored.

The [copy-guard probe](../../evidence/1.1.9/001/after/copy-guard-check.json)
positively established rejection of an injected current governance definition
copy and preservation of an excluded evidence copy. The temporary probe was
removed. The [verification summary](../../evidence/1.1.9/001/after/verification-check.json)
records commands/results, **73** checked local links and limits. The initial
default-Python invocation failed imports because `jsonschema` was unavailable;
the completed runs used already-installed declared dependencies through
`PYTHONPATH=/tmp/harmonia-ea-deps`. This was an environment prerequisite issue,
not an architecture failure. No TIMEOUT or STALLED outcome occurred.

The release helper's expected-current `1.1.8` operation prepares `1.1.9`, previous
`1.1.8`, once. This governance-only transition retains 1.1.8 architectural
publications under the release policy. Current navigation distinguishes the new
governed release from the latest unchanged architectural publication. Verification
may generate temporary 1.1.9 projections; those supply no retained new normative
copy or publication/acceptance claim.

Task-Issued-At and its required filename prefix are **UNRESOLVED**: the interaction
exposes the instruction date and wording but no attributable instruction-entry
time. The earlier release's accepted temporal treatment is not extended here.
Report production records its separate observable emission event. Solution
Architect treatment of the Task time/prefix is required before closure.

The [iteration evidence check](../../evidence/1.1.9/001/after/iteration-evidence-check.json)
is **UNRESOLVED**, with one Task, one Report and **zero structural errors**.
Only the disclosed unavailable Task event/prefix requires Solution Architect
treatment. The Report has its own recorded emission event and matching UTC
filename prefix. The read-only checker establishes layout and event/prefix
correspondence, not semantic coverage, attribution authenticity or acceptance.
Working-tree whitespace checks passed. These facts supply no result confirmation.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [bounded supporting group and disclosed Task event-time/prefix limit]
```

Final result confirmation remains with the Solution Architect; Codex does not
create a completed register record or self-confirm architectural closure.
