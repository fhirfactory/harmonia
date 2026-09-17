# Harmonia HIE Target-State Capability Baseline

## Purpose
This OpenSpec baseline translates the HIE Target State use-case catalogue into discrete, implementation-oriented behavioural capability specifications for Harmonia.

## Source
`HIE - Target State - Use Cases - v0.5.docx` (latest supplied/retrieved revision)

## Interpretation rules
- Treat each `spec.md` as a discrete capability contract traceable to one source use case.
- Preserve source business intent, actors, preconditions, exceptions, data considerations and technology-independence boundaries.
- Requirements describe observable behaviour; implementation mechanics belong in design/tasks artifacts.
- Where the source use case is generic or underspecified, do not invent domain rules. Raise the gap for clarification before implementation.
- Cross-use-case duplication is expected in this baseline. During implementation, Junie may identify shared services or reusable components, but SHALL NOT merge away distinct behavioural requirements without an explicit architecture decision.
