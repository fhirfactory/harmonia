# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only result checks for this offline consolidation; no architectural acceptance."""
from pathlib import Path
import re,json,collections,zipfile,xml.etree.ElementTree as E,posixpath,hashlib,csv
BASE=Path('enterprise-architecture/design/evidence/1.1.20/001');OUT=BASE/'after/pass3-xd-consolidation'
features=json.loads((OUT/'feature-reconciliation.json').read_text());caps=json.loads((OUT/'capability-reconciliation.json').read_text());manifest=json.loads((OUT/'source-result-manifest.json').read_text());xd=json.loads((OUT/'xd-incorporation.json').read_text())
source=(BASE/'before/Harmonia_1.1.20_Candidate_017_Pass3_011_XD009_Working_Overlay.md').read_text();target=(OUT/'Harmonia_1.1.20_Candidate_017_Pass3_012_XD_Consolidated_Catalogue.md').read_text()
FP=re.compile(r'^- \*\*`([^`]+)` — (.*?):\*\* (.*)$',re.M);HP=re.compile(r'^(#{2,4}) ([BE]\d+(?:\.\d+){0,2}) — (.*)$',re.M)
sf={i:(n,d) for i,n,d in FP.findall(source)};tf={i:(n,d) for i,n,d in FP.findall(target)};sc={i:(len(h),n) for h,i,n in HP.findall(source)};tc={i:(len(h),n) for h,i,n in HP.findall(target)}
checks={}
def check(k,v):checks[k]='PASS' if v else 'FAIL'
check('baseline_population',len(sf)==2941 and sum(v[0]==4 for v in sc.values())==586)
check('result_population_and_identity_preservation',set(sf)==set(tf) and set(sc)==set(tc))
check('no_duplicate_feature_or_capability_identifiers',len(FP.findall(target))==len(tf) and len(HP.findall(target))==len(tc) and not (set(tf)&set(tc)))
parents={};cur={};bad=[]
for line in target.splitlines():
 m=HP.fullmatch(line)
 if m:
  h,i,n=m.groups();lvl=len(h);cur[lvl]=i
  if lvl>2 and (i.rsplit('.',1)[0]!=cur.get(lvl-1)):bad.append((i,'capability parent'))
  parents[i]=cur.get(lvl-1)
 m=FP.fullmatch(line)
 if m:
  i,n,d=m.groups()
  if i.rsplit('.',1)[0]!=cur.get(4):bad.append((i,'feature parent'))
