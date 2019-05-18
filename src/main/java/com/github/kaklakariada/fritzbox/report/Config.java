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

    public String getInfluxdbUrl() {
        return properties.getProperty("influxdb.url");
    }

    public static Config readConfig() {
        return readConfig(Paths.get("application.properties"));
    }

    private static Config readConfig(Path path) {
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