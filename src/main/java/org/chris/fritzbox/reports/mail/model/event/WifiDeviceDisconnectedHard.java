package org.chris.fritzbox.reports.mail.model.event;

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
}
