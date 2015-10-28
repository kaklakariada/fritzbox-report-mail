package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class WifiDeviceConnected extends Event {

    private static final long serialVersionUID = 1L;

    private final String speed;
    private final String macAddress;
    private final String name;

    public WifiDeviceConnected(final String speed, final String macAddress, final String name) {
        this.speed = speed;
        this.macAddress = macAddress;
        this.name = name;
    }

    @Override
    public String getDescription() {
        return "wifi connected: name=" + name + ", speed=" + speed + ", mac=" + macAddress;
    }

    public String getSpeed() {
        return speed;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }
}
