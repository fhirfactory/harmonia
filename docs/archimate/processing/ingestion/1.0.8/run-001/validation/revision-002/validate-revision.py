#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Validate the shared decision revision without changing proposal architecture."""
import gzip
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
import xml.etree.ElementTree as ET

ROOT = Path.cwd()
RUN = Path(__file__).resolve().parents[2]
V = Path(__file__).resolve().parent
sys.path.insert(0, str(ROOT / 'scripts'))
from rads_business_architecture import anchors, source_era_text


def sha(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()


def read(path):
    return json.loads(Path(path).read_text())


def main():
    checks = []
    def check(label, result):
        checks.append({'check': label, 'outcome': 'PASS' if result else 'FAIL'})
        if not result:
            raise AssertionError(label)

    before = read(V/'start-preservation.json')
    history = RUN/'history/revision-001/change-review.md'
    original = history.read_text()
    review = (RUN/'change-review.md').read_text()
    manifest = read(history.parent/'manifest.json')
    original_path = str((RUN/'change-review.md').relative_to(ROOT))
    check('Review revision 1 retained byte-identically with original link origin',
          sha(history) == before['sha256'][original_path] == manifest['sha256'] and
          manifest['original_path'] == original_path)
    check('Revision, decision date and preparation date are distinguished',
          'revision **2**' in review and 'Codex / 2026-10-18' in review and
          '2026-10-19 (Australia/Sydney)' in review and 'shared-metamodel-decision' in review)
    check('First visible content is the governed compact table', review.startswith(
          '| ID | Brief Description | Status |\n| --- | --- | --- |\n'))
    expected = [f'CR-{n:03}' for n in range(1, 37)]
    summary = re.findall(r'^\| \[(CR-\d+)\]\(#(cr-\d+)\) \| (.*?) \| (.*?) \|$', review, re.M)
    def sections(text):
        return {m.group(1): (m.group(2), m.group(3)) for m in re.finditer(
            r'^## (CR-\d+) — (.*?)\n(.*?)(?=^<a id="cr-\d+"></a>|\Z)', text, re.M|re.S)}
    current, prior = sections(review), sections(original)
    check('36 stable unique IDs agree with the original summary/details',
          list(current) == list(prior) == expected == [r[0] for r in summary] and
          len(set(r[0] for r in summary)) == 36)
    overlay = read(RUN/'review-revision-002.json')
    detection = read(RUN/'detection/change-requests.json')
    check('Revision overlay includes all stable CRs and exactly six shared findings',
          [r['id'] for r in overlay['crs']] == expected and len(overlay['shared_findings']) == 6)
    headings = ['Proposed Architectural Change', 'Detected Evidence',
                'Architectural Questions / Adjudication', 'Related Change Requests',
                'Authorised Reconciliation Instruction', 'Reconciliation / Processing Record']
    def field(text, heading):
        return text.split('### '+heading+'\n', 1)[1].split('\n### ', 1)[0].strip()
    def decision(text):
        return re.search(r'^\*\*Solution Architect Decision:\*\* (.*)$', text, re.M).group(1)
    for i, cid in enumerate(expected):
        brief, body = current[cid]
        old_body = prior[cid][1]
        cr = overlay['crs'][i]
        check(cid+' template headings/table/status/instruction/processing agree',
              all('### '+h+'\n' in body for h in headings) and
              summary[i] == (cid, cid.lower(), brief, 'On-Hold') and len(brief) <= 80 and
              body.count('**Status:** On-Hold') == 1 and cr['status'] == 'On-Hold' and
              field(body,'Authorised Reconciliation Instruction') == cr['instruction'] == 'Not authorised' and
              field(body,'Reconciliation / Processing Record') == cr['processing'] == 'Not yet processed')
        check(cid+' exact Detected Evidence retained, including source identity',
              field(body,'Detected Evidence') == field(old_body,'Detected Evidence') == detection[i]['evidence'].strip())
        check(cid+' required-state dependencies and source observations retained',
              cr['dependencies'] == detection[i]['dependencies'] and
              cr['observations'] == detection[i]['observations'] and
              all(dep['cr'] in expected and dep['cr'] != cid and
                  dep['required_governed_state'] in body and
                  ('reconciled' in dep['required_governed_state'] or
                   'reconciliation' in dep['required_governed_state']) for dep in cr['dependencies']))
        n = i+1
        if 16 <= n <= 21:
            check(cid+' shares one governing meaning decision without instance approval',
                  'Original revision-1 questions' in body and 'Current treatment' in body and
                  'bec-composition-hierarchy' in body and 'shared-metamodel-decision' in body and
                  'No independent instance-level CT3 framing interpretation remains' in body and
                  'On-Hold pending Solution Architect confirmation of record handling' in decision(body) and
                  f'This is not acceptance of [CR-{n-6:03}]' in decision(body) and
                  detection[i]['proposal'] in body and all(q in body for q in detection[i]['questions']))
        else:
            check(cid+' individual Solution Architect decision remains unchanged', decision(body) == decision(old_body))
        if 10 <= n <= 15:
            check(cid+' Composition remains independently undecided with deterministic meaning if accepted',
                  'If accepted' in body and 'no separate instance interpretation decision' in body and
                  'bec-composition-hierarchy' in body)
        if n in list(range(2,10)) + list(range(22,33)) + list(range(34,37)):
            check(cid+' complete unrelated detail unchanged', body == old_body)
    graph = {c['id']: [d['cr'] for d in c['dependencies']] for c in overlay['crs']}
    def walk(cid, seen):
        assert cid not in seen, ('dependency cycle',cid,seen)
        for parent in graph[cid]:
            walk(parent, seen+[cid])
    for cid in graph:
        walk(cid, [])
    check('Dependencies are valid and acyclic, not executed', True)
    check('View question no longer asks for six independent context approvals',
          'not additional interpretation-approval prerequisites' in current['CR-033'][1] and
          not set(graph['CR-033']) & {f'CR-{n:03}' for n in range(16,22)})

    meta = 'docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md'
    metamodel = Path(meta).read_text()
    check('Governed hierarchy, direction, Feature typing and non-semantic placement are explicit', all(
        text in metamodel for text in ['CT1 → Composition → CT2 → Composition → CT3 → Composition → Feature',
        'whole/source to part/target', 'native ArchiMate `Capability`', '`harmoniaType=Feature`',
        'Model-tree/folder nesting, View membership, View geometry or visual placement,',
        'naming proximity and navigation organisation SHALL NOT independently establish',
        'does not accept any particular submitted Composition']))
    governance = ['rad-archimate-model-mapping.md','archimate-ingestion-publication-semantics.md',
                  'rad-archimate-mapping-catalogue.md','rad-archimate-coverage-assessment.md','README.md']
    check('Related current governance consistently references the resolved metamodel rule', all(
          'bec-composition-hierarchy' in Path('docs/markdown/governance',p).read_text() for p in governance))
    release = read('architecture-release.json')
    check('One governed patch from actual starting release; no publication inference',
          before['release'] == {'version':'1.0.11','previous_version':'1.0.10'} and
          release == {'version':'1.0.12','previous_version':'1.0.11'} and
          overlay['published_comparison_baseline'] == '1.0.8')
    pins = read('docs/markdown/governance/baseline/correspondence.json')
    old_pins = json.loads(subprocess.check_output(['git','show','HEAD:docs/markdown/governance/baseline/correspondence.json'],text=True))
    check('All current authority pins match live sources', all(sha(p)==h for p,h in pins['current_authority_sha256'].items()))
    check('Source/recovery/identity establishment pins remain historical and unchanged', all(
          pins[key] == old_pins[key] for key in ['pinned_inputs','motivation_capture_sha256','canonical_identity_establishment']))
    capture = read('docs/markdown/03-business-architecture/reference-model/business-architecture.json')
    era = capture['sources']['metamodel']['sha256']
    check('Frozen capture uses exact hash-proved historical source, distinct from new authority',
          hashlib.sha256(source_era_text(Path(meta), era).encode()).hexdigest() == era and sha(meta) != era)
    prov = read(RUN/'detection/provenance.json')
    check('Submission, retained Submission and originating Published Baseline hashes unchanged',
          sha(prov['submission_path']) == sha(RUN/'detection/submission.xml') == prov['submission_sha256'] ==
          '6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e' and
          sha(prov['baseline_path']) == prov['baseline_sha256'] ==
          '504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a')
    ns = {'a':'http://www.opengroup.org/xsd/archimate/3.0/'}
    def elements(path):
        root = ET.parse(path).getroot()
        return {e.get('identifier'):e for e in root.findall('a:elements/a:element', ns)}
    baseline, incoming = elements(prov['baseline_path']), elements(prov['submission_path'])
    additions = incoming.keys() - baseline.keys()
    check('All nine proposed elements remain without Canonical IDs or generated metadata',
          len(additions)==9 and all(incoming[k].find('a:properties',ns) is None for k in additions))

    changed = sorted(p for p,h in before['sha256'].items() if not Path(p).is_file() or sha(p)!=h)
    allowed = {original_path,'architecture-release.json',meta,
        'docs/markdown/governance/README.md','docs/markdown/governance/archimate-ingestion-publication-semantics.md',
        'docs/markdown/governance/baseline/README.md','docs/markdown/governance/baseline/correspondence.json',
        'docs/markdown/governance/rad-archimate-coverage-assessment.md',
        'docs/markdown/governance/rad-archimate-mapping-catalogue.md',
        'docs/markdown/governance/rad-archimate-model-mapping.md',
        'scripts/rads_business_architecture.py','scripts/tests/test_rads_source_presentation.py'}
    check('Only authorised governance, release, review and historical validation files modified',set(changed)==allowed)
    files = {p.decode() for p in subprocess.check_output(['git','ls-files','-z','--cached','--others','--exclude-standard']).split(b'\0') if p}
    added = sorted(p for p in files-before['sha256'].keys() if Path(p).is_file())
    prefix = str(RUN.relative_to(ROOT))+'/'
    report = Path('.junie/reports/2026-10-19-ct3-feature-composition-decision.md')
    archive = pins['historical_authority_sources'][meta]['snapshot']
    check('Additions bounded to revision evidence, exact historical source archive and report',all(
          p.startswith(prefix) or p in {str(report),archive} for p in added))
    protected = [p for p in before['sha256'] if p not in allowed]
    check('All other pre-existing files preserved, including semantic sources, capture, evidence and publications',
          all(Path(p).is_file() and sha(p)==before['sha256'][p] for p in protected))
    check('No reconciliation, new run or publication; repository HEAD unchanged',
          not (RUN/'reconciliation').exists() and
          subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip() == before['head'] and
          not any('/exchange/' in p or '/reconciliation/' in p for p in added))

    documents = [RUN/'change-review.md',report,Path(meta)] + [Path('docs/markdown/governance',p) for p in governance] + [Path('docs/markdown/governance/baseline/README.md')]
    link_count = 0
    for doc in documents:
        text = doc.read_text()
        # Check all current review/report links, plus newly added governance links.
        links = re.findall(r'\[[^\n]*?\]\(([^)\s]+)\)',text)
        if doc not in [RUN/'change-review.md',report]:
            old = subprocess.check_output(['git','show','HEAD:'+str(doc)],text=True)
            links = [link for link in links if link not in old]
        for href in links:
            if '://' in href:
                continue
            path,_,fragment = href.partition('#')
            target = (doc.parent/path).resolve() if path else doc
            check('Link resolves: '+str(doc)+': '+href, target.is_file())
            if fragment and target.suffix=='.md':
                check('Markdown anchor resolves: '+fragment,fragment in anchors(target.read_text()))
            link_count += 1
        check('No newly introduced trailing whitespace in edited Markdown '+str(doc), all(
              l==l.rstrip() or (doc not in [RUN/'change-review.md',report] and l in old.splitlines())
              for l in text.splitlines()))
    check('Architecture and governance regression suites passed',
          'Tests run: 90, Failures: 0, Errors: 0, Skipped: 0' in re.sub(r'\x1b\[[0-9;]*m','',gzip.decompress((V/'architecture-tests.log.gz').read_bytes()).decode()) and
          'Ran 72 tests' in gzip.decompress((V/'governance-tests.log.gz').read_bytes()).decode() and
          gzip.decompress((V/'governance-tests.log.gz').read_bytes()).decode().rstrip().endswith('OK'))
    check('Historical capture validation preserves 2467 unresolved dimensions',
          json.loads(gzip.decompress((V/'rads-validation.log.gz').read_bytes())) ==
          {'result':'PASS','invalid':0,'unresolved_by_architectural_decision':2467})
    result = {'outcome':'PASS','revision':2,'checks':checks,'check_count':len(checks),
              'cr_count':36,'all_statuses':'On-Hold','all_instructions':'Not authorised','all_processing':'Not yet processed',
              'shared_meaning_decision':'Resolved by Solution Architect; no individual proposal adjudication',
              'retained_finding_disposition':'On-Hold pending confirmation of record handling',
              'evidence_links_checked':link_count,'preserved_pre_existing_file_count':len(protected),
              'modified_pre_existing_files':changed,'new_revision_files':added,
              'submission_sha256':prov['submission_sha256'],'baseline_sha256':prov['baseline_sha256'],
              'canonical_id_allocation':'None','proposal_reconciliation':'Not performed','publication':'Not performed'}
    (V/'validation-results.json').write_text(json.dumps(result,indent=2)+'\n')
    print(json.dumps({k:result[k] for k in ['outcome','check_count','cr_count','evidence_links_checked','preserved_pre_existing_file_count']}))


if __name__ == '__main__':
    main()
