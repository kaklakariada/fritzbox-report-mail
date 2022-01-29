package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;

public class FritzBoxInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String product;
    private final String firmwareVersion;
    private final int energyUsagePercent;

    public FritzBoxInfo(String product, String firmwareVersion, int energyUsagePercent) {
        this.product = product;
        this.firmwareVersion = firmwareVersion;
        this.energyUsagePercent = energyUsagePercent;
    }

    public String getProduct() {
        return product;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public int getEnergyUsagePercent() {
        return energyUsagePercent;
    }
}
