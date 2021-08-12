package io.github.jordanmartin.datagenerator.cli;

import io.github.jordanmartin.datagenerator.definition.YamlDefinitionParser;
import io.github.jordanmartin.datagenerator.output.CsvOutput;
import io.github.jordanmartin.datagenerator.output.ObjectOutput;
import io.github.jordanmartin.datagenerator.output.ObjectWriterOuput;
import io.github.jordanmartin.datagenerator.output.SqlOutput;
import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.apache.commons.cli.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

public class Cli {

    private final Options options;
    private CommandLine cmd;

    private final String SEPARATOR = "separator";
    private final String XML_TABLE_NAME = "table-name";
    private final String PRETTY = "pretty";
    private final String DEFINITION = "d";
    private final String STDOUT = "stdout";
    private final String GZIP = "gzip";
    private final String COUNT = "c";
    private final String FORMAT = "f";
    private final String OUT = "o";

    public Cli() {
        this.options = new Options();
        options.addOption(Option.builder(OUT).longOpt("out")
                .type(String.class)
                .desc("Fichier de sortie")
                .hasArg(true)
                .build());
        options.addOption(Option.builder().longOpt(STDOUT)
                .desc("Utilise la sortie standard")
                .hasArg(false)
                .build());
        options.addOption(Option.builder(DEFINITION).longOpt("definition")
                .type(String.class)
                .desc("Fichier de defintion")
                .hasArg(true)
                .required()
                .build());
        options.addOption(Option.builder(COUNT).longOpt("count")
                .type(Integer.class)
                .desc("Nombre d'objet à générer")
                .hasArg(true)
                .required()
                .build());
        options.addOption(Option.builder(FORMAT).longOpt("format")
                .type(String.class)
                .desc("Format de sortie : yaml, json, csv, sql, xml")
                .hasArg(true)
                .required()
                .build());
        options.addOption(Option.builder().longOpt(XML_TABLE_NAME)
                .type(String.class)
                .desc("Nom de la table SQL à utilier pour les requêtes insert")
                .hasArg(true)
                .build());
        options.addOption(Option.builder().longOpt(SEPARATOR)
                .type(String.class)
                .desc("Séparateur à utiliser pour le format CSV")
                .hasArg(true)
                .build());
        options.addOption(Option.builder().longOpt(PRETTY)
                .desc("Active le mode pretty pour la sortie JSON ou YAML")
                .hasArg(false)
                .build());
        options.addOption(Option.builder().longOpt(GZIP)
                .desc("Compresse la sortie en GZIP")
                .hasArg(false)
                .build());
    }

    public static void main(String[] args) throws IOException {
        new Cli().parse(args);
    }

    private void parse(String[] args) throws IOException {

        try {
            this.cmd = new DefaultParser().parse(options, args);

            if (cmd.hasOption("h")) {
                printHelp();
                return;
            }

            int count = getCount();
            ObjectWriterOuput writer = getWriter();
            try (OutputStream out = getOut()) {
                writer.writeMany(out, count);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            printHelp();
        }
    }

    private OutputStream getOut() throws IOException, MissingOptionException {
        OutputStream out = null;
        if (cmd.hasOption(OUT)) {
            out = new FileOutputStream(cmd.getOptionValue(OUT));
        } else if (cmd.hasOption(STDOUT)) {
            out = System.out;
        }

        if (out == null) {
            throw new MissingOptionException("--stdout ou -o doit être spécifié");
        }

        if (cmd.hasOption(GZIP)) {
            out = new GZIPOutputStream(out);
        }

        return out;
    }

    private ObjectWriterOuput getWriter() throws IOException {
        ObjectProvider provider = getProvider();
        boolean pretty = cmd.hasOption(PRETTY);
        switch (cmd.getOptionValue(FORMAT).toLowerCase()) {
            case "yaml":
                return ObjectOutput.from(provider)
                        .toYaml().setPretty(pretty);
            case "csv":
                CsvOutput csvOutput = ObjectOutput.from(provider).toCsv();
                if (cmd.hasOption(SEPARATOR)) {
                    csvOutput.setSeparator(cmd.getOptionValue(SEPARATOR));
                }
                return csvOutput;
            case "json":
                return ObjectOutput.from(provider).toJson()
                        .setPretty(pretty);
            case "sql":
                SqlOutput sqlOutput = ObjectOutput.from(provider).toSQL();
                if (cmd.hasOption(XML_TABLE_NAME)) {
                    sqlOutput.setTableName(cmd.getOptionValue(XML_TABLE_NAME));
                }
                return sqlOutput;
            case "xml":
                return ObjectOutput.from(provider).toXml()
                        .setPretty(true);
            default:
                throw new IllegalArgumentException("Ce format n'existe pas");
        }
    }

    private int getCount() {
        return Integer.parseInt(cmd.getOptionValue("c"));
    }

    private ObjectProvider getProvider() throws IOException {
        String file = cmd.getOptionValue(DEFINITION);
        String def = Files.readString(Paths.get(file));
        return new YamlDefinitionParser(def).parse();
    }

    public void printHelp() {
        new HelpFormatter().printHelp("generator", options);
    }
}
