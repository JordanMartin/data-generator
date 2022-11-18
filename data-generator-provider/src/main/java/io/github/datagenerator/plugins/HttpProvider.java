package io.github.datagenerator.plugins;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

@Provider(name = "Http", description = "Get result from HTTP endpoint")
public class HttpProvider implements ValueProvider<String> {

    private final HttpClient httpClient;
    private final ValueProvider<String> urlProvider;

    @ProviderCtor
    public HttpProvider(ValueProvider<String> urlProvider) {
        this.urlProvider = urlProvider;
        httpClient = HttpClient.newHttpClient();
    }

    @Override
    public String getOneWithContext(IObjectProviderContext ctx) {
        String url = this.urlProvider.getOneWithContext(ctx);
        return httpGet(url);
    }

    private String httpGet(String url) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, ofString());
            return response.body();
        } catch (Exception e) {
            throw new HttpRequestFailed("Failed to request " + urlProvider, e);
        }
    }
}