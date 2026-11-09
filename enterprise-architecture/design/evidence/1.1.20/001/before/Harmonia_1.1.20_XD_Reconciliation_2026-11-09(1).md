# Harmonia 1.1.20 — XD-001–XD-028 Reconciliation

**Date:** 9 November 2026  
**Standing:** Working reconciliation report, **not** an architecture change, adjudication, publication, or acceptance.  
**Basis:** Library `Harmonia_1.1.20_Candidate_017_Pass3_011_XD009_Working_Overlay.md`, `Harmonia_1.1.20_Pass3_011_NR001_Adjudication_Workbook.xlsx`, Pass3/005 screening, accepted XD-008/009 decision reports, and subsequent discussion supplied in this conversation.  
**Scope:** Reconcile recovered XD decisions with existing offline working candidate. Preserve identifiers except where prior accepted decisions expressly retire them. Distinguish historical proposals from accepted implementation.

## Executive finding

The latest located working candidate is **Pass3/011**, at **586 CT3s and 2,941 Features** (offline, unpublished). XD-001, XD-002, XD-005, XD-007 have structurally affected the candidate; XD-008 and XD-009 wording refinements are applied. XD-003, XD-004 and XD-006 are accepted KEEP BOTH with wording work recorded as pending. The remaining XD-010–XD-028 discussions postdate the available working overlay and are **not incorporated** into it merely because their rationale was discussed.

**Do not bulk-apply every later proposed friendly name and criterion verbatim.** Several newer interpretations are materially different from the actual candidate criteria or CT3 boundaries, and some refer to a different identifier. Distinguish direct incorporation, controlled semantic change, and items needing a narrow authority decision.

## Reconciled register

