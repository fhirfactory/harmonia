<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Current Enterprise Architecture operations

Run current EA operations here. Maintained inputs are the canonical
`enterprise-architecture/continuum/`, current `governance/`, `design/` and
`tooling/`, plus the repository-root `architecture-release.json` release authority.
Python 3.10 or later, GNU Make/timeout and the packages in
[requirements.txt](requirements.txt) are required. The browser smoke check uses Node.

From the repository root:

```bash
python3 -m pip install -r enterprise-architecture/tooling/requirements.txt
make -C enterprise-architecture/tooling all
make -C enterprise-architecture/tooling verify-independent
make -C enterprise-architecture/tooling browser-test
```

`continuum` validates and generates the manifest, Open Exchange, HTML browser,
Markdown index, LaTeX catalogue source and publication manifest for the root
release. `check` performs read-only staleness and link checks. `test` runs current
creation, mutation, identity, naming, containment, round-trip, publication, release
and evidence-layout tests. `verify-independent` repeats generation, checks and
the current tests in an isolated tree with no `docs/`, `scripts/`, Git metadata,
historical framework, historical location mapping or historical processors.
It compares derived bytes and confirms canonical source bytes are unchanged.
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

For an optional PDF, compile the generated `continuum-catalogue.tex` twice with
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
