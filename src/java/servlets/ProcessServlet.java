/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import commons.Const;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import utils.InternetUtils;
import utils.XMLUtils;

/**
 *
 * @author ThinhLPSE61759
 */
public class ProcessServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String realPath = this.getServletContext().getRealPath("/");
        String htmlFilePath = realPath + Const.FILE_PATH.ONE_BEEPER_HTML;
        String url = "https://www.onebeeper.com/collections/sneakers";

        InternetUtils.parseHTML(htmlFilePath, url);

        InputStream inputStream = null;
        XMLStreamReader reader = null;

        try (PrintWriter out = response.getWriter()) {
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
                            out.println("\n===\n");
                            isInItem = true;
                            findDiscountPrice = false;
                        } // end if item prop
                    } else if (isInItem) {
                        String content;
                        if (tagName.equals("img")) {
                            String imgSrc = XMLUtils.getNodeValue(reader, tagName, "", "data-src");
                            out.println("Src: " + imgSrc);
                        } else if (tagName.equals("span")) {
                            String itemProp = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                            if (itemProp != null && itemProp.equals("name")) {
                                String itemName = XMLUtils.getTextContent(reader, tagName);
                                out.println("Name: " + itemName);
                            } else if (itemProp != null && itemProp.equals("brand")) {
                                String itemName = XMLUtils.getTextContent(reader, tagName);
                                out.println("Brand: " + itemName);
                            } 
                            // Get original price
                            String classAttr = XMLUtils.getNodeValue(reader, tagName, "", "class");
                         
                            if (classAttr != null && classAttr.equals("money")) {
                                reader.next();
                                if (reader.getEventType() == XMLStreamConstants.CHARACTERS) {
                                    out.println("Original price: " + reader.getText().trim());
                                } else {
                                    out.println("Original price: " + XMLUtils.getTextContent(reader, reader.getLocalName()).trim());
                                }
                                isInItem = false;
                            }
                        } else if (tagName.equals("link")) {
                            content = XMLUtils.getNodeValue(reader, tagName, "", "href");
                            if (content != null && content.contains("InStock")) {
                                out.println("Status: in stock");
                            } else if (content != null && content.contains("OutOfStock")) {
                                out.println("Status: out of stock");
                                isInItem = false;
                            }
                        } else if (!findDiscountPrice && tagName.equals("meta")) {
                            String prop = XMLUtils.getNodeValue(reader, tagName, "", "itemprop");
                            if (prop.equals("price")) {
                                content = XMLUtils.getNodeValue(reader, tagName, "", "content");
                                out.println("Discounted Price: " + content);
                                findDiscountPrice = true;
                            }
                        }
                    } // end if is in item
                      

                } // end if cursor
            } // end while reader has next

        } catch (XMLStreamException ex) {
            Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (XMLStreamException ex) {
                Logger.getLogger(ProcessServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
