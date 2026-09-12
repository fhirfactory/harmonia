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

package net.fhirfactory.hie.mllpgatewaycli.command;

import net.fhirfactory.hie.mllpgatewaycli.client.MllpClient;
import net.fhirfactory.hie.mllpgatewaycli.client.MllpResult;
import net.fhirfactory.hie.mllpgatewaycli.template.AdtMessageBuilder;
import net.fhirfactory.hie.mllpgatewaycli.template.AdtMessageParameters;
import net.fhirfactory.hie.mllpgatewaycli.template.AdtTemplateRegistry;
import net.fhirfactory.hie.mllpgatewaycli.template.AdtTemplateType;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command-line interface for generating and sending HL7 v2.4 ADT messages to an MLLP Gateway.
 */
@Command(
        name = "mllp-gateway-cli",
        mixinStandardHelpOptions = true,
        version = "mllp-gateway-cli 1.0.0",
        description = "Sends HL7 v2.4 ADT messages over MLLP to the HIE MLLP Gateway instance with pre-defined templates and parameter overrides."
)
public class MllpGatewayCliCommand implements Callable<Integer> {

    @Option(names = {"-t", "--template"}, description = "Pre-defined ADT message template (e.g. A01, A02, A03, A04, A05, A08, A11, A12, A13, A31, A40). Default: ${DEFAULT-VALUE}", defaultValue = "A01")
    private String templateName;

    @Option(names = {"-m", "--mrn"}, description = "Patient Medical Record Number (MRN)")
    private String mrn;

    @Option(names = {"-f", "--first-name", "--firstName"}, description = "Patient First Name (Given Name)")
    private String firstName;

    @Option(names = {"-l", "--last-name", "--lastName"}, description = "Patient Last Name (Family Name)")
    private String lastName;

    @Option(names = {"-d", "--dob", "--date-of-birth", "--birthDate"}, description = "Patient Date of Birth (format: YYYY-MM-DD or YYYYMMDD)")
    private String dateOfBirth;

    @Option(names = {"-g", "--gender", "--sex"}, description = "Patient Administrative Gender (M, F, O, U)")
    private String gender;

    @Option(names = {"-H", "--host"}, description = "MLLP Gateway host. Default: ${DEFAULT-VALUE} (or env MLLP_HOST)", defaultValue = "localhost")
    private String host;

    @Option(names = {"-p", "--port"}, description = "MLLP Gateway port. Default: ${DEFAULT-VALUE} (or env MLLP_PORT)", defaultValue = "2575")
    private int port;

    @Option(names = {"--timeout"}, description = "Socket connection and read timeout in milliseconds. Default: ${DEFAULT-VALUE}", defaultValue = "5000")
    private int timeout;

    @Option(names = {"--message-id", "--control-id"}, description = "HL7 message control ID (MSH-10)")
    private String messageControlId;

    @Option(names = {"--visit-number", "--encounter-id"}, description = "Patient visit or encounter number (PV1-19)")
    private String visitNumber;

    @Option(names = {"--sending-app"}, description = "Sending application (MSH-3). Default: HIE_CLI")
    private String sendingApp;

    @Option(names = {"--sending-facility"}, description = "Sending facility (MSH-4). Default: FACILITY_CLI")
    private String sendingFacility;

    @Option(names = {"--receiving-app"}, description = "Receiving application (MSH-5). Default: HIE")
    private String receivingApp;

    @Option(names = {"--receiving-facility"}, description = "Receiving facility (MSH-6). Default: HIE_IM")
    private String receivingFacility;

    @Option(names = {"--location"}, description = "Patient location (PV1-3). Default: WARD1^RM01^BED1")
    private String currentLocation;

    @Option(names = {"--attending-doc"}, description = "Attending doctor (PV1-7). Default: DOC01^SMITH^JOHN^^DR")
    private String attendingDoctor;

    @Option(names = {"--file"}, description = "Path to raw HL7 message file to send")
    private File file;

    @Option(names = {"--raw-message"}, description = "Raw HL7 message string to send directly")
    private String rawMessage;

    @Option(names = {"-n", "--dry-run"}, description = "Generate and display HL7 message without sending over the network")
    private boolean dryRun;

    @Option(names = {"--list-templates"}, description = "List all available pre-defined ADT message templates")
    private boolean listTemplates;

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output detailing socket operations and HL7 framing")
    private boolean verbose;

    private PrintStream out = System.out;
    private PrintStream err = System.err;

    public MllpGatewayCliCommand() {
    }

