package io.github.datagenerator.generation.writer;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.generation.conf.IOutputConfig;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format XML
 */
public class XmlWriter extends ObjectWriter {

    public static final String ROOT_TAG_NAME = "data";
    public static final String CONFIG_OBJECT_NAME = "object_name";
    private static final String CONFIG_OBJECT_NAME_DEFAULT = "my_object";
    public static final String CONFIG_PRETTY = "pretty";
    public static final boolean CONFIG_PRETTY_DEFAULT = false;
    public static final String CONFIG_INCLUDES_NULLS = "include_null";
    public static final boolean CONFIG_INCLUDES_NULLS_DEFAULT = true;

    private boolean pretty = CONFIG_PRETTY_DEFAULT;
    private String objectName = CONFIG_OBJECT_NAME_DEFAULT;
    private boolean includeNull = CONFIG_INCLUDES_NULLS_DEFAULT;

    public XmlWriter(MapProvider provider) {
        super(provider);
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws WriterException {
        writeXml(out, Stream.of(object), false);
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) {
        writeXml(out, stream, true);
    }

    private void writeXml(OutputStream out, Stream<Map<String, ?>> stream, boolean addRootTag) {
        try {
            XMLStreamWriter writer = XMLOutputFactory.newInstance()
                    .createXMLStreamWriter(out, "UTF-8");

            if (pretty) {
                writer = new IndentingXMLStreamWriter(writer);
            }

            writer.writeStartDocument("UTF-8", "1.0");
            if (addRootTag) {
                writer.writeStartElement(ROOT_TAG_NAME);
            }

            XMLStreamWriter finalWriter = writer;
            stream.forEach(object -> {
                try {
                    finalWriter.writeStartElement(objectName);
                    appendChild(finalWriter, object);
                    finalWriter.writeEndElement();
                } catch (XMLStreamException e) {
                    throw new WriterException(e);
                }
            });

            if (addRootTag) {
                finalWriter.writeEndElement();
            }
            finalWriter.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new WriterException(e);
        }
    }

    @Override
    public void configure(IOutputConfig config) {
        setPretty(config.getBoolean(CONFIG_PRETTY).orElse(CONFIG_PRETTY_DEFAULT));
        setObjectName(config.getString(CONFIG_OBJECT_NAME).orElse(CONFIG_OBJECT_NAME_DEFAULT));
        setIncludeNull(config.getBoolean(CONFIG_INCLUDES_NULLS).orElse(CONFIG_INCLUDES_NULLS_DEFAULT));
    }

    @SuppressWarnings("unchecked")
    private void appendChild(XMLStreamWriter writer, Object object) throws XMLStreamException {
        if (object instanceof Map) {
            for (Map.Entry<String, ?> entry : ((Map<String, ?>) object).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (!includeNull && value == null) {
                    continue;
                }

                // Si le champ commence par "@", il est ajouté en tant qu'attribut
                if (key.startsWith("@")) {
                    writer.writeAttribute(key.substring(1), String.valueOf(value));
                } else {
                    writer.writeStartElement(key);
                    appendChild(writer, value);
                    writer.writeEndElement();
                }
            }
        } else if (object instanceof Iterable) {
            for (Object o : ((List<?>) object)) {
                appendChild(writer, o);
            }
        } else {
            writer.writeCharacters(String.valueOf(object));
        }
    }

    public XmlWriter setPretty(Boolean pretty) {
        this.pretty = pretty != null && pretty;
        return this;
    }

    public XmlWriter setObjectName(String objectName) {
        if (objectName == null || objectName.isBlank()) {
            this.objectName = CONFIG_OBJECT_NAME_DEFAULT;
        } else {
            this.objectName = objectName;
        }
        return this;
    }

    public XmlWriter setIncludeNull(Boolean includeNull) {
        if (includeNull != null) {
            this.includeNull = includeNull;
        }
        return this;
    }
}
