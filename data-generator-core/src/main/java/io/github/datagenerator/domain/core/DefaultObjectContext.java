package io.github.datagenerator.domain.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class DefaultObjectContext implements ObjectContext {
    private final DefaultObjectContext parentContext;

    @Setter
    private Map<String, Object> object;

    @Setter
    private Map<String, ValueProvider<?>> refs;

    @Setter
    private Map<String, Object> refsSnapshot;

    @Getter
    @Setter
    private int currentFieldNumber;

    @Getter
    @Setter
    private long objectIndex;

    public DefaultObjectContext() {
        this(null);
    }

    public DefaultObjectContext(DefaultObjectContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFieldValue(String name, Class<T> type) {
        if (!object.containsKey(name)) {
            if (parentContext == null || !parentContext.object.containsKey(name)) {
                return null;
            }
            return (T) parentContext.object.get(name);
        }
        return (T) object.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getRefValue(String name, Class<T> type) {
        if (!refs.containsKey(name)) {
            if (parentContext == null || !parentContext.refs.containsKey(name)) {
                return null;
            }
            ValueProvider<?> provider = parentContext.refs.get(name);
            return (T) provider.get(this);
        }
        ValueProvider<?> provider = refs.get(name);
        return (T) provider.get(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFixedRefValue(String name, Class<Object> type) {
        if (!refsSnapshot.containsKey(name)) {
            if (parentContext == null || !parentContext.refsSnapshot.containsKey(name)) {
                return null;
            }
            return (T) parentContext.refsSnapshot.get(name);
        }
        return (T) refsSnapshot.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T evaluateProvider(Object provider) {
        Object value = provider;
        if (value instanceof ValueProvider<?>) {
            ValueProvider<?> valueProvider = (ValueProvider<?>) value;
            value = valueProvider.get(this);
            return evaluateProvider(value);
        }

        return (T) value;
    }
}
