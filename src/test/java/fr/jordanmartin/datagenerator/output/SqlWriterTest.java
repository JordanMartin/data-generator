package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlWriterTest {

    @Test
    void writeOne() throws IOException {
        ObjectProvider provider = new ObjectProvider()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new SqlWriter("test_table").writeOne(out, provider.getOne());

        assertEquals("INSERT INTO test_table(id,a) VALUES(0,'a');", out.toString());
    }
}