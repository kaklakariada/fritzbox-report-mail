package com.github.kaklakariada.fritzbox.report;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.serialization.KryoSerializerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadMails {
    private static final Logger LOG = LoggerFactory.getLogger(ReadMails.class);

    public static void main(String[] args) {
        final Config config = Config.readConfig();
        LOG.info("Reading mails from {}...", config.getMboxPath());
        final Instant start = Instant.now();
        final Stream<EmailContent> mailCollection = new ReportService().loadRawThunderbirdMails(config.getMboxPath());
        final AtomicInteger counter = new AtomicInteger(0);
        new KryoSerializerService<>(
                EmailContent.class).serializeStream(config.getRawMailsPath(),
                        mailCollection.peek(mail -> counter.incrementAndGet()));
        LOG.info("Wrote {} mails to {} in {}", counter.get(), config.getRawMailsPath(),
                Duration.between(start, Instant.now()));

    }
}
