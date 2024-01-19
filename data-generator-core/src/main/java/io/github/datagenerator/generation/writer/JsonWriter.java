package io.github.datagenerator.generation.writer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.generation.conf.IOutputConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
@Formatter(name = "json", description = "JSON Format", args = {
        @FormatterConfig(name = "pretty")
})
public class JsonWriter extends ObjectWriter {

    public static final String CONFIG_PRETTY = "pretty";
    public static final boolean CONFIG_PRETTY_DEFAULT = false;
    public static final String CONFIG_INCLUDES_NULLS = "include_null";
    public static final boolean CONFIG_INCLUDES_NULLS_DEFAULT = true;

    /**
     * Activation du pretty print
     */
    private boolean pretty = CONFIG_PRETTY_DEFAULT;

    /**
     * Détermine si les valeurs null doivent être incluses
     */
    private boolean includeNull = CONFIG_INCLUDES_NULLS_DEFAULT;

    public JsonWriter(MapProvider provider) {
        super(provider);
    }

    public JsonWriter(MapProvider provider, IOutputConfig config) {
        super(provider);
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
                throw new WriterException(e);
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

    public void configure(IOutputConfig config) {
        this.pretty = config.getBoolean(CONFIG_PRETTY).orElse(CONFIG_PRETTY_DEFAULT);
        this.includeNull = config.getBoolean(CONFIG_INCLUDES_NULLS).orElse(CONFIG_INCLUDES_NULLS_DEFAULT);
    }

    /**
     * Active ou désactive le formattage pretty json
     */
    public JsonWriter setPretty(boolean pretty) {
        this.pretty = pretty;
        return this;
    }

    public JsonWriter setIncludeNull(boolean includeNull) {
        this.includeNull = includeNull;
        return this;
    }
}
