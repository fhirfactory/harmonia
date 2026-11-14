<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| ID | Brief Description | Disposition | Outcome |
| --- | --- | --- | --- |
| [CR-001](#cr-001) | Exact 1.1.24 candidate strict Archi transport exception | On-Hold | Unresolved |

# Candidate transport Change Review

This specialised control feeds [HCRL DR-4](../../../../review/1.1.24/hcrl-1.1.24-001.pending.md#dr-4),
the authoritative semantic/decision record. This is a canonical-Markdown ADD,
not an architect-authored model submission. The return is a no-edit transport
control; it supplies no architectural application directive.

## Exact basis

Accepted predecessor: **1.1.23**; entry branch `Mneme-and-Mnemosyne-Build`, HEAD
`8204fd49a4d380c068a03acdf30c17bb06e5d953`. Root release remains 1.1.23.

- [Candidate XML](candidate/reader/Harmonia-Architecture.xml), SHA-256
  `6f6432a979a92a3adbf0261ba4bc761d092f87eb55fd4e5d7968f597e4d64d3a`.
- [Exact return](archi-transport/returned.xml), SHA-256
  `40996d0972757a322d36636a9ec1dc30a35981a7a7cbed98892b14fafc8f44b4`.
- [Imported native model](archi-transport/candidate.archimate), SHA-256
  `2b3b16ac9de5dfdb1b9da7222076e5fd53c89b650e00ce75e54aceb0f9b56f4b`.
- Installed Archi **5.10.0.202605271610**; [invocation](archi-transport/invocation.json),
  [successful execution](archi-transport/execution.txt), [execution result](archi-transport/execution-status.json).
  Configuration/workspace/model-home were isolated. Initial GTK/display failures
  occurred before import and are retained; local-display access enabled the same
  bounded import/native-save/export check to complete.

<a id="cr-001"></a>
## CR-001 — Strict transport exception

**Disposition:** On-Hold  
**Outcome:** Unresolved  
**Solution Architect decision:** Not recorded for this candidate.  
**Authorised Reconciliation Instruction:** Not authorised.  
**Processing record:** Comparison performed; no returned model application,
template/View alteration, comparator weakening or release publication.

Actual import, native save and Exchange export **PASS**. All **6,563** canonical
identifiers, native types, specialisations, names, documentation and four governed
identity/name property values/owners survive. No relationship or View ID is lost
or added. The unchanged strict verifier returns **FAIL: Relationship/View transport
drift**: **6,507 of 9,427 relationships** and **15 of 1,304 Views** differ.

[Raw comparator findings](raw-transport-comparison.json.gz) remain unchanged:
4,693 equivalent normalisations, 18,552 information-loss observations, 112,129
semantic-difference observations and 16,481 unexplained differences. These are
conservative field/subtree classifications, not independently established counts
of architectural defects. No category is silently converted to PASS.

[Actual-record correspondence](predecessor-transport-correspondence.json) compares
both source and returned records against retained 1.1.23 evidence, preserving text,
language, attributes and child order. All **9,376 old relationship records** match
across releases both before and after transport; all **1,304 View records** also
match before and after. The **51 raw findings inside the 51 new relationship
records** are exclusively absent-to-`en` documentation-language annotations.
That evidence bounds the observed delta; it does not waive current strict FAIL,
establish pixel equivalence, or adjudicate every raw category as harmless.

Canonical Markdown, identities and authored Views are independently preserved;
exact-input no-edit workbench review and reproducibility pass. The return is not
applied. Previous release-specific acceptance supplies no exception for 1.1.24.

**Required disposition:** determine treatment of this exact candidate's failed
transport gate before dependent finalisation. Human native-tree/reader structural
review and whole-result confirmation remain separate checkpoints. This task stops
with the pending candidate; no automatic remediation or implementation is proposed.
