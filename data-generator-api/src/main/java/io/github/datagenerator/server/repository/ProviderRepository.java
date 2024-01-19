package io.github.datagenerator.server.repository;

import io.github.datagenerator.server.domain.FormOutputConfig;
import io.github.datagenerator.server.domain.ProviderConf;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository {

    ProviderConf createOrUpdate(String name, String template, FormOutputConfig outputConfig);

    Optional<ProviderConf> remove(String name);

    Optional<ProviderConf> get(String name);

    List<ProviderConf> findAll();

    boolean exists(String name);

}
