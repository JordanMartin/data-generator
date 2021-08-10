package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import io.github.jordanmartin.datagenerator.provider.random.Sample;
import io.github.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ObjectOutputTest {
    @Test
    void generation_should_not_fail() throws IOException {
        ObjectProvider provider = new ObjectProvider()
                .field("id", new IntAutoIncrement())
                .field("name", new Sample("Name.firstName"));

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