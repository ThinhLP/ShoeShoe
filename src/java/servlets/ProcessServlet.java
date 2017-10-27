/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import commons.Const;
import daos.BrandDAO;
import daos.ProductDAO;
import dtos.BrandDto;
import dtos.BrandListDto;
import dtos.ProductDto;
import dtos.ProductListDto;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import services.Parser;
import utils.InternetUtils;
import utils.Utils;
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
        String saigonSneakerHtmlPath = realPath + Const.FILE_PATH.SAIGON_SNEAKER_HTML;
        String onebeeperHtmlPath = realPath + Const.FILE_PATH.ONE_BEEPER_HTML;
        //String url = "https://ngstore.com.vn/danh-muc/do-the-thao/giay-the-thao/giay-sneaker";
        String url2 = "https://www.onebeeper.com/collections/sneakers";
        String url1 = "https://saigonsneaker.com/collections/all/cf-type-sneakers";
        InternetUtils.parseHTML(saigonSneakerHtmlPath, url1);
        InternetUtils.parseHTML(onebeeperHtmlPath, url2);
        
        List<BrandDto> brands = new ArrayList<>();
        List<ProductDto> products = new ArrayList<>();

        BrandListDto currentBrands = new BrandListDto(brands);
        ProductListDto currentsProduct = new ProductListDto(products);

        Parser.getListProductFromOneBeeperPage(saigonSneakerHtmlPath, currentsProduct, currentBrands);
        Parser.getListProductFromOneBeeperPage(onebeeperHtmlPath, currentsProduct, currentBrands);

        BrandDAO brandDAO = new BrandDAO();
        ProductDAO productDAO = new ProductDAO();
        
        try (PrintWriter out = response.getWriter()) {
            String data = XMLUtils.marsalData(currentsProduct);
            boolean isValid = XMLUtils.validateXML(data, realPath + Const.FILE_PATH.SCHEMA_FILE, currentsProduct);
            if (isValid) {
                // Insert brand
                for (BrandDto brand: currentBrands.getBrandList()) {
                    brandDAO.insert(brand);
                }
                // Insert product
                for (ProductDto product: currentsProduct.getProductList()) {
                    productDAO.insert(product);
                }
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
