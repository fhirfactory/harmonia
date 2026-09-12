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

import net.fhirfactory.hie.workflowcli.client.WorkflowHttpClient;
import net.fhirfactory.hie.workflowcli.formatter.WorkflowOutputFormatter;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Main command and CLI interface for the HIE Workflow / Task Sequence Processor.
 */
@Command(
        name = "hie-workflow-cli",
        mixinStandardHelpOptions = true,
        version = "hie-workflow-cli 1.0.0",
        description = "Command-line tool for connecting to task-sequence-processor, triggering reload/synchronization, and validating queue names and task-sequence definitions.",
        subcommands = {
                WorkflowCliCommand.ReloadCmd.class,
                WorkflowCliCommand.ValidateCmd.class,
                WorkflowCliCommand.StatusCmd.class,
                WorkflowCliCommand.ListModulesCmd.class,
                WorkflowCliCommand.ReloadQueuesCmd.class,
                WorkflowCliCommand.ReloadSequencesCmd.class
        }
)
public class WorkflowCliCommand implements Callable<Integer> {

    @Option(names = {"-s", "--server-url", "--url"}, description = "Task Sequence Processor base URL. Default: http://localhost:8083 (or env TASK_PROCESSOR_URL / WORKFLOW_SERVER_URL)")
    private String serverUrl;

    @Option(names = {"-H", "--host"}, description = "Task Sequence Processor host. Default: localhost (or env WORKFLOW_HOST)")
    private String host;

    @Option(names = {"-p", "--port"}, description = "Task Sequence Processor HTTP port. Default: 8083 (or env WORKFLOW_PORT)")
    private Integer port;

    @Option(names = {"--timeout"}, description = "HTTP request timeout in seconds. Default: ${DEFAULT-VALUE}", defaultValue = "10")
    private int timeout = 10;

    @Option(names = {"-o", "--output"}, description = "Output format: table, json, raw, summary. Default: ${DEFAULT-VALUE}", defaultValue = "table")
    private String outputFormat = "table";

    @Option(names = {"--pretty"}, description = "Format JSON output with indentation")
    private boolean pretty = false;

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose logging")
    private boolean verbose = false;

    // Direct Action Flags
    @Option(names = {"-r", "--reload", "--sync"}, description = "Trigger reload and synchronization of message queues and task-sequence definitions")
    private boolean reload = false;

    @Option(names = {"--validate", "--check"}, description = "Trigger validation of queue names and task-sequence definitions against runtime environment")
    private boolean validate = false;

    @Option(names = {"--status", "--info"}, description = "Query runtime workflow processor status (broker, camel routes, active sequences)")
    private boolean status = false;

    @Option(names = {"-m", "--list-modules", "--modules"}, description = "List all registered cluster modules and their operational readiness status")
    private boolean listModules = false;

    @Option(names = {"--reload-queues"}, description = "Reload broker message queues only")
    private boolean reloadQueues = false;

    @Option(names = {"--reload-sequences"}, description = "Reload task sequences only")
    private boolean reloadSequences = false;

    private PrintStream out = System.out;
    private PrintStream err = System.err;
    private WorkflowHttpClient customClient;
    private WorkflowOutputFormatter formatter = new WorkflowOutputFormatter();

    public WorkflowCliCommand() {
    }

