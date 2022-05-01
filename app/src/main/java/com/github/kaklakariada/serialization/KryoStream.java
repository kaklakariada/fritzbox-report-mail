package com.github.kaklakariada.serialization;

import java.util.logging.Logger;
import java.util.stream.Stream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;

class KryoStream<T> {
    private static final Logger LOG = Logger.getLogger(KryoStream.class.getName());

    private final Kryo kryo;
    private final Class<T> type;
    private final Input input;
    private T next;

    private KryoStream(Kryo kryo, Class<T> type, Input input) {
        this.kryo = kryo;
        this.type = type;
        this.input = input;
        this.next = read();
    }

    static <T> KryoStream<T> start(Kryo kryo, Class<T> type, Input input) {
        return new KryoStream<>(kryo, type, input);
    }

    private boolean hasNext(T o) {
        return next != null;
    }

    private T next(T ignore) {
        final T o = next;
        next = read();
        return o;
    }

    private T read() {
        try {
            return kryo.readObject(input, type);
        } catch (final KryoException e) {
            if (e.getMessage().startsWith("Buffer underflow.")) {
                LOG.finest(() -> "End of input " + input + " reached: " + e.getMessage());
                return null;
            }
            throw new IllegalStateException("Error reading " + type.getName() + " from input " + input, e);
        }
    }

    public Stream<T> createStream() {
        return Stream.iterate(next, this::hasNext, this::next);
    }
}