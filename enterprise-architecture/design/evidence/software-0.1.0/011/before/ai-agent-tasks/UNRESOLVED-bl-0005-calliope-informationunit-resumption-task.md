<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# AI-Agent Task Details — BL-0005 InformationUnit implementation resumption

**Task-Issued-At:** UNRESOLVED. The attributable system event time at which these operative instructions entered the interaction was not supplied at sufficient precision. No UTC event time or filename prefix is inferred.

**Agent:** Junie  
**Software target:** `software-0.1.0` development candidate; HCRL iteration `011`  
**Originating Task:** [BL-0005 implementation Task](UNRESOLVED-bl-0005-calliope-informationunit-implementation-task.md). This record supplements that Task and does not replace it.  
**Preceding Report:** [Stop Report](../../after/ai-agent-reports/UNRESOLVED-bl-0005-calliope-informationunit-implementation-report.md), preserved unchanged.  
**Source state:** `HEAD` remains commit `a2e68b9f871102c435dca4cc2583ea69d881dfe1` on `feature/BL-0005-Calliope-InformationUnit`.

## Operative Solution Architect dispositions (verbatim)

These were the replies to the questions raised in the stop Report and HCRL 011.

**IU profile.** "The DRP was reviewed and approved to override the ConceptFDN with the InformationConcept name."

**Cache metadata.** "The referenced DRP-Information-Unit-Framework.Draft.002.004.md is the reviewed and approved model baseline. It should override pre-existing spefications."

**Authority reference (first reply).** "The DRP-Information-Unit-Framework.Draft.002.004.md details the InformationQuality attribute - which will explicitly encapsulate the information-authority concepts."

**RAW Topic.** "you asked this previously — qualifier=System,value=Harmonia.qualifier=Domain,value=Data.qualifier=InformationContainer,value=RAW"

**Schema vocabulary.** "derive from FHIR R4 (resource names) and MediaTypes"

**Media format.**

> We explicitly distinguished:
>
> - contentConcept — what the content represents.
> - contentSchema — its structure, schema or profile, including applicable versions.
> - Content format — its concrete encoding or serialisation, such as JSON, XML or PDF.
>
> A media-format identifier is not necessarily a schema identifier.
>
> For example, a FHIR Patient conforming to a particular profile can be serialised as JSON or XML without changing its underlying schema.
>
> Therefore:
>
> Junie should not populate ContentSchema builders with media-format identifiers merely to accommodate formats.
>
> This does not prohibit a schema from being expressed in XML or JSON. It simply preserves the distinction between the schema and the encoding.
>
> No architectural amendment is necessary, and Junie should not invent a new representation field as part of BL-0005.

**Disposition standing.**

> Draft 002.004 does not automatically override G02 or HCRL 1.1.25. However, we explicitly reviewed and adjudicated the differences, and you subsequently accepted the reconciled BL-0005 design.
>
> That acceptance is an attributable Solution Architect disposition.
>
> Junie should therefore:
> - Record the accepted BL-0005 decisions in HCRL software-0.1.0/011.
> - Identify the affected earlier contracts and the specific differences being superseded.
> - Preserve unrelated architectural obligations.
> - Distinguish architectural acceptance from software implementation acceptance.
>   The governing authority is your explicit acceptance of the reconciled design, not the draft document's status alone.
>
> There is no need to request another architectural decision on matters you've already adjudicated.

**Authority member.** "add informationAuthority : Topic directly to InformationMetadata, rather than InformationQuality."

The second authority reply supersedes the first for the authority member.

## Operative instruction (verbatim)

> Proceed with the implementation

## Execution boundaries

- Implement the BL-0005 scope as amended by the dispositions above. Every other boundary of the originating Task still applies.
- Do not commit, push, merge, tag, publish, deploy, allocate a release or declare acceptance.
- Architectural acceptance of the reconciled design is not software implementation acceptance. The resulting candidate is submitted for Solution Architect review.
