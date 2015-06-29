package com.slyak.bean;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */

import java.util.Collection;
import java.util.Map;

/**
 * @param <S> source
 * @param <K> key
 * @param <T> target
 */
public interface Assembler<S, K, T> {

    K getKey(S source);

    T get(K key);

    Map<K, T> mget(Collection<K> keys);

    void assemble(S source, T target);
}
