# Harmonia 1.1.20 — FR-001–FR-009 Accepted Feature Rationalisation Adjudication Package

**Document type:** Governed adjudication input / bounded incorporation instruction  
**Programme:** Harmonia Capability Model Consolidation — 1.1.20, Pass 3  
**Standing:** Solution Architect decisions accepted in review; **pending controlled incorporation and verification**  
**Scope:** Nine reviewed items (FR-001 through FR-009)  
**Authority of architecture:** Governed Markdown Architecture Continuum under `enterprise-architecture/continuum/`; this document is an input, **not** a publication or an independently authoritative replacement.  
**Publication/commit authorisation:** **None**. Do not publish the Continuum, close the pending HCRL, commit, or push on the strength of this document alone.

## 1. Purpose and interpretation

This package consolidates all nine Feature Rationalisation (FR) decisions expressly adjudicated by the Solution Architect in the current review. It replaces the *proposed dispositions* in the historical screening workbook **for these nine FR decisions only**. The screening workbook is discovery and traceability evidence, not controlling authority when its suggested action conflicts with the accepted disposition herein.

The incorporation target is the **actual current** 1.1.20 candidate in the working branch, verified by the repository agent before edits. The earlier Catalogue and workbook extracts used during review are evidence of identities and prior wording, **not assurances that they match the latest repository state**. Preserve subsequent governed changes not explicitly in conflict with these accepted decisions; report any such conflict instead of silently overwriting it.

The package is deliberately specific about accepted semantics and deliberately **does not invent** implementation designs, new Features, new architecture decisions, universal timing thresholds, technical protocols, or solution component allocations.

## 2. Adjudication summary

| ID | Identifiers reviewed | Accepted disposition | Change boundary |
|---|---|---|---|
| FR-001 | `E03.03.03.F01`, `.F02` | **KEEP BOTH — REFINE** | Separate native representation conformance from native content fidelity |
| FR-002 | `B11.05.01.F01`, `.F02` | **CONSOLIDATE** | Retain/refine F01; retire F02 with traceability, without renumbering |
| FR-003 | `E07.02.02.F01`, `.F03` | **KEEP BOTH — REFINE** | Separate programmatic from human action confirmation; broaden the parent CT3 wording |
| FR-004 | `B02.02.03.F01`, `.F05` | **KEEP BOTH — REFINE** | Appropriate reassessment versus timely reassessment |
| FR-005 | `E06.06.02.F01`, `.F03` | **KEEP BOTH — REFINE** | Checkpoint integrity versus workflow continuation position |
| FR-006 | `E07.04.01.F01`, `.F02` | **KEEP BOTH — REFINE** | Working context versus interaction/work position |
| FR-007 | `B12.01.02.F01`, `.F02` | **KEEP BOTH — REFINE** | Evidence comparability versus investment option comparability |
| FR-008 | `B08.06.02.F01`, `.F03` | **KEEP BOTH — REFINE** | Readiness determination versus assessment currency |
| FR-009 | `E07.04.01.F01`, `.F05` | **KEEP BOTH — REFINE F05** | Routine restoration versus recovery after unexpected interruption; do not modify the FR-006-approved F01 |

**Population effect:** Eight reviewed pairs are preserved; only `B11.05.01.F02` is retired. If the latest verified candidate still contains all reviewed active IDs, this is **net one fewer active Feature**, with **no CT1/CT2/CT3 structural changes** and no renumbering. Confirm actual counts from the repository; do not presume older summary totals are current.

## 3. Accepted canonical Feature changes

The following is the **target semantic wording for incorporation**. Codex may apply repository-required syntactic conventions without diluting these distinctions. Preserve identifiers exactly. For all entries, keep assessment criteria / KPIs in their appropriate measurement or assessment location; do not use `proportion of ...`, measured elapsed time, or `PROPOSED / unverified` as the definition of what the capability *is*.

### FR-001 — Native representation conformance versus native content fidelity

**Parent:** `E03.03.03 — Native Representation Preservation`  
**Decision:** **KEEP BOTH — REFINE**.

- **`E03.03.03.F01 — Native Representation Conformance`**  
  **Canonical semantic definition:** The structural and conformance characteristics of a retained source-native information representation can be identified and evaluated against the applicable declared native format or standard, while preserving the distinction between the representation as received and its conformance status. A received representation need not be conformant in order for its original native form to be retained.
