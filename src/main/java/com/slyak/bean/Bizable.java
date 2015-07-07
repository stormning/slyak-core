package com.slyak.bean;

import org.springframework.data.domain.Persistable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */
public interface Bizable extends Persistable<Long>{
    int getBiz();
}
