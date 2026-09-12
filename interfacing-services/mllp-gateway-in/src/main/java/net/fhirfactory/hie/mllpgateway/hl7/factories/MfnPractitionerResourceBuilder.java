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
 * Builds FHIR R5 {@link Practitioner} and {@link PractitionerRole} resources from HL7 v2 MFN^M02 messages.
 */
@ApplicationScoped
public class MfnPractitionerResourceBuilder {

    public static final String EXTENSION_ETHNICITY = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity";
    public static final String EXTENSION_CITIZENSHIP = "http://hl7.org/fhir/StructureDefinition/patient-citizenship";
    public static final String EXTENSION_RELIGION = "http://hl7.org/fhir/StructureDefinition/patient-religion";

    private final MfnMessageExtractor extractor;

    public MfnPractitionerResourceBuilder() {
        this(new MfnMessageExtractor());
    }

    @Inject
    public MfnPractitionerResourceBuilder(MfnMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new MfnMessageExtractor();
    }

    public List<Practitioner> buildPractitioners(Terser terser, String rawMessage, String messageControlId) {
        List<Practitioner> practitioners = new ArrayList<>();
        if (StringUtils.isBlank(rawMessage)) {
            return practitioners;
        }

        String mfeEventCode = extractor.getTerserValue(terser, "/MFE-1", "MFE-1");
        boolean isDeactivated = "MDL".equalsIgnoreCase(mfeEventCode) || "MDC".equalsIgnoreCase(mfeEventCode);

        Pattern stfPattern = Pattern.compile("^STF\\|(.*?)$", Pattern.MULTILINE);
        Matcher stfMatcher = stfPattern.matcher(rawMessage);

        int stfIndex = 1;
        while (stfMatcher.find()) {
            String stfLine = stfMatcher.group(1);
            String[] fields = ("STF|" + stfLine).split("\\|", -1);

            String stf1PrimaryId = fields.length > 1 ? fields[1] : "";
            String stf2StaffIds = fields.length > 2 ? fields[2] : "";
            String stf3Name = fields.length > 3 ? fields[3] : "";
            String stf5Gender = fields.length > 5 ? fields[5] : "";
            String stf6Dob = fields.length > 6 ? fields[6] : "";
            String stf7Active = fields.length > 7 ? fields[7] : "";
            String stf10Phone = fields.length > 10 ? fields[10] : "";
            String stf11Address = fields.length > 11 ? fields[11] : "";
            String stf15Email = fields.length > 15 ? fields[15] : "";
            String stf18Marital = fields.length > 18 ? fields[18] : "";
            String stf33Citizenship = fields.length > 33 ? fields[33] : "";
            String stf36Language = fields.length > 36 ? fields[36] : "";
            String stf38Ethnicity = fields.length > 38 ? fields[38] : "";

            String practitionerId = resolvePractitionerId(stf1PrimaryId, stf2StaffIds, messageControlId, stfIndex);

            Practitioner practitioner = new Practitioner();
            practitioner.setId("Practitioner/" + extractor.cleanId(practitionerId));

            if (isDeactivated) {
                practitioner.setActive(false);
            } else if (StringUtils.isNotBlank(stf7Active)) {
                String act = stf7Active.trim().toUpperCase();
                practitioner.setActive("Y".equals(act) || "A".equals(act) || "ACTIVE".equals(act));
            } else {
                practitioner.setActive(true);
            }

            if (StringUtils.isNotBlank(stf1PrimaryId)) {
                String[] parts = stf1PrimaryId.split("\\^");
                Identifier id = practitioner.addIdentifier();
                id.setUse(Identifier.IdentifierUse.OFFICIAL);
                id.setValue(parts[0]);
                id.getType().setText("Primary Key").addCoding()
                        .setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
                        .setCode("PRN")
                        .setDisplay("Provider number");
                if (parts.length > 2 && StringUtils.isNotBlank(parts[2])) {
                    id.setSystem(parts[2]);
                }
            }

            if (StringUtils.isNotBlank(stf2StaffIds)) {
                String[] idReps = stf2StaffIds.split("~");
                for (String rep : idReps) {
                    if (StringUtils.isNotBlank(rep)) {
                        String[] parts = rep.split("\\^");
                        Identifier id = practitioner.addIdentifier();
                        id.setValue(parts[0]);
                        if (parts.length > 3 && StringUtils.isNotBlank(parts[3])) {
                            id.getType().setText(parts[3]);
                        }
                        if (parts.length > 4 && StringUtils.isNotBlank(parts[4])) {
                            id.getType().addCoding()
                                    .setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
                                    .setCode(parts[4]);
                        }
                        if (parts.length > 1 && StringUtils.isNotBlank(parts[1])) {
                            id.setSystem("http://example.org/identifier/" + parts[1]);
                        }
                    }
                }
            }

            if (StringUtils.isNotBlank(stf3Name)) {
                String[] parts = stf3Name.split("\\^");
                HumanName name = practitioner.addName();
                name.setUse(HumanName.NameUse.OFFICIAL);
                if (parts.length > 0 && StringUtils.isNotBlank(parts[0])) name.setFamily(parts[0]);
                if (parts.length > 1 && StringUtils.isNotBlank(parts[1])) name.addGiven(parts[1]);
                if (parts.length > 2 && StringUtils.isNotBlank(parts[2])) name.addGiven(parts[2]);
                if (parts.length > 3 && StringUtils.isNotBlank(parts[3])) name.addSuffix(parts[3]);
                if (parts.length > 4 && StringUtils.isNotBlank(parts[4])) name.addPrefix(parts[4]);
                if (parts.length > 5 && StringUtils.isNotBlank(parts[5])) name.addSuffix(parts[5]);
            }

            if (StringUtils.isNotBlank(stf5Gender)) {
                practitioner.setGender(extractor.mapAdministrativeGender(stf5Gender));
            }

            if (StringUtils.isNotBlank(stf6Dob)) {
                Date dob = extractor.parseHl7Date(stf6Dob);
                if (dob != null) {
                    practitioner.setBirthDate(dob);
                }
            }

            if (StringUtils.isNotBlank(stf10Phone)) {
                String[] reps = stf10Phone.split("~");
                for (String rep : reps) {
                    if (StringUtils.isNotBlank(rep)) {
                        String[] parts = rep.split("\\^");
                        ContactPoint cp = practitioner.addTelecom();
                        String phoneVal = parts.length > 0 ? parts[0] : "";
                        String use = parts.length > 1 ? parts[1] : "WPN";
                        String equip = parts.length > 2 ? parts[2] : "PH";

                        if ("FX".equalsIgnoreCase(equip)) {
                            cp.setSystem(ContactPoint.ContactPointSystem.FAX);
                        } else if ("Internet".equalsIgnoreCase(equip) || "NET".equalsIgnoreCase(use) || phoneVal.contains("@")) {
                            cp.setSystem(ContactPoint.ContactPointSystem.EMAIL);
                        } else {
                            cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
                        }

                        if ("PRN".equalsIgnoreCase(use) || "ORN".equalsIgnoreCase(use) || "H".equalsIgnoreCase(use)) {
                            cp.setUse(ContactPoint.ContactPointUse.HOME);
                        } else if ("WPN".equalsIgnoreCase(use) || "O".equalsIgnoreCase(use)) {
                            cp.setUse(ContactPoint.ContactPointUse.WORK);
                        } else if ("MOBILE".equalsIgnoreCase(use) || "CELL".equalsIgnoreCase(use)) {
                            cp.setUse(ContactPoint.ContactPointUse.MOBILE);
                        }

                        cp.setValue(cp.getSystem() == ContactPoint.ContactPointSystem.EMAIL ? phoneVal : extractor.cleanPhoneNumber(phoneVal));
                    }
                }
            }

            if (StringUtils.isNotBlank(stf15Email)) {
                ContactPoint emailCp = practitioner.addTelecom();
                emailCp.setSystem(ContactPoint.ContactPointSystem.EMAIL);
                emailCp.setUse(ContactPoint.ContactPointUse.WORK);
                emailCp.setValue(stf15Email.trim());
            }

            if (StringUtils.isNotBlank(stf11Address)) {
                String[] addrParts = stf11Address.split("\\^");
                Address addr = practitioner.addAddress();
                addr.setUse(Address.AddressUse.WORK);
                if (addrParts.length > 0 && StringUtils.isNotBlank(addrParts[0])) addr.addLine(addrParts[0]);
                if (addrParts.length > 1 && StringUtils.isNotBlank(addrParts[1])) addr.addLine(addrParts[1]);
                if (addrParts.length > 2 && StringUtils.isNotBlank(addrParts[2])) addr.setCity(addrParts[2]);
                if (addrParts.length > 3 && StringUtils.isNotBlank(addrParts[3])) addr.setState(addrParts[3]);
                if (addrParts.length > 4 && StringUtils.isNotBlank(addrParts[4])) addr.setPostalCode(addrParts[4]);
                if (addrParts.length > 5 && StringUtils.isNotBlank(addrParts[5])) addr.setCountry(addrParts[5]);
            }

            if (StringUtils.isNotBlank(stf36Language)) {
                String[] langParts = stf36Language.split("\\^");
                Practitioner.PractitionerCommunicationComponent comm = practitioner.addCommunication();
                CodeableConcept langCode = new CodeableConcept();
                langCode.setText(langParts.length > 1 ? langParts[1] : langParts[0]);
                langCode.addCoding()
                        .setSystem("urn:ietf:bcp:47")
                        .setCode(langParts[0])
                        .setDisplay(langParts.length > 1 ? langParts[1] : langParts[0]);
                comm.setLanguage(langCode);
                comm.setPreferred(true);
            }

            populateLanSegments(practitioner, rawMessage);
            populateEduSegments(practitioner, rawMessage);
            populateCerSegments(practitioner, rawMessage);
            populateNteSegments(practitioner, rawMessage);

            practitioners.add(practitioner);
            stfIndex++;
        }

        if (practitioners.isEmpty() && StringUtils.isNotBlank(rawMessage)) {
            String primaryId = extractor.extractPractitionerId(rawMessage, terser);
            if (!"UNKNOWN".equalsIgnoreCase(primaryId)) {
                Practitioner p = new Practitioner();
                p.setId("Practitioner/" + extractor.cleanId(primaryId));
                p.setActive(true);
                p.addIdentifier().setValue(primaryId);
                String name = extractor.extractPractitionerFullName(rawMessage, terser);
                if (StringUtils.isNotBlank(name)) {
                    p.addName().setText(name);
                }
                practitioners.add(p);
            }
        }

        return practitioners;
    }

