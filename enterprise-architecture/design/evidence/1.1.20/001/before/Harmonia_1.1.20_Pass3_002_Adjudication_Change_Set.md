# Harmonia 1.1.20 — Pass 3 / Step 4 — Consolidated FC-001–FC-101 Adjudication Change Set

**Iteration:** 002  
**Status:** **OFFLINE REVIEW CHANGE SET — NOT GOVERNED / NOT COMMITTED**  
**Input:** Candidate 017, Pass 3 Step 4 Iteration 001 functional responsibility matrix, and Solution Architect adjudications of FC-001 through FC-101.  
**Reference:** Immutable RAD 1.1.19 functional coverage, not historical element/UUID migration.  
**Original Candidate 017 SHA256:** `50b7877a471d71f35e1444a693339da271ddd7dd7dfb3a9cca7df59f0ff72ae2`  
**Overlay SHA256:** `6a1f50f18a0c99b3b964ec52527e02af11002eb08192a9c98afe1bc513d093b4`

## 1. Executive outcome

**All 101 responsibility bundles have received an explicit disposition**, but only **98 are accepted within coverage/scope**, and **three remain exceptions**. The first coverage review is adjudicated with qualifications; it is **not a claim that every 1.1.19 responsibility has passed unqualified equivalence**. No 1.1.19 or 1.1.20 governed repository content was changed.

| Adjudicated status | Count | Meaning |
|---|---:|---|
| Covered | 86 | Existing equivalent capability; may have an implementation note or already-applied clarification |
| Covered differently | 2 | Functionally equivalent, reorganised; concept/collaboration clarification retained |
| Covered with addition | 1 | FC-076; accepted new CT3, provisional individual Feature wording |
| Sufficient for Harmonia scope | 8 | Equivalent or acceptable at the project boundary; residual details outside scope or implementation |
| Accepted partial — EMR responsibility | 1 | FC-064 remainder belongs to EMR |
| Known gap deferred | 1 | FC-042: gap acknowledged, analysis outside project |
| Open | 1 | FC-059: requires further analysis, no closure |
| Parked | 1 | FC-101: R3.x Data Monitor candidate, no coverage determination |
| **Total** | **101** | **98 accepted; 3 qualified exceptions** |

**Review-gate conclusion:** No basis for a blanket PASS or an assertion of zero regressions. The Solution Architect expressly accepted coverage/sufficiency for the other 98 bundles, with the agreed corrections and boundary qualifications below. FC-042 is a known gap; FC-059 is unresolved; FC-101 parked. The final Continuum acceptance remains a separate governed act.

## 2. Accepted candidate-level amendments

### A01 — FC-001 — Pragma → Contextualiser → Analipsi (wording; no new CT3/Feature)

**Targets:** `E06.01.04`, `E06.02.02`.

- `ActionableTask` remains the abstract identifiable work obligation; `FulfillmentTask` remains an abstract distinct undertaking.
- In actionable-work contexts, a `Pragma` is the common governed work/information basis. The `Contextualiser` individually realises zero or more recipient-specific `Analipsi`, each carrying **its own Pragmaton and undertaking state** to its Ergon or equivalent processing recipient.
- One Pragma may have several independent, concurrent realisations. Their internal Pragmaton state is **not shared accumulated state**. Each supplies its own output/progression, audit, provenance and assurance correlation basis.
- No equation of Analipsi with an Ergon invocation, multiple downstream Ergon traversals or a technical retry; no claim that all kinds of Pragma are ActionableTasks.
- The existing TaskOutcome, ExecutionConcurrency, OutcomeConcurrency and OutputRules relationships remain authoritative and intact.

This is an explicit **representation and relationship clarification**, not a new requirement to invent task-processing capabilities.

### A02 — FC-012 — External provenance acquisition wording (no new CT3/Feature)

**Target:** `E01.03.02 — Information Origin Preservation`.

Accepted wording:

