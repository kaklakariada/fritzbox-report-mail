package com.github.kaklakariada.fritzbox.dbloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.itsallcode.jdbc.ConnectionFactory;
import org.itsallcode.jdbc.SimpleConnection;

import com.github.kaklakariada.fritzbox.report.model.Event;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;

public class DbSchema {

    private final SimpleConnection connection;

    public DbSchema(SimpleConnection connection) {
        this.connection = connection;
    }

    public static DbSchema connect(String jdbcUrl, String user, String password) {
        final Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);
        final SimpleConnection connection = ConnectionFactory.create().create(jdbcUrl, info);
        return new DbSchema(connection);
    }

    public void createSchema() {
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
        connection.insert(
                "insert into report_mail (id, \"DATE\", \"TIMESTAMP\", message_id, subject, product_name, firmware_version, energy_usage_percent) values (?, ?, ?, ?, ?, ?, ?, ?)",
                mail -> new Object[] { mail.getReportId(), mail.getDate(),
                        mail.getEmailMetadata().getTimestamp(), mail.getEmailMetadata().getMessageId(),
                        mail.getEmailMetadata().getSubject(), mail.getFritzBoxInfo().getProduct(),
                        mail.getFritzBoxInfo().getFirmwareVersion(), mail.getFritzBoxInfo().getEnergyUsagePercent() },
                reportCollection.getReports());

        connection.insert(
                "insert into DATA_VOLUME (report_id, \"DATE\", UPLOAD_MB, DOWNLOAD_MB, total_mb) VALUES (?, ?, ?, ?, ?)",
                volume -> new Object[] { volume.getReportId(), volume.getDay(),
                        volume.getSentVolume().getVolumeMb(), volume.getReveivedVolume().getVolumeMb(),
                        volume.getTotalVolume().getVolumeMb() },
                reportCollection.getDataVolumeByDay());

        connection.insert("insert into LOG_ENTRY (id, report_id, \"TIMESTAMP\", MESSAGE, EVENT) VALUES (?, ?, ?, ?, ?)",
                entry -> new Object[] { entry.getLogEntryId(), entry.getReportId(), entry.getTimestamp(),
                        entry.getMessage(), entry.getEvent().map(Event::toString).orElse(null) },
                reportCollection.getLogEntries());

        connection.insert(
                "insert into wifi_event (log_entry_id, \"TIMESTAMP\", event_type, wifi_type, device_name, speed, mac_address) values (?,?,?,?,?,?,?)",
                entry -> {
                    if (entry.getEvent().get() instanceof final WifiDeviceConnected event) {
                        return new Object[] { entry.getLogEntryId(), entry.getTimestamp(), "connected",
                                event.getWifiType().toString(), event.getName(), event.getSpeed(),
                                event.getMacAddress() };
                    } else if (entry.getEvent().get() instanceof final WifiDeviceDisconnected event) {
                        return new Object[] { entry.getLogEntryId(), entry.getTimestamp(), "disconnected",
                                event.getWifiType().toString(), event.getName(), null,
                                event.getMacAddress() };
                    } else {
                        throw new IllegalStateException(
                                "Unsupported event type " + entry.getEvent().get().getClass().getName());
                    }
                },
                reportCollection.getLogEntries().filter(e -> e.getEvent().isPresent())
                        .filter(e -> (e.getEvent().get() instanceof WifiDeviceConnected
                                || e.getEvent().get() instanceof WifiDeviceDisconnected)));
    }

}
