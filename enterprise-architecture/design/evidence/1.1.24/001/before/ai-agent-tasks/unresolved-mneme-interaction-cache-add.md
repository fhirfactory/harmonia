Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

# Harmonia 1.1.24 — Governed ADD: Mneme–Mnemosyne Interaction and Mneme Cache Architecture

## Authority

**Solution Architect:** Mark Hunter  
**Repository:** `fhirfactory/harmonia`  
**Branch:** `Mneme-and-Mnemosyne-Build`  
**Accepted predecessor:** Architecture Continuum 1.1.23  
**Target:** Architecture Continuum 1.1.24  
**Activity:** Governed Architecture ADD — not implementation.

The Solution Architect has explicitly reviewed and approved both complete DRPs:

1. `enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md`
2. `enterprise-architecture/design/offline-design/DRP-Mneme-Cache-Architecture.Final.001.md`

These documents are accepted architectural inputs. Their internal pre-approval status wording is historical and must not be interpreted as a new approval requirement. Preserve the accepted source files and record the subsequent approval in the governed Task/HCRL.

## Instructions

### 1. Establish authority and baseline

Read both complete final DRPs, the current Continuum, applicable architectural axioms, incorporation governance, release policy and completed 1.1.22/1.1.23 HCRLs.

Verify the actual Git branch, HEAD, release state and working-tree condition before modification. Do not rely solely on the historical commit identifiers embedded in the DRPs.

Establish HCRL **1.1.24/001** using the existing Architecture Review Praxis, including iteration-scoped formal Task/Report evidence in:

`enterprise-architecture/design/evidence/1.1.24/001/before|after`

### 2. Reconcile and incorporate the approved architecture

Perform an **exceptionally exception-based** semantic reconciliation.

Codex should resolve determinable mappings itself, reusing established identities, relationships, Features, Requirements and architectural classifications.

Incorporate the approved interaction model containing exactly five exposed contracts:

- `GovernedSummaryReader`
- `GovernedSummaryWriter(CREATE, RETIRE)`
- `GovernedSummarySearch`
- `GovernedReader`
- `GovernedWriter`

`GovernedSummarySearch` is the **only** search capability exposed across the Mneme–Mnemosyne interaction. Reconcile the existing historical `GovernedSearch` ApplicationService accordingly, preserving its identity where semantic continuity supports that treatment. Do not retain or create a sixth interaction method.

Preserve all accepted AV-01–AV-05, FC-01–FC-05, MI-05, MI-06 and SearchResult decisions. In particular:

- IU Summary is the IU projection excluding Content.
- Content references reside in ContentMetadata and are opaque to Mneme.
- Full reads materialise Content, subject to established pass-by-reference exceptions.
- Full writes establish coherent authoritative Content and Summary persistence.
- R1/R2 search examines only IU Summary information.
- External Content is SEP; Summary CREATE manages references without external Content verification.
- Required IU criteria require complete coverage by eligible supported capabilities.
- Indeterminate higher-version writes may be reconciled using an authoritative `version=*` read, subject to correspondence evidence.
- SearchResult items are IU Summary projections, distinct from Mneme's Search Result Set of IU-version references.

Preserve the original Interaction DRP MI-07 RETIRE/DELETE identifier; the later SearchResult refinement maps to the existing MI-03 subject.

Incorporate the agreed Mneme cache architecture:

- **Mneme-Standard-Cache:** distributed, reference-based, Infinispan Server/Hot Rod.
- **Mneme-Workflow-Cache:** embedded Infinispan LOCAL, materialised processing content, resident in applicable Ponos, DigitalTwin and Dokimasia JVMs.
- Cache instances and active generations are not independent authoritative IUs.
- Ergon framework responsibilities for loading, unloading, checkpointing and recovery remain intact.

Preserve the stated MC-01–MC-07 technical/deployment qualifications. Do not claim unsupported multi-site Infinispan guarantees or weaken the established authoritative multi-site write-success contract.

### 3. Maintain architectural boundaries

Do not reopen the accepted Information Unit Framework (1.1.22) or Mnemosyne persistence architecture (1.1.23).

Do not invent new architectural identities, generic models, service interfaces, TopicMask languages, transaction identifiers, deployment mechanisms or persistence responsibilities unless necessary to express an accepted semantic gap.

Ensure the required architectural homes and connected relations are represented across Strategy, Motivation, Business, Application and Technology domains as appropriate. Do not create elements merely to populate every layer.

### 4. Validate and preserve evidence

Apply the established canonical Markdown and projection tooling.

Execute the relevant architectural validations, independent verification, publication/transport comparisons and reproducibility checks.

Retain exact before/after evidence, the formal AI-agent Task and Report, validation results, qualifications and any unresolved discrepancies.

Distinguish:

- Accepted architectural semantics
- Deterministic incorporation decisions
- Deferred implementation/detail choices
- Genuine issues requiring Solution Architect adjudication

Do not silently resolve a genuine outstanding architectural decision.

### 5. Return for Solution Architect review

Produce the candidate 1.1.24 Continuum, pending HCRL Decision Review Log, validation evidence, and reviewable architectural projections.

Present a concise exception-based execution report identifying the incorporated delta, preserved identities, qualifications and only those unresolved issues that genuinely require a decision.

**STOP before final HCRL closure, commit or push.**

Do not commence Mneme or Mnemosyne Java implementation. Wait for explicit Solution Architect acceptance of the actual incorporated architecture.

## Expected outcome

A governed, validated and reviewable **Architecture Continuum 1.1.24 candidate**, faithfully incorporating both approved final DRPs without unnecessary architectural proliferation.

## Material follow-up — candidate-specific CR-001 disposition

Mark Hunter, Solution Architect, subsequently issues
[CR-001 — ACCEPTED WITH QUALIFICATION (1.1.24 only)](../cr001-solution-architect-disposition.md)
for the exact candidate SHA-256 identified there. That input preserves the complete
issued wording. Strict FAIL and representation/presentation qualifications remain;
no model application, remediation or subsequent-release waiver is authorised.
Native-tree/HTML-reader structural review and whole-result acceptance remain pending.
Recording this disposition is provenance maintenance in 1.1.24/001; no new Task,
Report, HCRL iteration or release allocation is initiated. Original Task issuance
time/prefix remains unresolved; capture time is not substituted.
