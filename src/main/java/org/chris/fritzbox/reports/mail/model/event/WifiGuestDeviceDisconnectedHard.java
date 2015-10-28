package org.chris.fritzbox.reports.mail.model.event;

public class WifiGuestDeviceDisconnectedHard extends WifiGuestDeviceDisconnected {

    private final String code;

    public WifiGuestDeviceDisconnectedHard(final String macAddress, final String code) {
        super(macAddress);
        this.code = code;
    }

    @Override
    public String getDescription() {
        return "wifi guest disconnected: mac=" + getMacAddress() + ", code=" + code;
    }

    public String getCode() {
        return code;
    }
}