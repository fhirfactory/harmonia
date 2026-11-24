<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Standalone reconciled LaTeX publication

**PASS — human-reviewed BEC reconciliation, 16 October 2026.** Compile the existing
master directly in Kile or another conventional LaTeX editor, or run:

```bash
latexmk -pdf -interaction=nonstopmode -halt-on-error Harmonia-EA-Capability-Map.tex
```

The normal in-place master compilation and a clean external source-only copy both
succeeded. The external copy contained the master, thirty included TeX files and
`.latexmkrc` (32 files), with no model JSON, Python, Makefile or repository parent.
The clean external build took 54.423 seconds and produced text identical to the
published PDF. Compiler input auditing found no nonstandard input outside the copy.
Only normal pdfLaTeX and makeindex were required; `.latexmkrc` contains no generator
hook. Subsidiary files retain editor root hints and the existing Kile project is
preserved. The Kile GUI was not driven. XeLaTeX support remains in the template;
it was not rerun for this reconciliation.

The PDF contains 1531 pages, 946 indexed records and 1,007 outline entries. The
current hierarchy has 845 entries; 101 superseded or unresolved records are in the
separate preserved-responsibility register. All 2,681 pre-review labels survive.
There are no duplicate labels, undefined references, missing glyphs or overfull
boxes. Representative scope and consolidated profile pages were visually inspected.

Generated sources remain editable: ordinary compilation reads the TeX tree and
performs no generation. Optional regeneration with `make compose render` is a
separate repository action and overwrites generated TeX. Maintain enduring
architecture reconciliation in `reviewed-bec-hierarchy.json` / `tools/reviewed_bec.py`
and renderer changes in `templates/` / `tools/render.py`.

Detailed results: [standalone-validation.json](standalone-validation.json),
[validation.json](validation.json), and the
[reconciliation report](../../../../.junie/reports/2026-10-16-bec-human-adjudicated-reconciliation.md).
Original pre-review verification prose and results are retained verbatim in
[pre-review-publication-evidence.json](pre-review-publication-evidence.json).
