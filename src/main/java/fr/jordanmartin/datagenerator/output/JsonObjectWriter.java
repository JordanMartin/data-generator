package fr.jordanmartin.datagenerator.output;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
public class JsonObjectWriter implements ObjectWriter {

    /**
     * Activation du pretty print
     */
    private final boolean pretty;

    public JsonObjectWriter() {
        this(false);
    }

    public JsonObjectWriter(boolean pretty) {
        this.pretty = pretty;
    }

    @Override
    public void write(OutputStream outputStream, Map<String, Object> object) throws IOException {
        com.fasterxml.jackson.databind.ObjectWriter objectWriter;
        if (pretty) {
            objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        } else {
            objectWriter = new ObjectMapper().writer();
        }
        objectWriter.writeValue(outputStream, object);
    }

    @Override
    public void write(OutputStream outputStream, Stream<Map<String, Object>> stream) throws IOException {
        JsonGenerator jsonGenerator = new JsonFactory()
                .createGenerator(outputStream)
                .setCodec(new ObjectMapper());

        if (this.pretty) {
            jsonGenerator.useDefaultPrettyPrinter();
        }

        try (jsonGenerator) {
            jsonGenerator.writeStartArray();
            stream.forEach(object -> {
                try {
                    jsonGenerator.writeObject(object);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonGenerator.writeEndArray();
        }
    }
}
