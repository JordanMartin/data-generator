package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ContextAwareProvider;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlWriterTest {

    @Test
    void writeOne() throws IOException {
        ObjectProvider child = new ObjectProvider()
                .field("b", () -> "b")
                .field("c", () -> "c");

        ObjectProvider provider = new ObjectProvider()
                .field("id", () -> 0)
                .field("a", () -> "a")
                .field("child", (ContextAwareProvider<?>) child);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new XmlWriter("obj", false).writeOne(out, provider.getOne());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><obj><id>0</id><a>a</a><child><b>b</b><c>c</c></child></obj>", out.toString());
    }
}