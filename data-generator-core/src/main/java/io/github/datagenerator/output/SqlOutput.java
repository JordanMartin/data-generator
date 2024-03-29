package io.github.datagenerator.output;

import io.github.datagenerator.domain.core.MapProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format insertion SQL
 */
public class SqlOutput extends ObjectWriterOuput {

    private static final String DEFAULT_TABLE_NAME = "my_table";
    private String tableName;

    public SqlOutput(MapProvider provider, String tableName) {
        super(provider);
        setTableName(tableName);
    }

    public SqlOutput(MapProvider provider) {
        this(provider, DEFAULT_TABLE_NAME);
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
                throw new OutputException(e);
            }
        });
        writer.flush();
    }

    @Override
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        setTableName(outputConfig.getTableName());
        return this;
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

    public SqlOutput setTableName(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            this.tableName = DEFAULT_TABLE_NAME;
        } else {
            this.tableName = tableName;
        }
        return this;
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
}
