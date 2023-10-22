package com.github.kaklakariada.fritzbox.report.model.event;

import java.util.Objects;

import com.github.kaklakariada.fritzbox.report.model.Event;

public abstract class WifiDeviceEvent extends Event {

    private static final long serialVersionUID = 1L;

    protected final String macAddress;
    protected final String name;
    protected final WifiType wifiType;
    protected final String ipAddress;

    protected WifiDeviceEvent(final WifiType wifiType, final String ipAddress, final String macAddress,
            final String name) {
        this.wifiType = wifiType;
        this.ipAddress = ipAddress;
        this.macAddress = Objects.requireNonNull(macAddress, "macAddress");
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }

    public WifiType getWifiType() {
        return wifiType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public abstract boolean isConnectEvent();

    public abstract boolean isDisconnectEvent();

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, macAddress, name, wifiType);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WifiDeviceEvent other = (WifiDeviceEvent) obj;
        return Objects.equals(ipAddress, other.ipAddress) && Objects.equals(macAddress, other.macAddress)
                && Objects.equals(name, other.name) && wifiType == other.wifiType;
    }
}
