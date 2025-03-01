package com.kevin.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class XSSFilter {
	// 用來避免XSS
		public static String sanitize(String input) {
	        return Jsoup.clean(input, Safelist.relaxed());
	    }
}
