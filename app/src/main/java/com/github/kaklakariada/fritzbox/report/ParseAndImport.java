package com.github.kaklakariada.fritzbox.report;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.dbloader.DbService;
import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;

public class ParseAndImport {
    private static final Logger LOG = Logger.getLogger(ParseAndImport.class.getName());

    public static void main(String[] args) {
        final Config config = Config.readConfig();
        LOG.fine(() -> "Reading mails from " + config.getMboxPath() + "...");
        final Instant start = Instant.now();
        final Stream<EmailContent> mails = new ReportService().loadRawThunderbirdMails(config.getMboxPath());
        final FritzBoxReportCollection reportCollection = new ReportService().parseMails(mails);
        LOG.fine(() -> "Parsed mails in " + Duration.between(start, Instant.now()));

        final DbService dbService = DbService.connect(config.getJdbcUrl(), config.getJdbcUser(),
                config.getJdbcPassword(), config.getJdbcSchema());
        dbService.createSchema();

        final Path wifiDeviceDetailsCsv = config.getWifiDeviceDetailsCsv();
        if (Files.exists(wifiDeviceDetailsCsv)) {
            dbService.loadWifiDeviceDetailsCsv(wifiDeviceDetailsCsv);
        } else {
            LOG.warning(() -> "Wifi device details CSV not found at " + wifiDeviceDetailsCsv);
        }

        LOG.fine("Importing into new schema...");
        final Instant dbImportStart = Instant.now();
        dbService.load(reportCollection);
        Instant end = Instant.now();
        LOG.fine(() -> "DB import finished in " + Duration.between(dbImportStart, end));
        LOG.fine(() -> "Total duration: " + Duration.between(start, end));
    }
}
