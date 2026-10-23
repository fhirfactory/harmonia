<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Continuum documentation

Documentation publishes the [canonical Architecture Continuum](../continuum/MANIFEST.md).
It lives alongside canonical state, governance, design and tooling as a separate
Enterprise Architecture concern. Publication does not confer architectural authority
or alter the standing of an element or assertion.

- [Continuum Manifest](continuum-manifest/README.md): structural catalogue, browser,
  index, Exchange projection and identity/standing LaTeX catalogue.
- [Continuum RAD](continuum-rad/README.md): narrative LaTeX document following the
  canonical Continuum's architectural taxonomy, with canonical definitions, explanatory reading
  notes, selected relationship diagrams, navigation and a name index.

Both products use the root [release authority](../../architecture-release.json)
and [existing EA tooling](../tooling/README.md). Generate/check with
`make -C enterprise-architecture/tooling all`; compile the narrative PDF with
`make -C enterprise-architecture/tooling rad-pdf`.

Current governed release: **1.1.10**, predecessor **1.1.9**. The
[completed 1.1.10 HCRL](../design/register/1.1.10/hcrl-1.1.10-001.complete.md) records the
Solution Architect’s accepted taxonomy remap, incorporated result, explicit
classification deferrals and documented projection/provenance limits. Publications remain derived.
The [completed 1.1.9 record](../design/register/1.1.9/hcrl-1.1.9-001.complete.md)
and [accepted 1.1.7 baseline](../design/register/1.1.7/hcrl-1.1.7-001.complete.md)
retain their standing; no historical release or evidence is rewritten.
