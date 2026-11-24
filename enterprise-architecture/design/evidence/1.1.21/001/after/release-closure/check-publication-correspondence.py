# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify published content and destination-relative links against the reviewed candidate.

This publication check does not alter any source, output or transport comparator.
Only known URL carriers and generated digest maps are compared by their actual
resolved targets; all remaining text, metadata and hierarchy must be exact.
"""
import json,sys,re,html
from pathlib import Path
from urllib.parse import urlsplit,unquote
sys.path.insert(0,'enterprise-architecture/tooling')
import architecture_continuum as c
root=Path.cwd();base=root/'enterprise-architecture/design/evidence/1.1.21/001';dest=base/'after/release-closure';source=base/'after/patient-transport-follow-up'
manifest=root/c.PUBLICATION/'1.1.21';rad=root/c.RAD_PUBLICATION/'1.1.21';arch=root/c.ARCHIMATE_PUBLICATION/'1.1.21/Harmonia-Architecture.xml'
counts={'relativeUrlCarriers':0,'readerElementRecords':0,'digestEntries':0}
def url(value,directory):
 u=urlsplit(html.unescape(value))
 if u.scheme or value.startswith('//') or not u.path:return value
 counts['relativeUrlCarriers']+=1
 target=(directory/unquote(u.path)).resolve()
 # Sibling artifact links identify corresponding members of the reviewed and
 # published bundles; repository source links must resolve to the same file.
 label=str(target)
 for folder,kind in [(source/'candidate','manifest'),(manifest,'manifest'),(source/'narrative','rad'),(rad,'rad')]:
  if target.is_relative_to(folder):
   label='publication:'+kind+'/'+str(target.relative_to(folder));break
 return label+('?' + u.query if u.query else '')+('#' + u.fragment if u.fragment else '')
def html_urls(text,directory):
 # JavaScript source may contain a literal dynamic href expression; it is code,
 # not an emitted URL carrier. Preserve every script byte without rewriting it.
 parts=re.split(r'(<script\b[^>]*>.*?</script>)',text,flags=re.S)
 for i in range(0,len(parts),2):
  parts[i]=re.sub(r'href="([^"]*)"',lambda m:'href="'+html.escape(url(m[1],directory),quote=True)+'"',parts[i])
 return ''.join(parts)
def normalized(path):
 text=path.read_text();directory=path.parent.parent if path.suffix=='.tex' and path.parent.name=='parts' else path.parent
 if path.name=='continuum.html':
  pattern=r'(<script type="application/json" id="architecture">)(.*?)(</script>)'
  match=re.search(pattern,text,re.S);assert match
  rows=json.loads(match[2])
  for row in rows:
   row['source']=url(row['source'],directory);row['html']=html_urls(row['html'],directory)
  counts['readerElementRecords']+=len(rows)
  return html_urls(text[:match.start(2)]+'REVIEWED_ARCHITECTURE_DATA'+text[match.end(2):],directory),rows
 if path.suffix=='.tex':
  text=re.sub(r'\\url\{([^{}]*)\}',lambda m:r'\url{'+url(m[1],directory)+'}',text)
  text=re.sub(r'\\href\{\\detokenize\{([^{}]*)\}\}',lambda m:r'\href{\detokenize{'+url(m[1],directory)+'}}',text)
  return re.sub(r'\\href\{([^{}]*)\}',lambda m:r'\href{'+url(m[1],directory)+'}',text)
 if path.suffix=='.md':return re.sub(r'\]\(([^)]+)\)',lambda m:']('+url(m[1],directory)+')',text)
 raise AssertionError('No documented representation change expected: '+str(path))
comparisons={};correspondence=[]
for folder,target in [('candidate',manifest),('narrative',rad)]:
 for p in (source/folder).rglob('*'):
  if not p.is_file():continue
  q=target/p.relative_to(source/folder);assert q.is_file(),str(q)
  outcome='EXACT'
  if p.read_bytes()!=q.read_bytes():
   if p.name=='publication.json':
    a,b=c.read_json(p),c.read_json(q);ha,hb=a.pop('sha256'),b.pop('sha256');assert a==b and set(ha)==set(hb)
    for key in ha:assert c.sha((p.parent/key).read_bytes())==ha[key] and c.sha((q.parent/key).read_bytes())==hb[key]
    counts['digestEntries']+=len(ha);outcome='Generated digest map corresponds to verified destination outputs'
   else:
    assert normalized(p)==normalized(q),str(q);outcome='Only destination-relative URLs differ; all resolve to identical targets'
  comparisons[str(q.relative_to(root))]=c.sha(q.read_bytes())
  correspondence.append({'candidate':str(p.relative_to(root)),'published':str(q.relative_to(root)),'result':outcome})
candidate_sha=c.sha((source/'candidate/Harmonia-Architecture.xml').read_bytes());assert candidate_sha=='6471411187a5ff72b005c9b9855c48beb3d86b7ae6c7c556a4eb58338011a739'
assert arch.read_bytes()==(source/'candidate/Harmonia-Architecture.xml').read_bytes()==(manifest/'Harmonia-Architecture.xml').read_bytes()
comparisons[str(arch.relative_to(root))]=candidate_sha;assert len(comparisons)==19
before=c.read_json(base/'before/release-closure/release-input-state.json')
assert all(c.sha((root/p).read_bytes())==h for p,h in before['protectedAcceptedPublicationRegisterAndReleaseSha256'].items())
state=c.read_json(base/'before/solution-architect-disposition/input-state.json');assert all(c.sha((root/p).read_bytes())==h for p,h in state['sha256'].items())
record={'status':'PASS','publishedRelease':'1.1.21','acceptedPredecessor':'1.1.20','acceptedCandidateSha256':candidate_sha,'publishedXmlIdenticalToAcceptedCandidate':True,'publishedArtifactSha256':comparisons,'candidatePublicationCorrespondence':correspondence,'comparisonChecks':counts,'publishedArtifacts':19,'unchangedAcceptedInputs':len(state['sha256']),'protectedHistoricalPublicationRegisterAndReleaseFilesUnchanged':len(before['protectedAcceptedPublicationRegisterAndReleaseSha256']),'patchAllocation':'Existing 1.1.21 allocation reused; root release bytes unchanged.','humanAcceptance':'Final issued Solution Architect acceptance at 2026-11-10T22:52:33.672Z, exact Task retained before/ai-agent-tasks.','transport':'Strict FAIL accepted with exact bounded qualification; raw classifications/evidence unchanged.','resolvedVerificationFinding':'Preliminary URL diagnostics were corrected to resolve TeX part links from the master publication directory and map sibling artifact correspondence. A preliminary HTML link diagnostic also interpreted a JavaScript dynamic-link string as a URL; the checker now preserves all script bytes exactly. An initial all-artifact byte-equality assertion failed on the catalogue because maintained publishers intentionally rebase relative links for each output directory. This check proves identical resolved URL targets and exact non-URL content; generated digest maps are independently verified. No generated file or maintained comparator was changed.','pdf':'Not compiled; established HTML/Exchange/LaTeX source publications generated and checked.'}
(dest/'publication-integrity.json').write_bytes(c.encoded(record))
print(json.dumps({'status':'PASS','publishedArtifacts':19,'unchangedAcceptedInputs':len(state['sha256']),'sha256':candidate_sha,'checks':counts},indent=2))
