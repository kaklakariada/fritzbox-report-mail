package com.github.kaklakariada.fritzbox.report.convert;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.DataConnections;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.DataVolume;
import com.github.kaklakariada.fritzbox.report.model.DataVolume.Unit;
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
        assertConnections(ReportVersion.V05_05, //
                connection(TimePeriod.YESTERDAY, LocalDate.of(2012, 2, 17), Duration.ofHours(24).plusMinutes(1),
                        volumeMb(5534), volumeMb(753), volumeMb(4781), 1), //
                connection(TimePeriod.THIS_WEEK, LocalDate.of(2012, 2, 17), Duration.ofHours(102).plusMinutes(51),
                        volumeMb(18706), volumeMb(2180), volumeMb(16526), 9), //
                connection(TimePeriod.THIS_MONTH, LocalDate.of(2012, 2, 17), Duration.ofHours(173).plusMinutes(25),
                        volumeMb(30803), volumeMb(4377), volumeMb(26426), 23), //
                connection(TimePeriod.LAST_MONTH, LocalDate.of(2012, 2, 17), Duration.ofHours(161).plusMinutes(27),
                        volumeMb(33013), volumeMb(7624), volumeMb(25389), 38));
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
        assertEquals(expectedConnections.length, actualConnections.size());
        for (final DataConnections expectedConnection : expectedConnections) {
            assertEquals(expectedConnection, actualConnections.get(expectedConnection.getTimePeriod()));
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
