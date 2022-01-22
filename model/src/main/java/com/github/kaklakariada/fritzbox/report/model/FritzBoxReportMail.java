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
package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;

public class FritzBoxReportMail implements Serializable {

	private static final long serialVersionUID = 1L;
	private final LocalDate date;
	private final Map<TimePeriod, DataConnections> dataConnections;
	private final List<EventLogEntry> eventLog;
	private final List<InternetConnection> connections;
	private final EmailMetadata emailMetadata;

	public FritzBoxReportMail(final LocalDate date, EmailMetadata emailMetadata,
			final Map<TimePeriod, DataConnections> dataConnections,
			final List<EventLogEntry> eventLog, final List<InternetConnection> connections) {
		this.date = date;
		this.emailMetadata = emailMetadata;
		this.dataConnections = dataConnections;
		this.eventLog = eventLog;
		this.connections = connections;
	}

	public LocalDate getDate() {
		return date;
	}

	public EmailMetadata getEmailMetadata() {
		return emailMetadata;
	}

	public Map<TimePeriod, DataConnections> getDataConnections() {
		return dataConnections;
	}

	public List<EventLogEntry> getEventLog() {
		return eventLog;
	}

	public List<InternetConnection> getConnections() {
		return connections;
	}
}
