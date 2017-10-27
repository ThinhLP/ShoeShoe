/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dtos.BrandDto;
import dtos.BrandListDto;
import dtos.ProductDto;
import dtos.ProductListDto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import servlets.ProcessServlet;
import utils.Utils;
import utils.XMLUtils;

/**
 *
 * @author ThinhLPSE61759
 */
public class Parser {

    public static void getListProductFromOneBeeperPage(String htmlFilePath, ProductListDto currentProductList, BrandListDto currentBrandList) {
        InputStream inputStream = null;
        XMLStreamReader reader = null;

        try {
            File file = new File(htmlFilePath);

            inputStream = new FileInputStream(file);
            reader = XMLUtils.parseFileToCursor(inputStream);
            boolean isInItem = false;
            boolean findDiscountPrice = false;
            boolean isFindItem = false;
            boolean findItemImage = false;
            
            int curProIndex = Utils.getMaxId(currentProductList.getProductList());
            int curBrandIndex = Utils.getMaxId(currentBrandList.getBrandList());
            List<ProductDto> curProList = currentProductList.getProductList();
            List<BrandDto> curBrandList = currentBrandList.getBrandList();
            
            String proName = "", imgUrl = "", brandName = "", originalPrice = "", discountedPrice = "";
            boolean inStock = false;
            ProductDto product;

            while (reader.hasNext()) {
                int cursor = reader.next();

                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals("div")) {
                        String itemProp = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                        if (itemProp != null && itemProp.equals("itemListElement")) {
                            isInItem = true;
                            findDiscountPrice = false;
                            findItemImage = false;
                            proName = "";
                            imgUrl = "";
                            brandName = "";
                            originalPrice = "";
                            discountedPrice = "";
                        } // end if item prop
                    } else if (isInItem) {
                        String content;
                        if (!findItemImage && tagName.equals("img")) {
                            imgUrl = XMLUtils.getNodeValue(reader, tagName, "", "data-src");
                            findItemImage = true;
                        } else if (tagName.equals("span")) {
                            String itemProp = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                            if (itemProp != null && itemProp.equals("name")) {
                                proName = XMLUtils.getTextContent(reader, tagName).trim();
                            } else if (itemProp != null && itemProp.equals("brand")) {
                                brandName = XMLUtils.getTextContent(reader, tagName).trim();
                            }
                            // Get original price
                            String classAttr = XMLUtils.getNodeValue(reader, tagName, "", "class");

                            if (classAttr != null && classAttr.equals("money")) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamConstants.CHARACTERS) {
                                    originalPrice = reader.getText().trim();
                                } else {
                                    originalPrice = XMLUtils.getTextContent(reader, reader.getLocalName()).trim();
                                }
                                isInItem = false;
                                isFindItem = true;
                            }
                        } else if (tagName.equals("link")) {
                            content = XMLUtils.getNodeValue(reader, tagName, "", "href");
                            if (content != null && content.contains("InStock")) {
                                inStock = true;
                            } else if (content != null && content.contains("OutOfStock")) {
                                inStock = false;
                                isInItem = false;
                                isFindItem = true;
                            }
                        } else if (!findDiscountPrice && tagName.equals("meta")) {
                            String prop = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                            if (prop.equals("price")) {
                                discountedPrice = XMLUtils.getNodeValue(reader, tagName, "", "content");
                                findDiscountPrice = true;
                            }
                        }
                    } // end if is in item
                } // end if cursor
                if (isFindItem) {
                    isFindItem = false;
                    long original = Utils.convertToRawMoney(originalPrice);
                    long discounted = Utils.convertToRawMoney(discountedPrice);
                    product = new ProductDto(++curProIndex, proName, discounted, original, new Date(), imgUrl, inStock);
                    BrandDto brand = Utils.existBrand(brandName, curBrandList);
                    if (brand == null) {
                        brand = new BrandDto(++curBrandIndex, brandName);
                        curBrandList.add(brand);
                    }
                    product.setBrand(brand);
                    curProList.add(product);
                }
            } // end while reader has next
        } catch (XMLStreamException ex) {
            Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
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
                                String itemName = XMLUtils.getTextContent(reader, tagName).trim();
                                System.out.println("Name: " + itemName);
                            } else if (itemProp != null && itemProp.equals("brand")) {
                                String itemName = XMLUtils.getTextContent(reader, tagName).trim();
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
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
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
