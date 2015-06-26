package com.slyak.bean;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.slyak.core.RepoUtils;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */
public class BizFetcher<T extends BizKey<K>, K extends Serializable> {

    private Map<Integer, T> bizDomainClasses;

    public void setBizDomainClasses(Map<Integer, T> bizDomainClasses) {
        this.bizDomainClasses = bizDomainClasses;
    }

    public T get(BizKey<K> bizKey) {
        return (T) getBizRepo(bizKey).findOne(bizKey);
    }

    public Map<BizKey<K>, T> mget(Collection<BizKey<K>> keys) {
        Map<Integer, Collection<BizKey<K>>> bizs = Maps.newHashMap();
        for (BizKey<K> key : keys) {
            int biz = key.getBiz();
            Collection<BizKey<K>> bizKeys = bizs.get(biz);
            if (bizKeys == null) {
                bizKeys = Sets.newHashSet();
                bizs.put(biz, bizKeys);
            }
            bizKeys.add(key);
        }
        Map<BizKey<K>, T> result = Maps.newHashMap();
        for (Map.Entry<Integer, Collection<BizKey<K>>> bize : bizs.entrySet()) {
            Iterable<T> objs = getBizRepo(bize.getKey()).findAll(bize.getValue());
            for (T obj : objs) {
                result.put(obj.getBizKey(), obj);
            }
        }
        return result;
    }

    private CrudRepository<T, BizKey<K>> getBizRepo(int biz) {
        return (CrudRepository<T, BizKey<K>>) RepoUtils.getRepositories().getRepositoryFor(bizDomainClasses.get(biz).getClass());
    }

    private CrudRepository<T, BizKey<K>> getBizRepo(BizKey<K> bizKey) {
        return getBizRepo(bizKey.getBiz());
    }
}
