package io.github.datagenerator.generation.writer;

import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.generation.conf.IOutputConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Classe abstraite pour l'écriture d'objet à partir d'un générateur dans un OutputStream
 */
public abstract class ObjectWriter {

    protected final MapProvider provider;

    public ObjectWriter(MapProvider provider) {
        this.provider = provider;
    }

    /**
     * Generates {@code count} elements from the provider and write to the outputstream
     * Note: the {@code out} stream is not closed
     *
     * @param out   Target output stream
     * @param count Number of object to write to the stream
     * @throws IOException En cas d'erreur d'écriture
     */
    public void writeMany(OutputStream out, int count) throws IOException {
        if (count == 1) {
            writeOne(out, provider.get());
        } else {
            writeMany(out, provider.getStream(count));
        }
    }

    /**
     * Génère {@code count} éléments et les retournes sous forme de String
     *
     * @param count Nombre d'élément à générer
     * @return Les éléments formattés
     * @throws IOException En cas d'erreur de génération
     */
    public String writeToString(int count) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writeMany(baos, count);
            return baos.toString(StandardCharsets.UTF_8);
        }
    }


    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        writeMany(out, Stream.of(object));
    }

    protected abstract void writeMany(OutputStream out, Stream<Map<String, ?>> objects) throws IOException;

    public abstract void configure(IOutputConfig config);
}
