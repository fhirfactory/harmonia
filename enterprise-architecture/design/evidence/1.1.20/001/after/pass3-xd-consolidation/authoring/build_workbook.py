# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Evidence-local OOXML update; no external spreadsheet dependencies."""
from pathlib import Path
import zipfile,xml.etree.ElementTree as E,json,re,posixpath
ROOT=Path('enterprise-architecture/design/evidence/1.1.20/001')
OUT=ROOT/'after/pass3-xd-consolidation'
SOURCE=ROOT/'before/Harmonia_1.1.20_Pass3_011_NR001_Adjudication_Workbook.xlsx'
TARGET=OUT/'Harmonia_1.1.20_Pass3_012_XD_Adjudication_Workbook.xlsx'
S='http://schemas.openxmlformats.org/spreadsheetml/2006/main';R='http://schemas.openxmlformats.org/officeDocument/2006/relationships';P='http://schemas.openxmlformats.org/package/2006/relationships';T='http://schemas.openxmlformats.org/package/2006/content-types'
E.register_namespace('x',S);E.register_namespace('r',R)
def q(t):return '{'+S+'}'+t
def bytes_(r):return E.tostring(r,encoding='utf-8',xml_declaration=True)
def col(n):
 s=''
 while n:n,a=divmod(n-1,26);s=chr(65+a)+s
 return s
def cell(row,n,value):
 ref=f'{col(n)}{row.get("r")}';old=next((x for x in row if x.get('r')==ref),None)
 if old is not None:row.remove(old)
 c=E.SubElement(row,q('c'),r=ref,t='inlineStr');i=E.SubElement(c,q('is'));t=E.SubElement(i,q('t'));t.text=str(value)
 if str(value).strip()!=str(value):t.set('{http://www.w3.org/XML/1998/namespace}space','preserve')
 return c
def value(c):
 return ''.join(t.text or '' for t in c.iter() if t.tag in (q('t'),q('v')))
def rows_of(root):return root.find(q('sheetData')).findall(q('row'))
def sheet(rows):
 r=E.Element(q('worksheet'));views=E.SubElement(r,q('sheetViews'));v=E.SubElement(views,q('sheetView'),workbookViewId='0');E.SubElement(v,q('pane'),ySplit='1',topLeftCell='A2',activePane='bottomLeft',state='frozen')
 columns=E.SubElement(r,q('cols'));E.SubElement(columns,q('col'),min='1',max='1',width='25',customWidth='1');E.SubElement(columns,q('col'),min='2',max=str(max(len(x) for x in rows)),width='65',customWidth='1')
 sd=E.SubElement(r,q('sheetData'))
 for n,vals in enumerate(rows,1):
  row=E.SubElement(sd,q('row'),r=str(n))
  for k,val in enumerate(vals,1):cell(row,k,val)
 E.SubElement(r,q('autoFilter'),ref=f'A1:{col(max(len(x) for x in rows))}{len(rows)}')
 return r
with zipfile.ZipFile(SOURCE) as z:data={n:z.read(n) for n in z.namelist()}
wb=E.fromstring(data['xl/workbook.xml']);rels=E.fromstring(data['xl/_rels/workbook.xml.rels']);types=E.fromstring(data['[Content_Types].xml']);sheets=wb.find(q('sheets'))
relmap={x.get('Id'):x.get('Target') for x in rels}
def path_of(s):
 p=relmap[s.get('{'+R+'}id')]
 return p.lstrip('/') if p.startswith('/') else posixpath.normpath('xl/'+p)
lookup={s.get('name'):s for s in sheets}
def edit(name):return E.fromstring(data[path_of(lookup[name])])
def save(name,root):data[path_of(lookup[name])]=bytes_(root)
nextid=max(int(s.get('sheetId')) for s in sheets)+1
added=[]
def append(name,content):
 global nextid
 n=nextid;nextid+=1;relid=f'rIdPass3Consolidation{n}';path=f'xl/worksheets/sheet{n}.xml'
 assert name not in {s.get('name') for s in sheets} and len(name)<=31
 s=E.SubElement(sheets,q('sheet'),name=name,sheetId=str(n));s.set('{'+R+'}id',relid)
 E.SubElement(rels,'{'+P+'}Relationship',Id=relid,Type=R+'/worksheet',Target=f'worksheets/sheet{n}.xml')
 E.SubElement(types,'{'+T+'}Override',PartName='/'+path,ContentType='application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml')
 data[path]=bytes_(content) if isinstance(content,E.Element) else content
 added.append(name)
