package com.github.kaklakariada.fritzbox.report.convert;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.github.kaklakariada.html.HtmlElement;

public class DataExtractorTest {

    private static final String FILE_DATE = "date.html";
    private static final Path TEST_REPORT_PATH = Paths.get("src/test/resources/reports");

    @Before
    public void setUp() {

    }

    @Test
    public void test0505data() {
        assertDate(LocalDate.of(2012, 2, 17), ReportVersion.V05_05);
    }

    @Test
    public void test0650data() {
        assertDate(LocalDate.of(2015, 12, 19), ReportVersion.V06_50);
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
