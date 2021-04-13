package fr.jordanmartin.datagenerator.output;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
public class CsvObjectWriter implements ObjectWriter {

    @Override
    public void write(OutputStream outputStream, Map<String, Object> object) throws IOException {
        String header = String.join(";", object.keySet());
        String fields = object.values().stream().map(Object::toString).collect(Collectors.joining(";"));

        outputStream.write(header.getBytes(StandardCharsets.UTF_8));
        outputStream.write('\n');
        outputStream.write(fields.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void write(OutputStream outputStream, Stream<Map<String, Object>> stream) throws IOException {
        AtomicBoolean headerLine = new AtomicBoolean(false);
        stream.forEach(object -> {
            try {
                if (!headerLine.get()) {
                    String header = String.join(";", object.keySet());
                    outputStream.write(header.getBytes(StandardCharsets.UTF_8));
                    outputStream.write('\n');
                    headerLine.set(true);
                }
                String fields = object.values().stream().map(Object::toString).collect(Collectors.joining(";"));
                outputStream.write(fields.getBytes(StandardCharsets.UTF_8));
                outputStream.write('\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
