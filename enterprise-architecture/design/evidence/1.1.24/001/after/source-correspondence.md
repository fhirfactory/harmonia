<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Approved inputs, source succession and semantic correspondence

Mark Hunter's [operative Task](../before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md)
approves both complete Final.001 DRPs and authorises this bounded ADD. Internal
pre-approval wording is historical. The exact approved inputs are tracked at entry
HEAD `8204fd49a4d380c068a03acdf30c17bb06e5d953`; [before pins](../before/baseline.json)
and [preservation verification](preservation.json) establish unchanged bytes.

- [Interaction Final.001](../../../../offline-design/DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md):
  SHA-256 `9696e135a47fe5bd0d568a7f88624b64cb172d1c565c6529a60311b37431b3a6`.
- [Cache Final.001](../../../../offline-design/DRP-Mneme-Cache-Architecture.Final.001.md):
  exact SHA-256 in the before pins and section inventory.

This is the bounded source-succession record under Incorporation Praxis §9. Both
unchanged sources now serve as approved decision/input provenance; the canonical
resources below represent their incorporated definitions and obligations. The DRPs
are not parallel active multi-element definitions. Pending-result standing and
technical/detail qualifications remain explicit. No neighbouring source is accepted,
migrated, rewritten or retired by this task. Source references to adjoining drafts
retain provenance/context roles and confer no further authority.

## Canonical identity selection

| Responsibility | Canonical identity and treatment |
| --- | --- |
| Full read | [GovernedReader](../../../../../continuum/application/application-service/410a938f42964474b7cf5655815dc830.md), unchanged identity; refined full Content acquisition, exact versions and reconciliation |
| Full write | [GovernedWriter](../../../../../continuum/application/application-service/105f6ebe61f74bc6b51938ba550bc1a7.md), unchanged identity; coherent Content/reference/Summary, actual resulting version and multi-site Success |
| Sole search | [GovernedSummarySearch](../../../../../continuum/application/application-service/dcb31810ae134818adeeba9b27bd2f30.md), historical Search identity retained; old names in `naming.previousNames`, prior source prose explicitly superseded |
| Summary read | [GovernedSummaryReader](../../../../../continuum/application/application-service/2988247b75664896a7deb97b12b20c0a.md), missing independently usable service justifies one new ApplicationService |
| Summary CREATE/RETIRE | [GovernedSummaryWriter](../../../../../continuum/application/application-service/bffb611e29844eedaf39a5b27ccbbcc7.md), missing independently usable service justifies one new ApplicationService |
| Standard Cache | [Mneme-Standard-Cache](../../../../../continuum/application/application-function/cfe4e0dbe6894a14841452b731ab9cb8.md), one ApplicationFunction for bounded active reference-cache behaviour |
| Workflow Cache | [Mneme-Workflow-Cache](../../../../../continuum/application/application-function/0bddade1309e426aa84d0cd97c35742a.md), one ApplicationFunction for bounded local materialised working-cache behaviour |
| Standard machinery | [Infinispan Server](../../../../../continuum/technology/system-software/c2b216d461b24d659c202268f0ec7fb7.md), selected SystemSoftware configuration, no prescribed cluster deployment |
| Workflow machinery | [Embedded Infinispan LOCAL](../../../../../continuum/technology/system-software/4048a6c775c542b6a29610d9e248a4d8.md), selected SystemSoftware configuration, inside applicable existing execution JVMs |

All six allocations used existing native profiles and the governed `create` operation.
Runtime cache instances/generations and JVM multiplicity do not allocate architectural
identities. No new Requirement, Feature, Business activity, IU, Summary entity,
SearchResult entity, Topic, Interaction metamodel, transaction identifier or persistence
responsibility is required. Six formal canonical names remain explicitly unestablished;
accepted display/friendly names and Home/native relations supply reviewable context.

Existing Store, Façade, Mneme, Mnemosyne, Calliope information models, Search Result
Set, Ponos, DigitalTwin, Dokimasia, common Ergon, Themis, Kleio and persistence
responsibilities remain. [Selected identities](selected-identities.json),
[allocation reasons](identity-allocation.json), [51 native relationships](native-relationship-correspondence.json)
and [ten actual Feature paths](feature-correspondence.md) supply exact correspondence.
Native Home, Assignment, Serving, Access and partial Realization retain their meanings;
governing applicability Associations do not claim satisfaction or replace stronger
native operational relationships. Business and Strategy definitions remain unchanged.

## Complete source section accounting

The [section inventory](source-section-inventory.json) pins every top-level source
section and its complete nested text by exact line span and SHA-256. The grouped
dispositions here cover all sections, not only the accepted review-decision tables.

