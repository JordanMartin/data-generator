package io.github.datagenerator.server.domain;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.definition.YamlDefinitionParser;
import io.github.datagenerator.output.ObjectOutput;
import io.github.datagenerator.output.ObjectWriterOuput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderConf {
    private String name;
    private String definition;
    private MapProvider provider;
    private ObjectWriterOuput output;
    private String contentType;
    private OutputConfig outputConfig;

    public static ProviderConf from(String name, String definition, OutputConfig outputConfig) {
        String format = outputConfig.getFormat();
        ProviderConf providerConf = new ProviderConf();
        YamlDefinitionParser parser = new YamlDefinitionParser(definition);
        MapProvider provider = parser.parse();
        ObjectOutput output = ObjectOutput.from(provider);

        switch (format) {
            case "yaml":
                providerConf.setOutput(output.toYaml().setConfig(outputConfig));
                providerConf.setContentType("text/yaml");
                break;
            case "sql":
                providerConf.setOutput(output.toSQL().setConfig(outputConfig));
                providerConf.setContentType("text/sql");
                break;
            case "csv":
                providerConf.setOutput(output.toCsv().setConfig(outputConfig));
                providerConf.setContentType("text/csv");
                break;
            case "xml":
                providerConf.setOutput(output.toXml().setConfig(outputConfig));
                providerConf.setContentType("text/xml");
                break;
            case "template":
                providerConf.setOutput(output.toTemplate(outputConfig.getOutputTemplate()).setConfig(outputConfig));
                providerConf.setContentType("text/plain");
                break;
            default:
                providerConf.setOutput(output.toJson().setConfig(outputConfig));
                providerConf.setContentType("application/json");
                break;
        }

        providerConf.setProvider(provider);
        providerConf.setDefinition(definition);
        providerConf.setOutputConfig(outputConfig);
        providerConf.setName(name);

        return providerConf;
    }

    public static ProviderConf from(String definition, OutputConfig outputConfig) {
        return from(null, definition, outputConfig);
    }

    public OutputConfig getOutputConfig() {
        return outputConfig;
    }
}
