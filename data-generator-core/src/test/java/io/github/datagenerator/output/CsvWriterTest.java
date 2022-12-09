package io.github.datagenerator.output;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvWriterTest {

    @Test
    void writeOne() throws IOException {
        MapProvider provider = new MapProviderBuilder()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a")
                .build();

        String result = new CsvOutput(provider).oneToString();
        assertEquals("id;a\n0;a", result);
    }
}