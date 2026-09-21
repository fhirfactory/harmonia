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

package net.fhirfactory.harmonia.model.pragma;

import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Annotation;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.Task;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Bi-directional converter utility between canonical {@link Pragma} domain models and HAPI FHIR R5 {@link Task} resources.
 * <p>
 * Ensures transparent mapping between internal workflow domain abstractions and external FHIR R5 compliance.
 */
public final class PragmaFhirConverter {

    public static final String IDENTIFIER_SYSTEM_PRAGMA_ID = "http://fhirfactory.net/harmonia/task/pragma-id";
    public static final String IDENTIFIER_SYSTEM_CORRELATION_ID = "http://fhirfactory.net/harmonia/task/correlation-id";
    public static final String IDENTIFIER_SYSTEM_CAUSATION_ID = "http://fhirfactory.net/harmonia/task/causation-id";

    public static final String EXTENSION_PRAXIS_ID = "http://fhirfactory.net/harmonia/task/praxis-id";
    public static final String EXTENSION_CHECKPOINT = "http://fhirfactory.net/harmonia/task/checkpoint";
    public static final String EXTENSION_CHECKPOINT_STAGE = "http://fhirfactory.net/harmonia/task/checkpoint-stage";
    public static final String EXTENSION_CHECKPOINT_ERGON = "http://fhirfactory.net/harmonia/task/checkpoint-ergon";
    public static final String EXTENSION_CHECKPOINT_STEP = "http://fhirfactory.net/harmonia/task/checkpoint-step";
    public static final String EXTENSION_METADATA_PREFIX = "http://fhirfactory.net/harmonia/task/metadata/";

    public static final String EXTENSION_SECURITY_PREFIX = "http://fhirfactory.net/harmonia/task/security/";
    public static final String EXTENSION_SECURITY_PRINCIPAL_ID = EXTENSION_SECURITY_PREFIX + "principal-id";
    public static final String EXTENSION_SECURITY_PRINCIPAL_TYPE = EXTENSION_SECURITY_PREFIX + "principal-type";
    public static final String EXTENSION_SECURITY_SOURCE_DOMAIN = EXTENSION_SECURITY_PREFIX + "source-domain";
    public static final String EXTENSION_SECURITY_AUTHORITY = EXTENSION_SECURITY_PREFIX + "authority";
    public static final String EXTENSION_SECURITY_POLICY_VERSION = EXTENSION_SECURITY_PREFIX + "policy-version";

    public static final String LEGACY_IDENTIFIER_SYSTEM_PRAGMA_ID = "http://fhirfactory.net/hie/task/pragma-id";
    public static final String LEGACY_IDENTIFIER_SYSTEM_CORRELATION_ID = "http://fhirfactory.net/hie/task/correlation-id";
    public static final String LEGACY_IDENTIFIER_SYSTEM_CAUSATION_ID = "http://fhirfactory.net/hie/task/causation-id";
    public static final String LEGACY_EXTENSION_PRAXIS_ID = "http://fhirfactory.net/hie/task/praxis-id";
    public static final String LEGACY_EXTENSION_METADATA_PREFIX = "http://fhirfactory.net/hie/task/metadata/";
    public static final String LEGACY_EXTENSION_SECURITY_PREFIX = "http://fhirfactory.net/hie/task/security/";

    private PragmaFhirConverter() {
        // Utility class
    }

