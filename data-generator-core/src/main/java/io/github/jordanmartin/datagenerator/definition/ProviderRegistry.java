package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.ProviderDoc;
import io.github.jordanmartin.datagenerator.provider.doc.ProviderDocumentationParser;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class ProviderRegistry {

    private static ProviderRegistry INSTANCE;

    final Map<String, Class<? extends ValueProvider<?>>> providers = new HashMap<>();
    final Map<String, ProviderDoc> providersDoc = new HashMap<>();
    private static final String PROVIDER_SEARCH_PACKAGE = "io.github.jordanmartin.datagenerator.provider";

    private ProviderRegistry() {
        registerDefaultProviders();
    }

    public static ProviderRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProviderRegistry();
        }
        return INSTANCE;
    }

    private void registerDefaultProviders() {
        log.info("Recherche des générateur dans le package : {}", PROVIDER_SEARCH_PACKAGE);
        new Reflections(PROVIDER_SEARCH_PACKAGE)
                .getTypesAnnotatedWith(Provider.class)
                .stream().filter(aClass -> Modifier.isPublic(aClass.getModifiers()))
                .forEach(this::registerProvider);
        log.info("{} générateurs enregistrés : {}", providers.size(), providers.entrySet());
    }

    @SuppressWarnings("unchecked")
    private void registerProvider(Class<?> providerClass) {
        ProviderDoc providerDoc = ProviderDocumentationParser.parse(providerClass).orElse(null);
        if (providerDoc == null) {
            return;
        }

        Optional.ofNullable(providers.get(providerDoc.getName()))
                .ifPresentOrElse(existingProviderClass -> {
                    log.warn("Le generateur {} est ignoré car le générateur {} est déjà enregistré sous le même nom : {}",
                            providerClass, existingProviderClass, providerDoc.getName());
                }, () -> {
                    providers.put(providerDoc.getName(), (Class<? extends ValueProvider<?>>) providerClass);
                    providersDoc.put(providerDoc.getName(), providerDoc);
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
