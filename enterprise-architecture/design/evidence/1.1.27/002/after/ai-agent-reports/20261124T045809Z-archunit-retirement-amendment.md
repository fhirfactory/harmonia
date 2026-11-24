Report-Produced-At: 2026-11-24T04:58:09.040450Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

# ArchUnit requirement retirement — amendment preparation report

**Task** [Issued amendment preparation](../../before/ai-agent-tasks/unresolved-archunit-retirement-amendment.md)  
**Review** [Architecture 1.1.27 / 002](../../../../../review/1.1.27/hcrl-1.1.27-002.pending.md)

Prepared the [exact proposed amendment](../proposed-archunit-retirement.patch)
against accepted Architecture 1.1.26, source checkpoint
`718fc278be5b89b6138d4341feddefce1269eaa6`. The proposal preserves the architectural
invariants and prospective test guidance while making verification proportionate
under the common Praxis. It removes the old executable mandate, and labels
the legacy build and operations entry points as historical. No replacement
tests are supplied by this amendment.

`git apply --check` passed for the five-file proposal. The source policy and
documentation files remain as at the checkpoint. The proposed resulting
release is 1.1.27, with its distinct review iteration 002 preserving the earlier
cancelled 001. The release authority remains 1.1.26; allocation, application and
publication remain subject to their separate governance decisions.

The linked [software reset review](../../../../../review/software-0.1.0/hcrl-software-0.1.0-009.pending.md)
records the empty reactor verification and the canonical validator's pre-existing
retained-input failure. The attributable instruction-entry time is **UNRESOLVED**.
