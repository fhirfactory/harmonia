<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Diagnostic supporting evidence

These files support the corresponding formal Report in `../ai-agent-reports/`.
They are diagnostic outputs, fixtures and read-only analysis scripts, not an
implemented remediation, accepted architecture or replacement publication.

`summary.json` records complete population/branch counts and omitted navigation
items. `elements.json.gz`, `folders.json.gz` and `views.json.gz` retain complete
path correspondence in compressed JSON. The Report supplies the human-readable
representative table. `selected-source-items.json` contains bounded source-item
extracts with immediate child counts and the first two child references.

`published-imported.archimate.gz` is the actual isolated Archi 5.10 native save.
`execution.json` records its hashes, plugin identity and the exact-existing-evidence
correspondence of the full no-edit Exchange return. That return is byte-identical
to `design/evidence/1.1.12/001/after/archi-transport/returned-model.xml.gz` after
decompression; that existing control is referenced rather than recopied.
The original published XML remains unchanged. Logs distinguish the initial GTK
environment failure from the successful display-enabled import and second pass.

`fixtures/` retains four small Exchange inputs, their native saves, returned XML,
second-pass native/XML controls and logs. Fixture identifiers are synthetic
representation identifiers, not canonical Continuum IDs. The Topic analogues use
native DataObjects and diagnostic labels to test organisation, not semantic
specialisation validation. The real publication supplies independently governed
Topic classification and confirms the same mechanics.

`archi-importer-organisation.javap.txt` is a bounded disassembly extract from the
installed 5.10 plugin; it is evidence of importer mechanics, not architectural
authority. No whole projector or plugin source snapshot is retained.

Original read-only analyses, from repository root:

```bash
python3 -B enterprise-architecture/design/evidence/1.1.13/001/after/diagnostics/inspect_organisation.py \
  --work /tmp/harmonia-1113-workbench --output /tmp/reproduced-workbench-inventory
python3 -B enterprise-architecture/design/evidence/1.1.13/001/after/diagnostics/verify_diagnostic.py \
  --work /tmp/harmonia-1113-workbench --output /tmp/reproduced-workbench-verification.json
```

The original temporary workspace is working material, not a continuing recovery
requirement. To reproduce after its disposal, use an isolated temporary directory,
decompress the retained native save and existing no-edit Exchange control, copy
the small retained fixture saves/returns, and repeat the recorded second-pass
Archi commands to create `published-second.archimate`. The scripts expect the
original descriptive basenames. `fixture-runs.json` and `second-runs.json` can be
reconstructed from the same-named arrays in `verification.json`. Actual importer
execution uses the command recorded in `execution.json`, with isolated
`-configuration` / `-data` directories, `--saveModel`, `--xmlexchange.export`,
`--xmlexchange.exportFolders` and `--xmlexchange.exportLang en`. No publication
regeneration is necessary. Use temporary output paths, not accepted evidence paths.

`verification.json` records exact field/signature comparisons, expected synthetic
paths, repeated-reference behaviour and second-pass path stability. Full XML byte
equality and generated native-folder ID equality are not asserted. Explicit View
selection asserts 1,209 records. `architecture-tests.txt` retains the bounded
required Java suite result; `preservation.json` records scope preservation.
`evidence-layout.json` records the limited direct event/prefix audit and absence
of a corresponding HCRL. Neither script results nor evidence placement confer
human usability acceptance or permission to implement alternatives.
