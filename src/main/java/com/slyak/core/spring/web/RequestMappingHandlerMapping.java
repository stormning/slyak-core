package com.slyak.core.spring.web;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/5/4
 */
public class RequestMappingHandlerMapping extends org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping {

    private String[] controllerSuffixes = new String[]{"Controller", "Ctl"};

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info;
        RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);

        RequestCondition<?> methodCondition = getCustomMethodCondition(method);
        if (methodAnnotation == null || ArrayUtils.isEmpty(methodAnnotation.value())) {
            info = createRequestMappingInfo(methodCondition, method.getName());
        } else {
            info = createRequestMappingInfo(methodAnnotation, methodCondition);
        }

        RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
        RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
        if (typeAnnotation == null) {
            Controller ctlAnnotation = AnnotationUtils.findAnnotation(handlerType, Controller.class);
            String pattern = ctlAnnotation.value();
            if (StringUtils.isBlank(pattern)) {
                pattern = StringUtils.replaceEach(ClassUtils.getShortName(handlerType), controllerSuffixes, new String[]{"", ""});
            }
            //first character to lowercase
            pattern = pattern.substring(0, 1).toLowerCase() + pattern.substring(1);
            info = createRequestMappingInfo(typeCondition, pattern).combine(info);
        } else {
            info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
        }
        return info;
    }


    protected RequestMappingInfo createRequestMappingInfo(RequestCondition<?> customCondition, String... patterns) {
        return new RequestMappingInfo(
                null,
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
                        useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(null),
                new ParamsRequestCondition(null),
                new HeadersRequestCondition(null),
                new ConsumesRequestCondition(null, null),
                new ProducesRequestCondition(null, null, getContentNegotiationManager()),
                customCondition);
    }

}
