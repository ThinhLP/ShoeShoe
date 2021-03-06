/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import commons.Const;
import dtos.BrandDto;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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

    public static String removeTag(String line, String tagName) {
        return line.replaceAll("(<" + tagName + ")[^>]*(>)", "")
                .replaceAll("</" + tagName + ">", "");
    }

    public static String nomarlizeEmptyTag(String line, String tagName) {
        int emptyTagPos = line.indexOf("<" + tagName);
        StringBuilder builder = new StringBuilder();
        boolean inOpenTag = false;
        boolean isTag = false;
        boolean isInEmptyTag = false;

        for (int i = 0; i < line.length() - 1; i++) {
            char ch = line.charAt(i);
            builder.append(ch);
            if (ch == '<') {
                inOpenTag = true;
                isTag = true;
            } else if (ch == '>') {
                inOpenTag = false;
            }
            if (ch == ' ') {
                isTag = false;
            }

            if (i == emptyTagPos) {
                isInEmptyTag = true;
            }

            if (inOpenTag && !isTag) {
                if (isInEmptyTag) {
                    char postCh = line.charAt(i + 1);
                    if (ch != '/' && postCh == '>') {
                        builder.append("/");
                        isInEmptyTag = false;
                    }
                }
            }
        }
        if (!line.isEmpty()) {
            builder.append(line.charAt(line.length() - 1));
        }
        return builder.toString();
    }

    public static String normalizeLine(String line) {
        StringBuilder builder = new StringBuilder();

        //line = line.replaceAll("[&](\\w)+[;]", "");
        line = line.replaceAll("\\s{2}", " ")
                .replaceAll("[\\s]+=", "=")
                .replaceAll(" & ", " &amp; ")
                .replaceAll(":=", "=");
        if (line.contains("<a")) {
            line = line.replaceAll(" href=\"[^\"]*\"", "");
        }
        if (line.contains("<img")) {
            line = nomarlizeEmptyTag(line, "img");
        }
        if (line.contains("<link")) {
            line = nomarlizeEmptyTag(line, "link");
        }

        //line = removeTag(line, "a");
        //Normalize Tag Have Attribute Without Value
        boolean inOpenTag = false;
        boolean isTag = false;
        boolean isInQuoteMark = false;

        for (int i = 0; i < line.length() - 1; i++) {
            char ch = line.charAt(i);
            builder.append(ch);

            if (ch == '<') {
                inOpenTag = true;
                isTag = true;
            } else if (ch == '>') {
                inOpenTag = false;
            }
            if (ch == ' ') {
                isTag = false;
            }
            if (ch == '\"' || ch == '\'') {
                isInQuoteMark = !isInQuoteMark;
            }

            if (inOpenTag && !isTag && !isInQuoteMark) {
                if (Character.isAlphabetic(ch)) {
                    char postCh = line.charAt(i + 1);
                    if (postCh == ' ') {
                        builder.append("=\"\"");
                    }
                }
            }
        }
        if (!line.isEmpty()) {
            builder.append(line.charAt(line.length() - 1));
        }
        return builder.toString();
    }

    public static int countStringInALine(String line, String strCount) {
        int count = 0;
        while (line.contains(strCount)) {
            count++;
            line = line.replaceFirst(strCount, "");
        }
        return count;
    }

    public static String getContentBeforeTagInLine(String line, String tagName) {
        return line.substring(0, line.indexOf("<" + tagName)).trim();
    }

    public static String getContentAfterTagInLine(String line, String tagName) {
        return line.substring(line.lastIndexOf("</" + tagName) + (tagName.length() + 3)).trim();
    }

    public static boolean containRedundantTag(String line) {
        String[] redundantTags = Const.REDUNDANT_TAGS;
        for (int i = 0; i < redundantTags.length; i++) {
            if (line.contains("<" + redundantTags[i])) {
                return true;
            }
        }
        return false;
    }

    public static int countOpenAndCloseTag(String line, String tag) {
        return Utils.countStringInALine(line, "<" + tag) - Utils.countStringInALine(line, "</" + tag + ">");
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return sdf.format(date);
    }

    public static BrandDto existBrand(String brand, List<BrandDto> brands) {
        if (brands == null || brands.isEmpty()) {
            return null;
        }
        for (BrandDto dto : brands) {
            if (dto.getBrandName().equalsIgnoreCase(brand)) {
                return dto;
            }
        }
        return null;
    }

    public static <T> int getMaxId(List<T> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T lastElm = list.get(list.size() - 1);
        Method[] lastElmMethods = lastElm.getClass().getMethods();
        try {
            for (Method method : lastElmMethods) {
                String methodName = method.getName();
                if (methodName.matches("get\\w*Id")) {
                    return (Integer) method.invoke(lastElm);
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        try {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            return xmlGregorianCalendar;
        } catch (DatatypeConfigurationException ex) {
            return null;
        }
    }

    public static Date convertStringGregorianCalendarToDate(String strXmlGregorianCalendar) {
        try {
            XMLGregorianCalendar calendar  = DatatypeFactory.newInstance().newXMLGregorianCalendar(strXmlGregorianCalendar);
            return calendar.toGregorianCalendar().getTime();
        } catch (DatatypeConfigurationException ex) {
            return null;
        }
    }
    
    public static java.sql.Date toSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
    
    public static java.util.Date toUtilDate(java.sql.Date date) {
        return new java.util.Date(date.getTime());
    }
    
    public static int toNumber(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
