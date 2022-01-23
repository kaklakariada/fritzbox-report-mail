import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class ParseReportMails {
    public static void main(final String[] args) {
        final Config config = Config.readConfig(DbImporter.CONFIG_FILE);
        FritzBoxReportCollection reportCollection;
        reportCollection = new ReportService().loadThunderbirdMails(config.getMboxPath());
        new KryoSerializerService<>(
                FritzBoxReportCollection.class).serialize(DbImporter.SERIALIZED_DATA, reportCollection);
    }
}
