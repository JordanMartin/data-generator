package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import io.github.datagenerator.domain.providers.base.IntIncrement;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemplateWriterTest {

    @Test
    void basic_template_should_works() throws IOException {
        MapProvider provider = new MapProviderBuilder()
                .field("id", new IntIncrement())
                .field("source", (ctx) -> "template")
                .field("utf8", (ctx) -> "éllo")
                .build();

        String template = "#foreach($d in $list)" +
                "id=$d.id, source=$d.source, utf8=$d.utf8\n" +
                "#end";

        String result = new TemplateWriter(provider)
                .setTemplate(template)
                .writeToString(2);

        assertEquals("id=0, source=template, utf8=éllo\nid=1, source=template, utf8=éllo\n", result);
    }

    @Test
    void bad_directive_should_fail() throws IOException {
        MapProvider provider = new MapProviderBuilder()
                .field("id", ctx -> 1)
                .build();
        String template = "#if (baddirective";
        assertThrows(WriterException.class, () -> {
            new TemplateWriter(provider)
                    .setTemplate(template)
                    .writeToString(1);
        });
    }

    @Test
    void non_existing_var_should_fail() {
        MapProvider provider = new MapProviderBuilder()
                .field("id", ctx -> 1)
                .build();
        String template = "$noop";
        assertThrows(WriterException.class, () -> {
            new TemplateWriter(provider)
                    .setTemplate(template)
                    .writeToString(1);
        });
    }
}