# Preserve historical sheets exactly and make standing visible in their names.
for old,new in {'Overview':'History Overview','Wording Flags':'History Wording Flags','Draft Rewrites':'History Draft Rewrites','NR001 Method':'History NR001 Method','XD Decisions':'History XD Decisions','NR001 Cross-Path':'History NR001 Cross-Path','NR001 Screening Queue':'History NR001 Screening Queue','XD002 Merge Map':'History XD002 Merge Map'}.items():lookup[old].set('name',new)
features=json.loads((OUT/'feature-reconciliation.json').read_text());caps=json.loads((OUT/'capability-reconciliation.json').read_text());xd=json.loads((OUT/'xd-incorporation.json').read_text());byxd={x['id']:x for x in xd}
append('Overview',sheet([
['Harmonia 1.1.20 / Pass3 012','Offline XD consolidation; Solution Architect result review pending'],
['Baseline','Pass3/011 exact retained catalogue and workbook; hashes in source-result-manifest.json'],
['Hierarchy','24 CT1 / 145 CT2 / 586 CT3 / 2941 Features; unchanged identities and containment'],
['XD-001–XD-028','Accepted and incorporated into offline candidate; exact decision and result in XD Decisions'],
['XD-027 placement','SA explicitly authorised additive refinement of E06.02.03.F03; no new Feature'],
['AR-001','Canonical outcomes separated from original source criteria; measurement definitions remain evidence, not assertions of successful execution'],
['AR-002','Source status annotations separated; implementation verification not established'],
['Historical sheets','History-prefixed sheets are preserved input snapshots; superseded screening proposals are not active decisions'],
['SR Decisions','Original accepted register preserved unchanged. Pre-existing catalogue correspondence discrepancies are identified in Reconciliation Exceptions; SR follow-up remains outside this task'],
['FR-001–FR-009','Unresolved and outside scope; Feature Duplicates sheet retained unchanged'],
['E07.03.05 / FC-042,059,101','Separate semantic review / deferred, open and parked respectively; no closure inferred'],
['Measurement Criteria','One row per active Feature; verbatim original plus treatment and current assurance condition'],
['No publication','No Continuum or accepted release mutation, release allocation, commit or push'],
['Traceability','Feature Definitions, Capability Definitions, Measurement Criteria, Retirement Trace, XD Decisions and source files support review'],
]))
append('XD Decisions',sheet([['XD','Accepted disposition and incorporation status','Subject','Identifiers','Actual incorporation','Full Revision 2 decision']] + [[r['id'],r['status'],r['title'],', '.join(r['elements']),r['incorporation'],r['accepted_specification']] for r in xd]))
# Current cross-path register retains original reason/proposal in historical-only columns.
r=edit('NR001 Cross-Path')
for row in rows_of(r):
 if row.get('r')=='1':
  cell(row,3,'Historical screening assessment');cell(row,9,'Historical screening proposal (superseded where conflicting)');cell(row,10,'Accepted Revision 2 standing');cell(row,11,'Actual offline incorporation');continue
 vals={c.get('r'):value(c) for c in row};id_=next((v for v in vals.values() if re.fullmatch(r'XD-\d{3}',v)),None)
 if id_ in byxd:
  x=byxd[id_];cell(row,10,x['status']);cell(row,11,x['incorporation'])
append('NR001 Cross-Path',r)
r=edit('NR001 Screening Queue')
for row in rows_of(r):
 vals=[value(c) for c in row];id_=next((v for v in vals if re.fullmatch(r'XD-\d{3}',v)),None)
 if id_ in byxd:cell(row,6,byxd[id_]['status']);cell(row,8,'Accepted Revision 2; incorporated offline. Earlier lexical screen retained as history.')
append('NR001 Screening Queue',r)
r=edit('XD002 Merge Map')
for row in rows_of(r):
 vals=[value(c) for c in row];ident=vals[0] if vals else ''
 if ident=='B04.03.03.F01':cell(row,4,'B04.03.04.F04');cell(row,5,'Retired identity; time-bounded acknowledgement and missing/late escalation incorporated under XD-018.');cell(row,6,'ACCEPTED AND INCORPORATED')
 elif ident in ('B04.03.03.F02','B04.03.03.F05'):cell(row,4,'B04.03.04.F01; B04.03.04.F05');cell(row,5,'Retired identity; accountable intended recipient, sufficient identity assurance, intended/actual delivery evidence incorporated under XD-018.');cell(row,6,'ACCEPTED AND INCORPORATED')
 elif ident=='B04.03.03.F06':cell(row,5,'Retired identity; retained F04 covers missing/late acknowledgement escalation; F05 retains evidence and F02/F03 distinguish clinical action/follow-up.');cell(row,6,'ACCEPTED AND INCORPORATED')
 elif ident=='B04.03.04.F01–F05':cell(row,3,'IDENTITIES RETAINED; XD-018 WORDING REFINED');cell(row,5,'F01/F04/F05 refined; F02/F03 preserve receipt/clinical action distinction and follow-up. No renumbering.')
 elif ident=='B04.03.03.F03':cell(row,5,'Classification responsibility and original criterion retained at target F06; AR-001 separates measurement and definition.')
 elif ident=='B04.03.03.F04':cell(row,5,'Rapid appropriate notification and original timing criterion retained at target F07; wording refined under XD-018/AR-001.')
