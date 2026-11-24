#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Run-specific, manually curated decomposition; generates On-Hold records only."""
import hashlib
import json
import os
from pathlib import Path

ROOT = Path.cwd()
HERE = Path(__file__).resolve().parent
RUN = HERE.parent
load = lambda f: json.loads((HERE / f).read_text())
prov = load('provenance.json')
es = load('elements.json')['added']
rs = load('relationships.json')['added']
vs = load('views.json')['relevant_views']
nav = load('navigation.json')
by_e = {e['exchange_id']: e for e in es}
by_r = {r['exchange_id']: r for r in rs}
by_v = {v['exchange_id']: v for v in vs}


def link(path, label=None, origin=RUN):
    return f'[{label or str(path)}]({os.path.relpath(ROOT / path, origin)})'


def raw(record):
    return f"[submission.xml:{record['physical_line']}](detection/submission.xml#L{record['physical_line']}) — `{record['exchange_id']}`; XPath `{record['xpath']}`"


def short_type(record):
    return record['native_type'].split('}')[-1]


def text_name(record):
    return record['name'][0]['text']


def clean(s):
    return s.split('] ', 1)[-1] if s.startswith('[') else s


def ep(e):
    identity = f"existing canonicalId `{e['canonicalId']}`" if e['canonicalId'] else 'governed identity unresolved; submitted record only'
    return f"`{e['native_name']}` ({e['native_type'].split('}')[-1]}, Exchange `{e['exchange_id']}`, {identity})"


EIDS = [
    'id-727a264026584f80bc3304ff8383dc92',
    'id-234b080d0ea54d57bb0001a2cb861080',
    'id-bfdd958364d1466a914d474dce8baab9',
    'id-3c5ae26723224eb0a6e6a70d70fe2c83',
    'id-d84d8e1f988c4bc998b18834aabfcd74',
    'id-fddda503b8f0438e995465cd93204326',
    'id-0088d61ccfcf4962b3aab8bc3b96021d',
    'id-c645a859ac2c4447ad5ae850cf384bf3',
    'id-9bfedbfd5b404694a2224c039fa5d4c4',
]
E = {f'A{n}': by_e[k] for n, k in enumerate(EIDS, 1)}
R = {f'R{n}': r for n, r in enumerate(rs, 1)}
V = {key: by_v[k] for key, k in [
    ('V1', 'id-e2a326c7d36751af9076a32dabe0e9cd'),
    ('V2', 'id-c8ec850977565a17869af357ee28d917'),
    ('V3', 'id-216c4ab0757b45b3894e7e0c2594b9f9'),
    ('V4', 'id-8fb2f8b1776f4ebf969e9b5b638e4ed3')]}
crs = []


def add(n, brief, proposal, evidence, questions, observations, deps=None, related=None, authority=None):
    assert n == len(crs) + 1 and len(brief) <= 80
    crs.append(dict(id=f'CR-{n:03}', brief=brief, status='On-Hold', proposal=proposal,
        evidence=evidence, questions=questions, observations=observations,
        dependencies=deps or [], related=related or [], authority=authority or
        'Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.',
        instruction='Not authorised', processing='Not yet processed'))


def dependency(n, state):
    return dict(cr=f'CR-{n:03}', required_governed_state=state)


element_cr = {k: n for n, k in enumerate(EIDS, 1)}
for n in range(1, 10):
    e = E[f'A{n}']
    kind = 'Feature' if n == 1 else short_type(e).replace('Business', 'Business ')
    local = clean(text_name(e))
    questions = [f'Accept `{text_name(e)}` as a new Harmonia {kind} with native type `{short_type(e)}`? '
                 'Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.',
                 'What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.']
    if n == 1:
        questions.append('The governed `[Feature]` + Capability rule recognises the proposed Feature classification deterministically. Does the Solution Architect accept this architectural instance? Its CT3 relationship/context is adjudicated separately; recognition supplies no identity or acceptance.')
    else:
        questions.append(f'`{text_name(e).split("]")[0] + "]"}` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.')
    if 2 <= n <= 5:
        questions.append('What Product meaning/scope is intended? The Name supplies no deployment, component allocation, service ownership or domain-decision authority; none is proposed by inference.')
    if n >= 6:
        questions.append('Confirm the responsibility boundary, including whether identity verification acts on source-attributed information without assuming master-person matching, golden-record or autonomous merge authority. Any different domain authority requires explicit architecture review under AX-18.')
    add(n, f'Add {local} {kind}',
        f'Add the single architectural instance `{text_name(e)}`, proposed native `{short_type(e)}`'
        + (' and recognised Harmonia classification `Feature`.' if n == 1 else '. No additional Harmonia classification is inferred.'),
        f"**Observed Evidence:** {raw(e)}. New semantic element, absent from the 1.0.8 baseline. "
        f"Native Name: `{text_name(e)}`. The record has only its Name: no Properties or Documentation. "
        f"Recognition: {e['recognition']}. See [element observations](detection/elements.json) (`A{n}` mapping in [inventory](detection/observations.md)). "
        'Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.',
        questions, [f'A{n}'])

