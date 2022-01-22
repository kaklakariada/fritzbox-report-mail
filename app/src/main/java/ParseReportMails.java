
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.ReportService;
import com.github.kaklakariada.fritzbox.report.model.FritzBoxReportCollection;
import com.github.kaklakariada.serialization.KryoSerializerService;

public class ParseReportMails {
    private static final Logger LOG = LoggerFactory.getLogger(ParseReportMails.class);

    public static void main(final String[] args) {
        final Config config = Config.readConfig(DbImporter.CONFIG_FILE);
        FritzBoxReportCollection reportCollection;
        reportCollection = new ReportService().loadThunderbirdMails(config.getMboxPath());
        new KryoSerializerService<>(
                FritzBoxReportCollection.class).serialize(DbImporter.SERIALIZED_DATA, reportCollection);
    }
}
