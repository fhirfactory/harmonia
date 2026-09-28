#!/usr/bin/env bash
# Copyright (c) 2026 Mark Hunter
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

set -eo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

cd "${REPO_ROOT}"

echo "================================================================================"
echo "Running Step 3 Verification Suite"
echo "================================================================================"

echo ""
echo ">>> Phase 1: Running Mnemosyne Clinical Subsystem Tests (including PostgreSQL Concurrency Suite)..."
python3 scripts/test_runner.py --process-timeout=180 --stall-timeout=60 test -pl hestia/mnemosyne-clinical

echo ""
echo ">>> Phase 2: Running ArchUnit Architecture Test Suite..."
python3 scripts/test_runner.py --process-timeout=180 --stall-timeout=60 test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

echo ""
echo "================================================================================"
echo "SUCCESS: All Step 3 Verification Suites (Mnemosyne Clinical & ArchUnit) Passed!"
echo "================================================================================"
