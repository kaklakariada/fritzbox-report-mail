package com.github.kaklakariada.fritzbox.reports.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InternetConnection implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LocalDateTime timestamp;
    private final String ipAddress;
    private final String dnsServer1;
    private final String dnsServer2;
    private final String gateway;
    private final String pop;

    public InternetConnection(final LocalDateTime timestamp, final String ipAddress, final String dnsServer1,
            final String dnsServer2, final String gateway, final String pop) {
        super();
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.dnsServer1 = dnsServer1;
        this.dnsServer2 = dnsServer2;
        this.gateway = gateway;
        this.pop = pop;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDnsServer1() {
        return dnsServer1;
    }

    public String getDnsServer2() {
        return dnsServer2;
    }

    public String getGateway() {
        return gateway;
    }

    public String getPop() {
        return pop;
    }

    @Override
    public String toString() {
        return "InternetConnection [" + timestamp + ", ip: " + ipAddress + ", dnsServer1=" + dnsServer1
                + ", dnsServer2=" + dnsServer2 + ", gateway=" + gateway + ", pop=" + pop + "]";
    }
}
