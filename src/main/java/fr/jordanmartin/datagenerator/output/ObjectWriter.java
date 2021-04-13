package fr.jordanmartin.datagenerator.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

public interface ObjectWriter {

    void write(OutputStream outputStream, Map<String, Object> object) throws IOException;

    void write(OutputStream outputStream, Stream<Map<String, Object>> stream) throws IOException;
}