check('hierarchy_and_containment',not bad)
check('complete_feature_source_correspondence',len(features)==len(sf) and all((r['original_name'],r['original_description'])==sf[r['id']] and (r['name'],r['description'])==tf[r['id']] for r in features))
check('original_assessment_evidence_preserved_verbatim',all(source.splitlines()[r['source_line']-1]==f"- **`{r['id']}` — {r['original_name']}:** {r['original_description']}" for r in features))
check('no_kpi_formula_as_canonical_definition',not any(re.search(r'^(proportion\b|percentage\b|elapsed time\b|number of\b|rate of\b|conformance of .*under tests)',r['description'],re.I) for r in features))
check('no_editorial_status_tokens',not any(re.search(r'\*PROPOSED|\bPROPOSED\b|\[NEW C\d+\]|INFERRED_FROM_EVIDENCE|— unverified',x) for r in features+caps for x in (r['name'],r['description'])))
check('no_measurement_criterion_or_source_status_lost',len(list(csv.DictReader((OUT/'measurement-criteria.csv').open())))==2941 and all(r['source_criterion'] and r['criterion_treatment'] and r['original_status'] and r['implementation_verification']=='Not established by this activity' for r in features))
retired=['E01.01.05.F02','E03.05.04.F02','E07.03.04.F03','B04.03.03']+[f'B04.03.03.F{n:02}' for n in range(1,7)]
check('accepted_retirements_absent_from_active_hierarchy',all(i not in tf and i not in tc for i in retired))
check('no_active_references_to_retired_identifiers',not any(re.search(r'(?<![\w.])'+re.escape(i)+r'(?![\w.])',target) for i in retired))
check('accepted_numbering_gaps_preserved',set(i for i in tf if i.startswith('E01.01.05.'))==set('E01.01.05.'+f for f in ('F01','F03','F04','F05')) and set(i for i in tf if i.startswith('E07.03.04.'))==set('E07.03.04.'+f for f in ('F01','F02','F04','F05')))
check('all_28_xd_records',set(r['id'] for r in xd)=={f'XD-{n:03}' for n in range(1,29)} and all(r['status']=='Accepted and incorporated' for r in xd))
by={r['id']:r for r in features};cf={r['id']:r for r in caps}
check('only_authorised_ct3_renames',[r['id'] for r in caps if r['name']!=re.sub(r'\s*\*\*\[NEW C\d+\]\*\*','',r['original_name']).strip()]==['E01.04.04','E08.08.04'])
check('xd018_recipient_acknowledgement_and_evidence',all(w in by[i]['description'].lower() for i,w in [('B04.03.04.F01','accountable'),('B04.03.04.F01','identity assurance'),('B04.03.04.F04','interval'),('B04.03.04.F04','missing or late'),('B04.03.04.F05','actual recipient'),('B04.03.04.F05','follow-up'),('B04.03.04.F07','interval')]))
check('xd014_policy_disclosure_not_unconditional',[w in by['E08.08.04.F01']['description'].lower() for w in ('selective','all-or-nothing','policy')]==[True]*3 and 'whole-set authorisation' not in target.lower())
check('xd020_broad_access_parent_preserved',cf['E02.01.02']['description']==cf['E02.01.02']['original_description'] and 'independently verifies' in by['E02.01.02.F03']['description'])
check('xd021_model_and_instance_verification_preserved','information-instance conformance' in cf['E04.04.01']['description'] and all(w in by['E04.04.01.F01']['description'] for w in ('reference data','terminology dependencies','completeness')) and 'Information codes and values' in by['E04.04.02.F01']['description'])
check('xd022_enterprise_outcome_unchanged',by['B12.04.04.F01']['description']==re.sub(r'\s*\*PROPOSED(?: — unverified)?\*\.?','',by['B12.04.04.F01']['original_description']).strip() and 'second persistence' in by['E08.06.02.F01']['description'])
check('xd027_authorised_existing_feature_allocation','General inputs remain valid' in by['E06.02.03.F03']['description'] and all(by[i]['normalisation']!='Revision-2-XD' for i in ('E06.02.03.F04','E06.02.03.F05','E06.02.04.F02','E06.02.05.F01','E06.02.05.F04','E05.05.02.F02')))
check('xd028_permission_and_reference_distinction','authorisation' in by['E02.03.02.F05']['description'] and 'embedded' in by['E02.03.02.F02']['description'])
check('baseline_catalogue_bytes_preserved',hashlib.sha256(Path(manifest['baseline_catalogue']).read_bytes()).hexdigest()==manifest['baseline_sha256'])
check('baseline_workbook_bytes_preserved',hashlib.sha256(Path(manifest['baseline_workbook']).read_bytes()).hexdigest()==manifest['baseline_workbook_sha256'])
check('release_authority_unchanged',Path('architecture-release.json').read_text()==__import__('subprocess').check_output(['git','show','HEAD:architecture-release.json'],text=True))
S='http://schemas.openxmlformats.org/spreadsheetml/2006/main';R='http://schemas.openxmlformats.org/officeDocument/2006/relationships';q=lambda t:'{'+S+'}'+t
workbook=OUT/'Harmonia_1.1.20_Pass3_012_XD_Adjudication_Workbook.xlsx'
with zipfile.ZipFile(workbook) as z,zipfile.ZipFile(manifest['baseline_workbook']) as orig:
 check('xlsx_zip_integrity',z.testzip() is None)
 for n in z.namelist():
  if n.endswith(('.xml','.rels')):E.fromstring(z.read(n))
 check('xlsx_xml_wellformed',True)
 wb=E.fromstring(z.read('xl/workbook.xml'));rel={r.get('Id'):r.get('Target') for r in E.fromstring(z.read('xl/_rels/workbook.xml.rels'))};sheets=wb.find(q('sheets'));names={s.get('name'):s for s in sheets}
 def path(s):
  p=rel[s.get('{'+R+'}id')];return p.lstrip('/') if p.startswith('/') else posixpath.normpath('xl/'+p)
 def sheet(name):return E.fromstring(z.read(path(names[name])))
 def vals(row):return [''.join(t.text or '' for t in c.iter() if t.tag in (q('t'),q('v'))) for c in row]
 check('xlsx_all_sheet_targets_exist',all(path(s) in z.namelist() for s in sheets))
 check('xlsx_sheet_names_unique',len(names)==len(sheets))
 check('xlsx_sr_register_unchanged',z.read(path(names['SR Decisions']))==orig.read('xl/worksheets/sheet8.xml'))
 check('xlsx_fr_register_unchanged',z.read(path(names['Feature Duplicates']))==orig.read('xl/worksheets/sheet4.xml'))
 checks_old={'History Overview':1,'History XD Decisions':13,'History NR001 Cross-Path':9,'History NR001 Screening Queue':11,'History XD002 Merge Map':14,'History Wording Flags':7}
 check('xlsx_historical_snapshots_preserved',all(z.read(path(names[name]))==orig.read(f'xl/worksheets/sheet{n}.xml') for name,n in checks_old.items()))
 fr=sheet('Feature Definitions').find(q('sheetData')).findall(q('row'))[1:];mr=sheet('Measurement Criteria').find(q('sheetData')).findall(q('row'))[1:];xr=sheet('XD Decisions').find(q('sheetData')).findall(q('row'))[1:]
 check('xlsx_feature_correspondence',len(fr)==2941 and all(vals(r)[0] in tf and tuple(vals(r)[2:4])==tf[vals(r)[0]] for r in fr))
 check('xlsx_source_criterion_preservation',len(mr)==2941 and all(vals(r)[1]==sf[vals(r)[0]][1] for r in mr))
 check('xlsx_all_xd_current',len(xr)==28 and all(vals(r)[1]=='Accepted and incorporated' for r in xr))
 cr=sheet('NR001 Cross-Path').find(q('sheetData')).findall(q('row'))[1:]
 check('xlsx_current_crosspath_not_pending',len(cr)==28 and all(vals(r)[9]=='Accepted and incorporated' for r in cr))
 def cn(ref):
  n=0
  for ch in re.match('[A-Z]+',ref).group():n=n*26+ord(ch)-64
  return n
 check('xlsx_cell_order',all([cn(c.get('r')) for c in row]==sorted(cn(c.get('r')) for c in row) for sh in sheets for row in sheet(sh.get('name')).find(q('sheetData')).findall(q('row'))))
