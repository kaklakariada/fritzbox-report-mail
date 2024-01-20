package com.github.kaklakariada.fritzbox.dbloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

class DbServiceTest {
    private static final String SCHEMA = "TESTING_SCHEMA";

    private static ExasolDbTestSetup db;

    private DbService dbService;
    private SimpleConnection connection;

    @BeforeAll
    static void beforeAll() {
        db = ExasolDbTestSetup.startup();
    }

    @AfterAll
    static void afterAll() {
        db.stop();
    }

    @BeforeEach
    void setup() {
        this.dbService = new DbService(db.createConnection(), SCHEMA);
        this.connection = db.createConnection();
    }

    @Test
    void createSchema() {
        dbService.createSchema();
        final List<Row> result = connection.query("SELECT schema_name FROM exa_user_schemas").toList();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).get(0).value()).isEqualTo(SCHEMA);
        assertThat(
                connection.query("SELECT count(*) FROM exa_user_tables").toList().get(0).get(0).value())
                .as("table count").isEqualTo(6L);
        assertThat((Long) connection.query("SELECT count(*) FROM exa_user_views").toList().get(0).get(0)
                .value())
                .as("view count").isGreaterThan(7L);
    }

    @Test
    void loadFritzBoxDetailsCsvFails() {
        final Path missingFile = Path.of("missing-file");
        assertThatThrownBy(() -> dbService.loadFritzBoxDetailsCsv(missingFile))
                .isInstanceOf(UncheckedIOException.class).hasMessage("Error reading from missing-file: missing-file");
    }

    @Test
    void loadFritzBoxDetailsCsv(@TempDir final Path tempDir) throws IOException {
        final Path file = tempDir.resolve("file");
        Files.writeString(file, "PRODUCT_NAME,READABLE_NAME\nfritzbox-name,readable-name");
        dbService.createSchema();
        dbService.loadFritzBoxDetailsCsv(file);
        final List<Row> result = connection.query("select count(*) from " + SCHEMA + ".FRITZBOX_DETAILS").toList();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).get(0).value()).isEqualTo(1L);
    }

    @Test
    void loadWifiDeviceDetailsCsvFails() {
        final Path missingFile = Path.of("missing-file");
        assertThatThrownBy(() -> dbService.loadWifiDeviceDetailsCsv(missingFile))
                .isInstanceOf(UncheckedIOException.class).hasMessage("Error reading from missing-file: missing-file");
    }

    @Test
    void loadWifiDeviceDetailsCsv(@TempDir final Path tempDir) throws IOException {
        final Path file = tempDir.resolve("file");
        Files.writeString(file, "DEVICE_NAME,MAC_ADDRESS,READABLE_NAME,TYPE,OWNER\n" + //
                "device,mac,name,type,owner");
        dbService.createSchema();
        dbService.loadWifiDeviceDetailsCsv(file);
        final List<Row> result = connection.query("select count(*) from " + SCHEMA + ".WIFI_DEVICE_DETAILS").toList();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).get(0).value()).isEqualTo(1L);
    }
}
