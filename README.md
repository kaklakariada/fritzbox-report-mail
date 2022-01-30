# fritzbox-report-mail

[![Java CI](https://github.com/kaklakariada/fritzbox-report-mail/actions/workflows/gradle.yml/badge.svg)](https://github.com/kaklakariada/fritzbox-report-mail/actions/workflows/gradle.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.kaklakariada%3Afritzbox-report-mail&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.kaklakariada%3Afritzbox-report-mail)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.github.kaklakariada%3Afritzbox-report-mail&metric=coverage)](https://sonarcloud.io/dashboard?id=com.github.kaklakariada%3Afritzbox-report-mail)

A Java API for parsing and processing status report mails of a FritzBox.

## Usage

### Initial Setup

* Configure FritzBox to send daily reports.
* Create a mail rule that puts all report mails into a separate folder.
* Setup your mail account in [Thunderbird](https://mozilla.org/thunderbird) (or any other email client that uses the same mbox format) and sync all mails in this folder.
* Start an Exasol instance

    ```shell
    mkdir $HOME/exadata
    docker run --publish 8563:8563 --detach --privileged --stop-timeout 120 --volume $HOME/exadata/:/exa exasol/docker-db:7.1.4
    ```

* Copy file `application.properties.template` to `application.properties`, add the path to your mbox file and your database connection.

### Importing Report Mails

To parse and import the reports into database schema `FRITZBOX` run the following command.

**This will drop the `FRITZBOX` schema if it exists!**

```shell
./gradlew runMailReader runMailParser runDbImport
```

After the import is finished, you can visualize your data e.g. with [Metabase](https://www.metabase.com/) and the [Exasol driver for Metabase](https://github.com/exasol/metabase-driver).

## Development

### Check if dependencies are up-to-date

```bash
./gradlew dependencyUpdates
```
