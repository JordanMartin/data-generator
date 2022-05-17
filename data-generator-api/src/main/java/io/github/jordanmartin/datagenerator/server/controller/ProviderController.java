package io.github.jordanmartin.datagenerator.server.controller;

import io.github.jordanmartin.datagenerator.definition.DefinitionException;
import io.github.jordanmartin.datagenerator.server.domain.OutputConfig;
import io.github.jordanmartin.datagenerator.server.domain.ProviderConf;
import io.github.jordanmartin.datagenerator.server.domain.ProviderConfDto;
import io.github.jordanmartin.datagenerator.server.repository.ProviderRepository;
import io.github.jordanmartin.datagenerator.server.utils.ProviderDownloadUtil;
import io.github.jordanmartin.datagenerator.server.utils.SleepUtil;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/provider")
@Produces(MediaType.APPLICATION_JSON)
public class ProviderController {

    private final ProviderRepository providerRepository;

    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Provider
    public static class DefinitionExceptionMapper implements ExceptionMapper<DefinitionException> {
        @Override
        public Response toResponse(DefinitionException exception) {
            return Response.status(400)
                    .entity(exception.getMessage())
                    .build();
        }
    }

    @Provider
    public static class GlobalExceptionMapper implements ExceptionMapper<Exception> {
        @Override
        public Response toResponse(Exception exception) {
            return Response.status(500)
                    .entity(exception.getClass().getSimpleName() + " : " + exception.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createProvider(@QueryParam("name") String name,
                                   @QueryParam("format") String format,
                                   @Context UriInfo uriInfo,
                                   String definition) {
        OutputConfig outputConfig = new OutputConfig(Map.of(
                "pretty", "true",
                "format", format
        ));
        providerRepository.createOrUpdate(name, definition, outputConfig);
        URI providerPath = uriInfo.getAbsolutePathBuilder().path(name).build();
        return Response.created(providerPath).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProviderConfDto> listProviders(@Context UriInfo uriInfo) {
        return providerRepository.findAll().stream()
                .map(providerConf -> {
                    ProviderConfDto providerConfDto = new ProviderConfDto();
                    providerConfDto.setName(providerConf.getName());
                    providerConfDto.setTemplate(providerConf.getDefinition());
                    providerConfDto.setFormat(providerConf.getOutputConfig().getFormat());
                    providerConfDto.setHref(
                            uriInfo.getAbsolutePathBuilder().path(providerConf.getName()).build().toString()
                    );
                    return providerConfDto;
                })
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{name}")
    public Response runProvider(@PathParam("name") String name,
                                @QueryParam("count") @DefaultValue("1") int count,
                                @QueryParam("delay") String delay) {
        ProviderConf providerConf = providerRepository.get(name)
                .orElseThrow(NotFoundException::new);

        StreamingOutput streamingOutput = out -> providerConf
                .getOutput()
                .writeMany(out, count);

        SleepUtil.sleep(delay);

        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, providerConf.getContentType())
                .build();
    }

    @DELETE
    @Path("/{name}")
    public Response removeProvider(@PathParam("name") String name) {
        providerRepository.remove(name)
                .orElseThrow(NotFoundException::new);
        return Response.ok().build();
    }

    @POST
    @Path("/live")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response liveProvider(@FormParam("definition") String definition,
                                 @BeanParam OutputConfig outputConfig) {

        ProviderConf providerConf = ProviderConf.from("live", definition, outputConfig);
        StreamingOutput streamingOutput = out -> {
            int count = outputConfig.getInt("count").orElse(1);
            providerConf.getOutput().writeMany(out, count);
        };

        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, providerConf.getContentType())
                .build();
    }

    @POST
    @Path("/download")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response download(@BeanParam OutputConfig outputConfig, @FormParam("definition") String definition) {

        ProviderConf providerConf = ProviderConf.from(definition, outputConfig);

        if (outputConfig.getZip()) {
            if (outputConfig.getCountPerFile() <= 1) {
                return ProviderDownloadUtil.zipOneObjectPerFile(providerConf);
            } else {
                return ProviderDownloadUtil.zipMultipleObjectPerFile(providerConf);
            }
        } else if (outputConfig.getGzip()) {
            return ProviderDownloadUtil.gzipObjects(providerConf);
        } else {
            return ProviderDownloadUtil.singleFile(providerConf);
        }
    }
}
