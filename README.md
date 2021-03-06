# fritzbox-report-mail

[![Java CI](https://github.com/kaklakariada/fritzbox-report-mail/actions/workflows/gradle.yml/badge.svg)](https://github.com/kaklakariada/fritzbox-report-mail/actions/workflows/gradle.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.kaklakariada%3Afritzbox-report-mail&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.kaklakariada%3Afritzbox-report-mail)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.github.kaklakariada%3Afritzbox-report-mail&metric=coverage)](https://sonarcloud.io/dashboard?id=com.github.kaklakariada%3Afritzbox-report-mail)

A Java API for parsing and processing status report mails of a FritzBox.

## Usage
* Configure FritzBox to send daily reports.
* Create a mail rule that puts all report mails into a folder.
* Download this folder with [Thunderbird](https://mozilla.org/thunderbird) or another email client that uses mbox format.
* Use class [`ReportService`](https://github.com/kaklakariada/fritzbox-report-mail/blob/master/src/main/java/com/github/kaklakariada/fritzbox/report/ReportService.java) to read the mbox file.

## Running the example program
* Copy file `application.properties.template` to `application.properties` and the path to your mbox file.
* Read mbox file using class [`ReadThunderbirdReportMails`](https://github.com/kaklakariada/fritzbox-report-mail/blob/master/src/main/java/ReadThunderbirdReportMails.java). This will parse the reports and aggregate data volume statistics.
