package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import io.github.datagenerator.domain.providers.base.IntIncrement;
import io.github.datagenerator.domain.providers.random.FakerExpression;
import io.github.datagenerator.generation.output.PojoOutput;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class OutputTest {
    @Test
    void generation_should_not_fail() throws IOException {
        MapProvider provider = new MapProviderBuilder()
                .field("id", new IntIncrement())
                .field("name", new FakerExpression("Name.firstName"))
                .build();

        new JsonWriter(provider)
                .setPretty(true)
                .writeMany(System.out, 2);
        new CsvWriter(provider)
                .writeMany(System.out, 2);
        new XmlWriter(provider)
                .setPretty(true)
                .writeMany(System.out, 2);
        new YamlWriter(provider)
                .setPretty(true)
                .writeMany(System.out, 2);
        new SqlWriter(provider)
                .writeMany(System.out, 2);
        new PojoOutput(provider, Pojo.class)
                .getMany(2);
        new TemplateWriter(provider)
                .setTemplate("#foreach($d in $list)id=$d.id\n#end")
                .writeMany(System.out, 2);
    }

    @Data
    private static class Pojo {
        String id;
        String name;
    }
}