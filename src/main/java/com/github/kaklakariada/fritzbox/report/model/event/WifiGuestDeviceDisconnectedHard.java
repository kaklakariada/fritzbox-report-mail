package com.github.kaklakariada.fritzbox.report.model.event;

public class WifiGuestDeviceDisconnectedHard extends WifiGuestDeviceDisconnected {

    private static final long serialVersionUID = 1L;

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
