<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi 5.10.0 final human inspection — Release 1.0.6

Mark performs final Open Exchange import and human acceptance in **Archi 5.10.0**.
Import `Harmonia-Architecture-Baseline.xml` as a new model. Automated checks prove
Exchange/XSD structure, exact source correspondence, serialized order and layout
invariants; they do not prove native GUI behaviour. No successful GUI import is
claimed. The prior native CLI attempt failed to initialise GTK.

## Landscapes and natural drill-down

Use **Views / 1-Strategy / Capabilities** and repeat the BC/BEC checks in
**Views / 2-BusinessLayer**. Landscape must be the first **View** inside its
own context; classified subfolders may appear before View entries in Archi.
Landscape display Names carry a local **0-** prefix to sort first in Archi;
the descriptive Names below omit that prefix for readability. Both serialized
order and name order are validated. Confirm native ordering. No separate
Landscapes folder should appear.

1. Open **1-Business Capabilities / Business Capabilities — CT1 Landscape**.
   It should show all 18 governed CT1 Business Capabilities, no CT2/CT3/Features.
2. Drill into **[CT1] Care Access & Coordination** and open its **CT2 Landscape**.
   Show that context and exactly Care Access Management, Care Scheduling & Capacity
   Management, Care Coordination & Transition, Care Support Logistics Management
   and Service Delivery Preparedness Management. No grandchildren belong here.
3. Open **[CT2] Care Access Management / Care Access Management — CT3 Landscape**.
   Show the context and exactly Care Intake Management, Care Referral Management
   and Care Eligibility Determination.
4. Open **2-Business Enabling Capabilities / Business Enabling Capabilities — CT1
   Landscape**. It should show all 7 current CT1 BECs, no deeper descendants.
5. Drill into **[CT1] Entity Information Enablement / Entity Information Enablement
   — CT2 Landscape**. Its seven direct CT2 children include Client Administration
   and Provider Administration. Then open **[CT2] Client Administration / Client
   Administration — CT3 Landscape**. Its four direct children are Person Identity
   Administration, Healthcare Subject Profile Administration, Client Relationship
   Administration and Client Privacy Guardrail Management.
6. Open **[CT3] Person Identity Administration / Person Identity Administration —
   Feature Landscape** in both Strategy and Business View projections. Its first
   View shows the CT3 and exactly five Features: Cross-Authority Identifier
   Correlation; Governed Identity Correction; Identifier Resolution; Identity Alias
   Association; Identity Source & Correction Lineage. No Business behaviour is
   included in this immediate decomposition landscape.
7. Inspect a natural terminal: **1-Business Capabilities / [CT1] Individual Care
   Delivery / [CT2] Individual Health Assessment / [CT3] Health History Assessment**.
   Retain its intentional Overview anchor. BC has no governed Features; no BC
   Feature example or invented Feature landscape should appear.
8. The existing Enterprise catalogue under Strategy follows the same direct-child
   rule. Application coverage boundary Views still express non-expansion.

## Person Identity Administration — three consistent projections

Resource path: **Business / Business Enabling Capabilities / [CT1] Entity Information
Enablement / [CT2] Client Administration / [CT3] Person Identity Administration**.

| Feature context | Functions | Processes | Services |
| --- | --- | --- | --- |
| Cross-Authority Identifier Correlation | Correlate Person Identifiers | — | Person Identifier Correlation |
| Governed Identity Correction | Apply Governed Person Identity Correction | Governed Person Identity Correction Process | Person Identity Correction |
| Identifier Resolution | Resolve Person Identifier | — | Person Identifier Resolution |
| Identity Alias Association | Maintain Person Identity Aliases | — | — |
| Identity Source & Correction Lineage | No established association | — | — |

These allocations come from exact assertions. The Process navigation path follows
its explicit Function→governed_process record; no direct Process→Feature semantic
relationship was added. Empty association contexts remain meaningful.

1. Expand each classified Feature folder in the Business resources and inspect
   its Functions / Processes / Services. Check the table above. The semantic
   Names remain unprefixed; classification appears in navigation folders.
2. At **Views / 2-BusinessLayer / 2-Business Enabling Capabilities**, follow the
   same classified branch. Feature-specific realisation Views belong inside their
   exact Feature folders. For example, the Resolve Person Identifier realisation
   View belongs inside **[Feature] Identifier Resolution**. No generic CT3-level
   Feature Realisations collection should remain.
3. **Person Identity Administration — Behaviour 1** remains directly in the CT3
   context. It must not be placed beneath one Feature.
4. Open that Behaviour View. The CT3 is above Feature columns; each Feature's
   associated behaviour is beneath it. Inspect the correction Function, Process
   and Service together. Confirm readable spacing, labels, actual relationship
   endpoints and reduced crossing. Layout does not assert a mandatory chain.
5. Compare the same Feature-bearing branch in Strategy: it retains complete
   CT1→CT2→CT3→Feature framing and its Feature Landscape; Business behaviour remains
   in Business projections. Candidate and Proposed standing remains in Properties.

## Shared, terminal and uncertain cases

Shared behaviour retains its recorded Capability resource home because the
single-home convention supplies no preferred Feature. For example, **Manage Theatre
Case Progression** relates to two Features; its Process likewise has contextual
paths through both. Confirm multiple visual appearances reference the same
semantic ID, and realisation Views sit under their exact Features. Review the 13
listed shared-resource cases in the completion report. No arbitrary resource home
under one Feature should be selected.

Inspect **Health Information Communication** under the Business Enabling pending
population and read its adjudication note/currentness in Properties. Also inspect
an unresolved Feature assertion from the report: it remains metadata/findings,
not a silently created relationship. Historical 1.0.4 reference population and
unresolved identity crosswalks remain separate. Do not treat display as approval.

## Numbering, Documentation and authority

- The View root sequence remains **0-Motivation, 1-Strategy, 2-BusinessLayer,
  3-ApplicationLayer, 4-TechnologyLayer, 5-Implementation**.
- Native resource roots contain no redundant `Strategy / 1-Strategy`,
  `Motivation / 0-Motivation` or `Business / 2-BusinessLayer` construction.
  Resource labels are human-readable and unnumbered.
- Inspect **Care Access Management** Documentation: definition and current
  explanation, including Intent/Boundary where available, appear before History.
  Inspect pending **Health Information Communication** for retained uncertain
  predecessor material below `---` / `## History`. Structured metadata stays in
  Properties. The 396-entry / 675-field current classification and 212 historical
  fields across 98 entries are unchanged.
- Markdown RAD remains source authority; Archi edits are reconciliation inputs;
  LaTeX is downstream publication. Existing Exchange schemas remain unchanged.
- Confirm release Properties say **1.0.6**. Nothing has been committed. Record
  human acceptance or remaining projection findings after these checks.
