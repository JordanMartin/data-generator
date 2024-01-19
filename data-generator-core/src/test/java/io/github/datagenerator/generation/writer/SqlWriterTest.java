package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlWriterTest {

    @Test
    void writeOne() throws IOException {
        MapProvider provider = new MapProviderBuilder()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a")
                .build();

        String result = new SqlWriter(provider)
                .setTableName("test_table")
                .writeToString(1);
        assertEquals("INSERT INTO test_table(id,a) VALUES(0,'a');", result);
    }
}