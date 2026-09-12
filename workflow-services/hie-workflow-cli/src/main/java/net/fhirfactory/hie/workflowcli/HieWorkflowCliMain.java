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

package net.fhirfactory.hie.workflowcli;

import net.fhirfactory.hie.workflowcli.command.WorkflowCliCommand;
import picocli.CommandLine;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Main application entry point for the HIE Workflow CLI.
 */
public class HieWorkflowCliMain {

    public static void main(String[] args) {
        int exitCode = execute(args);
        System.exit(exitCode);
    }

    public static int execute(String... args) {
        WorkflowCliCommand command = new WorkflowCliCommand();
        return new CommandLine(command).execute(args);
    }

    public static int executeWithStreams(PrintStream out, PrintStream err, String... args) {
        WorkflowCliCommand command = new WorkflowCliCommand(out, err);
        return new CommandLine(command)
                .setOut(new PrintWriter(out, true))
                .setErr(new PrintWriter(err, true))
                .execute(args);
    }
}
