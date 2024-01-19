package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonWriterTest {

    @Test
    void writeOne() throws IOException {
        MapProvider child = new MapProviderBuilder()
                .field("b", (objectContext) -> "b")
                .field("c", (objectContext) -> "c")
                .build();

        MapProvider provider = new MapProviderBuilder()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a")
                .field("child", child)
                .build();

        String result = new JsonWriter(provider).writeToString(1);
        assertEquals("{\"id\":0,\"a\":\"a\",\"child\":{\"b\":\"b\",\"c\":\"c\"}}", result);
    }
}