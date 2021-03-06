package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YamlWriterTest {
    @Test
    void write() throws IOException {
        ObjectProvider child = new ObjectProvider()
                .field("b", (objectContext) -> "b")
                .field("c", (objectContext) -> "c");

        ObjectProvider provider = new ObjectProvider()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a")
                .field("child", child);

        String result = new YamlOutput(provider).manyToString(2);
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