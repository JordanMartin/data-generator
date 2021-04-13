package fr.jordanmartin.datagenerator.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

public interface ObjectWriter {

    void writeOne(OutputStream out, Map<String, ?> object) throws IOException;

    void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException;

}