    public List<PractitionerRole> buildPractitionerRoles(Terser terser, String rawMessage,
                                                         List<Practitioner> practitioners,
                                                         List<Organization> organizations,
                                                         List<Location> locations) {
        List<PractitionerRole> roles = new ArrayList<>();
        if (practitioners == null || practitioners.isEmpty()) {
            return roles;
        }

        Practitioner primaryPractitioner = practitioners.get(0);
        String practId = primaryPractitioner.getIdPart();

        Organization primaryOrg = (organizations != null && !organizations.isEmpty()) ? organizations.get(0) : null;
        Location primaryLoc = (locations != null && !locations.isEmpty()) ? locations.get(0) : null;

        Pattern praPattern = Pattern.compile("^PRA\\|(.*?)$", Pattern.MULTILINE);
        Matcher praMatcher = praPattern.matcher(rawMessage);

        int roleIndex = 1;
        while (praMatcher.find()) {
            String praLine = praMatcher.group(1);
            String[] fields = ("PRA|" + praLine).split("\\|", -1);

            PractitionerRole role = new PractitionerRole();
            role.setId("PractitionerRole/" + extractor.cleanId(practId) + "-role-" + roleIndex);
            role.setActive(primaryPractitioner.getActive());
            role.setPractitioner(new Reference("Practitioner/" + extractor.cleanId(practId))
                    .setDisplay(extractPractitionerFullName(primaryPractitioner)));

            if (primaryOrg != null) {
                role.setOrganization(new Reference("Organization/" + primaryOrg.getIdPart()).setDisplay(primaryOrg.getName()));
            }
            if (primaryLoc != null) {
                role.addLocation(new Reference("Location/" + primaryLoc.getIdPart()).setDisplay(primaryLoc.getName()));
            }

            if (fields.length > 2 && StringUtils.isNotBlank(fields[2])) {
                String[] groupParts = fields[2].split("\\^");
                CodeableConcept roleConcept = role.addCode();
                roleConcept.setText(groupParts.length > 1 ? groupParts[1] : groupParts[0]);
                roleConcept.addCoding()
                        .setSystem("http://example.org/practitioner-group")
                        .setCode(groupParts[0])
                        .setDisplay(groupParts.length > 1 ? groupParts[1] : groupParts[0]);
            }

            if (fields.length > 3 && StringUtils.isNotBlank(fields[3])) {
                String[] titleParts = fields[3].split("\\^");
                CodeableConcept titleConcept = role.addCode();
                titleConcept.setText(titleParts.length > 1 ? titleParts[1] : titleParts[0]);
                titleConcept.addCoding()
                        .setSystem("http://terminology.hl7.org/CodeSystem/practitioner-role")
                        .setCode(titleParts[0])
                        .setDisplay(titleParts.length > 1 ? titleParts[1] : titleParts[0]);
            }

            if (fields.length > 5 && StringUtils.isNotBlank(fields[5])) {
                String[] specReps = fields[5].split("~");
                for (String spec : specReps) {
                    if (StringUtils.isNotBlank(spec)) {
                        String[] specParts = spec.split("\\^");
                        CodeableConcept specialty = role.addSpecialty();
                        specialty.setText(specParts.length > 1 ? specParts[1] : specParts[0]);
                        specialty.addCoding()
                                .setSystem(specParts.length > 2 ? "http://terminology.hl7.org/CodeSystem/" + specParts[2] : "http://snomed.info/sct")
                                .setCode(specParts[0])
                                .setDisplay(specParts.length > 1 ? specParts[1] : specParts[0]);
                    }
                }
            }

            roles.add(role);
            roleIndex++;
        }

        if (roles.isEmpty()) {
            PractitionerRole role = new PractitionerRole();
            role.setId("PractitionerRole/" + extractor.cleanId(practId) + "-role-1");
            role.setActive(primaryPractitioner.getActive());
            role.setPractitioner(new Reference("Practitioner/" + extractor.cleanId(practId))
                    .setDisplay(extractPractitionerFullName(primaryPractitioner)));

            if (primaryOrg != null) {
                role.setOrganization(new Reference("Organization/" + primaryOrg.getIdPart()).setDisplay(primaryOrg.getName()));
            }
            if (primaryLoc != null) {
                role.addLocation(new Reference("Location/" + primaryLoc.getIdPart()).setDisplay(primaryLoc.getName()));
            }

            roles.add(role);
        }

        return roles;
    }

