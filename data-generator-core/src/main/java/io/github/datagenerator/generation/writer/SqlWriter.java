package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.generation.conf.IOutputConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Génère les données au format insertion SQL
 */
public class SqlWriter extends ObjectWriter {

    private static final String DEFAULT_TABLE_NAME = "my_table";
    private String tableName;

    public SqlWriter(MapProvider provider) {
        super(provider);
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        writeInsertInto(writer, object);
        writer.write(" VALUES");
        writeValue(writer, object);
        writer.append(";");
        writer.flush();
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        stream.forEach(object -> {
            try {
                writeInsertInto(writer, object);
                writer.write(" VALUE");
                writeValue(writer, object);
                writer.append(";");
                writer.append('\n');
            } catch (IOException e) {
                throw new WriterException(e);
            }
        });
        writer.flush();
    }

    private void writeInsertInto(Writer writer, Map<String, ?> object) throws IOException {
        String columns = String.join(",", object.keySet());
        writer
                .append("INSERT INTO ").append(tableName)
                .append("(").append(columns).append(")");
    }

    private void writeValue(Writer writer, Map<String, ?> object) throws IOException {
        writer.append("(");
        boolean firstColumn = true;
        for (Map.Entry<String, ?> entry : object.entrySet()) {
            if (firstColumn) {
                firstColumn = false;
            } else {
                writer.append(",");
            }
            String value = mapValue(entry.getValue());
            writer.append(value);
        }
        writer.append(")");
    }

    private String mapValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        return "'" + value + "'";
    }

    @Override
    public void configure(IOutputConfig config) {
        this.tableName = Optional.ofNullable(tableName)
                .filter(String::isBlank)
                .orElse(DEFAULT_TABLE_NAME);
    }

    public SqlWriter setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
}
