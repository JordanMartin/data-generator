package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    public void writeMany(OutputStream out, int count) throws IOException {
        writeMany(out, provider.getStream(count));
    }

    private void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        Yaml yaml = newYaml();
        List<Map<String, ?>> list = stream.collect(Collectors.toList());
        yaml.dump(list, new OutputStreamWriter(out));
        out.flush();
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

    public YamlOutput setPretty(boolean pretty) {
        this.pretty = pretty;
        return this;
    }
}
