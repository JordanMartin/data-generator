package io.github.datagenerator.domain.definition;

import io.github.datagenerator.domain.ResourceUtils;
import io.github.datagenerator.domain.core.MapProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class YamlDefinitionParserTest {

    @Test
    void should_parse_valid_definition() {
        String definition = ResourceUtils.getResourceFileContent("definitions/simple-definition.yml");
        YamlDefinitionParser parser = new YamlDefinitionParser(definition);
        MapProvider generator = parser.parse();
        Map<String, ?> object = generator.get();
    }

    @Test
    void null_definition_should_failed() {
        YamlDefinitionParser parser = new YamlDefinitionParser("");
        Assertions.assertThrows(DefinitionException.class, parser::parse);
    }

    @Test
    void empty_definition_should_failed() {
        YamlDefinitionParser parser = new YamlDefinitionParser("");
        Assertions.assertThrows(DefinitionException.class, parser::parse);
    }
}