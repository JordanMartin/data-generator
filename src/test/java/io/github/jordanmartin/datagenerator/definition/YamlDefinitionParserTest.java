package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.ResourceUtils;
import io.github.jordanmartin.datagenerator.output.ObjectOuput;
import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class YamlDefinitionParserTest {

    @Test
    void loadDefinition() throws IOException {
        String definition = ResourceUtils.getResourceFileContent("definitions/simple-definition.yml");
        YamlDefinitionParser parser = new YamlDefinitionParser(definition);
        ObjectProvider generator = parser.parse();
        ObjectOuput.from(generator).toJson().writeMany(System.out, 10);
    }

}