package com.github.kaklakariada.fritzbox.report.convert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.model.DataConnections;
import com.github.kaklakariada.fritzbox.report.model.EventLogEntry;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;
import com.github.kaklakariada.fritzbox.report.model.InternetConnection;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.html.HtmlElement;

public class FritzBoxMessageConverter implements Function<HtmlElement, FritzBoxReportMail> {
    final static Logger logger = LoggerFactory.getLogger(FritzBoxMessageConverter.class);

    @Override
    public FritzBoxReportMail apply(final HtmlElement mail) {
        final DataExtractor extractor = new DataExtractor(mail);
        final Map<TimePeriod, DataConnections> dataConnections = extractor.getDataConnections();
        final DataConnections connectionsYesterday = dataConnections.get(TimePeriod.YESTERDAY);
        final List<EventLogEntry> eventLog = extractor.getEventLog();
        final List<InternetConnection> connections = eventLog.stream().flatMap(this::convertInternetConnection)
                .collect(Collectors.toList());
        logger.debug("{}: received {}, sent {}, log entries: {}, internet connections: {}", extractor.getDate(),
                connectionsYesterday.getReveivedVolume(), connectionsYesterday.getSentVolume(), eventLog.size(),
                connections.size());
        return new FritzBoxReportMail(extractor.getDate(), dataConnections, eventLog, connections);
    }

    private Stream<InternetConnection> convertInternetConnection(final EventLogEntry entry) {
        final String ipPattern = "([\\d\\.]+)";
        final Pattern p = Pattern.compile(".*IP-Adresse: " + ipPattern + ", DNS-Server: " + ipPattern + " und "
                + ipPattern + ", Gateway: " + ipPattern + ", Breitband-PoP: ([\\w-]+)");
        final Matcher matcher = p.matcher(entry.getMessage());
        if (!matcher.matches()) {
            return Stream.empty();
        }
        final String ipAddress = matcher.group(1);
        final String dnsServer1 = matcher.group(2);
        final String dnsServer2 = matcher.group(3);
        final String gateway = matcher.group(4);
        final String pop = matcher.group(5);
        final InternetConnection connection = new InternetConnection(entry.getTimestamp(), ipAddress, dnsServer1,
                dnsServer2, gateway, pop);
        return Stream.of(connection);
    }

}
