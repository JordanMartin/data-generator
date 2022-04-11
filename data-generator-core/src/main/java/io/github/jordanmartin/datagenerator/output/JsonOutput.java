package io.github.jordanmartin.datagenerator.output;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    /**
     * Détermine si les valeurs null doivent être incluses
     */
    private boolean includeNull;

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
        ObjectMapper objectMapper = getObjectMapper();
        if (pretty) {
            objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        } else {
            objectWriter = objectMapper.writer();
        }
        objectWriter = objectWriter.withoutFeatures(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectWriter.writeValue(out, object);
        out.flush();
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        JsonGenerator jsonGenerator = new JsonFactory()
                .createGenerator(out)
                .setCodec(getObjectMapper());
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

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        if (!includeNull) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        return objectMapper;
    }

    @Override
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        setPretty(outputConfig.getOutputPretty());
        setIncludeNull(outputConfig.getIncludeNull());
        return this;
    }

    /**
     * Active ou désactive le formattage pretty json
     */
    public JsonOutput setPretty(Boolean pretty) {
        this.pretty = pretty != null && pretty;
        return this;
    }

    public JsonOutput setIncludeNull(Boolean includeNull) {
        if (includeNull != null) {
            this.includeNull = includeNull;
        }
        return this;
    }
}