relationship_cr = {}
for idx in range(1, 7):
    r = R[f'R{idx}']
    target = clean(r['target']['native_name'])
    n = 9 + idx
    relationship_cr[r['exchange_id']] = n
    deps = [dependency(1, 'Identifier Capture exists as the resolved governed architectural endpoint for submitted A1, with its accepted native type/classification and evidenced reconciliation; identity has not been allocated here.')] if idx == 6 else []
    add(n, f'Add Composition to {target}',
        f"Add only the native Composition from `{r['source']['native_name']}` to `{r['target']['native_name']}`, preserving whole/source → part/target direction. "
        'This CR reviews the explicit edge and its bounded whole/part meaning; the CT3-framing interpretation is separately adjudicable.',
        f"**Observed Evidence:** {raw(r)}. Source: {ep(r['source'])}. Target: {ep(r['target'])}. "
        f"No Name, Properties or Documentation. It has one `Relations` root reference and one V1 visual appearance. {r['duplicate_result']}. "
        'See [relationship observations](detection/relationships.json); endpoint resolution is independent of folder placement.',
        [f"Accept the submitted native Composition from `[CT3] Person Identity Administration` to `{r['target']['native_name']}` for this instance, with the stated endpoints and direction?",
         'Confirm native endpoint legality, intended whole/part meaning and relationship standing. The unresolved CT3/Feature profile is not resolved by native legality or deliberate authorship.',
         'The separate framing CR may receive a different disposition. No universal CT3 → Composition → Feature rule follows from this instance.'],
        [f'R{idx}', 'V1'], deps, [f'CR-{15+idx:03}', 'CR-033'],
        '2B semantic containment and unresolved CT3/Feature matter; mapping §5; 2A separate interpretations; AX-14/17.')

for idx in range(1, 7):
    r = R[f'R{idx}']
    target = clean(r['target']['native_name'])
    existing_context = ('The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. '
                        if idx <= 5 else 'There is no established source record for A1; its CT3 context is a candidate requiring explicit adjudication. ')
    add(15+idx, f'Interpret CT3 framing for {target}',
        f'For this instance only, determine whether the submitted Composition encodes CT3 framing/context of `{r["target"]["native_name"]}` by Person Identity Administration, and what bounded standing/context effect is authorised. '
        + existing_context + 'This is a candidate Harmonia interpretation, not another semantic-edge addition or a universal profile decision.',
        f"**Observed Evidence:** R{idx}, {raw(r)}. The XML contains a bare native Composition only; it contains no assertion that establishes a universal framing rule. "
        + existing_context + 'See [framing evidence and RADS locators](detection/observations.md#existing-feature-context), [relationships](detection/relationships.json).',
        [f'Does this Composition, if accepted and reconciled, encode the governed CT3 framing/context for `{r["target"]["native_name"]}` for this instance?',
         'State the exact semantic/naming-context effect and standing. Existing Proposed placement and, for Lineage, Proposed architectural standing remain unchanged unless explicitly adjudicated; acceptance of an edge alone does not approve those matters.',
         'If the native edge is accepted without this extra interpretation, identify any unresolved context that must remain blocked. No general metamodel rule is submitted or established here.'],
        [f'R{idx}', 'V1'], [dependency(9+idx, f'The exact native R{idx} Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient.')],
        ['CR-033'], '2B bounded unresolved CT3/Feature matter and existing parent_key/owner_key context; mapping §5; 2A; AX-17.')

