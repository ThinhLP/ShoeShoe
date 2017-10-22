/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author ThinhLPSE61759
 */
public class Utils {
    
    public static long convertToRawMoney(String strMoney) {
        String money = strMoney.replaceAll("\\D", "");
        try {
            return Long.parseLong(money);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
}
