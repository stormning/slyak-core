package com.slyak.core.freemarker;

import com.google.common.collect.Maps;
import com.slyak.core.StringUtils;
import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/5/8
 */
public class FreeMarkerConfigurer extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {

    private static final String FTLROOT_REGEX = "\\[#--\\s{1,}@ftlroot\\s{1,}\"(.*)\"\\s{1,}--\\]";
    private static final String IMPORT_REGEX = "\\[#import\\s{1,}['\"](.*)['\"]\\s{1,}as\\s{1,}(.*)\\]";
    private static final String VARIABLE_REGEX = "\\[#--\\s{1,}@ftlvariable\\s{1,}name=\"(.*)\"\\s{1,}type=\"(.*)\"\\s{1,}--]";

    private String implicitFile = "classpath:freemarker_implicit.ftl";

    private String ftlroot;
    private Map<String, String> imports = Maps.newHashMap();
    private Map<String, String> variables = Maps.newHashMap();

    private BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_21).build();

    private WebApplicationContext wac;

    @Override
    protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
        super.postProcessTemplateLoaders(templateLoaders);
        try {
            Resource resource = getResourceLoader().getResource(implicitFile);
            if (resource.exists()) {
                List<String> lines = IOUtils.readLines(resource.getInputStream());
                for (String line : lines) {
                    //find root
                    List<String> rootVars = StringUtils.findGroupsIfMatch(FTLROOT_REGEX, line);
                    if (CollectionUtils.isNotEmpty(rootVars)) {
                        ftlroot = rootVars.get(0);
                        continue;
                    }

                    List<String> imps = StringUtils.findGroupsIfMatch(IMPORT_REGEX, line);
                    if (CollectionUtils.isNotEmpty(imps)) {
                        imports.put(imps.get(1), imps.get(0));
                        continue;
                    }

                    List<String> vals = StringUtils.findGroupsIfMatch(VARIABLE_REGEX, line);
                    if (CollectionUtils.isNotEmpty(vals)) {
                        variables.put(vals.get(0), vals.get(1));
                    }
                }

                if (ftlroot != null) {
                    templateLoaders.add(new SpringTemplateLoader(getResourceLoader(), ResourceLoader.CLASSPATH_URL_PREFIX + ftlroot));
                }
            }
        } catch (Exception e) {
            //ignore?
        }
    }

    @Override
    protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
        config.setObjectWrapper(new ConfigurableObjectWrapper());
        if (!imports.isEmpty()) {
            for (Map.Entry<String, String> iptEntry : imports.entrySet()) {
                config.addAutoImport(iptEntry.getKey(), iptEntry.getValue());
            }
        }
        if (!variables.isEmpty()) {
            for (Map.Entry<String, String> valEntry : variables.entrySet()) {
                String vname = valEntry.getKey();
                String val = valEntry.getValue();
                if (vname.equalsIgnoreCase("null")) {
                    config.setSharedVariable(vname, TemplateModel.NOTHING);
                    continue;
                }
                if (config.getSharedVariableNames().contains(vname) || val.contains("freemarker.ext.servlet.")) {
                    continue;
                }
                try {
                    Class<?> aClass = ClassUtils.forName(val, ClassUtils.getDefaultClassLoader());
                    if (TemplateModel.class.isAssignableFrom(aClass) || AnnotationUtils.findAnnotation(aClass, Ftm.class) != null) {
                        Object instantiate = BeanUtils.instantiate(aClass);
                        //autowire
//                        wac.getAutowireCapableBeanFactory().autowireBean(instantiate);
                        config.setSharedVariable(vname, instantiate);
                    } else {
                        Controller ctlAnn = AnnotationUtils.findAnnotation(aClass, Controller.class);
                        if (ctlAnn == null) {
                            //must be static methods
                            config.setSharedVariable(vname, beansWrapper.getStaticModels().get(val));
                        } else {
                            config.setSharedVariable(vname, new ControllerModel(aClass));
                        }
                    }

                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }

    public void setImplicitFile(String implicitFile) {
        this.implicitFile = implicitFile;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        super.setServletContext(servletContext);
        wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }
}
