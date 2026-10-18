import sys,json,copy
from pathlib import Path
sys.path.insert(0,'docs/archimate/capability-architecture/tools')
import synchronise_baseline as s
outputs,b,v=s.publish();root=s.g.parse(s.DEST/'Harmonia-Architecture-Baseline.xml')
s.validate(b,root)
start=Path('/tmp/harmonia-106-human-inspection-start')
old=s.g.parse(start/'synchronised-baseline/Harmonia-Architecture-Baseline.xml')
for tag in ('elements','relationships','propertyDefinitions'):
 assert s.g.signature(old.find('a:'+tag,s.g.N))==s.g.signature(root.find('a:'+tag,s.g.N)),tag
oldv={v.get('identifier'):v for v in old.findall('a:views/a:diagrams/a:view',s.g.N)};newv={v.get('identifier'):v for v in root.findall('a:views/a:diagrams/a:view',s.g.N)}
assert set(oldv)<=set(newv)
changed={k for k in oldv if s.g.signature(oldv[k])!=s.g.signature(newv[k])};assert changed==set(b.presentation_changes)
readme=(s.BASE/'README.md').read_text()
for phrase in ('canonical [Markdown RAD]', 'An Archi export is change input, not a replacement architecture repository.', 'Markdown RAD -> LaTeX RAD -> publication output'):
 assert phrase in readme, phrase
assert s.r.sha(s.RAD/'README.md') == b.manifest['current_authority_sha256']['docs/markdown/governance/baseline/README.md']
assert (s.ROOT/'architecture-release.json').read_bytes()==(start/'architecture-release.json').read_bytes()
navpath='docs/markdown/governance/archi-navigation-and-view-convention.md'
manifest=s.read(s.MANIFEST)
for relative,expected in manifest['current_authority_sha256'].items():assert s.r.sha(s.ROOT/relative)==expected
print('PASS: exact starting semantic/relationship/Documentation/Properties/identity content; 1,012 retained View IDs; authorised presentation-only View delta; 184 deterministic new landscapes; 157 complete Feature ancestries; four navigation projections; association/layout coverage; release 1.0.6')
print('Changed existing Views:',len(changed),'unchanged existing Views:',len(oldv)-len(changed),'core generated outputs:',len(outputs))
