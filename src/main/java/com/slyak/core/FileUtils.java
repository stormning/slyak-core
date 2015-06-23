package com.slyak.core;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/23
 */
public class FileUtils {

    private static Map<String, Long> fileLastModifiedCache = Maps.newConcurrentMap();

    public static boolean isModified(File file) {
        String cacheKey = file.toString();
        Long timestamp = fileLastModifiedCache.get(cacheKey);
        long now = file.lastModified();
        boolean changed = timestamp == null || timestamp != now;
        if (changed) {
            fileLastModifiedCache.put(cacheKey, now);
        }
        return changed;
    }
}
