package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class WifiDeviceDisconnected extends Event {

    private static final long serialVersionUID = 1L;

    private final String macAddress;
    private final String name;

    public WifiDeviceDisconnected(final String macAddress, final String name) {
        this.macAddress = macAddress;
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "wifi disconnected: name=" + name + ", mac=" + macAddress;
    }
}
