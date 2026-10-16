#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render release evidence using the single architecture-release authority."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import shlex
import sys

BASE = Path(__file__).resolve().parents[1]
ROOT = BASE.parents[2]
sys.path.insert(0, str(ROOT / "scripts"))
from architecture_release import DEFAULT_RELEASE, load_release

PUBLICATION = "docs/archimate/capability-architecture/"
GOVERNANCE = "docs/markdown/governance/"
# This bounded change-set manifest is reporting provenance, not a semantic registry.
FILES = {
    "AGENTS.md": "Discoverable repository-wide release rule",
    "architecture-release.json": "Single machine-readable release authority",
    "scripts/architecture_release.py": "Shared loader, checks and explicit guarded patch increment",
    "scripts/tests/test_architecture_release.py": "Release validation and increment tests",
    GOVERNANCE + "README.md": "Governance navigation",
    GOVERNANCE + "architecture-release-policy.md": "Owner-authorised release policy and mechanism choice",
    GOVERNANCE + "rad-archimate-model-mapping.md": "Common-model mapping, authority, identity and publication rules",
    GOVERNANCE + "rad-archimate-mapping-catalogue.md": "Construct mappings and instance dispositions; unresolved decisions retained",
    GOVERNANCE + "rad-archimate-coverage-assessment.md": "Coverage, source conflicts and readiness findings",
    GOVERNANCE + "rad-archimate-publication-plan.md": "Small gated slices; later slices remain unauthorised",
    GOVERNANCE + "rad-archimate-source-inventory.json": "Reproducible source assertions and assessment inventory",
    PUBLICATION + "Harmonia-Capability-Architecture.xml": "14 exact-ID Documentation additions only",
    PUBLICATION + "Makefile": "Bounded generation, assessment, tests, checking and reporting",
    PUBLICATION + "README.md": "Portable inputs, release authority and reproduction instructions",
    PUBLICATION + "documentation-map.json": "Explicit 14 EC UUID/Reference-ID/source crosswalk",
    PUBLICATION + "documentation-publication-report.md": "Generated release change and validation report",
    PUBLICATION + "tests/test_documentation.py": "Exact-ID, preservation, drift and portable release-input checks",
    PUBLICATION + "tests/test_publication_metadata.py": "Assessment findings and release-evidence consistency checks",
    PUBLICATION + "tools/assess_rad.py": "Assessment inventory; no XML semantic generation",
    PUBLICATION + "tools/publish_documentation.py": "Constrained Documentation overlay and release-aware validation report",
    PUBLICATION + "tools/report_publication.py": "Report renderer reading authoritative release and pinned verification evidence",
    PUBLICATION + "validation.json": "XML, identity, reference, deterministic-output and source checks",
    PUBLICATION + "release-verification.json": "Bounded suite results and verified-file fingerprints",
}


