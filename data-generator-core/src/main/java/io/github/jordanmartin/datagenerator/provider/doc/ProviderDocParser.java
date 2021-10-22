package io.github.jordanmartin.datagenerator.provider.doc;

import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderCtor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

public class ProviderDocParser {

    public static ProviderDoc getProviderDoc(Class<?> clazz) {
        Provider provider = Optional.ofNullable(clazz.getAnnotation(Provider.class))
                .orElseThrow(() -> new IllegalArgumentException("La classe " + clazz + " ne possède pas l'annotation " + Provider.class));

        ProviderDoc providerDoc = new ProviderDoc();
        providerDoc.setKey(provider.key());
        providerDoc.setDescription(provider.description());

        Arrays.stream(clazz.getConstructors())
                .filter(ctor -> ctor.isAnnotationPresent(ProviderCtor.class))
                .map(ProviderDocParser::getCtor)
                .forEach(providerDoc.constructors::add);

        return providerDoc;
    }

    private static ProviderCtorDoc getCtor(Constructor<?> ctor) {
        ProviderCtorDoc providerCtorDoc = new ProviderCtorDoc();
        providerCtorDoc.setDescription(ctor.getAnnotation(ProviderCtor.class).value());

        Arrays.stream(ctor.getParameters())
                .filter(param -> param.isAnnotationPresent(ProviderArg.class))
                .map(ProviderDocParser::getArg)
                .forEach(providerCtorDoc.args::add);

        return providerCtorDoc;
    }

    private static ProviderArgDoc getArg(Parameter param) {
        ProviderArg arg = param.getAnnotation(ProviderArg.class);
        return new ProviderArgDoc(arg.name(), arg.description());
    }
}
