package io.github.jordanmartin.datagenerator.output;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
public class JsonOutput extends ObjectWriterOuput {

    /**
     * Activation du pretty print
     */
    private boolean pretty;

    public JsonOutput(ObjectProvider provider) {
        this(provider, false);
    }

    public JsonOutput(ObjectProvider provider, boolean pretty) {
        super(provider);
        this.pretty = pretty;
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        com.fasterxml.jackson.databind.ObjectWriter objectWriter;
        if (pretty) {
            objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        } else {
            objectWriter = new ObjectMapper().writer();
        }
        objectWriter = objectWriter.withoutFeatures(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectWriter.writeValue(out, object);
        out.flush();
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        JsonGenerator jsonGenerator = new JsonFactory()
                .createGenerator(out)
                .setCodec(new ObjectMapper());

        if (this.pretty) {
            jsonGenerator.useDefaultPrettyPrinter();
        }

        jsonGenerator.writeStartArray();
        stream.forEach(object -> {
            try {
                jsonGenerator.writeObject(object);
            } catch (IOException e) {
                throw new OutputException(e);
            }
        });
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
    }

    @Override
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        setPretty(outputConfig.getOutputPretty());
        return this;
    }

    /**
     * Active ou désactive le formattage pretty json
     */
    public JsonOutput setPretty(Boolean pretty) {
        this.pretty = pretty != null && pretty;
        return this;
    }
}
