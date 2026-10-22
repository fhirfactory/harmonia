# HISTORICAL ONLY: current EA operations use make -C enterprise-architecture/tooling all.
# This frozen entry point is retained for originating-checkout replay.
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
PYTHON ?= python3
BASELINE ?= baseline/Harmonia-Capability-Architecture-v2.1a.xml
SCHEMA ?= schemas/archimate3_Diagram.xsd
OUTPUT ?= ../exchange/publish/1.0.4/Harmonia-Capability-Architecture.xml
REPORT ?= ../processing/publishing/1.0.4/validation.json
DOCUMENTATION_MAP ?= documentation-map.json
REPOSITORY ?= ../../..
RELEASE_FILE ?= $(REPOSITORY)/architecture-release.json
INVENTORY ?= ../../markdown/governance/rad-archimate-source-inventory.json
.NOTPARALLEL:

.PHONY: all baseline baseline-check baseline-report baseline-independent recovery-all recovery recovery-check recovery-report legacy-all generate inventory adoption bindings check test report
RAD_VERSION := $(shell $(PYTHON) $(REPOSITORY)/scripts/architecture_release.py show)
CONTINUUM_OUTPUT ?= $(REPOSITORY)/enterprise-architecture/continuum/documentation/published/$(RAD_VERSION)
all: continuum continuum-test continuum-check

.PHONY: continuum continuum-test continuum-check historical-baseline-all
continuum:
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) $(REPOSITORY)/scripts/continuum_manifest.py
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) $(REPOSITORY)/scripts/architecture_continuum.py project --output $(CONTINUUM_OUTPUT)/Harmonia-Architecture.xml
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) $(REPOSITORY)/scripts/publish_continuum.py --output $(CONTINUUM_OUTPUT)

continuum-check:
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) $(REPOSITORY)/scripts/continuum_manifest.py --check
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) $(REPOSITORY)/scripts/architecture_continuum.py project --output $(CONTINUUM_OUTPUT)/Harmonia-Architecture.xml --check
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) $(REPOSITORY)/scripts/publish_continuum.py --output $(CONTINUUM_OUTPUT) --check

continuum-test:
	timeout --signal=TERM --kill-after=5s 120s $(PYTHON) -m unittest discover -s $(REPOSITORY)/scripts/tests -p 'test_continuum*.py' -v

# Historical procedures require their originating checkout/fixtures. They are
# retained for historical recovery and are not the current publication pipeline.
historical-baseline-all: baseline test baseline-check baseline-report baseline-independent

baseline:
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) tools/synchronise_baseline.py

baseline-check:
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) tools/synchronise_baseline.py --check

baseline-report:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/report_baseline.py --check

baseline-independent:
	timeout --signal=TERM --kill-after=5s 120s $(PYTHON) tools/verify_baseline_outputs.py

recovery-all: recovery test recovery-check recovery-report

recovery:
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) tools/recover_v02.py

recovery-check:
	timeout --signal=TERM --kill-after=5s 90s $(PYTHON) tools/recover_v02.py --check

recovery-report:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/report_recovery.py --check

# Historical 1.0.4 publisher, retained for replay against its release fixtures.
legacy-all: generate inventory adoption bindings test check report

generate:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/publish_navigation.py --baseline "$(BASELINE)" --schema "$(SCHEMA)" --output "$(OUTPUT)" --report "$(REPORT)" --documentation-map "$(DOCUMENTATION_MAP)" --release-file "$(RELEASE_FILE)"

inventory:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/assess_rad.py --repository "$(REPOSITORY)" --documentation-map "$(DOCUMENTATION_MAP)" --output "$(INVENTORY)" --release-file "$(RELEASE_FILE)"
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) "$(REPOSITORY)/scripts/architecture_identity.py" inventory

adoption:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) "$(REPOSITORY)/scripts/architecture_identity.py" adoption

bindings:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) "$(REPOSITORY)/scripts/architecture_identity.py" bindings

check:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/publish_navigation.py --baseline "$(BASELINE)" --schema "$(SCHEMA)" --output "$(OUTPUT)" --report "$(REPORT)" --documentation-map "$(DOCUMENTATION_MAP)" --release-file "$(RELEASE_FILE)" --check
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/assess_rad.py --repository "$(REPOSITORY)" --documentation-map "$(DOCUMENTATION_MAP)" --output "$(INVENTORY)" --release-file "$(RELEASE_FILE)" --check
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) "$(REPOSITORY)/scripts/architecture_identity.py" inventory --check
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) "$(REPOSITORY)/scripts/architecture_identity.py" validate
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) "$(REPOSITORY)/scripts/architecture_identity.py" adoption --check
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) "$(REPOSITORY)/scripts/architecture_identity.py" bindings --check

test:
	timeout --signal=TERM --kill-after=5s 420s $(PYTHON) -m unittest discover -s tests -v

report:
	timeout --signal=TERM --kill-after=5s 30s $(PYTHON) tools/report_navigation.py --check
