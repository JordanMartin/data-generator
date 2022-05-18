package io.github.jordanmartin.datagenerator.provider.base;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

import java.util.Date;

@Provider(
        name = "Now",
        description = "Returns the current date",
        group = "date"
)
public class Now implements StatelessValueProvider<Date> {
    @Override
    public Date getOne() {
        return new Date();
    }
}
