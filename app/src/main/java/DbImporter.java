import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.kaklakariada.fritzbox.dbloader.DbSchema;
import com.github.kaklakariada.fritzbox.report.Config;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;
import com.github.kaklakariada.serialization.SerializerService;

public class DbImporter {
	public static void main(String[] args) {
		final Path tempFile = Paths.get("../data.kryo");

		final SerializerService<FritzBoxReportCollection> serializer = new KryoSerializerService<>(
				FritzBoxReportCollection.class);
		final Config config = Config.readConfig(Paths.get("../application.properties"));
		final FritzBoxReportCollection reportCollection = serializer.deserialize(tempFile);

		final DbSchema schema = DbSchema.connect(config.getJdbcUrl(), config.getJdbcUser(), config.getJdbcPassword());
		schema.createSchema();

		schema.load(reportCollection);
	}
}
