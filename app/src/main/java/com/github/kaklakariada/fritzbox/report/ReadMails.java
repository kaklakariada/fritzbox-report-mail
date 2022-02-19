package com.github.kaklakariada.fritzbox.report;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class ReadMails {
    private static final Logger LOG = Logger.getLogger(ReadMails.class.getName());

    public static void main(String[] args) {
        final Config config = Config.readConfig();
        LOG.fine(() -> "Reading mails from " + config.getMboxPath() + "...");
        final Instant start = Instant.now();
        final Stream<EmailContent> mailCollection = new ReportService().loadRawThunderbirdMails(config.getMboxPath());
        final AtomicInteger counter = new AtomicInteger(0);
        new KryoSerializerService<>(EmailContent.class).serializeStream(config.getRawMailsPath(),
                mailCollection.peek(mail -> counter.incrementAndGet()));
        LOG.fine(() -> "Wrote " + counter + " mails to " + config.getRawMailsPath() + " in "
                + Duration.between(start, Instant.now()));
    }
}
