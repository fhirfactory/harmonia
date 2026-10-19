<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Initial IA element population — Archi handoff

Import `docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml`
into Archi 5.10 as a new model. Retain the exact originating package provenance.

Locate 50 elements at **Business → BusinessObjectMap** and 4 at
**Application → InformationMap**. Archi's native Business/Application/Technology & Physical
resource roots remain native; the separate Views tree follows:

```text
2-BusinessLayer
    1-BusinessCapabilities
    2-BusinessEnablingCapabilities
    3-BusinessObjectMap
3-ApplicationLayer
    1-InformationMap
    2-EnterpriseCapabilities
    3-ApplicationMap
    4-MiddlewareMap
4-TechnologyLayer
    1-DataMap
```

DataMap is empty. The new View folders are empty workbench locations; there are
no generated IA diagrams, nodes, connection lines or semantic grouping elements.
Read source qualifications in Documentation. Every new resource carries its
canonicalId, native type, displayName, unchanged local lexical friendlyName and
completed canonicalName using the governed BO/TOPIC qualifiers. No Artifact/ARTEFACT
qualifier is established. This completes the same unsigned RAD-1.0.10 candidate.

Review the full task report before authoring IA organisation, relationships and
Views. In particular both Endpoint BO interpretations remain unpopulated. Similar
names across levels imply no mapping. Existing Home applies only in its governed
Business scope. Subsequent architect-authored work must provide the evidence for
IA ingestion/reconciliation/round-trip validation; this handoff implements none.
