package com.github.kaklakariada.fritzbox.report.model.event;

public class WifiDeviceDisconnectedHard extends WifiDeviceDisconnected {

    private static final long serialVersionUID = 1L;

    private final String code;

    public WifiDeviceDisconnectedHard(final String macAddress, final String name, final String code) {
        super(macAddress, name);
        this.code = code;
    }

    @Override
    public String getDescription() {
        return "wifi disconnected: name=" + getName() + ", mac=" + getMacAddress() + ", code=" + code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WifiDeviceDisconnectedHard other = (WifiDeviceDisconnectedHard) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }
}
