package com.github.kaklakariada.fritzbox.report.model.event;

import java.util.Arrays;

public enum WifiType {
    _5_GHZ("5 GHz"), _2_4_GHZ("2.4 GHz");

    private String name;

    private WifiType(String name) {
        this.name = name;
    }

    public static WifiType parse(String value) {
        return Arrays.stream(values())
                .filter(t -> t.name.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No wifi type found for string '" + value + "'"));
    }

    @Override
    public String toString() {
        return name;
    }
}
