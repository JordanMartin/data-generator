package fr.jordanmartin.datagenerator.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

public interface ObjectWriter {

    void writeOne(OutputStream out, Map<String, ?> object) throws IOException;

    void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException;

    default String asString(Map<String, ?> object) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeOne(out, object);
            return out.toString();
        }
    }

    default String asString(Stream<Map<String, ?>> stream) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeMany(out, stream);
            return out.toString();
        }
    }

}

