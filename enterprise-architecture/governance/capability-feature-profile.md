<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Capability / Feature framing profile

> **Current EA operational succession (RAD-1.1.6):** This maintained contract
> is promoted under the Solution Architect's instruction to sever Legacy-RAD
> operational dependencies. Current sources and procedures are the
> [Continuum Representation](continuum-representation.md),
> [Incorporation Praxis](continuum-incorporation-praxis.md),
> [Archi workbench](../design/archimate/README.md) and
> [EA tooling](../tooling/README.md). Release-specific migration/adoption
> accounts and links labelled “Historical source” preserve decision provenance;
> they supply no current configuration, schema, source capture or authority.
> The promoted rules retain their existing precedence, qualifications and
> unresolved matters. Historical identity registers, RAD source adapters,
> publication slices and processing layouts are not current procedures.

<a id="bec-composition-hierarchy"></a>

#### Business Enabling Composition hierarchy

**Solution Architect decision — 2026-10-19 (Australia/Sydney).** Within the
Harmonia Business Enabling Capability hierarchy, an accepted native ArchiMate
`Composition` relationship from a CT3 Capability to a Feature Capability is the
canonical semantic representation that the Feature is framed by, and semantically
contained within, that CT3. The governed semantic hierarchy is:

```text
CT1 → Composition → CT2 → Composition → CT3 → Composition → Feature
```

Each Composition runs from whole/source to part/target. A Harmonia Feature remains
native ArchiMate `Capability`, with Harmonia classification `Feature`
(`harmoniaType=Feature`); it is not CT4. Capability-specialisation meaning does not
require a native `Specialization` edge in place of this Composition.

Model-tree/folder nesting, View membership, View geometry or visual placement,
naming proximity and navigation organisation SHALL NOT independently establish
this semantic containment. They may project or navigate the governed architecture;
they are not substitutes for the semantic Composition relationship.

This decision governs **what an accepted CT3 → Feature Composition means**. It
does not accept any particular submitted Composition, establish missing ancestors,
promote existing architectural standing or authorise reconciliation/publication.
Acceptance of an instance remains separately adjudicable under the
[Change Review contract](archimate-ingestion-change-review.md).
Once accepted, its framing/containment meaning follows deterministically from this
rule and needs no second instance-level interpretation decision. Existing
incomplete ancestry and identity uncertainty remain explicit under the current representation and identity/naming contracts.
The decision arose from the common question exposed by CR-016–CR-021 in
[Historical source: 1.0.8/run-001](../../docs/archimate/processing/ingestion/1.0.8/run-001/change-review.md#shared-metamodel-decision);
those review findings are traceability evidence, while this subsection establishes
the durable metamodel authority. Realization-to-Feature logical context is outside
this decision.
