/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import daos.ProductDAO;
import dtos.ProductDto;
import dtos.ProductListDto;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import utils.Utils;
import utils.XMLUtils;

/**
 *
 * @author ThinhLPSE61759
 */
public class RequestListener implements ServletRequestListener{

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletRequest request=  sre.getServletRequest();
        
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = 0;
        if (pageNoStr != null && !pageNoStr.isEmpty()) {
            pageNo = Utils.toNumber(pageNoStr);
        }
        pageNo = pageNo > 0 ? pageNo : 1;
        ProductDAO productDAO = new ProductDAO();
        List<ProductDto> productList =  productDAO.getProductList(pageNo);
        ProductListDto productListDto = new ProductListDto(productList);
        String data = XMLUtils.marsalData(productListDto);
        request.setAttribute("PRODUCTS", data);
        
    }
    
}
