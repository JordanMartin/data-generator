package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;

/**
 * Classe de base pour les formats de sortie des générateurs
 */
public class ObjectOutput {

    /**
     * Le générateur d'objet
     */
    private final ObjectProvider provider;

    /**
     * Constructeur privé. Utiliser {@link #from(ObjectProvider)}
     *
     * @param provider Le générateur d'objet
     */
    protected ObjectOutput(ObjectProvider provider) {
        this.provider = provider;
    }

    public JsonOutput toJson() {
        return new JsonOutput(provider);
    }

    public XmlOutput toXml() {
        return new XmlOutput(provider);
    }

    public TemplateOutput toTemplate(String template) {
        return new TemplateOutput(provider, template);
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

    public <T> PojoOutput<T> toPojo(Class<T> targetClazz) {
        return new PojoOutput<>(provider, targetClazz);
    }

    /**
     * Retourne une instance de l'ObjectOutput avec le générateur spécifié
     *
     * @param provider Le générateur d'objet
     */
    public static ObjectOutput from(ObjectProvider provider) {
        return new ObjectOutput(provider);
    }
}