    public Practitioner createFallbackPractitioner(String identifier) {
        Practitioner fallback = new Practitioner();
        fallback.setId("Practitioner/" + (StringUtils.isNotBlank(identifier) ? extractor.cleanId(identifier) : "unknown"));
        fallback.setActive(true);
        Identifier id = fallback.addIdentifier();
        id.setValue(StringUtils.isNotBlank(identifier) ? identifier : "unknown");
        id.getType().setText("Primary Identifier");
        fallback.addName().setText("Unknown Practitioner");
        return fallback;
    }

    public String extractPractitionerFullName(Practitioner practitioner) {
        if (practitioner == null || !practitioner.hasName()) return "Unknown Practitioner";
        HumanName name = practitioner.getNameFirstRep();
        if (StringUtils.isNotBlank(name.getText())) return name.getText();
        String given = name.hasGiven() ? String.join(" ", name.getGivenAsSingleString()) : "";
        String family = name.hasFamily() ? name.getFamily() : "";
        return extractor.buildFullName(given, null, family, null, null);
    }

    private String resolvePractitionerId(String stf1, String stf2, String messageControlId, int index) {
        if (StringUtils.isNotBlank(stf1)) {
            return stf1.split("\\^")[0].trim();
        }
        if (StringUtils.isNotBlank(stf2)) {
            return stf2.split("~")[0].split("\\^")[0].trim();
        }
        if (StringUtils.isNotBlank(messageControlId)) {
            return messageControlId + (index > 1 ? "-stf" + index : "");
        }
        return UUID.randomUUID().toString();
    }

