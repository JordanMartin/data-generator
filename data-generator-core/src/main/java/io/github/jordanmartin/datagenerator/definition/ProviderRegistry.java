package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.ProviderDoc;
import io.github.jordanmartin.datagenerator.provider.doc.ProviderDocParser;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;
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

//        registerProvider(Constant.class);
//        registerProvider(CurrentDate.class);
//        registerProvider(RandomDate.class);
//        registerProvider(RandomBoolean.class);
//        registerProvider(RandomFromList.class);
//        registerProvider(RandomFromRegex.class);
//        registerProvider(RandomInt.class);
//        registerProvider(RandomDouble.class);
//        registerProvider(Round.class);
//        registerProvider(RandomUUID.class);
//        registerProvider(IntAutoIncrement.class);
//        registerProvider(SequenceFromList.class);
//        registerProvider(AsString.class);
//        registerProvider(FormatDate.class);
//        registerProvider(Idempotent.class);
//        registerProvider(ListOf.class);
//        registerProvider(ListByRepeat.class);
//        registerProvider(Sample.class);
//
//        // FIXME
//        registerProvider("ItemWeight", RandomFromList.ItemWeight.class);
//
//        registerProvider(Expression.class);
//        registerProvider(Reference.class);
//        registerProvider(FixedReference.class);
    }

    @SuppressWarnings("unchecked")
    private void registerProvider(Class<?> providerClass) {
        if (!ValueProvider.class.isAssignableFrom(providerClass)) {
            log.warn("Le generateur {} est ignoré car il n'implémente pas {}", providerClass, ValueProvider.class);
            return;
        }

        ProviderDoc providerDoc = ProviderDocParser.parse(providerClass).orElse(null);
        if (providerDoc == null) {
            return;
        }

        Optional.ofNullable(providers.get(providerDoc.getName()))
                .ifPresentOrElse(existingProviderClass -> {
                    log.warn("Le generateur {} est ignoré car le générateur {} enregistré sous le même nom : {}",
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