realizations = [(7,22,23),(8,24,25),(9,26,27),(12,28,29)]
for idx, n, ctx in realizations:
    r = R[f'R{idx}']
    source, target = clean(r['source']['native_name']), clean(r['target']['native_name'])
    deps = [dependency(element_cr[e['exchange_id']], f"`{e['native_name']}` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing.") for e in [r['source'],r['target']] if e['exchange_id'] in element_cr]
    relationship_cr[r['exchange_id']] = n
    add(n, f'Add {source} Realization',
        f'Add the explicit native Realization from `{r["source"]["native_name"]}` to `{r["target"]["native_name"]}`, preserving direction and bounded realization meaning. This alone establishes no Harmonia logical Feature containment/context.',
        f"**Observed Evidence:** {raw(r)}. Source: {ep(r['source'])}. Target: {ep(r['target'])}. "
        f"No Name, Properties or Documentation. {r['duplicate_result']}. See [relationship evidence](detection/relationships.json). "
        + ('The target Lineage has existing Proposed architectural and placement standing, which this edge does not promote.' if idx==12 else ''),
        [f'Accept the submitted Realization from `{r["source"]["native_name"]}` to `{r["target"]["native_name"]}`?',
         'Confirm native legality, intended realization responsibility and standing. Does the stated scope respect the target Feature boundary? Additional logical context is a separate question.'],
        [f'R{idx}', 'V4' if idx==12 else 'V3'], deps, [f'CR-{ctx:03}'],
        'Mapping §5 behaviour → Feature Realization; 2B explicit relationships and unresolved logical context; 2A; AX-14/17/18.')
    add(ctx, f'Interpret Feature context for {source}',
        f'For this instance only, consider treating R{idx} Realization as establishing Harmonia logical Feature context of `{r["source"]["native_name"]}` in `{r["target"]["native_name"]}`. '
        'This additional interpretation is a question, not observed XML containment. Preserve the native Realization; do not substitute or add Composition.',
        f"**Observed Evidence:** R{idx}, {raw(r)}. Source: {ep(r['source'])}. Target: {ep(r['target'])}. "
        'The View presents these endpoints but contains no independent semantic containment assertion. The native Realization and View placement do not themselves answer the Harmonia context question.',
        [f'Does this Realization also establish Harmonia logical Feature context for `{r["source"]["native_name"]}` in `{r["target"]["native_name"]}`?',
         'If so, specify the bounded context, standing and any naming consequences for this instance. If not, retain the native Realization without invented context. No universal Realization inference or Composition is authorised.'],
        [f'R{idx}', 'V4' if idx==12 else 'V3'],
        [dependency(n, f'The native R{idx} Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state.')],
        ['CR-036' if idx==12 else 'CR-035'], '2B unresolved Realization → Feature logical-context matter; 2A separate interpretations; AX-14/17.')

for idx, n in [(10,30),(11,31),(13,32)]:
    r = R[f'R{idx}']
    typ = short_type(r)
    source, target = clean(r['source']['native_name']), clean(r['target']['native_name'])
    relationship_cr[r['exchange_id']] = n
    deps = [dependency(element_cr[e['exchange_id']], f"`{e['native_name']}` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing.") for e in [r['source'],r['target']]]
    question = ('What actual information/value transfer does this Flow assert, with what bounded direction and standing? A Flow is not automatically Triggering, execution order or orchestration.' if typ=='Flow' else
                'What service consumption does Serving assert? Confirm native legality, consuming behaviour and standing; this does not create ownership or domain authority.')
    add(n, f'Add {source} {typ} to Identifier Verification',
        f'Add only native `{typ}` from `{r["source"]["native_name"]}` to `{r["target"]["native_name"]}` with the submitted direction and explicitly adjudicated bounded meaning.',
        f"**Observed Evidence:** {raw(r)}. Source: {ep(r['source'])}. Target: {ep(r['target'])}. "
        f"No Name, Properties or Documentation. {r['duplicate_result']}. One visual connection in V3; see [relationships](detection/relationships.json) and [Views](detection/views.json).",
        [f'Accept this submitted {typ} from `{r["source"]["native_name"]}` to `{r["target"]["native_name"]}`?', question],
        [f'R{idx}', 'V3'], deps, ['CR-035'], 'Mapping §5 Flow/Serving direction and meaning; 2B; 2A; AX-14/17/18.')

