<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Continuum documentation

Documentation publishes the [canonical Architecture Continuum](../continuum/MANIFEST.md).
It lives alongside canonical state, governance, design and tooling as a separate
Enterprise Architecture concern. Publication does not confer architectural authority
or alter the standing of an element or assertion.

- [Continuum Manifest](continuum-manifest/README.md): structural catalogue, browser,
  index, Exchange projection and identity/standing LaTeX catalogue.
- [Continuum RAD](continuum-rad/README.md): narrative LaTeX document following the
  current model's organisation, with canonical definitions, explanatory reading
  notes, selected relationship diagrams, navigation and a name index.

Both products use the root [release authority](../../architecture-release.json)
and [existing EA tooling](../tooling/README.md). Generate/check with
`make -C enterprise-architecture/tooling all`; compile the narrative PDF with
`make -C enterprise-architecture/tooling rad-pdf`.

Current governed release: **1.1.9**, predecessor **1.1.8**. The
[completed 1.1.9 HCRL](../design/register/1.1.9/hcrl-1.1.9-001.complete.md) records
the Solution Architect's acceptance and confirmation of the governance-only
Principle-authority consolidation.
The unchanged architecture publications remain at **1.1.8** under the
[release policy](../governance/architecture-release-policy.md).
The
[completed 1.1.8 HCRL](../design/register/1.1.8/hcrl-1.1.8-001.complete.md)
records the Solution Architect's accepted documentation change and confirmation. The
[accepted 1.1.7 baseline](../design/register/1.1.7/hcrl-1.1.7-001.complete.md)
and all individual source qualifications remain in force.
