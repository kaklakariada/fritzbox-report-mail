package com.github.kaklakariada.fritzbox.report;

import java.util.logging.Logger;

import com.github.kaklakariada.fritzbox.dbloader.DbService;

public class CreateWifiDeviceDetailsTemplate {
    private static final Logger LOG = Logger.getLogger(CreateWifiDeviceDetailsTemplate.class.getName());

    public static void main(String[] args) {
        final Config config = Config.readConfig();
        final DbService dbService = DbService.connect(config.getJdbcUrl(), config.getJdbcUser(),
                config.getJdbcPassword(), config.getJdbcSchema());
        LOG.fine(() -> "Writing template to " + config.getWifiDeviceDetailsCsv());
        dbService.writeWifiDeviceDetailsCsv(config.getWifiDeviceDetailsCsv());
    }
}
