/*
 * Project:  slyak-core
 * Module:   slyak-core
 * File:     User.java
 * Modifier: stormning
 * Modified: 2014-11-20 10:55
 * Copyright (c) 2014 Slyak All Rights Reserved.
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.slyak.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/11/20
 */
@MappedSuperclass
public abstract class User implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 64)
    private String name;

    @Column(length = 64)
    private String password;

    @Column
    private Gender gender;

    private long avatar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public long getAvatar() {
        return avatar;
    }

    public void setAvatar(long avatar) {
        this.avatar = avatar;
    }
}
