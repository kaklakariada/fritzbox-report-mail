package com.github.kaklakariada.fritzbox.report.convert;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.util.*;

import org.apache.james.mime4j.dom.Message;
import org.junit.jupiter.api.Test;

import com.github.kaklakariada.fritzbox.report.LogEntryIdGenerator;
import com.github.kaklakariada.fritzbox.report.model.*;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.DataVolume.Unit;

class DataExtractorTest {

    private static final String FILE_DATE = "date.html";
    private static final String FILE_CONNECTIONS = "data-connections.html";
    private static final Path TEST_REPORT_PATH = Paths.get("src/test/resources/reports");
    private static final int REPORT_ID = 0;

    @Test
    void test0505date() {
        assertDate(LocalDate.of(2012, 2, 17), ReportVersion.V05_05);
    }

    @Test
    void test0650date() {
        assertDate(LocalDate.of(2015, 12, 19), ReportVersion.V06_50);
    }

    @Test
    void test0505connections() {
        final LocalDate date = LocalDate.of(2012, 2, 17);
        assertConnections(ReportVersion.V05_05,
                connection(TimePeriod.YESTERDAY, date, Duration.ofHours(24).plusMinutes(1), volumeMb(5534),
                        volumeMb(753), volumeMb(4781), 1),
                connection(TimePeriod.THIS_WEEK, date, Duration.ofHours(102).plusMinutes(51), volumeMb(18706),
                        volumeMb(2180), volumeMb(16526), 9),
                connection(TimePeriod.THIS_MONTH, date, Duration.ofHours(173).plusMinutes(25), volumeMb(30803),
                        volumeMb(4377), volumeMb(26426), 23),
                connection(TimePeriod.LAST_MONTH, date, Duration.ofHours(161).plusMinutes(27), volumeMb(33013),
                        volumeMb(7624), volumeMb(25389), 38));
    }

    @Test
    void test0650connections() {
        final LocalDate date = LocalDate.of(2015, 12, 19);
        assertConnections(ReportVersion.V06_50,
                connection(TimePeriod.YESTERDAY, date, Duration.ofHours(24).plusMinutes(0), volumeMb(9992),
                        volumeMb(1196), volumeMb(8796), 2),
                connection(TimePeriod.THIS_WEEK, date, Duration.ofHours(144).plusMinutes(1), volumeMb(43553),
                        volumeMb(7816), volumeMb(35737), 12),
                connection(TimePeriod.LAST_WEEK, date, Duration.ofHours(167).plusMinutes(56), volumeMb(79881),
                        volumeMb(21123), volumeMb(58758), 18),
                connection(TimePeriod.THIS_MONTH, date, Duration.ofHours(455).plusMinutes(56), volumeMb(176936),
                        volumeMb(37695), volumeMb(139241), 42));
    }

    @Test
    void test0505log() {
        final LocalDate day = LocalDate.of(2012, 2, 17);
        assertLog(ReportVersion.V05_05, logEntry(0, day.atTime(23, 59, 48), "event1"),
                logEntry(1, day.atTime(23, 57, 35), "event2"),
                logEntry(2, day.atTime(23, 28, 19), "event3"));
    }

    @Test
    void test0650log() {
        final LocalDate day = LocalDate.of(2015, 12, 18);
        assertLog(ReportVersion.V06_50, logEntry(0, day.atTime(22, 28, 25), "event1"),
                logEntry(1, day.atTime(22, 23, 19), "event2"),
                logEntry(2, day.atTime(22, 23, 01), "event3"),
                logEntry(3, day.atTime(22, 23, 01), "event4"),
                logEntry(4, day.atTime(22, 23, 01), "event5"));
    }

    private EventLogEntry logEntry(final int id, final LocalDateTime timestamp, final String message) {
        return new EventLogEntry(REPORT_ID, id, timestamp, message, null);
    }

    private void assertLog(final ReportVersion version, final EventLogEntry... expectedLogEntries) {
        final List<EventLogEntry> actualLogEntries = createExtractor(version, "log.html").getEventLog();
        assertThat(actualLogEntries, hasSize(expectedLogEntries.length));
        assertEquals(asList(expectedLogEntries).toString(), actualLogEntries.toString());
        assertEquals(asList(expectedLogEntries), actualLogEntries);
        assertThat(actualLogEntries, contains(expectedLogEntries));
    }

    private DataVolume volumeMb(final int volumeMb) {
        return DataVolume.of(volumeMb, Unit.MB);
    }

    private DataConnections connection(final TimePeriod timePeriod, final LocalDate date, final Duration onlineTime,
            final DataVolume totalVolume, final DataVolume sentVolume, final DataVolume receivedVolume,
            final int numberOfConnections) {
        return new DataConnections(REPORT_ID, date, timePeriod, onlineTime, totalVolume, sentVolume, receivedVolume,
                numberOfConnections);
    }

    private void assertConnections(final ReportVersion version, final DataConnections... expectedConnections) {
        final Map<TimePeriod, DataConnections> actualConnections = createExtractor(version, FILE_CONNECTIONS)
                .getDataConnections();
        assertThat(actualConnections.keySet(), hasSize(expectedConnections.length));
        assertEquals(expectedConnections.length, actualConnections.size());
        for (final DataConnections expectedConnection : expectedConnections) {
            final DataConnections actual = actualConnections.get(expectedConnection.getTimePeriod());
            assertEquals(expectedConnection.toString(), actual.toString());
            assertEquals(expectedConnection, actual);
        }
    }

    private void assertDate(final LocalDate expectedDate, final ReportVersion version) {
        assertEquals(expectedDate, createExtractor(version, FILE_DATE).getDate());
    }

    private DataExtractor createExtractor(final ReportVersion reportVersion, final String fileName) {
        final Path path = TEST_REPORT_PATH.resolve(reportVersion.getName()).resolve(fileName);
        final String htmlContent = readFile(path);
        final Message messageMock = mock(Message.class);
        when(messageMock.getDate()).thenReturn(new Date());
        when(messageMock.getMessageId()).thenReturn("mock msg id");
        when(messageMock.getSubject()).thenReturn("mock msg subject");
        return DataExtractor.create(new EmailContent(messageMock, List.of(new EmailBody(htmlContent))), REPORT_ID,
                new LogEntryIdGenerator());
    }

    private String readFile(final Path path) throws AssertionError {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error reading from file " + path, e);
        }
    }
}
