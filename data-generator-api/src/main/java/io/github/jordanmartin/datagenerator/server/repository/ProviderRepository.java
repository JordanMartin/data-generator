package io.github.jordanmartin.datagenerator.server.repository;

import io.github.jordanmartin.datagenerator.server.domain.OutputConfig;
import io.github.jordanmartin.datagenerator.server.domain.ProviderConf;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository {

    ProviderConf createOrUpdate(String name, String template, OutputConfig outputConfig);

    Optional<ProviderConf> remove(String name);

    Optional<ProviderConf> get(String name);

    List<ProviderConf> findAll();

    boolean exists(String name);

}
