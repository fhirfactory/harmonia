# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
import re,json,csv,hashlib,collections,zipfile,xml.etree.ElementTree as ET,posixpath
from pathlib import Path
exec(Path(__file__).with_name('normalise_definitions.py').read_text())
exec(Path(__file__).with_name('explicit_outcomes.py').read_text())
exec(Path(__file__).with_name('xd_outcomes.py').read_text())
DEST=BASE.parent/'after/pass3-xd-consolidation'
DEST.mkdir(exist_ok=True)
CAT=DEST/'Harmonia_1.1.20_Candidate_017_Pass3_012_XD_Consolidated_Catalogue.md'
# Read the baseline only. All source bytes remain unchanged.
original_lines=text.splitlines(); feature_records=[];cap_records=[];supporting=[];new=[];ct3='';ct3_name='';active_cap=None;expect_desc=False
for lineno,line in enumerate(original_lines,1):
 m=re.match(r'^(#{2,4}) ([BE]\d+(?:\.\d+){0,2}) — (.*)$',line)
 if m:
  hashes,i,name=m.groups();orig=name;name=clean(name)
  if len(hashes)==4:
   ct3=i;ct3_name=name
   if i in CT3 and CT3[i][0]:name=CT3[i][0];ct3_name=name
  active_cap=dict(id=i,level=len(hashes)-1,original_name=orig,name=name,original_description='',description='',source_line=lineno)
  cap_records.append(active_cap);expect_desc=True;new.append(f'{hashes} {i} — {name}');continue
 if expect_desc and line.strip():
  expect_desc=False;active_cap['original_description']=line;desc=clean(line)
  if active_cap['id'] in CT3:desc=CT3[active_cap['id']][1]
  active_cap['description']=desc;new.append(desc);continue
 m=FP.fullmatch(line)
 if m:
  i,n,d=m.groups();desc,rule=convert(i,n,d)
  if i in OUTCOMES:desc=OUTCOMES[i];rule='explicit-source-outcome'
  newname=clean(n)
  if i in FEATURES:newname,desc=FEATURES[i];rule='Revision-2-XD'
  assert desc,(i,d)
  # Retain exact original criteria and actual source status independently.
  status=re.search(r'\*(PROPOSED(?: — unverified)?)\*',d)
  record=dict(id=i,parent=ct3,parent_name=ct3_name,original_name=n,name=newname,original_description=d,description=desc,source_line=lineno,normalisation=rule,original_status=status.group(1) if status else 'No explicit status token',source_criterion=clean(d),measurement_criterion=clean(d) if re.search(r'^(proportion|percentage|elapsed time|number|rate|retrieval responsiveness|conformance of)',clean(d),re.I) else '',implementation_verification='Not established by this activity')
  record['technical_correspondence']={'E06.08.01.F03':'Published Pragma -> accepted undertaking with corresponding FulfillmentTask; publication is not acceptance.','E06.08.05.F03':'Accepted FulfillmentTask fully contextualised and loaded into assigned Ergon; successful progression beyond governed Ergon.start() checkpoint, not completion.'}.get(i,'')
  feature_records.append(record);new.append(f'- **`{i}` — {newname}:** {desc}');continue
 # Source stage annotations are supporting evidence, not canonical definitions.
 if re.search(r'^\*\*(New Candidate|Candidate \d|New .*Candidate)',line,re.I):supporting.append(dict(source_line=lineno,text=line));continue
 new.append(line)
# Replace accumulated historical banner with the actual current preview standing.
body='\n'.join(new);body=body[body.index('# Business Capabilities'):]
preamble='''# Harmonia 1.1.20 — Consolidated Business and Enterprise Capability Catalogue

**Offline working candidate — Pass 3/012.** Prepared under accepted XD-001–XD-028 Revision 2 and AR-001/AR-002. Not incorporated into the Architecture Continuum; not implementation verification or release acceptance. The combined Business/Business Enabling hierarchy remains separate from Enterprise; CT1 → CT2 → CT3 → Feature identities and containment are preserved.

Definitions below express required architectural capabilities and outcomes, not claims that they are implemented. Original wording, proposal/verification standing, measurements and source traceability are retained by identifier in [the complete reconciliation register](feature-reconciliation.json) and [the measurement register](measurement-criteria.csv), and in the companion adjudication workbook. Original source criteria are historical evidence where Revision 2 supersedes their meaning; they must not override accepted revised definitions. No numerical target or new measurement programme is introduced.

**XD-027 placement:** The Solution Architect authorised refining existing `E06.02.03.F03` to retain Twin event context and add selective Stimulus information contextualisation. Its original criterion is preserved; no Feature is added or repurposed from recovery, security or work allocation. See [the reconciliation report](reconciliation-report.md). Pre-existing accepted-SR correspondence discrepancies are preserved in [the qualification register](sr-correspondence-qualifications.json); they are not represented as newly incorporated. FR-001–FR-009, E07.03.05's separate semantic review and FC-042/059/101 retain their existing unresolved/excluded standing. No completion of those workstreams is implied.

'''
CAT.write_text(preamble+body+'\n')
# Deliberate semantic shifts mean the old evaluative text is evidence, not a current mandate.
superseded={'E08.08.02.F04','E08.08.03.F05','E08.08.04.F01','E08.08.04.F02','B10.06.04.F01','B10.06.04.F02','B11.03.04.F04','E02.03.04.F02','E06.08.01.F03','E06.08.05.F03','E08.06.02.F01','B07.03.01.F04','B07.03.02.F02','E04.04.01.F01'}
for r in feature_records:
 r['criterion_treatment']='Historical source criterion; superseded by Revision 2 outcome' if r['id'] in superseded else 'Retained source assessment criterion; does not by itself assess every refinement' if r['id'] in FEATURES else 'Retained associated measurement criterion' if r['measurement_criterion'] else 'Retained outcome/assurance condition'
 r['current_assurance_condition']=r['description'] if r['id'] in FEATURES else r['source_criterion']
