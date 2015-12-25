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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
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
        final WifiDeviceAuthorizationFailed other = (WifiDeviceAuthorizationFailed) obj;
        if (macAddress == null) {
            if (other.macAddress != null) {
                return false;
            }
        } else if (!macAddress.equals(other.macAddress)) {
            return false;
        }
        return true;
    }
}