append('XD002 Merge Map',r)
append('Feature Definitions',sheet([['Feature ID','Parent CT3','Name','Canonical definition','Authoring treatment','Technical correspondence']] + [[x['id'],x['parent'],x['name'],x['description'],x['normalisation'],x['technical_correspondence']] for x in features]))
append('Capability Definitions',sheet([['Capability ID','Level','Name','Definition','Original name','Original definition']] + [[x['id'],x['level'],x['name'],x['description'],x['original_name'],x['original_description']] for x in caps]))
append('Measurement Criteria',sheet([['Feature ID','Verbatim original description and status','Original source measurement','Criterion treatment','Current assurance condition','Source status','Implementation verification']] + [[x['id'],x['original_description'],x['measurement_criterion'],x['criterion_treatment'],x['current_assurance_condition'],x['original_status'],x['implementation_verification']] for x in features]))
retired=[['E01.01.05.F02','E01.01.01.F02','XD-001 duplicate retirement'],['E03.05.04.F02','E03.05.03.F02','XD-005 duplicate retirement'],['E07.03.04.F03','E07.03.01.F04','XD-007 duplicate retirement'],['B04.03.03','B04.03.04','XD-002 CT3 consolidation']]
for n in range(1,7):
 target={1:'B04.03.04.F04',2:'B04.03.04.F01; B04.03.04.F05',3:'B04.03.04.F06',4:'B04.03.04.F07',5:'B04.03.04.F01; B04.03.04.F05',6:'B04.03.04.F04; B04.03.04.F05'}[n]
 retired.append([f'B04.03.03.F{n:02}',target,'XD-002 transfer' if n in (3,4) else 'XD-002 retired separate identity; retained survivor coverage refined by XD-018'])
prior_path=ROOT/'before/Harmonia_1.1.20_Candidate_017_Pass3_002_Adjudicated_Overlay.md'
prior_text=prior_path.read_text();prior_features={i:(n,d) for i,n,d in re.findall(r'^- \*\*`([^`]+)` — (.*?):\*\* (.*)$',prior_text,re.M)}
trace=[]
for ident,target,decision in retired:
 name,criterion=prior_features.get(ident,('Critical Finding Communication' if ident=='B04.03.03' else '', 'CT3 context; original Feature criteria are in the individual Feature rows.'))
 trace.append([ident,target,decision,name,criterion,str(prior_path)])
