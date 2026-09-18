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

package net.fhirfactory.harmonia.befe.model.operations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Metric time-series representation containing historical data points over a given window
 * (e.g., 15m, 1h, 6h, 24h) along with aggregate summary statistics.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSeries implements Serializable {

    private String metricName;
    private String window;               // 15m, 1h, 6h, 24h
    private String unit;                 // ms, msg/s, %, count, etc.
    private List<TimeSeriesPoint> points = new ArrayList<>();
    private Map<String, Double> summary = new HashMap<>();

    public TimeSeries() {
    }

    public TimeSeries(String metricName, String window, String unit) {
        this.metricName = metricName;
        this.window = window;
        this.unit = unit;
    }

    public TimeSeries(String metricName, String window, String unit, List<TimeSeriesPoint> points) {
        this.metricName = metricName;
        this.window = window;
        this.unit = unit;
        if (points != null) {
            this.points = new ArrayList<>(points);
            calculateSummary();
        }
    }

    public void calculateSummary() {
        if (points == null || points.isEmpty()) {
            return;
        }
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        double sum = 0.0;
        for (TimeSeriesPoint pt : points) {
            double v = pt.getValue();
            if (v < min) min = v;
            if (v > max) max = v;
            sum += v;
        }
        double avg = sum / points.size();
        double current = points.get(points.size() - 1).getValue();

        summary.put("min", min);
        summary.put("max", max);
        summary.put("avg", avg);
        summary.put("current", current);
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<TimeSeriesPoint> getPoints() {
        return points;
    }

    public void setPoints(List<TimeSeriesPoint> points) {
        this.points = points != null ? new ArrayList<>(points) : new ArrayList<>();
        calculateSummary();
    }

    public Map<String, Double> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, Double> summary) {
        this.summary = summary != null ? new HashMap<>(summary) : new HashMap<>();
    }
}
