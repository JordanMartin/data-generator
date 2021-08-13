package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Génère les données au format JSON
 */
public class YamlOutput extends ObjectWriterOuput {

    /**
     * Active le pretty print
     */
    private boolean pretty = true;

    public YamlOutput(ObjectProvider provider) {
        super(provider);
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        Yaml yaml = newYaml();
        List<Map<String, ?>> list = stream.collect(Collectors.toList());
        yaml.dump(list, new OutputStreamWriter(out, StandardCharsets.UTF_8));
        out.flush();
    }

    @Override
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        setPretty(outputConfig.getOutputPretty());
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
}
