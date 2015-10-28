package com.github.kaklakariada.fritzbox.report.convert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.kaklakariada.fritzbox.report.model.eventfactory.EventLogEntryFactory;
import com.github.kaklakariada.fritzbox.reports.model.DataConnections;
import com.github.kaklakariada.fritzbox.reports.model.DataVolume;
import com.github.kaklakariada.fritzbox.reports.model.Event;
import com.github.kaklakariada.fritzbox.reports.model.EventLogEntry;
import com.github.kaklakariada.fritzbox.reports.model.DataConnections.TimePeriod;
import com.github.kaklakariada.html.HtmlElement;

class DataExtractor {

    private final DateTimeFormatter REPORT_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final DateTimeFormatter LOG_ENTRY_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
    private final HtmlElement mail;

    DataExtractor(final HtmlElement mail) {
        this.mail = mail;
    }

    public String getHtmlTitle() {
        return mail.selectSingleElement("html>head>title").text();
    }

    public LocalDateTime getDate() {
        final String date = mail.getRegexpResult("td:containsOwn(Ihre FRITZ!Box Verbindungsübersicht)",
                "Ihre FRITZ!Box Verbindungsübersicht vom ([\\d\\.: ]+) Uhr");
        return LocalDateTime.parse(date, REPORT_TIMESTAMP_FORMAT);
    }

    public Map<TimePeriod, DataConnections> getDataConnections() {
        final LocalDateTime date = getDate();
        final HtmlElement section = getSection("Online-Zähler");
        final List<DataConnections> connectionsList = section.map("div.backdialog>div.foredialog>table>tbody>tr",
                row -> convertDataConnection(date, row));
        if (connectionsList.size() < 4) {
            throw new AssertionError("Did not find all data connections in " + section);
        }
        return connectionsList.stream().collect(Collectors.toMap(DataConnections::getTimePeriod, Function.identity()));
    }

    private DataConnections convertDataConnection(final LocalDateTime date, final HtmlElement row) {
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
        return section.map("div.foredialog > table tr", this::convertEventLog);
    }

    public EventLogEntry convertEventLog(final HtmlElement element) {
        final List<HtmlElement> cells = element.select("td");
        if (cells.isEmpty() || cells.size() == 3) {
            return null;
        }
        if (cells.size() != 2) {
            System.out.println(mail);
            throw new IllegalStateException("Expected 2 cells but got " + cells.size() + ": " + cells);
        }

        final LocalDateTime timestamp = LocalDateTime.parse(cells.get(0).text(), LOG_ENTRY_TIMESTAMP_FORMAT);
        final String message = cells.get(1).text();
        final Event event = EventLogEntryFactory.createEventLogEntry(timestamp, message);
        return new EventLogEntry(timestamp, message, event);
    }

    private HtmlElement getSection(final String sectionName) {
        final HtmlElement contentDiv = mail
                .selectSingleElement("div.content div.foretitel:containsOwn(" + sectionName + ")").getNthAncestor(6);
        if (contentDiv.getCssClass().equals("content") && contentDiv.getName().equals("div")) {
            return contentDiv;
        }
        throw new AssertionError("Found invalid content div " + contentDiv);
    }
}