for key, n, brief in [
    ('V1',33,'Revise Person Identity Administration Feature Landscape'),
    ('V2',34,'Expose existing Compositions in Entity Information Enablement View'),
    ('V3',35,'Add Identifier Capture Feature realisation View'),
    ('V4',36,'Add Identity Source and Correction Lineage realisation View')]:
    v = V[key]
    deps = []
    for e in sorted({node['attributes']['elementRef'] for node in v['nodes']}):
        if e in element_cr:
            deps.append(dependency(element_cr[e], f'The governed element corresponding to submitted `{e}` exists with evidenced reconciled identity, native type/classification and standing for this View appearance.'))
    for r in sorted({edge['attributes']['relationshipRef'] for edge in v['connections']}):
        if r in relationship_cr:
            deps.append(dependency(relationship_cr[r], f'The exact submitted relationship `{r}` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.'))
    purpose = {
        'V1':'Show Person Identity Administration and six Feature appearances with six explicit Composition connections, in the submitted left-source/right-Feature arrangement.',
        'V2':'Expose seven already-governed Composition relationships from Entity Information Enablement to its CT2 appearances; preserve the existing eight nodes and geometry.',
        'V3':'Present Identifier Capture, Identifier Create, Identifier Update, Identifier Verification and Verify Identity Source with the six submitted Realization/Flow/Serving appearances.',
        'V4':'Present Verify Identity Source realizing Identity Source & Correction Lineage, with two element appearances and one Realization connection.',
    }[key]
    questions = [f'Accept this {v["change"]} authored presentation of `{text_name(v)}` with the scope, membership and visual connections listed below and in the View evidence?',
                 'Confirm its bounded purpose, scope/standing filter, View home and submitted layout/readability for later authorised preservation. Geometry and folder placement establish no semantic containment.']
    if key=='V1':
        questions += ['The unchanged View Documentation says “Context and immediate governed children only. Standing remains in element Properties.” A1 has no standing Properties and Lineage remains Proposed. What precise scope/filter wording and standing presentation should the accepted View use?',
            'Should the View communicate native Composition alone, or the additional instance CT3-framing interpretation? If it claims the latter, the applicable CR-016 through CR-021 resulting context states are additional prerequisites; record that choice and exact dependency revision before any instruction. The base dependency list below supplies presentation of native edges only.']
    if key in ['V3','V4']:
        questions += ['There is no submitted Documentation or Properties for this new View. Confirm the proposed purpose above; its Feature-realisation Name is evidence of presentation intent, not proof of logical Feature containment.',
            'Does the accepted View require the separately reviewed logical Feature-context interpretations? Its explicit native-edge presentation can stand without them. Record any stronger context claim and corresponding resulting-state prerequisites before authorisation.']
    add(n, brief, f'{purpose} This is one independently reviewable authored View delta; it accepts no element, semantic edge or additional logical interpretation.',
        f"**Observed Evidence:** {key}, {raw(v)}. `{v['change']}` relative to the exact baseline; "
        f"{v['baseline_node_count']} → {len(v['nodes'])} nodes, {v['baseline_connection_count']} → {len(v['connections'])} connections. "
        f"See [complete per-View inventory](detection/observations.md#{key.lower()}) and [exact visual records, before/after geometry/styles and references](detection/views.json). "
        + ('Every added connection resolves to an existing baseline relationship with unchanged identity, endpoints and assertion Properties. There is no new semantic edge for this CR.' if key=='V2' else
           'Every displayed connection references a separately proposed semantic edge; presentation acceptance cannot create it.'),
        questions, [key], deps,
        ([f'CR-{x:03}' for x in range(16,22)] if key=='V1' else (['CR-023','CR-025','CR-027','CR-029'] if key=='V3' else ['CR-029'] if key=='V4' else [])),
        '2B legitimate authored Views / existing-edge reuse; navigation/View convention; 2A dependencies; AX-14/17.')

assert len(crs) == 36
(HERE/'change-requests.json').write_text(json.dumps(crs,ensure_ascii=False,indent=2)+'\n')

obs = ['# Fresh detection evidence — 1.0.8/run-001', '',
       'Observation only. A/R/V labels below are evidence locators, not architectural identities. All physical lines refer to the immutable snapshot [submission.xml](submission.xml).', '',
       '## Method and complete difference summary', '',
       'The unchanged Increment 1 comparator ran P → submission and transport-control C → submission; its identity/source-context audit also ran. [Raw diagnostic commands](diagnostic-commands.json) and compressed JSON preserve all events. [detect.py](detect.py) independently indexes the final submission against P and compares retained records against the justified no-edit control. It resolves Property references by exact keys, retains values/languages/multiplicity and compares native records, visual nodes/connections and path/reference multisets separately. Indentation is ignored only as XML syntax; evidence-text changes remain visible. No label-based identity matching or new comparator normalisation is used.', '',
       '| Collection | Baseline | Submission | Fresh finding |', '| --- | ---: | ---: | --- |',
       '| Elements | 1,299 | 1,308 | 9 additions; no removals or retained semantic edits beyond control |',
       '| Relationships | 852 | 865 | 6 Composition, 4 Realization, 2 Flow, 1 Serving added |',
       '| Views | 1,196 | 1,198 | 2 added, 2 existing modified; 1,194 unchanged beyond transport |',
       '| Visual nodes | 3,915 | 3,923 | 8 added; 6 retained nodes edited beyond transport |',
       '| Visual connections | 1,449 | 1,469 | 20 added: 13 new-edge appearances and 7 existing-edge appearances |',
       '| Explicit bendpoints | 2,922 | 2,928 | 6 added in V3 |',
       '| Folder paths / references | 3,749 / 3,347 | 3,752 / 3,371 | +3 folders, +24 references; no lost/moved existing membership |',
       '| Governed canonical owners | 1,299 | 1,299 | Exact continuity; no mutated, malformed or duplicate claims |', '',
       'No semantic element/relationship/View removals, retained retypes, native Name mutations, semantic Documentation edits, new Property keys/datatypes, or unresolved XML/visual endpoint references were detected. Model identity/Name/Documentation/resolved model Properties retain the 1.0.8 baseline. All 1,299 retained element records and all 852 retained relationship records match C exactly. Raw P → submission changed-record counts include transport, not additional architectural changes.', '',
       '## Elements', '', '| Evidence | Native type | Exact submitted Name | Exchange identity / physical line | Recognition |', '| --- | --- | --- | --- | --- |']
for key,e in E.items():
    obs.append(f"| {key} | {short_type(e)} | `{text_name(e)}` | `{e['exchange_id']}` / {e['physical_line']} | {e['recognition']} |")
