package org.chris.serialization;

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
