package com.slyak.bean;

import javax.persistence.Column;
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
public class BizKey<ID extends Serializable> implements Serializable, Bizable {
    @Id
    private int biz;

    @Id
    @Column(name = "biz_id")
    private ID bizId;

    public BizKey(int biz, ID bizId) {
        this.biz = biz;
        this.bizId = bizId;
    }

    public int getBiz() {
        return biz;
    }

    public void setBiz(int biz) {
        this.biz = biz;
    }

    public ID getBizId() {
        return bizId;
    }

    public void setBizId(ID bizId) {
        this.bizId = bizId;
    }

    public BizKey<ID> getBizKey(){
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BizKey)) return false;

        BizKey bizKey = (BizKey) o;

        if (biz != bizKey.biz) return false;
        if (bizId != null ? !bizId.equals(bizKey.bizId) : bizKey.bizId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = biz;
        result = 31 * result + (bizId != null ? bizId.hashCode() : 0);
        return result;
    }
}
