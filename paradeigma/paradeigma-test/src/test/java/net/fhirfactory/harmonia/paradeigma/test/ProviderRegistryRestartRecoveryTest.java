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

package net.fhirfactory.harmonia.paradeigma.test;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.erga.registry.PractitionerChangeErgon;
import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.pragma.ProviderRegistryChangePragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.praxis.sequence.Praxis;
import net.fhirfactory.harmonia.pylai.fhir.service.ChangeRequestSubmissionService;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProviderRegistryRestartRecoveryTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private FhirResourceRepository mockRepository;
    private FhirStorageService storageService;
    private ProviderRegistryReferenceValidator referenceValidator;
    private PragmaCacheService pragmaCacheService;
    private ChangeRequestSubmissionService submissionService;

    private Map<String, FhirResourceEntity> store;
    private Map<String, Pragma> cache;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        store = new ConcurrentHashMap<>();
        cache = new ConcurrentHashMap<>();
        mockRepository = Mockito.mock(FhirResourceRepository.class);

        when(mockRepository.findByResourceTypeAndFhirId(any(), any())).thenAnswer(invocation -> {
            String type = invocation.getArgument(0);
            String id = invocation.getArgument(1);
            FhirResourceEntity entity = store.get(type + "/" + id);
            return entity != null ? Optional.of(entity) : Optional.empty();
        });

        when(mockRepository.findByResourceTypeAndDeletedFalse(any())).thenAnswer(invocation -> {
            String type = invocation.getArgument(0);
            List<FhirResourceEntity> list = new ArrayList<>();
            for (FhirResourceEntity e : store.values()) {
                if (e.getResourceType().equalsIgnoreCase(type) && !e.isDeleted()) {
                    list.add(e);
                }
            }
            return list;
        });

        when(mockRepository.save(any(FhirResourceEntity.class))).thenAnswer(invocation -> {
            FhirResourceEntity e = invocation.getArgument(0);
            store.put(e.getResourceType() + "/" + e.getFhirId(), e);
            return e;
        });

        storageService = new FhirStorageService(mockRepository);
        referenceValidator = new ProviderRegistryReferenceValidator(mockRepository);

        pragmaCacheService = Mockito.mock(PragmaCacheService.class);
        when(pragmaCacheService.savePragma(any(Pragma.class))).thenAnswer(invocation -> {
            Pragma p = invocation.getArgument(0);
            cache.put(p.getPragmaId(), p);
            return p;
        });
        when(pragmaCacheService.getPragma(any(String.class))).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            Pragma p = cache.get(id);
            return p != null ? Optional.of(p) : Optional.empty();
        });

        submissionService = new ChangeRequestSubmissionService(pragmaCacheService, null);
    }

    @AfterEach
    void tearDown() {
        if (camelContext != null) {
            try {
                camelContext.stop();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    @DisplayName("Broker & Engine Restart: In-flight Pragma resumes and completes idempotently without duplicate records")
    void testRestartRecoveryResumption() throws Exception {
        // 1. Submit a change request before crash
        Practitioner pr = new Practitioner();
        pr.setId("pract-rec-01");
        pr.addName(new HumanName().setFamily("Turing").addGiven("Alan"));
        pr.addIdentifier(new Identifier().setSystem("http://ns.electronichealth.net.au/id/hi/hpii/1.0").setValue("8003610000000088"));
        String json = fhirContext.newJsonParser().encodeResourceToString(pr);

        ChangeRequestSubmissionService.SubmissionResult subResult = submissionService.submitChangeRequest(
                ProviderRegistryConstants.OPERATION_CREATE,
                "Practitioner",
                "pract-rec-01",
                json,
                "admin",
                "pas-gateway",
                "corr-crash-1",
                null
        );

        String pragmaId = subResult.getPragmaId();
        Pragma uncompletedPragma = cache.get(pragmaId);
        assertThat(uncompletedPragma).isNotNull();
        assertThat(uncompletedPragma.getStatus()).isEqualTo(PragmaStatus.ACCEPTED);

        // 2. Simulate WorkEngine / Ponos crash & restart: Create fresh CamelContext & Praxis pipeline
        camelContext = new DefaultCamelContext();

        PractitionerChangeErgon step1 = new PractitionerChangeErgon();
        step1.setStorageService(storageService);
        step1.setReferenceValidator(referenceValidator);

        Praxis recoveryPraxis = new Praxis(ProviderRegistryConstants.PRAXIS_PROVIDER_REGISTRY_CHANGE_PIPELINE, "Provider Registry Pipeline");
        recoveryPraxis.setInputEndpoint("direct:recovery-in");
        recoveryPraxis.setPragmaCacheService(pragmaCacheService);
        recoveryPraxis.addActivity(step1);
        recoveryPraxis.configureChainedEndpoints();
        recoveryPraxis.registerRoutes(camelContext);
        camelContext.addRoutes(recoveryPraxis.createSequencePipelineRoute());
        camelContext.start();

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

        // 3. Dispatch resumed Pragma through the pipeline route
        producerTemplate.sendBody("direct:recovery-in", uncompletedPragma);

        // 4. Verify Pragma reaches COMPLETED in cache
        Pragma recovered = cache.get(pragmaId);
        assertThat(recovered.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(recovered.getOutput()).isNotEmpty();

        // 5. Verify authoritative store has exactly 1 record and is queryable
        assertThat(store.size()).isEqualTo(1);
        Practitioner storedPr = storageService.getResource("Practitioner", "pract-rec-01");
        assertThat(storedPr.getNameFirstRep().getFamily()).isEqualTo("Turing");

        // 6. Resubmitting or replaying the completed Pragma does not duplicate records
        producerTemplate.sendBody("direct:recovery-in", recovered);
        assertThat(store.size()).isEqualTo(1);
    }
}
