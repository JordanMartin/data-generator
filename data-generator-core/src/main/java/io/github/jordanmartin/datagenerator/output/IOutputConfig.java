package io.github.jordanmartin.datagenerator.output;

public interface IOutputConfig {

    void setFormat(String format);

    String getFormat();

    void setCount(String count);

    Integer getCount();

    void setGzip(String gzip);

    Boolean getGzip();

    void setSingleFile(String singleFile);

    Boolean getSingleFile();

    void setOutputTemplate(String outputTemplate);

    String getOutputTemplate();

    void setOutputPretty(String outputPretty);

    Boolean getOutputPretty();

    void setObjectName(String objectName);

    String getObjectName();

    void setTableName(String tableName);

    String getTableName();

    void setSeparator(String separator);

    String getSeparator();

    void setTemplateFilename(String templateFilename);

    String getTemplateFilename();

    void setIncludeNull(String includeNull);

    Boolean getIncludeNull();
}
