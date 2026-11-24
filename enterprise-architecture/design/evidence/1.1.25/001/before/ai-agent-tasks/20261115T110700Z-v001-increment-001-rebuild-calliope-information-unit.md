<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->
# AI Agent Task Details: V001 Increment 001 — Comprehensive Rebuild of Calliope InformationUnit Model

## Metadata
- **Task Identity:** V001 Increment 001 Comprehensive Rebuild
- **Governed Branch:** `Mneme-and-Mnemosyne-Build`
- **Architectural Authority Baseline:** Accepted 1.1.25 Architecture Continuum
- **Task-Issued-At:** `2026-11-15T11:07:00Z`
- **Agent Assigned:** Junie (Autonomous Agent)

## Task Instructions
```
## Issue Task
The current Calliope InformationUnit implementation misses the accepted architecture by a wide margin. Some legacy implementation decisions have been preserved despite being inconsistent with the architectural model.

This needs to be corrected comprehensively, not through another series of superficial, property-by-property patches.

Fundamental Harmonia implementation principles:
1. Topic is the default descriptor and identity/classification mechanism for named concepts, unless there is a sound architectural reason otherwise.
2. Topic is the default mechanism for identifying and classifying types, unless there is a sound architectural reason otherwise.
3. Where applicable, an associated Version describes the version of the identified concept, model or structure. Topic itself has no Version property.
4. Java Strings should represent genuinely textual values, not substitute for governed identities or classifications.
5. Existing Java code, including committed historical code, is not architectural authority. Preserve compatibility only where it is architecturally justified, not merely because an implementation already exists.

Harmonia has an extensive accepted architectural and requirements model.
The authoritative Architecture Continuum is located at:
enterprise-architecture/continuum/
Supporting design decisions are located at:
enterprise-architecture/design/offline-design/

Your task:
Rebuild the Calliope InformationUnit model and its associated foundational elements so that they faithfully represent the accepted architecture.
Do not merely refactor the existing implementation. Reconsider its modelling assumptions, class responsibilities, types, relationships, invariants and representation boundaries.
Apply sound Java engineering judgement, but do not substitute conventional Java implementation patterns for the intended architectural semantics.
In particular, examine Topic, TopicMask, InformationUnit, InformationMetadata, OperationalMetadata, InformationRepresentation, Content and their associated types and references.
Implement appropriate tests for the architectural semantics, not merely tests that confirm the behaviour of your chosen implementation.
Provide a concise report identifying significant modelling decisions, corrected architectural deviations, unresolved questions and actual validation results.
Stop for Solution Architect review before proceeding to Mneme/Mnemosyne service implementation.
```
