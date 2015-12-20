import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.model.DataConnections;
import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportMail;

public class ImportReportsToInfluxDb {
    private static final Logger LOG = LoggerFactory.getLogger(ImportReportsToInfluxDb.class);

    private static InfluxDB influxDB;
    private static final String dbName = "fritzbox_reports";

    public static void main(String[] args) {
        final Properties config = ReadThunderbirdReportMails.readConfig(Paths.get("application.properties"));
        final Path mboxFile = Paths.get(config.getProperty("mbox.path"));

        influxDB = InfluxDBFactory.connect(config.getProperty("influxdb.url"), "root", "root");
        if (influxDB.describeDatabases().contains(dbName)) {
            LOG.info("Deleting db {}", dbName);
            influxDB.deleteDatabase(dbName);
        }
        influxDB.createDatabase(dbName);

        new ReportService().streamReports(mboxFile).forEach(ImportReportsToInfluxDb::importReport);
    }

    private static void importReport(FritzBoxReportMail mail) {
        final BatchPoints batchPoints = BatchPoints.database(dbName) //
                // .tag("async", "true") //
                .retentionPolicy("default") //
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
                .field("volume_total", dataConnections.getTotalVolume().getVolumeKb())
                .field("volume_received", dataConnections.getReveivedVolume().getVolumeKb())
                .field("volume_sent", dataConnections.getSentVolume().getVolumeKb())
                .field("num_connections", dataConnections.getNumberOfConnections())
                .field("online_time_min", dataConnections.getOnlineTime().toMinutes()) //
                .build();
        return dataConnectionPoint;
    }
}
