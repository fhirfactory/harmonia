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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.mllpgateway.hl7.IncomingOruMessageProcessor;
import net.fhirfactory.harmonia.mllpgateway.hl7.OruProcessingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/oru")
@ApplicationScoped
public class OruResource {

    private static final Logger log = LoggerFactory.getLogger(OruResource.class);

    @Inject
    private IncomingOruMessageProcessor incomingOruMessageProcessor;

    public OruResource() {
    }

    public OruResource(IncomingOruMessageProcessor incomingOruMessageProcessor) {
        this.incomingOruMessageProcessor = incomingOruMessageProcessor;
    }

    @POST
    @Consumes({MediaType.TEXT_PLAIN, "application/hl7-v2", "text/plain"})
    @Produces(MediaType.TEXT_PLAIN)
    public Response ingestOruMessage(String rawHl7Message) {
        log.info("Received HTTP POST ORU message ingestion request");
        OruProcessingResult result = incomingOruMessageProcessor.processOruMessage(rawHl7Message);
        if (result.isSuccess()) {
            return Response.ok(result.getAckMessage()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getAckMessage()).build();
        }
    }
}
