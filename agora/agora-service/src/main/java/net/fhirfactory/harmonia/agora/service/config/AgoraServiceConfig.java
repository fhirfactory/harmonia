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

package net.fhirfactory.harmonia.agora.service.config;

import net.fhirfactory.harmonia.agora.core.identity.AgoraIdentityService;
import net.fhirfactory.harmonia.agora.core.lifecycle.AgoraCollaborationLifecycleService;
import net.fhirfactory.harmonia.agora.core.messaging.AgoraPetasosEventProducer;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingRepository;
import net.fhirfactory.harmonia.agora.core.reconciliation.AgoraMembershipReconciliationService;
import net.fhirfactory.harmonia.agora.core.security.AgoraCollaborationPolicy;
import net.fhirfactory.harmonia.agora.matrix.admin.SynapseAdministrationGateway;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixClientAdapter;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.api.producer.PetasosProducer;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

/**
 * Spring configuration providing default beans for Matrix adapters, Synapse admin gateway,
 * and Petasos event integration governed by Themis.
 */
@Configuration
public class AgoraServiceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgoraServiceConfig.class);

    @Bean
    @ConditionalOnMissingBean
    public MatrixClientAdapter matrixClientAdapter(AgoraProperties agoraProperties) {
        String baseUrl = agoraProperties.getSynapse().getBaseUrl();
        String asToken = agoraProperties.getSecurity().getAsToken() != null ? agoraProperties.getSecurity().getAsToken() : "";
        return new MatrixClientAdapter(baseUrl, asToken);
    }

    @Bean
    @ConditionalOnMissingBean
    public SynapseAdministrationGateway synapseAdministrationGateway(AgoraProperties agoraProperties) {
        String baseUrl = agoraProperties.getSynapse().getBaseUrl();
        String adminToken = agoraProperties.getSecurity().getAdminToken() != null ? agoraProperties.getSecurity().getAdminToken() : "";
        return new SynapseAdministrationGateway(baseUrl, adminToken);
    }

    @Bean
    @ConditionalOnMissingBean
    public ThemisAuthorizer themisAuthorizer() {
        DeterministicPolicyEvaluator evaluator = DeterministicPolicyEvaluator.withDefaultPolicies();
        evaluator.registerPolicy(new AgoraCollaborationPolicy());
        return evaluator;
    }

    @Bean
    @ConditionalOnMissingBean
    public AgoraIdentityService agoraIdentityService(
            SynapseAdministrationGateway adminGateway,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer,
            AgoraProperties agoraProperties
    ) {
        String serverName = agoraProperties.getMatrix().getServerName();
        return new AgoraIdentityService(adminGateway, mappingRepository, themisAuthorizer, serverName);
    }

    @Bean
    @ConditionalOnMissingBean
    public AgoraCollaborationLifecycleService agoraCollaborationLifecycleService(
            MatrixClientAdapter matrixClientAdapter,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer
    ) {
        return new AgoraCollaborationLifecycleService(matrixClientAdapter, mappingRepository, themisAuthorizer);
    }

    @Bean
    @ConditionalOnMissingBean
    public AgoraMembershipReconciliationService agoraMembershipReconciliationService(
            MatrixClientAdapter matrixClientAdapter,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer
    ) {
        return new AgoraMembershipReconciliationService(matrixClientAdapter, mappingRepository, themisAuthorizer);
    }

    @Bean
    @ConditionalOnMissingBean
    public PetasosProducer petasosProducer() {
        return new DefaultLoggingPetasosProducer();
    }

    @Bean
    @ConditionalOnMissingBean
    public AgoraPetasosEventProducer agoraPetasosEventProducer(PetasosProducer petasosProducer, ThemisAuthorizer themisAuthorizer) {
        return new AgoraPetasosEventProducer(petasosProducer, themisAuthorizer);
    }

    /**
     * Default PetasosProducer implementation used when no Artemis JMS broker is configured.
     */
    static class DefaultLoggingPetasosProducer implements PetasosProducer {

        @Override
        public void send(PetasosDestination destination, PetasosMessage message) {
            LOGGER.info("Petasos dispatch destination={}, messageId={}",
                    destination != null ? destination.getName() : "null",
                    message != null ? message.getMessageId() : "null");
        }

        @Override
        public CompletableFuture<Void> sendAsync(PetasosDestination destination, PetasosMessage message) {
            send(destination, message);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void close() {
            // No-op
        }
    }
}
