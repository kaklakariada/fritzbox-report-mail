import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class ParseReportMails {
    private static final Logger LOG = LoggerFactory.getLogger(ParseReportMails.class);

    public static void main(final String[] args) {
        final Config config = Config.readConfig();
        LOG.info("Parsing mails from {}...", config.getRawMailsPath());
        final Stream<EmailContent> mails = new KryoSerializerService<>(EmailContent.class)
                .deserializeStream(config.getRawMailsPath());

        final FritzBoxReportCollection reportCollection = new ReportService().parseMails(mails);
        new KryoSerializerService<>(
                FritzBoxReportCollection.class).serialize(config.getSerializedReportPath(), reportCollection);
    }
}
