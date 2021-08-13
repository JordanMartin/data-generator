package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
public class CsvOutput extends ObjectWriterOuput {

    private static final String DEFAULT_SEPARATOR = ";";
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
        this(provider, DEFAULT_SEPARATOR);
    }

    public CsvOutput(ObjectProvider provider, String separator) {
        super(provider);
        setSeparator(separator);
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        String fields = object.values().stream().map(Object::toString).collect(Collectors.joining(";"));
        writer.write(String.join(";", object.keySet()));
        writer.write('\n');
        writer.write(fields);
        writer.flush();
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
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
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        return setSeparator(outputConfig.getSeparator());
    }

    public CsvOutput setSeparator(String separator) {
        this.separator = separator == null
                ? DEFAULT_SEPARATOR
                : separator;
        this.separator = this.separator.replace("<tab>", "\t");
        return this;
    }
}
