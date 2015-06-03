package com.slyak.core.freemarker;

import com.slyak.core.spring.web.MvcUriComponentsBuilder;
import freemarker.template.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
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
        Method method = BeanUtils.findMethodWithMinimalParameters(controller, methodName);
        Class<?>[] types = method.getParameterTypes();

        Object[] args = new Object[types.length];
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < types.length; i++) {
                Class typeClass = types[i];
                if (Model.class.isAssignableFrom(typeClass) || Pageable.class.isAssignableFrom(typeClass)) {
                    args[i] = null;
                } else {
                    TemplateModel argModel = (TemplateModel) list.get(i);
                    if (argModel instanceof TemplateScalarModel) {
                        args[i] = ((TemplateScalarModel) argModel).getAsString();
                    } else if (argModel instanceof TemplateNumberModel) {
                        args[i] = ((TemplateNumberModel) argModel).getAsNumber();
                    } else if (argModel instanceof TemplateBooleanModel) {
                        args[i] = ((TemplateBooleanModel) argModel).getAsBoolean();
                    } else {
                        args[i] = null;
                    }
                }
            }
        }
        return MvcUriComponentsBuilder.fromMethod(method, args).toUriString();
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
