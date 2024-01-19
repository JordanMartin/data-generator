package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.generation.conf.IOutputConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvWriter extends ObjectWriter {

    public static final String CONFIG_SEPARATOR = "separator";
    private static final String CONFIG_SEPARATOR_DEFAULT = ";";
    public static final String CONFIG_INCLUDE_HEADER = "include_header";
    private static final boolean CONFIG_INCLUDE_HEADER_DEFAULT = true;

    private String separator = CONFIG_SEPARATOR_DEFAULT;
    private boolean includeHeader = CONFIG_INCLUDE_HEADER_DEFAULT;

    public CsvWriter(MapProvider provider) {
        super(provider);
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        String fields = object.values().stream().map(Object::toString).collect(Collectors.joining(";"));
        if (includeHeader) {
            writer.write(String.join(";", object.keySet()));
            writer.write('\n');
        }
        writer.write(fields);
        writer.flush();
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        AtomicBoolean headerLine = new AtomicBoolean(false);
        stream.forEach(object -> {
            try {
                if (includeHeader && !headerLine.get()) {
                    String headers = String.join(separator, object.keySet());
                    writer.write(headers);
                    writer.write('\n');
                    headerLine.set(true);
                }
                String fields = object.values().stream()
                        .map(this::mapValue)
                        .collect(Collectors.joining(separator));
                writer.write(fields);
                writer.write('\n');
            } catch (IOException e) {
                throw new WriterException(e);
            }
        });
        writer.flush();
    }

    private String mapValue(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    @Override
    public void configure(IOutputConfig config) {
        setSeparator(config.getString(CONFIG_SEPARATOR).orElse(CONFIG_SEPARATOR_DEFAULT));
        setIncludeHeader(config.getBoolean(CONFIG_INCLUDE_HEADER).orElse(CONFIG_INCLUDE_HEADER_DEFAULT));
    }

    public CsvWriter setSeparator(String separator) {
        this.separator = separator.replace("<tab>", "\t");
        return this;
    }

    public CsvWriter setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
        return this;
    }
}
