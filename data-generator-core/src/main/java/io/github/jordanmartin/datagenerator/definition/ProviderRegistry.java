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
        String pluginsProviderSearchPackage = Optional.ofNullable(System.getenv("PROVIDER_PLUGINS_SEARCH_PACKAGE"))
                .orElse(PROVIDER_PLUGINS_SEARCH_PACKAGE);

        log.info("Search providers in packages : {}, {}",
                PROVIDER_CORE_SEARCH_PACKAGE, pluginsProviderSearchPackage);
        new Reflections(PROVIDER_CORE_SEARCH_PACKAGE, pluginsProviderSearchPackage)
                .getTypesAnnotatedWith(Provider.class)
                .stream().filter(aClass -> Modifier.isPublic(aClass.getModifiers()))
                .forEach(this::registerProvider);
        log.info("{} providers registered : {}", providers.size(), providers.entrySet());
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
