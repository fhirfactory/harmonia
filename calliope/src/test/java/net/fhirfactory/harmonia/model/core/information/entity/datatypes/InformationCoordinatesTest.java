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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InformationCoordinates Strongly Typed Topic Model Tests")
public class InformationCoordinatesTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should construct InformationCoordinates with strongly typed canonical Topic references")
    void testCoordinatesConstructionAndIdentityPreservation() {
        Topic gwTopic = TopicFactory.fromName("Health.Gateway.Postgres");
        Topic epTopic = TopicFactory.fromName("Health.Endpoint.Rest");
        Topic locTopic = TopicFactory.fromName("Health.Location.HospitalA");
        Topic ownerTopic = TopicFactory.fromName("Health.Owner.HealthSystem");

        InformationCoordinates coords = InformationCoordinates.builder()
                .persistenceGateway(gwTopic)
                .gatewayEndpoint(epTopic)
                .informationLocation(locTopic)
                .informationOwner(ownerTopic)
                .build();

        assertThat(coords.persistenceGateway()).contains(gwTopic);
        assertThat(coords.gatewayEndpoint()).contains(epTopic);
        assertThat(coords.informationLocation()).contains(locTopic);
        assertThat(coords.informationOwner()).contains(ownerTopic);

        // Verify complete canonical identities are preserved
        assertThat(coords.persistenceGatewayCanonicalName()).contains(gwTopic.getCanonicalName());
        assertThat(coords.gatewayEndpointCanonicalName()).contains(epTopic.getCanonicalName());
        assertThat(coords.informationLocationCanonicalName()).contains(locTopic.getCanonicalName());
        assertThat(coords.informationOwnerCanonicalName()).contains(ownerTopic.getCanonicalName());

        // Verify derived name helpers
        assertThat(coords.persistenceGatewayName()).contains("Health.Gateway.Postgres");
        assertThat(coords.gatewayEndpointName()).contains("Health.Endpoint.Rest");
        assertThat(coords.informationLocationName()).contains("Health.Location.HospitalA");
        assertThat(coords.informationOwnerName()).contains("Health.Owner.HealthSystem");
    }

    @Test
    @DisplayName("Should serialize and deserialize InformationCoordinates preserving full Topic state")
    void testCoordinatesJsonRoundtrip() throws Exception {
        Topic gwTopic = TopicFactory.fromName("Health.Gateway.Postgres");
        Topic epTopic = TopicFactory.fromName("Health.Endpoint.Rest");

        InformationCoordinates coords = InformationCoordinates.builder()
                .persistenceGateway(gwTopic)
                .gatewayEndpoint(epTopic)
                .build();

        String json = objectMapper.writeValueAsString(coords);
        InformationCoordinates deserialized = objectMapper.readValue(json, InformationCoordinates.class);

        assertThat(deserialized.persistenceGateway()).isEqualTo(coords.persistenceGateway());
        assertThat(deserialized.gatewayEndpoint()).isEqualTo(coords.gatewayEndpoint());
        assertThat(deserialized.informationLocation()).isEmpty();
        assertThat(deserialized.informationOwner()).isEmpty();
    }
}
