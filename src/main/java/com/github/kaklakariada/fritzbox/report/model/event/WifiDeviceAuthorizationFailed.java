package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class WifiDeviceAuthorizationFailed extends Event {

    private static final long serialVersionUID = 1L;

    private final String macAddress;

    public WifiDeviceAuthorizationFailed(final String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String getDescription() {
        return "wifi authorization failed: mac=" + macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
