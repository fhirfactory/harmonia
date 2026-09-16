# Harmonia Architecture Specification (LibreOffice / OpenDocument Edition)

This directory contains the publication-grade, interactive **LibreOffice OpenDocument Text (`.odt`)** architectural specification for the **Harmonia Health Information Exchange (HIE)** platform.

The document provides 100% content parity with the primary LaTeX specification in `docs/latex/`, complete with embedded 300 DPI ArchiMate 3.2 architectural views, formatted technical data tables, color-coded callout blocks, and interactive navigation (clickable Table of Contents, List of Figures, List of Tables, and internal cross-reference bookmarks).

---

## 1. Directory Structure

```
docs/libreoffice/
├── Makefile                               # Automated build & validation tooling (make, make validate, make clean)
├── README.md                              # This developer and user guide
├── harmonia-architecture-specification.odt# Ready-to-use master LibreOffice specification document
├── assets/
│   └── diagrams/                          # 300 DPI high-resolution PNG architectural views
│       ├── legend.png                     # ArchiMate 3.2 notation, element types & relationship arrows
│       ├── fig-motivation-map.png         # Motivation & Strategy View
│       ├── fig-clinical-process.png       # Business / Clinical Process View
│       ├── fig-app-overview-5tier.png     # 5-Tier Application Component Landscape View
│       ├── fig-petasos-messaging.png      # Petasos Messaging & Clustered Transport View
│       ├── fig-energeia-workflow.png      # Energeia Workflow & Camel Sequence View
│       ├── fig-pragma-state-flow.png      # Pragma State Machine & Checkpoint Lifecycle View
│       ├── fig-technology-nodes.png       # Technology Infrastructure & Port Allocation View
│       ├── fig-hestia-data-grid.png       # Hestia In-Memory Data Grid & Persistence View
│       ├── fig-deployment-ha.png          # ActiveMQ Artemis 4-Node HA Replication View
│       ├── fig-paradeigma-architecture.png# Paradeigma Simulation & Testbed Component View
│       ├── fig-mllp-ingress-process.png   # MLLP Ingress Pipeline View
│       ├── fig-mllp-egress-process.png    # MLLP Egress Pipeline View
│       ├── fig-ergon-module-architecture.png # Ergon Activity Processing Topology View
│       └── fig-praxis-workflow-architecture.png # Praxis Sequence & Route Synthesis View
├── templates/
│   ├── styles.xml                         # OpenDocument style definitions (typography, headers, footers)
│   └── manifest.xml                       # META-INF manifest declaration
└── scripts/
    └── generate_odt.py                    # Zero-dependency Python builder and compiler
```

---

## 2. Viewing and Editing

The master document (`harmonia-architecture-specification.odt`) adheres to the **OASIS OpenDocument Format (ODF) 1.3** standard and can be viewed and edited in:

- **LibreOffice Writer** (Version 7.x or 8.x recommended) — full native support for outline levels, header/footer page numbering, and vector image scaling.
- **Collabora Office / Apache OpenOffice** — full compatibility.
- **Microsoft Word** (2016, 2019, 2021, Microsoft 365) — native support via OpenDocument Text filter.
- **Google Docs** — importable via Google Drive.

### Interactive Navigation Features
- **Table of Contents (TOC)**: Click on any chapter or section heading to jump directly to that section.
- **List of Figures (LOF)**: Click on any figure entry to jump to the architectural diagram and caption.
- **List of Tables (LOT)**: Click on any table entry to jump to the corresponding data table.
- **Internal Cross-References**: In-text references to figures, tables, and appendices (e.g., `Figure 3.1`, `Appendix B`, `Table 5.2`) are live clickable hyperlinks.

---

## 3. Automated Document Generation

The specification is compiled directly from the modular LaTeX sources in `docs/latex/` using a standalone Python 3 generator with zero external compilation dependencies.

### Quick Build with Make
```bash
cd docs/libreoffice
make
```

