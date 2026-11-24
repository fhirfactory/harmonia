# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
import re,json,collections
from pathlib import Path
BASE=Path('enterprise-architecture/design/evidence/1.1.20/001/before')
SRC=BASE/'Harmonia_1.1.20_Candidate_017_Pass3_011_XD009_Working_Overlay.md'
text=SRC.read_text()
FP=re.compile(r'^- \*\*`([^`]+)` — (.*?):\*\* (.*)$',re.M)
def clean(s):
 s=re.sub(r'\s*\*PROPOSED(?: — unverified)?\*\.?','',s)
 s=re.sub(r'\s*\*\*\[NEW C\d+\]\*\*','',s)
 return s.strip()
def sentence(s):return s[0].upper()+s[1:]+('' if s.endswith('.') else '.')
def de_nominal(s):
 # Existing nominal responsibilities rendered as predicates, without adding scope.
 endings={'Recognition':'is recognised','Detection':'is detected','Identification':'is identified','Verification':'is verified','Confirmation':'is confirmed','Assessment':'is assessed','Evaluation':'is evaluated','Review':'is reviewed','Preservation':'is preserved','Protection':'is protected','Retention':'is retained','Availability':'is available','Accessibility':'is accessible','Visibility':'is visible','Traceability':'is traceable','Attribution':'is attributable','Clarity':'is clear','Completeness':'is complete','Integrity':'retains its integrity','Fidelity':'is faithfully preserved','Consistency':'is consistent','Currency':'is current','Validity':'is valid','Suitability':'is suitable','Correctness':'is correct','Reliability':'is reliable','Readiness':'is established','Disclosure':'is disclosed','Communication':'is communicated','Escalation':'is escalated','Reconciliation':'is reconciled','Resolution':'is resolved','Allocation':'is allocated','Matching':'is matched','Ordering':'is ordered','Correlation':'is correlated','Qualification':'is qualified','Refresh':'is refreshed','Capture':'is captured','Definition':'is defined','Control':'is controlled','Enforcement':'is enforced','Handling':'is handled','Support':'is supported','Access':'can be accessed','Evidence':'is evidenced','Outcome':'has an explicit outcome','Continuity':'is maintained','Conformance':'conforms to the applicable requirements','Compliance':'complies with the applicable requirements','Interpretation':'is interpreted','Accuracy':'is accurate','Transparency':'is transparent','Usability':'is usable','Understanding':'can be understood','Authority':'has an established authority basis','Permission':'requires the applicable permission','Scope':'has an explicit scope','Consent':'requires applicable consent','Context':'has its relevant context','Identity':'has an identifiable identity','History':'retains its history','Source':'has an identifiable source','Version':'has an identifiable version','Release':'is released under the applicable conditions','Disposition':'has an explicit disposition','Response':'receives an appropriate response','Update':'is updated','Recovery':'can be recovered','Reassessment':'is reassessed'}
 words=s.split();last=words[-1]
 for end,pred in endings.items():
  if last.lower()==end.lower() and len(words)>1:
   obj=' '.join(words[:-1]);plural=obj.split()[-1].lower().endswith('s') and not obj.lower().endswith(('access','process','status','loss'))
   if plural:pred=re.sub(r'^is\b','are',pred);pred=re.sub(r'^has\b','have',pred);pred=re.sub(r'^retains\b','retain',pred)
   return obj+' '+pred
 return s+' is supported'
patterns=[
(r'proportion of relevant, evidence-backed (.*?) scenarios demonstrating (.*?) against an explicitly defined assessment rubric\.',lambda a,b:de_nominal(b)),
(r'proportion of sampled risk-bearing (.*?) scenarios with correct (.*?), including prescribed adverse cases\.',lambda a,b:de_nominal(b)+', including in adverse conditions'),
(r'proportion of independently checked (.*?) instances satisfying the specified (.*?) quality condition\.',lambda a,b:de_nominal(b)),
(r'proportion of representative (.*?) scenarios in which (.*?) is demonstrably available and usable\.',lambda a,b:(b.rsplit(' ',1)[0]+' is accessible and usable' if b.lower().endswith(' accessibility') else b.rsplit(' ',1)[0]+' is available and usable' if b.lower().endswith(' availability') else de_nominal(b)+' through available and usable support')),
(r'proportion of applicable (.*?) requests or situations with verified (.*?)\.',lambda a,b:de_nominal(b)),
(r'proportion of relevant changes or disruptions in (.*?) for which (.*?) is maintained or correctly recovered\.',lambda a,b:de_nominal(b)+' through relevant changes or disruptions, with recovery where required'),
(r'proportion of applicable (.*?) interactions where (.*?) reaches and is understood by the intended recipient\.',lambda a,b:b+' reaches and can be understood by the intended recipient'),
(r'proportion of sampled (.*?) cases with accessible, accurate evidence of (.*?)\.',lambda a,b:de_nominal(b)+', with accessible and accurate supporting evidence'),
(r'proportion of positive and negative (.*?) test cases meeting the applicable (.*?) criteria\.',lambda a,b:de_nominal(b)+', distinguishing permitted and nonconforming conditions'),
(r'proportion of assessed (.*?) cases with observable (.*?) against an agreed evaluation basis\.',lambda a,b:de_nominal(b)),
(r'elapsed time from the applicable trigger to the (.*?) outcome; proportion within a context-defined interval\.',lambda a:de_nominal(a)+' within the applicable context-defined interval'),
]
def convert(i,n,d):
 d=clean(d)
 if not re.match(r'(proportion|percentage|elapsed time|number|rate|retrieval responsiveness)',d,re.I):return d,'retained-outcome'
 for k,(p,fn) in enumerate(patterns):
  m=re.fullmatch(p,d,re.I)
  if m:return sentence(fn(*m.groups())),f'contextual-responsibility-{k+1}'
 return '', 'needs-review'
exec(Path(__file__).with_name('nominal_predicates.py').read_text())
exec(Path(__file__).with_name('phrase_outcomes.py').read_text())
