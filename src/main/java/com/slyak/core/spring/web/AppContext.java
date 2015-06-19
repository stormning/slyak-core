package com.slyak.core.spring.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/5/12
 */
@Component
public class AppContext implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        AppContext.context = context;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    public static RequestCondition<RequestMappingInfo> getRequestCondition() {
        try {
            RequestMappingHandlerMapping rmh = getApplicationContext().getBean(RequestMappingHandlerMapping.class);
            HandlerExecutionChain hec = rmh.getHandler(AppContext.getRequest());
            HandlerMethod handlerMethod = (HandlerMethod) hec.getHandler();
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = rmh.getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> rmie : handlerMethods.entrySet()) {
                if (rmie.getValue() == handlerMethod) {
                    return rmie.getKey();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static boolean urlMath(String url, RequestCondition<RequestMappingInfo> rc) {
        try {
            return rc.compareTo(createRmi(url), AppContext.getRequest()) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static RequestMappingInfo createRmi(String url) {
        PatternsRequestCondition prc = new PatternsRequestCondition(url);
        return new RequestMappingInfo(prc, null, null, null, null, null, null);
    }

    public static boolean urlMathCurrentRequest(String url) {
        return urlMath(url, getRequestCondition());
    }
}
