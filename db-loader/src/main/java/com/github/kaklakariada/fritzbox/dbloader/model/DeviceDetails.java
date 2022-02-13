package com.github.kaklakariada.fritzbox.dbloader.model;

import de.siegmar.fastcsv.reader.NamedCsvRow;

public record DeviceDetails(String deviceName, String macAddress, String readableName, String type, String owner) {

    public String[] toCsv() {
        return new String[] { deviceName, macAddress, readableName, type, owner };
    }

    public static String[] csvHeader() {
        return new String[] { "DEVICE_NAME", "MAC_ADDRESS", "READABLE_NAME", "TYPE", "OWNER" };
    }

    public static DeviceDetails fromCsv(NamedCsvRow row) {
        return new DeviceDetails(row.getField("DEVICE_NAME"), row.getField("MAC_ADDRESS"),
                row.getField("READABLE_NAME"), row.getField("TYPE"), row.getField("OWNER"));
    }
}