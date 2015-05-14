package com.slyak.core.freemarker;

import com.slyak.core.spring.web.AppContext;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private RequestMappingHandlerMapping rmhm;

    public ControllerModel(String name) {
        this.name = name;
        this.rmhm = AppContext.getApplicationContext().getBean(RequestMappingHandlerMapping.class);
    }

    public TemplateModel get(String s) throws TemplateModelException {
        return new ControllerModel(this.name + "." + s);
    }

    public boolean isEmpty() throws TemplateModelException {
        return false;
    }

    public Object exec(List list) throws TemplateModelException {
        try {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = rmhm.getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                HandlerMethod hm = entry.getValue();
                RequestMappingInfo rmi = entry.getKey();
                Set<String> patterns = rmi.getPatternsCondition().getPatterns();
//                rmi.getMatchingCondition()
                return patterns.iterator().next();
            }
            return null;
        } catch (Exception e) {
            throw new TemplateModelException("Not support yet");
        }
    }
}
