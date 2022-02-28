/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class DslSyncSuccessful extends Event {

    private static final long serialVersionUID = 1L;

    public DslSyncSuccessful(int downloadKbitPs, int uploadKbitPs) {
        this.downloadKbitPs = downloadKbitPs;
        this.uploadKbitPs = uploadKbitPs;
    }

    private final int downloadKbitPs;
    private final int uploadKbitPs;

    @Override
    public String getDescription() {
        return "DSL sync successful, down: " + downloadKbitPs + "kbit/s, up: " + uploadKbitPs + "kbit/s";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + downloadKbitPs;
        result = prime * result + uploadKbitPs;
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
        final DslSyncSuccessful other = (DslSyncSuccessful) obj;
        if (downloadKbitPs != other.downloadKbitPs) {
            return false;
        }
        return uploadKbitPs == other.uploadKbitPs;
    }
}
