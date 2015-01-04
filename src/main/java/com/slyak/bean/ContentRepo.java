/*
 * Project:  slyak-core
 * Module:   slyak-core
 * File:     AbstractContentRepo.java
 * Modifier: stormning
 * Modified: 2014-11-20 09:41
 * Copyright (c) 2014 Slyak All Rights Reserved.
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.slyak.bean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/11/20
 */
@NoRepositoryBean
public interface ContentRepo<T extends Content> extends JpaRepository<T, Long> {
    @Query("select c from #{#entityName} c where status=?2 and (c.title like ?1 or c.body like ?1)")
    Page<T> findAll(String keyword, Status status, Pageable pageable);

    @Query("select c from #{#entityName} c where status=?2")
    Page<T> findAll(Status status, Pageable pageable);
}