obs += ['', 'All nine records have no Properties or Documentation. Their missing machine metadata is expected authoring evidence under 2B, not an architectural defect. Final identity/new-instance binding and responsibility remain adjudication questions; no IDs or final names have been supplied. A2–A5 have no semantic relationships or visual appearances. A1 has R6–R9 and appearances in V1/V3. A6–A9 participation is explicit in [elements.json](elements.json).', '',
        '## Semantic relationships', '', '| Evidence | Native type | Source → target (exact native Names) | Exchange identity / physical line | Duplicate finding |', '| --- | --- | --- | --- | --- |']
for key,r in R.items():
    obs.append(f"| {key} | {short_type(r)} | `{r['source']['native_name']}` → `{r['target']['native_name']}` | `{r['exchange_id']}` / {r['physical_line']} | {r['duplicate_result']} |")
obs += ['', 'Each is an explicit semantic record, with no Name, Properties or Documentation. Every one is referenced once directly under `Relations`. [relationships.json](relationships.json) retains exact semantic endpoint IDs, existing Canonical IDs, target standing, comparison keys and discriminator evidence. XSD/reference success does not adjudicate native legality or Harmonia meaning.', '',
        '### Relationship reuse and duplicate analysis', '',
        'R1–R5 have both endpoint Canonical IDs resolved and have no baseline native-type/source/target match. No resolved-canonical-key duplicates or exact Exchange-triple duplicates occur anywhere in the final model. R6–R13 contain at least one new endpoint without governed identity: their final semantic duplicate status remains **UNRESOLVED**, despite unique observed transport triples. No attempted duplicate against fully resolved governed endpoints is positively identified. This is more precise than the historical exact-Exchange-triple “none” result. Do not allocate, merge or collapse an edge to resolve that uncertainty.', '',
        'The seven V2 connections reuse established governed relationship bindings. Their exact baseline Properties/assertion discriminators are retained in [views.json](views.json); none is replaced by tuple matching or a new semantic relationship. Multiple appearances do not create multiple edges.', '',
        '<a id="existing-feature-context"></a>', '', '### Existing Feature context', '',
        'The current RADS [Feature records]('+os.path.relpath(ROOT/'docs/markdown/governance/baseline/features.md',HERE)+') already retain source-qualified Person Identity Administration responsibility/framing context for the five existing targets, each with **Proposed placement**. This existing contextual evidence is not a baseline native Composition. The proposed native encoding and any change in interpretation/standing require separate adjudication. Existing names/contexts are not regenerated here.', '',
        '| R | RADS Feature source anchor | Existing canonicalId | Architectural standing / placement |', '| --- | --- | --- | --- |']
anchors = ['feat-em-02','feat-em-04','hcm-ft-identity-source-correction-lineage','feat-em-01','feat-em-03']
for idx,anchor in enumerate(anchors,1):
    r = R[f'R{idx}']; target=r['target']
    obs.append(f"| R{idx} | [features.md#{anchor}]({os.path.relpath(ROOT/'docs/markdown/governance/baseline/features.md',HERE)}#{anchor}) | `{target['canonicalId']}` | {'Proposed' if idx==3 else 'Canonical'} / Proposed |")
obs += ['', 'The baseline source record `proposal:feature-identity-source-correction-lineage` retains its boundary: no probabilistic matching, golden-record selection or autonomous merge authority. R3/R12 and V1/V4 do not promote its standing or alter this boundary. Context-interpretation CRs ask about the native encoding and bounded effects, not silent promotion of existing placement.', '', '## Views', '']
for key,v in V.items():
    obs += [f'<a id="{key.lower()}"></a>', '', f'### {key} — {text_name(v)}', '',
            f"`{v['change']}`; Exchange `{v['exchange_id']}`, physical line {v['physical_line']}. Baseline {v['baseline_node_count']} nodes / {v['baseline_connection_count']} connections; submitted {len(v['nodes'])} nodes / {len(v['connections'])} connections. No removed nodes/connections. All nodes are direct children of the View: no XML semantic containment is asserted.", '',
            'Submitted Documentation: '+('`'+v['documentation'][0]['text']+'`' if v['documentation'] else 'absent; purpose inferred only as a candidate from membership and Name.'), '',
            '| Node ID | Exact represented Name | Appearance | Submitted (x, y, w, h) |', '| --- | --- | --- | --- |']
    for node in v['nodes']:
        obs.append(f"| `{node['exchange_id']}` | `{node['endpoint']['native_name']}` | {node['appearance_change']} | {tuple(node['attributes'][k] for k in ['x','y','w','h'])} |")
    obs += ['', '| Connection ID | relationshipRef | Semantic source → target | Relationship origin |', '| --- | --- | --- | --- |']
    for conn in v['connections']:
        obs.append(f"| `{conn['exchange_id']}` | `{conn['attributes']['relationshipRef']}` | `{conn['semantic_source']['native_name']}` → `{conn['semantic_target']['native_name']}` | {conn['relationship_origin']} |")
    obs += ['', 'Exact View-local source/target IDs, native semantic endpoints, styles, ordered bendpoints, baseline node XML and submitted XML are retained in [views.json](views.json). Every visual endpoint agrees with its referenced semantic edge; reference resolution does not approve architecture or visual readability.', '']
