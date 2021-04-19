package fr.jordanmartin.datagenerator.output;

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
public class XmlWriter implements ObjectWriter {

    private final Document doc;
    private final Transformer transformer;
    private final String objectName;

    public XmlWriter() {
        this("object", false);
    }

    @SneakyThrows
    public XmlWriter(String objectName, boolean pretty) {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        this.objectName = objectName;
        this.doc = df.newDocumentBuilder().newDocument();
        transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, pretty ? "yes" : "no");
    }

    @SneakyThrows
    @Override
    public void writeOne(OutputStream out, Map<String, ?> object) throws IOException {
        Element root = doc.createElement(objectName);
        doc.appendChild(root);
        appendChild(root, object);
        transformer.transform(new DOMSource(doc), new StreamResult(out));
    }

    @Override
    public void writeMany(OutputStream out, Stream<Map<String, ?>> stream) {
        Element root = doc.createElement("data");
        doc.appendChild(root);
        stream.forEach(object -> {
            Element element = doc.createElement(objectName);
            appendChild(element, object);
            root.appendChild(element);
        });
        try {
            transformer.transform(new DOMSource(doc), new StreamResult(out));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void appendChild(Element root, Map<String, ?> object) {
        for (Map.Entry<String, ?> entry : object.entrySet()) {
            Object value = entry.getValue();
            Element element = doc.createElement(entry.getKey());
            if (value instanceof Map) {
                appendChild(element, (Map<String, ?>) value);
            } else {
                element.appendChild(doc.createTextNode(String.valueOf(value)));
            }
            root.appendChild(element);
        }
    }
}
