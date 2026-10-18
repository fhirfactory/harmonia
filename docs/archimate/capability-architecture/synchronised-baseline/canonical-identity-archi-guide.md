<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Solution Architect — Archi 5.10 no-edit identity transport proof

Status: **AWAITING ARCHI EXPORT** for Architecture Release **1.0.7**.

1. In **Archi 5.10.0**, import the Open Exchange model
   `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-1.0.7.xml`
   as a new model. Its SHA-256 must be `def447de2f522737c8b9c30dd9850a57bfec71eaa01f0820ff49283c40fba4bb`.
2. Make **NO edits**, including names, Properties, folders and Views.
3. Export the **complete model** in Open Exchange format, including all elements,
   relationships, Properties and Views, to:
   `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml`.
   Include organisation information, use English (`en`) import locale/export
   language, and retain the raw exported bytes.
4. Record the actual execution, Archi version and export profile as evidence.
   From the repository root run the existing Increment 1 comparator:

```bash
python3 docs/archimate/capability-architecture/tools/diagnose_roundtrip.py compare \
  --published docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-1.0.7.xml \
  --incoming .junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml \
  --output .junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-fidelity.json
```

Review `contract_properties`: all published owners must preserve exact values,
language, multiplicity and owning-element association for `canonicalId`,
`displayName`, `friendlyName`, `canonicalName` and applicable `harmoniaType`.
All element and relationship IDs, native types and relationship source/target
attributes must survive. Retained EC aliases must stay on the same elements.
The companion `archi-proof-manifest.json` pins the input and records this gate.

Known evidence-text transformations, organisation identity/order loss, definition
language loss, View Documentation newline loss, style materialisation and connection
ordering may remain visible. Comparator exit code 2 reports differences requiring
review; it does not by itself reject the bounded identity/naming transport claim.
Do not broaden normalisation to hide differences or claim whole-model losslessness.
Self-comparison and automated regeneration do not prove a real Archi execution.
Do not proceed into Increment 2 until the Solution Architect has reviewed that proof.
