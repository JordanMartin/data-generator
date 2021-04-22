package fr.jordanmartin.datagenerator.output;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;

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
    public void writeMany(OutputStream out, int count) throws IOException {
        writeObjects(out, provider.getStream(count));
    }

    @Override
    public void writeOne(OutputStream out) throws IOException {
        writeObject(out, provider.getOne());
    }

    private void writeObject(OutputStream out, Map<String, ?> object) throws IOException {
        com.fasterxml.jackson.databind.ObjectWriter objectWriter;
        if (pretty) {
            objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        } else {
            objectWriter = new ObjectMapper().writer();
        }
        objectWriter.writeValue(out, object);
        out.flush();
    }

    private void writeObjects(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
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

    /**
     * Active ou désactive le formattage pretty json
     */
    public JsonOutput setPretty(boolean pretty) {
        this.pretty = pretty;
        return this;
    }
}
