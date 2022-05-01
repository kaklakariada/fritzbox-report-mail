package com.github.kaklakariada.fritzbox.dbloader;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.dbloader.model.DeviceDetails;
import com.github.kaklakariada.fritzbox.report.model.AggregatedVolume;
import com.github.kaklakariada.fritzbox.report.model.Event;
import com.github.kaklakariada.fritzbox.report.model.EventLogEntry;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnectedHard;

import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.identifier.Identifier;
import org.itsallcode.jdbc.identifier.QualifiedIdentifier;
import org.itsallcode.jdbc.identifier.SimpleIdentifier;

public class ExasolDao {
    private static final String MAC_ADDRESS_COL = "MAC_ADDRESS";
    private static final String DEVICE_NAME_COL = "DEVICE_NAME";
    private static final String TIMESTAMP_COL = "TIMESTAMP";

    private final SimpleConnection connection;
    private final String schema;

    public ExasolDao(final SimpleConnection connection, final String schema) {
        this.connection = connection;
        this.schema = Objects.requireNonNull(schema, "schema");
    }

    public void insertReportMails(final Stream<FritzBoxReportMail> reports) {
        connection.insert(table("REPORT_MAIL"), columns("ID", "DATE", TIMESTAMP_COL, "MESSAGE_ID", "SUBJECT",
                "PRODUCT_NAME", "FIRMWARE_VERSION", "ENERGY_USAGE_PERCENT"), this::mapReportMail, reports);
    }

    private Object[] mapReportMail(final FritzBoxReportMail mail) {
        return new Object[] { mail.getReportId(), mail.getDate(), mail.getEmailMetadata().getTimestamp(),
                mail.getEmailMetadata().getMessageId(), mail.getEmailMetadata().getSubject(),
                mail.getFritzBoxInfo().getProduct(), mail.getFritzBoxInfo().getFirmwareVersion(),
                mail.getFritzBoxInfo().getEnergyUsagePercent() };
    }

    public void insertDataVolume(final Stream<AggregatedVolume> dataVolume) {
        connection.insert(table("DATA_VOLUME"), columns("REPORT_ID", "DATE", "UPLOAD_MB", "DOWNLOAD_MB", "TOTAL_MB"),
                this::mapDataVolume, dataVolume);
    }

    private Object[] mapDataVolume(final AggregatedVolume volume) {
        return new Object[] { volume.getReportId(), volume.getDay(), volume.getSentVolume().getVolumeMb(),
                volume.getReveivedVolume().getVolumeMb(), volume.getTotalVolume().getVolumeMb() };
    }

    public void insertLogEntries(final Stream<EventLogEntry> entries) {
        connection.insert(table("LOG_ENTRY"), columns("ID", "REPORT_ID", TIMESTAMP_COL, "MESSAGE", "EVENT"),
                this::mapLogEntry, entries);
    }

    private Object[] mapLogEntry(final EventLogEntry entry) {
        return new Object[] { entry.getLogEntryId(), entry.getReportId(), entry.getTimestamp(), entry.getMessage(),
                entry.getEvent().map(Event::toString).orElse(null) };
    }

    public void insertWifiLogEntries(final Stream<EventLogEntry> entries) {
        connection.insert(table("WIFI_EVENT"), columns("LOG_ENTRY_ID", TIMESTAMP_COL, "EVENT_TYPE", "WIFI_TYPE",
                DEVICE_NAME_COL, "SPEED", MAC_ADDRESS_COL, "DISCONNECT_CODE"), this::mapWifiEvents, entries);
    }

    private Object[] mapWifiEvents(final EventLogEntry entry) {
        final Event genericEvent = entry.getEvent().orElseThrow();
        if (genericEvent instanceof final WifiDeviceConnected event) {
            return new Object[] { entry.getLogEntryId(), entry.getTimestamp(), "connected",
                    event.getWifiType().toString(), event.getName(), event.getSpeed(), event.getMacAddress(), null };
        } else if (genericEvent instanceof final WifiDeviceDisconnected event) {
            String disconnectCode = null;
            if (event instanceof final WifiDeviceDisconnectedHard hardDisconnect) {
                disconnectCode = hardDisconnect.getCode();
            }
            return new Object[] { entry.getLogEntryId(), entry.getTimestamp(), "disconnected",
                    event.getWifiType().toString(), event.getName(), null, event.getMacAddress(), disconnectCode };
        } else {
            throw new IllegalStateException(
                    "Unsupported event type " + entry.getEvent().orElseThrow().getClass().getName());
        }
    }

    public Stream<DeviceDetails> getDeviceDetails() {
        return connection.query(
                "select w.device_name, w.mac_address, d.readable_name, d.type, d.owner " //
                        + "from " + table("WIFI_EVENT")
                        + " w full outer join " + table("WIFI_DEVICE_DETAILS")
                        + " d ON w.mac_address = d.mac_address AND w.device_name = d.device_name "
                        + "group by w.device_name, w.mac_address, d.readable_name, d.type, d.owner "
                        + "order by w.mac_address, w.device_name",
                (rs, rowNum) -> new DeviceDetails(rs.getString(DEVICE_NAME_COL), rs.getString(MAC_ADDRESS_COL),
                        rs.getString("READABLE_NAME"), rs.getString("TYPE"), rs.getString("OWNER")))
                .stream();
    }

    public void insertDeviceDetails(final Stream<DeviceDetails> deviceDetails) {
        connection.insert(table("WIFI_DEVICE_DETAILS"),
                columns(DEVICE_NAME_COL, MAC_ADDRESS_COL, "READABLE_NAME", "TYPE", "OWNER"),
                device -> new Object[] { device.deviceName(), device.macAddress(), device.readableName(), device.type(),
                        device.owner() },
                deviceDetails);
    }

    private List<Identifier> columns(final String... columns) {
        return Arrays.stream(columns).map(this::id).toList();
    }

    private Identifier id(final String id) {
        return SimpleIdentifier.of(id);
    }

    private Identifier table(final String name) {
        return QualifiedIdentifier.of(id(schema), id(name));
    }
}
