package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import io.github.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemplateOutputTest {

    @Test
    void basic_template_should_works() throws IOException {
        ObjectProvider provider = new ObjectProvider()
                .field("id", new IntAutoIncrement())
                .field("source", (ctx) -> "template")
                .field("utf8", (ctx) -> "éllo");

        String template = "#foreach($d in $list)" +
                "id=$d.id, source=$d.source, utf8=$d.utf8\n" +
                "#end";

        String result = ObjectOutput.from(provider).toTemplate(template)
                .manyToString(2);

        assertEquals("id=0, source=template, utf8=éllo\nid=1, source=template, utf8=éllo\n", result);
    }

    @Test
    void bad_directive_should_fail() throws IOException {
        ObjectProvider provider = new ObjectProvider()
                .field("id", ctx -> 1);
        String template = "#if (baddirective";
        assertThrows(OutputException.class, () -> {
            ObjectOutput.from(provider).toTemplate(template).oneToString();
        });
    }

    @Test
    void non_existing_var_should_fail() {
        ObjectProvider provider = new ObjectProvider()
                .field("id", ctx -> 1);
        String template = "$noop";
        assertThrows(OutputException.class, () -> {
            ObjectOutput.from(provider).toTemplate(template).oneToString();
        });
    }
}