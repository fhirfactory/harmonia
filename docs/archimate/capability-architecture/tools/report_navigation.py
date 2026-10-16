#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Render the navigation release report from pinned, completed verification."""
import argparse
import hashlib
import json
from pathlib import Path
import shlex
import sys

import publish_navigation as n
from architecture_release import load_release

BASE = n.BASE
ROOT = BASE.parents[2]
GOVERNANCE = ROOT / "docs/markdown/governance"
EVIDENCE = BASE / "navigation-release-verification.json"
OUTPUT = BASE / "navigation-release-report.md"
REQUIRED_CHECKS = {"architecture", "publication", "scripts", "rad", "identity",
                   "reproducibility", "preservation", "links"}


def read(path):
    return json.loads(path.read_text())


def required_files():
    """All publication inputs, validators and source evidence must be pinned."""
    paths = {ROOT / p for p in ("AGENTS.md", "docs/AGENTS.md", "architecture-release.json",
        "pom.xml", "scripts/architecture_release.py", "scripts/architecture_identity.py",
        "scripts/rads_business_architecture.py")}
    for directory, pattern in ((ROOT / "scripts/tests", "*.py"), (BASE / "tools", "*.py"),
                               (BASE / "tests", "*.py"), (BASE / "baseline", "*"), (BASE / "schemas", "*")):
        paths.update(p for p in directory.glob(pattern) if p.is_file())
    paths.update(BASE / p for p in ("Makefile", "README.md", "documentation-map.json",
                                    "Harmonia-Capability-Architecture.xml", "validation.json"))
    paths.update(GOVERNANCE / p for p in ("README.md", "archi-navigation-and-view-convention.md",
        "architecture-release-policy.md", "rad-archimate-model-mapping.md", "rad-archimate-publication-plan.md",
        "rad-archimate-source-inventory.json", "architecture-identity/model.md",
        "architecture-identity/registry.json", "architecture-identity/registry.schema.json",
        "architecture-identity/adoption-1.0.3.md", "architecture-identity/adoption-baseline-1.0.2.json",
        "architecture-identity/adoption-validation.json", "architecture-identity/verification.json",
        "architecture-identity/release-report.md", "architecture-identity/inventory.json",
        "architecture-identity/inventory.md", "architecture-identity/bindings-validation.json"))
    paths.update(ROOT / item["path"] for item in read(GOVERNANCE / "rad-archimate-source-inventory.json")["source_files"])
    return {str(p.relative_to(ROOT)) for p in paths}


def verify_fingerprints(verification, required, root=ROOT):
    hashes = verification["verified_files_sha256"]
    if not required.issubset(hashes):
        raise ValueError("Incomplete verification fingerprints")
    for path, digest in hashes.items():
        if hashlib.sha256((root / path).read_bytes()).hexdigest() != digest:
            raise ValueError(f"Stale verification fingerprint: {path}; rerun affected checks")