obs += ['V1 changes six retained node geometries/styles and adds A1 plus six new Composition appearances. Five retained child fonts change 12 → 9; all six existing nodes end with fill (245,222,170); the source retains Sans 12. V2 retains every node/geometry/style beyond transport and adds seven existing-edge appearances. V3 has five new nodes, six new connections, six explicit bendpoints. V4 has two new nodes and one new connection. No View was regenerated or laid out by this run.', '',
        '## Navigation', '',
        'New element folders: `Business / Products` and the `[Feature] Identifier Capture` folder under Strategy / Business Layer / Business Enabling Capabilities / [CT1] Entity Information Enablement / [CT2] Client Administration / [CT3] Person Identity Administration. A6–A9 references are directly under `Business`. New View folder: the corresponding Identifier Capture context beneath Views / 2-BusinessLayer / 2-Business Enabling Capabilities / the same CT1–CT3 path. V3 is referenced there; V4 is added to the existing Lineage Feature folder. Existing View homes do not move.', '',
        'All thirteen relationship references are directly under `Relations`; this does not establish relationship meaning or an authoritative home. 2B governs later source-home reprojection from resolved governed source context. No CR exists merely to replay arbitrary incoming relationship folders. [navigation.json](navigation.json) preserves every added path/reference and confirms no existing membership loss/move. The authored new View homes remain explicit presentation questions in CR-035/036; their nesting supplies no semantic framing.', '',
        '## Transport and evidence-only findings', '',
        'The preserved real no-edit 1.0.7 Archi export is a transport control, justified by exact in-memory restoration of 1.0.8’s single model release value to the 1.0.7 published byte stream. It is not the semantic baseline. [transport.json](transport.json) and the raw diagnostics preserve the following:', '',
        '- All 61 Property-definition IDs are rekeyed with references redirected; unique keys/string datatypes remain and all 61 definition-name language attributes are lost.',
        '- 1,669 evidence-Property whitespace changes match C exactly: 615 Snapshot Record JSON, 409 Source Locators, 291 Deferred Relationship Assertions, 269 Source Assertion JSON, 85 Derivation Provenance.',
        '- All 7,096 original organisation item identities are absent after transport; path/reference membership is checked independently. Original folder identity/edit sequence remains unknown.',
        '- 3,910 original node alpha defaults materialise as 100; 34 View Documentation strings lose a final LF; 15 Views have known connection-order transformations. No whole-model fidelity or paint-order equivalence is claimed.',
        '- Raw P → submission: 10,806 events (1,669 semantic, 4,270 loss, 4,805 unexplained, 62 equivalent); C → submission: 125 events (123 unexplained, 2 positional loss, including the governed release transition). The two positional losses do not establish semantic/reference deletion; path/reference multisets demonstrate retention.',
        '- The identity diagnostic executes successfully but reports NONCONFORMANT readiness for nine missing metadata sets. Current 2B treats absence on new authoring as evidence, not nine metadata-defect CRs.',
        '- The baseline has 841 existing native Name/displayName discrepancies, unchanged in the submission. The pre-existing migration population remains external/deferred; there are no submitted Name/displayName mutation CRs.', '',
        'Known transport differences, missing generated metadata and existing migration debt receive no architectural acceptance or reconciliation through this record. No new publication, universal profile, ID allocation, relationship home or generated naming is established.', '']
(HERE/'observations.md').write_text('\n'.join(line.rstrip() for line in obs))

decomp = ['# Change Request decomposition — 1.0.8/run-001', '',
          'Revision 1. Stable review-local IDs allocated once, CR-001 through CR-036. These process IDs are not Canonical IDs. Evidence labels identify records/appearances only.', '',
          'The 36 CRs comprise 9 element-instance additions, 13 native relationship additions, 6 instance-specific CT3-framing interpretation questions, 4 logical Feature-context interpretation questions and 4 authored View changes. Deterministic proposed Feature recognition belongs with its element proposal; a separate classification CR would add no unresolved independent decision. Repeated native-type qualifiers establish no extra subtype. No missing-metadata, transport, folder-replay or pre-existing migration CRs are created.', '',
          'Each relationship can be decided independently of its endpoint addition and its optional additional Harmonia context. The six CT3-framing questions do not recreate existing source-qualified context: five targets already have Proposed placement, and the question concerns whether/how this native encoding carries it. The four Realization context questions remain separate from the native edges. Neither family proposes a universal rule.', '',
          'Each View CR covers one coherent authored presentation delta—membership, connections, layout and declared scope—because those observations together express its review question. Native element/edge decisions remain separate. A Solution Architect may request a bounded amendment/split without losing these original IDs/evidence. V2 is independently reviewable against existing architecture; the others depend on the exact reconciled displayed elements/edges. Logical-context CRs are not silently prerequisites of a native-edge-only presentation. A stronger scope claim needs an explicit dependency revision before authorisation.', '',
          '| Evidence | CR | Independently adjudicable architectural matter |', '| --- | --- | --- |']
