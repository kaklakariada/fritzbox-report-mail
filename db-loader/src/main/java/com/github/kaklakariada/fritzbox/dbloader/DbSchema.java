package com.github.kaklakariada.fritzbox.dbloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import org.itsallcode.jdbc.ConnectionFactory;
import org.itsallcode.jdbc.SimpleConnection;

import com.github.kaklakariada.fritzbox.report.model.AggregatedVolume;
import com.github.kaklakariada.fritzbox.report.model.Event;
import com.github.kaklakariada.fritzbox.report.model.EventLogEntry;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;
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

    private Object[] mapReportMail(FritzBoxReportMail mail) {
        return new Object[] { mail.getReportId(), mail.getDate(),
                mail.getEmailMetadata().getTimestamp(), mail.getEmailMetadata().getMessageId(),
                mail.getEmailMetadata().getSubject(), mail.getFritzBoxInfo().getProduct(),
                mail.getFritzBoxInfo().getFirmwareVersion(), mail.getFritzBoxInfo().getEnergyUsagePercent() };
    }

    private Object[] mapDataVolume(AggregatedVolume volume) {
        return new Object[] { volume.getReportId(), volume.getDay(),
                volume.getSentVolume().getVolumeMb(), volume.getReveivedVolume().getVolumeMb(),
                volume.getTotalVolume().getVolumeMb() };
    }

    private Object[] mapLogEntry(EventLogEntry entry) {
        return new Object[] { entry.getLogEntryId(), entry.getReportId(), entry.getTimestamp(),
                entry.getMessage(), entry.getEvent().map(Event::toString).orElse(null) };
    }

    private Object[] mapWifiConnection(EventLogEntry entry) {
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
    }

    public void load(FritzBoxReportCollection reportCollection) {
        connection.insert("REPORT_MAIL",
                List.of("ID", "DATE", "TIMESTAMP", "MESSAGE_ID", "SUBJECT", "PRODUCT_NAME", "FIRMWARE_VERSION",
                        "ENERGY_USAGE_PERCENT"),
                this::mapReportMail, reportCollection.getReports());

        connection.insert("DATA_VOLUME", List.of("REPORT_ID", "DATE", "UPLOAD_MB", "DOWNLOAD_MB", "TOTAL_MB"),
                this::mapDataVolume, reportCollection.getDataVolumeByDay());

        connection.insert("LOG_ENTRY", List.of("ID", "REPORT_ID", "TIMESTAMP", "MESSAGE", "EVENT"), this::mapLogEntry,
                reportCollection.getLogEntries());

        connection.insert(
                "insert into wifi_connection (device_name, mac_address, wifi_type, speed, \"BEGIN\", \"END\") values (?, ?, ?, ?, ?, ?)",
                (entry) -> new Object[] { entry.getDeviceName(), entry.getMacAddress(), entry.getWifiType().toString(),
                        entry.getSpeed(), entry.getBegin(), entry.getEnd() },
                reportCollection.getWifiConnections());

        connection.insert("WIFI_EVENT",
                List.of("LOG_ENTRY_ID", "TIMESTAMP", "EVENT_TYPE", "WIFI_TYPE", "DEVICE_NAME", "SPEED", "MAC_ADDRESS"),
                this::mapWifiConnection, reportCollection.getWifiLogEntries());
    }
}
