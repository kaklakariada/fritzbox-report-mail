package org.apache.james.mime4j.mboxiterator;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class CharBufferWrapperTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(CharBufferWrapper.class).verify();
    }
}
