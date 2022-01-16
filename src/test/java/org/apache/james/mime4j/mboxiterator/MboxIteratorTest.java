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
package org.apache.james.mime4j.mboxiterator;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for {@link MboxIterator}.
 */
public class MboxIteratorTest {

    private final static Logger LOG = LoggerFactory.getLogger(MboxIteratorTest.class);

    @Rule
    public final TestName name = new TestName();
    public static final String MBOX_PATH = "src/test/resources/test-1/mbox.rlug";
    private final int DEFAULT_MESSAGE_SIZE = 10 * 1024;
    // number of chars oin our largest test message
    private static final int CHARS_IN_MAX_MSG = 3500;
    private static final int MORE_THAN_FILE_SIZE = 13291;

    /**
     * Test of iterator method, of class MboxIterator.
     */
    @Test
    public void testIterator() throws FileNotFoundException, IOException {
        LOG.info("Executing {}", name.getMethodName());
        iterateWithMaxMessage(DEFAULT_MESSAGE_SIZE);
    }

    /**
     * Test of iterator method, of class MboxIterator.
     */
    @Test
    public void testIteratorLoop() throws FileNotFoundException, IOException {
        LOG.info("Executing {}", name.getMethodName());
        for (int i = CHARS_IN_MAX_MSG; i < MORE_THAN_FILE_SIZE; i++) {
            LOG.trace("Runinng iteration {}  with message size {}", i - CHARS_IN_MAX_MSG, i);
            iterateWithMaxMessage(i);
        }
    }

    private void iterateWithMaxMessage(int maxMessageSize) throws IOException {
        int count = 0;
        for (final CharBufferWrapper msg : MboxIterator.fromFile(MBOX_PATH).maxMessageSize(maxMessageSize).build()) {
            final String message = fileToString(new File(MBOX_PATH + "-" + count));
            assertEquals("String sizes match for file " + count, message.length(), msg.toString().length());
            assertEquals("Missmatch with file " + count, message, msg.toString());
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
