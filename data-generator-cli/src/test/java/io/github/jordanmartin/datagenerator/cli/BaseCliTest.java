package io.github.jordanmartin.datagenerator.cli;

import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseCliTest {
    @TempDir
    Path tempDir;

    String resourcePath(String path) throws URISyntaxException {
        URL resource = CliTest.class.getResource(path);
        return new File(resource.toURI()).getPath();
    }

    String getFileContent(File file) throws IOException {
        return Files.readString(Path.of(file.toURI()));
    }

    File getNewTempFile() throws IOException {
        return Files.createTempFile("generated_", ".json").toFile();
    }

    String cli(String... args) throws Exception {
        PrintStream stdout = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try (baos) {
            Cli.main(args);
        }
        System.setOut(stdout);
        return baos.toString();
    }
}
