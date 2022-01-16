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
package com.github.kaklakariada.fritzbox.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	private static final Logger LOG = LoggerFactory.getLogger(Config.class);

	private final Properties properties;

	private Config(Properties properties) {
		this.properties = properties;
	}

	public Path getMboxPath() {
		return Paths.get(properties.getProperty("mbox.path"));
	}

	public String getJdbcUrl() {
		return properties.getProperty("jdbc.url");
	}

	public String getJdbcUser() {
		return properties.getProperty("jdbc.user");
	}

	public String getJdbcPassword() {
		return properties.getProperty("jdbc.password");
	}

	public String getJdbcSchemaName() {
		return properties.getProperty("jdbc.schema");
	}

	public static Config readConfig() {
		return readConfig(Paths.get("application.properties"));
	}

	public static Config readConfig(Path path) {
		final Properties properties = new Properties();
		final Path absolutePath = path.toAbsolutePath();
		LOG.debug("Reading config from file {}", absolutePath);
		try (InputStream in = Files.newInputStream(absolutePath)) {
			properties.load(in);
		} catch (final IOException e) {
			throw new UncheckedIOException("Error loading configuration from " + absolutePath, e);
		}
		return new Config(properties);
	}
}
