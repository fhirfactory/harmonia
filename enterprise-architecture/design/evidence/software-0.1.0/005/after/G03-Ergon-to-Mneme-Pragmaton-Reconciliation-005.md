<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# G03 Ergon–Mneme Pragmaton interaction reconciliation · Iteration 005

**Origin** · Architecture 1.1.26 · `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`).  
**Review correspondence** · [G03 Task][task] · [Source Draft.002][previous] · [Interaction Draft.000.003][candidate] · [Cumulative HCRL.004 / DR-12][hcrl]

## Preserved interaction semantics

| Draft.002 concern | Retained responsibility or constraint |
| --- | --- |
| Contextualisation and return | Mneme owns acquisition, contextualisation and subsequent information management. Common Ergo/Ergon mediates processing entry and return through its Loader and Unloader. Mnemosyne establishes authorised durable state under the governed interaction. [Ergo roles][ergo] · [Five-contract interaction][persistence] |
| Processing and results | Ergon performs bounded processing. Working changes, new outputs and execution outcome retain independent semantics. Existing Praxis continuation and originating-work resolution remain governed separately. [Pragma Model][pragma] · [Publication/subscription][publication] |
| Protection and recovery | Preserve Themis authorisation, evidence correlation, source IU version and durable execution/working-state separation. Recovery reconstructs required working information at the existing boundary without resuming inside a failed Ergon. [G01][security] · [IU authority][iu] · [Two-cache architecture][caches] |

## Incorporated decisions

- **Establishment and use.** The identified Pragmatosis or specialised realisation anchors context establishment. ContextLoader collaborates with Mneme to establish and expose WorkSpace. WorkSpace is the access abstraction for one Mneme-managed Pragmaton. The ProcessingEngine uses it for authorised local working values, lazy reference resolution and additional governed acquisition. Digital Twin and Pylai retain their specialised entry semantics. [Pragma Model][pragma] · [Pragmaton model][pragmaton]
- **Access mediation.** The associative information-space analogy guides access by identity, characteristics and relationships. The logical adapter mediates further access through Mneme under applicable Themis PEP/PDP controls. R1/R2 Summary-only search and full-read materialisation with the existing large-binary pass-by-reference exception are preserved. The adapter's interface correspondence remains a review matter. [Pragmaton model][pragmaton] · [Five-contract interaction][persistence] · [G01][security]
- **Cache support.** Workflow-local working information uses the existing per-JVM embedded LOCAL Workflow Cache and its isolation and validity constraints. Standard Cache retains its summary/reference role. Exact-version correspondence, transient-state recovery and MC-01–MC-07 remain binding. [Workflow Cache][workflow] · [Two-cache architecture][caches]
- **Completion and failure.** Context Unloader returns working changes through Mneme save. New IUs become Pragma Outputs for the separate PublishingMask process. Work execution, access, save, authoritative persistence and Petasos delivery outcomes remain independently attributable, with denied, unavailable and indeterminate outcomes retained where applicable. The reference, working value and unload result each carry their own meaning under the existing authority contracts. [Pragma Model][pragma] · [Publication/subscription][publication] · [Explicit Indeterminate Outcome][indeterminate]

## Historical discrepancies and correspondence

| Earlier wording or question | Candidate treatment and incorporation obligation |
| --- | --- |
| Receiving and returning the Mneme-managed Pragmaton, with a whole-envelope-or-view choice | Record WorkSpace access and governed return of working changes. The conceptual choice is settled at design level. Formal association and lifecycle, canonical representation and permitted access remain under review. [Source Draft.002][previous] · [Pragmaton model][pragmaton] · [Earlier interaction proposals][previous-evidence] |
| Mneme Context Load completes before Ergo exposes the already-loaded envelope | Preserve the expected-resource preload as the ordinary preferred path and Mneme ownership of acquisition. Document the Loader's collaboration with Mneme, specialised initial context and subsequent authorised resolution. Historical Context can also mean the Praxis requirements or security context. Preserve those meanings and reconcile the owning elements explicitly. [Ergo roles][ergo] · [Pragmaton model][pragmaton] |
| Direct WorkSpace access, lazy resolution of a held reference, and explicit ad hoc search/acquisition | Treat these as distinct access cases. An empty local working set supplies no authoritative absence determination. Preserve current R1/R2 Summary-only search and the deferred R3.x criteria-driven dynamic Context scope. Establish the permitted results and existing interface correspondence without creating another access contract. [Ergo roles][ergo] · [Ad hoc access candidate][adhoc] · [Five-contract interaction][persistence] |
| Save as return of resulting working state | Retain Unloader versus Mneme Save and the governed full-writer boundary. Preserve separation from output publication and work completion. A save request, cache update or successful unload supplies no proof of an authoritative write. [Source Draft.002][previous] · [Workflow Cache][workflow] · [Five-contract interaction][persistence] |

