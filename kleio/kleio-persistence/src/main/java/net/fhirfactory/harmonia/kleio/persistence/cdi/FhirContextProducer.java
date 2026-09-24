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

package net.fhirfactory.harmonia.kleio.persistence.cdi;

import ca.uhn.fhir.context.FhirContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper;

/**
 * CDI producer providing a thread-safe singleton {@link FhirContext} and {@link HarmoniaAuditEventMapper}.
 */
@ApplicationScoped
public class FhirContextProducer {

    private final FhirContext fhirContext = FhirContext.forR5();
    private final HarmoniaAuditEventMapper mapper = new HarmoniaAuditEventMapper();

    @Produces
    @ApplicationScoped
    public FhirContext produceFhirContext() {
        return fhirContext;
    }

    @Produces
    @ApplicationScoped
    public HarmoniaAuditEventMapper produceMapper() {
        return mapper;
    }
}
