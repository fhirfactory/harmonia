<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia ArchiMate workspace

```text
docs/archimate/
├── framework/                       reusable machinery
├── processing/
│   ├── publishing/<release>/        publication working state and evidence
│   └── ingestion/                   incoming-export working state and evidence
└── exchange/
    ├── publish/<release>/           finished governed exchange packages
    └── ingest/                      incoming exchange files
```

[Framework](framework/README.md) contains reusable tools, schemas, tests,
projection support and framework baselines. [Processing](processing/README.md)
keeps working material separate from exchanged files and separates publishing
from ingestion. [Publish](exchange/publish/README.md) contains versioned finished
packages: currently one Open Exchange XML per release. [Ingest](exchange/ingest/README.md)
is the landing area for incoming Archi exports.

Publication: **Harmonia RAD → framework → processing/publishing → exchange/publish → Archi**.
The future return flow is **Archi → exchange/ingest → processing/ingestion →
comparison/reconciliation → Harmonia RAD**. Increment 2 is not implemented here.

The governed [Markdown RAD](../markdown/) owns the architecture. Canonical identity
and governed names belong to governed architectural resources and elements.
The framework, processing files and exchange packages establish no competing
architectural authority. **Archi export is architecture change input, not
replacement architecture repository.** Incoming files may contain semantic changes,
presentation changes and known transport transformations. Comparison/reconciliation
and Solution Architect acceptance must precede changes to the Harmonia RAD.

Release 1.0.7 remains the frozen accepted identity-bearing baseline and real
Archi 5.10 identity/naming proof. Release 1.0.8 is the policy-defined structure and
tooling release with unchanged architecture and presentation apart from release
metadata. The root [release authority](../../architecture-release.json) determines
the current release; generation never increments it.

Historical reports, manifests, logs and source archives retain their original
bytes and path statements. Use the [location map](framework/historical-locations.json)
for current navigation to a recorded historical locator. Relative links inside
frozen reports may still describe their original location. The
[migration report](../../.junie/reports/2026-10-18-archimate-workspace-restructure.md)
records preservation and validation. The [former README](framework/historical-workspace-readme.md)
is retained as history; use the framework README for current commands.
