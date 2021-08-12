package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Classe abstraite pour l'écriture d'objet à partir d'un générateur dans un OutputStream
 */
public abstract class ObjectWriterOuput {

    /**
     * Le générateur d'objet
     */
    protected final ObjectProvider provider;

    protected ObjectWriterOuput(ObjectProvider provider) {
        this.provider = provider;
    }

    /**
     * Génère un élément à partir du générateur et les écrit  dans l'OutputStream
     * Note: le stream de sortie n'es pas fermé après l'écriture
     *
     * @param out Le stream de destination
     * @throws IOException En cas d'erreur d'écriture
     */
    public void writeOne(OutputStream out) throws IOException {
        writeMany(out, 1);
    }

    /**
     * Génère {@code count} éléments à partir du générateur et les écrit  dans l'OutputStream
     * Note: le stream de sortie n'es pas fermé après l'écriture
     *
     * @param out   Le stream de destination
     * @param count Nombre d'élément à ecrire
     * @throws IOException En cas d'erreur d'écriture
     */
    public abstract void writeMany(OutputStream out, int count) throws IOException;

    /**
     * Génère {@code count} éléments et les retournes sous forme de String
     *
     * @param count Nombre d'élément à générer
     * @return Les éléments formattés
     * @throws IOException En cas d'erreur de génération
     */
    public String manyToString(int count) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writeMany(baos, count);
            return baos.toString(StandardCharsets.UTF_8);
        }
    }

    /**
     * Génère 1 élément et les retourne sous forme de String
     *
     * @return Les éléments formattés
     * @throws IOException En cas d'erreur de génération
     */
    public String oneToString() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writeOne(baos);
            return baos.toString(StandardCharsets.UTF_8);
        }
    }
}
