/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

/**
 *
 * @author ThinhLPSE61759
 */
public class Const {

    public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

    public static class FILE_PATH {
        public final static String ONE_BEEPER_HTML = "WEB-INF/one-beeper.html";
        public final static String SAIGON_SNEAKER_HTML = "WEB-INF/saigon-sneaker.html";
        public final static String SCHEMA_FILE = "WEB-INF/products.xsd";
    }

    public static String[] REDUNDANT_TAGS = new String[]{"form", "script", "style"};
    public static String[] EMPTY_TAGS = new String[]{"img", "link"};

    public static String[] ONE_BEEPER_URL = new String[]{
        "https://www.onebeeper.com/collections/sneakers",
        "https://www.onebeeper.com/collections/sneakers?page=2",
        "https://www.onebeeper.com/collections/sneakers?page=3",
    };
    
    public static String[] SAIGON_SNEAKER_URL = new String[] {
        "https://saigonsneaker.com/collections/all/cf-type-sneakers",
        "https://saigonsneaker.com/collections/all/cf-type-sneakers?page=2",
        "https://saigonsneaker.com/collections/all/cf-type-sneakers?page=3"
    };
    
    public static final int NO_OF_PRODUCT_PER_PAGE = 20;

}
