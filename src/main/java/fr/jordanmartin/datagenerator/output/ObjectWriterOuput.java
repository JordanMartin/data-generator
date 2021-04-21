package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Classe abstraite pour l'écriture d'objet à partir d'un générateur dans un OutputStream
 */
public abstract class ObjectWriterOuput {

    protected final ObjectProvider provider;

    protected ObjectWriterOuput(ObjectProvider provider) {
        this.provider = provider;
    }

    public void writeOne(OutputStream out) throws IOException {
        writeMany(out, 1);
    }

    public abstract void writeMany(OutputStream out, int count) throws IOException;

    public String writeManyToString(int count) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writeMany(baos, count);
            return baos.toString();
        }
    }

    public String writeOneToString() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writeOne(baos);
            return baos.toString();
        }
    }
}
