package io.github.jordanmartin.datagenerator.provider.object;

import com.github.javafaker.Faker;
import io.github.jordanmartin.datagenerator.provider.base.Constant;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.random.*;
import io.github.jordanmartin.datagenerator.provider.sequence.IntIncrement;
import io.github.jordanmartin.datagenerator.provider.transform.FormatDate;
import io.github.jordanmartin.datagenerator.provider.transform.ListOf;
import io.github.jordanmartin.datagenerator.provider.transform.Repeat;
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
        this(true);
    }

    protected ObjectBuilder(boolean autoConfigure) {
        if (autoConfigure) {
            this.configure();
        }
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

    protected FormatDate formatDate(ValueProvider<Date> dateProvider, String format) {
        return new FormatDate(dateProvider, format);
    }

    protected EnumProvider<Object> randomFromList(Object... list) {
        return new EnumProvider<>(list);
    }

    protected EnumProvider<Object> randomFromList(EnumWeight... list) {
        return new EnumProvider<>(list);
    }

    protected EnumProvider<Object> randomFromList(ValueProvider<?>... list) {
        return new EnumProvider<>(list);
    }

    protected EnumWeight itemWeight(Object item, int weight) {
        return new EnumWeight(item, weight);
    }

    protected RandomFromRegex randomFromRegex(String regex, int count) {
        return new RandomFromRegex(regex, count);
    }

    protected RandomFromRegex randomFromRegex(String regex) {
        return new RandomFromRegex(regex);
    }

    protected IntIncrement increment(int start, int step, int max) {
        return new IntIncrement(start, step, max);
    }

    protected IntIncrement increment() {
        return new IntIncrement();
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

    protected RandomBoolean randomBoolean() {
        return new RandomBoolean();
    }

    protected RandomBoolean randomBoolean(double percentageOfTrue) {
        return new RandomBoolean(percentageOfTrue);
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

    protected FakerExpression sample(String expression) {
        return new FakerExpression(expression);
    }

}
