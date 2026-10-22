# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read-only Exchange structure and raw transport fidelity; no baseline discovery."""
import argparse
from collections import Counter, defaultdict
import gzip
import hashlib
import json
from pathlib import Path
import re
import exchange_xml as g
BASE = g.BASE

def sha(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()

ROLES = ("canonicalId", "displayName", "friendlyName", "canonicalName", "archimateType", "harmoniaType")
EQUIVALENT = "SEMANTICALLY EQUIVALENT NORMALISATION"
PRESENTATION = "PRESENTATION-PRESERVING TRANSFORMATION"
LOSS = "INFORMATION LOSS"
SEMANTIC = "SEMANTIC DIFFERENCE"
UNKNOWN = "UNEXPLAINED DIFFERENCE"
CATEGORIES = (EQUIVALENT, PRESENTATION, LOSS, SEMANTIC, UNKNOWN)


def local(tag):
    return tag.removeprefix("{" + g.NS + "}")



def children(node):
    return [c for c in node if isinstance(c.tag, str)]



def expanded_type(node):
    value = node.get(g.TYPE)
    if value is None:
        return None
    prefix, name = value.split(":", 1) if ":" in value else (None, value)
    namespace = node.nsmap.get(prefix)
    return "{" + namespace + "}" + name if namespace else value



def values(node, name):
    # Preserve language, whitespace, multiplicity and attributes. Never strip UUIDs.
    return [{"text": c.text or "", "attributes": dict(sorted(c.attrib.items()))}
            for c in node.findall("a:" + name, g.N)]



class Exchange:
    """Shared Property resolver over the existing secure parser and offline XSDs.

    Index all native types and all claims using explicit inputs only.
    """
    def __init__(self, path):
        self.path = Path(path)
        self.root = g.parse(path)
        self.sha256 = sha(path)
        self.xml_sha256 = (hashlib.sha256(gzip.decompress(self.path.read_bytes())).hexdigest()
                           if self.path.suffix == '.gz' else self.sha256)
        self.issues = []
        try:
            g.validate_identifiers(self.root)
        except ValueError as exc:
            self.issues.append(str(exc))
        schema = g.load_schema(BASE / "schemas/archimate3_Diagram.xsd")
        if not schema.validate(self.root):
            self.issues.extend("XSD: " + e.message for e in schema.error_log)
        self.elements = self.index("a:elements/a:element")
        self.relationships = self.index("a:relationships/a:relationship")
        self.views = self.index("a:views/a:diagrams/a:view")
        self.definitions = self.index("a:propertyDefinitions/a:propertyDefinition")
        by_name = defaultdict(list)
        for identifier, definition in self.definitions.items():
            names = definition.findall("a:name", g.N)
            if len(names) == 1 and names[0].text:
                by_name[names[0].text].append(identifier)
            else:
                self.issues.append("Ambiguous/empty Property definition name: " + str(identifier))
        self.keys = {ids[0]: key for key, ids in by_name.items() if len(ids) == 1}
        self.definition_ids = {key: identifier for identifier, key in self.keys.items()}
        for key, ids in sorted(by_name.items()):
            if len(ids) != 1:
                self.issues.append("Duplicate Property definition name: " + repr(key))
        for prop in self.root.findall(".//a:property", g.N):
            if prop.get("propertyDefinitionRef") not in self.keys:
                self.issues.append("Unresolved/ambiguous Property reference: " + str(prop.get("propertyDefinitionRef")))
        self.check_references()
        self.issues = sorted(set(self.issues))

    def index(self, path):
        result = {}
        for position, element in enumerate(self.root.findall(path, g.N)):
            key = element.get("identifier")
            if not key or key in result:
                # This is a diagnostic locator, never an allocated identity.
                # Invalid input remains unresolved; preserve every record.
                key = f"(unresolved identifier at {path} position {position})"
            result[key] = element
        return result

    def claims(self, owner, key):
        return [p for p in owner.findall("a:properties/a:property", g.N)
                if self.keys.get(p.get("propertyDefinitionRef")) == key]

    def texts(self, owner, key):
        return [v.text or "" for p in self.claims(owner, key) for v in p.findall("a:value", g.N)]

    def single(self, owner, key):
        claims = self.claims(owner, key)
        texts = self.texts(owner, key)
        return texts[0] if len(claims) == len(texts) == 1 else None

    def check_references(self):
        all_ids = {e.get("identifier") for e in self.root.iter() if e.get("identifier")}
        tables = {"elementRef": self.elements, "relationshipRef": self.relationships,
                  "viewRef": self.views, "identifierRef": all_ids}
        for node in self.root.iter():
            for attr, table in tables.items():
                if attr in node.attrib and node.get(attr) not in table:
                    self.issues.append("Unresolved " + attr + ": " + node.get(attr))
        semantic_ids = self.elements.keys() | self.relationships.keys()
        for identifier, rel in self.relationships.items():
            if any(rel.get(k) not in semantic_ids for k in ("source", "target")):
                self.issues.append("Unresolved relationship endpoint: " + str(identifier))
        for vid, view in self.views.items():
            nodes = {n.get("identifier"): n for n in view.findall(".//a:node", g.N)}
            connections = {n.get("identifier"): n for n in view.findall(".//a:connection", g.N)}
            for cid, connection in connections.items():
                endpoints = [connection.get(k) for k in ("source", "target")]
                if any(e not in nodes.keys() | connections.keys() for e in endpoints):
                    self.issues.append("Unresolved visual endpoint: " + str(vid) + "/" + str(cid))
                rel = self.relationships.get(connection.get("relationshipRef"))
                if rel is not None and all(e in nodes for e in endpoints):
                    refs = [nodes[e].get("elementRef") for e in endpoints]
                    if all(refs) and refs != [rel.get("source"), rel.get("target")]:
                        self.issues.append("Visual/semantic endpoint mismatch: " + str(cid))

    def metrics(self):
        rows = [{"identifier": key, "name": values(view, "name"),
                 "elements": len(view.findall(".//a:node[@elementRef]", g.N)),
                 "visual_objects": len(view.findall(".//a:node", g.N)),
                 "connections": len(view.findall(".//a:connection", g.N))}
                for key, view in self.views.items()]
        largest = sorted(rows, key=lambda r: (-r["visual_objects"], -r["connections"], r["identifier"]))
        return dict(semantic_elements=len(self.elements), relationships=len(self.relationships),
                    views=len(self.views), visual_objects=sum(r["visual_objects"] for r in rows),
                    visual_connections=sum(r["connections"] for r in rows),
                    largest_view=largest[0] if largest else None,
                    xml_bytes=self.path.stat().st_size, xml_decimal_mb=self.path.stat().st_size / 1_000_000)



def compare(before, after):
    """Exhaustive XML probe; match existing representation IDs, never names.

    Only Property-definition *keys* use exact names (not architectural identity).
    Unordered keyed semantic collections can reorder. Visual stacking, routing,
    languages, text and anonymous organisation order cannot silently reorder.
    """
    findings = []
    common_keys = before.definition_ids.keys() & after.definition_ids.keys()

    def add(category, path, a, b, reason):
        findings.append(dict(classification=category, path=path, before=a, after=b, reason=reason))

    def attrs(node, doc):
        result = dict(node.attrib)
        if g.TYPE in result:
            result[g.TYPE] = expanded_type(node)
        ref = result.get("propertyDefinitionRef")
        if ref in doc.keys and doc.keys[ref] in common_keys:
            result["propertyDefinitionRef"] = "Property key: " + doc.keys[ref]
        return result

    def keyed(parent, doc):
        counts, result = Counter(), {}
        for child in children(parent):
            tag = local(child.tag)
            if child.tag == g.tag("propertyDefinition") and doc.keys.get(child.get("identifier")) in common_keys:
                key = tag + "[key=" + doc.keys[child.get("identifier")] + "]"
            elif child.tag == g.tag("property") and doc.keys.get(child.get("propertyDefinitionRef")) in common_keys:
                key = tag + "[key=" + doc.keys[child.get("propertyDefinitionRef")] + "]"
            elif child.tag == g.tag("item"):
                # Anonymous navigation after Archi export has no stable identity.
                # Pair strictly by position only for this no-edit probe, retaining
                # every label, reference, order and lost identifier as evidence.
                counts[tag] += 1
                key = tag + "[position=" + str(counts[tag]) + "]"
            elif child.get("identifier") is not None:
                key = tag + "[id=" + child.get("identifier") + "]"
            else:
                counts[tag] += 1
                key = tag + "[" + str(counts[tag]) + "]"
            # Preserve duplicate claims instead of dict overwrite.
            if key in result:
                counts[key] += 1
                key += "[duplicate=" + str(counts[key]) + "]"
            result[key] = child
        return result

    def content_text(node):
        # XML element-only indentation is syntax. Leaf strings, even all-space
        # values, are data. Non-whitespace mixed content remains observable.
        if children(node) and not (node.text or "").strip():
            return None
        return node.text

    def walk(a, b, path):
        aa, ba = attrs(a, before), attrs(b, after)
        if a.tag == g.tag("propertyDefinition") and before.keys.get(a.get("identifier")) in common_keys:
            old, new = aa.pop("identifier", None), ba.pop("identifier", None)
            if old != new:
                # Rekeying alone is equivalent. Metadata below is still compared.
                add(EQUIVALENT, path + "/@identifier", old, new, "Exact unique Property key resolves both definition IDs; all other definition content and owner values compared independently")
        for key in sorted(aa.keys() | ba.keys()):
            if aa.get(key) == ba.get(key):
                continue
            if key not in ba:
                category, reason = LOSS, "Attribute absent after transport"
            elif key == "identifier" and a.tag == g.tag("item"):
                category, reason = UNKNOWN, "Organisation item identity transformed; paths do not establish identity"
            elif "/element[id=" in path or "/relationship[id=" in path:
                category, reason = SEMANTIC, "Semantic record attribute changed"
            else:
                category, reason = UNKNOWN, "No proven representational equivalence for this attribute"
            add(category, path + "/@" + key, aa.get(key), ba.get(key), reason)
        at, bt = content_text(a), content_text(b)
        if at != bt:
            category = LOSS if at is not None and bt is None else SEMANTIC if "/element[id=" in path or "/relationship[id=" in path else UNKNOWN
            add(category, path + "/text()", at, bt, "Exact text compared; whitespace/language selection is not normalised")
        ac, bc = keyed(a, before), keyed(b, after)
        if list(ac) != list(bc):
            safe = a.tag in {g.tag(n) for n in ("elements", "relationships", "diagrams", "propertyDefinitions", "properties")}
            safe = safe and ac.keys() == bc.keys() and not any("duplicate=" in k for k in ac)
            add(EQUIVALENT if safe else UNKNOWN, path + "/child-order", list(ac), list(bc),
                "Uniquely keyed unordered collection" if safe else "Order retained as evidence: may affect stacking, routing, languages or anonymous navigation")
        for key in sorted(ac.keys() | bc.keys()):
            subpath = path + "/" + key
            if key not in bc:
                add(LOSS, subpath, g.signature(ac[key]), None, "Subtree absent after transport; raw input retained by hash")
            elif key not in ac:
                add(UNKNOWN, subpath, None, g.signature(bc[key]), "Additional subtree with no established equivalence")
            else:
                walk(ac[key], bc[key], subpath)
                tail_a, tail_b = ac[key].tail, bc[key].tail
                if (tail_a or "").strip() or (tail_b or "").strip():
                    if tail_a != tail_b:
                        add(UNKNOWN, subpath + "/tail()", tail_a, tail_b, "Mixed-content tail changed")

    walk(before.root, after.root, "/model")
    counts = Counter(f["classification"] for f in findings)
    issues = {"published": before.issues, "incoming": after.issues}
    status = "UNRESOLVED" if any(issues.values()) else "DIFFERENCES" if any(counts[k] for k in (LOSS, SEMANTIC, UNKNOWN)) else "EQUIVALENT"

    changed_records = defaultdict(set)
    record_pattern = re.compile(r'/((?:element|relationship|view))\[id=([^\]]+)\]')
    material_paths = [f['path'] for f in findings if f['classification'] != EQUIVALENT]
    for path in material_paths:
        for prefix, identifier in record_pattern.findall(path):
            changed_records[prefix].add(identifier)

    def record_evidence(table_a, table_b, prefix):
        common = table_a.keys() & table_b.keys()
        changed = sorted(key for key in common if key in changed_records[prefix] or
                         (']' in key and any(f'/{prefix}[id={key}]' in path for path in material_paths)))
        return dict(before=len(table_a), after=len(table_b), retained_ids=len(table_a.keys() & table_b.keys()),
                    missing_ids=sorted(table_a.keys() - table_b.keys()), additional_ids=sorted(table_b.keys() - table_a.keys()),
                    changed_records=changed)

    presentation = record_evidence(before.views, after.views, "view")
    for tag in ("node", "connection"):
        left = {(vid, n.get("identifier")) for vid, v in before.views.items() for n in v.findall(".//a:" + tag, g.N)}
        right = {(vid, n.get("identifier")) for vid, v in after.views.items() for n in v.findall(".//a:" + tag, g.N)}
        presentation[tag + "_identity"] = dict(before=len(left), after=len(right), retained=len(left & right),
                                                missing=sorted(left - right), additional=sorted(right - left))
    presentation["coverage"] = "View-local visual IDs, parent/nesting, membership, coordinates, dimensions, ordered bendpoints, child order, style, labels and residual XML; no presentation replay or speculative equivalence rules"
    contract = {}
    for role in ROLES:
        observed, preserved, changed = 0, 0, []
        for identifier, owner in sorted(before.elements.items()):
            claims = before.claims(owner, role)
            if not claims:
                continue
            observed += 1
            target = after.elements.get(identifier)
            left = [values(p, "value") for p in claims]
            right = [] if target is None else [values(p, "value") for p in after.claims(target, role)]
            if left == right:
                preserved += 1
            else:
                changed.append(dict(element=identifier, before=left, after=right))
        contract[role] = dict(published_owners=observed, exact_value_language_multiplicity_owner_preserved=preserved, changes=changed)
    return dict(comparison_status=status, purpose="Raw representation fidelity; use continuum_workbench for canonical Change Review; no architecture acceptance",
                published_sha256=before.sha256, incoming_sha256=after.sha256, input_issues=issues,
                classifications={k: counts[k] for k in CATEGORIES}, metrics={"published": before.metrics(), "incoming": after.metrics()},
                elements=record_evidence(before.elements, after.elements, "element"),
                relationships=record_evidence(before.relationships, after.relationships, "relationship"),
                presentation=presentation, contract_properties=contract, findings=findings,
                empirical_archi_fidelity="UNRESOLVED: comparator alone does not authenticate an Archi execution; use the hash-pinned evidence procedure")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('command', choices=['compare'])
    parser.add_argument('--baseline', type=Path, required=True)
    parser.add_argument('--submitted', type=Path, required=True)
    parser.add_argument('--report', type=Path, required=True)
    args = parser.parse_args()
    report = compare(Exchange(args.baseline), Exchange(args.submitted))
    args.report.parent.mkdir(parents=True, exist_ok=True)
    args.report.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
    print(report['comparison_status'])
    return 1 if report['comparison_status'] == 'UNRESOLVED' else 0

if __name__ == '__main__':
    raise SystemExit(main())