append('Retirement Trace',sheet([['Inactive source identifier','Retained identity / coverage (not equivalence assertion)','Accepted decision','Original source name','Original source criterion','Retained source locator']] + trace))
(OUT/'retirement-trace.json').write_text(json.dumps(trace,ensure_ascii=False,indent=2)+'\n')
# Pre-existing SR discrepancies are preserved rather than silently incorporated or reopened.
exceptions=[
['SR-001','Accepted register says consolidate B05.03.04 into B05.03.01; both remain active in supplied Pass3/011.','Existing baseline discrepancy; no unreviewed Feature retirement or automatic merge. Accepted decision preserved verbatim; separate SR follow-up excluded by Revision 2 §6.'],
['SR-004','Accepted register says rename E06.03.01 to Governed Parcel Handoff & Accountability; baseline still says Governed Parcel Routing.','Identity and original baseline retained; accepted rename not silently represented as performed. Separate SR follow-up; no reopening of decision.'],
['SR-015','Accepted register describes E06.08.07 as Ergon output processing; baseline names it Published Work Recovery & Reconciliation. E06.09.07 remains Deferred Work Recovery & Reconciliation.','Semantic correspondence unestablished; preserve both active definitions and accepted register. Do not repurpose either under XD.'],
['SR-016','Accepted register frames B13.04.02 as research de-identification/minimisation; baseline is Research Participant Safeguarding, whose F01 concerns consent/safeguarding.','Preserve research/participant protection distinction and accepted register; no replacement of baseline obligations by narrower inferred correspondence.'],
['SR-017','Register narrative associates the first and second IDs with client/server opposite to baseline titles: E03.06.04 Server; E03.06.05 Client.','Both retained unchanged as accepted; narrative correspondence qualification only, no client/server responsibility swap.'],
['Temporal provenance','Original Task and follow-up instruction-entry timestamps are unavailable in this interaction.','Explicitly unresolved for SA treatment before HCRL closure; no fabricated timestamp or filename prefix.'],
]
append('Reconciliation Exceptions',sheet([['Reference','Observed correspondence issue','Treatment / boundary']] + exceptions))
# Current overview and review sheets precede retained source/history sheets.
order=['Overview','XD Decisions','NR001 Cross-Path','XD002 Merge Map','Feature Definitions','Capability Definitions','Measurement Criteria','Retirement Trace','Reconciliation Exceptions','SR Decisions','Feature Duplicates','NR001 Screening Queue']
ordered=sorted(list(sheets),key=lambda s:order.index(s.get('name')) if s.get('name') in order else len(order)+list(sheets).index(s))
sheets[:]=ordered
# Wrap new review cells and use the original workbook's fonts and colours.
styles=E.fromstring(data['xl/styles.xml']);xfs=styles.find(q('cellXfs'));normal_style=len(xfs)
xf=E.SubElement(xfs,q('xf'),numFmtId='0',fontId='0',fillId='0',borderId='0',xfId='0',applyAlignment='1');E.SubElement(xf,q('alignment'),vertical='top',wrapText='1')
header_style=len(xfs);xf=E.SubElement(xfs,q('xf'),numFmtId='0',fontId='1',fillId='2',borderId='0',xfId='0',applyAlignment='1');E.SubElement(xf,q('alignment'),vertical='top',wrapText='1');xfs.set('count',str(len(xfs)));data['xl/styles.xml']=bytes_(styles)
def col_number(ref):
 n=0
 for ch in re.match(r'[A-Z]+',ref).group():n=n*26+ord(ch)-64
 return n
for name in added:
 sh=next(s for s in sheets if s.get('name')==name);rid=sh.get('{'+R+'}id');target=next(r.get('Target') for r in rels if r.get('Id')==rid);path='xl/'+target
 root=E.fromstring(data[path])
 for row in rows_of(root):
  row[:]=sorted(list(row),key=lambda c:col_number(c.get('r')))
  row.set('ht','32' if row.get('r')=='1' else '72');row.set('customHeight','1')
  for c in row:c.set('s',str(header_style if row.get('r')=='1' else normal_style))
 data[path]=bytes_(root)

# Keep old style/shared-string assets; all new cells use inline strings.
data['xl/workbook.xml']=bytes_(wb);data['xl/_rels/workbook.xml.rels']=bytes_(rels);data['[Content_Types].xml']=bytes_(types)
# Spreadsheet viewers tolerate absent document properties; remove stale sheet counts only in derived app metadata.
if 'docProps/app.xml' in data:
 app=E.fromstring(data['docProps/app.xml']);vt='{http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes}';ap='{http://schemas.openxmlformats.org/officeDocument/2006/extended-properties}'
 titles=app.find(ap+'TitlesOfParts')
 if titles is not None:
  titles.clear();v=E.SubElement(titles,vt+'vector',size=str(len(sheets)),baseType='lpstr')
  for s in sheets:E.SubElement(v,vt+'lpstr').text=s.get('name')
 pairs=app.find(ap+'HeadingPairs')
 if pairs is not None:
  for x in pairs.iter(vt+'i4'):x.text=str(len(sheets))
 data['docProps/app.xml']=bytes_(app)
with zipfile.ZipFile(TARGET,'w',zipfile.ZIP_DEFLATED) as z:
 for name,content in data.items():z.writestr(name,content)
manifest_path=OUT/'source-result-manifest.json';manifest=json.loads(manifest_path.read_text());manifest['result_workbook']=str(TARGET);manifest['result_workbook_sha256']=__import__('hashlib').sha256(TARGET.read_bytes()).hexdigest();manifest_path.write_text(json.dumps(manifest,indent=2)+'\n')
print(TARGET, len(sheets),'sheets',len(features),'active Feature rows')
(OUT/'sr-correspondence-qualifications.json').write_text(json.dumps(exceptions,indent=2,ensure_ascii=False)+'\n')
