package io.github.jordanmartin.datagenerator.server.controller;

import io.github.jordanmartin.datagenerator.definition.DefinitionException;
import io.github.jordanmartin.datagenerator.server.domain.OutputConfig;
import io.github.jordanmartin.datagenerator.server.domain.ProviderConf;
import io.github.jordanmartin.datagenerator.server.domain.ProviderConfDto;
import io.github.jordanmartin.datagenerator.server.repository.ProviderRepository;
import io.github.jordanmartin.datagenerator.server.service.ProviderDownloadUtil;
import io.github.jordanmartin.datagenerator.server.utils.SleepUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/version")
@Produces(MediaType.APPLICATION_JSON)
public class AboutController {

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response version() {
        return Response.ok(Map.of("version", version)).build();
    }
}
