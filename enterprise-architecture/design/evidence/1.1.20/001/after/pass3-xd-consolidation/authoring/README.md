# Evidence-local authoring and validation

These files retain the actual transformation rules, explicit domain outcomes and read-only result checks used for this bounded offline activity. They are supporting evidence, not adopted repository tooling, a metamodel extension, architectural authority or a new required workflow. Source and resulting content remain independently inspectable in the accompanying reconciliation registers.

From the repository root, `python3 <this-directory>/build_catalogue.py`, followed by `build_workbook.py`, reproduces the evidence-local candidate and workbook from the retained before inputs. These commands overwrite only this activity’s working outputs. `verify_result.py` reads those results and emits `../validation.json`; it changes no architectural sources. Do not use any of these scripts to publish or allocate a release.

The other Python files are included mapping fragments used by `build_catalogue.py`; they are not independent CLIs. The exact accepted Revision 2 text and retained Solution Architect Task/disposition control over generated rules. The automated checks support review; they do not supply architectural acceptance or implementation assurance.
