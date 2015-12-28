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
package com.github.kaklakariada.serialization;

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