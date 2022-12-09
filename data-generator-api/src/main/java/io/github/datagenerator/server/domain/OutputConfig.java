package io.github.datagenerator.server.domain;


import io.github.datagenerator.output.IOutputConfig;

import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OutputConfig extends HashMap<String, String> implements IOutputConfig {

    public OutputConfig() {
    }

    public OutputConfig(Map<? extends String, ? extends String> source) {
        super(source);
    }

    @FormParam("output.format")
    public void setFormat(String format) {
        put("format", format);
    }

    public String getFormat() {
        return get("format");
    }

    @FormParam("output.count")
    public void setCount(String count) {
        put("count", count);
    }

    public Integer getCount() {
        return getInt("count").orElse(null);
    }

    @FormParam("output.gzip")
    public void setGzip(String gzip) {
        put("gzip", gzip);
    }

    public Boolean getGzip() {
        return getBoolean("gzip").orElse(null);
    }

    @FormParam("output.zip")
    public void setZip(String zip) {
        put("zip", zip);
    }

    @Override
    public Integer getCountPerFile() {
        return getInt("count_per_file").orElse(null);
    }

    @FormParam("output.count_per_file")
    public void setCountPerFile(String countPerFile) {
        put("count_per_file", countPerFile);
    }

    public Boolean getZip() {
        return getBoolean("zip").orElse(null);
    }

    @FormParam("output.template")
    public void setOutputTemplate(String outputTemplate) {
        put("template", outputTemplate);
    }

    public String getOutputTemplate() {
        return get("template");
    }

    @FormParam("output.pretty")
    public void setOutputPretty(String outputPretty) {
        put("pretty", outputPretty);
    }

    public Boolean getOutputPretty() {
        return getBoolean("pretty").orElse(null);
    }

    @FormParam("output.object_name")
    public void setObjectName(String objectName) {
        put("object_name", objectName);
    }

    public String getObjectName() {
        return get("object_name");
    }

    @FormParam("output.table_name")
    public void setTableName(String tableName) {
        put("table_name", tableName);
    }

    public String getTableName() {
        return get("table_name");
    }

    @FormParam("output.separator")
    public void setSeparator(String separator) {
        put("separator", separator);
    }

    public String getSeparator() {
        return get("separator");
    }

    @FormParam("output.filename_template")
    public void setTemplateFilename(String templateFilename) {
        put("filename_template", templateFilename);
    }

    public String getTemplateFilename() {
        return get("filename_template");
    }

    @FormParam("output.include_null")
    public void setIncludeNull(String includeNull) {
        put("include_null", includeNull);
    }

    @Override
    public Boolean getIncludeNull() {
        return getBoolean("include_null").orElse(null);
    }

    public Optional<Integer> getInt(String key) {
        String value = get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(value));
    }

    public Optional<Boolean> getBoolean(String key) {
        String value = get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(Boolean.parseBoolean(value));
    }
}
