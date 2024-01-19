package io.github.datagenerator.generation.output;

import io.github.datagenerator.domain.core.DataDefinition;
import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.generation.conf.OutputConfig;
import io.github.datagenerator.generation.writer.CsvWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GenericOutputTest {

    @Test
    void should_output_correct_csv() throws IOException {
        MapProvider provider = new DataDefinition()
                .field("name", () -> "Jordan")
                .field("age", () -> "28")
                .build();

        CsvWriter writer = new CsvWriter(provider).setIncludeHeader(false);

        OutputConfig config = new OutputConfig()
                .setParam("url", "http://localhost:8050/bute-force")
                .setParam("request_count", 10);
        HttpOutput output = new HttpOutput(writer, config);
        output.execute();

//        assertEquals("Jordan;28", output.getResult());
    }

    @Test
    void shoud_send_http_request() {
        MyTest myTest = new MyTest();
        System.out.println(myTest);
    }

    class MyTest {
        int a = 6;

        MyTest() {
            a = 4;
        }
    }
}
