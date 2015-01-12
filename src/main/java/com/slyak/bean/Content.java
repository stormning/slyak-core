/*
 * Project:  slyak-core
 * Module:   slyak-core
 * File:     Article.java
 * Modifier: stormning
 * Modified: 2014-11-19 16:56
 * Copyright (c) 2014 Slyak All Rights Reserved.
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.slyak.bean;

import com.slyak.core.hibernate.JSONType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/11/19
 */
@MappedSuperclass
public class Content extends AuditableBase implements Serializable {

    @Column(nullable = false)
    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column
    private String body;

    @Column
    @Type(type = JSONType.TYPE)
    private List<Long> fileIds;

    @Column
    private Status status = Status.ENABLED;

    @Column(nullable = false)
    private int weight = 0;

    public List<Long> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<Long> fileIds) {
        this.fileIds = fileIds;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
