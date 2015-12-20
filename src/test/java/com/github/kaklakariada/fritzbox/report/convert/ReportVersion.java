package com.github.kaklakariada.fritzbox.report.convert;

public enum ReportVersion {
    V05_05("05.05"), V05_21("05.21"), V05_50("05.50"), V06_20("06.20"), V06_24("06.24"), V06_50("06.50");
    private final String name;

    private ReportVersion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
