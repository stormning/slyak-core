package com.slyak.core;

import java.util.Calendar;
import java.util.Date;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/4/16
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static Calendar toCal(long ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts);
        return cal;
    }

    public static int getFiled(long ts, int field) {
        return toCal(ts).get(field);
    }

    public static int diff(long ts1, long ts2, int field) {
        return getFiled(ts1, field) - getFiled(ts2, field);
    }

    public static boolean isSameField(long ts1, long ts2, int field) {
        return diff(ts1, ts2, field) == 0;
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.toCal(new Date().getTime()).get(Calendar.YEAR));
    }
}
