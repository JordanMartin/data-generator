package io.github.datagenerator.plugins;

import com.jayway.jsonpath.JsonPath;
import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;

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
    public String get(ObjectContext ctx) {
        String json = jsonProvider.get(ctx);
        return JsonPath.parse(json).read(jsonPathExpression, String.class);
    }
}