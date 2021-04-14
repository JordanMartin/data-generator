package fr.jordanmartin.datagenerator.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
public class CsvWriter implements ObjectWriter {

    private final String separator;

    public CsvWriter(String separator) {
        this.separator = separator;
    }

    public CsvWriter() {
        this(";");
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        Writer writer = new OutputStreamWriter(out);
        String fields = object.values().stream().map(Object::toString).collect(Collectors.joining(";"));
        writer.write(String.join(";", object.keySet()));
        writer.write('\n');
        writer.write(fields);
        writer.flush();
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        Writer writer = new OutputStreamWriter(out);
        AtomicBoolean headerLine = new AtomicBoolean(false);
        stream.forEach(object -> {
            try {
                if (!headerLine.get()) {
                    String headers = String.join(separator, object.keySet());
                    writer.write(headers);
                    writer.write('\n');
                    headerLine.set(true);
                }
                String fields = object.values().stream().map(Object::toString).collect(Collectors.joining(separator));
                writer.write(fields);
                writer.write('\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writer.flush();
    }
}
