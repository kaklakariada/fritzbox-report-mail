package com.github.kaklakariada.fritzbox.report.convert;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.DataConnections;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.DataVolume;
import com.github.kaklakariada.fritzbox.report.model.DataVolume.Unit;
import com.github.kaklakariada.fritzbox.report.model.EventLogEntry;
import com.github.kaklakariada.html.HtmlElement;

public class DataExtractorTest {

    private static final String FILE_DATE = "date.html";
    private static final String FILE_CONNECTIONS = "data-connections.html";
    private static final Path TEST_REPORT_PATH = Paths.get("src/test/resources/reports");

    @Before
    public void setUp() {

    }

    @Test
    public void test0505date() {
        assertDate(LocalDate.of(2012, 2, 17), ReportVersion.V05_05);
    }

    @Test
    public void test0650date() {
        assertDate(LocalDate.of(2015, 12, 19), ReportVersion.V06_50);
    }

    @Test
    public void test0505connections() {
        final LocalDate date = LocalDate.of(2012, 2, 17);
        assertConnections(ReportVersion.V05_05, //
                connection(TimePeriod.YESTERDAY, date, Duration.ofHours(24).plusMinutes(1), volumeMb(5534),
                        volumeMb(753), volumeMb(4781), 1), //
                connection(TimePeriod.THIS_WEEK, date, Duration.ofHours(102).plusMinutes(51), volumeMb(18706),
                        volumeMb(2180), volumeMb(16526), 9), //
                connection(TimePeriod.THIS_MONTH, date, Duration.ofHours(173).plusMinutes(25), volumeMb(30803),
                        volumeMb(4377), volumeMb(26426), 23), //
                connection(TimePeriod.LAST_MONTH, date, Duration.ofHours(161).plusMinutes(27), volumeMb(33013),
                        volumeMb(7624), volumeMb(25389), 38));
    }

    @Test
    public void test0650connections() {
        final LocalDate date = LocalDate.of(2015, 12, 19);
        assertConnections(ReportVersion.V06_50, //
                connection(TimePeriod.YESTERDAY, date, Duration.ofHours(24).plusMinutes(0), volumeMb(9992),
                        volumeMb(1196), volumeMb(8796), 2), //
                connection(TimePeriod.THIS_WEEK, date, Duration.ofHours(144).plusMinutes(1), volumeMb(43553),
                        volumeMb(7816), volumeMb(35737), 12), //
                connection(TimePeriod.LAST_WEEK, date, Duration.ofHours(167).plusMinutes(56), volumeMb(79881),
                        volumeMb(21123), volumeMb(58758), 18), //
                connection(TimePeriod.THIS_MONTH, date, Duration.ofHours(455).plusMinutes(56), volumeMb(176936),
                        volumeMb(37695), volumeMb(139241), 42));
    }

    @Test
    public void test0505log() {
        final LocalDate day = LocalDate.of(2012, 2, 17);
        assertLog(ReportVersion.V05_05, logEntry(day.atTime(23, 59, 48), "event1"), //
                logEntry(day.atTime(23, 57, 35), "event2"), //
                logEntry(day.atTime(23, 28, 19), "event3"));
    }

    @Test
    public void test0650log() {
        final LocalDate day = LocalDate.of(2015, 12, 18);
        assertLog(ReportVersion.V06_50, logEntry(day.atTime(22, 28, 25), "event1"), //
                logEntry(day.atTime(22, 23, 19), "event2"), //
                logEntry(day.atTime(22, 23, 01), "event3"));
    }

    private EventLogEntry logEntry(LocalDateTime timestamp, String message) {
        return new EventLogEntry(timestamp, message, null);
    }

    private void assertLog(ReportVersion version, EventLogEntry... expectedLogEntries) {
        final List<EventLogEntry> actualLogEntries = createExtractor(version, "log.html").getEventLog();
        assertThat(actualLogEntries, hasSize(expectedLogEntries.length));
        assertEquals(asList(expectedLogEntries).toString(), actualLogEntries.toString());
        assertEquals(asList(expectedLogEntries), actualLogEntries);
        assertThat(actualLogEntries, contains(expectedLogEntries));
    }

    private DataVolume volumeMb(int volumeMb) {
        return DataVolume.of(volumeMb, Unit.MB);
    }

    private DataConnections connection(TimePeriod timePeriod, LocalDate date, Duration onlineTime,
            DataVolume totalVolume, DataVolume sentVolume, DataVolume reveivedVolume, int numberOfConnections) {
        return new DataConnections(date, timePeriod, onlineTime, totalVolume, sentVolume, reveivedVolume,
                numberOfConnections);
    }

    private void assertConnections(ReportVersion version, DataConnections... expectedConnections) {
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

    private void assertDate(LocalDate expectedDate, ReportVersion version) {
        assertEquals(expectedDate, createExtractor(version, FILE_DATE).getDate());
    }

    private DataExtractor createExtractor(ReportVersion reportVersion, String fileName) {
        final Path path = TEST_REPORT_PATH.resolve(reportVersion.getName()).resolve(fileName);
        final String htmlContent = readFile(path);
        final HtmlElement mail = new HtmlElement(htmlContent);
        return new DataExtractor(mail);
    }

    private String readFile(final Path path) throws AssertionError {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new AssertionError("Error reading from file " + path, e);
        }
    }
}
