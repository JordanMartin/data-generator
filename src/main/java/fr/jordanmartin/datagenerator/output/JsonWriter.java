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
public class JsonWriter implements ObjectWriter {

    /**
     * Activation du pretty print
     */
    private final boolean pretty;

    public JsonWriter() {
        this(false);
    }

    public JsonWriter(boolean pretty) {
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
                throw new RuntimeException(e);
            }
        });
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
    }
}
