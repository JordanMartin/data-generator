package io.github.jordanmartin.datagenerator.provider.base;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;

import java.util.Date;

@Provider(name = "CurrentDate", description = "Retourne la date courante")
public class CurrentDate implements StatelessValueProvider<Date> {

    @Override
    public Date getOne() {
        return new Date();
    }
}
