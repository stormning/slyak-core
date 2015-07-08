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
public class Assemblers {

    private List<Assembler> assemblers;

    public Assemblers(List<Assembler> assemblers) {
        this.assemblers = assemblers;
    }

    private Assemblers() {

    }

    public void assemble(List sources) {
        if (!CollectionUtils.isEmpty(sources) && !CollectionUtils.isEmpty(assemblers)) {
            if (sources.size() == 1) {
                assemble(sources.get(0));
            } else {
                for (Assembler assembler : assemblers) {
                    List<Object> keys = Lists.newArrayList();
                    for (Object s : sources) {
                        keys.add(assembler.getKey(s));
                    }
                    Map<Object, Object> kvs = assembler.mget(keys);
                    for (Object s : sources) {
                        Object key = assembler.getKey(s);
                        assembler.assemble(s, kvs.get(key));
                    }
                }
            }
        }
    }

    public void assemble(Object source) {
        if (source != null && !CollectionUtils.isEmpty(assemblers)) {
            for (Assembler assembler : assemblers) {
                Object id = assembler.getKey(source);
                Object target = assembler.get(id);
                assembler.assemble(source, target);
            }
        }
    }
}
