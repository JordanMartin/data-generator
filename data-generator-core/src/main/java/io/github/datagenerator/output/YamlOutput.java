package io.github.datagenerator.output;

import io.github.datagenerator.domain.core.MapProvider;
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
public class YamlOutput extends ObjectWriterOuput {

    /**
     * Active le pretty print
     */
    private boolean pretty = true;

    /**
     * Détermine si les valeurs null doivent être incluses
     */
    private boolean includeNull;

    public YamlOutput(MapProvider provider) {
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
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        setPretty(outputConfig.getOutputPretty());
        setIncludeNull(outputConfig.getIncludeNull());
        return this;
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

    public YamlOutput setPretty(Boolean pretty) {
        this.pretty = pretty != null && pretty;
        return this;
    }

    public YamlOutput setIncludeNull(Boolean includeNull) {
        if (includeNull != null) {
            this.includeNull = includeNull;
        }
        return this;
    }

    public Map<String, ?> mapWithoutNull(Map<String, ?> map) {
        Map<String, Object> copyMap = new LinkedHashMap<>(map);
        copyMap.entrySet().removeIf(entry -> entry.getValue() == null);
        return copyMap;
    }
}
