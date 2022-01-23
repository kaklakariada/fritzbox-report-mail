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
package com.github.kaklakariada.fritzbox.report.convert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.LogEntryIdGenerator;
import com.github.kaklakariada.fritzbox.report.model.DataConnections;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.EventLogEntry;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;
import com.github.kaklakariada.fritzbox.report.model.InternetConnection;

public class FritzBoxMessageConverter implements Function<EmailContent, FritzBoxReportMail> {
    private static final Logger LOG = LoggerFactory.getLogger(FritzBoxMessageConverter.class);
    private final LogEntryIdGenerator logEntryIdGenerator = new LogEntryIdGenerator();
    private int nextReportId = 0;

    @Override
    public FritzBoxReportMail apply(final EmailContent mail) {

        final DataExtractor extractor = new DataExtractor(mail, nextReportId, logEntryIdGenerator);
        final Map<TimePeriod, DataConnections> dataConnections = extractor.getDataConnections();
        final DataConnections connectionsYesterday = dataConnections.get(TimePeriod.YESTERDAY);
        final List<EventLogEntry> eventLog = extractor.getEventLog();
        final List<InternetConnection> connections = eventLog.stream()
                .flatMap(this::convertInternetConnection)
                .toList();
        LOG.trace("{}: received {}, sent {}, log entries: {}, internet connections: {}",
                extractor.getDate(),
                connectionsYesterday.getReveivedVolume(), connectionsYesterday.getSentVolume(),
                eventLog.size(),
                connections.size());
        return new FritzBoxReportMail(nextReportId++, extractor.getDate(), mail.getMetadata(), dataConnections,
                eventLog, connections);
    }

    private Stream<InternetConnection> convertInternetConnection(final EventLogEntry entry) {
        final String ipPattern = "([\\d\\.]+)";
        final Pattern p = Pattern.compile(".*IP-Adresse: " + ipPattern + ", DNS-Server: " + ipPattern + " und "
                + ipPattern + ", Gateway: " + ipPattern + ", Breitband-PoP: ([\\w-]+)");
        final Matcher matcher = p.matcher(entry.getMessage());
        if (!matcher.matches()) {
            return Stream.empty();
        }
        final String ipAddress = matcher.group(1);
        final String dnsServer1 = matcher.group(2);
        final String dnsServer2 = matcher.group(3);
        final String gateway = matcher.group(4);
        final String pop = matcher.group(5);
        final InternetConnection connection = new InternetConnection(entry.getTimestamp(), ipAddress, dnsServer1,
                dnsServer2, gateway, pop);
        return Stream.of(connection);
    }

}