    public WorkflowCliCommand(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    public WorkflowCliCommand(PrintStream out, PrintStream err, WorkflowHttpClient customClient) {
        this.out = out;
        this.err = err;
        this.customClient = customClient;
    }

    public String resolveEffectiveServerUrl() {
        if (serverUrl != null && !serverUrl.isBlank()) {
            return serverUrl;
        }
        String envUrl = System.getenv("TASK_PROCESSOR_URL");
        if (envUrl != null && !envUrl.isBlank()) {
            return envUrl;
        }
        String envWfUrl = System.getenv("WORKFLOW_SERVER_URL");
        if (envWfUrl != null && !envWfUrl.isBlank()) {
            return envWfUrl;
        }
        String h = host != null && !host.isBlank() ? host : System.getenv().getOrDefault("WORKFLOW_HOST", "localhost");
        int p = port != null ? port : Integer.parseInt(System.getenv().getOrDefault("WORKFLOW_PORT", "8083"));
        return "http://" + h + ":" + p;
    }

    public WorkflowHttpClient getHttpClient() {
        if (customClient != null) {
            return customClient;
        }
        String url = resolveEffectiveServerUrl();
        return new WorkflowHttpClient(url, timeout);
    }

    @Override
    public Integer call() {
        WorkflowHttpClient client = getHttpClient();

        try {
            if (reload) {
                return executeReload(client);
            }
            if (validate) {
                return executeValidate(client);
            }
            if (status) {
                return executeStatus(client);
            }
            if (listModules) {
                return executeListModules(client);
            }
            if (reloadQueues) {
                return executeReloadQueues(client);
            }
            if (reloadSequences) {
                return executeReloadSequences(client);
            }

            // Default: show usage help
            CommandLine.usage(this, out);
            return 0;
        } catch (Exception e) {
            err.println("Error: " + e.getMessage());
            if (verbose) {
                e.printStackTrace(err);
            }
            return 1;
        }
    }

    public int executeReload(WorkflowHttpClient client) throws Exception {
        if (verbose) {
            out.println("Connecting to Task Sequence Processor at " + client.getServerUrl() + " to trigger reload & sync...");
        }
        Map<String, Object> response = client.reloadAll();
        out.println(formatter.formatReloadResponse(response, outputFormat, pretty));
        return 0;
    }

    public int executeValidate(WorkflowHttpClient client) throws Exception {
        if (verbose) {
            out.println("Connecting to Task Sequence Processor at " + client.getServerUrl() + " to validate queues and sequences...");
        }
        Map<String, Object> response = client.validate();
        out.println(formatter.formatValidationReport(response, outputFormat, pretty));
        boolean allValid = Boolean.TRUE.equals(response.get("allValid")) || "VALID".equalsIgnoreCase(String.valueOf(response.get("status")));
        return allValid ? 0 : 1;
    }

    public int executeStatus(WorkflowHttpClient client) throws Exception {
        if (verbose) {
            out.println("Connecting to Task Sequence Processor at " + client.getServerUrl() + " to query status...");
        }
        Map<String, Object> response = client.getStatus();
        out.println(formatter.formatStatus(response, outputFormat, pretty));
        return 0;
    }

    public int executeListModules(WorkflowHttpClient client) throws Exception {
        if (verbose) {
            out.println("Connecting to Task Sequence Processor at " + client.getServerUrl() + " to list cluster modules...");
        }
        java.util.List<Map<String, Object>> modules = client.getClusterModules();
        out.println(formatter.formatModuleStatuses(modules, outputFormat, pretty));
        return 0;
    }

    public int executeReloadQueues(WorkflowHttpClient client) throws Exception {
        if (verbose) {
            out.println("Connecting to Task Sequence Processor at " + client.getServerUrl() + " to reload queues...");
        }
        Map<String, Object> response = client.reloadQueues();
        if ("json".equalsIgnoreCase(outputFormat) || pretty) {
            out.println(formatter.formatJsonString(client.getObjectMapper().writeValueAsString(response), pretty));
        } else {
            out.println("Message Queues successfully reloaded: " + response.getOrDefault("synchronizedQueues", "[]"));
        }
        return 0;
    }

    public int executeReloadSequences(WorkflowHttpClient client) throws Exception {
        if (verbose) {
            out.println("Connecting to Task Sequence Processor at " + client.getServerUrl() + " to reload sequences...");
        }
        Map<String, Object> response = client.reloadSequences();
        if ("json".equalsIgnoreCase(outputFormat) || pretty) {
            out.println(formatter.formatJsonString(client.getObjectMapper().writeValueAsString(response), pretty));
        } else {
            out.println("Task Sequences successfully reloaded in CamelContext. Active count: " + response.getOrDefault("count", 0));
        }
        return 0;
    }

    // =========================================================================
    // Subcommands
    // =========================================================================

    @Command(name = "reload", aliases = {"sync"}, description = "Trigger reload and synchronization of message queues and task-sequence definitions")
    public static class ReloadCmd implements Callable<Integer> {
        @ParentCommand
        private WorkflowCliCommand parent;

        @Override
        public Integer call() {
            try {
                return parent.executeReload(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "validate", aliases = {"check"}, description = "Validate queue names and task-sequence definitions against runtime environment")
    public static class ValidateCmd implements Callable<Integer> {
        @ParentCommand
        private WorkflowCliCommand parent;

        @Override
        public Integer call() {
            try {
                return parent.executeValidate(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "status", aliases = {"info"}, description = "Query runtime status of the workflow processor")
    public static class StatusCmd implements Callable<Integer> {
        @ParentCommand
        private WorkflowCliCommand parent;

        @Override
        public Integer call() {
            try {
                return parent.executeStatus(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "list-modules", aliases = {"modules"}, description = "List all cluster modules and their readiness status")
    public static class ListModulesCmd implements Callable<Integer> {
        @ParentCommand
        private WorkflowCliCommand parent;

        @Override
        public Integer call() {
            try {
                return parent.executeListModules(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "reload-queues", description = "Reload broker message queues only")
    public static class ReloadQueuesCmd implements Callable<Integer> {
        @ParentCommand
        private WorkflowCliCommand parent;

        @Override
        public Integer call() {
            try {
                return parent.executeReloadQueues(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "reload-sequences", description = "Reload task sequences only")
    public static class ReloadSequencesCmd implements Callable<Integer> {
        @ParentCommand
        private WorkflowCliCommand parent;

        @Override
        public Integer call() {
            try {
                return parent.executeReloadSequences(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    // Setters for testing
    public void setOut(PrintStream out) {
        this.out = out;
    }

    public void setErr(PrintStream err) {
        this.err = err;
    }

    public void setCustomClient(WorkflowHttpClient customClient) {
        this.customClient = customClient;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void setPretty(boolean pretty) {
        this.pretty = pretty;
    }
}
