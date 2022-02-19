package org.apache.james.mime4j.mboxiterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MboxIterator}.
 */
class MboxIteratorTest {
    static final String MBOX_PATH = "src/test/resources/test-1/mbox.rlug";
    private final int DEFAULT_MESSAGE_SIZE = 10 * 1024;
    /** number of chars in our largest test message */
    private static final int CHARS_IN_MAX_MSG = 3500;
    private static final int MORE_THAN_FILE_SIZE = 13291;

    /**
     * Test of iterator method, of class MboxIterator.
     */
    @Test
    void testIterator() throws FileNotFoundException, IOException {
        iterateWithMaxMessage(DEFAULT_MESSAGE_SIZE);
    }

    /**
     * Test of iterator method, of class MboxIterator.
     */
    @Test
    void testIteratorLoop() throws FileNotFoundException, IOException {
        for (int i = CHARS_IN_MAX_MSG; i < MORE_THAN_FILE_SIZE; i++) {
            iterateWithMaxMessage(i);
        }
    }

    private void iterateWithMaxMessage(int maxMessageSize) throws IOException {
        int count = 0;
        for (final CharBufferWrapper msg : MboxIterator.fromFile(MBOX_PATH).maxMessageSize(maxMessageSize).build()) {
            final String message = fileToString(new File(MBOX_PATH + "-" + count));
            assertEquals(message.length(), msg.toString().length(), "String sizes match for file " + count);
            assertEquals(message, msg.toString(), "Missmatch with file " + count);
            count++;
        }
    }

    private static String fileToString(File file) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        reader.close();
        return sb.toString();
    }
}
