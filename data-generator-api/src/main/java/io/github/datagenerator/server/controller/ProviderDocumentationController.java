package io.github.datagenerator.server.controller;


import io.github.datagenerator.domain.definition.ProviderRegistry;
import io.github.datagenerator.domain.doc.ProviderDoc;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/api/provider-doc")
@Produces(MediaType.APPLICATION_JSON)
public class ProviderDocumentationController {

    private final ProviderRegistry defaultProvider = ProviderRegistry.getInstance();

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, ProviderDoc> getAllProviderDoc() {
        return defaultProvider.getAllDoc();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProviderDoc getProviderDoc(@PathParam("name") String name) {
        return defaultProvider.getDoc(name)
                .orElseThrow(NotFoundException::new);
    }
}
