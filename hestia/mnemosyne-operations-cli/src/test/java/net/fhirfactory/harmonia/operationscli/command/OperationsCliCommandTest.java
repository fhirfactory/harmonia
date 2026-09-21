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

package net.fhirfactory.harmonia.operationscli.command;

import com.sun.net.httpserver.HttpServer;
import net.fhirfactory.harmonia.operationscli.OperationsCliMain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class OperationsCliCommandTest {

    private HttpServer server;
    private int port;
    private String serverUrl;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();
        server.setExecutor(null);
        server.start();

        serverUrl = "http://localhost:" + port + "/api/operations";
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    private CommandLine createCommandLine(OperationsCliCommand command, ByteArrayOutputStream out, ByteArrayOutputStream err) {
        return new CommandLine(command)
                .setOut(new PrintWriter(out, true))
                .setErr(new PrintWriter(err, true));
    }

    @Test
    @DisplayName("CLI displays help when --help is passed")
    void testHelpOption() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("--help");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("mnemosyne-operations-cli");
        assertThat(out.toString()).contains("--list-sequences");
    }

    @Test
    @DisplayName("CLI lists all TaskSequences with --list-sequences flag")
    void testListSequencesFlag() {
        String json = """
        [
          {
            "id": 1,
            "objectType": "tasksequence",
            "objectId": "seq-admission-pipeline",
            "versionId": 1,
            "dataJson": "{\\"sequenceId\\":\\"seq-admission-pipeline\\",\\"sequenceName\\":\\"Admission Task Sequence\\",\\"enabled\\":true,\\"targetGatewayInstances\\":[\\"*\\"],\\"targetTriggerTypes\\":[\\"A01\\",\\"A04\\"],\\"activityIds\\":[\\"patient-identity-update\\"]}",
            "deleted": false
          }
        ]
        """;

        server.createContext("/api/operations/tasksequence", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "-l");
        assertThat(exitCode).isEqualTo(0);
        String output = out.toString();
        assertThat(output).contains("Found 1 TaskSequence(s):");
        assertThat(output).contains("seq-admission-pipeline");
        assertThat(output).contains("Admission Task Sequence");
        assertThat(output).contains("patient-identity-update");
    }

    @Test
    @DisplayName("CLI lists all TaskSequences using list-sequences subcommand with JSON output")
    void testListSequencesSubcommandJson() {
        String json = """
        [
          {
            "id": 1,
            "objectType": "tasksequence",
            "objectId": "seq-orders",
            "versionId": 1,
            "dataJson": "{\\"sequenceId\\":\\"seq-orders\\",\\"sequenceName\\":\\"Orders Sequence\\",\\"enabled\\":true}",
            "deleted": false
          }
        ]
        """;

        server.createContext("/api/operations/tasksequence", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "-o", "json", "--pretty", "list-sequences");
        assertThat(exitCode).isEqualTo(0);
        String output = out.toString();
        assertThat(output).contains("\"praxisId\" : \"seq-orders\"");
    }

    @Test
    @DisplayName("CLI gets a specific TaskSequence by ID")
    void testGetSequence() {
        String json = """
        {
          "sequenceId": "seq-admission-pipeline",
          "sequenceName": "Admission Task Sequence",
          "sequenceDescription": "Handles admission triggers",
          "version": "1.0.0",
          "enabled": true,
          "targetGatewayInstances": ["*"],
          "targetTriggerTypes": ["A01"],
          "activityIds": ["patient-identity-update", "patient-demographics-update"]
        }
        """;

        server.createContext("/api/operations/tasksequence/seq-admission-pipeline", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "get-sequence", "seq-admission-pipeline");
        assertThat(exitCode).isEqualTo(0);
        String output = out.toString();
        assertThat(output).contains("Task Sequence: Admission Task Sequence");
        assertThat(output).contains("Sequence ID:      seq-admission-pipeline");
        assertThat(output).contains("1. patient-identity-update");
        assertThat(output).contains("2. patient-demographics-update");
    }

    @Test
    @DisplayName("CLI saves a TaskSequence from inline data")
    void testSaveSequence() {
        server.createContext("/api/operations/tasksequence/seq-test-save", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("PUT");
            byte[] bytes = "{\"sequenceId\":\"seq-test-save\"}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        String payload = "{\"sequenceId\":\"seq-test-save\",\"sequenceName\":\"Test Save\",\"enabled\":true}";
        int exitCode = cmd.execute("-s", serverUrl, "save-sequence", "-d", payload);
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Successfully saved TaskSequence: seq-test-save");
    }

    @Test
    @DisplayName("CLI deletes a TaskSequence")
    void testDeleteSequence() {
        server.createContext("/api/operations/tasksequence/seq-delete-target", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("DELETE");
            exchange.sendResponseHeaders(204, -1);
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "delete-sequence", "seq-delete-target");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Successfully deleted TaskSequence: seq-delete-target");
    }

    @Test
    @DisplayName("CLI lists operational resources by type")
    void testListResources() {
        String json = """
        [
          {
            "id": 10,
            "objectType": "config",
            "objectId": "hie-settings",
            "versionId": 1,
            "dataJson": "{\\"mode\\":\\"cluster\\"}",
            "deleted": false
          }
        ]
        """;

        server.createContext("/api/operations/config", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "list-resources", "-t", "config");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Found 1 Operational Resource(s):");
        assertThat(out.toString()).contains("hie-settings");
    }

    @Test
    @DisplayName("CLI checks resource existence")
    void testCheckResource() {
        server.createContext("/api/operations/tasksequence/seq-check-me", exchange -> {
            exchange.sendResponseHeaders(200, -1);
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "check-resource", "tasksequence", "seq-check-me");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Resource exists: tasksequence/seq-check-me");
    }

    @Test
    @DisplayName("CLI creates and saves a MessageQueue using create-queue command")
    void testCreateQueueCommand() {
        server.createContext("/api/operations/messagequeue/task.event.queue.pas-gw", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("PUT");
            byte[] bytes = "{\"queueId\":\"task.event.queue.pas-gw\"}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "create-queue",
                "-q", "task.event.queue.pas-gw",
                "-r", "ANYCAST",
                "--durable",
                "-g", "pas-gw",
                "--desc", "PAS Queue");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Successfully created/saved MessageQueue: task.event.queue.pas-gw");
    }

    @Test
    @DisplayName("CLI lists all MessageQueues with list-queues command")
    void testListQueuesCommand() {
        String json = """
        [
          {
            "id": 1,
            "objectType": "messagequeue",
            "objectId": "task.event.queue.pas-gw",
            "versionId": 1,
            "dataJson": "{\\"queueId\\":\\"task.event.queue.pas-gw\\",\\"queueName\\":\\"task.event.queue.pas-gw\\",\\"routingType\\":\\"ANYCAST\\",\\"durable\\":true,\\"gatewayInstanceId\\":\\"pas-gw\\",\\"enabled\\":true}",
            "deleted": false
          }
        ]
        """;

        server.createContext("/api/operations/messagequeue", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "list-queues");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Found 1 MessageQueue(s):");
        assertThat(out.toString()).contains("task.event.queue.pas-gw");
        assertThat(out.toString()).contains("pas-gw");
    }

    @Test
    @DisplayName("CLI gets a specific MessageQueue by ID")
    void testGetQueueCommand() {
        String json = """
        {
          "queueId": "task.event.queue.pas-gw",
          "queueName": "task.event.queue.pas-gw",
          "address": "task.event.queue.pas-gw",
          "routingType": "ANYCAST",
          "durable": true,
          "enabled": true,
          "gatewayInstanceId": "pas-gw",
          "description": "PAS Dedicated Queue"
        }
        """;

        server.createContext("/api/operations/messagequeue/task.event.queue.pas-gw", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "get-queue", "task.event.queue.pas-gw");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Message Queue: task.event.queue.pas-gw");
        assertThat(out.toString()).contains("Queue ID:         task.event.queue.pas-gw");
        assertThat(out.toString()).contains("Gateway Instance: pas-gw");
    }

    @Test
    @DisplayName("CLI deletes a MessageQueue by ID")
    void testDeleteQueueCommand() {
        server.createContext("/api/operations/messagequeue/task.event.queue.pas-gw", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("DELETE");
            exchange.sendResponseHeaders(204, -1);
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "delete-queue", "task.event.queue.pas-gw");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Successfully deleted MessageQueue: task.event.queue.pas-gw");
    }

    @Test
    @DisplayName("CLI creates a TaskSequence with parameters")
    void testCreateSequenceCommand() {
        server.createContext("/api/operations/tasksequence/seq-test-create", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("PUT");
            byte[] bytes = "{\"sequenceId\":\"seq-test-create\"}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "create-sequence",
                "-i", "seq-test-create",
                "-n", "Test Sequence Creation",
                "-a", "message-queue-to-exchange,patient-identity-update",
                "-g", "pas-gw",
                "-t", "A01,A08");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Successfully created/saved TaskSequence: seq-test-create");
    }

    @Test
    @DisplayName("CLI loads and applies initial configuration")
    void testInitConfigCommand() {
        server.createContext("/api/operations/messagequeue/", exchange -> {
            byte[] bytes = "{}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.createContext("/api/operations/tasksequence/", exchange -> {
            byte[] bytes = "{}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        OperationsCliCommand command = new OperationsCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "init-config");
        assertThat(exitCode).isEqualTo(0);
        String output = out.toString();
        assertThat(output).contains("Successfully applied initial configuration:");
        assertThat(output).contains("Message Queues populated:");
        assertThat(output).contains("Task Sequences populated:");
        assertThat(output).contains("seq-patient-identity-pipeline");
    }

    @Test
    @DisplayName("CLI main execute wrapper runs properly")
    void testMainExecute() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        int exitCode = OperationsCliMain.executeWithStreams(new PrintStream(out), new PrintStream(err), "--help");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("mnemosyne-operations-cli");
    }
}
