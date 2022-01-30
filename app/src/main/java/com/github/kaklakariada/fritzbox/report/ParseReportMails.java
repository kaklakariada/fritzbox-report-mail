package com.github.kaklakariada.fritzbox.report;

import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseReportMails {
    private static final Logger LOG = LoggerFactory.getLogger(ParseReportMails.class);

    public static void main(final String[] args) {
        final Config config = Config.readConfig();
        LOG.info("Parsing mails from {}...", config.getRawMailsPath());
        final Stream<EmailContent> mails = new KryoSerializerService<>(EmailContent.class)
                .deserializeStream(config.getRawMailsPath());

        final FritzBoxReportCollection reportCollection = new ReportService().parseMails(mails);
        new KryoSerializerService<>(
                FritzBoxReportCollection.class).serialize(config.getSerializedReportPath(), reportCollection);
    }
}
