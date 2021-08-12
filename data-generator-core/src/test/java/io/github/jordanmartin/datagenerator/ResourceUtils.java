package io.github.jordanmartin.datagenerator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceUtils {

    public static String getResourceFileContent(String fileName) {
        ClassLoader classLoader = ResourceUtils.class.getClassLoader();
        URL url = classLoader.getResource(fileName);
        try {
            Path path = Paths.get(url.toURI());
            return Files.readString(path);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}