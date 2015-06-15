package com.slyak.core.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.service.ServiceRegistry;
import org.springframework.orm.jpa.persistenceunit.SmartPersistenceUnitInfo;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/15
 */
public class SpringHibernateJpaPersistenceProvider extends HibernatePersistenceProvider {
    @Override
    @SuppressWarnings("rawtypes")
    public EntityManagerFactory createContainerEntityManagerFactory(final PersistenceUnitInfo info, Map properties) {
        return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(info), properties) {
            @Override
            public Configuration buildHibernateConfiguration(ServiceRegistry serviceRegistry) {
                Configuration configuration = super.buildHibernateConfiguration(serviceRegistry);
                if (info instanceof SmartPersistenceUnitInfo) {
                    for (String managedPackage : ((SmartPersistenceUnitInfo) info).getManagedPackages()) {
                        configuration.addPackage(managedPackage);
                    }
                }
                HibernateContext.configuration = configuration;
                return configuration;
            }
        }.build();
    }
}
