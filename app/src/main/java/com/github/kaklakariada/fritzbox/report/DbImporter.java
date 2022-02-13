package com.github.kaklakariada.fritzbox.report;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.dbloader.DbService;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class DbImporter {
    private static final Logger LOG = LoggerFactory.getLogger(DbImporter.class);

    public static void main(String[] args) {
        final Config config = Config.readConfig();
        final FritzBoxReportCollection reportCollection = new KryoSerializerService<>(
                FritzBoxReportCollection.class).deserialize(config.getSerializedReportPath());
        final DbService dbService = DbService.connect(config.getJdbcUrl(), config.getJdbcUser(),
                config.getJdbcPassword(), config.getJdbcSchema());
        dbService.createSchema();

        final Path wifiDeviceDetailsCsv = config.getWifiDeviceDetailsCsv();
        if (Files.exists(wifiDeviceDetailsCsv)) {
            dbService.loadWifiDeviceDetailsCsv(wifiDeviceDetailsCsv);
        } else {
            LOG.warn("Wifi device details CSV not found at {}", wifiDeviceDetailsCsv);
        }

        LOG.debug("Importing into new schema...");
        final Instant start = Instant.now();
        dbService.load(reportCollection);
        LOG.debug("Import finished in {}", Duration.between(start, Instant.now()));
    }
}
