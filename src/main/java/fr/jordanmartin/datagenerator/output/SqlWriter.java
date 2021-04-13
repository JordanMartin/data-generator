package fr.jordanmartin.datagenerator.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format insertion SQL
 */
public class SqlWriter implements ObjectWriter {

    private final String tableName;

    public SqlWriter(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        writeInsertIntoPart(writer, object);
        writer.write(" VALUES");
        writeValue(writer, object);
        writer.append(";");
        writer.flush();
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        stream.forEach(object -> {
            try {
                writeInsertIntoPart(writer, object);
                writer.write(" VALUE");
                writeValue(writer, object);
                writer.append(";");
                writer.append('\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writer.flush();
    }

    private void writeInsertIntoPart(Writer writer, Map<String, ?> object) throws IOException {
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
            Object value = entry.getValue();
            if (value instanceof Number || value instanceof Boolean) {
                writer.append(String.valueOf(value));
            } else {
                writer.append("'").append(String.valueOf(value)).append("'");
            }
        }
        writer.append(")");
    }
}
