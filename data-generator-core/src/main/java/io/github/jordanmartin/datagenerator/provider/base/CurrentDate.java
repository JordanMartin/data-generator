package io.github.jordanmartin.datagenerator.provider.base;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

import java.util.Date;

public class CurrentDate implements StatelessValueProvider<Date> {
    @Override
    public Date getOne() {
        return new Date();
    }
}
