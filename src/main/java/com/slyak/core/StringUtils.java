/**
 * Project name : slyak-core
 * File name : StringUtils.java
 * Package name : com.slyak.core.util
 * Date : 2013-11-27
 * Copyright : 2013 , SLYAK.COM All Rights Reserved
 * Author : stormning@163.com
 */
package com.slyak.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String CHINESE_REGEX = "[\u4e00-\u9fa5]";

    private static final String IMG_REGEX = "img.*\\W?src\\s*=\\s*[\"']{1}?([^\"']+?)[\"']{1}?";

    public static String cut(String sourceString, int byteSize) {
        if (isEmpty(sourceString)) {
            return null;
        }
        sourceString = cleanHtml(sourceString);
        if (isEmpty(sourceString)) {
            return null;
        }

        int count = 0;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sourceString.length(); i++) {
            String temp = String.valueOf(sourceString.charAt(i));
            if (temp.matches(CHINESE_REGEX)) {
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

    public static List<String> findImgSrcs(String input) {
        return findGroupsIfMatch(IMG_REGEX, input);
    }

    public static List<String> findGroupsIfMatch(String regex, String input) {
        List<String> groups = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            int c = matcher.groupCount();
            for (int i = 1; i <= c; i++) {
                String val = trimToNull(matcher.group(i));
                if (val != null) {
                    groups.add(val);
                }
            }
        }
        return groups;
    }

    public static String devidePath(String longPath, String seperator) {
        int len = longPath.length();
        int start = 0;
        StringBuilder sb = new StringBuilder();
        int step = 2;
        while (start < len) {
            if (len - start < step) {
                step = len - start;
            }
            sb.append(seperator).append(longPath.substring(start, start + step));
            start += 2;
        }
        return sb.toString();
    }

}
