package fr.jordanmartin.datagenerator.output;

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
public class YamlWriter implements ObjectWriter {

    private final Yaml yaml;

    public YamlWriter() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        this.yaml = new Yaml(options);
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        out.flush();
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) throws IOException {
        List<Map<String, ?>> list = stream.collect(Collectors.toList());
        yaml.dump(list, new OutputStreamWriter(out));
        out.flush();
    }
}
