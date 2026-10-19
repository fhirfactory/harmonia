<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Internal governance and tooling evidence — non-RAD

This namespace preserves internal governance/tooling development evidence.
Its identifiers are not RAD architecture versions or authoring baselines.
The root architecture release remains RAD-1.0.9.

- [1.0.14 original evidence](1.0.14/README.md): normalisation XML, reports,
  validation and reproduction evidence, with a machine-readable classification
  and exact relocation manifest.
- [Namespace-correction audit](migrations/2026-10-19-reclassify-1.0.14/README.md):
  pre-mutation inventory, preserved reference consumers and post-migration checks.

The framework's [historical location map](../framework/historical-locations.json)
registers these identifiers under `non_rad_evidence`. Publication tooling rejects
them as RAD releases and validates their absence from both architecture publication
roots. Exact file mappings resolve frozen historical locators without rewriting
the evidence or creating aliases inside architecture publication directories.
