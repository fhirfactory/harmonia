# =============================================================================
# Makefile for Harmonia ArchiMate 3.2 LaTeX Documentation
# =============================================================================

MAIN = main
LATEXMK = latexmk
PDFLATEX = pdflatex
LATEXMK_FLAGS = -pdf -interaction=nonstopmode -synctex=1
PDFLATEX_FLAGS = -interaction=nonstopmode -synctex=1

.PHONY: all pdf clean distclean docker view help

all: pdf

pdf:
	@if command -v $(LATEXMK) > /dev/null 2>&1; then \
		echo "Building with $(LATEXMK)..."; \
		$(LATEXMK) $(LATEXMK_FLAGS) $(MAIN).tex; \
	elif command -v $(PDFLATEX) > /dev/null 2>&1; then \
		echo "Building with $(PDFLATEX) (multi-pass)..."; \
		$(PDFLATEX) $(PDFLATEX_FLAGS) $(MAIN).tex; \
		$(PDFLATEX) $(PDFLATEX_FLAGS) $(MAIN).tex; \
	else \
		echo "Error: Neither latexmk nor pdflatex was found on PATH."; \
		echo "Please install TeX Live or run via Docker: make docker"; \
		exit 1; \
	fi

clean:
	@echo "Cleaning auxiliary files..."
	@rm -f *.aux *.log *.out *.toc *.lof *.lot *.fls *.fdb_latexmk *.synctex.gz *.nav *.snm *.vrb
	@rm -f chapters/*.aux diagrams/*.aux styles/*.aux

distclean: clean
	@echo "Removing generated PDF..."
	@rm -f $(MAIN).pdf

docker:
	@echo "Compiling document using Docker (texlive/texlive)..."
	docker run --rm -v "$$(pwd):/work" -w /work texlive/texlive:latest latexmk -pdf -interaction=nonstopmode $(MAIN).tex

help:
	@echo "Harmonia LaTeX Documentation Build Targets:"
	@echo "  make (or make pdf)  : Build the master PDF document"
	@echo "  make clean          : Remove auxiliary compilation artifacts"
	@echo "  make distclean      : Remove auxiliary files and output PDF"
	@echo "  make docker         : Build using Docker TeX Live container"
	@echo "  make help           : Display this help menu"
