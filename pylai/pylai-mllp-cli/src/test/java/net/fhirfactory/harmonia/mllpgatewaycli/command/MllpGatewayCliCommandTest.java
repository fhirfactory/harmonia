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

package net.fhirfactory.harmonia.mllpgatewaycli.command;

import net.fhirfactory.harmonia.mllpgatewaycli.MllpGatewayCliMain;
import net.fhirfactory.harmonia.mllpgatewaycli.client.MllpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MllpGatewayCliCommandTest {

    @Test
    @DisplayName("CLI displays template list when --list-templates is passed")
    void testListTemplates() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        MllpGatewayCliCommand command = new MllpGatewayCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = new CommandLine(command);

        int exitCode = cmd.execute("--list-templates");

        assertThat(exitCode).isEqualTo(0);
        String output = out.toString();
        assertThat(output).contains("Available HL7 v2.4 ADT Message Templates:");
        assertThat(output).contains("A01");
        assertThat(output).contains("A02");
        assertThat(output).contains("A03");
        assertThat(output).contains("A04");
        assertThat(output).contains("A05");
        assertThat(output).contains("A08");
        assertThat(output).contains("A11");
        assertThat(output).contains("A12");
        assertThat(output).contains("A13");
        assertThat(output).contains("A31");
        assertThat(output).contains("A40");
    }

    @Test
    @DisplayName("CLI generates message in dry-run mode with optional MRN, firstName, lastName, and date of birth")
    void testDryRunWithCustomParameters() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        MllpGatewayCliCommand command = new MllpGatewayCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = new CommandLine(command);

        int exitCode = cmd.execute(
                "-t", "A01",
                "-m", "PAT99988",
                "-f", "John",
                "-l", "Doe",
                "-d", "1985-06-15",
                "-g", "M",
                "--message-id", "MSG-TEST-12345",
                "--dry-run"
        );

        assertThat(exitCode).isEqualTo(0);
        String output = out.toString();
        assertThat(output).contains("[Dry Run] Message generated successfully");
        assertThat(output).contains("PAT99988^^^HOSPITAL^MR");
        assertThat(output).contains("DOE^JOHN^^^^");
        assertThat(output).contains("19850615");
        assertThat(output).contains("|M");
        assertThat(output).contains("MSG-TEST-12345");
    }

    @Test
    @DisplayName("CLI returns error when invalid template is requested")
    void testInvalidTemplate() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        MllpGatewayCliCommand command = new MllpGatewayCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = new CommandLine(command);

        int exitCode = cmd.execute("-t", "INVALID_TEMPLATE");

        assertThat(exitCode).isEqualTo(1);
        assertThat(err.toString()).contains("Error: Unknown ADT template 'INVALID_TEMPLATE'");
    }

    @Test
    @DisplayName("CLI loads message from file and customizes with CLI parameters in dry-run")
    void testFileInputDryRun(@TempDir Path tempDir) throws Exception {
        Path hl7File = tempDir.resolve("sample_adt.hl7");
        String originalHl7 = "MSH|^~\\&|OLD_APP|OLD_FAC|HIE|HIE_IM|20260101||ADT^A08|MSG-FILE-01|P|2.4\r" +
                "EVN|A08|20260101\r" +
                "PID|1||OLD_MRN^^^HOSPITAL^MR||OLD_LAST^OLD_FIRST^^^^||19700101|M\r" +
                "PV1|1|I|WARD1\r";
        Files.writeString(hl7File, originalHl7);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        MllpGatewayCliCommand command = new MllpGatewayCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = new CommandLine(command);

        int exitCode = cmd.execute(
                "--file", hl7File.toString(),
                "-m", "MRN-OVERRIDE-555",
                "-f", "Jane",
                "-l", "Smith",
                "-d", "1993-11-20",
                "--dry-run"
        );

        assertThat(exitCode).isEqualTo(0);
        String output = out.toString();
        assertThat(output).contains("MRN-OVERRIDE-555^^^HOSPITAL^MR");
        assertThat(output).contains("SMITH^JANE^^^^");
        assertThat(output).contains("19931120");
    }

    @Test
    @DisplayName("CLI transmits message over MLLP socket to a running server and processes ACK")
    void testFullSocketTransmission() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            Thread serverThread = new Thread(() -> {
                try (Socket clientSocket = serverSocket.accept()) {
                    InputStream in = clientSocket.getInputStream();
                    OutputStream socketOut = clientSocket.getOutputStream();

                    String received = MllpClient.readMllpFrame(in);
                    assertThat(received).contains("ADT^A08");
                    assertThat(received).contains("PAT-SOCKET-999");
                    assertThat(received).contains("BANNER^BRUCE^^^^");

                    String ack = "MSH|^~\\&|HIE|HIE_IM|HIE_CLI|FACILITY_CLI|20260907150000||ACK^A08|ACK-SOCK-01|P|2.4\r" +
                            "MSA|AA|MSG-SOCK-01|Transaction Accepted\r";
                    socketOut.write(MllpClient.encodeMllpFrame(ack));
                    socketOut.flush();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            serverThread.start();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            MllpGatewayCliCommand command = new MllpGatewayCliCommand(new PrintStream(out), new PrintStream(err));
            CommandLine cmd = new CommandLine(command);

            int exitCode = cmd.execute(
                    "-t", "A08",
                    "-m", "PAT-SOCKET-999",
                    "-f", "Bruce",
                    "-l", "Banner",
                    "-d", "1969-12-18",
                    "-g", "M",
                    "--message-id", "MSG-SOCK-01",
                    "-H", "127.0.0.1",
                    "-p", String.valueOf(port),
                    "--timeout", "3000",
                    "-v"
            );

            serverThread.join();

            assertThat(exitCode).isEqualTo(0);
            String output = out.toString();
            assertThat(output).contains("[SUCCESS] Received HL7 ACK");
            assertThat(output).contains("ACK Code:           AA");
            assertThat(output).contains("Message Control ID: MSG-SOCK-01");
        }
    }

    @Test
    @DisplayName("MllpGatewayCliMain execute helper runs command cleanly")
    void testMainClassExecution() {
        int exitCode = MllpGatewayCliMain.execute("--list-templates");
        assertThat(exitCode).isEqualTo(0);
    }
}
