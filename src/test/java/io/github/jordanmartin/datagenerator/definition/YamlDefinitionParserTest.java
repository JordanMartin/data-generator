package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.ResourceUtils;
import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

class YamlDefinitionParserTest {

    @Test
    void should_parse_valid_definition() throws IOException {
        String definition = ResourceUtils.getResourceFileContent("definitions/simple-definition.yml");
        YamlDefinitionParser parser = new YamlDefinitionParser(definition);
        ObjectProvider generator = parser.parse();
        Map<String, ?> object = generator.getOne();

    }

    @Test
    void null_definition_should_failed() throws IOException {
        YamlDefinitionParser parser = new YamlDefinitionParser("");
        Assertions.assertThrows(DefinitionException.class, parser::parse);
    }

    @Test
    void empty_definition_should_failed() {
        YamlDefinitionParser parser = new YamlDefinitionParser("");
        Assertions.assertThrows(DefinitionException.class, parser::parse);
    }
}