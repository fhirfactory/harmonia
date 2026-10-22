# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
PYTHON ?= python3
REPOSITORY := $(abspath ../..)
TOOLING := $(REPOSITORY)/enterprise-architecture/tooling
RAD_VERSION := $(shell $(PYTHON) $(TOOLING)/architecture_release.py show)
CONTINUUM_OUTPUT ?= $(REPOSITORY)/enterprise-architecture/continuum/documentation/published/$(RAD_VERSION)
BOUNDED = timeout --signal=TERM --kill-after=5s
.NOTPARALLEL:
.PHONY: all continuum continuum-check continuum-test validate check test verify-independent browser-test

all: continuum test check

validate:
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/architecture_continuum.py validate

continuum: validate
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/continuum_manifest.py
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/architecture_continuum.py project --output $(CONTINUUM_OUTPUT)/Harmonia-Architecture.xml
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/publish_continuum.py --output $(CONTINUUM_OUTPUT)

check continuum-check:
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/architecture_release.py check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/continuum_manifest.py --check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/architecture_continuum.py project --output $(CONTINUUM_OUTPUT)/Harmonia-Architecture.xml --check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/publish_continuum.py --output $(CONTINUUM_OUTPUT) --check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/verify_continuum_links.py --report /tmp/harmonia-current-ea-links.json

test continuum-test:
	$(BOUNDED) 420s $(PYTHON) -B -m unittest discover -s $(TOOLING)/tests -v

verify-independent:
	$(BOUNDED) 600s $(PYTHON) -B $(TOOLING)/verify_continuum_reproducibility.py --report /tmp/harmonia-current-ea-independence.json

browser-test:
	$(BOUNDED) 30s node $(TOOLING)/tests/continuum-browser-smoke.js $(CONTINUUM_OUTPUT)/continuum.html
