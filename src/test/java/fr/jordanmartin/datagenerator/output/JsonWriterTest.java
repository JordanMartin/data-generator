package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new JsonWriter(false).writeOne(out, provider.getOne());
        assertEquals("{\"id\":0,\"a\":\"a\",\"child\":{\"b\":\"b\",\"c\":\"c\"}}", out.toString());
    }
}