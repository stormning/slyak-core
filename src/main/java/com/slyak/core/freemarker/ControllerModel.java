package com.slyak.core.freemarker;

import com.slyak.core.spring.web.MvcUriComponentsBuilder;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/5/7
 */
public class ControllerModel implements TemplateMethodModelEx, TemplateHashModel {

    private Class controller;
    private String methodName;

    public ControllerModel(Class controller) {
        this.controller = controller;
    }

    public Object exec(List list) throws TemplateModelException {
        Object[] args = new Object[0];
        if (!CollectionUtils.isEmpty(list)) {
            args = list.toArray(new Object[list.size()]);
        }
        return MvcUriComponentsBuilder.fromMethod(BeanUtils.findMethodWithMinimalParameters(controller, methodName), args).toUriString();
    }

    @Override
    public TemplateModel get(String methodName) throws TemplateModelException {
        this.methodName = methodName;
        return this;
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        return false;
    }
}
