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

package net.fhirfactory.hie.model.topic;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Encapsulates the canonical topic metadata for clinical, operational, and integration messages
 * within the HIE platform.
 * <p>
 * Replaces discrete trigger event identifiers with structured hierarchical classification:
 * <ul>
 *   <li><b>Domain</b>: e.g. "Health", "Administrative", "Financial"</li>
 *   <li><b>Model</b>: e.g. "HL7", "FHIR"</li>
 *   <li><b>Model Version</b>: e.g. "2.4", "R5"</li>
 *   <li><b>Data Element</b>: e.g. "ADT", "ORU", "ORM", "Patient"</li>
 *   <li><b>Data Element Qualifier</b>: e.g. "A01", "A08", "R01", "O01"</li>
 *   <li><b>Source</b>: Gateway / ingest instance identifier (e.g. "pas-gw", "mllp-gateway-default")</li>
 *   <li><b>Target</b>: Target subsystem / channel identifier</li>
 *   <li><b>Origin</b>: Sending application / facility</li>
 *   <li><b>Destination</b>: Receiving application / facility</li>
 *   <li><b>ReceivedDate</b>: Timestamp when message was ingested</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DOMAIN_HEALTH = "Health";
    public static final String MODEL_HL7 = "HL7";
    public static final String MODEL_FHIR = "FHIR";
    public static final String DEFAULT_HL7_VERSION = "2.4";
    public static final String DEFAULT_FHIR_VERSION = "R5";

    private String domain = DOMAIN_HEALTH;
    private String model = MODEL_HL7;

    @JsonAlias({"modelVersion", "model_version", "Model Version"})
    private String modelVersion = DEFAULT_HL7_VERSION;

    @JsonAlias({"dataElement", "data_element", "Data Element"})
    private String dataElement;

    @JsonAlias({"dataElementQualifier", "data_element_qualifier", "Data Element Qualifier", "qualifier"})
    private String dataElementQualifier;

    private String source;
    private String target;
    private String origin;
    private String destination;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    @JsonAlias({"receivedDate", "received_date", "ReceivedDate", "timestamp"})
    private Date receivedDate;

    /**
     * Default constructor.
     */
    public Topic() {
        this.receivedDate = new Date();
    }

    /**
     * Constructor with core hierarchical parameters.
     */
    public Topic(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier) {
        this();
        this.domain = domain;
        this.model = model;
        this.modelVersion = modelVersion;
        this.dataElement = dataElement;
        this.dataElementQualifier = dataElementQualifier;
    }

    /**
     * Constructor with core parameters and source.
     */
    public Topic(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier, String source) {
        this(domain, model, modelVersion, dataElement, dataElementQualifier);
        this.source = source;
    }

    /**
     * Full constructor.
     */
    public Topic(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier,
                 String source, String target, String origin, String destination, Date receivedDate) {
        this.domain = domain;
        this.model = model;
        this.modelVersion = modelVersion;
        this.dataElement = dataElement;
        this.dataElementQualifier = dataElementQualifier;
        this.source = source;
        this.target = target;
        this.origin = origin;
        this.destination = destination;
        this.receivedDate = receivedDate != null ? receivedDate : new Date();
    }

    /**
     * Copy constructor.
     */
    public Topic(Topic other) {
        if (other != null) {
            this.domain = other.domain;
            this.model = other.model;
            this.modelVersion = other.modelVersion;
            this.dataElement = other.dataElement;
            this.dataElementQualifier = other.dataElementQualifier;
            this.source = other.source;
            this.target = other.target;
            this.origin = other.origin;
            this.destination = other.destination;
            this.receivedDate = other.receivedDate != null ? new Date(other.receivedDate.getTime()) : new Date();
        } else {
            this.receivedDate = new Date();
        }
    }

    // =========================================================================
    // Factory Helpers
    // =========================================================================

    /**
     * Creates a Topic for an HL7 v2 message.
     *
     * @param messageType  HL7 message code (e.g., "ADT", "ORU", "ORM")
     * @param triggerEvent HL7 trigger event code (e.g., "A01", "R01", "O01")
     * @param source       Gateway instance ID
     * @return populated Topic
     */
    public static Topic fromHl7(String messageType, String triggerEvent, String source) {
        return fromHl7(DEFAULT_HL7_VERSION, messageType, triggerEvent, source, null, null, null);
    }

    /**
     * Creates a Topic from a composite trigger string (e.g. "ADT^A01", "A01", "ORU^R01").
     *
     * @param triggerType Composite trigger or event type
     * @param source      Gateway instance ID
     * @return populated Topic
     */
    public static Topic fromHl7(String triggerType, String source) {
        String msgType = "*";
        String trigger = triggerType;
        if (triggerType != null && triggerType.contains("^")) {
            String[] parts = triggerType.split("\\^");
            msgType = parts[0];
            trigger = parts.length > 1 ? parts[1] : "*";
        } else if (triggerType == null || triggerType.isBlank() || "*".equals(triggerType)) {
            msgType = "*";
            trigger = "*";
        } else if (triggerType.startsWith("A")) {
            msgType = "ADT";
        } else if (triggerType.startsWith("R")) {
            msgType = "ORU";
        } else if (triggerType.startsWith("O")) {
            msgType = "ORM";
        } else if (triggerType.startsWith("T")) {
            msgType = "MDM";
        } else if (triggerType.startsWith("S")) {
            msgType = "SIU";
        }
        return fromHl7(DEFAULT_HL7_VERSION, msgType, trigger, source != null && !source.isBlank() ? source : "*", null, null, null);
    }

    /**
     * Creates a Topic for an HL7 message with full origin and destination routing.
     */
    public static Topic fromHl7(String version, String messageType, String triggerEvent,
                                String source, String target, String origin, String destination) {
        Topic topic = new Topic();
        topic.setDomain(DOMAIN_HEALTH);
        topic.setModel(MODEL_HL7);
        topic.setModelVersion(version != null && !version.isBlank() ? version : DEFAULT_HL7_VERSION);
        topic.setDataElement(messageType != null ? messageType : "ADT");
        topic.setDataElementQualifier(triggerEvent != null ? triggerEvent : "");
        topic.setSource(source);
        topic.setTarget(target);
        topic.setOrigin(origin);
        topic.setDestination(destination);
        topic.setReceivedDate(new Date());
        return topic;
    }

    // =========================================================================
    // Formatters and Convenience Methods
    // =========================================================================

    /**
     * Returns the composite trigger type string (e.g. "ADT^A01" or "A01").
     */
    public String getCompositeTrigger() {
        if (dataElement != null && !dataElement.isBlank() && dataElementQualifier != null && !dataElementQualifier.isBlank()) {
            return dataElement + "^" + dataElementQualifier;
        }
        if (dataElementQualifier != null && !dataElementQualifier.isBlank()) {
            return dataElementQualifier;
        }
        return dataElement != null ? dataElement : "";
    }

    /**
     * Returns the canonical dot-delimited topic string (e.g. "Health.HL7.2.4.ADT.A01").
     */
    public String toTopicString() {
        return (domain != null ? domain : "*") + "." +
                (model != null ? model : "*") + "." +
                (modelVersion != null ? modelVersion : "*") + "." +
                (dataElement != null ? dataElement : "*") + "." +
                (dataElementQualifier != null ? dataElementQualifier : "*");
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDataElement() {
        return dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public String getDataElementQualifier() {
        return dataElementQualifier;
    }

    public void setDataElementQualifier(String dataElementQualifier) {
        this.dataElementQualifier = dataElementQualifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(domain, topic.domain) &&
                Objects.equals(model, topic.model) &&
                Objects.equals(modelVersion, topic.modelVersion) &&
                Objects.equals(dataElement, topic.dataElement) &&
                Objects.equals(dataElementQualifier, topic.dataElementQualifier) &&
                Objects.equals(source, topic.source) &&
                Objects.equals(target, topic.target) &&
                Objects.equals(origin, topic.origin) &&
                Objects.equals(destination, topic.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, model, modelVersion, dataElement, dataElementQualifier, source, target, origin, destination);
    }

    @Override
    public String toString() {
        return "Topic{" +
                "domain='" + domain + '\'' +
                ", model='" + model + '\'' +
                ", modelVersion='" + modelVersion + '\'' +
                ", dataElement='" + dataElement + '\'' +
                ", dataElementQualifier='" + dataElementQualifier + '\'' +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", receivedDate=" + receivedDate +
                '}';
    }
}
