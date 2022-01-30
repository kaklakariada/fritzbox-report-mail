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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;

public class KryoSerializerService<T> extends SerializerService<T> {
    private final Kryo kryo;

    public KryoSerializerService(final Class<T> type) {
        super(type);
        kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    @Override
    protected void serialize(final OutputStream outputStream, final T object) {
        try (Output output = new Output(outputStream)) {
            kryo.writeObject(output, object);
        }
    }

    @Override
    protected void serializeStream(final OutputStream outputStream, final Stream<T> objects) {
        try (final Output output = new Output(outputStream)) {
            objects.forEach(o -> kryo.writeObject(output, o));
        }
    }

    @Override
    protected Stream<T> deserializeStream(final InputStream inputStream) {
        final Input input = new Input(inputStream);
        return KryoStream.start(kryo, type, input).createStream();
    }

    @Override
    protected T deserialize(final InputStream inputStream) {
        try (Input input = new Input(inputStream)) {
            return kryo.readObject(input, type);
        }
    }
}
