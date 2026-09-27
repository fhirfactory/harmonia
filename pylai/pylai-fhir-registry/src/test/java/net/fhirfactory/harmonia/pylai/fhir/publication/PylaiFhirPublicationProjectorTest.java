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

package net.fhirfactory.harmonia.pylai.fhir.publication;

import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityCodeSystem;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CanonicalType;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ContactPoint;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Integer64Type;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PylaiFhirPublicationProjector & FhirPublicationPolicy Tests")
class PylaiFhirPublicationProjectorTest {

    private static final String AUTHORITATIVE_VERSION_EXT_URL =
            "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private FhirPublicationPolicy defaultPolicy;
    private PylaiFhirPublicationProjector defaultProjector;

    @BeforeEach
    void setUp() {
        defaultPolicy = FhirPublicationPolicy.defaultPolicy();
        defaultProjector = new PylaiFhirPublicationProjector(defaultPolicy);
    }

    @Nested
    @DisplayName("Non-Destructive Projection Guarantees")
    class NonDestructiveProjectionTests {

        @Test
        @DisplayName("Projecting a Practitioner does not mutate the source in-memory instance")
        void testProjectResourceDoesNotMutateSource() {
            Practitioner source = new Practitioner();
            source.setId("practitioner-123");

            Meta meta = new Meta();
            meta.setVersionId("42");
            meta.setLastUpdated(new Date());
            meta.addExtension(new Extension(AUTHORITATIVE_VERSION_EXT_URL, new Integer64Type(42L)));
            meta.addSecurity(new Coding(HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM, "PROVIDER_REGISTRY", "Provider Registry"));
            meta.addSecurity(new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "R", "Restricted"));
            source.setMeta(meta);

            // Add private and unapproved extensions
            source.addExtension(new Extension("http://harmonia.fhirfactory.net/operational/internal-routing", new StringType("node-alpha")));
            source.addExtension(new Extension("http://example.org/unapproved-ext", new StringType("random-value")));

            // Add approved AU extension
            source.addExtension(new Extension("http://hl7.org.au/fhir/StructureDefinition/au-practitioner-role-code", new StringType("253111")));

            // Deep clone / project
            Practitioner projected = defaultProjector.projectForPublication(source);

            // Verify projected resource is a distinct instance
            assertThat(projected).isNotSameAs(source);
            assertThat(projected.getIdPart()).isEqualTo("practitioner-123");
            assertThat(projected.getMeta().getVersionId()).isEqualTo("42");

            // Verify projected resource has stripped operational and unapproved metadata
            assertThat(projected.getMeta().getExtension()).isEmpty();
            assertThat(projected.getMeta().getSecurity()).hasSize(1);
            assertThat(projected.getMeta().getSecurity().get(0).getCode()).isEqualTo("R");
            assertThat(projected.getMeta().getSecurity().get(0).getSystem()).isEqualTo(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM);

            assertThat(projected.getExtension()).hasSize(1);
            assertThat(projected.getExtension().get(0).getUrl()).isEqualTo("http://hl7.org.au/fhir/StructureDefinition/au-practitioner-role-code");

            // Verify SOURCE INSTANCE is UNMODIFIED
            assertThat(source.getMeta().getExtension()).hasSize(1);
            assertThat(source.getMeta().getExtension().get(0).getUrl()).isEqualTo(AUTHORITATIVE_VERSION_EXT_URL);
            assertThat(source.getMeta().getSecurity()).hasSize(2);
            assertThat(source.getExtension()).hasSize(3);
        }

