/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author ThinhLPSE61759
 */
public class XMLUtils {
    
    public static XMLStreamReader parseToCursor(String source) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        StringReader reader = new StringReader(source);
        return factory.createXMLStreamReader(reader);
    }

    public static String getNodeValue(XMLStreamReader reader, String elementName,
            String namespaceUri, String attrName) throws XMLStreamException {
        if (reader == null) {
            return null;
        }
        while (reader.hasNext()) {
            int cursor = reader.getEventType();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();
                if (tagName.equals(elementName)) {
                    String result = reader.getAttributeValue(namespaceUri, attrName);
                    return result;
                }
            } // end cursor
            reader.next();
        } //end while
        return null;
    }

    public static String getTextContent(XMLStreamReader reader, String elementName)
            throws XMLStreamException {
        if (reader == null) {
            return null;
        }
        String result = "";
        boolean isInElement = false;
        while (reader.hasNext()) {
            int cursor = reader.getEventType();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();
                if (tagName.equals(elementName)) {
                    isInElement = true;
                }
            } else if (isInElement && cursor == XMLStreamConstants.CHARACTERS || cursor == XMLStreamConstants.ENTITY_REFERENCE) {
                result += reader.getText() + " ";
            } else if (isInElement && cursor == XMLStreamConstants.END_ELEMENT) {
                return result;
            }
            reader.next();
        }// end while
        return null;
    }

    public static <T> String marsalData(T t) {
        try {
            JAXBContext context = JAXBContext.newInstance(t.getClass());
            Marshaller marshaller =  context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            StringWriter sw = new StringWriter();
            marshaller.marshal(t, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            return null;
        } 
    }
    
    public static <T> boolean validateXML(String data, String schemaFilePath) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaFilePath));
            
            Validator validator = schema.newValidator();
            InputSource input = new InputSource(new StringReader(data));
            
            validator.validate(new SAXSource(input));
            return true;
        } catch (SAXException | IOException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
  
}