# Full textual scan is supplemental to semantic review, not a proof of business correctness.
status_subjects=[r['id'] for r in features if re.search('unverified',r['name']+' '+r['description'],re.I)]
summary={'checks':checks,'all_automated_checks_pass':all(x=='PASS' for x in checks.values()),'before':dict(ct1=sum(r['level']==1 for r in caps),ct2=sum(r['level']==2 for r in caps),ct3=586,features=2941),'after':dict(ct1=24,ct2=145,ct3=586,features=2941),'population_by_hierarchy':{letter:{'ct3':sum(r['level']==3 and r['id'].startswith(letter) for r in caps),'features':sum(r['id'].startswith(letter) for r in features)} for letter in ('B','E')},'canonical_descriptions_changed_beyond_status_removal':sum(r['description']!=re.sub(r'\s*\*PROPOSED(?: — unverified)?\*\.?','',r['original_description']).strip() for r in features),'ct3_descriptions_changed':sum(r['level']==3 and r['description']!=r['original_description'] for r in caps),'feature_names_changed':sum(r['name']!=r['original_name'] for r in features),'capability_status_names_cleaned':sum(r['name']!=r['original_name'] and '[NEW' in r['original_name'] for r in caps),'source_measurements':sum(bool(r['measurement_criterion']) for r in features),'substantive_unverified_subjects_preserved':status_subjects,'sr_qualifications':'See sr-correspondence-qualifications.json; accepted SR decisions not reopened or represented as newly incorporated','limits':['Automated structure/text/OOXML checks do not prove every business interpretation or implementation conformance.','No interactive spreadsheet application inspection performed.','Whole-candidate Solution Architect acceptance and Continuum incorporation are outside scope.','Task issuance timestamps remain unresolved; iteration evidence check reports them as such.']}
(OUT/'validation.json').write_text(json.dumps(summary,indent=2,ensure_ascii=False)+'\n')
print(json.dumps(summary,indent=2,ensure_ascii=False))
raise SystemExit(0 if summary['all_automated_checks_pass'] else 1)
