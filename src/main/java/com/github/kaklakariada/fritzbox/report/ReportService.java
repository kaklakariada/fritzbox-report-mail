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

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.convert.FritzBoxMessageConverter;
import com.github.kaklakariada.fritzbox.report.convert.HtmlMailParser;
import com.github.kaklakariada.fritzbox.report.convert.MessageHtmlTextBodyConverter;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;

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
