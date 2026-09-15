# Harmonia Architecture Specification (LaTeX / ArchiMate 3.2)

This directory contains the production-grade, publication-ready architectural specification for the **Harmonia** Health Information Exchange (HIE) platform, modeled strictly in accordance with **The Open Group ArchiMate 3.2** standard and rendered using high-precision TikZ vector graphics.

---

## 1. Directory Structure

```
docs/latex/
├── Makefile                       # Automated build tooling (make, make clean, make docker)
├── README.md                      # Compilation instructions, prerequisites & package guide
├── main.tex                       # Master root document aggregating chapters and diagrams
├── styles/
│   ├── harmonia-archimate.sty     # ArchiMate 3.2 element styles, colors, and relation arrows
│   └── harmonia-doc.sty           # Typography, geometry, listings, headers/footers & hyperref
├── chapters/
│   ├── 00-frontmatter.tex         # Title, executive summary, notation guide, and terminology
│   ├── 01-motivation-strategy.tex # Stakeholders, drivers, goals, principles & capabilities
│   ├── 02-business-layer.tex      # Healthcare business actors, clinical services & workflows
│   ├── 03-application-layer.tex   # 5-tier application components, services & interactions
│   ├── 04-data-architecture.tex   # Pragma model, Petasos envelope, FHIR R5 schemas & data flows
│   ├── 05-technology-layer.tex    # Nodes, containers, system software, networks & ports
│   └── 06-deployment-ha.tex       # HA clustering, replication groups, persistence & recovery
└── diagrams/
    ├── legend.tex                 # ArchiMate notation, element types & relationship arrows
    ├── fig-motivation-map.tex     # Motivation & Strategy View
    ├── fig-clinical-process.tex   # Business / Clinical Process View
    ├── fig-app-overview-5tier.tex # 5-Tier Application Component Landscape View
    ├── fig-petasos-messaging.tex  # Petasos Messaging & Clustered Transport View
    ├── fig-energeia-workflow.tex  # Energeia Workflow & Camel Sequence View
    ├── fig-pragma-state-flow.tex  # Pragma State Machine & 5-Phase Checkpoint Lifecycle View
    ├── fig-hestia-data-grid.tex   # Hestia In-Memory Data Grid & Write-Behind Persistence View
    ├── fig-technology-nodes.tex   # Technology Infrastructure & Port Allocation View
    └── fig-deployment-ha.tex      # 4-Node Multi-Broker HA Deployment & Failover View
```

---

## 2. ArchiMate 3.2 Layer Mapping

The documentation is structured across the standard ArchiMate architectural layers:

| Layer | Hex Color Scheme | Key Architectural Entities in Harmonia |
| :--- | :--- | :--- |
| **Motivation & Strategy** | `#E6D0DE` (Lilac) / `#F2E3C6` (Ochre) | Healthcare Providers, Clinicians, Patients, Interoperability Drivers, 24/7 Availability, Inbound & Outbound Gateway Capabilities, End-to-End Value Streams. |
| **Business / Clinical** | `#FFFFB5` (Amber / Yellow) | Referring Hospitals, Diagnostic Labs, Clinical Ingestion & Outbound Dispatch Services, Patient Matching & Reconcile Processes. |
| **Application** | `#B5FFFF` (Cyan / Light Blue) | `Calliope`, `Pylai` (`pylai-mllp-in`, `pylai-mllp-out`, `pylai-mllp-base`, `pylai-mllp-cli`), `Petasos`, `Energeia` (`Ponos`, `Praxis`, `Erga`), `Hestia` (`Mneme`, `Mnemosyne`), `Iris` (`BEFE`, `Clinical UI`, `Console UI`). |
| **Data / Information** | `#D1F2EB` (Pale Teal) | `Pragma` task state model, `OutboundMllpRequest` / `Response`, `PetasosMessage` envelope, `ErgonPayload`, and 14 FHIR R5 Resources (`Patient`, `Task`, `Communication`, `Provenance`, `AuditEvent`, etc.). |
| **Technology / Software** | `#C9E4B5` (Light Green) | WildFly Jakarta EE 10, Apache ActiveMQ Artemis 2.38+, Infinispan 15+, PostgreSQL 16+, TCP/IP, MLLP (2575), Outbound Gateways (8087, 8088), HotRod (11222-11223), JMS (61616-61619). |
| **Physical / Deployment** | `#D5E8D4` (Slate Green) | Multi-broker Primary/Backup replication pairs (`group-a`, `group-b`), Docker Compose container topologies, and failover pathways. |

---

## 3. Compilation Requirements & Prerequisites

### TeX Live Packages
To build the specification PDF, ensure TeX Live or MacTeX is installed with the following packages:
- `texlive-latex-base`
- `texlive-latex-extra`
- `texlive-pictures` (PGF/TikZ)
- `latexmk`

On Debian/Ubuntu/Mint:
```bash
sudo apt-get update
sudo apt-get install -y texlive-latex-base texlive-latex-extra texlive-pictures latexmk
```

On macOS (via Homebrew):
```bash
brew install --cask mactex
```

---

## 4. Building the Documentation

### Using Make (Recommended)
```bash
cd docs/latex
make
```

### Direct CLI Compilation
```bash
cd docs/latex
latexmk -pdf main.tex
```

Or using `pdflatex`:
```bash
cd docs/latex
pdflatex -interaction=nonstopmode main.tex
pdflatex -interaction=nonstopmode main.tex
```

### Using Docker (Zero Local TeX Installation)
If TeX Live is not installed locally, compile using the official TeX Live Docker image:
```bash
cd docs/latex
make docker
```
