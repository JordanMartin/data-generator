package io.github.jordanmartin.datagenerator.output;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format XML
 */
public class XmlOutput extends ObjectWriterOuput {

    private static final String DEFAULT_OBJECT_NAME = "my_object";

    private boolean pretty;
    private String objectName;

    public XmlOutput(ObjectProvider provider) {
        this(provider, DEFAULT_OBJECT_NAME, false);
    }

    public XmlOutput setPretty(Boolean pretty) {
        this.pretty = pretty != null && pretty;
        return this;
    }

    public XmlOutput(ObjectProvider provider, String objectName, boolean pretty) {
        super(provider);
        setObjectName(objectName);
        setPretty(pretty);
    }

    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws OutputException {
        Document doc = newDocument();
        Transformer transformer = newTransformer();
        Element root = doc.createElement(objectName);
        doc.appendChild(root);
        appendChild(doc, root, object);
        try {
            transformer.transform(new DOMSource(doc), new StreamResult(out));
        } catch (TransformerException e) {
            throw new OutputException(e);
        }
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) {
        Document doc = newDocument();
        Transformer transformer = newTransformer();
        Element root = doc.createElement("data");
        doc.appendChild(root);
        stream.forEach(object -> {
            Element element = doc.createElement(objectName);
            appendChild(doc, element, object);
            root.appendChild(element);
        });
        try {
            transformer.transform(new DOMSource(doc), new StreamResult(out));
        } catch (TransformerException e) {
            throw new OutputException(e);
        }
    }

    @Override
    public ObjectWriterOuput setConfig(IOutputConfig outputConfig) {
        setPretty(outputConfig.getOutputPretty());
        setObjectName(outputConfig.getObjectName());
        return this;
    }

    @SuppressWarnings("unchecked")
    private void appendChild(Document doc, Element root, Object object) {
        if (object instanceof Map) {
            for (Map.Entry<String, ?> entry : ((Map<String, ?>) object).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.startsWith("@")) {
                    root.setAttribute(key.substring(1), String.valueOf(value));
                } else {
                    Element element = doc.createElement(key);
                    appendChild(doc, element, value);
                    root.appendChild(element);
                }
            }
        } else if (object instanceof Iterable) {
            for (Object o : ((List<?>) object)) {
                appendChild(doc, root, o);
            }
        } else {
            root.appendChild(doc.createTextNode(String.valueOf(object)));
        }
    }

    private Document newDocument() {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            return df.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new OutputException(e);
        }
    }

    private Transformer newTransformer() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, pretty ? "yes" : "no");
            return transformer;
        } catch (TransformerConfigurationException e) {
            throw new OutputException(e);
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
}
