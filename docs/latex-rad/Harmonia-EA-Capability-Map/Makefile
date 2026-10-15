# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
NAME := Harmonia-EA-Capability-Map
.NOTPARALLEL:

.PHONY: all render pdf check
all: render pdf check

render:
	python3 tools/render.py

pdf:
	mkdir -p build
	timeout --signal=TERM --kill-after=10s 180s latexmk -xelatex -interaction=nonstopmode -halt-on-error -outdir=build $(NAME).tex > build/latexmk.txt 2>&1

check:
	python3 tools/check.py
	cp build/$(NAME).pdf $(NAME).pdf
