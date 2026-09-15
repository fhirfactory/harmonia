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

package net.fhirfactory.harmonia.erga.hl7v2x.factories;

import ca.uhn.hl7v2.util.Terser;
import net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport.*;

/**
 * Builds FHIR R5 {@link Practitioner} and {@link PractitionerRole} resources from HL7 v2 MFN^M02 messages.
 */
public class MfnPractitionerResourceBuilder {

    public static final String EXTENSION_ETHNICITY = Hl7v2ParsingSupport.EXTENSION_ETHNICITY;
    public static final String EXTENSION_CITIZENSHIP = Hl7v2ParsingSupport.EXTENSION_CITIZENSHIP;
    public static final String EXTENSION_RELIGION = Hl7v2ParsingSupport.EXTENSION_RELIGION;

    /**
     * Builds all Practitioner resources from an MFN^M02 message.
     *
     * @param terser           optional HAPI Terser instance
     * @param rawMessage       raw HL7 v2 MFN message string
     * @param messageControlId message control ID for fallback ID generation
     * @return list of populated Practitioner resources
     */
    public List<Practitioner> buildPractitioners(Terser terser, String rawMessage, String messageControlId) {
        List<Practitioner> practitioners = new ArrayList<>();
        if (StringUtils.isBlank(rawMessage)) {
            return practitioners;
        }

        // Check for MFE record level event code
        String mfeEventCode = extractTerserOrRegex(terser, "/MFE-1", rawMessage, "MFE", 1, null);
        boolean isDeactivated = "MDL".equalsIgnoreCase(mfeEventCode) || "MDC".equalsIgnoreCase(mfeEventCode);

        // Find all STF segments
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

            // Determine Practitioner ID
            String practitionerId = resolvePractitionerId(stf1PrimaryId, stf2StaffIds, messageControlId, stfIndex);

            Practitioner practitioner = new Practitioner();
            practitioner.setId("Practitioner/" + cleanId(practitionerId));

            // Set Active flag
            if (isDeactivated) {
                practitioner.setActive(false);
            } else if (StringUtils.isNotBlank(stf7Active)) {
                String act = stf7Active.trim().toUpperCase();
                practitioner.setActive("Y".equals(act) || "A".equals(act) || "ACTIVE".equals(act));
            } else {
                practitioner.setActive(true);
            }

            // Identifiers: STF-1
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

            // Identifiers: STF-2 (repeating with ~)
            if (StringUtils.isNotBlank(stf2StaffIds)) {
                String[] idEntries = stf2StaffIds.split("~");
                for (String entry : idEntries) {
                    if (StringUtils.isNotBlank(entry)) {
                        String[] parts = entry.split("\\^");
                        Identifier id = practitioner.addIdentifier();
                        id.setValue(parts[0]);
                        if (parts.length > 4 && StringUtils.isNotBlank(parts[4])) {
                            String typeCode = parts[4];
                            id.getType().setText(typeCode);
                            if ("NPI".equalsIgnoreCase(typeCode)) {
                                id.setSystem("http://hl7.org/fhir/sid/us-npi");
                                id.getType().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("NPI").setDisplay("National Provider Identifier");
                            } else if ("TAX".equalsIgnoreCase(typeCode) || "TAXID".equalsIgnoreCase(typeCode)) {
                                id.getType().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("TAX").setDisplay("Tax ID number");
                            } else if ("MD".equalsIgnoreCase(typeCode) || "PRN".equalsIgnoreCase(typeCode)) {
                                id.getType().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("PRN").setDisplay("Provider number");
                            } else {
                                id.getType().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode(typeCode);
                            }
                        }
                        if (parts.length > 3 && StringUtils.isNotBlank(parts[3]) && !id.hasSystem()) {
                            id.setSystem("http://fhirfactory.net/hie/identifier/" + parts[3].toLowerCase());
                        }
                    }
                }
            }

            // HumanName: STF-3 (repeating with ~)
            if (StringUtils.isNotBlank(stf3Name)) {
                String[] nameEntries = stf3Name.split("~");
                for (String nameEntry : nameEntries) {
                    if (StringUtils.isNotBlank(nameEntry)) {
                        HumanName name = parseHumanName(nameEntry);
                        practitioner.addName(name);
                    }
                }
            }

            // Administrative Gender: STF-5
            if (StringUtils.isNotBlank(stf5Gender)) {
                practitioner.setGender(mapAdministrativeGender(stf5Gender));
            }

            // Date of Birth: STF-6
            if (StringUtils.isNotBlank(stf6Dob)) {
                Date dob = parseHl7Date(stf6Dob);
                if (dob != null) {
                    practitioner.setBirthDate(dob);
                }
            }

            // Telecom: STF-10 (repeating with ~)
            if (StringUtils.isNotBlank(stf10Phone)) {
                String[] phoneEntries = stf10Phone.split("~");
                for (String pEntry : phoneEntries) {
                    if (StringUtils.isNotBlank(pEntry)) {
                        ContactPoint cp = parseContactPoint(pEntry);
                        if (cp != null) {
                            practitioner.addTelecom(cp);
                        }
                    }
                }
            }

            // Telecom: STF-15 (Email)
            if (StringUtils.isNotBlank(stf15Email)) {
                ContactPoint emailCp = practitioner.addTelecom();
                emailCp.setSystem(ContactPoint.ContactPointSystem.EMAIL);
                emailCp.setUse(ContactPoint.ContactPointUse.WORK);
                emailCp.setValue(stf15Email.trim());
            }

            // Address: STF-11 (repeating with ~)
            if (StringUtils.isNotBlank(stf11Address)) {
                String[] addrEntries = stf11Address.split("~");
                for (String addrEntry : addrEntries) {
                    if (StringUtils.isNotBlank(addrEntry)) {
                        Address addr = parseAddress(addrEntry);
                        practitioner.addAddress(addr);
                    }
                }
            }

            // Communication / Language: STF-36 and LAN segments
            populatePractitionerLanguages(practitioner, stf36Language, rawMessage);

            // Qualifications: PRA-6, EDU, CER segments
            populatePractitionerQualifications(practitioner, rawMessage);

            // Extensions: Ethnicity, Citizenship, Marital Status
            if (StringUtils.isNotBlank(stf38Ethnicity)) {
                String[] ethParts = stf38Ethnicity.split("\\^");
                Extension ethExt = new Extension(EXTENSION_ETHNICITY);
                ethExt.setValue(new Coding("http://terminology.hl7.org/CodeSystem/v2-0189", ethParts[0], ethParts.length > 1 ? ethParts[1] : ethParts[0]));
                practitioner.addExtension(ethExt);
            }

            if (StringUtils.isNotBlank(stf33Citizenship)) {
                String[] citParts = stf33Citizenship.split("\\^");
                Extension citExt = new Extension(EXTENSION_CITIZENSHIP);
                citExt.setValue(new CodeableConcept().setText(citParts.length > 1 ? citParts[1] : citParts[0]));
                practitioner.addExtension(citExt);
            }

            practitioners.add(practitioner);
            stfIndex++;
        }

