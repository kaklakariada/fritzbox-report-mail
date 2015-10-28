package org.chris.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SerializerService<T> {
    final static Logger logger = LoggerFactory.getLogger(SerializerService.class);

    protected final Class<T> type;

    public SerializerService(final Class<T> type) {
        this.type = type;
    }

    public void serialize(final Path outputFile, final T reports) {
        final Instant start = Instant.now();
        try {
            serialize(new BufferedOutputStream(Files.newOutputStream(outputFile)), reports);
        } catch (final IOException e) {
            throw new RuntimeException("Error writing file " + outputFile);
        }
        final Duration duration = Duration.between(start, Instant.now());
        logger.debug("Wrote object to file {} in {}", outputFile, duration);
    }

    protected abstract void serialize(final OutputStream outputStream, final T reports);

    public T deserialize(final Path inputFile) {

        final Instant start = Instant.now();
        final T object;
        try {
            object = deserialize(new BufferedInputStream(Files.newInputStream(inputFile)));
        } catch (final IOException e) {
            throw new RuntimeException("Error reading file " + inputFile);
        }
        final Duration duration = Duration.between(start, Instant.now());
        logger.debug("Read object from file {} in {}", inputFile, duration);
        return object;
    }

    protected abstract T deserialize(final InputStream inputStream);
}