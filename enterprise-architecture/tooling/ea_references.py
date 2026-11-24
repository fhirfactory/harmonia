# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Resolve explicit governance succession; leave historical provenance intact."""
from pathlib import Path
import json


def successors(root):
    return json.loads((root / 'enterprise-architecture/governance/authority-successors.json').read_text())['successors']


def current_target(root, target, mapping):
    """No Legacy-RAD file access: succession is explicit maintained configuration."""
    try:
        relative = target.relative_to(root).as_posix()
    except ValueError:
        return target
    successor = mapping.get(relative)
    return root / successor if successor else target


def historical_target(root, target):
    try:
        relative = target.relative_to(root)
    except ValueError:
        return False
    return relative.is_relative_to('docs') or relative.is_relative_to('scripts')