    /**
     * Converts a canonical {@link Pragma} domain instance into a standard HAPI FHIR R5 {@link Task} resource.
     *
     * @param pragma canonical Pragma instance
     * @return FHIR Task resource, or null if input is null
     */
    public static Task toFhirTask(Pragma pragma) {
        if (pragma == null) {
            return null;
        }

        Task task = new Task();

        // 1. Identifiers & ID
        if (StringUtils.isNotBlank(pragma.getPragmaId())) {
            task.setId(pragma.getPragmaId());
            task.addIdentifier(new Identifier()
                    .setSystem(IDENTIFIER_SYSTEM_PRAGMA_ID)
                    .setValue(pragma.getPragmaId()));
        }
        if (StringUtils.isNotBlank(pragma.getCorrelationId())) {
            task.addIdentifier(new Identifier()
                    .setSystem(IDENTIFIER_SYSTEM_CORRELATION_ID)
                    .setValue(pragma.getCorrelationId()));
        }
        if (StringUtils.isNotBlank(pragma.getCausationId())) {
            task.addIdentifier(new Identifier()
                    .setSystem(IDENTIFIER_SYSTEM_CAUSATION_ID)
                    .setValue(pragma.getCausationId()));
        }

        // 2. Status
        if (pragma.getStatus() != null) {
            task.setStatus(pragma.getStatus().toFhirTaskStatus());
        } else {
            task.setStatus(Task.TaskStatus.DRAFT);
        }

        // 3. Priority
        if (StringUtils.isNotBlank(pragma.getPriorityCode())) {
            try {
                task.setPriority(Enumerations.RequestPriority.fromCode(pragma.getPriorityCode().toLowerCase()));
            } catch (Exception ignored) {
                task.setPriority(Enumerations.RequestPriority.ROUTINE);
            }
        } else if (pragma.getPriority() != null) {
            if (pragma.getPriority() >= 80) {
                task.setPriority(Enumerations.RequestPriority.STAT);
            } else if (pragma.getPriority() >= 60) {
                task.setPriority(Enumerations.RequestPriority.ASAP);
            } else if (pragma.getPriority() >= 40) {
                task.setPriority(Enumerations.RequestPriority.URGENT);
            } else {
                task.setPriority(Enumerations.RequestPriority.ROUTINE);
            }
        }

        // 4. Praxis / Instantiates Canonical
        if (StringUtils.isNotBlank(pragma.getPraxisId())) {
            task.setInstantiatesCanonical(pragma.getPraxisId());
            task.addExtension(new Extension(EXTENSION_PRAXIS_ID, new StringType(pragma.getPraxisId())));
        }

        // 5. Timestamps
        if (pragma.getAuthoredOn() != null) {
            task.setAuthoredOn(pragma.getAuthoredOn());
        }
        if (pragma.getLastModified() != null) {
            task.setLastModified(pragma.getLastModified());
        }

        // 6. Source / Requester
        if (StringUtils.isNotBlank(pragma.getSource())) {
            task.setRequester(new Reference().setDisplay(pragma.getSource()));
        }

        // 7. Destination / Owner
        if (StringUtils.isNotBlank(pragma.getDestination())) {
            task.setOwner(new Reference().setDisplay(pragma.getDestination()));
        }

        // 8. Inputs
        if (pragma.getInput() != null) {
            for (ErgonPayload inputPayload : pragma.getInput()) {
                if (inputPayload != null) {
                    task.addInput(inputPayload.toTaskInput());
                    if (inputPayload.isFhirResource() && inputPayload.getResourceReference() != null && inputPayload.getResourceReference().getResource() != null) {
                        Resource r = (org.hl7.fhir.r5.model.Resource) inputPayload.getResourceReference().getResource();
                        FhirSecurityTagManager.applyDefaultSecurityTag(r);
                        task.addContained(r);
                    }
                }
            }
        }

        // 9. Outputs
        if (pragma.getOutput() != null) {
            for (ErgonPayload outputPayload : pragma.getOutput()) {
                if (outputPayload != null) {
                    task.addOutput(outputPayload.toTaskOutput());
                    if (outputPayload.isFhirResource() && outputPayload.getResourceReference() != null && outputPayload.getResourceReference().getResource() != null) {
                        Resource r = (org.hl7.fhir.r5.model.Resource) outputPayload.getResourceReference().getResource();
                        FhirSecurityTagManager.applyDefaultSecurityTag(r);
                        task.addContained(r);
                    }
                }
            }
        }

        // 9.1 Patient Reference (for)
        if (pragma.getMetadata().containsKey("patientReference") || pragma.getMetadata().containsKey("patientDisplay")) {
            Reference forRef = new Reference();
            if (pragma.getMetadata().containsKey("patientReference")) {
                forRef.setReference(pragma.getMetadata().get("patientReference"));
            }
            if (pragma.getMetadata().containsKey("patientDisplay")) {
                forRef.setDisplay(pragma.getMetadata().get("patientDisplay"));
            }
            task.setFor(forRef);
        } else {
            for (ErgonPayload outputPayload : pragma.getOutput()) {
                if (outputPayload != null && outputPayload.isFhirResource() && outputPayload.getResourceReference() != null) {
                    if (outputPayload.getResourceReference().getResource() instanceof org.hl7.fhir.r5.model.Patient) {
                        org.hl7.fhir.r5.model.Patient p = (org.hl7.fhir.r5.model.Patient) outputPayload.getResourceReference().getResource();
                        String name = p.hasName() && p.getNameFirstRep().hasText() ? p.getNameFirstRep().getText() : null;
                        task.setFor(new Reference("Patient/" + p.getIdPart()).setDisplay(name));
                        break;
                    }
                }
            }
        }

        // 10. Checkpoints (as annotations and extensions)
        if (pragma.getCheckpoints() != null) {
            for (PragmaCheckpoint cp : pragma.getCheckpoints()) {
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

        // 11. Metadata extensions
        if (pragma.getMetadata() != null) {
            for (Map.Entry<String, String> entry : pragma.getMetadata().entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    task.addExtension(new Extension(EXTENSION_METADATA_PREFIX + entry.getKey(), new StringType(entry.getValue())));
                }
            }
        }

        // 12. Originating Security Context
        if (pragma.getOriginatingPrincipal() != null) {
            ThemisPrincipal principal = pragma.getOriginatingPrincipal();
            if (StringUtils.isNotBlank(principal.principalId())) {
                task.addExtension(new Extension(EXTENSION_SECURITY_PRINCIPAL_ID, new StringType(principal.principalId())));
            }
            if (principal.principalType() != null) {
                task.addExtension(new Extension(EXTENSION_SECURITY_PRINCIPAL_TYPE, new StringType(principal.principalType().name())));
            }
            if (StringUtils.isNotBlank(principal.sourceDomain())) {
                task.addExtension(new Extension(EXTENSION_SECURITY_SOURCE_DOMAIN, new StringType(principal.sourceDomain())));
            }
        }

        if (pragma.getOriginatingAuthorities() != null && !pragma.getOriginatingAuthorities().isEmpty()) {
            for (ThemisAuthority auth : pragma.getOriginatingAuthorities()) {
                if (auth != null && StringUtils.isNotBlank(auth.authorityCode())) {
                    task.addExtension(new Extension(EXTENSION_SECURITY_AUTHORITY, new StringType(auth.authorityCode())));
                }
            }
        }

        if (StringUtils.isNotBlank(pragma.getPolicyVersion())) {
            task.addExtension(new Extension(EXTENSION_SECURITY_POLICY_VERSION, new StringType(pragma.getPolicyVersion())));
        }

        // 13. Security Tagging
        if (pragma.getMetadata() != null && pragma.getMetadata().containsKey("confidentiality")) {
            FhirSecurityTagManager.applySecurityTag(task, pragma.getMetadata().get("confidentiality"));
        } else {
            FhirSecurityTagManager.applyDefaultSecurityTag(task);
        }

        return task;
    }

    /**
     * Converts a HAPI FHIR R5 {@link Task} resource into a canonical {@link Pragma} domain instance.
     *
     * @param task FHIR Task resource
     * @return canonical Pragma instance, or null if input is null
     */
    public static Pragma fromFhirTask(Task task) {
        if (task == null) {
            return null;
        }

        Pragma pragma = new Pragma();

        // 1. Identifiers & ID
        if (task.hasIdElement() && StringUtils.isNotBlank(task.getIdElement().getIdPart())) {
            pragma.setPragmaId(task.getIdElement().getIdPart());
        }

        if (task.hasIdentifier()) {
            for (Identifier identifier : task.getIdentifier()) {
                if (Objects.equals(identifier.getSystem(), IDENTIFIER_SYSTEM_PRAGMA_ID) || Objects.equals(identifier.getSystem(), LEGACY_IDENTIFIER_SYSTEM_PRAGMA_ID)) {
                    pragma.setPragmaId(identifier.getValue());
                } else if (Objects.equals(identifier.getSystem(), IDENTIFIER_SYSTEM_CORRELATION_ID) || Objects.equals(identifier.getSystem(), LEGACY_IDENTIFIER_SYSTEM_CORRELATION_ID)) {
                    pragma.setCorrelationId(identifier.getValue());
                } else if (Objects.equals(identifier.getSystem(), IDENTIFIER_SYSTEM_CAUSATION_ID) || Objects.equals(identifier.getSystem(), LEGACY_IDENTIFIER_SYSTEM_CAUSATION_ID)) {
                    pragma.setCausationId(identifier.getValue());
                }
            }
        }

        // 2. Status
        if (task.hasStatus()) {
            pragma.setStatus(PragmaStatus.fromFhirTaskStatus(task.getStatus()));
        }

        // 3. Priority
        if (task.hasPriority()) {
            Enumerations.RequestPriority taskPriority = task.getPriority();
            if (taskPriority != null) {
                pragma.setPriorityCode(taskPriority.toCode());
                pragma.setPriority(switch (taskPriority) {
                    case STAT -> 100;
                    case ASAP -> 75;
                    case URGENT -> 50;
                    case ROUTINE -> 25;
                    case NULL -> null;
                });
            }
        }

        // 4. Praxis ID
        if (task.hasInstantiatesCanonical()) {
            pragma.setPraxisId(task.getInstantiatesCanonical());
        } else if (task.hasExtension(EXTENSION_PRAXIS_ID)) {
            Extension praxisExt = task.getExtensionByUrl(EXTENSION_PRAXIS_ID);
            if (praxisExt.hasValue() && praxisExt.getValue() instanceof StringType) {
                pragma.setPraxisId(((StringType) praxisExt.getValue()).getValue());
            }
        } else if (task.hasExtension(LEGACY_EXTENSION_PRAXIS_ID)) {
            Extension praxisExt = task.getExtensionByUrl(LEGACY_EXTENSION_PRAXIS_ID);
            if (praxisExt.hasValue() && praxisExt.getValue() instanceof StringType) {
                pragma.setPraxisId(((StringType) praxisExt.getValue()).getValue());
            }
        }

        // 5. Timestamps
        if (task.hasAuthoredOn()) {
            pragma.setAuthoredOn(task.getAuthoredOn());
        }
        if (task.hasLastModified()) {
            pragma.setLastModified(task.getLastModified());
        }

        // 6. Source / Requester
        if (task.hasRequester() && task.getRequester().hasDisplay()) {
            pragma.setSource(task.getRequester().getDisplay());
        }

        // 7. Destination / Owner
        if (task.hasOwner() && task.getOwner().hasDisplay()) {
            pragma.setDestination(task.getOwner().getDisplay());
        }

        // 7.1 Patient Link (for)
        if (task.hasFor()) {
            if (task.getFor().hasDisplay()) {
                pragma.addMetadata("patientDisplay", task.getFor().getDisplay());
            }
            if (task.getFor().hasReference()) {
                pragma.addMetadata("patientReference", task.getFor().getReference());
            }
        }

        // 8. Inputs
        if (task.hasInput()) {
            for (Task.TaskInputComponent inputComponent : task.getInput()) {
                ErgonPayload payload = ErgonPayload.fromTaskInput(inputComponent);
                resolveContainedReference(payload, task);
                pragma.addInput(payload);
            }
        }

        // 9. Outputs
        if (task.hasOutput()) {
            for (Task.TaskOutputComponent outputComponent : task.getOutput()) {
                ErgonPayload payload = ErgonPayload.fromTaskOutput(outputComponent);
                resolveContainedReference(payload, task);
                pragma.addOutput(payload);
            }
        }

        // 9.1 Preserve any contained resources not yet referenced in payloads
        if (task.hasContained()) {
            for (org.hl7.fhir.r5.model.Resource res : task.getContained()) {
                boolean alreadyInOutput = pragma.getOutput().stream()
                        .anyMatch(ep -> ep.isFhirResource() && ep.getResourceReference() != null &&
                                (Objects.equals(ep.getResourceReference().getResource(), res) ||
                                 Objects.equals(ep.getResourceReference().getReference(), "#" + res.getIdPart()) ||
                                 Objects.equals(ep.getResourceReference().getReference(), res.fhirType() + "/" + res.getIdPart())));
                if (!alreadyInOutput) {
                    Topic topic = new Topic("Health", "FHIR", "R5", res.fhirType(), null);
                    pragma.addOutput(ErgonPayload.fromFhirResource(pragma.getOutput().size(), topic, topic, res));
                }
            }
        }

        // 10. Checkpoints from Notes
        if (task.hasNote()) {
            int stepIndex = 0;
            for (Annotation note : task.getNote()) {
                PragmaCheckpoint cp = new PragmaCheckpoint();
                cp.setPragmaId(pragma.getPragmaId());
                cp.setPraxisId(pragma.getPraxisId());
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

        // 11. Metadata from extensions
        if (task.hasExtension()) {
            String principalId = null;
            PrincipalType principalType = null;
            String sourceDomain = null;
            String policyVersion = null;

            for (Extension ext : task.getExtension()) {
                if (ext.hasUrl() && ext.getValue() instanceof StringType stringType) {
                    String url = ext.getUrl();
                    String val = stringType.getValue();
                    if (url.startsWith(EXTENSION_METADATA_PREFIX)) {
                        String key = url.substring(EXTENSION_METADATA_PREFIX.length());
                        pragma.addMetadata(key, val);
                    } else if (url.startsWith(LEGACY_EXTENSION_METADATA_PREFIX)) {
                        String key = url.substring(LEGACY_EXTENSION_METADATA_PREFIX.length());
                        pragma.addMetadata(key, val);
                    } else if (EXTENSION_SECURITY_PRINCIPAL_ID.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "principal-id").equals(url)) {
                        principalId = val;
                    } else if (EXTENSION_SECURITY_PRINCIPAL_TYPE.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "principal-type").equals(url)) {
                        try {
                            principalType = PrincipalType.valueOf(val);
                        } catch (Exception ignored) {}
                    } else if (EXTENSION_SECURITY_SOURCE_DOMAIN.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "source-domain").equals(url)) {
                        sourceDomain = val;
                    } else if (EXTENSION_SECURITY_AUTHORITY.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "authority").equals(url)) {
                        pragma.addOriginatingAuthority(val);
                    } else if (EXTENSION_SECURITY_POLICY_VERSION.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "policy-version").equals(url)) {
                        policyVersion = val;
                    }
                }
            }

