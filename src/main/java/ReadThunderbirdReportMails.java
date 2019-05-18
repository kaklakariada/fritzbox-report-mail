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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.Config;
import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.model.AggregatedVolume;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;
import com.github.kaklakariada.serialization.SerializerService;

public class ReadThunderbirdReportMails {
    static final Logger LOG = LoggerFactory.getLogger(ReadThunderbirdReportMails.class);

    private static final ReportService reportService = new ReportService();
    private static final SerializerService<FritzBoxReportCollection> serializer = new KryoSerializerService<>(
            FritzBoxReportCollection.class);

    public static void main(final String[] args) throws FileNotFoundException, IOException {
        final Path tempFile = Files.createTempFile("reports", ".ser");

        final Config config = Config.readConfig();
        FritzBoxReportCollection reportCollection;
        reportCollection = reportService.loadThunderbirdMails(config.getMboxPath());
        serializer.serialize(tempFile, reportCollection);
        reportCollection = serializer.deserialize(tempFile);

        reportCollection.getDataVolumeByDay() //
                .sorted(Comparator.comparing(AggregatedVolume::getTotalVolume).reversed()) //
                .limit(10) //
                .map(Object::toString) //
                .forEach(LOG::debug);

        // reportCollection.getLogEntries() //
        // .filter(e -> e.getEvent() != null && (e.getEvent() instanceof DslSyncFailed
        // || e.getEvent() instanceof InternetDisconnected || e.getEvent() instanceof DslSyncSuccessful)) //
        // .map(Object::toString) //
        // .forEach(LOG::debug);
    }

}
