package fr.jordanmartin.datagenerator.output;

import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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

    private boolean pretty;

    private String objectName;

    public XmlOutput(ObjectProvider provider) {
        this(provider, "object", false);
    }

    public XmlOutput setPretty(boolean pretty) {
        this.pretty = pretty;
        return this;
    }

    @SneakyThrows
    public XmlOutput(ObjectProvider provider, String objectName, boolean pretty) {
        super(provider);
        this.objectName = objectName;
        this.pretty = pretty;
    }

    @SneakyThrows
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        Document doc = newDocument();
        Transformer transformer = newTransformer();
        Element root = doc.createElement(objectName);
        doc.appendChild(root);
        appendChild(doc, root, object);
        transformer.transform(new DOMSource(doc), new StreamResult(out));
    }

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
            throw new RuntimeException(e);
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

    @SneakyThrows
    private Document newDocument() {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return df.newDocumentBuilder().newDocument();
    }

    @SneakyThrows
    private Transformer newTransformer() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, pretty ? "yes" : "no");
        return transformer;
    }

    public XmlOutput setObjectName(String objectName) {
        this.objectName = objectName;
        return this;
    }
}
