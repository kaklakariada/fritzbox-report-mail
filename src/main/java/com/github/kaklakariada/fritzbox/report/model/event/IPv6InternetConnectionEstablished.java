package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class IPv6InternetConnectionEstablished extends Event {

    private static final long serialVersionUID = 1L;

    private final String ipAddress;

    public IPv6InternetConnectionEstablished(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String getDescription() {
        return "IPv6 connection established, ip=" + ipAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
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
        final IPv6InternetConnectionEstablished other = (IPv6InternetConnectionEstablished) obj;
        if (ipAddress == null) {
            if (other.ipAddress != null) {
                return false;
            }
        } else if (!ipAddress.equals(other.ipAddress)) {
            return false;
        }
        return true;
    }
}
