package com.github.kaklakariada.fritzbox.report.convert.extractor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.fritzbox.report.model.regex.Regex;
import com.github.kaklakariada.html.HtmlElement;

class ReportDateExtractor {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm");
    private static final Regex SUBJECT_DATE_PATTERN = Regex
            .create("^FRITZ!Box-Info: Nutzungs- und Verbindungsdaten vom (\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d)$", 1);
    private static final Regex REPEATER_SUBJECT_DATE_PATTERN = Regex
            .create("^FRITZ!Repeater-Info: Nutzungs- und Verbindungsdaten vom (\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d).*$", 1);

    LocalDate extractDate(final EmailContent mail, final HtmlElement rootElement) {
        final Optional<LocalDate> subjectDate = SUBJECT_DATE_PATTERN.matches(mail.getSubject())
                .or(() -> REPEATER_SUBJECT_DATE_PATTERN.matches(mail.getSubject()))
                .map(m -> m.getGroups().get(0))
                .map(this::parseDate);
        if (subjectDate.isPresent()) {
            return subjectDate.get();
        }

        final String oldDate = rootElement.getOptionalRegexpResult(
                "td:containsOwn(Ihre FRITZ!Box Verbindungsübersicht)",
                "Ihre FRITZ!Box Verbindungsübersicht vom ([\\d\\.: ]+) Uhr");
        if (oldDate != null) {
            final LocalDateTime dateTime = LocalDateTime.parse(oldDate, DATE_TIME_FORMAT);
            return dateTime.toLocalDate().minusDays(1);
        }
        final String newDate = rootElement.getOptionalRegexpResult(
                "td:containsOwn(Ihre tägliche FRITZ!Box Verbindungsübersicht vom)",
                "Ihre tägliche FRITZ!Box Verbindungsübersicht vom ([\\d\\.]+)(:? .*)?");
        if (newDate != null) {
            return parseDate(newDate);
        }

        final String guestAccountDate = rootElement.getOptionalRegexpResult(
                "td:containsOwn(Folgende WLAN-Gastzugangs-Ereignisse wurden von Ihrer FRITZ!Box am)",
                "Folgende WLAN-Gastzugangs-Ereignisse wurden von Ihrer FRITZ!Box am ([\\d\\.]+) erfasst.");
        if (guestAccountDate != null) {
            return parseDate(guestAccountDate);
        }
        throw new IllegalStateException(
                "No date found in email with subject '" + mail.getSubject() + "' and content " + rootElement);
    }

    private LocalDate parseDate(final String value) {
        return LocalDate.parse(value, DATE_FORMAT);
    }
}
