package io.github.datagenerator.output;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import io.github.datagenerator.domain.core.MapProvider;

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
public class XmlOutput extends ObjectWriterOuput {

    public static final String ROOT_TAG_NAME = "data";
    private static final String DEFAULT_OBJECT_NAME = "my_object";

    private boolean pretty;
    private String objectName;
    private boolean includeNull;

    public XmlOutput(MapProvider provider) {
        this(provider, DEFAULT_OBJECT_NAME, false);
    }

    public XmlOutput setPretty(Boolean pretty) {
        this.pretty = pretty != null && pretty;
        return this;
    }

    public XmlOutput(MapProvider provider, String objectName, boolean pretty) {
        super(provider);
        setObjectName(objectName);
        setPretty(pretty);
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws OutputException {
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
                    throw new OutputException(e);
                }
            });

            if (addRootTag) {
                finalWriter.writeEndElement();
            }
            finalWriter.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new OutputException(e);
        }
    }

    @Override
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        setPretty(outputConfig.getOutputPretty());
        setObjectName(outputConfig.getObjectName());
        setIncludeNull(outputConfig.getIncludeNull());
        return this;
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

    public XmlOutput setObjectName(String objectName) {
        if (objectName == null || objectName.isBlank()) {
            this.objectName = DEFAULT_OBJECT_NAME;
        } else {
            this.objectName = objectName;
        }
        return this;
    }

    public XmlOutput setIncludeNull(Boolean includeNull) {
        if (includeNull != null) {
            this.includeNull = includeNull;
        }
        return this;
    }
}
