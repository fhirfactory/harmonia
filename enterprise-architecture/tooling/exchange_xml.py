# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Secure explicit-path Exchange IO and bundled offline schema validation."""
from pathlib import Path
import gzip
from lxml import etree as ET

BASE = Path(__file__).resolve().parent
NS = 'http://www.opengroup.org/xsd/archimate/3.0/'
TYPE = '{http://www.w3.org/2001/XMLSchema-instance}type'
N = {'a': NS}
class PublicationError(ValueError):
    pass

def require(condition, message):
    if not condition:
        raise PublicationError(message)

def tag(name):
    return '{' + NS + '}' + name

def parse(path):
    path = Path(path)
    data = path.read_bytes()
    if path.suffix == '.gz':
        data = gzip.decompress(data)
    root = ET.fromstring(data, ET.XMLParser(resolve_entities=False, no_network=True))
    require(not root.getroottree().docinfo.doctype, 'A publication input must not contain a DTD')
    require(root.tag == tag('model'), 'Expected an ArchiMate Exchange model')
    return root

def signature(element):
    """Compare XML content exactly, excluding indentation and namespace prefixes."""
    return [
        element.tag, sorted(element.attrib.items()),
        element.text if element.text and element.text.strip() else None,
        [signature(c) for c in element if isinstance(c.tag, str)],
    ]



class LocalSchemaResolver(ET.Resolver):
    def __init__(self, directory):
        super().__init__()
        self.directory = directory

    def resolve(self, url, public_id, context):
        if url in ("http://www.w3.org/2001/xml.xsd", "http://www.w3.org/2001/03/xml.xsd"):
            return self.resolve_filename(str(self.directory / "xml.xsd"), context)
        if "://" in url:
            raise PublicationError(f"Remote schema resolution is disabled: {url}")
        return None



def load_schema(path):
    path = Path(path).resolve()
    parser = ET.XMLParser(resolve_entities=False, no_network=True)
    parser.resolvers.add(LocalSchemaResolver(path.parent))
    return ET.XMLSchema(ET.parse(str(path), parser))



def validate_identifiers(root):
    ids = [e.get("identifier") for e in root.iter() if e.get("identifier") is not None]
    require(all(ids) and len(ids) == len(set(ids)), "Duplicate or empty identifier")

