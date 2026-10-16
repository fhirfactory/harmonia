#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Load the frozen v0.1 transcriber from its versioned directory unchanged."""
import types
from pathlib import Path

BASE=Path(__file__).resolve().parents[1]
V01=BASE.parent/'v0.1-draft'
ROOT=BASE.parents[3]


def frozen_file(name):
    # The original publication's .gitignore remains in the shared parent.
    return V01.parent/name if name=='.gitignore' else V01/name


def load_v01():
    path=V01/'tools/render.py'
    source=path.read_text()
    # v0.1 was frozen before relocation into v0.1-draft. Adapt only its root
    # lookup in memory; the file, source semantics and fingerprint stay frozen.
    old='ROOT = BASE.parents[2]'
    assert source.count(old)==1,'Unexpected frozen transcriber root lookup'
    module=types.ModuleType('v01_render')
    module.__file__=str(path)
    exec(compile(source.replace(old,'ROOT = BASE.parents[3]'),str(path),'exec'),module.__dict__)
    return module
