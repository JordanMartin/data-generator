package fr.jordanmartin.datagenerator.definition;

import fr.jordanmartin.datagenerator.ResourceUtils;
import fr.jordanmartin.datagenerator.output.ObjectOuput;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
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