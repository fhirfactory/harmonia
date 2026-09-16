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

package net.fhirfactory.harmonia.paradeigma.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Individual test observation line for HL7 OBX segments.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObservationItem {

    private int setSubId;             // OBX-1 Set ID
    private String valueType;        // OBX-2: NM (Numeric), ST (String), TX (Text), CE/CWE (Coded)
    private String observationId;    // OBX-3.1 LOINC or local code (e.g. 718-7, HB)
    private String observationText;  // OBX-3.2 Test name (e.g. Haemoglobin)
    private String codingSystem;     // OBX-3.3 (LN, L)
    private String value;            // OBX-5 Observation value
    private String units;            // OBX-6 Units (e.g. g/L, mmol/L)
    private String referenceRange;   // OBX-7 Normal range (e.g. 130-180)
    private String abnormalFlags;    // OBX-8: N (Normal), H (High), L (Low), A (Abnormal)
    private String resultStatus;     // OBX-11: F (Final), P (Preliminary), C (Corrected)

    public ObservationItem() {
        this.valueType = "NM";
        this.resultStatus = "F";
        this.abnormalFlags = "N";
    }

    public ObservationItem(int setSubId, String valueType, String observationId, String observationText,
                           String value, String units, String referenceRange, String abnormalFlags) {
        this.setSubId = setSubId;
        this.valueType = valueType != null ? valueType : "NM";
        this.observationId = observationId;
        this.observationText = observationText;
        this.codingSystem = "LN";
        this.value = value;
        this.units = units;
        this.referenceRange = referenceRange;
        this.abnormalFlags = abnormalFlags != null ? abnormalFlags : "N";
        this.resultStatus = "F";
    }

    public int getSetSubId() {
        return setSubId;
    }

    public void setSetSubId(int setSubId) {
        this.setSubId = setSubId;
    }

    public String getValueType() {
        return valueType != null ? valueType : "NM";
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    public String getObservationText() {
        return observationText;
    }

    public void setObservationText(String observationText) {
        this.observationText = observationText;
    }

    public String getCodingSystem() {
        return codingSystem != null ? codingSystem : "LN";
    }

    public void setCodingSystem(String codingSystem) {
        this.codingSystem = codingSystem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getReferenceRange() {
        return referenceRange;
    }

    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public String getAbnormalFlags() {
        return abnormalFlags != null ? abnormalFlags : "N";
    }

    public void setAbnormalFlags(String abnormalFlags) {
        this.abnormalFlags = abnormalFlags;
    }

    public String getResultStatus() {
        return resultStatus != null ? resultStatus : "F";
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    @Override
    public String toString() {
        return "ObservationItem{" +
                "id='" + observationId + '\'' +
                ", text='" + observationText + '\'' +
                ", value='" + value + ' ' + (units != null ? units : "") + '\'' +
                ", flag='" + abnormalFlags + '\'' +
                '}';
    }
}
