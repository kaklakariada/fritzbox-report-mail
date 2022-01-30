package com.github.kaklakariada.fritzbox.report.model;

import java.time.LocalDateTime;

import com.github.kaklakariada.fritzbox.report.model.event.WifiType;

public class WifiConnection {
    private final String deviceName;
    private final String macAddress;
    private final String speed;
    private final WifiType wifiType;
    private final LocalDateTime begin;
    private final LocalDateTime end;

    public WifiConnection(String deviceName, String macAddress, String speed, WifiType wifiType,
            LocalDateTime begin, LocalDateTime end) {
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.speed = speed;
        this.wifiType = wifiType;
        this.begin = begin;
        this.end = end;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getSpeed() {
        return speed;
    }

    public WifiType getWifiType() {
        return wifiType;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
