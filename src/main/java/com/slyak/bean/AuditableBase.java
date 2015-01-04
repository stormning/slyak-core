/*
 * Project:  slyak-core
 * Module:   slyak-core
 * File:     AuditableImpl.java
 * Modifier: stormning
 * Modified: 2014-11-20 11:16
 * Copyright (c) 2014 Slyak All Rights Reserved.
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.slyak.bean;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;

import javax.persistence.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/11/20
 */
@MappedSuperclass
public class AuditableBase implements Auditable<Long, Long> {
    @Id
    @GeneratedValue
    private Long id;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_date")
    private DateTime createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private DateTime lastModifiedDate;

    @Override
    public Long getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public DateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public DateTime getLastModifiedDate() {
        return getLastModifiedDate();
    }

    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    @Transient
    public boolean isNew() {
        return id == null;
    }


}
