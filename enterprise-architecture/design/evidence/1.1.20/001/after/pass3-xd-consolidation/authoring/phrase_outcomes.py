# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
PHRASES.update(dict(line.split('|',1) for line in '''
escalation of access delays|Access delays are escalated
consent-appropriate carer input|Carer input respects the applicable consent conditions
further intervention trigger|Conditions requiring further intervention can trigger the appropriate response
remediation of invalid investigation|Invalid investigations can be remediated
 timely contribution requests|Care contribution requests reach participants in time for their required contribution
contingency for transfer delay|Care transfer delays have an applicable contingency arrangement
handover across organisation|Care handovers preserve the required context across organisational boundaries
responsibility across boundaries|Care responsibility remains identifiable across provider boundaries
agreed medication list|The clinically agreed medication list is available
 equipment adjustment support|Equipment adjustments can be supported
support replacement decisions|Changes of assistive support have an explicit replacement decision
person-defined psychosocial concern|People can express their own psychosocial concerns
risk and protection factors|Relevant psychosocial risk and protection factors can be identified
network access equity|Provider network access can be assessed for equity
readiness reverification|Service readiness can be verified again when relevant conditions change
continuity of duties|Workforce reassignment preserves the continuity of required duties
expired eligibility alert|Expired professional eligibility produces an alert
credential audit trail|Credential evidence retains an audit trail
changed privilege effective date|Privilege changes identify their applicable effective date
agreed development goal|Professional development goals are agreed
practice change learning|Learning relevant to practice change can be applied
protected supervision time|Required supervision time remains protected
concern reporting safety|Practice concerns can be reported safely
forecast supply shortage|Anticipated therapeutic supply shortages can be identified
specimen integrity during transit|Specimens retain their required integrity during transit
continuity of essential service|Essential services can continue under the applicable contingency conditions
correction of record omissions|Clinical record omissions can be corrected
continuity across encounters|Clinical record context is preserved across encounters
identifiable information origin|The origin of business information is identifiable
evidence-based improvement lesson|Incident improvement lessons have an evidence basis
authoritative practice standard|The applicable authoritative practice standard is identifiable
representative practice sampling|Practice reviews can use representative samples
respondent privacy|Service experience respondents retain the applicable privacy protections
resource-constrained priorities|Investment priorities account for resource constraints
accurate financial statement|Financial statements accurately represent the relevant financial position
scenario-based readiness test|Readiness can be tested against the applicable service scenarios
rapid additional capacity|Additional surge capacity can be made available promptly
surge staff competence|Staff supporting surge response have the required competence
timely surveillance summary|Population surveillance summaries are available within the applicable timing conditions
programme reach planning|Prevention programmes plan their intended population reach
community engagement reach|Community engagement reaches the intended population groups
intervention reach measurement|The population reach of preventive interventions can be measured
reproducible study protocol|Study protocols support reproducible research
research data quality|Research data meets the applicable quality requirements
feedback to evidence owner|Practice learning feedback reaches the responsible evidence owner
participant-reported progress|Participants can report progress against their goals
environment change recommendation|Participation assessments can recommend relevant environmental changes
participant-reported value|Participants can report the value of supported participation
measurable service expectations|Commissioned service expectations are defined so their fulfilment can be assessed
provider accountability terms|Provider accountability terms are explicit
outcome review schedule|Commissioned outcomes have an established review schedule
market failure risk|Provider market failure risks can be identified
coverage change effective dating|Coverage changes identify their effective period
exceptional condition treatment|Exceptional coverage conditions receive an explicit treatment
purchasing decision audit|Service purchasing decisions can be audited
contract performance trend|Provider contract performance trends can be identified
service continuity on exit|Commissioning exit preserves the applicable service continuity obligations
equity impact of funding|The equity impact of funding can be assessed
evidence-based adjustment case|Funding adjustments have an evidence-supported case
archive retrieval latency|Archive retrieval completes within the applicable timing conditions
stable reference semantics|Information reference semantics remain stable
resolvable archived target|Archived reference targets remain resolvable
discoverability of relevant sources|Relevant information sources can be discovered
stable result references|Search result references remain stable
continuity under node failure|Governed information access continues or receives an explicit degraded disposition after node failure
contract conformant payloads|Clinical exchange payloads conform to the applicable contract
durable accepted exchange|Accepted inbound exchange content remains durable
validated incoming contract|Inbound exchange contracts are validated
service endpoint trust|External service endpoints meet the applicable trust conditions
timeout and retry discipline|Protocol timeouts and retries respect the applicable exchange rules
precise rejection cause|Exchange rejections identify their precise cause
time-limited uncertainty|Exchange uncertainty is bounded by the applicable time conditions
stable value set reference|Value-set references retain stable identity and meaning
conflicting semantic assumptions|Conflicting semantic assumptions can be identified
encrypted exchange channel|Information exchange channels provide the required encryption
sensitive-data-minimised security events|Security events minimise sensitive data while retaining the required evidence
bounded retry policy|Delivery retries follow the applicable bounded retry policy
durable task checkpoint|Required task checkpoints remain durable
alternative interaction method|Alternative interaction methods are available where required
context preservation between views|Relevant permitted context is preserved when moving between views
derived versus original distinction|Derived and original information remain distinguishable
structured operational event|Operational events retain their required structure
qualified finding severity|Assurance findings have their severity appropriately qualified
timely fault signal|Fault signals are available within the applicable timing conditions
result-set identity stability|Result-set identity remains stable
reusable selection parameters|Search selection parameters can be reused
decision-relevant business measure|Business reports provide measures relevant to the intended decision
no autonomous authority|Automated recommendations do not confer autonomous decision authority
non-disclosure of protected detail|Protected details are not disclosed
revoked privilege enforcement|Revoked clinical privileges cannot be exercised as current authority
rejected change preservation|Rejected changes leave previously governed information intact
'''.strip().splitlines()))
PHRASES={k.strip():v for k,v in PHRASES.items()}
prior_nominal=de_nominal
def de_nominal(s):return PHRASES.get(s.lower(),prior_nominal(s))

PHRASES['proportionate risk control']='Risk controls are proportionate to the applicable risk'