    public MllpGatewayCliCommand(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    @Override
    public Integer call() {
        try {
            // Check list-templates flag
            if (listTemplates) {
                printAvailableTemplates();
                return 0;
            }

            // Resolve target host and port (respect environment variables if defaults are untouched)
            String targetHost = resolveHost();
            int targetPort = resolvePort();

            // Build parameters object
            AdtMessageParameters params = buildParameters();

            // Build HL7 message string
            String hl7Payload;
            if (file != null) {
                if (!file.exists() || !file.canRead()) {
                    err.println("Error: Cannot read message file: " + file.getAbsolutePath());
                    return 1;
                }
                String fileContent = Files.readString(file.toPath());
                hl7Payload = AdtMessageBuilder.customizeRawHl7Message(fileContent, params);
            } else if (rawMessage != null && !rawMessage.isBlank()) {
                hl7Payload = AdtMessageBuilder.customizeRawHl7Message(rawMessage, params);
            } else {
                Optional<AdtTemplateType> templateOpt = AdtTemplateType.fromString(templateName);
                if (templateOpt.isEmpty()) {
                    err.println("Error: Unknown ADT template '" + templateName + "'. Use --list-templates to view available templates.");
                    return 1;
                }
                hl7Payload = AdtMessageBuilder.buildMessage(templateOpt.get(), params);
            }

            if (verbose || dryRun) {
                out.println("----------------------------------------");
                out.println("Generated HL7 Message Payload:");
                out.println("----------------------------------------");
                out.println(hl7Payload.replace("\r", "\n"));
                out.println("----------------------------------------");
            }

            if (dryRun) {
                out.println("[Dry Run] Message generated successfully. Skipping transmission.");
                return 0;
            }

            out.println("Sending MLLP message to " + targetHost + ":" + targetPort + " (timeout: " + timeout + "ms)...");

            MllpClient client = new MllpClient(targetHost, targetPort, timeout);
            MllpResult result = client.sendMessage(hl7Payload);

            if (result.isSuccess()) {
                out.println("[SUCCESS] Received HL7 ACK (" + result.getDurationMs() + " ms)");
                out.println("  ACK Code:           " + result.getAckCode());
                if (result.getMessageControlId() != null) {
                    out.println("  Message Control ID: " + result.getMessageControlId());
                }
                if (result.getAckText() != null && !result.getAckText().isBlank()) {
                    out.println("  ACK Text:           " + result.getAckText());
                }
                if (verbose) {
                    out.println("\nRaw ACK Message:");
                    out.println(result.getRawAck().replace("\r", "\n"));
                }
                return 0;
            } else {
                err.println("[FAILED] " + (result.getErrorMessage() != null ? result.getErrorMessage() : "NACK or error received"));
                if (result.getAckCode() != null) {
                    err.println("  ACK Code: " + result.getAckCode());
                }
                if (result.getRawAck() != null && verbose) {
                    err.println("\nRaw ACK Response:");
                    err.println(result.getRawAck().replace("\r", "\n"));
                }
                return 1;
            }

        } catch (Exception e) {
            err.println("Error: " + e.getMessage());
            if (verbose) {
                e.printStackTrace(err);
            }
            return 1;
        }
    }

    private String resolveHost() {
        if ("localhost".equals(this.host)) {
            String envHost = System.getenv("MLLP_HOST");
            if (envHost != null && !envHost.isBlank()) {
                return envHost.trim();
            }
        }
        return this.host;
    }

    private int resolvePort() {
        if (this.port == 2575) {
            String envPort = System.getenv("MLLP_PORT");
            if (envPort != null && !envPort.isBlank()) {
                try {
                    return Integer.parseInt(envPort.trim());
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return this.port;
    }

    private AdtMessageParameters buildParameters() {
        AdtMessageParameters.Builder builder = AdtMessageParameters.builder();

        if (mrn != null && !mrn.isBlank()) builder.mrn(mrn);
        if (firstName != null && !firstName.isBlank()) builder.firstName(firstName);
        if (lastName != null && !lastName.isBlank()) builder.lastName(lastName);
        if (dateOfBirth != null && !dateOfBirth.isBlank()) builder.dateOfBirth(dateOfBirth);
        if (gender != null && !gender.isBlank()) builder.gender(gender);
        if (messageControlId != null && !messageControlId.isBlank()) builder.messageControlId(messageControlId);
        if (visitNumber != null && !visitNumber.isBlank()) builder.visitNumber(visitNumber);
        if (sendingApp != null && !sendingApp.isBlank()) builder.sendingApp(sendingApp);
        if (sendingFacility != null && !sendingFacility.isBlank()) builder.sendingFacility(sendingFacility);
        if (receivingApp != null && !receivingApp.isBlank()) builder.receivingApp(receivingApp);
        if (receivingFacility != null && !receivingFacility.isBlank()) builder.receivingFacility(receivingFacility);
        if (currentLocation != null && !currentLocation.isBlank()) builder.currentLocation(currentLocation);
        if (attendingDoctor != null && !attendingDoctor.isBlank()) builder.attendingDoctor(attendingDoctor);

        return builder.build();
    }

    private void printAvailableTemplates() {
        out.println("Available HL7 v2.4 ADT Message Templates:");
        out.println("================================================================================");
        out.printf("%-8s | %-12s | %-40s | %s%n", "Code", "Message Type", "Description", "Default Class");
        out.println("--------------------------------------------------------------------------------");
        for (AdtTemplateType t : AdtTemplateType.values()) {
            out.printf("%-8s | %-12s | %-40s | %s%n",
                    t.getCode(), t.getMessageType(), t.getDescription(), t.getDefaultPatientClass());
        }
        out.println("================================================================================");
        out.println("Use -t <Code> or -t <MessageType> (e.g. -t A01 or -t ADT^A08) to select a template.");
    }

    // Getters and setters for programmatic testing
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public void setMrn(String mrn) { this.mrn = mrn; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setGender(String gender) { this.gender = gender; }
    public void setHost(String host) { this.host = host; }
    public void setPort(int port) { this.port = port; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    public void setMessageControlId(String messageControlId) { this.messageControlId = messageControlId; }
    public void setVisitNumber(String visitNumber) { this.visitNumber = visitNumber; }
    public void setSendingApp(String sendingApp) { this.sendingApp = sendingApp; }
    public void setSendingFacility(String sendingFacility) { this.sendingFacility = sendingFacility; }
    public void setReceivingApp(String receivingApp) { this.receivingApp = receivingApp; }
    public void setReceivingFacility(String receivingFacility) { this.receivingFacility = receivingFacility; }
    public void setFile(File file) { this.file = file; }
    public void setRawMessage(String rawMessage) { this.rawMessage = rawMessage; }
    public void setDryRun(boolean dryRun) { this.dryRun = dryRun; }
    public void setListTemplates(boolean listTemplates) { this.listTemplates = listTemplates; }
    public void setVerbose(boolean verbose) { this.verbose = verbose; }
}
