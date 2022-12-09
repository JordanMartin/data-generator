package io.github.datagenerator.server.controller;

import io.github.datagenerator.server.service.LatestVersionChecker;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

@Path("/api/version")
@Produces(MediaType.APPLICATION_JSON)
public class AboutController {

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    private final LatestVersionChecker latestVersionChecker;

    public AboutController(LatestVersionChecker latestVersionChecker) {
        this.latestVersionChecker = latestVersionChecker;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response version() {
        Optional<Map<String, String>> latest = latestVersionChecker.getLatestVersion()
                .map(l -> Map.of("version", l.getVersion(), "date", l.getCreated_at()));

        return Response.ok(Map.of(
                "version", version,
                "latest", latest
        )).build();
    }
}
