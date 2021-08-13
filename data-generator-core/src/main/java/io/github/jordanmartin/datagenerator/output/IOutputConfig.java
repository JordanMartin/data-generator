package io.github.jordanmartin.datagenerator.output;

public interface IOutputConfig {

    void setFormat(String format);

    String getFormat();

    void setCount(String Count);

    Integer getCount();

    void setGzip(String Gzip);

    Boolean getGzip();

    void setSingleFile(String SingleFile);

    Boolean getSingleFile();

    void setOutputTemplate(String OutputTemplate);

    String getOutputTemplate();

    void setOutputPretty(String OutputPretty);

    Boolean getOutputPretty();

    void setObjectName(String ObjectName);

    String getObjectName();

    void setTableName(String TableName);

    String getTableName();

    void setSeparator(String Separator);

    String getSeparator();

    void setTemplateFilename(String TemplateFilename);

    String getTemplateFilename();
}
