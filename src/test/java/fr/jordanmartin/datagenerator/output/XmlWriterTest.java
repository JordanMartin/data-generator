package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlWriterTest {

    @Test
    void writeOne() throws IOException {
        ObjectProvider child = new ObjectProvider()
                .field("b", (objectContext) -> "b")
                .field("c", (objectContext) -> "c");

        ObjectProvider provider = new ObjectProvider()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a")
                .field("child", child);

        String result = new XmlWriter("obj", false).asString(provider.getOne());
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><obj><id>0</id><a>a</a><child><b>b</b><c>c</c></child></obj>";
        assertEquals(expected, result);
    }
}