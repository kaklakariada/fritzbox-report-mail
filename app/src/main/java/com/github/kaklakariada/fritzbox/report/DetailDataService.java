package com.github.kaklakariada.fritzbox.report;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import com.github.kaklakariada.fritzbox.dbloader.DbService;

public class DetailDataService {
    private static final Logger LOG = Logger.getLogger(DetailDataService.class.getName());
    private final Config config;
    private final DbService dbService;

    public DetailDataService(final Config config, final DbService dbService) {
        this.config = config;
        this.dbService = dbService;
    }

    public void loadDetails() {
        LOG.info("Importing detail data from CSV files...");
        loadWifiDeviceDetails();
        loadFritzBoxDetails();
    }

    private void loadWifiDeviceDetails() {
        final Path wifiDeviceDetailsCsv = config.getWifiDeviceDetailsCsv();
        if (Files.exists(wifiDeviceDetailsCsv)) {
            dbService.loadWifiDeviceDetailsCsv(wifiDeviceDetailsCsv);
        } else {
            LOG.warning(() -> "Wifi device details CSV not found at " + wifiDeviceDetailsCsv);
        }
    }

    private void loadFritzBoxDetails() {
        final Path csvFile = config.getFritzBoxDetailsCsv();
        if (Files.exists(csvFile)) {
            dbService.loadFritzBoxDetailsCsv(csvFile);
        } else {
            LOG.warning(() -> "FritzBox details CSV not found at " + csvFile);
        }
    }
}
