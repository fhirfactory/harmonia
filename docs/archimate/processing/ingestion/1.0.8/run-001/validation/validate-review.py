#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Independent checks of the prepared review, pinned evidence and write boundary."""
import gzip
import hashlib
import json
import re
from pathlib import Path
import subprocess
import sys

ROOT = Path.cwd()
RUN = Path(__file__).resolve().parent.parent
D = RUN / 'detection'
V = RUN / 'validation'
REPORT = ROOT / '.junie/reports/2026-10-18-archimate-first-governed-ingestion-review.md'
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import diagnose_roundtrip as reader


def sha(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()


def read(name):
    return json.loads((D / name).read_text())


def main():
    checks = []
    def check(label, condition):
        checks.append(dict(check=label, outcome='PASS' if condition else 'FAIL'))
        if not condition:
            raise AssertionError(label)

    review = (RUN/'change-review.md').read_text()
    rows = read('change-requests.json')
    expected = [f'CR-{n:03}' for n in range(1, 37)]
    check('First visible review content is the compact summary table', review.startswith('| ID | Brief Description | Status |\n| --- | --- | --- |\n'))
    summary = re.findall(r'^\| \[(CR-\d+)\]\(#(cr-\d+)\) \| (.*?) \| (.*?) \|$', review, re.M)
    details = re.split(r'^## (CR-\d+) — (.*?)\n', review, flags=re.M)
    detail_ids = details[1::3]
    check('Stable unique sequential CR IDs in summary, details and evidence',
          [r['id'] for r in rows] == expected == [r[0] for r in summary] == detail_ids and len(set(detail_ids)) == 36)
    for index, cr in enumerate(rows):
        sid, anchor, brief, status = summary[index]
        body = details[index*3+3]
        check(cr['id']+' summary/detail status and description agree, <=80 characters',
              cr['brief'] == brief == details[index*3+2] and len(brief) <= 80 and
              cr['status'] == status == 'On-Hold' and body.count('**Status:** On-Hold') == 1 and
              f'<a id="{anchor}"></a>' in review)
        headings = ['Proposed Architectural Change', 'Detected Evidence', 'Architectural Questions / Adjudication',
                    'Related Change Requests', 'Authorised Reconciliation Instruction', 'Reconciliation / Processing Record']
        check(cr['id']+' follows all template distinctions', all('### '+h+'\n' in body for h in headings) and
              '**Candidate Architectural Interpretation:**' in body and '**Observed Evidence:**' in body)
        instruction = body.split('### Authorised Reconciliation Instruction\n',1)[1].split('###',1)[0].strip()
        processing = body.split('### Reconciliation / Processing Record\n',1)[1].split('<a ',1)[0].strip()
        decision = re.search(r'^\*\*Solution Architect Decision:\*\* (.*)$',body,re.M)
        check(cr['id']+' has no adjudication, authority or processing claim',
              instruction == cr['instruction'] == 'Not authorised' and processing == cr['processing'] == 'Not yet processed' and
              decision.group(1) == 'No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.')
        for dep in cr['dependencies']:
            state = dep['required_governed_state']
            check(cr['id']+' prerequisite '+dep['cr']+' names a required resulting state',
                  dep['cr'] in expected and dep['cr'] != cr['id'] and len(state) > 60 and
                  any(word in state for word in ['reconciled', 'reconciliation']) and state in body)
        check(cr['id']+' related references are valid', all(r in expected for r in cr['related']))
    graph = {r['id']: [d['cr'] for d in r['dependencies']] for r in rows}
    def walk(key, path):
        assert key not in path, ('dependency cycle', key, path)
        for parent in graph[key]:
            walk(parent,path+[key])
    for key in graph:
        walk(key,[])
    check('Recorded required-state dependency graph is acyclic', True)

    # Independent input and semantic-preservation checks, not a new allocation.
    prov = read('provenance.json')
    check('Submission and exact Published Baseline hashes are pinned and visible',
          sha(prov['submission_path']) == sha(D/'submission.xml') == prov['submission_sha256'] and
          sha(prov['baseline_path']) == prov['baseline_sha256'] and
          all(prov[key] in review for key in ['submission_sha256','baseline_sha256']) and
          prov['baseline_release_publication'] == '1.0.8')
    p, i = reader.Exchange(prov['baseline_path']), reader.Exchange(D/'submission.xml')
    check('Offline schema, XML identity, Property/reference and visual endpoint gates',not p.issues and not i.issues)
    added = i.elements.keys()-p.elements.keys()
    check('Nine proposed additions have no allocated canonical IDs or generated metadata',
          len(added) == 9 and all(not i.elements[k].findall('a:properties/a:property',reader.g.N) for k in added))
    check('Retained identity/naming/type claims have exact continuity',
          all(p.texts(p.elements[k],role) == i.texts(i.elements[k],role)
              for k in p.elements for role in ['canonicalId','displayName','friendlyName','canonicalName','archimateType','harmoniaType']))
    check('No retained Name/native type mutation or model header mutation',
          all(reader.values(p.elements[k],'name') == reader.values(i.elements[k],'name') and
              reader.expanded_type(p.elements[k]) == reader.expanded_type(i.elements[k]) for k in p.elements) and
          all(reader.values(p.root,key) == reader.values(i.root,key) for key in ['name','documentation']) and
          p.root.get('identifier') == i.root.get('identifier'))
    check('All 852 established semantic relationship identities/types/endpoints retained',
          p.relationships.keys() <= i.relationships.keys() and
          all(dict(edge.attrib) == dict(i.relationships[k].attrib) for k,edge in p.relationships.items()))
    element_observations = read('elements.json')['added']
    rels = read('relationships.json')
    views = read('views.json')['relevant_views']
    check('All fresh architectural observations represented in decomposition',
          len(element_observations) == 9 and len(rels['added']) == 13 and len(views) == 4 and
          set().union(*(set(c['observations']) for c in rows)) ==
          {f'A{n}' for n in range(1,10)} | {f'R{n}' for n in range(1,14)} | {f'V{n}' for n in range(1,5)})
    connections = [c for view in views for c in view['connections']]
    check('Seven added visual connections reuse existing governed semantic bindings',
          sum(c['relationship_origin'] == 'existing governed relationship' for c in connections) == 7 and
          all(c['semantic_relationship']['exchange_id'] in p.relationships for c in connections
              if c['relationship_origin'] == 'existing governed relationship'))
    check('Resolved duplicate checks and unresolved endpoint identity remain distinct',
          not rels['duplicate_raw_exchange_triples'] and not rels['duplicate_resolved_canonical_triples'] and
          sum(r['resolved_canonical_comparison_key'] is None for r in rels['added']) == 8 and
          all(not r['baseline_matches'] for r in rels['added']))

    # Validate locators directly against pinned XML and bytes.
    data = (D/'submission.xml').read_bytes()
    for record in element_observations + rels['added'] + views:
        nodes = i.root.xpath(record['xpath'])
        line = data[:data.find(('identifier="'+record['exchange_id']+'"').encode())].count(b'\n')+1
        check('Evidence locator '+record['exchange_id'],len(nodes)==1 and nodes[0].get('identifier')==record['exchange_id'] and line==record['physical_line'])
    local_links = []
    documents = list(RUN.rglob('*.md')) + ([REPORT] if REPORT.exists() else [])
    for document in documents:
        content = document.read_text()
        for href in re.findall(r'\[[^\n]*?\]\(([^)\s]+)\)',content):
            if '://' in href:
                continue
            path, _, fragment = href.partition('#')
            target = (document.parent/path).resolve() if path else document
            check('Evidence link resolves: '+str(document.relative_to(ROOT))+': '+href,target.is_file())
            if fragment and target.suffix=='.md':
                source=target.read_text()
                check('Markdown evidence anchor '+fragment, 'id="'+fragment+'"' in source or any(
                    re.sub(r'[^\w -]','',h.lower()).replace(' ','-')==fragment
                    for h in re.findall(r'^#+ (.*?)$',source,re.M)))
            local_links.append(href)
        check('New Markdown has no trailing whitespace: '+str(document.relative_to(ROOT)),
              all(line==line.rstrip() for line in content.splitlines()))

    before=read('start-preservation.json')
    changed=[path for path,digest in before['sha256'].items() if not Path(path).is_file() or sha(path)!=digest]
    files={x.decode() for x in subprocess.check_output(['git','ls-files','-z','--cached','--others','--exclude-standard']).split(b'\0') if x}
    additions=sorted(path for path in files-before['sha256'].keys() if Path(path).is_file())
    allowed=lambda path: path.startswith(str(RUN.relative_to(ROOT))+'/') or path==str(REPORT.relative_to(ROOT))
    check('Every pre-existing protected file is byte-identical; no deletions',not changed)
    check('Only governed run evidence and bounded report were created',all(allowed(path) for path in additions))
    check('Repository HEAD unchanged',subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip()==before['head'])
    check('No reconciliation evidence, source mutation or new publication',not (RUN/'reconciliation').exists() and not changed)
    check('Release remains 1.0.11 / previous 1.0.10',json.loads(Path('architecture-release.json').read_text())=={'version':'1.0.11','previous_version':'1.0.10'})
    for path,digest in prov['authority_pins'].items():
        check('Authority preserved '+path,sha(path)==digest)
    suite=gzip.decompress((V/'architecture-tests.log.gz').read_bytes()).decode()
    suite=re.sub(r'\x1b\[[0-9;]*m','',suite)
    check('Required bounded architecture suite positively passed',
          'Tests run: 90, Failures: 0, Errors: 0, Skipped: 0' in suite and 'BUILD SUCCESS' in suite)
    result={'outcome':'PASS','checks':checks,'check_count':len(checks),'cr_count':len(rows),
        'all_crs_on_hold':True,'all_instructions':'Not authorised','all_processing':'Not yet processed',
        'evidence_links_checked':len(local_links),'preserved_file_count':len(before['sha256']),
        'changed_pre_existing_files':changed,'new_task_files':additions,
        'submission_sha256':prov['submission_sha256'],'baseline_sha256':prov['baseline_sha256'],
        'reconciliation':'Not performed','publication':'Not performed','canonical_id_allocation':'None',
        'semantic_architecture_mutation':'None','adjudication':'None',
        'limitations':['Schema/reference success is not architectural adjudication or GUI rendering acceptance.',
                       'New endpoint governed identity and duplicate resolution remain unanswered as recorded.',
                       'No resulting reconciled state was created or validated.']}
    (V/'review-validation.json').write_text(json.dumps(result,indent=2)+'\n')
    (V/'preservation.json').write_text(json.dumps({k:result[k] for k in ['outcome','preserved_file_count','changed_pre_existing_files','new_task_files','semantic_architecture_mutation','publication','canonical_id_allocation']},indent=2)+'\n')
    print(json.dumps({k:result[k] for k in ['outcome','check_count','cr_count','evidence_links_checked','preserved_file_count','changed_pre_existing_files']}))


if __name__=='__main__':
    main()
