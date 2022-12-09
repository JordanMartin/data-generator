package io.github.datagenerator.output;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.providers.MapProviderBuilder;
import io.github.datagenerator.domain.providers.base.IntIncrement;
import io.github.datagenerator.domain.providers.random.FakerExpression;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ObjectOutputTest {
    @Test
    void generation_should_not_fail() throws IOException {
        MapProvider provider = new MapProviderBuilder()
                .field("id", new IntIncrement())
                .field("name", new FakerExpression("Name.firstName"))
                .build();

        ObjectOutput.from(provider)
                .toJson().setPretty(true)
                .writeMany(System.out, 2);
        ObjectOutput.from(provider)
                .toCsv()
                .writeMany(System.out, 2);
        ObjectOutput.from(provider)
                .toXml().setPretty(true)
                .writeMany(System.out, 2);
        ObjectOutput.from(provider)
                .toYaml().setPretty(true)
                .writeMany(System.out, 2);
        ObjectOutput.from(provider)
                .toSQL()
                .writeMany(System.out, 2);
        ObjectOutput.from(provider)
                .toPojo(Pojo.class)
                .getMany(2);
        ObjectOutput.from(provider)
                .toTemplate("#foreach($d in $list)id=$d.id\n#end")
                .writeMany(System.out, 2);
    }

    @Data
    private static class Pojo {
        String id;
        String name;
    }
}