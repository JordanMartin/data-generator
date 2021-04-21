package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import fr.jordanmartin.datagenerator.provider.random.Sample;
import fr.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ObjectOuputTest {
    @Test
    void toJson() throws IOException {
        ObjectProvider provider = new ObjectProvider()
                .field("id", new IntAutoIncrement())
                .field("name", new Sample("Name.firstName"));

        ObjectOuput.from(provider)
                .toJson().setPretty(true)
                .writeMany(System.out, 2);
        ObjectOuput.from(provider)
                .toCsv()
                .writeMany(System.out, 2);
        ObjectOuput.from(provider)
                .toXml().setPretty(true)
                .writeMany(System.out, 2);
        ObjectOuput.from(provider)
                .toYaml().setPretty(true)
                .writeMany(System.out, 2);
        ObjectOuput.from(provider)
                .toSQL()
                .writeMany(System.out, 2);
    }
}