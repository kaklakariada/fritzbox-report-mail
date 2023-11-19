package com.github.kaklakariada.fritzbox.report;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.dbloader.DbService;
import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;

public class ParseAndImport {
    private static final Logger LOG = Logger.getLogger(ParseAndImport.class.getName());

    public static void main(final String[] args) {
        final Config config = Config.readConfig();
        final Instant start = Instant.now();
        final Stream<EmailContent> mails = new ReportService().loadRawThunderbirdMails(config.getMboxPath());
        final FritzBoxReportCollection reportCollection = new ReportService().parseMails(mails);
        final Duration parsing = Duration.between(start, Instant.now());

        final DbService dbService = DbService.connect(config.getJdbcUrl(), config.getJdbcUser(),
                config.getJdbcPassword(), config.getJdbcSchema());
        dbService.createSchema();

        new DetailDataService(config, dbService).loadDetails();

        LOG.fine("Importing into new schema...");
        final Instant dbImportStart = Instant.now();
        dbService.load(reportCollection);
        final Instant end = Instant.now();
        final Duration dbImport = Duration.between(dbImportStart, end);
        LOG.fine(() -> "Finished. Total duration: " + Duration.between(start, end) + ", parsing: " + parsing
                + ", DB import: " + dbImport);
    }
}
