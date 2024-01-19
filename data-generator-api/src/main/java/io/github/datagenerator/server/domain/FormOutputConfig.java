package io.github.datagenerator.server.domain;


import io.github.datagenerator.generation.conf.OutputConfig;

import javax.ws.rs.FormParam;
import java.util.Map;

public class FormOutputConfig extends OutputConfig {

    public FormOutputConfig() {
    }

    public FormOutputConfig(Map<? extends String, ? extends String> source) {
        for (var entry : source.entrySet()) {
            setParam(entry.getKey(), entry.getValue());
        }
    }

    @FormParam("output.format")
    public void setFormat(String format) {
        setParam("format", format);
    }


    @FormParam("output.count")
    public void setCount(String count) {
        setParam("count", count);
    }


    @FormParam("output.gzip")
    public void setGzip(String gzip) {
        setParam("gzip", gzip);
    }


    @FormParam("output.zip")
    public void setZip(String zip) {
        setParam("zip", zip);
    }

    @FormParam("output.count_per_file")
    public void setCountPerFile(String countPerFile) {
        setParam("count_per_file", countPerFile);
    }


    @FormParam("output.template")
    public void setOutputTemplate(String outputTemplate) {
        setParam("template", outputTemplate);
    }


    @FormParam("output.pretty")
    public void setOutputPretty(String outputPretty) {
        setParam("pretty", outputPretty);
    }


    @FormParam("output.object_name")
    public void setObjectName(String objectName) {
        setParam("object_name", objectName);
    }


    @FormParam("output.table_name")
    public void setTableName(String tableName) {
        setParam("table_name", tableName);
    }


    @FormParam("output.separator")
    public void setSeparator(String separator) {
        setParam("separator", separator);
    }


    @FormParam("output.filename_template")
    public void setTemplateFilename(String templateFilename) {
        setParam("filename_template", templateFilename);
    }


    @FormParam("output.include_null")
    public void setIncludeNull(String includeNull) {
        setParam("include_null", includeNull);
    }

}
