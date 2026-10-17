<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia governed architecture — synchronised baseline

This is the working baseline established by the explicitly authorised
2026-10-17 Markdown / ArchiMate synchronisation. It incorporates the reconciled
1.0.5A recovery alongside the current RADS architecture. Inclusion preserves
each assertion's standing; it does not approve a proposal, resolve a conflict,
allocate an Architecture Identity or declare a domain complete.

The canonical Markdown RAD is the governed Harmonia source repository. ArchiMate
is its governed architectural projection and human architecture workbench;
Open Exchange XML is an interchange representation. Neither a generated diagram nor its folders
can establish missing semantics. [AX-04, AX-14 and AX-17](../architectural-axioms.md)
and the [metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md)
continue to govern. The [navigation convention](../archi-navigation-and-view-convention.md)
defines presentation. The root [release register](../../../../architecture-release.json)
remains the only current-release authority.

## Read the architecture

- [Business Capabilities](business-capabilities.md)
- [Business Enabling Capabilities](business-enabling-capabilities.md)
- [Enterprise Capabilities](enterprise-capabilities.md)
- [Features](features.md)
- [Functions](functions.md), [Services](services.md), [Processes](processes.md)
- [Relationship assertions](relationships.md)
- [Motivation correspondence](motivation.md), with definitions linked to their maintained sources
- [Coverage and representation boundaries](coverage.md)
- [Discrepancies and human review](discrepancies.json)
- [Archi inspection guide](../../../archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md)

Each record's concise governed definition appears first, followed by verbatim
explanation whose continued applicability is established by reconciled source
evidence. Historical provenance alone does not make architectural meaning historical.
Genuinely historical or unresolved reconciliation material follows a Markdown rule
and `## History`. A record table carries standing, currentness, placement, identity roles,
aliases and unresolved dimensions. A Canonical responsibility can have Proposed
placement. A Current record can be Proposed or Candidate. Historical assertions
and Pending Adjudication records remain available without becoming current facts.

The recovered catalogue supplies 946 source records: 845 current, 69 pending
adjudication and 32 superseded. Semantic standing is independently 716 Canonical,
227 Proposed and three Candidate. All 1,096 relationship assertions survive:
634 native edges and 462 metadata/findings. Framing, responsibility and enablement
must not silently become Composition. The 214 unbound 1.0.4 objects remain a
separate historical reference population, not a competing current catalogue.

## Source correspondence and maintenance

[architecture.json](architecture.json) preserves the reconciled structured
records, definitions, assertions, status, aliases and provenance exactly. It is
a governed capture shared by both projections, not a new metamodel or an identity
register. [correspondence.json](correspondence.json) pins the immutable recovery
and source boundary. [motivation.json](motivation.json) records explicit typed
current and historical Motivation selections and exact source assertions. The
current Motivation definitions are checked against their complete live RADS
sections. Historical source fingerprints are checked separately.

The earlier 705-record candidate capture is retained as a source-era validation
and navigation aid. Exact capture keys establish continuity for all 705 records;
names alone establish none. Its original unresolved assertions describe that
earlier capture, and do not override the later reconciled record states here.
Earlier catalogue prose and contextual views remain useful source explanations;
where an exact record now has a changed name, tier, placement or lifecycle state,
this baseline's explicitly qualified assertions govern its synchronised display.
Definitions and boundaries not changed by that reconciliation continue to apply.
The field-level [Documentation classification](documentation-classification.json)
records exact capture keys, text hashes, disposition and applicability evidence.
Unreplaced current explanation, exact text reused as a reviewed current definition
or overview, and explicitly responsibility-preserving Feature relocation are
current without changing their standing. Changed or uncertain semantic material,
definition gaps and non-current records retain historical explanation pending
review. These are presentation classifications, not new architectural decisions.
The capture remains byte-identical to the adopted historical evidence. Its
original source fingerprints and line locators are validated through the exact,
hash-proved presentation notices in
[source-presentation-overlays.json](source-presentation-overlays.json). Only those
insertions are removed in memory for that historical check; a changed definition
or unrecorded notice still fails. Current baseline generation does not use this
source-era normalisation.

Future accepted architecture changes must deliberately update the relevant
canonical assertions, reviewed correspondence and both projections. Do not refresh
the frozen LaTeX snapshot or its transcriber from current sources. Do not use
heading nesting, names or folders to fill missing relationships. A discrepancy
without deterministic source authority requires human adjudication.

An Archi export supplies change input for reconciliation into Markdown RAD and
subordinate governed projection metadata; it does not replace this repository.
Accepted layouts, coordinates, bendpoints and navigation may remain projection
metadata where Markdown has no appropriate representation. LaTeX publication is
strictly `Markdown RAD -> LaTeX RAD -> publication output`, with no normal
reverse-authoring path from LaTeX/PDF. Historical LaTeX recovery is bounded
migration work. The [collaboration lifecycle](../../../archimate/capability-architecture/README.md#authority-and-collaboration-lifecycle)
documents the full review, reconciliation, validation and next-release sequence.

From the repository root:

```bash
python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py
python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py --check
make -C docs/archimate/capability-architecture all
```

Generation reads the current release and never increments it. The separate
historical recovery generator reads its explicit 1.0.5 release fixture. Its
historical report retains the verification of the earlier repository state;
that report is not a claim about current-source fingerprint equality.
