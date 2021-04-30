package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlWriterTest {

    @Test
    void writeOne() throws IOException {
        ObjectProvider provider = new ObjectProvider()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a");

        String result = new SqlOutput(provider)
                .setTableName("test_table")
                .oneToString();
        assertEquals("INSERT INTO test_table(id,a) VALUES(0,'a');", result);
    }
}