        // If no STF segment was found, try fallback from MFE or messageControlId
        if (practitioners.isEmpty()) {
            String mfe4 = extractTerserOrRegex(terser, "/MFE-4", rawMessage, "MFE", 4, null);
            String fallbackId = StringUtils.isNotBlank(mfe4) ? mfe4.split("\\^")[0] : messageControlId;
            Practitioner fallback = createFallbackPractitioner(fallbackId);
            practitioners.add(fallback);
        }

        return practitioners;
    }

    /**
     * Builds PractitionerRole resources corresponding to the practitioners and administrative context in an MFN message.
     *
     * @param terser        optional HAPI Terser instance
     * @param rawMessage    raw HL7 v2 MFN message string
     * @param practitioners list of created Practitioner resources
     * @param organizations list of created Organization resources
     * @param locations     list of created Location resources
     * @return list of populated PractitionerRole resources
     */
    public List<PractitionerRole> buildPractitionerRoles(Terser terser, String rawMessage,
                                                         List<Practitioner> practitioners,
                                                         List<Organization> organizations,
                                                         List<Location> locations) {
        List<PractitionerRole> roles = new ArrayList<>();
        if (practitioners == null || practitioners.isEmpty()) {
            return roles;
        }

        // Extract STF role details
        String stf4StaffType = extractTerserOrRegex(terser, "/STF-4", rawMessage, "STF", 4, null);
        String stf8Dept = extractTerserOrRegex(terser, "/STF-8", rawMessage, "STF", 8, null);
        String stf9HospService = extractTerserOrRegex(terser, "/STF-9", rawMessage, "STF", 9, null);
        String stf12ActiveDate = extractTerserOrRegex(terser, "/STF-12", rawMessage, "STF", 12, null);
        String stf13InactiveDate = extractTerserOrRegex(terser, "/STF-13", rawMessage, "STF", 13, null);
        String stf19JobTitle = extractTerserOrRegex(terser, "/STF-19", rawMessage, "STF", 19, null);
        String stf20JobClass = extractTerserOrRegex(terser, "/STF-20", rawMessage, "STF", 20, null);
        String stf32Taxonomy = extractTerserOrRegex(terser, "/STF-32", rawMessage, "STF", 32, null);

        // Extract PRA details
        String pra3Category = extractTerserOrRegex(terser, "/PRA-3", rawMessage, "PRA", 3, null);
        String pra5Specialty = extractTerserOrRegex(terser, "/PRA-5", rawMessage, "PRA", 5, null);

        for (Practitioner p : practitioners) {
            String pId = p.getIdPart();
            PractitionerRole role = new PractitionerRole();
            role.setId("PractitionerRole/" + cleanId(pId) + "-role");
            role.setActive(p.getActive());

            // Practitioner reference
            String fullName = extractPractitionerFullName(p);
            role.setPractitioner(new Reference("Practitioner/" + cleanId(pId)).setDisplay(fullName));

            // Organization reference
            if (organizations != null && !organizations.isEmpty()) {
                Organization primaryOrg = organizations.get(0);
                role.setOrganization(new Reference("Organization/" + primaryOrg.getIdPart()).setDisplay(primaryOrg.getName()));
            }

            // Location reference
            if (locations != null && !locations.isEmpty()) {
                for (Location loc : locations) {
                    role.addLocation(new Reference("Location/" + loc.getIdPart()).setDisplay(loc.getName()));
                }
            }

            // Role / Code: STF-4, STF-19, STF-20, PRA-3
            if (StringUtils.isNotBlank(stf4StaffType)) {
                String[] entries = stf4StaffType.split("~");
                for (String entry : entries) {
                    String[] parts = entry.split("\\^");
                    CodeableConcept cc = role.addCode();
                    cc.setText(parts.length > 1 ? parts[1] : parts[0]);
                    cc.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0182").setCode(parts[0]).setDisplay(cc.getText());
                }
            }

            if (StringUtils.isNotBlank(stf19JobTitle)) {
                CodeableConcept cc = role.addCode();
                cc.setText(stf19JobTitle.trim());
            }

            if (StringUtils.isNotBlank(stf20JobClass)) {
                String[] parts = stf20JobClass.split("\\^");
                CodeableConcept cc = role.addCode();
                cc.setText(parts.length > 1 ? parts[1] : parts[0]);
                cc.addCoding().setCode(parts[0]).setDisplay(cc.getText());
            }

            if (StringUtils.isNotBlank(pra3Category)) {
                String[] parts = pra3Category.split("\\^");
                CodeableConcept cc = role.addCode();
                cc.setText(parts.length > 1 ? parts[1] : parts[0]);
                cc.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/practitioner-role").setCode(parts[0]).setDisplay(cc.getText());
            }

            // Specialty: PRA-5, STF-32
            if (StringUtils.isNotBlank(pra5Specialty)) {
                String[] entries = pra5Specialty.split("~");
                for (String entry : entries) {
                    String[] parts = entry.split("\\^");
                    CodeableConcept cc = role.addSpecialty();
                    cc.setText(parts.length > 1 ? parts[1] : parts[0]);
                    cc.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0265").setCode(parts[0]).setDisplay(cc.getText());
                }
            }

            if (StringUtils.isNotBlank(stf32Taxonomy)) {
                String[] parts = stf32Taxonomy.split("\\^");
                CodeableConcept cc = role.addSpecialty();
                cc.setText(parts.length > 1 ? parts[1] : parts[0]);
                cc.addCoding().setSystem("http://nucc.org/provider-taxonomy").setCode(parts[0]).setDisplay(cc.getText());
            }

            // Period: STF-12, STF-13
            if (StringUtils.isNotBlank(stf12ActiveDate) || StringUtils.isNotBlank(stf13InactiveDate)) {
                Period period = new Period();
                if (StringUtils.isNotBlank(stf12ActiveDate)) {
                    Date start = parseHl7Date(stf12ActiveDate);
                    if (start != null) period.setStart(start);
                }
                if (StringUtils.isNotBlank(stf13InactiveDate)) {
                    Date end = parseHl7Date(stf13InactiveDate);
                    if (end != null) period.setEnd(end);
                }
                role.setPeriod(period);
            }

            // Telecom: copy from practitioner telecom
            if (p.hasTelecom()) {
                for (ContactPoint cp : p.getTelecom()) {
                    role.addContact().addTelecom(cp.copy());
                }
            }

            roles.add(role);
        }

        return roles;
    }

    /**
     * Resolves the primary identifier for a Practitioner from STF-1, STF-2, or fallback.
     */
    private String resolvePractitionerId(String stf1, String stf2, String messageControlId, int index) {
        if (StringUtils.isNotBlank(stf1)) {
            String p1 = stf1.split("\\^")[0].trim();
            if (StringUtils.isNotBlank(p1)) return p1;
        }
        if (StringUtils.isNotBlank(stf2)) {
            String p2 = stf2.split("~")[0].split("\\^")[0].trim();
            if (StringUtils.isNotBlank(p2)) return p2;
        }
        return "practitioner-" + (StringUtils.isNotBlank(messageControlId) ? cleanId(messageControlId) : UUID.randomUUID().toString().substring(0, 8)) + "-" + index;
    }

    /**
     * Parses an XPN HL7 name composite into a FHIR HumanName.
     */
    public HumanName parseHumanName(String xpn) {
        HumanName name = new HumanName();
        name.setUse(HumanName.NameUse.OFFICIAL);
        String[] parts = xpn.split("\\^");

        String family = parts.length > 0 ? parts[0].trim() : "";
        String given = parts.length > 1 ? parts[1].trim() : "";
        String middle = parts.length > 2 ? parts[2].trim() : "";
        String suffix = parts.length > 3 ? parts[3].trim() : "";
        String prefix = parts.length > 4 ? parts[4].trim() : "";
        String degree = parts.length > 5 ? parts[5].trim() : "";

        if (StringUtils.isNotBlank(family)) name.setFamily(family);
        if (StringUtils.isNotBlank(given)) name.addGiven(given);
        if (StringUtils.isNotBlank(middle)) name.addGiven(middle);
        if (StringUtils.isNotBlank(prefix)) name.addPrefix(prefix);
        if (StringUtils.isNotBlank(suffix)) name.addSuffix(suffix);
        if (StringUtils.isNotBlank(degree)) name.addSuffix(degree);

        name.setText(buildFullName(given, middle, family, prefix, StringUtils.isNotBlank(suffix) ? suffix + (StringUtils.isNotBlank(degree) ? " " + degree : "") : degree));
        return name;
    }

    /**
     * Parses an XTN HL7 contact composite into a FHIR ContactPoint.
     */
    public ContactPoint parseContactPoint(String xtn) {
        if (StringUtils.isBlank(xtn)) return null;
        String[] parts = xtn.split("\\^");
        String value = parts[0].trim();
        String useCode = parts.length > 1 ? parts[1].trim().toUpperCase() : "";
        String equipType = parts.length > 2 ? parts[2].trim().toUpperCase() : "";
        String emailPart = parts.length > 3 ? parts[3].trim() : "";

        ContactPoint cp = new ContactPoint();

        if (value.contains("@") || "NET".equals(equipType) || "INTERNET".equalsIgnoreCase(useCode)) {
            cp.setSystem(ContactPoint.ContactPointSystem.EMAIL);
            cp.setValue(StringUtils.isNotBlank(emailPart) ? emailPart : value);
            cp.setUse(ContactPoint.ContactPointUse.WORK);
        } else if ("FX".equals(equipType) || "FAX".equals(equipType) || "WPN".equals(useCode) && value.toLowerCase().contains("fax")) {
            cp.setSystem(ContactPoint.ContactPointSystem.FAX);
            cp.setValue(cleanPhoneNumber(value));
            cp.setUse(ContactPoint.ContactPointUse.WORK);
        } else if ("CP".equals(equipType) || "CELL".equals(equipType) || "MOBILE".equals(equipType)) {
            cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
            cp.setValue(cleanPhoneNumber(value));
            cp.setUse(ContactPoint.ContactPointUse.MOBILE);
        } else {
            cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
            cp.setValue(cleanPhoneNumber(value));
            if ("PRN".equals(useCode) || "HOME".equals(useCode) || "H".equals(useCode)) {
                cp.setUse(ContactPoint.ContactPointUse.HOME);
            } else {
                cp.setUse(ContactPoint.ContactPointUse.WORK);
            }
        }

        return cp;
    }

    /**
     * Parses an XAD HL7 address composite into a FHIR Address.
     */
    public Address parseAddress(String xad) {
        Address addr = new Address();
        String[] parts = xad.split("\\^");

        String street = parts.length > 0 ? parts[0].trim() : "";
        String otherLine = parts.length > 1 ? parts[1].trim() : "";
        String city = parts.length > 2 ? parts[2].trim() : "";
        String state = parts.length > 3 ? parts[3].trim() : "";
        String postalCode = parts.length > 4 ? parts[4].trim() : "";
        String country = parts.length > 5 ? parts[5].trim() : "";
        String type = parts.length > 6 ? parts[6].trim().toUpperCase() : "";

        if (StringUtils.isNotBlank(street)) addr.addLine(street);
        if (StringUtils.isNotBlank(otherLine)) addr.addLine(otherLine);
        if (StringUtils.isNotBlank(city)) addr.setCity(city);
        if (StringUtils.isNotBlank(state)) addr.setState(state);
        if (StringUtils.isNotBlank(postalCode)) addr.setPostalCode(postalCode);
        if (StringUtils.isNotBlank(country)) addr.setCountry(country);

        if ("H".equals(type) || "HOME".equals(type)) {
            addr.setUse(Address.AddressUse.HOME);
        } else {
            addr.setUse(Address.AddressUse.WORK);
        }

        return addr;
    }

    /**
     * Populates languages on the Practitioner from STF-36 and LAN segments.
     */
    private void populatePractitionerLanguages(Practitioner practitioner, String stf36Language, String rawMessage) {
        Set<String> addedLanguages = new HashSet<>();

        if (StringUtils.isNotBlank(stf36Language)) {
            String[] parts = stf36Language.split("\\^");
            String code = parts[0].trim();
            String display = parts.length > 1 ? parts[1].trim() : code;
            practitioner.addCommunication().getLanguage().setText(display).addCoding()
                    .setSystem("urn:ietf:bcp:47")
                    .setCode(code)
                    .setDisplay(display);
            addedLanguages.add(code.toLowerCase());
        }

        Pattern lanPattern = Pattern.compile("^LAN\\|(.*?)$", Pattern.MULTILINE);
        Matcher lanMatcher = lanPattern.matcher(rawMessage);
        while (lanMatcher.find()) {
            String lanLine = lanMatcher.group(1);
            String[] fields = ("LAN|" + lanLine).split("\\|", -1);
            String langCodeField = fields.length > 2 ? fields[2] : "";
            if (StringUtils.isNotBlank(langCodeField)) {
                String[] parts = langCodeField.split("\\^");
                String code = parts[0].trim();
                String display = parts.length > 1 ? parts[1].trim() : code;
                if (!addedLanguages.contains(code.toLowerCase())) {
                    practitioner.addCommunication().getLanguage().setText(display).addCoding()
                            .setSystem("urn:ietf:bcp:47")
                            .setCode(code)
                            .setDisplay(display);
                    addedLanguages.add(code.toLowerCase());
                }
            }
        }
    }

    /**
     * Populates qualifications on the Practitioner from PRA-6, EDU, and CER segments.
     */
    private void populatePractitionerQualifications(Practitioner practitioner, String rawMessage) {
        // 1. PRA-6 Practitioner ID Numbers / Licenses
        Pattern praPattern = Pattern.compile("^PRA\\|(.*?)$", Pattern.MULTILINE);
        Matcher praMatcher = praPattern.matcher(rawMessage);
        if (praMatcher.find()) {
            String praLine = praMatcher.group(1);
            String[] fields = ("PRA|" + praLine).split("\\|", -1);
            String pra6Licenses = fields.length > 6 ? fields[6] : "";
            if (StringUtils.isNotBlank(pra6Licenses)) {
                String[] licenses = pra6Licenses.split("~");
                for (String lic : licenses) {
                    if (StringUtils.isNotBlank(lic)) {
                        String[] parts = lic.split("\\^");
                        Practitioner.PractitionerQualificationComponent q = practitioner.addQualification();
                        Identifier id = q.addIdentifier();
                        id.setValue(parts[0].trim());
                        id.getType().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("LN").setDisplay("License number");
                        if (parts.length > 1 && StringUtils.isNotBlank(parts[1])) {
                            q.getCode().setText(parts[1].trim()).addCoding().setCode(parts[1].trim());
                        } else {
                            q.getCode().setText("Medical License");
                        }
                        if (parts.length > 2 && StringUtils.isNotBlank(parts[2])) {
                            q.setIssuer(new Reference("Organization/" + cleanId(parts[2])).setDisplay(parts[2].trim()));
                        }
                        if (parts.length > 3 && StringUtils.isNotBlank(parts[3])) {
                            Date exp = parseHl7Date(parts[3]);
                            if (exp != null) {
                                q.getPeriod().setEnd(exp);
                            }
                        }
                    }
                }
            }
        }

        // 2. EDU Educational Detail segments
        Pattern eduPattern = Pattern.compile("^EDU\\|(.*?)$", Pattern.MULTILINE);
        Matcher eduMatcher = eduPattern.matcher(rawMessage);
        while (eduMatcher.find()) {
            String eduLine = eduMatcher.group(1);
            String[] fields = ("EDU|" + eduLine).split("\\|", -1);

            String major = fields.length > 2 ? fields[2] : "";
            String degree = fields.length > 3 ? fields[3] : "";
            String dateReceived = fields.length > 4 ? fields[4] : "";
            String school = fields.length > 5 ? fields[5] : "";

            Practitioner.PractitionerQualificationComponent q = practitioner.addQualification();
            StringBuilder text = new StringBuilder();
            if (StringUtils.isNotBlank(degree)) text.append(degree.split("\\^")[0]);
            if (StringUtils.isNotBlank(major)) {
                if (text.length() > 0) text.append(" in ");
                text.append(major.split("\\^")[0]);
            }
            q.getCode().setText(text.toString());
            if (StringUtils.isNotBlank(degree)) {
                String[] degParts = degree.split("\\^");
                q.getCode().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0360").setCode(degParts[0]).setDisplay(degParts.length > 1 ? degParts[1] : degParts[0]);
            }
            if (StringUtils.isNotBlank(school)) {
                String schoolName = school.split("\\^")[0].trim();
                q.setIssuer(new Reference("Organization/" + cleanId(schoolName)).setDisplay(schoolName));
            }
            if (StringUtils.isNotBlank(dateReceived)) {
                Date gradDate = parseHl7Date(dateReceived);
                if (gradDate != null) {
                    q.getPeriod().setEnd(gradDate);
                }
            }
        }

        // 3. CER Certificate Detail segments
        Pattern cerPattern = Pattern.compile("^CER\\|(.*?)$", Pattern.MULTILINE);
        Matcher cerMatcher = cerPattern.matcher(rawMessage);
        while (cerMatcher.find()) {
            String cerLine = cerMatcher.group(1);
            String[] fields = ("CER|" + cerLine).split("\\|", -1);

            String certType = fields.length > 2 ? fields[2] : "";
            String certNum = fields.length > 3 ? fields[3] : "";
            String certIssuer = fields.length > 4 ? fields[4] : "";

            Practitioner.PractitionerQualificationComponent q = practitioner.addQualification();
            if (StringUtils.isNotBlank(certType)) {
                String[] parts = certType.split("\\^");
                q.getCode().setText(parts.length > 1 ? parts[1] : parts[0]);
                q.getCode().addCoding().setCode(parts[0]).setDisplay(q.getCode().getText());
            } else {
                q.getCode().setText("Board Certification");
            }
            if (StringUtils.isNotBlank(certNum)) {
                Identifier id = q.addIdentifier();
                id.setValue(certNum.trim());
                id.getType().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("CER").setDisplay("Certificate");
            }
            if (StringUtils.isNotBlank(certIssuer)) {
                String issuerName = certIssuer.split("\\^")[0].trim();
                q.setIssuer(new Reference("Organization/" + cleanId(issuerName)).setDisplay(issuerName));
            }
        }
    }

    /**
     * Creates a minimal fallback Practitioner resource.
     */
    public Practitioner createFallbackPractitioner(String idSeed) {
        Practitioner practitioner = new Practitioner();
        String id = cleanId(StringUtils.isNotBlank(idSeed) ? idSeed : UUID.randomUUID().toString().substring(0, 8));
        practitioner.setId("Practitioner/" + id);
        practitioner.setActive(true);
        HumanName name = practitioner.addName();
        name.setUse(HumanName.NameUse.OFFICIAL);
        name.setFamily("Unknown");
        name.addGiven("Practitioner");
        name.setText("Unknown Practitioner");
        return practitioner;
    }

    /**
     * Extracts full name string from a Practitioner resource.
     */
    public String extractPractitionerFullName(Practitioner practitioner) {
        if (practitioner == null || !practitioner.hasName()) return "Unknown Practitioner";
        HumanName name = practitioner.getNameFirstRep();
        if (StringUtils.isNotBlank(name.getText())) return name.getText();
        String given = name.hasGiven() ? String.join(" ", name.getGivenAsSingleString()) : "";
        String family = name.hasFamily() ? name.getFamily() : "";
        return buildFullName(given, "", family, "", "");
    }
}