def render(release, publication, inventory, verification):
    for label, record in (("publication", publication), ("inventory", inventory), ("verification", verification)):
        if record.get("architecture_release") != release:
            raise ValueError(f"Stale {label} release evidence; regenerate and verify")
    if publication["status"] != "PASS" or any(c["status"] != "PASS" for c in verification["checks"]):
        raise ValueError("Release verification is incomplete; cannot report PASS")
    files = "\n".join(f"| [{path}]({os.path.relpath(ROOT / path, BASE)}) | {meaning} |" for path, meaning in FILES.items())
    checks = "\n".join(f"| {item['name']} | {item['status']} — {item['result']} |" for item in verification["checks"])
    views = "\n".join(f"| {view['name']} | {view['nodes']} | {view['connections']} |" for view in publication["generated_views"])
    commands = "\n".join(shlex.join(c["command"]) for c in verification["checks"] if "command" in c)
    return f"""<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia RAD / Architecture Release {release['version']}

**Prepared and validated: {release['previous_version']} → {release['version']}.** The single
current-release authority is [architecture-release.json](../../../architecture-release.json).
The [governed release policy](../../markdown/governance/architecture-release-policy.md)
records the owner's decision, patch increment procedure and the JSON/Python convention
choice. Publication, inventory and this report read the shared authority. Regeneration
never increments it. Maven remains `1.0.0-SNAPSHOT`. No commit or Archi GUI review is claimed.

The mapping specification, catalogue, coverage assessment and incremental plan are
installed in governed RADS. Proposed mappings and missing architectural decisions
remain findings; installation does not authorise their semantic generation.

## Files added or changed in this bundle

Paths describe this bounded bundle relative to the working tree before installation;
the previously prepared `docs/archimate/` publication is already untracked. Unrelated
LaTeX publication changes and the existing Junie report are outside this bundle.

| File | Change |
| --- | --- |
{files}

## Identity and Documentation

Semantic identity changes: **0**. All **{publication['semantic_elements']} capabilities**,
**{publication['semantic_relationships']} Composition relationships**, labels, properties,
hierarchy, model identity, existing Views and visual IDs remain intact.
Documentation enrichment: **{publication['documentation']['enriched_elements']} exact matches**,
EC-01–EC-14; the remaining **{publication['documentation']['unresolved_elements']} identities**
remain unresolved and receive no inferred Documentation. No semantic objects or
relationships, new Views or visual identities are allocated in this release.

The unchanged View publisher is wrapped by an explicit exact-ID overlay. The manifest
checks the existing UUID, unique Reference ID, layer, source heading, source fingerprint
and full-description fingerprint. Names check drift; they confer no mapping authority.
Complete governed EC section bodies are copied verbatim, including EC-12/14 separation,
anti-responsibilities and EC-14 non-allocation/conceptual-sequence qualifications.

The baseline fixture and original semantic lock remain immutable. Validation removes
only the verified allowed Documentation additions in memory and checks the original
lock again. Existing UUIDv5 visual namespaces/keys and layout are unchanged: a tiered
layout stacks siblings vertically; unconnected capabilities use wrapped grids.
No missing BEC natural-group memberships or Enterprise hierarchy are inferred.

| Preserved generated View | Nodes | Connections |
| --- | ---: | ---: |
{views}

The baseline's original import diagnostic is also retained. Output:
[Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml).
SHA-256: `{publication['output_sha256']}`.

## Coverage and unresolved findings

The source inventory fingerprints **{inventory['summary']['source_files']} RADS files**
(the original discovery snapshot covered 124; the release policy is new).
It preserves 705 Business records, 462 relationship assertions, 73 contexts,
110 participant definitions, 57 Information Family section candidates and 146 C/F/D/M
rows. Heading counts are navigation evidence, not approved architecture element counts.
The assessment tool never feeds XML semantic generation.

- **214 unresolved baseline capability identities** remain unmapped. The 228-object
  Exchange baseline and current 280 established RAD capabilities remain unreconciled
  outside the 14 exact EC matches. Names, including exact label coincidences, confer
  no identity authority.
- **F46 identity collision** remains: Omilia operations/client experience and Shared
  Security and Assurance Nomenclature. No renumbering or merge is performed.
- **Component-register C keys** (and F/D/M keys) remain documentary navigation keys,
  not Canonical IDs. All 705 captured full Canonical IDs remain unallocated;
  137 local Feature IDs and incomplete BEC/Feature ancestry remain as governed.
- **Unresolved relationship assertions** remain: 22 captured assertions, including
  11 realises_feature and 11 associated_feature. Native predicate/profile decisions,
  capability ownership and information Requires/Consumes/Produces distinctions are
  not normalised into convenient graph edges.
- **Domain05 deferrals** remain: accepted candidate standing, B2.1 PASS only, later
  application allocation/derivation gates and the final Solution Architecture remain
  incomplete. C rows are not automatically 49 ApplicationComponents.
- **2,467 unresolved RAD dimensions/relationships** remain unchanged. The 53 absent
  capture summaries are source-review leads, not proof of absent governed definitions.
- Health Information Exchange category/name conflict, clinical-handover wording,
  mixed information/application types and unestablished accepted technology/migration
  catalogues remain findings. Missing cross-layer traceability is not invented.

See the [coverage assessment](../../markdown/governance/rad-archimate-coverage-assessment.md)
and [catalogue](../../markdown/governance/rad-archimate-mapping-catalogue.md) for evidence.

## Validation

| Check | Result |
| --- | --- |
{checks}
| Archi 5.9 bundled Model/View/Diagram XSDs, offline imports | PASS — serialized XML |
| Original semantics/IDs and all View/reference checks | PASS — original lock after only authorised Documentation is removed |
| Repeat generation, idempotence and configurable input portability | PASS |
| Archi GUI import and visual inspection | NOT PERFORMED — review before the next architecture slice |

[validation.json](validation.json) records detailed XML/identity/reference checks.
[release-verification.json](release-verification.json) records bounded command results
and file fingerprints. The report renderer refuses stale release or file evidence.
Commands below run from the repository root; publication commands inside Make are bounded.

```bash
{commands}
```

## Presentation and stop point

Verbatim Markdown and relative source links require rendering review in Archi. The
BEC View remains tall (12,540 layout units); Enterprise remains flat because the
baseline supplies no hierarchy. No natural-group membership is inferred from names.
No GUI visual result is claimed by XSD validation.

This release stops at the installed mapping deliverables and 14 exact Documentation
additions, with four generated capability Views preserved. No next RAD publication
slice, Features, Functions or other architecture visualisation has begun.
"""


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--release-file", type=Path, default=DEFAULT_RELEASE)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    try:
        release = load_release(args.release_file)
        publication = json.loads((BASE / "validation.json").read_text())
        inventory = json.loads((ROOT / GOVERNANCE / "rad-archimate-source-inventory.json").read_text())
        verification = json.loads((BASE / "release-verification.json").read_text())
        for path, digest in verification["verified_files_sha256"].items():
            if hashlib.sha256((ROOT / path).read_bytes()).hexdigest() != digest:
                raise ValueError(f"Stale verification fingerprint: {path}; rerun affected checks")
        if hashlib.sha256((BASE / "Harmonia-Capability-Architecture.xml").read_bytes()).hexdigest() != publication["output_sha256"]:
            raise ValueError("Published XML differs from validation evidence")
        report = render(release, publication, inventory, verification)
        output = BASE / "documentation-publication-report.md"
        if args.check:
            if output.read_text() != report:
                raise ValueError("Publication report differs from current evidence")
        else:
            output.write_text(report)
        print(f"PASS: release {release['version']} report {'checked' if args.check else 'generated'}")
        return 0
    except (OSError, ValueError, KeyError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
