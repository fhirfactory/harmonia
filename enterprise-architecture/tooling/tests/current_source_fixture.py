# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Supply declared retained provenance when copying current canonical fixtures."""
from pathlib import Path
import shutil

import architecture_continuum as c


def copy_retained_inputs(source, destination):
    # Match current independent-operation staging: retained provenance is an
    # explicit maintained input, not a reason to omit validation in test trees.
    _, elements = c.load(source)
    paths = {Path(item['retainedAt']) for metadata, _, _ in elements.values()
             for item in metadata['provenance'] if item.get('retainedAt')}
    for relative in paths:
        c.require(not relative.is_absolute() and '..' not in relative.parts,
                  'Retained fixture evidence must be repository-relative')
        target = destination / relative
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(source / relative, target)
