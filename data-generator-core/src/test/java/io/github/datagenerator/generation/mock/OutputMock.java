package io.github.datagenerator.generation.mock;

import io.github.datagenerator.generation.output.Output;
import io.github.datagenerator.generation.writer.ObjectWriter;
import lombok.Getter;
import lombok.SneakyThrows;

public class OutputMock implements Output {

    private final ObjectWriter formatter;
    @Getter
    private String result;

    public OutputMock(ObjectWriter formatter) {
        this.formatter = formatter;
    }

    @Override
    @SneakyThrows
    public void execute()  {
        result = formatter.writeToString(1);
    }
}
