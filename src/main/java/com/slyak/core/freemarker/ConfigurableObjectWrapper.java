package com.slyak.core.freemarker;

import com.google.common.collect.Maps;
import freemarker.ext.beans.StringModel;
import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.*;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/5/7
 */
public class ConfigurableObjectWrapper extends DefaultObjectWrapper {

    private Map<Class, Class<TemplateModel>> classMapping = Collections.emptyMap();
    private Map<Class, Constructor> cache = Maps.newHashMap();

    public void setClassMapping(Map<Class, Class<TemplateModel>> classMapping) {
        this.classMapping = classMapping;
    }

    public ConfigurableObjectWrapper() {
        super(Configuration.VERSION_2_3_21);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected TemplateModel handleUnknownType(Object obj) throws TemplateModelException {
        Class clazz = obj.getClass();
        if (clazz.isEnum()) {
            return new StringModel(obj, this);
        }
        Constructor constructor = null;
        if (cache.containsKey(clazz)) {
            constructor = cache.get(clazz);
        } else {
            for (Map.Entry<Class, Class<TemplateModel>> entry : classMapping.entrySet()) {
                if (entry.getKey().isAssignableFrom(clazz)) {
                    constructor = entry.getValue().getConstructors()[0];
                    break;
                }
            }
            cache.put(clazz, constructor);
        }
        if (constructor != null) {
            try {
                return (TemplateModel) constructor.newInstance(obj, this);
            } catch (Exception ignored) {
            }
        }
        return super.handleUnknownType(obj);
    }

    @Override
    public Object unwrap(TemplateModel model) throws TemplateModelException {
        if (model == null) {
            return null;
        } else if (model instanceof WrapperTemplateModel) {
            return ((WrapperTemplateModel) model).getWrappedObject();
        } else if (model instanceof AdapterTemplateModel) {
            return ((AdapterTemplateModel) model).getAdaptedObject(Object.class);
        } else if (model instanceof TemplateScalarModel) {
            return ((TemplateScalarModel) model).getAsString();
        } else if (model instanceof TemplateNumberModel) {
            return ((TemplateNumberModel) model).getAsNumber();
        } else if (model instanceof TemplateDateModel) {
            return ((TemplateDateModel) model).getAsDate();
        }
        return super.unwrap(model);
    }
}