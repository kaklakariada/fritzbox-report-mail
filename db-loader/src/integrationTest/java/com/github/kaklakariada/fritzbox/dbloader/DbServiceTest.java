package com.github.kaklakariada.fritzbox.dbloader;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.itsallcode.jdbc.ConnectionFactory;
import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.resultset.Row;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.containers.ExasolContainer;
import com.exasol.containers.ExasolService;

@Testcontainers
class DbServiceTest {
    private static final String SCHEMA = "testing_schema";
    private static final String EXASOL_VERSION = "7.1.6";
    private static final ConnectionFactory CONNECTION_FACTORY = ConnectionFactory.create();

    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> CONTAINER = new ExasolContainer<>(EXASOL_VERSION)
            .withRequiredServices(ExasolService.JDBC).withReuse(true);

    private DbService dbService;
    private SimpleConnection connection;

    @BeforeEach
    void setup() {
        this.dbService = new DbService(createConnection(), SCHEMA);
        this.connection = createConnection();
    }

    private SimpleConnection createConnection() {
        return CONNECTION_FACTORY.create(CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword());
    }

    @Test
    void createSchema() {
        dbService.createSchema();
        List<Row> result = connection.query("SELECT schema_name FROM exa_user_schemas").toList();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getColumnValue(0).getValue()).isEqualTo(SCHEMA);
        assertThat(
                connection.query("SELECT count(*) FROM exa_user_tables").toList().get(0).getColumnValue(0).getValue())
                .as("table count").isEqualTo(5L);
        assertThat(connection.query("SELECT count(*) FROM exa_user_views").toList().get(0).getColumnValue(0).getValue())
                .as("view count").isEqualTo(3L);
    }
}
