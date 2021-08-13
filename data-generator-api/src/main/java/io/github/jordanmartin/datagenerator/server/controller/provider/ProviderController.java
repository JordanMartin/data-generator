package io.github.jordanmartin.datagenerator.server.controller.provider;

import io.github.jordanmartin.datagenerator.definition.DefinitionException;
import io.github.jordanmartin.datagenerator.server.domain.OutputConfig;
import io.github.jordanmartin.datagenerator.server.domain.ProviderConf;
import io.github.jordanmartin.datagenerator.server.repository.ProviderRepository;
import io.github.jordanmartin.datagenerator.server.utils.SleepUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Path("/api/provider")
@Produces(MediaType.APPLICATION_JSON)
public class ProviderController {

    private final ProviderRepository providerRepository;
    private final Pattern FILENAME_TEMPLATE_VAR_PATTERN = Pattern.compile("\\$\\{(.+)}");

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

        if (outputConfig.getSingleFile()) {
            return zipOneObjectPerFile(providerConf);
        } else if (outputConfig.getGzip()) {
            return gzipObjects(providerConf);
        } else {
            return singleFile(providerConf);
        }
    }

    private Response singleFile(ProviderConf providerConf) {
        OutputConfig outputConfig = providerConf.getOutputConfig();
        int count = outputConfig.getCount();
        StreamingOutput streamingOutput = out -> providerConf.getOutput().writeMany(out, count);
        String filename = getFilename(outputConfig);
        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, providerConf.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .build();
    }

    private Response gzipObjects(ProviderConf providerConf) {
        int count = providerConf.getOutputConfig().getCount();
        String filename = getFilename(providerConf.getOutputConfig()) + ".gz";
        StreamingOutput streamingOutput = out -> {
            try (OutputStream gzipOut = new GZIPOutputStream(out)) {
                providerConf.getOutput().writeMany(gzipOut, count);
            }
        };

        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, "application/gzip")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .build();
    }

    private Response zipOneObjectPerFile(ProviderConf providerConf) {
        OutputConfig outputConfig = providerConf.getOutputConfig();
        int count = outputConfig.getCount();
        StreamingOutput streamingOutput = out -> {
            try (ZipOutputStream zos = new ZipOutputStream(out)) {
                for (int i = 0; i < count; i++) {
                    Map<String, ?> object = providerConf.getProvider().getOne();
                    String entryName = getEntryFilename(outputConfig, object, i);
                    zos.putNextEntry(new ZipEntry(entryName));
                    providerConf.getOutput().writeOne(zos, object);
                    zos.closeEntry();
                }
            }
        };
        String filename = getFilename(outputConfig) + ".zip";
        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, "application/zip")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .build();
    }

    private String getFilename(OutputConfig outputConfig) {
        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        return String.format("%d__%s.%s",
                outputConfig.getCount(),
                date,
                outputConfig.getFormat()
        );

    }

    private String getEntryFilename(OutputConfig outputConfig, Map<String, ?> object, int index) {
        String filename = Optional.ofNullable(outputConfig.getTemplateFilename()).orElse("#uuid")
                .replace("#num", Integer.toString(index))
                .replace("#uuid", UUID.randomUUID().toString());

        Matcher matcher = FILENAME_TEMPLATE_VAR_PATTERN.matcher(filename);
        while (matcher.find()) {
            String fieldName = matcher.group(1);
            filename = filename.replace("${" + fieldName + "}", String.valueOf(object.get(fieldName)));
        }
        return filename;
    }
}
