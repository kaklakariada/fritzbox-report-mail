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
package com.github.kaklakariada.serialization;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;
import java.util.stream.Stream;

public abstract class SerializerService<T> {
    private static final Logger LOG = Logger.getLogger(SerializerService.class.getName());

    protected final Class<T> type;

    protected SerializerService(final Class<T> type) {
        this.type = type;
    }

    public void serialize(final Path outputFile, final T reports) {
        final Instant start = Instant.now();
        try {
            serialize(new BufferedOutputStream(Files.newOutputStream(outputFile)), reports);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error writing file " + outputFile, e);
        }
        final Duration duration = Duration.between(start, Instant.now());
        LOG.fine(() -> "Wrote object to file " + outputFile + " in " + duration);
    }

    protected abstract void serialize(final OutputStream outputStream, final T reports);

    public T deserialize(final Path inputFile) {
        final Instant start = Instant.now();
        final T object;
        try {
            object = deserialize(new BufferedInputStream(Files.newInputStream(inputFile)));
        } catch (final IOException e) {
            throw new UncheckedIOException("Error reading file " + inputFile, e);
        }
        final Duration duration = Duration.between(start, Instant.now());
        LOG.fine(() -> "Read object from file " + inputFile + " in " + duration);
        return object;
    }

    protected abstract T deserialize(final InputStream inputStream);

    public void serializeStream(final Path outputFile, final Stream<T> objects) {
        try (OutputStream stream = Files.newOutputStream(outputFile)) {
            final int count = serializeStream(stream, objects);
            LOG.fine(() -> "Wrote " + count + " objects of type " + type.getSimpleName() + " to " + outputFile);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error writing to " + outputFile, e);
        }
    }

    protected abstract int serializeStream(final OutputStream outputStream, final Stream<T> objects);

    public Stream<T> deserializeStream(final Path inputFile) {
        try {
            final InputStream stream = Files.newInputStream(inputFile);
            return deserializeStream(stream);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error reading from " + inputFile, e);
        }
    }

    protected abstract Stream<T> deserializeStream(final InputStream inputStream);
}