package io.github.datagenerator.domain.providers.base;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.core.StatelessValueProvider;

import java.util.Date;

@Provider(
        name = "Now",
        description = "Returns the current date",
        group = "date"
)
public class Now implements StatelessValueProvider<Date> {

    @Override
    public Date get() {
        return new Date();
    }

}
