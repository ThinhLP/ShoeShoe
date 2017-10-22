/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
            } else if (isInElement && cursor == XMLStreamConstants.CHARACTERS || cursor ==  XMLStreamConstants.ENTITY_REFERENCE) {
                result += reader.getText() + " ";
            } else if (isInElement && cursor == XMLStreamConstants.END_ELEMENT) {
                return result;
            } 
            reader.next();
        }// end while
        return null;
    }
}
