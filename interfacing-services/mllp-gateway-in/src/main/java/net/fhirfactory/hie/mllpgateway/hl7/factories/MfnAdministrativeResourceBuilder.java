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

package net.fhirfactory.hie.mllpgateway.hl7.factories;

import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.mllpgateway.hl7.MfnMessageExtractor;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds FHIR R5 {@link Organization} and {@link Location} resources from HL7 v2 MFN^M02 messages.
 */
@ApplicationScoped
public class MfnAdministrativeResourceBuilder {

    private final MfnMessageExtractor extractor;

    public MfnAdministrativeResourceBuilder() {
        this(new MfnMessageExtractor());
    }

    @Inject
    public MfnAdministrativeResourceBuilder(MfnMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new MfnMessageExtractor();
    }

    public List<Organization> buildOrganizations(Terser terser, String rawMessage, String sendingFacility, String receivingFacility) {
        List<Organization> organizations = new ArrayList<>();
        Set<String> seenOrgIds = new HashSet<>();

        if (StringUtils.isNotBlank(rawMessage)) {
            Pattern orgPattern = Pattern.compile("^ORG\\|(.*?)$", Pattern.MULTILINE);
            Matcher orgMatcher = orgPattern.matcher(rawMessage);

            while (orgMatcher.find()) {
                String orgLine = orgMatcher.group(1);
                String[] fields = ("ORG|" + orgLine).split("\\|", -1);

                String orgUnitCode = fields.length > 2 ? fields[2] : "";
                String orgUnitId = fields.length > 3 ? fields[3] : "";
                String orgName = fields.length > 5 ? fields[5] : "";
                String orgAddress = fields.length > 6 ? fields[6] : "";
                String orgPhone = fields.length > 7 ? fields[7] : "";

                String orgId = StringUtils.isNotBlank(orgUnitId) ? orgUnitId :
                        (StringUtils.isNotBlank(orgUnitCode) ? orgUnitCode : "org-" + UUID.randomUUID().toString());

                orgId = extractor.cleanId(orgId);
                if (seenOrgIds.contains(orgId)) continue;
                seenOrgIds.add(orgId);

                Organization organization = new Organization();
                organization.setId("Organization/" + orgId);
                organization.setActive(true);
                organization.setName(StringUtils.isNotBlank(orgName) ? orgName.trim() : "Organization " + orgId);

                Identifier id = organization.addIdentifier();
                id.setValue(orgId);
                if (StringUtils.isNotBlank(orgUnitCode)) {
                    organization.addType().setText(orgUnitCode);
                }

                if (StringUtils.isNotBlank(orgAddress) || StringUtils.isNotBlank(orgPhone)) {
                    ExtendedContactDetail contact = organization.addContact();
                    if (StringUtils.isNotBlank(orgAddress)) {
                        String[] addrParts = orgAddress.split("\\^");
                        Address addr = new Address();
                        addr.setUse(Address.AddressUse.WORK);
                        if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                        if (addrParts.length > 1 && StringUtils.isNotBlank(addrParts[1])) addr.addLine(addrParts[1]);
                        if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setCity(addrParts[2]);
                        if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setState(addrParts[3]);
                        if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setPostalCode(addrParts[4]);
                        if (addrParts.length > 5 && StringUtils.isNotBlank(addrParts[5])) addr.setCountry(addrParts[5]);
                        contact.setAddress(addr);
                    }
                    if (StringUtils.isNotBlank(orgPhone)) {
                        ContactPoint cp = contact.addTelecom();
                        cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
                        cp.setUse(ContactPoint.ContactPointUse.WORK);
                        cp.setValue(extractor.cleanPhoneNumber(orgPhone));
                    }
                }

                organizations.add(organization);
            }

            // Also check AFF segments
            Pattern affPattern = Pattern.compile("^AFF\\|(.*?)$", Pattern.MULTILINE);
            Matcher affMatcher = affPattern.matcher(rawMessage);
            while (affMatcher.find()) {
                String[] fields = ("AFF|" + affMatcher.group(1)).split("\\|", -1);
                String orgName = fields.length > 2 ? fields[2] : "";
                String orgAddress = fields.length > 3 ? fields[3] : "";

                if (StringUtils.isNotBlank(orgName)) {
                    String orgId = extractor.cleanId(orgName.replaceAll("[^a-zA-Z0-9-]", "-").toLowerCase());
                    if (!seenOrgIds.contains(orgId)) {
                        seenOrgIds.add(orgId);
                        Organization org = new Organization();
                        org.setId("Organization/" + orgId);
                        org.setActive(true);
                        org.setName(orgName.trim());

                        if (StringUtils.isNotBlank(orgAddress)) {
                            String[] addrParts = orgAddress.split("\\^");
                            ExtendedContactDetail contact = org.addContact();
                            Address addr = new Address();
                            addr.setUse(Address.AddressUse.WORK);
                            if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                            if (addrParts.length > 1 && StringUtils.isNotBlank(addrParts[1])) addr.addLine(addrParts[1]);
                            if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setCity(addrParts[2]);
                            if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setState(addrParts[3]);
                            if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setPostalCode(addrParts[4]);
                            if (addrParts.length > 5 && StringUtils.isNotBlank(addrParts[5])) addr.setCountry(addrParts[5]);
                            contact.setAddress(addr);
                        }

                        organizations.add(org);
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(sendingFacility)) {
            String orgId = extractor.cleanId(sendingFacility);
            if (!seenOrgIds.contains(orgId)) {
                seenOrgIds.add(orgId);
                Organization sendingOrg = new Organization();
                sendingOrg.setId("Organization/" + orgId);
                sendingOrg.setActive(true);
                sendingOrg.setName(sendingFacility.trim());
                organizations.add(sendingOrg);
            }
        }

        if (StringUtils.isNotBlank(receivingFacility)) {
            String orgId = extractor.cleanId(receivingFacility);
            if (!seenOrgIds.contains(orgId)) {
                seenOrgIds.add(orgId);
                Organization receivingOrg = new Organization();
                receivingOrg.setId("Organization/" + orgId);
                receivingOrg.setActive(true);
                receivingOrg.setName(receivingFacility.trim());
                organizations.add(receivingOrg);
            }
        }

        if (organizations.isEmpty()) {
            Organization defaultOrg = new Organization();
            defaultOrg.setId("Organization/default-org");
            defaultOrg.setActive(true);
            defaultOrg.setName("Healthcare Provider Organization");
            organizations.add(defaultOrg);
        }

        return organizations;
    }

    public List<Location> buildLocations(Terser terser, String rawMessage) {
        List<Location> locations = new ArrayList<>();
        Set<String> seenLocIds = new HashSet<>();

        if (StringUtils.isNotBlank(rawMessage)) {
            Pattern orgPattern = Pattern.compile("^ORG\\|(.*?)$", Pattern.MULTILINE);
            Matcher orgMatcher = orgPattern.matcher(rawMessage);

            while (orgMatcher.find()) {
                String[] fields = ("ORG|" + orgMatcher.group(1)).split("\\|", -1);
                String orgUnitId = fields.length > 3 ? fields[3] : "";
                String orgName = fields.length > 5 ? fields[5] : "";
                String orgAddress = fields.length > 6 ? fields[6] : "";

                if (StringUtils.isNotBlank(orgAddress)) {
                    String locId = extractor.cleanId("loc-" + (StringUtils.isNotBlank(orgUnitId) ? orgUnitId : UUID.randomUUID().toString()));
                    if (!seenLocIds.contains(locId)) {
                        seenLocIds.add(locId);
                        Location location = new Location();
                        location.setId("Location/" + locId);
                        location.setStatus(Location.LocationStatus.ACTIVE);
                        location.setName(StringUtils.isNotBlank(orgName) ? orgName.trim() + " Location" : "Location " + locId);

                        String[] addrParts = orgAddress.split("\\^");
                        Address addr = location.getAddress();
                        if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                        if (addrParts.length > 1 && StringUtils.isNotBlank(addrParts[1])) addr.addLine(addrParts[1]);
                        if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setCity(addrParts[2]);
                        if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setState(addrParts[3]);
                        if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setPostalCode(addrParts[4]);
                        if (addrParts.length > 5 && StringUtils.isNotBlank(addrParts[5])) addr.setCountry(addrParts[5]);

                        locations.add(location);
                    }
                }
            }
        }

        return locations;
    }
}
