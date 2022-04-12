package io.github.jordanmartin.datagenerator.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.Startup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@ApplicationScoped
@Startup
public class LatestVersionChecker {

    public static final int ONE_DAY_IN_MS = 86400000;

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    @ConfigProperty(name = "github.latest-release.api")
    String latestReleaseApiUrl;

    @Getter
    private Optional<GithubLatestVersionDTO> latestVersion = Optional.empty();

    @PostConstruct
    void scheduleCheck() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateLatestVersion();
                logIfNotLatestVersion();
            }
        }, 0, ONE_DAY_IN_MS);
    }

    private void logIfNotLatestVersion() {
        String v = latestVersion.map(GithubLatestVersionDTO::getVersion).orElse(version);
        if (!v.equals(version)) {
            log.info("Nouvelle version disponible : {} (version actuelle : {})", v, version);
        }
    }

    private void updateLatestVersion() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(latestReleaseApiUrl)).build();
        try {
            String json = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString())
                    .body();
            latestVersion = Optional.of(new ObjectMapper().readValue(json, GithubLatestVersionDTO.class));
        } catch (Exception ignored) {
            log.warn("Echec de récupération de la dernière version disponible");
        }
    }
}

