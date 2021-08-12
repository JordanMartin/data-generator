package io.github.jordanmartin.datagenerator.server.controller.provider;

import io.github.jordanmartin.datagenerator.definition.DefinitionException;
import io.github.jordanmartin.datagenerator.server.domain.ProviderConf;
import io.github.jordanmartin.datagenerator.server.repository.ProviderRepository;
import io.github.jordanmartin.datagenerator.server.utils.SleepUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

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
            StringWriter stacktrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stacktrace));
            return Response.status(500)
                    .entity(stacktrace.toString())
                    .build();
        }
    }

    @POST
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createProvider(@QueryParam("name") String name,
                                   @QueryParam("format") String format,
                                   @Context UriInfo uriInfo,
                                   String template) {
        providerRepository.createOrUpdate(name, template, format, Map.of("pretty", "true"));
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
                    providerConfDto.setTemplate(providerConf.getTemplate());
                    providerConfDto.setFormat(providerConf.getFormat());
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
    public Response liveProvider(@FormParam("output.format") String format,
                                 @FormParam("count") int count,
                                 @FormParam("definition") String definition,
                                 @FormParam("output.template") String outputTemplate,
                                 @FormParam("output.pretty") String outputPretty,
                                 @FormParam("output.object_name") String objectName,
                                 @FormParam("output.table_name") String tableName,
                                 @FormParam("output.separator") String separator) {

        Map<String, String> outputConfig = new HashMap<>();
        outputConfig.put("pretty", outputPretty);
        outputConfig.put("template", outputTemplate);
        outputConfig.put("object_name", objectName);
        outputConfig.put("table_name", tableName);
        outputConfig.put("separator", separator);

        ProviderConf providerConf = ProviderConf.from("live", definition, format, outputConfig);
        StreamingOutput streamingOutput = out -> providerConf.getOutput().writeMany(out, count);
        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, providerConf.getContentType())
                .build();
    }

    @POST
    @Path("/download")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response download(@FormParam("output.format") String format,
                             @FormParam("count") int count,
                             @FormParam("gzip") boolean gzip,
                             @FormParam("definition") String definition,
                             @FormParam("output.template") String outputTemplate,
                             @FormParam("output.pretty") String outputPretty,
                             @FormParam("output.object_name") String objectName,
                             @FormParam("output.table_name") String tableName,
                             @FormParam("output.separator") String separator) {

        Map<String, String> outputConfig = new HashMap<>();
        outputConfig.put("pretty", outputPretty);
        outputConfig.put("template", outputTemplate);
        outputConfig.put("object_name", objectName);
        outputConfig.put("table_name", tableName);
        outputConfig.put("separator", separator);

        ProviderConf providerConf = ProviderConf.from("live", definition, format, outputConfig);
        StreamingOutput streamingOutput = out -> {
            if (gzip) {
                out = new GZIPOutputStream(out);
            }
            providerConf.getOutput().writeMany(out, count);
            out.close();
        };
        String date = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date());
        String filename = count + "__" + date + "." + format;
        String contentType = providerConf.getContentType();
        if (gzip) {
            filename += ".gz";
            contentType = "application/gzip";
        }
        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .build();
    }
}
