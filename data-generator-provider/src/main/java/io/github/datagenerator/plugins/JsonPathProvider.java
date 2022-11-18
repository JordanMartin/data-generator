package io.github.datagenerator.plugins;

import com.jayway.jsonpath.JsonPath;
import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

@Provider(name = "JsonPath", description = "Use a jsonpath expression to extract a value from a json")
public class JsonPathProvider implements ValueProvider<String> {

    private final String jsonPathExpression;
    private final ValueProvider<String> jsonProvider;

    @ProviderCtor
    public JsonPathProvider(String jsonPathExpression, ValueProvider<String> jsonProvider) {
        this.jsonPathExpression = jsonPathExpression;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public String getOneWithContext(IObjectProviderContext ctx) {
        String json = jsonProvider.getOneWithContext(ctx);
        return JsonPath.parse(json).read(jsonPathExpression, String.class);
    }
}