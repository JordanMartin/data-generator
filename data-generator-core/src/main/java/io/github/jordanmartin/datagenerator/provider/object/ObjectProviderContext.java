package io.github.jordanmartin.datagenerator.provider.object;


import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

import java.util.Map;

public class ObjectProviderContext implements IObjectProviderContext {
    private final IObjectProviderContext parentCtx;
    private final Map<String, ValueProvider<?>> refProviders;
    private final Map<String, Object> object;
    private final Map<Object, Object> refProvidersSnapshot;

    public ObjectProviderContext(IObjectProviderContext parentCtx, Map<String, ValueProvider<?>> refProviders, Map<String, Object> object, Map<Object, Object> refProvidersSnapshot) {
        this.parentCtx = parentCtx;
        this.refProviders = refProviders;
        this.object = object;
        this.refProvidersSnapshot = refProvidersSnapshot;
    }

    @SuppressWarnings("unchecked")
    @Override

    public <T> T getFieldValue(String fieldName, Class<T> clazz) {
        T ref = (T) object.get(fieldName);
        if (ref == null && parentCtx != null) {
            return parentCtx.getFieldValue(fieldName, clazz);
        } else {
            return ref;
        }
    }

    @SuppressWarnings("unchecked")
    @Override

    public <T> T getRefValue(String refName, Class<T> clazz) {
        ValueProvider<?> refProvider = refProviders.get(refName);
        if (refProvider != null) {
            T ref = (T) refProvider.getOneWithContext(this);
            if (ref != null) {
                return ref;
            }
        }
        if (parentCtx != null) {
            return parentCtx.getRefValue(refName, clazz);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")

    public <T> T getFixedRefValue(String refName, Class<T> clazz) {
        T ref = (T) refProvidersSnapshot.get(refName);
        if (ref != null) {
            return ref;
        }
        if (parentCtx == null) {
            return null;
        }
        return parentCtx.getFixedRefValue(refName, clazz);
    }
}
