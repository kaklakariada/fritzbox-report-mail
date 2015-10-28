package org.chris.fritzbox.reports.mail.model.event;

import org.chris.fritzbox.reports.mail.model.Event;

public class WifiDeviceConnected extends Event {

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
