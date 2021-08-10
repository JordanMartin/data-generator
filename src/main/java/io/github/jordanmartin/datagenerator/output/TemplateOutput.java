package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class TemplateOutput extends ObjectWriterOuput {

    private final String template;

    protected TemplateOutput(ObjectProvider provider, String template) {
        super(provider);
        this.template = template;
    }

    @Override
    public void writeMany(OutputStream out, int count) throws IOException {
        Map<String, Object> context = new HashMap<>(Map.of("list", provider.getList(count)));
        VelocityContext velocityContext = new VelocityContext(context);

        StringReader reader = new StringReader(template);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        try (reader; writer) {
            Velocity.evaluate(velocityContext, writer, "Velocity String Template Evaluation", reader);
        }
    }
}
