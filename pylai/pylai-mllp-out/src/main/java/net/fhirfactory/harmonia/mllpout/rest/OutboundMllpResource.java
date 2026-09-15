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

package net.fhirfactory.harmonia.mllpout.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationConfig;
import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationRegistry;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.mllpout.config.MllpOutboundConfig;
import net.fhirfactory.harmonia.mllpout.consumer.OutboundTaskQueueConsumer;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JAX-RS REST Resource for outbound MLLP dispatch and destination administration.
 */
@Path("/mllp/outbound")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OutboundMllpResource {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundMllpResource.class);

    @Inject
    private OutboundTaskQueueConsumer outboundConsumer;

    @Inject
    private MllpDestinationRegistry destinationRegistry;

    @Inject
    private MllpOutboundConfig outboundConfig;

    public OutboundMllpResource() {
    }

    public OutboundMllpResource(OutboundTaskQueueConsumer outboundConsumer,
                                MllpDestinationRegistry destinationRegistry,
                                MllpOutboundConfig outboundConfig) {
        this.outboundConsumer = outboundConsumer;
        this.destinationRegistry = destinationRegistry;
        this.outboundConfig = outboundConfig;
    }

    /**
     * Synchronous MLLP message dispatch endpoint.
     */
    @POST
    @Path("/send")
    public Response send(OutboundMllpRequest request) {
        if (request == null || StringUtils.isBlank(request.getRawMessage())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Request body and rawMessage are required"))
                    .build();
        }

        LOG.info("Received REST send request: requestId={}, destinationId={}",
                request.getRequestId(), request.getDestinationId());

        try {
            OutboundMllpResponse response = outboundConsumer.processOutboundRequest(request);
            if (response.isSuccessful()) {
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.ACCEPTED).entity(response).build();
            }
        } catch (Exception e) {
            LOG.error("Error during REST MLLP dispatch: {}", e.getMessage(), e);
            OutboundMllpResponse failure = OutboundMllpResponse.failure(
                    request.getRequestId(), request.getMessageControlId(), request.getDestinationId(),
                    e.getMessage(), 0L);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(failure).build();
        }
    }

    /**
     * Synchronous ADT event dispatch endpoint.
     */
    @POST
    @Path("/adt/send")
    public Response sendAdt(OutboundMllpRequest request) {
        if (request == null) {
            request = new OutboundMllpRequest();
        }
        if (request.getTopic() == null) {
            request.setTopic(Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", request.getDestinationId()));
        }
        return send(request);
    }

    /**
     * Synchronous MFN event dispatch endpoint.
     */
    @POST
    @Path("/mfn/send")
    public Response sendMfn(OutboundMllpRequest request) {
        if (request == null) {
            request = new OutboundMllpRequest();
        }
        if (request.getTopic() == null) {
            request.setTopic(Topic.forEgress("MFN", "M02", "harmonia", "mllp-out", request.getDestinationId()));
        }
        return send(request);
    }

    /**
     * Health and status inquiry for this gateway instance.
     */
    @GET
    @Path("/health")
    public Response health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        if (outboundConfig != null) {
            status.put("instanceId", outboundConfig.getInstanceId());
            status.put("targetEndpointId", outboundConfig.getTargetEndpointId());
            status.put("dedicatedQueue", outboundConfig.getDedicatedEventQueueName());
            status.put("restPort", outboundConfig.getRestPort());
        }
        if (outboundConsumer != null) {
            status.put("consumerRunning", outboundConsumer.isRunning());
        }
        if (destinationRegistry != null) {
            status.put("registeredDestinationsCount", destinationRegistry.size());
        }
        return Response.ok(status).build();
    }

    /**
     * Destination Registry: list all registered destinations.
     */
    @GET
    @Path("/destinations")
    public Response getDestinations() {
        if (destinationRegistry == null) {
            return Response.ok(List.of()).build();
        }
        return Response.ok(destinationRegistry.getAllDestinations()).build();
    }

    /**
     * Destination Registry: get destination by ID.
     */
    @GET
    @Path("/destinations/{id}")
    public Response getDestinationById(@PathParam("id") String id) {
        if (destinationRegistry == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Optional<MllpDestinationConfig> dest = destinationRegistry.getDestination(id);
        return dest.map(config -> Response.ok(config).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Destination not found: " + id))
                        .build());
    }

    /**
     * Destination Registry: register or update a destination.
     */
    @POST
    @Path("/destinations")
    public Response registerDestination(MllpDestinationConfig config) {
        if (config == null || StringUtils.isBlank(config.getDestinationId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "destinationId is required"))
                    .build();
        }
        if (destinationRegistry != null) {
            destinationRegistry.registerDestination(config);
            return Response.status(Response.Status.CREATED).entity(config).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * Destination Registry: delete a destination.
     */
    @DELETE
    @Path("/destinations/{id}")
    public Response deleteDestination(@PathParam("id") String id) {
        if (destinationRegistry != null) {
            MllpDestinationConfig removed = destinationRegistry.unregisterDestination(id);
            if (removed != null) {
                return Response.ok(Map.of("status", "deleted", "destinationId", id)).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
