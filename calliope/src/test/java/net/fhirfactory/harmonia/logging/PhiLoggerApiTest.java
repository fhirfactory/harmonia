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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PhiLogger API Constraints & Reflection Verification")
class PhiLoggerApiTest {

    @Test
    @DisplayName("TEST 6: PhiLogger exposes no INFO method")
    void testPhiLoggerExposesNoInfoMethod() {
        Method[] methods = PhiLogger.class.getMethods();
        List<String> infoMethods = Arrays.stream(methods)
                .map(Method::getName)
                .filter(name -> name.equalsIgnoreCase("info") || name.startsWith("info"))
                .collect(Collectors.toList());

        assertThat(infoMethods)
                .as("PhiLogger interface must strictly contain NO info methods")
                .isEmpty();
    }

    @Test
    @DisplayName("TEST 7: PhiLogger exposes no WARN method")
    void testPhiLoggerExposesNoWarnMethod() {
        Method[] methods = PhiLogger.class.getMethods();
        List<String> warnMethods = Arrays.stream(methods)
                .map(Method::getName)
                .filter(name -> name.equalsIgnoreCase("warn") || name.startsWith("warn"))
                .collect(Collectors.toList());

        assertThat(warnMethods)
                .as("PhiLogger interface must strictly contain NO warn methods")
                .isEmpty();
    }

    @Test
    @DisplayName("TEST 8: PhiLogger exposes no ERROR method")
    void testPhiLoggerExposesNoErrorMethod() {
        Method[] methods = PhiLogger.class.getMethods();
        List<String> errorMethods = Arrays.stream(methods)
                .map(Method::getName)
                .filter(name -> name.equalsIgnoreCase("error") || name.startsWith("error"))
                .collect(Collectors.toList());

        assertThat(errorMethods)
                .as("PhiLogger interface must strictly contain NO error methods")
                .isEmpty();
    }

    @Test
    @DisplayName("PhiLogger exposes ONLY debug, trace, and level check methods")
    void testPhiLoggerAllowedMethodsOnly() {
        Method[] methods = PhiLogger.class.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            assertThat(name)
                    .as("Method %s on PhiLogger is permitted", name)
                    .matches("^(debug|trace|isDebugEnabled|isTraceEnabled)$");
        }
    }

    @Test
    @DisplayName("DefaultPhiLogger exposes NO info, warn, or error methods")
    void testDefaultPhiLoggerHasNoOperationalMethods() {
        Method[] methods = DefaultPhiLogger.class.getDeclaredMethods();
        List<String> operationalMethods = Arrays.stream(methods)
                .map(Method::getName)
                .filter(name -> name.startsWith("info") || name.startsWith("warn") || name.startsWith("error"))
                .collect(Collectors.toList());

        assertThat(operationalMethods)
                .as("DefaultPhiLogger implementation must have NO info, warn, or error methods")
                .isEmpty();
    }

    @Test
    @DisplayName("PhiLoggerFactory provides cached, non-null logger instances")
    void testPhiLoggerFactoryReturnsNonNull() {
        PhiLogger logger1 = PhiLoggerFactory.getLogger(PhiLoggerApiTest.class);
        PhiLogger logger2 = PhiLoggerFactory.getLogger(PhiLoggerApiTest.class.getName());
        PhiLogger logger3 = PhiLoggerFactory.getLogger(PhiLoggerApiTest.class);

        assertThat(logger1).isNotNull();
        assertThat(logger2).isNotNull();
        assertThat(logger3).isSameAs(logger1);
    }
}
