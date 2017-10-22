/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import commons.Const;

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
        line =  line.replaceAll("\\s{2}", " ")
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

    
}
