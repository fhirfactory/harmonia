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

package net.fhirfactory.hie.workflowcli.command;

import com.sun.net.httpserver.HttpServer;
import net.fhirfactory.hie.workflowcli.HieWorkflowCliMain;
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

class WorkflowCliCommandTest {

    private HttpServer server;
    private int port;
    private String serverUrl;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();
        server.setExecutor(null);
        server.start();

        serverUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    private CommandLine createCommandLine(WorkflowCliCommand command, ByteArrayOutputStream out, ByteArrayOutputStream err) {
        return new CommandLine(command)
                .setOut(new PrintWriter(out, true))
                .setErr(new PrintWriter(err, true));
    }

    @Test
    @DisplayName("Workflow CLI displays help")
    void testHelpOption() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        WorkflowCliCommand command = new WorkflowCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("--help");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("hie-workflow-cli");
        assertThat(out.toString()).contains("--reload");
        assertThat(out.toString()).contains("--validate");
    }

    @Test
    @DisplayName("Workflow CLI triggers reload with --reload flag")
    void testReloadFlag() {
        String json = """
        {
          "status": "SYNCHRONIZED",
          "synchronizedQueues": ["task.event.queue", "task.event.queue.pas-gw"],
          "synchronizedQueuesCount": 2,
          "sequencesReloaded": true,
          "activeSequencesCount": 1,
          "activeSequenceIds": ["seq-patient-identity-pipeline"]
        }
        """;

        server.createContext("/workflow/reload", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        WorkflowCliCommand command = new WorkflowCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "-r");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("HIE Workflow Reload & Synchronization Report");
        assertThat(out.toString()).contains("Status:                   SYNCHRONIZED");
        assertThat(out.toString()).contains("task.event.queue.pas-gw");
    }

    @Test
    @DisplayName("Workflow CLI triggers validation with validate subcommand")
    void testValidateSubcommand() {
        String json = """
        {
          "status": "VALID",
          "allValid": true,
          "queueValidation": {
            "totalQueues": 1,
            "validQueues": 1,
            "invalidQueues": 0,
            "queues": [{"queueName": "task.event.queue", "valid": true, "routingType": "ANYCAST", "durable": false}]
          },
          "sequenceValidation": {
            "totalSequences": 1,
            "validSequences": 1,
            "invalidSequences": 0,
            "sequences": [{"sequenceId": "seq-1", "sequenceName": "Seq 1", "valid": true}]
          }
        }
        """;

        server.createContext("/workflow/validate", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        WorkflowCliCommand command = new WorkflowCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "validate");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("HIE Workflow Configuration Validation Report");
        assertThat(out.toString()).contains("Overall Validation:       VALID");
    }

    @Test
    @DisplayName("Workflow CLI queries status with status subcommand")
    void testStatusSubcommand() {
        String json = """
        {
          "module": "task-sequence-processor",
          "brokerRunning": true,
          "camelStarted": true,
          "activeSequencesCount": 1,
          "activeSequences": [{"sequenceId": "seq-patient-identity-pipeline", "sequenceName": "Patient Identity Pipeline", "activityCount": 3, "enabled": true}]
        }
        """;

        server.createContext("/workflow/status", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        WorkflowCliCommand command = new WorkflowCliCommand(new PrintStream(out), new PrintStream(err));
        CommandLine cmd = createCommandLine(command, out, err);

        int exitCode = cmd.execute("-s", serverUrl, "status");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("HIE Workflow Processor Runtime Status");
        assertThat(out.toString()).contains("seq-patient-identity-pipeline");
    }

    @Test
    @DisplayName("HieWorkflowCliMain executeWithStreams wrapper works")
    void testMainWrapper() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        int exitCode = HieWorkflowCliMain.executeWithStreams(new PrintStream(out), new PrintStream(err), "--help");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("hie-workflow-cli");
    }
}
