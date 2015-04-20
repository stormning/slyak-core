package com.slyak.core.spring.mvc.router.support;

import com.slyak.core.spring.mvc.router.Router;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

/**
 * Bears the request mapping information to be handled by the RequestMappingHandlerAdapter
 * to invoke the corresponding Controller.action.
 * <p>Encapsulates a HandlerMethod object + additional info:
 * <ul>
 * <li>the matching Controller</li>
 * <li>the Matching Method</li>
 * <li>the Route that matches that request</li>
 * <li>the HTTPRequestAdapter, containing the actual request</li>
 * 
 * @see org.springframework.web.method.HandlerMethod
 * @author Brian Clozel
 */
public class RouterHandler extends HandlerMethod {
 
    private Router.Route route;
    
    public RouterHandler(Object bean, Method method, Router.Route route) {
        // calling the actual HandlerMethod constructor
        super(bean, method);
        this.route = route;
    }

    public Router.Route getRoute() {
        return route;
    }
    
}