| XD | Source feature pair | Recovered disposition / distinguishing responsibility | Candidate standing | Next action |
|---|---|---|---|---|
| 001 | `E01.01.01.F02` / `E01.01.05.F02` | CONSOLIDATE: one persistence-before-acknowledgement obligation | Applied offline | Retain existing change |
| 002 | `B04.03.03` / `B04.03.04` | Merge critical finding communication with receipt/follow-up | Applied offline; residual coverage qualification | Preserve merger; cross-check XD-018 |
| 003 | `E05.01.02.F04` / `E05.01.04.F04` | KEEP: internal versus external machine trust | Decision recorded; wording pending | Align CT3/Feature wording |
| 004 | `E02.03.01.F07` / `E02.03.04.F02` | KEEP: complete node loss versus intermittent connectivity | Decision recorded; wording pending | Clarify criteria |
| 005 | `E03.05.03.F02` / `E03.05.04.F02` | CONSOLIDATE: release/version-qualified repeatable tests | Applied offline | Retain existing change |
| 006 | `E01.05.02.F02` / `E01.05.04.F04` | KEEP: managed internal media versus externally referenced media | Decision recorded; wording pending | Clarify ownership boundaries |
| 007 | `E07.03.01.F04` / `E07.03.04.F03` | CONSOLIDATE version accessibility; retain source-based cross-domain presentation | Applied offline | Retain existing change |
| 008 | `E06.02.03.F04` / `E06.02.05.F01` | KEEP: Twin execution isolation versus controller input allocation | Applied offline | Retain existing change |
| 009 | `E06.02.03.F05` / `E06.02.05.F04` | KEEP: engine work recovery versus operational context survivability | Applied offline | Retain existing change |
| 010 | `B07.03.01.F04` / `B07.03.02.F02` | KEEP: service offering availability versus capacity adequacy | Not applied; current criteria both address unmet demand | Material Feature/parent semantic alignment required |
| 011 | `E04.01.02.F05` / `E04.01.03.F02` | KEEP: versioned reference-data governance versus ontology management | Not applied; current criteria address historic models/interpretability | Material semantic scope/parent alignment required |
| 012 | `E06.03.02.F04` / `E06.03.03.F02` | KEEP: actual Parcel movements observed versus routing-decision evidence | Not applied; current criteria are route decision visibility/traceability | Align assurance owner and routing evidence owner |
| 013 | `E08.06.02.F01` / `E08.06.04.F03` | KEEP: operational durability versus catastrophic recoverability | Not applied | Clarify failure assumptions and correlate with XD-022 |
| 014 | `E08.08.03.F05` / `E08.08.04.F01` | KEEP: provenance evidence assembly versus selective disclosure | Not applied; current parent/criteria express all-or-nothing release gates | **Material change: reconcile parent `All-or-Nothing Governed Release` before replacing criterion** |
| 015 | `E01.02.01.F03` / `E01.02.03.F03` | KEEP: version-specific retrieval versus version-specification integrity | Not applied | Refine criterion and respect retrieval/custody parents |
| 016 | `E03.04.01.F04` / `E03.04.05.F04` | KEEP: per-protocol acknowledgement versus multi-level correlation | Not applied | Refine wording; protocol ACK is not application success |
| 017 | `E06.08.01.F03` / `E06.08.05.F03` | KEEP: work undertaking versus `Ergon.start()` commencement | Not applied | Clarify Pragma publication, acceptance and commencement boundaries |
| 018 | `B04.03.03.F02` / `B04.03.04.F01` | Original source Feature retired by XD-002; intended-recipient identification versus accountable delivery remains a coverage qualification | Source retired; qualification OPEN | Check retained B04.03.04 obligations; do **not** resurrect retired CT3 automatically |
| 019 | `E05.05.01.F01` / `E05.05.02.F01` | KEEP UNCHANGED: protection in exchange versus custody | Existing criteria already distinct | No change |
| 020 | `E01.01.02.F03` / `E02.01.02.F03` | KEEP: overall Mneme–Mnemosyne preconditions versus Mnemosyne enforcement (later discussion) | Not applied; candidate parent names suggest persistence under E01 and governed access under E02 | **Reconcile named subsystem assignments with parent CT3 architecture** |
| 021 | `E04.04.01.F01` / `E04.04.02.F01` | KEEP: model structural conformance versus information-content conformance (later discussion) | Not applied; candidate second is **Terminology Conformance Checking** and code/value tests | **Check whether intended content conformance includes/replaces terminology, without erasing an accepted separate obligation** |
| 022 | `B12.04.04.F01` / `E08.06.02.F01` | KEEP: accepted work capture/queueing versus pre-processing preservation assurance (later discussion) | Not applied; B12 criterion currently service continuity after failure, not capture/queueing | **Material business responsibility/parent change; reconcile with XD-013** |
| 023 | `E04.02.02.F02` / `E04.02.04.F01` | Screening distinction: approved versioned value-set membership access versus release-stable publication | No later adjudication supplied | Preserve as pending, do not claim accepted |
| 024 | `E01.04.01.F03` / `E01.04.04.F02` | Proposal KEEP: retention-hold preservation versus enforcement at lifecycle-action boundary | Not applied; renaming E01.04.04 considered, not accepted | Confirm naming/boundary with E01.04.05 before changing parent |
| 025 | `B05.04.01.F01` / `B05.04.04.F01` | Later user clarification: myriad encounter details versus links to associated referral/diagnostic/follow-up information | **Candidate criteria concern longitudinal care intent and longitudinal episode continuity, not encounter detail collection** | **Material mismatch: cannot safely replace without parent hierarchy reconciliation** |
| 026 | `E05.05.01.F02` / `E05.05.02.F02` | KEEP: encryption in exchange versus encryption in custody | Already separate in candidate | Clarification optional; no consolidation |
| 027 | **Screening** `E06.02.04.F02` / `E06.02.05.F04` | Later user interpretation: Twin handles incoming non-Twin workloads without assuming Twin semantics | **Conversation named `E05.05.02.F02` instead**, which in candidate is *Encrypted Custody Confirmation*. Candidate E06.02.04.F02 is `NonTwinWorkRecognised`; E06.02.05.F04 already revised under XD-009 | **Identifier/ownership conflict; do not mutate E05.05.02.F02 or overwrite XD-009** |
| 028 | `E02.03.01.F04` / `E02.03.02.F05` | KEEP: distributed information availability versus coherent distributed reference detail (embedded or independent) | Candidate second criterion is **Distributed Access Permission**, not reference coherence | **Resolve permission-versus-reference-coherence obligation and parent CT3 definition** |

