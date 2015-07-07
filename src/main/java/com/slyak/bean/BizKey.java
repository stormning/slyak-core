package com.slyak.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class BizKey implements Serializable, Bizable {
    @Id
    private int biz;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public BizKey(){

    }

    public BizKey(int biz, Long id) {
        this.biz = biz;
        this.id = id;
    }

    public BizKey getBizKey() {
        return this;
    }

    public Long getId() {
        return id;
    }

    protected void setId(final Long id) {
        this.id = id;
    }

    public boolean isNew() {
        return null == getId();
    }

    @Override
    public int getBiz() {
        return biz;
    }

    public void setBiz(int biz) {
        this.biz = biz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BizKey)) return false;

        BizKey bizKey = (BizKey) o;

        if (biz != bizKey.biz) return false;
        if (id != null ? !id.equals(bizKey.id) : bizKey.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = biz;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
