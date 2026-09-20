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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.test.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Architectural tests enforcing strict unidirectional layering across Harmonia
 * subproject packages: Calliope -> Themis API -> Hestia / Petasos API -> Energeia / Pylai.
 */
public class PackageLayeringArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void loadClasses() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia");
    }

    @Test
    @DisplayName("ArchUnit: Calliope canonical models must not depend on higher-layer subprojects")
    void calliopeMustNotDependOnHigherLayers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.calliope..")
                .or().resideInAPackage("net.fhirfactory.harmonia.model..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "net.fhirfactory.harmonia.themis.core..",
                        "net.fhirfactory.harmonia.themis.audit..",
                        "net.fhirfactory.harmonia.hestia..",
                        "net.fhirfactory.harmonia.petasos.core..",
                        "net.fhirfactory.harmonia.petasos.artemis..",
                        "net.fhirfactory.harmonia.erga..",
                        "net.fhirfactory.harmonia.praxis..",
                        "net.fhirfactory.harmonia.ponos..",
                        "net.fhirfactory.harmonia.mllpgateway..",
                        "net.fhirfactory.harmonia.gateway..",
                        "net.fhirfactory.harmonia.iris..",
                        "net.fhirfactory.harmonia.agora..",
                        "net.fhirfactory.harmonia.paradeigma.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Themis API must not depend on engine implementations or higher layers")
    void themisApiMustNotDependOnHigherLayers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.themis.api..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "net.fhirfactory.harmonia.themis.core..",
                        "net.fhirfactory.harmonia.themis.audit..",
                        "net.fhirfactory.harmonia.hestia..",
                        "net.fhirfactory.harmonia.petasos..",
                        "net.fhirfactory.harmonia.erga..",
                        "net.fhirfactory.harmonia.praxis..",
                        "net.fhirfactory.harmonia.ponos..",
                        "net.fhirfactory.harmonia.mllpgateway..",
                        "net.fhirfactory.harmonia.gateway..",
                        "net.fhirfactory.harmonia.iris..",
                        "net.fhirfactory.harmonia.agora..",
                        "net.fhirfactory.harmonia.paradeigma.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Petasos API must not depend on Pylai gateways, Energeia workflow, or Iris")
    void petasosApiMustNotDependOnWorkflowOrGateways() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.petasos.api..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "net.fhirfactory.harmonia.erga..",
                        "net.fhirfactory.harmonia.praxis..",
                        "net.fhirfactory.harmonia.ponos..",
                        "net.fhirfactory.harmonia.mllpgateway..",
                        "net.fhirfactory.harmonia.gateway..",
                        "net.fhirfactory.harmonia.iris..",
                        "net.fhirfactory.harmonia.agora..",
                        "net.fhirfactory.harmonia.paradeigma.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Ponos workflow runtime must not depend on Artemis server packages")
    void ponosMustNotDependOnArtemisServer() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage("net.fhirfactory.harmonia.ponos..", "net.fhirfactory.harmonia.praxis..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.apache.activemq.artemis.core.server..",
                        "org.apache.activemq.artemis.jms.server.."
                );

        rule.check(importedClasses);
    }
}
