package com.github.kaklakariada.serialization;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;

class KryoIterable<T> implements Iterable<T> {
    private static final Logger LOG = Logger.getLogger(KryoIterable.class.getName());

    private final Kryo kryo;
    private final Class<T> type;
    private final Input input;

    private KryoIterable(final Kryo kryo, final Class<T> type, final Input input) {
        this.kryo = kryo;
        this.type = type;
        this.input = input;
    }

    static <T> KryoIterable<T> start(final Kryo kryo, final Class<T> type, final Input input) {
        return new KryoIterable<>(kryo, type, input);
    }

    @Override
    public Iterator<T> iterator() {
        return new KryoIterator<>(kryo, type, input);
    }

    private static class KryoIterator<T> implements Iterator<T> {
        private final Kryo kryo;
        private final Class<T> type;
        private final Input input;
        private T next;

        private KryoIterator(final Kryo kryo, final Class<T> type, final Input input) {
            this.kryo = kryo;
            this.type = type;
            this.input = input;
            this.next = read();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements");
            }
            final T o = next;
            next = read();
            return o;
        }

        private T read() {
            try {
                return kryo.readObject(input, type);
            } catch (final KryoException exception) {
                if (exception.getMessage().startsWith("Buffer underflow.")) {
                    LOG.finest(() -> "End of input reached: " + exception.getMessage());
                    return null;
                }
                throw new IllegalStateException("Error reading " + type.getName() + " from input " + input, exception);
            }
        }
    }
}