for cr in crs:
    decomp.append(f"| {', '.join(cr['observations'])} | [{cr['id']}](../change-review.md#{cr['id'].lower()}) | {cr['brief']} |")
decomp += ['', '## Evidence-only observations', '',
           '| Evidence | Treatment |', '| --- | --- |',
           '| 61 definition rekeys/language losses; 1,669 evidence-Property changes; organisation-ID/style/newline/order transport | Retained transport evidence, no architectural CR |',
           '| A1–A9 absent canonical identity/derived metadata | Expected authoring boundary; questions about new-instance binding/meaning stay with element CRs; no metadata-population CR |',
           '| 13 root Relations references | Preserve incoming navigation evidence; governed publication homes follow resolved source, no semantic CR for replay |',
           '| 841 existing Name/displayName discrepancies | Existing 2B migration debt, unchanged; no submission CR |',
           '| Seven V2 existing-edge appearances | Shared presentation evidence for CR-034; zero new semantic-edge CRs |',
           '| Incomplete governed endpoint identity for R6–R13 | Duplicate resolution remains unresolved pending endpoint resulting states; no guessed merge/new allocation |', '',
           '## Dependencies', '', 'The following are required resulting states for later processing, not execution scheduling or an instruction to reconcile. Dispositions alone establish no endpoint/relationship. No dependency logic was executed.', '',
           '| Dependent CR | Prerequisite CR | Required governed resulting state |', '| --- | --- | --- |']
for cr in crs:
    for dep in cr['dependencies']:
        decomp.append(f"| {cr['id']} | {dep['cr']} | {dep['required_governed_state']} |")
decomp += ['', 'Existing-endpoint prerequisites additionally require the exact canonical bindings and source standing recorded in relationships.json, with current-target applicability checked before any later instruction. Target Lineage retains Proposed standing. Optional stronger View scope prerequisites are explicit questions in the relevant View CR; they are not assumed executed or silently added here.', '']
(HERE/'decomposition.md').write_text('\n'.join(line.rstrip() for line in decomp))

review = ['| ID | Brief Description | Status |', '| --- | --- | --- |']
for cr in crs:
    review.append(f"| [{cr['id']}](#{cr['id'].lower()}) | {cr['brief']} | On-Hold |")
