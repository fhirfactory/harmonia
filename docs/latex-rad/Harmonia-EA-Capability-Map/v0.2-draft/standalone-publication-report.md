<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Standalone LaTeX publication

Open `Harmonia-EA-Capability-Map.tex` as the master in a normal LaTeX editor,
or compile from this directory:

```bash
latexmk -pdf -interaction=nonstopmode -halt-on-error Harmonia-EA-Capability-Map.tex
```

The generated project supports PDFLaTeX with DejaVu TeX fonts and Unicode-safe
text wrapping; XeLaTeX remains supported. Standard latexmk handles references,
contents and makeindex. Its project configuration contains no generator hooks.
Included sources carry root-document hints. The README separates regeneration,
ordinary compilation, editing and cleanup.

Validation: **PASS**. Clean ordinary builds succeeded in place and in an external
copy containing only the master, 28 included TeX files and `.latexmkrc`. Compiler
inputs were limited to that copy and installed TeX packages/fonts. Compilation
invoked only PDFLaTeX and makeindex; no Make target or generator was involved.

The resulting PDF has 1,224 pages, all 862 index entries and the same 916 bookmarks
and 2,681 label numbers as the accepted publication. There are no duplicate labels,
undefined references, missing glyphs, overfull boxes or final LaTeX warnings.
Representative pages were visually checked. A temporary prose edit appeared after
ordinary recompilation; the edit was reverted and the restored PDF verified.

Architectural data, publication prose, hierarchy, numbering, identities, statuses,
relationships and provenance are unchanged. Semantic-baseline checks and all
90 architecture tests pass. See [standalone-validation.json](standalone-validation.json)
and [validation.json](validation.json) for evidence.
