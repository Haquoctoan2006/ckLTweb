package vn.edu.ute.cklt_web.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public final class XssSanitizerUtil {

    private XssSanitizerUtil() {
    }

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.none());
    }
}
