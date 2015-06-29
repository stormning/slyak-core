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
 * 将decorator添加到Entity的listener实现自动装配一些额外属性.
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */
public abstract class AbstractDecorator<S> {

    private List<Assembler> assemblers;

    public AbstractDecorator() {
        assemblers = Lists.newArrayList();
        initAssemblers(assemblers);
    }

    protected abstract void initAssemblers(List<Assembler> assemblers);

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
        if (!CollectionUtils.isEmpty(items) && !CollectionUtils.isEmpty(assemblers)) {
            Map<Object, S> keyItems = Maps.newHashMap();
            for (Assembler assembler : assemblers) {
                Set<Object> keys = Sets.newHashSet();
                for (S item : items) {
                    Object key = assembler.getKey(item);
                    keys.add(key);
                    keyItems.put(key, item);
                }
                if (keys.size() == 1) {
                    Object key = keys.iterator().next();
                    Object t = assembler.get(key);
                    assembler.assemble(keyItems.get(key), t);
                } else {
                    Map<Object, Object> targets = assembler.mget(keys);
                    for (Map.Entry<Object, Object> kte : targets.entrySet()) {
                        assembler.assemble(keyItems.get(kte.getKey()), kte.getValue());
                    }
                }
            }
        }
    }
}
