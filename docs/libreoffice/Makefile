# =============================================================================
# Makefile for Harmonia Architecture Specification (LibreOffice .odt Edition)
# =============================================================================

PYTHON = python3
SCRIPT = scripts/generate_odt.py
OUTPUT = harmonia-architecture-specification.odt

.PHONY: all build validate clean help

all: build validate

build:
	@echo "Building LibreOffice (.odt) architecture specification..."
	$(PYTHON) $(SCRIPT)

validate:
	@echo "Validating OpenDocument (.odt) package structure and XML schemas..."
	$(PYTHON) $(SCRIPT) --validate

clean:
	@echo "Cleaning generated artifacts..."
	rm -f $(OUTPUT)

help:
	@echo "Harmonia LibreOffice Documentation Build Targets:"
	@echo "  make (or make all) : Build and validate the master .odt document"
	@echo "  make build         : Generate the .odt package from LaTeX sources"
	@echo "  make validate      : Verify OpenDocument package integrity and XML validity"
	@echo "  make clean         : Remove generated .odt document"
	@echo "  make help          : Display this help menu"
