package org.chris.fritzbox.reports.mail.model.event;

import org.chris.fritzbox.reports.mail.model.Event;

public class WifiGuestDeviceDisconnected extends Event {

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
