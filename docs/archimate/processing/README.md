<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate processing

`publishing/<release>/` and `ingestion/` hold publication and incoming-analysis
working material. Finished XML remains in `../exchange/publish/<release>/`;
incoming files land in `../exchange/ingest/`; reusable machinery belongs in
`../framework/`.

[Architecture Change Provenance](../../markdown/governance/architecture-change-provenance.md)
defines durable evidence, independent of this processing layout. Preserve
working/historical material while needed for recovery, validation, unresolved
decisions or migration dependencies. Current operation must eventually become
independent of historical processing before the affected material can become
eligible for separately authorised disposal. Installation authorises no cleanup.
Frozen records retain their original bytes; the
[location map](../framework/historical-locations.json) supports navigation.

------- Legacy Content - Superseded ------- Start ------

Superseded by: [Architecture Change Provenance](../../markdown/governance/architecture-change-provenance.md#lifecycle-closure-and-migration) and [Change Review processing records](../../markdown/governance/archimate-ingestion-change-review.md#processing-record-and-audit-evidence).
Only the retention/evidentiary meaning of the following unchanged
guidance is superseded; operational and historical details remain useful.

---------------------------------------------------------

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

------- Legacy Content - Superseded ------- Finish ----

Internal governance/tooling development evidence belongs in the separate
[governance-tooling namespace](../governance-tooling/README.md), including the
1.0.14 normalisation evidence. It must not occupy `publishing/<release>/` or
`exchange/publish/<release>/`. Those namespaces describe governed RAD publications.
