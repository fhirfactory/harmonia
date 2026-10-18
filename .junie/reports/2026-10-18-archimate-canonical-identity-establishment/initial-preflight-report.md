<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Canonical Identity and Governed Naming establishment — preflight stop

Date: 2026-10-18. Branch: `Harmonia-GovernedAccess`.
Starting commit: `d060702d6055bc42ef4e0b64917703636f466faa`.
Starting release: **1.0.6**. Resulting release: **unchanged, 1.0.6**.
Status: **STOPPED BEFORE ESTABLISHMENT — naming authority unresolved**.

The requested establishment has **not** been completed. No Canonical IDs were
allocated and no architectural source, publication, release authority or protected
fixture was changed. The task explicitly requires stopping if a governed name
cannot be established without inference. Inspection confirms two such gaps:

1. There is no approved deterministic `friendlyName` token/source grammar for
   the **1,299 elements**. Agreement to persist and validate the value establishes
   its treatment, but does not define its derivation.
2. **353 elements** lack a governed qualified structural path: **214 historical
   reference Capabilities and 139 Motivation elements**. The Business structural
   grammar does not establish qualified naming rules for every native element
   type or supply these missing contexts.

The exact affected Exchange IDs, native names, native types, source populations,
source keys and available context are recorded for every element in
[naming-blockers.json](2026-10-18-archimate-canonical-identity-establishment/naming-blockers.json).
These are report locators, not allocated identity or a second identity register.
The [complete diagnostic](2026-10-18-archimate-canonical-identity-establishment/active-diagnostic.json)
and [summary](2026-10-18-archimate-canonical-identity-establishment/active-diagnostic-summary.json)
retain the existing metadata and exact observed alias bindings.

## Authority and evidence for the stop

The explicit task decisions establish resource-owned UUID32 identity, EC identifiers
as retained aliases/provenance, the three naming concepts and persisted-and-validated
qualified names. Those decisions supersede incompatible older recommendations about
UUID URNs and registry-owned handles for this authorised migration. They do not
define a friendly-name algorithm, approve missing ancestry or establish a naming
grammar for the remaining element populations.

Repository authority was checked independently of the Increment 1 reports:

