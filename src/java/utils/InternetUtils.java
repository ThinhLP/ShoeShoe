/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import commons.Const;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

      public static String parseHTML(String uri) {
        InputStream is = null;
        BufferedReader reader = null;
        String[] redundantTags = Const.REDUNDANT_TAGS;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            con.addRequestProperty("User-agent", Const.USER_AGENT);

            String line;

            is = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));


            boolean isBodyTagStart = false;
            boolean isInScriptTab = false;
            int countOpenDiv = 0;

            builder.append("<body>").append("\n");
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.contains("<body")) {
                    isBodyTagStart = true;
                }

                if (!isBodyTagStart) {
                    continue;
                }

                String beforeTag = "";

                // Check open unused tags
                for (int i = 0; i < redundantTags.length; i++) {
                    if (line.contains("<" + redundantTags[i])) {
                        beforeTag = Utils.getContentBeforeTagInLine(line, redundantTags[i]);
                        isInScriptTab = true;
                        if (!beforeTag.isEmpty()) {
                            builder.append(beforeTag).append("\n");
                        }
                        break;
                    }
                }

                // Check close unused tags
                boolean isEndScriptTab = false;
                String afterTag = "";
                for (int i = 0; i < redundantTags.length; i++) {
                    if (line.contains("</" + redundantTags[i] + ">")) {
                        afterTag = Utils.getContentAfterTagInLine(line, redundantTags[i]);
                        isInScriptTab = false;
                        isEndScriptTab = true;
                        if (!afterTag.isEmpty() && !Utils.containRedundantTag(afterTag)) {
                            builder.append(afterTag).append("\n");
                        }
                        break;
                    }
                }

                if (isInScriptTab) {
                    continue;
                }

                countOpenDiv += Utils.countStringInALine(line, "<div") - Utils.countStringInALine(line, "</div>");

                if (countOpenDiv < 0) {
                    countOpenDiv = 0;
                    continue;
                }

                if (!line.isEmpty()
                        && !isEndScriptTab
                        && !line.contains("<body")
                        && !line.contains("<iframe")
                        && !line.contains("<br>")) {
                    line = Utils.normalizeLine(line);
                    if (line.contains("</body>")) {
                        break;
                    }
                    builder.append(line).append("\n");
                }
            }
                builder.append("</body>");
            return builder.toString();
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
         
            } catch (IOException ex) {
                Logger.getLogger(InternetUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }
}
