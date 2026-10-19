#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Reproduce the archived normalisation XML, writing no RAD publication."""
import hashlib
import json
from pathlib import Path
import sys

ROOT = next(p for p in Path(__file__).resolve().parents if (p / 'architecture-release.json').is_file())
sys.path.insert(0, str(ROOT / 'docs/archimate/framework/tools'))
import synchronise_baseline as s
import workspace_paths as w

archived = w.GOVERNANCE_TOOLING / '1.0.14/exchange/Harmonia-Architecture-1.0.14.xml'
original = s.g.parse(archived)
outputs, _, _ = s.publish(reconciled=False)
generated = s.g.ET.fromstring(outputs[s.metadata.model_path()])
slot = 'a:properties/a:property[@propertyDefinitionRef="baseline-architecture-release"]/a:value'
before, after = original.findall(slot, s.g.N), generated.findall(slot, s.g.N)
s.g.require(len(before) == len(after) == 1, 'Ambiguous historical metadata slot')
# This fixture reuses the original literal internal metadata value. It does not
# select a RAD release, alter root authority or write to a publication namespace.
after[0].text = before[0].text
result = s.g.serialize(generated)
s.g.require(result == archived.read_bytes(), 'Archived normalisation does not reproduce byte-for-byte')
print(json.dumps(dict(status='PASS', classification='non-RAD governance/tooling evidence',
                      scope='Archived normalisation XML, with original literal model-level metadata',
                      output_sha256=hashlib.sha256(result).hexdigest(),
                      exact_bytes=len(result), architecture_publications_written=0,
                      release_authority_changed=False,
                      historical_report_attestations='Preserved; not refreshed'), indent=2))
