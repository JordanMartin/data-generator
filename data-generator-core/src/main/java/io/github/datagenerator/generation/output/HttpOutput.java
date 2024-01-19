package io.github.datagenerator.generation.output;

import io.github.datagenerator.generation.conf.IOutputConfig;
import io.github.datagenerator.generation.writer.CsvWriter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpOutput implements Output {

    private final CsvWriter formatter;
    private String targetUrl;
    private int requestCount;

    public HttpOutput(CsvWriter formatter, IOutputConfig config) {
        this.formatter = formatter;
        configure(config);
    }

    public void configure(IOutputConfig config) {
        this.targetUrl = config.getRequiredString("url");
        this.requestCount = config.getRequiredInteger("request_count");
    }

    @Override
    public void execute() {
        for (int i = 0; i < requestCount; i++) {
            doRequest();
        }
    }

    private void doRequest() {
        try {
            HttpClient http = HttpClient.newHttpClient();
            String body = formatter.writeToString(1);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.targetUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            http.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