> Preserve and associate provenance for governed information and events, **including provenance captured at external acquisition boundaries**. Record available original-source identity, acquisition context, source attribution and relevant temporal information, distinguishing externally supplied evidence from Harmonia-generated observations and transformations. Preserve missing, uncertain or unverified provenance as explicitly qualified information.

This does not convert Harmonia into the originating clinical authority or confuse Provenance with AuditEvent.

### A03 — FC-016 / FC-033 / FC-074 — Independent Assurance Execution (no new CT3/Feature)

**Target:** `E08.04 — Conformance & Assurance Support`.

Assurance assessment SHALL execute as a **separately governed, logically independent activity** from the service/Praxis/Journey being assessed. It evaluates applicable criteria and evidence, creates attributable findings, qualifications and conclusions; it **does not** fulfil the assessed work, decide its operational outcome or recursively assure itself. Logical separation does not prescribe a separate JVM/deployment. Dokimasia assurance remains governed and R1.x/R2.x reporting/advisory, without autonomous operational or release authority.

### A04 — FC-076 — General-purpose collaboration (one NEW CT3)

**Target:** `E07.02.08 — Governed Collaboration Interaction`, beneath `E07.02 — Governed User Interaction`.

Agreed CT3 scope: (1) directed messaging; (2) direct/group chat and managed Agora/Omilia Rooms/Spaces and membership; (3) capture of **defined meaningful interactions** as governed information/work without promoting every utterance to Pragma; (4) distinguish request/notification of reassignment from **authoritative task reassignment**.

**Four proposed, not separately approved Feature formulations** are shown in the overlay:

- `E07.02.08.F01` — Directed Communication Interaction.
- `E07.02.08.F02` — Managed Conversational Collaboration.
- `E07.02.08.F03` — Defined Collaboration Action Capture.
- `E07.02.08.F04` — Task Reassignment Authority Separation.

Do **not** create a fifth Feature simply to match the existing mechanically uniform population. Keep `E02.04` (distribution), `E06.08` (task assignment/reassignment), and `E07.06.04` (task-context collaboration) as distinct existing contributions. Agora controls; Omilia operates, without assuming a particular physical deployment.

## 3. Recorded design questions (NOT coverage gaps)

| ID | Related FCs | Disposition / boundary |
|---|---|---|
| D01 | FC-002 | How Praxis/Pathway continuation and Mneme Pub/Sub independently authorised selection become destination-specific Petasos handoff instructions, with each source/reason/audit correlation preserved. No automatic union of queues, no mandated Router component; no all-outputs-become-new-Pragma rule established. |
| D02 | FC-001 | Detailed Contextualiser, Analipsi and Pragmaton lifetime, input/output processing and persisted correlation contracts remain downstream; each Analipsi has instance-specific state. |
| D03 | FC-050–FC-053; FC-058; FC-060–FC-063; FC-066 | Digital Twin Behaviour where applicable; optional external rostering integration for FC-063; no automatic mapping to new Enterprise capabilities. |
| D04 | FC-090 | Scope-limited provided and consumed API contracts require further design, but FC-090 coverage accepted as sufficient. |

**FC-002 router decision specifically:** A logical Router may coordinate the result, but *what* continues a Praxis/Pathway and *which* publications/subscriptions match are separate decisions; Petasos owns physical persistent handoff/queue state. The combination contract, source of routing authority, correlation and exception handling require solution design. This is **not** approval of a universal queue union, automatic `output[i]` → Pragma rule or separate deployed Router component.

## 4. Explicit exceptions — do not auto-close

| ID | FC | Standing | Why it remains |
|---|---|---|---|
| X01 | FC-042 | Deferred; outside current project analysis | Community/outreach care context, visit synchronisation and batch reconciliation: deliberately retained as gap; no declaration that it is equivalent. |
| X02 | FC-059 | Open; no coverage decision | Clinical device connectivity, identifiers, endpoints and OA&M metadata: needs adjudication; no catalogue change authorised. |
| X03 | FC-101 | Parked; no coverage decision | R3.x external-system/data-monitoring impact analysis. No new R1.x/R2.x capability claim or CT3. |

