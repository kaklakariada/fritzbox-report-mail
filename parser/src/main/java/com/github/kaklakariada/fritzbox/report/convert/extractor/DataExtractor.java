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
package com.github.kaklakariada.fritzbox.report.convert.extractor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.github.kaklakariada.fritzbox.report.LogEntryIdGenerator;
import com.github.kaklakariada.fritzbox.report.convert.EmailBody.Type;
import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.fritzbox.report.model.*;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.eventfactory.EventLogEntryFactory;
import com.github.kaklakariada.html.HtmlElement;

public class DataExtractor {
    private static final Logger LOG = Logger.getLogger(DataExtractor.class.getName());
    private static final DateTimeFormatter LOG_ENTRY_TIMESTAMP_FORMAT = DateTimeFormatter
            .ofPattern("dd.MM.yy HH:mm:ss");

    private final HtmlElement rootElement;
    private final EventLogEntryFactory eventLogEntryFactory;
    private final int reportId;
    private final LogEntryIdGenerator logEntryIdGenerator;
    private final LocalDate date;

    public static DataExtractor create(final EmailContent mail, final int reportId,
            final LogEntryIdGenerator logEntryIdGenerator) {
        return new DataExtractor(mail, getBody(mail), new EventLogEntryFactory(), reportId, logEntryIdGenerator);
    }

    private static HtmlElement getBody(final EmailContent mail) {
        return mail.getPart(Type.HTML).getElement();
    }

    private DataExtractor(final EmailContent mail, final HtmlElement rootElement,
            final EventLogEntryFactory eventLogEntryFactory, final int reportId,
            final LogEntryIdGenerator logEntryIdGenerator) {
        this.rootElement = rootElement;
        this.eventLogEntryFactory = eventLogEntryFactory;
        this.reportId = reportId;
        this.logEntryIdGenerator = logEntryIdGenerator;
        this.date = new ReportDateExtractor().extractDate(mail, rootElement);
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<TimePeriod, DataConnections> getDataConnections() {
        final Optional<HtmlElement> section = getOptionalSection("Online-Zähler");
        if (section.isEmpty()) {
            return Collections.emptyMap();
        }
        List<DataConnections> connectionsList = section.get()
                .map("div.backdialog>div.foredialog>table>tbody>tr", this::convertDataConnection);
        if (connectionsList.isEmpty()) {
            connectionsList = section.get().map("table>tbody>tr:nth-child(3)>td>table>tbody>tr>td>table>tbody>tr",
                    this::convertNewDataConnection);
        }

        if (connectionsList.size() < 4) {
            throw new IllegalStateException("Did not find all data connections in " + section + " for report " + date);
        }
        return connectionsList.stream().collect(Collectors.toMap(DataConnections::getTimePeriod, Function.identity()));
    }

    private DataConnections convertNewDataConnection(final HtmlElement row) {
        final HtmlElement firstCol = row.selectSingleElement("tr>*:nth-child(1)");
        if (!firstCol.getName().equals("td")) {
            return null;
        }
        final List<HtmlElement> cells = row.select("td");
        if (cells.size() != 7) {
            throw new IllegalStateException("Expected 7 cells but got " + cells.size() + ": " + row);
        }
        final TimePeriod timePeriod = TimePeriod.forName(cells.get(0).text());
        final Duration onlineTime = parseDuration(cells.get(1).text());
        final DataVolume totalVolume = DataVolume.parse(cells.get(2).text());
        final DataVolume sentVolume = DataVolume.parse(cells.get(3).text());
        final DataVolume receivedVolume = DataVolume.parse(cells.get(4).text());
        final int numberOfConnections = cells.get(5).number();
        return new DataConnections(reportId, date, timePeriod, onlineTime, totalVolume, sentVolume, receivedVolume,
                numberOfConnections);
    }

    private DataConnections convertDataConnection(final HtmlElement row) {
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
        final String[] sentReceivedVolume = cells.get(2).text().split("/");
        if (sentReceivedVolume.length != 2) {
            throw new IllegalStateException("Sent/received volume has wrong format: " + cells.get(2).text());
        }
        final DataVolume sentVolume = DataVolume.parse(sentReceivedVolume[0]);
        final DataVolume receivedVolume = DataVolume.parse(sentReceivedVolume[1]);
        final int numberOfConnections = cells.get(3).number();
        return new DataConnections(reportId, date, timePeriod, onlineTime, totalVolume, sentVolume, receivedVolume,
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
        final Optional<HtmlElement> section = getOptionalSection("Ereignisse");
        if (section.isEmpty()) {
            return Collections.emptyList();
        }
        final List<EventLogEntry> entries = section.get().map("div.foredialog > table tr", this::convertEventLog);
        if (!entries.isEmpty()) {
            return entries;
        }
        return section.get().map(
                "table>tbody>tr:nth-child(2)>td>table>tbody>tr:nth-child(2)>td>table>tbody>tr",
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
        return new EventLogEntry(reportId, logEntryIdGenerator.getNextId(), timestamp, message, event);
    }

    private Optional<HtmlElement> getOptionalSection(final String sectionName) {
        final String selector = "div.content div.foretitel:containsOwn(" + sectionName + ")";
        final HtmlElement sectionTitle = rootElement.selectOptionalSingleElement(selector);
        if (sectionTitle != null) {
            final HtmlElement oldContentDiv = sectionTitle.getNthAncestor(6);
            if (oldContentDiv.getCssClass().equals("content") && oldContentDiv.getName().equals("div")) {
                return Optional.of(oldContentDiv);
            }
            throw new IllegalStateException("Found invalid content div " + oldContentDiv);
        }
        return rootElement.selectOptionalElementWithContent("td", sectionName)
                .map(e -> e.getNthAncestor(7));
    }

    public FritzBoxInfo getFritzBoxInfo() {
        return new FritzBoxInfo(getProductInfo(), getProductVersion(), getEnergyUsage());
    }

    private int getEnergyUsage() {
        final HtmlElement element = rootElement.selectSingleElement("td:containsOwn(Energieverbrauch) ~ td");
        final int energyUsage = Integer.parseInt(element.getText().replace('%', ' ').trim());
        if (energyUsage < 0 || energyUsage > 100) {
            throw new IllegalStateException("Invalid energy usage " + energyUsage + " found for report " + date);
        }
        return energyUsage;
    }

    private String getProductVersion() {
        HtmlElement element = rootElement.selectOptionalSingleElement("td:containsOwn(FRITZ!OS) ~ td");
        if (element == null) {
            element = rootElement.selectSingleElement("td:containsOwn(Firmware) ~ td");
        }
        String version = element.getText();
        final String prefix = "FRITZ!OS";
        if (version.startsWith(prefix)) {
            version = version.substring(prefix.length());
        }
        version = version.trim();
        if (version.isEmpty()) {
            throw new IllegalStateException("No version number found for report " + date);
        }
        return version;
    }

    private String getProductInfo() {
        final String productInfo = rootElement.selectSingleElement("td:containsOwn(Produktname) ~ td").getText().trim();
        if (productInfo.isEmpty()) {
            throw new IllegalStateException("No product info found for report " + date);
        }
        return productInfo;
    }
}
