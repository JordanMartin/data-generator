package io.github.datagenerator.domain.core;

public interface ObjectContext {
    <T> T getFieldValue(String name, Class<T> type);

    <T> T getRefValue(String name, Class<T> type);

    <T> T getFixedRefValue(String ref, Class<Object> type);

    int getCurrentFieldNumber();

    long getObjectIndex();

    <T> T evaluateProvider(Object provider);
}
