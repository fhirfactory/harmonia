<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate processing

- `publishing/<release>/`: release-specific working state and evidence from
  establishing, generating, validating and verifying a publication. Historical
  recovery, synchronisation and identity-establishment material stays with the
  release it records. Finished exchange XML lives in `../exchange/publish/<release>/`.
- `ingestion/`: working state and evidence for incoming exchange analysis.
  Incoming exchange files themselves land in `../exchange/ingest/`.

Reusable machinery belongs in `../framework/`. Processing evidence is not
architectural authority. Frozen historical evidence retains its original content;
current navigation is in the [location map](../framework/historical-locations.json).
Increment 2 reconciliation is future work.

Internal governance/tooling development evidence belongs in the separate
[governance-tooling namespace](../governance-tooling/README.md), including the
1.0.14 normalisation evidence. It must not occupy `publishing/<release>/` or
`exchange/publish/<release>/`. Those namespaces describe governed RAD publications.
