package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.ProviderDoc;
import io.github.jordanmartin.datagenerator.provider.doc.ProviderDocumentationParser;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class ProviderRegistry {

    private static final String PROVIDER_CORE_SEARCH_PACKAGE = "io.github.jordanmartin.datagenerator.provider";
    private static final String PROVIDER_PLUGINS_SEARCH_PACKAGE = "io.github.datagenerator.plugins";
    private static ProviderRegistry INSTANCE;

    final Map<String, Class<? extends ValueProvider<?>>> providers = new HashMap<>();
    final Map<String, ProviderDoc> providersDoc = new HashMap<>();

    private ProviderRegistry() {
        registerProviders();
    }

    public static ProviderRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProviderRegistry();
        }
        return INSTANCE;
    }

    private void registerProviders() {
        log.info("Search core providers...");
        new Reflections(PROVIDER_CORE_SEARCH_PACKAGE)
                .getTypesAnnotatedWith(Provider.class)
                .stream().filter(aClass -> Modifier.isPublic(aClass.getModifiers()))
                .forEach(this::registerProvider);
        int coreProviderCount = providers.size();
        log.info("{} core providers registered", coreProviderCount);

        log.info("Search addons providers in packages : {}...", PROVIDER_PLUGINS_SEARCH_PACKAGE);
        try {
            new Reflections(PROVIDER_PLUGINS_SEARCH_PACKAGE)
                    .getTypesAnnotatedWith(Provider.class)
                    .stream().filter(aClass -> Modifier.isPublic(aClass.getModifiers()))
                    .forEach(this::registerProvider);
        } catch (ReflectionsException e) {
            // ignore
        }

        log.info("{} addons providers registered", providers.size() - coreProviderCount);
    }

    @SuppressWarnings("unchecked")
    private void registerProvider(Class<?> providerClass) {
        ProviderDoc providerDoc = ProviderDocumentationParser.parse(providerClass).orElse(null);
        if (providerDoc == null) {
            return;
        }

        Optional.ofNullable(providers.get(providerDoc.getName()))
                .ifPresentOrElse(existingProviderClass -> {
                    log.warn("The provider {} is ignored because another provider {} is already registered with the same name : {}",
                            providerClass, existingProviderClass, providerDoc.getName());
                }, () -> {
                    providers.put(providerDoc.getName(), (Class<? extends ValueProvider<?>>) providerClass);
                    providersDoc.put(providerDoc.getName(), providerDoc);
                    log.info("Register provider : {} -> {}", providerDoc.getName(), providerClass);
                });
    }

    public Class<?> get(String providerName) {
        return providers.get(providerName);
    }

    public Set<String> listNames() {
        return providers.keySet();
    }

    public Optional<ProviderDoc> getDoc(String providerName) {
        return Optional.ofNullable(providersDoc.get(providerName));
    }

    public Map<String, ProviderDoc> getAllDoc() {
        return providersDoc;
    }
}