FC-042 is a **known missing responsibility left outside current analysis**, not the same as an intentional system boundary with demonstrated adequate equivalent coverage. FC-059 stays **OPEN**. FC-101 stays **PARKED** despite the previously agreed R3.x Data Monitor release direction.

## 5. Earlier 16-priority finding register reconciled

| Finding | FCs | Accepted treatment | Change/exception |
|---|---|---|---|
| P01 | FC-016;FC-033;FC-074;FC-085 | Accepted FC-016 independent execution clarification; no extra CT3 | A03 |
| P02 | FC-051;FC-052 | Digital Twin Behaviour implementation; no new capability | None |
| P03 | FC-048 | Sufficient for Harmonia; remaining clinical detail outside Harmonia scope | None |
| P04 | FC-049 | Sufficient for Harmonia; remaining acute service detail outside Harmonia scope | None |
| P05 | FC-076 | General governed collaboration CT3 accepted; 4 proposed Features | A04 |
| P06 | FC-090 | Harmonia capability sufficient; management/integration technology boundary detailed later | None |
| P07 | FC-063;FC-066 | Digital Twin Behaviour; optional roster integration; no new capability | None |
| P08 | FC-044 | Covered | None |
| P09 | FC-031;FC-059 | FC-031 sufficient; FC-059 remains OPEN | X02 |
| P10 | FC-061;FC-062;FC-078 | Digital Twin Behaviour / sufficiently covered | None |
| P11 | FC-042;FC-069 | FC-042 known gap deferred; FC-069 sufficient within Harmonia, external responsibilities | X01 |
| P12 | FC-075 | Covered | None |
| P13 | FC-016;FC-032 | FC-016 independent assurance wording; FC-032 covered | A03 |
| P14 | FC-053;FC-064 | Digital Twin Behaviour for FC-053; EMR-owned remainder for FC-064 | None |
| P15 | FC-090 | Sufficiently covered; API specifics implementation detail | None |
| P16 | FC-012 | Capture externally sourced provenance at acquisition; wording only | A02 |

## 6. Candidate impact, validation and constraints

| Population measure | Original Candidate 017 | Offline overlay | Net |
|---|---:|---:|---:|
| CT3 | 586 | 587 | +1 |
| Features | 2944 | 2948 | +4 |

No existing CT3/Feature was deleted, renamed or renumbered; existing identifiers and existing Feature descriptions remain unchanged. The only added identifiers are `E07.02.08` and `E07.02.08.F01` through `F04`. There is **no automatic acceptance** of the four draft Feature wordings; the CT3 scope was accepted, not a separate approval of every exact test criterion.

The patch preserves the complete Candidate 017 as a working input; no governed canonical IDs, relationships, source-reconciled ArchiMate XML, repository edits, Codex instructions or release actions are produced. No 1.1.19 historical element identity mapping is created.

**Use of artifacts:**

1. `Harmonia_1.1.20_Pass3_002_FC_Adjudication_Register.csv` — all 101 accepted/dispositioned decisions, original vs final judgement and action/exception.
2. `Harmonia_1.1.20_Pass3_002_Candidate_Changes.csv` — four grouped accepted change requirements; A04 Feature details explicitly provisional.
3. `Harmonia_1.1.20_Candidate_017_Pass3_002_Adjudicated_Overlay.md` — full catalogue with bounded preview applied.
4. `Harmonia_1.1.20_Pass3_002_Adjudicated_Overlay.diff` — mechanical change patch vs unchanged Candidate 017.
5. `Harmonia_1.1.20_Pass3_002_Priority_Reconciliation.csv` — closes or appropriately qualifies previous P01–P16 observations.
6. Review workbook and ZIP for accessible adjudication.

**Next authorised workstream (not performed):** detailed semantic rationalisation of the complete CT1 → CT2 → CT3 → Feature hierarchy, then recheck the no-functional-regression coverage gate against any consolidation, subject to the three explicit exceptions. Any Continuum incorporation requires separate authorisation.