    private void populateLanSegments(Practitioner practitioner, String rawMessage) {
        Pattern lanPattern = Pattern.compile("^LAN\\|(.*?)$", Pattern.MULTILINE);
        Matcher lanMatcher = lanPattern.matcher(rawMessage);
        while (lanMatcher.find()) {
            String[] fields = ("LAN|" + lanMatcher.group(1)).split("\\|", -1);
            if (fields.length > 2 && StringUtils.isNotBlank(fields[2])) {
                String[] langParts = fields[2].split("\\^");
                Practitioner.PractitionerCommunicationComponent comm = practitioner.addCommunication();
                CodeableConcept langCode = new CodeableConcept();
                langCode.setText(langParts.length > 1 ? langParts[1] : langParts[0]);
                langCode.addCoding()
                        .setSystem("urn:ietf:bcp:47")
                        .setCode(langParts[0])
                        .setDisplay(langParts.length > 1 ? langParts[1] : langParts[0]);
                comm.setLanguage(langCode);
            }
        }
    }

    private void populateEduSegments(Practitioner practitioner, String rawMessage) {
        Pattern eduPattern = Pattern.compile("^EDU\\|(.*?)$", Pattern.MULTILINE);
        Matcher eduMatcher = eduPattern.matcher(rawMessage);
        while (eduMatcher.find()) {
            String[] fields = ("EDU|" + eduMatcher.group(1)).split("\\|", -1);
            Practitioner.PractitionerQualificationComponent qual = practitioner.addQualification();
            if (fields.length > 3 && StringUtils.isNotBlank(fields[3])) {
                String[] degParts = fields[3].split("\\^");
                qual.getCode().setText(degParts.length > 1 ? degParts[1] : degParts[0]);
                qual.getCode().addCoding()
                        .setSystem("http://terminology.hl7.org/CodeSystem/v2-0360")
                        .setCode(degParts[0]);
            }
            if (fields.length > 5 && StringUtils.isNotBlank(fields[5])) {
                qual.setIssuer(new Reference().setDisplay(fields[5].trim()));
            }
        }
    }

