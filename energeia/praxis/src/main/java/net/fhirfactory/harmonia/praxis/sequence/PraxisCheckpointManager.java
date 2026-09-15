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

package net.fhirfactory.harmonia.praxis.sequence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * Manages the creation, recording, and durable caching of {@link PragmaCheckpoint} audit records
 * across all execution boundaries in a sequential {@link PraxisImplementation} workflow.
 * <p>
 * Checkpoints ensure complete state traceability, step-by-step auditing, and state recovery
 * by snapshotting the in-flight {@link Pragma} into Mneme Infinispan cache.
 */
@ApplicationScoped
public class PraxisCheckpointManager {

    private static final Logger log = LoggerFactory.getLogger(PraxisCheckpointManager.class);

    public static final String STAGE_PIPELINE_INGRESS = "PIPELINE_INGRESS";
    public static final String STAGE_PRE_ERGON = "PRE_ERGON";
    public static final String STAGE_POST_ERGON = "POST_ERGON";
    public static final String STAGE_PIPELINE_COMPLETE = "PIPELINE_COMPLETE";
    public static final String STAGE_PIPELINE_FAILED = "PIPELINE_FAILED";

    @Inject
    private PragmaCacheService pragmaCacheService;

    @Inject
    private TaskCacheService taskCacheService;

    public PraxisCheckpointManager() {
    }

    public PraxisCheckpointManager(PragmaCacheService pragmaCacheService, TaskCacheService taskCacheService) {
        this.pragmaCacheService = pragmaCacheService;
        this.taskCacheService = taskCacheService;
    }

    /**
     * Records an ingress checkpoint upon entry into a Praxis workflow pipeline.
     *
     * @param pragma   canonical Pragma domain instance
     * @param praxisId Praxis workflow identifier
     * @return recorded PragmaCheckpoint
     */
    public PragmaCheckpoint recordPipelineIngress(Pragma pragma, String praxisId) {
        if (pragma == null) {
            return null;
        }
        if (StringUtils.isNotBlank(praxisId)) {
            pragma.setPraxisId(praxisId);
        }
        pragma.setStatus(PragmaStatus.IN_PROGRESS);

        PragmaCheckpoint checkpoint = PragmaCheckpoint.builder()
                .checkpointId(UUID.randomUUID().toString())
                .pragmaId(pragma.getPragmaId())
                .praxisId(praxisId)
                .stageName(STAGE_PIPELINE_INGRESS)
                .status(PragmaStatus.IN_PROGRESS)
                .stepIndex(0)
                .statusMessage(String.format("Pragma/%s accepted into Praxis pipeline [%s]", pragma.getPragmaId(), praxisId))
                .timestamp(new Date())
                .build();

        pragma.addCheckpoint(checkpoint);
        persistCheckpoint(pragma);

        log.info("[{}] Recorded PIPELINE_INGRESS checkpoint for Pragma/{}", praxisId, pragma.getPragmaId());
        return checkpoint;
    }

    /**
     * Records a pre-execution checkpoint immediately before dispatching into an Ergon step.
     *
     * @param pragma    canonical Pragma domain instance
     * @param praxisId  Praxis workflow identifier
     * @param ergonId   Ergon activity identifier
     * @param stepIndex 0-based step sequence index
     * @return recorded PragmaCheckpoint
     */
    public PragmaCheckpoint recordPreErgon(Pragma pragma, String praxisId, String ergonId, int stepIndex) {
        if (pragma == null) {
            return null;
        }

        PragmaCheckpoint checkpoint = PragmaCheckpoint.builder()
                .checkpointId(UUID.randomUUID().toString())
                .pragmaId(pragma.getPragmaId())
                .praxisId(praxisId)
                .ergonId(ergonId)
                .stageName(STAGE_PRE_ERGON)
                .status(PragmaStatus.IN_PROGRESS)
                .stepIndex(stepIndex)
                .statusMessage(String.format("Dispatching Pragma/%s into Ergon [%s] (step %d)", pragma.getPragmaId(), ergonId, stepIndex))
                .timestamp(new Date())
                .build();

        pragma.addCheckpoint(checkpoint);
        persistCheckpoint(pragma);

        log.debug("[{}] Recorded PRE_ERGON step-{} checkpoint for Ergon [{}]", praxisId, stepIndex, ergonId);
        return checkpoint;
    }

