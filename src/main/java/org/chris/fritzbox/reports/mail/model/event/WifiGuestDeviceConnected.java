package org.chris.fritzbox.reports.mail.model.event;

import org.chris.fritzbox.reports.mail.model.Event;

public class WifiGuestDeviceConnected extends Event {

    private static final long serialVersionUID = 1L;

    private final String speed;
    private final String macAddress;

    public WifiGuestDeviceConnected(final String speed, final String macAddress) {
        this.speed = speed;
        this.macAddress = macAddress;
    }

    @Override
    public String getDescription() {
        return "wifi guest connected: speed=" + speed + ", mac=" + macAddress;
    }

    public String getSpeed() {
        return speed;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
