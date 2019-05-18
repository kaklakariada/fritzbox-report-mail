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

/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2015 Christoph Pirkl <christoph at users.sourceforge.net>
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
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.Config;
import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.model.DataConnections;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;

public class ImportReportsToInfluxDb {
    private static final Logger LOG = LoggerFactory.getLogger(ImportReportsToInfluxDb.class);

    private static InfluxDB influxDB;
    private static final String dbName = "fritzbox_reports";

    public static void main(String[] args) {
        final Config config = Config.readConfig();
        final Path mboxFile = config.getMboxPath();

        influxDB = InfluxDBFactory.connect(config.getInfluxdbUrl(), "root", "root");
        if (influxDB.describeDatabases().contains(dbName)) {
            LOG.info("Deleting db {}", dbName);
            influxDB.deleteDatabase(dbName);
        }
        influxDB.createDatabase(dbName);

        new ReportService().streamReports(mboxFile).forEach(ImportReportsToInfluxDb::importReport);
    }

    private static void importReport(FritzBoxReportMail mail) {
        final BatchPoints batchPoints = BatchPoints.database(dbName) //
                .consistency(ConsistencyLevel.ALL) //
                .build();
        batchPoints.point(getDataConnectionPointYesterday(mail));
        influxDB.write(batchPoints);
    }

    private static Point getDataConnectionPointYesterday(FritzBoxReportMail mail) {
        final DataConnections dataConnections = mail.getDataConnections().get(TimePeriod.YESTERDAY);
        final LocalDate dataConnectionDate = mail.getDate();
        final Point dataConnectionPoint = Point.measurement("data_connections_day") //
                .time(dataConnectionDate.toEpochDay(), TimeUnit.DAYS) //
                .addField("volume_total_kb", dataConnections.getTotalVolume().getVolumeKb())
                .addField("volume_received_kb", dataConnections.getReveivedVolume().getVolumeKb())
                .addField("volume_sent_kb", dataConnections.getSentVolume().getVolumeKb())
                .addField("num_connections", dataConnections.getNumberOfConnections())
                .addField("online_time_min", dataConnections.getOnlineTime().toMinutes()) //
                .build();
        return dataConnectionPoint;
    }
}
