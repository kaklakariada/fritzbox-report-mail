package com.github.kaklakariada.fritzbox.report.model;

public record DeviceDetails(String deviceName, String macAddress, String readableName, String type, String owner) {
}