## Remaining Solution Architect decisions

Confirm canonical correspondence for historical `Context`, Load/Save, Ergo/Ergon and the adapter. Decide the minimum work, source-version and durable checkpoint evidence for recovery and the outcomes when initial contextualisation cannot safely complete. Confirm allowed associative criteria, admission versus bounded return, authority changes, non-disclosure and denied/unavailable outcomes. Set what information and evidence must cross unload/save, and the formal association and lifetime of Pragmaton. Prior G03 scheduling, selector, handoff, specialisation and trusted-context obligations carry forward. API signatures, lookup methods and physical storage remain software design. [Interaction candidate][candidate] · [Pragma model reconciliation][pragma-evidence] · [DR-12][hcrl]

## Downstream corrections for review

- **Petasos–Mneme Draft.002 §§3.2–3.5.** Reconcile the wording about establishing Pragma on receipt with the subscriber-specific realisation established through Mneme before handoff. The parcel identifies that realisation by reference. Governed authoritative state and resolvable lineage precede acknowledged durable acceptance. On delivery or redelivery, resolve or recover the same undertaking and its progression. Rebuild its transient Pragmaton through the execution-side interaction when governed execution or recovery requires it. Preserve origin, subscriber, applicable IU identity/version, scheduling and security/provenance correspondence. Decide the minimum handoff evidence and acknowledgement ownership under DR-12 without deriving information authority from transport metadata. Keep Petasos delivery, execution, save and persistence outcomes separate. [Petasos–Mneme Draft.002][petasos] · [Publication/subscription][publication] · [Pragma model reconciliation][pragma-evidence]
- **Ad hoc access and companion draft correspondence.** Carry the agreed access and ownership boundaries into the next applicable revisions. Review ad hoc result admission versus bounded return and distinguish explicit acquisition from lazy resolution. Align older Pragmaton introductory wording and publication/subscription references when those drafts next change. [Ad hoc access candidate][adhoc] · [Pragmaton model][pragmaton] · [Earlier interaction proposals][previous-evidence]

## Review checks

Compare ordinary and specialised context establishment. Trace an immutable input version across initial load, local access, lazy resolution, an authorised additional result and cache loss. Check refusal on changed or unavailable authority, and recover the same durable realisation without internal resumption. Verify that working changes, new Pragma Outputs and execution completion remain traceable when save or delivery is indeterminate. These are candidate review checks and future conformance cases. [Interaction candidate][candidate]

[task]: ../before/ai-agent-tasks/unresolved-g03-ergon-to-mneme-pragmaton-reconciliation-task.md
[previous]: ../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md
[candidate]: ../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md
[pragma]: ../../../../offline-design/DRP-Mneme-Pragma-Model.draft.000.003.md
[pragmaton]: ../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.003.md
[publication]: ../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[adhoc]: ../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.001.002.md#4-architectural-definition
[petasos]: ../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md#4-architectural-definition
[security]: ../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[hcrl]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[previous-evidence]: Pragma-Publication-and-Subscription-Reconciliation-005.md#proposed-ergonmneme-interaction-amendments
[pragma-evidence]: G03-Mneme-Pragma-Model-Reconciliation-005.md
[persistence]: ../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[caches]: ../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[iu]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[ergo]: ../../../../../continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md
[workflow]: ../../../../../continuum/application/application-function/0bddade1309e426aa84d0cd97c35742a.md
[indeterminate]: ../../../../../continuum/motivation/requirement/fc732b64fc564127b73174e28d9ccce8.md
