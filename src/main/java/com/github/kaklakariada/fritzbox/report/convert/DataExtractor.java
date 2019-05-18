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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.model.DataConnections;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.DataVolume;
import com.github.kaklakariada.fritzbox.report.model.Event;
import com.github.kaklakariada.fritzbox.report.model.EventLogEntry;
import com.github.kaklakariada.fritzbox.report.model.eventfactory.EventLogEntryFactory;
import com.github.kaklakariada.html.HtmlElement;

class DataExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(DataExtractor.class);
    private static final DateTimeFormatter NEW_REPORT_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter OLD_REPORT_TIMESTAMP_FORMAT = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter LOG_ENTRY_TIMESTAMP_FORMAT = DateTimeFormatter
            .ofPattern("dd.MM.yy HH:mm:ss");
    private final HtmlElement mail;
    private final EventLogEntryFactory eventLogEntryFactory;

    DataExtractor(final HtmlElement mail) {
        this(mail, new EventLogEntryFactory());
    }

    private DataExtractor(final HtmlElement mail, EventLogEntryFactory eventLogEntryFactory) {
        this.mail = mail;
        this.eventLogEntryFactory = eventLogEntryFactory;
    }

    public String getHtmlTitle() {
        return mail.selectSingleElement("html>head>title").text();
    }

    public LocalDate getDate() {
        final String oldDate = mail.getOptionalRegexpResult("td:containsOwn(Ihre FRITZ!Box Verbindungsübersicht)",
                "Ihre FRITZ!Box Verbindungsübersicht vom ([\\d\\.: ]+) Uhr");
        if (oldDate != null) {
            final LocalDateTime dateTime = LocalDateTime.parse(oldDate, OLD_REPORT_TIMESTAMP_FORMAT);
            return dateTime.toLocalDate().minusDays(1);
        }
        final String newDate = mail.getOptionalRegexpResult(
                "td:containsOwn(Ihre tägliche FRITZ!Box Verbindungsübersicht vom)",
                "Ihre tägliche FRITZ!Box Verbindungsübersicht vom ([\\d\\.]+)(:? .*)?");
        return LocalDate.parse(newDate, NEW_REPORT_TIMESTAMP_FORMAT);
    }

    public Map<TimePeriod, DataConnections> getDataConnections() {
        final LocalDate date = getDate();
        final HtmlElement section = getSection("Online-Zähler");
        List<DataConnections> connectionsList = section.map("div.backdialog>div.foredialog>table>tbody>tr",
                row -> convertDataConnection(date, row));
        if (connectionsList.isEmpty()) {
            connectionsList = section.map("table>tbody>tr:nth-child(3)>td>table>tbody>tr>td>table>tbody>tr",
                    row -> convertNewDataConnection(date, row));
        }

        if (connectionsList.size() < 4) {
            throw new AssertionError("Did not find all data connections in " + section);
        }
        return connectionsList.stream().collect(Collectors.toMap(DataConnections::getTimePeriod, Function.identity()));
    }

    private DataConnections convertNewDataConnection(LocalDate date, HtmlElement row) {
        final HtmlElement firstCol = row.selectSingleElement("tr>*:nth-child(1)");
        if (!firstCol.getName().equals("td")) {
            LOG.trace("Ignore row {} with first col {}", row, firstCol);
            return null;
        }
        final List<HtmlElement> cells = row.select("td");
        LOG.trace("Parse {} cells: {}", cells.size(), row);
        if (cells.size() != 7) {
            throw new IllegalStateException("Expected 7 cells but got " + cells.size() + ": " + row);
        }
        final TimePeriod timePeriod = TimePeriod.forName(cells.get(0).text());
        final Duration onlineTime = parseDuration(cells.get(1).text());
        final DataVolume totalVolume = DataVolume.parse(cells.get(2).text());
        final DataVolume sentVolume = DataVolume.parse(cells.get(3).text());
        final DataVolume reveivedVolume = DataVolume.parse(cells.get(4).text());
        final int numberOfConnections = cells.get(5).number();
        return new DataConnections(date, timePeriod, onlineTime, totalVolume, sentVolume, reveivedVolume,
                numberOfConnections);
    }

    private DataConnections convertDataConnection(final LocalDate date, final HtmlElement row) {
        final String firstCol = row.select("th").get(0).text().trim();
        if (firstCol.length() <= 1 || firstCol.equals("Zeitraum")) {
            return null;
        }
        final List<HtmlElement> cells = row.select("td");
        if (cells.size() != 4) {
            throw new IllegalStateException("Expected 4 cells but got " + cells.size() + ": " + cells);
        }
        final TimePeriod timePeriod = TimePeriod.forName(firstCol);
        final Duration onlineTime = parseDuration(cells.get(0).text());
        final DataVolume totalVolume = DataVolume.parse(cells.get(1).text());
        final String[] sentReceivedVolumen = cells.get(2).text().split("/");
        if (sentReceivedVolumen.length != 2) {
            throw new IllegalStateException("Sent/received volume has wrong format: " + cells.get(2).text());
        }
        final DataVolume sentVolume = DataVolume.parse(sentReceivedVolumen[0]);
        final DataVolume reveivedVolume = DataVolume.parse(sentReceivedVolumen[1]);
        final int numberOfConnections = cells.get(3).number();
        return new DataConnections(date, timePeriod, onlineTime, totalVolume, sentVolume, reveivedVolume,
                numberOfConnections);
    }

    private Duration parseDuration(final String value) {
        final String[] split = value.trim().split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid time format: " + value);
        }
        final int hours = Integer.parseInt(split[0]);
        final int minutes = Integer.parseInt(split[1]);
        return Duration.ofHours(hours).plusMinutes(minutes);
    }

    public List<EventLogEntry> getEventLog() {
        final HtmlElement section = getSection("Ereignisse");
        final List<EventLogEntry> entries = section.map("div.foredialog > table tr", this::convertEventLog);
        if (!entries.isEmpty()) {
            return entries;
        }
        return section.map("table>tbody>tr:nth-child(2)>td>table>tbody>tr:nth-child(2)>td>table>tbody>tr",
                this::convertEventLog);
    }

    private EventLogEntry convertEventLog(final HtmlElement element) {
        final List<HtmlElement> cells = element.select("td");
        if (cells.isEmpty() || cells.size() == 3) {
            return null;
        }
        if (cells.size() != 2) {
            throw new IllegalStateException("Expected 2 cells but got " + cells.size() + ": " + cells);
        }

        final LocalDateTime timestamp = LocalDateTime.parse(cells.get(0).text(), LOG_ENTRY_TIMESTAMP_FORMAT);
        final String message = cells.get(1).text();
        final Event event = eventLogEntryFactory.createEventLogEntry(message);
        return new EventLogEntry(timestamp, message, event);
    }

    private HtmlElement getSection(final String sectionName) {
        final HtmlElement sectionTitle = mail
                .selectOptionalSingleElement("div.content div.foretitel:containsOwn(" + sectionName + ")");
        if (sectionTitle != null) {
            final HtmlElement oldContentDiv = sectionTitle.getNthAncestor(6);
            if (oldContentDiv.getCssClass().equals("content") && oldContentDiv.getName().equals("div")) {
                return oldContentDiv;
            }
            throw new AssertionError("Found invalid content div " + oldContentDiv);
        }
        return mail.selectElementWithContent("td", sectionName).getNthAncestor(7);
    }
}
