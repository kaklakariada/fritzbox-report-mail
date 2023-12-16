package com.github.kaklakariada.fritzbox.dbloader.views;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Collections;
import java.util.List;

import org.itsallcode.jdbc.ParamConverter;
import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.identifier.*;
import org.junit.jupiter.api.*;

import com.github.kaklakariada.fritzbox.dbloader.DbService;
import com.github.kaklakariada.fritzbox.dbloader.ExasolDbTestSetup;

class WifConnectionTest {
    private static final String SCHEMA = "VIEW_TEST";
    private static final Instant START = Instant.parse("2007-12-03T10:15:30.00Z");
    private static final String DEVICE1 = "device1";
    private static final String DEVICE2 = "device2";
    private static ExasolDbTestSetup db;

    private SimpleConnection connection;
    private int nextLogEntryId = 0;

    @BeforeAll
    static void beforeAll() {
        db = ExasolDbTestSetup.startup();
        new DbService(db.createConnection(), SCHEMA).createSchema();
    }

    @AfterAll
    static void afterAll() {
        db.stop();
    }

    @BeforeEach
    void setup() {
        connection = db.truncateTables(SCHEMA);
    }

    @Test
    void empty() {
        assertThat(wifiConnections()).isEmpty();
    }

    @Test
    void singleConnection() {
        insertEvents(connect(START, DEVICE1), disconnect(START.plusSeconds(10), DEVICE1));
        assertThat(wifiConnections())
                .containsExactly(new WifiConnection(DEVICE1, 10L, START, START.plusSeconds(10)));
    }

    @Test
    void twoConnection() {
        final Instant start1 = START;
        final Instant end1 = START.plusSeconds(10);
        final Instant start2 = end1.plusSeconds(30);
        final Instant end2 = start2.plusSeconds(15);
        insertEvents(connect(start1, DEVICE1), disconnect(end1, DEVICE1),
                connect(start2, DEVICE1), disconnect(end2, DEVICE1));
        assertThat(wifiConnections())
                .containsExactly(new WifiConnection(DEVICE1, 10L, start1, end1),
                        new WifiConnection(DEVICE1, 15L, start2, end2));
    }

    @Test
    void overlappingConnections() {
        final Instant start2 = START.plusSeconds(10);
        final Instant end1 = START.plusSeconds(20);
        final Instant end2 = START.plusSeconds(30);
        insertEvents(connect(START, DEVICE1), disconnect(end1, DEVICE1),
                connect(start2, DEVICE1), disconnect(end2, DEVICE1));
        assertThat(wifiConnections()).containsExactly(new WifiConnection(DEVICE1, 10L, start2, end1));
    }

    @Test
    void differentDevices() {
        insertEvents(connect(START, DEVICE1), disconnect(START.plusSeconds(10), DEVICE2));
        assertThat(wifiConnections()).isEmpty();
    }

    private WifiEvent connect(final Instant timestamp, final String macAddress) {
        return new WifiEvent(nextLogEntryId(), timestamp, WifiEventType.CONNECT, macAddress);
    }

    private WifiEvent disconnect(final Instant timestamp, final String macAddress) {
        return new WifiEvent(nextLogEntryId(), timestamp, WifiEventType.DISCONNECT, macAddress);
    }

    private void insertEvents(final WifiEvent... events) {
        insertEvents(asList(events));
    }

    private void insertEvents(final List<WifiEvent> events) {
        insertMail(1, LocalDate.ofInstant(START, ZoneId.systemDefault()), START);
        insertLogEntries(1, events);
        insertEventStream(events);
    }

    private void insertEventStream(final List<WifiEvent> events) {
        connection.insert(QualifiedIdentifier.of(id(SCHEMA), id("WIFI_EVENT")),
                List.of(id("LOG_ENTRY_ID"), id("TIMESTAMP"), id("EVENT_TYPE"), id("MAC_ADDRESS")),
                event -> new Object[] { event.eventId, event.timestamp, event.type.name, event.macAddress },
                events.stream());
    }

    private void insertLogEntries(final int reportId, final List<WifiEvent> events) {
        connection.insert(QualifiedIdentifier.of(id(SCHEMA), id("LOG_ENTRY")),
                List.of(id("ID"), id("REPORT_ID"), id("TIMESTAMP"), id("MESSAGE"), id("EVENT")),
                event -> new Object[] { event.eventId, reportId, event.timestamp, "msg", event.toString() },
                events.stream());
    }

    private void insertMail(final int id, final LocalDate date, final Instant timestamp) {
        connection.insert(QualifiedIdentifier.of(id(SCHEMA), id("REPORT_MAIL")),
                List.of(id("ID"), id("DATE"), id("TIMESTAMP"), id("MESSAGE_ID"), id("SUBJECT"), id("PRODUCT_NAME"),
                        id("FIRMWARE_VERSION"), id("ENERGY_USAGE_PERCENT")),
                ParamConverter.identity(), Collections.singletonList(new Object[] { id, date, timestamp,
                        "message-" + id, "subject-" + id, "product-" + id, "1.2.3", 10 }).stream());
    }

    private enum WifiEventType {
        CONNECT("connected"), DISCONNECT("disconnected");

        private final String name;

        private WifiEventType(final String name) {
            this.name = name;
        }
    }

    private record WifiEvent(int eventId, Instant timestamp, WifiEventType type, String macAddress) {
    }

    private int nextLogEntryId() {
        return nextLogEntryId++;
    }

    private Identifier id(final String id) {
        return SimpleIdentifier.of(id);
    }

    private List<WifiConnection> wifiConnections() {
        return connection.query(
                "select mac_address, duration_seconds, connect_timestamp, disconnect_timestamp from v_wifi_connection",
                this::mapWifiConnection).toList();
    }

    private WifiConnection mapWifiConnection(final ResultSet rs, final int rowNum) throws SQLException {
        return new WifiConnection(rs.getString(1),
                rs.getLong(2),
                rs.getObject(3, Instant.class),
                rs.getObject(4, Instant.class));
    }

    record WifiConnection(String macAddress, long durationSeconds, Instant connected, Instant disconnected) {
    }
}
