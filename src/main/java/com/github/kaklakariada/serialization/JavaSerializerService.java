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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaSerializerService<T> extends SerializerService<T> {
    final static Logger logger = LoggerFactory.getLogger(JavaSerializerService.class);

    public JavaSerializerService(final Class<T> type) {
        super(type);
    }

    @Override
    protected void serialize(final OutputStream outputStream, final T object) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(object);
        } catch (final IOException e) {
            throw new RuntimeException("Error serializing " + object, e);
        }
    }

    @Override
    protected T deserialize(final InputStream inputStream) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            final T object = type.cast(objectInputStream.readObject());
            return object;
        } catch (final IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error deserializing", e);
        }
    }
}
