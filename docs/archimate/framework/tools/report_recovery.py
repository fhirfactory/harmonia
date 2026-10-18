#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render the 1.0.5A recovery report from pinned completed verification."""
import argparse
import collections
import json
from pathlib import Path
import sys

import recover_v02 as r

DEST = r.DEST
REQUIRED = {"publication", "scripts", "architecture", "snapshot", "snapshot_tests", "rad", "identity", "regeneration", "release", "diff"}


def read(name):
    from workspace_paths import RECOVERY_EVIDENCE
    path = DEST / name
    return json.loads((path if path.exists() else RECOVERY_EVIDENCE / name).read_text())


def render():
    validation = read("validation.json")
    findings = read("recovery-findings.json")
    crosswalk = read("representation-crosswalk.json")
    evidence = read("verification.json")
    manifest = read("source-manifest.json")
    drift = read("corpus-drift.json")
    release = r.load_release()
    r.g.require(evidence["architecture_release"] == validation["architecture_release"] == release, "Stale recovery release evidence")
    r.g.require(evidence["status"] == validation["status"] == "PASS", "Recovery validation incomplete")
    r.g.require(set(evidence["checks"]) == REQUIRED and all(c["status"] == "PASS" for c in evidence["checks"].values()), "Required recovery checks incomplete")
    for path, expected in evidence["files_sha256"].items():
        r.g.require(r.sha(r.ROOT / path) == expected, "Stale verification fingerprint: " + path)
    r.g.require(r.sha(r.OUTPUT) == validation["output_sha256"], "Recovery model hash mismatch")
    model = json.loads((r.ROOT / manifest["source_directory"] / "model.json").read_text())
    rows = []
    for kind in r.TYPES:
        elements = [e for e in model["elements"] if e["type"] == kind]
        count = collections.Counter(e["architectural_status"] for e in elements)
        states = collections.Counter(e["record_state"] for e in elements)
        rows.append(f"| {kind} → {r.TYPES[kind]} | {len(elements)} | {count['Canonical']} | {count['Proposed']} | {count['Candidate']} | {states['current']} / {states['superseded']} / {states['pending_adjudication']} |")
    relation_rows = []
    for native in ("Composition", "Realization"):
        entries = [e for e in crosswalk["relationships"] if e["archimate_type"] == native]
        status = collections.Counter(e["source_assertion"]["status"] for e in entries)
        states = collections.Counter(e["record_state"] for e in entries)
        relation_rows.append(f"| {native} | {len(entries)} | {status['established']} | {status['Proposed']} | {states['current']} | {states['preserved historical assertion']} |")
    deferred = "\n".join(f"| `{kind}` | {count} | {r.DEFERRED.get(kind, 'Unresolved source realisation; no graph fact allocated.')} |" for kind, count in sorted(
        collections.Counter(e["source_assertion"]["type"] for e in findings["deferred_relationships"]).items()))
    gaps = "\n".join(f"- {e['name']} (`{e['reference_id']}`; {e['record_state']})." for e in model["elements"] if e["key"] in findings["definition_gaps"])
    view_counts = collections.Counter(e["role"].split(":")[0] for e in crosswalk["views"].values())
    views = "\n".join(f"| {role} | {count} |" for role, count in sorted(view_counts.items()))
    checks = "\n".join(f"| {key} | {entry['status']} | {entry['result']} |" for key, entry in evidence["checks"].items())
    cases = "\n".join(f"| {e['name']} | {e['status']} | " + "; ".join(c["name"] + " (" + c["status"] + ")" for c in e["children"]) + " |" for e in validation["care_access_regression"]["children"])
    categories = "\n".join(f"| {e['category']} | {e['reason']} |" for e in findings["additional_material_categories"])
    changed = "\n".join(f"- `{path}`" for path in evidence["change_set_files"])
    drift_files = "\n".join(f"- `{entry['path']}`" for entry in drift["drift"])
    return f"""<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Architecture 1.0.5A — LaTeX RAD v0.2 Recovery Report

Ready for human inspection. Release authority: **{release['previous_version']} → {release['version']}**;
1.0.5A is the bounded task label, not a second release register. No commit, Archi
GUI import, human visual acceptance, architectural adjudication, canonical Markdown
RAD synchronisation or 1.0.5B overlay is claimed.

Import [Harmonia-Architecture-1.0.5A.xml](Harmonia-Architecture-1.0.5A.xml) using
Archi 5.9 Open Exchange import. The model contains **1,165 elements, 823 relationships
and 984 Views**. These totals include the explicitly separated legacy comparison
population. The recovered snapshot itself contains 946 records plus five transcribed
Principles. This is a historical recovery reference, not the final reconciled baseline.

## 1. Sources and tooling actually used

The starting snapshot is the committed `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/`:
its editable master and all 30 included TeX files, 946-record `model.json`,
`identity-lock.json`, `pre-review-identity-lock.json`, `semantic-baseline.json`,
`v0.1-fingerprints.json`, reviewed BEC hierarchy, reviewed identity history,
reconciliation ledger, v0.3 reconciliation, derivation provenance and source/coverage
registers. [source-manifest.json](source-manifest.json) pins 75 source, tooling,
identity and baseline files. It is an immutable recovery input, not an identity allocator.

The existing v0.2 `render.py` establishes how records, source definitions, independent
standing, placement, history, scope and source excerpts reach TeX. Its `baseline.py`
locates the frozen v0.1 transcriber. No PDF reconstruction, model recomposition or
current-RAD prose enrichment was performed. The unchanged v0.2 `validate_model`,
`validate_review` and `navigation.validate_source` functions and its 17 reviewed-BEC
tests were reused for snapshot verification.

The 1.0.4 Exchange XML is the retained publication reference. Its `generate_views.py`
provides secure XML parsing, serialization, offline Archi 5.9 schemas, deterministic
UUIDv5 presentation identities and the reused three-tier layout algorithm.
`publish_navigation.navigation_homes` checks actual homes; the original semantic lock,
documentation manifest and common identity registry prove the retained bindings.
The new [recover_v02.py](../tools/recover_v02.py) adapts those mechanisms to the pinned
snapshot. The existing publishers and original model remain intact.

Representation authority is the RAD→ArchiMate mapping catalogue/profile, Architecture
Identity model and Archi navigation convention. AX-04 keeps representation machinery
from determining meaning; AX-14 preserves type, status and identity distinctions;
AX-17 keeps absent authority/relationships/definitions unresolved. Current authority
was used to constrain the recovery, not to overlay new architecture.

## 2. Extraction and reuse

Each source capture record is represented exactly once by its locked snapshot Reference
ID. Types and bounded meaning come from the structured records used by the LaTeX
renderer. Explicit relationship assertions and separate `relationship_currentness`
records determine native edges and history. `parent_key` and `owner_key` supply
different navigation contexts; directory names and diagram adjacency supply neither.

Readable Documentation decodes the transcriber's known text macros and retains exact
definition wording, baseline explanation, Proposed definition/overview/boundary,
source excerpts, provenance and adjudication qualifications. The full original record
is inspectable in `v0.2 / Snapshot Record JSON`; unknown TeX markup is retained rather
than silently discarded. [representation-crosswalk.json](representation-crosswalk.json)
records every capture-to-XML mapping, native relationship and View scope.

The master's include graph is traversed with path/cycle checks. All 30 included source
files are preserved verbatim in named evidence Views' Documentation, with path and hash
Properties. Their complete content remains inspectable even where no native semantic
mapping exists. The 118-entry corpus-register is provenance, not a mandate to import
all current files it names. Its four changed live entries are explicitly excluded:

{drift_files}

[corpus-drift.json](corpus-drift.json) records snapshot/current hashes. A changed file
does not date or adjudicate each assertion; frozen model and TeX content define this
exercise's boundary.

## 3. Constructs and relationships recovered

Source semantic standing and record currentness are independent:

| Source type → native type | Records | Canonical | Proposed | Candidate | Current / Superseded / Pending |
| --- | ---: | ---: | ---: | ---: | --- |
{chr(10).join(rows)}
| Total | 946 | 716 | 227 | 3 | 845 / 32 / 69 |

Five explicitly transcribed axioms become native Principles: AX-01, AX-04, AX-14,
AX-17 and AX-18. Their appendix selections and qualifications are retained; this
does not imply recovery of an otherwise absent complete axiom register. Existing
native AX codes are source IDs; no common-registry adoption was made.

Of 1,096 relationship assertions, **634 become native edges** and **462 remain
metadata plus findings**. No unresolved source assertion becomes a graph fact.

| Native type | Recovered | Established | Proposed | Current | Preserved historical assertion |
| --- | ---: | ---: | ---: | ---: | ---: |
{chr(10).join(relation_rows)}

Composition is restricted to explicit same-category capability decomposition.
Realization is restricted to explicitly asserted BusinessFunction → Feature-typed
Capability. FT remains Feature classification, never CT4. Historical edges retain
their endpoints and status but do not become current navigation ancestry. Every
native endpoint pair satisfies this bounded mapping; XSD validity alone was not
used as semantic proof.

## 4. Existing elements and bindings retained

All **228 elements, 189 Compositions and 219 Views** of the proven 1.0.4 file survive
with their semantic, relationship, View, node and connection IDs. Original relationship
endpoints, names, properties and View contents are preserved. The 14 original EC
Documentation bodies remain intact as the prefix of their enriched Documentation.
Additional snapshot Properties are explicitly named `v0.2 / …`; the retained legacy
properties do not silently determine the recovered source standing or tier.

EC-01–EC-14 reuse their exact existing semantic bindings. The other 214 old BC/BEC
elements have no demonstrated source crosswalk; their names, tiers and hierarchy
cannot supply one. They remain unchanged under **Unreconciled 1.0.4 Reference** in
Strategy/Relations/Views. Their relative navigation conventions survive the explicit
reference-branch relocation. They are a comparison population, not additional claims
that those objects have been reconciled into the v0.2 snapshot. No lexical merge or
unproven identity equivalence was made.

## 5. New deterministic representation elements

The recovery creates **932 new snapshot representation elements plus five Principles
= 937**, and 634 relationship representations. The other 14 snapshot records reuse
the proven EC elements. New XML IDs use the existing UUIDv5 publication namespace
with a distinct recovery role and frozen source Reference ID; relationship keys use
predicate and exact endpoint Reference IDs. Mutable names, release, geometry and
source-array ordinal do not allocate element/relationship identity. Collision aborts
generation; there is no salting or lexical fallback.

View/node/connection/folder/reference identities are deterministic presentation
identities. Capability context folders use the source subject identity, so a rename
or source-context relocation preserves the folder identity. No generated UUID is
written into a persistent Architecture Identity register or a structural Canonical ID.

## 6. Documentation and metadata coverage

All **946 source records** have Documentation and a complete source-record property.
Definitions are available on **929**; **17** have an explicit definition-gap statement
and preserved source excerpts/context. All five Principles, all 634 new relationships
and all 30 evidence Views have source Documentation. The 14 governed EC definitions
are preserved. No definition was synthesised from a name or heading.
All 647 nonempty preserved source semantic-detail fields were also found in the
included TeX after only heading-wrapper, line-break-hint and whitespace normalization.

Properties expose source/capture Reference ID, LaTeX label, construct/tier,
Canonical/Proposed/Candidate standing, record currentness, placement and structural-ID
standing, exact existing structural IDs/aliases, names/history, source locators,
derivation, unresolved dimensions, explicit/inherited release scope and source,
adjudication notes, full source record and deferred relationship assertions. All
eleven explicit **Not Addressed in R1.x / R2.x** annotations are retained; inherited
scope follows the same explicit source context as the v0.2 renderer. Release scope
does not change semantic standing.

Definition gaps, listed without inventing replacement definitions:

{gaps}

## 7. Navigation and Views

Strategy retains separate BC, BEC and Enterprise structures. Current BC has 200
capabilities; current reviewed BEC has 7 CT1 / 76 CT2 / 95 CT3; Enterprise has its
explicit Proposed v0.2 hierarchy independent of BC/BEC. This extends the formerly
flat EC catalogue only because the snapshot supplies explicit Proposed edges.
Pending and Superseded records have separate homes. Feature folders explicitly say
**Features (framing only)**. Business navigation mirrors the evidenced responsibility
context and separates Functions, Services and Processes without creating ownership
edges. Relations mirror their source object's home, including history where relevant.

The three recovered category overviews show CT1+CT2+CT3 with current source Composition
edges in the proven left-to-right layout. Normal context Views show only context and
immediate children, with readable 12-point nodes. No grandchildren leak into them.
Features have separate framing Views, not invented Composition. Behaviour contexts
are bounded into pages of at most 21 nodes; every recoverable Function/Feature
Realization also has its own two-element inspection View.

| New View role | Count |
| --- | ---: |
{views}

**765 new Views + 219 retained = 984.** Source/findings Views contain explanatory
notes; their full evidence is in View Documentation. Model-tree folder identities
are deterministic recovery projections; original diagram identities remain unchanged.
Orange nodes mark Proposed material, purple Candidate, grey noncurrent/pending and
yellow Canonical. Placement/edge standing is inspected independently in Properties.

## 8. Architecture Identity

The common register remains **14 records**, byte-identical to the accepted input.
There are **zero persistent identity allocations, adoptions or migrations**. All 946
snapshot lock entries, their Reference IDs, LaTeX labels, local IDs, structural IDs and
historical aliases are checked against the immutable input. Pre-review locks/history
are preserved. The 932 newly represented non-EC snapshot constructs, five Principles
and 214 unbound legacy objects retain explicit unresolved common-identity status;
the new relationship representation IDs likewise do not allocate persistent identities.
Existing native AX IDs and source structural IDs remain distinct from registry adoption.

## 9. Candidate, Proposed, Pending, Superseded and conflicts

All **227 Proposed**, **three Candidate**, **69 Pending Adjudication** and **32
Superseded** records retain their independent standing and exact original records.
Proposed placement does not demote a Canonical responsibility or promote a Candidate.
The three Candidate concerns are Operational Readiness, Clinical Device Maintenance
Management and Health Information Sharing Programme Management in their source names.

Adjudication notes preserve subject assurance versus assurance-service review;
physical badge/access versus professional qualification; technical boundary versus
patient communication; internal governed execution versus external work bridge;
unallocated compatible responsibilities; general-security Feature framing; and mixed
specimen/materiel wording. The BC/BEC Health Information Exchange CM-R05 collision
remains explicit, including the Proposed display qualification. Client consent
ownership and its FEAT-EM-09 history remain intact. No substantive conflict was resolved.

## 10. Material not represented as native semantics

No source record is omitted. The following source predicates are inspectable on both
endpoints and in [recovery-findings.json](recovery-findings.json), with the complete
assertion, status, currentness, source evidence and reason for each occurrence:

| Source predicate | Deferred | Reason |
| --- | ---: | --- |
{deferred}

Other documentary categories remain inspectable without fabricated native concepts:

| Category | Reason / retained representation |
| --- | --- |
{categories}

All 178 Feature framing records and all source relationship constraints (including
negative/No Material Role constraints) are separately preserved in the findings
ledger. Per-section accounting identifies every included TeX file and its evidence
View. The source appendices include illustrative/qualified references to information
constructs, participants and mappings; these were not treated as a complete independent
definition catalogue. Native allocation of those references needs source-level
identification/mapping review; absence of persistent ID alone is not the reason for
deferral. No information, application or technology architecture is completed by inference.

## 11. Care Access & Coordination regression

The recovered CT1 reuses its locked snapshot Reference ID and has exactly five CT2
children, with the established four separate from the Proposed fifth:

| CT2 | Element standing | Immediate deeper decomposition |
| --- | --- | --- |
{cases}

The four Canonical CT2 branches expose **15 Canonical CT3** capabilities. The Proposed
preparedness branch exposes **Health Service Operational Readiness**, which remains
**Candidate** with **Proposed placement**. Its missing structured definition remains
reported. The context View has six nodes and five explicit Composition connections;
no grandchild is shown as a direct CT2. Successive Views expose the deeper branches.

## 12. Validation and tests

| Check | Outcome | Evidence |
| --- | --- | --- |
{checks}

All publication contract checks in [validation.json](validation.json) are PASS:
deterministic regeneration; offline bundled Archi 5.9 Diagram/View/Model XSD;
identifier/reference integrity; exact endpoint agreement; retained bindings/semantic
records/Views; lock preservation; source-status/metadata coverage; source-based
single homes; current-parent assertion agreement; and the Care Access regression.
Tests reject source drift, silent promotion, altered definitions, extra graph edges,
wrong View scope, name-only identity conflation and unsupported Feature Composition.

The initial online Maven invocation ended before architecture testing because the
dependency cache is read-only. Offline Java 25 execution emitted unsupported major
version 69 warnings; it is not the relied-on verification evidence. The final bounded
offline Java 21 run completed **90 architecture tests, zero failures/errors/skips**.
No production code was changed to work around those environment conditions.

The unchanged full v0.2 `check.py --model-only` returned **FAIL for current-source
fingerprint equality**, starting at the changed metamodel. It must not be reported as
a full PASS or rerun through recomposition against newer sources. The independent
frozen-model/review/navigation checks and 17 tests pass; four drift entries remain
recorded. The current RAD validator separately passes while retaining **2,467 unresolved
dimensions/relationships**. Those current-RAD results do not populate this snapshot.

Detailed completed commands, outcomes, logs and fingerprints are in
[verification.json](verification.json) and [verification/](verification/).
Human Archi import/visual acceptance remains pending. Automated validation establishes
Exchange validity and model invariants; it does not claim that the GUI was exercised.

## 13. Files changed/generated

The original 1.0.4 XML, baseline locks, LaTeX corpus, Markdown RAD and identity register
are unchanged. The change set is:

{changed}

The report and inspection guide are generated by [report_recovery.py](../tools/report_recovery.py).
Run from `docs/archimate/capability-architecture/`:

```bash
PYTHONDONTWRITEBYTECODE=1 make all
python3 tools/report_recovery.py --check
```

`make all` regenerates the separate snapshot XML/ledgers, runs publication tests,
compares deterministic outputs and checks the pinned completion report. Generation
never bumps the release, reallocates source/persistent identity, refreshes live RAD
inventory or recomposes LaTeX. Separate repository/ArchUnit results are recorded evidence.

## 14. Review before 1.0.5B

1. Inspect the Care Access regression, reviewed BEC tree, Proposed Enterprise hierarchy,
   native realisation Views and retained 1.0.4 reference branches in Archi 5.9.
2. Decide any future crosswalk for the 214 unbound old objects using identity evidence;
   labels and visual similarity remain insufficient. Their parallel reference
   representations are intentional until that review, not asserted equivalence.
3. Review native Feature framing and the deferred enablement/contribution/responsibility
   profiles before graph publication. Review illustrative information/participant
   references before treating them as independently identified objects.
4. Preserve the 69 pending records, 32 superseded records, three Candidates, definition
   gaps, naming/authority conflicts and release-scope distinctions through any delta.
5. Use the pinned v0.2 model/crosswalk and the four drift records as the reference
   boundary for an explicitly authorised 1.0.5B overlay. A changed source fingerprint
   alone does not establish a reviewed architectural delta.

**Stop point reached:** recovery is complete as far as the existing source authority
and mappings permit. Model, findings, report and guide are ready for Mark's review.
No commit or subsequent architecture slice has been started.

Model SHA-256: `{validation['output_sha256']}`.
"""


GUIDE = """<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

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
"""


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    try:
        outputs = {"1.0.5A-recovery-report.md": render(), "archi-inspection-guide.md": GUIDE}
        for filename, value in outputs.items():
            from workspace_paths import RECOVERY_EVIDENCE
            path = RECOVERY_EVIDENCE / filename
            if args.check:
                r.g.require(path.read_text() == value, "Stale recovery report/guide: " + filename)
            else:
                path.write_text(value)
        print("PASS: 1.0.5A recovery report and inspection guide")
        return 0
    except (OSError, ValueError, KeyError) as exc:
        print("FAIL: " + str(exc), file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
