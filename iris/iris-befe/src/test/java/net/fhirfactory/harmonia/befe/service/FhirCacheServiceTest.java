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

package net.fhirfactory.harmonia.befe.service;

import net.fhirfactory.harmonia.befe.security.ThemisSecurityContextProvider;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Person;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.util.CloseableIterator;
import org.infinispan.commons.util.CloseableIteratorCollection;
import org.infinispan.commons.util.CloseableIteratorSet;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FhirCacheServiceTest {

    private FhirCacheService cacheService;
    private ThemisSecurityContextProvider securityContextProvider;
    private RemoteCacheManager mockCacheManager;
    private Map<String, Map<String, String>> mockStore;

    private static <T> CloseableIterator<T> toCloseableIterator(Iterator<T> iterator) {
        return new CloseableIterator<T>() {
            @Override
            public void close() {}

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        mockCacheManager = mock(RemoteCacheManager.class);
        mockStore = new ConcurrentHashMap<>();

        when(mockCacheManager.isStarted()).thenReturn(true);
        when(mockCacheManager.getCache(anyString())).thenAnswer(inv -> {
            String cacheName = inv.getArgument(0);
            Map<String, String> cacheMap = mockStore.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>());
            RemoteCache<String, String> mockCache = mock(RemoteCache.class);
            when(mockCache.get(anyString())).thenAnswer(i -> cacheMap.get(i.getArgument(0)));
            when(mockCache.put(anyString(), anyString())).thenAnswer(i -> cacheMap.put(i.getArgument(0), i.getArgument(1)));
            when(mockCache.remove(anyString())).thenAnswer(i -> cacheMap.remove(i.getArgument(0)));

            CloseableIteratorCollection<String> mockValues = mock(CloseableIteratorCollection.class);
            when(mockValues.iterator()).thenAnswer(i -> toCloseableIterator(cacheMap.values().iterator()));
            when(mockValues.stream()).thenAnswer(i -> cacheMap.values().stream());
            when(mockValues.isEmpty()).thenAnswer(i -> cacheMap.isEmpty());
            when(mockValues.size()).thenAnswer(i -> cacheMap.size());
            doReturn(mockValues).when(mockCache).values();

            CloseableIteratorSet<String> mockKeys = mock(CloseableIteratorSet.class);
            when(mockKeys.iterator()).thenAnswer(i -> toCloseableIterator(cacheMap.keySet().iterator()));
            when(mockKeys.stream()).thenAnswer(i -> cacheMap.keySet().stream());
            when(mockKeys.isEmpty()).thenAnswer(i -> cacheMap.isEmpty());
            when(mockKeys.size()).thenAnswer(i -> cacheMap.size());
            doReturn(mockKeys).when(mockCache).keySet();

            when(mockCache.size()).thenAnswer(i -> cacheMap.size());
            when(mockCache.isEmpty()).thenAnswer(i -> cacheMap.isEmpty());
            return mockCache;
        });

        cacheService = new FhirCacheService(mockCacheManager);
        cacheService.init();
        securityContextProvider = new ThemisSecurityContextProvider();
        cacheService.setSecurityContextProvider(securityContextProvider);
    }

    @Test
    @DisplayName("Verify saveResource applies default security tag when missing and persists to RemoteCache")
    void testSaveResourceWithDefaultSecurityTag() {
        Person person = new Person();
        person.getNameFirstRep().setFamily("Taylor").addGiven("Alice");

        Person saved = cacheService.saveResource(person);
        assertThat(saved).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(saved, FhirConfidentialityEnum.N)).isTrue();
        assertThat(saved.getMeta().getSecurity()).hasSize(1);
        assertThat(saved.getMeta().getSecurityFirstRep().getCode()).isEqualTo("N");
        assertThat(saved.getMeta().getSecurityFirstRep().getSystem()).isEqualTo(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM);

        // Fetch back and check
        Person fetched = cacheService.getResource("Person", saved.getIdPart(), Person.class);
        assertThat(fetched).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched, FhirConfidentialityEnum.N)).isTrue();
        assertThat(mockStore.get("person-cache")).containsKey(saved.getIdPart());
    }

    @Test
    @DisplayName("Verify saveResource preserves explicit security tags")
    void testSaveResourcePreservesExplicitSecurityTag() {
        Organization org = new Organization();
        org.setName("Restricted Clinic");
        org.setMeta(new Meta());
        org.getMeta().addSecurity(new Coding(FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM, "R", "Restricted"));

        Organization saved = cacheService.saveResource(org);
        assertThat(saved).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(saved, FhirConfidentialityEnum.R)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(saved, FhirConfidentialityEnum.N)).isFalse();
        assertThat(saved.getMeta().getSecurity()).hasSize(1);

        Organization fetched = cacheService.getResource("Organization", saved.getIdPart(), Organization.class);
        assertThat(fetched).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched, FhirConfidentialityEnum.R)).isTrue();
        assertThat(mockStore.get("organization-cache")).containsKey(saved.getIdPart());
    }

    @Test
    @DisplayName("Verify search and searchAsBundle queries RemoteCache values")
    void testSearchAndSearchAsBundle() {
        Person person1 = new Person();
        person1.getNameFirstRep().setFamily("Smith").addGiven("Bob");
        cacheService.saveResource(person1);

        Person person2 = new Person();
        person2.getNameFirstRep().setFamily("Johnson").addGiven("Carol");
        cacheService.saveResource(person2);

        List<IBaseResource> results = cacheService.searchResources("Person", null, "Smith", null);
        assertThat(results).hasSize(1);

        Bundle bundle = cacheService.searchAsBundle("Person", null, null, null);
        assertThat(bundle.getTotal()).isEqualTo(2);
    }

    @Test
    @DisplayName("Verify deleteResource removes resource from RemoteCache")
    void testDeleteResource() {
        Person person = new Person();
        person.getNameFirstRep().setFamily("Davis");
        Person saved = cacheService.saveResource(person);

        boolean deleted = cacheService.deleteResource("Person", saved.getIdPart());
        assertThat(deleted).isTrue();
        assertThat(cacheService.getResourceJson("Person", saved.getIdPart())).isNull();
    }

    @Test
    @DisplayName("Verify valid cache miss returns null without error")
    void testValidCacheMissReturnsNull() {
        String json = cacheService.getResourceJson("Person", "non-existent-id");
        assertThat(json).isNull();

        Person person = cacheService.getResource("Person", "non-existent-id", Person.class);
        assertThat(person).isNull();
    }

    @Test
    @DisplayName("Verify active security context is accessible in FhirCacheService when bound")
    void testActiveSecurityContextAccessibleWhenBound() {
        ThemisPrincipal human = ThemisPrincipal.of("dr.watson", PrincipalType.HUMAN, "harmonia-clinical");
        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .requestingPrincipal(human)
                .executingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE)
                .securityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
                .authorities(Set.of(ThemisAuthority.of("clinical.create"), ThemisAuthority.of("clinical.read")))
                .correlationId(UUID.randomUUID().toString())
                .requestedAt(Instant.now())
                .build();

        securityContextProvider.setSecurityContext(context);

        assertThat(cacheService.getActiveSecurityContext()).contains(context);
        assertThat(cacheService.getActiveSecurityContext().get().requestingPrincipal().principalId()).isEqualTo("dr.watson");
        assertThat(cacheService.getActiveSecurityContext().get().executingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_IRIS_BEFE);
    }

    @Test
    @DisplayName("Verify active security context returns empty when unbound without error")
    void testActiveSecurityContextEmptyWhenUnbound() {
        assertThat(cacheService.getActiveSecurityContext()).isEmpty();

        Person person = new Person();
        person.getNameFirstRep().setFamily("Doe").addGiven("John");

        Person saved = cacheService.saveResource(person);
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("Verify explicit IllegalStateException when RemoteCacheManager is null")
    void testExplicitFailureWhenRemoteCacheManagerNull() {
        FhirCacheService unconfigured = new FhirCacheService(null);
        unconfigured.init();

        Person person = new Person();
        person.getNameFirstRep().setFamily("Taylor");

        assertThatThrownBy(() -> unconfigured.saveResource(person))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");

        assertThatThrownBy(() -> unconfigured.getResourceJson("Person", "id-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");

        assertThatThrownBy(() -> unconfigured.deleteResource("Person", "id-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");

        assertThatThrownBy(() -> unconfigured.searchResources("Person", null, null, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");
    }

    @Test
    @DisplayName("Verify explicit IllegalStateException when RemoteCacheManager is not started")
    void testExplicitFailureWhenRemoteCacheManagerNotStarted() {
        RemoteCacheManager unstartedManager = mock(RemoteCacheManager.class);
        when(unstartedManager.isStarted()).thenReturn(false);

        FhirCacheService service = new FhirCacheService(unstartedManager);
        service.init();

        Person person = new Person();
        person.getNameFirstRep().setFamily("Taylor");

        assertThatThrownBy(() -> service.saveResource(person))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");
    }

    @Test
    @DisplayName("Verify explicit IllegalStateException when getCache returns null")
    void testExplicitFailureWhenNamedCacheNull() {
        RemoteCacheManager manager = mock(RemoteCacheManager.class);
        when(manager.isStarted()).thenReturn(true);
        when(manager.getCache("person-cache")).thenReturn(null);

        FhirCacheService service = new FhirCacheService(manager);
        service.init();

        assertThatThrownBy(() -> service.getResourceJson("Person", "id-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");
    }

    @Test
    @DisplayName("Verify transport exceptions from RemoteCache propagate")
    void testRemoteCacheExceptionPropagates() {
        RemoteCacheManager manager = mock(RemoteCacheManager.class);
        when(manager.isStarted()).thenReturn(true);
        @SuppressWarnings("unchecked")
        RemoteCache<String, String> failingCache = mock(RemoteCache.class);
        when(failingCache.get("fail-id")).thenThrow(new HotRodClientException("HotRod transport timeout"));
        doReturn(failingCache).when(manager).getCache("person-cache");

        FhirCacheService service = new FhirCacheService(manager);
        service.init();

        assertThatThrownBy(() -> service.getResourceJson("Person", "fail-id"))
                .isInstanceOf(HotRodClientException.class)
                .hasMessageContaining("HotRod transport timeout");
    }

    @Test
    @DisplayName("Verify recovery upon cache reconnection without stale fallback state")
    void testRecoveryUponCacheReconnection() {
        FhirCacheService service = new FhirCacheService(null);
        service.init();

        Person person = new Person();
        person.getNameFirstRep().setFamily("Recovered");

        // Fails while disconnected
        assertThatThrownBy(() -> service.saveResource(person))
                .isInstanceOf(IllegalStateException.class);

        // Reconnects
        service.setRemoteCacheManager(mockCacheManager);

        // Succeeds now
        Person saved = service.saveResource(person);
        assertThat(saved).isNotNull();
        Person fetched = service.getResource("Person", saved.getIdPart(), Person.class);
        assertThat(fetched).isNotNull();
        assertThat(fetched.getNameFirstRep().getFamily()).isEqualTo("Recovered");
    }
}
