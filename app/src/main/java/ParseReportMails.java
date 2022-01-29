import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.convert.MailCollection;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class ParseReportMails {

    public static void main(final String[] args) {
        final Config config = Config.readConfig();
        System.out.println("Reading mails");
        final MailCollection mailCollection = new KryoSerializerService<>(MailCollection.class)
                .deserialize(config.getRawMailsPath());
        System.out.println("Found " + mailCollection.getMails().size() + " mails");
        final FritzBoxReportCollection reportCollection = new ReportService().parseMails(mailCollection);
        new KryoSerializerService<>(
                FritzBoxReportCollection.class).serialize(config.getSerializedReportPath(), reportCollection);
    }
}
