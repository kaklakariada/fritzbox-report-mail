package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.report.model.Event;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
        result = prime * result + ((speed == null) ? 0 : speed.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WifiGuestDeviceConnected other = (WifiGuestDeviceConnected) obj;
        if (macAddress == null) {
            if (other.macAddress != null) {
                return false;
            }
        } else if (!macAddress.equals(other.macAddress)) {
            return false;
        }
        if (speed == null) {
            if (other.speed != null) {
                return false;
            }
        } else if (!speed.equals(other.speed)) {
            return false;
        }
        return true;
    }
}
