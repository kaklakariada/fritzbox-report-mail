import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.convert.EmailContent;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class ReadMails {
    public static void main(String[] args) {
        final Config config = Config.readConfig();
        final Stream<EmailContent> mailCollection = new ReportService().loadRawThunderbirdMails(config.getMboxPath());
        new KryoSerializerService<>(
                EmailContent.class).serializeStream(config.getRawMailsPath(), mailCollection);
    }
}
