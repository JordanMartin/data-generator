package fr.jordanmartin.datagenerator.provider.object;

import com.github.javafaker.Faker;
import fr.jordanmartin.datagenerator.provider.base.Constant;
import fr.jordanmartin.datagenerator.provider.core.ValueProvider;
import fr.jordanmartin.datagenerator.provider.random.*;
import fr.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import fr.jordanmartin.datagenerator.provider.transform.FormatDate;
import fr.jordanmartin.datagenerator.provider.transform.ListByRepeat;
import fr.jordanmartin.datagenerator.provider.transform.ListOf;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Locale;

/**
 * Un builder permettant de créer un générateur par une notation DSL
 */
@Slf4j
public abstract class ObjectBuilder extends ObjectProvider {

    protected Faker faker = new Faker(Locale.FRANCE);

    protected ObjectBuilder() {
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

    protected RandomFromList<Object> randomFromList(Object... list) {
        return new RandomFromList<>(list);
    }

    protected RandomFromList<Object> randomFromList(RandomFromList.ItemWeight<?>... list) {
        return new RandomFromList<>(list);
    }

    protected RandomFromList<Object> randomFromList(ValueProvider<?>... list) {
        return new RandomFromList<>(list);
    }

    protected RandomFromList.ItemWeight<Object> itemWeight(Object item, int weight) {
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

    protected <T> ListByRepeat<T> listByRepeat(ValueProvider<T> provider, int count) {
        return new ListByRepeat<>(provider, count);
    }

    protected <T> ListByRepeat<T> listByRepeat(ValueProvider<T> provider, ValueProvider<Integer> count) {
        return new ListByRepeat<>(provider, count);
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

    protected Reference<Object> reference(String ref) {
        return new Reference<>(ref);
    }

    protected FixedReference<Object> fixedReference(String ref) {
        return new FixedReference<>(ref);
    }

    protected Sample sample(String expression) {
        return new Sample(expression);
    }

}
