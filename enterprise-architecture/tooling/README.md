<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Current Enterprise Architecture operations

Run current EA operations here. Maintained inputs are the canonical
`enterprise-architecture/continuum/`, current `governance/`, `design/` and
`tooling/`, the narrative publication's presentation inputs under
`documentation/continuum-rad/`, plus the repository-root
`architecture-release.json` release authority.
Python 3.10 or later, GNU Make/timeout and the packages in
[requirements.txt](requirements.txt) are required. The browser smoke check uses Node.

From the repository root:

```bash
python3 -m pip install -r enterprise-architecture/tooling/requirements.txt
make -C enterprise-architecture/tooling all
make -C enterprise-architecture/tooling verify-independent
make -C enterprise-architecture/tooling browser-test
```

`continuum` validates and generates the root navigation manifest, Open Exchange, HTML browser,
Markdown index, LaTeX catalogue source and publication manifest for the root
release under `documentation/continuum-manifest/published/<version>/`.
The same validated Exchange bytes are also published at
`design/archimate/published/<version>/Harmonia-Architecture.xml`, the durable
starting file for importing that release into Archi. `project --publish` generates
only that release destination; combining `--publish` with `--output` writes both
destinations from one projection. `--check` verifies both without writing.
The source tree's root release authority selects the version; generation does
not allocate releases or supply architectural acceptance. Differing existing
published XML is rejected rather than overwriting release history.
The initial backfill is accepted 1.1.11; historical Manifest publications are
preserved. See the [workbench procedure](../design/archimate/README.md) for canonical
authority, architect-modified submissions and evidence distinctions.
The canonical `continuum/navigation.json` supplies architectural organisation,
while native-type Markdown paths remain storage locators. The shared
`continuum_navigation.py` validates complete unique coverage and derives every
hierarchical coordinate. All projections consume that same traversal. The
1.1.11 reference area R holds the non-decompositional Stakeholder Index and
historical Capability identities. Business Behaviour repeats only the complete
recorded ancestry of populated contexts, with typed canonical Strategy links.
Stakeholder contexts cite existing Association IDs. Exchange item documentation
and derived `Continuum / Navigation References` properties expose the same
references and context paths; those properties preserve orientation when Archi
cannot preserve navigation-only folders. None of these links asserts an edge.
The 1.1.13 Exchange adapter uses native-layer markers to preserve those folders,
gives every member of the established Capability/Feature and logical Topic
hierarchies and explicit governed model containment a stable directory containing
its canonical element, including leaves (212 Business Behaviour Home subjects
retain their accepted Business contexts),
retains other navigation-parent folders without inferring containment, and
qualifies cross-layer and reference indexes explicitly. RAD/Manifest keep the
same canonical hierarchy. See the [physical workbench policy](../design/archimate/README.md#physical-workbench-organisation--1113).
Material navigation/publication changes require Solution Architect structural
review under the existing Review Praxis before HCRL acceptance.
The
generated Manifest `navigation.json` exposes the fully numbered tree for inspection;
it is not an editable architecture source. Creation specifications require an
explicit `navigationParent` context; publication performs no classification.
`continuum-rad` generates the narrative LaTeX publication and complete model/source
correspondence under `documentation/continuum-rad/published/<version>/`.
`all` includes both products. `rad-pdf` compiles the narrative PDF with XeLaTeX,
latexmk, makeindex, DejaVu fonts and TikZ using a fixed `SOURCE_DATE_EPOCH`;
the release-local `build/` intermediates are ignored. Its PDF remains a derived
publication, separate from source-byte checks and human acceptance.
`check` performs read-only staleness and link checks. `test` runs current
creation, mutation, identity, naming, containment, round-trip, publication, release
and evidence-layout tests. `verify-independent` repeats generation, checks and
the current tests in an isolated tree with no `docs/`, `scripts/`, Git metadata,
historical framework, historical location mapping or historical processors.
It compares derived bytes for both products, including the durable ArchiMate XML, and confirms canonical source bytes
are unchanged. The isolated run retains only the new RAD's template and cited
reader notes as presentation inputs; it reads no superseded LaTeX RAD workspace.
Override `PYTHON` for a Python environment containing the declared dependencies.

```bash
python3 enterprise-architecture/tooling/architecture_continuum.py template --type Principle
python3 enterprise-architecture/tooling/architecture_continuum.py create --spec /tmp/authorised-element.json
python3 enterprise-architecture/tooling/continuum_workbench.py review --baseline /path/to/exact.xml --submitted /path/to/submission.xml --output /path/to/review.json
python3 enterprise-architecture/tooling/continuum_workbench.py apply --decision /path/to/reviewed-decision.json
python3 enterprise-architecture/tooling/exchange_compare.py compare --baseline /path/to/exact.xml --submitted /path/to/returned.xml.gz --report /path/to/raw-comparison.json
python3 enterprise-architecture/tooling/verify_continuum_transport.py --baseline /path/to/exact.xml --returned /path/to/returned.xml --execution-log /path/to/archi.txt --operator 'attributed operator' --report /path/to/transport.json
python3 enterprise-architecture/tooling/architecture_review_evidence.py --version VERSION --iteration ITERATION
```

The [workbench procedure](../design/archimate/README.md) and
[Round-Trip Praxis](../governance/archimate-round-trip-engineering-praxis.md)
govern exact inputs, human review and application. The raw comparator observes
transport fidelity; the workbench observes canonical amendments. A processing
agent prepares `change-review.md` from that evidence and the
[current template](../governance/templates/archimate-ingestion-change-review-template.md).
The tools supply no Solution Architect decision or final acceptance.
Review and apply use explicit locators; there is no newest-export discovery rule.

For an optional catalogue PDF, compile the generated `continuum-catalogue.tex` twice with
XeLaTeX, a fixed `SOURCE_DATE_EPOCH` and temporary output directory. Inspect it
before retaining it as a derived publication. Existing historical PDFs are unchanged.

The offline schemas are byte-preserving promotions of Archi's Open Exchange
schemas. `exchange_xml.py` supplies the secure parser/offline resolver;
`governed_naming.py`, `relationship_state.py`, `home_relationships.py`,
`containment.py`, `information_naming.py`, `element_correlation.py` and
`exchange_signatures.py` retain the required current rules. No adoption,
RAD capture replay, identity registry, old publication subsystem or historical
location resolver is imported. `architecture_identity.py` remains historical.

Canonical source provenance and historical evidence keep their original locators.
[Authority succession](../governance/authority-successors.json) maps the bounded
promoted governance references to maintained successors when rendering/checking
current links, without editing canonical source bytes. Other Legacy-RAD links are
historical source/evidence locators; standing checks count them without requiring
the historical files. Historical preservation/replay checks remain historical
procedures in their originating checkout. Current operations do not run them.
