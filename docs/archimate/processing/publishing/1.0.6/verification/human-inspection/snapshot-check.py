import sys,json
from pathlib import Path
p=Path('docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools');sys.path.insert(0,str(p.resolve()))
import check,navigation
model=check.read('model.json');elements,aliases,baseline=check.validate_model(model)
check.validate_review(model,elements)
master=p.parent/'Harmonia-EA-Capability-Map.tex'
if not master.exists():
 print('Master candidates:',[x.name for x in p.parent.glob('*.tex')]);raise ValueError('Use actual pinned snapshot master')
result=navigation.validate_source(elements,check.included_tex(master))
print('PASS: read-only frozen snapshot capability structure, identities, aliases, reviewed ancestry and source navigation contracts')
print(json.dumps(result,indent=2))
