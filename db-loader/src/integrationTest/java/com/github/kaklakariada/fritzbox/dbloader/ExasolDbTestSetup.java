package com.github.kaklakariada.fritzbox.dbloader;

import org.itsallcode.jdbc.ConnectionFactory;
import org.itsallcode.jdbc.SimpleConnection;

import com.exasol.containers.ExasolContainer;
import com.exasol.containers.ExasolService;

public class ExasolDbTestSetup {
    private final ExasolContainer<? extends ExasolContainer<?>> container;
    private final ConnectionFactory connectionFactory;

    private ExasolDbTestSetup(final ExasolContainer<? extends ExasolContainer<?>> container) {
        this.container = container;
        connectionFactory = ConnectionFactory.create();
    }

    public static ExasolDbTestSetup startup() {
        @SuppressWarnings("resource") // Closed in stop() method
        final ExasolContainer<?> container = new ExasolContainer<>("8.23.1")
                .withRequiredServices(ExasolService.JDBC).withReuse(true);
        container.start();
        return new ExasolDbTestSetup(container);
    }

    public SimpleConnection createConnection() {
        return connectionFactory.create(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }

    public void stop() {
        container.stop();
    }

    public SimpleConnection truncateTables(final String schema) {
        final SimpleConnection connection = createConnection();
        new DbCleanupService(connection, schema).truncateTables();
        connection.executeStatement("open schema " + schema);
        return connection;
    }
}
