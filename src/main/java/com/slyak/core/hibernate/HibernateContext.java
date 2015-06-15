package com.slyak.core.hibernate;

import org.hibernate.cfg.Configuration;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/15
 */
public class HibernateContext {
    static Configuration configuration;

    public static Configuration getConfiguration(){
        return configuration;
    }
}
