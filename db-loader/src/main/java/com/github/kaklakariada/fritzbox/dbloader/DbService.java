package com.github.kaklakariada.fritzbox.dbloader;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.dbloader.model.DeviceDetails;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;

import org.itsallcode.jdbc.ConnectionFactory;
import org.itsallcode.jdbc.SimpleConnection;

import de.siegmar.fastcsv.reader.NamedCsvReader;
import de.siegmar.fastcsv.writer.CsvWriter;

public class DbService {

    private final SimpleConnection connection;
    private final ExasolDao dao;
    private final String schema;

    public DbService(SimpleConnection connection, String schema) {
        this.connection = connection;
        this.schema = schema;
        this.dao = new ExasolDao(connection, schema);
    }

    public static DbService connect(String jdbcUrl, String user, String password, String schema) {
        final SimpleConnection connection = ConnectionFactory.create().create(jdbcUrl, user, password);
        return new DbService(connection, schema);
    }

    public void createSchema() {
        connection.executeStatement("drop schema if exists \"" + schema + "\" cascade");
        connection.executeStatement("create schema \"" + schema + "\"");
        connection.executeStatement("open schema \"" + schema + "\"");
        connection.executeScript(readSchemaScript("/exasol-schema.sql"));
    }

    private String readSchemaScript(String resource) {
        try (InputStream stream = getClass().getResourceAsStream(resource)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void load(FritzBoxReportCollection reportCollection) {
        dao.insertReportMails(reportCollection.getReports());
        dao.insertDataVolume(reportCollection.getDataVolumeByDay());
        dao.insertLogEntries(reportCollection.getLogEntries());
        dao.insertWifiLogEntries(reportCollection.getWifiLogEntries());
    }

    public void writeWifiDeviceDetailsCsv(Path outputPath) {
        if (Files.exists(outputPath)) {
            throw new IllegalStateException("Output file " + outputPath + " already exists");
        }
        try (CsvWriter writer = CsvWriter.builder().build(outputPath, StandardCharsets.UTF_8);
                Stream<DeviceDetails> deviceDetails = dao.getDeviceDetails()) {
            writer.writeRow(DeviceDetails.csvHeader());
            deviceDetails.map(DeviceDetails::toCsv).forEach(writer::writeRow);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error writing file " + outputPath, e);
        }
    }

    public void loadWifiDeviceDetailsCsv(Path inputPath) {
        try (NamedCsvReader reader = NamedCsvReader.builder().build(inputPath, StandardCharsets.UTF_8)) {
            final List<DeviceDetails> deviceDetails = reader.stream().map(DeviceDetails::fromCsv)
                    .filter(d -> d.type() != null && !d.type().isEmpty() && !d.readableName().isEmpty()).toList();
            checkForDuplicateKeys(deviceDetails, inputPath);
            dao.insertDeviceDetails(deviceDetails.stream());
        } catch (final IOException e) {
            throw new UncheckedIOException("Error reading from " + inputPath, e);
        }
    }

    private void checkForDuplicateKeys(final List<DeviceDetails> deviceDetails, Path inputPath) {
        final Map<String, Long> primaryKeyFrequencies = deviceDetails.stream()
                .map(d -> d.deviceName() + "," + d.macAddress()).collect(groupingBy(Function.identity(), counting()));
        final List<String> duplicateKeys = new ArrayList<>();
        for (final Entry<String, Long> entry : primaryKeyFrequencies.entrySet()) {
            if (entry.getValue().longValue() > 1) {
                duplicateKeys.add(entry.getKey());
            }
        }
        if (!duplicateKeys.isEmpty()) {
            throw new IllegalArgumentException(
                    "Found entries with duplicate primary keys " + duplicateKeys + " in file " + inputPath);
        }
    }
}
