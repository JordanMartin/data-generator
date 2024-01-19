package io.github.datagenerator.server.domain;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.definition.YamlDefinitionParser;
import io.github.datagenerator.generation.writer.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderConf {
    private String name;
    private String definition;
    private MapProvider provider;
    private ObjectWriter output;
    private String contentType;
    private FormOutputConfig outputConfig;

    public static ProviderConf from(String name, String definition, FormOutputConfig config) {
        String format = config.getRequiredString("format");
        ProviderConf providerConf = new ProviderConf();
        YamlDefinitionParser parser = new YamlDefinitionParser(definition);
        MapProvider provider = parser.parse();
        ObjectWriter writer;

        switch (format) {
            case "yaml":
                writer = new YamlWriter(provider);
                providerConf.setOutput(writer);
                providerConf.setContentType("text/yaml");
                break;
            case "sql":
                writer = new SqlWriter(provider);
                providerConf.setOutput(writer);
                providerConf.setContentType("text/sql");
                break;
            case "csv":
                writer = new CsvWriter(provider);
                providerConf.setOutput(writer);
                providerConf.setContentType("text/csv");
                break;
            case "xml":
                writer = new XmlWriter(provider);
                providerConf.setOutput(writer);
                providerConf.setContentType("text/xml");
                break;
            case "template":
                writer = new TemplateWriter(provider);
                providerConf.setOutput(writer);
                providerConf.setContentType("text/plain");
                break;
            default:
                writer = new JsonWriter(provider);
                providerConf.setOutput(writer);
                providerConf.setContentType("application/json");
                break;
        }

        writer.configure(config);
        providerConf.setProvider(provider);
        providerConf.setDefinition(definition);
        providerConf.setOutputConfig(config);
        providerConf.setName(name);

        return providerConf;
    }

    public static ProviderConf from(String definition, FormOutputConfig outputConfig) {
        return from(null, definition, outputConfig);
    }

    public FormOutputConfig getOutputConfig() {
        return outputConfig;
    }
}
