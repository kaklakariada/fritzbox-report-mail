package com.github.kaklakariada.fritzbox.dbloader;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.identifier.Identifier;
import org.itsallcode.jdbc.identifier.QualifiedIdentifier;
import org.itsallcode.jdbc.identifier.SimpleIdentifier;

import com.github.kaklakariada.fritzbox.dbloader.model.DeviceDetails;
import com.github.kaklakariada.fritzbox.report.model.AggregatedVolume;
import com.github.kaklakariada.fritzbox.report.model.Event;
import com.github.kaklakariada.fritzbox.report.model.EventLogEntry;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;
import com.github.kaklakariada.fritzbox.report.model.WifiConnection;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnectedHard;

public class ExasolDao {
    private final SimpleConnection connection;
    private final String schema;

    public ExasolDao(SimpleConnection connection, String schema) {
        this.connection = connection;
        this.schema = schema;
    }

    public void insertReportMails(Stream<FritzBoxReportMail> reports) {
        connection.insert(table("REPORT_MAIL"),
                columns("ID", "DATE", "TIMESTAMP", "MESSAGE_ID", "SUBJECT", "PRODUCT_NAME", "FIRMWARE_VERSION",
                        "ENERGY_USAGE_PERCENT"),
                this::mapReportMail, reports);
    }

    private Object[] mapReportMail(FritzBoxReportMail mail) {
        return new Object[] { mail.getReportId(), mail.getDate(),
                mail.getEmailMetadata().getTimestamp(), mail.getEmailMetadata().getMessageId(),
                mail.getEmailMetadata().getSubject(), mail.getFritzBoxInfo().getProduct(),
                mail.getFritzBoxInfo().getFirmwareVersion(), mail.getFritzBoxInfo().getEnergyUsagePercent() };
    }

    public void insertDataVolume(Stream<AggregatedVolume> dataVolume) {
        connection.insert(table("DATA_VOLUME"),
                columns("REPORT_ID", "DATE", "UPLOAD_MB", "DOWNLOAD_MB", "TOTAL_MB"),
                this::mapDataVolume, dataVolume);
    }

    private Object[] mapDataVolume(AggregatedVolume volume) {
        return new Object[] { volume.getReportId(), volume.getDay(),
                volume.getSentVolume().getVolumeMb(), volume.getReveivedVolume().getVolumeMb(),
                volume.getTotalVolume().getVolumeMb() };
    }

    public void insertLogEntries(Stream<EventLogEntry> entries) {
        connection.insert(table("LOG_ENTRY"), columns("ID", "REPORT_ID", "TIMESTAMP", "MESSAGE", "EVENT"),
                this::mapLogEntry,
                entries);
    }

    private Object[] mapLogEntry(EventLogEntry entry) {
        return new Object[] { entry.getLogEntryId(), entry.getReportId(), entry.getTimestamp(),
                entry.getMessage(), entry.getEvent().map(Event::toString).orElse(null) };
    }

    public void insertWifiConnections(Stream<WifiConnection> entries) {
        connection.insert(table("WIFI_CONNECTION"),
                columns("DEVICE_NAME", "MAC_ADDRESS", "WIFI_TYPE", "SPEED", "BEGIN", "END"),
                this::mapWifiConnection, entries);
    }

    private Object[] mapWifiConnection(WifiConnection entry) {
        return new Object[] { entry.getDeviceName(), entry.getMacAddress(), entry.getWifiType().toString(),
                entry.getSpeed(), entry.getBegin(), entry.getEnd() };
    }

    public void insertWifiLogEntries(Stream<EventLogEntry> entries) {
        connection.insert(table("WIFI_EVENT"),
                columns("LOG_ENTRY_ID", "TIMESTAMP", "EVENT_TYPE", "WIFI_TYPE", "DEVICE_NAME", "SPEED", "MAC_ADDRESS",
                        "DISCONNECT_CODE"),
                this::mapWifiEvents, entries);
    }

    private Object[] mapWifiEvents(EventLogEntry entry) {
        final Event genericEvent = entry.getEvent().orElseThrow();
        if (genericEvent instanceof final WifiDeviceConnected event) {
            return new Object[] { entry.getLogEntryId(), entry.getTimestamp(), "connected",
                    event.getWifiType().toString(), event.getName(), event.getSpeed(),
                    event.getMacAddress(), null };
        } else if (genericEvent instanceof final WifiDeviceDisconnected event) {
            String disconnectCode = null;
            if (event instanceof final WifiDeviceDisconnectedHard hardDisconnect) {
                disconnectCode = hardDisconnect.getCode();
            }
            return new Object[] { entry.getLogEntryId(), entry.getTimestamp(), "disconnected",
                    event.getWifiType().toString(), event.getName(), null,
                    event.getMacAddress(), disconnectCode };
        } else {
            throw new IllegalStateException(
                    "Unsupported event type " + entry.getEvent().get().getClass().getName());
        }
    }

    public Stream<DeviceDetails> getDeviceDetails() {
        return connection.query("select w.device_name, w.mac_address, d.readable_name, d.type, d.owner "
                + "from " + table("WIFI_CONNECTION")
                + " w full outer join " + table("WIFI_DEVICE_DETAILS")
                + " d ON w.mac_address = d.mac_address AND w.device_name = d.device_name "
                + "group by w.device_name, w.mac_address, d.readable_name, d.type, d.owner "
                + "order by w.mac_address, w.device_name",
                (rs, rowNum) -> new DeviceDetails(rs.getString("DEVICE_NAME"), rs.getString("MAC_ADDRESS"),
                        rs.getString("READABLE_NAME"), rs.getString("TYPE"), rs.getString("OWNER")))
                .stream();
    }

    public void insertDeviceDetails(Stream<DeviceDetails> deviceDetails) {
        connection.insert(table("WIFI_DEVICE_DETAILS"),
                columns("DEVICE_NAME", "MAC_ADDRESS", "READABLE_NAME", "TYPE", "OWNER"),
                device -> new Object[] { device.deviceName(), device.macAddress(), device.readableName(), device.type(),
                        device.owner() },
                deviceDetails);
    }

    private List<Identifier> columns(String... columns) {
        return Arrays.stream(columns).map(this::id).collect(toList());
    }

    private Identifier id(String id) {
        return new SimpleIdentifier(id);
    }

    private Identifier table(String name) {
        return new QualifiedIdentifier(id(schema), id(name));
    }
}