review += ['', '<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->', '',
           '# ArchiMate Ingestion Change Review — 1.0.8/run-001', '',
           '## Review basis', '',
           '- Review/run: **1.0.8/run-001**; revision **1**; prepared by **Codex / 2026-10-18**.',
           f"- Submitted incoming evidence: {link(prov['submission_path'])}; SHA-256 **`{prov['submission_sha256']}`**; 21,854,756 bytes.",
           '- Byte-identical retained input: [detection/submission.xml](detection/submission.xml), same SHA-256. The Authoring Iteration is **closed** by this deliberate Submission. Earlier evolving exports are historical working-state evidence, not separate Submissions.',
           f"- Exact originating Published Baseline: **Release/Publication 1.0.8**, {link(prov['baseline_path'])}; SHA-256 **`{prov['baseline_sha256']}`**; 20,800,334 bytes.",
           '- Publication provenance: '+', '.join(link(p) for p in prov['publication_evidence'])+'. The index identifies this published model; the publication/inspection/comparison pin its hash. Exact one-value restoration reproduces the published 1.0.7 byte stream. Submission retains the 1.0.8 model ID/release Property and all established bindings. Native edit/operator history beyond that evidence is not invented.',
           f"- Current repository governance release: **1.0.11**, previous 1.0.10; target source HEAD **`{prov['head']}`**, with pre-existing working-tree changes captured in [start-preservation](detection/start-preservation.json). Working-tree authority hashes, provenance and tool versions: [provenance.json](detection/provenance.json). **1.0.11 is not this semantic comparison baseline.**",
           '- Transport control: '+link(prov['control_path'])+f"; SHA-256 `{prov['control_sha256']}`. Used solely to recognise characterised Archi transformations, not to select architecture.",
           '- Detection: unchanged Increment 1 P→submission and C→submission comparisons, identity/source audit, plus independent run-specific [query](detection/detect.py). [Observations](detection/observations.md), [raw diagnostics](detection/diagnostic-commands.json), [summary](detection/summary.json), [exact visual records](detection/views.json). All model inputs were read only.',
           '- Governed template: '+link('docs/markdown/governance/templates/archimate-ingestion-change-review-template.md')+'. Authority: '+', '.join(link('docs/markdown/governance/'+p) for p in ['archimate-ingestion-change-review.md','archimate-ingestion-publication-semantics.md','architecture-authoring-praxis.md'])+'. AX-04 preserves meaning independent of tools; AX-14 separates evidence/interpretation/decision and semantics/navigation/presentation/identity; AX-15 preserves uncertainty; AX-17 leaves missing meaning for adjudication; AX-18 forbids inferred domain authority.',
           '- Historical supporting evidence: [prior-analysis comparison](detection/historical-evidence.md). The exact second intermediate XML remains unavailable; no reconstruction or new Submission was inferred. Current P→final detection needs neither that snapshot nor historical conclusions.',
           '- Run evidence: `detection/` contains input, baseline reference, observations and decomposition; `validation/` contains review/preservation/test evidence. `reconciliation/` is reserved and not materialised because it has no evidence, as required by the run convention. No reconciliation/validation-of-reconciled-state/publication is claimed.',
           '- Release handling: this is preparation of an unadjudicated review, not acceptance of an architectural repository change set. Under release policy, preparation/generation does not increment; **architecture-release.json is untouched**. Any later accepted change set remains subject to the patch rule.', '',
           '### Decomposition record', '',
           'Fresh observations yield **36 independently adjudicable CRs**, all On-Hold: 9 element additions, 13 explicit semantic edges, 6 CT3-framing interpretation questions, 4 Realization/logical-context questions and 4 View changes. The full evidence-to-CR rationale and resulting-state dependency matrix are in [decomposition.md](detection/decomposition.md); the exact structured entries are [change-requests.json](detection/change-requests.json).', '',
           'Known transport transformations, expected absent machine metadata, arbitrary relationship folders and unchanged migration debt generate no architectural CRs. Seven V2 connections expose governed edges and support CR-034 only. Five new Compositions have fully resolved endpoints and no baseline duplicate; the remaining eight added edges retain unresolved final semantic duplicate status because new endpoint identities are unresolved. No positive attempted duplicate is established.', '',
           'The existing five Feature targets already retain source-qualified CT3 context at Proposed placement standing. The interpretation CRs review the additional native encoding/effect without silently promoting that context. Element/edge/context/View decisions remain independent. All dependencies state the governed result required; none has been evaluated or executed.', '',
           '| Observation/evidence locator | CR(s) supported | Decomposition reason / evidence-only treatment |', '| --- | --- | --- |']
review += ['| A1–A9; elements.json | CR-001–009 | One proposed architectural instance each; recognised classification stays with its instance |',
           '| R1–R6; relationships.json | CR-010–015 | Six independently reviewable native Composition edges |',
           '| R1–R6 plus current RADS Feature context | CR-016–021 | Six instance framing/native-encoding questions; no universal rule |',
           '| R7–R9/R12 | CR-022/024/026/028 and CR-023/025/027/029 | Native Realizations and optional logical contexts can receive different decisions |',
           '| R10/R11/R13 | CR-030–032 | Two transfer proposals and one service-use proposal |',
           '| V1/V2/V3/V4; views.json | CR-033–036 | One coherent authored View proposal each, independent of semantic approvals |',
           '| Transport/navigation/absent metadata/pre-existing migration | None except View-home questions | Evidence retained; no inferred semantic architecture or machinery-only CR |', '']
for cr in crs:
    review += [f'<a id="{cr["id"].lower()}"></a>', '', f'## {cr["id"]} — {cr["brief"]}', '',
               '**Status:** On-Hold', '', '### Proposed Architectural Change', '',
               '**Candidate Architectural Interpretation:** '+cr['proposal'], '',
               '### Detected Evidence', '', cr['evidence'], '',
               '### Architectural Questions / Adjudication', '']
    review += [f'{n}. {q}' for n,q in enumerate(cr['questions'],1)]
    review += ['', '**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.', '',
               '**Applicable authority / identified gap:** '+cr['authority'], '', '### Related Change Requests', '']
    if cr['dependencies']:
        review += [f"- Required resulting state from [{dep['cr']}](#{dep['cr'].lower()}): {dep['required_governed_state']}" for dep in cr['dependencies']]
    else:
        review += ['No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.']
    if cr['related']:
        review += ['', 'Informationally related (not automatic prerequisites): '+', '.join(f'[{r}](#{r.lower()})' for r in cr['related'])+'.']
    if cr['id']=='CR-034':
        review += ['', 'Required existing governed state: the seven exact Composition bindings, endpoint Canonical IDs and assertion Properties listed under V2 remain intact; no relationship-addition CR is required.']
    review += ['', '### Authorised Reconciliation Instruction', '', 'Not authorised', '',
               '### Reconciliation / Processing Record', '', 'Not yet processed', '']
(RUN/'change-review.md').write_text('\n'.join(line.rstrip() for line in review))
print(json.dumps({'review':'1.0.8/run-001/change-review.md','cr_count':len(crs),'all_on_hold':all(c['status']=='On-Hold' for c in crs)}))
