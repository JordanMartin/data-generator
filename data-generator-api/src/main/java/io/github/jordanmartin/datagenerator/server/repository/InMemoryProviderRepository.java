package io.github.jordanmartin.datagenerator.server.repository;

import io.github.jordanmartin.datagenerator.server.domain.ProviderConf;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

/**
 * Sauvegarde des générateur et de leur configuration en mémoire
 */
@ApplicationScoped
public class InMemoryProviderRepository implements ProviderRepository {

    private final Map<String, ProviderConf> map = new HashMap<>();

    @Override
    public ProviderConf createOrUpdate(String name, String template, String format, Map<String, String> outputConfig) {
        return map.put(name, ProviderConf.from(name, template, format, outputConfig));
    }

    @Override
    public Optional<ProviderConf> remove(String name) {
        return Optional.ofNullable(map.remove(name));
    }

    @Override
    public Optional<ProviderConf> get(String name) {
        return Optional.ofNullable(map.get(name));
    }

    @Override
    public List<ProviderConf> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public boolean exists(String name) {
        return map.containsKey(name);
    }

}
