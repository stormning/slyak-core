/**
 * Project name : slyak-core
 * File name : StringUtils.java
 * Package name : com.slyak.core.util
 * Date : 2013-11-27
 * Copyright : 2013 , SLYAK.COM All Rights Reserved
 * Author : stormning@163.com
 */
package com.slyak.core.util;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String CHINESE_PATTERN = "[\u4e00-\u9fa5]";

    private static final String IMG_PATTERN = "img.*\\W?src\\s*=[\"']{1}?([^\"']+?)[\"']{1}?";

    public static String cut(String sourceString, int byteSize) {
        if (isEmpty(sourceString)) {
            return null;
        }
        sourceString = cleanHtml(sourceString);
        if (isEmpty(sourceString)) {
            return null;
        }

        int count = 0;

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sourceString.length(); i++) {
            String temp = String.valueOf(sourceString.charAt(i));
            if (temp.matches(CHINESE_PATTERN)) {
                count += 2;
            } else {
                count += 1;
            }
            if (byteSize >= count) {
                sb.append(temp);
            }
        }
        return sb.toString();
    }

    public static String cleanHtml(String html) {
        return html.replaceAll("<style[^>]*?>[\\s\\S]*?</style>|<script[^>]*?>[\\s\\S]*?</script>|<.+?>", "");
    }

    public static Set<String> findImgSrcs(String htmlString) {
        Set<String> imgSrcs = new LinkedHashSet<String>();
        Pattern imagePattern = Pattern.compile(IMG_PATTERN);
        Matcher imgMatcher = imagePattern.matcher(htmlString);
        while (imgMatcher.find()) {
            String src = trimToNull(imgMatcher.group(1));
            if (src != null) {
                imgSrcs.add(imgMatcher.group(1));
            }
        }
        return imgSrcs;
    }

    public static String devidePath(String longPath, String seperator) {
        int len = longPath.length();
        int start = 0;
        StringBuffer sb = new StringBuffer();
        int step = 2;
        while (start < len) {
            if (len - start < step) {
                step = len - start;
            }
            sb.append(seperator + longPath.substring(start, start + step));
            start += 2;
        }
        return sb.toString();
    }

}
