package fr.jordanmartin.datagenerator.provider.base;

import fr.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

import java.util.Date;

public class CurrentDate implements StatelessValueProvider<Date> {
    @Override
    public Date getOne() {
        return new Date();
    }
}
