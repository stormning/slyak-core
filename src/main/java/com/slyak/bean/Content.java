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

import javax.persistence.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/11/19
 */
@MappedSuperclass
abstract class Content extends Summary implements Bodyable {

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
