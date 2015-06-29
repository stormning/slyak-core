package com.slyak.bean;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/25
 */
@MappedSuperclass
public class BizKey<PK extends Serializable> extends AbstractPersistable<PK> implements Serializable, Bizable {
    @Id
    private int biz;

    @Override
    public int getBiz() {
        return biz;
    }

    public void setBiz(int biz) {
        this.biz = biz;
    }

    public BizKey(int biz, PK id) {
        super();
        setId(id);
    }

    public BizKey getBizKey(){
        return this;
    }
}
