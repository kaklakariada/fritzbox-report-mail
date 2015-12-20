# fritzbox-report-mail

[![Build Status](https://travis-ci.org/kaklakariada/fritzbox-report-mail.svg?branch=master)](https://travis-ci.org/kaklakariada/fritzbox-report-mail)

A Java API for parsing and processing status report mails of a FritzBox.

## Usage
* Configure FritzBox to send daily reports.
* Create a mail rule that puts all report mails into a folder.
* Download this folder with [Thunderbird](https://mozilla.org/thunderbird) or another email client that uses mbox format.
* Use class [`ReportService`](https://github.com/kaklakariada/fritzbox-report-mail/blob/master/src/main/java/com/github/kaklakariada/fritzbox/report/ReportService.java) to read the mbox file.

## Running the example program
* Copy file `application.properties.template` to `application.properties` and the path to your mbox file.
* Read mbox file using class [`ReadThunderbirdReportMails`](https://github.com/kaklakariada/fritzbox-report-mail/blob/master/src/main/java/ReadThunderbirdReportMails.java). This will parse the reports and aggregate data volume statistics.
