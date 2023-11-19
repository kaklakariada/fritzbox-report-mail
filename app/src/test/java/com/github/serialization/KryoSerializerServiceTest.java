package com.github.serialization;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.github.kaklakariada.serialization.KryoSerializerService;

class KryoSerializerServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void serializeStream() {
        final KryoSerializerService<String> service = new KryoSerializerService<>(String.class);
        final Path file = tempDir.resolve("file");
        service.serializeStream(file, Stream.of("s1", "s2", "s3"));
        final List<String> result = service.deserializeStream(file).collect(toList());
        assertThat(result).containsExactly("s1", "s2", "s3");
    }

    @Test
    void serialize() {
        final KryoSerializerService<String> service = new KryoSerializerService<>(String.class);
        final Path file = tempDir.resolve("file");
        service.serialize(file, "data");
        final String result = service.deserialize(file);
        assertThat(result).isEqualTo("data");
    }
}