            if (principalId != null) {
                ThemisPrincipal principal = new ThemisPrincipal(
                        principalId,
                        principalType != null ? principalType : PrincipalType.HUMAN,
                        sourceDomain,
                        Map.of()
                );
                pragma.setOriginatingPrincipal(principal);
                pragma.setOriginatingSecurityContext(ThemisSecurityContext.fromPrincipal(principal, pragma.getCorrelationId()));
            }

            if (policyVersion != null) {
                pragma.setPolicyVersion(policyVersion);
            }
        }

        // 12. Security Tagging
        if (task.hasMeta() && task.getMeta().hasSecurity()) {
            FhirSecurityTagManager.getConfidentiality(task).ifPresent(c ->
                    pragma.addMetadata("confidentiality", c.getCode()));
        }

        return pragma;
    }

    private static void resolveContainedReference(ErgonPayload payload, Task task) {
        if (payload != null && payload.isFhirResource() && payload.getResourceReference() != null && task.hasContained()) {
            Reference ref = payload.getResourceReference();
            if (ref.getResource() == null && ref.hasReference()) {
                String targetId = ref.getReference().replace("#", "");
                for (org.hl7.fhir.r5.model.Resource contained : task.getContained()) {
                    String cId = contained.getIdPart() != null ? contained.getIdPart().replace("#", "") : "";
                    if (Objects.equals(cId, targetId) || Objects.equals(contained.fhirType() + "/" + cId, targetId) || Objects.equals(contained.getId(), ref.getReference())) {
                        ref.setResource(contained);
                        break;
                    }
                }
            }
        }
    }

    private static String extractStageName(String text) {
        if (text != null && text.startsWith("[") && text.contains("]")) {
            return text.substring(1, text.indexOf("]"));
        }
        return "NOTE";
    }
}
