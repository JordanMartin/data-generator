package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
public class CsvOutput extends ObjectWriterOuput {

    /**
     * Séparateur CSV
     */
    private String separator;

    /**
     * Nouveau CsvOuput avec ";" en tant que séparateur
     *
     * @param provider Le générateur d'objet
     */
    public CsvOutput(ObjectProvider provider) {
        this(provider, ";");
    }

    public CsvOutput(ObjectProvider provider, String separator) {
        super(provider);
        this.separator = separator;
    }

    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        String fields = object.values().stream().map(Object::toString).collect(Collectors.joining(";"));
        writer.write(String.join(";", object.keySet()));
        writer.write('\n');
        writer.write(fields);
        writer.flush();
    }

    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
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
                throw new OutputException(e);
            }
        });
        writer.flush();
    }

    @Override
    public void writeMany(OutputStream out, int count) throws IOException {
        if (count == 1) {
            writeOne(out, provider.getOne());
        } else {
            writeMany(out, provider.getStream(count));
        }
    }


    public CsvOutput setSeparator(String separator) {
        this.separator = separator;
        return this;
    }
}
