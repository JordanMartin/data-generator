package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonWriterTest {

    @Test
    void writeOne() throws IOException {
        ObjectProvider child = new ObjectProvider()
                .field("b", (objectContext) -> "b")
                .field("c", (objectContext) -> "c");

        ObjectProvider provider = new ObjectProvider()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a")
                .field("child", child);

        String result = new JsonOutput(provider).oneToString();
        assertEquals("{\"id\":0,\"a\":\"a\",\"child\":{\"b\":\"b\",\"c\":\"c\"}}", result);
    }
}