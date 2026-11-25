<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Calliope Naming

This document describes the supported software contract implemented under the
accepted [Backlog Item 0003 revision 000.004](../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md)
and [Final.003 design](../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md).
Those governed sources remain authoritative; this README is implementation
documentation, not an independent design decision.

Calliope's `Topic` is an immutable ordered path of completed
`TopicEntry(qualifier, value, optional version)` values. A Topic's identity,
hash code and natural order use only the case-sensitive ordered qualifier/value
pairs; optional versions are opaque entry data and do not affect identity.
`TopicEntry.equals` compares the complete entry, including version presence and
spelling. Parent and ancestor projections retain the versions of entries they
contain. No runtime Topic identifier is generated.

## Construction and projections

`Topic.builder()` provides two construction modes. Sequential `addEntry` calls
append in order; indexed `addEntry(index, ...)` calls target final zero-based
positions and may arrive out of order. The first successful addition selects
the mode. Failed additions do not select a mode or mutate stored input. Indexed
`build()` requires contiguous positions starting at zero; a valid replacement
does not change depth. Missing/empty qualifiers supplied during Topic assembly
receive the deterministic `N<index>` qualifier. Strict canonical/XML
reconstruction never applies that default. Builders are caller-confined;
completed Topics and their entry lists are immutable snapshots.

The identity projection is `qualifier=value` entries joined by periods. The
version-capable projection is `qualifier[:version]=value`. Canonical parsing
requires completed, nonempty fields and accepts only the documented outer
escapes (`.`, `=`, `:`, and backslash). It strips exactly one outer escaping
layer. `getName` joins raw values with periods, `getFriendlyName` returns the
final raw value, and `getDisplayName` uses the final two values separated by a
hyphen (or just the sole value for a root). `getRelativeName` is the escaped
qualified representation of the final entry. Valid Unicode is preserved
without normalization.

## TopicMask selection

`TopicMask` is a neutral Boolean selector, not an authorization grant. Ordinary
masks evaluate ordered positions and by default require exact depth. Setting
`includeContainedElements(true)` accepts additional trailing Topic entries.
An explicit `TopicMask.all()` is a separate universal kind; ordinary `*` text
does not create that kind. Universal-only restrictions on expression and
containment accessors are documented on `TopicMask`.

Qualifier and value patterns support only these whole-field forms:

| Pattern | Meaning |
| --- | --- |
| `Patient` | Exact |
| `Patient*` | Prefix |
| `*Patient` | Suffix |
| `*Patient*` | Contains |
| `Patient*Record` | Prefix and suffix, without overlap |
| `*` | Any valid field |

Backslash escapes a literal asterisk or backslash. Before any other character,
or at the end, backslash remains literal. Other characters (including regex
punctuation) have no special meaning. Unsupported wildcard arrangements are
rejected while the entry is built. Patterns are classified once and matched
with bounded equality, prefix/suffix operations or substring search.

`TopicMaskEntry` factories accept raw patterns; `ofLiterals` and the literal
helpers first apply the completed Topic field rules and escape pattern
metacharacters. `TopicMaskBuilder` offers the same patterns/literals choices,
validates before mutation and returns immutable independent snapshots. It does
not generate Topic qualifiers. As with Topic builders, callers confine builder
use to one thread.

## Version constraints

Topic versions are opaque printable-ASCII text. A mask's omitted constraint
matches either an absent or present Topic version; a present-empty constraint
never matches. A nonempty constraint is either exact case-sensitive text or an
operator-led numeric expression. Numeric forms are `=v`, `<v`, `<=v`, `>v`,
`>=v`, or a lower/upper range such as `>=1.2,<2.0`. Ranges require a lower
comparison followed by an upper comparison and ordered endpoints; equal
endpoints are valid only when both sides are inclusive.

An initial backslash escapes one leading `=`, `<`, `>`, or backslash to select
exact text. Other leading uses are invalid. Exact comparisons preserve spelling;
numeric comparisons operate component-wise on bounded ASCII decimal spans,
without numeric conversion. Leading zeroes and absent trailing zero components
compare equivalently, while the stored Topic version and raw mask constraint
remain unchanged. Numeric syntax has no semantic-version, prerelease or
compatibility interpretation.

## XML interchange and validation

The only supported XML boundary is the stateless `NamingXmlCodec` in this
package: `readTopic(byte[])`, `writeTopic(Topic)`, `readTopicMask(byte[])`, and
`writeTopicMask(TopicMask)`. Topic entries and ordinary mask criteria retain
order. Mask records explicitly identify ordinary/universal kind and ordinary
containment. Raw qualifier/value patterns and optional version constraints use
canonical padded UTF-8 Base64; omitted and present-empty constraints remain
distinct. Output is deterministic UTF-8. Input is strict UTF-8, structurally
ordered and bounded; DTDs, external entities, duplicate/unexpected content,
unsupported encodings and malformed Base64 are rejected.

Validation failures use `NamingValidationException` with the public
`NamingFailureCategory`, `NamingOperation`, `NamingFieldKind` and an
`OptionalInt` for a valid known entry index. Messages contain only safe
metadata, never raw values, patterns, versions, paths or payloads. Failed
builder operations preserve prior state; strict reconstruction does not publish
partial models.

The fixed limits are depth 16; qualifier 64 code points; Topic value and
version 256 and 64 code points respectively; each qualifier/value pattern 512
code points; Topic and ordinary-mask canonical text 32 KiB and 128 KiB UTF-8;
XML records 256 KiB UTF-8; numeric operands 64 ASCII characters and 32
components; and raw numeric ranges 133 ASCII characters. Completed models and
their parsed mask descriptions are immutable and safe for concurrent reuse.

Public `TopicMask.matches(Topic)` has no diagnostic-handler parameter. The
optional synchronous diagnostic sink and its bounded records are package
private, invocation local, and absent on the public matching path. A match
returns only a Boolean and does not perform authorization, routing, persistence,
audit capture or logging; those responsibilities remain with their owning
Harmonia components.