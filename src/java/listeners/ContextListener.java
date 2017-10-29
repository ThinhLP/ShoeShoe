/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import commons.Const;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import services.Parser;

/**
 *
 * @author ThinhLPSE61759
 */
public class ContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String realPath = sce.getServletContext().getRealPath("/");
        Parser.getDataFromWebAndSaveToDB(realPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
}
