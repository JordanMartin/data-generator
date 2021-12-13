package io.github.jordanmartin.datagenerator.server.controller;

import io.github.jordanmartin.datagenerator.definition.ProviderRegistry;
import io.github.jordanmartin.datagenerator.provider.doc.ProviderDoc;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/api/provider-doc")
@Produces(MediaType.APPLICATION_JSON)
public class ProviderDocumentationController {

    private final ProviderRegistry defaultProvider = ProviderRegistry.getInstance();

//    @Provider
//    public static class DefinitionExceptionMapper implements ExceptionMapper<DefinitionException> {
//        @Override
//        public Response toResponse(DefinitionException exception) {
//            return Response.status(400)
//                    .entity(exception.getMessage())
//                    .build();
//        }
//    }

//    @Provider
//    public static class GlobalExceptionMapper implements ExceptionMapper<Exception> {
//        @Override
//        public Response toResponse(Exception exception) {
//            StringWriter stacktrace = new StringWriter();
//            exception.printStackTrace(new PrintWriter(stacktrace));
//            return Response.status(500)
//                    .entity(stacktrace.toString())
//                    .build();
//        }
//    }

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
