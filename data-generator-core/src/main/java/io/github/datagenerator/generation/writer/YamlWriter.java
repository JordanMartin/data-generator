package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.generation.conf.IOutputConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
public class YamlWriter extends ObjectWriter {

    public static final String CONFIG_PRETTY = "pretty";
    public static final boolean CONFIG_PRETTY_DEFAULT = false;
    public static final String CONFIG_INCLUDES_NULLS = "include_null";
    public static final boolean CONFIG_INCLUDES_NULLS_DEFAULT = true;


    /**
     * Active le pretty print
     */
    private boolean pretty = true;

    /**
     * Détermine si les valeurs null doivent être incluses
     */
    private boolean includeNull;

    public YamlWriter(MapProvider provider) {
        super(provider);
    }


    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        Yaml yaml = newYaml();
        if (!includeNull) {
            stream = stream.map(this::mapWithoutNull);
        }
        OutputStreamWriter writter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        stream.forEach(map -> yaml.dump(List.of(map), writter));
        out.flush();
    }

    @Override
    public void configure(IOutputConfig config) {
        this.pretty = config.getBoolean(CONFIG_PRETTY).orElse(CONFIG_PRETTY_DEFAULT);
        this.includeNull = config.getBoolean(CONFIG_INCLUDES_NULLS).orElse(CONFIG_INCLUDES_NULLS_DEFAULT);
    }

    private Yaml newYaml() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        if (this.pretty) {
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        }
        return new Yaml(options);
    }

    public YamlWriter setPretty(boolean pretty) {
        this.pretty = pretty;
        return this;
    }

    public YamlWriter setIncludeNull(boolean includeNull) {
        this.includeNull = includeNull;
        return this;
    }

    public Map<String, ?> mapWithoutNull(Map<String, ?> map) {
        Map<String, Object> copyMap = new LinkedHashMap<>(map);
        copyMap.entrySet().removeIf(entry -> entry.getValue() == null);
        return copyMap;
    }
}
