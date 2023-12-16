package com.github.kaklakariada.fritzbox.dbloader;

import org.itsallcode.jdbc.SimpleConnection;

class DbCleanupService {

    private final SimpleConnection connection;
    private final String schema;

    public DbCleanupService(final SimpleConnection connection, final String schema) {
        this.connection = connection;
        this.schema = schema;
    }

    public void truncateTables() {
        modifyForeignKeyConstraints(false);
        try {
            connection.query("SELECT TABLE_NAME FROM SYS.EXA_ALL_TABLES WHERE TABLE_SCHEMA = ?",
                    stmt -> stmt.setString(1, schema), (rs, row) -> rs.getString(1)).stream()
                    .forEach(table -> connection.executeStatement("truncate table " + schema + "." + table));
        } finally {
            modifyForeignKeyConstraints(true);
        }
    }

    private void modifyForeignKeyConstraints(final boolean enable) {
        connection.query(
                "SELECT CONSTRAINT_SCHEMA, CONSTRAINT_TABLE, CONSTRAINT_NAME FROM Sys.exa_dba_constraints WHERE constraint_type = 'FOREIGN KEY' AND CONSTRAINT_SCHEMA = ?",
                stmt -> stmt.setString(1, schema),
                (rs, row) -> new Constraint(rs.getString(1), rs.getString(2), rs.getString(3))).stream()
                .forEach(constraint -> connection.executeStatement("alter table " + constraint.schema() + "."
                        + constraint.table() + " modify constraint " + constraint.name() + " "
                        + (enable ? "ENABLE" : "DISABLE")));
    }

    private record Constraint(String schema, String table, String name) {

    }
}
