/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import servlets.ProcessServlet;
import utils.XMLUtils;

/**
 *
 * @author ThinhLPSE61759
 */
public class ParseService {

    public static void parseOneBeeperPage(String htmlFilePath) {
        InputStream inputStream = null;
        XMLStreamReader reader = null;

        try {
            File file = new File(htmlFilePath);

            inputStream = new FileInputStream(file);
            reader = XMLUtils.parseFileToCursor(inputStream);
            boolean isInItem = false;
            boolean findDiscountPrice = false;
            while (reader.hasNext()) {
                int cursor = reader.next();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals("div")) {
                        String itemProp = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                        if (itemProp != null && itemProp.equals("itemListElement")) {
                            System.out.println("\n===\n");
                            isInItem = true;
                            findDiscountPrice = false;
                        } // end if item prop
                    } else if (isInItem) {
                        String content;
                        if (tagName.equals("img")) {
                            String imgSrc = XMLUtils.getNodeValue(reader, tagName, "", "data-src");
                            System.out.println("Src: " + imgSrc);
                        } else if (tagName.equals("span")) {
                            String itemProp = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                            if (itemProp != null && itemProp.equals("name")) {
                                String itemName = XMLUtils.getTextContent(reader, tagName);
                                System.out.println("Name: " + itemName);
                            } else if (itemProp != null && itemProp.equals("brand")) {
                                String itemName = XMLUtils.getTextContent(reader, tagName);
                                System.out.println("Brand: " + itemName);
                            }
                            // Get original price
                            String classAttr = XMLUtils.getNodeValue(reader, tagName, "", "class");

                            if (classAttr != null && classAttr.equals("money")) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamConstants.CHARACTERS) {
                                    System.out.println("Original price: " + reader.getText().trim());
                                } else {
                                    System.out.println("Original price: " + XMLUtils.getTextContent(reader, reader.getLocalName()).trim());
                                }
                                isInItem = false;
                            }
                        } else if (tagName.equals("link")) {
                            content = XMLUtils.getNodeValue(reader, tagName, "", "href");
                            if (content != null && content.contains("InStock")) {
                                System.out.println("Status: in stock");
                            } else if (content != null && content.contains("OutOfStock")) {
                                System.out.println("Status: out of stock");
                                isInItem = false;
                            }
                        } else if (!findDiscountPrice && tagName.equals("meta")) {
                            String prop = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                            if (prop.equals("price")) {
                                content = XMLUtils.getNodeValue(reader, tagName, "", "content");
                                System.out.println("Discounted Price: " + content);
                                findDiscountPrice = true;
                            }
                        }
                    } // end if is in item

                } // end if cursor
            } // end while reader has next

        } catch (XMLStreamException ex) {
            Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (XMLStreamException ex) {
                Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ParseService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void parseSaigonSneakerPage(String htmlFilePath) {
        InputStream inputStream = null;
        XMLStreamReader reader = null;

        try {
            File file = new File(htmlFilePath);
            inputStream = new FileInputStream(file);
            reader = XMLUtils.parseFileToCursor(inputStream);

            boolean isInItem = false;
            boolean findDiscountPrice = false;
            boolean findItemImage = false;
            while (reader.hasNext()) {
                int cursor = reader.next();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals("div")) {
                        String itemProp = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                        if (itemProp != null && itemProp.equals("itemListElement")) {
                            System.out.println("\n===\n");
                            isInItem = true;
                            findDiscountPrice = false;
                            findItemImage = false;
                        } // end if item prop
                    } else if (isInItem) {
                        String content;
                        if (!findItemImage && tagName.equals("img")) {
                            String imgSrc = XMLUtils.getNodeValue(reader, tagName, "", "data-src");
                            System.out.println("Src: " + imgSrc);
                            findItemImage = true;
                        } else if (tagName.equals("span")) {
                            String itemProp = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                            if (itemProp != null && itemProp.equals("name")) {
                                String itemName = XMLUtils.getTextContent(reader, tagName);
                                System.out.println("Name: " + itemName);
                            } else if (itemProp != null && itemProp.equals("brand")) {
                                String itemName = XMLUtils.getTextContent(reader, tagName);
                                System.out.println("Brand: " + itemName);
                            }
                            String className = XMLUtils.getNodeValue(reader, tagName, "", "class");
                            if (className != null && className.equals("money")) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamConstants.CHARACTERS) {
                                    System.out.println("Original price: " + reader.getText().trim());
                                } else {
                                    System.out.println("Original price: " + XMLUtils.getTextContent(reader, reader.getLocalName()).trim());
                                }
                                isInItem = false;
                            }
                        } else if (tagName.equals("link")) {
                            content = XMLUtils.getNodeValue(reader, tagName, "", "href");
                            if (content != null && content.contains("InStock")) {
                                System.out.println("Status: in stock");
                            } else if (content != null && content.contains("OutOfStock")) {
                                System.out.println("Status: out of stock");
                                isInItem = false;
                            }
                        } else if (!findDiscountPrice && tagName.equals("meta")) {
                            String prop = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                            if (prop.equals("price")) {
                                content = XMLUtils.getNodeValue(reader, tagName, "", "content");
                                System.out.println("Discounted Price: " + content);
                                findDiscountPrice = true;
                            }
                        }
                    } // end if is in item

                } // end if cursor
            } // end while reader has next
        } catch (XMLStreamException ex) {
            Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (XMLStreamException ex) {
                Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
