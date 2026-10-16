# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
PYTHON ?= python3
BASELINE ?= baseline/Harmonia-Capability-Architecture-v2.1a.xml
SCHEMA ?= schemas/archimate3_Diagram.xsd
OUTPUT ?= Harmonia-Capability-Architecture.xml
REPORT ?= validation.json
DOCUMENTATION_MAP ?= documentation-map.json
REPOSITORY ?= ../../..
RELEASE_FILE ?= $(REPOSITORY)/architecture-release.json
INVENTORY ?= ../../markdown/governance/rad-archimate-source-inventory.json
.NOTPARALLEL:

.PHONY: all generate inventory check test report
all: generate inventory test check report

generate:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/publish_documentation.py --baseline "$(BASELINE)" --schema "$(SCHEMA)" --output "$(OUTPUT)" --report "$(REPORT)" --documentation-map "$(DOCUMENTATION_MAP)" --release-file "$(RELEASE_FILE)"

inventory:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/assess_rad.py --repository "$(REPOSITORY)" --documentation-map "$(DOCUMENTATION_MAP)" --output "$(INVENTORY)" --release-file "$(RELEASE_FILE)"

check:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/publish_documentation.py --baseline "$(BASELINE)" --schema "$(SCHEMA)" --output "$(OUTPUT)" --report "$(REPORT)" --documentation-map "$(DOCUMENTATION_MAP)" --release-file "$(RELEASE_FILE)" --check
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/assess_rad.py --repository "$(REPOSITORY)" --documentation-map "$(DOCUMENTATION_MAP)" --output "$(INVENTORY)" --release-file "$(RELEASE_FILE)" --check

test:
	timeout --signal=TERM --kill-after=5s 60s $(PYTHON) -m unittest discover -s tests -v

report:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) "$(REPOSITORY)/scripts/architecture_identity.py" report --check
