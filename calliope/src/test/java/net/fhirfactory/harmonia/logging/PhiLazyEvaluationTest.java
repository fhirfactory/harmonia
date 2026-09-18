/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PhiLogger Lazy Supplier Evaluation Tests")
class PhiLazyEvaluationTest {

    private Logger phiLogbackLogger;
    private PhiLogger phiLogger;
    private AtomicInteger evaluationCounter;

    @BeforeEach
    void setUp() {
        PhiLoggingConfig.reset();
        phiLogbackLogger = (Logger) LoggerFactory.getLogger(DefaultPhiLogger.PHI_LOGGER_NAME);
        phiLogger = new DefaultPhiLogger(PhiLazyEvaluationTest.class.getName(), phiLogbackLogger);
        evaluationCounter = new AtomicInteger(0);
    }

    @AfterEach
    void tearDown() {
        PhiLoggingConfig.reset();
    }

    private Supplier<String> createExpensiveSupplier(String payload) {
        return () -> {
            evaluationCounter.incrementAndGet();
            return payload;
        };
    }

    @Test
    @DisplayName("TEST 9: PHI object/supplier is not evaluated when PHI mode is disabled")
    void test9_SupplierNotEvaluatedWhenPhiDisabled() {
        PhiLoggingConfig.setPhiEnabled(false);
        phiLogbackLogger.setLevel(Level.DEBUG);

        phiLogger.debug("Patient Resource: {}", createExpensiveSupplier("Expensive FHIR Bundle JSON"));
        phiLogger.trace("Patient Payload: {}", createExpensiveSupplier("Expensive HL7 Message"));

        assertThat(evaluationCounter.get())
                .as("Suppliers must NOT be evaluated when phi-enabled=false")
                .isZero();
    }

    @Test
    @DisplayName("TEST 10: PHI object/supplier is not evaluated when relevant SLF4J level is disabled")
    void test10_SupplierNotEvaluatedWhenLevelDisabled() {
        PhiLoggingConfig.setPhiEnabled(true);
        phiLogbackLogger.setLevel(Level.INFO); // DEBUG and TRACE are disabled

        phiLogger.debug("Patient Resource: {}", createExpensiveSupplier("Expensive FHIR Bundle JSON"));
        phiLogger.trace("Patient Payload: {}", createExpensiveSupplier("Expensive HL7 Message"));

        assertThat(evaluationCounter.get())
                .as("Suppliers must NOT be evaluated when SLF4J level is INFO")
                .isZero();
    }

    @Test
    @DisplayName("TEST 23: Lazy PHI serialization does not execute while disabled, but executes when enabled")
    void test23_LazySerializationExecutionSemantics() {
        // Disabled: No execution
        PhiLoggingConfig.setPhiEnabled(false);
        phiLogbackLogger.setLevel(Level.TRACE);

        phiLogger.debug("Debug payload: {}", createExpensiveSupplier("Data 1"));
        phiLogger.trace("Trace payload: {}", createExpensiveSupplier("Data 2"));
        assertThat(evaluationCounter.get()).isZero();

        // Enabled: Executes
        PhiLoggingConfig.setPhiEnabled(true);
        phiLogger.debug("Debug payload: {}", createExpensiveSupplier("Data 3"));
        assertThat(evaluationCounter.get()).isEqualTo(1);

        phiLogger.trace("Trace payload: {}", createExpensiveSupplier("Data 4"));
        assertThat(evaluationCounter.get()).isEqualTo(2);
    }
}
