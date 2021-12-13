package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

public class ProgressObjectWriterOutput extends ObjectWriterOuput {

    private ObjectWriterOuput writerOuput;

    public ProgressObjectWriterOutput(ObjectProvider provider, ObjectWriterOuput writerOuput) {
        super(provider);
        this.writerOuput = writerOuput;
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> objects) throws IOException {
    }

    @Override
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        return null;
    }
}
