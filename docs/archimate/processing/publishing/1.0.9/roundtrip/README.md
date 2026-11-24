<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# RAD-1.0.9 real Archi transport evidence

The installed Archi 5.10 command-line application imported the complete governed
Open Exchange model, exported it without editing, validated import and export
against its schema and saved the native model. The successful run exited 0;
[its log](archi-unsandboxed-roundtrip.log) records those operations. The preceding
sandboxed GTK initialisation failures are retained separately. The successful
run used an isolated temporary configuration, workspace and keyring.

The actual invocation was:

```bash
timeout --signal=TERM --kill-after=5s 90s \
env GDK_BACKEND=x11 DISPLAY=:0 \
/home/mhunter/Development/Tools/Archi/Archi \
-nosplash -nl en -consoleLog \
-configuration /tmp/harmonia-rad109-archi/configuration \
-data /tmp/harmonia-rad109-archi/workspace \
-eclipse.keyring /tmp/harmonia-rad109-archi/secure-storage \
-application com.archimatetool.commandline.app \
--xmlexchange.import /home/mhunter/Development/code/projects/github/harmonia/docs/archimate/exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml \
--saveModel /tmp/harmonia-rad109-archi/imported.archimate \
--xmlexchange.export /tmp/harmonia-rad109-archi/no-edit-export.xml \
--xmlexchange.exportFolders --xmlexchange.exportLang en
```

The native model and no-edit export are preserved as byte-recoverable gzip files.
The [verification record](archi-transport-verification.json) states the bounded
assertions and original XML hashes. The unmodified comparator's full findings
are retained in `raw-comparator.json.gz`. The raw result is **DIFFERENCES**;
all 1,669 semantic-text findings match the established 1.0.7 evidence exactly.
No new comparator equivalence or normalisation was introduced. This evidence
does not establish whole-model lossless fidelity or human rendering acceptance.

From the repository root, reproduce the read-only transport assertions:

```bash
timeout --signal=TERM --kill-after=5s 30s python3 -B \
docs/archimate/processing/publishing/1.0.9/roundtrip/verify-transport.py
```

This verifies exact identity/naming Properties, native non-Property semantic
content, all 212 directed Home assertions, all accepted resource records, all
four authored Views, every View's geometry and connections, navigation homes,
and unchanged historical semantic-text findings. It reads the actual compressed
export and extracts it only into a temporary directory. It establishes no new
architectural or comparison rule.

Human import/rendering acceptance remains **PENDING INSPECTION**, as recorded in
the [inspection guide](../archi-inspection-guide.md).
