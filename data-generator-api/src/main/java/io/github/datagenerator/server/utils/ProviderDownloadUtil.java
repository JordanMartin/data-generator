package io.github.datagenerator.server.utils;

import io.github.datagenerator.server.domain.FormOutputConfig;
import io.github.datagenerator.server.domain.ProviderConf;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ProviderDownloadUtil {

    private final static Pattern FILENAME_TEMPLATE_VAR_PATTERN = Pattern.compile("\\$\\{([a-zA-Z-_ ]+)}");

    /**
     * Génène un unique fichier unique avec l'ensemble des objets
     *
     * @param providerConf La configuration du générateur
     */
    public static Response singleFile(ProviderConf providerConf) {
        FormOutputConfig config = providerConf.getOutputConfig();
        int count = config.getRequiredInteger("count");
        StreamingOutput streamingOutput = out -> providerConf.getOutput().writeMany(out, count);
        String filename = getFilename(config);
        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, providerConf.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .build();
    }

    public static Response gzipObjects(ProviderConf config) {
        int count = config.getOutputConfig().getRequiredInteger("count");
        String filename = getFilename(config.getOutputConfig()) + ".gz";
        StreamingOutput streamingOutput = out -> {
            try (OutputStream gzipOut = new GZIPOutputStream(out)) {
                config.getOutput().writeMany(gzipOut, count);
            }
        };

        return Response
                .ok(streamingOutput)
                .header(HttpHeaders.CONTENT_TYPE, "application/gzip")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .build();
    }

    /**
     * Génère un ZIP avec un objet par fichier
     *
     * @param config La configuration du générateur
     */
    public static Response zipMultipleObjectPerFile(ProviderConf config) {
        FormOutputConfig outputConfig = config.getOutputConfig();
        StreamingOutput streamingOutput = out -> {
            int countPerFile = outputConfig.getRequiredInteger("count_per_file");
            int count = outputConfig.getRequiredInteger("count");
            try (ZipOutputStream zos = new ZipOutputStream(out)) {
                int fileNum = 0;
                while (count > 0) {
                    int numberToWrite = Math.min(count, countPerFile);
                    String templateFilename = outputConfig.getRequiredString("filename_template");
                    String entryName = Optional.ofNullable(templateFilename).orElse("#uuid")
                            .replace("#num", Integer.toString(fileNum++))
                            .replace("#uuid", UUID.randomUUID().toString());
                    zos.putNextEntry(new ZipEntry(entryName));
                    config.getOutput().writeMany(zos, numberToWrite);
                    zos.closeEntry();
                    count -= countPerFile;
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

    /**
     * Génère un ZIP avec un objet par fichier
     *
     * @param providerConf La configuration du générateur
     */
    public static Response zipOneObjectPerFile(ProviderConf providerConf) {
        FormOutputConfig outputConfig = providerConf.getOutputConfig();
        int count = outputConfig.getRequiredInteger("count");
        StreamingOutput streamingOutput = out -> {
            try (ZipOutputStream zos = new ZipOutputStream(out)) {
                for (int i = 0; i < count; i++) {
                    Map<String, ?> object = providerConf.getProvider().get();
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

    /**
     * Calcule le nom du fichier à partir de la date courante et de la configuration du format de sortie
     *
     * @param config La configuration du générateur
     */
    static String getFilename(FormOutputConfig config) {
        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        return String.format("%d__%s.%s",
                config.getRequiredInteger("count"),
                date,
                config.getRequiredString("format")
        );

    }

    /**
     * Retourne le nom du fichier à insérer dans le zip en fonction de l'expression du nom
     *
     * @param config La configuration du générateur
     * @param object       Les champs de l'objet
     * @param index        Index du fichier génré
     */
    static String getEntryFilename(FormOutputConfig config, Map<String, ?> object, int index) {
        String templateFilename = config.getRequiredString("filename_template");
        String filename = Optional.ofNullable(templateFilename).orElse("#uuid")
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
