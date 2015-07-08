package com.slyak.bean;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/7/8
 */
public class Assemblers<S, K, V> {

    private List<Assembler<S, K, V>> assemblers;

    public Assemblers(List<Assembler<S, K, V>> assemblers) {
        this.assemblers = assemblers;
    }

    public void assemble(List<S> sources) {
        if (!CollectionUtils.isEmpty(sources) && assemblers.size() > 0) {
            if (sources.size() == 1) {
                assemble(sources.get(0));
            } else {
                for (Assembler<S, K, V> assembler : assemblers) {
                    List<K> keys = Lists.newArrayList();
                    for (S s : sources) {
                        keys.add(assembler.getKey(s));
                    }
                    Map<K, V> kvs = assembler.mget(keys);
                    for (S s : sources) {
                        K key = assembler.getKey(s);
                        assembler.assemble(s, kvs.get(key));
                    }
                }
            }
        }
    }

    public void assemble(S source) {
        if (source != null) {
            for (Assembler<S, K, V> assembler : assemblers) {
                K id = assembler.getKey(source);
                V target = assembler.get(id);
                assembler.assemble(source, target);
            }
        }
    }
}
