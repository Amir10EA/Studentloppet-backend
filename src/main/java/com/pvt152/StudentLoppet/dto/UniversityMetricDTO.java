package com.pvt152.StudentLoppet.dto;

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

    public double getMetricAsDouble() {
        return metric.doubleValue();
    }

    public void setMetric(Number metric) {
        this.metric = metric;
    }
}
