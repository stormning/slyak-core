package com.slyak.core.hibernate;

import com.google.common.collect.Maps;
import com.slyak.core.spring.web.AppContext;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.internal.ConnectionProviderInitiator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/15
 */
@Component
public class HibernateContext implements ApplicationListener<ContextRefreshedEvent> {
    private static Configuration configuration;

    static void setConfiguration(Configuration configuration) {
        HibernateContext.configuration = configuration;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext appContext = AppContext.getApplicationContext();
        DataSource ds = appContext.getBean(DataSource.class);
        Properties properties = configuration.getProperties();
        properties.put(AvailableSettings.CONNECTION_PROVIDER, "org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl");
        HashMap<Object, Object> injectionData = Maps.newHashMap();
        injectionData.put("dataSource",ds);
        properties.put(ConnectionProviderInitiator.INJECTION_DATA, injectionData);
    }
}
