package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YamlWriterTest {
    @Test
    void write() throws IOException {
        MapProvider child = new MapProviderBuilder()
                .field("b", (objectContext) -> "b")
                .field("c", (objectContext) -> "c")
                .build();

        MapProvider provider = new MapProviderBuilder()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a")
                .field("child", child)
                .build();

        String result = new YamlWriter(provider).writeToString(2);
        var expected = "- id: 0\n" +
                "  a: a\n" +
                "  child:\n" +
                "    b: b\n" +
                "    c: c\n" +
                "- id: 0\n" +
                "  a: a\n" +
                "  child:\n" +
                "    b: b\n" +
                "    c: c\n";
        assertEquals(expected, result);
    }
}