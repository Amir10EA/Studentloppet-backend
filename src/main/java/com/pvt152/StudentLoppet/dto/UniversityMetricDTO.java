package com.pvt152.StudentLoppet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UniversityMetricDTO {
    private String universityDisplayName;
    private Number metric;

    public UniversityMetricDTO(String universityDisplayName, Number metric) {
        this.universityDisplayName = universityDisplayName;
        this.metric = metric;
    }

    public String getUniversityDisplayName() {
        return universityDisplayName;
    }

    public void setUniversityDisplayName(String universityDisplayName) {
        this.universityDisplayName = universityDisplayName;
    }

    public Number getMetric() {
        return metric;
    }

    @JsonIgnore
    public double getMetricAsDouble() {
        return metric.doubleValue();
    }

    public void setMetric(Number metric) {
        this.metric = metric;
    }
}