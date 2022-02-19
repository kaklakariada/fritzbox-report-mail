package com.github.kaklakariada.fritzbox.report;

import java.util.logging.Logger;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class ParseReportMails {
    private static final Logger LOG = Logger.getLogger(ParseReportMails.class.getName());

    public static void main(final String[] args) {
        final Config config = Config.readConfig();
        LOG.fine(() -> "Parsing mails from " + config.getRawMailsPath() + "...");
        final Stream<EmailContent> mails = new KryoSerializerService<>(EmailContent.class)
                .deserializeStream(config.getRawMailsPath());

        final FritzBoxReportCollection reportCollection = new ReportService().parseMails(mails);
        new KryoSerializerService<>(FritzBoxReportCollection.class).serialize(config.getSerializedReportPath(),
                reportCollection);
    }
}
