package com.github.kaklakariada.fritzbox.report;

import java.time.Duration;
import java.time.Instant;

import com.github.kaklakariada.fritzbox.dbloader.DbSchema;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbImporter {
    private static final Logger LOG = LoggerFactory.getLogger(DbImporter.class);

    public static void main(String[] args) {
        final Config config = Config.readConfig();
        final FritzBoxReportCollection reportCollection = new KryoSerializerService<>(
                FritzBoxReportCollection.class).deserialize(config.getSerializedReportPath());
        final DbSchema schema = DbSchema.connect(config.getJdbcUrl(), config.getJdbcUser(), config.getJdbcPassword());
        schema.createSchema();
        LOG.debug("Importing into new schema...");
        final Instant start = Instant.now();
        schema.load(reportCollection);
        LOG.debug("Import finished in {}", Duration.between(start, Instant.now()));
    }
}
