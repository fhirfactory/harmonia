<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Baseline coverage and representation boundaries

The workbench represents the recovered Strategy and Business records and the
explicitly selected current and historical Motivation architecture. Missing native
Application, Technology and Implementation elements are representation gaps; they
are not evidence that RADS contains no architecture in those domains.

| Projection | Established baseline representation | Boundary |
| --- | --- | --- |
| Strategy / BC / BEC / EC | Reconciled records and explicit decompositions, retaining independent standing/currentness | FT framing is not CT4; Proposed placements remain Proposed |
| BusinessLayer / BC-Grouped-Elements / BEC-Grouped-Elements | Existing Functions, Services and Processes at their exact source responsibility contexts | Mirrored folders are navigation, with no Capability copies or ownership edges |
| InformationMap | No native Application-layer information objects in the recovery | [Domain04](../../04-information-architecture/README.md) has conceptual information architecture; [Domain05 data](../../05-information-systems-architecture/data-and-persistence.md) has application representations. Identity/type/publication crosswalks remain downstream work; do not turn every heading or D row into a DataObject |
| EC-Grouped-Elements | EC hierarchy mirrored as empty navigation context | No inferred EC-to-application allocation. EC-14 retains its explicit non-allocation |
| ApplicationMap | No native ApplicationComponents in the recovery | [Domain05](../../05-information-systems-architecture/README.md) contains candidate/reconciled responsibilities; [component register](../../05-information-systems-architecture/component-register.md) mixes constructs and bundles. C keys are navigation, not common identities |
| MiddlewareMap | No native middleware projection in the recovery | [Middleware map](../../05-information-systems-architecture/middleware.md) records collaboration assertions; no M-row-to-component conversion or inferred flow |
| TechnologyLayer / Implementation | Ordered top-level View locations; no invented semantic objects | [Completion plan](../../architecture-completion-plan.md) retains domain scope and future derivation gates |
| Unreconciled 1.0.4 Reference | 214 unbound historical elements; all 219 original Views; original edges | Names do not establish the missing crosswalk. Fourteen proven EC bindings are reused |
| Historical Motivation | Explicitly typed source constructs and source graph assertions; exact source evidence retained | Historical is not Current. Source projections with unresolved equivalence are not persistent Architecture Identity adoptions |

Validation distinguishes **INVALID** (broken correspondence, identity, reference,
format or source assertion), **UNRESOLVED BY ARCHITECTURAL DECISION** (permitted
pending/candidate/assertion/identity state), and **NOT YET REPRESENTED / FUTURE
ARCHITECTURE EXPANSION** (a deliberately unallocated projection). The latter two
are review information, not automatic validation failures.

No Business, Application or Technology derivation, speculative Application
Capabilities, historical-conversation search, F46 repair, missing traceability,
new identity adoption or architecture adjudication was performed by this task.
