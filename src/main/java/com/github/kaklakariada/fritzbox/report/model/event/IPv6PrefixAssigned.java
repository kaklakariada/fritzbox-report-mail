package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class IPv6PrefixAssigned extends Event {

    private static final long serialVersionUID = 1L;

    private final String prefix;

    public IPv6PrefixAssigned(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getDescription() {
        return "IPv6 prefix assigned, prefix=" + prefix;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
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
        final IPv6PrefixAssigned other = (IPv6PrefixAssigned) obj;
        if (prefix == null) {
            if (other.prefix != null) {
                return false;
            }
        } else if (!prefix.equals(other.prefix)) {
            return false;
        }
        return true;
    }
}
