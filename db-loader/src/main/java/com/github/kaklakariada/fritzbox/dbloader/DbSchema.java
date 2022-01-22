package com.github.kaklakariada.fritzbox.dbloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.itsallcode.jdbc.ConnectionFactory;
import org.itsallcode.jdbc.SimpleConnection;

import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;

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
        connection.insert("insert into report_mail (\"DATE\", \"TIMESTAMP\", message_id, subject) values (?, ?, ?, ?)",
                mail -> new Object[] { mail.getDate(),
                        mail.getEmailMetadata().getTimestamp(), mail.getEmailMetadata().getMessageId(),
                        mail.getEmailMetadata().getSubject() },
                reportCollection.getReports());

        connection.insert("insert into DATA_VOLUME (\"DATE\", UPLOAD_MB, DOWNLOAD_MB) VALUES (?, ?, ?)",
                volume -> new Object[] { volume.getDay(),
                        volume.getSentVolume().getVolumeMb(), volume.getReveivedVolume().getVolumeMb() },
                reportCollection.getDataVolumeByDay());

        connection.insert("insert into LOG_ENTRY (\"TIMESTAMP\", MESSAGE, EVENT) VALUES (?, ?, ?, ?)",
                entry -> new Object[] { entry.getTimestamp(), entry.getMessage(),
                        entry.getEvent() != null ? entry.getEvent().toString() : null },
                reportCollection.getLogEntries());
    }
}
