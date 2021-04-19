package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvWriterTest {

    @Test
    void writeOne() throws IOException {
        ObjectProvider provider = new ObjectProvider()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new CsvWriter().writeOne(out, provider.getOne());
        assertEquals("id;a\n0;a", out.toString());
    }
}