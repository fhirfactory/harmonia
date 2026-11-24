<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Common canonical element template

Generate the exact metadata skeleton with
`python3 enterprise-architecture/tooling/architecture_continuum.py template --type <NativeType>`
and the explicit `--specialization <governed-specialisation>` where applicable.
The [type catalogue](type-catalogue.json) supplies the complete native/specialisation
mapping, including the accepted Iteration 001 architectural statement specialisations.
Fill the metadata and body according to the [representation contract](../continuum-representation.md).
The tool's template is the executable form; placeholders are not architecture.

The body uses this structure:

```markdown
# <displayName>

## Architectural content

<Definition and bounded architectural responsibility.>

<Relevant boundaries, exclusions, qualified relationships and assertions.>

<Explicit unresolved matters with their source standing.>

## Source-qualified contribution

<Additional evidence for this same identity, where needed; preserve standing.>

## History

<Material retained historical assertions, where applicable.>
```

Only the heading and Architectural content section are universally required.
Other headings follow the actual element's explanatory needs. Do not copy empty
boilerplate into every element or turn template headings into architectural types.
