package com.github.kaklakariada.fritzbox.dbloader;

import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.DeviceDetails;

import org.itsallcode.jdbc.SimpleConnection;

public class GenerateDeviceDetailsTemplate {

    private final SimpleConnection connection;

    public GenerateDeviceDetailsTemplate(SimpleConnection connection) {
        this.connection = connection;
    }

    public List<DeviceDetails> getDeviceDetailsTemplate() {
        return null;
    }
}