| Interaction source | Incorporated treatment / retained boundary |
| --- | --- |
| Title/preamble; §0 | Exact Final.001 input lineage and subsequent complete approval retained in Task/HCRL; current entry state replaces historical commit locators for this activity |
| §§1.1–1.3 | Existing Requirements, axioms, Feature ancestry and adjoining responsibilities reused; no new motivation/strategy identities |
| §§2–3 | Five-contract seam through established Store/Façade/components and their existing Flow; no new interaction object or deployment boundary |
| §4.1 | Declarative synchronous governed contracts; exact implementation interfaces and transport deferred |
| §4.2 | Store required criteria; Façade supported criteria; complete eligible union coverage, per-request matching, configured identities/sites, verification and shortage alarms |
| §§4.3–4.3.1 | Same IU/version/Representation, Summary excludes exactly Content, opaque reference in ContentMetadata; logical separate management without mandatory physical split |
| §4.4 | SummaryReader definition and IU/Outcome Access, Façade Realization, Store/Standard-cache Serving and Home |
| §4.5 | SummaryWriter CREATE/RETIRE only; no external Content fetch/verification; SEP; independent external referent lifecycle; immutable history retained |
| §4.6 | Existing Search identity refined; R1/R2 predicates, matching, indexing domain and results Summary-only; logical SearchResult fields, disclosure, focused usage and originating-instance continuation |
| §4.7 | Full Reader materialisation attempt and large-binary exception; absence/denial/failure/uncertainty; exact versions; orthogonal consumer cache/wait choices |
| §4.8 | Full Writer Content/reference/Summary coherence, multi-site durable Success, actual higher version, immutable history, per-IU concurrency and evidence-based reconciliation |
| §4.9 | Existing Calliope Outcome used by every service; closed statuses/common Reason plus governed domain extensions; factual effect, reporting stage and consumer disposition distinct |
| §4.10 | Themis action/disclosure and source-near audit/provenance retained in service definitions; Kleio mandatory capture authority remains fail-closed; no speculative compulsory event list |
| §4.11 | Deterministic instance topology and dynamic eligible selection; AV decisions and boundary-only Ergon scheduling context; no new scheduling API |
| §§4.12–4.13 | Common invariants and all FC decisions retained within owning service/Store/cache definitions; no decision-specific service proliferation |
| §5 | Explicit exclusions retained in owning definitions and HCRL DR-5 |
| §6 | Rationale and source provenance retained; existing Java is not architectural authority and is unchanged |
| §7 | Exact predecessor identities and source-stage qualifications preserved; 1.1.22/1.1.23 are not reopened |
| §8 | Automated architecture/projection/preservation checks and deferred runtime conformance distinguished; successful static validation claims no deployed behaviour |
| §§9.1–9.2C | Accepted predecessor decisions and all AV/FC/MI/SearchResult decisions mapped below |
| §§9.3–9.4 | MI-01/03/08 deterministic lineage/classification/identity reconciliation; MI-02/06/07 detail remains with owning contracts; MI-04/09 inherited security/evidence obligations retained without invented catalogue |
| §§10.1–10.7 | Connected Motivation/Strategy/Business/Application/Technology expression through existing identities, native relationships and scoped six additions; no layer-filling elements |
| §§11–12 | Historical whole-document approval fields superseded by subsequent operative approval in Task/HCRL; actual result still awaits SA acceptance |
| Appendices A–B | Historical aliases/source succession and accepted adjudication correspondence retained unchanged; exact Final.001 locators govern these inputs |

