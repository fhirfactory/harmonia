Task-Issued-At: UNRESOLVED

Event source: the operative user instruction for this bounded correction task; its attributable system timestamp is not available in the retained interaction context. The timestamp is intentionally unresolved rather than inferred.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Calliope Naming — Codex Review Corrections Task

**Agent:** Junie  
**Role:** Software correction, testing and candidate evidence  
**Software target:** `0.1.0-SNAPSHOT`  
**Branch:** `feature/BL-0003-Calliope-Topic-and-TopicMask`  
**Backlog authority:** Backlog Item 0003 revision `000.004`  
**Detailed design:** `DRP-Topic-and-TopicMask.Final.003.md`  
**Review context:** Codex independent Naming implementation review, Software HCRL `software-0.1.0/010`  
**Starting commit:** `680e8032289db9e97f2aeefa9783ba48d64f1b53`

## Authorised work

Apply the bounded review corrections specified by the operative assignment:

1. Reject XML 1.1 and accept XML 1.0 only; add regressions for Topic and
   TopicMask input, including acceptance of a missing declaration and XML 1.0.
2. Enforce the maximum ordinary TopicMask depth at the model construction
   boundary, including direct completed-entry reconstruction.
3. Improve typed validation metadata when the offending field is unambiguous,
   without exposing payload text.
4. Add XML resource-bound regressions for a maximum-sized record, an oversized
   record, excessive entries and malformed XML at the size boundary.

Finding 2 (existing XML parsing approach and validation order), Finding 3
(bounded numeric scanning), functional package organisation and Java
serialization are accepted without implementation changes. Preserve accepted
Naming semantics and the existing architectural decisions. Do not edit the
accepted design, backlog item or HCRLs; identify any required reconciliation
for Solution Architect treatment. Do not commit or push. Present the resulting
source as an unaccepted review candidate.

## Verification required

Use JDK 21. Run `mvn -B -ntp -pl calliope -am test` and
`mvn -B -ntp verify`, plus applicable Javadoc, architecture-selector and
dependency checks. Record actual executions and outcomes without treating a
missing architecture test suite as a pass.

## Starting-state and preservation notes

The working branch and `HEAD` matched the stated starting commit. The task
correction files were modified in this worktree. A separate unscoped change in
`calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/NamingSerialization.java`
(`/**what`) was observed and left untouched; it is excluded from the candidate
patch. The task timestamp remains unresolved; no filesystem or current-time
substitute is asserted.