#!/bin/bash
set -eo pipefail

echo "========================================================"
echo "Running Step 2 PostgreSQL Concurrency Tests"
echo "========================================================"

MVN_CMD="mvn test -pl hestia/mnemosyne-clinical -Dtest=HapiJpaAuthoritativePersistencePostgreSqlConcurrencyTest"

if $MVN_CMD; then
    echo "========================================================"
    echo "SUCCESS: All HapiJpaAuthoritativePersistencePostgreSqlConcurrencyTest tests passed!"
    echo "========================================================"
    exit 0
else
    EXIT_CODE=$?
    echo "========================================================"
    echo "FAILURE: Concurrency tests failed with exit code ${EXIT_CODE}"
    echo "========================================================"
    echo "Failed test reports:"
    find hestia/mnemosyne-clinical/target/surefire-reports -name "*HapiJpaAuthoritativePersistencePostgreSqlConcurrencyTest*.txt" -exec cat {} + || true
    exit ${EXIT_CODE}
fi
