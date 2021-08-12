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
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Génère les données au format XML
 */
public class XmlOutput extends ObjectWriterOuput {

    private static final String DEFAULT_OBJECT_NAME = "object";

    private boolean pretty;
    private String objectName;

    public XmlOutput(ObjectProvider provider) {
        this(provider, DEFAULT_OBJECT_NAME, false);
    }

    public XmlOutput setPretty(boolean pretty) {
        this.pretty = pretty;
        return this;
    }

    public XmlOutput(ObjectProvider provider, String objectName, boolean pretty) {
        super(provider);
        setObjectName(objectName);
        setPretty(pretty);
    }

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

    private void writeMany(OutputStream out, Stream<Map<String, ?>> stream) {
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

    @SuppressWarnings("unchecked")
    private void appendChild(Document doc, Element root, Map<String, ?> object) {
        for (Map.Entry<String, ?> entry : object.entrySet()) {
            Object value = entry.getValue();
            Element element = doc.createElement(entry.getKey());
            if (value instanceof Map) {
                appendChild(doc, element, (Map<String, ?>) value);
            } else {
                element.appendChild(doc.createTextNode(String.valueOf(value)));
            }
            root.appendChild(element);
        }
    }

    @Override
    public void writeMany(OutputStream out, int count) throws IOException {
        writeMany(out, provider.getStream(count));
    }

    @Override
    public void writeOne(OutputStream out) throws IOException {
        writeOne(out, provider.getOne());
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