(DEST/'feature-reconciliation.json').write_text(json.dumps(feature_records,ensure_ascii=False,indent=2)+'\n')
(DEST/'capability-reconciliation.json').write_text(json.dumps(cap_records,ensure_ascii=False,indent=2)+'\n')
(DEST/'source-editorial-notes.json').write_text(json.dumps({'preamble':text[:text.index('# Business Capabilities')],'other_notes':supporting},ensure_ascii=False,indent=2)+'\n')
with (DEST/'measurement-criteria.csv').open('w',newline='') as f:
 w=csv.writer(f);w.writerow(['Feature ID','Original source criterion (verbatim)','Source measurement','Treatment','Current assurance condition','Source status','Implementation verification'])
 for r in feature_records:w.writerow([r['id'],r['original_description'],r['measurement_criterion'],r['criterion_treatment'],r['current_assurance_condition'],r['original_status'],r['implementation_verification']])
# Preserve accepted decision text in full with source reference, not a new adjudication.
spec=(BASE/'Harmonia_1.1.20_XD001-XD028_Consolidated_Adjudications_Rev2.md').read_text()
sections=dict((int(m.group(1)),m.group(2).strip()) for m in re.finditer(r'^### XD-(\d{3}) — (.*?)(?=^### XD-|^## 5\.)',spec,re.M|re.S))
assert set(sections)==set(range(1,29))
xd=[]
for k,v in sections.items():
 title=v.splitlines()[0];detail='\n'.join(v.splitlines()[1:]).strip();ids=list(dict.fromkeys(re.findall(r'[BE]\d+\.\d+\.\d+(?:\.F\d+)?',detail)))
 status='Accepted and incorporated'
 facts='Baseline structural decision preserved; no repeated retirement or renumbering.' if k in (1,2,5,7) else 'Baseline Twin refinement preserved; canonical status separated.' if k in (8,9) else 'Distinct responsibilities preserved; source criteria separated under AR-001.' if k in (19,26) else 'Revision 2 outcome incorporated; affected parent and neighbouring definitions reconciled.'
 if k==18:facts='F01 accountable recipient/identity, F04 timed acknowledgement/escalation, F05 attributable recipient/delivery/acknowledgement/follow-up, F07 timely notification incorporated; F02/F03 preserve clinical-response distinction and follow-up.'
 if k==22:facts='B12 enterprise continuity retained; processing-boundary assurance refined; initial durable intake already represented by E06.07.01 and E06.08.05.F04 without new Feature or queue mandate.'
 if k==27:facts='Original E06.02.04.F02 and E06.02.05.F04 retained. Solution Architect explicitly authorised additive refinement of E06.02.03.F03 (Twin Event Context) in this interaction. Twin event context and its original assessment criterion retained; selective input contextualisation added without overwriting allocation, recovery or custody Features. No new Feature.'
 xd.append(dict(id=f'XD-{k:03}',title=title,status=status,elements=ids,accepted_specification=detail,incorporation=facts))
(DEST/'xd-incorporation.json').write_text(json.dumps(xd,ensure_ascii=False,indent=2)+'\n')
# Source and resulting population inventories are independent of release authority.
def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
manifest={'baseline_catalogue':str(SRC),'baseline_sha256':sha(SRC),'baseline_workbook':str(BASE/'Harmonia_1.1.20_Pass3_011_NR001_Adjudication_Workbook.xlsx'),'baseline_workbook_sha256':sha(BASE/'Harmonia_1.1.20_Pass3_011_NR001_Adjudication_Workbook.xlsx'),'specification_sha256':sha(BASE/'Harmonia_1.1.20_XD001-XD028_Consolidated_Adjudications_Rev2.md'),'result_catalogue':str(CAT),'result_sha256':sha(CAT),'features':len(feature_records),'ct3':sum(r['level']==3 for r in cap_records),'feature_rules':dict(collections.Counter(r['normalisation'] for r in feature_records))}
(DEST/'source-result-manifest.json').write_text(json.dumps(manifest,indent=2)+'\n')
print(json.dumps(manifest,indent=2))
