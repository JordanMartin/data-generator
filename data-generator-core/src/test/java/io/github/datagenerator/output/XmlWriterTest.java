package io.github.datagenerator.output;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlWriterTest {

    @Test
    void writeOne() throws IOException {
        MapProvider child = new MapProviderBuilder()
                .field("b", (objectContext) -> "b")
                .field("c", (objectContext) -> "c")
                .build();

        MapProvider provider = new MapProviderBuilder()
                .field("id", (objectContext) -> 0)
                .field("a", (objectContext) -> "a")
                .field("child", child)
                .build();

        String result = new XmlOutput(provider).setObjectName("obj").oneToString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><obj><id>0</id><a>a</a><child><b>b</b><c>c</c></child></obj>";
        assertEquals(expected, result);
    }
}
