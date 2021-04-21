package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;

public class ObjectOuput {

    private final ObjectProvider provider;

    protected ObjectOuput(ObjectProvider provider) {
        this.provider = provider;
    }

    public JsonOutput toJson() {
        return new JsonOutput(provider);
    }

    public XmlOutput toXml() {
        return new XmlOutput(provider);
    }

    public YamlOutput toYaml() {
        return new YamlOutput(provider);
    }

    public CsvOutput toCsv() {
        return new CsvOutput(provider);
    }

    public SqlOutput toSQL() {
        return new SqlOutput(provider);
    }

    public static ObjectOuput from(ObjectProvider provider) {
        return new ObjectOuput(provider);
    }
}