| Cache source | Incorporated treatment / retained boundary |
| --- | --- |
| Title/preamble; §0 | Complete approval and bounded activity recorded; source-stage wording retained unchanged |
| §§1.1–1.3 | Existing Feature paths, active-access/durability/security/availability Requirements and axioms reused |
| §2 | Two bounded cache functions and selected machinery; no new authority or execution framework |
| §3.1 | Two legitimate ingress paths and reference workflow recorded in Standard-cache/full-service definitions; no universal content replication/caching |
| §§3.2–3.3 | Same IU/version working manifestations; cache residence/generation is not authoritative identity or durable acceptance |
| §4.1 | Distributed reference-based Standard Cache; Server/Hot Rod; synchronous direction qualified by MC-01/02 |
| §4.2 | Embedded LOCAL materialised Workflow Cache; independent immutable working copies; no distributed L1, affinity registry or inter-JVM copy replication |
| §§4.3–4.4 | Existing Ponos/DigitalTwin/Dokimasia definitions and Serving edges express per-applicable-JVM residency; diagram is conceptual, not prescribed site/owner counts or containers |
| §4.5 | Common Ergon load/unload/checkpoint/recovery and existing restart boundary retained; cache loss reconstructs needed information, no resume inside failed Ergon |
| §§4.6–4.6A | All five operations and FC decisions connected to reference/working-cache roles without cache ownership transfer |
| §4.7 | Themis, source-near evidence, external SEP, full and Summary mutation durability and reference flow retained; no generic audit/log/cache stream |
| §4.8 | Normal/failure rules retained in functions, executors and service definitions; no unsupported deployment guarantee |
| §5 | Explicit exclusions retained, particularly no new recovery/persistence/affinity mechanism |
| §§6.1–6.2 | Source provenance and qualified technology selection retained; cited product capability evidence is not project authority or verified deployment behaviour |
| §7 | Accepted IU/authoritative-persistence boundaries and historical Feature reconciliation preserved |
| §8 | Candidate/static checks performed; as-built topology, security/resource, failure and cache conformance remain future implementation/deployment evidence |
| §9 | MC-01–MC-07 retained in both functions and selected technology definitions; MC-06 deterministic reconciliation completed; MC-07 existing Feature mapping completed, physical detail deferred |
| §§10.1–10.9 | Native connected delta, existing Business/information/execution identities and additive contribution edges; no duplicate layers or reopening |
| §§11–12 | Subsequent complete approval is operative; reviewable actual candidate returned before closure/implementation |
| Appendices A–B | Attribution, accepted decision crosswalk and original MI identifier correspondence retained |

## Accepted decisions and qualifications

| Decision | Owning canonical expression / result |
| --- | --- |
| AV-01 | Both Readers and Mneme: exact requested version; `version=*` selects; consumer cache/wait families are orthogonal |
| AV-02 | Store/Façade: known configured identities/sites/capabilities, eligible selection and alarms; availability is not an operation Outcome |
| AV-03 | Writer/Readers/Outcome use: synchronous durable Success, timeout distinct from factual status; evidence-based read-back |
| AV-04 | Writer/Store/Mneme: return actual higher version when required, preserve immutable history, retain Mneme individual-IU concurrency |
| AV-05 | SummarySearch: continuation bound to originating instance; loss permits a fresh search, never invisible cursor migration/restart; practical search warning |
| FC-01 | Writer: actual Content, opaque reference and Summary form one coherent multi-site durable IU/version; partial physical effects are not Success |
| FC-02 | Full Reader attempts Content materialisation, preserving large-binary exception and factual external-read outcomes |
| FC-03 | SummarySearch: both search-input domain and returned items Summary-only in R1/R2; no Content-derived index expansion |
| FC-04 | Shared Summary definitions: IU projection excluding exactly Content; reference in ContentMetadata, opaque to Mneme; no second representation/version or mandatory physical dual-store |
| FC-05 | SummaryWriter CREATE: reference registration without external retrieval/verification; external custody/lifecycle/integrity/availability SEP; RETIRE does not touch referent |
| MI-05 | Store: complete coverage of all required IU types by union of eligible available supported criteria, concrete Topic matching and alarms; proof method deferred |
| MI-06 | Full Reader/Writer/Store: authoritative `version=*` read-back may reconcile uncertain higher-version write only with correspondence and durability evidence; newer version alone insufficient |
| MI-03 / session MI-07 | SummarySearch: `itemList : List<IU>` contains Summary projections, `size == itemList.size()`, paginationSize, extraction timestamp, origin-bound nextSetReference; Mneme's Search Result Set remains IU-version references |
| Original MI-07 | Writer/SummaryWriter: governed RETIRE versus provider DELETE/retention mapping remains its original subject; no duplicate or renumbered register identity |
| MC-01/02 | Topology, owners/site placement, concurrency/partition/failure behaviour unproved; no native linearizability or universal replication claim |
| MC-03 | Per-JVM memory/security/lifetime policy deferred within common context semantics |
| MC-04 | Durable propagation does not prove immediate read visibility; governing retrieval/wait/Indeterminate contracts control reacquisition |
| MC-05 | Two sites minus one cannot establish new multi-site durable write Success without another eligible site or explicit owning-contract disposition; N+1 target does not weaken Success |
| MC-06/07 | Five service identities and existing Features reconciled; exact physical implementation/observation/deployment allocation remains downstream |

Exact DTOs/signatures, Reason members, stages, reference/cursor encoding, proof
thresholds, provider RETIRE/DELETE mappings and topology/resource configurations
are accepted detail boundaries, not missing incorporation authority. No new
architectural design decision was needed for deterministic mapping. The candidate's
strict transport exception and required human/time/name treatment remain in HCRL
DR-4/DR-5; complete input approval does not self-accept those results.
