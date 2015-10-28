import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.chris.fritzbox.reports.mail.ReportService;
import org.chris.fritzbox.reports.mail.model.FritzBoxReportCollection;
import org.chris.serialization.KryoSerializerService;
import org.chris.serialization.SerializerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadThunderbirdReportMails {
    final static Logger logger = LoggerFactory.getLogger(ReadThunderbirdReportMails.class);

    private final static ReportService reportService = new ReportService();
    private static final SerializerService<FritzBoxReportCollection> serializer = new KryoSerializerService<>(
            FritzBoxReportCollection.class);
    private static final Path mboxFile = Paths.get(
            "/Users/chris/Library/Thunderbird/Profiles/h6dqt45l.default/ImapMail/imap.googlemail.com/FritzBox.sbd/Report");
    final static Path tempFile = Paths.get("/tmp/reports.ser");

    public static void main(final String[] args) throws FileNotFoundException, IOException {
        FritzBoxReportCollection reportCollection;
        // reportCollection = reportService.loadThunderbirdMails(mboxFile);
        // serializer.serialize(tempFile, reportCollection);
        reportCollection = serializer.deserialize(tempFile);

        reportCollection.getDataVolumeByDay().forEach(System.out::println);
        System.out.println("--------");
        reportCollection.getDataVolumeByMonth().forEach(System.out::println);
        System.out.println("--------");
        reportCollection.getDataVolumeByYear().forEach(System.out::println);
    }

}
