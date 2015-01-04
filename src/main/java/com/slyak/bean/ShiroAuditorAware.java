/*
 * Project:  slyak-core
 * Module:   slyak-core
 * File:     ShiroAuditorAware.java
 * Modifier: stormning
 * Modified: 2014-11-20 11:40
 * Copyright (c) 2014 Slyak All Rights Reserved.
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.slyak.bean;

import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.AuditorAware;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/11/20
 */
public class ShiroAuditorAware implements AuditorAware<Long> {
    @Override
    public Long getCurrentAuditor() {
        return (Long) SecurityUtils.getSubject().getPrincipal();
    }
}
