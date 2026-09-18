---
sessionId: session-260916-100843-138d
---

# Requirements

### Overview & Goals
Clarify how past plans are recalled and detailed across sessions, ensuring the most reliable and efficient use of persisted plan documentation in `.junie/plans/`.

### Scope
#### In Scope
- Explaining session boundary constraints versus persistent disk storage.
- Defining the workflow for querying `.junie/plans/` to reconstruct past plan specifications without loss of accuracy.

#### Out of Scope
- Modifying application source code.
- Deleting or altering existing `.junie/plans/*.md` files without user instruction.

### Functional Clarification
- Each session starts with a clean working context; long-term memory between disconnected sessions relies on persistent files stored in the repository.
- To retrieve and detail any previous plan, the agent must inspect `.junie/plans/` to read the exact Markdown source of truth.

# Technical Design

### Current Implementation
- Past planning artifacts are written to `.junie/plans/<plan-name>.md`.
- Attached files such as `.junie/plans/ergon-praxis-workflow-engine.md` contain full requirement specifications, technical design sections, and delivery milestones.

### Key Decisions
- **Persistent Disk Storage as Ground Truth**: Rely exclusively on reading `.junie/plans/` files to detail past work rather than relying on ungrounded conversational assumptions.
  - *Rationale*: Maximizes utility, eliminates hallucinations, and ensures exact traceability with prior architectural decisions.

### Retrieval Workflow
1. Use glob or file search tools to list files in `.junie/plans/`.
2. Inspect target `.md` files to extract specific requirements, architecture designs, or delivery steps.
3. Present detailed findings or use historical context to guide subsequent changes.

# Delivery Steps

###   Step 1: Verify and inspect existing plans in .junie/plans directory
Ensure all past plan artifacts are identified and indexed for accurate historical context.

- Scan `.junie/plans/` for all stored plan Markdown files (e.g., `ergon-praxis-workflow-engine.md`).
- Read metadata and frontmatter headers (such as `sessionId`) to identify relevant contexts.

###   Step 2: Detail historical plan specifications for user tasks
Extract and present detailed historical specifications from the targeted plan files.

- Parse requirements, technical designs, and delivery stages from the target plan documents.
- Provide comprehensive answers or execute follow-up tasks based on verified file contents.