- **`E03.03.03.F02 — Native Content Fidelity`**  
  **Canonical semantic definition:** Source-native information content, including content not interpreted or supported locally, remains faithful to the received source without unauthorised alteration through transformation, translation or subsequent handling.

**Rationale:** Format/structural conformance and source-content fidelity are distinct: source content can be preserved exactly even when it is nonconformant, or conformant structure can be maintained despite unauthorised content change. Neither Feature warrants suppressing evidence of invalid native inputs. **Boundary:** `E03.03.04.F01` retains its distinct native payload retention/retrieval obligation; do not merge it into either Feature. Do not conflate content fidelity, format compliance, and retrieval/payload custody.

### FR-002 — Applicable obligation identification and mapping

**Parent:** `B11.05.01` (retain repository's accepted CT3 title)  
**Decision:** **CONSOLIDATE**.

- **Retain `B11.05.01.F01 — Applicable Obligation Identification & Mapping`**  
  **Canonical semantic definition:** Applicable statutory, regulatory, contractual and other authoritative obligations are identified for a governed service or enterprise context and mapped to their authoritative sources, relevant jurisdictions, effective scope and versions, and the contexts to which they apply, with accountable interpretation where required.
- **Retire `B11.05.01.F02 — Applicable Obligation Identification`** as an independently active Feature. Preserve historical identity, provenance, criteria and the explicit consolidation link to F01 in the repository's governed retirement/change mechanism. **Do not renumber surviving Features.**

**Rationale:** F02 is subsumed by the identification aspect of F01; F01 additionally carries the required applicable mapping and authoritative source/scope treatment. No generic deletion or loss of historical evidence is authorised.

### FR-003 — Programmatic versus human action confirmation

**Parent:** `E07.02.02 — Review & Confirmation Interaction`  
**Decision:** **KEEP BOTH — REFINE BOTH AND PARENT CT3**.

- **`E07.02.02.F01 — Programmatic Action Confirmation`**  
  **Canonical semantic definition:** An authorised software component or automated participant, including a Digital Twin or agentic participant where permitted, can explicitly confirm or reject an intended action through a correlatable programmatic interaction, with the action, confirmation disposition and attributable supporting evidence distinguishable from transport delivery or completed execution.
- **`E07.02.02.F03 — Human Action Confirmation`**  
  **Canonical semantic definition:** A human participant is presented with the intended action and relevant consequences when confirmation is required and can explicitly confirm or reject that action, with the response and attributable supporting evidence recorded separately from message delivery and completed execution.
- **Parent CT3 semantic refinement:** `E07.02.02 — Review & Confirmation Interaction` supports review and explicit confirmation/rejection of intended actions by both authorised programmatic participants and human participants, according to their distinct interaction and authority requirements. It does **not** presume that every confirmation requires a human or that automated confirmation is equivalent to human authority.

**Rationale:** The former screening conflated these meanings. Confirmation of **intent** is not proof of completed **outcome**, and transport acknowledgement/delivery is neither. Preserve other child Features and do not silently rewrite permission/delegation policy. **Agentic direction:** compatible with human / Digital Twin / AI participation without granting AI clinical or other reserved authority merely through participation. This FR adjudication is **not** authorisation to formalise the separately discussed candidate AR-003 or modify Agora/Omilia protocol design.

### FR-004 — Appropriate versus timely needs reassessment

**Parent:** `B02.02.03 — Health Needs Reassessment`  
**Decision:** **KEEP BOTH — REFINE**.

- **`B02.02.03.F01 — Appropriate Needs Reassessment`**  
  **Canonical semantic definition:** Changes in a person's condition, needs or circumstances that warrant reassessment result in an appropriate reassessment addressing the relevant changes and their implications.
- **`B02.02.03.F05 — Timely Needs Reassessment`**  
  **Canonical semantic definition:** Required reassessments occur within timeframes appropriate to their clinical urgency, applicable care requirements and individual circumstances.

**Rationale and adjacent boundaries:** `.F02 — Meaningful Change Recognition` identifies significant change; `.F01` ensures an appropriate reassessment occurs; `.F05` concerns its timing. Do not invent a universal time limit. Keep Business Capability semantics independent of human/agent/Digital Twin fulfilment mechanisms and independently governed clinical authority.

### FR-005 — Checkpoint integrity versus continuation position

**Parent:** `E06.06.02 — Workflow Progress Checkpoint`  
**Decision:** **KEEP BOTH — REFINE**.

- **`E06.06.02.F01 — Recoverable Checkpoint Integrity`**  
  **Canonical semantic definition:** Workflow checkpoints preserve a valid and identifiable recovery state, enabling their integrity and suitability for recovery to be established following interruption or failure.
- **`E06.06.02.F03 — Recoverable Workflow Position`**  
  **Canonical semantic definition:** Following interruption or failure, the workflow's valid continuation position can be recovered from accepted progress and checkpoint information, without incorrectly repeating completed work or bypassing required activities.

**Rationale and boundaries:** Integrity of a recoverable checkpoint and identification of where work may safely continue are independent. Preserve `.F02 — Checkpoint Boundary Definition`, `.F04 — Dependent Work State Consistency`, and `.F05 — Checkpoint Completeness Evidence` as independently defined responsibilities. Recovery must not imply resuming **inside an interrupted Ergon**; preserve accepted/completed, pending and uncertain distinctions, including long-duration asynchronous work. Do not shift work or execution authority to a messaging system or participant-facing context recovery.

### FR-006 — Restored working context versus participant work position

**Parent:** `E07.04.01 — Persistent Work Context`  
**Decision:** **KEEP BOTH — REFINE**.

- **`E07.04.01.F01 — Restored Work Context`**  
  **Canonical semantic definition:** A participant returning to unfinished work can recover the relevant, authorised information, subject context and work references necessary to continue that work without unintended loss of working context.
- **`E07.04.01.F02 — Restored Work Position`**  
  **Canonical semantic definition:** A participant returning to unfinished work can resume at the appropriate interaction or navigation position, consistent with the restored work context and current authorisation, without incorrectly repeating or bypassing required interactions.

**Rationale and boundaries:** What information and references are being worked with is distinct from where the participant is within that interaction. Apply participant-neutral semantics for human and agentic interaction; do not equate client interaction position with Pragma/Ergon execution checkpoint. Preserve information version and authorisation correctness. `.F05` is considered separately in FR-009, below. Leave `.F03 — Unsaved Work Warning` and `.F04 — Context Expiry Handling` independent.

### FR-007 — Comparable evidence versus comparable investment options

**Parent:** `B12.01.02 — Investment Option Appraisal`  
**Decision:** **KEEP BOTH — REFINE**.

- **`B12.01.02.F01 — Comparable Investment Evidence`**  
  **Canonical semantic definition:** Evidence supporting alternative investments is prepared and assessed using sufficiently consistent assumptions, scope, reference periods and evaluation methods to enable meaningful comparison.
- **`B12.01.02.F02 — Investment Option Comparability`**  
  **Canonical semantic definition:** Alternative investment options can be evaluated against a common decision framework that makes their relative value, costs, benefits, risks, constraints and relevant differences explicit.

**Rationale and boundaries:** Comparable underlying evidence differs from meaningful comparison of options themselves. Preserve the distinct `.F03 — Full Lifecycle Cost Visibility`, `.F04 — Benefit and Risk Appraisal`, and `.F05 — Decision Assumption Transparency`; neither F01 nor F02 replaces them. The Business Capability does not confer investment-decision authority on Harmonia or an AI participant.

### FR-008 — Workforce readiness determination versus assessment currency

**Parent:** `B08.06.02 — Workforce Fitness and Readiness Assessment`  
**Decision:** **KEEP BOTH — REFINE**.

- **`B08.06.02.F01 — Current Workforce Readiness Determination`**  
  **Canonical semantic definition:** The fitness and readiness of personnel for designated duties or assignments can be determined and represented accurately against applicable requirements, conditions and restrictions.
- **`B08.06.02.F03 — Readiness Assessment Currency`**  
  **Canonical semantic definition:** The currency and continuing applicability of workforce fitness and readiness assessments can be established against applicable validity periods, assessment requirements and relevant circumstances.

**Rationale and boundaries:** A current duty-specific readiness determination is categorically different from the currency of the assessment evidence supporting it. `.F02 — Duty-Relevant Readiness Criteria` and `.F04 — Restriction Visibility` retain their responsibilities. `.F05 — Reassessment after Change` concerns reassessment triggered by relevant changes, not routine evidence currency; do not merge or rewrite it. A current assessment is not necessarily a current fitness determination, and vice versa.

### FR-009 — Routine restoration versus interruption recovery

**Parent:** `E07.04.01 — Persistent Work Context`  
**Decision:** **KEEP BOTH — REFINE F05 ONLY**.

- **`E07.04.01.F01 — Restored Work Context`**: **Carry forward unchanged from the accepted FR-006 wording above. Do not re-adjudicate or overwrite it independently.**
- **`E07.04.01.F05 — Interrupted Work Context Recovery`**  
  **Canonical semantic definition:** Following an unexpected interruption, a participant's recoverable working context can be reconstructed or restored from available preserved state, with any lost, uncertain or unrecoverable context explicitly identified rather than silently assumed to have been preserved.

**Rationale and boundaries:** F01 restores preserved working context in ordinary return-to-work scenarios; F05 covers exceptional recovery when continuity and preservation are uncertain. Preserve `.F02` as interaction position, `.F03` as unsaved-work warning, `.F04` as expiry handling. A browser/client failure or agent interaction-session disruption must not be conflated with governed workflow recovery in FR-005. Do not promise reconstruction of unavailable information or an agent's private internal reasoning.

## 4. Cross-cutting accepted constraints

### 4.1 AR-001 — Capability semantics versus measurements

Canonical Feature names and definitions must state a tangible capability, service outcome or assurance responsibility. Test criteria, percentage measures, sampled-case proportions, elapsed-time targets and KPI computations are **assessment mechanisms**, not Feature definitions. Preserve meaningful existing measurement obligations and their provenance in a suitable separate measurement representation; do **not** discard their substance merely when removing metric-shaped prose from a canonical description. Do not invent targets, thresholds, SLAs or new technical machinery.

### 4.2 AR-002 — Canonical language

No canonical Feature definition, CT3 description or other authoritative text introduced by this package should carry review-status phrases such as `PROPOSED`, `unverified`, or historical screening conjecture as though they were capability semantics. Historical review/evidence records may retain their original status language for provenance. This package does not authorise indiscriminate deletion or rewriting of historical audit trails.

### 4.3 Identifier and containment integrity

Retain all existing identifiers and parent containment except **retirement of `B11.05.01.F02`** as explicitly authorised. No renumbering, automatic gap-filling, reassignment to another CT3, creation of new Features, or removal of any neighbouring Feature. Historical retirement traceability must be retained by the applicable governed method.

### 4.4 Authority and platform boundaries

- Business and Enterprise Capabilities state what is supported, not a prescribed technology implementation.
- Human and programmatic confirmations have different participation and authority constraints. Message delivery and transport acknowledgements do not equate to action confirmation or work acceptance.
- Themis/security, information governance, workflow durability, execution and audit boundaries remain intact.
- Participant-facing work context and position are distinct from executable Pragma/Ergon progression.
- The separate Agora/Omilia/Digital Twin–Agentic DRP remains an architectural discovery/design input; this package does not independently approve its open target-state questions or protocols.

## 5. Incorporation, validation and reporting expectations

Perform **bounded, governed reconciliation** against the actual current working candidate and its accepted predecessor evidence. In particular:

1. Establish the current branch, published predecessor, latest accepted candidate content, and relevant HCRL standing before modifications. The most recently reported candidate summary was Pass3/013 with **24 CT1 / 145 CT2 / 585 CT3 / 2,936 active Features**, but treat these only as historical reference numbers, not a target to force or an assurance of current state.
2. Apply the **nine accepted FR dispositions** and canonical semantic refinements; implement FR-002 retirement using an existing governed historical-retirement mechanism, preserving provenance and without renumbering.
3. Verify that FR-006 and FR-009 agree on the single final `E07.04.01.F01` definition, and that `E07.04.01.F05` is distinct. Verify that FR-003 parent CT3 is consistent with both human and programmatic forms.
4. Preserve all remaining neighbouring Feature descriptions unless a minimally necessary, evidenced clarification is needed for internal consistency; record that clarification instead of silently introducing further decisions.
5. Validate containment and identifier integrity, active and retired population counts, absence of accidental deletions, cross-references, parent/child consistency and preservation of other previously accepted XD, SR and AR outcomes. Run all applicable repo-native checks. Report checks not executable, rather than treating them as passes.
6. Preserve before/after evidence, exact changed paths, and a decision-to-change trace keyed by `FR-001`–`FR-009`. Report any conflict or ambiguity without defaulting to a superseded rationalisation recommendation.
7. Leave the HCRL in its existing governed pending state unless a separately established closure procedure and explicit Solution Architect acceptance authorise otherwise. **Do not publish, commit or push.**

**Expected active Feature delta:** `−1`, solely for `B11.05.01.F02`, **if** it was active in the verified starting candidate. The counts themselves are a validation result, not an authorisation to manipulate the catalogue until an expected number is reached.

## 6. Codex execution hand-off (bounded task)

### Goal

Incorporate the Solution Architect's accepted `FR-001`–`FR-009` Feature Rationalisation dispositions into the latest governed Harmonia 1.1.20 Capability Model candidate, preserving the approved distinctions and historical evidence.

### Expected Outcome

A coherent reconciled **candidate** with the nine accepted decisions applied, exactly one authorised Feature retirement (`B11.05.01.F02`), no inadvertent containment or identifier changes, validated cross-references and a reviewable execution/evidence report. No publication, HCRL closure, commit or push.

### Context

The Feature Rationalisation screening workbooks supplied *review proposals*, not authority. FR-001 through FR-009 have now been individually adjudicated by the Solution Architect, who explicitly accepted the dispositions and wording refinements collected in §3. FR-006 and FR-009 share an F01 that must remain a single consistent accepted definition. Earlier 1.1.20 work already incorporated XD and AR adjudications and bounded SR reconciliation. The governed source resides under `enterprise-architecture/continuum/`, while ArchiMate and other representations are projections/outputs, not unilateral authoring authority.

### Constraints

Follow all repo governance and current RAD/Continuum authority rules; respect existing accepted release and HCRL state. Do not infer Feature deletion from original workbook suggestions, collapse distinct capabilities, renumber any identifiers, create new Features or make unsolicited architectural decisions. Distinguish canonical capability descriptions from separate KPI criteria (AR-001), and remove review-status wording from canonical text introduced by this reconciliation (AR-002), preserving historical evidence. Treat the separate Agora/Omilia DRP and possible AR-003 as **not** additional change authorisation. No commit/push, release publication or HCRL closure.

### Authorised Activity / Scope

Discover the latest candidate and relevant baseline; prepare bounded before evidence; reconcile only the nine FR decisions and the necessary parent CT3 refinement for FR-003; retire F02 under FR-002 with preserved historical traceability; preserve all other identifiers and accepted adjudications; run applicable consistency and governance checks; prepare after evidence and report. If changed scopes or accepted revisions conflict, record a precise issue for Solution Architect disposition instead of silently choosing.

### Report

Write the governed execution report **under `.junie/reports/`** (using the repository's established report naming and evidence conventions). Include starting/ending versions and counts; a nine-row FR disposition and implementation table; exact altered paths/identifiers; retirement/provenance handling; before/after validation evidence; checks pass/fail/not-run and qualifications; outstanding decisions or conflicts; and explicit confirmation of **no publication, HCRL closure, commit or push**. Reference existing governed evidence paths where applicable.

## 7. Decision standing and exclusions

**Accepted:** FR-001 through FR-009 as itemised; AR-001 and AR-002 treatment as previously agreed; no other FR item remains pending within this nine-item review.

**Not decided here:** Detailed room/space taxonomy, Omilia client/server code modifications, Matrix protocol interfaces, candidate AR-003, AI delegated authority, implementation of interaction semantics, revised workflow runtime design, new KPIs, universal clinical deadlines, new Feature identities or wider model rationalisation.

**Next governance gate:** The Solution Architect reviews the resulting reconciled candidate, discrepancies and verification report and separately decides any HCRL closure, publication or repository commit/push in accordance with current governance.

---

### Evidence basis for this consolidation

- Direct Solution Architect acceptance of each FR-001–FR-009 disposition in the review conversation.
- Historical feature-rationalisation workbooks, especially `Harmonia_1.1.20_Pass3_004_Feature_Rationalisation_Workbook.xlsx` and NR-001 review/adjudication workbooks, as **discovery and identifier evidence**.
- Capability catalogues/working overlays from the 1.1.20 programme for CT3 context and adjacent Feature coverage, as **historical evidence** rather than verified latest repo bytes.
- `Harmonia_1.1.20_DRP_Agora_Omilia_DigitalTwin_Agentic_Interaction.md` for the related, separately scoped architectural exploration (not incorporated by this task).

**End of accepted FR adjudication package.**
