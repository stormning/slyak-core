package com.slyak.core.freemarker;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/5/7
 */
public class ControllerModel implements TemplateMethodModelEx, TemplateHashModel {

    public static final Map<String, String> ROUTER_CACHE = new ConcurrentHashMap();
    private final String name;

    public ControllerModel(String name) {
        this.name = name;
    }

    public TemplateModel get(String s) throws TemplateModelException {
        return new ControllerModel(this.name + "." + s);
    }

    public boolean isEmpty() throws TemplateModelException {
        return false;
    }

    public Object exec(List list) throws TemplateModelException {
        throw new TemplateModelException("Not support yet");
    }
}
