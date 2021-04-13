package fr.jordanmartin.datagenerator;

import com.github.javafaker.Faker;
import fr.jordanmartin.datagenerator.output.JsonWriter;
import fr.jordanmartin.datagenerator.output.ObjectWriter;
import fr.jordanmartin.datagenerator.provider.ListOf;
import fr.jordanmartin.datagenerator.provider.Repeat;
import fr.jordanmartin.datagenerator.provider.ValueProvider;
import fr.jordanmartin.datagenerator.provider.constant.Constant;
import fr.jordanmartin.datagenerator.provider.object.Expression;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import fr.jordanmartin.datagenerator.provider.object.Reference;
import fr.jordanmartin.datagenerator.provider.random.*;
import fr.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import fr.jordanmartin.datagenerator.provider.transformer.FormatDate;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;

/**
 * Un builder permettant de créer un générateur par une notation DSL
 */
@Slf4j
public abstract class ObjectBuilder extends ObjectProvider {


    protected Faker faker = new Faker(Locale.FRANCE);

    public ObjectBuilder() {
        this.configure();
    }

    public abstract void configure();

    protected void setFakerLocale(Locale locale) {
        faker = new Faker(locale);
    }

    protected <T> Constant<T> constant(T constant) {
        return new Constant<>(constant);
    }

    protected RandomDate randomDate(String min, String max) {
        return new RandomDate(min, max);
    }

    protected FormatDate format(ValueProvider<Date> dateProvider, String format) {
        return new FormatDate(dateProvider, format);
    }

    protected RandomFromList<?> randomFromList(Object... list) {
        return new RandomFromList<>(list);
    }

    protected RandomFromList<?> randomFromList(RandomFromList.ItemWeight<?>... list) {
        return new RandomFromList<>(list);
    }

    protected RandomFromList.ItemWeight<?> itemWeight(Object item, int weight) {
        return new RandomFromList.ItemWeight<>(item, weight);
    }

    protected RandomFromRegex randomFromRegex(String regex, int count) {
        return new RandomFromRegex(regex, count);
    }

    protected RandomFromRegex randomFromRegex(String regex) {
        return new RandomFromRegex(regex);
    }

    protected IntAutoIncrement increment(int start, int step, int max) {
        return new IntAutoIncrement(start, step, max);
    }

    protected IntAutoIncrement increment() {
        return new IntAutoIncrement();
    }

    protected <T> Repeat<T> listByRepeat(ValueProvider<T> provider, int count) {
        return new Repeat<>(provider, count);
    }

    protected <T> Repeat<T> listByRepeat(ValueProvider<T> provider, ValueProvider<Integer> count) {
        return new Repeat<>(provider, count);
    }

    protected ListOf list(ValueProvider<?>... providers) {
        return new ListOf(providers);
    }

    protected RandomInt randomInt(int min, int max) {
        return new RandomInt(min, max);
    }

    protected RandomUUID randomUUID() {
        return new RandomUUID();
    }

    protected Expression expression(String expression) {
        return new Expression(expression);
    }

    protected Reference<?> reference(String ref) {
        return new Reference<>(ref);
    }

    public void writeOne(OutputStream output, ObjectWriter objectWriter) throws IOException {
        objectWriter.writeOne(output, getOne());
    }

    public void writeMultiple(int count, OutputStream output, ObjectWriter objectWriter) throws IOException {
        Stream<Map<String, ?>> stream = getStream(count);

        if (log.isInfoEnabled()) {
            AtomicInteger current = new AtomicInteger();
            stream = stream.peek(o -> {
                int percent = (int) (((double) current.incrementAndGet() / count) * 100);
                System.err.printf("\r%d%% Génération de %d/%d objets", percent, current.get(), count);
            });
        }
        objectWriter.writeMany(output, stream);
        if (log.isInfoEnabled()) {
            System.err.println();
        }
    }

    public void toGzipJsonFile(String filepath, int count) throws IOException {
        log.info("Création du fichier \"{}\"", filepath);
        OutputStream out = new GZIPOutputStream(new FileOutputStream(filepath));
        writeMultiple(count, out, new JsonWriter());
    }

    public void toJsonFile(String filepath, int count) throws IOException {
        log.info("Création du fichier \"{}\"", filepath);
        OutputStream out = new FileOutputStream(filepath);
        writeMultiple(count, out, new JsonWriter());
    }
}
