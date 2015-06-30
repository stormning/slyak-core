package com.slyak.bean;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.slyak.core.RepoUtils;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */
public class BizFetcher<T extends BizKey> {

    private Map<Integer, T> bizDomainClasses;

    public void setBizDomainClasses(Map<Integer, T> bizDomainClasses) {
        this.bizDomainClasses = bizDomainClasses;
    }

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
                result.put(obj.getBizKey(), obj);
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
}
