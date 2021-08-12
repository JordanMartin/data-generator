package io.github.jordanmartin.datagenerator.server.domain;

import io.github.jordanmartin.datagenerator.definition.YamlDefinitionParser;
import io.github.jordanmartin.datagenerator.output.ObjectOutput;
import io.github.jordanmartin.datagenerator.output.ObjectWriterOuput;
import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProviderConf {
    private String name;
    private String template;
    private String format;
    private ObjectProvider provider;
    private ObjectWriterOuput output;
    private String contentType;

    public static ProviderConf from(String name, String template, String format, Map<String, String> outputConfig) {
        ProviderConf providerConf = new ProviderConf();
        YamlDefinitionParser parser = new YamlDefinitionParser(template);
        ObjectProvider provider = parser.parse();
        ObjectOutput output = ObjectOutput.from(provider);

        boolean pretty = "true".equals(outputConfig.get("pretty"));
        String objectName = outputConfig.get("object_name");

        switch (format) {
            case "yaml":
                providerConf.setOutput(output.toYaml().setPretty(pretty));
                providerConf.setContentType("text/yaml");
                break;
            case "sql":
                providerConf.setOutput(output.toSQL().setTableName(objectName));
                providerConf.setContentType("text/sql");
                break;
            case "csv":
                providerConf.setOutput(output.toCsv());
                providerConf.setContentType("text/csv");
                break;
            case "xml":
                providerConf.setOutput(output.toXml().setPretty(pretty).setObjectName(objectName));
                providerConf.setContentType("text/xml");
                break;
            case "template":
                providerConf.setOutput(output.toTemplate(outputConfig.get("template")));
                providerConf.setContentType("text/plain");
                break;
            default:
                providerConf.setOutput(output.toJson().setPretty(pretty));
                providerConf.setContentType("application/json");
                break;
        }
        
        providerConf.setProvider(provider);
        providerConf.setTemplate(template);
        providerConf.setFormat(format);
        providerConf.setName(name);

        return providerConf;
    }
}
