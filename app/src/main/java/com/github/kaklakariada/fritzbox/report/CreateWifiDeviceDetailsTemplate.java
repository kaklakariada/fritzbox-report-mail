package com.github.kaklakariada.fritzbox.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.dbloader.DbService;

public class CreateWifiDeviceDetailsTemplate {
    private static final Logger LOG = LoggerFactory.getLogger(CreateWifiDeviceDetailsTemplate.class);

    public static void main(String[] args) {
        final Config config = Config.readConfig();
        final DbService dbService = DbService.connect(config.getJdbcUrl(), config.getJdbcUser(),
                config.getJdbcPassword(), config.getJdbcSchema());
        LOG.info("Writing template to {}", config.getWifiDeviceDetailsCsv());
        dbService.writeWifiDeviceDetailsCsv(config.getWifiDeviceDetailsCsv());
    }
}
