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

import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Kryo.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializerService<T> extends SerializerService<T> {
    final static Logger logger = LoggerFactory.getLogger(KryoSerializerService.class);
    private final Kryo kryo;

    public KryoSerializerService(final Class<T> type) {
        super(type);
        kryo = new Kryo();
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    @Override
    protected void serialize(final OutputStream outputStream, final T object) {
        try (Output output = new Output(outputStream)) {
            kryo.writeObject(output, object);
        }
    }

    @Override
    protected T deserialize(final InputStream inputStream) {
        try (Input input = new Input(inputStream)) {
            return kryo.readObject(input, type);
        }
    }
}
