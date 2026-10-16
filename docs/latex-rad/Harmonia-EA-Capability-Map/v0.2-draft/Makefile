# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
NAME := Harmonia-EA-Capability-Map
.NOTPARALLEL:
.PHONY: all compose render pdf check model-check semantic-tests
all: compose render pdf check

compose:
	python3 tools/compose.py
	python3 tools/reconcile.py

render:
	python3 tools/render.py

model-check:
	python3 tools/check.py --model-only

semantic-tests:
	python3 tools/probes.py
	python3 -m unittest discover -s tools -p 'test_*.py' -v

pdf:
	mkdir -p build
	timeout --signal=TERM --kill-after=10s 300s latexmk -pdf -interaction=nonstopmode -halt-on-error -outdir=build $(NAME).tex > build/latexmk.txt 2>&1

check:
	python3 tools/check.py
	cp build/$(NAME).pdf $(NAME).pdf
