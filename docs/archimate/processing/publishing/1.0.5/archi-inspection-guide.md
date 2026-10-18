<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi 5.9 inspection guide — 1.0.5A

Import **Harmonia-Architecture-1.0.5A.xml** using **File → Import → Open Exchange XML**.
The model is named **Harmonia Architecture — LaTeX RAD v0.2 Recovery (1.0.5A)**.

1. Start at **Views / Capabilities / Harmonia Capability Landscape — v0.2**.
   The three category folders contain CT1+CT2+CT3 overview diagrams. Use their
   nested context Views for progressive inspection; their nodes are the same
   semantic elements found under Strategy.
2. Open **Views / Capabilities / Business Capabilities / Care Access & Coordination /
   Care Access & Coordination Overview — v0.2**. Expect four Canonical CT2 nodes and
   orange Proposed **Service Delivery Preparedness Management**. Open the matching
   child folders: the four Canonical branches have 3 / 6 / 3 / 3 CT3 children;
   preparedness has purple Candidate **Health Service Operational Readiness**.
   Its placement is Proposed. Names/colours do not override Properties.
3. Expand **Strategy / Business Enabling Capabilities** for the reviewed current
   7 CT1 / 76 CT2 / 95 CT3 hierarchy. **Pending Adjudication** and **Superseded**
   are separate branches. Open **Views / Capabilities / Business Enabling
   Capabilities** to follow matching overviews and **Features (framing only)**
   diagrams. Framing folders create no Composition or Specialization edge.
4. Inspect **Views / Capabilities / Enterprise Capabilities** for the explicitly
   Proposed v0.2 hierarchy. The original EC-01–EC-14 elements keep their proven
   identities and names; their new parents/placement are independent Proposed
   assertions. Enterprise remains separate from BC/BEC.
5. Select a node and read **Documentation** and **Properties**. Look for `v0.2 /`
   **Architectural Status**, **Record State**, **Placement Status**, **Structural ID
   Status**, **Architecture Identity Status**, **Adjudication Note**, **Release
   Scope**, **Source Locators**, **Snapshot Record JSON** and **Deferred Relationship
   Assertions**. The latter retains predicates that could not safely become edges.
   Relations are under **Relations / Capabilities** or **Relations / Business**, following
   the source object's path. Select a relationship to inspect its independent status
   and currentness. Historical edges do not supply current navigation parents.
6. Open **Views / Business** for bounded behaviour contexts and **Views / Traceability /
   Feature Realisations** for two-element Function→Feature diagrams. Business
   Functions, Services and Processes live under **Business**, following their
   source responsibility context. Folder proximity creates no ownership edge.
7. Compare the original objects/diagrams under **Unreconciled 1.0.4 Reference** in
   Strategy, Relations and Views. All 219 original Views survive unchanged. The 214
   unbound old BC/BEC objects remain a separate comparison population; matching names
   do not establish identity. The 14 exact EC bindings are reused in the recovery.
8. Open **Views / Recovery Evidence / Recovery Findings — mappings and identity**.
   Open **LaTeX Source Corpus** for all 30 included source files. Select the View
   in the model tree and read its **Documentation**; the canvas note explains this
   route. The complete frozen TeX is retained, including unresolved or unmapped text.
   **Views / Motivation** shows the five explicitly transcribed axiom selections.

Orange = Proposed; purple = Candidate; grey = pending/historical; yellow = Canonical.
Element status, placement, record currentness and release scope are separate dimensions.
Native relationships retain their own source status. Use [the report](1.0.5A-recovery-report.md)
and [the findings ledger](recovery-findings.json) for the limits and architectural
review items. No acceptance, 1.0.5B overlay or commit is implied by this import.
