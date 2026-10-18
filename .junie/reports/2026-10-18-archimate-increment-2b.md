<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Increment 2B — ArchiMate ingestion and publication semantics

**2026-10-18 — Architecture Release 1.0.10; bounded governance increment.**

The governed 2B contract is ready for Solution Architect review. It formalises
the rules applied by 2A Change Review and later authorised reconciliation and
publication. No reconciliation/layout engine, blind-test adjudication, incoming
identity allocation or semantic model change was performed.

## Governance changes

| Document | Bounded change |
| --- | --- |
| [Ingestion/publication semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md) | New authoritative RADS contract covering the 2B rules and explicit deferrals. |
| [Canonical identity/naming](../../docs/markdown/governance/canonical-identity-and-naming.md) | Establishes universal native `Name == displayName`, supersedes separate native-name preservation in future governed results, and recognises post-acceptance machine metadata. Identity/name syntax is unchanged. |
| [Navigation/View convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md) | Links the distinct Containment View archetype and authored-View preservation; explicitly qualifies historical instructions to preserve different native Names. Historical examples/artefacts remain intact. |
| [2A Change Review](../../docs/markdown/governance/archimate-ingestion-change-review.md#semantic-relationships-navigation-and-views) | Adds the 2B integration reference without changing dispositions or executable-instruction authority. |
| [Governance index](../../docs/markdown/governance/README.md) | Makes 2B and its unresolved boundaries discoverable. |
| [Baseline correspondence](../../docs/markdown/governance/baseline/correspondence.json) | Updates only two current-governance document hashes and adds the new 2B authority hash. No semantic source/crosswalk, recovery pin or identity-establishment history changes. [Exact pin changes](2026-10-18-archimate-increment-2b/authority-pin-changes.json). |
| [Release authority](../../architecture-release.json) | Existing helper applied 1.0.9 → 1.0.10; previous_version is 1.0.9. |

This report and its [evidence bundle](2026-10-18-archimate-increment-2b/) are
execution evidence, not architectural authority. Existing uncommitted 2A work,
incoming workbench changes and blind-analysis reports were preserved.

## Rules formalised and existing authority reused

Fresh repository authority was read: root/docs instructions, axioms, canonical
identity/naming, navigation/View convention, mapping, publication/release gates
and 2A. AX-04 leaves meaning with governed architecture; AX-14 separates native
type/classification, identity, semantics, navigation and presentation; AX-15
preserves uncertain processing outcomes; AX-17 requires explicit adjudication of
missing architecture. The approach changes no subsystem/runtime boundary and
introduces no conflict with these axioms.

- Native Name equals governed displayName for every governed element. Existing
  qualifier rules apply only where established and compatible with native type;
  arbitrary brackets create neither classification nor a subtype.
- Accepted valid explicit Composition supplies containment. Folders, geometry
  and Views supply none. Naming uses authoritative semantic context, preserving
  existing source-qualified context and standing without inventing native edges.
  An element without containment can retain local governed names.
- Missing machine metadata on a new submission is expected. After acceptance
  and resolution of required inputs, reconciliation can supply applicable fields
  under existing syntax/history rules. Publication never allocates identity.
- Explicit relationships are proposals with native type, direction, endpoints
  and representation evidence. New edges, new appearances and duplicates remain
  distinct. Comparison uses resolved governed endpoints/type and any material
  assertion discriminator; incoming Exchange IDs never become architectural IDs.
- Existing source-based relationship navigation is applied during reconstruction.
  Views reuse semantic edges, and accepted authored presentation is preserved
  independently of generated deterministic publication/navigation Views.
- The Containment View selects one source and its relevant direct Composition
  children/edges. Source-left layout uses one child column for 1–10 and two for
  11–20, with second-column centres/routes in geometry-derived first-column gaps.
  The partition must provide enough gaps; no arbitrary ten-node first-column cap
  is imposed. Membership, references, no semantic mutation, geometry and repeat
  determinism have explicit future conformance expectations. No engine is built.
- Ingestion, adjudication, reconciliation and publication remain separate. 2A
  decisions/instructions govern acceptance and execution; 2B governs authorised
  representation without rewriting submission evidence.

Existing identity formats, name spelling/qualification, persistence/non-reuse,
relationship assertion distinctions, native mapping legality, source homes,
View identity/history, CR dispositions/dependencies and release gates are
referenced rather than redefined. The mapping profile's disposable-projection
wording does not authorise discarding accepted authored Views. Historical grid
layouts are not retroactively relaid out by the new archetype.

## Conformance and migration findings

A read-only audit of the governed published **1.0.8** model checked all **1,299**
elements: **841** have different native Name/displayName values, all native
Capabilities; **458** conform, and no displayName is missing. This includes
`Identifier Resolution` versus `[Feature] Identifier Resolution` on representation
`id-f1e1928516765880955f638a7fec8c35`. The
[complete finding list](2026-10-18-archimate-increment-2b/name-displayname-conformance.json)
retains affected representation/Canonical IDs and exact values.

These are existing publication conformance findings requiring a separately
authorised migration of affected native Names while retaining governed identities
and naming history. No current release rule requires that migration during this
governance adoption. No source/model name, existing XML or blind-analysis evidence
was repaired. Existing publication tooling does not yet enforce the new invariant
or implement the Containment View. Successful preservation-only regeneration is
not a claim that its output conforms to either new rule.

## Release and validation

The root authority at task start was 1.0.9 / previous 1.0.8, independently checked
from the repository. `architecture_release.py bump --expect 1.0.9` produced
1.0.10 / previous 1.0.9. No Maven/minor/major change was made. The release and
governance changes form one reviewable change set; no commit or human acceptance
is claimed.

All commands used `timeout --signal=TERM --kill-after=5s` with the bounds below.

| Verification | Result |
| --- | --- |
| Required offline Maven architecture suite, JDK 21, 180s | **PASS**, 90 tests, zero failures/errors/skips; Maven 30.522s. [Log](2026-10-18-archimate-increment-2b/architecture-tests.log). |
| Existing release/identity unit tests, 60s | **PASS**, 25 tests in 28.429s. [Log](2026-10-18-archimate-increment-2b/release-identity-tests.log). |
| Release validation, 30s | **PASS**, 1.0.10 / previous 1.0.9. [Log](2026-10-18-archimate-increment-2b/release-check.log). |
| RADS source/reference validation, 60s | **PASS**, zero invalid, 2,467 pre-existing unresolved findings retained. [Log](2026-10-18-archimate-increment-2b/rads-validation.log). |
| Canonical identity/naming metadata validation, 60s | **PASS**, 1,299 valid Canonical IDs; no missing/malformed/duplicate/multiple claims; deterministic existing names and retained history validate. [Log](2026-10-18-archimate-increment-2b/metadata-validation.log). |
| Independent repeated generation, 90s | **PASS**, existing generator runs twice, with source/identity/reference/endpoint and offline bundled XSD gates. [Log](2026-10-18-archimate-increment-2b/independent-generation.log), [generated validation](2026-10-18-archimate-increment-2b/independent-generation-validation.json). |
| Output comparison and protected-file audit, 30s | **PASS**, XML differs from 1.0.8 only in one model-level release value; all 10 generated RADS files are byte-identical. Existing inputs, models, blind reports/evidence and unrelated work remain unchanged. [Detailed audit](2026-10-18-archimate-increment-2b/documentation-preservation-validation.json). |
| Task-document links/anchors and focused whitespace, 30s | **PASS**, local targets checked with the existing RADS anchor helper; focused `git diff --check` passes. Same detailed audit. |
| New universal Name conformance | **NONCONFORMANT**, 841 existing mismatches recorded above; migration deferred by task scope. No repair attempted. |

Commands, from the repository root (each with the bound above):

```bash
env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
python3 -B -m unittest discover -s scripts/tests -p 'test_architecture_*.py' -v
python3 -B scripts/architecture_release.py check
python3 -B scripts/rads_business_architecture.py validate
python3 -B docs/archimate/framework/tools/governed_metadata.py validate
python3 -B docs/archimate/framework/tools/synchronise_baseline.py --output-directory /tmp/harmonia-increment-2b-1.0.10
python3 -B .junie/reports/2026-10-18-archimate-increment-2b/validate-increment.py
```

Independent reproduction was written only under `/tmp`; no new repository
exchange publication/package or ingestion run/CR was created. Link validation is
bounded to task documents and their targets, not a whole-repository link repair.
No GUI import/rendering or human Archi acceptance is claimed. No timeout/stall
occurred. The audit initially looked for the generic validation filename; it was
corrected to the existing release-qualified publication-validation filename
without rerunning or changing generation.
The initial link audit preceded its own three generated evidence files; after
those outputs existed, the final audit passed. Its
[initial result](2026-10-18-archimate-increment-2b/documentation-preservation-initial.json)
is retained and does not indicate a governance or model failure.

## Explicit unresolved matters and stop

1. **Universal CT3 → Feature Composition:** mapping §5 still explicitly defers
   the native framing edge. Blind-test edges are proposals, not precedent.
2. **Universal Realization → Feature logical context:** remains ungoverned.
   Per-instance 2A adjudication may authorise extra context while retaining
   Realization and creating no substitute/additional Composition. No instance
   was adjudicated here.
3. **More than 20 direct Containment View children:** unsupported/unresolved;
   no third column, View splitting, pagination or alternative was selected.

**STOP:** 2B governance, required patch handling and bounded validation are
complete. Return to the Solution Architect for review. No reconciliation,
blind-test processing, identity allocation to incoming additions, deferred
metamodel adjudication, layout implementation or Increment 2C follows.
