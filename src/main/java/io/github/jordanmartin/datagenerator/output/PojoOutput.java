package io.github.jordanmartin.datagenerator.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Génère des objets POJO
 */
public class PojoOutput<T> {

    /**
     * Le générateur d'objet
     */
    protected final ObjectProvider provider;
    private final Class<T> targetClass;

    protected PojoOutput(ObjectProvider provider, Class<T> targetClass) {
        this.provider = provider;
        this.targetClass = targetClass;
    }

    public Stream<T> getStream(int count) {
        ObjectMapper objectMapper = new ObjectMapper();
        return provider.getStream(count)
                .map(map -> objectMapper.convertValue(map, targetClass));
    }

    public List<T> getMany(int count) {
        return getStream(count).collect(Collectors.toList());
    }

    public T getOne() {
        return getStream(1).findFirst().get();
    }
}
