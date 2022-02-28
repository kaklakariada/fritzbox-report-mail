package com.github.kaklakariada.fritzbox.report;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
    private static final Logger LOG = Logger.getLogger(Config.class.getName());
    private static final Path DEFAULT_CONFIG_FILE = Paths.get("../application.properties");

    private final Properties properties;
    private final Path path;

    private Config(Path path, Properties properties) {
        this.path = path;
        this.properties = properties;
    }

    public Path getMboxPath() {
        return Paths.get(getRequiredProperty("mbox.path")).toAbsolutePath();
    }

    public String getJdbcUrl() {
        return getRequiredProperty("jdbc.url");
    }

    public String getJdbcUser() {
        return properties.getProperty("jdbc.user");
    }

    public String getJdbcPassword() {
        return properties.getProperty("jdbc.password");
    }

    public String getJdbcSchema() {
        return getRequiredProperty("jdbc.schema");
    }

    private String getRequiredProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Property '" + key + "' not defined in " + path);
        }
        return value;
    }

    public Path getSerializedReportPath() {
        return Paths.get("../data.kryo").toAbsolutePath();
    }

    public Path getRawMailsPath() {
        return Paths.get("../mails.kryo").toAbsolutePath();
    }

    public Path getWifiDeviceDetailsCsv() {
        return Paths.get("../wifi-device-details.csv").toAbsolutePath();
    }

    public static Config readConfig() {
        return readConfig(DEFAULT_CONFIG_FILE);
    }

    public static Config readConfig(Path path) {
        final Properties properties = new Properties();
        final Path absolutePath = path.toAbsolutePath();
        LOG.fine(() -> "Reading config from file " + absolutePath);
        try (InputStream in = Files.newInputStream(absolutePath)) {
            properties.load(in);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error loading configuration from " + absolutePath, e);
        }
        return new Config(path, properties);
    }
}
