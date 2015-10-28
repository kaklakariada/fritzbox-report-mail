package org.chris.serialization;

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
