package com.slyak.bean;

import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/26
 */
@NoRepositoryBean
public interface BizRepo<T extends BizKey<ID>> extends JpaRepository<T, BizKey<ID>> {

}
