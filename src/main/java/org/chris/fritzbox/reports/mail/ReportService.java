package org.chris.fritzbox.reports.mail;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.chris.fritzbox.reports.mail.convert.FritzBoxMessageConverter;
import org.chris.fritzbox.reports.mail.convert.HtmlMailParser;
import org.chris.fritzbox.reports.mail.convert.MessageHtmlTextBodyConverter;
import org.chris.fritzbox.reports.mail.model.FritzBoxReportCollection;
import org.chris.fritzbox.reports.mail.model.FritzBoxReportMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportService {
    final static Logger logger = LoggerFactory.getLogger(ReportService.class);

    public FritzBoxReportCollection loadThunderbirdMails(final Path mboxFile) {
        final Instant start = Instant.now();
        final List<FritzBoxReportMail> reports = new ThunderbirdMboxReader() //
                .readMbox(mboxFile) //
                .map(new MessageHtmlTextBodyConverter()) //
                .map(new HtmlMailParser()) //
                .map(new FritzBoxMessageConverter()) //
                .sorted(Comparator.comparing(FritzBoxReportMail::getDate)) //
                .collect(Collectors.toList());
        final FritzBoxReportCollection reportCollection = new FritzBoxReportCollection(reports);
        final Duration duration = Duration.between(start, Instant.now());
        logger.info("Loaded {} reports in {}", reportCollection.getReportCount(), duration);
        return reportCollection;
    }

}
