package io.github.datagenerator.plugins;

public class HttpRequestFailed extends RuntimeException {
    public HttpRequestFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