        @Test
        @DisplayName("Projecting a Bundle does not mutate source Bundle or constituent entries")
        void testProjectBundleDoesNotMutateSource() {
            Bundle sourceBundle = new Bundle();
            sourceBundle.setType(Bundle.BundleType.SEARCHSET);
            sourceBundle.setTotal(1);

            Practitioner source = new Practitioner();
            source.setId("practitioner-999");
            source.addExtension(new Extension(AUTHORITATIVE_VERSION_EXT_URL, new Integer64Type(1L)));
            source.getMeta().addSecurity(new Coding(HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM, "INTERNAL", "Internal"));

            sourceBundle.addEntry().setResource(source);

            Bundle projectedBundle = defaultProjector.projectForPublication(sourceBundle);

            assertThat(projectedBundle).isNotSameAs(sourceBundle);
            assertThat(projectedBundle.getEntry()).hasSize(1);

            Practitioner projectedPractitioner = (Practitioner) projectedBundle.getEntry().get(0).getResource();
            assertThat(projectedPractitioner).isNotSameAs(source);
            assertThat(projectedPractitioner.getExtension()).isEmpty();
            assertThat(projectedPractitioner.getMeta().getSecurity()).isEmpty();

            // Verify source is untouched
            assertThat(source.getExtension()).hasSize(1);
            assertThat(source.getMeta().getSecurity()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Contract-Driven Extension Filtering")
    class ExtensionFilteringTests {

        @Test
        @DisplayName("Excludes Harmonia operational extensions by default")
        void testExcludeHarmoniaOperationalExtensions() {
            assertThat(defaultPolicy.isExtensionPermitted(AUTHORITATIVE_VERSION_EXT_URL)).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted(PragmaFhirConverter.EXTENSION_PRAXIS_ID)).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted(PragmaFhirConverter.EXTENSION_CHECKPOINT)).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted(PragmaFhirConverter.EXTENSION_CHECKPOINT_STAGE)).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_ID)).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted(PragmaFhirConverter.LEGACY_EXTENSION_PRAXIS_ID)).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted("http://harmonia.fhirfactory.net/state/lock")).isFalse();
        }

        @Test
        @DisplayName("Permits standard FHIR core extensions")
        void testPermitStandardFhirCoreExtensions() {
            assertThat(defaultPolicy.isExtensionPermitted("http://hl7.org/fhir/StructureDefinition/patient-birthPlace")).isTrue();
            assertThat(defaultPolicy.isExtensionPermitted("http://hl7.org/fhir/StructureDefinition/individual-genderIdentity")).isTrue();
            assertThat(defaultPolicy.isExtensionPermitted("http://hl7.org/fhir/5.0/StructureDefinition/extension-Practitioner.active")).isTrue();
        }

        @Test
        @DisplayName("Permits standard AU Base / AU Core jurisdictional extensions")
        void testPermitAuJurisdictionalExtensions() {
            assertThat(defaultPolicy.isExtensionPermitted("http://hl7.org.au/fhir/StructureDefinition/au-practitioner-role-code")).isTrue();
            assertThat(defaultPolicy.isExtensionPermitted("http://hl7.org.au/fhir/StructureDefinition/au-medicarecardnumber")).isTrue();
            assertThat(defaultPolicy.isExtensionPermitted("http://ns.electronichealth.net.au/fhir/StructureDefinition/dh-practitionerrole-core-1")).isTrue();
        }

        @Test
        @DisplayName("Fails closed for unknown / unapproved extensions")
        void testFailClosedForUnknownExtensions() {
            assertThat(defaultPolicy.isExtensionPermitted("http://example.org/fhir/custom-extension")).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted("https://untrusted.org/metadata/tracking")).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted(null)).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted("")).isFalse();
            assertThat(defaultPolicy.isExtensionPermitted("   ")).isFalse();
        }

        @Test
        @DisplayName("Permits contract-approved third-party or Harmonia interoperability extensions when explicitly configured")
        void testContractApprovedExtensions() {
            FhirPublicationPolicy customPolicy = FhirPublicationPolicy.builder()
                    .permitExtensionUrl("http://vendor.example.com/fhir/extensions/custom-flag")
                    .permitExtensionUrl("http://fhirfactory.net/harmonia/interop/extension/external-routing")
                    .permitExtensionPrefix("http://custom-jurisdiction.gov/fhir/")
                    .build();

            PylaiFhirPublicationProjector customProjector = new PylaiFhirPublicationProjector(customPolicy);

            assertThat(customPolicy.isExtensionPermitted("http://vendor.example.com/fhir/extensions/custom-flag")).isTrue();
            assertThat(customPolicy.isExtensionPermitted("http://fhirfactory.net/harmonia/interop/extension/external-routing")).isTrue();
            assertThat(customPolicy.isExtensionPermitted("http://custom-jurisdiction.gov/fhir/StructureDefinition/id-card")).isTrue();

            // Other Harmonia operational extensions remain stripped
            assertThat(customPolicy.isExtensionPermitted(PragmaFhirConverter.EXTENSION_PRAXIS_ID)).isFalse();
        }
    }

    @Nested
    @DisplayName("Contract-Driven Security Label Filtering")
    class SecurityLabelFilteringTests {

        @Test
        @DisplayName("Permits standard HL7 v3 Confidentiality security labels")
        void testPermitClinicalConfidentialityLabels() {
            Coding labelNormal = new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "N", "Normal");
            Coding labelRestricted = new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "R", "Restricted");
            Coding labelVeryRestricted = new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "V", "Very Restricted");

            assertThat(defaultPolicy.isSecurityLabelPermitted(labelNormal)).isTrue();
            assertThat(defaultPolicy.isSecurityLabelPermitted(labelRestricted)).isTrue();
            assertThat(defaultPolicy.isSecurityLabelPermitted(labelVeryRestricted)).isTrue();
        }

        @Test
        @DisplayName("Strips Harmonia operational security labels")
        void testStripHarmoniaOperationalSecurityLabels() {
            Coding providerRegistryLabel = new Coding(HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM, "PROVIDER_REGISTRY", "Provider Registry");
            Coding internalLabel = new Coding(HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM, "INTERNAL", "Internal");
            Coding auditLabel = new Coding(HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM, "AUDIT", "Audit");
            Coding customHarmoniaSec = new Coding("http://harmonia.fhirfactory.net/security/custom", "TAG", "Tag");

            assertThat(defaultPolicy.isSecurityLabelPermitted(providerRegistryLabel)).isFalse();
            assertThat(defaultPolicy.isSecurityLabelPermitted(internalLabel)).isFalse();
            assertThat(defaultPolicy.isSecurityLabelPermitted(auditLabel)).isFalse();
            assertThat(defaultPolicy.isSecurityLabelPermitted(customHarmoniaSec)).isFalse();
        }

        @Test
        @DisplayName("Fails closed for unknown security labels unless permitted by contract")
        void testFailClosedForUnknownSecurityLabels() {
            Coding unknownLabel = new Coding("http://example.com/security", "TOP_SECRET", "Top Secret");
            assertThat(defaultPolicy.isSecurityLabelPermitted(unknownLabel)).isFalse();
            assertThat(defaultPolicy.isSecurityLabelPermitted(null)).isFalse();

            FhirPublicationPolicy customPolicy = FhirPublicationPolicy.builder()
                    .permitSecurityLabel("http://example.com/security", "TOP_SECRET")
                    .build();

            assertThat(customPolicy.isSecurityLabelPermitted(unknownLabel)).isTrue();
        }
    }

    @Nested
    @DisplayName("Conformance Profile and Tag Filtering")
    class ProfileAndTagFilteringTests {

        @Test
        @DisplayName("Permits standard profiles while stripping internal operational profiles")
        void testProfileFiltering() {
            assertThat(defaultPolicy.isProfilePermitted("http://hl7.org/fhir/StructureDefinition/Practitioner")).isTrue();
            assertThat(defaultPolicy.isProfilePermitted("http://hl7.org.au/fhir/StructureDefinition/au-practitioner")).isTrue();
            assertThat(defaultPolicy.isProfilePermitted("http://harmonia.fhirfactory.net/profiles/internal-practitioner")).isFalse();
            assertThat(defaultPolicy.isProfilePermitted("http://example.org/unknown-profile")).isFalse();
        }

        @Test
        @DisplayName("Strips Harmonia private tags and permits standard tags")
        void testTagFiltering() {
            Coding standardTag = new Coding("http://terminology.hl7.org/CodeSystem/common-tags", "actionable", "Actionable");
            Coding harmoniaTag = new Coding("http://harmonia.fhirfactory.net/tags", "active-state", "Active");

            assertThat(defaultPolicy.isTagPermitted(standardTag)).isTrue();
            assertThat(defaultPolicy.isTagPermitted(harmoniaTag)).isFalse();
        }
    }

    @Nested
    @DisplayName("Generic Recursive Element Traversal")
    class RecursiveElementTraversalTests {

        @Test
        @DisplayName("Recursively sanitizes extensions inside nested elements (Identifier, Name, Telecom)")
        void testNestedElementExtensionSanitization() {
            Practitioner practitioner = new Practitioner();
            practitioner.setId("dr-1");

            // Name with standard and unapproved extension
            HumanName name = practitioner.addName();
            name.setFamily("Smith");
            name.addGiven("John");
            name.addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/iso21090-EN-qualifier", new StringType("CL")));
            name.addExtension(new Extension("http://example.org/internal-name-score", new StringType("0.98")));

            // Identifier with AU Base and Harmonia operational extension
            Identifier identifier = practitioner.addIdentifier();
            identifier.setSystem("http://ns.electronichealth.net.au/id/hi/hpii/1.0");
            identifier.setValue("8003610000000000");
            identifier.addExtension(new Extension("http://hl7.org.au/fhir/StructureDefinition/au-hpii", new StringType("valid")));
            identifier.addExtension(new Extension("http://harmonia.fhirfactory.net/identifier/verification-token", new StringType("tok-99")));

            // Telecom with unapproved extension
            ContactPoint telecom = practitioner.addTelecom();
            telecom.setValue("dr.smith@hospital.example.org");
            telecom.addExtension(new Extension("http://unapproved.org/telecom-rank", new StringType("1")));

            Practitioner projected = defaultProjector.projectForPublication(practitioner);

            // Verify Name extensions
            assertThat(projected.getName().get(0).getExtension()).hasSize(1);
            assertThat(projected.getName().get(0).getExtension().get(0).getUrl())
                    .isEqualTo("http://hl7.org/fhir/StructureDefinition/iso21090-EN-qualifier");

            // Verify Identifier extensions
            assertThat(projected.getIdentifier().get(0).getExtension()).hasSize(1);
            assertThat(projected.getIdentifier().get(0).getExtension().get(0).getUrl())
                    .isEqualTo("http://hl7.org.au/fhir/StructureDefinition/au-hpii");

            // Verify Telecom extensions
            assertThat(projected.getTelecom().get(0).getExtension()).isEmpty();
        }

        @Test
        @DisplayName("Generic Task projection strips operational execution context while preserving core Task properties")
        void testGenericTaskProjection() {
            Task task = new Task();
            task.setId("task-change-001");
            task.setStatus(Task.TaskStatus.INPROGRESS);
            task.setIntent(Task.TaskIntent.ORDER);
            task.setAuthoredOn(new Date());
            task.setDescription("Governed Provider Registry Practitioner change");
            task.setFocus(new Reference("Practitioner/123"));

            // Operational Task root extensions (Praxis ID, checkpoints, security context)
            task.addExtension(new Extension(PragmaFhirConverter.EXTENSION_PRAXIS_ID, new StringType("praxis-exec-777")));
            task.addExtension(new Extension(PragmaFhirConverter.EXTENSION_CHECKPOINT, new StringType("FANOUT_DISPATCH_INITIATED")));
            task.addExtension(new Extension(PragmaFhirConverter.EXTENSION_CHECKPOINT_STAGE, new StringType("ERGON_EXECUTION")));
            task.addExtension(new Extension(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_ID, new StringType("user-admin")));

            // Permitted standard extension on Task
            task.addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/task-replaces", new Reference("Task/task-change-000")));

            // Task output with operational and standard extensions
            Task.TaskOutputComponent output = task.addOutput();
            output.setType(new CodeableConcept().setText("change-result"));
            output.setValue(new StringType("Committed"));
            output.addExtension(new Extension("http://harmonia.fhirfactory.net/task/internal-metric", new StringType("35ms")));
            output.addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/task-output-details", new StringType("detail")));

            Task projectedTask = defaultProjector.projectForPublication(task);

            // Core attributes intact
            assertThat(projectedTask.getIdPart()).isEqualTo("task-change-001");
            assertThat(projectedTask.getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);
            assertThat(projectedTask.getIntent()).isEqualTo(Task.TaskIntent.ORDER);
            assertThat(projectedTask.getDescription()).isEqualTo("Governed Provider Registry Practitioner change");
            assertThat(projectedTask.getFocus().getReference()).isEqualTo("Practitioner/123");

            // Operational extensions stripped, standard extension retained
            assertThat(projectedTask.getExtension()).hasSize(1);
            assertThat(projectedTask.getExtension().get(0).getUrl()).isEqualTo("http://hl7.org/fhir/StructureDefinition/task-replaces");

            // Output extensions sanitized
            assertThat(projectedTask.getOutput()).hasSize(1);
            assertThat(projectedTask.getOutput().get(0).getExtension()).hasSize(1);
            assertThat(projectedTask.getOutput().get(0).getExtension().get(0).getUrl())
                    .isEqualTo("http://hl7.org/fhir/StructureDefinition/task-output-details");
        }
    }

    @Nested
    @DisplayName("Edge Cases & Null Safety")
    class EdgeCasesAndNullSafetyTests {

        @Test
        @DisplayName("Handles null input gracefully")
        void testNullHandling() {
            assertThat(defaultProjector.projectForPublication((Practitioner) null)).isNull();
            assertThat(defaultProjector.projectResource((Practitioner) null)).isNull();
            assertThat(defaultProjector.projectBundle(null)).isNull();
        }

        @Test
        @DisplayName("Handles resources without meta gracefully")
        void testResourceWithoutMeta() {
            Practitioner practitioner = new Practitioner();
            practitioner.setId("simple-practitioner");

            Practitioner projected = defaultProjector.projectForPublication(practitioner);
            assertThat(projected).isNotNull();
            assertThat(projected.getIdPart()).isEqualTo("simple-practitioner");
        }
    }
}
