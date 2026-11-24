# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Workspace boundaries and exact navigation for frozen historical locators.

Location mapping changes neither evidence text nor its expected content hash.
It supplies no architectural correspondence or comparison normalisation.
"""
import json
from pathlib import Path

FRAMEWORK = Path(__file__).resolve().parents[1]
ROOT = FRAMEWORK.parents[2]
EXCHANGE = FRAMEWORK.parent / 'exchange'
PUBLISH = EXCHANGE / 'publish'
INGEST = EXCHANGE / 'ingest'
PROCESSING = FRAMEWORK.parent / 'processing'
PUBLISHING = PROCESSING / 'publishing'
INGESTION = PROCESSING / 'ingestion'
SYNCHRONISATION_EVIDENCE = PUBLISHING / '1.0.6'
ESTABLISHMENT_EVIDENCE = PUBLISHING / '1.0.7'
RECOVERY_EVIDENCE = PUBLISHING / '1.0.5'
LEGACY_EVIDENCE = PUBLISHING / '1.0.4'
DOCUMENTATION_EVIDENCE = PUBLISHING / '1.0.1'
LEGACY_PUBLICATION = PUBLISH / '1.0.4/Harmonia-Capability-Architecture.xml'
LEGACY_VALIDATION = LEGACY_EVIDENCE / 'validation.json'
LOCATIONS = json.loads((FRAMEWORK / 'historical-locations.json').read_text())


def historical_path(path, root=ROOT):
    """Resolve only explicitly recorded moved files/directories, under root."""
    root = Path(root).resolve()
    path = Path(path)
    absolute = path if path.is_absolute() else root / path
    try:
        relative = absolute.relative_to(root).as_posix()
    except ValueError:
        return absolute
    target = LOCATIONS['files'].get(relative, LOCATIONS['directories'].get(relative))
    return root / target if target else absolute


def incoming_path(path):
    """A bare export filename selects the ingest landing area; explicit paths stay explicit."""
    path = Path(path)
    return INGEST / path if not path.is_absolute() and len(path.parts) == 1 else path
