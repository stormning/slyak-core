package com.slyak.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.data.domain.Page;

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

    public void decorate(Page<S> page) {
        decorate(page.getContent());
    }

    public void decorate(List<S> items) {
        Map<Assembler<S, K, T>, Set<K>> assemblerKeys = Maps.newHashMap();
        for (S item : items) {
            for (Assembler<S, K, T> assembler : assemblers) {
                K key = assembler.getKey(item);
                Set<K> ks = assemblerKeys.get(key);
                if (ks == null) {
                    ks = Sets.newHashSet();
                }
                ks.add(key);
            }
        }

        for (Map.Entry<Assembler<S, K, T>, Set<K>> asse : assemblerKeys.entrySet()) {
            Assembler<S, K, T> assembler = asse.getKey();
            Set<K> ks = asse.getValue();
            Map<K, T> targets = Maps.newHashMap();
            if (ks.size() == 1) {
                K key = ks.iterator().next();
                targets.put(key, assembler.get(key));
            } else {
                targets = assembler.mget(ks);
            }
        }

//        assembler.assemble()
//        for (Assembler<S, K,T> assembler : assemblers) {
//            Set<K> ks = assemblerKeys.get(assembler);
//            if (ks != null) {
//                if (ks.size() == 1){
//                    assembler.assemble()
//                }
//            }
//        }
    }
}
