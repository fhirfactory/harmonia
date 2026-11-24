# Archi 5.10 native-tree inspection aid

This inventory describes the actual saved native model, not a generated imitation of the Archi tree. Agent structural checks do not replace Solution Architect visual inspection.

- Exact candidate SHA-256: `b3c1bd71497497eb64e39e754dc69443d8012c762ffa00b9221116f12f3578a1`.
- Native save SHA-256 (uncompressed): `5759b90091dbe7cc29dd16003f55691822ae16d703e56aaa1aca953c2b833c45`.
- [Native model](archi-transport/imported-model.archimate.gz), [returned Exchange](archi-transport/returned-model.xml.gz), [execution log](archi-transport/execution.txt), [attributed invocation](archi-transport/invocation.json).

| Native category | Type directory beneath Catalogue | Elements |
| --- | --- | --- |
| Application | ApplicationCollaboration | 1 |
| Application | ApplicationComponent | 24 |
| Application | ApplicationFunction | 2 |
| Application | ApplicationInterface | 2 |
| Application | ApplicationService | 5 |
| Application | DataObject | 82 |
| Business | BusinessCollaboration | 7 |
| Business | BusinessFunction | 160 |
| Business | BusinessInteraction | 69 |
| Business | BusinessObject | 13 |
| Business | BusinessProcess | 21 |
| Business | BusinessRole | 34 |
| Business | BusinessService | 142 |
| Business | Product | 4 |
| Motivation | Assessment | 5 |
| Motivation | Constraint | 70 |
| Motivation | Driver | 10 |
| Motivation | Goal | 11 |
| Motivation | Outcome | 3 |
| Motivation | Principle | 79 |
| Motivation | Requirement | 105 |
| Motivation | Stakeholder | 10 |
| Other | Grouping | 92 |
| Strategy | Capability | 842 |
| Strategy | CourseOfAction | 42 |
| Strategy | Resource | 3 |
| Strategy | ValueStream | 4 |

Technology & Physical and Implementation & Migration have Catalogue directories with no represented native types. No synthetic architecture is added to populate them.

Relations contains one Catalogue with **681 source folders** and **1,882 direct relationship entries**, without relationship-type subdirectories. Each source folder is labelled with governed displayName and full canonicalId. All specialised Groupings remain under Other / Catalogue / Grouping; all Requirement specialisations remain under Motivation / Catalogue / Requirement.

All **1,216 Views** and their original folder paths/labels and visual records are preserved. Inspect representative Motivation, Strategy, Business and Application Views in the actual workbench. Existing Views were not developed or reorganised.

Transport omits redundant element/relationship entry labels and generated folder IDs, and adds English language tags. Native element names and folder structure were checked directly; catalogue regeneration reconstructs the exact candidate organisation. [Raw differences](archi-transport/raw-comparison.json.gz) and [normalised review](archi-transport/normalised-review.json) remain retained. No whole-model raw XML equality is asserted.
