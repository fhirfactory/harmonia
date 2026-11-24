# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
PYTHON ?= python3
REPOSITORY := $(abspath ../..)
TOOLING := $(REPOSITORY)/enterprise-architecture/tooling
RAD_VERSION := $(shell $(PYTHON) $(TOOLING)/architecture_release.py show)
CONTINUUM_OUTPUT ?= $(REPOSITORY)/enterprise-architecture/documentation/continuum-manifest/published/$(RAD_VERSION)
RAD_OUTPUT ?= $(REPOSITORY)/enterprise-architecture/documentation/continuum-rad/published/$(RAD_VERSION)
SOURCE_DATE_EPOCH ?= 1792627200
BOUNDED = timeout --signal=TERM --kill-after=5s
.NOTPARALLEL:
.PHONY: all continuum continuum-rad rad-pdf continuum-check continuum-test validate check test verify-independent browser-test

all: continuum continuum-rad test check

validate:
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/architecture_continuum.py validate

continuum: validate
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/continuum_manifest.py
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/architecture_continuum.py project --output $(CONTINUUM_OUTPUT)/Harmonia-Architecture.xml --publish
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/publish_continuum.py --output $(CONTINUUM_OUTPUT)

continuum-rad: validate
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/publish_continuum_rad.py --output $(RAD_OUTPUT)

rad-pdf: continuum-rad
	cd $(RAD_OUTPUT) && SOURCE_DATE_EPOCH=$(SOURCE_DATE_EPOCH) FORCE_SOURCE_DATE=1 $(BOUNDED) 240s latexmk -xelatex -interaction=nonstopmode -halt-on-error -outdir=build continuum-rad.tex
	cp $(RAD_OUTPUT)/build/continuum-rad.pdf $(RAD_OUTPUT)/continuum-rad.pdf

check continuum-check:
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/architecture_release.py check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/continuum_manifest.py --check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/architecture_continuum.py project --output $(CONTINUUM_OUTPUT)/Harmonia-Architecture.xml --publish --check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/publish_continuum.py --output $(CONTINUUM_OUTPUT) --check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/publish_continuum_rad.py --output $(RAD_OUTPUT) --check
	$(BOUNDED) 90s $(PYTHON) -B $(TOOLING)/verify_continuum_links.py --report /tmp/harmonia-current-ea-links.json

test continuum-test:
	$(BOUNDED) 420s $(PYTHON) -B -m unittest discover -s $(TOOLING)/tests -v

verify-independent:
	$(BOUNDED) 600s $(PYTHON) -B $(TOOLING)/verify_continuum_reproducibility.py --report /tmp/harmonia-current-ea-independence.json

browser-test:
	$(BOUNDED) 30s node $(TOOLING)/tests/continuum-browser-smoke.js $(CONTINUUM_OUTPUT)/continuum.html
