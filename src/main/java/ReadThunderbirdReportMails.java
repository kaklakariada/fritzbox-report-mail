import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;
import com.github.kaklakariada.serialization.SerializerService;

public class ReadThunderbirdReportMails {
    final static Logger LOG = LoggerFactory.getLogger(ReadThunderbirdReportMails.class);

    private final static ReportService reportService = new ReportService();
    private static final SerializerService<FritzBoxReportCollection> serializer = new KryoSerializerService<>(
            FritzBoxReportCollection.class);
    final static Path tempFile = Paths.get("/tmp/reports.ser");

    public static void main(final String[] args) throws FileNotFoundException, IOException {
        final Properties config = readConfig(Paths.get("application.properties"));
        final Path mboxFile = Paths.get(config.getProperty("mbox.path"));

        FritzBoxReportCollection reportCollection;
        reportCollection = reportService.loadThunderbirdMails(mboxFile);
        serializer.serialize(tempFile, reportCollection);
        reportCollection = serializer.deserialize(tempFile);

        reportCollection.getDataVolumeByDay() //
                .map(Object::toString) //
                .forEach(LOG::debug);

        reportCollection.getLogEntries() //
                .map(Object::toString) //
                .forEach(LOG::debug);
    }

    public static Properties readConfig(Path path) {
        final Properties config = new Properties();
        final Path absolutePath = path.toAbsolutePath();
        LOG.debug("Reading config from file {}", absolutePath);
        try (InputStream in = Files.newInputStream(absolutePath)) {
            config.load(in);
        } catch (final IOException e) {
            throw new RuntimeException("Error loading configuration from " + absolutePath, e);
        }
        return config;
    }
}
