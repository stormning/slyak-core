package com.slyak.core.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/5/8
 */
public class FreeMarkerConfigurer extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {

    @Override
    protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
        config.setObjectWrapper(new ConfigurableObjectWrapper());
    }
}
