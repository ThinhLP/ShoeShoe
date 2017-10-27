/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public static XMLStreamReader parseFileToCursor(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        return factory.createXMLStreamReader(inputStream);
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

//    public static void saveToXML(String xmlFilePath, ProductListDto listProduct) {
//        try {
//            JAXBContext context = JAXBContext.newInstance(listProduct.getClass());
//            Marshaller marshaller = context.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//            marshaller.marshal(listProduct, new File(xmlFilePath));
//        } catch (JAXBException ex) {
//            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    public static <T> String marsalData(T t) {
        try {
            JAXBContext context = JAXBContext.newInstance(t.getClass());
            Marshaller marshaller =  context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(t, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            return null;
        } 
    }
    
    public static <T> boolean validateXML(String data, String schemaFilePath, T t) {
        try {
            JAXBContext context = JAXBContext.newInstance(t.getClass());
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaFilePath));
            
            Validator validator = schema.newValidator();
            InputSource inputFile = new InputSource(new StringReader(data));
            
            validator.validate(new SAXSource(inputFile));
            return true;
        } catch (JAXBException | SAXException | IOException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
  
}