## Highest-priority semantic reconciliation points

1. **XD-027 — wrong identifier in discussion.** `E05.05.02.F02` is the encryption-of-custody Feature from XD-026. The actual screened XD-027 pair is `E06.02.04.F02` / `E06.02.05.F04`. User's newer non-Twin workload handling meaning may apply to `E06.02.04.F02`, to another Feature, or require a fresh relationship with Twin input allocation; it must not silently overwrite `E06.02.05.F04` (already defined by XD-009).
2. **XD-025 — proposed encounter semantics conflict with catalogue meaning.** `B05.04.01.F01` is `CoherentLongitudinalCare` (care intent consistency). `B05.04.04.F01` is `LongitudinalEncounterContinuity` (distinguishing events belonging to episodes). They do not currently encode the encounter-detail-versus-linked-context pair described in the later discussion. Search the correct capability homes before reassigning identities.
3. **XD-014 — selective disclosure reverses the all-or-nothing naming/criteria.** The revised intent is valid as a proposal but requires reconciliation with the parent `E08.08.04 — All-or-Nothing Governed Release`, and a distinction between a complete assembled evidence universe and a permitted disclosed subset. Avoid leaking the existence of protected records through qualification.
4. **XD-022 — business continuity changed into work intake.** `B12.04.04.F01` currently asserts recoverability after bounded failure. Turning it into initial capture and queuing changes its parent capability semantics; identify whether initial capture has a different home before overwriting the business outcome.
5. **XD-021 — terminology conformance is not the whole content conformance problem.** `E04.04.02.F01` currently tests code/value terminology. Replacing that with generic content conformance may widen or delete a specific accepted guarantee.
6. **XD-028 — reference availability/coherence is not access permission.** `E02.03.02.F05` currently specifies distributed access permission. Clarify whether the intention is to refine a misleading criterion, create/reuse another Feature for coherent references, or retain both obligations under separate existing homes.
7. **XD-020 — subsystem direction differs between historical screening and new dialogue.** Validate the actual Mneme/Mnemosyne ownership against CT3 parents before assigning a specific persistence-engine obligation to `E02.01.02`.
8. **XD-010/011/012 — proposed titles change subject and test.** Treat as architectural semantic refinements needing parent alignment, not simple cosmetic renames.
9. **XD-018 — retired source does not imply closed clinical coverage.** Verify two residual tests: an identifiable intended recipient; and accountable delivery/notification to the correct responsible party, including related time-bounded acknowledgements and escalation where applicable.

## Incorporation approach (proposed, not executed)

- **Preserve offline work:** start from Pass3/011; retain applied XD-001/002/005/007/008/009 decisions and 586 CT3 / 2,941 Feature starting population.
- **Low-risk text reconciliation:** incorporate accepted wording where architectural owner and subject match, including XD-003/004/006, XD-013, XD-015/016/017, and clarify XD-019/026 only if needed. Keep friendly names/criteria accountable and measurable.
- **Controlled semantic reconciliation:** XD-010/011/012/014/020/021/022/025/027/028 need a check against enclosing CT3 and other obligations before rewriting. The conversation's *architectural distinction* is evidence of intent; it does not automatically prove the same pair of catalogue identifiers is the right home.
- **Unresolved scope:** XD-018 remains a qualification, XD-023 lacks a definitive later disposition in supplied dialogue, XD-024 has proposal-level wording and parent-name uncertainty.
- **Boundary:** no governed publication, canonical identity changes, commits/pushes, new Feature creation, or deletion is authorised by this reconciliation report.

## Verification performed

- Read the actual offline Pass3/011 catalogue and checked the existing single-line definitions for the XD-010–XD-028 Feature identifiers against the later discussion.
- Confirmed direct conflicts on XD-014, XD-020, XD-021, XD-022, XD-025, XD-027 and XD-028.
- Reviewed the Pass3/005 screening and Pass3/011 workbook surfaced in the Library, including the accepted early decisions and XD-018 qualification.
- Did **not** claim that post-009 decisions had been applied to the catalogue or that the repo was modified.
