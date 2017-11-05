/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import commons.Const;
import daos.BrandDAO;
import daos.ProductDAO;
import dtos.BrandDto;
import dtos.BrandListDto;
import dtos.ProductDto;
import dtos.ProductListDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import servlets.ProcessServlet;
import utils.InternetUtils;
import utils.Utils;
import utils.XMLUtils;

/**
 *
 * @author ThinhLPSE61759
 */
public class Parser {

    public static void getListProductFromOneBeeperPage(String htmlSource, ProductListDto currentProductList, BrandListDto currentBrandList) {
        InputStream inputStream = null;
        XMLStreamReader reader = null;

        try {
            reader = XMLUtils.parseToCursor(htmlSource);
            boolean isInItem = false;
            boolean findDiscountPrice = false;
            boolean isFindItem = false;
            boolean findItemImage = false;
            boolean findOriginalPrice = false;
            boolean hasSale = false;

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
                            findOriginalPrice = false;
                            findItemImage = false;
                            hasSale = false;
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
                            String classAttr = XMLUtils.getNodeValue(reader, tagName, "", "class").trim();

                            if (classAttr != null) {
                                if (classAttr.equals("price sale")) {
                                    hasSale = true;
                                } else if (classAttr.equals("price")) {
                                    hasSale = false;
                                }

                                if (!hasSale && classAttr.equals("money")) {
                                    originalPrice = XMLUtils.getTextContent(reader, reader.getLocalName()).trim();
                                    isInItem = false;
                                    isFindItem = true;
                                } else if (hasSale && classAttr.equals("was_price")) {
                                    findOriginalPrice = true;                            
                                } else if (findOriginalPrice && classAttr.equals("money")) {
                                    originalPrice = XMLUtils.getTextContent(reader, reader.getLocalName()).trim();
                                    isInItem = false;
                                    isFindItem = true;
                                }
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
                    proName = proName.replaceAll("'", "\"");
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

    public static void getDataFromWebAndSaveToDB(String realPath) {
        BrandDAO brandDAO = new BrandDAO();
        ProductDAO productDAO = new ProductDAO();

        List<BrandDto> brands = new ArrayList<>();
        List<ProductDto> products = new ArrayList<>();

        BrandListDto currentBrands = new BrandListDto(brands);
        ProductListDto currentsProduct = new ProductListDto(products);

        String[] urlOneBeeper = Const.ONE_BEEPER_URL;
        String[] urlSGSneaker = Const.SAIGON_SNEAKER_URL;
        String htmlSource;

        for (String url : urlOneBeeper) {
            htmlSource = InternetUtils.parseHTML(url);
            Parser.getListProductFromOneBeeperPage(htmlSource, currentsProduct, currentBrands);
            String data = XMLUtils.marsalData(currentsProduct);
            boolean isValid = XMLUtils.validateXML(data, realPath + Const.FILE_PATH.SCHEMA_FILE);
            if (isValid) {
                // Insert brand
                for (BrandDto brand : currentBrands.getBrandList()) {
                    brandDAO.insert(brand);
                }
                // Insert product
                for (ProductDto product : currentsProduct.getProductList()) {
                    productDAO.insert(product);
                }
            }
        }

        for (String url : urlSGSneaker) {
            htmlSource = InternetUtils.parseHTML(url);
            Parser.getListProductFromOneBeeperPage(htmlSource, currentsProduct, currentBrands);
            String data = XMLUtils.marsalData(currentsProduct);
            boolean isValid = XMLUtils.validateXML(data, realPath + Const.FILE_PATH.SCHEMA_FILE);
            if (isValid) {
                // Insert brand
                for (BrandDto brand : currentBrands.getBrandList()) {
                    brandDAO.insert(brand);
                }
                // Insert product
                for (ProductDto product : currentsProduct.getProductList()) {
                    productDAO.insert(product);
                }
            }
        }
    }
}
