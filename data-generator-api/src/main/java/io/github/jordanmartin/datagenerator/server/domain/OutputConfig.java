package io.github.jordanmartin.datagenerator.server.domain;

import io.github.jordanmartin.datagenerator.output.IOutputConfig;

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
    public void setCount(String Count) {
        put("count", Count);
    }

    public Integer getCount() {
        return getInt("count").orElse(null);
    }

    @FormParam("output.gzip")
    public void setGzip(String Gzip) {
        put("gzip", Gzip);
    }

    public Boolean getGzip() {
        return getBoolean("gzip").orElse(null);
    }

    @FormParam("output.single_file")
    public void setSingleFile(String SingleFile) {
        put("single_file", SingleFile);
    }

    public Boolean getSingleFile() {
        return getBoolean("single_file").orElse(null);
    }

    @FormParam("output.template")
    public void setOutputTemplate(String OutputTemplate) {
        put("template", OutputTemplate);
    }

    public String getOutputTemplate() {
        return get("template");
    }

    @FormParam("output.pretty")
    public void setOutputPretty(String OutputPretty) {
        put("pretty", OutputPretty);
    }

    public Boolean getOutputPretty() {
        return getBoolean("pretty").orElse(null);
    }

    @FormParam("output.object_name")
    public void setObjectName(String ObjectName) {
        put("object_name", ObjectName);
    }

    public String getObjectName() {
        return get("object_name");
    }

    @FormParam("output.table_name")
    public void setTableName(String TableName) {
        put("table_name", TableName);
    }

    public String getTableName() {
        return get("table_name");
    }

    @FormParam("output.separator")
    public void setSeparator(String Separator) {
        put("separator", Separator);
    }

    public String getSeparator() {
        return get("separator");
    }

    @FormParam("output.filename_template")
    public void setTemplateFilename(String TemplateFilename) {
        put("filename_template", TemplateFilename);
    }

    public String getTemplateFilename() {
        return get("filename_template");
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
