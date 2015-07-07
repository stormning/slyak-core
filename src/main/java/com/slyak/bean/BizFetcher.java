package com.slyak.bean;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.slyak.core.RepoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.ManagedType;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */
@Component
public class BizFetcher<T extends Bizable> implements InitializingBean {

    @PersistenceContext
    private EntityManager em;

    private Map<Integer, Class<?>> bizDomainClasses = Maps.newHashMap();

    public T get(BizKey bizKey) {
        return getBizRepo(bizKey).findOne(bizKey);
    }

    public Map<BizKey, T> mget(Collection<BizKey> keys) {
        Map<Integer, Collection<BizKey>> bizs = Maps.newHashMap();
        for (BizKey key : keys) {
            int biz = key.getBiz();
            Collection<BizKey> bizKeys = bizs.get(biz);
            if (bizKeys == null) {
                bizKeys = Sets.newHashSet();
                bizs.put(biz, bizKeys);
            }
            bizKeys.add(key);
        }
        Map<BizKey, T> result = Maps.newHashMap();
        for (Map.Entry<Integer, Collection<BizKey>> bize : bizs.entrySet()) {
            Iterable<T> objs = getBizRepo(bize.getKey()).findAll(bize.getValue());
            for (T obj : objs) {
                obj.getBiz();
                result.put(new BizKey(obj.getBiz(), obj.getId()), obj);
            }
        }
        return result;
    }

    private CrudRepository<T, BizKey> getBizRepo(int biz) {
        return (CrudRepository<T, BizKey>) RepoUtils.getRepositories().getRepositoryFor(bizDomainClasses.get(biz).getClass());
    }

    private CrudRepository<T, BizKey> getBizRepo(BizKey bizKey) {
        return getBizRepo(bizKey.getBiz());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<ManagedType<?>> managedTypes = em.getMetamodel().getManagedTypes();
        for (ManagedType<?> mt : managedTypes) {
            Class<?> javaType = mt.getJavaType();
            if (BizKey.class.isAssignableFrom(javaType)) {
                Object instantiate = BeanUtils.instantiate(javaType);
                Method getBizMethod = ReflectionUtils.findMethod(javaType, "getBiz");
                Integer biz = (Integer) ReflectionUtils.invokeMethod(getBizMethod, instantiate);
                if (biz != null) {
                    bizDomainClasses.put(biz, javaType);
                }
            }
        }
    }
}
