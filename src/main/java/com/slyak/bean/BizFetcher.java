package com.slyak.bean;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.slyak.core.RepoUtils;
import com.sun.xml.internal.bind.v2.model.core.ID;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */
public class BizFetcher<T extends BizKey<ID>> {

    private Map<Integer, T> bizDomainClasses;

    public void setBizDomainClasses(Map<Integer, T> bizDomainClasses) {
        this.bizDomainClasses = bizDomainClasses;
    }

    public T get(BizKey<ID> bizKey) {
        return getBizRepo(bizKey).findOne(bizKey);
    }

    public Map<BizKey<ID>, T> mget(Collection<BizKey<ID>> keys) {
        Map<Integer, Collection<BizKey<ID>>> bizs = Maps.newHashMap();
        for (BizKey<ID> key : keys) {
            int biz = key.getBiz();
            Collection<BizKey<ID>> bizKeys = bizs.get(biz);
            if (bizKeys == null) {
                bizKeys = Sets.newHashSet();
                bizs.put(biz, bizKeys);
            }
            bizKeys.add(key);
        }
        Map<BizKey<ID>, T> result = Maps.newHashMap();
        for (Map.Entry<Integer, Collection<BizKey<ID>>> bize : bizs.entrySet()) {
            List<T> objs = getBizRepo(bize.getKey()).findAll(bize.getValue());
            for (T obj : objs) {
                result.put(obj.getBizKey(), obj);
            }
        }
        return result;
    }

    private BizRepo<T> getBizRepo(int biz) {
        return (BizRepo<T>) RepoUtils.getRepositories().getRepositoryFor(bizDomainClasses.get(biz).getClass());
    }

    private BizRepo<T> getBizRepo(BizKey<ID> bizKey) {
        return getBizRepo(bizKey.getBiz());
    }
}
