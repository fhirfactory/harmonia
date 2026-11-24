#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Check the Disposition/Outcome revision against retained input and review history."""
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
sys.path.insert(0, str(ROOT/'scripts'))
from rads_business_architecture import anchors


def sha(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()


def read(path):
    return json.loads(Path(path).read_text())


def sections(text):
    return {m.group(1):(m.group(2),m.group(3)) for m in re.finditer(
        r'^## (CR-\d+) — (.*?)\n(.*?)(?=^<a id="cr-\d+[^"]*"></a>|\Z)',text,re.M|re.S)}


def field(body, name):
    return body.split('### '+name+'\n',1)[1].split('\n### ',1)[0].strip()


def main():
    checks=[]
    def check(label, condition):
        checks.append({'check':label,'outcome':'PASS' if condition else 'FAIL'})
        if not condition:
            raise AssertionError(label)
    before=read(V/'start-preservation.json')
    review=(RUN/'change-review.md').read_text()
    historical=RUN/'history/revision-002'
    previous=(historical/'change-review.md').read_text()
    manifest=read(historical/'manifest.json')
    review_path=str((RUN/'change-review.md').relative_to(ROOT))
    check('Revision 2 retained byte-identically with original link origin',
          sha(historical/'change-review.md')==before['sha256'][review_path]==manifest['sha256'] and
          manifest['original_path']==review_path)
    for name,record in manifest['governance_snapshots'].items():
        check('Prior governing representation retained: '+name,
              sha(historical/name)==before['sha256'][record['original_path']]==record['sha256'])
    check('Revision 3 uses same run, with recorded actor/date and semantic transition',
          'revision **3**' in review and '2026-10-19 (Australia/Sydney)' in review and
          '1.0.8/run-001' in review and 'disposition-outcome-transition' in review)
    check('First visible content separates Disposition and Outcome', review.startswith(
          '| ID | Brief Description | Disposition | Outcome |\n| --- | --- | --- | --- |\n'))
    contract_path=Path('docs/markdown/governance/archimate-ingestion-change-review.md')
    template_path=contract_path.parent/'templates/archimate-ingestion-change-review-template.md'
    contract=contract_path.read_text(); template=template_path.read_text()
    dispositions=contract.split('## Solution Architect dispositions\n',1)[1].split('## Outcome',1)[0]
    outcomes=contract.split('## Outcome and Outcome Reason\n',1)[1].split('## Architectural questions',1)[0]
    check('Exactly four governed Dispositions/calls to action',
          re.findall(r'^\| \*\*(.*?)\*\* \|',dispositions,re.M)==['Accept','Reject','On-Hold','Manual Intervention'] and
          "authorised call to action to the\nprocessing agent" in dispositions)
    check('Exactly two independent CR Outcomes, with separate descriptive Reason and evidence',
          re.findall(r'^\| \*\*(.*?)\*\* \|',outcomes,re.M)==['Unresolved','Resolved'] and
          'no Disposition uniquely determines an Outcome' in outcomes and
          '**Outcome Reason** SHALL be recorded separately' in outcomes and
          'descriptive, not a closed enumeration' in outcomes)
    check('Issued Dispositions retained as audit records; subsequent outcomes confer no authority',
          'An issued Disposition is an adjudication/audit record' in dispositions and
          'SHALL NOT silently\nrewrite it' in dispositions and
          'Only an explicit subsequent Solution Architect instruction may revise' in dispositions and
          'Outcome SHALL NOT be collapsed into the Reconciliation / Processing\nRecord or treated as reconciliation authority' in outcomes)
    check('Default CR state and dependency authority/state gates remain explicit',
          'New CRs SHALL start with `Disposition: On-Hold`, `Outcome: Unresolved`' in contract and
          'Outcome Resolved alone supplies neither that resulting state nor\nreconciliation authority' in contract)
    check('No current Status field/header mislabels a Disposition',all(
          '**Status:**' not in text and '| Status |' not in text for text in [contract,template,review]))
    expected=[f'CR-{n:03}' for n in range(1,37)]
    current,prior=sections(review),sections(previous)
    summary=re.findall(r'^\| \[(CR-\d+)\]\(#(cr-\d+)\) \| (.*?) \| (.*?) \| (.*?) \|$',review,re.M)
    overlay=read(RUN/'review-revision-003.json'); prior_overlay=read(RUN/'review-revision-002.json')
    check('All 36 stable unique IDs retained in summary, details and overlay',
          list(current)==list(prior)==expected==[s[0] for s in summary]==[c['id'] for c in overlay['crs']] and len(set(expected))==36)
    headings=['Proposed Architectural Change','Detected Evidence','Architectural Questions / Adjudication',
              'Related Change Requests','Authorised Reconciliation Instruction','Reconciliation / Processing Record']
    for i,cid in enumerate(expected):
        n=i+1; resolved=16<=n<=21; outcome='Resolved' if resolved else 'Unresolved'
        brief,body=current[cid];old=prior[cid][1];cr=overlay['crs'][i];prior_cr=prior_overlay['crs'][i]
        reason=re.search(r'^\*\*Outcome Reason:\*\* (.+)$',body,re.M).group(1)
        check(cid+' summary/detail/structured fields agree; disposition remains original On-Hold',
              summary[i]==(cid,cid.lower(),brief,'On-Hold',outcome) and len(brief)<=80 and
              body.count('**Disposition:** On-Hold')==1 and body.count('**Outcome:** '+outcome)==1 and
              cr['disposition']==prior_cr['status']=='On-Hold' and cr['outcome']==outcome and
              '**Status:** On-Hold' in old and len(reason)>70 and len(cr['outcome_reason'])>60)
        check(cid+' proposal/detected evidence/instruction/processing preserved exactly',
              all('### '+heading+'\n' in body for heading in headings) and
              all(field(body,h)==field(old,h) for h in ['Proposed Architectural Change','Detected Evidence',
                   'Authorised Reconciliation Instruction','Reconciliation / Processing Record']) and
              field(body,'Authorised Reconciliation Instruction')==cr['instruction']=='Not authorised' and
              field(body,'Reconciliation / Processing Record')==cr['processing']=='Not yet processed')
        check(cid+' source observation correspondence and exact evidence digest retained',
              cr['observations']==prior_cr['observations'] and
              cr['detected_evidence_sha256']==prior_cr['detected_evidence_sha256'])
        if resolved:
            check(cid+' Resolved by exact 1.0.12 authority; valid original finding; no related acceptance',
                  'business-architecture-metamodel.md#bec-composition-hierarchy' in reason and
                  '1.0.12' in reason and 'valid finding' in reason and
                  'The original On-Hold instruction remains correct and preserved' in reason and
                  'authorises no reconciliation' in reason and
                  f'without revising Disposition or accepting [CR-{n-6:03}]' in body and
                  'Outcome is **Resolved**' in body and 'former record-handling gap is resolved' in body)
            check(cid+' future instance state is conditional, not a resolution prerequisite',
                  cr['dependencies']==[] and cr['conditional_future_instance_states']==prior_cr['dependencies'] and
                  "not a prerequisite for this finding's Resolved Outcome" in body and
                  all(d['required_governed_state'] in body for d in cr['conditional_future_instance_states']))
        else:
            old_decision=re.search(r'^\*\*Solution Architect Decision:\*\* (.*)$',old,re.M).group(1)
            new_decision=re.search(r'^\*\*Solution Architect Decision:\*\* (.*)$',body,re.M).group(1)
            expected_decision=old_decision.replace('No final disposition.',
                'No acceptance, rejection or manual-resolution decision recorded. The existing/default On-Hold Disposition is preserved.')
            check(cid+' remains architecturally undecided with no new adjudication',
                  new_decision==expected_decision and 'remains undecided' in reason)
            check(cid+' dependencies/related state remain unchanged',
                  cr['dependencies']==prior_cr['dependencies'] and field(body,'Related Change Requests')==field(old,'Related Change Requests'))
        for dep in cr['dependencies']+cr.get('conditional_future_instance_states',[]):
            check(cid+' valid required-state reference '+dep['cr'],
                  dep['cr'] in expected and dep['cr']!=cid and dep['required_governed_state'] in body and
                  ('reconciled' in dep['required_governed_state'] or 'reconciliation' in dep['required_governed_state']))
    check('Exactly six resolved findings; 30 remaining unresolved proposals',
          sum(c['outcome']=='Resolved' for c in overlay['crs'])==6 and
          all(c['outcome']=='Unresolved' for c in overlay['crs'] if c['id'] not in expected[15:21]))
    graph={c['id']:[d['cr'] for d in c['dependencies']] for c in overlay['crs']}
    def walk(cid,seen):
        assert cid not in seen,('dependency cycle',cid,seen)
        for dep in graph[cid]: walk(dep,seen+[cid])
    for cid in graph: walk(cid,[])
    check('Required-state graph acyclic; resolved findings introduce no execution gate',True)
    # Worked form demonstrates independent pairings and separately records all required distinctions.
    trows=re.findall(r'^\| \[(CR-\d+)\]\(#([^)]*)\) \| (.*?) \| (.*?) \| (.*?) \|$',template,re.M)
    tparts=sections(template)
    check('Future template initialises new CRs On-Hold/Unresolved and keeps Outcome Reason separate',
          'every newly generated CR to Disposition On-Hold and Outcome\nUnresolved' in template and
          len(trows)==len(tparts)==5 and 'EXAMPLE ONLY' in template)
    for cid,anchor,brief,disp,outcome in trows:
        body=tparts[cid][1]
        check('Template '+cid+' separately expresses instruction, decision, Outcome, Reason and processing',
              f'<a id="{anchor}"></a>' in template and tparts[cid][0]==brief and len(brief)<=80 and
              '**Disposition:** '+disp in body and '**Outcome:** '+outcome in body and
              '**Outcome Reason:**' in body and '**Solution Architect Decision:**' in body and
              '### Authorised Reconciliation Instruction' in body and '### Reconciliation / Processing Record' in body)
    check('Template demonstrates both unresolved and resolved findings with the same On-Hold Disposition',
          {(r[3],r[4]) for r in trows}>={('On-Hold','Unresolved'),('On-Hold','Resolved'),('Accept','Unresolved')})
    check('One policy patch from actual starting authority',
          before['release']=={'version':'1.0.12','previous_version':'1.0.11'} and
          read('architecture-release.json')=={'version':'1.0.13','previous_version':'1.0.12'})
    correspondence=read('docs/markdown/governance/baseline/correspondence.json')
    check('All existing current-authority hashes resolve correctly',all(sha(p)==h for p,h in correspondence['current_authority_sha256'].items()))
    prov=read(RUN/'detection/provenance.json')
    check('Exact Submission, retained input and Published Baseline hashes preserved',
          sha(prov['submission_path'])==sha(RUN/'detection/submission.xml')==prov['submission_sha256']==
          '6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e' and
          sha(prov['baseline_path'])==prov['baseline_sha256']==
          '504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a')
    ns={'a':'http://www.opengroup.org/xsd/archimate/3.0/'}
    def elements(path): return {e.get('identifier'):e for e in ET.parse(path).getroot().findall('a:elements/a:element',ns)}
    baseline,incoming=elements(prov['baseline_path']),elements(prov['submission_path'])
    additions=incoming.keys()-baseline.keys()
    check('Nine submitted additions still lack any allocated IDs or generated Properties',
          len(additions)==9 and all(incoming[k].find('a:properties',ns) is None for k in additions))
    allowed={review_path,'architecture-release.json',str(contract_path),str(template_path),
             'docs/markdown/governance/archimate-ingestion-publication-semantics.md',
             'docs/markdown/governance/architecture-authoring-praxis.md','docs/markdown/governance/README.md',
             'docs/markdown/governance/baseline/correspondence.json','docs/archimate/processing/ingestion/README.md'}
    changed=sorted(p for p,h in before['sha256'].items() if not Path(p).is_file() or sha(p)!=h)
    check('Modified original files exactly match bounded governance/review/release scope',set(changed)==allowed)
    protected=[p for p in before['sha256'] if p not in allowed]
    check('Every other original file unchanged, including 1.0.12 metamodel, immutable detection and all prior history/validation',
          all(Path(p).is_file() and sha(p)==before['sha256'][p] for p in protected))
    files={p.decode() for p in subprocess.check_output(['git','ls-files','-z','--cached','--others','--exclude-standard']).split(b'\0') if p}
    added=sorted(p for p in files-before['sha256'].keys() if Path(p).is_file())
    report=Path('.junie/reports/2026-10-19-change-request-disposition-outcome.md')
    prefix=str(RUN.relative_to(ROOT))+'/'
    check('Additions confined to retained revision, current overlay/validation and bounded report',
          all(p.startswith(prefix) or p==str(report) for p in added))
    check('No proposal reconciliation, authoring iteration, run, or Published Baseline created',
          not (RUN/'reconciliation').exists() and not any('/exchange/' in p or '/reconciliation/' in p for p in added) and
          subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip()==before['head'])
    documents=[RUN/'change-review.md',report,contract_path,template_path,
               Path('docs/markdown/governance/architecture-authoring-praxis.md'),
               Path('docs/markdown/governance/archimate-ingestion-publication-semantics.md'),
               Path('docs/markdown/governance/README.md'),Path('docs/archimate/processing/ingestion/README.md')]
    link_count=0
    for doc in documents:
        content=doc.read_text()
        rendered=re.sub(r'```[^\n]*\n.*?```','',content,flags=re.S)
        for href in re.findall(r'\[[^\n]*?\]\(([^)\s]+)\)',rendered):
            if '://' in href: continue
            path,_,anchor=href.partition('#');target=(doc.parent/path).resolve() if path else doc
            check('Link target resolves: '+str(doc)+': '+href,target.is_file())
            if anchor and target.suffix=='.md': check('Markdown anchor resolves: '+anchor,anchor in anchors(target.read_text()))
            link_count+=1
        check('No new trailing whitespace: '+str(doc),all(line==line.rstrip() for line in content.splitlines()))
    architecture=re.sub(r'\x1b\[[0-9;]*m','',gzip.decompress((V/'architecture-tests.log.gz').read_bytes()).decode())
    tests=gzip.decompress((V/'governance-tests.log.gz').read_bytes()).decode()
    check('Bounded architecture and governance suites positively passed',
          'Tests run: 90, Failures: 0, Errors: 0, Skipped: 0' in architecture and 'BUILD SUCCESS' in architecture and
          'Ran 72 tests' in tests and tests.rstrip().endswith('OK'))
    check('Historical capture validation passes with 2467 preserved unresolved dimensions',
          json.loads(gzip.decompress((V/'rads-validation.log.gz').read_bytes()))==
          {'result':'PASS','invalid':0,'unresolved_by_architectural_decision':2467})
    result={'outcome':'PASS','revision':3,'checks':checks,'check_count':len(checks),'cr_count':36,
            'disposition_counts':{'On-Hold':36},'outcome_counts':{'Unresolved':30,'Resolved':6},
            'all_reconciliation_instructions':'Not authorised','all_processing_records':'Not yet processed',
            'review_links_checked':link_count,'preserved_pre_existing_files':len(protected),
            'modified_pre_existing_files':changed,'new_revision_files':added,
            'submission_sha256':prov['submission_sha256'],'baseline_sha256':prov['baseline_sha256'],
            'proposal_adjudication':'None','proposal_reconciliation':'None','canonical_id_allocation':'None',
            'publication':'None','remaining_governance_uncertainty_in_scope':'None'}
    (V/'validation-results.json').write_text(json.dumps(result,indent=2)+'\n')
    print(json.dumps({k:result[k] for k in ['outcome','revision','check_count','disposition_counts','outcome_counts','preserved_pre_existing_files']}))


if __name__=='__main__':
    main()
