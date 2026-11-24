<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Reader-facing inspection for Solution Architect review

Codex inspected these rendered samples from the generated 1.1.11 PDF, alongside
the [selected navigation excerpt](navigation-excerpt.md), complete generated
Manifest/browser hierarchy and source-to-projection correspondence. This is
**agent inspection evidence, not Solution Architect acceptance**. Physical PDF
page numbers below include front matter; printed page labels differ.

The inspected PDF is 550 pages, SHA-256
`552120ecdcb79eaa58d301588352210f06e952dd325e70bd3120ea07ad106a9e`.
[Inspection metadata](inspection.json) pins the PDF and rendered PNGs. The
[full outline inventory](pdf-outline.json) accounts for all 1,968 canonical
navigation nodes and their distinct destinations; the other two PDF bookmarks
are the reading guide and architectural name index. It confirms eight-component
deep numbering without claiming semantic acceptance from bookmark counts.

| Representative inspection | Rendered evidence | Observation |
| --- | --- | --- |
| Motivation and actual Stakeholder context | [23](page-23.png), [24](page-24.png) | Motivation retains its architectural subject spine. Healthcare Provider and Patient appear as contextual references against actual historical Drivers; Health Network and Care Team appear against Clinical Interoperability. Each reference identifies the index identity, current index coordinate, historical standing and actual Association ID. No association is inferred for the six unassociated canonical Stakeholders. |
| Strategy Business Capabilities and remediated pair | [95](page-95.png) | II.1.2.1 Care Access & Coordination is the current subject. Its R.2.1 predecessor is an explicit history link with preserved identity and superseded standing, rather than an indistinguishable peer. |
| Strategy Business Enabling Capabilities | [163](page-163.png) | II.1.3 begins with the proposed/current Care Administration Enablement. Its history is linked separately. Other superseded source nodes with no adjudicated successor remain explicitly qualified, rather than being silently reassigned. |
| Distinct BC exchange responsibility | [109](page-109.png) | II.1.2.4.2 Health Information Exchange remains the Business Capability under Connected Health Services. Its stated business meaning remains intact. |
| BEC exchange successor and differently named predecessor | [261](page-261.png), [521](page-521.png) | II.1.3.9.10 Health Information Exchange Enablement is the enabling responsibility. It links to R.2.191 historical Health Information Exchange; that history links back to this BEC successor, rather than to the same-named BC. |
| Complete CT1 → CT2 → CT3 contextual preamble | [323](page-323.png) | III.4.1 Care Administration Enablement → III.4.1.1 Care Plan Administration → III.4.1.1.1 Community Care Coordination appears on one page. All three occurrences are marked Capability context and identify their canonical Strategy coordinate, name, immutable ID and standing. Existing Services and Functions follow that context. |
| Another CT2/CT3 context and behaviour | [331](page-331.png) | Episode & Encounter Administration → Acute Care Monitoring & Event Coordination leads to Business Services and Acute Admission Dossier Query, retaining actual canonical references and behavioural content. |
| Deep Feature path and preserved missing definitions | [343](page-343.png) | III.4.2.1.4.1.1.1 Identifier Create is an eight-component coordinate under Person Identity Administration → Identifier Capture → Business Services. Canonical absence of a definition remains explicit; no description was manufactured. Existing Service and Process identities remain separate. |
| Application hierarchy | [447](page-447.png) | IV.3 Applications → Agora → Application Overview → canonical Agora preserves the independently qualified application responsibility. No application taxonomy redesign is supplied. |
| Cross-cutting reference boundary | [481](page-481.png) | The separate title says “Reference navigation — R Cross-Cutting References”, outside the architectural Parts I–VI. R is not an additional Motivation decomposition subject. |
| Stakeholder Index | [483](page-483.png) | R.1 explicitly describes cross-cutting discovery, including Stakeholders with no represented canonical Motivation association. Care Team's actual historical association is visible; current Clinicians & Care Teams is explicitly unassociated. The full index contains all ten canonical identities. Existing source prose about concerns remains source prose and is not converted into native associations. |
| Historical identity accessibility and return link | [488](page-488.png) | R.2 explicitly describes history/provenance rather than current Capability subjects. R.2.1 preserves Care Access & Coordination's old ID and superseded standing and links to II.1.2.1 with its actual direct-correspondence disposition. |

The three actual internal PDF links on page 323 resolve to the canonical Strategy
CT1, CT2 and CT3 on physical pages 163, 164 and 165. This was verified against
real PDF GoTo annotations and named destinations, not only LaTeX source strings.
The browser DOM smoke check verifies search/filter/selection and complete
hierarchy coordinates; it does not claim pixel-based browser inspection.

Archi was actually exercised separately. All 89 Capability-context folder labels
survive its native round trip. The four leaf Stakeholder contextual navigation
items do not survive as organisation labels; the real Associations and explicit
context metadata on the Motivation subjects and Stakeholders do survive. See
[the transport limitation inventory](../exchange-navigation-limits.json).
This is an explicit representation limit, not loss of canonical architecture.

No newly reproducible coordinate collision was found. The reported extra I.6
remains **not reproduced**, with no repair attributed to it. Known superseded
ancestry, pending descendants and missing behavioural definitions remain visibly
qualified. No new architectural meaning was adopted during inspection.

The Solution Architect should review these representative and structurally
significant portions of the actual generated publication before HCRL acceptance,
under the bounded checkpoint added to the existing Review Praxis. This evidence
does not require or substitute an exhaustive page-by-page review.
