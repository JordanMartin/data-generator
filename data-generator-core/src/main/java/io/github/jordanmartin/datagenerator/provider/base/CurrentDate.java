package io.github.jordanmartin.datagenerator.provider.base;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

import java.util.Date;

@Provider(name = "CurrentDate", description = "Retourne la date courante")
public class CurrentDate implements StatelessValueProvider<Date> {

    @Override
    public Date getOne() {
        return new Date();
    }
}
