package com.slyak.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */
public abstract class AbstractDecorator<S, K, T> {

    private List<Assembler<S, K, T>> assemblers;

    public AbstractDecorator() {
        assemblers = Lists.newArrayList();
        initAssemblers(assemblers);
    }

    abstract void initAssemblers(List<Assembler<S, K, T>> assemblers);

    public void decorate(S source) {
        if (source != null) {
            decorate(Collections.singletonList(source));
        }
    }

    public void decorate(Page<S> page) {
        if (page != null) {
            decorate(page.getContent());
        }
    }

    public void decorate(List<S> items) {
        if (!CollectionUtils.isEmpty(items)) {
            Map<K, S> keyItems = Maps.newHashMap();
            for (Assembler<S, K, T> assembler : assemblers) {
                Set<K> keys = Sets.newHashSet();
                for (S item : items) {
                    K key = assembler.getKey(item);
                    keys.add(key);
                    keyItems.put(key, item);
                }
                if (keys.size() == 1) {
                    K key = keys.iterator().next();
                    T t = assembler.get(key);
                    assembler.assemble(keyItems.get(key), t);
                } else {
                    Map<K, T> targets = assembler.mget(keys);
                    for (Map.Entry<K, T> kte : targets.entrySet()) {
                        assembler.assemble(keyItems.get(kte.getKey()), kte.getValue());
                    }
                }
            }
        }
    }
}