| Source | Material finding |
| --- | --- |
| [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), AX-04 | Reuse source capture, publication and validation machinery; introduce no competing identity architecture. |
| AX-14 | Keep lifetime identity, names, source keys, representation IDs, native/Harmonia types, standing and both alias channels distinct. |
| AX-15 / AX-17 | Preserve uncertain outcomes and missing architectural authority; no inferred ancestry, token grammar or naming completeness. |
| [Business metamodel §8.2](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md#82-typed-canonical-identifier-grammar) | Defines structural paths for Capability, Feature and Business Function/Service/Process. Explicitly withholds token vocabularies and structural contexts for other governed Business types. It does not define Motivation naming. |
| [Business metamodel §§8.4–8.7](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md#84-rename-relocation-and-alias-rules) | Preserves historical names/structural identifiers and unresolved ancestry. A lifetime handle does not establish missing context. |
| [RAD → ArchiMate mapping §§2–4, 9](../../docs/markdown/governance/rad-archimate-model-mapping.md) | Source assertions and standing govern; headings, folder placement and similar labels cannot supply missing identities or relationships. |
| [Navigation convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md) | Tier/type prefixes qualify folder labels while semantic element Names remain unchanged. Navigation paths do not establish additional architectural ancestry. |
| [Architecture Completion Plan §8.1](../../docs/markdown/architecture-completion-plan.md#81-ai-context-independence-and-progressive-authority-loading) | Missing or ambiguous authority must be raised under AX-17, rather than recovered from remembered interpretations. |

A scoped search of RADS, the architecture scripts and active publication tools
found no approved `friendlyName` derivation. The current diagnostic explicitly
records that absence; it does not implement such a rule. Existing
`synchronise_baseline.Baseline.chain` follows recorded `parent_key`/`owner_key`
context and can be reused once naming token rules are approved.

The accepted [Increment 1 report](2026-10-18-archimate-roundtrip-increment-1.md)
and [least-step assessment](2026-10-18-archimate-roundtrip-least-step-plan.md)
independently identify these same naming gaps. Their transport result remains
**PASS for Canonical Identity and Governed Naming Transport**. Exact Property
transport does not supply missing architectural naming authority. Whole-model
lossless transport remains unestablished, with the previously characterised
evidence-text, organisation, language, newline, style and ordering findings retained.
No comparator normalisation was added.

## Source representation and implementation inspection

The existing governed resource captures are the appropriate source layer:

| Population | Elements | Existing source / binding |
| --- | ---: | --- |
| Reconciled Capability, Feature and behaviour records | 946 | [baseline/architecture.json](../../docs/markdown/governance/baseline/architecture.json), with exact recovery crosswalk bindings |
| Current/historical Motivation records | 139 | [baseline/motivation.json](../../docs/markdown/governance/baseline/motivation.json), with the publisher's existing exact key/native-ID representation bindings |
| Unreconciled historical reference Capabilities | 214 | Preserved immutable baseline/recovery representations; no demonstrated RAD record crosswalk |

The [baseline source contract](../../docs/markdown/governance/baseline/README.md)
identifies the JSON as governed capture shared by Markdown and ArchiMate, rather
than a new metamodel or identity register. `load_inputs` currently requires the
946-record capture to equal its reconciled historical snapshot, with additional
source/hash and Documentation-classification checks. A future authorised extension
must explicitly distinguish added resource metadata from unchanged captured
historical evidence; it cannot simply bypass those checks or edit the frozen source.

Resource-local fields in these existing captures are the intended authority for
the new metadata, rather than generated Exchange Properties or the 14-record EC
register. No schema extension or final source representation for the 214 historical
reference resources was installed at this stop. Their source-local metadata must
be accommodated in the existing capture model without inventing a RAD identity
crosswalk, new ancestry or a parallel registry.

The active publisher, Property writer, source loader, naming/ancestry validator,
recovery implementation, alias search/rendering, identity validator, baseline
preservation gates and Increment 1 diagnostic/comparator were inspected and left
unchanged. No architecture-change reconciliation was implemented.

## Identity census and allocation status

The existing diagnostic was rerun against the accepted active publication using
`diagnose --baseline-context`. Its **audit execution is PASS**, with no XML/schema,
identifier or reference issues. Future-contract conformance remains **NONCONFORMANT**.

| Measurement | Result |
| --- | ---: |
| Governed elements inspected | 1,299 |
| Valid UUID32 `canonicalId` | 0 |
| Missing `canonicalId` | 1,299 |
| Malformed claims | 0 |
| Duplicate UUID32 values | 0 |
| Multiple claims on an element | 0 |
| IDs allocated by this task | 0 |
| EC alias claims / duplicate EC aliases / frozen-binding conflicts | 14 / 0 / 0 |

**Allocation mechanism: not implemented or executed. UUID version actually used:
none.** The existing identity model recommends random UUIDv4 allocation once;
after naming adjudication, standard `uuid.uuid4().hex` is consistent with that
construction convention and the task's UUID32 representation. This is a proposed
implementation choice, not a claim that IDs have been allocated or persisted.
No semantic UUID derivation or ordinary-publication allocation was introduced.

Persistence, immutability, deletion detection, lifetime non-reuse and protection
against accidental reallocation have **not** been established for new Canonical IDs.
The existing history/binding validation mechanisms remain available for extension.
The retained source already includes superseded/pending records and the model
retains historical reference resources. This inspection establishes no concrete
physical deletion requiring a new identity ledger; none was created.

## Naming and provenance findings

| Naming evidence | Observed result / disposition |
| --- | --- |
| Native element Names | 1,299 single nonempty values; existing display source remains intact. No `displayName` Property established. |
| Legacy short names | 719 published; 34 differ from native Names. They were not mechanically promoted to `displayName`. |
| `displayName`, `friendlyName`, `canonicalName` Properties | Each absent on all 1,299 elements; no partial migration performed. |
| Bound legacy qualified paths | 946 agree exactly with their source; 190 Established and 756 Proposed. Proposed paths remain Proposed. |
| Missing qualified paths | 353: 214 historical Capability references and 139 Motivation records. |
| Qualified-name drift in existing evidence | 0 observed source-path conflicts; this does not prove a future derivation contract. |
| Legacy name-alias / historical structural-ID-alias publication | 44 / 430 owning elements, respectively; unchanged. Source lists retain 46 / 432 individual entries, respectively. |

The semantic migration mapping is settled by the task: old `canonical_id` is
qualified-path evidence for **`canonicalName`**, never **`canonicalId`**. Old
`canonical_name` is short-name source/provenance; it is not automatically the
governed presentation value. No legacy field or alias was renamed or removed.
Names remain separate from EC identifier aliases and representation identities.

The Person Identity record demonstrates the unresolved readable-token choice:

```text
source key:            capability:person-identity
current source Name:   Person Identity Administration
legacy canonical_name: Person Identity
reviewed_name:         Person Identity Administration
name_aliases:          [Person Identity]
structural standing:   Proposed
legacy canonical_id:   CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY
```

Using current names versus retained short names/codes changes whether a readable
component contains `PersonIdentityAdministration` or `PersonIdentity`. The task's
illustration explains the naming concept but does not explicitly adjudicate this
record's current name or define a universal token transformation. Folder `[CT3]`
qualification alone supplies neither decision. Punctuation, acronym handling,
token boundaries, separator escaping and collision treatment also need an approved
deterministic rule before validation can be authoritative.

The three existing duplicate legacy-short-name pairs are retained explicitly in
the diagnostic summary: Demographic Change Distribution, Diagnostic Report
Distribution and Health Information Exchange. They do not establish duplicate
architectural identity. Existing Capability-name uniqueness findings are not
waived by eventual UUID allocation or qualified naming.

No useful name, qualified path, EC identifier or provenance was discarded, so no
new alias records or alias system were needed in this preflight. Existing
historical structural aliases retain their historical channel and meaning.

## Preservation and validation actually completed

All commands were bounded. None timed out or stalled. The complete logs are in
[the evidence directory](2026-10-18-archimate-canonical-identity-establishment/).

| Command / gate | Bound | Result |
| --- | ---: | --- |
| `python3 docs/archimate/capability-architecture/tools/diagnose_roundtrip.py diagnose --baseline-context --output <evidence>/active-diagnostic.json` | 30 s | **PASS audit**; current identity/naming contract remains NONCONFORMANT, as counted above. |
| `python3 -m unittest discover -s docs/archimate/capability-architecture/tests -v` | 150 s | **PASS**, 128 tests in 69.212 s; includes baseline, recovery, publication, navigation and Increment 1 tests. |
| `mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` | 180 s | **PASS**, 90 tests, zero failures/errors/skips, BUILD SUCCESS; Maven elapsed 28.897 s. Raw log retained compressed as `architecture-tests.log.gz`. |
| `python3 -m unittest discover -s scripts/tests -p 'test_architecture_*.py' -v` | 60 s | **PASS**, 25 identity/release tests in 25.506 s; see `identity-release-tests.log`. |
| `python3 docs/archimate/capability-architecture/tools/recover_v02.py --check` | 90 s | **PASS**, protected recovery XML SHA-256 `f85be36ff6edb3c6491dd526c42d4207db9214154da437941b709de34dc106a1`. |
| `python3 scripts/architecture_identity.py validate` | 30 s | **PASS**, 14 existing registered records; no allocation. |
| `python3 scripts/architecture_identity.py bindings --check` | 60 s | **PASS**, all 14 retained EC bindings. |
| `python3 scripts/architecture_release.py check` | 30 s | **PASS**, 1.0.6 / previous 1.0.5. |
| `python3 docs/archimate/capability-architecture/tools/verify_baseline_outputs.py` | 120 s | **PASS**, all 14 generated outputs reproduce byte for byte in independent processes; release unchanged. |
| SHA-256 preservation census | Bounded local read | **PASS**, all 219 pinned inputs unchanged; see `input-preservation.json` and `preservation-result.json`. |

The accepted active XML is unchanged at SHA-256
`d57a5adeeef6b0906b30a02c3ea80cc6ff6308988775e7e3a619db01564ca52a`.
The frozen EC register is unchanged at SHA-256
`aadfaa9c1c6d9d35908036683fff4837537de9fe0683c192e2fd0959fced6073`.
All 1,299 element representations, 852 relationships and their endpoints/types,
1,196 Views, 3,915 visual objects and 1,449 visual connections remain in the same
accepted XML. Its byte preservation establishes unchanged architecture semantics,
Exchange IDs, metadata, relationships and presentation for this task. No
relationship, View, node or visual-connection Canonical IDs were introduced.
Protected recovery, existing Increment 1 evidence/exports and the user's
pre-existing staged/unstaged/untracked work remain intact.

**Tests added: none**, because no migration, validation or publication code was
changed. The requested new positive/negative establishment tests, including
post-establishment deletion/mutation rejection, name-derivation drift rejection
and ordinary-publication reallocation rejection, remain **NOT IMPLEMENTED / NOT
EXECUTED**. Existing diagnostic negative tests passing is not acceptance of a new
source identity contract. Independent regeneration proves preservation of 1.0.6;
it does not prove stable allocation or naming for a release that was not created.

## Release, publication and Archi proof

The [release policy](../../docs/markdown/governance/architecture-release-policy.md)
determines a **1.0.6 → 1.0.7 patch transition** for the completed accepted change set,
assuming no intervening acceptance. Naming establishment and validation stopped
before that acceptance gate. The root authority was not incremented. This report
is a work-in-progress stop record, not a new accepted release; acceptance of a
report-only change set would itself require the policy's patch increment.

**Publication changes: none. New Open Exchange artifact: NOT PREPARED.** The
accepted 1.0.6 artifacts remain at their original paths and hashes, including
`docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml`.
No historical artifact was overwritten and no new baseline was declared synchronised.
Minimum governed documentation/schema updates establishing the new contract remain
dependent on naming adjudication; this report does not purport to install them.

**Mark's new no-edit Archi 5.10 proof: NOT READY.** There is no established new
publication to import, so an exact new import/export path cannot yet be issued
honestly. After establishment, the handoff must name the actual generated input,
pin it, and specify the complete-model no-edit export destination. Mark's real
export must then be checked using the existing Increment 1 comparator for exact
Canonical ID values/owners, all three names, applicable Harmonia types and
element/relationship identity/endpoints. The known unrelated transport findings
must remain visible; no additional normalisation is authorised by this report.

## Required Mark + ChatGPT adjudication and resumption boundary

1. Establish the `friendlyName` derivation/source contract: current readable labels
   versus retained short-name/local-code tokens, exact tokenisation and punctuation/
   acronym rules, qualified separators and collision handling. Preserve current
   versus Proposed standing and historical naming evidence. The Person Identity
   example above needs an explicit interpretation under that rule.
2. Establish `canonicalName` grammar and qualified-context treatment for the 139
   Motivation records and 214 historical reference Capabilities listed in the
   blocker inventory. A deliberately approved root/local naming convention may
   avoid invented ancestry, but none is presently established for this population.
   Missing RAD crosswalks must remain missing unless separately adjudicated.

The source-field ownership, UUID32 format, EC-alias treatment, persisted validation
direction and bounded scope are already authorised; they do not need permission
again. Resume by applying the adjudicated naming rules through the existing source
capture and validators, then implement validation before the separate one-time
UUID allocation, preserve 1.0.6, publish and validate the patch release, and prepare
the real Archi handoff. No additional approval is inferred from this stop report.

**Increment 2 readiness: NOT READY.** Stable Canonical IDs and governed names have
not yet been established. Increment 2, relationship Canonical IDs and presentation
replay / Increment 3 were not commenced.

## Files added by this preflight

- This report.
- `2026-10-18-archimate-canonical-identity-establishment/active-diagnostic.json`
  and `active-diagnostic-summary.json`.
- The same directory's `naming-blockers.json`, `input-preservation.json` and
  `preservation-result.json`.
- `publication-tests.log`, `architecture-tests.log.gz`,
  `identity-release-tests.log`, `independent-regeneration.log`,
  `recovery-check.log`, `identity-validation.log`, `ec-bindings-check.log`
  and `release-check.log` in that directory.

No existing task input, implementation or governed architecture file was edited.