    /**
     * Records a post-execution checkpoint immediately following successful completion of an Ergon step.
     *
     * @param pragma      canonical Pragma domain instance
     * @param praxisId    Praxis workflow identifier
     * @param ergonId     Ergon activity identifier
     * @param stepIndex   0-based step sequence index
     * @param outputCount number of output items produced
     * @return recorded PragmaCheckpoint
     */
    public PragmaCheckpoint recordPostErgon(Pragma pragma, String praxisId, String ergonId, int stepIndex, int outputCount) {
        if (pragma == null) {
            return null;
        }

        PragmaCheckpoint checkpoint = PragmaCheckpoint.builder()
                .checkpointId(UUID.randomUUID().toString())
                .pragmaId(pragma.getPragmaId())
                .praxisId(praxisId)
                .ergonId(ergonId)
                .stageName(STAGE_POST_ERGON)
                .status(PragmaStatus.IN_PROGRESS)
                .stepIndex(stepIndex)
                .statusMessage(String.format("Ergon [%s] completed step %d with %d outputs", ergonId, stepIndex, outputCount))
                .timestamp(new Date())
                .build();

        pragma.addCheckpoint(checkpoint);
        persistCheckpoint(pragma);

        log.debug("[{}] Recorded POST_ERGON step-{} checkpoint for Ergon [{}] (outputs={})", praxisId, stepIndex, ergonId, outputCount);
        return checkpoint;
    }

    /**
     * Records a completion checkpoint upon successful termination of the full workflow pipeline.
     *
     * @param pragma     canonical Pragma domain instance
     * @param praxisId   Praxis workflow identifier
     * @param totalSteps total Ergon steps executed
     * @return recorded PragmaCheckpoint
     */
    public PragmaCheckpoint recordPipelineComplete(Pragma pragma, String praxisId, int totalSteps) {
        if (pragma == null) {
            return null;
        }
        pragma.setStatus(PragmaStatus.COMPLETED);

        PragmaCheckpoint checkpoint = PragmaCheckpoint.builder()
                .checkpointId(UUID.randomUUID().toString())
                .pragmaId(pragma.getPragmaId())
                .praxisId(praxisId)
                .stageName(STAGE_PIPELINE_COMPLETE)
                .status(PragmaStatus.COMPLETED)
                .stepIndex(totalSteps)
                .statusMessage(String.format("Praxis pipeline [%s] completed all %d steps successfully for Pragma/%s",
                        praxisId, totalSteps, pragma.getPragmaId()))
                .timestamp(new Date())
                .build();

        pragma.addCheckpoint(checkpoint);
        persistCheckpoint(pragma);

        log.info("[{}] Recorded PIPELINE_COMPLETE checkpoint for Pragma/{}", praxisId, pragma.getPragmaId());
        return checkpoint;
    }

    /**
     * Records a failure checkpoint when an error occurs during workflow execution.
     *
     * @param pragma    canonical Pragma domain instance
     * @param praxisId  Praxis workflow identifier
     * @param ergonId   active Ergon activity identifier (if known)
     * @param stepIndex current step sequence index (if known)
     * @param error     exception or error cause
     * @return recorded PragmaCheckpoint
     */
    public PragmaCheckpoint recordPipelineFailure(Pragma pragma, String praxisId, String ergonId, int stepIndex, Throwable error) {
        if (pragma == null) {
            return null;
        }
        pragma.setStatus(PragmaStatus.FAILED);

        String errorMsg = error != null ? error.getMessage() : "Unknown execution failure";
        PragmaCheckpoint checkpoint = PragmaCheckpoint.builder()
                .checkpointId(UUID.randomUUID().toString())
                .pragmaId(pragma.getPragmaId())
                .praxisId(praxisId)
                .ergonId(ergonId)
                .stageName(STAGE_PIPELINE_FAILED)
                .status(PragmaStatus.FAILED)
                .stepIndex(stepIndex)
                .statusMessage(String.format("Failure in Praxis [%s] at step %d (Ergon [%s]): %s",
                        praxisId, stepIndex, ergonId != null ? ergonId : "N/A", errorMsg))
                .timestamp(new Date())
                .addMetadata("errorClass", error != null ? error.getClass().getName() : "Unknown")
                .build();

        pragma.addCheckpoint(checkpoint);
        persistCheckpoint(pragma);

        log.error("[{}] Recorded PIPELINE_FAILED checkpoint for Pragma/{}: {}", praxisId, pragma.getPragmaId(), errorMsg);
        return checkpoint;
    }

    private void persistCheckpoint(Pragma pragma) {
        if (pragma == null) {
            return;
        }
        if (pragmaCacheService != null) {
            try {
                pragmaCacheService.savePragma(pragma);
            } catch (Exception e) {
                log.warn("Failed to persist checkpoint to PragmaCacheService: {}", e.getMessage());
            }
        } else if (taskCacheService != null) {
            try {
                taskCacheService.savePragma(pragma);
            } catch (Exception e) {
                log.warn("Failed to persist checkpoint to TaskCacheService: {}", e.getMessage());
            }
        }
    }
}
