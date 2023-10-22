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

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.nio.file.Path;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.report.convert.*;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;

public class ReportService {

    private static final Logger LOG = Logger.getLogger(ReportService.class.getName());

    public Stream<EmailContent> loadRawThunderbirdMails(final Path mboxFile) {
        return new ThunderbirdMboxReader().readMbox(mboxFile).map(new MessageHtmlTextBodyConverter());
    }

    public FritzBoxReportCollection parseMails(final Stream<EmailContent> mails) {
        final Instant start = Instant.now();
        final List<FritzBoxReportMail> reports = mails.map(new FritzBoxMessageConverter())
                .collect(groupingBy(FritzBoxReportMail::getDate))
                .entrySet().stream()
                .flatMap(this::removeDuplicates).toList();
        final FritzBoxReportCollection reportCollection = new FritzBoxReportCollection(reports);
        final Duration duration = Duration.between(start, Instant.now());
        LOG.fine(() -> "Found " + reportCollection.getReportCount() + " reports in " + duration);
        final long logEntries = reportCollection.getLogEntries().count();
        final long logEntriesWithEvent = reportCollection.getLogEntries().filter(e -> e.getEvent().isPresent()).count();
        LOG.fine(() -> "Found " + logEntriesWithEvent + " of " + logEntries + " ("
                + (100 * logEntriesWithEvent / logEntries) + "%) log entries with events");
        return reportCollection;
    }

    private Stream<FritzBoxReportMail> removeDuplicates(final Entry<LocalDate, List<FritzBoxReportMail>> mails) {
        final Map<Object, List<FritzBoxReportMail>> mailsByProductName = mails.getValue().stream()
                .collect(groupingBy(m -> m.getFritzBoxInfo().getProduct()));
        return mailsByProductName.values().stream().map(this::findLatest);
    }

    private FritzBoxReportMail findLatest(final List<FritzBoxReportMail> mails) {
        return mails.stream()
                .sorted(comparing((final FritzBoxReportMail m) -> m.getEmailMetadata().getTimestamp()).reversed())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Empty list"));
    }

    public Stream<FritzBoxReportMail> streamReports(final Path mboxFile) {
        LOG.fine(() -> "Loading reports from mbox file " + mboxFile + "...");
        return new ThunderbirdMboxReader().readMbox(mboxFile).map(new MessageHtmlTextBodyConverter())
                .map(new FritzBoxMessageConverter());
    }
}
