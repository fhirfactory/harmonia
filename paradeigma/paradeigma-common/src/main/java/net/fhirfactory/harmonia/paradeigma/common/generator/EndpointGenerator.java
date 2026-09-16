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

package net.fhirfactory.harmonia.paradeigma.common.generator;

import org.hl7.fhir.r5.model.*;

import java.util.Date;

/**
 * Deterministic generator for FHIR R5 Endpoint resources (FHIR REST, HL7 v2 MLLP, Direct Secure Messaging).
 */
public class EndpointGenerator {

    public static final String SYSTEM_ENDPOINT_CONN_TYPE = "http://terminology.hl7.org/CodeSystem/endpoint-connection-type";

    private static final String[] CONN_TYPES = {
            "hl7-fhir-rest", "hl7-fhir-msg", "hl7v2-mllp", "direct-project"
    };

    private static final String[] PAYLOAD_TYPES = {
            "application/fhir+json", "application/fhir+xml", "text/plain", "application/hl7-v2"
    };

    private final SeedRandom random;

    public EndpointGenerator() {
        this(new SeedRandom());
    }

    public EndpointGenerator(long seed) {
        this(new SeedRandom(seed));
    }

    public EndpointGenerator(SeedRandom random) {
        this.random = random != null ? random : new SeedRandom();
    }

    public Endpoint generate(String id, String orgId) {
        return generateValid(id, orgId);
    }

    public Endpoint generateValid(String id, String orgId) {
        String epId = id != null ? id : "ep-" + random.nextInt(10000, 99999);
        String connCode = random.pick(CONN_TYPES);

        Endpoint endpoint = new Endpoint();
        endpoint.setId(new IdType("Endpoint", epId));
        endpoint.setName("Harmonia Secure Gateway Endpoint (" + epId + ")");
        endpoint.setStatus(Endpoint.EndpointStatus.ACTIVE);

        endpoint.addConnectionType(new CodeableConcept().addCoding(
                new Coding(SYSTEM_ENDPOINT_CONN_TYPE, connCode, connCode.toUpperCase())
        ));

        if ("hl7v2-mllp".equals(connCode)) {
            endpoint.setAddress("mllp://gateway.health.example.org:" + random.nextInt(25700, 25799));
        } else {
            endpoint.setAddress("https://fhir.health.example.org/r5/" + epId);
        }

        if (orgId != null) {
            String ref = orgId.startsWith("Organization/") ? orgId : "Organization/" + orgId;
            endpoint.setManagingOrganization(new Reference(ref));
        }

        endpoint.addIdentifier(new Identifier()
                .setSystem("urn:harmonia:endpoint:identifier")
                .setValue("EP-" + random.nextInt(1000, 9999)));

        Endpoint.EndpointPayloadComponent payload = new Endpoint.EndpointPayloadComponent();
        payload.addMimeType(random.pick(PAYLOAD_TYPES));
        endpoint.addPayload(payload);

        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        endpoint.setMeta(meta);

        return endpoint;
    }

    public Endpoint generateIncomplete(String id) {
        String epId = id != null ? id : "ep-inc-" + random.nextInt(10000, 99999);
        Endpoint endpoint = new Endpoint();
        endpoint.setId(new IdType("Endpoint", epId));
        endpoint.setStatus(Endpoint.EndpointStatus.ACTIVE);
        // Missing connection type, address, payload
        return endpoint;
    }

    public Endpoint generateInvalid(String id) {
        String epId = id != null ? id : "ep-inv-" + random.nextInt(10000, 99999);
        Endpoint endpoint = new Endpoint();
        endpoint.setId(new IdType("Endpoint", epId));
        endpoint.setStatus(Endpoint.EndpointStatus.SUSPENDED);
        endpoint.setAddress("not-a-valid-uri-address");
        return endpoint;
    }

    public Endpoint generateBrokenReference(String id) {
        return generateValid(id, "Organization/non-existent-org-99999");
    }

    public Endpoint generateInactive(String id, String orgId) {
        Endpoint endpoint = generateValid(id, orgId);
        endpoint.setStatus(Endpoint.EndpointStatus.OFF);
        return endpoint;
    }

    public Endpoint generateUpdate(Endpoint existing, int newVersion) {
        Endpoint updated = existing.copy();
        if (updated.getMeta() == null) {
            updated.setMeta(new Meta());
        }
        updated.getMeta().setVersionId(String.valueOf(newVersion));
        updated.getMeta().setLastUpdated(new Date());
        updated.setAddress(existing.getAddress() + "/v2");
        return updated;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