### Direct Python Execution
```bash
cd docs/libreoffice
python3 scripts/generate_odt.py
```

### Validating Document Integrity
```bash
cd docs/libreoffice
make validate
```

---

## 4. Document Styling & Visual Palette

The document follows the Harmonia brand typography and ArchiMate layer color guidelines:

### Typography
- **Headings**: Liberation Sans / Arial Bold
  - Title: 26pt Bold `#1A365D`
  - Subtitle: 15pt Bold `#2B6CB0`
  - Heading 1 (Chapters / Appendices): 18pt Bold `#1A365D`
  - Heading 2 (Sections): 14pt Bold `#2B6CB0`
  - Heading 3 (Subsections): 12pt Bold `#2C5282`
  - Heading 4 (Subsubsections): 11pt Bold `#4A5568`
- **Body Text**: Liberation Serif / Times New Roman (11pt, 1.15 line spacing, 6pt bottom margin).
- **Code Listings & Identifiers**: Liberation Mono / Courier New (9pt on `#F7FAFC` background with `#E2E8F0` border).

### Technical Tables
- **Header Row**: Dark Navy fill (`#1A365D`), bold white text (`#FFFFFF`).
- **Alternating Body Rows**: Pure white (`#FFFFFF`) / soft light tint (`#F7FAFC`), subtle grid borders (`#E2E8F0`).
- **Header Repetition**: Tables automatically repeat header rows across page breaks.

### Callout Blocks
- **Architectural Principle**: Teal left accent border 4pt (`#319795`), soft teal background (`#E6FFFA`), dark teal text (`#234E52`).
- **Architectural Note / Tip**: Blue left accent border 4pt (`#3182CE`), soft blue background (`#EBF8FF`), dark blue text (`#2B6CB0`).
- **Warning / Operational Alert**: Amber left accent border 4pt (`#DD6B20`), soft amber background (`#FFFAF0`), dark brown text (`#7B341E`).

---

## 5. Architectural Diagram Assets

All 15 architectural viewpoints are embedded as crisp 300 DPI high-resolution raster images rendered directly from vector TikZ definitions:

| Figure | Architectural Viewpoint | Diagram Asset |
| :--- | :--- | :--- |
| **Figure 1** | ArchiMate 3.2 Notation, Palette & Connector Guide | `legend.png` |
| **Figure 2** | Motivation & Healthcare Strategy Architecture | `fig-motivation-map.png` |
| **Figure 3** | Business & Clinical Event Processing Architecture | `fig-clinical-process.png` |
| **Figure 4** | 5-Tier Application Component Landscape | `fig-app-overview-5tier.png` |
| **Figure 5** | Petasos Clustered Messaging Subsystem | `fig-petasos-messaging.png` |
| **Figure 6** | Energeia Workflow Engine & Sequential Erga Route Pipeline | `fig-energeia-workflow.png` |
| **Figure 7** | Pragma Task State Machine & 5-Phase Checkpoint Lifecycle | `fig-pragma-state-flow.png` |
| **Figure 8** | Technology Infrastructure, System Software & Port Paths | `fig-technology-nodes.png` |
| **Figure 9** | Hestia In-Memory Data Grid & Write-Behind Persistence | `fig-hestia-data-grid.png` |
| **Figure 10** | Physical Deployment & ActiveMQ Artemis Multi-Broker HA | `fig-deployment-ha.png` |
| **Figure 11** | Paradeigma Simulation Subsystem Component Landscape | `fig-paradeigma-architecture.png` |
| **Figure 12** | MLLP Ingress Gateway Processing Pipeline | `fig-mllp-ingress-process.png` |
| **Figure 13** | MLLP Egress Multi-Instance Dispatch Pipeline | `fig-mllp-egress-process.png` |
| **Figure 14** | Ergon Activity Module Processing Topology | `fig-ergon-module-architecture.png` |
| **Figure 15** | Praxis Workflow Sequence Architecture & Route Synthesis | `fig-praxis-workflow-architecture.png` |
