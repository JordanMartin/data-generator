package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.generation.conf.IOutputConfig;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Génère un document à partir d'un template velocity
 */
public class TemplateWriter extends ObjectWriter {

    public static final String CONFIG_TEMPLATE = "template";
    private String template;

    public TemplateWriter(MapProvider provider) {
        super(provider);
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> objects) throws IOException {
        List<Map<String, ?>> list = objects.collect(Collectors.toList());
        Map<String, Object> context = new HashMap<>(Map.of("list", list));
        VelocityContext velocityContext = new VelocityContext(context);
        VelocityEngine velocity = new VelocityEngine();
        velocity.setProperty("runtime.strict_mode.enable", "true");
        velocity.init();

        StringReader reader = new StringReader(template);
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        try {
            velocity.evaluate(velocityContext, writer, "Velocity String Template Evaluation", reader);
            writer.flush();
        } catch (Exception e) {
            throw new WriterException(e);
        }
    }

    @Override
    public void configure(IOutputConfig config) {
        this.template = config.getRequiredString(CONFIG_TEMPLATE);
    }

    public TemplateWriter setTemplate(String template) {
        this.template = template;
        return this;
    }
}
