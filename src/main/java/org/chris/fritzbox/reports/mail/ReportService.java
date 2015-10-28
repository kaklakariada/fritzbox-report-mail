package org.chris.fritzbox.reports.mail;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.chris.fritzbox.reports.mail.convert.FritzBoxMessageConverter;
import org.chris.fritzbox.reports.mail.convert.HtmlMailParser;
import org.chris.fritzbox.reports.mail.convert.MessageHtmlTextBodyConverter;
import org.chris.fritzbox.reports.mail.model.FritzBoxReportCollection;
import org.chris.fritzbox.reports.mail.model.FritzBoxReportMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportService {

    final static Logger LOG = LoggerFactory.getLogger(ReportService.class);

    public FritzBoxReportCollection loadThunderbirdMails(final Path mboxFile) {
        final Instant start = Instant.now();
        final List<FritzBoxReportMail> reports = streamReports(mboxFile) //
                .sorted(Comparator.comparing(FritzBoxReportMail::getDate)) //
                .collect(Collectors.toList());
        final FritzBoxReportCollection reportCollection = new FritzBoxReportCollection(reports);
        final Duration duration = Duration.between(start, Instant.now());
        LOG.info("Loaded {} reports in {} from {}", reportCollection.getReportCount(), duration, mboxFile);
        return reportCollection;
    }

    public Stream<FritzBoxReportMail> streamReports(final Path mboxFile) {
        LOG.info("Loading reports from mbox file {}...", mboxFile);
        return new ThunderbirdMboxReader() //
                .readMbox(mboxFile) //
                .map(new MessageHtmlTextBodyConverter()) //
                .map(new HtmlMailParser()) //
                .map(new FritzBoxMessageConverter());
    }
}
