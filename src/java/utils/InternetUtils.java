/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import commons.Const;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ThinhLPSE61759
 */
public class InternetUtils {

    public static void parseHTML(String filePath, String uri) {
        Writer writer = null;
        InputStream is = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            con.addRequestProperty("User-agent", Const.USER_AGENT);

            String line;

            is = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));

            boolean isBodyTagStart = false;
            boolean isInScriptTag = false;
            int countOpenDiv = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.contains("<body")) {
                    isBodyTagStart = true;
                }
                if (line.contains("<script") || line.contains("<style") || line.contains("<form")) {
                    isInScriptTag = true;
                }
                if (line.contains("</script>") || line.contains("</style>") || line.contains("</form>")) {
                    isInScriptTag = false;
                }
                if (isInScriptTag) {
                    continue;
                }

                countOpenDiv += countStringInALine(line, "<div") - countStringInALine(line, "</div>");

                if (countOpenDiv < 0) {
                    countOpenDiv = 0;
                    continue;
                }

                if (isBodyTagStart && !line.isEmpty() && !line.contains("</script>")
                        && !line.contains("</style>")
                        && !line.contains("<iframe")
                        && !line.contains("</form>") && !line.contains("<br>")) {
                    line = normalizeLine(line);
                    writer.write(line + "\n");
                    if (line.contains("</body>")) {
                        break;
                    }
                }
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(InternetUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InternetUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (is != null) {
                    is.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(InternetUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static String nomarlizeEmptyTag(String line, String tagName) {
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

    private static String normalizeLine(String line) {
        StringBuilder builder = new StringBuilder();

        //line = line.replaceAll("[&](\\w)+[;]", "");
        line = line.replaceAll("\\W&\\W", " &amp; ");
                
        if (line.contains("<img")) {
            line = nomarlizeEmptyTag(line, "img");
        }
        if (line.contains("<link")) {
            line = nomarlizeEmptyTag(line, "link");
        }

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

    private static int countStringInALine(String line, String strCount) {
        int count = 0;
        while (line.contains(strCount)) {
            count++;
            line = line.replaceFirst(strCount, "");
        }
        return count;
    }

}
