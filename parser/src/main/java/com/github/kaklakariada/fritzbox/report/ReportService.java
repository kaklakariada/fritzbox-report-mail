/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kaklakariada.fritzbox.report;

import static java.util.stream.Collectors.toMap;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.fritzbox.report.convert.FritzBoxMessageConverter;
import com.github.kaklakariada.fritzbox.report.convert.MessageHtmlTextBodyConverter;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;

public class ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportService.class);

    public Stream<EmailContent> loadRawThunderbirdMails(final Path mboxFile) {
        return new ThunderbirdMboxReader()
                .readMbox(mboxFile)
                .map(new MessageHtmlTextBodyConverter());
    }

    public FritzBoxReportCollection parseMails(final Stream<EmailContent> mails) {
        final Instant start = Instant.now();
        final AtomicInteger counter = new AtomicInteger(0);
        final Map<LocalDate, FritzBoxReportMail> reports = mails.peek(mail -> counter.incrementAndGet())
                .map(new FritzBoxMessageConverter())
                .collect(toMap(FritzBoxReportMail::getDate, Function.identity(), this::merge));
        final FritzBoxReportCollection reportCollection = new FritzBoxReportCollection(reports);
        final Duration duration = Duration.between(start, Instant.now());
        LOG.info("Found {} reports in {} from {} mails", reportCollection.getReportCount(), duration,
                counter.get());
        return reportCollection;
    }

    private FritzBoxReportMail merge(FritzBoxReportMail a, FritzBoxReportMail b) {
        LOG.warn("Found two mails for {} with same date:\n\t{}\n\t{}", a.getDate(),
                a.getEmailMetadata(), b.getEmailMetadata());
        if (a.getEmailMetadata().getTimestamp().isAfter(b.getEmailMetadata().getTimestamp())) {
            return a;
        } else {
            return b;
        }
    }

    public Stream<FritzBoxReportMail> streamReports(final Path mboxFile) {
        LOG.info("Loading reports from mbox file {}...", mboxFile);
        return new ThunderbirdMboxReader()
                .readMbox(mboxFile)
                .map(new MessageHtmlTextBodyConverter())
                .map(new FritzBoxMessageConverter());
    }
}