    private void populateCerSegments(Practitioner practitioner, String rawMessage) {
        Pattern cerPattern = Pattern.compile("^CER\\|(.*?)$", Pattern.MULTILINE);
        Matcher cerMatcher = cerPattern.matcher(rawMessage);
        while (cerMatcher.find()) {
            String[] fields = ("CER|" + cerMatcher.group(1)).split("\\|", -1);
            Practitioner.PractitionerQualificationComponent qual = practitioner.addQualification();
            if (fields.length > 2 && StringUtils.isNotBlank(fields[2])) {
                String[] certParts = fields[2].split("\\^");
                qual.getCode().setText(certParts.length > 1 ? certParts[1] : certParts[0]);
                qual.getCode().addCoding()
                        .setSystem("http://example.org/certifications")
                        .setCode(certParts[0]);
            }
            if (fields.length > 3 && StringUtils.isNotBlank(fields[3])) {
                qual.addIdentifier().setValue(fields[3].trim());
            }
            if (fields.length > 4 && StringUtils.isNotBlank(fields[4])) {
                qual.setIssuer(new Reference().setDisplay(fields[4].trim()));
            }
        }
    }

    private void populateNteSegments(Practitioner practitioner, String rawMessage) {
        Pattern ntePattern = Pattern.compile("^NTE\\|(.*?)$", Pattern.MULTILINE);
        Matcher nteMatcher = ntePattern.matcher(rawMessage);
        while (nteMatcher.find()) {
            String[] fields = ("NTE|" + nteMatcher.group(1)).split("\\|", -1);
            if (fields.length > 3 && StringUtils.isNotBlank(fields[3])) {
                Extension ext = practitioner.addExtension();
                ext.setUrl("http://example.org/fhir/StructureDefinition/practitioner-note");
                ext.setValue(new StringType(fields[3].trim()));
            }
        }
    }
}
