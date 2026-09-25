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

package net.fhirfactory.harmonia.infinispan.scenario.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reusable test-support narrator that formats and outputs structured educational scenario narratives
 * for the Mneme Distributed Behaviour Laboratory (ADR-019).
 */
public class MnemeScenarioNarrator {

    private final String scenarioNumber;
    private final String scenarioTitle;
    private String purpose;
    private String hypothesis;
    private final List<String> participants = new ArrayList<>();
    private final List<String> initialState = new ArrayList<>();
    private final List<String> steps = new ArrayList<>();
    private final List<String> finalState = new ArrayList<>();
    private String observedSemantics;
    private String infinispanMechanism;
    private String adr019Assessment;

    public MnemeScenarioNarrator(String scenarioNumber, String scenarioTitle) {
        this.scenarioNumber = scenarioNumber;
        this.scenarioTitle = scenarioTitle;
    }

    public static MnemeScenarioNarrator scenario(String number, String title) {
        return new MnemeScenarioNarrator(number, title);
    }

    public MnemeScenarioNarrator purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public MnemeScenarioNarrator hypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
        return this;
    }

    public MnemeScenarioNarrator addParticipant(String name, String details) {
        this.participants.add(name + " (" + details + ")");
        return this;
    }

    public MnemeScenarioNarrator addInitialState(String key, String details) {
        this.initialState.add(key + ": " + details);
        return this;
    }

    public MnemeScenarioNarrator addStep(int stepNumber, String title, String... details) {
        StringBuilder sb = new StringBuilder();
        sb.append("STEP ").append(stepNumber).append(" \u2014 ").append(title);
        for (String detail : details) {
            sb.append("\n  ").append(detail);
        }
        this.steps.add(sb.toString());
        return this;
    }

    public MnemeScenarioNarrator addFinalState(String key, String details) {
        this.finalState.add(key + ": " + details);
        return this;
    }

    public MnemeScenarioNarrator observedSemantics(String observedSemantics) {
        this.observedSemantics = observedSemantics;
        return this;
    }

    public MnemeScenarioNarrator infinispanMechanism(String infinispanMechanism) {
        this.infinispanMechanism = infinispanMechanism;
        return this;
    }

    public MnemeScenarioNarrator adr019Assessment(String assessmentTerm, String details) {
        this.adr019Assessment = assessmentTerm + "\n  " + details;
        return this;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("======================================================================\n");
        sb.append("MNEME SCENARIO ").append(scenarioNumber).append(" \u2014 ").append(scenarioTitle).append("\n");
        sb.append("======================================================================\n\n");

        if (purpose != null) {
            sb.append("PURPOSE\n");
            sb.append("  ").append(purpose.replace("\n", "\n  ")).append("\n\n");
        }

        if (hypothesis != null) {
            sb.append("HYPOTHESIS / EXPECTATION\n");
            sb.append("  ").append(hypothesis.replace("\n", "\n  ")).append("\n\n");
        }

        if (!participants.isEmpty()) {
            sb.append("PARTICIPANTS\n");
            for (String p : participants) {
                sb.append("  \u2022 ").append(p).append("\n");
            }
            sb.append("\n");
        }

        if (!initialState.isEmpty()) {
            sb.append("INITIAL STATE\n");
            for (String s : initialState) {
                sb.append("  ").append(s).append("\n");
            }
            sb.append("\n");
        }

        if (!steps.isEmpty()) {
            sb.append("STEP-BY-STEP OPERATIONS\n");
            for (String step : steps) {
                sb.append(step).append("\n\n");
            }
        }

        if (!finalState.isEmpty()) {
            sb.append("FINAL OBSERVED STATE\n");
            for (String s : finalState) {
                sb.append("  ").append(s).append("\n");
            }
            sb.append("\n");
        }

        if (observedSemantics != null) {
            sb.append("OBSERVED SEMANTICS\n");
            sb.append("  ").append(observedSemantics.replace("\n", "\n  ")).append("\n\n");
        }

        if (infinispanMechanism != null) {
            sb.append("INFINISPAN MECHANISM\n");
            sb.append("  ").append(infinispanMechanism.replace("\n", "\n  ")).append("\n\n");
        }

        if (adr019Assessment != null) {
            sb.append("ADR-019 ASSESSMENT\n");
            sb.append("  ").append(adr019Assessment.replace("\n", "\n  ")).append("\n");
        }

        sb.append("======================================================================");
        return sb.toString();
    }

    public void narrate() {
        System.out.println(render());
    }
}
