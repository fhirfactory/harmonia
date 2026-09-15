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

package net.fhirfactory.harmonia.mllpgateway.rest;

import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.mllpgateway.hl7.IncomingMfnMessageProcessor;
import net.fhirfactory.harmonia.mllpgateway.hl7.MfnProcessingResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

@Path("/hl7/mfn")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.TEXT_PLAIN, "application/hl7-v2", "*/*"})
public class MfnResource {

    private static final Logger log = LoggerFactory.getLogger(MfnResource.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    private IncomingMfnMessageProcessor transformer;

    @Inject
    private IParser fhirParser;

    public MfnResource() {
    }

    public MfnResource(IncomingMfnMessageProcessor transformer, IParser fhirParser) {
        this.transformer = transformer;
        this.fhirParser = fhirParser;
    }

    public void setTransformer(IncomingMfnMessageProcessor transformer) {
        this.transformer = transformer;
    }

    public IncomingMfnMessageProcessor getTransformer() {
        return transformer;
    }

    public void setFhirParser(IParser fhirParser) {
        this.fhirParser = fhirParser;
    }

    public IParser getFhirParser() {
        return fhirParser;
    }

    @POST
    public Response ingestMfn(String hl7Payload) {
        if (StringUtils.isBlank(hl7Payload)) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("success", false);
            error.put("error", "HL7 message payload cannot be empty");
            return Response.status(Response.Status.BAD_REQUEST).entity(toJson(error)).build();
        }

        MfnProcessingResult result = transformer.processMfnMessage(hl7Payload);

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("success", result.isSuccess());
        responseMap.put("messageControlId", result.getMessageControlId());
        responseMap.put("triggerEvent", result.getTriggerEvent());
        if (result.getTopic() != null) {
            responseMap.put("topic", result.getTopic());
            responseMap.put("topicString", result.getTopic().toTopicString());
        }
        responseMap.put("practitionerId", result.getPractitionerId());
        responseMap.put("practitionerName", result.getPractitionerName());
        responseMap.put("ackMessage", result.getAckMessage());

        if (result.getCommunication() != null) {
            responseMap.put("communicationId", result.getCommunication().getIdPart());
            if (fhirParser != null) {
                responseMap.put("communicationJson", fhirParser.encodeResourceToString(result.getCommunication()));
            }
        }
        if (result.getTask() != null) {
            responseMap.put("taskId", result.getTask().getIdPart());
            if (fhirParser != null) {
                responseMap.put("taskJson", fhirParser.encodeResourceToString(result.getTask()));
            }
        }
        if (result.getProvenance() != null) {
            responseMap.put("provenanceId", result.getProvenance().getIdPart());
            if (fhirParser != null) {
                responseMap.put("provenanceJson", fhirParser.encodeResourceToString(result.getProvenance()));
            }
        }
        if (!result.isSuccess()) {
            responseMap.put("error", result.getErrorMessage());
            return Response.status(422).entity(toJson(responseMap)).build();
        }

        return Response.ok(toJson(responseMap)).build();
    }

    private String toJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.error("Error serializing response: {}", e.getMessage(), e);
            return "{\"error\":\"Serialization error\"}";
        }
    }
}
