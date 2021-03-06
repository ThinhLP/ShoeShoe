/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ThinhLPSE61759
 */
public class ProcessServlet extends HttpServlet {

    public final String loginPage = "login.jsp";
    public final String loginServlet = "LoginServlet";
    public final String productServlet = "ProductServlet";
    public final String productsPage = "products.jsp";
    public final String viewCartPage = "cart.jsp";
    public final String logoutServlet = "LogoutServlet";
    public final String updateDataServlet = "UpdateDataServlet";
   
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
         
        String button = request.getParameter("btAction");
        String url = productsPage;
        if (button == null || button.isEmpty()) {
            
        } else if (button.equals("Login")) {
            url = loginServlet;
        } else if (button.equals("ViewCart")){
            url = viewCartPage;
        } else if (button.equals("logout")) {
            url = logoutServlet;
        } else if (button.equals("loginPage")) {
            url = loginPage;
            request.setAttribute("PREVIOUS_URL", request.getHeader("referer"));
        } else if (button.equals("updateData")) {
            url = updateDataServlet;
        }
        
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
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
