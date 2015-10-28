package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.reports.model.Event;

public class WifiGuestDeviceDisconnected extends Event {

    private static final long serialVersionUID = 1L;

    private final String macAddress;

    public WifiGuestDeviceDisconnected(final String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public String getDescription() {
        return "wifi guest disconnected: mac=" + macAddress;
    }
}