def render(release, publication, inventory, bindings, verification, previous):
    for label, record in (("publication", publication), ("inventory", inventory),
                          ("bindings", bindings), ("verification", verification)):
        if record.get("architecture_release") != release:
            raise ValueError(f"Stale {label} release evidence")
    checks = verification["checks"]
    if (verification.get("status") != "PASS" or publication["status"] != "PASS" or bindings["status"] != "PASS"
        or {c["key"] for c in checks} != REQUIRED_CHECKS or len(checks) != len(REQUIRED_CHECKS)
        or any(c["status"] != "PASS" for c in checks) or any(v != "PASS" for v in publication["checks"].values())):
        raise ValueError("Required release verification incomplete")
    if (publication["semantic_elements"] != 228 or publication["semantic_relationships"] != 189
        or any(publication["semantic_changes"].values()) or bindings["retained_identities"] != 14
        or publication["architecture_content_sha256"] != previous["architecture_content_sha256"]
        or publication["architecture_content_sha256"] != bindings["architecture_content_sha256"]
        or publication["previous_publication_sha256"] != previous["files_sha256"][
            "docs/archimate/capability-architecture/Harmonia-Capability-Architecture.xml"]
        or set(publication["view_changes"]["retained"]) != set(previous["views"])
        or len(publication["view_changes"]["added"]) != 214 or publication["view_changes"]["replaced"]):
        raise ValueError("Navigation-only release boundary violated")
    nav = publication["navigation"]
    details = publication["detailed_views"]
    retained = "\n".join(f"| {v['name']} | `{identifier}` |" for identifier, v in previous["views"].items())
    gates = "\n".join(f"| {c['name']} | {c['status']} — {c['result']} |" for c in checks)
    commands = "\n".join(shlex.join(c["command"]) for c in checks if "command" in c)
    return f"""<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia Architecture Release {release['version']} — Archi navigation and Views

**Ready for human inspection: {release['previous_version']} → {release['version']}.**
The [root release authority](../../../architecture-release.json) was incremented
with `scripts/architecture_release.py bump --expect {release['previous_version']}`.
Generation does not increment it. Maven is unchanged. No commit or human visual
acceptance is claimed. The previous accepted baseline is commit
`{previous['repository_commit']}`.

## Semantic preservation and identity

| Measure | Observed |
| --- | ---: |
| Existing Capability elements preserved | 228 |
| Semantic elements added / removed / changed | 0 / 0 / 0 |
| Existing Composition relationships preserved | 189 |
| Relationships added / removed / changed | 0 / 0 / 0 |
| Semantic identity changes | 0 |
| Capability hierarchy / relationship endpoint changes | 0 / 0 |
| Existing visual identity / contents changes | 0 |
| EC Architecture Identity bindings retained | 14 (EC-01–EC-14) |
| Identity adoptions / allocations / migrations in 1.0.4 | 0 / 0 / 0 |
| Governed Documentation fields preserved / changed | 14 / 0 |
| Unresolved capability identity crosswalks | 214 |

[Binding validation](../../markdown/governance/architecture-identity/bindings-validation.json)
checks the same governed definitions, provenance hashes, register and existing
semantic bindings. The register and its schema remain byte-identical. Architecture
Identity, native definition/source IDs, navigation keys, semantic IDs, visual IDs
and implementation IDs retain their separate meanings. No identity property was
added to the XML. Names, descriptions, tiers and relationships are unchanged.

The unchanged original publishers replay the exact 1.0.3 XML against the immutable
baseline/semantic lock. Comparing all content except organisations and Views gives
`{publication['architecture_content_sha256']}` in both releases. Each retained
View is also compared independently, including its original visual IDs and geometry.
Folder containment cannot supply semantics: the hierarchy is reconstructed from
existing Composition relationships before navigation is generated. Rejection tests
cover moved folders, duplicate homes, target-based relationship placement, invented
Composition, changed semantics/endpoints and incorrect View scopes.

## Visible navigation and View changes

The reusable [governed convention](../../markdown/governance/archi-navigation-and-view-convention.md)
separates architecture, model-tree navigation and View navigation. It also records
future Business context and independent Application Data Objects Model / Software
Components structures; **none of those domains is published in this release**.

| Presentation measure | Before | After |
| --- | ---: | ---: |
| Exchange navigation folders (including empty context branches) | {nav['folders_before']} | {nav['folders_after']} |
| BC detail Views | 0 | 52 |
| BEC detail Views | 0 | 162 |
| Existing diagrams retained unchanged | 5 | 5 |
| Total diagrams | 5 | {nav['view_homes']} |
| Views replaced / removed | 0 | 0 |

All {nav['folder_ids_removed']} prior folder IDs are replaced by {nav['folder_ids_added']}
deterministic presentation folder IDs. Semantic element/relationship IDs and
retained diagram/node/connection IDs do not change. Each element has one home;
each of the 189 relationships has exactly one home following its source capability.
The diagnostic's original reference item remains directly under Views.

BC/BEC use same-named folders containing the existing capability and its child
folders. EC stays an orthogonal catalogue. Detailed View folders mirror the same
BC/BEC decomposition. Each new diagram shows its current context and immediate
children only, including leaf overviews. The 214 additional diagrams add
{sum(v['nodes'] for v in details)} visual nodes and {sum(v['connections'] for v in details)} visual
connections representing existing semantic objects. The largest detail View has
{max(v['nodes'] for v in details)} nodes, within a four-column layout of at most
{max(v['width'] for v in details)} × {max(v['height'] for v in details)} units.
The retained BEC all-tier overview remains large; smaller branch Views provide the
progressive route through it. No missing natural-view memberships are inferred.

### Views retained

| Existing View | Preserved ID |
| --- | --- |
{retained}

The landscape and the three category overviews move under `Views / Capabilities`.
Their contents remain unchanged. BC/BEC overviews show CT1 + CT2 + CT3 and no deeper
constructs. The import diagnostic stays at its original direct Views home.
No View is replaced. [validation.json](validation.json) lists every added View ID,
context, exact navigation path, node/connection count and dimensions.

## Inspect in Archi

Import this exact Open Exchange artefact:
[Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml).
SHA-256: `{publication['output_sha256']}`.

1. Expand `Strategy / Business Capabilities / Individual Care Delivery`.
   The folder contains its same-named CT1 element and the existing child folders.
   Follow `Individual Health Assessment` to its existing CT3 folders. Also inspect
   the independent `Strategy / Business Enabling Capabilities / Care Administration
   Enablement / Care Plan Administration / Care Plan Lifecycle & Coordination` path.
2. Expand `Strategy / Enterprise Capabilities`: the 14 existing EC elements remain
   a flat, separate catalogue. Check EC-12/EC-14 Documentation and identity continuity.
3. Expand `Relations / Capabilities / Business Capabilities / Individual Care Delivery`:
   outgoing CT1 relationships live here; outgoing CT2 relationships live under their
   source's child folder. Each relationship occurs once. Empty source-context folders
   may be omitted by Archi's importer because they contain no relationships.
4. Open `Views / Capabilities / Harmonia Capability Landscape`, then
   `Business Capabilities / Business Capability Model` and
   `Business Enabling Capabilities / Business Enabling Capability Model` for the
   retained three-tier overview diagrams.
5. Under `Views / Capabilities / Business Capabilities / Individual Care Delivery`,
   open `Individual Care Delivery Overview`. Then follow the `Individual Health
   Assessment` folder and open `Individual Health Assessment Overview`; follow its
   CT3 folders for leaf overviews. Each drill-down narrows to context plus immediate
   children. Repeat with the BEC Care Plan Administration path above.
6. Open `Views / Capabilities / Enterprise Capabilities / Enterprise Capability Model`
   to confirm the independent catalogue projection.

These tree structures and bounded diagrams are the visible change to review.
**Human visual acceptance remains pending.** The optional installed-Archi 5.9 CLI
attempt returned exit 13 before model import with SWT `gtk_init_check() failed`;
`xdpyinfo -display :0` independently could not open the display. This is an
**UNRESOLVED environment check**, not a model rejection. No repeated blind import
attempt or GUI success is claimed. Import in a working desktop session is the
next visual check. Offline Archi 5.9 XSD validation below passes.

## Validation

| Required check | Result |
| --- | --- |
{gates}
| Offline Archi 5.9 Model / View / Diagram XSD validation | PASS |
| Unique semantic objects, canonical homes and source-based relation paths | PASS |
| Stable semantic hierarchy, endpoints, EC bindings and governed Documentation | PASS |
| Independent semantics, exact progressive scopes and retained View preservation | PASS |
| Byte-identical repeated generation and idempotent presentation | PASS |

[navigation-release-verification.json](navigation-release-verification.json) pins
completed commands and verified files. The report renderer refuses stale release,
missing required checks, failed checks and stale file fingerprints. Historical
1.0.1/1.0.3 reports and proofs remain unchanged. The source inventory currently
fingerprints {inventory['summary']['source_files']} RADS documents; its file census
increases for the new convention, while all prior architectural assertion and
collision digests remain unchanged. Commands run from the repository root:

```bash
{commands}
```

## Unresolved findings preserved and stop point

- 214 unresolved capability crosswalks remain unallocated and unreconciled.
- F46 remains the Omilia / Shared Security and Assurance Nomenclature navigation-key
  collision. The BC/BEC Health Information Exchange CM-R05 name conflict also remains.
- C/F/D/M/CDG keys remain navigation/register keys. Incomplete Canonical IDs, 705
  unallocated full structural IDs and 137 local Feature tokens retain their standing.
- The 22 unresolved captured relationship assertions and all 189 baseline Composition
  RAD crosswalks remain unresolved; folders add no missing assertion.
- Domain05 candidate standing, B2.1-only completion and later allocation/derivation
  deferrals remain. No additional architecture domain or semantic construct is added.
- All 2,467 unresolved RAD dimensions/relationships remain. Existing incomplete
  ancestry, source conflicts and five natural-view membership gaps are preserved.

No unexpected semantic architecture finding was established. The display failure
above is the outstanding environment limitation. Stop after this navigation slice
for human Archi inspection; further publication, F46 adjudication, Domain03 identity
migration and allocation to the remaining 214 capabilities require separate work.

## Canonical Documentation Assessment

The convention is recorded in RADS under the owner's explicit bounded instruction.
The reviewed Exchange baseline supplies only the already-authorised publication
hierarchy; no legacy architecture is promoted to RADS or used to resolve missing
crosswalks. Superseded presentation sections in the publication README are preserved
unchanged inside explicit wrappers referencing the new governed convention.
"""


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args(argv)
    try:
        verification = read(EVIDENCE)
        verify_fingerprints(verification, required_files())
        publication = read(BASE / "validation.json")
        if hashlib.sha256((BASE / "Harmonia-Capability-Architecture.xml").read_bytes()).hexdigest() != publication["output_sha256"]:
            raise ValueError("Published XML differs from validation evidence")
        result = render(load_release(), publication, read(GOVERNANCE / "rad-archimate-source-inventory.json"),
            read(GOVERNANCE / "architecture-identity/bindings-validation.json"), verification, read(n.DEFAULT_LOCK))
        if args.check:
            if OUTPUT.read_text() != result:
                raise ValueError("Navigation release report is stale")
        else:
            OUTPUT.write_text(result)
        print(f"PASS: navigation release report {'checked' if args.check else 'generated'}")
        return 0
    except (OSError, ValueError, KeyError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
