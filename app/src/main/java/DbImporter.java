import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.dbloader.DbSchema;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class DbImporter {
    private static final Logger LOG = LoggerFactory.getLogger(DbImporter.class);
    static final Path SERIALIZED_DATA = Paths.get("../data.kryo");
    static final Path CONFIG_FILE = Paths.get("../application.properties");

    public static void main(String[] args) {
        final FritzBoxReportCollection reportCollection = new KryoSerializerService<>(
                FritzBoxReportCollection.class).deserialize(SERIALIZED_DATA);
        final Config config = Config.readConfig(CONFIG_FILE);
        final DbSchema schema = DbSchema.connect(config.getJdbcUrl(), config.getJdbcUser(), config.getJdbcPassword());
        schema.createSchema();
        LOG.debug("Importing into new schema...");
        final Instant start = Instant.now();
        schema.load(reportCollection);
        LOG.debug("Import finished in {}", Duration.between(start, Instant.now()));
    }
}
