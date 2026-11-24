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
# AI Agent Task Details: V001 Increment 001 Attempt 2 — Foundational Calliope Information Models

## Metadata
- **Task Identity:** V001 Increment 001, Attempt 2
- **Governed Branch:** `Mneme-and-Mnemosyne-Build`
- **Architectural Authority Baseline:** Accepted 1.1.25 Architecture Continuum
- **Task-Issued-At:** `2026-11-15T05:44:00Z`
- **Agent Assigned:** Junie (Autonomous Agent)

## Task Instructions
```
# Harmonia — V001 Increment 001, Attempt 2

## Objective

Implement the foundational Calliope information models required by Mneme and Mnemosyne, using the **accepted 1.1.25 Architecture Continuum** as architectural authority.

This is a bounded implementation increment, not an invitation to redesign the architecture.

## 1. Establish the baseline
Before modifying code:
1. Confirm the current branch and accepted 1.1.25 baseline.
2. Read the applicable repository governance and implementation guidance.
3. Identify the accepted Topic/TopicMask, InformationUnit and OperationalMetadata design sources.
4. Inspect the existing Calliope implementation.
5. Identify all changes remaining from the rejected first attempt.

## 2. Implementation sequence
### Stage A — Topic and TopicMask
Implement the accepted foundational Topic and TopicMask contracts:
- Immutable Topic definitions with UUID identity.
- Explicitly sequenced qualified canonical names.
- Derived Topic names.
- Default qualifiers N0 through Nn.
- Immutable TopicMask definitions with UUID identity.
- TopicNameBased and TopicCanonicalNameBased matching.
- Element-wise regular expressions.
- Exact and Inclusive matching semantics.
- Unrestricted default matching.

### Stage B — InformationUnit foundation
Implement the accepted InformationUnit structure and its associated information model:
- IU identity and authoritative persisted version.
- InformationCoordinates with its four accepted properties.
- InformationItinerary as a distinct concern with strongly typed Topic references.
- Information-related security and lifecycle semantics.
- Creator-supplied IU relationships.
- Exactly one InformationRepresentation.
- ContainerMetadata, ContentMetadata and Content.
- Portable Content representation and appropriate references.

### Stage C — OperationalMetadata foundation
Implement the accepted separation of OperationalMetadata from InformationMetadata:
- Authoritative persisted IU version.
- Mneme operational revision.
- Cache Active Generation.
- Mneme–Mnemosyne persistence coordination.
- Governed information lifecycle.
- Native Content version and Content Structure Version.

## 3. Testing & Invariants
Verify all changes against automated unit, serialization, and ArchUnit architecture tests.
```
