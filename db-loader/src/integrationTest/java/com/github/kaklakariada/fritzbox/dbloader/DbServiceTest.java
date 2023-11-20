package com.github.kaklakariada.fritzbox.dbloader;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.resultset.Row;
import org.junit.jupiter.api.*;

class DbServiceTest {
    private static final String SCHEMA = "testing_schema";

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
        assertThat(result.get(0).getColumnValue(0).getValue()).isEqualTo(SCHEMA);
        assertThat(
                connection.query("SELECT count(*) FROM exa_user_tables").toList().get(0).getColumnValue(0).getValue())
                .as("table count").isEqualTo(6L);
        assertThat((Long) connection.query("SELECT count(*) FROM exa_user_views").toList().get(0).getColumnValue(0)
                .getValue())
                .as("view count").isGreaterThan(7L);
    }
}
