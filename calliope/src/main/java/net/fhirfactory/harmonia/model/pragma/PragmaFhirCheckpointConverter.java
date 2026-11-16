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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.pragma;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Annotation;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.Task;

import java.util.List;

import static net.fhirfactory.harmonia.model.pragma.PragmaFhirConstants.*;

/**
 * Handles bi-directional conversion between {@link PragmaCheckpoint} objects and FHIR {@link Task} annotations and extensions.
 */
public final class PragmaFhirCheckpointConverter {

    private PragmaFhirCheckpointConverter() {
        // Utility class
    }

    public static void applyCheckpoints(Task task, List<PragmaCheckpoint> checkpoints) {
        if (task == null || checkpoints == null) {
            return;
        }
        for (PragmaCheckpoint cp : checkpoints) {
            if (cp != null) {
                Annotation note = new Annotation();
                note.setTime(cp.getTimestamp());
                StringBuilder sb = new StringBuilder();
                sb.append("[").append(cp.getStageName() != null ? cp.getStageName() : "CHECKPOINT").append("] ");
                if (cp.getErgonId() != null) {
                    sb.append("Ergon: ").append(cp.getErgonId()).append(" ");
                }
                if (cp.getStatus() != null) {
                    sb.append("Status: ").append(cp.getStatus().getCode()).append(" ");
                }
                if (cp.getStatusMessage() != null) {
                    sb.append("- ").append(cp.getStatusMessage());
                }
                note.setText(sb.toString().trim());
                task.addNote(note);
            }
        }
    }

    public static void extractCheckpoints(Task task, Pragma pragma) {
        if (task == null || pragma == null) {
            return;
        }

        if (task.hasExtension(EXTENSION_CHECKPOINT)) {
            for (Extension ext : task.getExtensionsByUrl(EXTENSION_CHECKPOINT)) {
                PragmaCheckpoint cp = new PragmaCheckpoint();
                cp.setPragmaId(pragma.getPragmaId());
                if (ext.hasExtension(EXTENSION_CHECKPOINT_STAGE)) {
                    cp.setStageName(ext.getExtensionString(EXTENSION_CHECKPOINT_STAGE));
                }
                if (ext.hasExtension(EXTENSION_CHECKPOINT_ERGON)) {
                    cp.setErgonId(ext.getExtensionString(EXTENSION_CHECKPOINT_ERGON));
                }
                if (ext.hasExtension(EXTENSION_CHECKPOINT_STEP)) {
                    Extension stepExt = ext.getExtensionByUrl(EXTENSION_CHECKPOINT_STEP);
                    if (stepExt != null && stepExt.getValue() instanceof IntegerType) {
                        cp.setStepIndex(((IntegerType) stepExt.getValue()).getValue());
                    }
                }
                pragma.getCheckpoints().add(cp);
            }
        } else if (task.hasNote()) {
            int stepIndex = 0;
            for (Annotation note : task.getNote()) {
                PragmaCheckpoint cp = new PragmaCheckpoint();
                cp.setPragmaId(pragma.getPragmaId());
                cp.setStepIndex(stepIndex++);
                if (note.hasTime()) {
                    cp.setTimestamp(note.getTime());
                }
                if (note.hasText()) {
                    cp.setStatusMessage(note.getText());
                    cp.setStageName(extractStageName(note.getText()));
                }
                pragma.getCheckpoints().add(cp);
            }
        }
    }

    public static String extractStageName(String text) {
        if (text != null && text.startsWith("[") && text.contains("]")) {
            return text.substring(1, text.indexOf("]")).trim();
        }
        return "UNKNOWN";
    }
}
